package com.modularmclib.gtpm.api.gui.element;

import com.modularmclib.gtpm.GTPM;
import com.modularmclib.gtpm.api.gui.UITemplate;

import com.lowdragmc.lowdraglib2.gui.ui.elements.ScrollerView;
import com.lowdragmc.lowdraglib2.registry.annotation.LDLRegister;

import net.minecraft.MethodsReturnNonnullByDefault;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * LDLib2 scroller view facade for GTM pixel-positioned scroll containers.
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
@LDLRegister(name = GTPM.MOD_ID + "-scroller-view", group = GTPM.MOD_ID, registry = "ldlib2:ui_element")
public class GTScrollerViewElement extends ScrollerView {

    public GTScrollerViewElement() {}

    public GTScrollerViewElement(int x, int y, int width, int height) {
        UITemplate.setBounds(this, x, y, width, height);
    }
}
