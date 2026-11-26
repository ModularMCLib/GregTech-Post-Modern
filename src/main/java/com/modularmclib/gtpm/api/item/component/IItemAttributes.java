package com.modularmclib.gtpm.api.item.component;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;

public interface IItemAttributes {

    ItemAttributeModifiers getDefaultAttributeModifiers(ItemStack stack);
}
