package com.modularmclib.gtpm.common.block;

import com.modularmclib.gtpm.api.block.MaterialPipeBlock;
import com.modularmclib.gtpm.api.blockentity.PipeBlockEntity;
import com.modularmclib.gtpm.api.capability.GTCapability;
import com.modularmclib.gtpm.api.data.chemical.material.Material;
import com.modularmclib.gtpm.api.data.chemical.material.properties.ItemPipeProperties;
import com.modularmclib.gtpm.api.data.chemical.material.properties.PropertyKey;
import com.modularmclib.gtpm.api.pipenet.IPipeNode;
import com.modularmclib.gtpm.api.registry.registrate.provider.GTBlockstateProvider;
import com.modularmclib.gtpm.client.model.pipe.PipeModel;
import com.modularmclib.gtpm.common.blockentity.ItemPipeBlockEntity;
import com.modularmclib.gtpm.common.data.GTBlockEntities;
import com.modularmclib.gtpm.common.pipelike.item.ItemPipeType;
import com.modularmclib.gtpm.common.pipelike.item.LevelItemPipeNet;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

import java.util.List;

public class ItemPipeBlock extends MaterialPipeBlock<ItemPipeType, ItemPipeProperties, LevelItemPipeNet> {

    public ItemPipeBlock(Properties properties, ItemPipeType itemPipeType, Material material) {
        super(properties, itemPipeType, material);
    }

    @Override
    protected ItemPipeProperties createProperties(ItemPipeType itemPipeType, Material material) {
        return itemPipeType.modifyProperties(material.getProperty(PropertyKey.ITEM_PIPE));
    }

    public void attachCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlock(Capabilities.ItemHandler.BLOCK, (level, pos, state, blockEntity, side) -> {
            if (level.isClientSide) return null;
            if (blockEntity instanceof ItemPipeBlockEntity itemPipeBlockEntity) {
                if (side != null && itemPipeBlockEntity.isConnected(side)) {
                    itemPipeBlockEntity.ensureHandlersInitialized();
                    itemPipeBlockEntity.checkNetwork();
                    return itemPipeBlockEntity.getHandler(side, true);
                }
            }
            return null;
        }, this);
        event.registerBlock(GTCapability.CAPABILITY_COVERABLE, (level, pos, state, blockEntity, side) -> {
            if (blockEntity instanceof PipeBlockEntity<?, ?> fluidPipeBlockEntity) {
                return fluidPipeBlockEntity.getCoverContainer();
            }
            return null;
        }, this);
    }

    @Override
    protected ItemPipeProperties createMaterialData() {
        return material.getProperty(PropertyKey.ITEM_PIPE);
    }

    @Override
    public PipeModel createPipeModel(GTBlockstateProvider provider) {
        return pipeType.createPipeModel(this, material, provider);
    }

    @Override
    public LevelItemPipeNet getWorldPipeNet(ServerLevel level) {
        return LevelItemPipeNet.getOrCreate(level);
    }

    @Override
    public BlockEntityType<? extends PipeBlockEntity<ItemPipeType, ItemPipeProperties>> getBlockEntityType() {
        return GTBlockEntities.ITEM_PIPE.get();
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip,
                                TooltipFlag flag) {
        super.appendHoverText(stack, context, tooltip, flag);
        ItemPipeProperties properties = createProperties(defaultBlockState(), stack);

        if (properties.getTransferRate() % 1 != 0) {
            tooltip.add(Component.translatable("gtpm.universal.tooltip.item_transfer_rate",
                    (int) ((properties.getTransferRate() * 64) + 0.5)));
        } else {
            tooltip.add(Component.translatable("gtpm.universal.tooltip.item_transfer_rate_stacks",
                    (int) properties.getTransferRate()));
        }

        tooltip.add(Component.translatable("gtpm.item_pipe.priority", properties.getPriority()));
    }

    @Override
    public boolean canPipesConnect(IPipeNode<ItemPipeType, ItemPipeProperties> selfTile, Direction side,
                                   IPipeNode<ItemPipeType, ItemPipeProperties> sideTile) {
        return selfTile instanceof ItemPipeBlockEntity && sideTile instanceof ItemPipeBlockEntity;
    }

    @Override
    public boolean canPipeConnectToBlock(IPipeNode<ItemPipeType, ItemPipeProperties> selfTile, Direction side,
                                         Level level, BlockPos pos) {
        return level.getCapability(Capabilities.ItemHandler.BLOCK, pos, side.getOpposite()) != null;
    }
}
