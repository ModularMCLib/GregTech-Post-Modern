package com.modularmc.gtpm.common.data.models;

import com.modularmc.gtpm.GTPM;
import com.modularmc.gtpm.api.block.*;
import com.modularmc.gtpm.api.block.property.GTBlockStateProperties;
import com.modularmc.gtpm.api.data.chemical.material.properties.PropertyKey;
import com.modularmc.gtpm.api.fluids.GTFluid;
import com.modularmc.gtpm.api.fluids.store.FluidStorage;
import com.modularmc.gtpm.api.fluids.store.FluidStorageKey;
import com.modularmc.gtpm.api.machine.multiblock.IBatteryData;
import com.modularmc.gtpm.api.registry.GTRegistries;
import com.modularmc.gtpm.api.registry.registrate.provider.GTBlockstateProvider;
import com.modularmc.gtpm.common.block.*;
import com.modularmc.gtpm.core.MixinHelpers;
import com.modularmc.gtpm.data.pack.GTDynamicResourcePack;

import com.modularmc.registrate.providers.DataGenContext;
import com.modularmc.registrate.providers.generators.RegistrateBlockModelGenerator;
import com.modularmc.registrate.providers.generators.RegistrateItemModelGenerator;
import com.modularmc.registrate.util.nullness.NonNullBiConsumer;

import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.ModelBuilder;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.client.model.generators.ModelProvider;

import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.IOException;

public class GTModels {

    public static final ResourceLocation BLANK_TEXTURE = GTPM.id("block/void");

    public static final String ACTIVE_SUFFIX = "_active";

    // region BLOCK MODELS

    public static NonNullBiConsumer<DataGenContext<Block, ? extends Block>, GTBlockstateProvider> createModelBlockState(ResourceLocation modelLocation) {
        return (ctx, prov) -> {
            prov.simpleBlock(ctx.getEntry(), prov.models().getExistingFile(modelLocation));
        };
    }

    public static void createCrossBlockState(DataGenContext<Block, ? extends Block> ctx,
                                             RegistrateBlockModelGenerator prov) {
        prov.simpleBlock(ctx.getEntry(), prov.models().cross(ctx.getName(), prov.blockTexture(ctx.getEntry())));
    }

    public static void cellModel(DataGenContext<Item, ? extends Item> ctx, RegistrateItemModelGenerator prov) {
        // empty model
        prov.getBuilder("item/" + prov.name(ctx::getEntry) + "_empty")
                .parent(new ModelFile.UncheckedModelFile("item/generated"))
                .texture("layer0", prov.modLoc("item/%s/base".formatted(prov.name(ctx))));

        // filled model
        prov.getBuilder("item/" + prov.name(ctx::getEntry) + "_filled")
                .parent(new ModelFile.UncheckedModelFile("item/generated"))
                .texture("layer0", prov.modLoc("item/%s/base".formatted(prov.name(ctx))))
                .texture("layer1", prov.modLoc("item/%s/overlay".formatted(prov.name(ctx))));

        // root model
        prov.generateFlatItem(ctx.get(), prov.modLoc("item/%s/base".formatted(prov.name(ctx))))
                .override().predicate(GTPM.id("fluid_cell"), 0)
                .model(new ModelFile.UncheckedModelFile(prov.modLoc("item/%s_empty".formatted(prov.name(ctx)))))
                .end()
                .override().predicate(GTPM.id("fluid_cell"), 1)
                .model(new ModelFile.UncheckedModelFile(prov.modLoc("item/%s_filled".formatted(prov.name(ctx)))))
                .end();
    }

    public static <
            T extends Item> NonNullBiConsumer<DataGenContext<Item, T>, RegistrateItemModelGenerator> overrideModel(ResourceLocation predicate,
                                                                                                                   int modelNumber) {
        if (modelNumber <= 0) return NonNullBiConsumer.noop();
        return (ctx, prov) -> {
            var rootModel = prov.generateFlatItem(ctx.get(), prov.modLoc("item/%s/1".formatted(prov.name(ctx))));
            for (int i = 0; i < modelNumber; i++) {
                var subModelBuilder = prov.getBuilder("item/" + prov.name(ctx::getEntry) + "/" + i)
                        .parent(new ModelFile.UncheckedModelFile("item/generated"));
                subModelBuilder.texture("layer0", prov.modLoc("item/%s/%d".formatted(prov.name(ctx), i + 1)));

                rootModel = rootModel.override().predicate(predicate, i / 100f)
                        .model(new ModelFile.UncheckedModelFile(prov.modLoc("item/%s/%d".formatted(prov.name(ctx), i))))
                        .end();
            }
        };
    }

