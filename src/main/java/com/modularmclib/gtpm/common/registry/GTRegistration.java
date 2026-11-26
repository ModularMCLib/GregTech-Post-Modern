package com.modularmclib.gtpm.common.registry;

import com.modularmclib.gtpm.GTPM;
import com.modularmclib.gtpm.api.registry.registrate.GTRegistrate;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;

public class GTRegistration {

    public static final GTRegistrate REGISTRATE = GTRegistrate.create(GTPM.MOD_ID, false);
    static {
        GTRegistration.REGISTRATE.defaultCreativeTab((ResourceKey<CreativeModeTab>) null);
    }

    private GTRegistration() {/**/}
}
