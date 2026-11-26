package com.modularmc.gtpm.api.machine.feature.multiblock;

import com.modularmc.gtpm.api.machine.feature.IRecipeLogicMachine;
import com.modularmc.gtpm.api.machine.multiblock.MultiblockControllerMachine;

public interface IWorkableMultiController extends IRecipeLogicMachine {

    MultiblockControllerMachine self();
}
