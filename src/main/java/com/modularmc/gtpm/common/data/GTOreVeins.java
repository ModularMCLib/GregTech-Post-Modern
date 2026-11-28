package com.modularmc.gtpm.common.data;

import com.modularmc.gtpm.api.data.worldgen.*;
import com.modularmc.gtpm.api.data.worldgen.generator.indicators.SurfaceIndicatorGenerator;
import com.modularmc.gtpm.api.data.worldgen.generator.veins.NoopVeinGenerator;

import com.modularmc.registrate.localization.MinecraftLocale;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;

import lombok.Getter;

import java.util.*;
import java.util.function.Consumer;

import static com.modularmc.gtpm.api.data.worldgen.generator.veins.DikeVeinGenerator.DikeBlockDefinition;
import static com.modularmc.gtpm.api.data.worldgen.generator.veins.VeinedVeinGenerator.VeinBlockDefinition;
import static com.modularmc.gtpm.common.data.GTMaterials.*;
import static com.modularmc.gtpm.common.registry.GTRegistration.REGISTRATE;

@SuppressWarnings("unused")
public class GTOreVeins {

    /**
     * The size of the largest registered vein.
     * This becomes available after all veins have been loaded.
     */
    @Getter
    private static int largestVeinSize = 0;

    @Getter
    private static int largestIndicatorOffset = 0;

    //////////////////////////////////////
    // ******** End Vein *********//
    /// ///////////////////////////////////

    public static final ResourceKey<GTOreDefinition> END_BAUXITE_VEIN = REGISTRATE.oreVein("end_bauxite")
            .localization(text -> text.source("End Bauxite").translation(MinecraftLocale.ZH_CN, "末地铝土"))
            .register();
    public static final ResourceKey<GTOreDefinition> END_MAGNETITE_VEIN = REGISTRATE.oreVein("end_magnetite")
            .localization(text -> text.source("End Magnetite").translation(MinecraftLocale.ZH_CN, "末地磁铁"))
            .register();
    public static final ResourceKey<GTOreDefinition> NAQUADAH_VEIN = REGISTRATE.oreVein("naquadah")
            .localization(text -> text.source("Naquadah").translation(MinecraftLocale.ZH_CN, "硅岩"))
            .register();
    public static final ResourceKey<GTOreDefinition> PITCHBLENDE_VEIN = REGISTRATE.oreVein("pitchblende")
            .localization(text -> text.source("Pitchblende").translation(MinecraftLocale.ZH_CN, "沥青铀矿"))
            .register();
    public static final ResourceKey<GTOreDefinition> SCHEELITE_VEIN = REGISTRATE.oreVein("scheelite")
            .localization(text -> text.source("Scheelite").translation(MinecraftLocale.ZH_CN, "白钨"))
            .register();
    public static final ResourceKey<GTOreDefinition> SHELDONITE_VEIN = REGISTRATE.oreVein("sheldonite")
            .localization(text -> text.source("Sheldonite").translation(MinecraftLocale.ZH_CN, "谢尔顿"))
            .register();

    //////////////////////////////////////
    // ****** Nether Vein *******//
    /// ///////////////////////////////////

    public static final ResourceKey<GTOreDefinition> BANDED_IRON_VEIN = REGISTRATE.oreVein("banded_iron")
            .localization(text -> text.source("Banded Iron").translation(MinecraftLocale.ZH_CN, "带状铁矿"))
            .register();
    public static final ResourceKey<GTOreDefinition> BERYLLIUM_VEIN = REGISTRATE.oreVein("beryllium")
            .localization(text -> text.source("Beryllium").translation(MinecraftLocale.ZH_CN, "铍"))
            .register();
    public static final ResourceKey<GTOreDefinition> CERTUS_QUARTZ_VEIN = REGISTRATE.oreVein("certus_quartz")
            .localization(text -> text.source("Certus Quartz").translation(MinecraftLocale.ZH_CN, "赛特斯石英"))
            .register();
    public static final ResourceKey<GTOreDefinition> NETHER_MANGANESE_VEIN = REGISTRATE.oreVein("nether_manganese")
            .localization(text -> text.source("Nether Manganese").translation(MinecraftLocale.ZH_CN, "下界锰"))
            .register();
    public static final ResourceKey<GTOreDefinition> MOLYBDENUM_VEIN = REGISTRATE.oreVein("molybdenum")
            .localization(text -> text.source("Molybdenum").translation(MinecraftLocale.ZH_CN, "钼"))
            .register();
    public static final ResourceKey<GTOreDefinition> MONAZITE_VEIN = REGISTRATE.oreVein("monazite")
            .localization(text -> text.source("Monazite").translation(MinecraftLocale.ZH_CN, "独居石"))
            .register();
    public static final ResourceKey<GTOreDefinition> NETHER_QUARTZ_VEIN = REGISTRATE.oreVein("nether_quartz")
            .localization(text -> text.source("Nether Quartz").translation(MinecraftLocale.ZH_CN, "下界石英"))
            .register();
    public static final ResourceKey<GTOreDefinition> METHER_REDSTONE_VEIN = REGISTRATE.oreVein("nether_redstone")
            .localization(text -> text.source("Nether Redstone").translation(MinecraftLocale.ZH_CN, "下界红石"))
            .register();
    public static final ResourceKey<GTOreDefinition> SALTPETER_VEIN = REGISTRATE.oreVein("saltpeter")
            .localization(text -> text.source("Saltpeter").translation(MinecraftLocale.ZH_CN, "硝石"))
            .register();
    public static final ResourceKey<GTOreDefinition> SULFUR_VEIN = REGISTRATE.oreVein("sulfur")
            .localization(text -> text.source("Sulfur").translation(MinecraftLocale.ZH_CN, "硫"))
            .register();
    public static final ResourceKey<GTOreDefinition> TETRAHEDRITE_VEIN = REGISTRATE.oreVein("tetrahedrite")
            .localization(text -> text.source("Tetrahedrite").translation(MinecraftLocale.ZH_CN, "黝铜"))
            .register();
    public static final ResourceKey<GTOreDefinition> TOPAZ_VEIN = REGISTRATE.oreVein("topaz")
            .localization(text -> text.source("Topaz").translation(MinecraftLocale.ZH_CN, "黄玉"))
            .register();

    //////////////////////////////////////
    // ***** Stone *****//
    /// ///////////////////////////////////

