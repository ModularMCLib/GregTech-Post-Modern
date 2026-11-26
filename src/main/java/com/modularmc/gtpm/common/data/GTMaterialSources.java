package com.modularmc.gtpm.common.data;

import com.modularmc.gtpm.GTPM;
import com.modularmc.gtpm.api.multiblock.autobuild.MaterialSources.Registration;
import com.modularmc.gtpm.api.multiblock.autobuild.PlayerInventoryMaterialSource;
import com.modularmc.gtpm.common.data.materialsource.GTAEMaterialSources;

import com.modularmc.registrate.util.entry.RegistryEntry;

import static com.modularmc.gtpm.common.registry.GTRegistration.REGISTRATE;

public class GTMaterialSources {

    public static final RegistryEntry<Registration, Registration> PLAYER_INVENTORY = REGISTRATE
            .materialSource("player_inventory", 0, PlayerInventoryMaterialSource::request).register();

    public static void init() {
        if (GTPM.MODS.AE2.isLoaded()) {
            GTAEMaterialSources.init();
        }
    }
}
