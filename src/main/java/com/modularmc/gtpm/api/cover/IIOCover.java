package com.modularmc.gtpm.api.cover;

import com.modularmc.gtpm.api.capability.recipe.IO;
import com.modularmc.gtpm.common.cover.data.ManualIOMode;

public interface IIOCover {

    int getTransferRate();

    IO getIo();

    ManualIOMode getManualIOMode();
}
