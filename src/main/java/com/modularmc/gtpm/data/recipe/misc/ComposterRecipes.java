package com.modularmc.gtpm.data.recipe.misc;

import com.modularmc.gtpm.common.data.GTBlocks;

import net.minecraft.world.item.Item;

import java.util.function.BiConsumer;

public class ComposterRecipes {

    // Add composter things here.
    public static void addComposterRecipes(BiConsumer<Item, Float> provider) {
        provider.accept(GTBlocks.RUBBER_SAPLING.asItem(), 0.3F);
    }
}
