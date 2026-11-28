package com.modularmc.gtpm.common.data;

import com.modularmc.gtpm.GTPM;
import com.modularmc.gtpm.api.GTCEuAPI;
import com.modularmc.gtpm.api.GTValues;
import com.modularmc.gtpm.api.cover.CoverDefinition;
import com.modularmc.gtpm.client.renderer.cover.*;
import com.modularmc.gtpm.common.cover.*;
import com.modularmc.gtpm.common.cover.detector.*;
import com.modularmc.gtpm.common.cover.ender.EnderFluidLinkCover;
import com.modularmc.gtpm.common.cover.ender.EnderItemLinkCover;
import com.modularmc.gtpm.common.cover.ender.EnderRedstoneLinkCover;
import com.modularmc.gtpm.common.cover.voiding.AdvancedFluidVoidingCover;
import com.modularmc.gtpm.common.cover.voiding.AdvancedItemVoidingCover;
import com.modularmc.gtpm.common.cover.voiding.FluidVoidingCover;
import com.modularmc.gtpm.common.cover.voiding.ItemVoidingCover;

import it.unimi.dsi.fastutil.ints.Int2ObjectFunction;

import java.util.Arrays;
import java.util.Locale;
import java.util.function.Supplier;

import static com.modularmc.gtpm.common.registry.GTRegistration.REGISTRATE;

public class GTCovers {

    public static final int[] ALL_TIERS = GTValues.tiersBetween(GTValues.LV,
            GTCEuAPI.isHighTier() ? GTValues.OpV : GTValues.UV);
    public static final int[] ALL_TIERS_WITH_ULV = GTValues.tiersBetween(GTValues.ULV,
            GTCEuAPI.isHighTier() ? GTValues.OpV : GTValues.UV);

    public final static CoverDefinition FACADE = REGISTRATE.cover("facade")
            .behavior(FacadeCover::new)
            .renderer(() -> () -> FacadeCoverRenderer.INSTANCE)
            .register();

    public final static CoverDefinition ITEM_FILTER = REGISTRATE.cover("item_filter")
            .behavior(ItemFilterCover::new).defaultRenderer().register();
    public final static CoverDefinition FLUID_FILTER = REGISTRATE.cover("fluid_filter")
            .behavior(FluidFilterCover::new).defaultRenderer().register();

    public final static CoverDefinition INFINITE_WATER = REGISTRATE.cover("infinite_water")
            .behavior(InfiniteWaterCover::new).defaultRenderer().register();
    public final static CoverDefinition ENDER_FLUID_LINK = REGISTRATE.cover("ender_fluid_link")
            .behavior(EnderFluidLinkCover::new).defaultRenderer().register();
    public final static CoverDefinition ENDER_ITEM_LINK = REGISTRATE.cover("ender_item_link")
            .behavior(EnderItemLinkCover::new).defaultRenderer().register();
    public final static CoverDefinition ENDER_REDSTONE_LINK = REGISTRATE.cover("ender_redstone_link")
            .behavior(EnderRedstoneLinkCover::new).defaultRenderer().register();
    public final static CoverDefinition SHUTTER = REGISTRATE.cover("shutter")
            .behavior(ShutterCover::new).defaultRenderer().register();
    public final static CoverDefinition COVER_STORAGE = REGISTRATE.cover("storage")
            .behavior(StorageCover::new).defaultRenderer().register();
    public final static CoverDefinition WIRELESS_TRANSMITTER = REGISTRATE.cover("wireless_transmitter")
            .behavior(WirelessTransmitterCover::new).defaultRenderer().register();

    public final static CoverDefinition[] CONVEYORS = registerTiered("conveyor", ConveyorCover::new,
            () -> tier -> new IOCoverRenderer(
                    GTPM.id("block/cover/conveyor"),
                    null,
                    GTPM.id("block/cover/conveyor_emissive"),
                    GTPM.id("block/cover/conveyor_inverted_emissive")),
            ALL_TIERS);

    public final static CoverDefinition[] ROBOT_ARMS = registerTiered("robot_arm", RobotArmCover::new,
            () -> tier -> new IOCoverRenderer(
                    GTPM.id("block/cover/arm"),
                    null,
                    GTPM.id("block/cover/arm_emissive"),
                    GTPM.id("block/cover/arm_inverted_emissive")),
            ALL_TIERS);

    public final static CoverDefinition[] PUMPS = registerTiered("pump", PumpCover::new,
            () -> tier -> IOCoverRenderer.PUMP_LIKE_COVER_RENDERER, ALL_TIERS);