    public static final ResourceKey<GTOreDefinition> APATITE_VEIN = REGISTRATE.oreVein("apatite")
            .localization(text -> text.source("Apatite").translation(MinecraftLocale.ZH_CN, "磷灰石"))
            .register();
    public static final ResourceKey<GTOreDefinition> CASSITERITE_VEIN = REGISTRATE.oreVein("cassiterite")
            .localization(text -> text.source("Cassiterite").translation(MinecraftLocale.ZH_CN, "锡石"))
            .register();
    public static final ResourceKey<GTOreDefinition> COAL_VEIN = REGISTRATE.oreVein("coal")
            .localization(text -> text.source("Coal").translation(MinecraftLocale.ZH_CN, "煤炭"))
            .register();
    public static final ResourceKey<GTOreDefinition> COPPER_TIN_VEIN = REGISTRATE.oreVein("copper_tin")
            .localization(text -> text.source("Copper Tin").translation(MinecraftLocale.ZH_CN, "铜锡"))
            .register();
    public static final ResourceKey<GTOreDefinition> GALENA_VEIN = REGISTRATE.oreVein("galena")
            .localization(text -> text.source("Galena").translation(MinecraftLocale.ZH_CN, "方铅"))
            .register();
    public static final ResourceKey<GTOreDefinition> GARNET_TIN_VEIN = REGISTRATE.oreVein("garnet_tin")
            .localization(text -> text.source("Garnet Tin").translation(MinecraftLocale.ZH_CN, "锡石榴石"))
            .register();
    public static final ResourceKey<GTOreDefinition> GARNET_VEIN = REGISTRATE.oreVein("garnet")
            .localization(text -> text.source("Garnet").translation(MinecraftLocale.ZH_CN, "石榴石"))
            .register();
    public static final ResourceKey<GTOreDefinition> IRON_VEIN = REGISTRATE.oreVein("iron")
            .localization(text -> text.source("Iron").translation(MinecraftLocale.ZH_CN, "铁"))
            .register();
    public static final ResourceKey<GTOreDefinition> LUBRICANT_VEIN = REGISTRATE.oreVein("lubricant")
            .localization(text -> text.source("Lubricant").translation(MinecraftLocale.ZH_CN, "皂滑"))
            .register();
    public static final ResourceKey<GTOreDefinition> MAGNETITE_VEIN = REGISTRATE.oreVein("magnetite")
            .localization(text -> text.source("Magnetite").translation(MinecraftLocale.ZH_CN, "磁铁"))
            .register();
    public static final ResourceKey<GTOreDefinition> MINERAL_SAND_VEIN = REGISTRATE.oreVein("mineral_sand")
            .localization(text -> text.source("Mineral Sand").translation(MinecraftLocale.ZH_CN, "矿砂"))
            .register();
    public static final ResourceKey<GTOreDefinition> NICKEL_VEIN = REGISTRATE.oreVein("nickel")
            .localization(text -> text.source("Nickel").translation(MinecraftLocale.ZH_CN, "镍"))
            .register();
    public static final ResourceKey<GTOreDefinition> SALTS_VEIN = REGISTRATE.oreVein("salts")
            .localization(text -> text.source("Salts").translation(MinecraftLocale.ZH_CN, "盐"))
            .register();
    public static final ResourceKey<GTOreDefinition> OILSANDS_VEIN = REGISTRATE.oreVein("oilsands")
            .localization(text -> text.source("Oilsands").translation(MinecraftLocale.ZH_CN, "油砂"))
            .register();

    //////////////////////////////////////
    // ***** Deepslate *****//
    /// ///////////////////////////////////

    public static final ResourceKey<GTOreDefinition> COPPER_VEIN = REGISTRATE.oreVein("copper")
            .localization(text -> text.source("Copper").translation(MinecraftLocale.ZH_CN, "铜"))
            .register();
    public static final ResourceKey<GTOreDefinition> DIAMOND_VEIN = REGISTRATE.oreVein("diamond")
            .localization(text -> text.source("Diamond").translation(MinecraftLocale.ZH_CN, "钻石"))
            .register();
    public static final ResourceKey<GTOreDefinition> LAPIS_VEIN = REGISTRATE.oreVein("lapis")
            .localization(text -> text.source("Lapis").translation(MinecraftLocale.ZH_CN, "青金石"))
            .register();
    public static final ResourceKey<GTOreDefinition> MANGANESE_VEIN = REGISTRATE.oreVein("manganese")
            .localization(text -> text.source("Manganese").translation(MinecraftLocale.ZH_CN, "锰"))
            .register();
    public static final ResourceKey<GTOreDefinition> MICA_VEIN = REGISTRATE.oreVein("mica")
            .localization(text -> text.source("Mica").translation(MinecraftLocale.ZH_CN, "云母"))
            .register();
    public static final ResourceKey<GTOreDefinition> OLIVINE_VEIN = REGISTRATE.oreVein("olivine")
            .localization(text -> text.source("Olivine").translation(MinecraftLocale.ZH_CN, "橄榄石"))
            .register();
    public static final ResourceKey<GTOreDefinition> REDSTONE_VEIN = REGISTRATE.oreVein("redstone")
            .localization(text -> text.source("Redstone").translation(MinecraftLocale.ZH_CN, "红石"))
            .register();
    public static final ResourceKey<GTOreDefinition> SAPPHIRE_VEIN = REGISTRATE.oreVein("sapphire")
            .localization(text -> text.source("Sapphire").translation(MinecraftLocale.ZH_CN, "蓝宝石"))
            .register();

    public static void init() {}

    public static void updateLargestVeinSize(Registry<GTOreDefinition> registry) {
        // map to average of min & max values.
        GTOreVeins.largestVeinSize = registry.stream()
                .map(GTOreDefinition::clusterSize)
                .mapToInt(intProvider -> (intProvider.getMinValue() + intProvider.getMaxValue()) / 2)
                .max()
                .orElse(0);

        GTOreVeins.largestIndicatorOffset = registry.stream()
                .flatMapToInt(definition -> definition.indicatorGenerators().stream()
                        .mapToInt(indicatorGenerator -> indicatorGenerator.getSearchRadiusModifier(
                                (int) Math.ceil(definition.clusterSize().getMinValue() / 2.0))))
                .max()
                .orElse(0);
    }

