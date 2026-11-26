package com.modularmc.gtpm.api.machine.feature;

import com.modularmc.gtpm.api.machine.MetaMachine;

public interface IMachineFeature {

    default MetaMachine self() {
        return (MetaMachine) this;
    }
}
