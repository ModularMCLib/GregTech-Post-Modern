package com.modularmclib.gtpm.api;

import com.modularmclib.gtpm.GTPM;
import com.modularmclib.gtpm.api.addon.AddonFinder;
import com.modularmclib.gtpm.api.addon.IGTAddon;
import com.modularmclib.gtpm.api.block.ICoilType;
import com.modularmclib.gtpm.api.block.IFilterType;
import com.modularmclib.gtpm.api.machine.multiblock.IBatteryData;
import com.modularmclib.gtpm.common.block.BatteryBlock;
import com.modularmclib.gtpm.common.block.CoilBlock;
import com.modularmclib.gtpm.config.ConfigHolder;

import net.minecraft.world.level.block.Block;

import lombok.Getter;
import org.jetbrains.annotations.ApiStatus;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class GTCEuAPI {

    public static final int GT_DATA_VERSION = 4;
    public static final String NETWORK_VERSION = "26.11.3";

    /** Will always be available */
    public static GTPM instance;
    /** Will be available at the Pre-Initialization stage */
    @Getter
    private static boolean highTier;
    private static boolean highTierInitialized;

    public static final Map<ICoilType, Supplier<CoilBlock>> HEATING_COILS = new HashMap<>();
    public static final Map<IFilterType, Supplier<Block>> CLEANROOM_FILTERS = new HashMap<>();
    public static final Map<IBatteryData, Supplier<BatteryBlock>> PSS_BATTERIES = new HashMap<>();

    /**
     * Initializes High-Tier. Internal use only, do not attempt to call this.
     */
    @ApiStatus.Internal
    public static void initializeHighTier() {
        if (highTierInitialized) throw new IllegalStateException("High-Tier is already initialized.");
        highTier = ConfigHolder.INSTANCE.machines.highTierContent ||
                AddonFinder.getAddonList().stream().anyMatch(IGTAddon::requiresHighTier) || GTPM.isDev();
        highTierInitialized = true;

        if (isHighTier()) GTPM.LOGGER.info("High-Tier is Enabled.");
        else GTPM.LOGGER.info("High-Tier is Disabled.");
    }
}
