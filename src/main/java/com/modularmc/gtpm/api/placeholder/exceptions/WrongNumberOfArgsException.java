package com.modularmc.gtpm.api.placeholder.exceptions;

import com.modularmc.registrate.localization.MinecraftLocale;
import com.modularmc.registrate.localization.annotation.Trans;
import com.modularmc.registrate.localization.annotation.Translation;

import net.minecraft.network.chat.Component;

@Trans(key = "gtpm.computer_monitor_cover.error.wrong_number_of_args",
       source = "Expected %d args, got %d!",
       translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "预期接收%d个参数，实际传入%d个！"))
public class WrongNumberOfArgsException extends PlaceholderException {

    public WrongNumberOfArgsException(int expected, int got) {
        super(Component.translatable("gtpm.computer_monitor_cover.error.wrong_number_of_args", expected, got)
                .getString());
    }
}
