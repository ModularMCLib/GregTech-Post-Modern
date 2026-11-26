package com.modularmclib.gtpm.integration.jade.provider;

import com.modularmclib.gtpm.GTPM;
import com.modularmclib.gtpm.common.machine.multiblock.primitive.PrimitivePumpMachine;
import com.modularmclib.gtpm.utils.FormattingUtil;

import net.minecraft.nbt.LongTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;

import snownee.jade.api.BlockAccessor;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public class PrimitivePumpBlockProvider extends MachineInfoProvider<PrimitivePumpMachine, LongTag> {

    public PrimitivePumpBlockProvider() {
        super(GTPM.id("primitive_pump"), PrimitivePumpMachine.class);
    }

    @Override
    protected LongTag write(PrimitivePumpMachine machine) {
        return LongTag.valueOf(machine.getFluidProduction());
    }

    @Override
    protected void addTooltip(LongTag data, ITooltip tooltip, Player player, BlockAccessor block,
                              BlockEntity blockEntity, IPluginConfig config) {
        tooltip.add(Component.translatable("gtpm.top.primitive_pump_production",
                FormattingUtil.formatNumbers(data.getAsLong())));
    }
}
