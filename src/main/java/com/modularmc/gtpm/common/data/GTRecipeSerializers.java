package com.modularmc.gtpm.common.data;

import com.modularmc.gtpm.api.recipe.*;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
import net.neoforged.neoforge.registries.DeferredHolder;

import static com.modularmc.gtpm.common.registry.GTRegistration.REGISTRATE;

public class GTRecipeSerializers {

    public static final DeferredHolder<RecipeSerializer<?>, StrictShapedRecipe.Serializer> CRAFTING_SHAPED_STRICT = REGISTRATE
            .generic("crafting_shaped_strict", Registries.RECIPE_SERIALIZER, StrictShapedRecipe.Serializer::new)
            .register();
    public static final DeferredHolder<RecipeSerializer<?>, ShapedEnergyTransferRecipe.Serializer> CRAFTING_SHAPED_ENERGY_TRANSFER = REGISTRATE
            .generic("crafting_shaped_energy_transfer", Registries.RECIPE_SERIALIZER, ShapedEnergyTransferRecipe.Serializer::new)
            .register();
    public static final DeferredHolder<RecipeSerializer<?>, ShapedFluidContainerRecipe.Serializer> CRAFTING_SHAPED_FLUID_CONTAINER = REGISTRATE
            .generic("crafting_shaped_fluid_container", Registries.RECIPE_SERIALIZER, ShapedFluidContainerRecipe.Serializer::new)
            .register();

    public static final DeferredHolder<RecipeSerializer<?>, SimpleCraftingRecipeSerializer<ToolHeadReplaceRecipe>> CRAFTING_TOOL_HEAD_REPLACE = REGISTRATE
            .generic("crafting_tool_head_replace", Registries.RECIPE_SERIALIZER,
                    () -> new SimpleCraftingRecipeSerializer<>(ToolHeadReplaceRecipe::new))
            .register();
    public static final DeferredHolder<RecipeSerializer<?>, SimpleCraftingRecipeSerializer<FacadeCoverRecipe>> CRAFTING_FACADE_COVER = REGISTRATE
            .generic("crafting_facade_cover", Registries.RECIPE_SERIALIZER,
                    () -> new SimpleCraftingRecipeSerializer<>(FacadeCoverRecipe::new))
            .register();

    public static void init() {}
}
