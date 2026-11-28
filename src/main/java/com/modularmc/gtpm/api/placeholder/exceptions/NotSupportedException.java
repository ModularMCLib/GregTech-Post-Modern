package com.modularmc.gtpm.api.placeholder.exceptions;

import com.modularmc.registrate.localization.MinecraftLocale;
import com.modularmc.registrate.localization.annotation.Trans;
import com.modularmc.registrate.localization.annotation.Translation;

import net.minecraft.network.chat.Component;

@Trans(key = "gtpm.computer_monitor_cover.error.not_supported",
       source = "This feature is not supported by this block/cover!",
       translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "该方块/覆盖板不支持此功能！"))
public class NotSupportedException extends PlaceholderException {

    public NotSupportedException() {
        super(Component.translatable("gtpm.computer_monitor_cover.error.not_supported").getString());
    }
}
