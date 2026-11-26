package com.modularmc.gtpm.common.cover.detector;

import com.modularmc.gtpm.api.capability.GTCapabilityHelper;
import com.modularmc.gtpm.api.capability.ICoverable;
import com.modularmc.gtpm.api.cover.CoverDefinition;
import com.modularmc.gtpm.api.machine.feature.multiblock.IMaintenanceMachine;
import com.modularmc.gtpm.config.ConfigHolder;

import net.minecraft.core.Direction;

public class MaintenanceDetectorCover extends DetectorCover {

    public MaintenanceDetectorCover(CoverDefinition definition, ICoverable coverHolder, Direction attachedSide) {
        super(definition, coverHolder, attachedSide);
    }

    @Override
    public boolean canAttach() {
        if (!ConfigHolder.INSTANCE.machines.enableMaintenance) {
            return false;
        }

        return super.canAttach() &&
                GTCapabilityHelper.getMaintenanceMachine(coverHolder.getLevel(), coverHolder.getBlockPos(),
                        attachedSide) !=
                        null;
    }

    @Override
    protected void update() {
        if (this.coverHolder.getOffsetTimer() % 20 != 0) {
            return;
        }

        IMaintenanceMachine maintenance = GTCapabilityHelper.getMaintenanceMachine(coverHolder.getLevel(),
                coverHolder.getBlockPos(), attachedSide);

        int signal = getRedstoneSignalOutput();
        boolean shouldSignal = isInverted() != maintenance.hasMaintenanceProblems();

        if (shouldSignal && signal != 15) {
            setRedstoneSignalOutput(15);
        } else if (!shouldSignal && signal == 15) {
            setRedstoneSignalOutput(0);
        }
    }
}
