package com.modularmc.gtpm.integration.map.layer.builtin;

import com.modularmc.gtpm.api.data.chemical.ChemicalHelper;
import com.modularmc.gtpm.api.data.chemical.material.Material;
import com.modularmc.gtpm.api.data.tag.TagPrefix;
import com.modularmc.gtpm.api.data.worldgen.GTOreDefinition;
import com.modularmc.gtpm.api.data.worldgen.ores.GeneratedVeinMetadata;
import com.modularmc.gtpm.common.data.GTMaterials;
import com.modularmc.gtpm.config.GTPMConfigHolder;
import com.modularmc.gtpm.integration.map.GenericMapRenderer;
import com.modularmc.gtpm.integration.map.layer.MapRenderLayer;
import com.modularmc.gtpm.integration.xei.widgets.GTOreVeinWidget;

import com.modularmc.registrate.localization.MinecraftLocale;
import com.modularmc.registrate.localization.annotation.TransVariants;
import com.modularmc.registrate.localization.annotation.Translation;
import com.modularmc.registrate.localization.annotation.Variant;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@TransVariants(keyPrefix = "gtpm.minimap.ore_vein",
               value = {
                       @Variant(id = "depleted",
                                source = "Depleted",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "已枯竭")),
               })
public class OreRenderLayer extends MapRenderLayer {

    public OreRenderLayer(String key, GenericMapRenderer renderer) {
        super(key, renderer);
    }

    public static String getId(GeneratedVeinMetadata vein) {
        BlockPos center = vein.center();
        return "ore_veins@[" + center.getX() + "," + center.getY() + "," + center.getZ() + "]";
    }

    public static MutableComponent getName(GeneratedVeinMetadata vein) {
        // noinspection ConstantValue IDK, it crashed
        if (vein == null || vein.definition() == null || vein.definition().unwrapKey().isEmpty()) {
            return Component.translatable("gtpm.minimap.ore_vein.depleted");
        }
        return Component.translatable(GTOreVeinWidget.getOreName(vein.definition()));
    }

    public static @NotNull Material getMaterial(@NotNull GeneratedVeinMetadata vein) {
        Material firstMaterial = null;
        GTOreDefinition definition = vein.definition().value();
        if (!definition.indicatorGenerators().isEmpty()) {
            var blockOrMaterial = definition.indicatorGenerators().getFirst().block();
            firstMaterial = blockOrMaterial == null ? null : blockOrMaterial.map(
                    state -> {
                        var matStack = ChemicalHelper.getMaterialStack(state.getBlock());
                        return matStack.isEmpty() ? GTMaterials.NULL : matStack.material();
                    },
                    Function.identity());
        }
        if (firstMaterial == null) {
            firstMaterial = definition.veinGenerator().getAllMaterials().getFirst();
        }
        return firstMaterial;
    }

    public static List<Component> getTooltip(String name, GeneratedVeinMetadata vein) {
        final List<Component> tooltip = new ArrayList<>();
        var title = Component.literal(name);
        if (vein.depleted()) {
            title.append(" (").append(Component.translatable("gtpm.minimap.ore_vein.depleted")).append(")");
        }
        tooltip.add(title);

        for (var filler : vein.definition().value().veinGenerator().getAllEntries()) {
            filler.vein().ifLeft(state -> {
                tooltip.add(Component.literal(GTPMConfigHolder.INSTANCE.compat.minimap.oreNamePrefix)
                        .append(state.getBlock().getName()));
            }).ifRight(material -> {
                tooltip.add(Component.literal(GTPMConfigHolder.INSTANCE.compat.minimap.oreNamePrefix)
                        .append(TagPrefix.ore.getLocalizedName(material)));
            });
        }
        return tooltip;
    }
}
