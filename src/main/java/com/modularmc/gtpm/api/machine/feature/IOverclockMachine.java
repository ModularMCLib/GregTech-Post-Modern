package com.modularmc.gtpm.api.machine.feature;

import com.modularmc.registrate.localization.MinecraftLocale;
import com.modularmc.registrate.localization.annotation.TransVariants;
import com.modularmc.registrate.localization.annotation.Translation;
import com.modularmc.registrate.localization.annotation.Variant;

/** Exposes the voltage limit used for a machine's recipe overclocking. */
@TransVariants(keyPrefix = "gtpm.gui.overclock",
               value = {
                       @Variant(id = "title",
                                source = "Overclock Tier",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "超频电压级")),
                       @Variant(id = "range",
                                source = "Available Tiers [%s, %s]",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "可用电压级 [%s，%s]")),
                       @Variant(id = "description.0",
                                source = "Overclock Button",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "超频按钮")),
                       @Variant(id = "description.1",
                                source = "§7Recipes can overclock up to the set tier",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7配方可以超频到设定的电压等级")),
                       @Variant(id = "disabled.0",
                                source = "Overclocking Disabled.",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "超频已禁用")),
                       @Variant(id = "disabled.1",
                                source = "Click to Enable",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "单击启用")),
                       @Variant(id = "enabled.0",
                                source = "Overclocking Enabled.",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "超频已启用")),
                       @Variant(id = "enabled.1",
                                source = "Click to Disable",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "单击禁用")),
                       @Variant(id = "off",
                                source = "X",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "X")),
               })
public interface IOverclockMachine extends IMachineFeature {

    void setOverclockTier(int tier);

    long getOverclockVoltage();
}
