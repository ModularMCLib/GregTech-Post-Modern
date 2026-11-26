package com.modularmclib.gtpm.api.machine.feature;

public interface IOverclockMachine extends IMachineFeature {

    void setOverclockTier(int tier);

    long getOverclockVoltage();
}
