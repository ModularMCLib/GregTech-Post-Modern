package com.modularmc.gtpm.common.machine.electric;

import com.modularmc.gtpm.api.blockentity.BlockEntityCreationInfo;
import com.modularmc.gtpm.api.machine.SimpleTieredMachine;
import com.modularmc.gtpm.common.data.machines.GTMachineUtils;

public class RockCrusherMachine extends SimpleTieredMachine {

    public RockCrusherMachine(BlockEntityCreationInfo info, int tier) {
        super(info, tier, GTMachineUtils.defaultTankSizeFunction);
        environmentalExplosionTrait.setEnableEnvironmentalExplosions(false);
    }
}
