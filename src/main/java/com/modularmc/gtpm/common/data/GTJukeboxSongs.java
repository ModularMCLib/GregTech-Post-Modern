package com.modularmc.gtpm.common.data;

import com.modularmc.registrate.localization.MinecraftLocale;
import com.modularmc.registrate.localization.annotation.Trans;
import com.modularmc.registrate.localization.annotation.Translation;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.JukeboxSong;

import static com.modularmc.gtpm.common.registry.GTRegistration.REGISTRATE;

public class GTJukeboxSongs {

    @Trans(key = "metaitem.record.sus.tooltip",
           source = "§7Leonz - Among Us Drip",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7Leonz - Among Us Drip"))
    public static final ResourceKey<JukeboxSong> SUS = REGISTRATE.jukeboxSong("sus", "sus_record")
            .localization(text -> text.source("§7sussy!").translation(MinecraftLocale.ZH_CN, "§7可疑！"))
            .register();

    public static void init() {}

    public static void bootstrap(BootstrapContext<JukeboxSong> ctx) {
        ctx.register(SUS, new JukeboxSong(
                BuiltInRegistries.SOUND_EVENT.wrapAsHolder(GTSoundEntries.SUS_RECORD.getMainEvent()),
                Component.translatable("item.gtpm.sus_record.desc"),
                820, 1));
    }
}
