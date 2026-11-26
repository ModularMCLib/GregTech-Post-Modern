package com.modularmclib.gtpm.api.capability;

import com.modularmclib.gtpm.api.machine.feature.IRecipeLogicMachine;

public interface IMiner extends IRecipeLogicMachine {

    boolean drainInput(boolean simulate);

    static int getWorkingArea(int maximumRadius) {
        return maximumRadius * 2 + 1;
    }
}
