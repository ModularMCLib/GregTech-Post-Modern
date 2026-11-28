package com.modularmc.gtpm.common.machine.multiblock.part.hpca;

import com.modularmc.gtpm.api.GTValues;
import com.modularmc.gtpm.api.blockentity.BlockEntityCreationInfo;
import com.modularmc.gtpm.api.gui.GuiTextures;
import com.modularmc.gtpm.api.gui.texture.IGuiTexture;
import com.modularmc.gtpm.common.machine.trait.hpca.HPCAComponentTrait;
import com.modularmc.gtpm.common.machine.trait.hpca.HPCAComputationProviderTrait;

import com.modularmc.registrate.localization.MinecraftLocale;
import com.modularmc.registrate.localization.annotation.TransVariants;
import com.modularmc.registrate.localization.annotation.Translation;
import com.modularmc.registrate.localization.annotation.Variant;

import net.minecraft.MethodsReturnNonnullByDefault;

import lombok.Getter;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
@TransVariants(keyPrefix = "gtpm.machine.hpca",
               value = {
                       @Variant(id = "computation_component.damaged.name",
                                source = "Damaged HPCA Computation Component",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "损坏的HPCA计算组件")),
                       @Variant(id = "advanced_computation_component.damaged.name",
                                source = "Damaged HPCA Advanced Computation Component",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "损坏的HPCA高级计算组件")),
               })
public class HPCAComputationPartMachine extends HPCAComponentPartMachine {

    @Getter
    private final boolean advanced;

    public HPCAComputationPartMachine(BlockEntityCreationInfo info, boolean advanced) {
        super(info, createHPCATrait(advanced));
        this.advanced = advanced;
    }

    public static HPCAComponentTrait createHPCATrait(boolean isAdvanced) {
        int upkeepEUt = GTValues.VA[isAdvanced ? GTValues.IV : GTValues.EV];
        int maxEUt = GTValues.VA[isAdvanced ? GTValues.ZPM : GTValues.LuV];
        int cooling = isAdvanced ? 4 : 2;
        int cwu = isAdvanced ? 16 : 4;
        return new HPCAComputationProviderTrait(upkeepEUt, maxEUt, true, false, cwu, cooling);
    }

    @Override
    public IGuiTexture getComponentIcon() {
        if (hpcaComponentTrait.isDamaged()) {
            return advanced ? GuiTextures.HPCA_ICON_DAMAGED_ADVANCED_COMPUTATION_COMPONENT :
                    GuiTextures.HPCA_ICON_DAMAGED_COMPUTATION_COMPONENT;
        }
        return advanced ? GuiTextures.HPCA_ICON_ADVANCED_COMPUTATION_COMPONENT :
                GuiTextures.HPCA_ICON_COMPUTATION_COMPONENT;
    }
}
