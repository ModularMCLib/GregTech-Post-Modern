package com.modularmc.gtpm.common.pipelike.item.longdistance;

import com.modularmc.gtpm.api.pipenet.longdistance.LongDistancePipeType;
import com.modularmc.gtpm.config.GTPMConfigHolder;

public class LDItemPipeType extends LongDistancePipeType {

    public static final LDItemPipeType INSTANCE = new LDItemPipeType();

    private LDItemPipeType() {
        super("item");
    }

    @Override
    public int getMinLength() {
        return GTPMConfigHolder.INSTANCE.machines.ldItemPipeMinDistance;
    }
}
