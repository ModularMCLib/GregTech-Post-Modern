package com.modularmc.gtpm.data.recipe;

import com.modularmc.gtpm.api.recipe.ingredient.*;

import net.neoforged.neoforge.common.crafting.IngredientType;
import net.neoforged.neoforge.fluids.crafting.FluidIngredientType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import static com.modularmc.gtpm.common.registry.GTRegistration.REGISTRATE;

public class GTIngredientTypes {

    public static final DeferredHolder<IngredientType<?>, IngredientType<IntCircuitIngredient>> INT_CIRCUIT_INGREDIENT = REGISTRATE
            .generic("circuit", NeoForgeRegistries.Keys.INGREDIENT_TYPES,
                    () -> new IngredientType<>(IntCircuitIngredient.CODEC))
            .register();

    public static final DeferredHolder<IngredientType<?>, IngredientType<IntProviderIngredient>> INT_PROVIDER_INGREDIENT = REGISTRATE
            .generic("int_provider", NeoForgeRegistries.Keys.INGREDIENT_TYPES,
                    () -> new IngredientType<>(IntProviderIngredient.CODEC))
            .register();

    public static final DeferredHolder<IngredientType<?>, IngredientType<FluidContainerIngredient>> FLUID_CONTAINER_INGREDIENT = REGISTRATE
            .generic("fluid_container", NeoForgeRegistries.Keys.INGREDIENT_TYPES,
                    () -> new IngredientType<>(FluidContainerIngredient.CODEC))
            .register();

    public static final DeferredHolder<IngredientType<?>, IngredientType<ExDataComponentIngredient>> DATA_COMPONENT_INGREDIENT = REGISTRATE
            .generic("components", NeoForgeRegistries.Keys.INGREDIENT_TYPES,
                    () -> new IngredientType<>(ExDataComponentIngredient.CODEC))
            .register();

    public static final DeferredHolder<FluidIngredientType<?>, FluidIngredientType<ExDataComponentFluidIngredient>> DATA_COMPONENT_FLUID_INGREDIENT = REGISTRATE
            .generic("components", NeoForgeRegistries.Keys.FLUID_INGREDIENT_TYPES,
                    () -> new FluidIngredientType<>(ExDataComponentFluidIngredient.CODEC))
            .register();

    public static final DeferredHolder<FluidIngredientType<?>, FluidIngredientType<IntProviderFluidIngredient>> INT_PROVIDER_FLUID_INGREDIENT = REGISTRATE
            .generic("int_provider", NeoForgeRegistries.Keys.FLUID_INGREDIENT_TYPES,
                    () -> new FluidIngredientType<>(IntProviderFluidIngredient.CODEC))
            .register();

    public static void init() {}
}
