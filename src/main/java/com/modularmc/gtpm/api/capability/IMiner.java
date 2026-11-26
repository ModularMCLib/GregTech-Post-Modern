package com.modularmc.gtpm.api.capability;

import com.modularmc.gtpm.api.machine.feature.IRecipeLogicMachine;

public interface IMiner extends IRecipeLogicMachine {

    boolean drainInput(boolean simulate);

    static int getWorkingArea(int maximumRadius) {
        return maximumRadius * 2 + 1;
    }
}
