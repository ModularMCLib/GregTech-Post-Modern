package com.modularmclib.gtpm.common.machine.electric;

import com.modularmclib.gtpm.GTPM;
import com.modularmclib.gtpm.api.GTValues;
import com.modularmclib.gtpm.api.blockentity.BlockEntityCreationInfo;
import com.modularmclib.gtpm.api.capability.IMonitorComponent;
import com.modularmclib.gtpm.api.gui.GuiTextures;
import com.modularmclib.gtpm.api.gui.texture.IGuiTexture;
import com.modularmclib.gtpm.api.machine.multiblock.part.TieredPartMachine;
import com.modularmclib.gtpm.api.machine.trait.NotifiableEnergyContainer;
import com.modularmclib.gtpm.api.sync_system.annotations.SaveField;
import com.modularmclib.gtpm.integration.ae2.machine.trait.GridNodeHostTrait;

import net.minecraft.core.Direction;

public class HullMachine extends TieredPartMachine implements IMonitorComponent {

    private final Object gridNodeHost;

    @SaveField
    protected NotifiableEnergyContainer energyContainer;

    public HullMachine(BlockEntityCreationInfo info, int tier) {
        super(info, tier);
        if (GTPM.MODS.AE2.isLoaded()) {
            this.gridNodeHost = GridNodeHostFactory.attachToMachine(this);
        } else {
            this.gridNodeHost = null;
        }

        long tierVoltage = GTValues.V[getTier()];
        this.energyContainer = attachTrait(
                new NotifiableEnergyContainer(tierVoltage * 16L, tierVoltage, 1L, tierVoltage, 1L));
        this.energyContainer.setSideOutputCondition(s -> s == getFrontFacing());
    }

    @Override
    public void onLoad() {
        super.onLoad();
        if (GTPM.MODS.AE2.isLoaded() && gridNodeHost instanceof GridNodeHostTrait connectedBlockEntity) {
            scheduleForNextServerTick(connectedBlockEntity::init);
        }
    }

    @Override
    public void onUnload() {
        super.onUnload();
        if (GTPM.MODS.AE2.isLoaded() && gridNodeHost instanceof GridNodeHostTrait connectedBlockEntity) {
            connectedBlockEntity.getMainNode().destroy();
        }
    }

    @Override
    public void setFrontFacing(Direction facing) {
        super.setFrontFacing(facing);
        if (isFacingValid(facing)) {
            if (GTPM.MODS.AE2.isLoaded() && gridNodeHost instanceof GridNodeHostTrait connectedBlockEntity) {
                connectedBlockEntity.init();
            }
        }
    }

    //////////////////////////////////////
    // ********** Misc **********//
    //////////////////////////////////////

    private static class GridNodeHostFactory {

        private static Object attachToMachine(HullMachine machine) {
            return machine.attachTrait(new GridNodeHostTrait(machine));
        }
    }

    @Override
    public int tintColor(int index) {
        if (index == 2) {
            return GTValues.VC[getTier()];
        }
        return super.tintColor(index);
    }

    @Override
    public IGuiTexture getComponentIcon() {
        return GuiTextures.BUTTON_CHECK; // temporary (until there's a texture that is not fully 16x16 for this)
    }
}
