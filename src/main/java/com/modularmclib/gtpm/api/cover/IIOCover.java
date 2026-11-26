package com.modularmclib.gtpm.api.cover;

import com.modularmclib.gtpm.api.capability.recipe.IO;
import com.modularmclib.gtpm.common.cover.data.ManualIOMode;

public interface IIOCover {

    int getTransferRate();

    IO getIo();

    ManualIOMode getManualIOMode();
}
