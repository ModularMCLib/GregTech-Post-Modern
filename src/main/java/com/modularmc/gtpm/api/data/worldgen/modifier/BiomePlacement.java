package com.modularmc.gtpm.api.data.worldgen.modifier;

import com.modularmc.gtpm.api.data.worldgen.BiomeWeightModifier;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.placement.PlacementContext;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;

import com.mojang.serialization.MapCodec;

import java.util.List;
import java.util.stream.Stream;

import static com.modularmc.gtpm.common.registry.GTRegistration.REGISTRATE;

public class BiomePlacement extends PlacementModifier {

    public static final PlacementModifierType<BiomePlacement> BIOME_PLACEMENT = REGISTRATE
            .<PlacementModifierType<BiomePlacement>>registryEntry("biome_placement",
                    BuiltInRegistries.PLACEMENT_MODIFIER_TYPE, () -> BiomePlacement.CODEC)
            .register();

    public static final MapCodec<BiomePlacement> CODEC = BiomeWeightModifier.CODEC.listOf().fieldOf("modifiers")
            .xmap(BiomePlacement::new, placement -> placement.modifiers);

    public final List<BiomeWeightModifier> modifiers;

    public BiomePlacement(List<BiomeWeightModifier> modifiers) {
        this.modifiers = modifiers;
    }

    @Override
    public Stream<BlockPos> getPositions(PlacementContext context, RandomSource random, BlockPos pos) {
        Stream<BlockPos> positions = Stream.of(pos);
        for (BiomeWeightModifier modifier : modifiers) {
            if (modifier.biomes.contains(context.getLevel().getBiome(pos))) {
                if (modifier.addedWeight < 100 && random.nextInt(100) >= modifier.addedWeight) {
                    return Stream.of();
                }
            }
        }
        return positions;
    }

    @Override
    public PlacementModifierType<?> type() {
        return BIOME_PLACEMENT;
    }
}
