package com.modularmc.gtpm.common.network.packets;

import com.modularmc.gtpm.client.ProspectingMapClientHandler;

import com.modularmc.registrate.util.RegistrateDistExecutor;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.neoforge.network.handling.IPayloadContext;

/**
 * Dist-gated entry points for clientbound packet effects that need client-only classes.
 */
public final class ClientPacketHandlers {

    private ClientPacketHandlers() {}

    public static void handleProspectingMapData(SPacketProspectingMapData packet, IPayloadContext context) {
        RegistrateDistExecutor.unsafeRunWhenOn(Dist.CLIENT,
                () -> () -> ProspectingMapClientHandler.handle(packet, context));
    }
}
