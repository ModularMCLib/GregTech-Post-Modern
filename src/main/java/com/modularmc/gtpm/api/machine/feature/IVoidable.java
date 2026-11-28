package com.modularmc.gtpm.api.machine.feature;

import com.modularmc.gtpm.api.capability.recipe.FluidRecipeCapability;
import com.modularmc.gtpm.api.capability.recipe.ItemRecipeCapability;
import com.modularmc.gtpm.api.capability.recipe.RecipeCapability;
import com.modularmc.gtpm.api.gui.GuiTextures;
import com.modularmc.gtpm.api.gui.SelectableEnum;
import com.modularmc.gtpm.api.gui.texture.IGuiTexture;

import com.modularmc.registrate.localization.MinecraftLocale;
import com.modularmc.registrate.localization.annotation.TransVariants;
import com.modularmc.registrate.localization.annotation.Translation;
import com.modularmc.registrate.localization.annotation.Variant;

import net.minecraft.util.StringRepresentable;

import it.unimi.dsi.fastutil.objects.Reference2IntMap;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

@TransVariants(keyPrefix = "gtpm.gui",
               value = {
                       @Variant(id = "all_voiding",
                                source = "§7Voiding §cAll",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7销毁§c全部")),
                       @Variant(id = "fluid_voiding",
                                source = "§7Voiding §9Fluids",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7销毁§9流体")),
                       @Variant(id = "item_voiding",
                                source = "§7Voiding §6Items",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7销毁§6物品")),
                       @Variant(id = "no_voiding",
                                source = "§7Voiding Nothing",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7不销毁")),
               })
public interface IVoidable extends IMachineFeature {

    default boolean canVoidRecipeOutputs(RecipeCapability<?> capability) {
        return getVoidingMode().canVoid(capability) ||
                self().getDefinition().getRecipeOutputLimits().getOrDefault(capability, -1) == 0;
    }

    default Reference2IntMap<RecipeCapability<?>> getOutputLimits() {
        return self().getDefinition().getRecipeOutputLimits();
    }

    default void setVoidingMode(VoidingMode mode) {}

    default VoidingMode getVoidingMode() {
        return VoidingMode.VOID_NONE;
    }

    enum VoidingMode implements StringRepresentable, SelectableEnum {

        VOID_NONE("gtpm.gui.no_voiding", cap -> false),
        VOID_ITEMS("gtpm.gui.item_voiding", cap -> cap == ItemRecipeCapability.CAP),
        VOID_FLUIDS("gtpm.gui.fluid_voiding", cap -> cap == FluidRecipeCapability.CAP),
        VOID_ITEMS_FLUIDS("gtpm.gui.all_voiding",
                cap -> cap == ItemRecipeCapability.CAP || cap == FluidRecipeCapability.CAP);

        public static final VoidingMode[] VALUES = values();

        private final String localeName;
        @Getter
        private final IGuiTexture icon;
        private final Predicate<RecipeCapability<?>> canVoid;

        VoidingMode(String name, Predicate<RecipeCapability<?>> canVoid) {
            this.localeName = name;
            this.canVoid = canVoid;
            this.icon = GuiTextures.BUTTON_VOID_MULTIBLOCK.getSubTexture(0, ordinal() * 0.25, 1, 0.25);
        }

        public boolean canVoid(RecipeCapability<?> capability) {
            return canVoid.test(capability);
        }

        @NotNull
        @Override
        public String getSerializedName() {
            return localeName;
        }

        @Override
        public @NotNull String getTooltip() {
            return localeName;
        }
    }
}
