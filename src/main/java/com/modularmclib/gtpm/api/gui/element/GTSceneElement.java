package com.modularmclib.gtpm.api.gui.element;

import com.modularmclib.gtpm.GTPM;
import com.modularmclib.gtpm.api.gui.UITemplate;

import com.lowdragmc.lowdraglib2.gui.ui.elements.Scene;
import com.lowdragmc.lowdraglib2.registry.annotation.LDLRegister;

import net.minecraft.MethodsReturnNonnullByDefault;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * LDLib2 scene facade for GTM preview scenes.
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
@LDLRegister(name = GTPM.MOD_ID + "-scene", group = GTPM.MOD_ID, registry = "ldlib2:ui_element")
public class GTSceneElement extends Scene {

    public GTSceneElement() {}

    public GTSceneElement(int x, int y, int width, int height) {
        UITemplate.setBounds(this, x, y, width, height);
    }
}
