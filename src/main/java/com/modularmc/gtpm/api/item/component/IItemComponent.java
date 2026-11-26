package com.modularmc.gtpm.api.item.component;

import com.modularmc.gtpm.api.item.IComponentItem;

import net.minecraft.world.item.Item;

/**
 * Describes a generic component attachable to a {@link IComponentItem}
 * Multiple components can be attached to one item
 */
public interface IItemComponent {

    default void onAttached(Item item) {}
}
