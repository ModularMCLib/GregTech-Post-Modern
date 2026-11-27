package com.modularmc.gtpm.data;

import com.modularmc.gtpm.api.registry.registrate.provider.GTBlockstateProvider;
import com.modularmc.gtpm.common.registry.GTRegistration;
import com.modularmc.gtpm.data.datamap.DataMapsHandler;
import com.modularmc.gtpm.data.lang.LangHandler;
import com.modularmc.gtpm.data.model.BlockstateModelLoader;
import com.modularmc.gtpm.data.tags.BlockTagLoader;
import com.modularmc.gtpm.data.tags.EntityTypeTagLoader;
import com.modularmc.gtpm.data.tags.FluidTagLoader;
import com.modularmc.gtpm.data.tags.ItemTagLoader;

import com.modularmc.registrate.providers.core.ProviderType;

import net.minecraft.data.DataProvider;

public class GregTechDatagen {

    public static void initPre() {
        DataProvider.INDENT_WIDTH.set(4);
        ProviderType.registerClientProvider("model",
                () -> context -> new GTBlockstateProvider(context.parent(), context.output(), context.fileHelper()));

        GTRegistration.REGISTRATE.addDataGenerator(ProviderType.BLOCKSTATE,
                p -> BlockstateModelLoader.init((GTBlockstateProvider) p));
    }

    public static void initPost() {
        GTRegistration.REGISTRATE.addDataGenerator(ProviderType.BLOCK_TAGS, BlockTagLoader::init);
        GTRegistration.REGISTRATE.addDataGenerator(ProviderType.ITEM_TAGS, ItemTagLoader::init);
        GTRegistration.REGISTRATE.addDataGenerator(ProviderType.FLUID_TAGS, FluidTagLoader::init);
        GTRegistration.REGISTRATE.addDataGenerator(ProviderType.ENTITY_TAGS, EntityTypeTagLoader::init);
        GTRegistration.REGISTRATE.addDataGenerator(ProviderType.LANG, LangHandler::init);
        GTRegistration.REGISTRATE.addDataGenerator(ProviderType.DATA_MAP, DataMapsHandler::init);
    }
}
