package com.modularmc.gtpm.api.item.component;

import net.minecraft.world.item.ItemStack;

@FunctionalInterface
public interface IRecipeRemainder extends IItemComponent {

    ItemStack getRecipeRemained(ItemStack itemStack);
}
