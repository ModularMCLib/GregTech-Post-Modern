package com.modularmc.gtpm.data.recipe.generated;

import com.modularmc.gtpm.api.data.chemical.ChemicalHelper;
import com.modularmc.gtpm.api.data.chemical.material.Material;
import com.modularmc.gtpm.api.data.chemical.material.properties.PropertyKey;
import com.modularmc.gtpm.api.data.chemical.material.stack.MaterialStack;
import com.modularmc.gtpm.api.data.tag.TagPrefix;
import com.modularmc.gtpm.api.registry.GTRegistries;
import com.modularmc.gtpm.data.recipe.misc.RecyclingRecipes;

import net.minecraft.data.recipes.RecipeOutput;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.modularmc.gtpm.api.data.chemical.material.info.MaterialFlags.*;
import static com.modularmc.gtpm.api.data.tag.TagPrefix.*;

public final class RecyclingRecipeHandler {

    private static final List<TagPrefix> IGNORE_ARC_SMELTING = Arrays.asList(ingot, gem, nugget);

    private RecyclingRecipeHandler() {}

    public static void run(@NotNull RecipeOutput provider, @NotNull Material material) {
        // registers universal maceration recipes for specified ore prefixes
        for (TagPrefix prefix : GTRegistries.TAG_PREFIXES) {
            if (prefix.generateRecycling()) {
                processCrushing(provider, prefix, material);
            }
        }
    }

    private static void processCrushing(@NotNull RecipeOutput provider, @NotNull TagPrefix prefix,
                                        @NotNull Material material) {
        if (!material.shouldGenerateRecipesFor(prefix) || !material.hasProperty(PropertyKey.DUST)) {
            return;
        }

        ArrayList<MaterialStack> materialStacks = new ArrayList<>();
        materialStacks.add(new MaterialStack(material, prefix.getMaterialAmount(material)));
        materialStacks.addAll(prefix.secondaryMaterials());
        // only ignore arc smelting for blacklisted prefixes if yielded material is the same as input material
        // if arc smelting gives different material, allow it
        boolean ignoreArcSmelting = IGNORE_ARC_SMELTING.contains(prefix) &&
                !(material.hasProperty(PropertyKey.INGOT) &&
                        material.getProperty(PropertyKey.INGOT).getArcSmeltingInto() != material);
        RecyclingRecipes.registerRecyclingRecipes(provider, ChemicalHelper.get(prefix, material), materialStacks,
                ignoreArcSmelting, prefix);
    }
}
