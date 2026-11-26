package com.modularmclib.gtpm.integration.emi.orevein;

import com.modularmclib.gtpm.GTPM;
import com.modularmclib.gtpm.api.registry.GTRegistries;
import com.modularmclib.gtpm.common.data.GTItems;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Items;

import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiStack;

public class GTOreVeinEmiCategory extends EmiRecipeCategory {

    public static final GTOreVeinEmiCategory CATEGORY = new GTOreVeinEmiCategory();

    public GTOreVeinEmiCategory() {
        super(GTPM.id("ore_vein_diagram"), EmiStack.of(Items.RAW_IRON));
    }

    public static void registerDisplays(EmiRegistry registry) {
        var fluids = Minecraft.getInstance().level.registryAccess()
                .registryOrThrow(GTRegistries.ORE_VEIN_REGISTRY);
        fluids.holders()
                .filter(ore -> ore.value().canGenerate())
                .forEach(ore -> registry.addRecipe(new GTEmiOreVein(ore)));
    }

    public static void registerWorkStations(EmiRegistry registry) {
        registry.addWorkstation(CATEGORY, EmiStack.of(GTItems.PROSPECTOR_LV.asStack()));
        registry.addWorkstation(CATEGORY, EmiStack.of(GTItems.PROSPECTOR_HV.asStack()));
        registry.addWorkstation(CATEGORY, EmiStack.of(GTItems.PROSPECTOR_LuV.asStack()));
    }

    @Override
    public Component getName() {
        return Component.translatable("gtpm.jei.ore_vein_diagram");
    }
}
