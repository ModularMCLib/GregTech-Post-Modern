package com.modularmclib.gtpm.api.data.worldgen.generator;

import com.modularmclib.gtpm.GTPM;
import com.modularmclib.gtpm.api.addon.AddonFinder;
import com.modularmclib.gtpm.api.addon.IGTAddon;
import com.modularmclib.gtpm.api.data.worldgen.WorldGeneratorUtils;
import com.modularmclib.gtpm.api.data.worldgen.generator.veins.*;

import net.minecraft.resources.ResourceLocation;

import com.mojang.serialization.MapCodec;

import java.util.function.Supplier;

@SuppressWarnings("unused")
public class VeinGenerators {

    public static final MapCodec<NoopVeinGenerator> NO_OP = register(GTPM.id("no_op"), NoopVeinGenerator.CODEC,
            () -> NoopVeinGenerator.INSTANCE);

    public static final MapCodec<StandardVeinGenerator> STANDARD = register(GTPM.id("standard"),
            StandardVeinGenerator.CODEC, StandardVeinGenerator::new);
    public static final MapCodec<LayeredVeinGenerator> LAYER = register(GTPM.id("layer"), LayeredVeinGenerator.CODEC,
            LayeredVeinGenerator::new);
    public static final MapCodec<GeodeVeinGenerator> GEODE = register(GTPM.id("geode"), GeodeVeinGenerator.CODEC,
            GeodeVeinGenerator::new);
    public static final MapCodec<DikeVeinGenerator> DIKE = register(GTPM.id("dike"), DikeVeinGenerator.CODEC,
            DikeVeinGenerator::new);
    public static final MapCodec<VeinedVeinGenerator> VEINED = register(GTPM.id("veined"), VeinedVeinGenerator.CODEC,
            VeinedVeinGenerator::new);
    public static final MapCodec<ClassicVeinGenerator> CLASSIC = register(GTPM.id("classic"),
            ClassicVeinGenerator.CODEC, ClassicVeinGenerator::new);
    public static final MapCodec<CuboidVeinGenerator> CUBOID = register(GTPM.id("cuboid"), CuboidVeinGenerator.CODEC,
            CuboidVeinGenerator::new);

    public static <
            T extends VeinGenerator> MapCodec<T> register(ResourceLocation id, MapCodec<T> codec,
                                                          Supplier<? extends VeinGenerator> function) {
        WorldGeneratorUtils.VEIN_GENERATORS.put(id, codec);
        WorldGeneratorUtils.VEIN_GENERATOR_FUNCTIONS.put(id, function);
        return codec;
    }

    public static void registerAddonGenerators() {
        AddonFinder.getAddonList().forEach(IGTAddon::registerVeinGenerators);
    }
}
