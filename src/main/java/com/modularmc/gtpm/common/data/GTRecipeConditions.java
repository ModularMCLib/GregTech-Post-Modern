package com.modularmc.gtpm.common.data;

import com.modularmc.gtpm.GTPM;
import com.modularmc.gtpm.api.recipe.condition.RecipeConditionType;
import com.modularmc.gtpm.common.recipe.condition.*;

import com.modularmc.registrate.localization.MinecraftLocale;

import static com.modularmc.gtpm.common.registry.GTRegistration.REGISTRATE;

public final class GTRecipeConditions {

    private GTRecipeConditions() {}

    // spotless:off
    public static final RecipeConditionType<BiomeCondition> BIOME = REGISTRATE
            .recipeCondition("biome", BiomeCondition::new, BiomeCondition.CODEC)
            .localization(text -> text.source("Biome: %s").translation(MinecraftLocale.ZH_CN, "生物群系：%s"))
            .register();
    public static final RecipeConditionType<DimensionCondition> DIMENSION = REGISTRATE
            .recipeCondition("dimension", DimensionCondition::new, DimensionCondition.CODEC)
            .localization(text -> text.source("Dimension: %s").translation(MinecraftLocale.ZH_CN, "维度：%s"))
            .register();
    public static final RecipeConditionType<BiomeTagCondition> BIOME_TAG = REGISTRATE
            .recipeCondition("biome_tag", BiomeTagCondition::new, BiomeTagCondition.CODEC)
            .register();
    public static final RecipeConditionType<PositionYCondition> POSITION_Y = REGISTRATE
            .recipeCondition("pos_y", PositionYCondition::new, PositionYCondition.CODEC)
            .localization(text -> text.source("Y Level: %d <= Y <= %d")
                    .translation(MinecraftLocale.ZH_CN, "Y轴高度：%d <= Y <= %d"))
            .register();
    public static final RecipeConditionType<RainingCondition> RAINING = REGISTRATE
            .recipeCondition("rain", RainingCondition::new, RainingCondition.CODEC)
            .localization(text -> text.source("Rain Level: %d").translation(MinecraftLocale.ZH_CN, "雨量：%d"))
            .register();
    public static final RecipeConditionType<AdjacentFluidCondition> ADJACENT_FLUID = REGISTRATE
            .recipeCondition("adjacent_fluid", AdjacentFluidCondition::new, AdjacentFluidCondition.CODEC)
            .localization(text -> text.source("Fluid blocks around").translation(MinecraftLocale.ZH_CN, "水平相邻流体应为"))
            .register();
    public static final RecipeConditionType<AdjacentBlockCondition> ADJACENT_BLOCK = REGISTRATE
            .recipeCondition("adjacent_block", AdjacentBlockCondition::new, AdjacentBlockCondition.CODEC)
            .localization(text -> text.source("Blocks around").translation(MinecraftLocale.ZH_CN, "水平相邻方块应为"))
            .register();
    public static final RecipeConditionType<ThunderCondition> THUNDER = REGISTRATE
            .recipeCondition("thunder", ThunderCondition::new, ThunderCondition.CODEC)
            .localization(text -> text.source("Thunder Level: %d").translation(MinecraftLocale.ZH_CN, "雷级：%d"))
            .register();
    public static final RecipeConditionType<VentCondition> VENT = REGISTRATE
            .recipeCondition("steam_vent", VentCondition::new, VentCondition.CODEC)
            .localization(text -> text.source("Clean steam vent").translation(MinecraftLocale.ZH_CN, "排气口堵塞"))
            .register();
    public static final RecipeConditionType<CleanroomCondition> CLEANROOM = REGISTRATE
            .recipeCondition("cleanroom", CleanroomCondition::new, CleanroomCondition.CODEC)
            .localization("gtpm.recipe.cleanroom",
                    text -> text.source("Requires %s").translation(MinecraftLocale.ZH_CN, "需要%s"))
            .register();
    public static final RecipeConditionType<EUToStartCondition> EU_TO_START = REGISTRATE
            .recipeCondition("eu_to_start", EUToStartCondition::new, EUToStartCondition.CODEC)
            .localization(text -> text.source("EU to Start: %d%s").translation(MinecraftLocale.ZH_CN, "启动耗能：%d%s"))
            .register();
    public static final RecipeConditionType<ResearchCondition> RESEARCH = REGISTRATE
            .recipeCondition("research", ResearchCondition::new, ResearchCondition.CODEC)
            .localization("gtpm.recipe.research",
                    text -> text.source("Requires Research").translation(MinecraftLocale.ZH_CN, "需要研究"))
            .register();
    public static final RecipeConditionType<EnvironmentalHazardCondition> ENVIRONMENTAL_HAZARD = REGISTRATE
            .recipeCondition("environmental_hazard", EnvironmentalHazardCondition::new, EnvironmentalHazardCondition.CODEC)
            .localization("gtpm.recipe.environmental_hazard",
                    text -> text.source("§cArea must have %s").translation(MinecraftLocale.ZH_CN, "§c区域内必须有%s"))
            .localization("gtpm.recipe.environmental_hazard.reverse",
                    text -> text.source("§cArea must be free of %s").translation(MinecraftLocale.ZH_CN, "§c区域内必须没有%s"))
            .register();
    public static final RecipeConditionType<DaytimeCondition> DAYTIME = REGISTRATE
            .recipeCondition("daytime", DaytimeCondition::new, DaytimeCondition.CODEC)
            .localization("recipe.condition.daytime.day.tooltip",
                    text -> text.source("Requires day time to work").translation(MinecraftLocale.ZH_CN, "需要白天才能工作"))
            .localization("recipe.condition.daytime.night.tooltip",
                    text -> text.source("Requires night time to work").translation(MinecraftLocale.ZH_CN, "需要夜晚才能工作"))
            .register();
    public static RecipeConditionType<FTBQuestCondition> FTB_QUEST;
    public static RecipeConditionType<HeraclesQuestCondition> HERACLES_QUEST;
    // spotless:on

    public static void init() {
        REGISTRATE
                .recipeConditionLocalization("dimension_marker.tooltip",
                        text -> text.source("Dimension:").translation(MinecraftLocale.ZH_CN, "维度："))
                .recipeConditionLocalization("gamestage.locked_stage",
                        text -> text.source("Locked at stage: %s").translation(MinecraftLocale.ZH_CN, "锁定阶段：%s"))
                .recipeConditionLocalization("gamestage.unlocked_stage",
                        text -> text.source("Unlocked at stage: %s").translation(MinecraftLocale.ZH_CN, "解锁阶段：%s"))
                .recipeConditionLocalization("quest.completed.tooltip",
                        text -> text.source("Requires %s completed").translation(MinecraftLocale.ZH_CN, "完成任务：%s"))
                .recipeConditionLocalization("quest.not_completed.tooltip",
                        text -> text.source("Requires %s not completed").translation(MinecraftLocale.ZH_CN, "未完成任务：%s"));
        if (GTPM.MODS.FTB_QUESTS.isLoaded()) {
            FTB_QUEST = REGISTRATE.recipeCondition("ftb_quest", FTBQuestCondition::new, FTBQuestCondition.CODEC)
                    .register();
        }
        if (GTPM.MODS.HERACLES.isLoaded()) {
            HERACLES_QUEST = REGISTRATE
                    .recipeCondition("heracles_quest", HeraclesQuestCondition::new, HeraclesQuestCondition.CODEC)
                    .register();
        }
    }
}
