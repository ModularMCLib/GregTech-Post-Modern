package com.modularmc.gtpm.api.data.worldgen.bedrockore;

import com.modularmc.gtpm.api.data.chemical.material.Material;
import com.modularmc.gtpm.api.registry.GTRegistries;
import com.modularmc.gtpm.api.sync_system.FieldCodecs;
import com.modularmc.gtpm.utils.WeightedEntry;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record WeightedMaterial(Material material, int weight) implements WeightedEntry {

    public static final Codec<WeightedMaterial> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    GTRegistries.MATERIALS.byNameCodec().fieldOf("material").forGetter(WeightedMaterial::material),
                    Codec.INT.fieldOf("weight").forGetter(WeightedMaterial::weight))
                    .apply(instance, WeightedMaterial::new));

    static {
        FieldCodecs.register(WeightedMaterial.class, CODEC);
    }
}
