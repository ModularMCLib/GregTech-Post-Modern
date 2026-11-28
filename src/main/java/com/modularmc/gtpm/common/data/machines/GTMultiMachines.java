package com.modularmc.gtpm.common.data.machines;

import com.modularmc.gtpm.GTPM;
import com.modularmc.gtpm.api.GTValues;
import com.modularmc.gtpm.api.capability.recipe.ItemRecipeCapability;
import com.modularmc.gtpm.api.data.RotationState;
import com.modularmc.gtpm.api.data.tag.TagPrefix;
import com.modularmc.gtpm.api.fluids.PropertyFluidFilter;
import com.modularmc.gtpm.api.machine.MachineDefinition;
import com.modularmc.gtpm.api.machine.MultiblockMachineDefinition;
import com.modularmc.gtpm.api.machine.feature.multiblock.IMultiPart;
import com.modularmc.gtpm.api.machine.multiblock.CoilWorkableElectricMultiblockMachine;
import com.modularmc.gtpm.api.machine.multiblock.MultiblockControllerMachine;
import com.modularmc.gtpm.api.machine.multiblock.WorkableElectricMultiblockMachine;
import com.modularmc.gtpm.api.machine.property.GTMachineModelProperties;
import com.modularmc.gtpm.api.machine.trait.RecipeLogic;
import com.modularmc.gtpm.api.machine.trait.WorkLogic;
import com.modularmc.gtpm.api.multiblock.pattern.dsl.PatternBuilder;
import com.modularmc.gtpm.client.renderer.machine.*;
import com.modularmc.gtpm.client.util.TooltipHelper;
import com.modularmc.gtpm.common.block.BoilerFireboxType;
import com.modularmc.gtpm.common.data.GTMaterialItems;
import com.modularmc.gtpm.common.data.GTMaterials;
import com.modularmc.gtpm.common.data.GTRecipeModifiers;
import com.modularmc.gtpm.common.data.GTRecipeTypes;
import com.modularmc.gtpm.common.machine.multiblock.electric.*;
import com.modularmc.gtpm.common.machine.multiblock.electric.BedrockOreMinerMachine;
import com.modularmc.gtpm.common.machine.multiblock.primitive.CharcoalPileIgniterMachine;
import com.modularmc.gtpm.common.machine.multiblock.primitive.CokeOvenMachine;
import com.modularmc.gtpm.common.machine.multiblock.primitive.PrimitiveBlastFurnaceMachine;
import com.modularmc.gtpm.common.machine.multiblock.primitive.PrimitivePumpMachine;
import com.modularmc.gtpm.common.machine.multiblock.steam.SteamParallelMultiblockMachine;
import com.modularmc.gtpm.config.ConfigHolder;
import com.modularmc.gtpm.utils.FormattingUtil;
import com.modularmc.gtpm.utils.GTUtil;

import com.modularmc.registrate.localization.MinecraftLocale;
import com.modularmc.registrate.localization.annotation.Trans;
import com.modularmc.registrate.localization.annotation.TransVariants;
import com.modularmc.registrate.localization.annotation.Translation;
import com.modularmc.registrate.localization.annotation.Variant;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.client.model.generators.BlockModelBuilder;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;

import appeng.api.networking.pathing.ChannelMode;
import appeng.core.AEConfig;

import java.util.Comparator;
import java.util.Locale;

import static com.modularmc.gtpm.api.GTValues.*;
import static com.modularmc.gtpm.api.multiblock.util.RelativeDirection.*;
import static com.modularmc.gtpm.common.data.GTBlocks.*;
import static com.modularmc.gtpm.common.data.GTMaterials.DrillingFluid;
import static com.modularmc.gtpm.common.data.GTRecipeModifiers.*;
import static com.modularmc.gtpm.common.data.GTRecipeTypes.DUMMY_RECIPES;
import static com.modularmc.gtpm.common.data.machines.GTMachineUtils.*;
import static com.modularmc.gtpm.common.data.models.GTMachineModels.*;
import static com.modularmc.gtpm.common.registry.GTRegistration.REGISTRATE;
import static com.modularmc.gtpm.utils.FormattingUtil.toRomanNumeral;

