package com.modularmc.gtpm.common.machine.multiblock.part;

import com.modularmc.gtpm.api.GTValues;
import com.modularmc.gtpm.api.blockentity.BlockEntityCreationInfo;
import com.modularmc.gtpm.api.capability.recipe.IO;
import com.modularmc.gtpm.api.machine.feature.IDataInfoProvider;
import com.modularmc.gtpm.api.machine.multiblock.MultiblockControllerMachine;
import com.modularmc.gtpm.api.machine.multiblock.part.TieredIOPartMachine;
import com.modularmc.gtpm.api.machine.trait.NotifiableLaserContainer;
import com.modularmc.gtpm.api.sync_system.annotations.SaveField;
import com.modularmc.gtpm.common.item.behavior.PortableScannerBehavior;

import net.minecraft.network.chat.Component;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LaserHatchPartMachine extends TieredIOPartMachine
                                   implements IDataInfoProvider {

    @SaveField
    private NotifiableLaserContainer buffer;

    public LaserHatchPartMachine(BlockEntityCreationInfo info, IO io, int tier, int amperage) {
        super(info, tier, io);
        if (io == IO.OUT) {
            this.buffer = attachTrait(NotifiableLaserContainer.emitterContainer(GTValues.V[tier] * 64L * amperage,
                    GTValues.V[tier], amperage));
            this.buffer.setSideOutputCondition(s -> s == getFrontFacing());
        } else {
            this.buffer = attachTrait(NotifiableLaserContainer.receiverContainer(GTValues.V[tier] * 64L * amperage,
                    GTValues.V[tier], amperage));
            this.buffer.setSideInputCondition(s -> s == getFrontFacing());
        }
    }

    @Override
    public boolean canShared(MultiblockControllerMachine controller, String structureName) {
        return false;
    }

    @NotNull
    @Override
    public List<Component> getDataInfo(PortableScannerBehavior.DisplayMode mode) {
        if (mode == PortableScannerBehavior.DisplayMode.SHOW_ALL ||
                mode == PortableScannerBehavior.DisplayMode.SHOW_ELECTRICAL_INFO) {
            return Collections.singletonList(Component.translatable(
                    String.format("%d/%d EU", buffer.getEnergyStored(), buffer.getEnergyCapacity())));
        }
        return new ArrayList<>();
    }

    //////////////////////////////////////
    // ********** Misc **********//
    //////////////////////////////////////

    @Override
    public int tintColor(int index) {
        if (index == 2) {
            return GTValues.VC[getTier()];
        }
        return super.tintColor(index);
    }
}
