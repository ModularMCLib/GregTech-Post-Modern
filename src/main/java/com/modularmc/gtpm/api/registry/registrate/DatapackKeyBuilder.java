package com.modularmc.gtpm.api.registry.registrate;

import com.modularmc.registrate.localization.LocalizationDeclaration;
import com.modularmc.registrate.localization.TranslationKey;

import net.minecraft.resources.ResourceKey;

import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.Nullable;

import java.util.function.Consumer;

/** Declares a datapack registry key and its display name before the definition is bound by bootstrap. */
public final class DatapackKeyBuilder<T> {

    private final GTRegistrate registrate;
    private final ResourceKey<T> key;
    private final String translationKey;
    private @Nullable Object2ObjectArrayMap<String, Consumer<LocalizationDeclaration.Builder>> localizations;

    public DatapackKeyBuilder(GTRegistrate registrate, ResourceKey<T> key) {
        this(registrate, key, key.location().toLanguageKey(key.registry().getPath()));
    }

    @ApiStatus.Internal
    public DatapackKeyBuilder(GTRegistrate registrate, ResourceKey<T> key, String translationKey) {
        this.registrate = registrate;
        this.key = key;
        this.translationKey = translationKey;
    }

    public DatapackKeyBuilder<T> localization(Consumer<LocalizationDeclaration.Builder> configure) {
        return addLocalization(translationKey, configure);
    }

    public DatapackKeyBuilder<T> localizationVariant(String suffix, Consumer<LocalizationDeclaration.Builder> configure) {
        return addLocalization(translationKey + "." + suffix, configure);
    }

    private DatapackKeyBuilder<T> addLocalization(String key, Consumer<LocalizationDeclaration.Builder> configure) {
        if (localizations == null) {
            localizations = new Object2ObjectArrayMap<>(2);
        }
        localizations.put(key, configure);
        return this;
    }

    public ResourceKey<T> register() {
        if (localizations != null) {
            localizations.forEach((translationKey, configure) -> registrate.addLocalization(
                    new TranslationKey(registrate.getModid(), translationKey), configure));
        }
        return key;
    }
}
