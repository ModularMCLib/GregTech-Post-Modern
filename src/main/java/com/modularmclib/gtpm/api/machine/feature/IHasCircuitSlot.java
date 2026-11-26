package com.modularmclib.gtpm.api.machine.feature;

import com.modularmclib.gtpm.api.machine.fancyconfigurator.CircuitFancyConfigurator;
import com.modularmclib.gtpm.api.machine.trait.NotifiableItemStackHandler;
import com.modularmclib.gtpm.api.sync_system.rpc.RPCMethod;

public interface IHasCircuitSlot {

    default boolean isCircuitSlotEnabled() {
        return true;
    }

    NotifiableItemStackHandler getCircuitInventory();

    @RPCMethod
    default void configureCircuit(int configuration) {
        CircuitFancyConfigurator.writeMachineCircuitConfiguration(getCircuitInventory(), configuration);
    }
}
