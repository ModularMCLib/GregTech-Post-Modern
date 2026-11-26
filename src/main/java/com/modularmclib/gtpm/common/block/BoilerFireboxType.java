package com.modularmclib.gtpm.common.block;

import com.modularmclib.gtpm.GTPM;
import com.modularmclib.gtpm.api.sync_system.FieldCodecs;

import net.minecraft.resources.ResourceLocation;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public record BoilerFireboxType(String name, ResourceLocation bottom, ResourceLocation top, ResourceLocation side) {

    public static final Map<String, BoilerFireboxType> FIREBOX_TYPES = new HashMap<>();
    public static final Codec<BoilerFireboxType> CODEC = Codec.STRING.comapFlatMap(name -> {
        BoilerFireboxType type = FIREBOX_TYPES.get(name);
        if (type != null) {
            return DataResult.success(type);
        } else {
            return DataResult.error(() -> "A firebox type named " + name + " does not exist");
        }
    }, BoilerFireboxType::name);

    static {
        FieldCodecs.register(BoilerFireboxType.class, CODEC);
    }

    public BoilerFireboxType {
        FIREBOX_TYPES.put(name, this);
    }

    public static BoilerFireboxType BRONZE_FIREBOX = new BoilerFireboxType("bronze_firebox",
            GTPM.id("block/casings/solid/machine_casing_bronze_plated_bricks"),
            GTPM.id("block/casings/solid/machine_casing_bronze_plated_bricks"),
            GTPM.id("block/casings/firebox/machine_casing_firebox_bronze"));
    public static BoilerFireboxType STEEL_FIREBOX = new BoilerFireboxType("steel_firebox",
            GTPM.id("block/casings/solid/machine_casing_solid_steel"),
            GTPM.id("block/casings/solid/machine_casing_solid_steel"),
            GTPM.id("block/casings/firebox/machine_casing_firebox_steel"));
    public static BoilerFireboxType TITANIUM_FIREBOX = new BoilerFireboxType("titanium_firebox",
            GTPM.id("block/casings/solid/machine_casing_stable_titanium"),
            GTPM.id("block/casings/solid/machine_casing_stable_titanium"),
            GTPM.id("block/casings/firebox/machine_casing_firebox_titanium"));
    public static BoilerFireboxType TUNGSTENSTEEL_FIREBOX = new BoilerFireboxType("tungstensteel_firebox",
            GTPM.id("block/casings/solid/machine_casing_robust_tungstensteel"),
            GTPM.id("block/casings/solid/machine_casing_robust_tungstensteel"),
            GTPM.id("block/casings/firebox/machine_casing_firebox_tungstensteel"));

    @NotNull
    @Override
    public String toString() {
        return name();
    }
}
