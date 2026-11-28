package com.modularmc.gtpm.api.placeholder.exceptions;

import com.modularmc.registrate.localization.MinecraftLocale;
import com.modularmc.registrate.localization.annotation.Trans;
import com.modularmc.registrate.localization.annotation.Translation;

import net.minecraft.network.chat.Component;

@Trans(key = "gtpm.computer_monitor_cover.error.no_placeholder",
       source = "No such placeholder: '%s'!",
       translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "不存在占位符“%s”！"))
public class UnknownPlaceholderException extends PlaceholderException {

    public UnknownPlaceholderException(String name) {
        super(Component.translatable("gtpm.computer_monitor_cover.error.no_placeholder", name).getString());
    }
}
