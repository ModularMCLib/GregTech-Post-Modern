package com.modularmc.gtpm.api.registry.registrate.provider;

import com.modularmc.gtpm.api.registry.registrate.GTRegistrate;

import com.modularmc.registrate.localization.DeclarationDefaults;
import com.modularmc.registrate.localization.LocalizationDeclaration;
import com.modularmc.registrate.localization.TranslationKey;
import com.modularmc.registrate.providers.RegistrateLangProvider;

import net.minecraft.data.PackOutput;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;

/** Stages language callbacks in order so an addon can replace its generated defaults. */
public final class GTLanguageProvider extends RegistrateLangProvider {

    private final Map<String, Consumer<RegistrateLangProvider>> submissions = new LinkedHashMap<>();

    public GTLanguageProvider(GTRegistrate owner, PackOutput output) {
        super(owner, output);
    }

    @Override
    public void add(String key, String value) {
        submissions.put(key, provider -> provider.add(key, value));
    }

    public static void addLocalization(RegistrateLangProvider provider, GTRegistrate owner, String key,
                                       Consumer<LocalizationDeclaration.Builder> configure) {
        var translationKey = new TranslationKey(owner.getModid(), key);
        if (provider instanceof GTLanguageProvider staged) {
            staged.submissions.put(key, output -> owner.addLocalization(translationKey, configure));
        } else {
            owner.addLocalization(translationKey, configure);
        }
    }

    public static void addLocalization(RegistrateLangProvider provider, GTRegistrate owner, String key,
                                       LocalizationDeclaration declaration) {
        var defaults = new DeclarationDefaults(declaration.domains(), declaration.type(), declaration.glossary(),
                declaration.glossaryIds(), declaration.unitIds());
        addLocalization(provider, owner, key, text -> {
            text.source(declaration.source()).defaults(defaults);
            declaration.translations().forEach(text::translation);
        });
    }

    public void submitTo(RegistrateLangProvider provider) {
        submissions.values().forEach(submit -> submit.accept(provider));
    }
}