@TransVariants(keyPrefix = "gtpm",
               value = {
                       @Variant(id = "machine.active_transformer.tooltip.0",
                                source = "§7Transformers: Lasers in Disguise",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7变形金刚：伪装的激光")),
                       @Variant(id = "machine.active_transformer.tooltip.1",
                                source = "§7Can combine any number of Energy §fInputs§7 into any number of Energy §fOutputs§7.",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "§7可以将任意数量的能量§f输入§7整合为任意数量的能量§f输出§7。")),
                       @Variant(id = "machine.active_transformer.tooltip.2",
                                source = "§7Can transmit power at incredible distance with",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "§7当你使用§d激光§7进行能量传输时，拥有惊人的传输距离。")),
                       @Variant(id = "machine.active_transformer.tooltip.3",
                                source = " Lasers§7.",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = " ")),
                       @Variant(id = "machine.bedrock_ore_miner.depletion",
                                source = "§bDepletion Rate: §f%s%%",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§b损耗率：§f%s%%")),
                       @Variant(id = "machine.bedrock_ore_miner.description",
                                source = "§7Drills ores from veins under bedrock.",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7钻掘基岩之下的叠叠矿石。")),
                       @Variant(id = "machine.bedrock_ore_miner.production",
                                source = "§eProduction Multiplier: §f%dx, %fx overclocked",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "§e产量乘数：§f%dx，超频后%fx")),
                       @Variant(id = "machine.charcoal_pile.tooltip.0",
                                source = "Turns Logs into §aCharcoal§7 when §cignited§7.",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "§c点火后§7将原木烧制成§a木炭§7。")),
                       @Variant(id = "machine.charcoal_pile.tooltip.1",
                                source = "Right Click with fire-starting items to start.",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "使用可点火的物品右击即可启动。")),
                       @Variant(id = "machine.charcoal_pile.tooltip.2",
                                source = "Pyrolysis occurs in up to a §b9x4x9§7 space beneath.",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "热解发生在下方至多§b9x4x9§7的空间内。")),
                       @Variant(id = "machine.charcoal_pile.tooltip.3",
                                source = "Logs must be not be exposed to §eAir§7!",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "原木不可暴露在§e空气§7中！")),
                       @Variant(id = "machine.cleanroom.tooltip.0",
                                source = "Place machines inside to run cleanroom recipes.",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "将机器放在内部运行超净间配方。")),
                       @Variant(id = "machine.cleanroom.tooltip.1",
                                source = "Uses §f30 EU/t§7 when dirty, §f4 EU/t§7 when clean.",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "清理污染时耗能为§f30EU/t§7，洁净状态耗能为§f4EU/t§7。")),
                       @Variant(id = "machine.cleanroom.tooltip.2",
                                source = "Overclocking increases cleaning per cycle.",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "超频可提升每个周期的清洁度。")),
                       @Variant(id = "machine.cleanroom.tooltip.3",
                                source = "§bSize: §f5x5x5 to 15x15x15",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "§b大小：§f5x5x5到15x15x15")),
                       @Variant(id = "machine.cleanroom.tooltip.4",
                                source = "Requires §fFilter Casings §7in the ceiling, excluding the edges.",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "天花板需要§f过滤器机械方块§7，不包含天花板的边缘。")),
                       @Variant(id = "machine.cleanroom.tooltip.5",
                                source = "Accepts up to §f4 Doors§7! Remains clean when the door is open.",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "接受最多§f四扇门§7！门处于打开状态时不会影响洁净度。")),
                       @Variant(id = "machine.cleanroom.tooltip.6",
                                source = "Generators, Mufflers, Drills, and Primitive Machines are too dirty for the cleanroom!",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "发电机、消声仓、采矿机和原始机器对于超净间来说太脏了！")),
                       @Variant(id = "machine.cleanroom.tooltip.7",
                                source = "Send power through §fHulls §7or §fDiodes §7in the walls.",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "通过墙壁上的§f机器外壳§7或§f二极管§7向内部输入能量。")),
                       @Variant(id = "machine.cleanroom.tooltip.ae2.channels",
                                source = "Send up to §f8 AE2 Channels §7through §fHulls§7 in the walls.",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "通过墙壁上的§f机器外壳§7发送最多§f8个AE2频道§7。")),
                       @Variant(id = "machine.cleanroom.tooltip.ae2.no_channels",
                                source = "Send §aAE2 Networks§7 through §fHulls§7 in the walls.",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "通过墙壁上的§f机器外壳§7连通§aAE2网络§7。")),
                       @Variant(id = "machine.cleanroom.tooltip.hold_ctrl",
                                source = "Hold CTRL to show additional Structure Information",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "按住CTRL以显示完整的结构信息")),
                       @Variant(id = "machine.cracker.tooltip.1",
                                source = "Every coil after §6Cupronickel§7 reduces energy usage by §f10%%§7.",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "使用§6白铜§7后的每个线圈等级可以减少§f10%%§7的能源消耗。")),
                       @Variant(id = "machine.fluid_drilling_rig.depletion",
                                source = "§bDepletion Rate: §f%s%%",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§b损耗率：§f%s%%")),
                       @Variant(id = "machine.fluid_drilling_rig.description",
                                source = "§7Drills fluids from veins under bedrock.",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7钻取基岩之下的漫漫流体。")),
                       @Variant(id = "machine.fluid_drilling_rig.production",
                                source = "§eProduction Multiplier: §f%dx, %fx overclocked",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "§e产量乘数：§f%dx，超频后%fx")),
                       @Variant(id = "machine.fusion_reactor.capacity",
                                source = "§7Maximum Energy Storage: §e%sM EU",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7储能上限：§e%sM EU")),
                       @Variant(id = "machine.fusion_reactor.overclocking",
                                source = "Overclocks double energy and halve duration.",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "超频时消耗两倍能量，工作时间减半")),
                       @Variant(id = "machine.miner.fluid_usage",
                                source = "Uses §f%d mB/t §7of §f%s§7, doubled per overclock.",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "消耗§f%d mB/t§7的§f%s§7，每次超频都会使消耗量加倍。")),
                       @Variant(id = "machine.miner.multi.description",
                                source = "A multiblock mining machine that covers a large area and produces huge quantity of ore.",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "一种占地面积大、产矿量大的多方块采矿机。")),
                       @Variant(id = "machine.miner.multi.modes",
                                source = "Has Silk Touch and Chunk Aligned Modes.",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "具有精准采集和区块对齐模式。")),
                       @Variant(id = "machine.miner.multi.production",
                                source = "Produces §f3x§7 more crushed ore than a §fMacerator§7.",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "它能产出§f三倍§7于§f研磨机§7的粉碎矿石。")),
                       @Variant(id = "machine.power_substation.tooltip.0",
                                source = "The heart of a centralized power grid",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "集中电网的核心")),
                       @Variant(id = "machine.power_substation.tooltip.1",
                                source = "§fCapacitors§7 do not need to be all the same tier.",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "所有§f电容§r不需要保持同一电压等级。")),
                       @Variant(id = "machine.power_substation.tooltip.2",
                                source = "Allows up to §f%d Capacitor Layers§7.",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "最多容许§f%d层电容§r。")),
                       @Variant(id = "machine.power_substation.tooltip.3",
                                source = "Loses energy equal to §f1%%§7 of total capacity every §f24 hours§7.",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "每§f24小时§7损失相当于总容量的§f1%%§r的能量。")),
                       @Variant(id = "machine.power_substation.tooltip.4",
                                source = "Capped at §f%d kEU/t§7 passive loss per Capacitor Block.",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "每个电容的损失上限为§f%d kEU/t§r。")),
                       @Variant(id = "machine.power_substation.tooltip.5",
                                source = "Can use",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "可以使用")),
                       @Variant(id = "machine.power_substation.tooltip.6",
                                source = " Laser Hatches§7.",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "激光仓§r。")),
                       @Variant(id = "machine.pyrolyse_oven.tooltip.1",
                                source = "§6Cupronickel §7coils are §f25%%§7 slower. Every coil after §bKanthal§7 increases speed by §f50%%§7.",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "§a白铜§7线圈会使效率降低§f25%%§7，§a坎塔尔合金§7后的每个线圈都会增加§f50%%§7的速度。")),
                       @Variant(id = "multiblock.cracking_unit.energy",
                                source = "Energy Usage: %s%%",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "耗能：%s%%")),
                       @Variant(id = "multiblock.multi_furnace.heating_coil_discount",
                                source = "Heating Coil EU Boost: %sx",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "加热线圈增幅：%sx")),
                       @Variant(id = "multiblock.multi_furnace.heating_coil_level",
                                source = "Heating Coil Level: %s",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "加热线圈等级：%s")),
                       @Variant(id = "multiblock.pyrolyse_oven.speed",
                                source = "Processing Speed: %s%%",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "处理速度：%s%%")),
                       @Variant(id = "universal.tooltip.energy_tier_range",
                                source = "§aAllowed Voltage Tiers: §f%s §f- %s",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "§a适配电压等级：§f%s§f-%s")),
                       @Variant(id = "multiblock.blast_furnace.max_temperature",
                                source = "Heat Capacity: %s",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "热容：%s")),
               })
public class GTMultiMachines {

