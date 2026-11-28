package com.modularmc.gtpm.api.machine.fancyconfigurator;

import com.modularmc.gtpm.api.gui.GuiTextures;
import com.modularmc.gtpm.api.gui.fancy.ConfiguratorPanel;
import com.modularmc.gtpm.api.gui.fancy.IFancyConfiguratorButton;
import com.modularmc.gtpm.api.gui.texture.IGuiTexture;
import com.modularmc.gtpm.api.machine.feature.AutoOutputMachine;

import com.modularmc.registrate.localization.MinecraftLocale;
import com.modularmc.registrate.localization.annotation.TransVariants;
import com.modularmc.registrate.localization.annotation.Translation;
import com.modularmc.registrate.localization.annotation.Variant;

import com.lowdragmc.lowdraglib2.gui.ui.event.UIEvent;

import net.minecraft.network.chat.Component;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BooleanSupplier;

/**
 * LDLib2 Fancy configurator helper for common auto-output item and fluid toggles.
 *
 * <p>
 * The buttons update the client fields; the owning UI synchronizes their changes.
 */
@TransVariants(keyPrefix = "gtpm.gui",
               value = {
                       @Variant(id = "fluid_auto_input.tooltip.disabled",
                                source = "Fluid Auto-Input Disabled",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "流体自动输入已禁用")),
                       @Variant(id = "fluid_auto_input.tooltip.enabled",
                                source = "Fluid Auto-Input Enabled",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "流体自动输入已启用")),
                       @Variant(id = "fluid_auto_output.allow_input.disabled",
                                source = "disable fluids input from the output side",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "禁止从输出面输入流体")),
                       @Variant(id = "fluid_auto_output.allow_input.enabled",
                                source = "allow fluids input from the output side",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "允许从输出面输入流体")),
                       @Variant(id = "fluid_auto_output.tooltip.disabled",
                                source = "Fluid Auto-Output Disabled",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "流体自动输出已禁用")),
                       @Variant(id = "fluid_auto_output.tooltip.enabled",
                                source = "Fluid Auto-Output Enabled",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "流体自动输出已启用")),
                       @Variant(id = "fluid_lock.tooltip.disabled",
                                source = "Fluid Locking Disabled",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "流体锁定已禁用")),
                       @Variant(id = "fluid_lock.tooltip.enabled",
                                source = "Fluid Locking Enabled",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "流体锁定已启用")),
                       @Variant(id = "fluid_voiding_partial.tooltip.disabled",
                                source = "Fluid Voiding Disabled",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "过量流体销毁已禁用")),
                       @Variant(id = "fluid_voiding_partial.tooltip.enabled",
                                source = "Fluid Voiding Enabled",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "过量流体销毁已启用")),
                       @Variant(id = "item_auto_input.tooltip.disabled",
                                source = "Item Auto-Input Disabled",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "物品自动输入已禁用")),
                       @Variant(id = "item_auto_input.tooltip.enabled",
                                source = "Item Auto-Input Enabled",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "物品自动输入已启用")),
                       @Variant(id = "item_auto_output.allow_input.disabled",
                                source = "disable items input from the output side",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "禁止从输出面输入物品")),
                       @Variant(id = "item_auto_output.allow_input.enabled",
                                source = "allow items input from the output side",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "允许从输出面输入物品")),
                       @Variant(id = "item_auto_output.tooltip.disabled",
                                source = "Item Auto-Output Disabled",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "物品自动输出已禁用")),
                       @Variant(id = "item_auto_output.tooltip.enabled",
                                source = "Item Auto-Output Enabled",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "物品自动输出已启用")),
                       @Variant(id = "item_lock.tooltip.disabled",
                                source = "Item Locking Disabled",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "物品锁定已禁用")),
                       @Variant(id = "item_lock.tooltip.enabled",
                                source = "Item Locking Enabled",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "物品锁定已启用")),
                       @Variant(id = "item_voiding_partial.tooltip.disabled",
                                source = "Item Voiding Disabled",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "物品过量销毁已禁用")),
                       @Variant(id = "item_voiding_partial.tooltip.enabled",
                                source = "Item Voiding Enabled",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "物品过量销毁已开启")),
               })
