package com.modularmc.gtpm.integration.jade.provider;

import com.modularmc.gtpm.GTPM;
import com.modularmc.gtpm.common.machine.trait.hazard.EnvironmentalHazardCleanerTrait;

import com.modularmc.registrate.localization.MinecraftLocale;
import com.modularmc.registrate.localization.annotation.Trans;
import com.modularmc.registrate.localization.annotation.Translation;

import net.minecraft.nbt.FloatTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;

import snownee.jade.api.BlockAccessor;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

@Trans(key = "gtpm.jade.cleaned_this_second",
       source = "Cleaned hazard: %s/s",
       translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "污染清理速率：%s/s"))
public class HazardCleanerBlockProvider extends MachineTraitProvider<EnvironmentalHazardCleanerTrait, FloatTag> {

    public HazardCleanerBlockProvider() {
        super(GTPM.id("hazard_cleaner_provider"), EnvironmentalHazardCleanerTrait.TYPE);
    }

    @Override
    protected FloatTag write(EnvironmentalHazardCleanerTrait capability) {
        return FloatTag.valueOf(capability.getRemovedLastSecond());
    }

    @Override
    protected void addTooltip(FloatTag capData, ITooltip tooltip, Player player, BlockAccessor block,
                              BlockEntity blockEntity, IPluginConfig config) {
        float cleaned = capData.getAsFloat();
        if (cleaned > 0) {
            tooltip.add(Component.translatable("gtpm.jade.cleaned_this_second", cleaned));
        }
    }
}
