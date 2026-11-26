package com.modularmc.gtpm.api.addon.events;

import com.modularmc.gtpm.api.data.chemical.material.Material;

import com.modularmc.registrate.util.entry.BlockEntry;

import net.minecraft.world.level.block.Block;

import com.google.common.collect.ImmutableMap;

public class MaterialCasingCollectionEvent {

    private final ImmutableMap.Builder<Material, BlockEntry<Block>> builder;

    public MaterialCasingCollectionEvent(ImmutableMap.Builder<Material, BlockEntry<Block>> builder) {
        this.builder = builder;
    }

    public void add(Material material, BlockEntry<Block> casingBlock) {
        builder.put(material, casingBlock);
    }
}