public final class AutoOutputConfigurator {

    private AutoOutputConfigurator() {}

    /**
     * Attaches supported auto-output toggles to a migrated LDLib2 Fancy configurator panel.
     */
    public static void attachConfigurators(ConfiguratorPanel configuratorPanel,
                                           AutoOutputMachine autoOutputMachine) {
        if (autoOutputMachine.supportsAutoOutputFluids()) {
            configuratorPanel.attachConfigurators(createAutoOutputFluidConfigurator(autoOutputMachine));
        }
        if (autoOutputMachine.supportsAutoOutputItems()) {
            configuratorPanel.attachConfigurators(createAutoOutputItemConfigurator(autoOutputMachine));
        }
    }

    static IFancyConfiguratorButton.Toggle createAutoOutputFluidConfigurator(
                                                                             AutoOutputMachine autoOutputMachine) {
        return createAutoOutputConfigurator(
                GuiTextures.group(
                        GuiTextures.TOGGLE_BUTTON_BACK.getSubTexture(0, 0, 1, 0.5),
                        GuiTextures.IO_CONFIG_FLUID_MODES_BUTTON.getSubTexture(0, 1 / 3f, 1, 1 / 3f)),
                GuiTextures.group(
                        GuiTextures.TOGGLE_BUTTON_BACK.getSubTexture(0, 0.5, 1, 0.5),
                        GuiTextures.IO_CONFIG_FLUID_MODES_BUTTON.getSubTexture(0, 2 / 3f, 1, 1 / 3f)),
                autoOutputMachine::isAutoOutputFluids,
                "gtpm.gui.fluid_auto_output",
                (event, enabled) -> {
                    autoOutputMachine.setAllowAutoOutputFluids(enabled);
                    event.stopImmediatePropagation();
                    event.hasHandler = true;
                });
    }

    static IFancyConfiguratorButton.Toggle createAutoOutputItemConfigurator(
                                                                            AutoOutputMachine autoOutputMachine) {
        return createAutoOutputConfigurator(
                GuiTextures.group(
                        GuiTextures.TOGGLE_BUTTON_BACK.getSubTexture(0, 0, 1, 0.5),
                        GuiTextures.IO_CONFIG_ITEM_MODES_BUTTON.getSubTexture(0, 1 / 3f, 1, 1 / 3f)),
                GuiTextures.group(
                        GuiTextures.TOGGLE_BUTTON_BACK.getSubTexture(0, 0.5, 1, 0.5),
                        GuiTextures.IO_CONFIG_ITEM_MODES_BUTTON.getSubTexture(0, 2 / 3f, 1, 1 / 3f)),
                autoOutputMachine::isAutoOutputItems,
                "gtpm.gui.item_auto_output",
                (event, enabled) -> {
                    autoOutputMachine.setAllowAutoOutputItems(enabled);
                    event.stopImmediatePropagation();
                    event.hasHandler = true;
                });
    }

    private static IFancyConfiguratorButton.Toggle createAutoOutputConfigurator(
                                                                                IGuiTexture disabledIcon,
                                                                                IGuiTexture enabledIcon,
                                                                                BooleanSupplier stateSupplier,
                                                                                String tooltipBaseLangKey,
                                                                                BiConsumer<UIEvent, Boolean> clickHandler) {
        return new IFancyConfiguratorButton.Toggle(disabledIcon, enabledIcon, stateSupplier, clickHandler)
                .setTooltipsSupplier(enabled -> List.of(Component.translatable(
                        tooltipBaseLangKey + "." + (enabled ? "enabled" : "disabled"))));
    }
}
