package com.modularmc.gtpm.common.recipe.condition;

import com.modularmc.gtpm.api.machine.trait.RecipeLogic;
import com.modularmc.gtpm.api.recipe.GTRecipe;
import com.modularmc.gtpm.api.recipe.RecipeCondition;
import com.modularmc.gtpm.api.recipe.condition.RecipeConditionType;
import com.modularmc.gtpm.common.data.GTRecipeConditions;
import com.modularmc.gtpm.common.machine.trait.ExhaustVentMachineTrait;

import net.minecraft.network.chat.Component;

import com.mojang.serialization.MapCodec;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

@NoArgsConstructor
public class VentCondition extends RecipeCondition<VentCondition> {

    public static final MapCodec<VentCondition> CODEC = RecipeCondition.simpleCodec(VentCondition::new);
    public final static VentCondition INSTANCE = new VentCondition();

    public VentCondition(boolean isReverse) {
        super(isReverse);
    }

    @Override
    public RecipeConditionType<VentCondition> getType() {
        return GTRecipeConditions.VENT;
    }

    @Override
    public Component getTooltips() {
        return Component.translatable("recipe.condition.steam_vent.tooltip");
    }

    @Override
    public boolean perTick() {
        return true;
    }

    @Override
    public boolean testCondition(@NotNull GTRecipe recipe, @NotNull RecipeLogic recipeLogic) {
        var ventTrait = recipeLogic.getMachine().getTrait(ExhaustVentMachineTrait.TYPE);
        if (recipeLogic.getProgress() % 10 == 0 && ventTrait != null) {
            return !(ventTrait.isNeedsVenting() && ventTrait.isVentingBlocked());
        }
        return true;
    }

    @Override
    public VentCondition createTemplate() {
        return new VentCondition();
    }
}
