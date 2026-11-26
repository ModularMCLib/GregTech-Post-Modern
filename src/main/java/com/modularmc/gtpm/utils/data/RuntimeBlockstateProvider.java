package com.modularmc.gtpm.utils.data;

import com.modularmc.gtpm.GTPM;
import com.modularmc.gtpm.api.registry.registrate.provider.GTBlockstateProvider;
import com.modularmc.gtpm.common.registry.GTRegistration;
import com.modularmc.gtpm.data.pack.GTDynamicResourcePack;

import com.modularmc.registrate.AbstractRegistrate;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.*;

import com.google.gson.JsonElement;

import java.util.Map;
import java.util.function.BiConsumer;

public class RuntimeBlockstateProvider extends GTBlockstateProvider {

    // Fake a data provider for the GT model builders so we don't need to handle this ourselves in any way :3
    public static final RuntimeBlockstateProvider INSTANCE = new RuntimeBlockstateProvider(
            GTRegistration.REGISTRATE, new PackOutput(GTPM.GTCEU_FOLDER),
            (loc, json) -> {
                if (!loc.getPath().endsWith(".json")) {
                    loc = loc.withSuffix(".json");
                }
                GTDynamicResourcePack.addResource(loc, json);
            });

    protected final BiConsumer<ResourceLocation, JsonElement> consumer;

    public RuntimeBlockstateProvider(AbstractRegistrate<?> parent, PackOutput packOutput,
                                     BiConsumer<ResourceLocation, JsonElement> consumer) {
        super(parent, packOutput, RuntimeExistingFileHelper.INSTANCE);
        this.consumer = consumer;
    }

    @Override
    protected void registerStatesAndModels() {}

    public void run() {
        processModelProvider(models());
        processModelProvider(itemModels());

        for (Map.Entry<Block, IGeneratedBlockState> entry : registeredBlocks.entrySet()) {
            ResourceLocation loc = GTDynamicResourcePack.BLOCKSTATE_ID_CONVERTER
                    .idToFile(BuiltInRegistries.BLOCK.getKey(entry.getKey()));
            this.consumer.accept(loc, entry.getValue().toJson());
        }
        // only clear the data *after* saving so we can keep track of it during the KJS event
        models().generatedModels.clear();
        itemModels().generatedModels.clear();
        registeredBlocks.clear();
    }

    public <T extends ModelBuilder<T>> void processModelProvider(ModelProvider<T> provider) {
        for (T model : provider.generatedModels.values()) {
            ResourceLocation loc = model.getLocation().withPrefix("models/");
            this.consumer.accept(loc, model.toJson());
        }
    }
}
