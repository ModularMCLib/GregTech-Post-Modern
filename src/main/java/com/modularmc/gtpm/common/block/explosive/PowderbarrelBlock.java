package com.modularmc.gtpm.common.block.explosive;

import com.modularmc.gtpm.common.entity.GTExplosiveEntity;
import com.modularmc.gtpm.common.entity.PowderbarrelEntity;

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

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@Trans(key = "block.gtpm.powderbarrel.drops_tooltip",
       source = "Slightly larger than TNT, drops all destroyed Blocks as Items",
       translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "爆炸范围略大于TNT，所有被摧毁的方块都会掉落"))
public class PowderbarrelBlock extends GTExplosiveBlock {

    public PowderbarrelBlock(Properties properties) {
        super(properties, false, true, 100);
    }

    @Override
    protected GTExplosiveEntity createEntity(@NotNull Level world, @NotNull BlockPos pos,
                                             @Nullable LivingEntity exploder) {
        float x = pos.getX() + 0.5F, y = pos.getY(), z = pos.getZ() + 0.5F;
        return new PowderbarrelEntity(world, x, y, z, exploder);
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip,
                                TooltipFlag flag) {
        tooltip.add(Component.translatable("block.gtpm.powderbarrel.drops_tooltip"));
        super.appendHoverText(stack, context, tooltip, flag);
    }
}
