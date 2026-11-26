package com.modularmclib.gtpm.common.cover.detector;

import com.modularmclib.gtpm.api.capability.GTCapabilityHelper;
import com.modularmclib.gtpm.api.capability.ICoverable;
import com.modularmclib.gtpm.api.cover.CoverDefinition;
import com.modularmclib.gtpm.utils.RedstoneUtil;

import net.minecraft.core.Direction;

public class AdvancedActivityDetectorCover extends ActivityDetectorCover {

    public AdvancedActivityDetectorCover(CoverDefinition definition, ICoverable coverHolder, Direction attachedSide) {
        super(definition, coverHolder, attachedSide);
    }

    @Override
    protected void update() {
        if (this.coverHolder.getOffsetTimer() % 20 != 0)
            return;

        var workable = GTCapabilityHelper.getWorkable(coverHolder.getLevel(), coverHolder.getBlockPos(), attachedSide);
        if (workable == null || workable.getMaxProgress() == 0) {
            setRedstoneSignalOutput(0);
            return;
        }

        int outputAmount = RedstoneUtil.computeRedstoneValue(workable.getProgress(), workable.getMaxProgress(),
                isInverted());

        // nonstandard logic for handling off state
        if (!workable.isWorkingEnabled() || !workable.isActive())
            outputAmount = 0;

        setRedstoneSignalOutput(outputAmount);
    }
}
