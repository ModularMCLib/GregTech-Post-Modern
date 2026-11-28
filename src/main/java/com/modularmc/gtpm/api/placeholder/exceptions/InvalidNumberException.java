package com.modularmc.gtpm.api.placeholder.exceptions;

import com.modularmc.registrate.localization.MinecraftLocale;
import com.modularmc.registrate.localization.annotation.Trans;
import com.modularmc.registrate.localization.annotation.Translation;

import net.minecraft.network.chat.Component;

@Trans(key = "gtpm.computer_monitor_cover.error.invalid_number",
       source = "Invalid number '%s'!",
       translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "无效的数字'%s'！"))
public class InvalidNumberException extends PlaceholderException {

    public InvalidNumberException(String number) {
        super(Component.translatable("gtpm.computer_monitor_cover.error.invalid_number", number).getString());
    }
}
