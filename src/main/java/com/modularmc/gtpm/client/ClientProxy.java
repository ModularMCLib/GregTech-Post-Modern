package com.modularmc.gtpm.client;

import com.modularmc.gtpm.GTPM;
import com.modularmc.gtpm.api.GTValues;
import com.modularmc.gtpm.api.cosmetics.event.RegisterGTCapesEvent;
import com.modularmc.gtpm.api.gui.factory.UIRegistry;
import com.modularmc.gtpm.api.item.IComponentItem;
import com.modularmc.gtpm.api.item.IGTTool;
import com.modularmc.gtpm.client.model.item.FacadeUnbakedModel;
import com.modularmc.gtpm.client.model.machine.MachineModelLoader;
import com.modularmc.gtpm.client.model.pipe.PipeModel;
import com.modularmc.gtpm.client.model.pipe.PipeModelLoader;
import com.modularmc.gtpm.client.particle.GTParticleManager;
import com.modularmc.gtpm.client.particle.HazardParticle;
import com.modularmc.gtpm.client.particle.MufflerParticle;
import com.modularmc.gtpm.client.renderer.MultiblockInWorldPreviewRenderer;
import com.modularmc.gtpm.client.renderer.block.LampItemRendererHelper;
import com.modularmc.gtpm.client.renderer.block.MaterialBlockRenderer;
import com.modularmc.gtpm.client.renderer.block.OreBlockRenderer;
import com.modularmc.gtpm.client.renderer.block.SurfaceRockRenderer;
import com.modularmc.gtpm.client.renderer.entity.GTExplosiveRenderer;
import com.modularmc.gtpm.client.renderer.item.ArmorItemRenderer;
import com.modularmc.gtpm.client.renderer.item.TagPrefixItemRenderer;
import com.modularmc.gtpm.client.renderer.item.ToolItemRenderer;
import com.modularmc.gtpm.client.renderer.item.decorator.GTComponentItemDecorator;
import com.modularmc.gtpm.client.renderer.item.decorator.GTLampItemOverlayRenderer;
import com.modularmc.gtpm.client.renderer.item.decorator.GTTankItemFluidPreview;
import com.modularmc.gtpm.client.renderer.item.decorator.GTToolBarRenderer;
import com.modularmc.gtpm.client.renderer.machine.DynamicRenderManager;
import com.modularmc.gtpm.client.renderer.machine.impl.*;
import com.modularmc.gtpm.client.renderer.machine.impl.BoilerMultiPartRender;
import com.modularmc.gtpm.client.util.ModelEventHelper;
import com.modularmc.gtpm.common.CommonEventListener;
import com.modularmc.gtpm.common.data.GTEntityTypes;
import com.modularmc.gtpm.common.data.GTMaterialBlocks;
import com.modularmc.gtpm.common.data.GTParticleTypes;
import com.modularmc.gtpm.common.data.models.GTModels;
import com.modularmc.gtpm.common.item.DrumMachineItem;
import com.modularmc.gtpm.common.item.LampBlockItem;
import com.modularmc.gtpm.common.item.QuantumTankMachineItem;
import com.modularmc.gtpm.config.ConfigHolder;
import com.modularmc.gtpm.data.model.builder.PipeModelBuilder;
import com.modularmc.gtpm.data.pack.event.RegisterDynamicResourcesEvent;
import com.modularmc.gtpm.data.pattern.StructurePatternRegistry;
import com.modularmc.gtpm.integration.map.ClientCacheManager;
import com.modularmc.gtpm.integration.map.cache.client.GTClientCache;
import com.modularmc.gtpm.integration.map.ftbchunks.FTBChunksPlugin;
import com.modularmc.gtpm.integration.map.layer.Layers;
import com.modularmc.gtpm.integration.map.layer.builtin.FluidRenderLayer;
import com.modularmc.gtpm.integration.map.layer.builtin.OreRenderLayer;
import com.modularmc.gtpm.utils.data.RuntimeBlockstateProvider;
import com.modularmc.gtpm.utils.input.SyncedKeyMapping;

import com.lowdragmc.lowdraglib2.gui.holder.ModularUIContainerScreen;

import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.common.NeoForge;

public class ClientProxy {

    public static void init(IEventBus modBus) {
        modBus.register(ClientProxy.class);
        if (!GTPM.isDataGen()) {
            ClientCacheManager.registerClientCache(GTClientCache.instance, GTPM.MOD_ID);
            Layers.registerLayer(OreRenderLayer::new, "ore_veins");
            Layers.registerLayer(FluidRenderLayer::new, "bedrock_fluids");
            CommonEventListener.registerCapes(new RegisterGTCapesEvent());
        }
        initializeDynamicRenders();
        StructurePatternRegistry.addReloadListener(MultiblockInWorldPreviewRenderer::cleanPreview);
        ModelEventHelper.initInternalAssetReloadListeners();

        NeoForge.EVENT_BUS.register(GTParticleManager.INSTANCE);
    }

    @SubscribeEvent
    public static void onRegisterMenuScreens(RegisterMenuScreensEvent event) {
        event.register(UIRegistry.MENU.get(), ModularUIContainerScreen::new);
    }

