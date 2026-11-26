package com.modularmclib.gtpm.common.machine.multiblock.part.hpca;

import com.modularmclib.gtpm.api.GTValues;
import com.modularmclib.gtpm.api.blockentity.BlockEntityCreationInfo;
import com.modularmclib.gtpm.api.gui.GuiTextures;
import com.modularmclib.gtpm.api.gui.texture.IGuiTexture;
import com.modularmclib.gtpm.common.machine.trait.hpca.HPCAComponentTrait;
import com.modularmclib.gtpm.common.machine.trait.hpca.HPCACoolantProviderTrait;

import lombok.Getter;

public class HPCACoolerPartMachine extends HPCAComponentPartMachine {

    @Getter
    private final boolean advanced;

    public HPCACoolerPartMachine(BlockEntityCreationInfo info, boolean advanced) {
        super(info, createHPCATrait(advanced));
        this.advanced = advanced;
    }

    public static HPCAComponentTrait createHPCATrait(boolean isAdvanced) {
        int upkeepEU = isAdvanced ? GTValues.VA[GTValues.IV] : 0;
        int coolingAmount = isAdvanced ? 2 : 1;
        int maxCoolant = isAdvanced ? 8 : 0;
        return new HPCACoolantProviderTrait(upkeepEU, upkeepEU, false, false, coolingAmount, maxCoolant,
                isAdvanced);
    }

    @Override
    public IGuiTexture getComponentIcon() {
        return advanced ? GuiTextures.HPCA_ICON_ACTIVE_COOLER_COMPONENT : GuiTextures.HPCA_ICON_HEAT_SINK_COMPONENT;
    }
}
