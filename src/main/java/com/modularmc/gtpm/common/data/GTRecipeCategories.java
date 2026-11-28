package com.modularmc.gtpm.common.data;

import com.modularmc.gtpm.GTPM;
import com.modularmc.gtpm.api.gui.GuiTextures;
import com.modularmc.gtpm.api.recipe.category.GTRecipeCategory;
import com.modularmc.gtpm.api.registry.GTRegistries;

import com.modularmc.registrate.localization.MinecraftLocale;

import static com.modularmc.gtpm.common.registry.GTRegistration.REGISTRATE;

public class GTRecipeCategories {

    // disable spotless on registration blocks so they can be formatted manually
    // spotless:off
    public static final GTRecipeCategory ORE_CRUSHING = REGISTRATE.recipeCategory("ore_crushing")
            .recipeType(GTRecipeTypes.MACERATOR_RECIPES)
            .localization(text -> text.source("Ore Grinding").translation(MinecraftLocale.ZH_CN, "研磨机：矿物处理"))
            .register();
    public static final GTRecipeCategory ORE_FORGING = REGISTRATE.recipeCategory("ore_forging")
            .recipeType(GTRecipeTypes.FORGE_HAMMER_RECIPES)
            .localization(text -> text.source("Ore Crushing").translation(MinecraftLocale.ZH_CN, "锻造锤：矿物处理"))
            .register();
    public static final GTRecipeCategory ORE_BATHING = REGISTRATE.recipeCategory("ore_bathing")
            .recipeType(GTRecipeTypes.CHEMICAL_BATH_RECIPES)
            .localization(text -> text.source("Ore Treating").translation(MinecraftLocale.ZH_CN, "化学浸洗机：矿物处理"))
            .register();
    public static final GTRecipeCategory CHEM_DYES = REGISTRATE.recipeCategory("chem_dyes")
            .recipeType(GTRecipeTypes.CHEMICAL_BATH_RECIPES)
            .localization(text -> text.source("Chemical Dyeing").translation(MinecraftLocale.ZH_CN, "化学浸洗机：化学染色"))
            .register();
    public static final GTRecipeCategory INGOT_MOLDING = REGISTRATE.recipeCategory("ingot_molding")
            .recipeType(GTRecipeTypes.ALLOY_SMELTER_RECIPES)
            .localization(text -> text.source("Metal Molding").translation(MinecraftLocale.ZH_CN, "合金炉：金属成型"))
            .register();

    public static final GTRecipeCategory ARC_FURNACE_RECYCLING = REGISTRATE.recipeCategory("arc_furnace_recycling")
            .recipeType(GTRecipeTypes.ARC_FURNACE_RECIPES)
            .localization(text -> text.source("Arc Scrapping").translation(MinecraftLocale.ZH_CN, "电弧炉：回收"))
            .configure(category -> category.setIcon(GuiTextures.ARC_FURNACE_RECYCLING_CATEGORY))
            .register();

    public static final GTRecipeCategory MACERATOR_RECYCLING = REGISTRATE.recipeCategory("macerator_recycling")
            .recipeType(GTRecipeTypes.MACERATOR_RECIPES)
            .localization(text -> text.source("Part Grinding").translation(MinecraftLocale.ZH_CN, "研磨机：粉碎回收"))
            .configure(category -> category.setIcon(GuiTextures.MACERATOR_RECYCLING_CATEGORY))
            .register();

    public static final GTRecipeCategory EXTRACTOR_RECYCLING = REGISTRATE.recipeCategory("extractor_recycling")
            .recipeType(GTRecipeTypes.EXTRACTOR_RECIPES)
            .localization(text -> text.source("Scrap Remelting").translation(MinecraftLocale.ZH_CN, "流体提取机：重熔回收"))
            .configure(category -> category.setIcon(GuiTextures.EXTRACTOR_RECYCLING_CATEGORY))
            .register();
    // spotless:on

    public static void init() {}

    public static GTRecipeCategory get(String name) {
        return GTRegistries.RECIPE_CATEGORIES.get(GTPM.id(name));
    }
}
