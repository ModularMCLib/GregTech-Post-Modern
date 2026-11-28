package com.modularmc.gtpm.common.recipe.condition;

import com.modularmc.gtpm.api.machine.trait.RecipeLogic;
import com.modularmc.gtpm.api.recipe.GTRecipe;
import com.modularmc.gtpm.api.recipe.RecipeCondition;
import com.modularmc.gtpm.api.recipe.condition.RecipeConditionType;
import com.modularmc.gtpm.common.data.GTRecipeConditions;

import com.modularmc.registrate.localization.MinecraftLocale;
import com.modularmc.registrate.localization.annotation.TransVariants;
import com.modularmc.registrate.localization.annotation.Translation;
import com.modularmc.registrate.localization.annotation.Variant;

import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;

import com.mojang.serialization.MapCodec;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

@TransVariants(keyPrefix = "recipe.condition.daytime",
               value = {
                       @Variant(id = "day.tooltip",
                                source = "Requires day time to work",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "需要白天才能工作")),
                       @Variant(id = "night.tooltip",
                                source = "Requires night time to work",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "需要夜晚才能工作")),
               })
@Getter
@NoArgsConstructor
public class DaytimeCondition extends RecipeCondition<DaytimeCondition> {

    // spotless:off
	public static final MapCodec<DaytimeCondition> CODEC = RecipeCondition.simpleCodec(DaytimeCondition::new);
	// spotless:off

	public DaytimeCondition(boolean isReverse) {
		super(isReverse);
	}

	@Override
	public RecipeConditionType<DaytimeCondition> getType() {
		return GTRecipeConditions.DAYTIME;
	}

	@Override
	public Component getTooltips() {
		if (isReverse) {
			return Component.translatable("recipe.condition.daytime.night.tooltip");
		} else {
			return Component.translatable("recipe.condition.daytime.day.tooltip");
		}
	}

	@Override
	public boolean perTick() {
		return true;
	}

	@Override
	public boolean testCondition(@NotNull GTRecipe recipe, @NotNull RecipeLogic recipeLogic) {
		Level level = recipeLogic.getLevel();
		return level != null && level.isDay();
	}

	@Override
	public DaytimeCondition createTemplate() {
		return new DaytimeCondition();
	}
}
