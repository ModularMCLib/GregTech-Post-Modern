package com.modularmclib.gtpm.api.machine.feature.multiblock;

import com.modularmclib.gtpm.api.machine.feature.IRecipeLogicMachine;
import com.modularmclib.gtpm.api.machine.multiblock.MultiblockControllerMachine;

public interface IWorkableMultiController extends IRecipeLogicMachine {

    MultiblockControllerMachine self();
}
