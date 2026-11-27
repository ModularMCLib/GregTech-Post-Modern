package com.modularmc.gtpm.integration.map.journeymap;

import com.modularmc.gtpm.GTPM;

import com.modularmc.registrate.localization.MinecraftLocale;
import com.modularmc.registrate.localization.annotation.Trans;
import com.modularmc.registrate.localization.annotation.TransVariants;
import com.modularmc.registrate.localization.annotation.Translation;
import com.modularmc.registrate.localization.annotation.Variant;

import journeymap.api.v2.client.IClientAPI;
import journeymap.api.v2.common.option.BooleanOption;
import journeymap.api.v2.common.option.OptionCategory;

import java.util.HashMap;
import java.util.Map;

@Trans(key = "gtpm.journeymap.options.layers",
       source = "Prospection layers",
       translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "勘探层"))
@TransVariants(keyPrefix = "gtpm.journeymap.options.layers",
               value = {
                       @Variant(id = "bedrock_fluids",
                                source = "Show Bedrock Fluid Veins",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "显示基岩流体矿脉")),
                       @Variant(id = "hide_depleted",
                                source = "Hide Depleted Veins",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "隐藏枯竭的矿脉")),
                       @Variant(id = "ore_veins",
                                source = "Show Ore Veins",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "显示矿脉")),
               })
public class JourneymapOptions {

    private final Map<String, BooleanOption> layerOptions = new HashMap<>();

    public JourneymapOptions() {
        final String prefix = "gtpm.journeymap.options.layers.";
        final OptionCategory category = new OptionCategory(GTPM.MOD_ID, "gtpm.journeymap.options.layers");

        final BooleanOption oreLayer = new BooleanOption(category, "ore_veins", prefix + "ore_veins", false);
        layerOptions.put(oreLayer.getFieldName(), oreLayer);
        final BooleanOption fluidLayer = new BooleanOption(category, "bedrock_fluids", prefix + "bedrock_fluids",
                false);
        layerOptions.put(fluidLayer.getFieldName(), fluidLayer);
    }

    public boolean showLayer(String name) {
        return layerOptions.get(name).get();
    }

    public void toggleLayer(String name, boolean active) {
        layerOptions.get(name).set(active);
        if (!active) {
            JourneymapRenderer.getMarkers().forEach((id, marker) -> {
                if (id.split("@")[0].equals(name)) {
                    IClientAPI api = JourneyMapPlugin.getJmApi();
                    api.remove(marker);
                }
            });
        } else {
            JourneymapRenderer.getMarkers().forEach((id, marker) -> {
                if (id.split("@")[0].equals(name)) {
                    try {
                        IClientAPI api = JourneyMapPlugin.getJmApi();
                        api.show(marker);
                    } catch (Exception e) {
                        // It never actually throws anything...
                        GTPM.LOGGER.error("Failed to enable marker with name {}", name, e);
                    }
                }
            });
        }
    }
}
