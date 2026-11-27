package com.modularmc.gtpm.integration.jade.provider;

import com.modularmc.gtpm.GTPM;
import com.modularmc.gtpm.api.blockentity.IPaintable;

import com.modularmc.registrate.localization.MinecraftLocale;
import com.modularmc.registrate.localization.annotation.Trans;
import com.modularmc.registrate.localization.annotation.Translation;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

@Trans(key = "gtpm.top.stained",
       source = "Colored: %s",
       translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "颜色：%s"))
public class StainedColorProvider implements IBlockComponentProvider, IServerDataProvider<BlockAccessor> {

    @Override
    public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        if (blockAccessor.getServerData().contains("StainedColor")) {
            int paintingColor = blockAccessor.getServerData().getInt("StainedColor");
            if (paintingColor != IPaintable.UNPAINTED_COLOR) {
                iTooltip.add(Component.translatable("gtpm.top.stained", String.format("#%06X", paintingColor))
                        .withStyle(style -> style.withColor(paintingColor)));
            }
        }
    }

    @Override
    public void appendServerData(CompoundTag compoundTag, BlockAccessor blockAccessor) {
        if (blockAccessor.getBlockEntity() instanceof IPaintable paintable) {
            int paintingColor = paintable.isPainted() ? paintable.getPaintingColor() : IPaintable.UNPAINTED_COLOR;
            compoundTag.putInt("StainedColor", paintingColor);
        }
    }

    @Override
    public ResourceLocation getUid() {
        return GTPM.id("stained_color");
    }
}
