package com.modularmc.gtpm.common.data;

import com.modularmc.gtpm.GTPM;
import com.modularmc.gtpm.api.GTCEuAPI;
import com.modularmc.gtpm.api.GTValues;
import com.modularmc.gtpm.api.addon.AddonFinder;
import com.modularmc.gtpm.api.addon.events.MaterialCasingCollectionEvent;
import com.modularmc.gtpm.api.block.*;
import com.modularmc.gtpm.api.data.chemical.material.ItemMaterialData;
import com.modularmc.gtpm.api.data.chemical.material.MarkerMaterials;
import com.modularmc.gtpm.api.data.chemical.material.Material;
import com.modularmc.gtpm.api.data.chemical.material.properties.PropertyKey;
import com.modularmc.gtpm.api.data.chemical.material.stack.MaterialEntry;
import com.modularmc.gtpm.api.data.tag.TagPrefix;
import com.modularmc.gtpm.api.item.PipeBlockItem;
import com.modularmc.gtpm.api.machine.multiblock.IBatteryData;
import com.modularmc.gtpm.api.pipenet.longdistance.LongDistancePipeBlock;
import com.modularmc.gtpm.common.block.*;
import com.modularmc.gtpm.common.block.explosive.IndustrialTNTBlock;
import com.modularmc.gtpm.common.block.explosive.PowderbarrelBlock;
import com.modularmc.gtpm.common.data.models.GTModels;
import com.modularmc.gtpm.common.item.LampBlockItem;
import com.modularmc.gtpm.common.item.LaserPipeBlockItem;
import com.modularmc.gtpm.common.pipelike.duct.DuctPipeType;
import com.modularmc.gtpm.common.pipelike.fluidpipe.longdistance.LDFluidPipeType;
import com.modularmc.gtpm.common.pipelike.item.longdistance.LDItemPipeType;
import com.modularmc.gtpm.common.pipelike.laser.LaserPipeType;
import com.modularmc.gtpm.common.pipelike.optical.OpticalPipeType;
import com.modularmc.gtpm.core.mixins.BlockPropertiesAccessor;
import com.modularmc.gtpm.data.recipe.CustomTags;
import com.modularmc.gtpm.utils.FormattingUtil;
import com.modularmc.gtpm.utils.TagUtil;
import com.modularmc.gtpm.utils.memoization.GTMemoizer;

import com.modularmc.registrate.builders.BlockBuilder;
import com.modularmc.registrate.localization.LocalizationDeclaration;
import com.modularmc.registrate.localization.MinecraftLocale;
import com.modularmc.registrate.localization.annotation.Trans;
import com.modularmc.registrate.localization.annotation.TransVariants;
import com.modularmc.registrate.localization.annotation.Translation;
import com.modularmc.registrate.localization.annotation.Variant;
import com.modularmc.registrate.providers.core.ProviderType;
import com.modularmc.registrate.util.entry.BlockEntry;
import com.modularmc.registrate.util.nullness.NonNullBiConsumer;
import com.modularmc.registrate.util.nullness.NonNullFunction;
import com.modularmc.registrate.util.nullness.NonNullSupplier;

import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.*;
import net.minecraft.world.level.FoliageColor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.grower.TreeGrower;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.client.model.generators.ModelProvider;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.registries.datamaps.builtin.Compostable;
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Table;
import it.unimi.dsi.fastutil.ints.Int2ObjectAVLTreeMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectSortedMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static com.modularmc.gtpm.api.GTValues.*;
import static com.modularmc.gtpm.common.data.GCYMBlocks.*;
import static com.modularmc.gtpm.common.registry.GTRegistration.REGISTRATE;

@TransVariants(keyPrefix = "tile.gtpm",
               value = {
                       @Variant(id = "brittle_charcoal.name",
                                source = "Brittle Charcoal",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "脆木炭块")),
                       @Variant(id = "foam.name",
                                source = "Foam",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "建筑泡沫")),
                       @Variant(id = "petrified_foam.name",
                                source = "Petrified Foam",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "石化建筑泡沫")),
                       @Variant(id = "reinforced_foam.name",
                                source = "Reinforced Foam",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "强化建筑泡沫")),
                       @Variant(id = "reinforced_stone.name",
                                source = "Reinforced Stone",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "防爆石")),
                       @Variant(id = "seal.name",
                                source = "Sealed Block",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "密封方块")),
                       @Variant(id = "brittle_charcoal.tooltip.0",
                                source = "Produced by the Charcoal Pile Igniter.",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "产自木炭堆点火器。")),
                       @Variant(id = "brittle_charcoal.tooltip.1",
                                source = "Mine this to get Charcoal.",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "挖掘可掉落木炭。")),
               })
@SuppressWarnings({ "removal", "unchecked" })
public class GTBlocks {

    public static final ObjectList<Block> GLASS = new ObjectArrayList<>();
    public static final Int2ObjectSortedMap<Block> MACHINE_CASINGS = new Int2ObjectAVLTreeMap<>();
    public static final Int2ObjectSortedMap<Block> HERMETIC_CASINGS = new Int2ObjectAVLTreeMap<>();
    public static final Int2ObjectSortedMap<Block> COMPUTER_CASINGS = new Int2ObjectAVLTreeMap<>();
    public static final Int2ObjectSortedMap<Block> COMPUTER_HEAT_VENTS = new Int2ObjectAVLTreeMap<>();

    //////////////////////////////////////
    // ***** Procedural Blocks *****//

