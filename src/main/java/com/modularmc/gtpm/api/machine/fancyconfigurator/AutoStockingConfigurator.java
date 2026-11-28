package com.modularmc.gtpm.api.machine.fancyconfigurator;

import com.modularmc.gtpm.api.gui.GuiTextures;
import com.modularmc.gtpm.api.gui.UITemplate;
import com.modularmc.gtpm.api.gui.element.GTIntInputElement;
import com.modularmc.gtpm.api.gui.element.GTLabelElement;
import com.modularmc.gtpm.api.gui.fancy.IFancyConfigurator;
import com.modularmc.gtpm.api.gui.texture.IGuiTexture;
import com.modularmc.gtpm.common.data.GTItems;
import com.modularmc.gtpm.config.GTPMConfigHolder;
import com.modularmc.gtpm.integration.ae2.machine.feature.multiblock.AutoStockingPart;

import com.modularmc.registrate.localization.MinecraftLocale;
import com.modularmc.registrate.localization.annotation.TransVariants;
import com.modularmc.registrate.localization.annotation.Translation;
import com.modularmc.registrate.localization.annotation.Variant;

import com.lowdragmc.lowdraglib2.gui.ui.UIElement;
import com.lowdragmc.lowdraglib2.gui.ui.elements.Button.State;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.chat.Component;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * LDLib2 Fancy page for advanced auto-stocking item and fluid thresholds.
 *
 * <p>
 * The page updates managed fields; the owning UI synchronizes their changes.
 */
@TransVariants(keyPrefix = "gtpm.gui",
               value = {
                       @Variant(id = "adv_stocking_config.ticks_per_cycle",
                                source = "Delay between item list updates",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "物品列表更新之间的延迟")),
                       @Variant(id = "adv_stocking_config.title",
                                source = "Configure Automatic Stocking",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "配置库存自动拉取")),
                       @Variant(id = "title.adv_stocking_config.ticks_per_cycle",
                                source = "Ticks Per Cycle",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "每次输出间隔Tick")),
               })
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public final class AutoStockingConfigurator implements IFancyConfigurator {

    private static final int CONFIGURATOR_WIDTH = 90;
    private static final int CONFIGURATOR_HEIGHT = 70;

    private final AutoStockingPart machine;

    /**
     * Creates an advanced auto-stocking configurator for the machine's annotated fields.
     */
    public AutoStockingConfigurator(AutoStockingPart machine) {
        this.machine = machine;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("gtpm.gui.adv_stocking_config.title");
    }

    @Override
    public IGuiTexture getIcon(State state) {
        return GuiTextures.itemStack(GTItems.TOOL_DATA_STICK.asStack());
    }

    @Override
    public UIElement createConfigurator() {
        UIElement root = new UIElement();
        UITemplate.setBounds(root, 0, 0, CONFIGURATOR_WIDTH, CONFIGURATOR_HEIGHT);

        String suffix = switch (machine.getStockingTarget()) {
            case ITEM -> "min_item_count";
            case FLUID -> "min_fluid_count";
        };
        root.addChild(new GTLabelElement(4, 2, 81, 10,
                "gtpm.gui.title.adv_stocking_config." + suffix, true));

        GTIntInputElement minStackSizeInput = new GTIntInputElement(4, 12, 81, 14, machine::getMinStackSize,
                machine::setMinStackSize);
        minStackSizeInput.setMin(1);
        minStackSizeInput.style(style -> style.tooltips(Component.translatable(
                "gtpm.gui.adv_stocking_config." + suffix)));
        root.addChild(minStackSizeInput);

        root.addChild(new GTLabelElement(4, 36, 81, 10,
                "gtpm.gui.title.adv_stocking_config.ticks_per_cycle", true));

        GTIntInputElement ticksPerCycleInput = new GTIntInputElement(4, 46, 81, 14, machine::getTicksPerCycle,
                machine::setTicksPerCycle);
        ticksPerCycleInput.setMin(GTPMConfigHolder.INSTANCE.compat.ae2.updateIntervals);
        ticksPerCycleInput.style(style -> style.tooltips(Component.translatable(
                "gtpm.gui.adv_stocking_config.ticks_per_cycle")));
        root.addChild(ticksPerCycleInput);

        return root;
    }

    @Override
    public int getConfiguratorWidth() {
        return CONFIGURATOR_WIDTH;
    }

    @Override
    public int getConfiguratorHeight() {
        return CONFIGURATOR_HEIGHT;
    }
}
