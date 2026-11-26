package com.modularmc.gtpm.data.datamap;

import com.modularmc.gtpm.data.recipe.misc.ComposterRecipes;

import com.modularmc.registrate.providers.RegistrateDataMapProvider;

import net.neoforged.neoforge.registries.datamaps.builtin.Compostable;
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps;

public class DataMapsHandler {

    public static void init(RegistrateDataMapProvider provider) {
        final var compostables = provider.builder(NeoForgeDataMaps.COMPOSTABLES);
        ComposterRecipes.addComposterRecipes((item, chance) -> compostables.add(item.builtInRegistryHolder(),
                new Compostable(chance), false));
    }
}
