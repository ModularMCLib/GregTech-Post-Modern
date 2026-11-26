package com.modularmc.gtpm.api.machine.trait.multiblock;

import com.modularmc.gtpm.api.machine.multiblock.MultiblockControllerMachine;
import com.modularmc.gtpm.api.machine.trait.MachineTrait;

import java.util.List;

public abstract class MultiblockMachineTrait extends MachineTrait {

    public MultiblockMachineTrait() {
        super();
    }

    @Override
    public MultiblockControllerMachine getMachine() {
        return (MultiblockControllerMachine) super.getMachine();
    }

    @Override
    protected List<Class<?>> validMachineClasses() {
        return List.of(MultiblockControllerMachine.class);
    }

    public void onStructureFormed(String structureName) {}

    public void onStructureInvalid(String structureName) {}
}
