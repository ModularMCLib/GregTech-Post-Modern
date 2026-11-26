package com.modularmclib.gtpm.common.cover;

import com.modularmclib.gtpm.api.capability.ICoverable;
import com.modularmclib.gtpm.api.cover.CoverBehavior;
import com.modularmclib.gtpm.api.cover.CoverDefinition;
import com.modularmclib.gtpm.api.sync_system.annotations.RerenderOnChanged;
import com.modularmclib.gtpm.api.sync_system.annotations.SaveField;
import com.modularmclib.gtpm.api.sync_system.annotations.SyncToClient;
import com.modularmclib.gtpm.common.item.behavior.FacadeItemBehaviour;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import lombok.Getter;
import org.jetbrains.annotations.Nullable;

public class FacadeCover extends CoverBehavior {

    @Getter
    @SyncToClient
    @SaveField
    @RerenderOnChanged
    private BlockState facadeState = Blocks.STONE.defaultBlockState();

    public FacadeCover(CoverDefinition definition, ICoverable coverHolder, Direction attachedSide) {
        super(definition, coverHolder, attachedSide);
    }

    @Override
    public void onAttached(ItemStack itemStack, @Nullable ServerPlayer player) {
        super.onAttached(itemStack, player);

        setFacadeState(FacadeItemBehaviour.getFacadeState(itemStack));
    }

    @Override
    public boolean shouldRenderPlate() {
        return facadeState.canOcclude();
    }

    public void setFacadeState(BlockState state) {
        facadeState = state;
    }

    /**
     * @return If the pipe this is placed on and a pipe on the other side should be able to connect
     */
    public boolean canPipePassThrough() {
        return false;
    }

    @Nullable
    public BlockState getAppearance(@Nullable BlockState sourceState, @Nullable BlockPos sourcePos) {
        return facadeState;
    }
}