    @SubscribeEvent
    public static void onRegisterEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(GTEntityTypes.DYNAMITE.get(), ThrownItemRenderer::new);
        event.registerEntityRenderer(GTEntityTypes.POWDERBARREL.get(), GTExplosiveRenderer::new);
        event.registerEntityRenderer(GTEntityTypes.INDUSTRIAL_TNT.get(), GTExplosiveRenderer::new);
    }

    @SubscribeEvent
    public static void onRegisterItemDecorations(RegisterItemDecorationsEvent event) {
        for (Item item : BuiltInRegistries.ITEM) {
            if (item instanceof IComponentItem) {
                event.register(item, GTComponentItemDecorator.INSTANCE);
            }
            if (item instanceof IGTTool) {
                event.register(item, GTToolBarRenderer.INSTANCE);
            }
            if (item instanceof LampBlockItem) {
                event.register(item, GTLampItemOverlayRenderer.INSTANCE);
            }
            if (item instanceof DrumMachineItem) {
                event.register(item, GTTankItemFluidPreview.DRUM);
            }
            if (item instanceof QuantumTankMachineItem) {
                event.register(item, GTTankItemFluidPreview.QUANTUM_TANK);
            }
        }
    }

    @SubscribeEvent
    public static void registerKeyBindings(RegisterKeyMappingsEvent event) {
        SyncedKeyMapping.onRegisterKeyBinds(event);
    }

    @SubscribeEvent
    public static void onRegisterGuiOverlays(RegisterGuiLayersEvent event) {
        event.registerAboveAll(GTPM.id("hud"), new HudGuiOverlay());
    }

    @SubscribeEvent
    public static void onRegisterParticleProviders(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(GTParticleTypes.HAZARD_PARTICLE.get(), HazardParticle.Provider::new);
        event.registerSpriteSet(GTParticleTypes.MUFFLER_PARTICLE.get(), MufflerParticle.Provider::new);
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        if (ConfigHolder.INSTANCE.compat.minimap.toggle.ftbChunksIntegration &&
                GTPM.isModLoaded(GTValues.MODID_FTB_CHUNKS)) {
            FTBChunksPlugin.addEventListeners();
        }
    }

    private static void initializeDynamicRenders() {
        DynamicRenderManager.register(GTPM.id("quantum_tank_fluid"), QuantumTankFluidRender.TYPE);
        DynamicRenderManager.register(GTPM.id("quantum_chest_item"), QuantumChestItemRender.TYPE);

        DynamicRenderManager.register(GTPM.id("fusion_ring"), FusionRingRender.TYPE);
        DynamicRenderManager.register(GTPM.id("boiler_multi_parts"), BoilerMultiPartRender.TYPE);

        DynamicRenderManager.register(GTPM.id("fluid_area"), FluidAreaRender.TYPE);
        DynamicRenderManager.register(GTPM.id("growing_plant"), GrowingPlantRender.TYPE);

        DynamicRenderManager.register(GTPM.id("central_monitor"), CentralMonitorRender.TYPE);
    }

    @SubscribeEvent
    public static void registerModelLoaders(ModelEvent.RegisterGeometryLoaders event) {
        event.register(MachineModelLoader.ID, MachineModelLoader.INSTANCE);
        event.register(PipeModelLoader.ID, PipeModelLoader.INSTANCE);
        event.register(GTPM.id("facade"), FacadeUnbakedModel.Loader.INSTANCE);
    }

    @SubscribeEvent
    public static void registerClientExtensions(RegisterClientExtensionsEvent event) {
        for (Item item : BuiltInRegistries.ITEM) {
            if (item instanceof LampBlockItem lampItem) {
                event.registerItem(LampItemRendererHelper.CLIENT_EXTENSIONS, lampItem);
                LampItemRendererHelper.registerModelBakeListener(lampItem);
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void preRegisterDynamicAssets(RegisterDynamicResourcesEvent event) {
        PipeModel.DYNAMIC_MODELS.clear();
    }

    @SubscribeEvent
    public static void registerDynamicAssets(RegisterDynamicResourcesEvent event) {
        // regenerate all pipe models in case their textures changed
        // cables may do this, others too if something's removed
        for (var block : GTMaterialBlocks.CABLE_BLOCKS.values()) {
            if (block == null) continue;
            block.get().createPipeModel(RuntimeBlockstateProvider.INSTANCE).dynamicModel();
        }
        for (var block : GTMaterialBlocks.FLUID_PIPE_BLOCKS.values()) {
            if (block == null) continue;
            block.get().createPipeModel(RuntimeBlockstateProvider.INSTANCE).dynamicModel();
        }
        for (var block : GTMaterialBlocks.ITEM_PIPE_BLOCKS.values()) {
            if (block == null) continue;
            block.get().createPipeModel(RuntimeBlockstateProvider.INSTANCE).dynamicModel();
        }

        MaterialBlockRenderer.reinitModels();
        TagPrefixItemRenderer.reinitModels();
        OreBlockRenderer.reinitModels();
        ToolItemRenderer.reinitModels();
        ArmorItemRenderer.reinitModels();
        SurfaceRockRenderer.reinitModels();
        GTModels.registerMaterialFluidModels();
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void postRegisterDynamicAssets(RegisterDynamicResourcesEvent event) {
        // do this last so addons can easily add new variants to the registered model set
        PipeModel.initDynamicModels();

        RuntimeBlockstateProvider.INSTANCE.run();
        PipeModelBuilder.clearRestrictorModelCache();
    }
}
