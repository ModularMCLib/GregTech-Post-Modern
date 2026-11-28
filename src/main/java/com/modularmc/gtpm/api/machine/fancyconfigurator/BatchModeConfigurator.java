package com.modularmc.gtpm.api.machine.fancyconfigurator;

import com.modularmc.gtpm.api.gui.GuiTextures;
import com.modularmc.gtpm.api.gui.fancy.ConfiguratorPanel;
import com.modularmc.gtpm.api.gui.fancy.IFancyConfiguratorButton;
import com.modularmc.gtpm.api.machine.feature.multiblock.BatchModeMachine;

import com.modularmc.registrate.localization.MinecraftLocale;
import com.modularmc.registrate.localization.annotation.TransVariants;
import com.modularmc.registrate.localization.annotation.Translation;
import com.modularmc.registrate.localization.annotation.Variant;

import net.minecraft.network.chat.Component;

import java.util.List;

/**
 * LDLib2 Fancy configurator helper for multiblock batch mode.
 *
 * <p>
 * The button updates the client field; the owning UI synchronizes the change.
 */
@TransVariants(keyPrefix = "gtpm.machine",
               value = {
                       @Variant(id = "batch_disabled",
                                source = "Batching Disabled",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "批处理已禁用")),
                       @Variant(id = "batch_enabled",
                                source = "Batching Enabled",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "批处理已启用")),
               })
public final class BatchModeConfigurator {

    private BatchModeConfigurator() {}

    /**
     * Attaches the batch mode toggle when the machine definition uses the batch recipe modifier.
     */
    public static void attachConfigurators(ConfiguratorPanel configuratorPanel,
                                           BatchModeMachine machine) {
        if (!machine.supportsBatchMode()) {
            return;
        }

        configuratorPanel.attachConfigurators(createBatchModeConfigurator(machine));
    }

    static IFancyConfiguratorButton.Toggle createBatchModeConfigurator(
                                                                       BatchModeMachine machine) {
        return new IFancyConfiguratorButton.Toggle(
                GuiTextures.BUTTON_BATCH.getSubTexture(0, 0, 1, 0.5),
                GuiTextures.BUTTON_BATCH.getSubTexture(0, 0.5, 1, 0.5),
                machine::isBatchEnabled,
                (event, pressed) -> {
                    machine.setBatchEnabled(pressed);
                    event.stopImmediatePropagation();
                    event.hasHandler = true;
                })
                .setTooltipsSupplier(pressed -> List.of(Component.translatable(
                        "gtpm.machine.batch_" + (pressed ? "enabled" : "disabled"))));
    }
}
