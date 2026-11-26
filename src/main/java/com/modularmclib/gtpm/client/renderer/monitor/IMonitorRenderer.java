package com.modularmclib.gtpm.client.renderer.monitor;

import com.modularmclib.gtpm.common.machine.multiblock.electric.CentralMonitorMachine;
import com.modularmclib.gtpm.common.machine.multiblock.electric.monitor.MonitorGroup;

import net.minecraft.client.renderer.MultiBufferSource;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import com.mojang.blaze3d.vertex.PoseStack;

public interface IMonitorRenderer {

    @OnlyIn(Dist.CLIENT)
    void render(CentralMonitorMachine machine, MonitorGroup group, float partialTick, PoseStack poseStack,
                MultiBufferSource buffer, int packedLight, int packedOverlay);
}
