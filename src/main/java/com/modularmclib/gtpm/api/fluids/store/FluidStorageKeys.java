package com.modularmclib.gtpm.api.fluids.store;

import com.modularmclib.gtpm.GTPM;
import com.modularmclib.gtpm.api.data.chemical.material.Material;
import com.modularmclib.gtpm.api.data.chemical.material.info.MaterialIconType;
import com.modularmclib.gtpm.api.data.chemical.material.properties.FluidProperty;
import com.modularmclib.gtpm.api.data.chemical.material.properties.PropertyKey;
import com.modularmclib.gtpm.api.fluids.FluidState;
import com.modularmclib.gtpm.data.recipe.CustomTags;

import org.jetbrains.annotations.NotNull;

public final class FluidStorageKeys {

    public static final FluidStorageKey LIQUID = new FluidStorageKey(GTPM.id("liquid"),
            MaterialIconType.liquid,
            m -> prefixedRegisteredName("liquid_", FluidStorageKeys.LIQUID, m),
            m -> m.hasProperty(PropertyKey.DUST) ? "gtpm.fluid.liquid_generic" : "gtpm.fluid.generic",
            FluidState.LIQUID, 0);

    public static final FluidStorageKey GAS = new FluidStorageKey(GTPM.id("gas"),
            MaterialIconType.gas,
            m -> postfixedRegisteredName("_gas", FluidStorageKeys.GAS, m),
            m -> {
                if (m.hasProperty(PropertyKey.DUST)) {
                    return "gtpm.fluid.gas_vapor";
                }
                if (m.isElement()) {
                    FluidProperty property = m.getProperty(PropertyKey.FLUID);
                    if (m.isElement() || (property != null && property.getPrimaryKey() != FluidStorageKeys.LIQUID)) {
                        return "gtpm.fluid.gas_generic";
                    }
                }
                return "gtpm.fluid.generic";
            },
            FluidState.GAS, 0);

    public static final FluidStorageKey PLASMA = new FluidStorageKey(GTPM.id("plasma"),
            MaterialIconType.plasma,
            m -> m.getName() + "_plasma",
            m -> "gtpm.fluid.plasma",
            FluidState.PLASMA, -1);

    public static final FluidStorageKey MOLTEN = new FluidStorageKey(GTPM.id("molten"), CustomTags.MOLTEN_FLUIDS,
            MaterialIconType.molten,
            m -> "molten_" + m.getName(),
            m -> "gtpm.fluid.molten",
            FluidState.LIQUID, -1);

    private FluidStorageKeys() {}

    private static @NotNull String prefixedRegisteredName(@NotNull String prefix, @NotNull FluidStorageKey key,
                                                          @NotNull Material material) {
        FluidProperty property = material.getProperty(PropertyKey.FLUID);
        if (property != null && property.getPrimaryKey() != key) {
            return prefix + material.getName();
        }
        return material.getName();
    }

    private static @NotNull String postfixedRegisteredName(@NotNull String postfix, @NotNull FluidStorageKey key,
                                                           @NotNull Material material) {
        FluidProperty property = material.getProperty(PropertyKey.FLUID);
        if (property != null && property.getPrimaryKey() != key) {
            return material.getName() + postfix;
        }
        return material.getName();
    }
}
