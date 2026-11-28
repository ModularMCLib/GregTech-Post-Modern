package com.modularmc.gtpm.common.machine.steam;

import com.modularmc.gtpm.api.blockentity.BlockEntityCreationInfo;
import com.modularmc.gtpm.api.capability.recipe.FluidRecipeCapability;
import com.modularmc.gtpm.api.capability.recipe.IO;
import com.modularmc.gtpm.api.gui.factory.MachineUIHolder;
import com.modularmc.gtpm.api.machine.steam.SteamBoilerMachine;
import com.modularmc.gtpm.api.machine.trait.NotifiableFluidTank;
import com.modularmc.gtpm.api.sync_system.annotations.SaveField;
import com.modularmc.gtpm.config.GTPMConfigHolder;
import com.modularmc.gtpm.utils.ExtendedUseOnContext;

import com.modularmc.registrate.localization.MinecraftLocale;
import com.modularmc.registrate.localization.annotation.Trans;
import com.modularmc.registrate.localization.annotation.Translation;

import com.lowdragmc.lowdraglib2.gui.ui.UIElement;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.fluids.FluidUtil;

import it.unimi.dsi.fastutil.objects.Object2BooleanMap;
import it.unimi.dsi.fastutil.objects.Object2BooleanOpenHashMap;

import java.util.Arrays;
import java.util.Collections;

@Trans(key = "block.gtpm.steam_liquid_boiler.bronze",
       source = "Small Steam Liquid Boiler",
       translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "小型蒸汽液体燃料锅炉"))
public class SteamLiquidBoilerMachine extends SteamBoilerMachine {

    public static final Object2BooleanMap<Fluid> FUEL_CACHE = new Object2BooleanOpenHashMap<>();

    @SaveField
    public final NotifiableFluidTank fuelTank;

    public SteamLiquidBoilerMachine(BlockEntityCreationInfo info, boolean isHighPressure) {
        super(info, isHighPressure);
        this.fuelTank = attachTrait(new NotifiableFluidTank(1, 16 * FluidType.BUCKET_VOLUME, IO.IN));
        fuelTank.setFilter(fluid -> FUEL_CACHE.computeIfAbsent(fluid.getFluid(), f -> {
            if (isRemote()) return true;
            return recipeLogic.getRecipeManager().getAllRecipesFor(getRecipeType()).stream().anyMatch(recipe -> {
                var list = recipe.value().inputs.getOrDefault(FluidRecipeCapability.CAP, Collections.emptyList());
                if (!list.isEmpty()) {
                    return Arrays.stream(FluidRecipeCapability.CAP.of(list.getFirst().content).getFluids())
                            .anyMatch(stack -> stack.getFluid() == f);
                }
                return false;
            });
        }));
    }

    //////////////////////////////////////
    // ***** Initialization *****//
    //////////////////////////////////////

    @Override
    protected long getBaseSteamOutput() {
        return isHighPressure ? GTPMConfigHolder.INSTANCE.machines.smallBoilers.hpLiquidBoilerBaseOutput :
                GTPMConfigHolder.INSTANCE.machines.smallBoilers.liquidBoilerBaseOutput;
    }

    @Override
    protected void addAdditionalWidgets(UIElement root, Player player, MachineUIHolder holder) {
        root.addChild(createFluidSlot(player, holder, fuelTank.getStorages()[0], 119, 26,
                FUEL_FLUID_SLOT, true, true));
    }

    @Override
    protected FluidClickTarget getFluidClickTarget(int fluidSlot) {
        if (fluidSlot == FUEL_FLUID_SLOT) {
            return createFluidClickTarget(fuelTank.getStorages()[0], true, true);
        }
        return super.getFluidClickTarget(fluidSlot);
    }

    @Override
    protected void randomDisplayTick(RandomSource random, float x, float y, float z) {
        super.randomDisplayTick(random, x, y, z);
        if (random.nextFloat() < 0.3F) {
            getLevel().addParticle(ParticleTypes.LAVA, x + random.nextFloat(), y,
                    z + random.nextFloat(), 0.0F, 0.0F,
                    0.0F);
        }
    }

    @Override
    public InteractionResult onUseWithItem(ExtendedUseOnContext context) {
        if (!isRemote()) {
            if (FluidUtil.interactWithFluidHandler(context.getPlayer(), context.getHand(), fuelTank)) {
                return InteractionResult.SUCCESS;
            }
        }
        return super.onUseWithItem(context);
    }
}
