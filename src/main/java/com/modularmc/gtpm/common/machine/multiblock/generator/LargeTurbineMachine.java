package com.modularmc.gtpm.common.machine.multiblock.generator;

import com.modularmc.gtpm.api.GTValues;
import com.modularmc.gtpm.api.blockentity.BlockEntityCreationInfo;
import com.modularmc.gtpm.api.capability.recipe.RecipeCapability;
import com.modularmc.gtpm.api.machine.MetaMachine;
import com.modularmc.gtpm.api.machine.feature.multiblock.IMultiPart;
import com.modularmc.gtpm.api.machine.multiblock.WorkableElectricMultiblockMachine;
import com.modularmc.gtpm.api.recipe.GTRecipe;
import com.modularmc.gtpm.api.recipe.content.ContentModifier;
import com.modularmc.gtpm.api.recipe.modifier.ModifierFunction;
import com.modularmc.gtpm.api.recipe.modifier.ParallelLogic;
import com.modularmc.gtpm.api.recipe.modifier.RecipeModifier;
import com.modularmc.gtpm.common.machine.multiblock.part.RotorHolderPartMachine;
import com.modularmc.gtpm.utils.FormattingUtil;

import com.modularmc.registrate.localization.MinecraftLocale;
import com.modularmc.registrate.localization.annotation.TransVariants;
import com.modularmc.registrate.localization.annotation.Translation;
import com.modularmc.registrate.localization.annotation.Variant;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@TransVariants(keyPrefix = "gtpm.multiblock.turbine",
               value = {
                       @Variant(id = "efficiency",
                                source = "Turbine Efficiency: %s%%",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "涡轮效率：%s%%")),
                       @Variant(id = "energy_per_tick",
                                source = "Energy Output: %s/%s EU/t",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "能量输出：%s/%s EU/t")),
                       @Variant(id = "rotor_durability",
                                source = "Rotor Durability: %s%%",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "转子耐久度：%s%%")),
                       @Variant(id = "rotor_speed",
                                source = "Rotor Speed: %s/%s RPM",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "转子转速：%s/%sRPM")),
                       @Variant(id = "fuel_amount",
                                source = "Fuel Amount: %sL (%s)",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "燃料量：%sL（%s）")),
                       @Variant(id = "obstructed",
                                source = "Turbine Face Obstructed",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "涡轮受阻")),
               })
public class LargeTurbineMachine extends WorkableElectricMultiblockMachine {

    public static final int MIN_DURABILITY_TO_WARN = 10;

    private final long BASE_EU_OUTPUT;
    @Getter
    private final int tier;

    public LargeTurbineMachine(BlockEntityCreationInfo info, int tier) {
        super(info);
        this.tier = tier;
        this.BASE_EU_OUTPUT = GTValues.V[tier] * 2;
    }

    @Nullable
    private RotorHolderPartMachine getRotorHolder() {
        for (IMultiPart part : getParts()) {
            if (part instanceof RotorHolderPartMachine rotorHolder) {
                return rotorHolder;
            }
        }
        return null;
    }

    @Override
    public long getOverclockVoltage() {
        var rotorHolder = getRotorHolder();
        if (rotorHolder != null && rotorHolder.hasRotor())
            return BASE_EU_OUTPUT * rotorHolder.getTotalPower() / 100;
        return 0;
    }

    /**
     * @return EUt multiplier that should be applied to the turbine's output
     */
    protected double productionBoost() {
        var rotorHolder = getRotorHolder();
        if (rotorHolder != null && rotorHolder.hasRotor()) {
            int maxSpeed = rotorHolder.getMaxRotorHolderSpeed();
            int currentSpeed = rotorHolder.getRotorSpeed();
            if (currentSpeed >= maxSpeed) return 1;
            return Math.pow(1.0 * currentSpeed / maxSpeed, 2);
        }
        return 0;
    }

    public boolean hasRotor() {
        var rotorHolder = getRotorHolder();
        return rotorHolder != null && rotorHolder.hasRotor();
    }

    public int getRotorSpeed() {
        var rotorHolder = getRotorHolder();
        if (rotorHolder != null && rotorHolder.hasRotor()) {
            return rotorHolder.getRotorSpeed();
        }
        return 0;
    }

    public int getMaxRotorHolderSpeed() {
        var rotorHolder = getRotorHolder();
        if (rotorHolder != null && rotorHolder.hasRotor()) {
            return rotorHolder.getMaxRotorHolderSpeed();
        }
        return 0;
    }

    public int getTotalEfficiency() {
        var rotorHolder = getRotorHolder();
        if (rotorHolder != null && rotorHolder.hasRotor()) {
            return rotorHolder.getTotalEfficiency();
        }
        return -1;
    }

