package com.modularmc.gtpm.api.placeholder.exceptions;

import com.modularmc.registrate.localization.MinecraftLocale;
import com.modularmc.registrate.localization.annotation.Trans;
import com.modularmc.registrate.localization.annotation.Translation;

import net.minecraft.network.chat.Component;

@Trans(key = "gtpm.computer_monitor_cover.error.not_in_range",
       source = "Expected %s to be between %d and %d (inclusive), got %d",
       translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "期望%s在%d和%d之间（含边界），但得到%d"))
public class OutOfRangeException extends PlaceholderException {

    public OutOfRangeException(String what, int min, int max, int provided) {
        super(Component.translatable("gtpm.computer_monitor_cover.error.not_in_range", what, min, max, provided)
                .getString());
    }
}
