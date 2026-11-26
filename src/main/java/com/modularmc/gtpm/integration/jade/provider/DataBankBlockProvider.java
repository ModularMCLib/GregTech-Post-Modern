package com.modularmc.gtpm.integration.jade.provider;

import com.modularmc.gtpm.GTPM;
import com.modularmc.gtpm.api.GTValues;
import com.modularmc.gtpm.common.machine.multiblock.electric.research.DataBankMachine;
import com.modularmc.gtpm.utils.FormattingUtil;
import com.modularmc.gtpm.utils.GTUtil;

import net.minecraft.nbt.LongTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;

import snownee.jade.api.BlockAccessor;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public class DataBankBlockProvider extends MachineInfoProvider<DataBankMachine, LongTag> {

    public DataBankBlockProvider() {
        super(GTPM.id("data_bank"), DataBankMachine.class);
    }

    @Override
    protected LongTag write(DataBankMachine machine) {
        return LongTag.valueOf(machine.getEnergyUsage());
    }

    @Override
    protected void addTooltip(LongTag data, ITooltip tooltip, Player player, BlockAccessor block,
                              BlockEntity blockEntity, IPluginConfig config) {
        long energyUsage = data.getAsLong();
        String energyFormatted = FormattingUtil.formatNumbers(energyUsage);
        Component voltageName = Component.literal(GTValues.VNF[GTUtil.getTierByVoltage(energyUsage)]);
        Component text = Component.translatable(
                "gtpm.multiblock.energy_consumption",
                energyFormatted,
                voltageName);

        tooltip.add(text);
    }
}