    public static void createTextureModel(DataGenContext<Item, ? extends Item> ctx, RegistrateItemModelGenerator prov,
                                          ResourceLocation texture) {
        prov.generateFlatItem(ctx.get(), texture);
    }

    public static void rubberTreeSaplingModel(DataGenContext<Item, BlockItem> context,
                                              RegistrateItemModelGenerator provider) {
        provider.generateFlatItem(context.get(), provider.modLoc("block/" + provider.name(context)));
    }

    public static NonNullBiConsumer<DataGenContext<Block, LampBlock>, RegistrateBlockModelGenerator> lampModel(DyeColor color,
                                                                                                               boolean border) {
        return (ctx, prov) -> {
            final String borderPart = (border ? "" : "_borderless");
            ModelFile parentOn = prov.models().getExistingFile(prov.modLoc("block/lamp" + borderPart));
            ModelFile parentOff = prov.models().getExistingFile(prov.modLoc("block/lamp" + borderPart + "_off"));

            prov.getVariantBuilder(ctx.getEntry())
                    .forAllStates(state -> {
                        if (LampBlock.isLightActive(state)) {
                            ModelBuilder<?> model = prov.models()
                                    .getBuilder(ctx.getName() +
                                            (state.getValue(GTBlockStateProperties.BLOOM) ? "_bloom" : ""))
                                    .parent(parentOn);
                            if (border) {
                                model.texture("active", "block/lamps/" + color.getName());
                                if (state.getValue(GTBlockStateProperties.BLOOM)) {
                                    model.texture("active_overlay", "block/lamps/" + color.getName() + "_emissive");
                                } else {
                                    model.texture("active_overlay", "block/lamps/" + color.getName());
                                }
                            } else {
                                if (state.getValue(GTBlockStateProperties.BLOOM)) {
                                    model.texture("active",
                                            "block/lamps/" + color.getName() + "_borderless_emissive");
                                } else {
                                    model.texture("active",
                                            "block/lamps/" + color.getName() + "_borderless");
                                }
                            }
                            return ConfiguredModel.builder()
                                    .modelFile(model)
                                    .build();
                        } else {
                            return ConfiguredModel.builder()
                                    .modelFile(prov.models()
                                            .getBuilder(ctx.getName() + "_off")
                                            .parent(parentOff)
                                            .texture("inactive",
                                                    "block/lamps/" + color.getName() + "_off" + borderPart))
                                    .build();
                        }
                    });
        };
    }

    public static NonNullBiConsumer<DataGenContext<Block, Block>, RegistrateBlockModelGenerator> randomRotatedModel(ResourceLocation texturePath) {
        return (ctx, prov) -> {
            Block block = ctx.getEntry();
            ModelFile cubeAll = prov.models().cubeAll(ctx.getName(), texturePath);
            ModelFile cubeMirroredAll = prov.models().singleTexture(ctx.getName() + "_mirrored",
                    prov.mcLoc(ModelProvider.BLOCK_FOLDER + "/cube_mirrored_all"), "all", texturePath);
            ConfiguredModel[] models = ConfiguredModel.builder()
                    .modelFile(cubeAll)
                    .rotationY(0)
                    .nextModel()
                    .modelFile(cubeAll)
                    .rotationY(180)
                    .nextModel()
                    .modelFile(cubeMirroredAll)
                    .rotationY(0)
                    .nextModel()
                    .modelFile(cubeMirroredAll)
                    .rotationY(180)
                    .build();
            prov.simpleBlock(block, models);
        };
    }

    public static NonNullBiConsumer<DataGenContext<Block, Block>, RegistrateBlockModelGenerator> createSidedCasingModel(ResourceLocation texture) {
        return (ctx, prov) -> {
            prov.simpleBlock(ctx.getEntry(), prov.models().cubeBottomTop(ctx.getName(),
                    texture.withSuffix("/side"),
                    texture.withSuffix("/bottom"),
                    texture.withSuffix("/top")));
        };
    }

