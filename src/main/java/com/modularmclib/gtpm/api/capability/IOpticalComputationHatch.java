package com.modularmclib.gtpm.api.capability;

public interface IOpticalComputationHatch extends IOpticalComputationProvider {

    /** If this hatch transmits or receives CWU/t. */
    boolean isTransmitter();
}
