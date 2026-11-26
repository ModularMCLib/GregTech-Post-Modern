package com.modularmc.gtpm.common.item;

import com.modularmc.gtpm.api.block.PipeBlock;
import com.modularmc.gtpm.api.item.PipeBlockItem;
import com.modularmc.gtpm.common.block.LaserPipeBlock;

import net.minecraft.client.color.item.ItemColor;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class LaserPipeBlockItem extends PipeBlockItem {

    public LaserPipeBlockItem(PipeBlock block, Properties properties) {
        super(block, properties);
    }

    @Override
    public LaserPipeBlock getBlock() {
        return (LaserPipeBlock) super.getBlock();
    }

    @OnlyIn(Dist.CLIENT)
    public static ItemColor tintColor() {
        return (itemStack, index) -> {
            if (itemStack.getItem() instanceof LaserPipeBlockItem materialBlockItem) {
                return LaserPipeBlock.tintedColor().getColor(materialBlockItem.getBlock().defaultBlockState(), null,
                        null, index);
            }
            return -1;
        };
    }
}