    //////////////////////////////////////
    // ******* Multiblock *******//
    //////////////////////////////////////
    public static final MultiblockMachineDefinition LARGE_BOILER_BRONZE = largeBoiler(REGISTRATE, "bronze",
            CASING_BRONZE_BRICKS, FIREBOX_BRONZE,
            GTPM.id("block/casings/solid/machine_casing_bronze_plated_bricks"), BoilerFireboxType.BRONZE_FIREBOX,
            ConfigHolder.INSTANCE.machines.largeBoilers.bronzeBoilerMaxTemperature,
            ConfigHolder.INSTANCE.machines.largeBoilers.bronzeBoilerHeatSpeed)
            .localization(text -> text.source("Large Bronze Boiler").translation(MinecraftLocale.ZH_CN, "大型青铜锅炉"))
            .register();
    public static final MultiblockMachineDefinition LARGE_BOILER_STEEL = largeBoiler(REGISTRATE, "steel",
            CASING_STEEL_SOLID, FIREBOX_STEEL,
            GTPM.id("block/casings/solid/machine_casing_solid_steel"), BoilerFireboxType.STEEL_FIREBOX,
            ConfigHolder.INSTANCE.machines.largeBoilers.steelBoilerMaxTemperature,
            ConfigHolder.INSTANCE.machines.largeBoilers.steelBoilerHeatSpeed)
            .localization(text -> text.source("Large Steel Boiler").translation(MinecraftLocale.ZH_CN, "大型钢锅炉"))
            .register();
    public static final MultiblockMachineDefinition LARGE_BOILER_TITANIUM = largeBoiler(REGISTRATE, "titanium",
            CASING_TITANIUM_STABLE, FIREBOX_TITANIUM,
            GTPM.id("block/casings/solid/machine_casing_stable_titanium"), BoilerFireboxType.TITANIUM_FIREBOX,
            ConfigHolder.INSTANCE.machines.largeBoilers.titaniumBoilerMaxTemperature,
            ConfigHolder.INSTANCE.machines.largeBoilers.titaniumBoilerHeatSpeed)
            .localization(text -> text.source("Large Titanium Boiler").translation(MinecraftLocale.ZH_CN, "大型钛锅炉"))
            .register();
    public static final MultiblockMachineDefinition LARGE_BOILER_TUNGSTENSTEEL = largeBoiler(REGISTRATE, "tungstensteel",
            CASING_TUNGSTENSTEEL_ROBUST, FIREBOX_TUNGSTENSTEEL,
            GTPM.id("block/casings/solid/machine_casing_robust_tungstensteel"),
            BoilerFireboxType.TUNGSTENSTEEL_FIREBOX,
            ConfigHolder.INSTANCE.machines.largeBoilers.tungstensteelBoilerMaxTemperature,
            ConfigHolder.INSTANCE.machines.largeBoilers.tungstensteelBoilerHeatSpeed)
            .localization(text -> text.source("Large Tungstensteel Boiler").translation(MinecraftLocale.ZH_CN, "大型钨钢锅炉"))
            .register();

    public static final MultiblockMachineDefinition COKE_OVEN = REGISTRATE.multiblock("coke_oven", CokeOvenMachine::new)
            .localization(text -> text
                    .source("Coke Oven")
                    .translation(MinecraftLocale.ZH_CN, "焦炉"))
            .rotationState(RotationState.ALL)
            .recipeType(GTRecipeTypes.COKE_OVEN_RECIPES)
            .appearanceBlock(CASING_COKE_BRICKS)
            .pattern(MultiblockControllerMachine.DEFAULT_STRUCTURE, definition -> PatternBuilder.start(definition)
                    .appendDefinition()
                    .build())
            .workableCasingModel(GTPM.id("block/casings/solid/machine_coke_bricks"),
                    GTPM.id("block/multiblock/coke_oven"))
            .register();

    public static final MultiblockMachineDefinition PRIMITIVE_BLAST_FURNACE = REGISTRATE
            .multiblock("primitive_blast_furnace", PrimitiveBlastFurnaceMachine::new)
            .localization(text -> text
                    .source("Primitive Blast Furnace")
                    .translation(MinecraftLocale.ZH_CN, "土高炉"))
            .rotationState(RotationState.ALL)
            .recipeType(GTRecipeTypes.PRIMITIVE_BLAST_FURNACE_RECIPES)
            .model(createWorkableCasingMachineModel(GTPM.id("block/casings/solid/machine_primitive_bricks"),
                    GTPM.id("block/multiblock/primitive_blast_furnace"))
                    .andThen(b -> b.addDynamicRenderer(DynamicRenderHelper::createPBFLavaRender)))
            .hasBER(true)
            .appearanceBlock(CASING_PRIMITIVE_BRICKS)
            .pattern(MultiblockControllerMachine.DEFAULT_STRUCTURE, definition -> PatternBuilder.start(definition)
                    .appendDefinition()
                    .build())
            .register();

    public static final MultiblockMachineDefinition ELECTRIC_BLAST_FURNACE = REGISTRATE
            .multiblock("electric_blast_furnace", CoilWorkableElectricMultiblockMachine::new)
            .localization(text -> text
                    .source("Electric Blast Furnace")
                    .translation(MinecraftLocale.ZH_CN, "电力高炉"))
            .rotationState(RotationState.ALL)
            .recipeType(GTRecipeTypes.BLAST_RECIPES)
            .recipeModifiers(GTRecipeModifiers::ebfOverclock, BATCH_MODE)
            .appearanceBlock(CASING_INVAR_HEATPROOF)
            .pattern(MultiblockControllerMachine.DEFAULT_STRUCTURE, definition -> PatternBuilder.start(definition)
                    .appendDefinition()
                    .build())
            .recoveryItems(
                    () -> new ItemLike[] {
                            GTMaterialItems.MATERIAL_ITEMS.get(TagPrefix.dustTiny, GTMaterials.Ash).get() })
            .workableCasingModel(GTPM.id("block/casings/solid/machine_casing_heatproof"),
                    GTPM.id("block/multiblock/electric_blast_furnace"))
            .tooltips(Component.translatable("gtpm.machine.electric_blast_furnace.tooltip.0"),
                    Component.translatable("gtpm.machine.electric_blast_furnace.tooltip.1"),
                    Component.translatable("gtpm.machine.electric_blast_furnace.tooltip.2"))
            .additionalDisplay((controller, components) -> {
            // spotless:off
                if (controller instanceof CoilWorkableElectricMultiblockMachine coilMachine && controller.isFormed()) {
                    components.add(Component.translatable("gtpm.multiblock.blast_furnace.max_temperature",
                            Component.translatable(
                                    FormattingUtil.formatNumbers(coilMachine.getCoilType().getCoilTemperature() +
                                            100L * Math.max(0, coilMachine.getTier() - GTValues.MV)) + "K")
                                    .setStyle(Style.EMPTY.withColor(ChatFormatting.RED))));
                }
                // spotless:on
            })
            .register();

    public static final MultiblockMachineDefinition LARGE_CHEMICAL_REACTOR = REGISTRATE
            .multiblock("large_chemical_reactor", WorkableElectricMultiblockMachine::new)
            .localization(text -> text
                    .source("Large Chemical Reactor")
                    .translation(MinecraftLocale.ZH_CN, "大型化学反应釜"))
            .conditionalTooltip(defaultEnvironmentRequirement(),
                    ConfigHolder.INSTANCE.gameplay.environmentalHazards)
            .rotationState(RotationState.ALL)
            .recipeType(GTRecipeTypes.LARGE_CHEMICAL_RECIPES)
            .recipeModifiers(DEFAULT_ENVIRONMENT_REQUIREMENT, OC_PERFECT_SUBTICK, BATCH_MODE)
            .appearanceBlock(CASING_PTFE_INERT)
            .pattern(MultiblockControllerMachine.DEFAULT_STRUCTURE, definition -> PatternBuilder.start(definition)
                    .appendDefinition()
                    .build())
            .workableCasingModel(GTPM.id("block/casings/solid/machine_casing_inert_ptfe"),
                    GTPM.id("block/multiblock/large_chemical_reactor"))
            .register();

