package com.modularmclib.gtpm.common.data.materialsource;

import com.modularmclib.gtpm.api.multiblock.autobuild.MaterialSources.Registration;
import com.modularmclib.gtpm.integration.ae2.autobuild.MEWirelessMaterialSource;

import com.tterrag.registrate.util.entry.RegistryEntry;

import static com.modularmclib.gtpm.common.registry.GTRegistration.REGISTRATE;

public class GTAEMaterialSources {

    public static final RegistryEntry<Registration, Registration> WIRELESS = REGISTRATE
            .materialSource("ae2_wireless", 100, MEWirelessMaterialSource::request)
            .register();

    public static void init() {}
}
