package com.modularmc.gtpm.client;

import com.modularmc.gtpm.GTPM;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.javafmlmod.FMLModContainer;

@Mod(value = GTPM.MOD_ID, dist = Dist.CLIENT)
public class GTCEuClient {

    public GTCEuClient(IEventBus modBus, FMLModContainer container) {
        ClientProxy.init(modBus);
    }
}
