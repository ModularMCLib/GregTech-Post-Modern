package com.modularmclib.gtpm.api.capability;

import com.modularmclib.gtpm.api.gui.texture.IGuiTexture;

import net.minecraft.core.BlockPos;
import net.neoforged.neoforge.items.IItemHandler;

import org.jetbrains.annotations.Nullable;

public interface IMonitorComponent {

    default boolean isMonitor() {
        return false;
    }

    IGuiTexture getComponentIcon();

    BlockPos getBlockPos();

    default @Nullable IItemHandler getDataItems() {
        return null;
    }
}
