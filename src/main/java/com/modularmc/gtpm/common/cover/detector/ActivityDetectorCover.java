package com.modularmc.gtpm.common.cover.detector;

import com.modularmc.gtpm.api.capability.GTCapabilityHelper;
import com.modularmc.gtpm.api.capability.ICoverable;
import com.modularmc.gtpm.api.cover.CoverDefinition;

import com.modularmc.registrate.localization.MinecraftLocale;
import com.modularmc.registrate.localization.annotation.TransVariants;
import com.modularmc.registrate.localization.annotation.Translation;
import com.modularmc.registrate.localization.annotation.Variant;

import net.minecraft.core.Direction;

@TransVariants(keyPrefix = "gtpm.cover.activity_detector",
               value = {
                       @Variant(id = "message_activity_inverted",
                                source = "Monitoring Inverted Activity Status",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "正在以反相模式监控活动状态")),
                       @Variant(id = "message_activity_normal",
                                source = "Monitoring Normal Activity Status",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "正在以普通模式监控活动状态")),
               })
public class ActivityDetectorCover extends DetectorCover {

    public ActivityDetectorCover(CoverDefinition definition, ICoverable coverHolder, Direction attachedSide) {
        super(definition, coverHolder, attachedSide);
    }

    @Override
    public boolean canAttach() {
        return super.canAttach() &&
                GTCapabilityHelper.getWorkable(coverHolder.getLevel(), coverHolder.getBlockPos(), attachedSide) != null;
    }

    @Override
    protected void update() {
        if (this.coverHolder.getOffsetTimer() % 20 != 0) {
            return;
        }

        var workable = GTCapabilityHelper.getWorkable(coverHolder.getLevel(), coverHolder.getBlockPos(), attachedSide);

        boolean isCurrentlyWorking = workable.isActive() && workable.isWorkingEnabled();

        setRedstoneSignalOutput(isCurrentlyWorking != isInverted() ? 15 : 0);
    }
}
