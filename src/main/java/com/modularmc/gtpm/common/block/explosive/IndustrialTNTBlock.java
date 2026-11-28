package com.modularmc.gtpm.common.block.explosive;

import com.modularmc.gtpm.common.entity.GTExplosiveEntity;
import com.modularmc.gtpm.common.entity.IndustrialTNTEntity;

import com.modularmc.registrate.localization.MinecraftLocale;
import com.modularmc.registrate.localization.annotation.Trans;
import com.modularmc.registrate.localization.annotation.Translation;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockBehaviour;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@Trans(key = "block.gtpm.itnt.drops_tooltip",
       source = "Much larger than TNT, drops all destroyed Blocks as Items",
       translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "爆炸范围比TNT大得多，所有被摧毁的方块都会掉落"))
public class IndustrialTNTBlock extends GTExplosiveBlock {

    public IndustrialTNTBlock(BlockBehaviour.Properties properties) {
        super(properties, true, true, 40);
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip,
                                TooltipFlag flag) {
        tooltip.add(Component.translatable("block.gtpm.itnt.drops_tooltip"));
        super.appendHoverText(stack, context, tooltip, flag);
    }

    @Override
    protected GTExplosiveEntity createEntity(@NotNull Level world, @NotNull BlockPos pos,
                                             @Nullable LivingEntity exploder) {
        float x = pos.getX() + 0.5F, y = pos.getY(), z = pos.getZ() + 0.5F;
        return new IndustrialTNTEntity(world, x, y, z, exploder);
    }
}