    public static final MultiblockMachineDefinition IMPLOSION_COMPRESSOR = REGISTRATE
            .multiblock("implosion_compressor", WorkableElectricMultiblockMachine::new)
            .localization(text -> text
                    .source("Implosion Compressor")
                    .translation(MinecraftLocale.ZH_CN, "聚爆压缩机"))
            .rotationState(RotationState.ALL)
            .recipeType(GTRecipeTypes.IMPLOSION_RECIPES)
            .recipeModifiers(OC_NON_PERFECT_SUBTICK, BATCH_MODE)
            .appearanceBlock(CASING_STEEL_SOLID)
            .pattern(MultiblockControllerMachine.DEFAULT_STRUCTURE, definition -> PatternBuilder.start(definition)
                    .appendDefinition()
                    .build())
            .workableCasingModel(GTPM.id("block/casings/solid/machine_casing_solid_steel"),
                    GTPM.id("block/multiblock/implosion_compressor"))
            .register();

    public static final MultiblockMachineDefinition PYROLYSE_OVEN = REGISTRATE
            .multiblock("pyrolyse_oven", CoilWorkableElectricMultiblockMachine::new)
            .localization(text -> text
                    .source("Pyrolyse Oven")
                    .translation(MinecraftLocale.ZH_CN, "热解炉"))
            .rotationState(RotationState.ALL)
            .recipeType(GTRecipeTypes.PYROLYSE_RECIPES)
            .recipeModifiers(GTRecipeModifiers::pyrolyseOvenOverclock, BATCH_MODE)
            .appearanceBlock(MACHINE_CASING_ULV)
            .pattern(MultiblockControllerMachine.DEFAULT_STRUCTURE, definition -> PatternBuilder.start(definition)
                    .appendDefinition()
                    .build())
            .workableCasingModel(GTPM.id("block/casings/voltage/ulv/side"),
                    GTPM.id("block/multiblock/pyrolyse_oven"))
            .tooltips(Component.translatable("gtpm.machine.pyrolyse_oven.tooltip.1"))
            .additionalDisplay((controller, components) -> {
                if (controller instanceof CoilWorkableElectricMultiblockMachine coilMachine && controller.isFormed()) {
                    components.add(Component.translatable("gtpm.multiblock.pyrolyse_oven.speed",
                            coilMachine.getCoilTier() == 0 ? 75 : 50 * (coilMachine.getCoilTier() + 1)));
                }
            })
            .register();

    public static final MultiblockMachineDefinition MULTI_SMELTER = REGISTRATE
            .multiblock("multi_smelter", CoilWorkableElectricMultiblockMachine::new)
            .localization(text -> text
                    .source("Multi Smelter")
                    .translation(MinecraftLocale.ZH_CN, "工业熔炉"))
            .rotationState(RotationState.ALL)
            .recipeTypes(GTRecipeTypes.FURNACE_RECIPES, GTRecipeTypes.ALLOY_SMELTER_RECIPES)
            .recipeModifiers(GTRecipeModifiers::multiSmelterParallel, BATCH_MODE)
            .appearanceBlock(CASING_INVAR_HEATPROOF)
            .tooltips(Component.translatable("gtpm.machine.available_recipe_map_2.tooltip",
                    Component.translatable("gtpm.electric_furnace"), Component.translatable("gtpm.alloy_smelter")))
            .pattern(MultiblockControllerMachine.DEFAULT_STRUCTURE, definition -> PatternBuilder.start(definition)
                    .appendDefinition()
                    .build())
            .recoveryItems(
                    () -> new ItemLike[] {
                            GTMaterialItems.MATERIAL_ITEMS.get(TagPrefix.dustTiny, GTMaterials.Ash).get() })
            .workableCasingModel(GTPM.id("block/casings/solid/machine_casing_heatproof"),
                    GTPM.id("block/multiblock/multi_furnace"))
            .additionalDisplay((controller, components) -> {
                if (controller instanceof CoilWorkableElectricMultiblockMachine coilMachine && controller.isFormed()) {
                    components.add(Component.translatable("gtpm.multiblock.multi_furnace.heating_coil_level",
                            coilMachine.getCoilType().getLevel()));
                    components.add(Component.translatable("gtpm.multiblock.multi_furnace.heating_coil_discount",
                            coilMachine.getCoilType().getEnergyDiscount()));
                }
            })
            .register();

    public static final MultiblockMachineDefinition CRACKER = REGISTRATE
            .multiblock("cracker", CoilWorkableElectricMultiblockMachine::new)
            .localization(text -> text
                    .source("Cracker")
                    .translation(MinecraftLocale.ZH_CN, "裂化机"))
            .rotationState(RotationState.ALL)
            .recipeType(GTRecipeTypes.CRACKING_RECIPES)
            .recipeModifiers(GTRecipeModifiers::crackerOverclock, BATCH_MODE)
            .appearanceBlock(CASING_STAINLESS_CLEAN)
            .pattern(MultiblockControllerMachine.DEFAULT_STRUCTURE, definition -> PatternBuilder.start(definition)
                    .appendDefinition()
                    .build())
            .workableCasingModel(GTPM.id("block/casings/solid/machine_casing_clean_stainless_steel"),
                    GTPM.id("block/multiblock/cracking_unit"))
            .tooltips(Component.translatable("gtpm.machine.cracker.tooltip.1"))
            .additionalDisplay((controller, components) -> {
                if (controller instanceof CoilWorkableElectricMultiblockMachine coilMachine && controller.isFormed()) {
                    components.add(Component.translatable("gtpm.multiblock.cracking_unit.energy",
                            100 - 10 * coilMachine.getCoilTier()));
                }
            })
            .register();

    public static final MultiblockMachineDefinition DISTILLATION_TOWER = REGISTRATE
            .multiblock("distillation_tower", DistillationTowerMachine::new)
            .localization(text -> text
                    .source("Distillation Tower")
                    .translation(MinecraftLocale.ZH_CN, "蒸馏塔"))
            .rotationState(RotationState.NON_Y_AXIS)
            .recipeType(GTRecipeTypes.DISTILLATION_RECIPES)
            .recipeModifiers(OC_NON_PERFECT_SUBTICK, BATCH_MODE)
            .appearanceBlock(CASING_STAINLESS_CLEAN)
            .pattern(MultiblockControllerMachine.DEFAULT_STRUCTURE, definition -> PatternBuilder
                    .start(definition, RIGHT, BACK, UP)
                    .appendDefinition()
                    .build())
            .allowExtendedFacing(false)
            .partSorter(Comparator.comparingInt((IMultiPart p) -> p.self().getBlockPos().getY()))
            .workableCasingModel(GTPM.id("block/casings/solid/machine_casing_clean_stainless_steel"),
                    GTPM.id("block/multiblock/distillation_tower"))
            .register();

