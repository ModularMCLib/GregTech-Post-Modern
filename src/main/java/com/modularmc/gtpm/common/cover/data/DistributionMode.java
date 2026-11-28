package com.modularmc.gtpm.common.cover.data;

import com.modularmc.gtpm.api.gui.GuiTextures;
import com.modularmc.gtpm.api.gui.SelectableEnum;
import com.modularmc.gtpm.api.gui.texture.IGuiTexture;

import com.modularmc.registrate.localization.MinecraftLocale;
import com.modularmc.registrate.localization.annotation.TransVariants;
import com.modularmc.registrate.localization.annotation.Translation;
import com.modularmc.registrate.localization.annotation.Variant;

public enum DistributionMode implements SelectableEnum {

    @TransVariants(keyPrefix = "cover.conveyor.distribution.round_robin_global",
                   value = {
                           @Variant(id = "0",
                                    source = "Distribution Mode: §bRound Robin",
                                    translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "分配模式：§b轮询调度")),
                           @Variant(id = "1",
                                    source = "§7Splits items equally across connected inventories",
                                    translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                                text = "§7将物品平分至相连的物品存储空间。")),
                   })
    ROUND_ROBIN_GLOBAL("round_robin_global"),
    @TransVariants(keyPrefix = "cover.conveyor.distribution.round_robin_prio",
                   value = {
                           @Variant(id = "0",
                                    source = "Distribution Mode: §bRound Robin with Restriction",
                                    translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                                text = "分配模式：§b轮询调度（低优先管道）")),
                           @Variant(id = "1",
                                    source = "§7Tries to split items equally across connected inventories.",
                                    translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                                text = "§7尝试将物品平分至相连的物品存储空间。")),
                           @Variant(id = "2",
                                    source = "§7Will not send items down Restrictive item pipes unless no other paths are available.",
                                    translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                                text = "§7除非没有其他可用路径，否则不会发送物品至低优先物品管道。")),
                   })
    ROUND_ROBIN_PRIO("round_robin_prio"),
    @TransVariants(keyPrefix = "cover.conveyor.distribution.insert_first",
                   value = {
                           @Variant(id = "0",
                                    source = "Distribution Mode: §bPriority",
                                    translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "分配模式：§b优先级")),
                           @Variant(id = "1",
                                    source = "§7Will insert into the first inventory with the highest priority it can find.",
                                    translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                                text = "§7将物品输入至所搜寻到的优先级最高的物品存储空间")),
                           @Variant(id = "2",
                                    source = "§7Restrictive item pipes lower the priority of a path.",
                                    translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                                text = "§7低优先物品管道会降低管道路径的优先级。")),
                   })
    INSERT_FIRST("insert_first");

    public static final DistributionMode[] VALUES = values();
    private static final float OFFSET = 1.0f / VALUES.length;

    public final String localeName;

    DistributionMode(String localeName) {
        this.localeName = localeName;
    }

    @Override
    public String getTooltip() {
        return "cover.conveyor.distribution." + localeName;
    }

    @Override
    public IGuiTexture getIcon() {
        return GuiTextures.resource("gtpm:textures/gui/icon/distribution_mode/" + localeName + ".png");
    }
}
