package com.modularmc.gtpm.common.cover.detector;

import com.modularmc.gtpm.api.capability.GTCapabilityHelper;
import com.modularmc.gtpm.api.capability.ICoverable;
import com.modularmc.gtpm.api.capability.IEnergyInfoProvider;
import com.modularmc.gtpm.api.cover.CoverDefinition;
import com.modularmc.gtpm.utils.RedstoneUtil;

import com.modularmc.registrate.localization.MinecraftLocale;
import com.modularmc.registrate.localization.annotation.TransVariants;
import com.modularmc.registrate.localization.annotation.Translation;
import com.modularmc.registrate.localization.annotation.Variant;

import net.minecraft.core.Direction;

import org.jetbrains.annotations.Nullable;

import java.math.BigInteger;

@TransVariants(keyPrefix = "gtpm.cover.energy_detector",
               value = {
                       @Variant(id = "message_electricity_storage_inverted",
                                source = "Monitoring Inverted Electricity Storage",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "正在以反相模式监控电力存储")),
                       @Variant(id = "message_electricity_storage_normal",
                                source = "Monitoring Normal Electricity Storage",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "正在以普通模式监控电力存储")),
               })
public class EnergyDetectorCover extends DetectorCover {

    public EnergyDetectorCover(CoverDefinition definition, ICoverable coverHolder, Direction attachedSide) {
        super(definition, coverHolder, attachedSide);
    }

    @Override
    public boolean canAttach() {
        return super.canAttach() && getEnergyInfoProvider() != null;
    }

    @Override
    protected void update() {
        if (this.coverHolder.getOffsetTimer() % 20 != 0)
            return;

        IEnergyInfoProvider energyInfoProvider = getEnergyInfoProvider();
        if (energyInfoProvider == null) return;

        var energyInfo = energyInfoProvider.getEnergyInfo();
        var isBigInt = energyInfoProvider.supportsBigIntEnergyValues();

        if (isBigInt) {
            if (energyInfo.capacity().equals(BigInteger.ZERO)) return;

            setRedstoneSignalOutput(
                    RedstoneUtil.computeRedstoneValue(energyInfo.stored(), energyInfo.capacity(), isInverted()));
        } else {
            long storedEnergy = energyInfo.stored().longValue();
            long energyCapacity = energyInfo.capacity().longValue();
            if (energyCapacity == 0) return;

            setRedstoneSignalOutput(RedstoneUtil.computeRedstoneValue(storedEnergy, energyCapacity, isInverted()));
        }
    }

    @Nullable
    protected IEnergyInfoProvider getEnergyInfoProvider() {
        return GTCapabilityHelper.getEnergyInfoProvider(coverHolder.getLevel(), coverHolder.getBlockPos(),
                attachedSide);
    }
}
