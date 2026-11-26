package com.modularmclib.gtpm.utils;

import com.modularmclib.gtpm.api.machine.trait.NotifiableEnergyContainer;

public class InfiniteEnergyContainer extends NotifiableEnergyContainer {

    public InfiniteEnergyContainer(long maxCapacity, long maxInputVoltage, long maxInputAmperage,
                                   long maxOutputVoltage, long maxOutputAmperage) {
        super(maxCapacity, maxInputVoltage, maxInputAmperage, maxOutputVoltage, maxOutputAmperage);
    }

    @Override
    public long getEnergyStored() {
        return getEnergyCapacity();
    }
}
