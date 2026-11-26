package com.modularmclib.gtpm.common.pipelike.item.longdistance;

import com.modularmclib.gtpm.api.pipenet.longdistance.LongDistancePipeType;
import com.modularmclib.gtpm.config.ConfigHolder;

public class LDItemPipeType extends LongDistancePipeType {

    public static final LDItemPipeType INSTANCE = new LDItemPipeType();

    private LDItemPipeType() {
        super("item");
    }

    @Override
    public int getMinLength() {
        return ConfigHolder.INSTANCE.machines.ldItemPipeMinDistance;
    }
}
