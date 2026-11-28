package com.modularmc.gtpm.api.placeholder.exceptions;

import com.modularmc.registrate.localization.MinecraftLocale;
import com.modularmc.registrate.localization.annotation.Trans;
import com.modularmc.registrate.localization.annotation.Translation;

import net.minecraft.network.chat.Component;

@Trans(key = "gtpm.computer_monitor_cover.error.missing_item",
       source = "Missing %s in slot %d!",
       translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "插槽%2$d中缺少%1$s！"))
public class MissingItemException extends PlaceholderException {

    public MissingItemException(String item, int slot) {
        super(Component.translatable("gtpm.computer_monitor_cover.error.missing_item", item, slot).getString());
    }
}
