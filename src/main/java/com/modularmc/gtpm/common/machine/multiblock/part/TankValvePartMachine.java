package com.modularmc.gtpm.common.machine.multiblock.part;

import com.modularmc.gtpm.api.blockentity.BlockEntityCreationInfo;
import com.modularmc.gtpm.api.capability.recipe.IO;
import com.modularmc.gtpm.api.machine.ConditionalSubscriptionHandler;
import com.modularmc.gtpm.api.machine.multiblock.MultiblockControllerMachine;
import com.modularmc.gtpm.api.machine.multiblock.part.MultiblockPartMachine;
import com.modularmc.gtpm.api.machine.trait.FluidTankProxyTrait;
import com.modularmc.gtpm.common.machine.multiblock.electric.MultiblockTankMachine;
import com.modularmc.gtpm.utils.GTTransferUtils;
import com.modularmc.gtpm.utils.ISubscription;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

import org.jetbrains.annotations.Nullable;

public class TankValvePartMachine extends MultiblockPartMachine {

    private final FluidTankProxyTrait tankProxy;
    private final ConditionalSubscriptionHandler autoIOSubscription;
    private ISubscription tankChangeListener;

    public TankValvePartMachine(BlockEntityCreationInfo info, boolean isMetal) {
        super(info);

        tankProxy = attachTrait(new FluidTankProxyTrait(getIO()));
        autoIOSubscription = new ConditionalSubscriptionHandler(this, this::autoIO, this::shouldAutoIO);
    }

    @Override
    public boolean canShared(MultiblockControllerMachine controller, String structureName) {
        return false;
    }

    @Override
    public void onUnload() {
        super.onUnload();
        autoIOSubscription.updateSubscription();
    }

    @Override
    public void addedToController(MultiblockControllerMachine controller, String structureName) {
        super.addedToController(controller, structureName);

        if (controller instanceof MultiblockTankMachine multiblockTank) {
            tankProxy.setProxy(multiblockTank.getTank());
            unsubscribeChanges();
            tankChangeListener = multiblockTank.getTank().addChangedListener(autoIOSubscription::updateSubscription);
        }
        autoIOSubscription.updateSubscription();
    }

    @Override
    public void removedFromController(MultiblockControllerMachine controller, String structureName) {
        super.removedFromController(controller, structureName);

        tankProxy.setProxy(null);
        autoIOSubscription.updateSubscription();
        unsubscribeChanges();
    }

    private void unsubscribeChanges() {
        if (tankChangeListener != null)
            tankChangeListener.unsubscribe();
    }

    @Override
    public void onRotated(Direction oldFacing, Direction newFacing) {
        super.onRotated(oldFacing, newFacing);
        autoIOSubscription.updateSubscription();
    }

    @Override
    public void onNeighborChanged(Block block, BlockPos fromPos, boolean isMoving) {
        super.onNeighborChanged(block, fromPos, isMoving);
        autoIOSubscription.updateSubscription();
    }

    @Nullable
    private IFluidHandler getTargetTank() {
        return GTTransferUtils.getAdjacentFluidHandler(getLevel(), getBlockPos(), getFrontFacing())
                .orElse(null);
    }

    private void autoIO() {
        if (getOffsetTimer() % 5 == 0) {
            tankProxy.exportToNearby(getFrontFacing());
        }

        autoIOSubscription.updateSubscription();
    }

    private boolean shouldAutoIO() {
        if (!isFormed()) return false;
        if (getFrontFacing() != Direction.DOWN) return false;
        if (tankProxy.isEmpty()) return false;
        return getTargetTank() != null;
    }

    private IO getIO() {
        return shouldAutoIO() ? IO.OUT : IO.BOTH;
    }
}
