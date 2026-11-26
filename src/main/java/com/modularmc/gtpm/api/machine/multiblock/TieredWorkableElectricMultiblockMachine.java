package com.modularmc.gtpm.api.machine.multiblock;

import com.modularmc.gtpm.api.GTValues;
import com.modularmc.gtpm.api.blockentity.BlockEntityCreationInfo;
import com.modularmc.gtpm.api.sync_system.annotations.SaveField;

import lombok.Getter;

/**
 * @author screret
 * @date 2023/7/11
 * @implNote TieredWorkableElectricMultiblockMachine
 */
public class TieredWorkableElectricMultiblockMachine extends WorkableElectricMultiblockMachine {

    private final int tier;
    @SaveField
    @Getter
    protected int overclockTier;

    public TieredWorkableElectricMultiblockMachine(BlockEntityCreationInfo info, int tier) {
        super(info);
        this.tier = tier;
    }

    //////////////////////////////////////
    // ***** Initialization ******//
    //////////////////////////////////////

    //////////////////////////////////////
    // ******** OVERCLOCK *********//
    //////////////////////////////////////
    public int getMinOverclockTier() {
        return 0;
    }

    @Override
    public void setOverclockTier(int tier) {
        if (!isRemote() && tier >= getMinOverclockTier() && tier <= getMaxOverclockTier()) {
            this.overclockTier = tier;
            this.recipeLogic.markLastRecipeDirty();
        }
    }

    @Override
    public long getOverclockVoltage() {
        return Math.min(GTValues.V[getOverclockTier()], super.getOverclockVoltage());
    }

    //////////////////////////////////////
    // ****** RECIPE LOGIC *******//
    //////////////////////////////////////
    @Override
    public int getTier() {
        return Math.min(tier, super.getTier());
    }

    @Override
    public long getTierVoltage() {
        return Math.min(GTValues.V[tier], super.getMaxVoltage());
    }
}
