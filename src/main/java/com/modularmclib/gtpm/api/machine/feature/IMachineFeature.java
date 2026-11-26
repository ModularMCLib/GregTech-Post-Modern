package com.modularmclib.gtpm.api.machine.feature;

import com.modularmclib.gtpm.api.machine.MetaMachine;

public interface IMachineFeature {

    default MetaMachine self() {
        return (MetaMachine) this;
    }
}
