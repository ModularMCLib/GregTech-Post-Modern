package com.modularmc.gtpm.api.registry.registrate;

import com.modularmc.registrate.localization.LocalizationDeclaration;
import com.modularmc.registrate.localization.TranslationKey;

import net.minecraft.resources.ResourceKey;

import org.jspecify.annotations.Nullable;

import java.util.function.Consumer;

/** Declares a datapack registry key and its display name before the definition is bound by bootstrap. */
public final class DatapackKeyBuilder<T> {

    private final GTRegistrate registrate;
    private final ResourceKey<T> key;
    private @Nullable Consumer<LocalizationDeclaration.Builder> localization;

    public DatapackKeyBuilder(GTRegistrate registrate, ResourceKey<T> key) {
        this.registrate = registrate;
        this.key = key;
    }

    public DatapackKeyBuilder<T> localization(Consumer<LocalizationDeclaration.Builder> configure) {
        this.localization = configure;
        return this;
    }

    public ResourceKey<T> register() {
        if (localization != null) {
            var translationKey = new TranslationKey(registrate.getModid(),
                    key.location().toLanguageKey(key.registry().getPath()));
            registrate.addLocalization(translationKey, localization);
        }
        return key;
    }
}
