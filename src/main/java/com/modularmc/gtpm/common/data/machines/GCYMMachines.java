package com.modularmc.gtpm.common.data.machines;

import com.modularmc.gtpm.GTPM;
import com.modularmc.gtpm.api.GTValues;
import com.modularmc.gtpm.api.data.RotationState;
import com.modularmc.gtpm.api.machine.MachineDefinition;
import com.modularmc.gtpm.api.machine.MultiblockMachineDefinition;
import com.modularmc.gtpm.api.machine.feature.multiblock.IMultiPart;
import com.modularmc.gtpm.api.machine.multiblock.CoilWorkableElectricMultiblockMachine;
import com.modularmc.gtpm.api.machine.multiblock.MultiblockControllerMachine;
import com.modularmc.gtpm.api.machine.multiblock.PartAbility;
import com.modularmc.gtpm.api.machine.multiblock.WorkableElectricMultiblockMachine;
import com.modularmc.gtpm.api.machine.property.GTMachineModelProperties;
import com.modularmc.gtpm.api.machine.trait.WorkLogic;
import com.modularmc.gtpm.api.multiblock.pattern.dsl.PatternBuilder;
import com.modularmc.gtpm.client.renderer.machine.DynamicRenderHelper;
import com.modularmc.gtpm.common.data.GTRecipeModifiers;
import com.modularmc.gtpm.common.data.GTRecipeTypes;
import com.modularmc.gtpm.common.data.machines.tooltip.MachineTooltips;
import com.modularmc.gtpm.common.machine.multiblock.electric.DistillationTowerMachine;
import com.modularmc.gtpm.common.machine.multiblock.electric.gcym.*;
import com.modularmc.gtpm.common.machine.multiblock.part.ParallelHatchPartMachine;
import com.modularmc.gtpm.config.ConfigHolder;
import com.modularmc.gtpm.utils.FormattingUtil;

import com.modularmc.registrate.localization.MinecraftLocale;
import com.modularmc.registrate.localization.annotation.TransVariants;
import com.modularmc.registrate.localization.annotation.Translation;
import com.modularmc.registrate.localization.annotation.Variant;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;

import java.util.Comparator;

import static com.modularmc.gtpm.api.GTValues.*;
import static com.modularmc.gtpm.api.machine.property.GTMachineModelProperties.IS_FORMED;
import static com.modularmc.gtpm.api.multiblock.util.RelativeDirection.*;
import static com.modularmc.gtpm.common.data.GCYMBlocks.*;
import static com.modularmc.gtpm.common.data.GCYMRecipeTypes.ALLOY_BLAST_RECIPES;
import static com.modularmc.gtpm.common.data.GTBlocks.*;
import static com.modularmc.gtpm.common.data.GTRecipeModifiers.*;
import static com.modularmc.gtpm.common.data.GTRecipeTypes.*;
import static com.modularmc.gtpm.common.data.machines.GTMachineUtils.registerTieredMachines;
import static com.modularmc.gtpm.common.data.models.GTMachineModels.*;
import static com.modularmc.gtpm.common.registry.GTRegistration.REGISTRATE;

@TransVariants(keyPrefix = "gtpm",
               value = {
                       @Variant(id = "multiblock.steam.duration_modifier",
                                source = "Takes §f1.5x §7base duration to process, not affected by number of items.",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "处理耗时为基础的§f1.5§7倍，不受物品数量影响。")),
                       @Variant(id = "multiblock.require_steam_parts",
                                source = "Requires Steam Hatches and Buses!",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "需要蒸汽仓与输入/输出总线（蒸汽）！")),
               })
public class GCYMMachines {

    public static void init() {}

