package com.modularmclib.gtpm.api.capability;

import com.modularmclib.gtpm.api.machine.trait.NotifiableComputationContainer;

/**
 * Used in conjunction with {@link NotifiableComputationContainer}.
 */
public interface IOpticalComputationReceiver {

    IOpticalComputationProvider getComputationProvider();
}