    public static final MultiblockMachineDefinition VACUUM_FREEZER = REGISTRATE
            .multiblock("vacuum_freezer", WorkableElectricMultiblockMachine::new)
            .localization(text -> text
                    .source("Vacuum Freezer")
                    .translation(MinecraftLocale.ZH_CN, "真空冷冻机"))
            .rotationState(RotationState.ALL)
            .recipeType(GTRecipeTypes.VACUUM_RECIPES)
            .recipeModifiers(GTRecipeModifiers.OC_NON_PERFECT_SUBTICK, BATCH_MODE)
            .appearanceBlock(CASING_ALUMINIUM_FROSTPROOF)
            .pattern(MultiblockControllerMachine.DEFAULT_STRUCTURE, definition -> PatternBuilder.start(definition)
                    .appendDefinition()
                    .build())
            .workableCasingModel(GTPM.id("block/casings/solid/machine_casing_frost_proof"),
                    GTPM.id("block/multiblock/vacuum_freezer"))
            .register();

    public static final MultiblockMachineDefinition ASSEMBLY_LINE = REGISTRATE
            .multiblock("assembly_line", AssemblyLineMachine::new)
            .localization(text -> text
                    .source("Assembly Line")
                    .translation(MinecraftLocale.ZH_CN, "装配线"))
            .rotationState(RotationState.ALL)
            .recipeType(GTRecipeTypes.ASSEMBLY_LINE_RECIPES)
            .alwaysTryModifyRecipe(true)
            .recipeModifiers(DEFAULT_ENVIRONMENT_REQUIREMENT, OC_NON_PERFECT)
            .appearanceBlock(CASING_STEEL_SOLID)
            .pattern(MultiblockControllerMachine.DEFAULT_STRUCTURE,
                    definition -> PatternBuilder.start(definition, BACK, UP, RIGHT)
                            .appendDefinition()
                            .build())
            .partSorter(AssemblyLineMachine::partSorter)
            .workableCasingModel(GTPM.id("block/casings/solid/machine_casing_solid_steel"),
                    GTPM.id("block/multiblock/assembly_line"))
            .register();

    public static final MultiblockMachineDefinition PRIMITIVE_PUMP = REGISTRATE
            .multiblock("primitive_pump", PrimitivePumpMachine::new)
            .localization(text -> text
                    .source("Primitive Pump")
                    .translation(MinecraftLocale.ZH_CN, "原始水泵"))
            .rotationState(RotationState.NON_Y_AXIS)
            .appearanceBlock(CASING_PUMP_DECK)
            .pattern(MultiblockControllerMachine.DEFAULT_STRUCTURE, definition -> PatternBuilder.start(definition)
                    .appendDefinition()
                    .build())
            .allowExtendedFacing(false)
            .modelProperty(GTMachineModelProperties.RECIPE_LOGIC_STATUS, WorkLogic.Status.IDLE)
            .model(createSidedWorkableCasingMachineModel(GTPM.id("block/casings/pump_deck"),
                    GTPM.id("block/multiblock/primitive_pump"))
                    .andThen(builder -> {
                        builder.replaceForAllStates((state, models) -> {
                            for (int i = 0; i < models.length; i++) {
                                models[i] = ConfiguredModel.builder()
                                        .modelFile(models[i].model).uvLock(true)
                                        .buildLast();
                            }
                            return models;
                        });
                    }))
            .register();

    public static final MultiblockMachineDefinition STEAM_GRINDER = REGISTRATE
            .multiblock("steam_grinder", SteamParallelMultiblockMachine::new)
            .localization(text -> text
                    .source("Steam Grinder")
                    .translation(MinecraftLocale.ZH_CN, "蒸汽碾磨机"))
            .rotationState(RotationState.ALL)
            .appearanceBlock(CASING_BRONZE_BRICKS)
            .recipeType(GTRecipeTypes.MACERATOR_RECIPES)
            .recipeModifier(SteamParallelMultiblockMachine::recipeModifier, true)
            .addOutputLimit(ItemRecipeCapability.CAP, 1)
            .pattern(MultiblockControllerMachine.DEFAULT_STRUCTURE, definition -> PatternBuilder.start(definition)
                    .appendDefinition()
                    .build())
            .workableCasingModel(GTPM.id("block/casings/solid/machine_casing_bronze_plated_bricks"),
                    GTPM.id("block/multiblock/steam_grinder"))
            .register();

    public static final MultiblockMachineDefinition STEAM_OVEN = REGISTRATE
            .multiblock("steam_oven", SteamParallelMultiblockMachine::new)
            .localization(text -> text
                    .source("Steam Oven")
                    .translation(MinecraftLocale.ZH_CN, "蒸汽熔炼炉"))
            .rotationState(RotationState.ALL)
            .appearanceBlock(CASING_BRONZE_BRICKS)
            .recipeType(GTRecipeTypes.FURNACE_RECIPES)
            .recipeModifier(SteamParallelMultiblockMachine::recipeModifier, true)
            .addOutputLimit(ItemRecipeCapability.CAP, 1)
            .pattern(MultiblockControllerMachine.DEFAULT_STRUCTURE, definition -> PatternBuilder.start(definition)
                    .appendDefinition()
                    .build())
            .modelProperty(GTMachineModelProperties.RECIPE_LOGIC_STATUS, WorkLogic.Status.IDLE)
            .model(createWorkableCasingMachineModel(GTPM.id("block/casings/solid/machine_casing_bronze_plated_bricks"),
                    GTPM.id("block/multiblock/steam_oven"))
                    .andThen(b -> b.addDynamicRenderer(
                            () -> DynamicRenderHelper.makeBoilerPartRender(
                                    BoilerFireboxType.BRONZE_FIREBOX, CASING_BRONZE_BRICKS))))
            .register();

    public static final MultiblockMachineDefinition[] FUSION_REACTOR = registerTieredMultis("fusion_reactor",
            FusionReactorMachine::new, (tier, builder) -> builder
                    .rotationState(RotationState.ALL)
                    .localization(text -> text
                            .source("Fusion Reactor Computer MK %s".formatted(toRomanNumeral(tier - 5)))
                            .translation(MinecraftLocale.ZH_CN,
                                    "核聚变反应堆控制电脑 MK-" + toRomanNumeral(tier - 5)))
                    .recipeType(GTRecipeTypes.FUSION_RECIPES)
                    .recipeModifiers(DEFAULT_ENVIRONMENT_REQUIREMENT,
                            FusionReactorMachine::recipeModifier, BATCH_MODE)
                    .tooltips(
                            Component.translatable("gtpm.machine.fusion_reactor.capacity",
                                    FusionReactorMachine.calculateEnergyStorageFactor(tier, 16) / 1000000L),
                            Component.translatable("gtpm.machine.fusion_reactor.overclocking"),
                            Component.translatable("gtpm.multiblock.%s_fusion_reactor.description"
                                    .formatted(VN[tier].toLowerCase(Locale.ROOT))))
                    .appearanceBlock(() -> FusionReactorMachine.getCasingState(tier))
                    .pattern(MultiblockControllerMachine.DEFAULT_STRUCTURE, definition -> PatternBuilder
                            .start(definition)
                            .appendDefinition()
                            .build())
                    .modelProperty(GTMachineModelProperties.RECIPE_LOGIC_STATUS, WorkLogic.Status.IDLE)
                    .model(createWorkableCasingMachineModel(FusionReactorMachine.getCasingType(tier).getTexture(),
                            GTPM.id("block/multiblock/fusion_reactor"))
                            .andThen(b -> b.addDynamicRenderer(DynamicRenderHelper::createFusionRingRender)))
                    .hasBER(true)
                    .register(),
            LuV, ZPM, UV);