    @TransVariants(keyPrefix = "gtpm.machine",
                   value = {
                           @Variant(id = "parallel_hatch_mk5.tooltip",
                                    source = "Allows to run up to 4 recipes in parallel.",
                                    translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                                text = "允许同时处理至多4个配方。")),
                           @Variant(id = "parallel_hatch_mk6.tooltip",
                                    source = "Allows to run up to 16 recipes in parallel.",
                                    translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                                text = "允许同时处理至多16个配方。")),
                           @Variant(id = "parallel_hatch_mk7.tooltip",
                                    source = "Allows to run up to 64 recipes in parallel.",
                                    translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                                text = "允许同时处理至多64个配方。")),
                           @Variant(id = "parallel_hatch_mk8.tooltip",
                                    source = "Allows to run up to 256 recipes in parallel.",
                                    translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                                text = "允许同时处理至多256个配方。")),
                   })
    public static final MachineDefinition[] PARALLEL_HATCH = registerTieredMachines("parallel_hatch",
            ParallelHatchPartMachine::new,
            (tier, builder) -> builder
                    .localization(text -> text.source(switch (tier) {
                        case IV -> "Elite Parallel Control Hatch";
                        case LuV -> "Master Parallel Control Hatch";
                        case ZPM -> "Ultimate Parallel Control Hatch";
                        case UV -> "Super Parallel Control Hatch";
                        default -> throw new IllegalArgumentException(
                                "Unsupported parallel control hatch tier: " + tier);
                    }).translation(MinecraftLocale.ZH_CN, switch (tier) {
                        case IV -> "§9IV§r并行控制仓";
                        case LuV -> "§dLuV§r并行控制仓";
                        case ZPM -> "§cZPM§r并行控制仓";
                        case UV -> "§3UV§r并行控制仓";
                        default -> throw new IllegalArgumentException(
                                "Unsupported parallel control hatch tier: " + tier);
                    }))
                    .rotationState(RotationState.ALL)
                    .abilities(PartAbility.PARALLEL_HATCH)
                    .modelProperty(IS_FORMED, false)
                    .modelProperty(GTMachineModelProperties.RECIPE_LOGIC_STATUS, WorkLogic.Status.IDLE)
                    .model(createWorkableTieredHullMachineModel(
                            GTPM.id("block/machines/parallel_hatch_mk" + (tier - 4)))
                            .andThen((ctx, prov, model) -> {
                                model.addReplaceableTextures("bottom", "top", "side");
                            }))
                    .tooltips(Component.translatable("gtpm.machine.parallel_hatch_mk" + tier + ".tooltip"),
                            Component.translatable("gtpm.part_sharing.disabled"))
                    .register(),
            IV, LuV, ZPM, UV);

    public final static MultiblockMachineDefinition LARGE_MACERATION_TOWER = REGISTRATE
            .multiblock("large_maceration_tower", LargeMacerationTowerMachine::new)
            .localization(text -> text
                    .source("Large Maceration Tower")
                    .translation(MinecraftLocale.ZH_CN, "大型研磨塔"))
            .tooltips(MachineTooltips.parallelizable(), MachineTooltips.recipeTypes(MACERATOR_RECIPES.getName()))
            .rotationState(RotationState.ALL)
            .recipeType(MACERATOR_RECIPES)
            .recipeModifiers(GTRecipeModifiers.PARALLEL_HATCH, OC_NON_PERFECT_SUBTICK, BATCH_MODE)
            .appearanceBlock(CASING_SECURE_MACERATION)
            .pattern(MultiblockControllerMachine.DEFAULT_STRUCTURE, definition -> PatternBuilder.start(definition)
                    .appendDefinition()
                    .build())
            .workableCasingModel(GTPM.id("block/casings/gcym/secure_maceration_casing"),
                    GTPM.id("block/multiblock/gcym/large_maceration_tower"))
            .register();

    public final static MultiblockMachineDefinition LARGE_CHEMICAL_BATH = REGISTRATE
            .multiblock("large_chemical_bath", LargeChemicalBathMachine::new)
            .localization(text -> text
                    .source("Large Chemical Bath")
                    .translation(MinecraftLocale.ZH_CN, "大型化学浸洗机"))
            .tooltips(MachineTooltips.parallelizable(),
                    MachineTooltips.recipeTypes(ORE_WASHER_RECIPES.getName(), CHEMICAL_BATH_RECIPES.getName()))
            .rotationState(RotationState.ALL)
            .recipeTypes(CHEMICAL_BATH_RECIPES, ORE_WASHER_RECIPES)
            .recipeModifiers(GTRecipeModifiers.PARALLEL_HATCH, OC_NON_PERFECT_SUBTICK, BATCH_MODE)
            .appearanceBlock(CASING_WATERTIGHT)
            .pattern(MultiblockControllerMachine.DEFAULT_STRUCTURE, definition -> PatternBuilder.start(definition)
                    .appendDefinition()
                    .build())
            .hasBER(true)
            .modelProperty(GTMachineModelProperties.RECIPE_LOGIC_STATUS, WorkLogic.Status.IDLE)
            .model(createWorkableCasingMachineModel(GTPM.id("block/casings/gcym/watertight_casing"),
                    GTPM.id("block/multiblock/gcym/large_chemical_bath"))
                    .andThen(b -> b.addDynamicRenderer(DynamicRenderHelper::makeRecipeFluidAreaRender)))
            .register();

    public final static MultiblockMachineDefinition LARGE_CENTRIFUGE = REGISTRATE
            .multiblock("large_centrifuge", WorkableElectricMultiblockMachine::new)
            .localization(text -> text
                    .source("Large Centrifugal Unit")
                    .translation(MinecraftLocale.ZH_CN, "大型离心机组"))
            .tooltips(MachineTooltips.parallelizable(),
                    MachineTooltips.recipeTypes(CENTRIFUGE_RECIPES.getName(), THERMAL_CENTRIFUGE_RECIPES.getName()))
            .rotationState(RotationState.ALL)
            .recipeTypes(CENTRIFUGE_RECIPES, THERMAL_CENTRIFUGE_RECIPES)
            .recipeModifiers(GTRecipeModifiers.PARALLEL_HATCH, OC_NON_PERFECT_SUBTICK, BATCH_MODE)
            .appearanceBlock(CASING_VIBRATION_SAFE)
            .pattern(MultiblockControllerMachine.DEFAULT_STRUCTURE, definition -> PatternBuilder.start(definition)
                    .appendDefinition()
                    .build())
            .workableCasingModel(GTPM.id("block/casings/gcym/vibration_safe_casing"),
                    GTPM.id("block/multiblock/gcym/large_centrifuge"))
            .register();

    public final static MultiblockMachineDefinition LARGE_MIXER = REGISTRATE
            .multiblock("large_mixer", LargeMixerMachine::new)
            .localization(text -> text
                    .source("Large Mixing Vessel")
                    .translation(MinecraftLocale.ZH_CN, "大型搅拌罐"))
            .tooltips(MachineTooltips.parallelizable(), MachineTooltips.recipeTypes(MIXER_RECIPES.getName()))
            .rotationState(RotationState.ALL)
            .recipeType(MIXER_RECIPES)
            .recipeModifiers(GTRecipeModifiers.PARALLEL_HATCH, OC_NON_PERFECT_SUBTICK, BATCH_MODE)
            .appearanceBlock(CASING_REACTION_SAFE)
            .pattern(MultiblockControllerMachine.DEFAULT_STRUCTURE, definition -> PatternBuilder.start(definition)
                    .appendDefinition()
                    .build())
            .hasBER(true)
            .modelProperty(GTMachineModelProperties.RECIPE_LOGIC_STATUS, WorkLogic.Status.IDLE)
            .model(createWorkableCasingMachineModel(GTPM.id("block/casings/gcym/reaction_safe_mixing_casing"),
                    GTPM.id("block/multiblock/gcym/large_mixer"))
                    .andThen(b -> b.addDynamicRenderer(DynamicRenderHelper::makeRecipeFluidAreaRender)))
            .register();

    public final static MultiblockMachineDefinition LARGE_ELECTROLYZER = REGISTRATE
            .multiblock("large_electrolyzer", WorkableElectricMultiblockMachine::new)
            .localization(text -> text
                    .source("Large Electrolysis Chamber")
                    .translation(MinecraftLocale.ZH_CN, "大型电解槽"))
            .tooltips(MachineTooltips.parallelizable(), MachineTooltips.recipeTypes(ELECTROLYZER_RECIPES.getName()))
            .rotationState(RotationState.ALL)
            .recipeType(ELECTROLYZER_RECIPES)
            .recipeModifiers(GTRecipeModifiers.PARALLEL_HATCH, OC_NON_PERFECT_SUBTICK, BATCH_MODE)
            .appearanceBlock(CASING_NONCONDUCTING)
            .pattern(MultiblockControllerMachine.DEFAULT_STRUCTURE, definition -> PatternBuilder.start(definition)
                    .appendDefinition()
                    .build())
            .workableCasingModel(GTPM.id("block/casings/gcym/nonconducting_casing"),
                    GTPM.id("block/multiblock/gcym/large_electrolyzer"))
            .register();

    public final static MultiblockMachineDefinition LARGE_ELECTROMAGNET = REGISTRATE
            .multiblock("large_electromagnet", WorkableElectricMultiblockMachine::new)
            .localization(text -> text
                    .source("Large Electromagnet")
                    .translation(MinecraftLocale.ZH_CN, "大型电磁工厂"))
            .tooltips(MachineTooltips.parallelizable(),
                    MachineTooltips.recipeTypes(ELECTROMAGNETIC_SEPARATOR_RECIPES.getName(),
                            POLARIZER_RECIPES.getName()))
            .rotationState(RotationState.ALL)
            .recipeTypes(ELECTROMAGNETIC_SEPARATOR_RECIPES, POLARIZER_RECIPES)
            .recipeModifiers(GTRecipeModifiers.PARALLEL_HATCH, OC_NON_PERFECT_SUBTICK, BATCH_MODE)
            .appearanceBlock(CASING_NONCONDUCTING)
            .pattern(MultiblockControllerMachine.DEFAULT_STRUCTURE, definition -> PatternBuilder.start(definition)
                    .appendDefinition()
                    .build())
            .workableCasingModel(GTPM.id("block/casings/gcym/nonconducting_casing"),
                    GTPM.id("block/multiblock/gcym/large_electrolyzer"))
            .register();

    public final static MultiblockMachineDefinition LARGE_PACKER = REGISTRATE
            .multiblock("large_packer", WorkableElectricMultiblockMachine::new)
            .localization(text -> text
                    .source("Large Packaging Machine")
                    .translation(MinecraftLocale.ZH_CN, "大型打包机"))
            .tooltips(MachineTooltips.parallelizable(), MachineTooltips.recipeTypes(PACKER_RECIPES.getName()))
            .rotationState(RotationState.ALL)
            .recipeType(GTRecipeTypes.PACKER_RECIPES)
            .recipeModifiers(GTRecipeModifiers.PARALLEL_HATCH, OC_NON_PERFECT_SUBTICK, BATCH_MODE)
            .appearanceBlock(CASING_TUNGSTENSTEEL_ROBUST)
            .pattern(MultiblockControllerMachine.DEFAULT_STRUCTURE, definition -> PatternBuilder.start(definition)
                    .appendDefinition()
                    .build())
            .workableCasingModel(GTPM.id("block/casings/solid/machine_casing_robust_tungstensteel"),
                    GTPM.id("block/multiblock/gcym/large_packer"))
            .register();

    public final static MultiblockMachineDefinition LARGE_ASSEMBLER = REGISTRATE
            .multiblock("large_assembler", WorkableElectricMultiblockMachine::new)
            .localization(text -> text
                    .source("Large Assembling Factory")
                    .translation(MinecraftLocale.ZH_CN, "大型组装厂"))
            .tooltips(MachineTooltips.parallelizable(), MachineTooltips.recipeTypes(ASSEMBLER_RECIPES.getName()))
            .tooltips(MachineTooltips.singleEnergyHatch())
            .conditionalTooltip(GTMachineUtils.defaultEnvironmentRequirement(),
                    ConfigHolder.INSTANCE.gameplay.environmentalHazards)
            .rotationState(RotationState.ALL)
            .recipeType(ASSEMBLER_RECIPES)
            .recipeModifiers(DEFAULT_ENVIRONMENT_REQUIREMENT, GTRecipeModifiers.PARALLEL_HATCH, OC_NON_PERFECT_SUBTICK,
                    BATCH_MODE)
            .appearanceBlock(CASING_LARGE_SCALE_ASSEMBLING)
            .pattern(MultiblockControllerMachine.DEFAULT_STRUCTURE, definition -> PatternBuilder.start(definition)
                    .appendDefinition()
                    .build())
            .workableCasingModel(GTPM.id("block/casings/gcym/large_scale_assembling_casing"),
                    GTPM.id("block/multiblock/gcym/large_assembler"))
            .register();

    public final static MultiblockMachineDefinition LARGE_CIRCUIT_ASSEMBLER = REGISTRATE
            .multiblock("large_circuit_assembler", WorkableElectricMultiblockMachine::new)
            .localization(text -> text
                    .source("Large Circuit Assembling Facility")
                    .translation(MinecraftLocale.ZH_CN, "大型电路组装设施"))
            .tooltips(MachineTooltips.parallelizable(),
                    MachineTooltips.recipeTypes(CIRCUIT_ASSEMBLER_RECIPES.getName()))
            .tooltips(MachineTooltips.singleEnergyHatch())
            .conditionalTooltip(GTMachineUtils.defaultEnvironmentRequirement(),
                    ConfigHolder.INSTANCE.gameplay.environmentalHazards)
            .rotationState(RotationState.ALL)
            .recipeType(CIRCUIT_ASSEMBLER_RECIPES)
            .recipeModifiers(DEFAULT_ENVIRONMENT_REQUIREMENT, GTRecipeModifiers.PARALLEL_HATCH, OC_NON_PERFECT_SUBTICK,
                    BATCH_MODE)
            .appearanceBlock(CASING_LARGE_SCALE_ASSEMBLING)
            .pattern(MultiblockControllerMachine.DEFAULT_STRUCTURE, definition -> PatternBuilder.start(definition)
                    .appendDefinition()
                    .build())
            .workableCasingModel(GTPM.id("block/casings/gcym/large_scale_assembling_casing"),
                    GTPM.id("block/multiblock/gcym/large_circuit_assembler"))
            .register();

    public final static MultiblockMachineDefinition LARGE_ARC_SMELTER = REGISTRATE
            .multiblock("large_arc_smelter", WorkableElectricMultiblockMachine::new)
            .localization(text -> text
                    .source("Large Arc Smelter")
                    .translation(MinecraftLocale.ZH_CN, "大型电弧炉"))
            .tooltips(MachineTooltips.parallelizable(), MachineTooltips.recipeTypes(ARC_FURNACE_RECIPES.getName()))
            .rotationState(RotationState.ALL)
            .recipeType(ARC_FURNACE_RECIPES)
            .recipeModifiers(GTRecipeModifiers.PARALLEL_HATCH, OC_NON_PERFECT_SUBTICK, BATCH_MODE)
            .appearanceBlock(CASING_HIGH_TEMPERATURE_SMELTING)
            .pattern(MultiblockControllerMachine.DEFAULT_STRUCTURE, definition -> PatternBuilder.start(definition)
                    .appendDefinition()
                    .build())
            .workableCasingModel(GTPM.id("block/casings/gcym/high_temperature_smelting_casing"),
                    GTPM.id("block/multiblock/gcym/large_arc_smelter"))
            .register();

    public final static MultiblockMachineDefinition LARGE_ENGRAVING_LASER = REGISTRATE
            .multiblock("large_engraving_laser", WorkableElectricMultiblockMachine::new)
            .localization(text -> text
                    .source("Large Engraving Laser")
                    .translation(MinecraftLocale.ZH_CN, "大型激光蚀刻机"))
            .tooltips(MachineTooltips.parallelizable(), MachineTooltips.recipeTypes(LASER_ENGRAVER_RECIPES.getName()))
            .conditionalTooltip(GTMachineUtils.defaultEnvironmentRequirement(),
                    ConfigHolder.INSTANCE.gameplay.environmentalHazards)
            .rotationState(RotationState.ALL)
            .recipeType(LASER_ENGRAVER_RECIPES)
            .recipeModifiers(DEFAULT_ENVIRONMENT_REQUIREMENT, GTRecipeModifiers.PARALLEL_HATCH, OC_NON_PERFECT_SUBTICK,
                    BATCH_MODE)
            .appearanceBlock(CASING_LASER_SAFE_ENGRAVING)
            .pattern(MultiblockControllerMachine.DEFAULT_STRUCTURE, definition -> PatternBuilder.start(definition)
                    .appendDefinition()
                    .build())
            .workableCasingModel(GTPM.id("block/casings/gcym/laser_safe_engraving_casing"),
                    GTPM.id("block/multiblock/gcym/large_engraving_laser"))
            .register();

    public final static MultiblockMachineDefinition LARGE_SIFTING_FUNNEL = REGISTRATE
            .multiblock("large_sifting_funnel", WorkableElectricMultiblockMachine::new)
            .localization(text -> text
                    .source("Large Sifting Funnel")
                    .translation(MinecraftLocale.ZH_CN, "大型筛选漏斗"))
            .tooltips(MachineTooltips.parallelizable(), MachineTooltips.recipeTypes(SIFTER_RECIPES.getName()))
            .rotationState(RotationState.ALL)
            .recipeType(SIFTER_RECIPES)
            .recipeModifiers(GTRecipeModifiers.PARALLEL_HATCH, OC_NON_PERFECT_SUBTICK, BATCH_MODE)
            .appearanceBlock(CASING_VIBRATION_SAFE)
            .pattern(MultiblockControllerMachine.DEFAULT_STRUCTURE, definition -> PatternBuilder.start(definition)
                    .appendDefinition()
                    .build())
            .workableCasingModel(GTPM.id("block/casings/gcym/vibration_safe_casing"),
                    GTPM.id("block/multiblock/gcym/large_sifting_funnel"))
            .register();

    public final static MultiblockMachineDefinition BLAST_ALLOY_SMELTER = REGISTRATE
            .multiblock("alloy_blast_smelter", CoilWorkableElectricMultiblockMachine::new)
            .localization(text -> text
                    .source("Alloy Blast Smelter")
                    .translation(MinecraftLocale.ZH_CN, "合金冶炼炉"))
            .tooltips(MachineTooltips.recipeTypes(ALLOY_BLAST_RECIPES.getName()))
            .tooltips(MachineTooltips.ELECTRIC_BLAST_FURNACE.get())
            .rotationState(RotationState.ALL)
            .recipeType(ALLOY_BLAST_RECIPES)
            .recipeModifiers(GTRecipeModifiers::ebfOverclock)
            .appearanceBlock(CASING_HIGH_TEMPERATURE_SMELTING)
            .pattern(MultiblockControllerMachine.DEFAULT_STRUCTURE, definition -> PatternBuilder.start(definition)
                    .appendDefinition()
                    .build())
            .workableCasingModel(GTPM.id("block/casings/gcym/high_temperature_smelting_casing"),
                    GTPM.id("block/multiblock/gcym/blast_alloy_smelter"))
            .additionalDisplay((controller, components) -> {
                if (controller instanceof CoilWorkableElectricMultiblockMachine coilMachine && controller.isFormed()) {
                    components.add(Component.translatable("gtpm.multiblock.blast_furnace.max_temperature",
                            Component
                                    .translatable(
                                            FormattingUtil
                                                    .formatNumbers(coilMachine.getCoilType().getCoilTemperature() +
                                                            100L * Math.max(0, coilMachine.getTier() - GTValues.MV)) +
                                                    "K")
                                    .setStyle(Style.EMPTY.withColor(ChatFormatting.RED))));
                }
            })
            .register();

    public final static MultiblockMachineDefinition LARGE_AUTOCLAVE = REGISTRATE
            .multiblock("large_autoclave", WorkableElectricMultiblockMachine::new)
            .localization(text -> text
                    .source("Large Crystallization Chamber")
                    .translation(MinecraftLocale.ZH_CN, "大型高压结晶室"))
            .tooltips(MachineTooltips.parallelizable(), MachineTooltips.recipeTypes(AUTOCLAVE_RECIPES.getName()))
            .rotationState(RotationState.ALL)
            .recipeType(AUTOCLAVE_RECIPES)
            .recipeModifiers(GTRecipeModifiers.PARALLEL_HATCH, OC_NON_PERFECT_SUBTICK, BATCH_MODE)
            .appearanceBlock(CASING_WATERTIGHT)
            .pattern(MultiblockControllerMachine.DEFAULT_STRUCTURE, definition -> PatternBuilder.start(definition)
                    .appendDefinition()
                    .build())
            .workableCasingModel(GTPM.id("block/casings/gcym/watertight_casing"),
                    GTPM.id("block/multiblock/gcym/large_autoclave"))
            .register();

    public final static MultiblockMachineDefinition LARGE_MATERIAL_PRESS = REGISTRATE
            .multiblock("large_material_press", WorkableElectricMultiblockMachine::new)
            .localization(text -> text
                    .source("Large Material Press")
                    .translation(MinecraftLocale.ZH_CN, "大型挤压机"))
            .tooltips(MachineTooltips.parallelizable(), MachineTooltips.recipeTypes(
                    BENDER_RECIPES.getName(), COMPRESSOR_RECIPES.getName(),
                    FORGE_HAMMER_RECIPES.getName(), FORMING_PRESS_RECIPES.getName()))
            .rotationState(RotationState.ALL)
            .recipeTypes(BENDER_RECIPES, COMPRESSOR_RECIPES, FORGE_HAMMER_RECIPES, FORMING_PRESS_RECIPES)
            .recipeModifiers(GTRecipeModifiers.PARALLEL_HATCH, OC_NON_PERFECT_SUBTICK, BATCH_MODE)
            .appearanceBlock(CASING_STRESS_PROOF)
            .pattern(MultiblockControllerMachine.DEFAULT_STRUCTURE, definition -> PatternBuilder.start(definition)
                    .appendDefinition()
                    .build())
            .workableCasingModel(GTPM.id("block/casings/gcym/stress_proof_casing"),
                    GTPM.id("block/multiblock/gcym/large_material_press"))
            .register();

    public final static MultiblockMachineDefinition LARGE_BREWER = REGISTRATE
            .multiblock("large_brewer", WorkableElectricMultiblockMachine::new)
            .localization(text -> text
                    .source("Large Brewing Vat")
                    .translation(MinecraftLocale.ZH_CN, "大型酿造厂"))
            .tooltips(MachineTooltips.parallelizable(), MachineTooltips.recipeTypes(
                    BREWING_RECIPES.getName(), FERMENTING_RECIPES.getName(), FLUID_HEATER_RECIPES.getName()))
            .rotationState(RotationState.ALL)
            .recipeTypes(BREWING_RECIPES, FERMENTING_RECIPES, FLUID_HEATER_RECIPES)
            .recipeModifiers(GTRecipeModifiers.PARALLEL_HATCH, OC_NON_PERFECT_SUBTICK, BATCH_MODE)
            .appearanceBlock(CASING_CORROSION_PROOF)
            .pattern(MultiblockControllerMachine.DEFAULT_STRUCTURE, definition -> PatternBuilder.start(definition)
                    .appendDefinition()
                    .build())
            .workableCasingModel(GTPM.id("block/casings/gcym/corrosion_proof_casing"),
                    GTPM.id("block/multiblock/gcym/large_brewer"))
            .register();

    public final static MultiblockMachineDefinition LARGE_CUTTER = REGISTRATE
            .multiblock("large_cutter", WorkableElectricMultiblockMachine::new)
            .localization(text -> text
                    .source("Large Cutting Saw")
                    .translation(MinecraftLocale.ZH_CN, "大型切割机"))
            .tooltips(MachineTooltips.parallelizable(),
                    MachineTooltips.recipeTypes(CUTTER_RECIPES.getName(), LATHE_RECIPES.getName()))
            .rotationState(RotationState.ALL)
            .recipeTypes(CUTTER_RECIPES, LATHE_RECIPES)
            .recipeModifiers(GTRecipeModifiers.PARALLEL_HATCH, OC_NON_PERFECT_SUBTICK, BATCH_MODE)
            .appearanceBlock(CASING_SHOCK_PROOF)
            .pattern(MultiblockControllerMachine.DEFAULT_STRUCTURE, definition -> PatternBuilder.start(definition)
                    .appendDefinition()
                    .build())
            .workableCasingModel(GTPM.id("block/casings/gcym/shock_proof_cutting_casing"),
                    GTPM.id("block/multiblock/gcym/large_cutter"))
            .register();

    public final static MultiblockMachineDefinition LARGE_DISTILLERY = REGISTRATE
            .multiblock("large_distillery", DistillationTowerMachine::new)
            .localization(text -> text
                    .source("Large Fractionating Distillery")
                    .translation(MinecraftLocale.ZH_CN, "大型分馏蒸馏厂"))
            .tooltips(MachineTooltips.parallelizable(),
                    MachineTooltips.recipeTypes(DISTILLATION_RECIPES.getName(), DISTILLERY_RECIPES.getName()))
            .rotationState(RotationState.NON_Y_AXIS)
            .recipeTypes(DISTILLATION_RECIPES, DISTILLERY_RECIPES)
            .recipeModifiers(GTRecipeModifiers.PARALLEL_HATCH, OC_NON_PERFECT_SUBTICK, BATCH_MODE)
            .appearanceBlock(CASING_WATERTIGHT)
            .pattern(MultiblockControllerMachine.DEFAULT_STRUCTURE, definition -> PatternBuilder
                    .start(definition, RIGHT, BACK, UP)
                    .appendDefinition()
                    .build())
            .allowExtendedFacing(false)
            .partSorter(Comparator.comparingInt((IMultiPart p) -> p.self().getBlockPos().getY()))
            .workableCasingModel(GTPM.id("block/casings/gcym/watertight_casing"),
                    GTPM.id("block/multiblock/gcym/large_distillery"))
            .register();

    public final static MultiblockMachineDefinition LARGE_EXTRACTOR = REGISTRATE
            .multiblock("large_extractor", WorkableElectricMultiblockMachine::new)
            .localization(text -> text
                    .source("Large Extraction Machine")
                    .translation(MinecraftLocale.ZH_CN, "大型提取机"))
            .tooltips(MachineTooltips.parallelizable(),
                    MachineTooltips.recipeTypes(EXTRACTOR_RECIPES.getName(), CANNER_RECIPES.getName()))
            .rotationState(RotationState.ALL)
            .recipeTypes(EXTRACTOR_RECIPES, CANNER_RECIPES)
            .recipeModifiers(GTRecipeModifiers.PARALLEL_HATCH, OC_NON_PERFECT_SUBTICK, BATCH_MODE)
            .appearanceBlock(CASING_WATERTIGHT)
            .pattern(MultiblockControllerMachine.DEFAULT_STRUCTURE, definition -> PatternBuilder.start(definition)
                    .appendDefinition()
                    .build())
            .workableCasingModel(GTPM.id("block/casings/gcym/watertight_casing"),
                    GTPM.id("block/multiblock/gcym/large_extractor"))
            .register();

    public final static MultiblockMachineDefinition LARGE_EXTRUDER = REGISTRATE
            .multiblock("large_extruder", WorkableElectricMultiblockMachine::new)
            .localization(text -> text
                    .source("Large Extrusion Machine")
                    .translation(MinecraftLocale.ZH_CN, "大型压模机"))
            .tooltips(MachineTooltips.parallelizable(), MachineTooltips.recipeTypes(EXTRUDER_RECIPES.getName()))
            .rotationState(RotationState.ALL)
            .recipeType(EXTRUDER_RECIPES)
            .recipeModifiers(GTRecipeModifiers.PARALLEL_HATCH, OC_NON_PERFECT_SUBTICK, BATCH_MODE)
            .appearanceBlock(CASING_STRESS_PROOF)
            .pattern(MultiblockControllerMachine.DEFAULT_STRUCTURE, definition -> PatternBuilder.start(definition)
                    .appendDefinition()
                    .build())
            .workableCasingModel(GTPM.id("block/casings/gcym/stress_proof_casing"),
                    GTPM.id("block/multiblock/gcym/large_extruder"))
            .register();

    public final static MultiblockMachineDefinition LARGE_SOLIDIFIER = REGISTRATE
            .multiblock("large_solidifier", WorkableElectricMultiblockMachine::new)
            .localization(text -> text
                    .source("Large Solidification Array")
                    .translation(MinecraftLocale.ZH_CN, "大型固化阵列"))
            .tooltips(MachineTooltips.parallelizable(),
                    MachineTooltips.recipeTypes(FLUID_SOLIDFICATION_RECIPES.getName()))
            .rotationState(RotationState.ALL)
            .recipeType(FLUID_SOLIDFICATION_RECIPES)
            .recipeModifiers(GTRecipeModifiers.PARALLEL_HATCH, OC_NON_PERFECT_SUBTICK, BATCH_MODE)
            .appearanceBlock(CASING_WATERTIGHT)
            .pattern(MultiblockControllerMachine.DEFAULT_STRUCTURE, definition -> PatternBuilder.start(definition)
                    .appendDefinition()
                    .build())
            .workableCasingModel(GTPM.id("block/casings/gcym/watertight_casing"),
                    GTPM.id("block/multiblock/gcym/large_solidifier"))
            .register();

    public final static MultiblockMachineDefinition LARGE_WIREMILL = REGISTRATE
            .multiblock("large_wiremill", WorkableElectricMultiblockMachine::new)
            .localization(text -> text
                    .source("Large Wire Factory")
                    .translation(MinecraftLocale.ZH_CN, "大型线材工厂"))
            .tooltips(MachineTooltips.parallelizable(), MachineTooltips.recipeTypes(WIREMILL_RECIPES.getName()))
            .rotationState(RotationState.ALL)
            .recipeType(WIREMILL_RECIPES)
            .recipeModifiers(GTRecipeModifiers.PARALLEL_HATCH, OC_NON_PERFECT_SUBTICK, BATCH_MODE)
            .appearanceBlock(CASING_STRESS_PROOF)
            .pattern(MultiblockControllerMachine.DEFAULT_STRUCTURE, definition -> PatternBuilder.start(definition)
                    .appendDefinition()
                    .build())
            .workableCasingModel(GTPM.id("block/casings/gcym/stress_proof_casing"),
                    GTPM.id("block/multiblock/gcym/large_wiremill"))
            .register();

    // spotless:off
    public static final MultiblockMachineDefinition ROTARY_HEARTH_FURNACE = REGISTRATE
            .multiblock("rotary_hearth_furnace", CoilWorkableElectricMultiblockMachine::new)
            .localization(text -> text
                    .source("Rotary Hearth Furnace")
                    .translation(MinecraftLocale.ZH_CN, "转底炉"))
            .tooltips(MachineTooltips.parallelizable(), MachineTooltips.recipeTypes(BLAST_RECIPES.getName()))
            .tooltips(MachineTooltips.ELECTRIC_BLAST_FURNACE.get())
            .rotationState(RotationState.ALL)
            .recipeType(BLAST_RECIPES)
            .recipeModifiers(GTRecipeModifiers.PARALLEL_HATCH, GTRecipeModifiers::ebfOverclock, BATCH_MODE)
            .appearanceBlock(CASING_HIGH_TEMPERATURE_SMELTING)
            .pattern(MultiblockControllerMachine.DEFAULT_STRUCTURE, definition -> PatternBuilder.start(definition)
                    .appendDefinition()
                    .build())
            .workableCasingModel(GTPM.id("block/casings/gcym/high_temperature_smelting_casing"),
                    GTPM.id("block/multiblock/gcym/rotary_hearth_furnace"))
            .additionalDisplay((controller, components) -> {
                if (controller instanceof CoilWorkableElectricMultiblockMachine coilMachine && controller.isFormed()) {
                    components.add(Component.translatable("gtpm.multiblock.blast_furnace.max_temperature",
                            Component.translatable(
                                    FormattingUtil.formatNumbers(coilMachine.getCoilType().getCoilTemperature() +
                                            100L * Math.max(0, coilMachine.getTier() - GTValues.MV)) + "K")
                                    .setStyle(Style.EMPTY.withColor(ChatFormatting.RED))));
                }
            })
            .register();

    public final static MultiblockMachineDefinition MEGA_VACUUM_FREEZER = REGISTRATE
            .multiblock("mega_vacuum_freezer", WorkableElectricMultiblockMachine::new)
            .localization(text -> text
                    .source("Bulk Blast Chiller")
                    .translation(MinecraftLocale.ZH_CN, "巨型真空冷冻机"))
            .tooltips(MachineTooltips.parallelizable(), MachineTooltips.recipeTypes(VACUUM_RECIPES.getName()))
            .rotationState(RotationState.ALL)
            .recipeType(VACUUM_RECIPES)
            .recipeModifiers(GTRecipeModifiers.PARALLEL_HATCH, OC_NON_PERFECT_SUBTICK, BATCH_MODE)
            .appearanceBlock(CASING_ALUMINIUM_FROSTPROOF)
            .pattern(MultiblockControllerMachine.DEFAULT_STRUCTURE, definition -> PatternBuilder.start(definition)
                    .appendDefinition()
                    .build())
            .workableCasingModel(GTPM.id("block/casings/solid/machine_casing_frost_proof"),
                    GTPM.id("block/multiblock/gcym/mega_vacuum_freezer"))
            .register();
    // spotless:on
}
