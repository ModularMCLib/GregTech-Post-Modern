package com.modularmc.gtpm.integration.jade.provider;

import com.modularmc.gtpm.GTPM;
import com.modularmc.gtpm.common.machine.electric.ConverterMachine;

import com.modularmc.registrate.localization.MinecraftLocale;
import com.modularmc.registrate.localization.annotation.TransVariants;
import com.modularmc.registrate.localization.annotation.Translation;
import com.modularmc.registrate.localization.annotation.Variant;

import net.minecraft.nbt.ByteTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;

import snownee.jade.api.BlockAccessor;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

@TransVariants(keyPrefix = "gtpm.top",
               value = {
                       @Variant(id = "convert_eu",
                                source = "Converting §eEU§r -> §cFE§r",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "转换模式：§eEU§r ->§cFE§r")),
                       @Variant(id = "convert_fe",
                                source = "Converting §cFE§r -> §eEU§r",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "转换模式：§cFE§r ->§eEU§r")),
               })
public class EnergyConverterModeProvider extends MachineInfoProvider<ConverterMachine, ByteTag> {

    public EnergyConverterModeProvider() {
        super(GTPM.id("energy_converter_provider"), ConverterMachine.class);
    }

    @Override
    protected ByteTag write(ConverterMachine machine) {
        return ByteTag.valueOf(machine.isFeToEu());
    }

    @Override
    protected void addTooltip(ByteTag data, ITooltip tooltip, Player player, BlockAccessor block,
                              BlockEntity blockEntity, IPluginConfig config) {
        if (data.getAsByte() == 1) {
            tooltip.add(Component.translatable("gtpm.top.convert_fe"));
        } else {
            tooltip.add(Component.translatable("gtpm.top.convert_eu"));
        }
    }
}
