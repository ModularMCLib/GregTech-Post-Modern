package com.modularmc.gtpm.common.cover.data;

import com.modularmc.gtpm.api.gui.GuiTextures;
import com.modularmc.gtpm.api.gui.SelectableEnum;
import com.modularmc.gtpm.api.gui.texture.IGuiTexture;

import com.modularmc.registrate.localization.MinecraftLocale;
import com.modularmc.registrate.localization.annotation.Trans;
import com.modularmc.registrate.localization.annotation.TransVariants;
import com.modularmc.registrate.localization.annotation.Translation;
import com.modularmc.registrate.localization.annotation.Variant;

@TransVariants(keyPrefix = "cover.universal.manual_import_export.mode.description",
               value = {
                       @Variant(id = "0",
                                source = "§eDisabled§r - Items/fluids will only move as specified by the cover and its filter. ",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "§e禁用§r-仅依照覆盖板与内部过滤器的指定规则传输。")),
                       @Variant(id = "1",
                                source = "§eAllow Filtered§r - Items/fluids can be extracted and inserted independently of the cover mode, as long as its filter matches (if any). ",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "§e过滤§r-外部I/O将只根据过滤器的指定规则传输，不受覆盖板的限制。")),
                       @Variant(id = "2",
                                source = "§eAllow Unfiltered§r - Items/fluids can be moved independently of the cover mode. Filter applies to the items inserted or extracted by this cover",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "§e无过滤§r-外部I/O无视所有传输规则，过滤器将仅控制覆盖板进行的I/O。")),
               })
public enum ManualIOMode implements SelectableEnum {

    @Trans(key = "cover.universal.manual_import_export.mode.disabled",
           source = "Manual I/O: §bDisabled\n§7Items / Fluids will only move as specified by the cover and its filter.",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "特殊I/O模式：§b禁用\n§7仅依照覆盖板与内部过滤器的指定规则传输。"))
    DISABLED("disabled"),
    @Trans(key = "cover.universal.manual_import_export.mode.filtered",
           source = "Manual I/O: §bFiltered\n§7Items / Fluids can be extracted and inserted independently of the cover mode, as long as its filter matches (if any)",
           translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                       text = "特殊I/O模式：§b过滤\n§7外部I/O将只根据过滤器的指定规则传输，不受覆盖板的限制。"))
    FILTERED("filtered"),
    @Trans(key = "cover.universal.manual_import_export.mode.unfiltered",
           source = "Manual I/O: §bUnfiltered\n§7Items / Fluids can be moved independently of the cover mode. The filter only applies to what is inserted or extracted by this cover itself.",
           translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                       text = "特殊I/O模式：§b无过滤\n§7外部I/O无视所有传输规则，过滤器将仅控制覆盖板进行的I/O。"))
    UNFILTERED("unfiltered");

    public static final ManualIOMode[] VALUES = values();

    public final String localeName;

    ManualIOMode(String localeName) {
        this.localeName = localeName;
    }

    @Override
    public String getTooltip() {
        return "cover.universal.manual_import_export.mode." + localeName;
    }

    @Override
    public IGuiTexture getIcon() {
        return GuiTextures.resource("gtpm:textures/gui/icon/manual_io_mode/" + localeName + ".png");
    }
}
