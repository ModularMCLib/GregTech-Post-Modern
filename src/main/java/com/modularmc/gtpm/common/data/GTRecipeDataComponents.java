package com.modularmc.gtpm.common.data;

import com.modularmc.gtpm.api.recipe.RecipeData;

import net.minecraft.core.component.DataComponentType;
import net.neoforged.neoforge.registries.DeferredHolder;

import static com.modularmc.gtpm.common.registry.GTRegistration.REGISTRATE;

public final class GTRecipeDataComponents {

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<RecipeData>> RECIPE_DATA = REGISTRATE
            .<RecipeData>dataComponent("recipe_data")
            .properties(builder -> builder.persistent(RecipeData.CODEC)
                    .networkSynchronized(RecipeData.STREAM_CODEC))
            .register();

    private GTRecipeDataComponents() {}

    public static void init() {}
}