    public static GTOreDefinition blankOreDefinition(HolderGetter<Biome> biomeLookup) {
        return new GTOreDefinition(
                ConstantInt.ZERO, 0, 0, IWorldGenLayer.NOWHERE, Set.of(),
                HeightRangePlacement.uniform(VerticalAnchor.absolute(0), VerticalAnchor.absolute(0)),
                0, HolderSet.empty(), BiomeWeightModifier.EMPTY, NoopVeinGenerator.INSTANCE,
                new ArrayList<>(), biomeLookup);
    }

    private static void register(BootstrapContext<GTOreDefinition> context,
                                 ResourceKey<GTOreDefinition> key,
                                 Consumer<GTOreDefinition> consumer) {
        GTOreDefinition builder = blankOreDefinition(context.lookup(Registries.BIOME));
        consumer.accept(builder);
        context.register(key, builder);
    }

    public static void bootstrap(BootstrapContext<GTOreDefinition> context) {
        // END
        RuleTest[] endRules = new RuleTest[] { WorldGeneratorUtils.END_ORE_REPLACEABLES };

        register(context, END_BAUXITE_VEIN, vein -> vein
                .clusterSize(UniformInt.of(32, 40)).density(0.3f).weight(40)
                .layer(WorldGenLayers.ENDSTONE)
                .heightRangeUniform(10, 80)
                .biomes(BiomeTags.IS_END)
                .layeredVeinGenerator(generator -> generator
                        .withLayerPattern(() -> GTLayerPattern.builder(endRules)
                                .layer(l -> l.weight(2).state(Blocks.END_STONE::defaultBlockState).size(1, 6))
                                .layer(l -> l.weight(2).mat(Bauxite).size(1, 4))
                                .layer(l -> l.weight(1).mat(Ilmenite).size(1, 2))
                                .layer(l -> l.weight(1).mat(Aluminium).size(1, 1))
                                .build()))
                .surfaceIndicatorGenerator(indicator -> indicator
                        .surfaceRock(Bauxite)
                        .placement(SurfaceIndicatorGenerator.IndicatorPlacement.ABOVE)));
        register(context, END_MAGNETITE_VEIN, vein -> vein
                .clusterSize(UniformInt.of(38, 44)).density(0.15f).weight(30)
                .layer(WorldGenLayers.ENDSTONE)
                .heightRangeUniform(20, 80)
                .biomes(BiomeTags.IS_END)
                .layeredVeinGenerator(generator -> generator
                        .withLayerPattern(() -> GTLayerPattern.builder(endRules)
                                .layer(l -> l.weight(3).mat(Magnetite).size(1, 4))
                                .layer(l -> l.weight(2).mat(VanadiumMagnetite).size(1, 2))
                                .layer(l -> l.weight(2).mat(Chromite).size(1, 1))
                                .layer(l -> l.weight(1).mat(Gold).size(1, 1))
                                .build()))
                .surfaceIndicatorGenerator(indicator -> indicator
                        .surfaceRock(Magnetite)
                        .placement(SurfaceIndicatorGenerator.IndicatorPlacement.ABOVE)));
        register(context, NAQUADAH_VEIN, vein -> vein
                .clusterSize(UniformInt.of(48, 80)).density(0.25f).weight(30)
                .layer(WorldGenLayers.ENDSTONE)
                .heightRangeUniform(10, 90)
                .biomes(BiomeTags.IS_END)
                .cuboidVeinGenerator(generator -> generator
                        .top(b -> b.mat(Naquadah).size(2))
                        .middle(b -> b.mat(Naquadah).size(3))
                        .bottom(b -> b.mat(Naquadah).size(2))
                        .spread(b -> b.mat(Plutonium239)))
                .surfaceIndicatorGenerator(indicator -> indicator
                        .surfaceRock(Naquadah)
                        .placement(SurfaceIndicatorGenerator.IndicatorPlacement.ABOVE)));
        register(context, PITCHBLENDE_VEIN, vein -> vein
                .clusterSize(UniformInt.of(32, 64)).density(0.25f).weight(30)
                .layer(WorldGenLayers.ENDSTONE)
                .heightRangeUniform(30, 60)
                .biomes(BiomeTags.IS_END)
                .cuboidVeinGenerator(generator -> generator
                        .top(b -> b.mat(Pitchblende).size(2))
                        .middle(b -> b.mat(Pitchblende).size(3))
                        .bottom(b -> b.mat(Pitchblende).size(2))
                        .spread(b -> b.mat(Uraninite)))
                .surfaceIndicatorGenerator(indicator -> indicator
                        .surfaceRock(Pitchblende)
                        .placement(SurfaceIndicatorGenerator.IndicatorPlacement.ABOVE)));
        register(context, SCHEELITE_VEIN, vein -> vein
                .clusterSize(UniformInt.of(50, 64)).density(0.7f).weight(20)
                .layer(WorldGenLayers.ENDSTONE)
                .heightRangeUniform(20, 60)
                .biomes(BiomeTags.IS_END)
                .dikeVeinGenerator(generator -> generator
                        .withBlock(new DikeBlockDefinition(Scheelite, 3, 20, 60))
                        .withBlock(new DikeBlockDefinition(Tungstate, 2, 35, 55))
                        .withBlock(new DikeBlockDefinition(Lithium, 1, 20, 40)))
                .surfaceIndicatorGenerator(indicator -> indicator
                        .surfaceRock(Scheelite)
                        .placement(SurfaceIndicatorGenerator.IndicatorPlacement.ABOVE)));
        register(context, SHELDONITE_VEIN, vein -> vein
                .clusterSize(UniformInt.of(25, 29)).density(0.2f).weight(10)
                .layer(WorldGenLayers.ENDSTONE)
                .heightRangeUniform(5, 50)
                .biomes(BiomeTags.IS_END)
                .layeredVeinGenerator(generator -> generator
                        .withLayerPattern(() -> GTLayerPattern.builder(endRules)
                                .layer(l -> l.weight(3).mat(Bornite).size(2, 4))
                                .layer(l -> l.weight(2).mat(Cooperite).size(1, 1))
                                .layer(l -> l.weight(2).mat(Platinum).size(1, 1))
                                .layer(l -> l.weight(1).mat(Palladium).size(1, 1))
                                .build()))
                .surfaceIndicatorGenerator(indicator -> indicator
                        .surfaceRock(Platinum)
                        .placement(SurfaceIndicatorGenerator.IndicatorPlacement.ABOVE)));

        // NETHER
        RuleTest[] netherRules = new RuleTest[] { new TagMatchTest(BlockTags.NETHER_CARVER_REPLACEABLES) };

        register(context, BANDED_IRON_VEIN, vein -> vein
                .clusterSize(UniformInt.of(40, 52)).density(1.0f).weight(30)
                .layer(WorldGenLayers.NETHERRACK)
                .heightRangeUniform(20, 40)
                .biomes(BiomeTags.IS_NETHER)
                .veinedVeinGenerator(generator -> generator
                        .oreBlock(new VeinBlockDefinition(Goethite, 3))
                        .oreBlock(new VeinBlockDefinition(YellowLimonite, 2))
                        .oreBlock(new VeinBlockDefinition(Hematite, 2))
                        .rareBlock(new VeinBlockDefinition(Gold, 1))
                        .rareBlockChance(0.075f)
                        .veininessThreshold(0.01f)
                        .maxRichnessThreshold(0.175f)
                        .minRichness(0.7f)
                        .maxRichness(1.0f)
                        .edgeRoundoffBegin(3)
                        .maxEdgeRoundoff(0.1f))
                .surfaceIndicatorGenerator(indicator -> indicator
                        .surfaceRock(Goethite)
                        .placement(SurfaceIndicatorGenerator.IndicatorPlacement.ABOVE)));
        register(context, BERYLLIUM_VEIN, vein -> vein
                .clusterSize(UniformInt.of(50, 64)).density(0.75f).weight(30)
                .layer(WorldGenLayers.NETHERRACK)
                .heightRangeUniform(5, 30)
                .biomes(BiomeTags.IS_NETHER)
                .dikeVeinGenerator(generator -> generator
                        .withBlock(new DikeBlockDefinition(Beryllium, 3, 5, 30))
                        .withBlock(new DikeBlockDefinition(Emerald, 2, 5, 19))
                        .withBlock(new DikeBlockDefinition(Thorium, 2, 16, 30)))
                .surfaceIndicatorGenerator(indicator -> indicator
                        .surfaceRock(Beryllium)
                        .placement(SurfaceIndicatorGenerator.IndicatorPlacement.ABOVE)));
        register(context, CERTUS_QUARTZ_VEIN, vein -> vein
                .clusterSize(UniformInt.of(25, 29)).density(0.25f).weight(40)
                .layer(WorldGenLayers.NETHERRACK)
                .heightRangeUniform(80, 120)
                .biomes(BiomeTags.IS_NETHER)
                .layeredVeinGenerator(generator -> generator
                        .withLayerPattern(() -> GTLayerPattern.builder(netherRules)
                                .layer(l -> l.weight(3).mat(Quartzite).size(2, 4))
                                .layer(l -> l.weight(2).mat(CertusQuartz).size(1, 1))
                                .layer(l -> l.weight(1).mat(Barite).size(1, 1))
                                .build()))
                .surfaceIndicatorGenerator(indicator -> indicator
                        .surfaceRock(CertusQuartz)
                        .placement(SurfaceIndicatorGenerator.IndicatorPlacement.BELOW)));
        register(context, NETHER_MANGANESE_VEIN, vein -> vein
                .clusterSize(UniformInt.of(50, 64)).density(0.75f).weight(20)
                .layer(WorldGenLayers.NETHERRACK)
                .heightRangeUniform(20, 30)
                .biomes(BiomeTags.IS_NETHER)
                .dikeVeinGenerator(generator -> generator
                        .withBlock(new DikeBlockDefinition(Grossular, 3, 20, 30))
                        .withBlock(new DikeBlockDefinition(Pyrolusite, 2, 20, 26))
                        .withBlock(new DikeBlockDefinition(Tantalite, 1, 24, 30)))
                .surfaceIndicatorGenerator(indicator -> indicator
                        .surfaceRock(Grossular)
                        .placement(SurfaceIndicatorGenerator.IndicatorPlacement.ABOVE)));
        register(context, MOLYBDENUM_VEIN, vein -> vein
                .clusterSize(UniformInt.of(25, 29)).density(0.25f).weight(5)
                .layer(WorldGenLayers.NETHERRACK)
                .heightRangeUniform(20, 50)
                .biomes(BiomeTags.IS_NETHER)
                .layeredVeinGenerator(generator -> generator
                        .withLayerPattern(() -> GTLayerPattern.builder(netherRules)
                                .layer(l -> l.weight(3).mat(Wulfenite).size(2, 4))
                                .layer(l -> l.weight(2).mat(Molybdenite).size(1, 1))
                                .layer(l -> l.weight(1).mat(Molybdenum).size(1, 1))
                                .layer(l -> l.weight(1).mat(Powellite).size(1, 1))
                                .build()))
                .surfaceIndicatorGenerator(indicator -> indicator
                        .surfaceRock(Molybdenum)
                        .placement(SurfaceIndicatorGenerator.IndicatorPlacement.ABOVE)));
        register(context, MONAZITE_VEIN, vein -> vein
                .clusterSize(UniformInt.of(25, 29)).density(0.25f).weight(30)
                .layer(WorldGenLayers.NETHERRACK)
                .heightRangeUniform(20, 40)
                .biomes(BiomeTags.IS_NETHER)
                .layeredVeinGenerator(generator -> generator
                        .withLayerPattern(() -> GTLayerPattern.builder(netherRules)
                                .layer(l -> l.weight(3).mat(Bastnasite).size(2, 4))
                                .layer(l -> l.weight(1).mat(Monazite).size(1, 1))
                                .layer(l -> l.weight(1).mat(Neodymium).size(1, 1))
                                .build()))
                .surfaceIndicatorGenerator(indicator -> indicator
                        .surfaceRock(Bastnasite)
                        .placement(SurfaceIndicatorGenerator.IndicatorPlacement.ABOVE)));
        register(context, NETHER_QUARTZ_VEIN, vein -> vein
                .clusterSize(UniformInt.of(32, 40)).density(0.2f).weight(80)
                .layer(WorldGenLayers.NETHERRACK)
                .heightRangeUniform(40, 80)
                .biomes(BiomeTags.IS_NETHER)
                .layeredVeinGenerator(generator -> generator
                        .withLayerPattern(() -> GTLayerPattern.builder(netherRules)
                                .layer(l -> l.weight(3).mat(NetherQuartz).size(2, 4))
                                .layer(l -> l.weight(1).mat(Quartzite).size(1, 1))
                                .build()))
                .surfaceIndicatorGenerator(indicator -> indicator
                        .surfaceRock(NetherQuartz)
                        .placement(SurfaceIndicatorGenerator.IndicatorPlacement.ABOVE)));
        register(context, METHER_REDSTONE_VEIN, vein -> vein
                .clusterSize(UniformInt.of(32, 40)).density(0.2f).weight(60)
                .layer(WorldGenLayers.NETHERRACK)
                .heightRangeUniform(5, 40)
                .biomes(BiomeTags.IS_NETHER)
                .layeredVeinGenerator(generator -> generator
                        .withLayerPattern(() -> GTLayerPattern.builder(netherRules)
                                .layer(l -> l.weight(3).mat(Redstone).size(2, 4))
                                .layer(l -> l.weight(2).mat(Ruby).size(1, 1))
                                .layer(l -> l.weight(1).mat(Cinnabar).size(1, 1))
                                .build()))
                .surfaceIndicatorGenerator(indicator -> indicator
                        .surfaceRock(Redstone)
                        .placement(SurfaceIndicatorGenerator.IndicatorPlacement.ABOVE)));
        register(context, SALTPETER_VEIN, vein -> vein
                .clusterSize(UniformInt.of(32, 40)).density(0.25f).weight(40)
                .layer(WorldGenLayers.NETHERRACK)
                .heightRangeUniform(5, 45)
                .biomes(BiomeTags.IS_NETHER)
                .layeredVeinGenerator(generator -> generator
                        .withLayerPattern(() -> GTLayerPattern.builder(netherRules)
                                .layer(l -> l.weight(3).mat(Saltpeter).size(2, 4))
                                .layer(l -> l.weight(2).mat(Diatomite).size(1, 1))
                                .layer(l -> l.weight(2).mat(Electrotine).size(1, 1))
                                .layer(l -> l.weight(1).mat(Alunite).size(1, 1))
                                .build()))
                .surfaceIndicatorGenerator(indicator -> indicator
                        .surfaceRock(Saltpeter)
                        .placement(SurfaceIndicatorGenerator.IndicatorPlacement.ABOVE)));
        register(context, SULFUR_VEIN, vein -> vein
                .clusterSize(UniformInt.of(32, 40)).density(0.2f).weight(100)
                .layer(WorldGenLayers.NETHERRACK)
                .heightRangeUniform(10, 30)
                .biomes(BiomeTags.IS_NETHER)
                .layeredVeinGenerator(generator -> generator
                        .withLayerPattern(() -> GTLayerPattern.builder(netherRules)
                                .layer(l -> l.weight(3).mat(Sulfur).size(2, 4))
                                .layer(l -> l.weight(2).mat(Pyrite).size(1, 1))
                                .layer(l -> l.weight(1).mat(Sphalerite).size(1, 1))
                                .build()))
                .surfaceIndicatorGenerator(indicator -> indicator
                        .surfaceRock(Sulfur)
                        .placement(SurfaceIndicatorGenerator.IndicatorPlacement.ABOVE)));
        register(context, TETRAHEDRITE_VEIN, vein -> vein
                .clusterSize(UniformInt.of(40, 52)).density(1.0f).weight(70)
                .layer(WorldGenLayers.NETHERRACK)
                .heightRangeUniform(80, 120)
                .biomes(BiomeTags.IS_NETHER)
                .veinedVeinGenerator(generator -> generator
                        .oreBlock(new VeinBlockDefinition(Tetrahedrite, 4))
                        .oreBlock(new VeinBlockDefinition(Copper, 2))
                        .rareBlock(new VeinBlockDefinition(Stibnite, 1))
                        .rareBlockChance(0.15f)
                        .veininessThreshold(0.01f)
                        .maxRichnessThreshold(0.175f)
                        .minRichness(0.7f)
                        .maxRichness(1.0f)
                        .edgeRoundoffBegin(3)
                        .maxEdgeRoundoff(0.1f))
                .surfaceIndicatorGenerator(indicator -> indicator
                        .surfaceRock(Tetrahedrite)
                        .placement(SurfaceIndicatorGenerator.IndicatorPlacement.BELOW)));
        register(context, TOPAZ_VEIN, vein -> vein
                .clusterSize(UniformInt.of(25, 29)).density(0.25f).weight(70)
                .layer(WorldGenLayers.NETHERRACK)
                .heightRangeUniform(80, 120)
                .biomes(BiomeTags.IS_NETHER)
                .layeredVeinGenerator(generator -> generator
                        .withLayerPattern(() -> GTLayerPattern.builder(netherRules)
                                .layer(l -> l.weight(3).mat(BlueTopaz).size(2, 4))
                                .layer(l -> l.weight(2).mat(Topaz).size(1, 1))
                                .layer(l -> l.weight(2).mat(Chalcocite).size(1, 1))
                                .layer(l -> l.weight(1).mat(Bornite).size(1, 1))
                                .build()))
                .surfaceIndicatorGenerator(indicator -> indicator
                        .surfaceRock(Topaz)
                        .placement(SurfaceIndicatorGenerator.IndicatorPlacement.BELOW)));

        // STONE
        RuleTest[] stoneRules = new RuleTest[] { new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES) };

