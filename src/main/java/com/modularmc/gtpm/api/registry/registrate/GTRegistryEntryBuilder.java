package com.modularmc.gtpm.api.registry.registrate;

import com.modularmc.gtpm.api.registry.GTRegistries;

import com.modularmc.registrate.localization.LocalizationDeclaration;
import com.modularmc.registrate.localization.TranslationKey;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;

import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.Nullable;

import java.util.function.Consumer;

/** Registers a definition that other GT declarations can reference before the registry event. */
public final class GTRegistryEntryBuilder<T> {

    private final GTRegistrate registrate;
    private final Registry<? super T> registry;
    private final ResourceLocation id;
    private final T value;
    private final String translationKey;
    private @Nullable Object2ObjectArrayMap<String, Consumer<LocalizationDeclaration.Builder>> localizations;

    @ApiStatus.Internal
    public GTRegistryEntryBuilder(GTRegistrate registrate, Registry<? super T> registry, ResourceLocation id, T value,
                                  String translationKey) {
        this.registrate = registrate;
        this.registry = registry;
        this.id = id;
        this.value = value;
        this.translationKey = translationKey;
    }

    public GTRegistryEntryBuilder<T> configure(Consumer<? super T> configure) {
        configure.accept(value);
        return this;
    }

    public GTRegistryEntryBuilder<T> localization(Consumer<LocalizationDeclaration.Builder> configure) {
        return localization(translationKey, configure);
    }

    public GTRegistryEntryBuilder<T> localization(String key, Consumer<LocalizationDeclaration.Builder> configure) {
        if (localizations == null) {
            localizations = new Object2ObjectArrayMap<>(2);
        }
        localizations.put(key, configure);
        return this;
    }

    public T register() {
        GTRegistries.register(registry, id, value);
        if (localizations != null) {
            localizations.forEach((key, configure) -> registrate.addLocalization(
                    new TranslationKey(registrate.getModid(), key), configure));
        }
        return value;
    }
}
