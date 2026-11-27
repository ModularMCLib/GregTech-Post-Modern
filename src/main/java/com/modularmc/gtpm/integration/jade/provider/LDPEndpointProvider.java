package com.modularmc.gtpm.integration.jade.provider;

import com.modularmc.gtpm.GTPM;
import com.modularmc.gtpm.common.machine.storage.LongDistanceEndpointMachine;
import com.modularmc.gtpm.utils.FormattingUtil;

import com.modularmc.registrate.localization.MinecraftLocale;
import com.modularmc.registrate.localization.annotation.TransVariants;
import com.modularmc.registrate.localization.annotation.Translation;
import com.modularmc.registrate.localization.annotation.Variant;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;

import snownee.jade.api.BlockAccessor;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

@TransVariants(keyPrefix = "gtpm.top.ldp_endpoint",
               value = {
                       @Variant(id = "io_type",
                                source = "IO Type: %s",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "IO类型：%s")),
                       @Variant(id = "is_formed",
                                source = "§aPipeline Formed§r",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§a管路已成型§r")),
                       @Variant(id = "not_formed",
                                source = "§cPipeline Incomplete§r",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§c管路未连接§r")),
                       @Variant(id = "output_direction",
                                source = "Output Direction: %s",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "输出方向：%s")),
               })
public class LDPEndpointProvider extends MachineInfoProvider<LongDistanceEndpointMachine, CompoundTag> {

    public LDPEndpointProvider() {
        super(GTPM.id("ldp_endpoint"), LongDistanceEndpointMachine.class);
    }

    @Override
    protected CompoundTag write(LongDistanceEndpointMachine machine) {
        var data = new CompoundTag();
        data.putBoolean("isFormed", machine.getLink() != null);
        data.putString("ioType", machine.getIoType().getTooltip());
        data.putString("outputDirection", machine.getOutputFacing().getName());
        return data;
    }

    @Override
    protected void addTooltip(CompoundTag data, ITooltip tooltip, Player player, BlockAccessor block,
                              BlockEntity blockEntity, IPluginConfig config) {
        boolean isFormed = data.getBoolean("isFormed");
        String ioType = data.getString("ioType");
        String outputDirection = data.getString("outputDirection");

        tooltip.add(Component.translatable(
                isFormed ? "gtpm.top.ldp_endpoint.is_formed" : "gtpm.top.ldp_endpoint.not_formed"));
        tooltip.add(Component.translatable("gtpm.top.ldp_endpoint.io_type", Component.translatable(ioType)
                .withStyle(ioType.contains("import") ? ChatFormatting.GREEN : ChatFormatting.RED)));
        tooltip.add(Component.translatable("gtpm.top.ldp_endpoint.output_direction",
                FormattingUtil.toEnglishName((outputDirection))));
    }
}
