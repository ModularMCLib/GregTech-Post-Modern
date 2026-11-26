package com.modularmc.gtpm.api.machine.trait;

import com.modularmc.gtpm.api.computation.ComputationConsumer;
import com.modularmc.gtpm.api.computation.ComputationPortPolicy;
import com.modularmc.gtpm.api.computation.ComputationProducer;
import com.modularmc.gtpm.api.machine.MetaMachine;

import org.jetbrains.annotations.Nullable;

public class DirectComputationPortTrait extends ComputationPortTrait {

    public DirectComputationPortTrait(MetaMachine machine, boolean acceptsOptical,
                                      @Nullable ComputationProducer producer,
                                      @Nullable ComputationConsumer consumer) {
        super(machine, acceptsOptical ? ComputationPortPolicy.OPTICAL_AND_ADJACENT :
                ComputationPortPolicy.ADJACENT_ONLY, producer, consumer);
    }
}
