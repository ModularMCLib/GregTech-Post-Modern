package com.modularmclib.gtpm.common.recipe.condition;

import com.modularmclib.gtpm.api.machine.trait.RecipeLogic;
import com.modularmclib.gtpm.api.recipe.GTRecipe;
import com.modularmclib.gtpm.api.recipe.RecipeCondition;
import com.modularmclib.gtpm.api.recipe.condition.RecipeConditionType;
import com.modularmclib.gtpm.common.data.GTRecipeConditions;

import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

@NoArgsConstructor
public class RainingCondition extends RecipeCondition<RainingCondition> {

    // spotless:off
    public static final MapCodec<RainingCondition> CODEC = RecordCodecBuilder.mapCodec(instance -> RecipeCondition.isReverse(instance).and(
            Codec.FLOAT.fieldOf("level").forGetter(val -> val.level)
    ).apply(instance, RainingCondition::new));
    // spotless:on

    private float level;

    public RainingCondition(boolean isReverse, float level) {
        super(isReverse);
        this.level = level;
    }

    public RainingCondition(float level) {
        this.level = level;
    }

    @Override
    public RecipeConditionType<RainingCondition> getType() {
        return GTRecipeConditions.RAINING;
    }

    @Override
    public Component getTooltips() {
        return Component.translatable("recipe.condition.rain.tooltip", level);
    }

    public float getLevel() {
        return level;
    }

    @Override
    public boolean perTick() {
        return true;
    }

    @Override
    public boolean testCondition(@NotNull GTRecipe recipe, @NotNull RecipeLogic recipeLogic) {
        Level level = recipeLogic.getLevel();
        return level.getRainLevel(1) >= this.level;
    }

    @Override
    public RainingCondition createTemplate() {
        return new RainingCondition();
    }
}
