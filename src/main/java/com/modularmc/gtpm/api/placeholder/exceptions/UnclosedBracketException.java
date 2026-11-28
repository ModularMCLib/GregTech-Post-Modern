package com.modularmc.gtpm.api.placeholder.exceptions;

import com.modularmc.registrate.localization.MinecraftLocale;
import com.modularmc.registrate.localization.annotation.Trans;
import com.modularmc.registrate.localization.annotation.Translation;

import net.minecraft.network.chat.Component;

@Trans(key = "gtpm.computer_monitor_cover.error.unclosed_bracket",
       source = "Unclosed bracket!",
       translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "括号未闭合！"))
public class UnclosedBracketException extends PlaceholderException {

    public UnclosedBracketException() {
        super(Component.translatable("gtpm.computer_monitor_cover.error.unclosed_bracket").getString());
    }
}
