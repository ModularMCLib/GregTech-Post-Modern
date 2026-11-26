package com.modularmc.gtpm.common.machine.multiblock.part.hpca;

import com.modularmc.gtpm.api.GTValues;
import com.modularmc.gtpm.api.blockentity.BlockEntityCreationInfo;
import com.modularmc.gtpm.api.gui.GuiTextures;
import com.modularmc.gtpm.api.gui.texture.IGuiTexture;
import com.modularmc.gtpm.common.machine.trait.hpca.HPCAComponentTrait;

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
