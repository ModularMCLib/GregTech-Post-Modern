package com.modularmc.gtpm.api.placeholder.exceptions;

import com.modularmc.registrate.localization.MinecraftLocale;
import com.modularmc.registrate.localization.annotation.Trans;
import com.modularmc.registrate.localization.annotation.Translation;

import net.minecraft.network.chat.Component;

@Trans(key = "gtpm.computer_monitor_cover.error.invalid_args",
       source = "Invalid arguments!",
       translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "无效参数！"))
public class InvalidArgsException extends PlaceholderException {

    public InvalidArgsException() {
        super(Component.translatable("gtpm.computer_monitor_cover.error.invalid_args").getString());
    }
}
