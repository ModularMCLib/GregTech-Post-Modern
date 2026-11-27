package com.modularmc.gtpm.common.data.machines;

import com.modularmc.gtpm.GTPM;
import com.modularmc.gtpm.api.GTValues;
import com.modularmc.gtpm.api.blockentity.BlockEntityCreationInfo;
import com.modularmc.gtpm.api.data.RotationState;
import com.modularmc.gtpm.api.machine.MachineDefinition;
import com.modularmc.gtpm.api.machine.MetaMachine;
import com.modularmc.gtpm.api.machine.MultiblockMachineDefinition;
import com.modularmc.gtpm.api.machine.multiblock.MultiblockControllerMachine;
import com.modularmc.gtpm.api.machine.multiblock.PartAbility;
import com.modularmc.gtpm.api.machine.property.GTMachineModelProperties;
import com.modularmc.gtpm.api.machine.trait.WorkLogic;
import com.modularmc.gtpm.api.multiblock.pattern.dsl.PatternBuilder;
import com.modularmc.gtpm.api.registry.registrate.MachineBuilder;
import com.modularmc.gtpm.client.util.TooltipHelper;
import com.modularmc.gtpm.common.data.GTMaterials;
import com.modularmc.gtpm.common.data.GTRecipeTypes;
import com.modularmc.gtpm.common.machine.multiblock.electric.research.DataBankMachine;
import com.modularmc.gtpm.common.machine.multiblock.electric.research.HPCAMachine;
import com.modularmc.gtpm.common.machine.multiblock.electric.research.NetworkSwitchMachine;
import com.modularmc.gtpm.common.machine.multiblock.electric.research.ResearchStationMachine;
import com.modularmc.gtpm.common.machine.multiblock.part.DataAccessHatchMachine;
import com.modularmc.gtpm.common.machine.multiblock.part.ObjectHolderMachine;
import com.modularmc.gtpm.common.machine.multiblock.part.OpticalComputationHatchMachine;
import com.modularmc.gtpm.common.machine.multiblock.part.OpticalDataHatchMachine;
import com.modularmc.gtpm.common.machine.multiblock.part.hpca.*;
import com.modularmc.gtpm.data.lang.LangHandler;
import com.modularmc.gtpm.utils.FormattingUtil;

import com.modularmc.registrate.localization.MinecraftLocale;
import com.modularmc.registrate.localization.annotation.TransVariants;
import com.modularmc.registrate.localization.annotation.Translation;
import com.modularmc.registrate.localization.annotation.Variant;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

import javax.annotation.ParametersAreNonnullByDefault;

import static com.modularmc.gtpm.api.GTValues.*;
import static com.modularmc.gtpm.api.machine.property.GTMachineModelProperties.IS_FORMED;
import static com.modularmc.gtpm.common.data.GTBlocks.*;
import static com.modularmc.gtpm.common.data.GTMachines.CREATIVE_TOOLTIPS;
import static com.modularmc.gtpm.common.data.models.GTMachineModels.*;
import static com.modularmc.gtpm.common.registry.GTRegistration.REGISTRATE;