    @Trans(key = "block.gtpm.normal_laser_pipe.tooltip",
           source = "§7Transmitting power with §fno loss§7 in straight lines",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§f无损§7传递能量，仅限直线摆放"))
    public static final BlockEntry<LaserPipeBlock>[] LASER_PIPES = new BlockEntry[LaserPipeType.values().length];
    @Trans(key = "block.gtpm.normal_optical_pipe.tooltip",
           source = "§7Transmitting §fComputation§7 or §fResearch Data§7",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7传递§f算力§7或§f研究数据§7"))
    public static final BlockEntry<OpticalPipeBlock>[] OPTICAL_PIPES = new BlockEntry[OpticalPipeType.values().length];
    public static final BlockEntry<DuctPipeBlock>[] DUCT_PIPES = new BlockEntry[DuctPipeType.VALUES.length];

    //////////////////////////////////////
    // Pipes Blocks //
    //////////////////////////////////////
    static {
        REGISTRATE.creativeModeTab(GTCreativeModeTabs.MATERIAL_PIPE);
    }

    // Laser Pipe Blocks
    private static void generateLaserPipeBlocks() {
        GTPM.LOGGER.debug("Generating GTPM Laser Pipe Blocks...");
        for (int i = 0; i < LaserPipeType.values().length; ++i) {
            registerLaserPipeBlock(i);
        }
        GTPM.LOGGER.debug("Generating GTPM Laser Pipe Blocks... Complete!");
    }

    private static void registerLaserPipeBlock(int index) {
        var type = LaserPipeType.values()[index];
        var entry = REGISTRATE
                .block("%s_laser_pipe".formatted(type.getSerializedName()), (p) -> new LaserPipeBlock(p, type))
                .localization(text -> text.source("Normal Laser Pipe")
                        .translation(MinecraftLocale.ZH_CN, "标准激光管道"))
                .initialProperties(() -> Blocks.IRON_BLOCK)
                .properties(p -> p.dynamicShape().noOcclusion().forceSolidOn())
                .gtBlockstate(GTModels::createPipeBlockModel)
                .defaultLoot()
                .addLayer(() -> RenderType::cutoutMipped)
                .addLayer(() -> RenderType::translucent)
                .tag(CustomTags.MINEABLE_WITH_CONFIG_VALID_PICKAXE_WIRE_CUTTER)
                .color(() -> LaserPipeBlock::tintedColor)
                .item(LaserPipeBlockItem::new)
                .setData(ProviderType.ITEM_MODEL, NonNullBiConsumer.noop())
                .color(() -> LaserPipeBlockItem::tintColor)
                .build()
                .register();
        LASER_PIPES[index] = entry;
    }

    // Optical Pipe Blocks
    private static void generateOpticalPipeBlocks() {
        GTPM.LOGGER.debug("Generating GTPM Optical Pipe Blocks...");
        for (int i = 0; i < OpticalPipeType.values().length; ++i) {
            registerOpticalPipeBlock(i);
        }
        GTPM.LOGGER.debug("Generating GTPM Optical Pipe Blocks... Complete!");
    }

    private static void registerOpticalPipeBlock(int index) {
        var type = OpticalPipeType.values()[index];
        var entry = REGISTRATE
                .block("%s_optical_pipe".formatted(type.getSerializedName()), (p) -> new OpticalPipeBlock(p, type))
                .localization(text -> text.source("Optical Fiber Cable")
                        .translation(MinecraftLocale.ZH_CN, "光缆"))
                .initialProperties(() -> Blocks.IRON_BLOCK)
                .properties(p -> p.dynamicShape().noOcclusion().forceSolidOn())
                .gtBlockstate(GTModels::createPipeBlockModel)
                .defaultLoot()
                .addLayer(() -> RenderType::cutoutMipped)
                .addLayer(() -> RenderType::translucent)
                .tag(CustomTags.MINEABLE_WITH_CONFIG_VALID_PICKAXE_WIRE_CUTTER)
                .color(() -> OpticalPipeBlock::tintedColor)
                .item(PipeBlockItem::new)
                .setData(ProviderType.ITEM_MODEL, NonNullBiConsumer.noop())
                .build()
                .register();
        OPTICAL_PIPES[index] = entry;
    }

    // Duct Pipe Blocks
    private static void generateDuctPipeBlocks() {
        GTPM.LOGGER.debug("Generating GTPM Duct Pipe Blocks...");
        for (int i = 0; i < DuctPipeType.VALUES.length; ++i) {
            registerDuctPipeBlock(i);
        }
        GTPM.LOGGER.debug("Generating GTPM Duct Pipe Blocks... Complete!");
    }

    private static void registerDuctPipeBlock(int index) {
        var type = DuctPipeType.VALUES[index];
        var entry = REGISTRATE
                .block("%s_duct_pipe".formatted(type.getSerializedName()), (p) -> new DuctPipeBlock(p, type))
                .localization(text -> {
                    switch (type) {
                        case SMALL -> text.source("Small Duct Pipe").translation(MinecraftLocale.ZH_CN, "小型风管");
                        case NORMAL -> text.source("Normal Duct Pipe").translation(MinecraftLocale.ZH_CN, "标准风管");
                        case LARGE -> text.source("Large Duct Pipe").translation(MinecraftLocale.ZH_CN, "大型风管");
                        case HUGE -> text.source("Huge Duct Pipe").translation(MinecraftLocale.ZH_CN, "巨型风管");
                    }
                })
                .initialProperties(() -> Blocks.IRON_BLOCK)
                .properties(p -> p.dynamicShape().noOcclusion().forceSolidOn())
                .gtBlockstate(GTModels::createPipeBlockModel)
                .defaultLoot()
                .addLayer(() -> RenderType::cutoutMipped)
                .addLayer(() -> RenderType::translucent)
                .tag(CustomTags.MINEABLE_WITH_CONFIG_VALID_PICKAXE_WRENCH)
                .item(PipeBlockItem::new)
                .setData(ProviderType.ITEM_MODEL, NonNullBiConsumer.noop())
                .build()
                .register();
        DUCT_PIPES[index] = entry;
    }

    // Long Distance Item Pipe Blocks
    public static final BlockEntry<LongDistancePipeBlock> LD_ITEM_PIPE = REGISTRATE
            .block("long_distance_item_pipeline",
                    properties -> new LongDistancePipeBlock(properties, LDItemPipeType.INSTANCE))
            .localization(text -> text
                    .source("Long Distance Item Pipeline")
                    .translation(MinecraftLocale.ZH_CN, "长距离物品管道"))
            .initialProperties(() -> Blocks.IRON_BLOCK)
            .exBlockstate(GTModels.cubeAllModel(GTPM.id("block/pipe/ld_item_pipe/block")))
            .tag(CustomTags.MINEABLE_WITH_CONFIG_VALID_PICKAXE_WRENCH, BlockTags.NEEDS_STONE_TOOL)
            .simpleItem()
            .register();

    // Long Distance Fluid Pipe Blocks
    public static final BlockEntry<LongDistancePipeBlock> LD_FLUID_PIPE = REGISTRATE
            .block("long_distance_fluid_pipeline",
                    properties -> new LongDistancePipeBlock(properties, LDFluidPipeType.INSTANCE))
            .localization(text -> text
                    .source("Long Distance Fluid Pipeline")
                    .translation(MinecraftLocale.ZH_CN, "长距离流体管道"))
            .initialProperties(() -> Blocks.IRON_BLOCK)
            .exBlockstate(GTModels.cubeAllModel(GTPM.id("block/pipe/ld_fluid_pipe/block")))
            .tag(CustomTags.MINEABLE_WITH_CONFIG_VALID_PICKAXE_WRENCH, BlockTags.NEEDS_STONE_TOOL)
            .simpleItem()
            .register();

    //////////////////////////////////////
    // ****** Casing Blocks *****//
    //////////////////////////////////////
    static {
        REGISTRATE.creativeModeTab(GTCreativeModeTabs.DECORATION);
    }

    // Multiblock Machine Casing Blocks
    public static final BlockEntry<Block> CASING_WOOD_WALL = createSidedCasingBlock("wood_wall",
            GTPM.id("block/casings/wood_wall"),
            text -> text.source("Wood Wall")
                    .translation(MinecraftLocale.ZH_CN, "木壁"));
    public static final BlockEntry<Block> CASING_COKE_BRICKS = createBrickCasingBlock("coke_oven_bricks",
            GTPM.id("block/casings/solid/machine_coke_bricks"),
            text -> text.source("Coke Oven Bricks")
                    .translation(MinecraftLocale.ZH_CN, "焦炉砖块"));
    public static final BlockEntry<Block> CASING_PRIMITIVE_BRICKS = createBrickCasingBlock("firebricks",
            GTPM.id("block/casings/solid/machine_primitive_bricks"),
            text -> text.source("Firebricks")
                    .translation(MinecraftLocale.ZH_CN, "耐火砖块"));
    public static final BlockEntry<Block> CASING_BRONZE_BRICKS = createCasingBlock("steam_machine_casing",
            GTPM.id("block/casings/solid/machine_casing_bronze_plated_bricks"),
            text -> text.source("Steam Machine Casing")
                    .translation(MinecraftLocale.ZH_CN, "蒸汽机械方块"));
    public static final BlockEntry<Block> CASING_INVAR_HEATPROOF = createCasingBlock("heatproof_machine_casing",
            GTPM.id("block/casings/solid/machine_casing_heatproof"),
            text -> text.source("Heat Proof Invar Machine Casing")
                    .translation(MinecraftLocale.ZH_CN, "殷钢隔热机械方块"));
    public static final BlockEntry<Block> CASING_ALUMINIUM_FROSTPROOF = createCasingBlock("frostproof_machine_casing",
            GTPM.id("block/casings/solid/machine_casing_frost_proof"),
            text -> text.source("Frost Proof Aluminium Machine Casing")
                    .translation(MinecraftLocale.ZH_CN, "铝制防冻机械方块"));
    public static final BlockEntry<Block> CASING_STEEL_SOLID = createCasingBlock("solid_machine_casing",
            GTPM.id("block/casings/solid/machine_casing_solid_steel"),
            text -> text.source("Solid Machine Casing")
                    .translation(MinecraftLocale.ZH_CN, "脱氧钢机械方块"));
    public static final BlockEntry<Block> CASING_STAINLESS_CLEAN = createCasingBlock("clean_machine_casing",
            GTPM.id("block/casings/solid/machine_casing_clean_stainless_steel"),
            text -> text.source("Clean Stainless Steel Casing")
                    .translation(MinecraftLocale.ZH_CN, "洁净不锈钢机械方块"));
    public static final BlockEntry<Block> CASING_TITANIUM_STABLE = createCasingBlock("stable_machine_casing",
            GTPM.id("block/casings/solid/machine_casing_stable_titanium"),
            text -> text.source("Stable Titanium Machine Casing")
                    .translation(MinecraftLocale.ZH_CN, "加强钛机械方块"));
    public static final BlockEntry<Block> CASING_TUNGSTENSTEEL_ROBUST = createCasingBlock("robust_machine_casing",
            GTPM.id("block/casings/solid/machine_casing_robust_tungstensteel"),
            text -> text.source("Robust Tungstensteel Machine Casing")
                    .translation(MinecraftLocale.ZH_CN, "强化钨钢机械方块"));
    public static final BlockEntry<Block> CASING_PTFE_INERT = createCasingBlock("inert_machine_casing",
            GTPM.id("block/casings/solid/machine_casing_inert_ptfe"),
            text -> text.source("Chemically Inert PTFE Machine Casing")
                    .translation(MinecraftLocale.ZH_CN, "聚四氟乙烯化学惰性机械方块"));
    public static final BlockEntry<Block> CASING_HSSE_STURDY = createCasingBlock("sturdy_machine_casing",
            GTPM.id("block/casings/solid/machine_casing_sturdy_hsse"),
            text -> text.source("Sturdy HSS-E Machine Casing")
                    .translation(MinecraftLocale.ZH_CN, "坚固高速钢-E机械方块"));
    public static final BlockEntry<Block> CASING_PALLADIUM_SUBSTATION = createCasingBlock("palladium_substation_casing",
            GTPM.id("block/casings/solid/machine_casing_palladium_substation"),
            text -> text.source("Palladium Substation Casing")
                    .translation(MinecraftLocale.ZH_CN, "镀钯变电站机械方块"));
    public static final BlockEntry<TransparentBlock> CASING_TEMPERED_GLASS = createGlassCasingBlock("tempered_glass",
            GTPM.id("block/casings/transparent/tempered_glass"), () -> RenderType::translucent,
            text -> text.source("Tempered Glass")
                    .translation(MinecraftLocale.ZH_CN, "钢化玻璃"));
    public static final ImmutableMap<Material, BlockEntry<Block>> MATERIALS_TO_CASINGS;

    static {
        ImmutableMap.Builder<Material, BlockEntry<Block>> builder = ImmutableMap.builder();
        builder.put(GTMaterials.Bronze, CASING_BRONZE_BRICKS);
        builder.put(GTMaterials.Invar, CASING_INVAR_HEATPROOF);
        builder.put(GTMaterials.Aluminium, CASING_ALUMINIUM_FROSTPROOF);
        builder.put(GTMaterials.Steel, CASING_STEEL_SOLID);
        builder.put(GTMaterials.StainlessSteel, CASING_STAINLESS_CLEAN);
        builder.put(GTMaterials.Titanium, CASING_TITANIUM_STABLE);
        builder.put(GTMaterials.TungstenSteel, CASING_TUNGSTENSTEEL_ROBUST);
        builder.put(GTMaterials.Polytetrafluoroethylene, CASING_PTFE_INERT);
        builder.put(GTMaterials.HSSE, CASING_HSSE_STURDY);
        // GCYM
        builder.put(GTMaterials.HSLASteel, CASING_NONCONDUCTING);
        builder.put(GTMaterials.IncoloyMA956, CASING_VIBRATION_SAFE);
        builder.put(GTMaterials.WatertightSteel, CASING_WATERTIGHT);
        builder.put(GTMaterials.Zeron100, CASING_SECURE_MACERATION);
        builder.put(GTMaterials.TungstenCarbide, CASING_HIGH_TEMPERATURE_SMELTING);
        builder.put(GTMaterials.TitaniumTungstenCarbide, CASING_LASER_SAFE_ENGRAVING);
        builder.put(GTMaterials.Stellite100, CASING_LARGE_SCALE_ASSEMBLING);
        builder.put(GTMaterials.HastelloyC276, CASING_SHOCK_PROOF);

        MaterialCasingCollectionEvent event = new MaterialCasingCollectionEvent(builder);
        AddonFinder.getAddonList().forEach(addon -> addon.collectMaterialCasings(event));

        MATERIALS_TO_CASINGS = builder.build();
    }

    // Assembly Line
    public static final BlockEntry<Block> CASING_GRATE = createCasingBlock("assembly_line_grating",
            GTPM.id("block/casings/pipe/machine_casing_grate"),
            text -> text.source("Assembly Line Grating")
                    .translation(MinecraftLocale.ZH_CN, "装配线格栅"));
    public static final BlockEntry<Block> CASING_ASSEMBLY_CONTROL = createCasingBlock("assembly_line_casing",
            GTPM.id("block/casings/mechanic/machine_casing_assembly_control"),
            text -> text.source("Assembly Line Casing")
                    .translation(MinecraftLocale.ZH_CN, "装配线外壳"));
    public static final BlockEntry<TransparentBlock> CASING_LAMINATED_GLASS = createGlassCasingBlock("laminated_glass",
            GTPM.id("block/casings/transparent/laminated_glass"), () -> RenderType::cutoutMipped,
            text -> text.source("Laminated Glass")
                    .translation(MinecraftLocale.ZH_CN, "夹层玻璃"));
    public static final BlockEntry<ActiveBlock> CASING_ASSEMBLY_LINE = createActiveCasing("assembly_line_unit",
            "block/variant/assembly_line",
            text -> text.source("Assembly Control Casing")
                    .translation(MinecraftLocale.ZH_CN, "装配线控制外壳"));

    // Gear Boxes
    public static final BlockEntry<Block> CASING_BRONZE_GEARBOX = createCasingBlock("bronze_gearbox",
            GTPM.id("block/casings/gearbox/machine_casing_gearbox_bronze"),
            text -> text.source("Bronze Gearbox Casing")
                    .translation(MinecraftLocale.ZH_CN, "青铜齿轮箱机械方块"));
    public static final BlockEntry<Block> CASING_STEEL_GEARBOX = createCasingBlock("steel_gearbox",
            GTPM.id("block/casings/gearbox/machine_casing_gearbox_steel"),
            text -> text.source("Steel Gearbox Casing")
                    .translation(MinecraftLocale.ZH_CN, "钢齿轮箱机械方块"));
    public static final BlockEntry<Block> CASING_STAINLESS_STEEL_GEARBOX = createCasingBlock("stainless_steel_gearbox",
            GTPM.id("block/casings/gearbox/machine_casing_gearbox_stainless_steel"),
            text -> text.source("Stainless Steel Gearbox Casing")
                    .translation(MinecraftLocale.ZH_CN, "不锈钢齿轮箱机械方块"));
    public static final BlockEntry<Block> CASING_TITANIUM_GEARBOX = createCasingBlock("titanium_gearbox",
            GTPM.id("block/casings/gearbox/machine_casing_gearbox_titanium"),
            text -> text.source("Titanium Gearbox Casing")
                    .translation(MinecraftLocale.ZH_CN, "钛齿轮箱机械方块"));
    public static final BlockEntry<Block> CASING_TUNGSTENSTEEL_GEARBOX = createCasingBlock("tungstensteel_gearbox",
            GTPM.id("block/casings/gearbox/machine_casing_gearbox_tungstensteel"),
            text -> text.source("Tungstensteel Gearbox Casing")
                    .translation(MinecraftLocale.ZH_CN, "钨钢齿轮箱机械方块"));

    // Turbine Casings
    public static final BlockEntry<Block> CASING_STEEL_TURBINE = createCasingBlock("steel_turbine_casing",
            GTPM.id("block/casings/mechanic/machine_casing_turbine_steel"),
            text -> text.source("Magnalium Turbine Casing")
                    .translation(MinecraftLocale.ZH_CN, "镁铝合金涡轮机械方块"));
    public static final BlockEntry<Block> CASING_TITANIUM_TURBINE = createCasingBlock("titanium_turbine_casing",
            GTPM.id("block/casings/mechanic/machine_casing_turbine_titanium"),
            text -> text.source("Titanium Turbine Casing")
                    .translation(MinecraftLocale.ZH_CN, "钛涡轮机械方块"));
    public static final BlockEntry<Block> CASING_STAINLESS_TURBINE = createCasingBlock("stainless_steel_turbine_casing",
            GTPM.id("block/casings/mechanic/machine_casing_turbine_stainless_steel"),
            text -> text.source("Stainless Turbine Casing")
                    .translation(MinecraftLocale.ZH_CN, "不锈钢涡轮机械方块"));
    public static final BlockEntry<Block> CASING_TUNGSTENSTEEL_TURBINE = createCasingBlock(
            "tungstensteel_turbine_casing", GTPM.id("block/casings/mechanic/machine_casing_turbine_tungstensteel"),
            text -> text.source("Tungstensteel Turbine Casing")
                    .translation(MinecraftLocale.ZH_CN, "钨钢涡轮机械方块"));

    // Pipe casings
    public static final BlockEntry<Block> CASING_BRONZE_PIPE = createCasingBlock("bronze_pipe_casing",
            GTPM.id("block/casings/pipe/machine_casing_pipe_bronze"),
            text -> text.source("Bronze Pipe Casing")
                    .translation(MinecraftLocale.ZH_CN, "青铜管道方块"));
    public static final BlockEntry<Block> CASING_STEEL_PIPE = createCasingBlock("steel_pipe_casing",
            GTPM.id("block/casings/pipe/machine_casing_pipe_steel"),
            text -> text.source("Steel Pipe Casing")
                    .translation(MinecraftLocale.ZH_CN, "钢管道方块"));
    public static final BlockEntry<Block> CASING_TITANIUM_PIPE = createCasingBlock("titanium_pipe_casing",
            GTPM.id("block/casings/pipe/machine_casing_pipe_titanium"),
            text -> text.source("Titanium Pipe Casing")
                    .translation(MinecraftLocale.ZH_CN, "钛管道方块"));
    public static final BlockEntry<Block> CASING_TUNGSTENSTEEL_PIPE = createCasingBlock("tungstensteel_pipe_casing",
            GTPM.id("block/casings/pipe/machine_casing_pipe_tungstensteel"),
            text -> text.source("Tungstensteel Pipe Casing")
                    .translation(MinecraftLocale.ZH_CN, "钨钢管道方块"));
    public static final BlockEntry<Block> CASING_POLYTETRAFLUOROETHYLENE_PIPE = createCasingBlock("ptfe_pipe_casing",
            GTPM.id("block/casings/pipe/machine_casing_pipe_polytetrafluoroethylene"),
            text -> text.source("PTFE Pipe Casing")
                    .translation(MinecraftLocale.ZH_CN, "聚四氟乙烯管道方块"));
    public static final BlockEntry<MinerPipeBlock> MINER_PIPE = REGISTRATE.block("miner_pipe", MinerPipeBlock::new)
            .localization(text -> text
                    .source("Miner Pipe")
                    .translation(MinecraftLocale.ZH_CN, "采矿管道"))
            .initialProperties(() -> Blocks.BEDROCK)
            .properties(BlockBehaviour.Properties::noOcclusion)
            .addLayer(() -> RenderType::cutoutMipped)
            .exBlockstate(GTModels.createModelBlockState(GTPM.id("block/miner_pipe")))
            .tag(BlockTags.DRAGON_IMMUNE, BlockTags.WITHER_IMMUNE, BlockTags.INFINIBURN_END,
                    BlockTags.FEATURES_CANNOT_REPLACE, BlockTags.GEODE_INVALID_BLOCKS)
            .register();

    // The Pump Deck
    public static final BlockEntry<Block> CASING_PUMP_DECK = REGISTRATE
            .block("pump_deck", Block::new)
            .localization(text -> text
                    .source("Pump Deck")
                    .translation(MinecraftLocale.ZH_CN, "水泵盖板"))
            .initialProperties(() -> Blocks.IRON_BLOCK)
            .properties(p -> p.sound(SoundType.WOOD).mapColor(MapColor.WOOD))
            .blockstate(() -> GTModels.createSidedCasingModel(GTPM.id("block/casings/pump_deck")))
            .tag(CustomTags.MINEABLE_WITH_WRENCH, BlockTags.MINEABLE_WITH_AXE)
            .item(BlockItem::new)
            .build()
            .register();

    // spotless:off

    // Machine Casings
    public static final BlockEntry<Block> MACHINE_CASING_ULV = createMachineCasingBlock(ULV);
    public static final BlockEntry<Block> MACHINE_CASING_LV = createMachineCasingBlock(LV);
    public static final BlockEntry<Block> MACHINE_CASING_MV = createMachineCasingBlock(MV);
    public static final BlockEntry<Block> MACHINE_CASING_HV = createMachineCasingBlock(HV);
    public static final BlockEntry<Block> MACHINE_CASING_EV = createMachineCasingBlock(EV);
    public static final BlockEntry<Block> MACHINE_CASING_IV = createMachineCasingBlock(IV);
    public static final BlockEntry<Block> MACHINE_CASING_LuV = createMachineCasingBlock(LuV);
    public static final BlockEntry<Block> MACHINE_CASING_ZPM = createMachineCasingBlock(ZPM);
    public static final BlockEntry<Block> MACHINE_CASING_UV = createMachineCasingBlock(UV);
    public static final BlockEntry<Block> MACHINE_CASING_UHV = createMachineCasingBlock(UHV);
    public static final BlockEntry<Block> MACHINE_CASING_UEV = createMachineCasingBlock(UEV);
    public static final BlockEntry<Block> MACHINE_CASING_UIV = createMachineCasingBlock(UIV);
    public static final BlockEntry<Block> MACHINE_CASING_UXV = createMachineCasingBlock(UXV);
    public static final BlockEntry<Block> MACHINE_CASING_OpV = createMachineCasingBlock(OpV);
    public static final BlockEntry<Block> MACHINE_CASING_MAX = createMachineCasingBlock(MAX);

    // Hermetic Casings
    public static final BlockEntry<Block> HERMETIC_CASING_LV = createHermeticCasing(LV);
    public static final BlockEntry<Block> HERMETIC_CASING_MV = createHermeticCasing(MV);
    public static final BlockEntry<Block> HERMETIC_CASING_HV = createHermeticCasing(HV);
    public static final BlockEntry<Block> HERMETIC_CASING_EV = createHermeticCasing(EV);
    public static final BlockEntry<Block> HERMETIC_CASING_IV = createHermeticCasing(IV);
    public static final BlockEntry<Block> HERMETIC_CASING_LuV = createHermeticCasing(LuV);
    public static final BlockEntry<Block> HERMETIC_CASING_ZPM = createHermeticCasing(ZPM);
    public static final BlockEntry<Block> HERMETIC_CASING_UV = createHermeticCasing(UV);
    public static final BlockEntry<Block> HERMETIC_CASING_UHV = createHermeticCasing(UHV);

    @Trans(key = "block.gtpm.bronze_machine_casing.tooltip",
           source = "§7For your first Steam Machines",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7用来制作你的第一批蒸汽机器"))
    public static final BlockEntry<Block> BRONZE_HULL = createSteamCasing("bronze_machine_casing", "bronze",
            text -> text.source("Bronze Machine Casing")
                    .translation(MinecraftLocale.ZH_CN, "青铜机器外壳"));
    @Trans(key = "block.gtpm.bronze_brick_casing.tooltip",
           source = "§7For your first Steam Machines",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7用来制作你的第一批蒸汽机器"))
    public static final BlockEntry<Block> BRONZE_BRICKS_HULL = createSteamCasing("bronze_brick_casing", "bricked_bronze",
            text -> text.source("Bricked Bronze Casing")
                    .translation(MinecraftLocale.ZH_CN, "砖砌青铜外壳"));
    @Trans(key = "block.gtpm.steel_machine_casing.tooltip",
           source = "§7For improved Steam Machines",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7用来制作更高等级的蒸汽机器"))
    public static final BlockEntry<Block> STEEL_HULL = createSteamCasing("steel_machine_casing", "steel",
            text -> text.source("Solid Steel Machine Casing")
                    .translation(MinecraftLocale.ZH_CN, "牢固机械方块"));
    @Trans(key = "block.gtpm.steel_brick_casing.tooltip",
           source = "§7For improved Steam Machines",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7用来制作更高等级的蒸汽机器"))
    public static final BlockEntry<Block> STEEL_BRICKS_HULL = createSteamCasing("steel_brick_casing", "bricked_steel",
            text -> text.source("Bricked Wrought Iron Casing")
                    .translation(MinecraftLocale.ZH_CN, "砖砌锻铁外壳"));

    // Heating Coils
    public static final BlockEntry<CoilBlock> COIL_CUPRONICKEL = createCoilBlock(CoilBlock.CoilType.CUPRONICKEL,
            text -> text.source("Cupronickel Coil Block")
                    .translation(MinecraftLocale.ZH_CN, "白铜线圈方块"));
    public static final BlockEntry<CoilBlock> COIL_KANTHAL = createCoilBlock(CoilBlock.CoilType.KANTHAL,
            text -> text.source("Kanthal Coil Block")
                    .translation(MinecraftLocale.ZH_CN, "坎塔尔合金线圈方块"));
    public static final BlockEntry<CoilBlock> COIL_NICHROME = createCoilBlock(CoilBlock.CoilType.NICHROME,
            text -> text.source("Nichrome Coil Block")
                    .translation(MinecraftLocale.ZH_CN, "镍铬合金线圈方块"));
    public static final BlockEntry<CoilBlock> COIL_RTMALLOY = createCoilBlock(CoilBlock.CoilType.RTMALLOY,
            text -> text.source("RTM Alloy Coil Block")
                    .translation(MinecraftLocale.ZH_CN, "钌钨钼合金线圈方块"));
    public static final BlockEntry<CoilBlock> COIL_HSSG = createCoilBlock(CoilBlock.CoilType.HSSG,
            text -> text.source("HSS-G Coil Block")
                    .translation(MinecraftLocale.ZH_CN, "高速钢-G线圈方块"));
    public static final BlockEntry<CoilBlock> COIL_NAQUADAH = createCoilBlock(CoilBlock.CoilType.NAQUADAH,
            text -> text.source("Naquadah Coil Block")
                    .translation(MinecraftLocale.ZH_CN, "硅岩线圈方块"));
    public static final BlockEntry<CoilBlock> COIL_TRINIUM = createCoilBlock(CoilBlock.CoilType.TRINIUM,
            text -> text.source("Trinium Coil Block")
                    .translation(MinecraftLocale.ZH_CN, "凯金线圈方块"));
    public static final BlockEntry<CoilBlock> COIL_TRITANIUM = createCoilBlock(CoilBlock.CoilType.TRITANIUM,
            text -> text.source("Tritanium Coil Block")
                    .translation(MinecraftLocale.ZH_CN, "三钛线圈方块"));

    // PSS batteries
    public static final BlockEntry<BatteryBlock> BATTERY_EMPTY_TIER_I = createBatteryBlock(BatteryBlock.BatteryPartType.EMPTY_TIER_I,
            text -> text.source("Empty Tier I Capacitor")
                    .translation(MinecraftLocale.ZH_CN, "空电容（Tier I）"));
    public static final BlockEntry<BatteryBlock> BATTERY_LAPOTRONIC_EV = createBatteryBlock(BatteryBlock.BatteryPartType.EV_LAPOTRONIC,
            text -> text.source("EV Lapotronic Capacitor")
                    .translation(MinecraftLocale.ZH_CN, "兰波顿电容（§5EV§r）"));
    public static final BlockEntry<BatteryBlock> BATTERY_LAPOTRONIC_IV = createBatteryBlock(BatteryBlock.BatteryPartType.IV_LAPOTRONIC,
            text -> text.source("IV Lapotronic Capacitor")
                    .translation(MinecraftLocale.ZH_CN, "兰波顿电容（§1IV§r）"));
    public static final BlockEntry<BatteryBlock> BATTERY_EMPTY_TIER_II = createBatteryBlock(BatteryBlock.BatteryPartType.EMPTY_TIER_II,
            text -> text.source("Empty Tier II Capacitor")
                    .translation(MinecraftLocale.ZH_CN, "空电容（Tier II）"));
    public static final BlockEntry<BatteryBlock> BATTERY_LAPOTRONIC_LuV = createBatteryBlock(BatteryBlock.BatteryPartType.LuV_LAPOTRONIC,
            text -> text.source("LuV Lapotronic Capacitor")
                    .translation(MinecraftLocale.ZH_CN, "兰波顿电容（§dLuV§r）"));
    public static final BlockEntry<BatteryBlock> BATTERY_LAPOTRONIC_ZPM = createBatteryBlock(BatteryBlock.BatteryPartType.ZPM_LAPOTRONIC,
            text -> text.source("ZPM Lapotronic Capacitor")
                    .translation(MinecraftLocale.ZH_CN, "兰波顿电容（§cZPM§r）"));
    public static final BlockEntry<BatteryBlock> BATTERY_EMPTY_TIER_III = createBatteryBlock(BatteryBlock.BatteryPartType.EMPTY_TIER_III,
            text -> text.source("Empty Tier III Capacitor")
                    .translation(MinecraftLocale.ZH_CN, "空电容（Tier III）"));
    public static final BlockEntry<BatteryBlock> BATTERY_LAPOTRONIC_UV = createBatteryBlock(BatteryBlock.BatteryPartType.UV_LAPOTRONIC,
            text -> text.source("UV Lapotronic Capacitor")
                    .translation(MinecraftLocale.ZH_CN, "兰波顿电容（§3UV§r）"));
    public static final BlockEntry<BatteryBlock> BATTERY_ULTIMATE_UHV = createBatteryBlock(BatteryBlock.BatteryPartType.UHV_ULTIMATE,
            text -> text.source("UHV Ultimate Capacitor")
                    .translation(MinecraftLocale.ZH_CN, "终极电容（§4UHV§r）"));

    // Intake casing
    public static final BlockEntry<ActiveBlock> CASING_ENGINE_INTAKE = createActiveCasing("engine_intake_casing", "block/variant/engine_intake",
            text -> text.source("Engine Intake Casing")
                    .translation(MinecraftLocale.ZH_CN, "引擎进气机械方块"));
    public static final BlockEntry<ActiveBlock> CASING_EXTREME_ENGINE_INTAKE = createActiveCasing("extreme_engine_intake_casing", "block/variant/extreme_engine_intake",
            text -> text.source("Extreme Engine Intake Casing")
                    .translation(MinecraftLocale.ZH_CN, "极限引擎进气机械方块"));

    // Fusion
    public static final Map<IFusionCasingType, Supplier<FusionCasingBlock>> ALL_FUSION_CASINGS = new HashMap<>();
    public static final BlockEntry<FusionCasingBlock> SUPERCONDUCTING_COIL = createFusionCasing(FusionCasingBlock.CasingType.SUPERCONDUCTING_COIL,
            text -> text.source("Superconducting Coil Block")
                    .translation(MinecraftLocale.ZH_CN, "超导线圈方块"));
    public static final BlockEntry<FusionCasingBlock> FUSION_COIL = createFusionCasing(FusionCasingBlock.CasingType.FUSION_COIL,
            text -> text.source("Fusion Coil Block")
                    .translation(MinecraftLocale.ZH_CN, "聚变线圈方块"));
    public static final BlockEntry<FusionCasingBlock> FUSION_CASING = createFusionCasing(FusionCasingBlock.CasingType.FUSION_CASING,
            text -> text.source("Fusion Machine Casing")
                    .translation(MinecraftLocale.ZH_CN, "聚变机械方块"));
    public static final BlockEntry<FusionCasingBlock> FUSION_CASING_MK2 = createFusionCasing(FusionCasingBlock.CasingType.FUSION_CASING_MK2,
            text -> text.source("Fusion Machine Casing MK II")
                    .translation(MinecraftLocale.ZH_CN, "聚变机械方块 MK-II"));
    public static final BlockEntry<FusionCasingBlock> FUSION_CASING_MK3 = createFusionCasing(FusionCasingBlock.CasingType.FUSION_CASING_MK3,
            text -> text.source("Fusion Machine Casing MK III")
                    .translation(MinecraftLocale.ZH_CN, "聚变机械方块 MK-III"));
    public static final BlockEntry<TransparentBlock> FUSION_GLASS = createGlassCasingBlock("fusion_glass",
            GTPM.id("block/casings/transparent/fusion_glass"), () -> RenderType::cutoutMipped,
            text -> text.source("Fusion Glass")
                    .translation(MinecraftLocale.ZH_CN, "聚变玻璃"));

    // Cleanroom
    public static final BlockEntry<Block> PLASTCRETE = createCasingBlock("plascrete", GTPM.id("block/casings/cleanroom/plascrete"),
            text -> text.source("Plascrete")
                    .translation(MinecraftLocale.ZH_CN, "塑料混凝土"));
    @Trans(key = "block.gtpm.filter_casing.tooltip",
           source = "Creates a §aParticle-Free§7 environment",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "创建一个§a无尘§7环境"))
    public static final BlockEntry<Block> FILTER_CASING = createCleanroomFilter(CleanroomFilterType.FILTER_CASING,
            text -> text.source("Filter Casing")
                    .translation(MinecraftLocale.ZH_CN, "过滤器机械方块"));
    @Trans(key = "block.gtpm.sterilizing_filter_casing.tooltip",
           source = "Creates a §aSterilized§7 environment",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "创造一个§a无菌§7环境"))
    public static final BlockEntry<Block> FILTER_CASING_STERILE = createCleanroomFilter(CleanroomFilterType.FILTER_CASING_STERILE,
            text -> text.source("Sterilizing Filter Casing")
                    .translation(MinecraftLocale.ZH_CN, "除菌过滤器机械方块"));
    public static final BlockEntry<TransparentBlock> CLEANROOM_GLASS = createGlassCasingBlock("cleanroom_glass", GTPM.id("block/casings/transparent/cleanroom_glass"), () -> RenderType::cutoutMipped,
            text -> text.source("Cleanroom Glass")
                    .translation(MinecraftLocale.ZH_CN, "超净间玻璃"));

    // Fireboxes
    public static final Map<BoilerFireboxType, BlockEntry<ActiveBlock>> ALL_FIREBOXES = new HashMap<>();
    public static final BlockEntry<ActiveBlock> FIREBOX_BRONZE = createFireboxCasing(BoilerFireboxType.BRONZE_FIREBOX,
            text -> text.source("Bronze Firebox Casing")
                    .translation(MinecraftLocale.ZH_CN, "青铜燃烧室"));
    public static final BlockEntry<ActiveBlock> FIREBOX_STEEL = createFireboxCasing(BoilerFireboxType.STEEL_FIREBOX,
            text -> text.source("Steel Firebox Casing")
                    .translation(MinecraftLocale.ZH_CN, "钢制燃烧室"));
    public static final BlockEntry<ActiveBlock> FIREBOX_TITANIUM = createFireboxCasing(BoilerFireboxType.TITANIUM_FIREBOX,
            text -> text.source("Titanium Firebox Casing")
                    .translation(MinecraftLocale.ZH_CN, "钛燃烧室"));
    public static final BlockEntry<ActiveBlock> FIREBOX_TUNGSTENSTEEL = createFireboxCasing(BoilerFireboxType.TUNGSTENSTEEL_FIREBOX,
            text -> text.source("Tungstensteel Firebox Casing")
                    .translation(MinecraftLocale.ZH_CN, "钨钢燃烧室"));

    // spotless:on

    // HPCA, AT
    public static final BlockEntry<Block> COMPUTER_CASING = REGISTRATE
            .block("computer_casing", Block::new)
            .localization(text -> text
                    .source("Computer Casing")
                    .translation(MinecraftLocale.ZH_CN, "计算机外壳"))
            .onRegister(block -> COMPUTER_CASINGS.put(1, block))
            .initialProperties(() -> Blocks.IRON_BLOCK)
            .properties(p -> p.isValidSpawn((state, level, pos, ent) -> false))
            .blockstate(() -> (ctx, prov) -> {
                prov.simpleBlock(ctx.getEntry(), prov.models().getExistingFile(GTPM.id("block/computer_casing")));
            })
            .tag(CustomTags.MINEABLE_WITH_CONFIG_VALID_PICKAXE_WRENCH)
            .item(BlockItem::new)
            .build()
            .register();
    public static final BlockEntry<Block> ADVANCED_COMPUTER_CASING = REGISTRATE
            .block("advanced_computer_casing", Block::new)
            .localization(text -> text
                    .source("Advanced Computer Casing")
                    .translation(MinecraftLocale.ZH_CN, "高级计算机外壳"))
            .onRegister(block -> COMPUTER_CASINGS.put(2, block))
            .initialProperties(() -> Blocks.IRON_BLOCK)
            .properties(p -> p.isValidSpawn((state, level, pos, ent) -> false))
            .blockstate(() -> (ctx, prov) -> {
                prov.simpleBlock(ctx.getEntry(),
                        prov.models().getExistingFile(GTPM.id("block/advanced_computer_casing")));
            })
            .tag(CustomTags.MINEABLE_WITH_CONFIG_VALID_PICKAXE_WRENCH)
            .item(BlockItem::new)
            .build()
            .register();
    public static final BlockEntry<Block> COMPUTER_HEAT_VENT = REGISTRATE
            .block("computer_heat_vent", Block::new)
            .localization(text -> text
                    .source("Computer Heat Vent")
                    .translation(MinecraftLocale.ZH_CN, "计算机散热口"))
            .onRegister(block -> COMPUTER_HEAT_VENTS.put(1, block))
            .initialProperties(() -> Blocks.IRON_BLOCK)
            .properties(p -> p.isValidSpawn((state, level, pos, ent) -> false))
            .blockstate(() -> (ctx, prov) -> {
                prov.simpleBlock(ctx.getEntry(), prov.models().cubeColumn("computer_heat_vent",
                        GTPM.id("block/casings/hpca/computer_heat_vent_side"),
                        GTPM.id("block/casings/hpca/computer_heat_vent_top_bot")));
            })
            .tag(CustomTags.MINEABLE_WITH_CONFIG_VALID_PICKAXE_WRENCH)
            .item(BlockItem::new)
            .build()
            .register();
    public static final BlockEntry<Block> HIGH_POWER_CASING = createCasingBlock("high_power_casing",
            GTPM.id("block/casings/hpca/high_power_casing"),
            text -> text.source("High Power Casing")
                    .translation(MinecraftLocale.ZH_CN, "高能机械方块"));

    @SuppressWarnings("SameParameterValue")
    private static BlockEntry<Block> createSidedCasingBlock(String name, ResourceLocation texture,
                                                            Consumer<LocalizationDeclaration.Builder> localization) {
        return REGISTRATE.block(name, Block::new)
                .localization(localization)
                .initialProperties(() -> Blocks.IRON_BLOCK)
                .properties(p -> p.isValidSpawn((state, level, pos, ent) -> false))
                .blockstate(() -> GTModels.createSidedCasingModel(texture))
                .tag(CustomTags.MINEABLE_WITH_CONFIG_VALID_PICKAXE_WRENCH)
                .item(BlockItem::new)
                .build()
                .register();
    }

    private static BlockEntry<Block> createBrickCasingBlock(String name, ResourceLocation texture,
                                                            Consumer<LocalizationDeclaration.Builder> localization) {
        return REGISTRATE.block(name, Block::new)
                .localization(localization)
                .initialProperties(() -> Blocks.IRON_BLOCK)
                .properties(p -> p.isValidSpawn((state, level, pos, ent) -> false))
                .exBlockstate(GTModels.cubeAllModel(texture))
                .tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .item(BlockItem::new)
                .build()
                .register();
    }

    private static BlockEntry<TransparentBlock> createGlassCasingBlock(String name, ResourceLocation texture,
                                                                       Supplier<Supplier<RenderType>> type,
                                                                       Consumer<LocalizationDeclaration.Builder> localization) {
        return REGISTRATE.block(name, TransparentBlock::new)
                .localization(localization)
                .initialProperties(() -> Blocks.GLASS)
                .properties(p -> p.isValidSpawn((state, level, pos, ent) -> false))
                .addLayer(type)
                .exBlockstate(GTModels.cubeAllModel(texture))
                .onRegister(GLASS::add)
                .tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .item(BlockItem::new)
                .build()
                .register();
    }

    public static BlockEntry<Block> createCasingBlock(String name, ResourceLocation texture) {
        return createCasingBlock(name, Block::new, texture, () -> Blocks.IRON_BLOCK,
                () -> RenderType::solid);
    }

    static BlockEntry<Block> createCasingBlock(String name, ResourceLocation texture,
                                               Consumer<LocalizationDeclaration.Builder> localization) {
        return createCasingBlock(name, Block::new, texture, () -> Blocks.IRON_BLOCK, () -> RenderType::solid,
                localization);
    }

    public static BlockEntry<Block> createCasingBlock(String name,
                                                      NonNullFunction<BlockBehaviour.Properties, Block> blockSupplier,
                                                      ResourceLocation texture,
                                                      NonNullSupplier<? extends Block> properties,
                                                      Supplier<Supplier<RenderType>> type) {
        return createCasingBlock(name, blockSupplier, texture, properties, type, null);
    }

    private static BlockEntry<Block> createCasingBlock(String name,
                                                       NonNullFunction<BlockBehaviour.Properties, Block> blockSupplier,
                                                       ResourceLocation texture,
                                                       NonNullSupplier<? extends Block> properties,
                                                       Supplier<Supplier<RenderType>> type,
                                                       @Nullable Consumer<LocalizationDeclaration.Builder> localization) {
        var builder = REGISTRATE.block(name, blockSupplier);
        if (localization != null) builder.localization(localization);
        return builder
                .initialProperties(properties)
                .properties(p -> p.isValidSpawn((state, level, pos, ent) -> false))
                .addLayer(type)
                .exBlockstate(GTModels.cubeAllModel(texture))
                .tag(CustomTags.MINEABLE_WITH_CONFIG_VALID_PICKAXE_WRENCH)
                .item(BlockItem::new)
                .build()
                .register();
    }

    private static BlockEntry<Block> createMachineCasingBlock(int tier) {
        String tierName = GTValues.VN[tier].toLowerCase(Locale.ROOT);
        var entry = REGISTRATE
                .block("%s_machine_casing".formatted(tierName), Block::new)
                .onRegister(block -> MACHINE_CASINGS.put(tier, block))
                .localization(text -> text
                        .source("%s Machine Casing".formatted(GTValues.VN[tier]))
                        .translation(MinecraftLocale.ZH_CN, GTValues.VNF[tier] + "§r机械方块"))
                .initialProperties(() -> Blocks.IRON_BLOCK)
                .properties(p -> p.isValidSpawn((state, level, pos, ent) -> false))
                .blockstate(() -> GTModels.createMachineCasingModel(tierName))
                .tag(CustomTags.MINEABLE_WITH_CONFIG_VALID_PICKAXE_WRENCH)
                .item(BlockItem::new)
                .build()
                .register();
        if (!GTCEuAPI.isHighTier() && tier > GTValues.UHV) {
            REGISTRATE.setCreativeTab(entry, null);
        }
        return entry;
    }

    private static BlockEntry<Block> createHermeticCasing(int tier) {
        String tierName = GTValues.VN[tier].toLowerCase(Locale.ROOT);
        var entry = REGISTRATE
                .block("%s_hermetic_casing".formatted(tierName), Block::new)
                .addLayer(() -> RenderType::cutoutMipped)
                .onRegister(block -> HERMETIC_CASINGS.put(tier, block))
                .localization(text -> text
                        .source("Hermetic Casing %s".formatted(GTValues.LVT[tier]))
                        .translation(MinecraftLocale.ZH_CN, "密封机械方块 " + GTValues.LVT[tier]))
                .initialProperties(() -> Blocks.IRON_BLOCK)
                .properties(p -> p.isValidSpawn((state, level, pos, ent) -> false))
                .blockstate(() -> GTModels.createHermeticCasingModel(tierName))
                .tag(CustomTags.MINEABLE_WITH_CONFIG_VALID_PICKAXE_WRENCH)
                .item(BlockItem::new)
                .build()
                .register();
        if (!GTCEuAPI.isHighTier() && tier > GTValues.UHV) {
            REGISTRATE.setCreativeTab(entry, null);
        }
        return entry;
    }

    private static BlockEntry<Block> createSteamCasing(String name, String material,
                                                       Consumer<LocalizationDeclaration.Builder> localization) {
        return REGISTRATE.block(name, Block::new)
                .localization(localization)
                .initialProperties(() -> Blocks.IRON_BLOCK)
                .blockstate(() -> GTModels.createSteamCasingModel(material))
                .tag(CustomTags.MINEABLE_WITH_CONFIG_VALID_PICKAXE_WRENCH)
                .item(BlockItem::new)
                .build()
                .register();
    }

    private static BlockEntry<CoilBlock> createCoilBlock(ICoilType coilType,
                                                         Consumer<LocalizationDeclaration.Builder> localization) {
        var coilBlock = REGISTRATE
                .block("%s_coil_block".formatted(coilType.getName()), p -> new CoilBlock(p, coilType))
                .localization(localization)
                .initialProperties(() -> Blocks.IRON_BLOCK)
                .properties(p -> p.isValidSpawn((state, level, pos, ent) -> false))
                .addLayer(() -> RenderType::cutoutMipped)
                .blockstate(() -> GTModels.createCoilModel(coilType))
                .tag(CustomTags.MINEABLE_WITH_CONFIG_VALID_PICKAXE_WRENCH)
                .item(BlockItem::new)
                .build()
                .register();
        GTCEuAPI.HEATING_COILS.put(coilType, coilBlock);
        return coilBlock;
    }

    private static BlockEntry<BatteryBlock> createBatteryBlock(IBatteryData batteryData,
                                                               Consumer<LocalizationDeclaration.Builder> localization) {
        var batteryBlock = REGISTRATE.block("%s_battery".formatted(batteryData.getBatteryName()),
                p -> new BatteryBlock(p, batteryData))
                .localization(localization)
                .initialProperties(() -> Blocks.IRON_BLOCK)
                .properties(p -> p.isValidSpawn((state, level, pos, entityType) -> false))
                .blockstate(() -> GTModels.createBatteryBlockModel(batteryData))
                .tag(CustomTags.MINEABLE_WITH_CONFIG_VALID_PICKAXE_WRENCH)
                .item(BlockItem::new)
                .build()
                .register();

        GTCEuAPI.PSS_BATTERIES.put(batteryData, batteryBlock);
        return batteryBlock;
    }

    private static BlockEntry<FusionCasingBlock> createFusionCasing(IFusionCasingType casingType,
                                                                    Consumer<LocalizationDeclaration.Builder> localization) {
        var casingBlock = REGISTRATE
                .block(casingType.getSerializedName(), p -> new FusionCasingBlock(p, casingType))
                .localization(localization)
                .initialProperties(() -> Blocks.IRON_BLOCK)
                .properties(properties -> properties.strength(5.0f, 10.0f).sound(SoundType.METAL))
                .addLayer(() -> RenderType::cutoutMipped)
                .blockstate(() -> GTModels.createFusionCasingModel(casingType))
                .tag(CustomTags.MINEABLE_WITH_CONFIG_VALID_PICKAXE_WRENCH,
                        CustomTags.TOOL_TIERS[casingType.getHarvestLevel()])
                .item(BlockItem::new)
                .build()
                .register();
        ALL_FUSION_CASINGS.put(casingType, casingBlock);
        return casingBlock;
    }

    private static BlockEntry<Block> createCleanroomFilter(IFilterType filterType,
                                                           Consumer<LocalizationDeclaration.Builder> localization) {
        var filterBlock = REGISTRATE.block(filterType.getSerializedName(), Block::new)
                .localization(localization)
                .initialProperties(() -> Blocks.IRON_BLOCK)
                .properties(properties -> properties.strength(2.0f, 8.0f).sound(SoundType.METAL)
                        .isValidSpawn((blockState, blockGetter, blockPos, entityType) -> false))
                .blockstate(() -> GTModels.createCleanroomFilterModel(filterType))
                .tag(CustomTags.MINEABLE_WITH_CONFIG_VALID_PICKAXE_WRENCH, CustomTags.TOOL_TIERS[1])
                .item(BlockItem::new)
                .build()
                .register();
        GTCEuAPI.CLEANROOM_FILTERS.put(filterType, filterBlock);
        return filterBlock;
    }

    protected static BlockEntry<ActiveBlock> createActiveCasing(String name, String baseModelPath) {
        return createActiveCasing(name, baseModelPath, null);
    }

    static BlockEntry<ActiveBlock> createActiveCasing(String name, String baseModelPath,
                                                      @Nullable Consumer<LocalizationDeclaration.Builder> localization) {
        var builder = REGISTRATE.block(name, ActiveBlock::new);
        if (localization != null) builder.localization(localization);
        return builder
                .initialProperties(() -> Blocks.IRON_BLOCK)
                .addLayer(() -> RenderType::cutoutMipped)
                .blockstate(() -> GTModels.createActiveModel(GTPM.id(baseModelPath)))
                .tag(CustomTags.MINEABLE_WITH_CONFIG_VALID_PICKAXE_WRENCH)
                .item(BlockItem::new)
                .model(() -> (ctx, prov) -> prov.withExistingParent(prov.name(ctx), GTPM.id(baseModelPath)))
                .build()
                .register();
    }

    private static BlockEntry<ActiveBlock> createFireboxCasing(BoilerFireboxType type,
                                                               Consumer<LocalizationDeclaration.Builder> localization) {
        var block = REGISTRATE
                .block("%s_casing".formatted(type.name()), ActiveBlock::new)
                .localization(localization)
                .initialProperties(() -> Blocks.IRON_BLOCK)
                .properties(p -> p.isValidSpawn((state, level, pos, ent) -> false))
                .addLayer(() -> RenderType::cutoutMipped)
                .blockstate(() -> GTModels.createFireboxModel(type))
                .tag(CustomTags.MINEABLE_WITH_CONFIG_VALID_PICKAXE_WRENCH)
                .item(BlockItem::new)
                .build()
                .register();
        ALL_FIREBOXES.put(type, block);
        return block;
    }

    //////////////////////////////////////
    // ********** Misc **********//
    //////////////////////////////////////

    public static final BlockEntry<PowderbarrelBlock> POWDERBARREL = REGISTRATE
            .block("powderbarrel", PowderbarrelBlock::new)
            .localization(text -> text
                    .source("Powderbarrel")
                    .translation(MinecraftLocale.ZH_CN, "火药桶"))
            .properties(p -> p.destroyTime(0.5F).sound(SoundType.WOOD).mapColor(MapColor.STONE)
                    .pushReaction(PushReaction.BLOCK))
            .tag(BlockTags.MINEABLE_WITH_AXE)
            .simpleItem()
            .register();

    public static final BlockEntry<IndustrialTNTBlock> INDUSTRIAL_TNT = REGISTRATE
            .block("industrial_tnt", IndustrialTNTBlock::new)
            .localization(text -> text
                    .source("Industrial TNT")
                    .translation(MinecraftLocale.ZH_CN, "工业TNT"))
            .properties(p -> p.mapColor(MapColor.FIRE).instabreak().sound(SoundType.GRASS).ignitedByLava())
            .tag(BlockTags.MINEABLE_WITH_AXE)
            .blockstate(() -> (ctx, prov) -> prov.simpleBlock(ctx.get(), prov.models().cubeBottomTop(ctx.getName(),
                    prov.blockTexture(ctx.get()).withSuffix("_side"),
                    ResourceLocation.withDefaultNamespace("block/tnt_bottom"),
                    ResourceLocation.withDefaultNamespace("block/tnt_top"))))
            .simpleItem()
            .register();

    public static final BlockEntry<SaplingBlock> RUBBER_SAPLING = REGISTRATE
            .block("rubber_sapling", GTBlocks::makeSaplingBlock)
            .initialProperties(() -> Blocks.OAK_SAPLING)
            .localization(text -> text
                    .source("Rubber Sapling")
                    .translation(MinecraftLocale.ZH_CN, "橡胶树苗"))
            .blockstate(() -> GTModels::createCrossBlockState)
            .addLayer(() -> RenderType::cutout)
            .tag(BlockTags.SAPLINGS)
            .item()
            .model(() -> GTModels::rubberTreeSaplingModel)
            .dataMap(NeoForgeDataMaps.COMPOSTABLES, new Compostable(0.3f))
            .tag(ItemTags.SAPLINGS)
            .build()
            .register();

    private static SaplingBlock makeSaplingBlock(BlockBehaviour.Properties properties) {
        TreeGrower treeGrower = new TreeGrower(
                GTPM.id("rubber").toString(),
                Optional.empty(),
                Optional.of(GTConfiguredFeatures.RUBBER_TREE),
                Optional.empty());
        return new SaplingBlock(treeGrower, properties);
    }

    public static final BlockEntry<RubberLogBlock> RUBBER_LOG = REGISTRATE.block("rubber_log", RubberLogBlock::new)
            .properties(p -> p.strength(2.0F).sound(SoundType.WOOD))
            .loot((table, block) -> table.add(block, LootTable.lootTable()
                    .withPool(table
                            .applyExplosionCondition(block, LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)))
                            .add(LootItem.lootTableItem(block)))
                    .withPool(table
                            .applyExplosionCondition(block, LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)))
                            .add(LootItem.lootTableItem(GTItems.STICKY_RESIN.get())
                                    .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
                                            .setProperties(StatePropertiesPredicate.Builder.properties()
                                                    .hasProperty(RubberLogBlock.NATURAL, true)))
                                    .when(LootItemRandomChanceCondition.randomChance(0.85F))))))
            .localization(text -> text
                    .source("Rubber Log")
                    .translation(MinecraftLocale.ZH_CN, "橡胶原木"))
            .tag(BlockTags.LOGS_THAT_BURN, BlockTags.OVERWORLD_NATURAL_LOGS)
            .blockstate(() -> (ctx, provider) -> provider.logBlock(ctx.get()))
            .item()
            .tag(ItemTags.LOGS_THAT_BURN, CustomTags.RUBBER_LOGS)
            .build()
            .register();

    // Fortune Level
    public static final float[] RUBBER_LEAVES_DROPPING_CHANCE = new float[] { 0.05F, 0.0625F, 0.083333336F, 0.1F };

    @OnlyIn(Dist.CLIENT)
    public static BlockColor leavesBlockColor() {
        return (state, reader, pos, tintIndex) -> {
            if (reader != null && pos != null) {
                // return reader.getBlockTint(pos, (biome, x, z) -> biome.getFoliageColor());
                return BiomeColors.getAverageFoliageColor(reader, pos);
            }
            return FoliageColor.getDefaultColor();
        };
    }

    @OnlyIn(Dist.CLIENT)
    public static ItemColor leavesItemColor() {
        return (stack, tintIndex) -> FoliageColor.getDefaultColor();
    }

    public static final BlockEntry<LeavesBlock> RUBBER_LEAVES = REGISTRATE
            .block("rubber_leaves", LeavesBlock::new)
            .initialProperties(() -> Blocks.OAK_LEAVES)
            .localization(text -> text
                    .source("Rubber Leaves")
                    .translation(MinecraftLocale.ZH_CN, "橡胶树叶"))
            .blockstate(() -> (ctx, prov) -> {
                prov.simpleBlock(ctx.get(), prov.models().leaves(ctx.getName(), prov.blockTexture(ctx.get())));
            })
            .loot((table, block) -> table.add(block,
                    table.createLeavesDrops(block, GTBlocks.RUBBER_SAPLING.get(), RUBBER_LEAVES_DROPPING_CHANCE)))
            .tag(BlockTags.LEAVES, BlockTags.MINEABLE_WITH_HOE)
            .color(() -> GTBlocks::leavesBlockColor)
            .item()
            .color(() -> GTBlocks::leavesItemColor)
            .dataMap(NeoForgeDataMaps.COMPOSTABLES, new Compostable(0.3f))
            .tag(ItemTags.LEAVES)
            .build()
            .register();

    public static final BlockSetType RUBBER_SET = BlockSetType
            .register(new BlockSetType(GTPM.id("rubber").toString()));
    public static final WoodType RUBBER_TYPE = WoodType
            .register(new WoodType(GTPM.id("rubber").toString(), RUBBER_SET));

    public static final BlockEntry<RotatedPillarBlock> STRIPPED_RUBBER_LOG = REGISTRATE
            .block("stripped_rubber_log", RotatedPillarBlock::new)
            .initialProperties(() -> Blocks.STRIPPED_SPRUCE_LOG)
            .localization(text -> text
                    .source("Stripped Rubber Log")
                    .translation(MinecraftLocale.ZH_CN, "去皮橡胶原木"))
            .blockstate(() -> (ctx, provider) -> provider.logBlock(ctx.get()))
            .tag(BlockTags.LOGS_THAT_BURN, BlockTags.MINEABLE_WITH_AXE)
            .item()
            .tag(ItemTags.LOGS_THAT_BURN, CustomTags.RUBBER_LOGS)
            .build()
            .register();
    public static final BlockEntry<RubberWoodBlock> RUBBER_WOOD = REGISTRATE
            .block("rubber_wood", RubberWoodBlock::new)
            .initialProperties(() -> Blocks.SPRUCE_WOOD)
            .localization(text -> text
                    .source("Rubber Wood")
                    .translation(MinecraftLocale.ZH_CN, "橡胶木"))
            .blockstate(() -> (ctx, provider) -> provider.axisBlock(ctx.get(),
                    provider.blockTexture(GTBlocks.RUBBER_LOG.get()), provider.blockTexture(GTBlocks.RUBBER_LOG.get())))
            .tag(BlockTags.LOGS_THAT_BURN, BlockTags.MINEABLE_WITH_AXE)
            .item()
            .tag(ItemTags.LOGS_THAT_BURN, CustomTags.RUBBER_LOGS)
            .build()
            .register();
    public static final BlockEntry<RotatedPillarBlock> STRIPPED_RUBBER_WOOD = REGISTRATE
            .block("stripped_rubber_wood", RotatedPillarBlock::new)
            .initialProperties(() -> Blocks.STRIPPED_SPRUCE_WOOD)
            .localization(text -> text
                    .source("Stripped Rubber Wood")
                    .translation(MinecraftLocale.ZH_CN, "去皮橡胶木"))
            .blockstate(() -> (ctx, provider) -> provider.axisBlock(ctx.get(), provider.blockTexture(ctx.get()),
                    provider.blockTexture(ctx.get())))
            .tag(BlockTags.LOGS_THAT_BURN, BlockTags.MINEABLE_WITH_AXE)
            .item()
            .tag(ItemTags.LOGS_THAT_BURN, CustomTags.RUBBER_LOGS)
            .build()
            .register();

    public static final BlockEntry<Block> RUBBER_PLANK = REGISTRATE
            .block("rubber_planks", Block::new)
            .initialProperties(() -> Blocks.SPRUCE_PLANKS)
            .localization(text -> text
                    .source("Rubber Planks")
                    .translation(MinecraftLocale.ZH_CN, "橡胶木板"))
            .tag(BlockTags.PLANKS, BlockTags.MINEABLE_WITH_AXE)
            .item()
            .tag(ItemTags.PLANKS)
            .build()
            .register();

    public static final BlockEntry<SlabBlock> RUBBER_SLAB = REGISTRATE
            .block("rubber_slab", SlabBlock::new)
            .initialProperties(() -> Blocks.SPRUCE_SLAB)
            .localization(text -> text
                    .source("Rubber Slab")
                    .translation(MinecraftLocale.ZH_CN, "橡胶木台阶"))
            .blockstate(
                    () -> (ctx, prov) -> prov.slabBlock(ctx.getEntry(), prov.blockTexture(GTBlocks.RUBBER_PLANK.get()),
                            prov.blockTexture(GTBlocks.RUBBER_PLANK.get())))
            .tag(BlockTags.WOODEN_SLABS, BlockTags.MINEABLE_WITH_AXE)
            .item()
            .tag(ItemTags.WOODEN_SLABS)
            .build()
            .register();

    public static final BlockEntry<FenceBlock> RUBBER_FENCE = REGISTRATE
            .block("rubber_fence", FenceBlock::new)
            .initialProperties(() -> Blocks.SPRUCE_FENCE)
            .localization(text -> text
                    .source("Rubber Fence")
                    .translation(MinecraftLocale.ZH_CN, "橡胶木栅栏"))
            .blockstate(() -> (ctx, prov) -> prov.fenceBlock(ctx.getEntry(), prov.blockTexture(RUBBER_PLANK.get())))
            .tag(BlockTags.WOODEN_FENCES, BlockTags.MINEABLE_WITH_AXE)
            .item()
            .model(() -> (ctx, prov) -> prov.fenceInventory(ctx.getName(),
                    GTBlocks.RUBBER_PLANK.getId().withPrefix("block/")))
            .tag(ItemTags.WOODEN_FENCES)
            .build()
            .register();

    public static final BlockEntry<StandingSignBlock> RUBBER_SIGN = REGISTRATE
            .block("rubber_sign", (p) -> new StandingSignBlock(RUBBER_TYPE, p))
            .initialProperties(() -> Blocks.SPRUCE_SIGN)
            .blockstate(() -> (ctx, prov) -> prov.signBlock(ctx.get(), GTBlocks.RUBBER_WALL_SIGN.get(),
                    prov.blockTexture(GTBlocks.RUBBER_PLANK.get())))
            .tag(BlockTags.STANDING_SIGNS, BlockTags.MINEABLE_WITH_AXE)
            .setData(ProviderType.LANG, NonNullBiConsumer.noop())
            .item((b, p) -> new SignItem(p, b, GTBlocks.RUBBER_WALL_SIGN.get()))
            .defaultModel()
            .tag(ItemTags.SIGNS)
            .build()
            .register();

    public static final BlockEntry<WallSignBlock> RUBBER_WALL_SIGN = REGISTRATE
            .block("rubber_wall_sign", (p) -> new WallSignBlock(RUBBER_TYPE, p))
            .initialProperties(() -> Blocks.SPRUCE_WALL_SIGN)
            .localization(text -> text
                    .source("Rubber Wall Sign")
                    .translation(MinecraftLocale.ZH_CN, "墙上的橡胶木告示牌"))
            .setData(ProviderType.BLOCKSTATE, NonNullBiConsumer.noop())
            .tag(BlockTags.WALL_SIGNS, BlockTags.MINEABLE_WITH_AXE)
            .loot((table, block) -> table.dropOther(block, RUBBER_SIGN.asItem()))
            .register();

    public static final BlockEntry<CeilingHangingSignBlock> RUBBER_HANGING_SIGN = REGISTRATE
            .block("rubber_hanging_sign", (p) -> new CeilingHangingSignBlock(RUBBER_TYPE, p))
            .initialProperties(() -> Blocks.SPRUCE_HANGING_SIGN)
            .blockstate(() -> (ctx, prov) -> {
                ModelFile model = prov.models().sign(ctx.getName(), prov.blockTexture(GTBlocks.RUBBER_PLANK.get()));
                prov.simpleBlock(ctx.get(), model);
            })
            .tag(BlockTags.CEILING_HANGING_SIGNS, BlockTags.MINEABLE_WITH_AXE)
            .setData(ProviderType.LANG, NonNullBiConsumer.noop())
            .item((b, p) -> new HangingSignItem(b, GTBlocks.RUBBER_WALL_HANGING_SIGN.get(), p))
            .defaultModel()
            .tag(ItemTags.HANGING_SIGNS)
            .setData(ProviderType.LANG, NonNullBiConsumer.noop())
            .build()
            .register();

    public static final BlockEntry<WallHangingSignBlock> RUBBER_WALL_HANGING_SIGN = REGISTRATE
            .block("rubber_wall_hanging_sign", (p) -> new WallHangingSignBlock(RUBBER_TYPE, p))
            .initialProperties(() -> Blocks.SPRUCE_WALL_HANGING_SIGN)
            .localization(text -> text
                    .source("Rubber Wall Hanging Sign")
                    .translation(MinecraftLocale.ZH_CN, "墙上的悬挂式橡胶木告示牌"))
            .blockstate(() -> (ctx, prov) -> {
                ModelFile model = prov.models().sign(ctx.getName(), prov.blockTexture(GTBlocks.RUBBER_PLANK.get()));
                prov.simpleBlock(ctx.get(), model);
            })
            .tag(BlockTags.WALL_HANGING_SIGNS, BlockTags.MINEABLE_WITH_AXE)
            .loot((table, block) -> table.dropOther(block, RUBBER_HANGING_SIGN.asItem()))
            .register();

    public static final BlockEntry<PressurePlateBlock> RUBBER_PRESSURE_PLATE = REGISTRATE
            .block("rubber_pressure_plate",
                    (p) -> new PressurePlateBlock(RUBBER_SET, p))
            .initialProperties(() -> Blocks.SPRUCE_PRESSURE_PLATE)
            .localization(text -> text
                    .source("Rubber Pressure Plate")
                    .translation(MinecraftLocale.ZH_CN, "橡胶木压力板"))
            .tag(BlockTags.WOODEN_PRESSURE_PLATES, BlockTags.MINEABLE_WITH_AXE)
            .blockstate(() -> (ctx, prov) -> prov.pressurePlateBlock(ctx.getEntry(),
                    prov.blockTexture(GTBlocks.RUBBER_PLANK.get())))
            .item()
            .tag(ItemTags.WOODEN_PRESSURE_PLATES)
            .build()
            .register();
    public static final BlockEntry<TrapDoorBlock> RUBBER_TRAPDOOR = REGISTRATE
            .block("rubber_trapdoor", (p) -> new TrapDoorBlock(RUBBER_SET, p))
            .initialProperties(() -> Blocks.SPRUCE_TRAPDOOR)
            .localization(text -> text
                    .source("Rubber Trapdoor")
                    .translation(MinecraftLocale.ZH_CN, "橡胶木活板门"))
            .blockstate(() -> (ctx, prov) -> prov.trapdoorBlock(ctx.get(), prov.blockTexture(ctx.get()), true))
            .tag(BlockTags.WOODEN_TRAPDOORS, BlockTags.MINEABLE_WITH_AXE)
            .item()
            .model(() -> (ctx, prov) -> prov.trapdoorOrientableBottom(ctx.getName(), ctx.getId().withPrefix("block/")))
            .tag(ItemTags.WOODEN_TRAPDOORS)
            .build()
            .register();
    public static final BlockEntry<StairBlock> RUBBER_STAIRS = REGISTRATE
            .block("rubber_stairs", (p) -> new StairBlock(RUBBER_PLANK.getDefaultState(), p))
            .initialProperties(() -> Blocks.SPRUCE_STAIRS)
            .localization(text -> text
                    .source("Rubber Stairs")
                    .translation(MinecraftLocale.ZH_CN, "橡胶木楼梯"))
            .tag(BlockTags.STAIRS, BlockTags.MINEABLE_WITH_AXE)
            .blockstate(() -> (ctx, prov) -> prov.stairsBlock(ctx.getEntry(),
                    prov.blockTexture(GTBlocks.RUBBER_PLANK.get())))
            .item()
            .tag(ItemTags.STAIRS)
            .build()
            .register();
    public static final BlockEntry<ButtonBlock> RUBBER_BUTTON = REGISTRATE
            .block("rubber_button", (p) -> new ButtonBlock(RUBBER_SET, 30, p))
            .initialProperties(() -> Blocks.SPRUCE_BUTTON)
            .localization(text -> text
                    .source("Rubber Button")
                    .translation(MinecraftLocale.ZH_CN, "橡胶木按钮"))
            .tag(BlockTags.WOODEN_BUTTONS, BlockTags.MINEABLE_WITH_AXE)
            .blockstate(() -> (ctx, prov) -> prov.buttonBlock(ctx.getEntry(), prov.blockTexture(RUBBER_PLANK.get())))
            .item()
            .model(() -> (ctx, prov) -> prov.buttonInventory(ctx.getName(),
                    GTBlocks.RUBBER_PLANK.getId().withPrefix("block/")))
            .tag(ItemTags.WOODEN_BUTTONS)
            .build()
            .register();
    public static final BlockEntry<FenceGateBlock> RUBBER_FENCE_GATE = REGISTRATE
            .block("rubber_fence_gate", (p) -> new FenceGateBlock(RUBBER_TYPE, p))
            .initialProperties(() -> Blocks.SPRUCE_FENCE_GATE)
            .localization(text -> text
                    .source("Rubber Fence Gate")
                    .translation(MinecraftLocale.ZH_CN, "橡胶木栅栏门"))
            .tag(BlockTags.FENCE_GATES, BlockTags.MINEABLE_WITH_AXE)
            .blockstate(() -> (ctx, prov) -> prov.fenceGateBlock(ctx.getEntry(),
                    prov.blockTexture(GTBlocks.RUBBER_PLANK.get())))
            .item()
            .tag(ItemTags.FENCE_GATES)
            .build()
            .register();
    public static final BlockEntry<DoorBlock> RUBBER_DOOR = REGISTRATE
            .block("rubber_door", (p) -> new DoorBlock(RUBBER_SET, p))
            .initialProperties(() -> Blocks.SPRUCE_DOOR)
            .localization(text -> text
                    .source("Rubber Door")
                    .translation(MinecraftLocale.ZH_CN, "橡胶木门"))
            .loot((table, block) -> table.add(block, table.createDoorTable(block)))
            .addLayer(() -> RenderType::cutout)
            .blockstate(() -> (ctx, prov) -> prov.doorBlock(ctx.getEntry(), GTPM.id("block/rubber_door_bottom"),
                    GTPM.id("block/rubber_door_top")))
            .tag(BlockTags.WOODEN_DOORS, BlockTags.MINEABLE_WITH_AXE)
            .item()
            .model(() -> (ctx, prov) -> prov.generateFlatItem(ctx.get(), ctx.getId().withPrefix("item/")))
            .tag(ItemTags.WOODEN_DOORS)
            .build()
            .register();

    public static final BlockSetType TREATED_WOOD_SET = BlockSetType
            .register(new BlockSetType(GTPM.id("treated_wood").toString()));
    public static final WoodType TREATED_WOOD_TYPE = WoodType
            .register(new WoodType(GTPM.id("treated_wood").toString(), TREATED_WOOD_SET));

    public static final BlockEntry<Block> TREATED_WOOD_PLANK = REGISTRATE
            .block("treated_wood_planks", Block::new)
            .initialProperties(() -> Blocks.OAK_PLANKS)
            .localization(text -> text
                    .source("Treated Wood Planks")
                    .translation(MinecraftLocale.ZH_CN, "防腐木板"))
            .properties(p -> p.mapColor(MapColor.TERRACOTTA_GRAY))
            .tag(TagUtil.createBlockTag("treated_wood")) // matches IE treated wood tag
            .tag(BlockTags.MINEABLE_WITH_AXE)
            .item()
            .tag(TagUtil.createItemTag("treated_wood"))
            .build()
            .register();

    public static final BlockEntry<SlabBlock> TREATED_WOOD_SLAB = REGISTRATE
            .block("treated_wood_slab", SlabBlock::new)
            .initialProperties(() -> Blocks.SPRUCE_SLAB)
            .localization(text -> text
                    .source("Treated Wood Slab")
                    .translation(MinecraftLocale.ZH_CN, "防腐木台阶"))
            .blockstate(
                    (ctx, prov) -> prov.slabBlock(ctx.getEntry(), prov.blockTexture(GTBlocks.TREATED_WOOD_PLANK.get()),
                            prov.blockTexture(GTBlocks.TREATED_WOOD_PLANK.get())))
            .tag(BlockTags.WOODEN_SLABS, BlockTags.MINEABLE_WITH_AXE)
            .item()
            .tag(ItemTags.WOODEN_SLABS)
            .build()
            .register();

    public static final BlockEntry<FenceBlock> TREATED_WOOD_FENCE = REGISTRATE
            .block("treated_wood_fence", FenceBlock::new)
            .initialProperties(() -> Blocks.SPRUCE_FENCE)
            .localization(text -> text
                    .source("Treated Wood Fence")
                    .translation(MinecraftLocale.ZH_CN, "防腐木栅栏"))
            .blockstate(
                    () -> (ctx, prov) -> prov.fenceBlock(ctx.getEntry(), prov.blockTexture(TREATED_WOOD_PLANK.get())))
            .tag(BlockTags.WOODEN_FENCES, BlockTags.MINEABLE_WITH_AXE)
            .item()
            .model(() -> (ctx, prov) -> prov.fenceInventory(ctx.getName(),
                    GTBlocks.TREATED_WOOD_PLANK.getId().withPrefix("block/")))
            .tag(ItemTags.WOODEN_FENCES)
            .build()
            .register();

    public static final BlockEntry<StandingSignBlock> TREATED_WOOD_SIGN = REGISTRATE
            .block("treated_wood_sign", (p) -> new StandingSignBlock(TREATED_WOOD_TYPE, p))
            .initialProperties(() -> Blocks.SPRUCE_SIGN)
            .blockstate(() -> (ctx, prov) -> prov.signBlock(ctx.get(), GTBlocks.TREATED_WOOD_WALL_SIGN.get(),
                    prov.blockTexture(GTBlocks.TREATED_WOOD_PLANK.get())))
            .tag(BlockTags.STANDING_SIGNS, BlockTags.MINEABLE_WITH_AXE)
            .setData(ProviderType.LANG, NonNullBiConsumer.noop())
            .item((b, p) -> new SignItem(p, b, GTBlocks.TREATED_WOOD_WALL_SIGN.get()))
            .defaultModel()
            .tag(ItemTags.SIGNS)
            .build()
            .register();
    public static final BlockEntry<WallSignBlock> TREATED_WOOD_WALL_SIGN = REGISTRATE
            .block("treated_wood_wall_sign", (p) -> new WallSignBlock(TREATED_WOOD_TYPE, p))
            .initialProperties(() -> Blocks.SPRUCE_WALL_SIGN)
            .localization(text -> text
                    .source("Treated Wood Wall Sign")
                    .translation(MinecraftLocale.ZH_CN, "墙上的防腐木告示牌"))
            .setData(ProviderType.BLOCKSTATE, NonNullBiConsumer.noop())
            .tag(BlockTags.WALL_SIGNS, BlockTags.MINEABLE_WITH_AXE)
            .loot((table, block) -> table.dropOther(block, TREATED_WOOD_SIGN.asItem()))
            .register();
    public static final BlockEntry<CeilingHangingSignBlock> TREATED_WOOD_HANGING_SIGN = REGISTRATE
            .block("treated_wood_hanging_sign", (p) -> new CeilingHangingSignBlock(TREATED_WOOD_TYPE, p))
            .initialProperties(() -> Blocks.SPRUCE_HANGING_SIGN)
            .blockstate(() -> (ctx, prov) -> {
                ModelFile model = prov.models().sign(ctx.getName(),
                        prov.blockTexture(GTBlocks.TREATED_WOOD_PLANK.get()));
                prov.simpleBlock(ctx.get(), model);
            })
            .tag(BlockTags.CEILING_HANGING_SIGNS, BlockTags.MINEABLE_WITH_AXE)
            .setData(ProviderType.LANG, NonNullBiConsumer.noop())
            .item((b, p) -> new HangingSignItem(b, GTBlocks.TREATED_WOOD_WALL_HANGING_SIGN.get(), p))
            .defaultModel()
            .tag(ItemTags.HANGING_SIGNS)
            .setData(ProviderType.LANG, NonNullBiConsumer.noop())
            .build()
            .register();
    public static final BlockEntry<WallHangingSignBlock> TREATED_WOOD_WALL_HANGING_SIGN = REGISTRATE
            .block("treated_wood_wall_hanging_sign", (p) -> new WallHangingSignBlock(TREATED_WOOD_TYPE, p))
            .initialProperties(() -> Blocks.SPRUCE_WALL_HANGING_SIGN)
            .localization(text -> text
                    .source("Treated Wood Wall Hanging Sign")
                    .translation(MinecraftLocale.ZH_CN, "墙上的悬挂式防腐木告示牌"))
            .blockstate(() -> (ctx, prov) -> {
                ModelFile model = prov.models().sign(ctx.getName(),
                        prov.blockTexture(GTBlocks.TREATED_WOOD_PLANK.get()));
                prov.simpleBlock(ctx.get(), model);
            })
            .tag(BlockTags.WALL_HANGING_SIGNS, BlockTags.MINEABLE_WITH_AXE)
            .loot((table, block) -> table.dropOther(block, TREATED_WOOD_HANGING_SIGN.asItem()))
            .register();
    public static final BlockEntry<PressurePlateBlock> TREATED_WOOD_PRESSURE_PLATE = REGISTRATE
            .block("treated_wood_pressure_plate",
                    (p) -> new PressurePlateBlock(TREATED_WOOD_SET, p))
            .initialProperties(() -> Blocks.SPRUCE_PRESSURE_PLATE)
            .localization(text -> text
                    .source("Treated Wood Pressure Plate")
                    .translation(MinecraftLocale.ZH_CN, "防腐木压力板"))
            .tag(BlockTags.WOODEN_PRESSURE_PLATES, BlockTags.MINEABLE_WITH_AXE)
            .blockstate(() -> (ctx, prov) -> prov.pressurePlateBlock(ctx.getEntry(),
                    prov.blockTexture(GTBlocks.TREATED_WOOD_PLANK.get())))
            .item()
            .tag(ItemTags.WOODEN_PRESSURE_PLATES)
            .build()
            .register();
    public static final BlockEntry<TrapDoorBlock> TREATED_WOOD_TRAPDOOR = REGISTRATE
            .block("treated_wood_trapdoor", (p) -> new TrapDoorBlock(TREATED_WOOD_SET, p))
            .initialProperties(() -> Blocks.SPRUCE_TRAPDOOR)
            .localization(text -> text
                    .source("Treated Wood Trapdoor")
                    .translation(MinecraftLocale.ZH_CN, "防腐木活板门"))
            .blockstate(() -> (ctx, prov) -> prov.trapdoorBlock(ctx.get(), prov.blockTexture(ctx.get()), true))
            .tag(BlockTags.WOODEN_TRAPDOORS, BlockTags.MINEABLE_WITH_AXE)
            .item()
            .model(() -> (ctx, prov) -> prov.trapdoorOrientableBottom(ctx.getName(), ctx.getId().withPrefix("block/")))
            .tag(ItemTags.WOODEN_TRAPDOORS)
            .build()
            .register();
    public static final BlockEntry<StairBlock> TREATED_WOOD_STAIRS = REGISTRATE
            .block("treated_wood_stairs", (p) -> new StairBlock(TREATED_WOOD_PLANK.getDefaultState(), p))
            .initialProperties(() -> Blocks.SPRUCE_STAIRS)
            .localization(text -> text
                    .source("Treated Wood Stairs")
                    .translation(MinecraftLocale.ZH_CN, "防腐木楼梯"))
            .tag(BlockTags.STAIRS, BlockTags.MINEABLE_WITH_AXE)
            .blockstate(() -> (ctx, prov) -> prov.stairsBlock(ctx.getEntry(),
                    prov.blockTexture(GTBlocks.TREATED_WOOD_PLANK.get())))
            .item()
            .tag(ItemTags.STAIRS)
            .build()
            .register();
    public static final BlockEntry<ButtonBlock> TREATED_WOOD_BUTTON = REGISTRATE
            .block("treated_wood_button", (p) -> new ButtonBlock(TREATED_WOOD_SET, 30, p))
            .initialProperties(() -> Blocks.SPRUCE_BUTTON)
            .localization(text -> text
                    .source("Treated Wood Button")
                    .translation(MinecraftLocale.ZH_CN, "防腐木按钮"))
            .tag(BlockTags.WOODEN_BUTTONS)
            .blockstate(
                    () -> (ctx, prov) -> prov.buttonBlock(ctx.getEntry(), prov.blockTexture(TREATED_WOOD_PLANK.get())))
            .item()
            .model(() -> (ctx, prov) -> prov.buttonInventory(ctx.getName(),
                    GTBlocks.TREATED_WOOD_PLANK.getId().withPrefix("block/")))
            .tag(ItemTags.WOODEN_BUTTONS)
            .build()
            .register();
    public static final BlockEntry<FenceGateBlock> TREATED_WOOD_FENCE_GATE = REGISTRATE
            .block("treated_wood_fence_gate", (p) -> new FenceGateBlock(TREATED_WOOD_TYPE, p))
            .initialProperties(() -> Blocks.SPRUCE_FENCE_GATE)
            .localization(text -> text
                    .source("Treated Wood Fence Gate")
                    .translation(MinecraftLocale.ZH_CN, "防腐木栅栏门"))
            .tag(BlockTags.FENCE_GATES)
            .blockstate(() -> (ctx, prov) -> prov.fenceGateBlock(ctx.getEntry(),
                    prov.blockTexture(GTBlocks.TREATED_WOOD_PLANK.get())))
            .item()
            .tag(ItemTags.FENCE_GATES)
            .build()
            .register();
    public static final BlockEntry<DoorBlock> TREATED_WOOD_DOOR = REGISTRATE
            .block("treated_wood_door", (p) -> new DoorBlock(TREATED_WOOD_SET, p))
            .initialProperties(() -> Blocks.SPRUCE_DOOR)
            .localization(text -> text
                    .source("Treated Wood Door")
                    .translation(MinecraftLocale.ZH_CN, "防腐木门"))
            .loot((table, block) -> table.add(block, table.createDoorTable(block)))
            .addLayer(() -> RenderType::cutout)
            .blockstate(() -> (ctx, prov) -> prov.doorBlock(ctx.getEntry(), GTPM.id("block/treated_wood_door_bottom"),
                    GTPM.id("block/treated_wood_door_top")))
            .tag(BlockTags.WOODEN_DOORS)
            .item()
            .model(() -> (ctx, prov) -> prov.generateFlatItem(ctx.get(), ctx.getId().withPrefix("item/")))
            .tag(ItemTags.WOODEN_DOORS)
            .build()
            .register();

    // Decoration Stuff
    public static final BlockEntry<Block> ACID_HAZARD_SIGN_BLOCK = createCasingBlock("acid_hazard_sign_block",
            GTPM.id("block/casings/signs/machine_casing_acidhazard"),
            text -> text.source("Acid Hazard Sign Block")
                    .translation(MinecraftLocale.ZH_CN, "强酸危害警示方块"));
    public static final BlockEntry<Block> ANTIMATTER_HAZARD_SIGN_BLOCK = createCasingBlock(
            "antimatter_hazard_sign_block", GTPM.id("block/casings/signs/machine_casing_antimatterhazard"),
            text -> text.source("Antimatter Hazard Sign Block")
                    .translation(MinecraftLocale.ZH_CN, "反物质危害警示方块"));
    public static final BlockEntry<Block> BIO_HAZARD_SIGN_BLOCK = createCasingBlock("bio_hazard_sign_block",
            GTPM.id("block/casings/signs/machine_casing_biohazard"),
            text -> text.source("Bio Hazard Sign Block")
                    .translation(MinecraftLocale.ZH_CN, "生化危害警示方块"));
    public static final BlockEntry<Block> BOSS_HAZARD_SIGN_BLOCK = createCasingBlock("boss_hazard_sign_block",
            GTPM.id("block/casings/signs/machine_casing_bosshazard"),
            text -> text.source("Boss Hazard Sign Block")
                    .translation(MinecraftLocale.ZH_CN, "Boss危害警示方块"));
    public static final BlockEntry<Block> CAUSALITY_HAZARD_SIGN_BLOCK = createCasingBlock("causality_hazard_sign_block",
            GTPM.id("block/casings/signs/machine_casing_causalityhazard"),
            text -> text.source("Causality Hazard Sign Block")
                    .translation(MinecraftLocale.ZH_CN, "因果律危害警示方块"));
    public static final BlockEntry<Block> EXPLOSION_HAZARD_SIGN_BLOCK = createCasingBlock("explosion_hazard_sign_block",
            GTPM.id("block/casings/signs/machine_casing_explosionhazard"),
            text -> text.source("Explosion Hazard Sign Block")
                    .translation(MinecraftLocale.ZH_CN, "爆炸危害警示方块"));
    public static final BlockEntry<Block> FIRE_HAZARD_SIGN_BLOCK = createCasingBlock("fire_hazard_sign_block",
            GTPM.id("block/casings/signs/machine_casing_firehazard"),
            text -> text.source("Fire Hazard Sign Block")
                    .translation(MinecraftLocale.ZH_CN, "易燃危害警示方块"));
    public static final BlockEntry<Block> FROST_HAZARD_SIGN_BLOCK = createCasingBlock("frost_hazard_sign_block",
            GTPM.id("block/casings/signs/machine_casing_frosthazard"),
            text -> text.source("Frost Hazard Sign Block")
                    .translation(MinecraftLocale.ZH_CN, "低温危害警示方块"));
    public static final BlockEntry<Block> GENERIC_HAZARD_SIGN_BLOCK = createCasingBlock("generic_hazard_sign_block",
            GTPM.id("block/casings/signs/machine_casing_generichazard"),
            text -> text.source("Generic Hazard Sign Block")
                    .translation(MinecraftLocale.ZH_CN, "泛用危害警示方块"));
    public static final BlockEntry<Block> GREGIFICATION_HAZARD_SIGN_BLOCK = createCasingBlock(
            "gregification_hazard_sign_block", GTPM.id("block/casings/signs/machine_casing_gregificationhazard"),
            text -> text.source("Gregification Hazard Sign Block")
                    .translation(MinecraftLocale.ZH_CN, "格雷化危害警示方块"));
    public static final BlockEntry<Block> HIGH_PRESSURE_HAZARD_SIGN_BLOCK = createCasingBlock(
            "high_pressure_hazard_sign_block", GTPM.id("block/casings/signs/machine_casing_highpressurehazard"),
            text -> text.source("High Pressure Hazard Sign Block")
                    .translation(MinecraftLocale.ZH_CN, "高压力危害警示方块"));
    public static final BlockEntry<Block> HIGH_VOLTAGE_HAZARD_SIGN_BLOCK = createCasingBlock(
            "high_voltage_hazard_sign_block", GTPM.id("block/casings/signs/machine_casing_highvoltagehazard"),
            text -> text.source("High Voltage Hazard Sign Block")
                    .translation(MinecraftLocale.ZH_CN, "高压电危害警示方块"));
    public static final BlockEntry<Block> HIGH_TEMPERATURE_HAZARD_SIGN_BLOCK = createCasingBlock(
            "high_temperature_hazard_sign_block", GTPM.id("block/casings/signs/machine_casing_hightemperaturehazard"),
            text -> text.source("High Temperature Hazard Sign Block")
                    .translation(MinecraftLocale.ZH_CN, "高温危害警示方块"));
    public static final BlockEntry<Block> LASER_HAZARD_SIGN_BLOCK = createCasingBlock("laser_hazard_sign_block",
            GTPM.id("block/casings/signs/machine_casing_laserhazard"),
            text -> text.source("Laser Hazard Sign Block")
                    .translation(MinecraftLocale.ZH_CN, "激光危害警示方块"));
    public static final BlockEntry<Block> MAGIC_HAZARD_SIGN_BLOCK = createCasingBlock("magic_hazard_sign_block",
            GTPM.id("block/casings/signs/machine_casing_magichazard"),
            text -> text.source("Magic Hazard Sign Block")
                    .translation(MinecraftLocale.ZH_CN, "魔力危害警示方块"));
    public static final BlockEntry<Block> MAGNETIC_HAZARD_SIGN_BLOCK = createCasingBlock("magnetic_hazard_sign_block",
            GTPM.id("block/casings/signs/machine_casing_magneticfieldhazard"),
            text -> text.source("Magnetic Hazard Sign Block")
                    .translation(MinecraftLocale.ZH_CN, "强磁危害警示方块"));
    public static final BlockEntry<Block> MOB_INFESTATION_HAZARD_SIGN_BLOCK = createCasingBlock(
            "mob_infestation_hazard_sign_block", GTPM.id("block/casings/signs/machine_casing_mobhazard"),
            text -> text.source("Mob Infestation Hazard Sign Block")
                    .translation(MinecraftLocale.ZH_CN, "怪物侵袭危害警示方块"));
    public static final BlockEntry<Block> MOB_SPAWNER_HAZARD_SIGN_BLOCK = createCasingBlock(
            "mob_spawner_hazard_sign_block", GTPM.id("block/casings/signs/machine_casing_mobspawnhazard"),
            text -> text.source("Mob Spawner Hazard Sign Block")
                    .translation(MinecraftLocale.ZH_CN, "刷怪笼危害警示方块"));
    public static final BlockEntry<Block> NOISE_HAZARD_SIGN_BLOCK = createCasingBlock("noise_hazard_sign_block",
            GTPM.id("block/casings/signs/machine_casing_noisehazard"),
            text -> text.source("Noise Hazard Sign Block")
                    .translation(MinecraftLocale.ZH_CN, "噪声危害警示方块"));
    public static final BlockEntry<Block> RADIOACTIVE_HAZARD_SIGN_BLOCK = createCasingBlock(
            "radioactive_hazard_sign_block", GTPM.id("block/casings/signs/machine_casing_radioactivehazard"),
            text -> text.source("Radioactive Hazard Sign Block")
                    .translation(MinecraftLocale.ZH_CN, "辐射危害警示方块"));
    public static final BlockEntry<Block> SPATIAL_STORAGE_HAZARD_SIGN_BLOCK = createCasingBlock(
            "spatial_storage_hazard_sign_block", GTPM.id("block/casings/signs/machine_casing_spatialhazard"),
            text -> text.source("Spatial Storage Hazard Sign Block")
                    .translation(MinecraftLocale.ZH_CN, "空间存储危害警示方块"));
    public static final BlockEntry<Block> TURRET_HAZARD_SIGN_BLOCK = createCasingBlock("turret_hazard_sign_block",
            GTPM.id("block/casings/signs/machine_casing_turrethazard"),
            text -> text.source("Turret Hazard Sign Block")
                    .translation(MinecraftLocale.ZH_CN, "炮塔危害警示方块"));
    public static final BlockEntry<Block> VOID_HAZARD_SIGN_BLOCK = createCasingBlock("void_hazard_sign_block",
            GTPM.id("block/casings/signs/machine_casing_voidhazard"),
            text -> text.source("Void Hazard Sign Block")
                    .translation(MinecraftLocale.ZH_CN, "虚空危害警示方块"));
    @TransVariants(keyPrefix = "block.gtpm.yellow_stripes_block",
                   value = {
                           @Variant(id = "a",
                                    source = "Yellow Stripes Block",
                                    translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "黄色路障方块")),
                           @Variant(id = "b",
                                    source = "Yellow Stripes Block",
                                    translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "黄色路障方块")),
                           @Variant(id = "c",
                                    source = "Yellow Stripes Block",
                                    translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "黄色路障方块")),
                           @Variant(id = "d",
                                    source = "Yellow Stripes Block",
                                    translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "黄色路障方块")),
                   })
    public static final BlockEntry<Block> YELLOW_STRIPES_BLOCK_A = createCasingBlock("yellow_stripes_block_a",
            GTPM.id("block/casings/signs/machine_casing_stripes_a"),
            text -> text.source("Yellow Stripes Block A")
                    .translation(MinecraftLocale.ZH_CN, "黄色路障方块A"));
    public static final BlockEntry<Block> YELLOW_STRIPES_BLOCK_B = createCasingBlock("yellow_stripes_block_b",
            GTPM.id("block/casings/signs/machine_casing_stripes_b"),
            text -> text.source("Yellow Stripes Block B")
                    .translation(MinecraftLocale.ZH_CN, "黄色路障方块B"));

    public static Table<StoneBlockType, StoneTypes, BlockEntry<Block>> STONE_BLOCKS;
    public static Map<TagPrefix, Supplier<BlockState>> COBBLE_BLOCKS = new HashMap<>();

    public static BlockEntry<Block> RED_GRANITE;
    public static BlockEntry<Block> MARBLE;
    public static BlockEntry<Block> LIGHT_CONCRETE;
    public static BlockEntry<Block> DARK_CONCRETE;

    public static BlockEntry<Block> BRITTLE_CHARCOAL = REGISTRATE
            .block("brittle_charcoal", Block::new)
            .properties(p -> p.strength(0.5f).explosionResistance(8.0f).sound(SoundType.STONE))
            .loot((table, block) -> table.add(block,
                    table.createSingleItemTable(Items.CHARCOAL, UniformGenerator.between(1.0F, 3.0F))))
            .localization(text -> text
                    .source("Brittle Charcoal")
                    .translation(MinecraftLocale.ZH_CN, "脆木炭块"))
            .defaultBlockstate()
            .tag(BlockTags.MINEABLE_WITH_SHOVEL)
            .item((b, p) -> new BlockItem(b, p) {

                @Override
                public void appendHoverText(@NotNull ItemStack stack, @NotNull Item.TooltipContext context,
                                            @NotNull List<Component> tooltip, @NotNull TooltipFlag isAdvanced) {
                    super.appendHoverText(stack, context, tooltip, isAdvanced);
                    tooltip.add(1, Component.translatable("tile.gtpm.brittle_charcoal.tooltip.0"));
                    tooltip.add(2, Component.translatable("tile.gtpm.brittle_charcoal.tooltip.1"));
                }
            })
            .build()
            .register();

    public static void generateStoneBlocks() {
        // Stone type blocks
        ImmutableTable.Builder<StoneBlockType, StoneTypes, BlockEntry<Block>> builder = ImmutableTable.builder();
        for (StoneTypes strata : StoneTypes.values()) {
            if (!strata.generateBlocks) continue;
            String chineseName = switch (strata) {
                case RED_GRANITE -> "红花岗岩";
                case MARBLE -> "大理岩";
                case CONCRETE_LIGHT -> "淡色混凝土";
                case CONCRETE_DARK -> "深色混凝土";
                default -> null;
            };
            String sourceName = FormattingUtil.toEnglishName(strata.getSerializedName());
            for (StoneBlockType type : StoneBlockType.values()) {
                String blockId = type.blockId.formatted(strata.getSerializedName());
                if (BuiltInRegistries.BLOCK.containsKey(ResourceLocation.parse(blockId))) continue;
                var entry = REGISTRATE.block(blockId, Block::new)
                        .localization(text -> {
                            text.source(type.namePattern.source().formatted(sourceName));
                            if (chineseName != null) {
                                text.translation(MinecraftLocale.ZH_CN,
                                        type.namePattern.translations().get(MinecraftLocale.ZH_CN)
                                                .formatted(chineseName));
                            }
                        })
                        .initialProperties(() -> Blocks.STONE)
                        .properties(p -> p.strength(type.hardness, type.resistance).mapColor(strata.mapColor))
                        .transform(type == StoneBlockType.STONE ?
                                GTBlocks.unificationBlock(strata.getTagPrefix(), strata.getMaterial()) :
                                builder2 -> builder2)
                        .tag(BlockTags.MINEABLE_WITH_PICKAXE, Tags.Blocks.NEEDS_WOOD_TOOL)
                        .loot((tables, block) -> {
                            if (type == StoneBlockType.STONE) {
                                tables.add(block, tables.createSingleItemTableWithSilkTouch(block,
                                        STONE_BLOCKS.get(StoneBlockType.COBBLE, strata).get()));
                            } else {
                                tables.add(block, tables.createSingleItemTable(block));
                            }
                        })
                        .item()
                        .build();
                if (type == StoneBlockType.STONE && strata.isNatural()) {
                    entry.tag(BlockTags.STONE_ORE_REPLACEABLES, BlockTags.BASE_STONE_OVERWORLD,
                            BlockTags.DRIPSTONE_REPLACEABLE, BlockTags.MOSS_REPLACEABLE)
                            .blockstate(
                                    () -> GTModels.randomRotatedModel(GTPM.id(ModelProvider.BLOCK_FOLDER + "/stones/" +
                                            strata.getSerializedName() + "/" + type.id)));
                } else {
                    entry.blockstate(() -> (ctx, prov) -> prov.simpleBlock(ctx.getEntry(),
                            prov.models().singleTexture(ctx.getName(),
                                    prov.mcLoc(ModelProvider.BLOCK_FOLDER + "/cube_all"), "all",
                                    prov.modLoc(ModelProvider.BLOCK_FOLDER + "/stones/" + strata.getSerializedName() +
                                            "/" + type.id))));
                }
                if (type == StoneBlockType.STONE) {
                    entry.tag(Tags.Blocks.STONES);
                }
                if (type == StoneBlockType.COBBLE) {
                    entry.tag(Tags.Blocks.COBBLESTONES);
                }
                builder.put(type, strata, entry.register());
            }
        }
        STONE_BLOCKS = builder.build();

        STONE_BLOCKS.row(StoneBlockType.COBBLE).forEach((strata, block) -> {
            if (strata.generateBlocks && strata.natural) {
                COBBLE_BLOCKS.put(strata.getOreBaseTagPrefix(), block::getDefaultState);
            }
        });

        RED_GRANITE = STONE_BLOCKS.get(StoneBlockType.STONE, StoneTypes.RED_GRANITE);
        MARBLE = STONE_BLOCKS.get(StoneBlockType.STONE, StoneTypes.MARBLE);
        LIGHT_CONCRETE = STONE_BLOCKS.get(StoneBlockType.STONE, StoneTypes.CONCRETE_LIGHT);
        DARK_CONCRETE = STONE_BLOCKS.get(StoneBlockType.STONE, StoneTypes.CONCRETE_DARK);
    }

    public static final BlockEntry<FoamBlock> FOAM = REGISTRATE
            .block("foam", p -> new FoamBlock(p, false))
            .localization(text -> text
                    .source("Foam")
                    .translation(MinecraftLocale.ZH_CN, "建筑泡沫"))
            .properties(p -> p.strength(0.5F, 0.3F)
                    .randomTicks()
                    .sound(SoundType.SNOW)
                    .pushReaction(PushReaction.DESTROY)
                    .noOcclusion().noCollission().noLootTable())
            .simpleItem()
            .register();

    public static final BlockEntry<FoamBlock> REINFORCED_FOAM = REGISTRATE
            .block("reinforced_foam", p -> new FoamBlock(p, true))
            .localization(text -> text
                    .source("Reinforced Foam")
                    .translation(MinecraftLocale.ZH_CN, "强化建筑泡沫"))
            .initialProperties(FOAM)
            .simpleItem()
            .register();

    public static final BlockEntry<Block> PETRIFIED_FOAM = REGISTRATE
            .block("petrified_foam", Block::new)
            .localization(text -> text
                    .source("Petrified Foam")
                    .translation(MinecraftLocale.ZH_CN, "石化建筑泡沫"))
            .initialProperties(() -> Blocks.STONE)
            .properties(p -> p.strength(1.0F, 4.0F).sound(SoundType.SNOW))
            .tag(BlockTags.MINEABLE_WITH_PICKAXE, BlockTags.NEEDS_STONE_TOOL)
            .simpleItem()
            .register();
    public static final BlockEntry<Block> REINFORCED_STONE = REGISTRATE
            .block("reinforced_stone", Block::new)
            .localization(text -> text
                    .source("Reinforced Stone")
                    .translation(MinecraftLocale.ZH_CN, "防爆石"))
            .initialProperties(() -> Blocks.STONE)
            .properties(p -> p.strength(4.0F, 16.0F))
            .tag(BlockTags.MINEABLE_WITH_PICKAXE, BlockTags.NEEDS_IRON_TOOL)
            .simpleItem()
            .register();

    // Lamps
    private static Consumer<LocalizationDeclaration.Builder> colorName(DyeColor color,
                                                                       String sourcePattern,
                                                                       String translationPattern) {
        Material marker = MarkerMaterials.Color.COLORS.get(color);
        return text -> {
            text.source(sourcePattern.formatted(FormattingUtil.toEnglishName(color.getName())));
            if (marker != null && marker.getLocalization() != null) {
                String chinese = marker.getLocalization().translations().get(MinecraftLocale.ZH_CN);
                if (chinese != null) text.translation(MinecraftLocale.ZH_CN, translationPattern.formatted(chinese));
            }
        };
    }

    public static final Map<DyeColor, BlockEntry<LampBlock>> LAMPS;
    public static final Map<DyeColor, BlockEntry<LampBlock>> BORDERLESS_LAMPS;
    static {
        ImmutableMap.Builder<DyeColor, BlockEntry<LampBlock>> lampBuilder = new ImmutableMap.Builder<>();
        DyeColor[] colors = DyeColor.values();
        for (DyeColor dyeColor : colors) {
            lampBuilder.put(dyeColor,
                    REGISTRATE.block("%s_lamp".formatted(dyeColor.getName()), (p) -> new LampBlock(p, dyeColor, true))
                            .localization(colorName(dyeColor, "%s Lamp", "%s灯"))
                            .initialProperties(() -> Blocks.GLASS)
                            .properties(p -> p.strength(0.3f, 8.0f).sound(SoundType.GLASS))
                            .addLayer(() -> RenderType::cutout)
                            .blockstate(() -> GTModels.lampModel(dyeColor, true))
                            .item(LampBlockItem::new)
                            .build()
                            .register());
        }
        LAMPS = lampBuilder.build();
        ImmutableMap.Builder<DyeColor, BlockEntry<LampBlock>> borderlessLampBuilder = new ImmutableMap.Builder<>();
        for (DyeColor dyeColor : colors) {
            borderlessLampBuilder.put(dyeColor, REGISTRATE
                    .block("%s_borderless_lamp".formatted(dyeColor.getName()), (p) -> new LampBlock(p, dyeColor, false))
                    .localization(colorName(dyeColor, "%s Borderless Lamp", "%s无框灯"))
                    .initialProperties(() -> Blocks.GLASS)
                    .properties(p -> p.strength(0.3f, 8.0f).sound(SoundType.GLASS))
                    .blockstate(() -> GTModels.lampModel(dyeColor, false))
                    .item(LampBlockItem::new)
                    .build()
                    .register());
        }
        BORDERLESS_LAMPS = borderlessLampBuilder.build();
    }

    // Decorations
    public static final Map<DyeColor, BlockEntry<Block>> METAL_SHEETS;
    public static final Map<DyeColor, BlockEntry<Block>> LARGE_METAL_SHEETS;
    public static final Map<DyeColor, BlockEntry<Block>> STUDS;

    static {
        DyeColor[] colors = DyeColor.values();
        ImmutableMap.Builder<DyeColor, BlockEntry<Block>> metalsheetBuilder = new ImmutableMap.Builder<>();
        for (DyeColor dyeColor : colors) {
            metalsheetBuilder.put(dyeColor, REGISTRATE.block("%s_metal_sheet".formatted(dyeColor.getName()), Block::new)
                    .localization(colorName(dyeColor, "%s Metal Sheet", "%s金属板方块"))
                    .initialProperties(() -> Blocks.IRON_BLOCK)
                    .properties(p -> p.strength(2.0F, 5.0F).mapColor(dyeColor))
                    .tag(CustomTags.MINEABLE_WITH_CONFIG_VALID_PICKAXE_WRENCH)
                    .blockstate(() -> (ctx, prov) -> prov.simpleBlock(ctx.get(),
                            prov.models().cubeAll(ctx.getName(),
                                    GTPM.id("block/decoration/metalsheet_%s".formatted(dyeColor.getName())))))
                    .simpleItem()
                    .register());
        }
        METAL_SHEETS = metalsheetBuilder.build();

        ImmutableMap.Builder<DyeColor, BlockEntry<Block>> largeMetalsheetBuilder = new ImmutableMap.Builder<>();
        for (DyeColor dyeColor : colors) {
            largeMetalsheetBuilder.put(dyeColor,
                    REGISTRATE.block("%s_large_metal_sheet".formatted(dyeColor.getName()), Block::new)
                            .localization(colorName(dyeColor, "%s Large Metal Sheet", "%s粗纹金属板方块"))
                            .initialProperties(() -> Blocks.IRON_BLOCK)
                            .properties(p -> p.strength(2.0F, 5.0F).mapColor(dyeColor))
                            .tag(CustomTags.MINEABLE_WITH_CONFIG_VALID_PICKAXE_WRENCH)
                            .blockstate(() -> (ctx, prov) -> prov.simpleBlock(ctx.get(),
                                    prov.models().cubeAll(ctx.getName(),
                                            GTPM.id("block/decoration/large_metalsheet_%s"
                                                    .formatted(dyeColor.getName())))))
                            .simpleItem()
                            .register());
        }
        LARGE_METAL_SHEETS = largeMetalsheetBuilder.build();

        ImmutableMap.Builder<DyeColor, BlockEntry<Block>> studsBuilder = new ImmutableMap.Builder<>();
        for (DyeColor dyeColor : colors) {
            studsBuilder.put(dyeColor, REGISTRATE.block("%s_studs".formatted(dyeColor.getName()), Block::new)
                    .localization(colorName(dyeColor, "%s Studs", "%s橡胶混凝土"))
                    .initialProperties(() -> Blocks.WHITE_WOOL)
                    .properties(p -> p.strength(1.5F, 2.5F).mapColor(dyeColor))
                    .tag(BlockTags.MINEABLE_WITH_PICKAXE, Tags.Blocks.NEEDS_WOOD_TOOL)
                    .blockstate(() -> (ctx, prov) -> prov.simpleBlock(ctx.get(),
                            prov.models().cubeAll(ctx.getName(),
                                    GTPM.id("block/decoration/studs_%s".formatted(dyeColor.getName())))))
                    .simpleItem()
                    .register());
        }
        STUDS = studsBuilder.build();
    }

    public static <P, T extends Block,
            S2 extends BlockBuilder<T, P>> NonNullFunction<S2, S2> unificationBlock(@NotNull TagPrefix tagPrefix,
                                                                                    @NotNull Material mat) {
        return builder -> {
            builder.onRegister(block -> {
                Supplier<Block> blockSupplier = GTMemoizer.memoizeBlockSupplier(() -> block);
                MaterialEntry entry = new MaterialEntry(tagPrefix, mat);
                GTMaterialItems.toUnify.put(entry, blockSupplier);
                ItemMaterialData.registerMaterialEntry(blockSupplier, entry);
            });
            return builder;
        };
    }

    public static void registerCobbleBlock(TagPrefix orePrefix, Supplier<BlockState> state) {
        COBBLE_BLOCKS.put(orePrefix, state);
    }

    public static void removeCobbleBlock(TagPrefix orePrefix) {
        COBBLE_BLOCKS.remove(orePrefix);
    }

    public static void init() {
        // Decor Blocks
        generateStoneBlocks();
        initializeCobbleReplacements();

        // Procedural Pipes/Wires
        REGISTRATE.creativeModeTab(GTCreativeModeTabs.MATERIAL_PIPE);
        generateLaserPipeBlocks();    // Laser Pipe Blocks
        generateOpticalPipeBlocks();  // Optical Pipe Blocks
        generateDuctPipeBlocks();     // Duct Pipe Blocks

        // GCYM
        GCYMBlocks.init();
    }

    private static void initializeCobbleReplacements() {
        // replacement blocks for mc based stone types
        COBBLE_BLOCKS.put(TagPrefix.ore, Blocks.COBBLESTONE::defaultBlockState);
        COBBLE_BLOCKS.put(TagPrefix.oreDeepslate, Blocks.COBBLED_DEEPSLATE::defaultBlockState);
        COBBLE_BLOCKS.put(TagPrefix.oreAndesite, Blocks.ANDESITE::defaultBlockState);
        COBBLE_BLOCKS.put(TagPrefix.oreDiorite, Blocks.DIORITE::defaultBlockState);
        COBBLE_BLOCKS.put(TagPrefix.oreGranite, Blocks.GRANITE::defaultBlockState);
        COBBLE_BLOCKS.put(TagPrefix.oreRedGranite,
                STONE_BLOCKS.get(StoneBlockType.COBBLE, StoneTypes.RED_GRANITE)::getDefaultState);
        COBBLE_BLOCKS.put(TagPrefix.oreMarble,
                STONE_BLOCKS.get(StoneBlockType.COBBLE, StoneTypes.MARBLE)::getDefaultState);
        COBBLE_BLOCKS.put(TagPrefix.oreSand, Blocks.SAND::defaultBlockState);
        COBBLE_BLOCKS.put(TagPrefix.oreGravel, Blocks.GRAVEL::defaultBlockState);
        COBBLE_BLOCKS.put(TagPrefix.oreRedSand, Blocks.RED_SAND::defaultBlockState);
        COBBLE_BLOCKS.put(TagPrefix.oreBasalt, Blocks.BASALT::defaultBlockState);
        COBBLE_BLOCKS.put(TagPrefix.oreBlackstone, Blocks.BLACKSTONE::defaultBlockState);
        COBBLE_BLOCKS.put(TagPrefix.oreEndstone, Blocks.END_STONE::defaultBlockState);
        COBBLE_BLOCKS.put(TagPrefix.oreNetherrack, Blocks.NETHERRACK::defaultBlockState);
        COBBLE_BLOCKS.put(TagPrefix.oreTuff, Blocks.TUFF::defaultBlockState);
    }

    public static boolean doMetalPipe(Material material) {
        return GTValues.FOOLS.getAsBoolean() && material.hasProperty(PropertyKey.INGOT) &&
                !material.hasProperty(PropertyKey.POLYMER) && !material.hasProperty(PropertyKey.WOOD);
    }

    /**
     * kinda nasty block property copy function because one doesn't exist.
     *
     * @param props the props to copy
     * @return a shallow copy of the block properties like {@link BlockBehaviour.Properties#ofFullCopy(BlockBehaviour)}
     *         does
     */
    public static BlockBehaviour.Properties copy(BlockBehaviour.Properties props, BlockBehaviour.Properties newProps) {
        if (props == null) {
            return newProps;
        }
        newProps.destroyTime(((BlockPropertiesAccessor) props).getDestroyTime());
        newProps.explosionResistance(((BlockPropertiesAccessor) props).getExplosionResistance());
        if (!((BlockPropertiesAccessor) props).isHasCollision()) newProps.noCollission();
        if (((BlockPropertiesAccessor) props).isIsRandomlyTicking()) newProps.randomTicks();
        newProps.lightLevel(((BlockPropertiesAccessor) props).getLightEmission());
        newProps.mapColor(((BlockPropertiesAccessor) props).getMapColor());
        newProps.sound(((BlockPropertiesAccessor) props).getSoundType());
        newProps.friction(((BlockPropertiesAccessor) props).getFriction());
        newProps.speedFactor(((BlockPropertiesAccessor) props).getSpeedFactor());
        if (((BlockPropertiesAccessor) props).isDynamicShape()) newProps.dynamicShape();
        if (!((BlockPropertiesAccessor) props).isCanOcclude()) newProps.noOcclusion();
        if (((BlockPropertiesAccessor) props).isIsAir()) newProps.air();
        if (((BlockPropertiesAccessor) props).isIgnitedByLava()) newProps.ignitedByLava();
        if (((BlockPropertiesAccessor) props).isLiquid()) newProps.liquid();
        if (((BlockPropertiesAccessor) props).isForceSolidOff()) newProps.forceSolidOff();
        if (((BlockPropertiesAccessor) props).isForceSolidOn()) newProps.forceSolidOn();
        newProps.pushReaction(((BlockPropertiesAccessor) props).getPushReaction());
        if (((BlockPropertiesAccessor) props).isRequiresCorrectToolForDrops()) newProps.requiresCorrectToolForDrops();
        ((BlockPropertiesAccessor) newProps).setOffsetFunction(((BlockPropertiesAccessor) props).getOffsetFunction());
        if (!((BlockPropertiesAccessor) props).isSpawnTerrainParticles()) newProps.noTerrainParticles();
        ((BlockPropertiesAccessor) newProps)
                .setRequiredFeatures(((BlockPropertiesAccessor) props).getRequiredFeatures());
        newProps.emissiveRendering(((BlockPropertiesAccessor) props).getEmissiveRendering());
        newProps.instrument(((BlockPropertiesAccessor) props).getInstrument());
        if (((BlockPropertiesAccessor) props).isReplaceable()) newProps.replaceable();
        return newProps;
    }
}
