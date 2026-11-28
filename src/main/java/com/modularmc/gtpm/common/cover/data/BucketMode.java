package com.modularmc.gtpm.common.cover.data;

import com.modularmc.gtpm.api.gui.GuiTextures;
import com.modularmc.gtpm.api.gui.SelectableEnum;
import com.modularmc.gtpm.api.gui.texture.IGuiTexture;

import com.modularmc.registrate.localization.MinecraftLocale;
import com.modularmc.registrate.localization.annotation.Trans;
import com.modularmc.registrate.localization.annotation.Translation;

import lombok.Getter;

public enum BucketMode implements SelectableEnum {

    @Trans(key = "cover.bucket.mode.bucket",
           source = "B",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "B"))
    BUCKET("cover.bucket.mode.bucket", "textures/item/water_bucket", 1000),
    @Trans(key = "cover.bucket.mode.milli_bucket",
           source = "mB",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "mB"))
    MILLI_BUCKET("cover.bucket.mode.milli_bucket", "gtpm:textures/gui/icon/bucket_mode/water_drop", 1);

    @Getter
    public final String tooltip;
    @Getter
    public final IGuiTexture icon;

    public final int multiplier;

    BucketMode(String tooltip, String textureName, int multiplier) {
        this.tooltip = tooltip;
        this.icon = GuiTextures.resource(textureName + ".png").scale(16F / 20F);
        this.multiplier = multiplier;
    }
}
