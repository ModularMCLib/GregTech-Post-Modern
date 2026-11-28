package com.modularmc.gtpm.common.data;

import com.modularmc.gtpm.data.recipe.CustomTags;

import net.minecraft.core.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.common.world.BiomeModifiers;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import static com.modularmc.gtpm.common.registry.GTRegistration.REGISTRATE;

public class GTBiomeModifiers {

    public static final ResourceKey<BiomeModifier> RUBBER_TREE = REGISTRATE
            .datapackKey(NeoForgeRegistries.Keys.BIOME_MODIFIERS, "rubber_tree").register();
    public static final ResourceKey<BiomeModifier> RUBBER_TREE_SWAMP = REGISTRATE
            .datapackKey(NeoForgeRegistries.Keys.BIOME_MODIFIERS, "rubber_tree_swamp").register();
    public static final ResourceKey<BiomeModifier> STONE_BLOB = REGISTRATE
            .datapackKey(NeoForgeRegistries.Keys.BIOME_MODIFIERS, "stone_blob").register();
    public static final ResourceKey<BiomeModifier> RAW_OIL_SPROUT = REGISTRATE
            .datapackKey(NeoForgeRegistries.Keys.BIOME_MODIFIERS, "raw_oil_sprout").register();

    public static void bootstrap(BootstrapContext<BiomeModifier> ctx) {
        HolderGetter<Biome> biomeLookup = ctx.lookup(Registries.BIOME);
        HolderGetter<PlacedFeature> placedFeatureRegistry = ctx.lookup(Registries.PLACED_FEATURE);

        ctx.register(RUBBER_TREE, new BiomeModifiers.AddFeaturesBiomeModifier(
                biomeLookup.getOrThrow(CustomTags.HAS_RUBBER_TREE),
                HolderSet.direct(placedFeatureRegistry.getOrThrow(GTPlacements.RUBBER_TREE)),
                GenerationStep.Decoration.VEGETAL_DECORATION));
        ctx.register(RUBBER_TREE_SWAMP, new BiomeModifiers.AddFeaturesBiomeModifier(
                biomeLookup.getOrThrow(Tags.Biomes.IS_SWAMP),
                HolderSet.direct(placedFeatureRegistry.getOrThrow(GTPlacements.RUBBER_TREE_SWAMP)),
                GenerationStep.Decoration.VEGETAL_DECORATION));

        HolderSet<Biome> overworld = biomeLookup.getOrThrow(BiomeTags.IS_OVERWORLD);
        Holder<PlacedFeature> redGraniteBlob = placedFeatureRegistry.getOrThrow(GTPlacements.RED_GRANITE_BLOB);
        Holder<PlacedFeature> marbleBlob = placedFeatureRegistry.getOrThrow(GTPlacements.MARBLE_BLOB);
        ctx.register(STONE_BLOB, new BiomeModifiers.AddFeaturesBiomeModifier(
                overworld,
                HolderSet.direct(redGraniteBlob, marbleBlob),
                GenerationStep.Decoration.UNDERGROUND_ORES));

        Holder<PlacedFeature> rawOilSprout = placedFeatureRegistry.getOrThrow(GTPlacements.RAW_OIL_SPROUT);
        ctx.register(RAW_OIL_SPROUT, new BiomeModifiers.AddFeaturesBiomeModifier(
                overworld,
                HolderSet.direct(rawOilSprout),
                GenerationStep.Decoration.FLUID_SPRINGS));
    }
}
