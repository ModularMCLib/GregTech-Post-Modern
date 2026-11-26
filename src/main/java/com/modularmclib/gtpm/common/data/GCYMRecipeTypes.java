package com.modularmclib.gtpm.common.data;

import com.modularmclib.gtpm.api.block.ICoilType;
import com.modularmclib.gtpm.api.capability.recipe.IO;
import com.modularmclib.gtpm.api.gui.GuiTextures;
import com.modularmclib.gtpm.api.recipe.GTRecipeType;
import com.modularmclib.gtpm.api.recipe.RecipeData;
import com.modularmclib.gtpm.utils.FormattingUtil;

import com.lowdragmc.lowdraglib2.utils.LocalizationUtils;

import net.minecraft.client.resources.language.I18n;

import static com.modularmclib.gtpm.api.gui.texture.ProgressTexture.FillDirection.LEFT_TO_RIGHT;
import static com.modularmclib.gtpm.common.data.GTRecipeTypes.MULTIBLOCK;
import static com.modularmclib.gtpm.common.data.GTRecipeTypes.register;

public class GCYMRecipeTypes {

    //////////////////////////////////////
    // ******* Multiblock *******//
    //////////////////////////////////////
    public final static GTRecipeType ALLOY_BLAST_RECIPES = register("alloy_blast_smelter", MULTIBLOCK)
            .setMaxIOSize(9, 0, 3, 1)
            .setEUIO(IO.IN)
            .setProgressBar(
                    GuiTextures.progressBar(GuiTextures.PROGRESS_BAR_ARROW, LEFT_TO_RIGHT))
            .setSlotOverlay(false, false, false, GuiTextures.FURNACE_OVERLAY_1)
            .setSlotOverlay(false, false, true, GuiTextures.FURNACE_OVERLAY_1)
            .setSlotOverlay(false, true, false, GuiTextures.FURNACE_OVERLAY_2)
            .setSlotOverlay(false, true, true, GuiTextures.FURNACE_OVERLAY_2)
            .setSlotOverlay(true, true, false, GuiTextures.FURNACE_OVERLAY_2)
            .setSlotOverlay(true, true, true, GuiTextures.FURNACE_OVERLAY_2)
            .addDataInfo(data -> {
                int temp = RecipeData.getInt(data, "ebf_temp");
                return LocalizationUtils.format("gtpm.recipe.temperature", FormattingUtil.formatTemperature(temp));
            })
            .addDataInfo(data -> {
                int temp = RecipeData.getInt(data, "ebf_temp");
                ICoilType requiredCoil = ICoilType.getMinRequiredType(temp);

                if (requiredCoil != null && !requiredCoil.getMaterial().isNull()) {
                    return LocalizationUtils.format("gtpm.recipe.coil.tier",
                            I18n.get(requiredCoil.getMaterial().getUnlocalizedName()));
                }
                return "";
            })
            .setUiBuilder(
                    (recipe, root, rootSize) -> GTRecipeTypes.addHeatingCoilSlot(recipe, root, rootSize, 40))
            .setSound(GTSoundEntries.ARC);

    public static void init() {}
}
