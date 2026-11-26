package com.modularmc.gtpm.api.machine.trait;

import com.modularmc.gtpm.api.computation.ComputationConsumer;
import com.modularmc.gtpm.api.computation.ComputationPortPolicy;
import com.modularmc.gtpm.api.computation.ComputationProducer;
import com.modularmc.gtpm.api.machine.MetaMachine;
import com.modularmc.gtpm.api.machine.feature.multiblock.IMultiPart;
import com.modularmc.gtpm.api.machine.multiblock.MultiblockControllerMachine;

import java.util.Optional;

public class MultiblockComputationPortTrait extends ComputationPortTrait {

    private final boolean producerPort;
    private final boolean consumerPort;

    public MultiblockComputationPortTrait(MetaMachine machine, boolean producerPort, boolean consumerPort) {
        super(machine, ComputationPortPolicy.OPTICAL_ONLY);
        this.producerPort = producerPort;
        this.consumerPort = consumerPort;
        this.capabilityValidator = side -> side == null || side == machine.getFrontFacing();
    }

    @Override
    public Optional<ComputationProducer> getComputationProducer() {
        return producerPort ? findProducer() : Optional.empty();
    }

    @Override
    public Optional<ComputationConsumer> getComputationConsumer() {
        return consumerPort ? findConsumer() : Optional.empty();
    }

    private Optional<ComputationProducer> findProducer() {
        if (!(getMachine() instanceof IMultiPart part) || !part.isFormed()) return Optional.empty();
        for (MultiblockControllerMachine controller : part.getControllers()) {
            if (controller instanceof ComputationProducer producer) {
                return Optional.of(producer);
            }
            for (MachineTrait trait : controller.self().getAllTraits()) {
                if (trait instanceof ComputationProducer producer) {
                    return Optional.of(producer);
                }
            }
        }
        return Optional.empty();
    }

    private Optional<ComputationConsumer> findConsumer() {
        if (!(getMachine() instanceof IMultiPart part) || !part.isFormed()) return Optional.empty();
        for (MultiblockControllerMachine controller : part.getControllers()) {
            if (controller instanceof ComputationConsumer consumer) {
                return Optional.of(consumer);
            }
            for (MachineTrait trait : controller.self().getAllTraits()) {
                if (trait instanceof ComputationConsumer consumer) {
                    return Optional.of(consumer);
                }
            }
        }
        return Optional.empty();
    }
}
