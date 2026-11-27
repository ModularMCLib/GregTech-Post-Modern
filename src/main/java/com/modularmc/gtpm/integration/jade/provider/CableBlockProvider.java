package com.modularmc.gtpm.integration.jade.provider;

import com.modularmc.gtpm.GTPM;
import com.modularmc.gtpm.api.GTValues;
import com.modularmc.gtpm.common.block.CableBlock;
import com.modularmc.gtpm.common.blockentity.CableBlockEntity;
import com.modularmc.gtpm.utils.GTUtil;

import com.modularmc.registrate.localization.MinecraftLocale;
import com.modularmc.registrate.localization.annotation.TransVariants;
import com.modularmc.registrate.localization.annotation.Translation;
import com.modularmc.registrate.localization.annotation.Variant;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;

import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

import static com.modularmc.gtpm.utils.FormattingUtil.DECIMAL_FORMAT_1F;

@TransVariants(keyPrefix = "gtpm.top",
               value = {
                       @Variant(id = "cable_amperage",
                                source = "Amperage: ",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "电流：")),
                       @Variant(id = "cable_overloaded",
                                source = "§4OVERHEATING: %s%%§r",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§4过热：%s%%§r")),
                       @Variant(id = "cable_voltage",
                                source = "Voltage: ",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "电压：")),
               })
public class CableBlockProvider implements IBlockComponentProvider, IServerDataProvider<BlockAccessor> {

    @Override
    public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        BlockEntity be = blockAccessor.getBlockEntity();
        if (be != null) {
            CompoundTag data = blockAccessor.getServerData().getCompound(getUid().toString());
            if (data.contains("cableData", Tag.TAG_COMPOUND)) {
                var tag = data.getCompound("cableData");
                long voltage = tag.getLong("currentVoltage");
                double amperage = tag.getDouble("currentAmperage");
                int temperature = tag.getInt("temperature");
                iTooltip.add(Component.translatable("gtpm.top.cable_voltage"));
                if (voltage != 0) {
                    iTooltip.append(Component.literal(GTValues.VNF[GTUtil.getTierByVoltage(voltage)]));
                    iTooltip.append(Component.literal(" / "));
                }
                iTooltip.append(Component.literal(GTValues.VNF[GTUtil.getTierByVoltage(tag.getLong("maxVoltage"))]));

                iTooltip.add(Component.translatable("gtpm.top.cable_amperage"));
                if (amperage != 0) {
                    iTooltip.append(Component.literal(DECIMAL_FORMAT_1F.format(amperage) + "A / "));
                }
                iTooltip.append(Component.translatable("gtpm.jade.amperage_use",
                        DECIMAL_FORMAT_1F.format(tag.getDouble("maxAmperage"))));

                if (temperature != CableBlockEntity.getDefaultTemp()) {
                    iTooltip.add(Component.translatable("gtpm.top.cable_overloaded", progressToFailure(
                            CableBlockEntity.getDefaultTemp(), CableBlockEntity.getMeltTemp(), temperature)));
                }
            }
        }
    }

    @Override
    public void appendServerData(CompoundTag compoundTag, BlockAccessor blockAccessor) {
        CompoundTag data = compoundTag.getCompound(getUid().toString());
        if (blockAccessor.getBlock() instanceof CableBlock cableBlock) {
            CableBlockEntity cable = (CableBlockEntity) cableBlock.getPipeTile(blockAccessor.getLevel(),
                    blockAccessor.getPosition());
            if (cable != null) {
                var cableData = new CompoundTag();
                cableData.putLong("maxVoltage", cable.getMaxVoltage());
                cableData.putLong("currentVoltage", cable.getCurrentMaxVoltage());
                cableData.putDouble("maxAmperage", cable.getMaxAmperage());
                cableData.putDouble("currentAmperage", cable.getAverageAmperage());
                cableData.putInt("temperature", cable.getTemperature());
                data.put("cableData", cableData);
            }
        }
        compoundTag.put(getUid().toString(), data);
    }

    @Override
    public ResourceLocation getUid() {
        return GTPM.id("cable_info");
    }

    private int progressToFailure(int base, int melt, int current) {
        return (100 * (current - base)) / (melt - base);
    }
}
