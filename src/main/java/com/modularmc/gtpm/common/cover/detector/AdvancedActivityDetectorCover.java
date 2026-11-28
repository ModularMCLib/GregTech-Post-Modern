package com.modularmc.gtpm.common.cover.detector;

import com.modularmc.gtpm.api.capability.GTCapabilityHelper;
import com.modularmc.gtpm.api.capability.ICoverable;
import com.modularmc.gtpm.api.cover.CoverDefinition;
import com.modularmc.gtpm.utils.RedstoneUtil;

import com.modularmc.registrate.localization.MinecraftLocale;
import com.modularmc.registrate.localization.annotation.TransVariants;
import com.modularmc.registrate.localization.annotation.Translation;
import com.modularmc.registrate.localization.annotation.Variant;

import net.minecraft.core.Direction;

@TransVariants(keyPrefix = "gtpm.cover.activity_detector_advanced",
               value = {
                       @Variant(id = "message_activity_inverted",
                                source = "Monitoring Inverted Progress Status",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "正在以反相模式监控处理状态")),
                       @Variant(id = "message_activity_normal",
                                source = "Monitoring Normal Progress Status",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "正在以普通模式监控处理状态")),
               })
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
