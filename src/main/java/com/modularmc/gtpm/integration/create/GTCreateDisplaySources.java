package com.modularmc.gtpm.integration.create;

import com.modularmc.gtpm.api.registry.GTRegistries;
import com.modularmc.gtpm.api.registry.registrate.GTRegistrate;
import com.modularmc.gtpm.common.registry.GTRegistration;
import com.modularmc.gtpm.integration.create.display.ComputerMonitorCoverDisplaySource;

import com.modularmc.registrate.util.entry.RegistryEntry;

import net.minecraft.core.registries.Registries;

import com.simibubi.create.api.behaviour.display.DisplaySource;
import com.simibubi.create.api.registry.registrate.SimpleBuilder;

import java.util.function.Supplier;

public class GTCreateDisplaySources {

    public static final RegistryEntry<DisplaySource, ComputerMonitorCoverDisplaySource> COMPUTER_MONITOR_COVER = registerToAllMachines(
            "computer_monitor_cover", ComputerMonitorCoverDisplaySource::new);

    @SuppressWarnings("SameParameterValue")
    private static <
            T extends DisplaySource> RegistryEntry<DisplaySource, T> registerToAllMachines(String name,
                                                                                           Supplier<T> supplier) {
        SimpleBuilder<DisplaySource, T, GTRegistrate> builder = GTCreateIntegration
                .displaySource(GTRegistration.REGISTRATE, name, supplier);
        builder.onRegisterAfter(Registries.BLOCK_ENTITY_TYPE,
                source -> GTRegistries.MACHINES.forEach(
                        (def) -> DisplaySource.BY_BLOCK_ENTITY.add(def.getBlockEntityType(), source)));
        return builder.register();
    }

    public static void init() {}
}
