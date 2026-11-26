package com.modularmclib.gtpm.api.data.worldgen.bedrockore;

import com.modularmclib.gtpm.api.data.chemical.material.Material;
import com.modularmclib.gtpm.api.registry.GTRegistries;
import com.modularmclib.gtpm.api.sync_system.FieldCodecs;
import com.modularmclib.gtpm.utils.WeightedEntry;

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
