package com.modularmc.gtpm.common.data;

import com.modularmc.gtpm.api.data.worldgen.modifier.BiomePlacement;
import com.modularmc.gtpm.api.data.worldgen.modifier.DimensionFilter;
import com.modularmc.gtpm.api.data.worldgen.modifier.FrequencyModifier;
import com.modularmc.gtpm.common.worldgen.feature.FluidSproutFeature;
import com.modularmc.gtpm.common.worldgen.feature.StoneBlobFeature;
import com.modularmc.gtpm.common.worldgen.modifier.RubberTreeChancePlacement;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.neoforged.neoforge.registries.DeferredHolder;

import static com.modularmc.gtpm.common.registry.GTRegistration.REGISTRATE;

public class GTFeatures {

    public static final DeferredHolder<Feature<?>, StoneBlobFeature> STONE_BLOB = REGISTRATE
            .generic("stone_blob", Registries.FEATURE, StoneBlobFeature::new)
            .register();
    public static final DeferredHolder<Feature<?>, FluidSproutFeature> FLUID_SPROUT = REGISTRATE
            .generic("fluid_sprout", Registries.FEATURE, FluidSproutFeature::new)
            .register();

    public static void init() {
        Object inst = FrequencyModifier.FREQUENCY_MODIFIER; // seemingly useless access to init the class in time
        inst = DimensionFilter.DIMENSION_FILTER;
        inst = BiomePlacement.BIOME_PLACEMENT;
        inst = RubberTreeChancePlacement.RUBBER_TREE_CHANCE_PLACEMENT;
    }
}
