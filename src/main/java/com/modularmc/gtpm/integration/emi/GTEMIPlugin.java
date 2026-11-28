package com.modularmc.gtpm.integration.emi;

import com.modularmc.gtpm.api.gui.factory.UIRegistry;
import com.modularmc.gtpm.api.machine.MachineDefinition;
import com.modularmc.gtpm.api.machine.MultiblockMachineDefinition;
import com.modularmc.gtpm.api.recipe.category.GTRecipeCategory;
import com.modularmc.gtpm.api.registry.GTRegistries;
import com.modularmc.gtpm.common.data.GTFluids;
import com.modularmc.gtpm.common.data.GTItems;
import com.modularmc.gtpm.common.data.GTRecipeTypes;
import com.modularmc.gtpm.common.data.machines.GTMultiMachines;
import com.modularmc.gtpm.common.fluid.potion.PotionFluid;
import com.modularmc.gtpm.common.fluid.potion.PotionFluidHelper;
import com.modularmc.gtpm.common.item.behavior.IntCircuitBehaviour;
import com.modularmc.gtpm.config.GTPMConfigHolder;
import com.modularmc.gtpm.integration.emi.circuit.GTProgrammedCircuitCategory;
import com.modularmc.gtpm.integration.emi.multipage.MultiblockInfoEmiCategory;
import com.modularmc.gtpm.integration.emi.oreprocessing.GTOreProcessingEmiCategory;
import com.modularmc.gtpm.integration.emi.orevein.GTBedrockFluidEmiCategory;
import com.modularmc.gtpm.integration.emi.orevein.GTBedrockOreEmiCategory;
import com.modularmc.gtpm.integration.emi.orevein.GTOreVeinEmiCategory;
import com.modularmc.gtpm.integration.emi.recipe.GTEmiRecipeHandler;
import com.modularmc.gtpm.integration.emi.recipe.GTRecipeEMICategory;

import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.alchemy.PotionContents;
import net.neoforged.neoforge.fluids.FluidStack;

import dev.emi.emi.api.EmiEntrypoint;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.stack.Comparison;
import dev.emi.emi.api.stack.EmiStack;
import org.jetbrains.annotations.ApiStatus;

import java.util.Comparator;
import java.util.List;

@EmiEntrypoint
public class GTEMIPlugin implements EmiPlugin {

    @ApiStatus.Internal
    public static List<MachineDefinition> SORTED_MACHINES = null;

    @Override
    public void register(EmiRegistry registry) {
        if (SORTED_MACHINES == null) {
            SORTED_MACHINES = GTRegistries.MACHINES.stream()
                    .sorted(SORT_MACHINES_BY_TIER)
                    .toList();
        }

        // Categories
        registry.addCategory(MultiblockInfoEmiCategory.CATEGORY);
        if (!GTPMConfigHolder.INSTANCE.compat.hideOreProcessingDiagrams)
            registry.addCategory(GTOreProcessingEmiCategory.CATEGORY);
        registry.addCategory(GTOreVeinEmiCategory.CATEGORY);
        registry.addCategory(GTBedrockFluidEmiCategory.CATEGORY);
        if (GTPMConfigHolder.INSTANCE.machines.doBedrockOres)
            registry.addCategory(GTBedrockOreEmiCategory.CATEGORY);
        for (GTRecipeCategory category : GTRegistries.RECIPE_CATEGORIES) {
            if (category.shouldRegisterDisplays()) {
                registry.addCategory(GTRecipeEMICategory.CATEGORIES.apply(category));
            }
        }
        registry.addRecipeHandler(UIRegistry.MENU.get(), new GTEmiRecipeHandler());
        registry.addCategory(GTProgrammedCircuitCategory.CATEGORY);

        // Recipes
        MultiblockInfoEmiCategory.registerDisplays(registry);
        GTRecipeEMICategory.registerDisplays(registry);
        if (!GTPMConfigHolder.INSTANCE.compat.hideOreProcessingDiagrams)
            GTOreProcessingEmiCategory.registerDisplays(registry);
        GTOreVeinEmiCategory.registerDisplays(registry);
        GTBedrockFluidEmiCategory.registerDisplays(registry);
        if (GTPMConfigHolder.INSTANCE.machines.doBedrockOres)
            GTBedrockOreEmiCategory.registerDisplays(registry);
        GTProgrammedCircuitCategory.registerDisplays(registry);

        // workstations
        GTRecipeEMICategory.registerWorkStations(registry);
        if (!GTPMConfigHolder.INSTANCE.compat.hideOreProcessingDiagrams)
            GTOreProcessingEmiCategory.registerWorkStations(registry);
        GTOreVeinEmiCategory.registerWorkStations(registry);
        GTBedrockFluidEmiCategory.registerWorkStations(registry);
        if (GTPMConfigHolder.INSTANCE.machines.doBedrockOres)
            GTBedrockOreEmiCategory.registerWorkStations(registry);
        registry.addWorkstation(GTRecipeEMICategory.CATEGORIES.apply(GTRecipeTypes.CHEMICAL_RECIPES.getCategory()),
                EmiStack.of(GTMultiMachines.LARGE_CHEMICAL_REACTOR.asStack()));

        // Comparators
        registry.setDefaultComparison(GTItems.TURBINE_ROTOR.asItem(), Comparison.compareComponents());

        registry.setDefaultComparison(GTItems.PROGRAMMED_CIRCUIT.asItem(), Comparison.compareComponents());
        registry.removeEmiStacks(EmiStack.of(GTItems.PROGRAMMED_CIRCUIT.asStack()));
        registry.addEmiStack(EmiStack.of(IntCircuitBehaviour.stack(0)));
        registry.addWorkstation(GTProgrammedCircuitCategory.CATEGORY, EmiStack.of(IntCircuitBehaviour.stack(0)));

        Comparison potionComparison = Comparison.compareData(
                stack -> stack.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY));
        PotionFluid potionFluid = GTFluids.POTION.get();
        registry.setDefaultComparison(potionFluid.getSource(), potionComparison);
        registry.setDefaultComparison(potionFluid.getFlowing(), potionComparison);

        BuiltInRegistries.POTION.holders().forEach(potion -> {
            FluidStack stack = PotionFluidHelper.getFluidFromPotion(potion, PotionFluidHelper.BOTTLE_AMOUNT);
            registry.addEmiStack(EmiStack.of(stack.getFluid(), stack.getComponentsPatch()));
        });
    }

    public static final Comparator<MachineDefinition> SORT_MACHINES_BY_TIER = (a, b) -> {
        boolean isAMulti = a instanceof MultiblockMachineDefinition;
        boolean isBMulti = b instanceof MultiblockMachineDefinition;
        if (isAMulti && !isBMulti) {
            return 1;
        } else if (!isAMulti && isBMulti) {
            return -1;
        } else {
            return a.getTier() - b.getTier();
        }
    };
}
