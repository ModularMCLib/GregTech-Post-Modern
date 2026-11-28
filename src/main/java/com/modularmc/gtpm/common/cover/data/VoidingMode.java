package com.modularmc.gtpm.common.cover.data;

import com.modularmc.gtpm.api.gui.GuiTextures;
import com.modularmc.gtpm.api.gui.SelectableEnum;
import com.modularmc.gtpm.api.gui.texture.IGuiTexture;

import com.modularmc.registrate.localization.MinecraftLocale;
import com.modularmc.registrate.localization.annotation.Trans;
import com.modularmc.registrate.localization.annotation.TransVariants;
import com.modularmc.registrate.localization.annotation.Translation;
import com.modularmc.registrate.localization.annotation.Variant;

import lombok.Getter;

@TransVariants(keyPrefix = "cover.voiding",
               value = {
                       @Variant(id = "label.disabled",
                                source = "Disabled",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "禁用")),
                       @Variant(id = "label.enabled",
                                source = "Enabled",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "启用")),
                       @Variant(id = "message.disabled",
                                source = "Voiding Cover Disabled",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "已禁用销毁覆盖板")),
                       @Variant(id = "message.enabled",
                                source = "Voiding Cover Enabled",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "已启用销毁覆盖板")),
                       @Variant(id = "tooltip",
                                source = "§cWARNING!§7 Setting this to \"Enabled\" means that fluids or items WILL be voided.",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "§c警告！§7“启用”时所有流体和物品全部都会被销毁。")),
                       @Variant(id = "voiding_mode.description.0",
                                source = "§eVoid Matching§r will void anything matching the filter. ",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "§e匹配销毁§r：匹配过滤规则即销毁。")),
                       @Variant(id = "voiding_mode.description.1",
                                source = "§eVoid Overflow§r will void anything matching the filter, up to the specified amount.",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "§e溢出销毁§r：匹配过滤规则即销毁超出设定数量的部分物品/流体。")),
               })
public enum VoidingMode implements SelectableEnum {

    @Trans(key = "cover.voiding.voiding_mode.void_any",
           source = "Void Matching",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "匹配销毁"))
    VOID_ANY("cover.voiding.voiding_mode.void_any", "void_any", 1),
    @Trans(key = "cover.voiding.voiding_mode.void_overflow",
           source = "Void Overflow",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "溢出销毁"))
    VOID_OVERFLOW("cover.voiding.voiding_mode.void_overflow", "void_overflow", 1024);

    @Getter
    public final String tooltip;
    @Getter
    public final IGuiTexture icon;
    public final int maxStackSize;

    VoidingMode(String tooltip, String textureName, int maxStackSize) {
        this.tooltip = tooltip;
        this.maxStackSize = maxStackSize;
        this.icon = GuiTextures.resource("gtpm:textures/gui/icon/voiding_mode/" + textureName + ".png");
    }
}
