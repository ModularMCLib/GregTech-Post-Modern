package com.modularmc.gtpm.common.machine.multiblock.part.monitor;

import com.modularmc.gtpm.api.blockentity.BlockEntityCreationInfo;
import com.modularmc.gtpm.api.capability.IMonitorComponent;
import com.modularmc.gtpm.api.machine.multiblock.part.MultiblockPartMachine;

public abstract class MonitorComponentPartMachine extends MultiblockPartMachine
                                                  implements IMonitorComponent {

    public MonitorComponentPartMachine(BlockEntityCreationInfo info) {
        super(info);
    }

    /** Creates the holder-scoped default preview used by the central monitor. */
}
