package com.modularmclib.gtpm.api.machine.multiblock.part;

import com.modularmclib.gtpm.api.blockentity.BlockEntityCreationInfo;
import com.modularmclib.gtpm.api.machine.feature.ITieredMachine;

import lombok.Getter;

public class TieredPartMachine extends MultiblockPartMachine implements ITieredMachine {

    @Getter
    protected final int tier;

    public TieredPartMachine(BlockEntityCreationInfo info, int tier) {
        super(info);
        this.tier = tier;
    }
}
