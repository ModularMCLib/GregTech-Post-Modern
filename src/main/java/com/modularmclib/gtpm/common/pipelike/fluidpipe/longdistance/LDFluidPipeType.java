package com.modularmclib.gtpm.common.pipelike.fluidpipe.longdistance;

import com.modularmclib.gtpm.api.pipenet.longdistance.LongDistancePipeType;
import com.modularmclib.gtpm.config.ConfigHolder;

public class LDFluidPipeType extends LongDistancePipeType {

    public static final LDFluidPipeType INSTANCE = new LDFluidPipeType();

    private LDFluidPipeType() {
        super("fluid");
    }

    @Override
    public int getMinLength() {
        return ConfigHolder.INSTANCE.machines.ldFluidPipeMinDistance;
    }
}
