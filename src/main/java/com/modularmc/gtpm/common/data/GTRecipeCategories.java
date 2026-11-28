package com.modularmc.gtpm.common.data;

import com.modularmc.gtpm.GTPM;
import com.modularmc.gtpm.api.gui.GuiTextures;
import com.modularmc.gtpm.api.recipe.GTRecipeType;
import com.modularmc.gtpm.api.recipe.category.GTRecipeCategory;
import com.modularmc.gtpm.api.registry.GTRegistries;

import com.modularmc.registrate.localization.MinecraftLocale;
import com.modularmc.registrate.localization.annotation.Trans;
import com.modularmc.registrate.localization.annotation.Translation;

import net.minecraft.resources.ResourceLocation;

import org.jetbrains.annotations.NotNull;

public class GTRecipeCategories {

    // disable spotless on registration blocks so they can be formatted manually
    // spotless:off
    @Trans(key = "recipe_category.gtpm.ore_crushing", source = "Ore Grinding",
            translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "研磨机：矿物处理"))
    public static final GTRecipeCategory ORE_CRUSHING = register("ore_crushing", GTRecipeTypes.MACERATOR_RECIPES);
    @Trans(key = "recipe_category.gtpm.ore_forging", source = "Ore Crushing",
            translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "锻造锤：矿物处理"))
    public static final GTRecipeCategory ORE_FORGING = register("ore_forging", GTRecipeTypes.FORGE_HAMMER_RECIPES);
    @Trans(key = "recipe_category.gtpm.ore_bathing", source = "Ore Treating",
            translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "化学浸洗机：矿物处理"))
    public static final GTRecipeCategory ORE_BATHING = register("ore_bathing", GTRecipeTypes.CHEMICAL_BATH_RECIPES);
    @Trans(key = "recipe_category.gtpm.chem_dyes", source = "Chemical Dyeing",
            translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "化学浸洗机：化学染色"))
    public static final GTRecipeCategory CHEM_DYES = register("chem_dyes", GTRecipeTypes.CHEMICAL_BATH_RECIPES);
    @Trans(key = "recipe_category.gtpm.ingot_molding", source = "Metal Molding",
            translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "合金炉：金属成型"))
    public static final GTRecipeCategory INGOT_MOLDING = register("ingot_molding", GTRecipeTypes.ALLOY_SMELTER_RECIPES);

    @Trans(key = "recipe_category.gtpm.arc_furnace_recycling", source = "Arc Scrapping",
            translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "电弧炉：回收"))
    public static final GTRecipeCategory ARC_FURNACE_RECYCLING = register("arc_furnace_recycling", GTRecipeTypes.ARC_FURNACE_RECIPES)
            .setIcon(GuiTextures.ARC_FURNACE_RECYCLING_CATEGORY);

    @Trans(key = "recipe_category.gtpm.macerator_recycling", source = "Part Grinding",
            translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "研磨机：粉碎回收"))
    public static final GTRecipeCategory MACERATOR_RECYCLING = register("macerator_recycling", GTRecipeTypes.MACERATOR_RECIPES)
            .setIcon(GuiTextures.MACERATOR_RECYCLING_CATEGORY);

    @Trans(key = "recipe_category.gtpm.extractor_recycling", source = "Scrap Remelting",
            translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "流体提取机：重熔回收"))
    public static final GTRecipeCategory EXTRACTOR_RECYCLING = register("extractor_recycling", GTRecipeTypes.EXTRACTOR_RECIPES)
            .setIcon(GuiTextures.EXTRACTOR_RECYCLING_CATEGORY);
    // spotless:on

    public static GTRecipeCategory register(ResourceLocation id, @NotNull GTRecipeType recipeType) {
        GTRecipeCategory category = new GTRecipeCategory(id, recipeType);
        GTRegistries.register(GTRegistries.RECIPE_CATEGORIES, category.registryKey, category);
        return category;
    }

    private static GTRecipeCategory register(String categoryName, @NotNull GTRecipeType recipeType) {
        return register(GTPM.id(categoryName), recipeType);
    }

    public static void init() {}

    public static GTRecipeCategory get(String name) {
        return GTRegistries.RECIPE_CATEGORIES.get(GTPM.id(name));
    }
}
