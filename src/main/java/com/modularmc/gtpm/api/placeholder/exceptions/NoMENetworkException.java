package com.modularmc.gtpm.api.placeholder.exceptions;

import com.modularmc.registrate.localization.MinecraftLocale;
import com.modularmc.registrate.localization.annotation.Trans;
import com.modularmc.registrate.localization.annotation.Translation;

import net.minecraft.network.chat.Component;

@Trans(key = "gtpm.computer_monitor_cover.error.no_ae",
       source = "Cover holder does not have an AE2 network!",
       translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "覆盖板支架未接入AE2网络！"))
public class NoMENetworkException extends PlaceholderException {

    public NoMENetworkException() {
        super(Component.translatable("gtpm.computer_monitor_cover.error.no_ae").getString());
    }
}
