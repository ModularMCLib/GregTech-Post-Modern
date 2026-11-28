package com.modularmc.gtpm.common.block;

import com.modularmc.gtpm.GTPM;
import com.modularmc.gtpm.api.block.MaterialBlock;
import com.modularmc.gtpm.api.block.MaterialPipeBlock;
import com.modularmc.gtpm.api.blockentity.PipeBlockEntity;
import com.modularmc.gtpm.api.capability.GTCapability;
import com.modularmc.gtpm.api.data.chemical.material.Material;
import com.modularmc.gtpm.api.data.chemical.material.properties.FluidPipeProperties;
import com.modularmc.gtpm.api.data.chemical.material.properties.PropertyKey;
import com.modularmc.gtpm.api.data.tag.TagPrefix;
import com.modularmc.gtpm.api.pipenet.IPipeNode;
import com.modularmc.gtpm.api.registry.registrate.provider.GTBlockstateProvider;
import com.modularmc.gtpm.client.model.pipe.PipeModel;
import com.modularmc.gtpm.common.blockentity.FluidPipeBlockEntity;
import com.modularmc.gtpm.common.data.GTBlockEntities;
import com.modularmc.gtpm.common.data.GTMaterialBlocks;
import com.modularmc.gtpm.common.pipelike.fluidpipe.FluidPipeType;
import com.modularmc.gtpm.common.pipelike.fluidpipe.LevelFluidPipeNet;
import com.modularmc.gtpm.utils.EntityDamageUtil;
import com.modularmc.gtpm.utils.FormattingUtil;
import com.modularmc.gtpm.utils.GTUtil;

import com.modularmc.registrate.localization.MinecraftLocale;
import com.modularmc.registrate.localization.annotation.Trans;
import com.modularmc.registrate.localization.annotation.TransVariants;
import com.modularmc.registrate.localization.annotation.Translation;
import com.modularmc.registrate.localization.annotation.Variant;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidUtil;

import java.util.List;

