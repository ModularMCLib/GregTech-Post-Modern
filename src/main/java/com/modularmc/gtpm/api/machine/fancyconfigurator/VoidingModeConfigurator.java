package com.modularmc.gtpm.api.machine.fancyconfigurator;

import com.modularmc.gtpm.api.gui.fancy.ConfiguratorPanel;
import com.modularmc.gtpm.api.machine.feature.IVoidable;

import com.modularmc.registrate.localization.MinecraftLocale;
import com.modularmc.registrate.localization.annotation.Trans;
import com.modularmc.registrate.localization.annotation.Translation;

import net.minecraft.network.chat.Component;

import java.util.List;

/**
 * LDLib2 Fancy configurator helper for multiblock output voiding mode.
 *
 * <p>
 * The selector updates the client field; the owning UI synchronizes the change.
 */
@Trans(key = "gtpm.gui.multiblock.voiding_mode",
       source = "Voiding Mode:",
       translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "销毁模式："))
public final class VoidingModeConfigurator {

    private VoidingModeConfigurator() {}

    /**
     * Attaches a voiding mode selector to a migrated LDLib2 Fancy configurator panel.
     */
    public static void attachConfigurators(ConfiguratorPanel configuratorPanel, IVoidable controller) {
        configuratorPanel.attachConfigurators(new FancySelectorConfigurator<>(
                IVoidable.VoidingMode.VALUES,
                controller::getVoidingMode,
                (event, mode) -> {
                    controller.setVoidingMode(mode);
                    event.stopImmediatePropagation();
                    event.hasHandler = true;
                })
                .setTooltip(mode -> List.of(Component.translatable("gtpm.gui.multiblock.voiding_mode"),
                        Component.translatable(mode.getTooltip()))));
    }
}
