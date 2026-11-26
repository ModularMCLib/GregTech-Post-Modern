package com.modularmclib.gtpm.integration.jade.provider;

import com.modularmclib.gtpm.GTPM;
import com.modularmclib.gtpm.api.GTValues;
import com.modularmclib.gtpm.common.machine.multiblock.electric.research.DataBankMachine;
import com.modularmclib.gtpm.utils.FormattingUtil;
import com.modularmclib.gtpm.utils.GTUtil;

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