@TransVariants(keyPrefix = "gtpm.machine",
               value = {
                       @Variant(id = "data_access_hatch.tooltip.0",
                                source = "Data Access for Multiblocks",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "令多方块结构得以访问数据")),
                       @Variant(id = "data_access_hatch.tooltip.1",
                                source = "Adds §a%s§7 slots for Data Items",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "添加了§a%s§7个数据容器（闪存等）插槽")),
                       @Variant(id = "data_bank.tooltip.0",
                                source = "Your Personal NAS",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "你的个人网络存储器")),
                       @Variant(id = "data_bank.tooltip.1",
                                source = "Bulk Data Storage. Transfer with Optical Cables.",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "大容量数据存储，使用光缆传输。")),
                       @Variant(id = "data_bank.tooltip.2",
                                source = "Data Banks can be chained together.",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "数据库间可以相互连接。")),
                       @Variant(id = "data_bank.tooltip.3",
                                source = "Uses §f%s EU/t§7 per Data/Optical Hatch normally.",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "通常，每个数据/光学仓耗能为§f%s EU/t§7。")),
                       @Variant(id = "data_bank.tooltip.4",
                                source = "Uses §f%s EU/t§7 per Data/Optical Hatch when chained.",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "安装了靶仓时，每个数据/光学仓耗能为§f%s EU/t§7。")),
                       @Variant(id = "high_performance_computation_array.tooltip.0",
                                source = "Just your average Supercomputer",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "只是普通的超级计算机")),
                       @Variant(id = "high_performance_computation_array.tooltip.1",
                                source = "Used to generate §fComputation§7 (and heat).",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "用于生产§f算力§7（同时会产生热量）。")),
                       @Variant(id = "high_performance_computation_array.tooltip.2",
                                source = "Requires HPCA components to generate §fCWU/t§7 (Compute Work Units).",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "需要HPCA组件来生产§fCWU/t§7（计算工作单元）。")),
                       @Variant(id = "hpca.component_general.max_eut",
                                source = "§6Max Energy: §f%d EU/t",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§6最大能量：§f%d EU/t")),
                       @Variant(id = "hpca.component_general.upkeep_eut",
                                source = "§eUpkeep Energy: §f%d EU/t",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§e维持耗能：§f%d EU/t")),
                       @Variant(id = "hpca.component_type.bridge",
                                source = "Allows §fHPCA§7 to connect to §fNetwork Switches§7",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "允许§fHPCA§7与§f网络交换机§7连接")),
                       @Variant(id = "hpca.component_type.computation_cooling",
                                source = "§cRequires up to: §f%d Cooling",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§c最多需要：§f%d 冷却")),
                       @Variant(id = "hpca.component_type.computation_cwut",
                                source = "§9Computation: §f%d CWU/t",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§9算力：§f%d CWU/t")),
                       @Variant(id = "hpca.component_type.cooler_active",
                                source = "§bCooler Type: §fActive",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§b冷却类型：§f主动")),
                       @Variant(id = "hpca.component_type.cooler_active_coolant",
                                source = "§cRequires up to: §f%d mB/t %s",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "§c最多需要：§f%d mB/t %s")),
                       @Variant(id = "hpca.component_type.cooler_cooling",
                                source = "§aProvides: §f%d Cooling",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§a提供：§f%d 冷却")),
                       @Variant(id = "hpca.component_type.cooler_passive",
                                source = "§bCooler Type: §fPassive",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§b冷却类型：§f被动")),
                       @Variant(id = "hpca.component_type.damaged",
                                source = "Can be damaged by HPCA overheating!",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "HPCA过热可能会损坏！")),
                       @Variant(id = "network_switch.tooltip.0",
                                source = "Ethernet Hub",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "以太网集线器")),
                       @Variant(id = "network_switch.tooltip.1",
                                source = "Used to route and distribute §fComputation§7.",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "用于路由与分发§f算力§7。")),
                       @Variant(id = "network_switch.tooltip.2",
                                source = "Can combine any number of Computation §fReceivers§7 into any number of Computation §fTransmitters§7.",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "可以将任意数量的算力§f输入§7整合为为任意数量的算力§f输出§7。")),
                       @Variant(id = "network_switch.tooltip.3",
                                source = "Uses §f%s EU/t§7 per Computation Data Hatch.",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "每个算力数据仓使用§f%s EU/t§7。")),
                       @Variant(id = "research_station.tooltip.0",
                                source = "More than just a Multiblock Scanner",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "不仅仅是一个多方块扫描仪")),
                       @Variant(id = "research_station.tooltip.1",
                                source = "Used to scan onto §fData Orbs§7 and §fData Modules§7.",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "用于扫描§f数据球§7和§f数据模块§7。")),
                       @Variant(id = "research_station.tooltip.2",
                                source = "Requires §fComputation§7 to work.",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "需要§f算力§7来进行工作。")),
                       @Variant(id = "research_station.tooltip.3",
                                source = "Providing more Computation allows the recipe to run faster.",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "提供更多的算力可以使研究进展的更快。")),
               })
