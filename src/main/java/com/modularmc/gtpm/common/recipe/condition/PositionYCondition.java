package com.modularmc.gtpm.common.recipe.condition;

import com.modularmc.gtpm.api.machine.trait.RecipeLogic;
import com.modularmc.gtpm.api.recipe.GTRecipe;
import com.modularmc.gtpm.api.recipe.RecipeCondition;
import com.modularmc.gtpm.api.recipe.condition.RecipeConditionType;
import com.modularmc.gtpm.common.data.GTRecipeConditions;

import com.modularmc.registrate.localization.MinecraftLocale;
import com.modularmc.registrate.localization.annotation.Trans;
import com.modularmc.registrate.localization.annotation.Translation;

import net.minecraft.network.chat.Component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

@Trans(key = "recipe.condition.pos_y.tooltip",
       source = "Y Level: %d <= Y <= %d",
       translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "Y轴高度：%d <= Y <= %d"))
@NoArgsConstructor
public class PositionYCondition extends RecipeCondition<PositionYCondition> {

    // spotless:off
    public static final MapCodec<PositionYCondition> CODEC = RecordCodecBuilder.mapCodec(instance -> RecipeCondition.isReverse(instance).and(instance.group(
            Codec.INT.fieldOf("min").forGetter(val -> val.min),
            Codec.INT.fieldOf("max").forGetter(val -> val.max)
    )).apply(instance, PositionYCondition::new));
    // spotless:on

    private int min;
    private int max;

    public PositionYCondition(int min, int max) {
        this.min = min;
        this.max = max;
    }

    public PositionYCondition(boolean isReverse, int min, int max) {
        super(isReverse);
        this.min = min;
        this.max = max;
    }

    @Override
    public RecipeConditionType<PositionYCondition> getType() {
        return GTRecipeConditions.POSITION_Y;
    }

    @Override
    public Component getTooltips() {
        return Component.translatable("recipe.condition.pos_y.tooltip", this.min, this.max);
    }

    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }

    @Override
    public boolean testCondition(@NotNull GTRecipe recipe, @NotNull RecipeLogic recipeLogic) {
        int y = recipeLogic.getBlockPos().getY();
        return y >= this.min && y <= this.max;
    }

    @Override
    public PositionYCondition createTemplate() {
        return new PositionYCondition();
    }
}
