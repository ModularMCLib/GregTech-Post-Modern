package com.modularmclib.gtpm.api.machine;

import com.modularmclib.gtpm.api.blockentity.BlockEntityCreationInfo;
import com.modularmclib.gtpm.api.machine.feature.ITieredMachine;

import lombok.Getter;

public class TieredMachine extends MetaMachine implements ITieredMachine {

    @Getter
    protected final int tier;

    public TieredMachine(BlockEntityCreationInfo info, int tier) {
        super(info);
        this.tier = tier;
    }
}
