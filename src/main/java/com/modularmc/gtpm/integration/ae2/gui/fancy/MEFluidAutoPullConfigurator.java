package com.modularmc.gtpm.integration.ae2.gui.fancy;

import com.modularmc.gtpm.api.gui.GuiTextures;
import com.modularmc.gtpm.api.gui.fancy.IFancyConfiguratorButton;
import com.modularmc.gtpm.integration.ae2.machine.MEStockingHatchPartMachine;

import com.modularmc.registrate.localization.MinecraftLocale;
import com.modularmc.registrate.localization.annotation.TransVariants;
import com.modularmc.registrate.localization.annotation.Translation;
import com.modularmc.registrate.localization.annotation.Variant;

import net.minecraft.network.chat.Component;

import java.util.List;

@TransVariants(keyPrefix = "gtpm.gui.me_bus",
               value = {
                       @Variant(id = "auto_pull_button",
                                source = "Click to toggle automatic item pulling from ME",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "单击以切换ME自动拉取模式")),
               })
public final class MEFluidAutoPullConfigurator extends IFancyConfiguratorButton.Toggle {

    public MEFluidAutoPullConfigurator(MEStockingHatchPartMachine machine) {
        super(GuiTextures.BUTTON_AUTO_PULL.getSubTexture(0, 0, 1, 0.5),
                GuiTextures.BUTTON_AUTO_PULL.getSubTexture(0, 0.5, 1, 0.5),
                machine::isAutoPull, (event, pressed) -> {
                    machine.setAutoPull(pressed);
                    event.stopImmediatePropagation();
                    event.hasHandler = true;
                });
        setTooltipsSupplier(pressed -> List.of(Component.translatable("gtpm.gui.me_bus.auto_pull_button")));
    }
}
