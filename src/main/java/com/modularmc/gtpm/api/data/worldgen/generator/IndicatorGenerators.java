package com.modularmc.gtpm.api.data.worldgen.generator;

import com.modularmc.gtpm.GTPM;
import com.modularmc.gtpm.api.addon.AddonFinder;
import com.modularmc.gtpm.api.addon.IGTAddon;
import com.modularmc.gtpm.api.data.worldgen.WorldGeneratorUtils;
import com.modularmc.gtpm.api.data.worldgen.generator.indicators.NoopIndicatorGenerator;
import com.modularmc.gtpm.api.data.worldgen.generator.indicators.SurfaceIndicatorGenerator;

import net.minecraft.resources.ResourceLocation;

import com.mojang.serialization.MapCodec;

import java.util.function.Supplier;

public class IndicatorGenerators {

    public static final MapCodec<NoopIndicatorGenerator> NO_OP = register(GTPM.id("no_op"),
            NoopIndicatorGenerator.CODEC, () -> NoopIndicatorGenerator.INSTANCE);

    public static final MapCodec<SurfaceIndicatorGenerator> SURFACE = register(GTPM.id("surface"),
            SurfaceIndicatorGenerator.CODEC, SurfaceIndicatorGenerator::new);

    public static <T extends IndicatorGenerator> MapCodec<T> register(ResourceLocation id, MapCodec<T> codec,
                                                                      Supplier<T> function) {
        WorldGeneratorUtils.INDICATOR_GENERATORS.put(id, codec);
        WorldGeneratorUtils.INDICATOR_GENERATOR_FUNCTIONS.put(id, function);
        return codec;
    }

    public static void registerAddonGenerators() {
        AddonFinder.getAddonList().forEach(IGTAddon::registerIndicatorGenerators);
    }
}
