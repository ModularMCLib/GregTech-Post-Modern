package com.modularmclib.gtpm.common.machine.multiblock.part;

import com.modularmclib.gtpm.api.GTValues;
import com.modularmclib.gtpm.api.blockentity.BlockEntityCreationInfo;
import com.modularmclib.gtpm.api.gui.fancy.TooltipsPanel;
import com.modularmclib.gtpm.api.machine.feature.multiblock.IMaintenanceMachine;
import com.modularmclib.gtpm.api.machine.multiblock.part.TieredPartMachine;

public class AutoMaintenanceHatchPartMachine extends TieredPartMachine
                                             implements IMaintenanceMachine {

    public AutoMaintenanceHatchPartMachine(BlockEntityCreationInfo info) {
        super(info, GTValues.HV);
    }

    @Override
    public void setTaped(boolean ignored) {}

    @Override
    public boolean isTaped() {
        return false;
    }

    @Override
    public boolean isFullAuto() {
        return true;
    }

    @Override
    public byte startProblems() {
        return NO_PROBLEMS;
    }

    @Override
    public byte getMaintenanceProblems() {
        return NO_PROBLEMS;
    }

    @Override
    public void setMaintenanceProblems(byte problems) {}

    @Override
    public int getTimeActive() {
        return 0;
    }

    @Override
    public void setTimeActive(int time) {}

    @Override
    public void attachTooltips(TooltipsPanel panel) {
        attachMaintenanceTooltips(panel);
    }
}
