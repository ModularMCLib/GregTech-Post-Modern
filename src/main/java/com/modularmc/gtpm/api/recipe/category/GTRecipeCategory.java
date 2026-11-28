package com.modularmc.gtpm.api.recipe.category;

import com.modularmc.gtpm.GTPM;
import com.modularmc.gtpm.api.gui.GuiTextures;
import com.modularmc.gtpm.api.gui.texture.IGuiTexture;
import com.modularmc.gtpm.api.recipe.GTRecipeDefinition;
import com.modularmc.gtpm.api.recipe.GTRecipeType;
import com.modularmc.gtpm.api.registry.GTRegistries;
import com.modularmc.gtpm.api.sync_system.FieldCodecs;
import com.modularmc.gtpm.common.data.GTRecipeTypes;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Accessors(chain = true)
public class GTRecipeCategory {

    static {
        FieldCodecs.register(GTRecipeCategory.class, GTRegistries.RECIPE_CATEGORIES.byNameCodec());
    }

    // Placeholder category used if category isn't defined for a recipe for registration
    public static final GTRecipeCategory DEFAULT = new GTRecipeCategory(GTPM.id("default"),
            GTRecipeTypes.DUMMY_RECIPES);

    public final ResourceLocation registryKey;
    public final String name;
    @Getter
    private final GTRecipeType recipeType;
    @Getter
    private final String languageKey;
    @Nullable
    @Setter
    private IGuiTexture icon = null;
    @Getter
    @Setter
    private boolean isXEIVisible = true;

    public GTRecipeCategory(@NotNull GTRecipeType recipeType) {
        this.recipeType = recipeType;
        this.name = recipeType.registryName.getPath();
        this.registryKey = recipeType.registryName;
        this.languageKey = recipeType.getTranslationKey();
    }

    public GTRecipeCategory(@NotNull ResourceLocation registryKey, @NotNull GTRecipeType recipeType) {
        this.recipeType = recipeType;
        this.name = registryKey.getPath();
        this.registryKey = registryKey;
        this.languageKey = registryKey.toLanguageKey("recipe_category");
    }

    public IGuiTexture getIcon() {
        if (icon == null) {
            if (recipeType.getIconSupplier() != null) icon = GuiTextures.itemStack(recipeType.getIconSupplier().get());
            else icon = GuiTextures.itemStack(Items.BARRIER);
        }
        return icon;
    }

    public void addRecipe(GTRecipeDefinition recipe) {
        recipeType.addToCategoryMap(this, recipe);
    }

    public boolean shouldRegisterDisplays() {
        return (isXEIVisible || GTPM.isDev()) &&
                this != GTRecipeTypes.FURNACE_RECIPES.getCategory();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof GTRecipeCategory that)) return false;
        return this.registryKey.equals(that.registryKey);
    }

    @Override
    public int hashCode() {
        return registryKey.hashCode();
    }

    @Override
    public String toString() {
        return "GTRecipeCategory{%s}".formatted(this.registryKey);
    }
}
