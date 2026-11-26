package com.modularmc.gtpm.integration.jei;

import com.modularmc.gtpm.GTPM;
import com.modularmc.gtpm.api.recipe.category.GTRecipeCategory;
import com.modularmc.gtpm.api.registry.GTRegistries;
import com.modularmc.gtpm.common.data.GTFluids;
import com.modularmc.gtpm.common.data.GTItems;
import com.modularmc.gtpm.common.data.GTRecipeTypes;
import com.modularmc.gtpm.common.data.machines.GTMultiMachines;
import com.modularmc.gtpm.common.fluid.potion.PotionFluid;
import com.modularmc.gtpm.common.item.behavior.IntCircuitBehaviour;
import com.modularmc.gtpm.config.ConfigHolder;
import com.modularmc.gtpm.integration.jei.circuit.GTProgrammedCircuitCategory;
import com.modularmc.gtpm.integration.jei.multipage.MultiblockInfoCategory;
import com.modularmc.gtpm.integration.jei.oreprocessing.GTOreProcessingInfoCategory;
import com.modularmc.gtpm.integration.jei.orevein.GTBedrockFluidInfoCategory;
import com.modularmc.gtpm.integration.jei.orevein.GTBedrockOreInfoCategory;
import com.modularmc.gtpm.integration.jei.orevein.GTOreVeinInfoCategory;
import com.modularmc.gtpm.integration.jei.recipe.GTRecipeJEICategory;
import com.modularmc.gtpm.integration.jei.subtype.CircuitSubtypeInterpreter;
import com.modularmc.gtpm.integration.jei.subtype.MaterialSubtypeInterpreter;
import com.modularmc.gtpm.integration.jei.subtype.PotionFluidSubtypeInterpreter;
import com.modularmc.gtpm.integration.xei.widgets.GTProgrammedCircuitWidget;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.helpers.IPlatformFluidHelper;
import mezz.jei.api.neoforge.NeoForgeTypes;
import mezz.jei.api.registration.*;
import mezz.jei.api.runtime.IJeiRuntime;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@JeiPlugin
public class GTJEIPlugin implements IModPlugin {

    private MultiblockInfoCategory multiblocks;
    public static IJeiRuntime jeiRuntime;
    public static IJeiHelpers jeiHelpers;

    @Override
    public @NotNull ResourceLocation getPluginUid() {
        return GTPM.id("jei_plugin");
    }

    @Override
    public void onRuntimeAvailable(@NotNull IJeiRuntime jeiRuntime) {
        GTJEIPlugin.jeiRuntime = jeiRuntime;
    }

    @Nullable
    public static Object getItemIngredient(ItemStack itemStack, int x, int y, int width, int height) {
        return jeiHelpers.getIngredientManager().createTypedIngredient(VanillaTypes.ITEM_STACK, itemStack, false)
                .map(typedIngredient -> new GTClickableIngredient<>(typedIngredient, x, y, width, height))
                .orElse(null);
    }

    @Override
    public void registerCategories(@NotNull IRecipeCategoryRegistration registry) {
        if (!GTPM.MODS.isJEILoaded()) return;

        jeiHelpers = registry.getJeiHelpers();
        multiblocks = new MultiblockInfoCategory(jeiHelpers);
        registry.addRecipeCategories(multiblocks);
        if (!ConfigHolder.INSTANCE.compat.hideOreProcessingDiagrams)
            registry.addRecipeCategories(new GTOreProcessingInfoCategory(jeiHelpers));
        registry.addRecipeCategories(new GTOreVeinInfoCategory(jeiHelpers));
        registry.addRecipeCategories(new GTBedrockFluidInfoCategory(jeiHelpers));
        if (ConfigHolder.INSTANCE.machines.doBedrockOres)
            registry.addRecipeCategories(new GTBedrockOreInfoCategory(jeiHelpers));
        for (GTRecipeCategory category : GTRegistries.RECIPE_CATEGORIES) {
            if (category.shouldRegisterDisplays()) {
                registry.addRecipeCategories(new GTRecipeJEICategory(category));
            }
        }
        registry.addRecipeCategories(new GTProgrammedCircuitCategory(jeiHelpers));
    }