    public static final MultiblockMachineDefinition[] FLUID_DRILLING_RIG = registerTieredMultis(
            "fluid_drilling_rig", FluidDrillMachine::new, (tier, builder) -> builder
                    .rotationState(RotationState.ALL)
                    .localization(text -> text
                            .source("%s Fluid Drilling Rig %s".formatted(VLVH[tier], VLVT[tier]))
                            .translation(MinecraftLocale.ZH_CN, switch (tier) {
                                case MV -> "§b进阶流体钻机 §r";
                                case HV -> "§6进阶流体钻机 II§r";
                                case EV -> "§5进阶流体钻机 III§r";
                                default -> throw new IllegalArgumentException("Unsupported fluid drilling rig tier: " + tier);
                            }))
                    .primaryTooltip(switch (tier) {
                        case MV -> text -> text.source("Oil Extraction Pump")
                                .translation(MinecraftLocale.ZH_CN, "我猜你做这个是为了采油");
                        case HV -> text -> text.source("Does not perform Fracking")
                                .translation(MinecraftLocale.ZH_CN, "并不能用来液压破碎");
                        case EV -> text -> text.source("Well Drainer")
                                .translation(MinecraftLocale.ZH_CN, "甚至可以抽空整个油田");
                        default -> throw new IllegalArgumentException("Unsupported fluid drilling rig tier: " + tier);
                    })
                    .recipeType(DUMMY_RECIPES)
                    .tooltips(
                            Component.translatable("gtpm.machine.fluid_drilling_rig.description"),
                            Component.translatable("gtpm.machine.fluid_drilling_rig.depletion",
                                    FormattingUtil.formatNumbers(100.0 / FluidDrillMachine.getDepletionChance(tier))),
                            Component.translatable("gtpm.universal.tooltip.energy_tier_range", GTValues.VNF[tier],
                                    GTValues.VNF[tier + 1]),
                            Component.translatable("gtpm.machine.fluid_drilling_rig.production",
                                    FluidDrillMachine.getRigMultiplier(tier),
                                    FormattingUtil.formatNumbers(FluidDrillMachine.getRigMultiplier(tier) * 1.5)))
                    .appearanceBlock(() -> FluidDrillMachine.getCasingState(tier))
                    .pattern(MultiblockControllerMachine.DEFAULT_STRUCTURE,
                            (definition) -> PatternBuilder.start(definition)
                                    .appendDefinition()
                                    .build())
                    .workableCasingModel(FluidDrillMachine.getBaseTexture(tier),
                            GTPM.id("block/multiblock/fluid_drilling_rig"))
                    .register(),
            MV, HV, EV);

    public static final MultiblockMachineDefinition[] LARGE_MINER = registerTieredMultis("large_miner",
            (holder, tier) -> new LargeMinerMachine(holder, tier, 64 / tier, 2 * tier - 5, tier, 8 - (tier - 5)),
            (tier, builder) -> builder
                    .rotationState(RotationState.NON_Y_AXIS)
                    .localization(text -> text
                            .source("%s Large Miner %s".formatted(VLVH[tier], VLVT[tier]))
                            .translation(MinecraftLocale.ZH_CN, switch (tier) {
                                case EV -> "§5进阶大型采矿机 III§r";
                                case IV -> "§9精英大型采矿机§r";
                                case LuV -> "§d精英大型采矿机 II§r";
                                default -> throw new IllegalArgumentException("Unsupported large miner tier: " + tier);
                            }))
                    .recipeType(GTRecipeTypes.MACERATOR_RECIPES)
                    .appearanceBlock(() -> LargeMinerMachine.getCasingState(tier))
                    .pattern(MultiblockControllerMachine.DEFAULT_STRUCTURE,
                            (definition) -> PatternBuilder.start(definition)
                                    .appendDefinition()
                                    .build())
                    .allowExtendedFacing(true)
                    .modelProperty(GTMachineModelProperties.RECIPE_LOGIC_STATUS, WorkLogic.Status.IDLE)
                    .model(createWorkableCasingMachineModel(
                            MATERIALS_TO_CASING_TEXTURES.get(LargeMinerMachine.getMaterial(tier)),
                            GTPM.id("block/multiblock/large_miner"))
                            .andThen((ctx, prov, modelBuilder) -> {
                                // replace the parent model for the formed large miner
                                modelBuilder.replaceForAllStates((state, models) -> {
                                    if (!state.getValue(GTMachineModelProperties.IS_FORMED)) {
                                        return models;
                                    }

                                    var parentModel = prov.models()
                                            .getExistingFile(GTPM.id("block/machine/large_miner_active"));
                                    for (ConfiguredModel model : models) {
                                        ((BlockModelBuilder) model.model).parent(parentModel);
                                    }
                                    return models;
                                });
                            }))
                    .tooltips(
                            Component.translatable("gtpm.machine.large_miner.%s.tooltip"
                                    .formatted(VN[tier].toLowerCase(Locale.ROOT))),
                            Component.translatable("gtpm.machine.miner.multi.description"))
                    .tooltipBuilder((stack, tooltip) -> {
                        int workingAreaChunks = (2 * tier - 5);
                        tooltip.add(Component.translatable("gtpm.machine.miner.multi.modes"));
                        tooltip.add(Component.translatable("gtpm.machine.miner.multi.production"));
                        tooltip.add(Component.translatable("gtpm.machine.miner.fluid_usage", 8 - (tier - 5),
                                DrillingFluid.getLocalizedName()));
                        tooltip.add(Component.translatable("gtpm.universal.tooltip.working_area_chunks",
                                workingAreaChunks, workingAreaChunks));
                        tooltip.add(Component.translatable("gtpm.universal.tooltip.energy_tier_range",
                                GTValues.VNF[tier], GTValues.VNF[tier + 1]));
                    })
                    .register(),
            EV, IV, LuV);

    public static final MultiblockMachineDefinition CLEANROOM = REGISTRATE
            .multiblock("cleanroom", CleanroomMachine::new)
            .localization(text -> text
                    .source("Cleanroom")
                    .translation(MinecraftLocale.ZH_CN, "超净间"))
            .rotationState(RotationState.NONE)
            .recipeType(DUMMY_RECIPES)
            .appearanceBlock(PLASTCRETE)
            .tooltips(Component.translatable("gtpm.machine.cleanroom.tooltip.0"),
                    Component.translatable("gtpm.machine.cleanroom.tooltip.1"),
                    Component.translatable("gtpm.machine.cleanroom.tooltip.2"),
                    Component.translatable("gtpm.machine.cleanroom.tooltip.3"))
            .tooltipBuilder((stack, tooltip) -> {
                if (GTUtil.isCtrlDown()) {
                    tooltip.add(Component.empty());
                    tooltip.add(Component.translatable("gtpm.machine.cleanroom.tooltip.4"));
                    tooltip.add(Component.translatable("gtpm.machine.cleanroom.tooltip.5"));
                    tooltip.add(Component.translatable("gtpm.machine.cleanroom.tooltip.6"));
                    tooltip.add(Component.translatable("gtpm.machine.cleanroom.tooltip.7"));
                    // tooltip.add(Component.translatable("gtpm.machine.cleanroom.tooltip.8"));
                    if (GTPM.MODS.AE2.isLoaded()) {
                        tooltip.add(
                                Component.translatable(AEConfig.instance().getChannelMode() == ChannelMode.INFINITE ?
                                        "gtpm.machine.cleanroom.tooltip.ae2.no_channels" :
                                        "gtpm.machine.cleanroom.tooltip.ae2.channels"));
                    }
                    tooltip.add(Component.empty());
                } else {
                    tooltip.add(Component.translatable("gtpm.machine.cleanroom.tooltip.hold_ctrl"));
                }
            })
            .pattern(MultiblockControllerMachine.DEFAULT_STRUCTURE,
                    (definition) -> PatternBuilder.start(definition)
                            .appendDefinition()
                            .build())
            .allowExtendedFacing(false)
            .allowFlip(false)
            .workableCasingModel(GTPM.id("block/casings/cleanroom/plascrete"),
                    GTPM.id("block/multiblock/cleanroom"))
            .register();

