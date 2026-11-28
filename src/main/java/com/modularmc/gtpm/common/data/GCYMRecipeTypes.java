package com.modularmc.gtpm.common.data;

import com.modularmc.gtpm.api.block.ICoilType;
import com.modularmc.gtpm.api.capability.recipe.IO;
import com.modularmc.gtpm.api.gui.GuiTextures;
import com.modularmc.gtpm.api.recipe.GTRecipeType;
import com.modularmc.gtpm.api.recipe.RecipeData;
import com.modularmc.gtpm.utils.FormattingUtil;

import com.modularmc.registrate.localization.MinecraftLocale;
import com.modularmc.registrate.localization.annotation.TransVariants;
import com.modularmc.registrate.localization.annotation.Translation;
import com.modularmc.registrate.localization.annotation.Variant;

import com.lowdragmc.lowdraglib2.utils.LocalizationUtils;

import net.minecraft.client.resources.language.I18n;

import static com.modularmc.gtpm.api.gui.texture.ProgressTexture.FillDirection.LEFT_TO_RIGHT;
import static com.modularmc.gtpm.common.data.GTRecipeTypes.MULTIBLOCK;
import static com.modularmc.gtpm.common.registry.GTRegistration.REGISTRATE;

@TransVariants(keyPrefix = "gtpm.recipe",
               value = {
                       @Variant(id = "coil.tier",
                                source = "Coil: %s",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "线圈：%s")),
                       @Variant(id = "temperature",
                                source = "Temp: %s",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "温度：%s")),
               })
public class GCYMRecipeTypes {

    //////////////////////////////////////
    // ******* Multiblock *******//
    //////////////////////////////////////
    public final static GTRecipeType ALLOY_BLAST_RECIPES = REGISTRATE.recipeType("alloy_blast_smelter", MULTIBLOCK)
            .localization(text -> text
                    .source("Alloy Blast Smelter")
                    .translation(MinecraftLocale.ZH_CN, "合金冶炼炉"))
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
            .setSound(GTSoundEntries.ARC)
            .register();

    public static void init() {}
}
