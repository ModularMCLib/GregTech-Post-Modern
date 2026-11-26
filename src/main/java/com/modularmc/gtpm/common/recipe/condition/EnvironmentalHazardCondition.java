package com.modularmc.gtpm.common.recipe.condition;

import com.modularmc.gtpm.api.data.medicalcondition.MedicalCondition;
import com.modularmc.gtpm.api.machine.trait.RecipeLogic;
import com.modularmc.gtpm.api.recipe.GTRecipe;
import com.modularmc.gtpm.api.recipe.RecipeCondition;
import com.modularmc.gtpm.api.recipe.condition.RecipeConditionType;
import com.modularmc.gtpm.common.capability.EnvironmentalHazardSavedData;
import com.modularmc.gtpm.common.data.GTMedicalConditions;
import com.modularmc.gtpm.common.data.GTRecipeConditions;
import com.modularmc.gtpm.config.ConfigHolder;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

@NoArgsConstructor
@AllArgsConstructor
public class EnvironmentalHazardCondition extends RecipeCondition<EnvironmentalHazardCondition> {

    // spotless:off
    public static final MapCodec<EnvironmentalHazardCondition> CODEC = RecordCodecBuilder.mapCodec(instance -> RecipeCondition.isReverse(instance).and(
            MedicalCondition.CODEC.fieldOf("condition").forGetter(EnvironmentalHazardCondition::getCondition)
    ).apply(instance, EnvironmentalHazardCondition::new));
    // spotless:on

    @Getter
    private MedicalCondition condition = GTMedicalConditions.CARBON_MONOXIDE_POISONING;

    public EnvironmentalHazardCondition(boolean isReverse, MedicalCondition condition) {
        super(isReverse);
        this.condition = condition;
    }

    @Override
    public RecipeConditionType<EnvironmentalHazardCondition> getType() {
        return GTRecipeConditions.ENVIRONMENTAL_HAZARD;
    }

    @Override
    public Component getTooltips() {
        return isReverse ?
                Component.translatable("gtpm.recipe.environmental_hazard.reverse",
                        Component.translatable("gtpm.medical_condition." + condition.name)) :
                Component.translatable("gtpm.recipe.environmental_hazard",
                        Component.translatable("gtpm.medical_condition." + condition.name));
    }

    @Override
    public boolean testCondition(@NotNull GTRecipe recipe, @NotNull RecipeLogic recipeLogic) {
        if (!ConfigHolder.INSTANCE.gameplay.hazardsEnabled) return true;
        if (!(recipeLogic.getMachine().getLevel() instanceof ServerLevel serverLevel)) {
            return false;
        }
        EnvironmentalHazardSavedData savedData = EnvironmentalHazardSavedData.getOrCreate(serverLevel);
        var zone = savedData.getZoneByContainedPos(recipeLogic.getMachine().getBlockPos());
        return zone != null && zone.strength() > 0;
    }

    @Override
    public EnvironmentalHazardCondition createTemplate() {
        return new EnvironmentalHazardCondition();
    }
}