    public static final MultiblockMachineDefinition LARGE_COMBUSTION_ENGINE = largeCombustionEngine(REGISTRATE,
            "large_combustion_engine", EV, CASING_TITANIUM_STABLE,
            GTPM.id("block/casings/solid/machine_casing_stable_titanium"),
            GTPM.id("block/multiblock/generator/large_combustion_engine"))
            .localization(text -> text.source("Large Combustion Engine").translation(MinecraftLocale.ZH_CN, "大型内燃引擎"))
            .register();

    public static final MultiblockMachineDefinition EXTREME_COMBUSTION_ENGINE = largeCombustionEngine(REGISTRATE,
            "extreme_combustion_engine", IV, CASING_TUNGSTENSTEEL_ROBUST,
            GTPM.id("block/casings/solid/machine_casing_robust_tungstensteel"),
            GTPM.id("block/multiblock/generator/extreme_combustion_engine"))
            .localization(text -> text.source("Extreme Combustion Engine").translation(MinecraftLocale.ZH_CN, "极限内燃引擎"))
            .register();

    public static final MultiblockMachineDefinition LARGE_STEAM_TURBINE = registerLargeTurbine(
            REGISTRATE, "steam_large_turbine",
            HV,
            GTRecipeTypes.STEAM_TURBINE_FUELS,
            CASING_STEEL_TURBINE, CASING_STEEL_GEARBOX,
            GTPM.id("block/casings/mechanic/machine_casing_turbine_steel"),
            GTPM.id("block/multiblock/generator/large_steam_turbine"),
            false,
            text -> text.source("Large Steam Turbine").translation(MinecraftLocale.ZH_CN, "大型蒸汽涡轮"));

    public static final MultiblockMachineDefinition LARGE_GAS_TURBINE = registerLargeTurbine(
            REGISTRATE, "gas_large_turbine", EV,
            GTRecipeTypes.GAS_TURBINE_FUELS,
            CASING_STAINLESS_TURBINE, CASING_STAINLESS_STEEL_GEARBOX,
            GTPM.id("block/casings/mechanic/machine_casing_turbine_stainless_steel"),
            GTPM.id("block/multiblock/generator/large_gas_turbine"),
            true,
            text -> text.source("Large Gas Turbine").translation(MinecraftLocale.ZH_CN, "大型燃气涡轮"));

    public static final MultiblockMachineDefinition LARGE_PLASMA_TURBINE = registerLargeTurbine(
            REGISTRATE, "plasma_large_turbine",
            IV,
            GTRecipeTypes.PLASMA_GENERATOR_FUELS,
            CASING_TUNGSTENSTEEL_TURBINE, CASING_TUNGSTENSTEEL_GEARBOX,
            GTPM.id("block/casings/mechanic/machine_casing_turbine_tungstensteel"),
            GTPM.id("block/multiblock/generator/large_plasma_turbine"),
            false);

    public static final MultiblockMachineDefinition ACTIVE_TRANSFORMER = REGISTRATE
            .multiblock("active_transformer", ActiveTransformerMachine::new)
            .localization(text -> text
                    .source("Active Transformer")
                    .translation(MinecraftLocale.ZH_CN, "有源变压器"))
            .rotationState(RotationState.ALL)
            .recipeType(GTRecipeTypes.DUMMY_RECIPES)
            .appearanceBlock(HIGH_POWER_CASING)
            .tooltips(Component.translatable("gtpm.machine.active_transformer.tooltip.0"),
                    Component.translatable("gtpm.machine.active_transformer.tooltip.1"))
            .tooltipBuilder(
                    (stack,
                     components) -> components.add(Component.translatable("gtpm.machine.active_transformer.tooltip.2")
                             .append(Component.translatable("gtpm.machine.active_transformer.tooltip.3")
                                     .withStyle(TooltipHelper.RAINBOW_HSL_SLOW))))
            .pattern(MultiblockControllerMachine.DEFAULT_STRUCTURE,
                    (definition) -> PatternBuilder.start(definition)
                            .appendDefinition()
                            .build())
            .workableCasingModel(GTPM.id("block/casings/hpca/high_power_casing"),
                    GTPM.id("block/multiblock/data_bank"))
            .register();

    public static final MultiblockMachineDefinition POWER_SUBSTATION = REGISTRATE
            .multiblock("power_substation", PowerSubstationMachine::new)
            .localization(text -> text
                    .source("Power Substation")
                    .translation(MinecraftLocale.ZH_CN, "蓄能变电站"))
            .rotationState(RotationState.ALL)
            .recipeType(GTRecipeTypes.DUMMY_RECIPES)
            .tooltips(Component.translatable("gtpm.machine.power_substation.tooltip.0"),
                    Component.translatable("gtpm.machine.power_substation.tooltip.1"),
                    Component.translatable("gtpm.machine.power_substation.tooltip.2",
                            PowerSubstationMachine.MAX_BATTERY_LAYERS),
                    Component.translatable("gtpm.machine.power_substation.tooltip.3"),
                    Component.translatable("gtpm.machine.power_substation.tooltip.4",
                            PowerSubstationMachine.PASSIVE_DRAIN_MAX_PER_STORAGE / 1000))
            .tooltipBuilder(
                    (stack,
                     components) -> components.add(Component.translatable("gtpm.machine.power_substation.tooltip.5")
                             .append(Component.translatable("gtpm.machine.power_substation.tooltip.6")
                                     .withStyle(TooltipHelper.RAINBOW_HSL_SLOW))))
            .appearanceBlock(CASING_PALLADIUM_SUBSTATION)
            .pattern(MultiblockControllerMachine.DEFAULT_STRUCTURE,
                    definition -> PatternBuilder.start(definition, RIGHT, BACK, UP)
                            .appendDefinition()
                            .build())
            .workableCasingModel(GTPM.id("block/casings/solid/machine_casing_palladium_substation"),
                    GTPM.id("block/multiblock/power_substation"))
            .register();

