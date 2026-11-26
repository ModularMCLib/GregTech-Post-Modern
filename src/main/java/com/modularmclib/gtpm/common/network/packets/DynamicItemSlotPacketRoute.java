package com.modularmclib.gtpm.common.network.packets;

import com.modularmclib.gtpm.GTPM;
import com.modularmclib.gtpm.api.gui.factory.GTDynamicItemSlotContainerMenu;
import com.modularmclib.gtpm.api.gui.slot.DynamicItemSlotOpeningToken;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import org.jetbrains.annotations.Nullable;

/** Routes dynamic-slot payloads only to the exact active container id on the expected logical side. */
final class DynamicItemSlotPacketRoute {

    private DynamicItemSlotPacketRoute() {}

    @Nullable
    static GTDynamicItemSlotContainerMenu resolveClient(IPayloadContext context,
                                                        DynamicItemSlotOpeningToken token,
                                                        ResourceLocation packetId) {
        Player player = context.player();
        if (!player.level().isClientSide ||
                !(player.containerMenu instanceof GTDynamicItemSlotContainerMenu menu) ||
                menu.containerId != token.containerId()) {
            GTPM.LOGGER.warn("Ignoring dynamic item-slot packet {} for inactive client container {} from {}",
                    packetId, token.containerId(), player.getGameProfile().getName());
            return null;
        }
        return menu;
    }

    @Nullable
    static GTDynamicItemSlotContainerMenu resolveServer(IPayloadContext context,
                                                        DynamicItemSlotOpeningToken token,
                                                        ResourceLocation packetId) {
        if (!(context.player() instanceof ServerPlayer player) ||
                !(player.containerMenu instanceof GTDynamicItemSlotContainerMenu menu) ||
                menu.containerId != token.containerId()) {
            GTPM.LOGGER.warn("Ignoring dynamic item-slot packet {} for inactive server container {} from {}",
                    packetId, token.containerId(), context.player().getGameProfile().getName());
            return null;
        }
        return menu;
    }
}
