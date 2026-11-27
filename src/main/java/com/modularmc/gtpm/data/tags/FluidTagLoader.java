package com.modularmc.gtpm.data.tags;

import com.modularmc.gtpm.common.data.GTMaterials;
import com.modularmc.gtpm.data.recipe.CustomTags;

import com.modularmc.registrate.providers.RegistrateTagsProvider;

import net.minecraft.world.level.material.Fluid;

public class FluidTagLoader {

    public static void init(RegistrateTagsProvider.IntrinsicTags<Fluid> provider) {
        provider.tag(CustomTags.LIGHTER_FLUIDS).add(GTMaterials.Butane.getFluid(), GTMaterials.Propane.getFluid());
        provider.tag(CustomTags.HPCA_COOLANTS).add(GTMaterials.PCBCoolant.getFluid());
    }
}
