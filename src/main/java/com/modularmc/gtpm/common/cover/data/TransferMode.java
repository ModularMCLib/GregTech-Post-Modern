package com.modularmc.gtpm.common.cover.data;

import com.modularmc.gtpm.api.gui.GuiTextures;
import com.modularmc.gtpm.api.gui.SelectableEnum;
import com.modularmc.gtpm.api.gui.texture.IGuiTexture;

import com.modularmc.registrate.localization.MinecraftLocale;
import com.modularmc.registrate.localization.annotation.Trans;
import com.modularmc.registrate.localization.annotation.Translation;

import lombok.Getter;

public enum TransferMode implements SelectableEnum {

    @Trans(key = "cover.robotic_arm.transfer_mode.transfer_any",
           source = "Transfer Any",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "任意传输"))
    TRANSFER_ANY("cover.robotic_arm.transfer_mode.transfer_any", "transfer_any", 1),
    @Trans(key = "cover.robotic_arm.transfer_mode.transfer_exact",
           source = "Supply Exact",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "精确补给"))
    TRANSFER_EXACT("cover.robotic_arm.transfer_mode.transfer_exact", "transfer_exact", 1024),
    @Trans(key = "cover.robotic_arm.transfer_mode.keep_exact",
           source = "Keep Exact",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "保持补给"))
    KEEP_EXACT("cover.robotic_arm.transfer_mode.keep_exact", "keep_exact", 1024);

    @Getter
    public final String tooltip;
    @Getter
    public final IGuiTexture icon;
    public final int maxStackSize;

    TransferMode(String tooltip, String textureName, int maxStackSize) {
        this.tooltip = tooltip;
        this.maxStackSize = maxStackSize;
        this.icon = GuiTextures.resource("gtpm:textures/gui/icon/transfer_mode/" + textureName + ".png");
    }
}