    public static NonNullBiConsumer<DataGenContext<Block, ? extends Block>, GTBlockstateProvider> cubeAllModel(ResourceLocation texture) {
        return (ctx, prov) -> {
            prov.simpleBlock(ctx.getEntry(), prov.models().cubeAll(ctx.getName(), texture));
        };
    }

    public static NonNullBiConsumer<DataGenContext<Block, Block>, RegistrateBlockModelGenerator> createMachineCasingModel(String tierName) {
        return (ctx, prov) -> {
            prov.simpleBlock(ctx.getEntry(),
                    prov.models()
                            .withExistingParent("%s_machine_casing".formatted(tierName),
                                    GTPM.id("block/cube/tinted/bottom_top"))
                            .texture("bottom", GTPM.id("block/casings/voltage/%s/bottom".formatted(tierName)))
                            .texture("top", GTPM.id("block/casings/voltage/%s/top".formatted(tierName)))
                            .texture("side", GTPM.id("block/casings/voltage/%s/side".formatted(tierName))));
        };
    }

    public static NonNullBiConsumer<DataGenContext<Block, Block>, RegistrateBlockModelGenerator> createHermeticCasingModel(String tierName) {
        return (ctx, prov) -> {
            prov.simpleBlock(ctx.getEntry(), prov.models()
                    .withExistingParent("%s_hermetic_casing".formatted(tierName), GTPM.id("block/hermetic_casing"))
                    .texture("bot_bottom", GTPM.id("block/casings/voltage/%s/bottom".formatted(tierName)))
                    .texture("bot_top", GTPM.id("block/casings/voltage/%s/top".formatted(tierName)))
                    .texture("bot_side", GTPM.id("block/casings/voltage/%s/side".formatted(tierName))));
        };
    }

    public static NonNullBiConsumer<DataGenContext<Block, Block>, RegistrateBlockModelGenerator> createSteamCasingModel(String material) {
        return (ctx, prov) -> {
            prov.simpleBlock(ctx.getEntry(), prov.models().cubeBottomTop(ctx.getName(),
                    GTPM.id("block/casings/steam/%s/side".formatted(material)),
                    GTPM.id("block/casings/steam/%s/bottom".formatted(material)),
                    GTPM.id("block/casings/steam/%s/top".formatted(material))));
        };
    }

    public static NonNullBiConsumer<DataGenContext<Block, CoilBlock>, RegistrateBlockModelGenerator> createCoilModel(ICoilType coilType) {
        return (ctx, prov) -> {
            String name = ctx.getName();
            ActiveBlock block = ctx.getEntry();
            ModelFile inactive = prov.models().cubeAll(name, coilType.getTexture());
            ModelFile active = prov.models().withExistingParent(name + "_active", GTPM.id("block/cube_2_layer/all"))
                    .texture("bot_all", coilType.getTexture())
                    .texture("top_all", coilType.getTexture().withSuffix("_bloom"));
            prov.getVariantBuilder(block)
                    .partialState().with(GTBlockStateProperties.ACTIVE, false).modelForState().modelFile(inactive)
                    .addModel()
                    .partialState().with(GTBlockStateProperties.ACTIVE, true).modelForState().modelFile(active)
                    .addModel();
        };
    }

    public static NonNullBiConsumer<DataGenContext<Block, BatteryBlock>, RegistrateBlockModelGenerator> createBatteryBlockModel(IBatteryData batteryData) {
        return (ctx, prov) -> {
            prov.simpleBlock(ctx.getEntry(), prov.models().cubeBottomTop(ctx.getName(),
                    GTPM.id("block/casings/battery/" + batteryData.getBatteryName() + "/side"),
                    GTPM.id("block/casings/battery/" + batteryData.getBatteryName() + "/top"),
                    GTPM.id("block/casings/battery/" + batteryData.getBatteryName() + "/top")));
        };
    }

    public static NonNullBiConsumer<DataGenContext<Block, FusionCasingBlock>, RegistrateBlockModelGenerator> createFusionCasingModel(IFusionCasingType casingType) {
        return (ctx, prov) -> {
            String name = ctx.getName();
            ActiveBlock block = ctx.getEntry();
            ModelFile inactive = prov.models().cubeAll(name, casingType.getTexture());
            ModelFile active = prov.models().withExistingParent(name + "_active", GTPM.id("block/cube_2_layer/all"))
                    .texture("bot_all", casingType.getTexture())
                    .texture("top_all", casingType.getTexture().withSuffix("_bloom"));
            prov.getVariantBuilder(block)
                    .partialState().with(GTBlockStateProperties.ACTIVE, false).modelForState().modelFile(inactive)
                    .addModel()
                    .partialState().with(GTBlockStateProperties.ACTIVE, true).modelForState().modelFile(active)
                    .addModel();
        };
    }

