package com.modularmclib.gtpm.common.data;

import com.modularmclib.gtpm.GTPM;
import com.modularmclib.gtpm.api.capability.recipe.*;
import com.modularmclib.gtpm.api.registry.GTRegistries;

import net.neoforged.neoforge.common.crafting.SizedIngredient;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;

public class GTRecipeCapabilities {

    public final static RecipeCapability<SizedIngredient> ITEM = ItemRecipeCapability.CAP;
    public final static RecipeCapability<SizedFluidIngredient> FLUID = FluidRecipeCapability.CAP;
    public final static RecipeCapability<Long> EU = EURecipeCapability.CAP;
    public final static RecipeCapability<Integer> CWU = CWURecipeCapability.CAP;

    public static void init() {
        GTRegistries.register(GTRegistries.RECIPE_CAPABILITIES, GTPM.id(ITEM.name), ITEM);
        GTRegistries.register(GTRegistries.RECIPE_CAPABILITIES, GTPM.id(FLUID.name), FLUID);
        GTRegistries.register(GTRegistries.RECIPE_CAPABILITIES, GTPM.id(EU.name), EU);
        GTRegistries.register(GTRegistries.RECIPE_CAPABILITIES, GTPM.id(CWU.name), CWU);
    }
}
