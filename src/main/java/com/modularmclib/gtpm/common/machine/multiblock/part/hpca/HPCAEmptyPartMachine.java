package com.modularmclib.gtpm.common.machine.multiblock.part.hpca;

import com.modularmclib.gtpm.api.blockentity.BlockEntityCreationInfo;
import com.modularmclib.gtpm.api.gui.GuiTextures;
import com.modularmclib.gtpm.api.gui.texture.IGuiTexture;
import com.modularmclib.gtpm.common.machine.trait.hpca.HPCAComponentTrait;

public class HPCAEmptyPartMachine extends HPCAComponentPartMachine {

    public HPCAEmptyPartMachine(BlockEntityCreationInfo info) {
        super(info, new HPCAComponentTrait(0, 0, false, false));
    }

    @Override
    public boolean isAdvanced() {
        return false;
    }

    @Override
    public IGuiTexture getComponentIcon() {
        return GuiTextures.HPCA_ICON_EMPTY_COMPONENT;
    }
}