        register(context, APATITE_VEIN, vein -> vein
                .clusterSize(UniformInt.of(32, 40)).density(0.25f).weight(40)
                .layer(WorldGenLayers.STONE)
                .heightRangeUniform(10, 80)
                .biomes(BiomeTags.IS_OVERWORLD)
                .layeredVeinGenerator(generator -> generator
                        .withLayerPattern(() -> GTLayerPattern.builder(stoneRules)
                                .layer(l -> l.weight(3).mat(Apatite).size(2, 4))
                                .layer(l -> l.weight(2).mat(TricalciumPhosphate).size(1, 1))
                                .layer(l -> l.weight(1).mat(Pyrochlore).size(1, 1))
                                .build()))
                .surfaceIndicatorGenerator(indicator -> indicator
                        .surfaceRock(Apatite)
                        .placement(SurfaceIndicatorGenerator.IndicatorPlacement.ABOVE)));
        register(context, CASSITERITE_VEIN, vein -> vein
                .clusterSize(UniformInt.of(40, 52)).density(1.0f).weight(80)
                .layer(WorldGenLayers.STONE)
                .heightRangeUniform(10, 80)
                .biomes(BiomeTags.IS_OVERWORLD)
                .veinedVeinGenerator(generator -> generator
                        .oreBlock(new VeinBlockDefinition(Tin, 4))
                        .rareBlock(new VeinBlockDefinition(Cassiterite, 2))
                        .rareBlockChance(0.33f)
                        .veininessThreshold(0.01f)
                        .maxRichnessThreshold(0.175f)
                        .minRichness(0.7f)
                        .maxRichness(1.0f)
                        .edgeRoundoffBegin(3)
                        .maxEdgeRoundoff(0.1f))
                .surfaceIndicatorGenerator(indicator -> indicator
                        .surfaceRock(Cassiterite)));
        register(context, COAL_VEIN, vein -> vein
                .clusterSize(UniformInt.of(38, 44)).density(0.25f).weight(80)
                .layer(WorldGenLayers.STONE)
                .heightRangeUniform(10, 140)
                .biomes(BiomeTags.IS_OVERWORLD)
                .layeredVeinGenerator(generator -> generator
                        .withLayerPattern(() -> GTLayerPattern.builder(stoneRules)
                                .layer(l -> l.weight(3).mat(Coal).size(2, 4))
                                .build()))
                .surfaceIndicatorGenerator(indicator -> indicator
                        .surfaceRock(Coal)));
        register(context, COPPER_TIN_VEIN, vein -> vein
                .clusterSize(UniformInt.of(40, 52)).density(1.0f).weight(50)
                .layer(WorldGenLayers.STONE)
                .heightRangeUniform(-10, 160)
                .biomes(BiomeTags.IS_OVERWORLD)
                .veinedVeinGenerator(generator -> generator
                        .oreBlock(new VeinBlockDefinition(Chalcopyrite, 5))
                        .oreBlock(new VeinBlockDefinition(Zeolite, 2))
                        .oreBlock(new VeinBlockDefinition(Cassiterite, 2))
                        .rareBlock(new VeinBlockDefinition(Realgar, 1))
                        .rareBlockChance(0.1f)
                        .veininessThreshold(0.01f)
                        .maxRichnessThreshold(0.175f)
                        .minRichness(0.7f)
                        .maxRichness(1.0f)
                        .edgeRoundoffBegin(3)
                        .maxEdgeRoundoff(0.1f))
                .surfaceIndicatorGenerator(indicator -> indicator
                        .surfaceRock(Chalcopyrite)));
        register(context, GALENA_VEIN, vein -> vein
                .clusterSize(UniformInt.of(32, 40)).density(0.25f).weight(40)
                .layer(WorldGenLayers.STONE)
                .heightRangeUniform(-15, 45)
                .biomes(BiomeTags.IS_OVERWORLD)
                .layeredVeinGenerator(generator -> generator
                        .withLayerPattern(() -> GTLayerPattern.builder(stoneRules)
                                .layer(l -> l.weight(3).mat(Galena).size(2, 4))
                                .layer(l -> l.weight(2).mat(Silver).size(1, 1))
                                .layer(l -> l.weight(1).mat(Lead).size(1, 1))
                                .build()))
                .surfaceIndicatorGenerator(indicator -> indicator
                        .surfaceRock(Galena)));
        register(context, GARNET_TIN_VEIN, vein -> vein
                .clusterSize(UniformInt.of(32, 40)).density(0.4f).weight(80)
                .layer(WorldGenLayers.STONE)
                .heightRangeUniform(30, 60)
                .biomes(BiomeTags.IS_OVERWORLD)
                .layeredVeinGenerator(generator -> generator
                        .withLayerPattern(() -> GTLayerPattern.builder(stoneRules)
                                .layer(l -> l.weight(3).mat(CassiteriteSand).size(2, 4))
                                .layer(l -> l.weight(2).mat(GarnetSand).size(1, 1))
                                .layer(l -> l.weight(2).mat(Asbestos).size(1, 1))
                                .layer(l -> l.weight(1).mat(Diatomite).size(1, 1))
                                .build()))
                .surfaceIndicatorGenerator(indicator -> indicator
                        .surfaceRock(GarnetSand)));
        register(context, GARNET_VEIN, vein -> vein
                .clusterSize(UniformInt.of(50, 64)).density(0.75f).weight(40)
                .layer(WorldGenLayers.STONE)
                .heightRangeUniform(-10, 50)
                .biomes(BiomeTags.IS_OVERWORLD)
                .dikeVeinGenerator(generator -> generator
                        .withBlock(new DikeBlockDefinition(GarnetRed, 3, -10, 50))
                        .withBlock(new DikeBlockDefinition(GarnetYellow, 2, -10, 50))
                        .withBlock(new DikeBlockDefinition(Amethyst, 2, -10, 22))
                        .withBlock(new DikeBlockDefinition(Opal, 1, 18, 50)))
                .surfaceIndicatorGenerator(indicator -> indicator
                        .surfaceRock(GarnetRed)
                        .placement(SurfaceIndicatorGenerator.IndicatorPlacement.ABOVE)));
        register(context, IRON_VEIN, vein -> vein
                .clusterSize(UniformInt.of(40, 52)).density(1.0f).weight(120)
                .layer(WorldGenLayers.STONE)
                .heightRangeUniform(-10, 60)
                .biomes(BiomeTags.IS_OVERWORLD)
                .veinedVeinGenerator(generator -> generator
                        .oreBlock(new VeinBlockDefinition(Goethite, 5))
                        .oreBlock(new VeinBlockDefinition(YellowLimonite, 2))
                        .oreBlock(new VeinBlockDefinition(Hematite, 2))
                        .oreBlock(new VeinBlockDefinition(Malachite, 1))
                        .veininessThreshold(0.01f)
                        .maxRichnessThreshold(0.175f)
                        .minRichness(0.7f)
                        .maxRichness(1.0f)
                        .edgeRoundoffBegin(3)
                        .maxEdgeRoundoff(0.1f))
                .surfaceIndicatorGenerator(indicator -> indicator
                        .surfaceRock(Goethite)));
        register(context, LUBRICANT_VEIN, vein -> vein
                .clusterSize(UniformInt.of(25, 29)).density(0.25f).weight(40)
                .layer(WorldGenLayers.STONE)
                .heightRangeUniform(0, 50)
                .biomes(BiomeTags.IS_OVERWORLD)
                .layeredVeinGenerator(generator -> generator
                        .withLayerPattern(() -> GTLayerPattern.builder(stoneRules)
                                .layer(l -> l.weight(3).mat(Soapstone).size(2, 4))
                                .layer(l -> l.weight(2).mat(Talc).size(1, 1))
                                .layer(l -> l.weight(2).mat(GlauconiteSand).size(1, 1))
                                .layer(l -> l.weight(1).mat(Pentlandite).size(1, 1))
                                .build()))
                .surfaceIndicatorGenerator(indicator -> indicator
                        .surfaceRock(Talc)));
        register(context, MAGNETITE_VEIN, vein -> vein
                .clusterSize(UniformInt.of(38, 44)).density(0.15f).weight(80)
                .layer(WorldGenLayers.STONE)
                .heightRangeUniform(10, 60)
                .biomes(BiomeTags.IS_OVERWORLD)
                .layeredVeinGenerator(generator -> generator
                        .withLayerPattern(() -> GTLayerPattern.builder(stoneRules)
                                .layer(l -> l.weight(3).mat(Magnetite).size(2, 4))
                                .layer(l -> l.weight(2).mat(VanadiumMagnetite).size(1, 1))
                                .layer(l -> l.weight(1).mat(Gold).size(1, 1))
                                .build()))
                .surfaceIndicatorGenerator(indicator -> indicator
                        .surfaceRock(Magnetite)));
        register(context, MINERAL_SAND_VEIN, vein -> vein
                .clusterSize(UniformInt.of(32, 40)).density(0.2f).weight(80)
                .layer(WorldGenLayers.STONE)
                .heightRangeUniform(15, 60)
                .biomes(BiomeTags.IS_OVERWORLD)
                .layeredVeinGenerator(generator -> generator
                        .withLayerPattern(() -> GTLayerPattern.builder(stoneRules)
                                .layer(l -> l.weight(3).mat(BasalticMineralSand).size(2, 4))
                                .layer(l -> l.weight(2).mat(GraniticMineralSand).size(1, 1))
                                .layer(l -> l.weight(2).mat(FullersEarth).size(1, 1))
                                .layer(l -> l.weight(1).mat(Gypsum).size(1, 1))
                                .build()))
                .surfaceIndicatorGenerator(indicator -> indicator
                        .surfaceRock(BasalticMineralSand)));
        register(context, NICKEL_VEIN, vein -> vein
                .clusterSize(UniformInt.of(32, 40)).density(0.25f).weight(40)
                .layer(WorldGenLayers.STONE)
                .heightRangeUniform(-10, 60)
                .biomes(BiomeTags.IS_OVERWORLD)
                .layeredVeinGenerator(generator -> generator
                        .withLayerPattern(() -> GTLayerPattern.builder(stoneRules)
                                .layer(l -> l.weight(3).mat(Garnierite).size(2, 4))
                                .layer(l -> l.weight(2).mat(Nickel).size(1, 1))
                                .layer(l -> l.weight(2).mat(Cobaltite).size(1, 1))
                                .layer(l -> l.weight(1).mat(Pentlandite).size(1, 1))
                                .build()))
                .surfaceIndicatorGenerator(indicator -> indicator
                        .surfaceRock(Nickel)));
        register(context, SALTS_VEIN, vein -> vein
                .clusterSize(UniformInt.of(32, 40)).density(0.2f).weight(50)
                .layer(WorldGenLayers.STONE)
                .heightRangeUniform(30, 70)
                .biomes(BiomeTags.IS_OVERWORLD)
                .layeredVeinGenerator(generator -> generator
                        .withLayerPattern(() -> GTLayerPattern.builder(stoneRules)
                                .layer(l -> l.weight(3).mat(RockSalt).size(2, 4))
                                .layer(l -> l.weight(2).mat(Salt).size(1, 1))
                                .layer(l -> l.weight(1).mat(Lepidolite).size(1, 1))
                                .layer(l -> l.weight(1).mat(Spodumene).size(1, 1))
                                .build()))
                .surfaceIndicatorGenerator(indicator -> indicator
                        .surfaceRock(Salt)));
        register(context, OILSANDS_VEIN, vein -> vein
                .clusterSize(UniformInt.of(25, 29)).density(0.3f).weight(40)
                .layer(WorldGenLayers.STONE)
                .heightRangeUniform(30, 80)
                .biomes(BiomeTags.IS_OVERWORLD)
                .layeredVeinGenerator(generator -> generator
                        .withLayerPattern(() -> GTLayerPattern.builder(stoneRules)
                                .layer(l -> l.weight(3).mat(Oilsands).size(2, 4))
                                .layer(l -> l.weight(2).mat(Oilsands).size(1, 1))
                                .layer(l -> l.weight(1).mat(Oilsands).size(1, 1))
                                .layer(l -> l.weight(1).mat(Oilsands).size(1, 1))
                                .build()))
                .surfaceIndicatorGenerator(indicator -> indicator
                        .surfaceRock(Oilsands)));

