package com.modularmclib.gtpm.common.machine.multiblock.part.hpca;

import com.modularmclib.gtpm.api.GTValues;
import com.modularmclib.gtpm.api.blockentity.BlockEntityCreationInfo;
import com.modularmclib.gtpm.api.gui.GuiTextures;
import com.modularmclib.gtpm.api.gui.texture.IGuiTexture;
import com.modularmclib.gtpm.common.machine.trait.hpca.HPCAComponentTrait;

public class HPCABridgePartMachine extends HPCAComponentPartMachine {

    public HPCABridgePartMachine(BlockEntityCreationInfo info) {
        super(info, new HPCAComponentTrait(GTValues.VA[GTValues.IV], GTValues.VA[GTValues.IV], false, true));
    }

    @Override
    public boolean isAdvanced() {
        return true;
    }

    @Override
    public IGuiTexture getComponentIcon() {
        return GuiTextures.HPCA_ICON_BRIDGE_COMPONENT;
    }
}