    public final static CoverDefinition[] FLUID_REGULATORS = registerTiered("fluid_regulator", FluidRegulatorCover::new,
            () -> tier -> IOCoverRenderer.PUMP_LIKE_COVER_RENDERER, ALL_TIERS);

    public final static CoverDefinition COMPUTER_MONITOR = REGISTRATE.cover("computer_monitor")
            .behavior(ComputerMonitorCover::new).defaultRenderer().register();

    public final static CoverDefinition MACHINE_CONTROLLER = REGISTRATE.cover("machine_controller")
            .behavior(MachineControllerCover::new).defaultRenderer().register();

    // Voiding
    public final static CoverDefinition ITEM_VOIDING = REGISTRATE.cover("item_voiding")
            .behavior(ItemVoidingCover::new).defaultRenderer().register();
    public final static CoverDefinition ITEM_VOIDING_ADVANCED = REGISTRATE.cover("item_voiding_advanced")
            .behavior(AdvancedItemVoidingCover::new).defaultRenderer().register();
    public final static CoverDefinition FLUID_VOIDING = REGISTRATE.cover("fluid_voiding")
            .behavior(FluidVoidingCover::new).defaultRenderer().register();
    public final static CoverDefinition FLUID_VOIDING_ADVANCED = REGISTRATE.cover("fluid_voiding_advanced")
            .behavior(AdvancedFluidVoidingCover::new).defaultRenderer().register();

    // Detectors
    public final static CoverDefinition ACTIVITY_DETECTOR = REGISTRATE.cover("activity_detector")
            .behavior(ActivityDetectorCover::new).defaultRenderer().register();
    public final static CoverDefinition ACTIVITY_DETECTOR_ADVANCED = REGISTRATE.cover("activity_detector_advanced")
            .behavior(AdvancedActivityDetectorCover::new).defaultRenderer().register();
    public final static CoverDefinition FLUID_DETECTOR = REGISTRATE.cover("fluid_detector")
            .behavior(FluidDetectorCover::new).defaultRenderer().register();
    public final static CoverDefinition FLUID_DETECTOR_ADVANCED = REGISTRATE.cover("fluid_detector_advanced")
            .behavior(AdvancedFluidDetectorCover::new).defaultRenderer().register();
    public final static CoverDefinition ITEM_DETECTOR = REGISTRATE.cover("item_detector")
            .behavior(ItemDetectorCover::new).defaultRenderer().register();
    public final static CoverDefinition ITEM_DETECTOR_ADVANCED = REGISTRATE.cover("item_detector_advanced")
            .behavior(AdvancedItemDetectorCover::new).defaultRenderer().register();
    public final static CoverDefinition ENERGY_DETECTOR = REGISTRATE.cover("energy_detector")
            .behavior(EnergyDetectorCover::new).defaultRenderer().register();
    public final static CoverDefinition ENERGY_DETECTOR_ADVANCED = REGISTRATE.cover("energy_detector_advanced")
            .behavior(AdvancedEnergyDetectorCover::new).defaultRenderer().register();
    public final static CoverDefinition MAINTENANCE_DETECTOR = REGISTRATE.cover("maintenance_detector")
            .behavior(MaintenanceDetectorCover::new).defaultRenderer().register();

    // Solar Panels
    public final static CoverDefinition SOLAR_PANEL_BASIC = REGISTRATE.cover("solar_panel")
            .behavior(CoverSolarPanel::new).defaultRenderer().register();
    public final static CoverDefinition[] SOLAR_PANEL = registerTiered("solar_panel", CoverSolarPanel::new,
            () -> tier -> new SimpleCoverRenderer(GTPM.id("block/cover/solar_panel")), ALL_TIERS_WITH_ULV);

    ///////////////////////////////////////////////
    // *********** UTIL METHODS ***********//
    ///////////////////////////////////////////////

    private static CoverDefinition[] registerTiered(String id,
                                                    CoverDefinition.TieredCoverBehaviourProvider behaviorCreator,
                                                    Supplier<Int2ObjectFunction<ICoverRenderer>> coverRenderer,
                                                    int... tiers) {
        return Arrays.stream(tiers).mapToObj(tier -> {
            var name = id + "." + GTValues.VN[tier].toLowerCase(Locale.ROOT);
            return REGISTRATE.cover(name)
                    .behavior((def, coverable, side) -> behaviorCreator.create(def, coverable, side, tier))
                    .renderer(() -> () -> coverRenderer.get().apply(tier))
                    .register();
        }).toArray(CoverDefinition[]::new);
    }

    public static void init() {}
}
