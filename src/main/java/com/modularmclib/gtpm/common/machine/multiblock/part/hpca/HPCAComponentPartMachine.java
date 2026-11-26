package com.modularmclib.gtpm.common.machine.multiblock.part.hpca;

import com.modularmclib.gtpm.api.blockentity.BlockEntityCreationInfo;
import com.modularmclib.gtpm.api.gui.texture.IGuiTexture;
import com.modularmclib.gtpm.api.machine.multiblock.MultiblockControllerMachine;
import com.modularmclib.gtpm.api.machine.multiblock.part.MultiblockPartMachine;
import com.modularmclib.gtpm.common.data.GTBlocks;
import com.modularmclib.gtpm.common.machine.trait.hpca.HPCAComponentTrait;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.world.item.ItemStack;

import lombok.Getter;

import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public abstract class HPCAComponentPartMachine extends MultiblockPartMachine {

    @Getter
    protected final HPCAComponentTrait hpcaComponentTrait;

    public HPCAComponentPartMachine(BlockEntityCreationInfo info,
                                    HPCAComponentTrait hpcaTrait) {
        super(info);
        this.hpcaComponentTrait = attachTrait(hpcaTrait);
    }

    public abstract boolean isAdvanced();

    public abstract IGuiTexture getComponentIcon();

    /** Creates the holder-scoped default preview used by the HPCA controller. */

    @Override
    public int getDefaultPaintingColor() {
        return 0xFFFFFF;
    }

    @Override
    public boolean canShared(MultiblockControllerMachine controller, String structureName) {
        return false;
    }

    @Override
    public void modifyDrops(List<ItemStack> drops) {
        for (int i = 0; i < drops.size(); ++i) {
            ItemStack drop = drops.get(i);
            if (drop.getItem() == this.getDefinition().getItem()) {
                if (hpcaComponentTrait.isDamaged()) {
                    if (isAdvanced()) {
                        drops.set(i, GTBlocks.ADVANCED_COMPUTER_CASING.asStack());
                    } else {
                        drops.set(i, GTBlocks.COMPUTER_CASING.asStack());
                    }
                }
                break;
            }
        }
    }
}
