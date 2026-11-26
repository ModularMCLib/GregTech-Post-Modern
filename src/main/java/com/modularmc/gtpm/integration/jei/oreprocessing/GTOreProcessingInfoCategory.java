package com.modularmc.gtpm.integration.jei.oreprocessing;

import com.modularmc.gtpm.GTPM;
import com.modularmc.gtpm.api.GTValues;
import com.modularmc.gtpm.api.data.chemical.ChemicalHelper;
import com.modularmc.gtpm.api.data.chemical.material.Material;
import com.modularmc.gtpm.api.data.chemical.material.info.MaterialFlags;
import com.modularmc.gtpm.api.data.chemical.material.properties.PropertyKey;
import com.modularmc.gtpm.api.registry.GTRegistries;
import com.modularmc.gtpm.integration.xei.widgets.GTOreByProductWidget;

import com.lowdragmc.lowdraglib2.integration.xei.jei.ModularUIRecipeCategory;

import net.minecraft.network.chat.Component;

import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import org.jetbrains.annotations.NotNull;

import static com.modularmc.gtpm.api.data.tag.TagPrefix.rawOre;
import static com.modularmc.gtpm.common.data.GTMachines.*;
import static com.modularmc.gtpm.common.data.GTMaterials.Iron;

public class GTOreProcessingInfoCategory extends ModularUIRecipeCategory<Material> {

    public final static RecipeType<Material> RECIPE_TYPE = new RecipeType<>(GTPM.id("ore_processing_diagram"),
            Material.class);
    private final int width;
    private final int height;
    private final IDrawable icon;

    public GTOreProcessingInfoCategory(IJeiHelpers helpers) {
        super(GTOreByProductWidget::createModularUI);
        this.width = GTOreByProductWidget.JEI_WIDTH;
        this.height = GTOreByProductWidget.JEI_HEIGHT;
        this.icon = helpers.getGuiHelper().createDrawableItemStack(ChemicalHelper.get(rawOre, Iron));
    }

    public static void registerRecipes(IRecipeRegistration registry) {
        registry.addRecipes(RECIPE_TYPE, GTRegistries.MATERIALS.stream()
                .filter(material -> material.hasProperty(PropertyKey.ORE) &&
                        !material.hasFlag(MaterialFlags.NO_ORE_PROCESSING_TAB))
                .toList());
    }

    public static void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(MACERATOR[GTValues.LV].asStack(), RECIPE_TYPE);
        registration.addRecipeCatalyst(ORE_WASHER[GTValues.LV].asStack(), RECIPE_TYPE);
        registration.addRecipeCatalyst(THERMAL_CENTRIFUGE[GTValues.LV].asStack(), RECIPE_TYPE);
        registration.addRecipeCatalyst(CENTRIFUGE[GTValues.LV].asStack(), RECIPE_TYPE);
        registration.addRecipeCatalyst(CHEMICAL_BATH[GTValues.LV].asStack(), RECIPE_TYPE);
        registration.addRecipeCatalyst(ELECTROMAGNETIC_SEPARATOR[GTValues.LV].asStack(), RECIPE_TYPE);
        registration.addRecipeCatalyst(SIFTER[GTValues.LV].asStack(), RECIPE_TYPE);
    }

    @Override
    @NotNull
    public RecipeType<Material> getRecipeType() {
        return RECIPE_TYPE;
    }

    @NotNull
    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public @NotNull Component getTitle() {
        return Component.translatable("gtpm.jei.ore_processing_diagram");
    }
}
