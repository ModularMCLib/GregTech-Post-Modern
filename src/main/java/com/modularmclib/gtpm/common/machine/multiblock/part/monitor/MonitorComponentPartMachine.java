package com.modularmclib.gtpm.common.machine.multiblock.part.monitor;

import com.modularmclib.gtpm.api.blockentity.BlockEntityCreationInfo;
import com.modularmclib.gtpm.api.capability.IMonitorComponent;
import com.modularmclib.gtpm.api.machine.multiblock.part.MultiblockPartMachine;

public abstract class MonitorComponentPartMachine extends MultiblockPartMachine
                                                  implements IMonitorComponent {

    public MonitorComponentPartMachine(BlockEntityCreationInfo info) {
        super(info);
    }

    /** Creates the holder-scoped default preview used by the central monitor. */
}