    public static NonNullBiConsumer<DataGenContext<Block, Block>, RegistrateBlockModelGenerator> createCleanroomFilterModel(IFilterType type) {
        return (ctx, prov) -> {
            prov.simpleBlock(ctx.getEntry(), prov.models()
                    .cubeAll(ctx.getName(), GTPM.id("block/casings/cleanroom/" + type.getSerializedName())));
        };
    }

    public static NonNullBiConsumer<DataGenContext<Block, ActiveBlock>, RegistrateBlockModelGenerator> createActiveModel(ResourceLocation modelPath) {
        return (ctx, prov) -> {
            ActiveBlock block = ctx.getEntry();
            ModelFile inactive = prov.models().getExistingFile(modelPath);
            ModelFile active = prov.models().getExistingFile(modelPath.withSuffix("_active"));
            prov.getVariantBuilder(block)
                    .partialState().with(GTBlockStateProperties.ACTIVE, false).modelForState().modelFile(inactive)
                    .addModel()
                    .partialState().with(GTBlockStateProperties.ACTIVE, true).modelForState().modelFile(active)
                    .addModel();
        };
    }

    public static NonNullBiConsumer<DataGenContext<Block, ActiveBlock>, RegistrateBlockModelGenerator> createFireboxModel(BoilerFireboxType type) {
        return (ctx, prov) -> {
            String name = ctx.getName();
            ActiveBlock block = ctx.getEntry();
            ModelFile inactive = prov.models().cubeBottomTop(name, type.side(), type.bottom(), type.top());
            ModelFile active = prov.models().withExistingParent(name + "_active", GTPM.id("block/fire_box_active"))
                    .texture("side", type.side())
                    .texture("bottom", type.bottom())
                    .texture("top", type.top());
            prov.getVariantBuilder(block)
                    .partialState().with(GTBlockStateProperties.ACTIVE, false)
                    .modelForState().modelFile(inactive).addModel()
                    .partialState().with(GTBlockStateProperties.ACTIVE, true)
                    .modelForState().modelFile(active).addModel();
        };
    }

    public static void createPipeBlockModel(DataGenContext<Block, ? extends PipeBlock<?, ?, ?>> ctx,
                                            GTBlockstateProvider prov) {
        // the pipe model generator handles adding its models to the provider by itself
        ctx.getEntry().createPipeModel(prov).initModels();
    }

    // endregion

    // region RUNTIME GEN

    /**
     * register fluid models for materials
     */
    public static void registerMaterialFluidModels() {
        for (var material : GTRegistries.MATERIALS) {
            var fluidProperty = material.getProperty(PropertyKey.FLUID);
            if (fluidProperty == null) continue;

            for (FluidStorageKey key : FluidStorageKey.allKeys()) {
                FluidStorage storage = fluidProperty.getStorage();
                // fluid block models.
                FluidStorage.FluidEntry fluidEntry = storage.getEntry(key);
                if (fluidEntry != null && fluidEntry.getBuilder() != null) {
                    MixinHelpers.addFluidTexture(material, fluidEntry);
                }

                // bucket models.
                Fluid fluid = storage.get(key);
                if (fluid instanceof GTFluid gtFluid) {
                    // read the base bucket model JSON
                    JsonObject original;
                    try (BufferedReader reader = Minecraft.getInstance().getResourceManager()
                            .openAsReader(GTPM.id("models/item/bucket/bucket.json"))) {
                        original = GsonHelper.parse(reader, true);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                    JsonObject newJson = original.deepCopy();
                    newJson.addProperty("fluid", BuiltInRegistries.FLUID.getKey(gtFluid).toString());
                    if (gtFluid.getFluidType().isLighterThanAir()) {
                        newJson.addProperty("flip_gas", true);
                    }
                    if (gtFluid.getFluidType().getLightLevel() > 0) {
                        newJson.addProperty("apply_fluid_luminosity", true);
                    }

                    GTDynamicResourcePack.addItemModel(BuiltInRegistries.ITEM.getKey(gtFluid.getBucket()), newJson);
                }
            }
        }
    }

    // endregion
}
