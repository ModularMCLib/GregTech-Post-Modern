package com.modularmc.gtpm;

import com.modularmc.gtpm.api.GTCEuAPI;
import com.modularmc.gtpm.api.GTValues;
import com.modularmc.gtpm.common.CommonProxy;
import com.modularmc.gtpm.common.network.GTNetwork;
import com.modularmc.gtpm.config.ConfigHolder;
import com.modularmc.gtpm.utils.FormattingUtil;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.javafmlmod.FMLModContainer;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.data.loading.DatagenModLoader;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

import com.mojang.serialization.Codec;
import dev.emi.emi.config.EmiConfig;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.ApiStatus;

import java.nio.file.Path;
import java.util.function.Supplier;

@Mod(GTPM.MOD_ID)
public class GTPM {

    public static final String MOD_ID = "gtpm";
    private static final ResourceLocation TEMPLATE_LOCATION = ResourceLocation.fromNamespaceAndPath(MOD_ID, "");
    public static final Codec<ResourceLocation> GTCEU_ID = Codec.STRING.comapFlatMap(
            str -> ResourceLocation.read(appendIdString(str)),
            s -> s.getNamespace().equals(MOD_ID) ? s.getPath() : s.toString());

    public static final String MOD_NAME = "GTPM";
    public static final Logger LOGGER = LogManager.getLogger(MOD_NAME);
    public static final Path GTCEU_FOLDER = getGameDir().resolve("gtpm");

    @ApiStatus.Internal
    public static IEventBus gtModBus;

    public GTPM(IEventBus modBus, FMLModContainer container) {
        GTCEuAPI.instance = this;
        GTPM.gtModBus = modBus;
        ConfigHolder.init();

        GTCEuAPI.initializeHighTier();
        if (GTPM.isDev()) {
            ConfigHolder.INSTANCE.recipes.generateLowQualityGems = true;
            ConfigHolder.INSTANCE.compat.energy.enableFEConverters = true;
        }
        CommonProxy.init(modBus);

        modBus.addListener(GTNetwork::registerPayloads);
    }

    public static ResourceLocation id(String path) {
        if (path.isBlank()) {
            return TEMPLATE_LOCATION;
        }

        int i = path.indexOf(':');
        if (i > 0) {
            return ResourceLocation.tryParse(path);
        } else if (i == 0) {
            path = path.substring(i + 1);
        }
        // only convert it to camel_case if it has any uppercase to begin with
        if (FormattingUtil.hasUpperCase(path)) {
            path = FormattingUtil.toLowerCaseUnderscore(path);
        }
        return TEMPLATE_LOCATION.withPath(path);
    }

    public static String appendIdString(String id) {
        int i = id.indexOf(':');
        if (i > 0) {
            return id;
        } else if (i == 0) {
            return MOD_ID + id;
        } else {
            return MOD_ID + ":" + id;
        }
    }

    /**
     * @return if we're running in a production environment
     */
    public static boolean isProd() {
        return FMLLoader.isProduction();
    }

    /**
     * @return if we're not running in a production environment
     */
    public static boolean isDev() {
        return !isProd();
    }

    /**
     * @return if we're running data generation
     */
    public static boolean isDataGen() {
        return DatagenModLoader.isRunningDataGen();
    }

    /**
     * A friendly reminder that the server instance is populated on the server side only, so null/side check it!
     *
     * @return the current minecraft server instance
     */
    public static MinecraftServer getMinecraftServer() {
        return ServerLifecycleHooks.getCurrentServer();
    }

    /**
     * @param modId the mod id to check for
     * @return if the mod whose id is {@code modId} is loaded or not
     */
    public static boolean isModLoaded(String modId) {
        ModList modList = ModList.get();
        if (modList != null) return modList.isLoaded(modId);
        else return FMLLoader.getLoadingModList().getModFileById(modId) != null;
    }

    /**
     * For async stuff use this, otherwise use {@link GTPM isClientSide}
     *
     * @return if the current thread is the client thread
     */
    @SuppressWarnings("ConstantValue")
    public static boolean isClientThread() {
        return isClientSide() && Minecraft.getInstance() != null && Minecraft.getInstance().isSameThread();
    }

    /**
     * @return if the game is the <strong>PHYSICAL</strong> client, e.g. not a dedicated server.
     * @apiNote Do not use this to check if you're currently on the server thread for side-specific actions!
     *          It does <strong>NOT</strong> work for that. Use {@link #isClientThread()} instead.
     * @see #isClientThread()
     */
    public static boolean isClientSide() {
        return FMLEnvironment.dist.isClient();
    }

    /** Runs client initialization without resolving its callback on a dedicated server. */
    public static void runOnClient(Supplier<Runnable> action) {
        if (isClientSide()) {
            action.get().run();
        }
    }

    /**
     * This check isn't the same for client and server!
     *
     * @return if it's safe to access the current instance {@link net.minecraft.world.level.Level Level} on client or if
     *         it's safe to access any level on server.
     */
    public static boolean canGetServerLevel() {
        if (isClientSide()) {
            return Minecraft.getInstance().level != null;
        }
        var server = getMinecraftServer();
        return server != null &&
                !(server.isStopped() || server.isShutdown() || !server.isRunning() || server.isCurrentlySaving());
    }

    /**
     * @return the path to the minecraft instance directory
     */
    public static Path getGameDir() {
        return FMLPaths.GAMEDIR.get();
    }

    public enum MODS {

        EMI(GTValues.MODID_EMI),
        JEI(GTValues.MODID_JEI),

        SODIUM(GTValues.MODID_SODIUM),
        IRIS(GTValues.MODID_IRIS),

        AE2(GTValues.MODID_APPENG),

        CURIOS(GTValues.MODID_CURIOS),

        MODERNFIX(GTValues.MODID_MODERNFIX),

        FTB_LIBRARY(GTValues.MODID_FTB_LIBRARY),
        FTB_TEAMS(GTValues.MODID_FTB_TEAMS),
        FTB_QUESTS(GTValues.MODID_FTB_QUEST),

        HERACLES(GTValues.MODID_HERACLES),

        ARGONAUTS(GTValues.MODID_ARGONAUTS),

        GAMESTAGES(GTValues.MODID_GAMESTAGES),

        CCTWEAKED(GTValues.MODID_CCTWEAKED);

        @Getter
        private final boolean loaded;

        MODS(String modId) {
            this.loaded = GTPM.isModLoaded(modId);
        }

        public static boolean isAnyRecipeViewerLoaded() {
            return EMI.isLoaded() || JEI.isLoaded();
        }

        public static boolean isJEILoaded() {
            return !EMI.isLoaded() && JEI.isLoaded();
        }

        public static boolean isEMILoaded() {
            return EMI.isLoaded() && (!GTPM.isClientSide() || EmiConfig.enabled);
        }
    }
}
