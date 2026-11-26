package com.modularmc.gtpm.common.item;

import com.modularmc.gtpm.GTPM;
import com.modularmc.gtpm.api.block.MetaMachineBlock;
import com.modularmc.gtpm.api.item.MetaMachineItem;
import com.modularmc.gtpm.api.misc.forge.QuantumFluidHandlerItemStack;
import com.modularmc.gtpm.common.machine.storage.QuantumTankMachine;

import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

public class QuantumTankMachineItem extends MetaMachineItem {

    public QuantumTankMachineItem(MetaMachineBlock block, Properties properties) {
        super(block, properties);
    }

    public static QuantumTankMachineItem create(MetaMachineBlock block, Properties props) {
        return new QuantumTankMachineItem(block, props);
    }

    public void attachCapabilities(RegisterCapabilitiesEvent event) {
        long capacity = 0L;

        if (!QuantumTankMachine.TANK_CAPACITY.containsKey(getDefinition())) {
            GTPM.LOGGER.error(
                    "Quantum tank " + getDefinition().getName() +
                            " does not have a registered TANK_CAPACITY, using capacity 0.");
        } else {
            capacity = QuantumTankMachine.TANK_CAPACITY.getLong(getDefinition());
        }

        final long finalCapacity = capacity;

        event.registerItem(
                Capabilities.FluidHandler.ITEM,
                (stack, ignored) -> new QuantumFluidHandlerItemStack(stack, finalCapacity),
                this);
    }
}
