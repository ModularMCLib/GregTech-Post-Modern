package com.modularmc.gtpm.common.cover.data;

import com.modularmc.gtpm.api.capability.recipe.IO;
import com.modularmc.gtpm.api.gui.GuiTextures;
import com.modularmc.gtpm.api.gui.SelectableEnum;

import com.modularmc.gtpm.api.gui.texture.IGuiTexture;

public enum FilterMode implements SelectableEnum {

    FILTER_INSERT("filter_insert"),
    FILTER_EXTRACT("filter_extract"),
    FILTER_BOTH("filter_both");

    public static final FilterMode[] VALUES = values();

    public final String localeName;

    FilterMode(String localeName) {
        this.localeName = localeName;
    }

    @Override
    public String getTooltip() {
        return "cover.filter.mode." + this.localeName;
    }

    @Override
    public IGuiTexture getIcon() {
        return GuiTextures.resource("gtpm:textures/gui/icon/filter_mode/" + localeName + ".png");
    }

    public boolean filters(IO io) {
        return (this == FILTER_INSERT && io.support(IO.IN)) || (this == FILTER_EXTRACT && io.support(IO.OUT)) ||
                (this == FILTER_BOTH);
    }
}
