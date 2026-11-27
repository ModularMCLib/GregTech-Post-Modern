package com.modularmc.gtpm.api.fluids.store;

import com.modularmc.gtpm.GTPM;
import com.modularmc.gtpm.api.data.chemical.material.Material;
import com.modularmc.gtpm.api.data.chemical.material.info.MaterialIconType;
import com.modularmc.gtpm.api.data.chemical.material.properties.FluidProperty;
import com.modularmc.gtpm.api.data.chemical.material.properties.PropertyKey;
import com.modularmc.gtpm.api.fluids.FluidState;
import com.modularmc.gtpm.common.registry.GTRegistration;
import com.modularmc.gtpm.data.recipe.CustomTags;

import com.modularmc.registrate.localization.LocalizationDeclaration;
import com.modularmc.registrate.localization.MinecraftLocale;
import com.modularmc.registrate.localization.TranslationKey;

import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public final class FluidStorageKeys {

    private static final Object2ObjectMap<String, LocalizationDeclaration> NAMES = new Object2ObjectOpenHashMap<>(6);
    private static final String GENERIC_NAME = localizeName("gtpm.fluid.generic", text -> text
            .source("%s").translation(MinecraftLocale.ZH_CN, "%s"));
    private static final String LIQUID_NAME = localizeName("gtpm.fluid.liquid_generic", text -> text
            .source("Liquid %s").translation(MinecraftLocale.ZH_CN, "液态%s"));
    private static final String GAS_NAME = localizeName("gtpm.fluid.gas_generic", text -> text
            .source("%s Gas").translation(MinecraftLocale.ZH_CN, "气态%s"));
    private static final String VAPOR_NAME = localizeName("gtpm.fluid.gas_vapor", text -> text
            .source("%s Vapor").translation(MinecraftLocale.ZH_CN, "%s蒸气"));
    private static final String MOLTEN_NAME = localizeName("gtpm.fluid.molten", text -> text
            .source("Molten %s").translation(MinecraftLocale.ZH_CN, "熔融%s"));
    private static final String PLASMA_NAME = localizeName("gtpm.fluid.plasma", text -> text
            .source("%s Plasma").translation(MinecraftLocale.ZH_CN, "%s等离子体"));

    public static final FluidStorageKey LIQUID = new FluidStorageKey(GTPM.id("liquid"),
            MaterialIconType.liquid,
            m -> prefixedRegisteredName("liquid_", FluidStorageKeys.LIQUID, m),
            m -> m.hasProperty(PropertyKey.DUST) ? LIQUID_NAME : GENERIC_NAME,
            FluidState.LIQUID, 0);

    public static final FluidStorageKey GAS = new FluidStorageKey(GTPM.id("gas"),
            MaterialIconType.gas,
            m -> postfixedRegisteredName("_gas", FluidStorageKeys.GAS, m),
            m -> {
                if (m.hasProperty(PropertyKey.DUST)) {
                    return VAPOR_NAME;
                }
                if (m.isElement()) {
                    FluidProperty property = m.getProperty(PropertyKey.FLUID);
                    if (m.isElement() || (property != null && property.getPrimaryKey() != FluidStorageKeys.LIQUID)) {
                        return GAS_NAME;
                    }
                }
                return GENERIC_NAME;
            },
            FluidState.GAS, 0);

    public static final FluidStorageKey PLASMA = new FluidStorageKey(GTPM.id("plasma"),
            MaterialIconType.plasma,
            m -> m.getName() + "_plasma",
            m -> PLASMA_NAME,
            FluidState.PLASMA, -1);

    public static final FluidStorageKey MOLTEN = new FluidStorageKey(GTPM.id("molten"), CustomTags.MOLTEN_FLUIDS,
            MaterialIconType.molten,
            m -> "molten_" + m.getName(),
            m -> MOLTEN_NAME,
            FluidState.LIQUID, -1);

    private FluidStorageKeys() {}

    private static String localizeName(String key, Consumer<LocalizationDeclaration.Builder> configure) {
        var text = new LocalizationDeclaration.Builder();
        configure.accept(text);
        NAMES.put(key, text.build());
        GTRegistration.REGISTRATE.addLocalization(new TranslationKey(GTPM.MOD_ID, key), configure);
        return key;
    }

    public static LocalizationDeclaration getGenericNameLocalization() {
        return getNameLocalization(GENERIC_NAME);
    }

    public static LocalizationDeclaration getNameLocalization(String key) {
        var name = NAMES.get(key);
        if (name == null) {
            throw new IllegalArgumentException("Unknown material fluid name: " + key);
        }
        return name;
    }

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
