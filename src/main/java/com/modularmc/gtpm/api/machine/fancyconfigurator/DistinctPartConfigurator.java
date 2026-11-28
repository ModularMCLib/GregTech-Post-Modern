package com.modularmc.gtpm.api.machine.fancyconfigurator;

import com.modularmc.gtpm.api.gui.GuiTextures;
import com.modularmc.gtpm.api.gui.fancy.ConfiguratorPanel;
import com.modularmc.gtpm.api.gui.fancy.IFancyConfiguratorButton;
import com.modularmc.gtpm.api.machine.feature.multiblock.DistinctPart;

import com.modularmc.registrate.localization.MinecraftLocale;
import com.modularmc.registrate.localization.annotation.TransVariants;
import com.modularmc.registrate.localization.annotation.Translation;
import com.modularmc.registrate.localization.annotation.Variant;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;

import java.util.List;

/**
 * LDLib2 Fancy configurator helper for multiblock distinct bus mode.
 *
 * <p>
 * The button updates the annotated distinct field; the owning UI synchronizes the change.
 */
@TransVariants(keyPrefix = "gtpm.multiblock.universal",
               value = {
                       @Variant(id = "distinct",
                                source = "Distinct Buses:",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "总线隔离：")),
                       @Variant(id = "distinct.no",
                                source = "No",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "否")),
                       @Variant(id = "distinct.yes",
                                source = "Yes",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "是")),
               })
public final class DistinctPartConfigurator {

    private DistinctPartConfigurator() {}

    /**
     * Attaches a distinct part toggle to a migrated LDLib2 Fancy configurator panel.
     */
    public static void attachConfigurators(ConfiguratorPanel configuratorPanel, DistinctPart part) {
        configuratorPanel.attachConfigurators(createDistinctConfigurator(part));
    }

    static IFancyConfiguratorButton.Toggle createDistinctConfigurator(
                                                                      DistinctPart part) {
        return new IFancyConfiguratorButton.Toggle(
                GuiTextures.BUTTON_DISTINCT_BUSES.getSubTexture(0, 0.5, 1, 0.5),
                GuiTextures.BUTTON_DISTINCT_BUSES.getSubTexture(0, 0, 1, 0.5),
                part::isDistinct,
                (event, pressed) -> {
                    part.setDistinct(pressed);
                    event.stopImmediatePropagation();
                    event.hasHandler = true;
                })
                .setTooltipsSupplier(pressed -> List.of(
                        Component.translatable("gtpm.multiblock.universal.distinct")
                                .setStyle(Style.EMPTY.withColor(ChatFormatting.YELLOW))
                                .append(Component.translatable(pressed ? "gtpm.multiblock.universal.distinct.yes" :
                                        "gtpm.multiblock.universal.distinct.no"))));
    }
}
