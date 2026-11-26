package com.modularmc.gtpm.api.machine.multiblock;

import com.modularmc.gtpm.api.block.ICoilType;
import com.modularmc.gtpm.api.blockentity.BlockEntityCreationInfo;
import com.modularmc.gtpm.common.block.CoilBlock;

import lombok.Getter;

public class CoilWorkableElectricMultiblockMachine extends WorkableElectricMultiblockMachine {

    @Getter
    private ICoilType coilType = CoilBlock.CoilType.CUPRONICKEL;

    public CoilWorkableElectricMultiblockMachine(BlockEntityCreationInfo info) {
        super(info);
    }

    //////////////////////////////////////
    // *** Multiblock LifeCycle ***//
    //////////////////////////////////////
    @Override
    public void formStructure(String structureName) {
        super.formStructure(structureName);
        if (DEFAULT_STRUCTURE.equals(structureName)) {
            var type = getMultiblockState(structureName).getFacts().get("CoilType");
            if (type instanceof ICoilType coil) {
                this.coilType = coil;
            }
        }
    }

    public int getCoilTier() {
        return coilType.getTier();
    }
}
