package com.modularmc.gtpm.api.registry.registrate;

import com.modularmc.gtpm.GTPM;
import com.modularmc.gtpm.api.registry.GTRegistries;
import com.modularmc.gtpm.api.sound.ConfiguredSoundEvent;
import com.modularmc.gtpm.api.sound.CustomSoundEntry;
import com.modularmc.gtpm.api.sound.SoundEntry;
import com.modularmc.gtpm.api.sound.WrappedSoundEntry;

import com.modularmc.registrate.localization.LocalizationDeclaration;
import com.modularmc.registrate.localization.TranslationKey;

import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;

import com.google.gson.JsonObject;
import org.jspecify.annotations.Nullable;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class SoundEntryBuilder {

    public static class SoundEntryProvider implements DataProvider {

        private final PackOutput output;
        private final String modId;

        public SoundEntryProvider(PackOutput output, String modId) {
            this.output = output;
            this.modId = modId;
        }

        @Override
        public CompletableFuture<?> run(CachedOutput cache) {
            return generate(output.getOutputFolder(PackOutput.Target.RESOURCE_PACK).resolve(modId), cache);
        }

        @Override
        public String getName() {
            return modId + "'s Custom Sounds";
        }

        public CompletableFuture<?> generate(Path path, CachedOutput cache) {
            JsonObject json = new JsonObject();
            try {
                for (SoundEntry sound : GTRegistries.SOUNDS) {
                    if (sound.getId().getNamespace().equals(modId)) sound.write(json);
                }
            } catch (Exception ignored) {}
            return DataProvider.saveStable(cache, json, path.resolve("sounds.json"));
        }
    }

    private final GTRegistrate registrate;
    private @Nullable Consumer<LocalizationDeclaration.Builder> localization;

    protected ResourceLocation id;
    protected boolean hasSubtitle = true;
    protected SoundSource category = SoundSource.BLOCKS;
    protected List<ConfiguredSoundEvent> wrappedEvents;
    protected List<ResourceLocation> variants;
    protected int attenuationDistance;

    public SoundEntryBuilder(GTRegistrate registrate, ResourceLocation id) {
        this.registrate = registrate;
        wrappedEvents = new ArrayList<>();
        variants = new ArrayList<>();
        this.id = id;
    }

    /** Configures the subtitle translation during data generation when subtitles are enabled for this sound. */
    public SoundEntryBuilder localization(Consumer<LocalizationDeclaration.Builder> configure) {
        this.localization = configure;
        return this;
    }

    public SoundEntryBuilder attenuationDistance(int distance) {
        this.attenuationDistance = distance;
        return this;
    }

    public SoundEntryBuilder noSubtitle() {
        this.hasSubtitle = false;
        return this;
    }

    public SoundEntryBuilder category(SoundSource category) {
        this.category = category;
        return this;
    }

    public SoundEntryBuilder addVariant(String name) {
        return addVariant(id.withPath(name));
    }

    public SoundEntryBuilder addVariant(ResourceLocation id) {
        variants.add(id);
        return this;
    }

    public SoundEntryBuilder playExisting(Supplier<SoundEvent> event, float volume, float pitch) {
        wrappedEvents.add(new ConfiguredSoundEvent(event, volume, pitch));
        return this;
    }

    public SoundEntryBuilder playExisting(SoundEvent event, float volume, float pitch) {
        return playExisting(() -> event, volume, pitch);
    }

    public SoundEntryBuilder playExisting(SoundEvent event) {
        return playExisting(event, 1, 1);
    }

    public SoundEntry build() {
        SoundEntry entry = wrappedEvents.isEmpty() ?
                new CustomSoundEntry(id, variants, hasSubtitle, category, attenuationDistance) :
                new WrappedSoundEntry(id, hasSubtitle, wrappedEvents, category, attenuationDistance);
        GTRegistries.register(GTRegistries.SOUNDS, entry.getId(), entry);
        if (localization != null && entry.hasSubtitle() && GTPM.isDataGen()) {
            registrate.addLocalization(new TranslationKey(registrate.getModid(), entry.getSubtitleKey()), localization);
        }
        return entry;
    }
}
