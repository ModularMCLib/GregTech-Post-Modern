package com.modularmc.gtpm.common.machine.multiblock.part.monitor;

import com.modularmc.gtpm.GTPM;
import com.modularmc.gtpm.api.blockentity.BlockEntityCreationInfo;
import com.modularmc.gtpm.api.gui.GuiTextures;
import com.modularmc.gtpm.api.gui.texture.IGuiTexture;

public class MonitorPartMachine extends MonitorComponentPartMachine {

    public MonitorPartMachine(BlockEntityCreationInfo info) {
        super(info);
    }

    @Override
    public boolean isMonitor() {
        return true;
    }

    @Override
    public IGuiTexture getComponentIcon() {
        return GuiTextures.spirit(GTPM.id("item/computer_monitor_cover"));
    }
}
