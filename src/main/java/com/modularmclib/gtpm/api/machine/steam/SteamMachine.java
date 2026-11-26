package com.modularmclib.gtpm.api.machine.steam;

import com.modularmclib.gtpm.api.blockentity.BlockEntityCreationInfo;
import com.modularmclib.gtpm.api.capability.recipe.IO;
import com.modularmclib.gtpm.api.machine.MetaMachine;
import com.modularmclib.gtpm.api.machine.feature.ITieredMachine;
import com.modularmclib.gtpm.api.machine.property.GTMachineModelProperties;
import com.modularmclib.gtpm.api.machine.trait.NotifiableFluidTank;
import com.modularmclib.gtpm.api.sync_system.annotations.SaveField;
import com.modularmclib.gtpm.common.data.GTMaterials;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.neoforged.neoforge.fluids.FluidType;

import lombok.Getter;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public abstract class SteamMachine extends MetaMachine implements ITieredMachine {

    public static final BooleanProperty STEEL_PROPERTY = GTMachineModelProperties.IS_STEEL_MACHINE;

    @Getter
    public final boolean isHighPressure;
    @SaveField
    public final NotifiableFluidTank steamTank;

    public SteamMachine(BlockEntityCreationInfo info, boolean isHighPressure,
                        NotifiableFluidTank steamTank) {
        super(info);
        this.isHighPressure = isHighPressure;
        this.steamTank = attachTrait(steamTank);
        this.steamTank.setFilter(f -> f.getFluid().is(GTMaterials.Steam.getFluidTag()));
    }

    public SteamMachine(BlockEntityCreationInfo info, boolean isHighPressure) {
        this(info, isHighPressure, new NotifiableFluidTank(1, 16 * FluidType.BUCKET_VOLUME, IO.IN));
    }

    //////////////////////////////////////
    // ***** Initialization *****//
    //////////////////////////////////////

    @Override
    public int getTier() {
        return isHighPressure ? 1 : 0;
    }
}