    public static final MultiblockMachineDefinition CHARCOAL_PILE_IGNITER = REGISTRATE
            .multiblock("charcoal_pile_igniter", CharcoalPileIgniterMachine::new)
            .localization(text -> text
                    .source("Charcoal Pile Igniter")
                    .translation(MinecraftLocale.ZH_CN, "木炭堆点火器"))
            .rotationState(RotationState.NONE)
            .recipeType(DUMMY_RECIPES)
            .appearanceBlock(BRONZE_HULL)
            .tooltips(Component.translatable("gtpm.machine.charcoal_pile.tooltip.0"),
                    Component.translatable("gtpm.machine.charcoal_pile.tooltip.1"),
                    Component.translatable("gtpm.machine.charcoal_pile.tooltip.2"),
                    Component.translatable("gtpm.machine.charcoal_pile.tooltip.3"))
            .pattern(MultiblockControllerMachine.DEFAULT_STRUCTURE, (def) -> PatternBuilder.start(def)
                    .appendDefinition()
                    .build())
            .allowFlip(false)
            .allowExtendedFacing(false)
            .workableCasingModel(GTPM.id("block/casings/solid/machine_casing_bronze_plated_bricks"),
                    GTPM.id("block/multiblock/charcoal_pile_igniter"))
            .register();

    public static MultiblockMachineDefinition[] BEDROCK_ORE_MINER = registerTieredMultis(
            "bedrock_ore_miner", BedrockOreMinerMachine::new, (tier, builder) -> builder
                    .rotationState(RotationState.NON_Y_AXIS)
                    .localization(text -> text
                            .source("%s Bedrock Ore Miner %s".formatted(VLVH[tier], VLVT[tier]))
                            .translation(MinecraftLocale.ZH_CN, switch (tier) {
                                case MV -> "§b进阶基岩采矿机§r";
                                case HV -> "§6进阶基岩采矿机 II§r";
                                case EV -> "§5进阶基岩采矿机 III§r";
                                default -> throw new IllegalArgumentException("Unsupported bedrock ore miner tier: " + tier);
                            }))
                    .recipeType(DUMMY_RECIPES)
                    .tooltips(
                            Component.translatable("gtpm.machine.bedrock_ore_miner.description"),
                            Component.translatable("gtpm.machine.bedrock_ore_miner.depletion",
                                    FormattingUtil.formatNumbers(
                                            100.0 / BedrockOreMinerMachine.getDepletionChance(tier))),
                            Component.translatable("gtpm.universal.tooltip.energy_tier_range",
                                    GTValues.VNF[tier], GTValues.VNF[tier + 1]),
                            Component.translatable("gtpm.machine.bedrock_ore_miner.production",
                                    BedrockOreMinerMachine.getRigMultiplier(tier),
                                    FormattingUtil.formatNumbers(
                                            BedrockOreMinerMachine.getRigMultiplier(tier) * 1.5)))
                    .appearanceBlock(() -> BedrockOreMinerMachine.getCasingState(tier))
                    .pattern(MultiblockControllerMachine.DEFAULT_STRUCTURE,
                            (definition) -> PatternBuilder.start(definition)
                                    .appendDefinition()
                                    .build())
                    .workableCasingModel(BedrockOreMinerMachine.getBaseTexture(tier),
                            GTPM.id("block/multiblock/bedrock_ore_miner"))
                    .register(),
            MV, HV, EV);

    // Multiblock Tanks
    public static final MachineDefinition WOODEN_TANK_VALVE = tankValve(REGISTRATE, "wooden_tank_valve", false)
            .localization(text -> text.source("Wooden Tank Valve").translation(MinecraftLocale.ZH_CN, "木制储罐阀门"))
            .sidedWorkableCasingModel(GTPM.id("block/casings/wood_wall"), TANK_VALVE_OVERLAY)
            .register();
    public static final MultiblockMachineDefinition WOODEN_MULTIBLOCK_TANK = multiblockTank(REGISTRATE,
            "wooden_multiblock_tank", 250 * 1000, CASING_WOOD_WALL,
            new PropertyFluidFilter(340, false, false, false, false))
            .localization(text -> text.source("Wooden Multiblock Tank").translation(MinecraftLocale.ZH_CN, "木制多方块储罐"))
            .sidedWorkableCasingModel(GTPM.id("block/casings/wood_wall"), MULTIBLOCK_TANK_OVERLAY)
            .register();

    public static final MachineDefinition BRONZE_TANK_VALVE = tankValve(REGISTRATE, "bronze_tank_valve", true)
            .localization(text -> text.source("Bronze Tank Valve").translation(MinecraftLocale.ZH_CN, "青铜储罐阀门"))
            .workableCasingModel(GTPM.id("block/casings/solid/machine_casing_bronze_plated_bricks"), TANK_VALVE_OVERLAY)
            .register();
    public static final MultiblockMachineDefinition BRONZE_MULTIBLOCK_TANK = multiblockTank(REGISTRATE,
            "bronze_multiblock_tank", 500 * 1000, CASING_BRONZE_BRICKS,
            new PropertyFluidFilter(1696, true, false, false, false))
            .localization(text -> text.source("Bronze Multiblock Tank").translation(MinecraftLocale.ZH_CN, "青铜多方块储罐"))
            .workableCasingModel(GTPM.id("block/casings/solid/machine_casing_bronze_plated_bricks"), MULTIBLOCK_TANK_OVERLAY)
            .register();

    public static final MachineDefinition STEEL_TANK_VALVE = tankValve(REGISTRATE, "steel_tank_valve", true)
            .localization(text -> text.source("Steel Tank Valve").translation(MinecraftLocale.ZH_CN, "钢制储罐阀门"))
            .workableCasingModel(GTPM.id("block/casings/solid/machine_casing_solid_steel"), TANK_VALVE_OVERLAY)
            .register();
    public static final MultiblockMachineDefinition STEEL_MULTIBLOCK_TANK = multiblockTank(REGISTRATE,
            "steel_multiblock_tank", 1000 * 1000, CASING_STEEL_SOLID, null)
            .localization(text -> text.source("Steel Multiblock Tank").translation(MinecraftLocale.ZH_CN, "钢制多方块储罐"))
            .workableCasingModel(GTPM.id("block/casings/solid/machine_casing_solid_steel"), MULTIBLOCK_TANK_OVERLAY)
            .register();

    public static final MultiblockMachineDefinition CENTRAL_MONITOR = REGISTRATE
            .multiblock("central_monitor", CentralMonitorMachine::new)
            .localization(text -> text
                    .source("Central Monitor")
                    .translation(MinecraftLocale.ZH_CN, "中央监控器"))
            .rotationState(RotationState.ALL)
            .recipeType(DUMMY_RECIPES)
            .appearanceBlock(CASING_ALUMINIUM_FROSTPROOF)
            .pattern(MultiblockControllerMachine.DEFAULT_STRUCTURE,
                    (definition) -> PatternBuilder.start(definition)
                            .appendDefinition()
                            .build())
            .modelProperty(RecipeLogic.STATUS_PROPERTY, WorkLogic.Status.IDLE)
            .model(createWorkableCasingMachineModel(
                    GTPM.id("block/casings/solid/machine_casing_frost_proof"),
                    GTPM.id("block/multiblock/central_monitor"))
                    .andThen(b -> b.addDynamicRenderer(DynamicRenderHelper::createCentralMonitorRender)))
            .hasBER(true)
            .register();

    public static void init() {}
}