        // DEEPSLATE
        RuleTest[] deepslateRules = new RuleTest[] { new TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES) };

        register(context, COPPER_VEIN, vein -> vein
                .clusterSize(UniformInt.of(40, 52)).density(1.0f).weight(80)
                .layer(WorldGenLayers.DEEPSLATE)
                .heightRangeUniform(-40, 10)
                .biomes(BiomeTags.IS_OVERWORLD)
                .veinedVeinGenerator(generator -> generator
                        .oreBlock(new VeinBlockDefinition(Chalcopyrite, 5))
                        .oreBlock(new VeinBlockDefinition(Iron, 2))
                        .oreBlock(new VeinBlockDefinition(Pyrite, 2))
                        .oreBlock(new VeinBlockDefinition(Copper, 2))
                        .veininessThreshold(0.01f)
                        .maxRichnessThreshold(0.175f)
                        .minRichness(0.7f)
                        .maxRichness(1.0f)
                        .edgeRoundoffBegin(3)
                        .maxEdgeRoundoff(0.1f))
                .surfaceIndicatorGenerator(indicator -> indicator
                        .surfaceRock(Copper)));
        register(context, DIAMOND_VEIN, vein -> vein
                .clusterSize(UniformInt.of(32, 40)).density(0.25f).weight(40)
                .layer(WorldGenLayers.DEEPSLATE)
                .heightRangeUniform(-55, -30)
                .biomes(BiomeTags.IS_OVERWORLD)
                .classicVeinGenerator(generator -> generator
                        .primary(b -> b.mat(Graphite).size(4))
                        .secondary(b -> b.mat(Graphite).size(3))
                        .between(b -> b.mat(Diamond).size(3))
                        .sporadic(b -> b.mat(Coal)))
                .surfaceIndicatorGenerator(indicator -> indicator
                        .surfaceRock(Diamond)
                        .density(0.1f)
                        .placement(SurfaceIndicatorGenerator.IndicatorPlacement.ABOVE)
                        .radius(2)));
        register(context, LAPIS_VEIN, vein -> vein
                .clusterSize(UniformInt.of(40, 52)).density(0.75f).weight(40)
                .layer(WorldGenLayers.DEEPSLATE)
                .heightRangeUniform(-60, 10)
                .biomes(BiomeTags.IS_OVERWORLD)
                .dikeVeinGenerator(generator -> generator
                        .withBlock(new DikeBlockDefinition(Lazurite, 3, -60, 10))
                        .withBlock(new DikeBlockDefinition(Sodalite, 2, -50, 0))
                        .withBlock(new DikeBlockDefinition(Lapis, 2, -50, 0))
                        .withBlock(new DikeBlockDefinition(Calcite, 1, -40, 10)))
                .surfaceIndicatorGenerator(indicator -> indicator
                        .surfaceRock(Lapis)
                        .density(0.15f)
                        .placement(SurfaceIndicatorGenerator.IndicatorPlacement.ABOVE)
                        .radius(3)));
        register(context, MANGANESE_VEIN, vein -> vein
                .clusterSize(UniformInt.of(50, 64)).density(0.75f).weight(20)
                .layer(WorldGenLayers.DEEPSLATE)
                .heightRangeUniform(-30, 0)
                .biomes(BiomeTags.IS_OVERWORLD)
                .dikeVeinGenerator(generator -> generator
                        .withBlock(new DikeBlockDefinition(Grossular, 3, -50, -5))
                        .withBlock(new DikeBlockDefinition(Spessartine, 2, -40, -15))
                        .withBlock(new DikeBlockDefinition(Pyrolusite, 2, -40, -15))
                        .withBlock(new DikeBlockDefinition(Tantalite, 1, -30, -5)))
                .surfaceIndicatorGenerator(indicator -> indicator
                        .surfaceRock(Grossular)
                        .density(0.15f)
                        .radius(3)));
        register(context, MICA_VEIN, vein -> vein
                .clusterSize(UniformInt.of(32, 40)).density(0.25f).weight(20)
                .layer(WorldGenLayers.DEEPSLATE)
                .heightRangeUniform(-40, -10)
                .biomes(BiomeTags.IS_OVERWORLD)
                .layeredVeinGenerator(generator -> generator
                        .withLayerPattern(() -> GTLayerPattern.builder(deepslateRules)
                                .layer(l -> l.weight(3).mat(Kyanite).size(2, 4))
                                .layer(l -> l.weight(2).mat(Mica).size(1, 1))
                                .layer(l -> l.weight(2).mat(Bauxite).size(1, 1))
                                .layer(l -> l.weight(1).mat(Pollucite).size(1, 1))
                                .build()))
                .surfaceIndicatorGenerator(indicator -> indicator
                        .surfaceRock(Mica)
                        .radius(3)));
        register(context, OLIVINE_VEIN, vein -> vein
                .clusterSize(UniformInt.of(32, 40)).density(0.25f).weight(20)
                .layer(WorldGenLayers.DEEPSLATE)
                .heightRangeUniform(-20, 10)
                .biomes(BiomeTags.IS_OVERWORLD)
                .layeredVeinGenerator(generator -> generator
                        .withLayerPattern(() -> GTLayerPattern.builder(deepslateRules)
                                .layer(l -> l.weight(3).mat(Bentonite).size(2, 4))
                                .layer(l -> l.weight(2).mat(Magnesite).size(1, 1))
                                .layer(l -> l.weight(2).mat(Olivine).size(1, 1))
                                .layer(l -> l.weight(1).mat(GlauconiteSand).size(1, 1))
                                .build()))
                .surfaceIndicatorGenerator(indicator -> indicator
                        .surfaceRock(Olivine)
                        .density(0.15f)
                        .radius(3)));
        register(context, REDSTONE_VEIN, vein -> vein
                .clusterSize(UniformInt.of(32, 40)).density(0.2f).weight(60)
                .layer(WorldGenLayers.DEEPSLATE)
                .heightRangeUniform(-65, -10)
                .biomes(BiomeTags.IS_OVERWORLD)
                .layeredVeinGenerator(generator -> generator
                        .withLayerPattern(() -> GTLayerPattern.builder(deepslateRules)
                                .layer(l -> l.weight(3).mat(Redstone).size(2, 4))
                                .layer(l -> l.weight(2).mat(Ruby).size(1, 1))
                                .layer(l -> l.weight(1).mat(Cinnabar).size(1, 1))
                                .build()))
                .surfaceIndicatorGenerator(indicator -> indicator
                        .surfaceRock(Redstone)));
        register(context, SAPPHIRE_VEIN, vein -> vein
                .clusterSize(UniformInt.of(25, 29)).density(0.25f).weight(60)
                .layer(WorldGenLayers.DEEPSLATE)
                .heightRangeUniform(-40, 0)
                .biomes(BiomeTags.IS_OVERWORLD)
                .layeredVeinGenerator(generator -> generator
                        .withLayerPattern(() -> GTLayerPattern.builder(deepslateRules)
                                .layer(l -> l.weight(3).mat(Almandine).size(2, 4))
                                .layer(l -> l.weight(2).mat(Pyrope).size(1, 1))
                                .layer(l -> l.weight(1).mat(Sapphire).size(1, 1))
                                .layer(l -> l.weight(1).mat(GreenSapphire).size(1, 1))
                                .build()))
                .surfaceIndicatorGenerator(indicator -> indicator
                        .surfaceRock(Sapphire)
                        .density(0.15f)
                        .placement(SurfaceIndicatorGenerator.IndicatorPlacement.ABOVE)
                        .radius(3)));
    }
}
