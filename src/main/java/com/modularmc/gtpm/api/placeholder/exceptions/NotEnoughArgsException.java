package com.modularmc.gtpm.api.placeholder.exceptions;

import com.modularmc.registrate.localization.MinecraftLocale;
import com.modularmc.registrate.localization.annotation.Trans;
import com.modularmc.registrate.localization.annotation.Translation;

import net.minecraft.network.chat.Component;

@Trans(key = "gtpm.computer_monitor_cover.error.not_enough_args",
       source = "Expected at least %d args, got %d!",
       translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "至少需要%d个参数，但实际只有%d个！"))
public class NotEnoughArgsException extends PlaceholderException {

    public NotEnoughArgsException(int expected, int got) {
        super(Component.translatable("gtpm.computer_monitor_cover.error.not_enough_args", expected, got).getString());
    }
}
