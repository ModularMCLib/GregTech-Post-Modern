package com.modularmc.gtpm.data.lang;

import com.modularmc.registrate.localization.MinecraftLocale;
import com.modularmc.registrate.localization.annotation.Trans;
import com.modularmc.registrate.localization.annotation.TransVariants;
import com.modularmc.registrate.localization.annotation.Translation;
import com.modularmc.registrate.localization.annotation.Variant;

@Trans(key = "item.invalid.name",
       source = "Invalid item",
       translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "无效物品"))
@TransVariants(keyPrefix = "metaitem",
               value = {
                       @Variant(id = "tricorder_scanner.tooltip",
                                source = "Tricorder",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "三录仪")),
                       @Variant(id = "debug_scanner.tooltip",
                                source = "Tricorder",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "三录仪")),
                       @Variant(id = "terminal.tooltip",
                                source = "Sharp tools make good work",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "工欲善其事，必先利其器。")),
                       @Variant(id = "terminal.tooltip.creative",
                                source = "§bCreative Mode",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§b创造模式")),
                       @Variant(id = "terminal.tooltip.hardware",
                                source = "§aHardware: %d",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§a硬件：%d")),
                       @Variant(id = "plugin.tooltips.1",
                                source = "Plugins can be added to the screen for more functionality.",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "可为屏幕安装插件以实现更多功能。")),
                       @Variant(id = "plugin.proxy.tooltips.1",
                                source = "(Please adjust to proxy mode in the screen)",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "（请将屏幕调整为代理模式）")),
               })
public class ItemLang {}
