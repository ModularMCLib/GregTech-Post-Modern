package com.modularmc.gtpm.core.mixins;

import net.minecraft.server.network.ServerGamePacketListenerImpl;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ServerGamePacketListenerImpl.class)
public interface ServerGamePacketListenerFloatingTickAccessor {

    @Accessor
    void setAboveGroundTickCount(int aboveGroundTickCount);
}
