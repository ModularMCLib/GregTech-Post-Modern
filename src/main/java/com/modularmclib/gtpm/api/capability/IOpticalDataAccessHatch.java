package com.modularmclib.gtpm.api.capability;

public interface IOpticalDataAccessHatch extends IDataAccessMachine {

    /**
     * @return if this hatch transmits data through cables
     */
    boolean isTransmitter();
}
