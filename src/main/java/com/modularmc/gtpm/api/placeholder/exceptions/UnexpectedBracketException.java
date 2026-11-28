package com.modularmc.gtpm.api.placeholder.exceptions;

import com.modularmc.registrate.localization.MinecraftLocale;
import com.modularmc.registrate.localization.annotation.Trans;
import com.modularmc.registrate.localization.annotation.Translation;

import net.minecraft.network.chat.Component;

@Trans(key = "gtpm.computer_monitor_cover.error.unexpected_bracket",
       source = "Unexpected closing bracket!",
       translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "意外的右括号！"))
public class UnexpectedBracketException extends RuntimeException {

    public UnexpectedBracketException() {
        super(Component.translatable("gtpm.computer_monitor_cover.error.unexpected_bracket").getString());
    }
}
