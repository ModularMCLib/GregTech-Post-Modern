package com.modularmc.gtpm.api.data.worldgen.ores;

import net.minecraft.world.level.chunk.BulkSectionAccess;

@FunctionalInterface
public interface OreIndicatorPlacer {

    void placeIndicators(BulkSectionAccess access);
}
