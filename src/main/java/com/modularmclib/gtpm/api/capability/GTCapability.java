package com.modularmclib.gtpm.api.capability;

import com.modularmclib.gtpm.GTPM;
import com.modularmclib.gtpm.api.computation.ComputationPort;
import com.modularmclib.gtpm.api.machine.feature.multiblock.IMaintenanceMachine;
import com.modularmclib.gtpm.api.machine.trait.RecipeLogic;

import net.minecraft.core.Direction;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.ItemCapability;

public class GTCapability {

    public static final BlockCapability<IEnergyContainer, Direction> CAPABILITY_ENERGY_CONTAINER = BlockCapability
            .createSided(GTPM.id("energy_container"), IEnergyContainer.class);
    public static final BlockCapability<IEnergyInfoProvider, Direction> CAPABILITY_ENERGY_INFO_PROVIDER = BlockCapability
            .createSided(GTPM.id("energy_info_provider"), IEnergyInfoProvider.class);
    public static final BlockCapability<ICoverable, Direction> CAPABILITY_COVERABLE = BlockCapability
            .createSided(GTPM.id("coverable"), ICoverable.class);
    public static final BlockCapability<IWorkable, Direction> CAPABILITY_WORKABLE = BlockCapability
            .createSided(GTPM.id("workable"), IWorkable.class);
    public static final BlockCapability<IControllable, Direction> CAPABILITY_CONTROLLABLE = BlockCapability
            .createSided(GTPM.id("controllable"), IControllable.class);
    public static final BlockCapability<RecipeLogic, Direction> CAPABILITY_RECIPE_LOGIC = BlockCapability
            .createSided(GTPM.id("recipe_logic"), RecipeLogic.class);
    public static final ItemCapability<IElectricItem, Void> CAPABILITY_ELECTRIC_ITEM = ItemCapability
            .createVoid(GTPM.id("electric_item"), IElectricItem.class);
    public static final BlockCapability<IMaintenanceMachine, Direction> CAPABILITY_MAINTENANCE_MACHINE = BlockCapability
            .createSided(GTPM.id("maintenance"), IMaintenanceMachine.class);
    public static final BlockCapability<ILaserContainer, Direction> CAPABILITY_LASER = BlockCapability
            .createSided(GTPM.id("laser_container"), ILaserContainer.class);
    public static final BlockCapability<ComputationPort, Direction> CAPABILITY_COMPUTATION_PORT = BlockCapability
            .createSided(GTPM.id("computation_port"), ComputationPort.class);
    public static final BlockCapability<IOpticalComputationProvider, Direction> CAPABILITY_COMPUTATION_PROVIDER = BlockCapability
            .createSided(GTPM.id("computation_provider"), IOpticalComputationProvider.class);
    public static final BlockCapability<IDataAccessMachine, Direction> CAPABILITY_DATA_ACCESS = BlockCapability
            .createSided(GTPM.id("data_access"), IDataAccessMachine.class);
    public static final BlockCapability<IHazardParticleContainer, Direction> CAPABILITY_HAZARD_CONTAINER = BlockCapability
            .createSided(GTPM.id("hazard_particle_container"), IHazardParticleContainer.class);
    public static final BlockCapability<IMonitorComponent, Direction> CAPABILITY_MONITOR_COMPONENT = BlockCapability
            .createSided(GTPM.id("monitor_component"), IMonitorComponent.class);
}
