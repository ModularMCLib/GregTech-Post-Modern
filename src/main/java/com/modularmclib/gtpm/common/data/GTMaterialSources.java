package com.modularmclib.gtpm.common.data;

import com.modularmclib.gtpm.GTPM;
import com.modularmclib.gtpm.api.multiblock.autobuild.MaterialSources.Registration;
import com.modularmclib.gtpm.api.multiblock.autobuild.PlayerInventoryMaterialSource;
import com.modularmclib.gtpm.common.data.materialsource.GTAEMaterialSources;

import com.tterrag.registrate.util.entry.RegistryEntry;

import static com.modularmclib.gtpm.common.registry.GTRegistration.REGISTRATE;

public class GTMaterialSources {

    public static final RegistryEntry<Registration, Registration> PLAYER_INVENTORY = REGISTRATE
            .materialSource("player_inventory", 0, PlayerInventoryMaterialSource::request).register();

    public static void init() {
        if (GTPM.MODS.AE2.isLoaded()) {
            GTAEMaterialSources.init();
        }
    }
}
