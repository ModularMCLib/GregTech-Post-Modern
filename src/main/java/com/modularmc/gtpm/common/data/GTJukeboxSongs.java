package com.modularmc.gtpm.common.data;

import com.modularmc.gtpm.GTPM;

import com.modularmc.registrate.localization.MinecraftLocale;
import com.modularmc.registrate.localization.annotation.Trans;
import com.modularmc.registrate.localization.annotation.Translation;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.JukeboxSong;

@Trans(key = "item.gtpm.sus_record.desc",
       source = "§7sussy!",
       translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7可疑！"))
public class GTJukeboxSongs {

    @Trans(key = "metaitem.record.sus.tooltip",
           source = "§7Leonz - Among Us Drip",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7Leonz - Among Us Drip"))
    public static final ResourceKey<JukeboxSong> SUS = ResourceKey.create(Registries.JUKEBOX_SONG, GTPM.id("sus"));

    public static void bootstrap(BootstrapContext<JukeboxSong> ctx) {
        ctx.register(SUS, new JukeboxSong(
                BuiltInRegistries.SOUND_EVENT.wrapAsHolder(GTSoundEntries.SUS_RECORD.getMainEvent()),
                Component.translatable("item.gtpm.sus_record.desc"),
                820, 1));
    }
}
