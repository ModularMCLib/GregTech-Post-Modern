package com.modularmclib.gtpm.common.machine.trait.multiblock;

import com.modularmclib.gtpm.api.machine.trait.MachineTraitType;
import com.modularmclib.gtpm.api.machine.trait.multiblock.MultiblockMachineTrait;
import com.modularmclib.gtpm.api.sync_system.annotations.RerenderOnChanged;
import com.modularmclib.gtpm.api.sync_system.annotations.SyncToClient;

import net.minecraft.core.BlockPos;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

public class MultiblockFluidRendererTrait extends MultiblockMachineTrait {

    public static final MachineTraitType<MultiblockFluidRendererTrait> TYPE = new MachineTraitType<>(
            MultiblockFluidRendererTrait.class, false);

    @SyncToClient
    @RerenderOnChanged
    private Set<BlockPos> fluidBlockOffsets = new HashSet<>();

    private final Supplier<Set<BlockPos>> offsetGetter;

    public MultiblockFluidRendererTrait(Supplier<Set<BlockPos>> offsetGetter) {
        super();
        this.offsetGetter = offsetGetter;
    }

    public Set<BlockPos> getFluidOffsets() {
        if (fluidBlockOffsets.isEmpty() && getMachine().isFormed()) {
            fluidBlockOffsets = offsetGetter.get();
            syncDataHolder.markClientSyncFieldDirty("fluidBlockOffsets");
        }
        return fluidBlockOffsets;
    }

    @Override
    public void onStructureInvalid(String structureName) {
        fluidBlockOffsets.clear();
    }

    @Override
    public MachineTraitType<?> getTraitType() {
        return TYPE;
    }
}