    public long getCurrentProduction() {
        return isActive() && recipeLogic.getLastRecipe() != null ?
                recipeLogic.getLastRecipe().getOutputEUt() : 0;
    }

    public int getRotorDurabilityPercent() {
        var rotorHolder = getRotorHolder();
        if (rotorHolder != null && rotorHolder.hasRotor()) {
            return rotorHolder.getRotorDurabilityPercent();
        }
        return -1;
    }

    //////////////////////////////////////
    // ****** Recipe Logic *******//
    //////////////////////////////////////

    /**
     * Recipe Modifier for <b>Large Turbine Multiblocks</b> - can be used as a valid {@link RecipeModifier}
     * <p>
     * Recipe is fast parallelized up to {@code (baseEUt * power) / recipeEUt} times.
     * Duration is then multiplied by the holder efficiency.
     * </p>
     *
     * @param machine a {@link LargeTurbineMachine}
     * @param recipe  recipe
     * @return A {@link ModifierFunction} for the given Turbine Multiblock and recipe
     */
    public static ModifierFunction recipeModifier(@NotNull MetaMachine machine, @NotNull GTRecipe recipe) {
        if (!(machine instanceof LargeTurbineMachine turbineMachine)) {
            return RecipeModifier.nullWrongType(LargeTurbineMachine.class, machine);
        }

        var rotorHolder = turbineMachine.getRotorHolder();
        if (rotorHolder == null) return ModifierFunction.NULL;

        long EUt = recipe.getOutputEUt();
        long turbineMaxVoltage = turbineMachine.getOverclockVoltage();
        double holderEfficiency = rotorHolder.getTotalEfficiency() / 100.0;

        if (EUt <= 0 || turbineMaxVoltage <= EUt || holderEfficiency <= 0) return ModifierFunction.NULL;

        // get the amount of parallel required to match the desired output voltage
        // Max Parallel is Ceilinged not Floored to ensure the output voltage is actually met,
        // at the cost of slightly increased fuel
        int maxParallel = (int) (turbineMaxVoltage / EUt);
        if (turbineMaxVoltage % EUt != 0) maxParallel++;

        int actualParallel = ParallelLogic.getParallelAmountFast(turbineMachine, recipe, maxParallel);
        double eutMultiplier = (maxParallel == actualParallel) ?
                turbineMachine.productionBoost() * turbineMaxVoltage / EUt :
                turbineMachine.productionBoost() * actualParallel;

        return ModifierFunction.builder()
                .inputModifier(ContentModifier.multiplier(actualParallel))
                .outputModifier(ContentModifier.multiplier(actualParallel))
                .eutMultiplier(eutMultiplier)
                .parallels(actualParallel)
                .durationMultiplier(holderEfficiency)
                .build();
    }

    @Override
    public boolean regressWhenWaiting() {
        return false;
    }

    @Override
    public boolean canVoidRecipeOutputs(RecipeCapability<?> capability) {
        // void both eu and fluid tick outputs
        return true;
    }

    //////////////////////////////////////
    // ******* GUI ********//
    //////////////////////////////////////

    @Override
    public void addDisplayText(List<Component> textList) {
        super.addDisplayText(textList);
        if (isFormed()) {
            var rotorHolder = getRotorHolder();

            if (rotorHolder != null && rotorHolder.getRotorEfficiency() > 0) {
                textList.add(Component.translatable("gtpm.multiblock.turbine.rotor_speed",
                        FormattingUtil.formatNumbers(rotorHolder.getRotorSpeed()),
                        FormattingUtil.formatNumbers(rotorHolder.getMaxRotorHolderSpeed())));
                textList.add(Component.translatable("gtpm.multiblock.turbine.efficiency",
                        rotorHolder.getTotalEfficiency()));

                long maxProduction = getOverclockVoltage();
                long currentProduction = getCurrentProduction();

                if (isActive()) {
                    textList.add(3, Component.translatable("gtpm.multiblock.turbine.energy_per_tick",
                            FormattingUtil.formatNumbers(currentProduction),
                            FormattingUtil.formatNumbers(maxProduction)));
                }

                int rotorDurability = rotorHolder.getRotorDurabilityPercent();
                if (rotorDurability > MIN_DURABILITY_TO_WARN) {
                    textList.add(Component.translatable("gtpm.multiblock.turbine.rotor_durability", rotorDurability));
                } else {
                    textList.add(Component.translatable("gtpm.multiblock.turbine.rotor_durability", rotorDurability)
                            .setStyle(Style.EMPTY.withColor(ChatFormatting.RED)));
                }
            }
        }
    }
}
