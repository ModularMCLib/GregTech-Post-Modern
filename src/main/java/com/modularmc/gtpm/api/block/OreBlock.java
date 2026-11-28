package com.modularmc.gtpm.api.block;

import com.modularmc.gtpm.GTPM;
import com.modularmc.gtpm.api.data.chemical.material.Material;
import com.modularmc.gtpm.api.data.tag.TagPrefix;
import com.modularmc.gtpm.client.renderer.block.OreBlockRenderer;
import com.modularmc.gtpm.config.GTPMConfigHolder;
import com.modularmc.gtpm.integration.map.cache.server.ServerCache;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import org.jetbrains.annotations.NotNull;

public class OreBlock extends MaterialBlock {

    public OreBlock(Properties properties, TagPrefix tagPrefix, Material material) {
        super(properties, tagPrefix, material, false);
        if (GTPM.isClientSide()) {
            OreBlockRenderer.create(this);
        }
    }

    @Override
    protected @NotNull InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos,
                                                        Player player, BlockHitResult hitResult) {
        if (!level.isClientSide) {
            ServerCache.instance.prospectByOreMaterial(
                    level.dimension(),
                    this.material,
                    pos,
                    (ServerPlayer) player,
                    GTPMConfigHolder.INSTANCE.compat.minimap.oreBlockProspectRange);
        }
        return InteractionResult.PASS;
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos,
                                              Player player, InteractionHand hand, BlockHitResult hit) {
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }
}
