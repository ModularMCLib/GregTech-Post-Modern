package com.modularmc.gtpm.common.data;

import com.modularmc.gtpm.GTPM;
import com.modularmc.gtpm.api.recipe.RecipeCondition;
import com.modularmc.gtpm.api.recipe.condition.RecipeConditionType;
import com.modularmc.gtpm.common.recipe.condition.*;

import com.modularmc.registrate.localization.MinecraftLocale;
import com.modularmc.registrate.localization.annotation.TransVariants;
import com.modularmc.registrate.localization.annotation.Translation;
import com.modularmc.registrate.localization.annotation.Variant;

import com.mojang.serialization.MapCodec;

import static com.modularmc.gtpm.common.registry.GTRegistration.REGISTRATE;

@TransVariants(keyPrefix = "recipe.condition",
               value = {
                       @Variant(id = "biome.tooltip",
                                source = "Biome: %s",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "生物群系：%s")),
                       @Variant(id = "dimension_marker.tooltip",
                                source = "Dimension:",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "维度：")),
                       @Variant(id = "gamestage.locked_stage",
                                source = "Locked at stage: %s",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "锁定阶段：%s")),
                       @Variant(id = "gamestage.unlocked_stage",
                                source = "Unlocked at stage: %s",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "解锁阶段：%s")),
                       @Variant(id = "quest.completed.tooltip",
                                source = "Requires %s completed",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "完成任务：%s")),
                       @Variant(id = "quest.not_completed.tooltip",
                                source = "Requires %s not completed",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "未完成任务：%s")),
               })
public final class GTRecipeConditions {

    private GTRecipeConditions() {}

    // spotless:off
    public static final RecipeConditionType<BiomeCondition> BIOME = register("biome", BiomeCondition::new, BiomeCondition.CODEC);
    public static final RecipeConditionType<DimensionCondition> DIMENSION = register("dimension", DimensionCondition::new, DimensionCondition.CODEC);
    public static final RecipeConditionType<BiomeTagCondition> BIOME_TAG = register("biome_tag", BiomeTagCondition::new, BiomeTagCondition.CODEC);
    public static final RecipeConditionType<PositionYCondition> POSITION_Y = register("pos_y", PositionYCondition::new, PositionYCondition.CODEC);
    public static final RecipeConditionType<RainingCondition> RAINING = register("rain", RainingCondition::new, RainingCondition.CODEC);
    public static final RecipeConditionType<AdjacentFluidCondition> ADJACENT_FLUID = register("adjacent_fluid", AdjacentFluidCondition::new, AdjacentFluidCondition.CODEC);
    public static final RecipeConditionType<AdjacentBlockCondition> ADJACENT_BLOCK = register("adjacent_block", AdjacentBlockCondition::new, AdjacentBlockCondition.CODEC);
    public static final RecipeConditionType<ThunderCondition> THUNDER = register("thunder", ThunderCondition::new, ThunderCondition.CODEC);
    public static final RecipeConditionType<VentCondition> VENT = register("steam_vent", VentCondition::new, VentCondition.CODEC);
    public static final RecipeConditionType<CleanroomCondition> CLEANROOM = register("cleanroom", CleanroomCondition::new, CleanroomCondition.CODEC);
    public static final RecipeConditionType<EUToStartCondition> EU_TO_START = register("eu_to_start", EUToStartCondition::new, EUToStartCondition.CODEC);
    public static final RecipeConditionType<ResearchCondition> RESEARCH = register("research", ResearchCondition::new, ResearchCondition.CODEC);
    public static final RecipeConditionType<EnvironmentalHazardCondition> ENVIRONMENTAL_HAZARD = register("environmental_hazard", EnvironmentalHazardCondition::new, EnvironmentalHazardCondition.CODEC);
    public static final RecipeConditionType<DaytimeCondition> DAYTIME = register("daytime", DaytimeCondition::new, DaytimeCondition.CODEC);
    public static RecipeConditionType<FTBQuestCondition> FTB_QUEST;
//     public static RecipeConditionType<GameStageCondition> GAMESTAGE;
    public static RecipeConditionType<HeraclesQuestCondition> HERACLES_QUEST;

    public static void init() {
        if (GTPM.MODS.FTB_QUESTS.isLoaded()) {
            FTB_QUEST = register("ftb_quest", FTBQuestCondition::new, FTBQuestCondition.CODEC);
        }
        // if (GTPM.Mods.isGameStagesLoaded()) {
        //     GAMESTAGE = register("game_stage", GameStageCondition::new, GameStageCondition.CODEC);
        // }
        if (GTPM.MODS.HERACLES.isLoaded()) {
            HERACLES_QUEST = register("heracles_quest", HeraclesQuestCondition::new, HeraclesQuestCondition.CODEC);
        }
    }
    // spotless:on

    private static <T extends RecipeCondition<T>> RecipeConditionType<T> register(String name,
                                                                                  RecipeConditionType.ConditionFactory<T> factory,
                                                                                  MapCodec<T> codec) {
        return REGISTRATE.recipeCondition(name, factory, codec).register();
    }
}
