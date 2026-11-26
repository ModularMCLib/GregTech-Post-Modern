package com.modularmc.gtpm.common.block;

import com.modularmc.gtpm.GTPM;
import com.modularmc.gtpm.api.block.PipeBlock;
import com.modularmc.gtpm.api.blockentity.PipeBlockEntity;
import com.modularmc.gtpm.api.capability.GTCapability;
import com.modularmc.gtpm.api.machine.MetaMachine;
import com.modularmc.gtpm.api.pipenet.IPipeNode;
import com.modularmc.gtpm.api.registry.registrate.provider.GTBlockstateProvider;
import com.modularmc.gtpm.client.model.pipe.PipeModel;
import com.modularmc.gtpm.common.blockentity.DuctPipeBlockEntity;
import com.modularmc.gtpm.common.data.GTBlockEntities;
import com.modularmc.gtpm.common.machine.trait.hazard.EnvironmentalHazardCleanerTrait;
import com.modularmc.gtpm.common.machine.trait.hazard.EnvironmentalHazardEmitterTrait;
import com.modularmc.gtpm.common.pipelike.duct.DuctPipeProperties;
import com.modularmc.gtpm.common.pipelike.duct.DuctPipeType;
import com.modularmc.gtpm.common.pipelike.duct.LevelDuctPipeNet;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

import org.jetbrains.annotations.Nullable;

import java.util.List;

public class DuctPipeBlock extends PipeBlock<DuctPipeType, DuctPipeProperties, LevelDuctPipeNet> {

    private final DuctPipeProperties properties;

    public DuctPipeBlock(Properties properties, DuctPipeType type) {
        super(properties, type);
        this.properties = new DuctPipeProperties(type.getRateMultiplier());
    }

    @Override
    public LevelDuctPipeNet getWorldPipeNet(ServerLevel world) {
        return LevelDuctPipeNet.getOrCreate(world);
    }

    @Override
    public BlockEntityType<? extends PipeBlockEntity<DuctPipeType, DuctPipeProperties>> getBlockEntityType() {
        return GTBlockEntities.DUCT_PIPE.get();
    }

    @Override
    public DuctPipeProperties createRawData(BlockState pState, @Nullable ItemStack pStack) {
        return properties;
    }

    @Override
    public DuctPipeProperties createProperties(IPipeNode<DuctPipeType, DuctPipeProperties> pipeTile) {
        DuctPipeType pipeType = pipeTile.getPipeType();
        if (pipeType == null) return getFallbackType();
        return this.pipeType.modifyProperties(properties);
    }

    public void attachCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlock(GTCapability.CAPABILITY_HAZARD_CONTAINER, (level, pos, state, blockEntity, side) -> {
            if (blockEntity instanceof DuctPipeBlockEntity ductPipeBlockEntity) {
                if (level.isClientSide)
                    return ductPipeBlockEntity.clientCapability;

                ductPipeBlockEntity.ensureHandlersInitialized();
                ductPipeBlockEntity.checkNetwork();
                return ductPipeBlockEntity.getHandlers().getOrDefault(side, ductPipeBlockEntity.getDefaultHandler());
            }
            return null;
        }, this);
        event.registerBlock(GTCapability.CAPABILITY_COVERABLE, (level, pos, state, blockEntity, side) -> {
            if (blockEntity instanceof PipeBlockEntity<?, ?> pipe) {
                return pipe.getCoverContainer();
            }
            return null;
        }, this);
    }

    @Override
    public DuctPipeProperties getFallbackType() {
        return properties;
    }

    @Override
    public PipeModel createPipeModel(GTBlockstateProvider provider) {
        return new PipeModel(this, provider, this.pipeType.getThickness(),
                GTPM.id("block/pipe/pipe_duct_side"), GTPM.id("block/pipe/pipe_duct_in"));
    }

    @Override
    public boolean canPipesConnect(IPipeNode<DuctPipeType, DuctPipeProperties> selfTile, Direction side,
                                   IPipeNode<DuctPipeType, DuctPipeProperties> sideTile) {
        return selfTile instanceof DuctPipeBlockEntity && sideTile instanceof DuctPipeBlockEntity;
    }

    public boolean canPipeConnectToBlock(IPipeNode<DuctPipeType, DuctPipeProperties> selfTile,
                                         Direction side, Level level, BlockPos pos) {
        return (level.getCapability(GTCapability.CAPABILITY_HAZARD_CONTAINER, pos, side.getOpposite()) != null ||
                level.getBlockEntity(pos) instanceof MetaMachine metaMachine &&
                        (metaMachine.getTrait(EnvironmentalHazardCleanerTrait.TYPE) != null ||
                                metaMachine.getTrait(EnvironmentalHazardEmitterTrait.TYPE) != null));
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip,
                                TooltipFlag flag) {
        super.appendHoverText(stack, context, tooltip, flag);
        tooltip.add(Component.translatable("gtpm.duct_pipe.transfer_rate",
                this.pipeType.modifyProperties(this.properties).getTransferRate()));
    }
}
