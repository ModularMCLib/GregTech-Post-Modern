package com.modularmc.gtpm.api.capability.recipe;

import com.modularmc.gtpm.api.gui.GuiTextures;
import com.modularmc.gtpm.api.gui.SelectableEnum;
import com.modularmc.gtpm.api.gui.texture.IGuiTexture;

import com.modularmc.registrate.localization.MinecraftLocale;
import com.modularmc.registrate.localization.annotation.Trans;
import com.modularmc.registrate.localization.annotation.Translation;

import lombok.Getter;

/**
 * The capability can be input or output or both
 */
public enum IO implements SelectableEnum {

    @Trans(key = "gtpm.io.import",
           source = "Import",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "输入"))
    IN("gtpm.io.import", "import"),
    @Trans(key = "gtpm.io.export",
           source = "Export",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "输出"))
    OUT("gtpm.io.export", "export"),
    @Trans(key = "gtpm.io.both",
           source = "Both",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "同时"))
    BOTH("gtpm.io.both", "both"),
    @Trans(key = "gtpm.io.none",
           source = "None",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "无"))
    NONE("gtpm.io.none", "none");

    @Getter
    public final String tooltip;
    @Getter
    public final IGuiTexture icon;

    IO(String tooltip, String textureName) {
        this.tooltip = tooltip;
        this.icon = GuiTextures.resource("gtpm:textures/gui/icon/io_mode/" + textureName + ".png");
    }

    public boolean support(IO io) {
        if (io == this) return true;
        if (io == NONE) return false;
        return this == BOTH;
    }
}
