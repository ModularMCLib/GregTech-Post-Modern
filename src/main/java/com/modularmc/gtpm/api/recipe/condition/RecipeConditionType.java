package com.modularmc.gtpm.api.recipe.condition;

import com.modularmc.gtpm.api.recipe.RecipeCondition;

import com.mojang.serialization.MapCodec;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class RecipeConditionType<T extends RecipeCondition<T>> {

    public final ConditionFactory<T> factory;
    @Getter
    public final MapCodec<T> codec;

    @FunctionalInterface
    public interface ConditionFactory<T extends RecipeCondition<T>> {

        T createDefault();
    }
}
