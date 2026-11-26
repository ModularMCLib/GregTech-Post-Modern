package com.modularmc.gtpm.common.pipelike.fluidpipe.longdistance;

import com.modularmc.gtpm.api.pipenet.longdistance.LongDistancePipeType;
import com.modularmc.gtpm.config.ConfigHolder;

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