@Trans(key = "gtpm.fluid_pipe.channels",
       source = "§eChannels: §f%d",
       translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§e并行管道：§f%d"))
@TransVariants(keyPrefix = "gtpm",
               value = {
                       @Variant(id = "fluid_pipe.capacity",
                                source = "§9Capacity: §f%d mB",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§9容积：§f%d mB")),
                       @Variant(id = "fluid_pipe.acid_proof",
                                source = "§6Can handle Acids",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§6可传输酸性物质")),
                       @Variant(id = "fluid_pipe.cryo_proof",
                                source = "§6Can handle Cryogenics",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§6可传输低温物质")),
                       @Variant(id = "fluid_pipe.gas_proof",
                                source = "§6Can handle Gases",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§6可以传输气体")),
                       @Variant(id = "fluid_pipe.not_gas_proof",
                                source = "§4Gases may leak!",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§4气体可能泄漏！")),
                       @Variant(id = "fluid_pipe.plasma_proof",
                                source = "§6Can handle all Plasmas",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§6可传输所有等离子体")),
                       @Variant(id = "tooltip.fluid_pipe_hold_shift",
                                source = "§7Hold SHIFT to show Fluid Containment Info",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "§7按住§6SHIFT§7以查看流体容器详情")),
               })
public class FluidPipeBlock extends MaterialPipeBlock<FluidPipeType, FluidPipeProperties, LevelFluidPipeNet> {

    public FluidPipeBlock(Properties properties, FluidPipeType fluidPipeType, Material material) {
        super(properties, fluidPipeType, material);
    }

    @Override
    protected FluidPipeProperties createProperties(FluidPipeType fluidPipeType, Material material) {
        return fluidPipeType.modifyProperties(material.getProperty(PropertyKey.FLUID_PIPE));
    }

    public void attachCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlock(Capabilities.FluidHandler.BLOCK, (level, pos, state, blockEntity, side) -> {
            if (blockEntity instanceof FluidPipeBlockEntity fluidPipeBlockEntity) {
                if (side != null && fluidPipeBlockEntity.isConnected(side)) {
                    return fluidPipeBlockEntity.getTankList(side);
                }
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
    protected FluidPipeProperties createMaterialData() {
        return material.getProperty(PropertyKey.FLUID_PIPE);
    }

    @Override
    public LevelFluidPipeNet getWorldPipeNet(ServerLevel level) {
        return LevelFluidPipeNet.getOrCreate(level);
    }

    @Override
    public BlockEntityType<? extends PipeBlockEntity<FluidPipeType, FluidPipeProperties>> getBlockEntityType() {
        return GTBlockEntities.FLUID_PIPE.get();
    }

    @Override
    public boolean canPipesConnect(IPipeNode<FluidPipeType, FluidPipeProperties> selfTile, Direction side,
                                   IPipeNode<FluidPipeType, FluidPipeProperties> sideTile) {
        return selfTile instanceof FluidPipeBlockEntity && sideTile instanceof FluidPipeBlockEntity;
    }

    @Override
    public boolean canPipeConnectToBlock(IPipeNode<FluidPipeType, FluidPipeProperties> selfTile,
                                         Direction side, Level level, BlockPos pos) {
        return FluidUtil.getFluidHandler(level, pos, side.getOpposite()).isPresent();
    }

    @Override
    public PipeModel createPipeModel(GTBlockstateProvider provider) {
        return pipeType.createPipeModel(this, material, provider);
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip,
                                TooltipFlag flag) {
        super.appendHoverText(stack, context, tooltip, flag);
        FluidPipeProperties properties = createProperties(defaultBlockState(), stack);

        tooltip.add(Component.translatable("gtpm.universal.tooltip.fluid_transfer_rate", properties.getThroughput()));
        tooltip.add(Component.translatable("gtpm.fluid_pipe.max_temperature",
                FormattingUtil.formatTemperature(properties.getMaxFluidTemperature())));

        if (properties.getChannels() > 1) {
            tooltip.add(Component.translatable("gtpm.fluid_pipe.channels", properties.getChannels()));
        }

        if (!GTUtil.isShiftDown()) {
            tooltip.add(Component.translatable("gtpm.tooltip.fluid_pipe_hold_shift"));
            return;
        }

        if (properties.isGasProof())
            tooltip.add(Component.translatable("gtpm.fluid_pipe.gas_proof"));
        else
            tooltip.add(Component.translatable("gtpm.fluid_pipe.not_gas_proof"));

        if (properties.isAcidProof()) tooltip.add(Component.translatable("gtpm.fluid_pipe.acid_proof"));
        if (properties.isCryoProof()) tooltip.add(Component.translatable("gtpm.fluid_pipe.cryo_proof"));
        if (properties.isPlasmaProof()) tooltip.add(Component.translatable("gtpm.fluid_pipe.plasma_proof"));
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        // dont apply damage if there is a frame box
        var pipeNode = getPipeTile(level, pos);
        if (pipeNode == null) {
            GTPM.LOGGER.error("Pipe was null");
            return;
        }
        if (!pipeNode.getFrameMaterial().isNull()) {
            BlockState frameState = GTMaterialBlocks.MATERIAL_BLOCKS.get(TagPrefix.frameGt, pipeNode.getFrameMaterial())
                    .getDefaultState();
            ((MaterialBlock) frameState.getBlock()).entityInside(frameState, level, pos, entity);
            return;
        }
        if (level.isClientSide) return;
        if (level.getBlockEntity(pos) == null) return;
        FluidPipeBlockEntity pipe = (FluidPipeBlockEntity) level.getBlockEntity(pos);

        if (pipe.getOffsetTimer() % 10 == 0) {
            if (entity instanceof LivingEntity livingEntity) {
                if (pipe.getFluidTanks().length > 1) {
                    // apply temperature damage for the hottest and coldest pipe (multi fluid pipes)
                    int maxTemperature = Integer.MIN_VALUE;
                    int minTemperature = Integer.MAX_VALUE;
                    for (var tank : pipe.getFluidTanks()) {
                        FluidStack stack = tank.getFluid();
                        if (!tank.getFluid().isEmpty()) {
                            maxTemperature = Math.max(maxTemperature,
                                    stack.getFluid().getFluidType().getTemperature(stack));
                            minTemperature = Math.min(minTemperature,
                                    stack.getFluid().getFluidType().getTemperature(stack));
                        }
                    }
                    if (maxTemperature != Integer.MIN_VALUE) {
                        EntityDamageUtil.applyTemperatureDamage(livingEntity, maxTemperature, 1.0F, 20);
                    }
                    if (minTemperature != Integer.MAX_VALUE) {
                        EntityDamageUtil.applyTemperatureDamage(livingEntity, minTemperature, 1.0F, 20);
                    }
                } else {
                    var tank = pipe.getFluidTanks()[0];
                    if (!tank.getFluid().isEmpty()) {
                        // Apply temperature damage for the pipe (single fluid pipes)
                        FluidStack stack = tank.getFluid();
                        EntityDamageUtil.applyTemperatureDamage(livingEntity,
                                stack.getFluid().getFluidType().getTemperature(stack), 1.0F, 20);
                    }
                }
            }
        }
        super.entityInside(state, level, pos, entity);
    }
}
