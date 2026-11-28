package com.modularmc.gtpm.api.machine.multiblock.part;

import com.modularmc.gtpm.api.blockentity.BlockEntityCreationInfo;
import com.modularmc.gtpm.api.capability.IControllable;
import com.modularmc.gtpm.api.capability.recipe.IO;
import com.modularmc.gtpm.api.sync_system.annotations.RerenderOnChanged;
import com.modularmc.gtpm.api.sync_system.annotations.SaveField;
import com.modularmc.gtpm.api.sync_system.annotations.ServerFieldChangeListener;
import com.modularmc.gtpm.api.sync_system.annotations.SyncBoth;

import com.modularmc.registrate.localization.MinecraftLocale;
import com.modularmc.registrate.localization.annotation.Trans;
import com.modularmc.registrate.localization.annotation.TransVariants;
import com.modularmc.registrate.localization.annotation.Translation;
import com.modularmc.registrate.localization.annotation.Variant;

import lombok.Getter;

@Trans(key = "gtpm.multiblock.page_switcher.io.export",
       source = "§4Outputs",
       translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§4输出"))
@TransVariants(keyPrefix = "gtpm.multiblock.page_switcher.io",
               value = {
                       @Variant(id = "both",
                                source = "§5Combined Inputs + Outputs",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§5兼顾输入/输出")),
                       @Variant(id = "import",
                                source = "§2Inputs",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§2输入")),
               })
public class TieredIOPartMachine extends TieredPartMachine implements IControllable {

    protected final IO io;

    /**
     * AUTO IO working?
     */
    @Getter
    @SaveField
    @SyncBoth
    @RerenderOnChanged
    protected boolean workingEnabled;

    public TieredIOPartMachine(BlockEntityCreationInfo info, int tier, IO io) {
        super(info, tier);
        this.io = io;
        this.workingEnabled = true;
    }

    @Override
    public void setWorkingEnabled(boolean workingEnabled) {
        this.workingEnabled = workingEnabled;
        onWorkingEnabledChanged();
    }

    /**
     * Rebuilds subclass runtime state after either a direct setter call or an accepted client update.
     */
    @ServerFieldChangeListener(fieldName = "workingEnabled")
    protected void onWorkingEnabledChanged() {}

    //////////////////////////////////////
    // ***** Initialization ******//
    //////////////////////////////////////

    @Override
    public PageGroupingData getPageGroupingData() {
        return switch (io) {
            case IN -> new PageGroupingData("gtpm.multiblock.page_switcher.io.import", 1);
            case OUT -> new PageGroupingData("gtpm.multiblock.page_switcher.io.export", 2);
            case BOTH -> new PageGroupingData("gtpm.multiblock.page_switcher.io.both", 3);
            case NONE -> null;
        };
    }
}