    @Override
    public void registerAdvanced(@NotNull IAdvancedRegistration registration) {
        if (!GTPM.MODS.isJEILoaded()) return;
        registration.addTypedRecipeManagerPlugin(MultiblockInfoCategory.RECIPE_TYPE, multiblocks);
    }

    @Override
    public void registerRecipeCatalysts(@NotNull IRecipeCatalystRegistration registration) {
        if (!GTPM.MODS.isJEILoaded()) return;

        GTRecipeJEICategory.registerRecipeCatalysts(registration);
        if (!ConfigHolder.INSTANCE.compat.hideOreProcessingDiagrams)
            GTOreProcessingInfoCategory.registerRecipeCatalysts(registration);
        GTOreVeinInfoCategory.registerRecipeCatalysts(registration);
        GTBedrockFluidInfoCategory.registerRecipeCatalysts(registration);
        if (ConfigHolder.INSTANCE.machines.doBedrockOres)
            GTBedrockOreInfoCategory.registerRecipeCatalysts(registration);
        registration.addRecipeCatalyst(GTMultiMachines.LARGE_CHEMICAL_REACTOR.asStack(),
                GTRecipeJEICategory.TYPES.apply(GTRecipeTypes.CHEMICAL_RECIPES.getCategory()));
        registration.addRecipeCatalyst(IntCircuitBehaviour.stack(0), GTProgrammedCircuitCategory.RECIPE_TYPE);
    }

    @Override
    public void registerRecipes(@NotNull IRecipeRegistration registration) {
        if (!GTPM.MODS.isJEILoaded()) return;

        GTRecipeJEICategory.registerRecipes(registration);
        if (!ConfigHolder.INSTANCE.compat.hideOreProcessingDiagrams)
            GTOreProcessingInfoCategory.registerRecipes(registration);
        GTOreVeinInfoCategory.registerRecipes(registration);
        GTBedrockFluidInfoCategory.registerRecipes(registration);
        if (ConfigHolder.INSTANCE.machines.doBedrockOres)
            GTBedrockOreInfoCategory.registerRecipes(registration);
        registration.addRecipes(GTProgrammedCircuitCategory.RECIPE_TYPE, List.of(new GTProgrammedCircuitWidget()));
    }

    @Override
    public void registerItemSubtypes(@NotNull ISubtypeRegistration registration) {
        if (!GTPM.MODS.isJEILoaded()) return;

        registration.registerSubtypeInterpreter(GTItems.PROGRAMMED_CIRCUIT.asItem(),
                CircuitSubtypeInterpreter.INSTANCE);
        registration.registerSubtypeInterpreter(GTItems.TURBINE_ROTOR.asItem(), MaterialSubtypeInterpreter.INSTANCE);
    }

    @Override
    public <T> void registerFluidSubtypes(@NotNull ISubtypeRegistration registration,
                                          @NotNull IPlatformFluidHelper<T> platformFluidHelper) {
        if (!GTPM.MODS.isJEILoaded()) return;

        PotionFluidSubtypeInterpreter interpreter = PotionFluidSubtypeInterpreter.INSTANCE;
        PotionFluid potionFluid = GTFluids.POTION.get();
        registration.registerSubtypeInterpreter(NeoForgeTypes.FLUID_STACK, potionFluid.getSource(), interpreter);
        registration.registerSubtypeInterpreter(NeoForgeTypes.FLUID_STACK, potionFluid.getFlowing(), interpreter);
    }

    @Override
    public void registerExtraIngredients(@NotNull IExtraIngredientRegistration registration) {
        if (!GTPM.MODS.isJEILoaded()) return;

        Collection<FluidStack> potionFluids = new ArrayList<>(BuiltInRegistries.POTION.size());
        BuiltInRegistries.POTION.holders().forEach(potion -> {
            FluidStack potionFluid = PotionFluid.of(1000, potion);
            potionFluids.add(potionFluid);
        });
        registration.addExtraIngredients(NeoForgeTypes.FLUID_STACK, potionFluids);
    }
}
