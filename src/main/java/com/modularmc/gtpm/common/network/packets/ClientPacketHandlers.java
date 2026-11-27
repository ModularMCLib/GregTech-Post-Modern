package com.modularmc.gtpm.common.network.packets;

import com.modularmc.gtpm.GTPM;
import com.modularmc.gtpm.client.ProspectingMapClientHandler;

import net.neoforged.neoforge.network.handling.IPayloadContext;

/**
 * Dist-gated entry points for clientbound packet effects that need client-only classes.
 */
public final class ClientPacketHandlers {

    private ClientPacketHandlers() {}

    public static void handleProspectingMapData(SPacketProspectingMapData packet, IPayloadContext context) {
        GTPM.runOnClient(
                () -> () -> ProspectingMapClientHandler.handle(packet, context));
    }
}
