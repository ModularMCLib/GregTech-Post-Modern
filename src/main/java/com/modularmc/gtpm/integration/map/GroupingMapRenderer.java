package com.modularmc.gtpm.integration.map;

import com.modularmc.gtpm.GTPM;
import com.modularmc.gtpm.api.GTValues;
import com.modularmc.gtpm.api.data.worldgen.ores.GeneratedVeinMetadata;
import com.modularmc.gtpm.api.gui.misc.ProspectorMode;
import com.modularmc.gtpm.config.GTPMConfigHolder;
import com.modularmc.gtpm.integration.map.ftbchunks.FTBChunksRenderer;
import com.modularmc.gtpm.integration.map.journeymap.JourneymapRenderer;
import com.modularmc.gtpm.integration.map.xaeros.XaerosRenderer;

import com.modularmc.registrate.localization.MinecraftLocale;
import com.modularmc.registrate.localization.annotation.TransVariants;
import com.modularmc.registrate.localization.annotation.Translation;
import com.modularmc.registrate.localization.annotation.Variant;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * A map renderer that groups together multiple map mods' renderers for supporting multiple simultaneous mods.
 */
@TransVariants(keyPrefix = "gtpm.button",
               value = {
                       @Variant(id = "ore_veins",
                                source = "Show GT Ore Veins",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "显示GT矿脉")),
                       @Variant(id = "bedrock_fluids",
                                source = "Show Bedrock Fluid Veins",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "显示基岩流体矿脉")),
                       @Variant(id = "show_depleted",
                                source = "Show Depleted Veins",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "显示枯竭的矿脉")),
               })
public class GroupingMapRenderer extends GenericMapRenderer {

    @Getter
    private static final GroupingMapRenderer instance;

    static {
        Map<String, GenericMapRenderer> renderers = new HashMap<>();
        var toggle = GTPMConfigHolder.INSTANCE.compat.minimap.toggle;
        if (toggle.journeyMapIntegration && GTPM.isModLoaded(GTValues.MODID_JOURNEYMAP)) {
            renderers.put(GTValues.MODID_JOURNEYMAP, new JourneymapRenderer());
        }
        if (toggle.xaerosMapIntegration && GTPM.isModLoaded(GTValues.MODID_XAEROS_MINIMAP)) {
            renderers.put(GTValues.MODID_XAEROS_MINIMAP, new XaerosRenderer());
        }
        if (toggle.ftbChunksIntegration && GTPM.isModLoaded(GTValues.MODID_FTB_CHUNKS)) {
            renderers.put(GTValues.MODID_FTB_CHUNKS, new FTBChunksRenderer());
        }

        instance = new GroupingMapRenderer(renderers);
    }

    @Getter
    private final Map<String, GenericMapRenderer> renderers;
    private final GenericMapRenderer[] rendererList;

    public GroupingMapRenderer(Map<String, GenericMapRenderer> renderers) {
        super(false);
        this.renderers = renderers;
        this.rendererList = renderers.values().toArray(GenericMapRenderer[]::new);
    }

    @Override
    public boolean addMarker(String name, String id, ResourceKey<Level> dim, ChunkPos pos,
                             ProspectorMode.FluidInfo fluid) {
        boolean value = false;
        for (GenericMapRenderer renderer : rendererList) {
            value |= renderer.addMarker(name, id, dim, pos, fluid);
        }
        return value;
    }

    @Override
    public boolean addMarker(String name, ResourceKey<Level> dim, GeneratedVeinMetadata vein, String id) {
        boolean value = false;
        for (GenericMapRenderer renderer : rendererList) {
            value |= renderer.addMarker(name, dim, vein, id);
        }
        return value;
    }

    @Override
    public boolean removeMarker(ResourceKey<Level> dim, String id) {
        boolean value = false;
        for (GenericMapRenderer renderer : rendererList) {
            value |= renderer.removeMarker(dim, id);
        }
        return value;
    }

    @Override
    public boolean doShowLayer(String name) {
        boolean value = false;
        for (GenericMapRenderer renderer : rendererList) {
            value |= renderer.doShowLayer(name);
        }
        return value;
    }

    @Override
    public void setLayerActive(String name, boolean active) {
        for (GenericMapRenderer renderer : rendererList) {
            renderer.setLayerActive(name, active);
        }
    }

    @Override
    public void clear() {
        for (GenericMapRenderer renderer : rendererList) {
            renderer.clear();
        }
    }
}
