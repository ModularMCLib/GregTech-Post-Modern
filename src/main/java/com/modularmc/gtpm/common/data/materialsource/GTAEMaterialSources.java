package com.modularmc.gtpm.common.data.materialsource;

import com.modularmc.gtpm.api.multiblock.autobuild.MaterialSources.Registration;
import com.modularmc.gtpm.integration.ae2.autobuild.MEWirelessMaterialSource;

import com.modularmc.registrate.util.entry.RegistryEntry;

import static com.modularmc.gtpm.common.registry.GTRegistration.REGISTRATE;

public class GTAEMaterialSources {

    public static final RegistryEntry<Registration, Registration> WIRELESS = REGISTRATE
            .materialSource("ae2_wireless", 100, MEWirelessMaterialSource::request)
            .register();

    public static void init() {}
}
