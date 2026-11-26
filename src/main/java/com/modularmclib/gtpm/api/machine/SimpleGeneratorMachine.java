package com.modularmclib.gtpm.api.machine;

import com.modularmclib.gtpm.api.GTValues;
import com.modularmclib.gtpm.api.blockentity.BlockEntityCreationInfo;
import com.modularmclib.gtpm.api.capability.recipe.EURecipeCapability;
import com.modularmclib.gtpm.api.capability.recipe.RecipeCapability;
import com.modularmclib.gtpm.api.gui.editor.EditableMachineUI;
import com.modularmclib.gtpm.api.gui.editor.MachineUIXmlTemplates;
import com.modularmclib.gtpm.api.gui.element.GTProgressBarElement;
import com.modularmclib.gtpm.api.gui.fancy.ConfiguratorPanel;
import com.modularmclib.gtpm.api.machine.fancyconfigurator.WorkingEnabledConfigurator;
import com.modularmclib.gtpm.api.machine.feature.IFancyUIMachine;
import com.modularmclib.gtpm.api.recipe.GTRecipe;
import com.modularmclib.gtpm.api.recipe.GTRecipeType;
import com.modularmclib.gtpm.api.recipe.content.ContentModifier;
import com.modularmclib.gtpm.api.recipe.modifier.ModifierFunction;
import com.modularmclib.gtpm.api.recipe.modifier.ParallelLogic;
import com.modularmclib.gtpm.api.recipe.modifier.RecipeModifier;
import com.modularmclib.gtpm.common.data.GTMedicalConditions;
import com.modularmclib.gtpm.common.machine.trait.hazard.EnvironmentalHazardEmitterTrait;

import net.minecraft.Util;
import net.minecraft.resources.ResourceLocation;

import it.unimi.dsi.fastutil.ints.Int2IntFunction;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiFunction;

public class SimpleGeneratorMachine extends WorkableTieredMachine
                                    implements IFancyUIMachine {

    @Getter
    private final EnvironmentalHazardEmitterTrait hazardEmitter;

    public SimpleGeneratorMachine(BlockEntityCreationInfo info, int tier,
                                  float hazardStrengthPerOperation, Int2IntFunction tankScalingFunction) {
        super(info, tier, tankScalingFunction);

        energyContainer.setSideOutputCondition(side -> !hasFrontFacing() || side == getFrontFacing());
        this.hazardEmitter = attachTrait(
                new EnvironmentalHazardEmitterTrait(GTMedicalConditions.CARBON_MONOXIDE_POISONING,
                        hazardStrengthPerOperation));
    }

    public SimpleGeneratorMachine(BlockEntityCreationInfo info, int tier, Int2IntFunction tankScalingFunction) {
        this(info, tier, 0.25f, tankScalingFunction);
    }

    //////////////////////////////////////
    // ***** Initialization ******//
    //////////////////////////////////////

    @Override
    protected boolean isEnergyEmitter() {
        return true;
    }

    @Override
    public int tintColor(int index) {
        if (index == 2) {
            return GTValues.VC[getTier()];
        }
        return super.tintColor(index);
    }

    //////////////////////////////////////
    // ****** RECIPE LOGIC *******//
    //////////////////////////////////////

    /**
     * Recipe Modifier for <b>Simple Generator Machines</b> - can be used as a valid {@link RecipeModifier}
     * <p>
     * Recipe is fast parallelized up to {@code desiredEUt / recipeEUt} times.
     * </p>
     *
     * @param machine a {@link SimpleGeneratorMachine}
     * @param recipe  recipe
     * @return A {@link ModifierFunction} for the given Simple Generator
     */
    public static ModifierFunction recipeModifier(@NotNull MetaMachine machine, @NotNull GTRecipe recipe) {
        if (!(machine instanceof SimpleGeneratorMachine generator)) {
            return RecipeModifier.nullWrongType(SimpleGeneratorMachine.class, machine);
        }
        long EUt = recipe.getOutputEUt();
        if (EUt <= 0) return ModifierFunction.NULL;

        int maxParallel = (int) (generator.getOverclockVoltage() / EUt);
        int parallels = ParallelLogic.getParallelAmountFast(generator, recipe, maxParallel);

        return ModifierFunction.builder()
                .inputModifier(ContentModifier.multiplier(parallels))
                .outputModifier(ContentModifier.multiplier(parallels))
                .eutMultiplier(parallels)
                .parallels(parallels)
                .build();
    }

    @Override
    public boolean regressWhenWaiting() {
        return false;
    }

    @Override
    public boolean canVoidRecipeOutputs(RecipeCapability<?> capability) {
        return capability != EURecipeCapability.CAP;
    }

    @Override
    public void afterWorking() {
        super.afterWorking();
        hazardEmitter.emitHazard();
    }

    @Override
    public long getDisplayRecipeVoltage() {
        return GTValues.V[this.tier];
    }

    //////////////////////////////////////
    // *********** GUI ***********//
    //////////////////////////////////////

    @Override
    public void attachConfigurators(ConfiguratorPanel configuratorPanel) {
        configuratorPanel.attachConfigurators(new WorkingEnabledConfigurator(
                this));
    }

    @SuppressWarnings("UnstableApiUsage")
    public static BiFunction<ResourceLocation, GTRecipeType, EditableMachineUI> EDITABLE_UI_CREATOR = Util
            .memoize((path, recipeType) -> new EditableMachineUI("generator", path,
                    () -> MachineUIXmlTemplates.createGeneratorMachineXml(
                            recipeType.getRecipeUI().createTemplateDocument()),
                    (root, machine) -> {
                        if (!(machine instanceof SimpleGeneratorMachine generatorMachine)) {
                            throw new IllegalArgumentException("Generator machine XML cannot bind " +
                                    machine.getClass().getName());
                        }
                        generatorMachine.bindRecipeElements(root);
                        generatorMachine.bindEnergyBar(MachineUIXmlTemplates.requireElement(root,
                                MachineUIXmlTemplates.ENERGY_BAR_ID, GTProgressBarElement.class));
                    }));
}