@SuppressWarnings("unused")
@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class GTResearchMachines {

    public static final MultiblockMachineDefinition RESEARCH_STATION = REGISTRATE
            .multiblock("research_station", ResearchStationMachine::new)
            .localization(text -> text
                    .source("Research Station")
                    .translation(MinecraftLocale.ZH_CN, "研究站"))
            .rotationState(RotationState.NON_Y_AXIS)
            .recipeType(GTRecipeTypes.RESEARCH_STATION_RECIPES)
            .appearanceBlock(ADVANCED_COMPUTER_CASING)
            .tooltips(LangHandler.getMultiLang("gtpm.machine.research_station.tooltip"))
            .pattern(MultiblockControllerMachine.DEFAULT_STRUCTURE, definition -> PatternBuilder.start(definition)
                    .appendDefinition()
                    .build())
            .sidedWorkableCasingModel(GTPM.id("block/casings/hpca/advanced_computer_casing"),
                    GTPM.id("block/multiblock/research_station"))
            .register();

    public static final MachineDefinition OBJECT_HOLDER = REGISTRATE.machine("object_holder", ObjectHolderMachine::new)
            .localization(text -> text
                    .source("Object Holder")
                    .translation(MinecraftLocale.ZH_CN, "物品支架"))
            .tier(ZPM)
            .rotationState(RotationState.ALL)
            .abilities(PartAbility.OBJECT_HOLDER)
            .modelProperty(IS_FORMED, false)
            .modelProperty(GTMachineModelProperties.RECIPE_LOGIC_STATUS, WorkLogic.Status.IDLE)
            .model(createWorkableTieredHullMachineModel(GTPM.id("block/machines/object_holder"))
                    .andThen((ctx, prov, model) -> {
                        model.addReplaceableTextures("bottom", "top", "side");
                    }))
            .register();

    public static final MachineDefinition DATA_BANK = REGISTRATE.multiblock("data_bank", DataBankMachine::new)
            .localization(text -> text
                    .source("Data Bank")
                    .translation(MinecraftLocale.ZH_CN, "数据库"))
            .rotationState(RotationState.NON_Y_AXIS)
            .appearanceBlock(COMPUTER_CASING)
            .recipeType(GTRecipeTypes.DUMMY_RECIPES)
            .tooltips(Component.translatable("gtpm.machine.data_bank.tooltip.0"),
                    Component.translatable("gtpm.machine.data_bank.tooltip.1"),
                    Component.translatable("gtpm.machine.data_bank.tooltip.2"),
                    Component.translatable("gtpm.machine.data_bank.tooltip.3",
                            FormattingUtil.formatNumbers(DataBankMachine.EUT_PER_HATCH)),
                    Component.translatable("gtpm.machine.data_bank.tooltip.4",
                            FormattingUtil.formatNumbers(DataBankMachine.EUT_PER_HATCH_CHAINED)))
            .pattern(MultiblockControllerMachine.DEFAULT_STRUCTURE, definition -> PatternBuilder.start(definition)
                    .appendDefinition()
                    .build())
            .workableCasingModel(GTPM.id("block/casings/hpca/high_power_casing"),
                    GTPM.id("block/multiblock/data_bank"))
            .register();

    public static final MachineDefinition NETWORK_SWITCH = REGISTRATE
            .multiblock("network_switch", NetworkSwitchMachine::new)
            .localization(text -> text
                    .source("Network Switch")
                    .translation(MinecraftLocale.ZH_CN, "网络交换机"))
            .rotationState(RotationState.NON_Y_AXIS)
            .appearanceBlock(COMPUTER_CASING)
            .recipeType(GTRecipeTypes.DUMMY_RECIPES)
            .tooltips(Component.translatable("gtpm.machine.network_switch.tooltip.0"),
                    Component.translatable("gtpm.machine.network_switch.tooltip.1"),
                    Component.translatable("gtpm.machine.network_switch.tooltip.2"),
                    Component.translatable("gtpm.machine.network_switch.tooltip.3",
                            FormattingUtil.formatNumbers(NetworkSwitchMachine.EUT_PER_HATCH)))
            .pattern(MultiblockControllerMachine.DEFAULT_STRUCTURE, definition -> PatternBuilder.start(definition)
                    .appendDefinition()
                    .build())
            .sidedWorkableCasingModel(GTPM.id("block/casings/hpca/computer_casing"),
                    GTPM.id("block/multiblock/network_switch"))
            .register();

    public static final MachineDefinition HIGH_PERFORMANCE_COMPUTING_ARRAY = REGISTRATE
            .multiblock("high_performance_computation_array", HPCAMachine::new)
            .localization(text -> text
                    .source("High Performance Computation Array (HPCA)")
                    .translation(MinecraftLocale.ZH_CN, "高性能计算阵列（HPCA）"))
            .rotationState(RotationState.NON_Y_AXIS)
            // TODO : Make a controllerAppearanceBlock() so the controller CTM's to the correct casings - Also just a
            // good API addition for packdevs
            .appearanceBlock(COMPUTER_CASING)
            .recipeType(GTRecipeTypes.DUMMY_RECIPES)
            .tooltips(LangHandler.getMultiLang("gtpm.machine.high_performance_computation_array.tooltip"))
            .pattern(MultiblockControllerMachine.DEFAULT_STRUCTURE, definition -> PatternBuilder.start(definition)
                    .appendDefinition()
                    .build())
            .sidedWorkableCasingModel(GTPM.id("block/casings/hpca/computer_casing"),
                    GTPM.id("block/multiblock/hpca"))
            .register();

    ////////////////////////////////////////////
    // ******** MULTIBLOCK PARTS ********//
    ////////////////////////////////////////////

    public static final MachineDefinition COMPUTATION_HATCH_TRANSMITTER = registerDataHatch(
            "computation_transmitter_hatch",
            ZPM, (holder) -> new OpticalComputationHatchMachine(holder, true),
            "computation_data_hatch", PartAbility.COMPUTATION_DATA_TRANSMISSION)
            .localization(text -> text.source("Computation Data Transmission Hatch")
                    .translation(MinecraftLocale.ZH_CN, "算力数据源仓"))
            .tooltips(Component.translatable("gtpm.part_sharing.disabled"))
            .register();

    public static final MachineDefinition COMPUTATION_HATCH_RECEIVER = registerDataHatch(
            "computation_receiver_hatch",
            ZPM, (holder) -> new OpticalComputationHatchMachine(holder, false),
            "computation_data_hatch", PartAbility.COMPUTATION_DATA_RECEPTION)
            .localization(text -> text.source("Computation Data Reception Hatch")
                    .translation(MinecraftLocale.ZH_CN, "算力数据靶仓"))
            .tooltips(Component.translatable("gtpm.part_sharing.disabled"))
            .register();

    public static final MachineDefinition DATA_HATCH_TRANSMITTER = registerDataHatch(
            "data_transmitter_hatch",
            LuV, (holder) -> new OpticalDataHatchMachine(holder, true),
            "optical_data_hatch", PartAbility.OPTICAL_DATA_TRANSMISSION)
            .localization(text -> text.source("Optical Data Transmission Hatch")
                    .translation(MinecraftLocale.ZH_CN, "光学数据源仓"))
            .tooltips(Component.translatable("gtpm.part_sharing.disabled"))
            .register();

    public static final MachineDefinition DATA_HATCH_RECEIVER = registerDataHatch(
            "data_receiver_hatch",
            LuV, (holder) -> new OpticalDataHatchMachine(holder, false),
            "optical_data_hatch", PartAbility.OPTICAL_DATA_RECEPTION)
            .localization(text -> text.source("Optical Data Reception Hatch")
                    .translation(MinecraftLocale.ZH_CN, "光学数据靶仓"))
            .tooltips(Component.translatable("gtpm.part_sharing.disabled"))
            .register();

    public static final MachineDefinition BASIC_DATA_ACCESS_HATCH = REGISTRATE
            .machine("basic_data_access_hatch", (holder) -> new DataAccessHatchMachine(holder, HV, false))
            .localization(text -> text
                    .source("Basic Data Access Hatch")
                    .translation(MinecraftLocale.ZH_CN, "基础数据访问仓"))
            .tier(HV)
            .rotationState(RotationState.ALL)
            .abilities(PartAbility.DATA_ACCESS)
            .tooltips(Component.translatable("gtpm.machine.data_access_hatch.tooltip.0"),
                    Component.translatable("gtpm.machine.data_access_hatch.tooltip.1", 4),
                    Component.translatable("gtpm.part_sharing.disabled"))
            .overlayTieredHullModel("data_access_hatch")
            .register();

    public static final MachineDefinition DATA_ACCESS_HATCH = REGISTRATE
            .machine("data_access_hatch", (holder) -> new DataAccessHatchMachine(holder, EV, false))
            .localization(text -> text
                    .source("Data Access Hatch")
                    .translation(MinecraftLocale.ZH_CN, "数据访问仓"))
            .tier(EV)
            .rotationState(RotationState.ALL)
            .abilities(PartAbility.DATA_ACCESS)
            .tooltips(Component.translatable("gtpm.machine.data_access_hatch.tooltip.0"),
                    Component.translatable("gtpm.machine.data_access_hatch.tooltip.1", 9),
                    Component.translatable("gtpm.part_sharing.disabled"))
            .overlayTieredHullModel("data_access_hatch")
            .register();

    public static final MachineDefinition ADVANCED_DATA_ACCESS_HATCH = REGISTRATE
            .machine("advanced_data_access_hatch", (holder) -> new DataAccessHatchMachine(holder, LuV, false))
            .localization(text -> text
                    .source("Advanced Data Access Hatch")
                    .translation(MinecraftLocale.ZH_CN, "高级数据访问仓"))
            .tier(LuV)
            .rotationState(RotationState.ALL)
            .abilities(PartAbility.DATA_ACCESS)
            .tooltips(Component.translatable("gtpm.machine.data_access_hatch.tooltip.0"),
                    Component.translatable("gtpm.machine.data_access_hatch.tooltip.1", 16),
                    Component.translatable("gtpm.part_sharing.disabled"))
            .overlayTieredHullModel("data_access_hatch")
            .register();

    public static final MachineDefinition CREATIVE_DATA_ACCESS_HATCH = REGISTRATE
            .machine("creative_data_access_hatch", (holder) -> new DataAccessHatchMachine(holder, MAX, true))
            .localization(text -> text
                    .source("Creative Data Access Hatch")
                    .translation(MinecraftLocale.ZH_CN, "创造模式数据访问仓"))
            .tier(MAX)
            .rotationState(RotationState.ALL)
            .abilities(PartAbility.DATA_ACCESS)
            .modelProperty(IS_FORMED, false)
            .tooltipBuilder((s, list) -> {
                list.add(Component.translatable("gtpm.machine.data_access_hatch.tooltip.0"));
                CREATIVE_TOOLTIPS.accept(s, list);
                list.add(Component.translatable("gtpm.part_sharing.enabled"));
            })
            .overlayTieredHullModel("data_access_hatch_creative")
            .register();

    //////////////////////////////////////
    // *********** HPCA ***********//
    //////////////////////////////////////

    public static final BiConsumer<ItemStack, List<Component>> OVERHEAT_TOOLTIPS = (stack, components) -> components
            .add(Component.translatable("gtpm.machine.hpca.component_type.damaged")
                    .withStyle(style -> style.withColor(TooltipHelper.BLINKING_ORANGE.getCurrent())));

    public static final MachineDefinition HPCA_EMPTY_COMPONENT = registerHPCAPart(
            "hpca_empty_component", HPCAEmptyPartMachine::new, "empty", false)
            .localization(text -> text.source("Empty HPCA Component")
                    .translation(MinecraftLocale.ZH_CN, "空HPCA组件"))
            .tooltips(Component.translatable("gtpm.part_sharing.disabled"))
            .register();
    public static final MachineDefinition HPCA_COMPUTATION_COMPONENT = registerHPCAPart(
            "hpca_computation_component",
            holder -> new HPCAComputationPartMachine(holder, false), "computation", false)
            .localization(text -> text.source("HPCA Computation Component")
                    .translation(MinecraftLocale.ZH_CN, "HPCA计算组件"))
            .tooltips(
                    Component.translatable("gtpm.machine.hpca.component_general.upkeep_eut", GTValues.VA[GTValues.EV]),
                    Component.translatable("gtpm.machine.hpca.component_general.max_eut", GTValues.VA[GTValues.LuV]),
                    Component.translatable("gtpm.machine.hpca.component_type.computation_cwut", 4),
                    Component.translatable("gtpm.machine.hpca.component_type.computation_cooling", 2),
                    Component.translatable("gtpm.part_sharing.disabled"))
            .tooltipBuilder(OVERHEAT_TOOLTIPS)
            .register();
    public static final MachineDefinition HPCA_ADVANCED_COMPUTATION_COMPONENT = registerHPCAPart(
            "hpca_advanced_computation_component",
            holder -> new HPCAComputationPartMachine(holder, true), "advanced_computation", true)
            .localization(text -> text.source("HPCA Advanced Computation Component")
                    .translation(MinecraftLocale.ZH_CN, "HPCA高级计算组件"))
            .tooltips(
                    Component.translatable("gtpm.machine.hpca.component_general.upkeep_eut", GTValues.VA[GTValues.IV]),
                    Component.translatable("gtpm.machine.hpca.component_general.max_eut", GTValues.VA[GTValues.ZPM]),
                    Component.translatable("gtpm.machine.hpca.component_type.computation_cwut", 16),
                    Component.translatable("gtpm.machine.hpca.component_type.computation_cooling", 4),
                    Component.translatable("gtpm.part_sharing.disabled"))
            .tooltipBuilder(OVERHEAT_TOOLTIPS)
            .register();
    public static final MachineDefinition HPCA_HEAT_SINK_COMPONENT = registerHPCAPart(
            "hpca_heat_sink_component",
            holder -> new HPCACoolerPartMachine(holder, false), "heat_sink", false)
            .localization(text -> text.source("HPCA Heat Sink Component")
                    .translation(MinecraftLocale.ZH_CN, "HPCA散热组件"))
            .tooltips(Component.translatable("gtpm.machine.hpca.component_type.cooler_passive"),
                    Component.translatable("gtpm.machine.hpca.component_type.cooler_cooling", 1),
                    Component.translatable("gtpm.part_sharing.disabled"))
            .register();
    public static final MachineDefinition HPCA_ACTIVE_COOLER_COMPONENT = registerHPCAPart(
            "hpca_active_cooler_component",
            holder -> new HPCACoolerPartMachine(holder, true), "active_cooler", true)
            .localization(text -> text.source("HPCA Active Cooling Component")
                    .translation(MinecraftLocale.ZH_CN, "HPCA主动冷却组件"))
            .tooltips(Component.translatable("gtpm.machine.hpca.component_general.max_eut", GTValues.VA[GTValues.IV]),
                    Component.translatable("gtpm.machine.hpca.component_type.cooler_active"),
                    Component.translatable("gtpm.machine.hpca.component_type.cooler_active_coolant",
                            8, GTMaterials.PCBCoolant.getLocalizedName()),
                    Component.translatable("gtpm.machine.hpca.component_type.cooler_cooling", 2),
                    Component.translatable("gtpm.part_sharing.disabled"))
            .register();
    public static final MachineDefinition HPCA_BRIDGE_COMPONENT = registerHPCAPart(
            "hpca_bridge_component", HPCABridgePartMachine::new, "bridge", false)
            .localization(text -> text.source("HPCA Bridge Component")
                    .translation(MinecraftLocale.ZH_CN, "HPCA桥接组件"))
            .tooltips(Component.translatable("gtpm.machine.hpca.component_type.bridge"),
                    Component.translatable("gtpm.machine.hpca.component_general.max_eut", GTValues.VA[GTValues.IV]),
                    Component.translatable("gtpm.part_sharing.disabled"))
            .register();

    @NotNull
    private static MachineBuilder<MachineDefinition, ?> registerDataHatch(String name, int tier,
                                                                          Function<BlockEntityCreationInfo, MetaMachine> constructor,
                                                                          String model, PartAbility... abilities) {
        return REGISTRATE.machine(name, constructor)
                .tier(tier)
                .rotationState(RotationState.ALL)
                .abilities(abilities)
                .overlayTieredHullModel(model);
    }

    private static MachineBuilder<MachineDefinition, ?> registerHPCAPart(String name,
                                                                         Function<BlockEntityCreationInfo, MetaMachine> constructor,
                                                                         String texture, boolean isAdvanced) {
        return REGISTRATE.machine(name, constructor)
                .rotationState(RotationState.ALL)
                .abilities(PartAbility.HPCA_COMPONENT)
                .modelProperty(GTMachineModelProperties.IS_FORMED, false)
                .modelProperty(GTMachineModelProperties.IS_HPCA_PART_DAMAGED, false)
                .modelProperty(GTMachineModelProperties.IS_ACTIVE, false)
                .model(createHPCAPartModel(isAdvanced,
                        GTPM.id("block/overlay/machine/hpca/" + texture),
                        GTPM.id("block/overlay/machine/hpca/damaged" + (isAdvanced ? "_advanced" : ""))));
    }

    public static void init() {}
}
