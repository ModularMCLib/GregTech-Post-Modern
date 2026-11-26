package com.modularmclib.gtpm.api.machine.trait;

import com.modularmclib.gtpm.api.computation.ComputationConsumer;
import com.modularmclib.gtpm.api.computation.ComputationPortPolicy;
import com.modularmclib.gtpm.api.computation.ComputationProducer;
import com.modularmclib.gtpm.api.machine.MetaMachine;

import org.jetbrains.annotations.Nullable;

public class DirectComputationPortTrait extends ComputationPortTrait {

    public DirectComputationPortTrait(MetaMachine machine, boolean acceptsOptical,
                                      @Nullable ComputationProducer producer,
                                      @Nullable ComputationConsumer consumer) {
        super(machine, acceptsOptical ? ComputationPortPolicy.OPTICAL_AND_ADJACENT :
                ComputationPortPolicy.ADJACENT_ONLY, producer, consumer);
    }
}
