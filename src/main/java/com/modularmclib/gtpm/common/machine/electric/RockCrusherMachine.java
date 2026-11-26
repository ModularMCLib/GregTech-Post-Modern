package com.modularmclib.gtpm.common.machine.electric;

import com.modularmclib.gtpm.api.blockentity.BlockEntityCreationInfo;
import com.modularmclib.gtpm.api.machine.SimpleTieredMachine;
import com.modularmclib.gtpm.common.data.machines.GTMachineUtils;

public class RockCrusherMachine extends SimpleTieredMachine {

    public RockCrusherMachine(BlockEntityCreationInfo info, int tier) {
        super(info, tier, GTMachineUtils.defaultTankSizeFunction);
        environmentalExplosionTrait.setEnableEnvironmentalExplosions(false);
    }
}
