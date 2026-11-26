package com.modularmc.gtpm.common.pipelike.optical;

import com.modularmc.gtpm.api.capability.GTCapability;
import com.modularmc.gtpm.api.capability.GTCapabilityHelper;
import com.modularmc.gtpm.api.capability.IDataAccessMachine;
import com.modularmc.gtpm.api.capability.IOpticalComputationProvider;
import com.modularmc.gtpm.api.capability.IOpticalDataAccessHatch;
import com.modularmc.gtpm.api.computation.ComputationPort;
import com.modularmc.gtpm.api.pipenet.IRoutePath;
import com.modularmc.gtpm.common.blockentity.OpticalPipeBlockEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class OpticalRoutePath implements IRoutePath<IDataAccessMachine> {

    @Getter
    private final OpticalPipeBlockEntity targetPipe;
    @Getter
    private final Direction targetFacing;
    @Getter
    private final int distance;

    public OpticalRoutePath(OpticalPipeBlockEntity targetPipe, Direction targetFacing, int distance) {
        this.targetPipe = targetPipe;
        this.targetFacing = targetFacing;
        this.distance = distance;
    }

    @Nullable
    public IOpticalDataAccessHatch getDataHatch() {
        IDataAccessMachine dataAccessHatch = getTargetCapability(GTCapability.CAPABILITY_DATA_ACCESS,
                targetPipe.getLevel());
        return dataAccessHatch instanceof IOpticalDataAccessHatch opticalHatch ? opticalHatch : null;
    }

    @Nullable
    public IOpticalComputationProvider getComputationHatch() {
        return getTargetCapability(GTCapability.CAPABILITY_COMPUTATION_PROVIDER, targetPipe.getLevel());
    }

    @Nullable
    public ComputationPort getComputationPort(Level world) {
        return GTCapabilityHelper.getComputationPort(world, getTargetPipePos().relative(targetFacing),
                targetFacing.getOpposite());
    }

    @Override
    public @NotNull BlockPos getTargetPipePos() {
        return targetPipe.getBlockPos();
    }

    @Nullable
    @Override
    public IDataAccessMachine getHandler(Level world) {
        return GTCapabilityHelper.getDataAccess(world, getTargetPipePos().relative(targetFacing),
                targetFacing.getOpposite());
    }
}
