package com.modularmc.gtpm.api.recipe.condition;

import com.modularmc.gtpm.api.recipe.RecipeCondition;
import com.modularmc.gtpm.api.registry.GTRegistries;
import com.modularmc.gtpm.api.registry.registrate.GTRegistrate;
import com.modularmc.gtpm.api.registry.registrate.GTRegistryEntryBuilder;

import net.minecraft.resources.ResourceLocation;

import com.mojang.serialization.MapCodec;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class RecipeConditionType<T extends RecipeCondition<T>> {

    public final ConditionFactory<T> factory;
    @Getter
    public final MapCodec<T> codec;

    public record Builder(GTRegistrate registrate, ResourceLocation id) {

        public <T extends RecipeCondition<T>> CodecBuilder<T> factory(ConditionFactory<T> factory) {
            return new CodecBuilder<>(registrate, id, factory);
        }
    }

    public record CodecBuilder<T extends RecipeCondition<T>>(GTRegistrate registrate, ResourceLocation id,
                                                             ConditionFactory<T> factory) {

        public GTRegistryEntryBuilder<RecipeConditionType<T>> codec(MapCodec<T> codec) {
            return new GTRegistryEntryBuilder<>(registrate, GTRegistries.RECIPE_CONDITIONS, id,
                    new RecipeConditionType<>(factory, codec), "recipe.condition." + id.getPath() + ".tooltip");
        }
    }

    @FunctionalInterface
    public interface ConditionFactory<T extends RecipeCondition<T>> {

        T createDefault();
    }
}
