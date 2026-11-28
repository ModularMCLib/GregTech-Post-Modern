package com.modularmc.gtpm.common.data;

import com.modularmc.gtpm.api.capability.recipe.*;

import net.neoforged.neoforge.common.crafting.SizedIngredient;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;

import static com.modularmc.gtpm.common.registry.GTRegistration.REGISTRATE;

public class GTRecipeCapabilities {

    public final static RecipeCapability<SizedIngredient> ITEM = ItemRecipeCapability.CAP;
    public final static RecipeCapability<SizedFluidIngredient> FLUID = FluidRecipeCapability.CAP;
    public final static RecipeCapability<Long> EU = EURecipeCapability.CAP;
    public final static RecipeCapability<Integer> CWU = CWURecipeCapability.CAP;

    public static void init() {
        REGISTRATE.recipeCapability(ITEM.name).definition(ITEM).register();
        REGISTRATE.recipeCapability(FLUID.name).definition(FLUID).register();
        REGISTRATE.recipeCapability(EU.name).definition(EU).register();
        REGISTRATE.recipeCapability(CWU.name).definition(CWU).register();
    }
}
