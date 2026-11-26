package com.modularmc.gtpm.common.pipelike.duct;

import com.modularmc.gtpm.api.capability.GTCapabilityHelper;
import com.modularmc.gtpm.api.capability.IHazardParticleContainer;
import com.modularmc.gtpm.api.pipenet.IRoutePath;
import com.modularmc.gtpm.common.blockentity.DuctPipeBlockEntity;
import com.modularmc.gtpm.utils.FacingPos;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DuctRoutePath implements IRoutePath<IHazardParticleContainer> {

    @Getter
    private final DuctPipeBlockEntity targetPipe;
    @NotNull
    @Getter
    private final Direction targetFacing;
    @Getter
    private final int distance;
    @Getter
    private final DuctPipeProperties properties;

    public DuctRoutePath(DuctPipeBlockEntity targetPipe, @NotNull Direction facing, int distance,
                         DuctPipeProperties properties) {
        this.targetPipe = targetPipe;
        this.targetFacing = facing;
        this.distance = distance;
        this.properties = properties;
    }

    @Override
    public @NotNull BlockPos getTargetPipePos() {
        return targetPipe.getBlockPos();
    }

    @Override
    public @Nullable IHazardParticleContainer getHandler(Level world) {
        return GTCapabilityHelper.getHazardContainer(world, getTargetPipePos().relative(targetFacing),
                targetFacing.getOpposite());
    }

    public FacingPos toFacingPos() {
        return new FacingPos(getTargetPipePos(), targetFacing);
    }
}
