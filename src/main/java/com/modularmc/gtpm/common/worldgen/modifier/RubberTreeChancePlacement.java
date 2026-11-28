package com.modularmc.gtpm.common.worldgen.modifier;

import com.modularmc.gtpm.config.GTPMConfigHolder;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import net.minecraft.world.level.levelgen.placement.RepeatingPlacement;

import com.mojang.serialization.MapCodec;
import org.jetbrains.annotations.NotNull;

import static com.modularmc.gtpm.common.registry.GTRegistration.REGISTRATE;

public class RubberTreeChancePlacement extends RepeatingPlacement {

    public static final PlacementModifierType<RubberTreeChancePlacement> RUBBER_TREE_CHANCE_PLACEMENT = REGISTRATE
            .<PlacementModifierType<RubberTreeChancePlacement>>registryEntry("rubber_tree_chance",
                    BuiltInRegistries.PLACEMENT_MODIFIER_TYPE, () -> RubberTreeChancePlacement.CODEC)
            .register();

    public static final RubberTreeChancePlacement INSTANCE = new RubberTreeChancePlacement();
    public static final MapCodec<RubberTreeChancePlacement> CODEC = MapCodec.unit(INSTANCE);

    @Override
    protected int count(RandomSource random, @NotNull BlockPos pos) {
        return random.nextFloat() < GTPMConfigHolder.INSTANCE.worldgen.rubberTreeSpawnChance ? 1 : 0;
    }

    @Override
    public PlacementModifierType<?> type() {
        return RUBBER_TREE_CHANCE_PLACEMENT;
    }
}
