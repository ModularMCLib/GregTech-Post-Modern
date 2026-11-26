package com.modularmclib.gtpm.common.machine.electric;

import com.modularmclib.gtpm.api.blockentity.BlockEntityCreationInfo;
import com.modularmclib.gtpm.api.data.medicalcondition.MedicalCondition;
import com.modularmclib.gtpm.api.machine.SimpleTieredMachine;
import com.modularmclib.gtpm.api.recipe.GTRecipe;
import com.modularmclib.gtpm.api.recipe.modifier.ModifierFunction;
import com.modularmclib.gtpm.common.data.GTRecipeTypes;
import com.modularmclib.gtpm.common.data.machines.GTMachineUtils;
import com.modularmclib.gtpm.common.machine.trait.hazard.EnvironmentalHazardCleanerTrait;
import com.modularmclib.gtpm.config.ConfigHolder;
import com.modularmclib.gtpm.data.recipe.builder.GTRecipeBuilder;

import net.minecraft.network.chat.Component;

import lombok.Getter;
import org.jetbrains.annotations.Nullable;

import static com.modularmclib.gtpm.api.GTValues.LV;
import static com.modularmclib.gtpm.api.GTValues.VHA;

public class AirScrubberMachine extends SimpleTieredMachine {

    public static final float MIN_CLEANING_PER_OPERATION = 10;

    private MedicalCondition currentRecipeMedicalCondition;

    @Getter
    private float removedLastSecond;

    @Getter
    private final EnvironmentalHazardCleanerTrait cleanerTrait;

    public AirScrubberMachine(BlockEntityCreationInfo info, int tier) {
        super(info, tier, GTMachineUtils.largeTankSizeFunction);
        this.cleanerTrait = attachTrait(new EnvironmentalHazardCleanerTrait(tier / 2, this::validateCleaningOperation));
    }

    @Override
    public boolean regressWhenWaiting() {
        return false;
    }

    public boolean validateCleaningOperation(MedicalCondition condition, float amount) {
        if (getWorkLogic().isActive()) {
            return false;
        }

        currentRecipeMedicalCondition = condition;

        GTRecipeBuilder builder = GTRecipeTypes.AIR_SCRUBBER_RECIPES.recipeBuilder(condition.name + "_autogen")
                .duration(200).EUt(VHA[LV]);
        condition.recipeModifier.accept(builder);
        return this.recipeLogic.checkMatchedRecipeAvailable(builder.build());
    }

    @Override
    public boolean isWorkLogicAvailable() {
        // Don't run recipes if hazards are off
        return ConfigHolder.INSTANCE.gameplay.environmentalHazards;
    }

    @Override
    public @Nullable Component beforeWorking(@Nullable GTRecipe recipe) {
        Component failReason = super.beforeWorking(recipe);
        if (failReason != null) {
            return failReason;
        }
        // Sets the amount of hazard to clean based on the recipe tier, not the machine tier.
        if (recipe != null && cleanerTrait.beginCleaningOperation(currentRecipeMedicalCondition,
                MIN_CLEANING_PER_OPERATION * (recipe.ocLevel + 1))) {
            return null;
        }
        return ModifierFunction.DEFAULT_FAILURE;
    }

    @Override
    public boolean onWorking() {
        if (!super.onWorking() || !ConfigHolder.INSTANCE.gameplay.environmentalHazards) {
            return false;
        }
        cleanerTrait.cleanHazard();
        return true;
    }

    @Override
    public void afterWorking() {
        cleanerTrait.endCleaningOperation();
    }
}
