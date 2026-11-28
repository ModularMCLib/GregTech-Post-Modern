package com.modularmc.gtpm.common.data;

import com.modularmc.gtpm.GTPM;
import com.modularmc.gtpm.api.GTCEuAPI;
import com.modularmc.gtpm.api.GTValues;
import com.modularmc.gtpm.api.capability.IMiner;
import com.modularmc.gtpm.api.capability.recipe.IO;
import com.modularmc.gtpm.api.capability.recipe.ItemRecipeCapability;
import com.modularmc.gtpm.api.data.RotationState;
import com.modularmc.gtpm.api.machine.MachineDefinition;
import com.modularmc.gtpm.api.machine.SimpleTieredMachine;
import com.modularmc.gtpm.api.machine.multiblock.CleanroomType;
import com.modularmc.gtpm.api.machine.multiblock.PartAbility;
import com.modularmc.gtpm.api.machine.property.GTMachineModelProperties;
import com.modularmc.gtpm.api.machine.steam.SimpleSteamMachine;
import com.modularmc.gtpm.api.machine.steam.SteamBoilerMachine;
import com.modularmc.gtpm.api.multiblock.util.RelativeDirection;
import com.modularmc.gtpm.api.recipe.GTRecipeType;
import com.modularmc.gtpm.api.registry.GTRegistries;
import com.modularmc.gtpm.client.model.machine.MachineRenderState;
import com.modularmc.gtpm.client.renderer.machine.DynamicRenderHelper;
import com.modularmc.gtpm.client.util.TooltipHelper;
import com.modularmc.gtpm.common.data.machines.*;
import com.modularmc.gtpm.common.data.machines.tooltip.MachineTooltips;
import com.modularmc.gtpm.common.data.models.GTModels;
import com.modularmc.gtpm.common.machine.electric.*;
import com.modularmc.gtpm.common.machine.multiblock.part.*;
import com.modularmc.gtpm.common.machine.multiblock.part.monitor.AdvancedMonitorPartMachine;
import com.modularmc.gtpm.common.machine.multiblock.part.monitor.MonitorPartMachine;
import com.modularmc.gtpm.common.machine.steam.SteamLiquidBoilerMachine;
import com.modularmc.gtpm.common.machine.steam.SteamMinerMachine;
import com.modularmc.gtpm.common.machine.steam.SteamSolarBoiler;
import com.modularmc.gtpm.common.machine.steam.SteamSolidBoilerMachine;
import com.modularmc.gtpm.common.machine.storage.*;
import com.modularmc.gtpm.common.pipelike.fluidpipe.longdistance.LDFluidEndpointMachine;
import com.modularmc.gtpm.common.pipelike.item.longdistance.LDItemEndpointMachine;
import com.modularmc.gtpm.config.ConfigHolder;
import com.modularmc.gtpm.data.lang.LangHandler;
import com.modularmc.gtpm.utils.FormattingUtil;

import com.modularmc.registrate.localization.LocalizationDeclaration;
import com.modularmc.registrate.localization.MinecraftLocale;
import com.modularmc.registrate.localization.annotation.TransVariants;
import com.modularmc.registrate.localization.annotation.Translation;
import com.modularmc.registrate.localization.annotation.Variant;

import net.minecraft.ChatFormatting;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;

import com.google.common.math.IntMath;
import it.unimi.dsi.fastutil.Pair;
import it.unimi.dsi.fastutil.ints.Int2IntFunction;
import org.jspecify.annotations.Nullable;

import java.util.Iterator;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.IntFunction;

import static com.modularmc.gtpm.api.GTValues.*;
import static com.modularmc.gtpm.api.capability.recipe.IO.*;
import static com.modularmc.gtpm.api.machine.property.GTMachineModelProperties.*;
import static com.modularmc.gtpm.common.data.GTCreativeModeTabs.MACHINE;
import static com.modularmc.gtpm.common.data.GTRecipeTypes.DUMMY_RECIPES;
import static com.modularmc.gtpm.common.data.GTRecipeTypes.STEAM_BOILER_RECIPES;
import static com.modularmc.gtpm.common.data.machines.GTMachineUtils.*;
import static com.modularmc.gtpm.common.data.machines.GTMachineUtils.ALL_TIERS;
import static com.modularmc.gtpm.common.data.models.GTMachineModels.*;
import static com.modularmc.gtpm.common.registry.GTRegistration.REGISTRATE;

@TransVariants(keyPrefix = "gtpm",
               value = {
                       @Variant(id = "part_sharing.disabled",
                                source = "Multiblock Sharing §4Disabled",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "多方块结构共享：§4禁止")),
                       @Variant(id = "part_sharing.enabled",
                                source = "Multiblock Sharing §aEnabled",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "多方块结构共享：§a允许")),
                       @Variant(id = "creative_tooltip.1",
                                source = "§7You just need",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7你需要")),
                       @Variant(id = "creative_tooltip.2",
                                source = " Creative Mode",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§b创造模式")),
                       @Variant(id = "creative_tooltip.3",
                                source = "§7 to use this",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7来使用它")),
                       @Variant(id = "machine.block_breaker.speed_bonus",
                                source = "§eSpeed Bonus: §f%d%%",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§e速度奖励：§f%d%%")),
                       @Variant(id = "machine.block_breaker.tooltip",
                                source = "§7Mines block on front face and collects its drops",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7挖掘正面的方块并收集掉落物")),
                       @Variant(id = "machine.buffer.tooltip",
                                source = "A Small Buffer to store Items and Fluids",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "存储物品和流体的小小缓冲器")),
                       @Variant(id = "machine.diode.tooltip_general",
                                source = "Allows Energy Flow in one direction and limits Amperage",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "将只允许能量向一个方向流动，并限制电流。")),
                       @Variant(id = "machine.diode.tooltip_starts_at",
                                source = "Starts as §f1A§7, use Soft Mallet to change",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "默认允许§f1A§r电流通行，使用软锤切换。")),
                       @Variant(id = "machine.dual_hatch.export.tooltip",
                                source = "Item and Fluid Output for Multiblocks",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "为多方块结构输出物品和流体")),
                       @Variant(id = "machine.dual_hatch.import.tooltip",
                                source = "Item and Fluid Input for Multiblocks",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "为多方块结构输入物品和流体")),
                       @Variant(id = "machine.endpoint.tooltip.0",
                                source = "Connect with §fLong Distance Pipe§7 blocks to create a pipeline.",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "使用§f长距离管道§7方块连接管道接口以创建管路。")),
                       @Variant(id = "machine.endpoint.tooltip.1",
                                source = "Pipelines must have exactly §f1 Input§7 and §f1 Output§7 endpoint.",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "管路必须有且仅有一个§f1输入§7接口和一个§f1输出§7接口。")),
                       @Variant(id = "machine.endpoint.tooltip.2",
                                source = "Only pipeline endpoints need to be §fchunk-loaded§7.",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "只有管道接口需要被§f区块加载§7。")),
                       @Variant(id = "machine.endpoint.tooltip.min_length",
                                source = "§bMinimum Endpoint Distance: §f%d Blocks",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§b最低接口间距：§f%d格方块")),
                       @Variant(id = "machine.energy_hatch.input_hi_amp.tooltip",
                                source = "Multiple Ampere Energy Input for Multiblocks",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "为多方块结构输入更多电流")),
                       @Variant(id = "machine.energy_hatch.input.tooltip",
                                source = "Energy Input for Multiblocks",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "为多方块结构输入能量")),
                       @Variant(id = "machine.energy_hatch.output_hi_amp.tooltip",
                                source = "Multiple Ampere Energy Output for Multiblocks",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "为多方块结构输出更多电流")),
                       @Variant(id = "machine.energy_hatch.output.tooltip",
                                source = "Energy Output for Multiblocks",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "为多方块结构输出能量")),
                       @Variant(id = "machine.fisher.requirement",
                                source = "Requires a %dx%d centered square of water directly below.",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "需要以机器正下方为中心的%dx%d正方形水源。")),
                       @Variant(id = "machine.fisher.speed",
                                source = "Catches something every %d ticks",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "每%d刻抓到一些东西")),
                       @Variant(id = "machine.fisher.tooltip",
                                source = "Costs string to fish. Consumes one string each time.",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "每次消耗一根线捕鱼")),
                       @Variant(id = "machine.hull.tooltip",
                                source = "§7You just need §5I§dm§4a§cg§ei§an§ba§3t§7i§1o§5n§7 to use this",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "§7你需要§5绝§d对§4天§c马§e行§a空§b的§3想§9象§1能§5力§7来使用它")),
                       @Variant(id = "machine.item_collector.gui.collect_range",
                                source = "Collect within an area of %sx%s blocks",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "作用区域：%sx%s格")),
                       @Variant(id = "machine.item_collector.tooltip",
                                source = "Collects Items around itself",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "吸取它周围的掉落物")),
                       @Variant(id = "machine.maintenance_hatch_cleanroom_auto.tooltip.0",
                                source = "For automatically maintaining Multiblocks with Cleaning!",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "可以自动维护多方块结构，附带清洁功能！")),
                       @Variant(id = "machine.maintenance_hatch_cleanroom_auto.tooltip.1",
                                source = "Cleans as:",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "清洁方式：")),
                       @Variant(id = "machine.miner.per_block",
                                source = "§7takes §f%ds §7per Block",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7每个方块需要§f%d§7秒。")),
                       @Variant(id = "machine.miner.tooltip",
                                source = "§7Mines ores below the Miner! Starts as §f%sx%s §7area",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "§7只挖掘机器下方的矿石！工作范围默认为§f%sx%s§7。")),
                       @Variant(id = "machine.muffler_hatch.tooltip.0",
                                source = "Recovers waste from machines",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "从机器中回收废料")),
                       @Variant(id = "machine.muffler_hatch.tooltip.1",
                                source = "DO NOT OBSTRUCT THE OUTPUT!",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§l切 勿 阻 隔 输 出 口！")),
                       @Variant(id = "machine.pump.tooltip",
                                source = "§7The best way to empty Oceans!",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7抽干大海的最佳手段！")),
                       @Variant(id = "machine.rotor_holder.tooltip.0",
                                source = "Rotor Holder for Multiblocks",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "为多方块结构固定转子，")),
                       @Variant(id = "machine.rotor_holder.tooltip.1",
                                source = "Holds Rotor in place so it will not fly away",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "使其不能飞走")),
                       @Variant(id = "machine.steam_bus.tooltip",
                                source = "Does not work with non-steam multiblocks",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "无法作用于非蒸汽驱动的多方块结构")),
                       @Variant(id = "machine.steam.steam_hatch.tooltip",
                                source = "§eAccepted Fluid: §fSteam",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§e接受流体：§f蒸汽")),
                       @Variant(id = "machine.substation_hatch.input.tooltip",
                                source = "Energy Input for the Power Substation",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "为蓄能变电站输入能量")),
                       @Variant(id = "machine.substation_hatch.output.tooltip",
                                source = "Energy Output for the Power Substation",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "为蓄能变电站输出能量")),
                       @Variant(id = "machine.world_accelerator.description",
                                source = "Tick accelerates nearby blocks in one of 2 modes: §fTile Entity§7 or §fRandom Tick§7. Use Screwdriver to change mode.",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "以两种模式之一加速周围方块的更新刻：§f方块实体§r或§f随机刻§7。使用螺丝刀切换模式。")),
                       @Variant(id = "machine.world_accelerator.working_area",
                                source = "§bWorking Area:",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§b作用范围：")),
                       @Variant(id = "machine.world_accelerator.working_area_random",
                                source = "  Random Tick Mode:§f %dx%d",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "  随机刻模式：§f%dx%d")),
                       @Variant(id = "machine.world_accelerator.working_area_tile",
                                source = "  Block Entity Mode:§f Adjacent Blocks",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "  方块实体模式：§f相邻方块")),
                       @Variant(id = "muffler.recovery_tooltip",
                                source = "§bRecovery Chance: §f%d%%",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§b回收概率：§f%d%%")),
                       @Variant(id = "universal.tooltip.amperage_in_out_till",
                                source = "§eAmperage IN/OUT up to: §f%dA",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "§e输入/输出电流上限：§f%dA")),
                       @Variant(id = "universal.tooltip.amperage_out",
                                source = "§eAmperage OUT: §f%dA",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§e输出电流：§f%dA")),
                       @Variant(id = "universal.tooltip.uses_per_tick",
                                source = "Uses §f%d EU/t §7while working",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "工作时消耗§f%d EU/t§7")),
                       @Variant(id = "universal.tooltip.uses_per_tick_steam",
                                source = "Uses §f%d mB/t §7of §fSteam §7while working",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "工作时消耗§f%d mB/t§7§f蒸汽§7")),
                       @Variant(id = "universal.tooltip.working_area_max",
                                source = "§bMax Working Area: §f%dx%d",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§b最大工作范围：§f%dx%d")),
                       @Variant(id = "machine.maintenance_hatch_configurable.tooltip.0",
                                source = "For finer control over Multiblocks",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "更精细的维护多方块结构")),
                       @Variant(id = "machine.maintenance_hatch_configurable.tooltip.1",
                                source = "Starts with no Maintenance problems!",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "起手无需维护！")),
                       @Variant(id = "machine.machine_hatch.locked",
                                source = "Machine Interface Locked",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "机器接口已锁定")),
                       @Variant(id = "machine.machine_hatch.processing_array",
                                source = "When in the §eProcessing Array§7, only holds machines that work in the §eProcessing Array",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "当在§e处理阵列§7中时，仅可搭载适用于§e处理阵列§7的机器")),
               })
public class GTMachines {

    static {
        REGISTRATE.creativeModeTab(MACHINE);
    }

    //////////////////////////////////////
    // ****** Steam Machine ******//
    //////////////////////////////////////
    public static final Pair<MachineDefinition, MachineDefinition> STEAM_SOLID_BOILER = registerSteamMachines(
            "steam_solid_boiler",
            SteamSolidBoilerMachine::new,
            (pressure, builder) -> builder.rotationState(RotationState.ALL)
                    .recipeType(STEAM_BOILER_RECIPES)
                    .recipeModifier(SteamBoilerMachine::recipeModifier)
                    .workableSteamHullModel(pressure, GTPM.id("block/generators/boiler/coal"))
                    .tooltips(Component.translatable("gtpm.universal.tooltip.produces_fluid",
                            (pressure ? ConfigHolder.INSTANCE.machines.smallBoilers.hpSolidBoilerBaseOutput :
                                    ConfigHolder.INSTANCE.machines.smallBoilers.solidBoilerBaseOutput) *
                                    FluidType.BUCKET_VOLUME / 20000))
                    .register());

    public static final Pair<MachineDefinition, MachineDefinition> STEAM_LIQUID_BOILER = registerSteamMachines(
            "steam_liquid_boiler",
            SteamLiquidBoilerMachine::new,
            (pressure, builder) -> builder.rotationState(RotationState.ALL)
                    .recipeType(STEAM_BOILER_RECIPES)
                    .recipeModifier(SteamBoilerMachine::recipeModifier)
                    .workableSteamHullModel(pressure, GTPM.id("block/generators/boiler/lava"))
                    .tooltips(Component.translatable("gtpm.universal.tooltip.produces_fluid",
                            (pressure ? ConfigHolder.INSTANCE.machines.smallBoilers.hpLiquidBoilerBaseOutput :
                                    ConfigHolder.INSTANCE.machines.smallBoilers.liquidBoilerBaseOutput) *
                                    FluidType.BUCKET_VOLUME / 20000))
                    .register());

    public static final Pair<MachineDefinition, MachineDefinition> STEAM_SOLAR_BOILER = registerSteamMachines(
            "steam_solar_boiler",
            SteamSolarBoiler::new,
            (pressure, builder) -> builder.rotationState(RotationState.NON_Y_AXIS)
                    .recipeType(STEAM_BOILER_RECIPES)
                    .recipeModifier(SteamBoilerMachine::recipeModifier)
                    .workableSteamHullModel(pressure, GTPM.id("block/generators/boiler/solar"))
                    .tooltips(Component.translatable("gtpm.universal.tooltip.produces_fluid",
                            (pressure ? ConfigHolder.INSTANCE.machines.smallBoilers.hpSolarBoilerBaseOutput :
                                    ConfigHolder.INSTANCE.machines.smallBoilers.solarBoilerBaseOutput) *
                                    FluidType.BUCKET_VOLUME / 20000))
                    .register());

    public static final Pair<MachineDefinition, MachineDefinition> STEAM_EXTRACTOR = registerSimpleSteamMachines(
            "extractor", GTRecipeTypes.EXTRACTOR_RECIPES);
    public static final Pair<MachineDefinition, MachineDefinition> STEAM_MACERATOR = registerSteamMachines(
            "steam_macerator", SimpleSteamMachine::new, (pressure, builder) -> builder
                    .rotationState(RotationState.NON_Y_AXIS)
                    .recipeType(GTRecipeTypes.MACERATOR_RECIPES)
                    .recipeModifier(SimpleSteamMachine::recipeModifier)
                    .addOutputLimit(ItemRecipeCapability.CAP, 1)
                    .modelProperty(GTMachineModelProperties.VENT_DIRECTION, RelativeDirection.BACK)
                    .workableSteamHullModel(pressure, GTPM.id("block/machines/macerator"))
                    .register());
    public static final Pair<MachineDefinition, MachineDefinition> STEAM_COMPRESSOR = registerSimpleSteamMachines(
            "compressor", GTRecipeTypes.COMPRESSOR_RECIPES);
    public static final Pair<MachineDefinition, MachineDefinition> STEAM_HAMMER = registerSimpleSteamMachines(
            "forge_hammer", GTRecipeTypes.FORGE_HAMMER_RECIPES);
    public static final Pair<MachineDefinition, MachineDefinition> STEAM_FURNACE = registerSimpleSteamMachines(
            "furnace", GTRecipeTypes.FURNACE_RECIPES);
    public static final Pair<MachineDefinition, MachineDefinition> STEAM_ALLOY_SMELTER = registerSimpleSteamMachines(
            "alloy_smelter", GTRecipeTypes.ALLOY_SMELTER_RECIPES);
    public static final Pair<MachineDefinition, MachineDefinition> STEAM_ROCK_CRUSHER = registerSimpleSteamMachines(
            "rock_crusher", GTRecipeTypes.ROCK_BREAKER_RECIPES);
    public static final Pair<MachineDefinition, MachineDefinition> STEAM_MINER = registerSteamMachines(
            "steam_miner",
            (holder, isHP) -> isHP ? new SteamMinerMachine(holder, true, 240, 6, 0, 32) :
                    new SteamMinerMachine(holder, false, 320, 4, 0, 16),
            (isHP, builder) -> builder
                    .rotationState(RotationState.NON_Y_AXIS)
                    .recipeType(DUMMY_RECIPES)
                    .tooltips(Component.translatable("gtpm.universal.tooltip.uses_per_tick_steam", isHP ? 32 : 16)
                            .append(ChatFormatting.GRAY + ", ")
                            .append(Component.translatable("gtpm.machine.miner.per_block",
                                    isHP ? 240 / 20 : 280 / 20)))
                    .tooltipBuilder((item, tooltip) -> {
                        int maxArea = IMiner.getWorkingArea(isHP ? 6 : 4);
                        tooltip.add(Component.translatable("gtpm.universal.tooltip.working_area", maxArea, maxArea));
                    })
                    .modelProperty(GTMachineModelProperties.VENT_DIRECTION, RelativeDirection.UP)
                    .workableSteamHullModel(isHP, isHP ?
                            GTPM.id("block/machines/high_pressure_steam_miner") :
                            GTPM.id("block/machines/steam_miner"))
                    .register());

    //////////////////////////////////////
    // *** SimpleTieredMachine ***//
    //////////////////////////////////////
    public static final MachineDefinition[] HULL = GTMachineUtils.registerTieredMachines("machine_hull",
            HullMachine::new,
            (tier, builder) -> builder
                    .rotationState(RotationState.ALL)
                    .overlayTieredHullModel("hull")
                    .abilities(PartAbility.PASSTHROUGH_HATCH)
                    .modelProperty(IS_FORMED, false)
                    .localization(text -> text
                            .source("%s §fMachine Hull".formatted(VNF[tier]))
                            .translation(MinecraftLocale.ZH_CN, VNF[tier] + "§f机器外壳"))
                    .tooltips(Component.translatable("gtpm.machine.hull.tooltip"))
                    .register(),
            ALL_TIERS);

    public static final MachineDefinition[] ELECTRIC_FURNACE = registerRecipeMachines("electric_furnace",
            GTRecipeTypes.FURNACE_RECIPES, standardTooltip(
                    text -> text.source("§7Not like using a Commodore 64")
                            .translation(MinecraftLocale.ZH_CN, "§7别把它当成 Commodore 64 了"),
                    text -> text.source("§7Electron Excitement Processor")
                            .translation(MinecraftLocale.ZH_CN, "§7电子激发处理器"),
                    text -> text.source("§7Atom Stimulator").translation(MinecraftLocale.ZH_CN, "§7原子激发器")));
    public static final MachineDefinition[] ALLOY_SMELTER = registerRecipeMachines("alloy_smelter",
            GTRecipeTypes.ALLOY_SMELTER_RECIPES, standardTooltip(
                    text -> text.source("§7HighTech combination Smelter")
                            .translation(MinecraftLocale.ZH_CN, "§7高科技融合冶炼炉"),
                    text -> text.source("§7Alloy Integrator").translation(MinecraftLocale.ZH_CN, "§7合金集聚器"),
                    text -> text.source("§7Metal Amalgamator").translation(MinecraftLocale.ZH_CN, "§7金属混合调制装置")));
    public static final MachineDefinition[] ARC_FURNACE = registerRecipeMachines("arc_furnace",
            GTRecipeTypes.ARC_FURNACE_RECIPES, hvCappedTankSizeFunction, false, standardTooltip(
                    text -> text.source("§7Who needs a Blast Furnace?").translation(MinecraftLocale.ZH_CN, "§7谁需要高炉？"),
                    text -> text.source("§7Discharge Heater").translation(MinecraftLocale.ZH_CN, "§7放电加热器"),
                    text -> text.source("§7Short Circuit Heater").translation(MinecraftLocale.ZH_CN, "§7短路加热器")));
    public static final MachineDefinition[] ASSEMBLER = registerRecipeMachines("assembler",
            GTRecipeTypes.ASSEMBLER_RECIPES, hvCappedTankSizeFunction, true, standardTooltip(
                    text -> text.source("§7Avengers, Assemble!").translation(MinecraftLocale.ZH_CN, "§7复仇者，集结！"),
                    text -> text.source("§7NOT a Crafting Table").translation(MinecraftLocale.ZH_CN, "§l不是§7工作台"),
                    text -> text.source("§7Assembly Constructor").translation(MinecraftLocale.ZH_CN, "§7装配构建者")));
    public static final MachineDefinition[] AUTOCLAVE = registerRecipeMachines("autoclave",
            GTRecipeTypes.AUTOCLAVE_RECIPES, hvCappedTankSizeFunction, false, standardTooltip(
                    text -> text.source("§7Crystallizing your Dusts").translation(MinecraftLocale.ZH_CN, "§7使粉末结晶"),
                    text -> text.source("§7Pressure Cooker").translation(MinecraftLocale.ZH_CN, "§7高压锅"),
                    text -> text.source("§7Encumbrance Unit").translation(MinecraftLocale.ZH_CN, "§7重压单元")));
    public static final MachineDefinition[] BENDER = registerRecipeMachines("bender", GTRecipeTypes.BENDER_RECIPES,
            standardTooltip(
                    text -> text.source("§7Boo, he's bad! We want BENDER!!!")
                            .translation(MinecraftLocale.ZH_CN, "§7吁，他不行！我们要的是BENDER！！！"),
                    text -> text.source("§7Shape Distorter").translation(MinecraftLocale.ZH_CN, "§7形态扭曲器"),
                    text -> text.source("§7Matter Deformer").translation(MinecraftLocale.ZH_CN, "§7物质畸变者")));
    public static final MachineDefinition[] BREWERY = registerRecipeMachines("brewery", GTRecipeTypes.BREWING_RECIPES,
            hvCappedTankSizeFunction, false, standardTooltip(
                    text -> text.source("§7Compact and efficient potion brewing")
                            .translation(MinecraftLocale.ZH_CN, "§7紧凑高效的药剂酿造"),
                    text -> text.source("§7Brewing your Drinks").translation(MinecraftLocale.ZH_CN, "§7酿酒好手"),
                    text -> text.source("§7Brew Rusher").translation(MinecraftLocale.ZH_CN, "§7强劲酿制者")));
    public static final MachineDefinition[] CANNER = registerRecipeMachines("canner", GTRecipeTypes.CANNER_RECIPES,
            standardTooltip(
                    text -> text.source("§7Puts things into and out of Containers")
                            .translation(MinecraftLocale.ZH_CN, "§7为容器填入或从中倒出物品及流体"),
                    text -> text.source("§7Can Operator").translation(MinecraftLocale.ZH_CN, "§7罐作机"),
                    text -> text.source("§7Can Actuator").translation(MinecraftLocale.ZH_CN, "§7罐子致动器")));
    public static final MachineDefinition[] CENTRIFUGE = registerRecipeMachines("centrifuge",
            GTRecipeTypes.CENTRIFUGE_RECIPES, largeTankSizeFunction, false, tier -> switch (tier) {
                case LV, MV, HV -> text -> text.source("§7Separating Molecules").translation(MinecraftLocale.ZH_CN,
                        "§7分离分子");
                case EV -> text -> text.source("§7Molecular Separator").translation(MinecraftLocale.ZH_CN, "§7分子分离机");
                case IV, LuV, ZPM -> text -> text.source("§7Molecular Cyclone").translation(MinecraftLocale.ZH_CN,
                        "§7分子旋风");
                case UV -> text -> text.source("§7Molecular Tornado").translation(MinecraftLocale.ZH_CN, "§7分子龙卷风");
                default -> null;
            });
    public static final MachineDefinition[] CHEMICAL_BATH = registerRecipeMachines("chemical_bath",
            GTRecipeTypes.CHEMICAL_BATH_RECIPES, hvCappedTankSizeFunction, false, standardTooltip(
                    text -> text.source("§7Bathing Ores in Chemicals to separate them")
                            .translation(MinecraftLocale.ZH_CN, "§7用化学品浸渍以分离矿物"),
                    text -> text.source("§7Chemical Soaker").translation(MinecraftLocale.ZH_CN, "§7化学品浸润器"),
                    text -> text.source("§7Chemical Dunktron").translation(MinecraftLocale.ZH_CN, "§7化学沉浸洗涤者")));
    public static final MachineDefinition[] CHEMICAL_REACTOR = registerRecipeMachines("chemical_reactor",
            GTRecipeTypes.CHEMICAL_RECIPES, tier -> 16 * FluidType.BUCKET_VOLUME, true, standardTooltip(
                    text -> text.source("§7Letting Chemicals react with each other")
                            .translation(MinecraftLocale.ZH_CN, "§7让化学品相互反应"),
                    text -> text.source("§7Chemical Performer").translation(MinecraftLocale.ZH_CN, "§7化学表演艺术家"),
                    text -> text.source("§7Reaction Catalyzer").translation(MinecraftLocale.ZH_CN, "§7反应催化器")));
    public static final MachineDefinition[] COMPRESSOR = registerRecipeMachines("compressor",
            GTRecipeTypes.COMPRESSOR_RECIPES, standardTooltip(
                    text -> text.source("§7Compress-O-Matic C77").translation(MinecraftLocale.ZH_CN, "§7全自动压缩机 C77"),
                    text -> text.source("§7Singularity Condenser").translation(MinecraftLocale.ZH_CN, "§7奇点凝缩机"),
                    text -> text.source("§7Matter Constrictor").translation(MinecraftLocale.ZH_CN, "§7物质约束器")));
    public static final MachineDefinition[] CUTTER = registerRecipeMachines("cutter", GTRecipeTypes.CUTTER_RECIPES,
            standardTooltip(
                    text -> text.source("§7Slice'N Dice").translation(MinecraftLocale.ZH_CN, "§7切片，切块"),
                    text -> text.source("§7Matter Cleaver").translation(MinecraftLocale.ZH_CN, "§7物质切削器"),
                    text -> text.source("§7Object Divider").translation(MinecraftLocale.ZH_CN, "§7对象分割者")));
    public static final MachineDefinition[] DISTILLERY = registerRecipeMachines("distillery",
            GTRecipeTypes.DISTILLERY_RECIPES, hvCappedTankSizeFunction, false, standardTooltip(
                    text -> text.source("§7Extracting most relevant Parts of Fluids")
                            .translation(MinecraftLocale.ZH_CN, "§7提取流体精华"),
                    text -> text.source("§7Condensation Separator").translation(MinecraftLocale.ZH_CN, "§7凝结物质分离器"),
                    text -> text.source("§7Fraction Splitter").translation(MinecraftLocale.ZH_CN, "§7馏分分离器")));
    public static final MachineDefinition[] ELECTROLYZER = registerRecipeMachines("electrolyzer",
            GTRecipeTypes.ELECTROLYZER_RECIPES, largeTankSizeFunction, false, tier -> switch (tier) {
                case LV, MV, HV, EV -> text -> text.source("§7Electrolyzing Molecules")
                        .translation(MinecraftLocale.ZH_CN, "§7电离分子");
                case IV -> text -> text.source("§7Molecular Disintegrator E-4906")
                        .translation(MinecraftLocale.ZH_CN, "§7分子崩解机 E-4906");
                case LuV -> text -> text.source("§7Molecular Disintegrator E-4907")
                        .translation(MinecraftLocale.ZH_CN, "§7分子崩解机 E-4907");
                case ZPM -> text -> text.source("§7Molecular Disintegrator E-4908")
                        .translation(MinecraftLocale.ZH_CN, "§7分子崩解机 E-4908");
                case UV -> text -> text.source("§7Atomic Ionizer").translation(MinecraftLocale.ZH_CN, "§7原子离子化装置");
                default -> null;
            });
    public static final MachineDefinition[] ELECTROMAGNETIC_SEPARATOR = registerRecipeMachines(
            "electromagnetic_separator", GTRecipeTypes.ELECTROMAGNETIC_SEPARATOR_RECIPES, standardTooltip(
                    text -> text.source("§7Separating the magnetic Ores from the rest")
                            .translation(MinecraftLocale.ZH_CN, "§7分离出磁性矿物"),
                    text -> text.source("§7EM Categorizer").translation(MinecraftLocale.ZH_CN, "§7电磁分类器"),
                    text -> text.source("§7EMF Dispeller").translation(MinecraftLocale.ZH_CN, "§7电磁场驱离装置")));
    public static final MachineDefinition[] EXTRACTOR = registerRecipeMachines("extractor",
            GTRecipeTypes.EXTRACTOR_RECIPES, standardTooltip(
                    text -> text.source("§7Dejuicer-Device of Doom - D123")
                            .translation(MinecraftLocale.ZH_CN, "§7毁灭级榨汁机 - D123"),
                    text -> text.source("§7Vacuum Extractinator").translation(MinecraftLocale.ZH_CN, "§7真空提取机"),
                    text -> text.source("§7Liquefying Sucker").translation(MinecraftLocale.ZH_CN, "§7液化吸取者")));
    public static final MachineDefinition[] EXTRUDER = registerRecipeMachines("extruder",
            GTRecipeTypes.EXTRUDER_RECIPES, standardTooltip(
                    text -> text.source("§7Universal Machine for Metal Working")
                            .translation(MinecraftLocale.ZH_CN, "§7通用型金属加工器"),
                    text -> text.source("§7Material Displacer").translation(MinecraftLocale.ZH_CN, "§7材料压出器"),
                    text -> text.source("§7Shape Driver").translation(MinecraftLocale.ZH_CN, "§7形制驱动者")));
    public static final MachineDefinition[] FERMENTER = registerRecipeMachines("fermenter",
            GTRecipeTypes.FERMENTING_RECIPES, hvCappedTankSizeFunction, false, standardTooltip(
                    text -> text.source("§7Fermenting Fluids").translation(MinecraftLocale.ZH_CN, "§7发酵流体"),
                    text -> text.source("§7Fermentation Hastener").translation(MinecraftLocale.ZH_CN, "§7发酵加速器"),
                    text -> text.source("§7Respiration Controller").translation(MinecraftLocale.ZH_CN, "§7呼吸控制器")));
    public static final MachineDefinition[] FLUID_HEATER = registerRecipeMachines("fluid_heater",
            GTRecipeTypes.FLUID_HEATER_RECIPES, hvCappedTankSizeFunction, false, standardTooltip(
                    text -> text.source("§7Heating up your Fluids").translation(MinecraftLocale.ZH_CN, "§7加热你的流体"),
                    text -> text.source("§7Heat Infuser").translation(MinecraftLocale.ZH_CN, "§7热量灌注器"),
                    text -> text.source("§7Thermal Imbuer").translation(MinecraftLocale.ZH_CN, "§7热量灌输器")));
    public static final MachineDefinition[] FLUID_SOLIDIFIER = registerRecipeMachines("fluid_solidifier",
            GTRecipeTypes.FLUID_SOLIDFICATION_RECIPES, hvCappedTankSizeFunction, false, standardTooltip(
                    text -> text.source("§7Cools Fluids down to form Solids")
                            .translation(MinecraftLocale.ZH_CN, "§7冷却液体形成固体"),
                    text -> text.source("§7Not an Ice Machine").translation(MinecraftLocale.ZH_CN, "§7并不是制冰机"),
                    text -> text.source("§7Fluid Petrificator").translation(MinecraftLocale.ZH_CN, "§7流体硬化器")));
    public static final MachineDefinition[] FORGE_HAMMER = registerRecipeMachines("forge_hammer",
            GTRecipeTypes.FORGE_HAMMER_RECIPES, standardTooltip(
                    text -> text.source("§7Stop, Hammertime!").translation(MinecraftLocale.ZH_CN, "§7停，抡锤时间到！"),
                    text -> text.source("§7Plate Forger").translation(MinecraftLocale.ZH_CN, "§7锻板机"),
                    text -> text.source("§7Impact Modulator").translation(MinecraftLocale.ZH_CN, "§7冲击调制器")));
    public static final MachineDefinition[] FORMING_PRESS = registerRecipeMachines("forming_press",
            GTRecipeTypes.FORMING_PRESS_RECIPES, standardTooltip(
                    text -> text.source("§7Imprinting Images into things").translation(MinecraftLocale.ZH_CN,
                            "§7图像拓印者"),
                    text -> text.source("§7Object Layerer").translation(MinecraftLocale.ZH_CN, "§7对象层化机"),
                    text -> text.source("§7Surface Shifter").translation(MinecraftLocale.ZH_CN, "§7表面移相器")));
    public static final MachineDefinition[] LATHE = registerRecipeMachines("lathe", GTRecipeTypes.LATHE_RECIPES,
            tier -> switch (tier) {
                case LV, MV, HV, EV -> text -> text.source("§7Produces Rods more efficiently")
                        .translation(MinecraftLocale.ZH_CN, "§7以更高的效率生产杆");
                case IV -> text -> text.source("§7Turn-O-Matic L-5906").translation(MinecraftLocale.ZH_CN,
                        "§7全自动旋转车削机 L-5906");
                case LuV -> text -> text.source("§7Turn-O-Matic L-5907").translation(MinecraftLocale.ZH_CN,
                        "§7全自动旋转车削机 L-5907");
                case ZPM -> text -> text.source("§7Turn-O-Matic L-5908").translation(MinecraftLocale.ZH_CN,
                        "§7全自动旋转车削机 L-5908");
                case UV -> text -> text.source("§7Rotation Grinder").translation(MinecraftLocale.ZH_CN, "§7飞旋磨轮");
                default -> null;
            });
    public static final MachineDefinition[] SCANNER = registerRecipeMachines("scanner", GTRecipeTypes.SCANNER_RECIPES,
            standardTooltip(
                    text -> text.source("§7Scans Materials and other things")
                            .translation(MinecraftLocale.ZH_CN, "§7扫描材料及其它物质"),
                    text -> text.source("§7Anomaly Detector").translation(MinecraftLocale.ZH_CN, "§7异常探测仪"),
                    text -> text.source("§7Electron Microscope").translation(MinecraftLocale.ZH_CN, "§7电子显微镜")));
    public static final MachineDefinition[] MIXER = registerRecipeMachines("mixer", GTRecipeTypes.MIXER_RECIPES,
            hvCappedTankSizeFunction, false, standardTooltip(
                    text -> text.source("§7Will it Blend?").translation(MinecraftLocale.ZH_CN, "§7它能被搅碎吗？"),
                    text -> text.source("§7Matter Organizer").translation(MinecraftLocale.ZH_CN, "§7物质组织器"),
                    text -> text.source("§7Material Homogenizer").translation(MinecraftLocale.ZH_CN, "§7材料均质机")));
    public static final MachineDefinition[] ORE_WASHER = registerRecipeMachines("ore_washer",
            GTRecipeTypes.ORE_WASHER_RECIPES, tier -> switch (tier) {
                case LV, MV, HV, EV -> text -> text.source("§7Getting more Byproducts from your Ores")
                        .translation(MinecraftLocale.ZH_CN, "§7清洗矿石以获得更多副产物");
                case IV -> text -> text.source("§7Repurposed Laundry-Washer I-360")
                        .translation(MinecraftLocale.ZH_CN, "§7全新用途滚筒洗衣机 I-360");
                case LuV -> text -> text.source("§7Repurposed Laundry-Washer I-361")
                        .translation(MinecraftLocale.ZH_CN, "§7全新用途滚筒洗衣机 I-361");
                case ZPM -> text -> text.source("§7Repurposed Laundry-Washer I-362")
                        .translation(MinecraftLocale.ZH_CN, "§7全新用途滚筒洗衣机 I-362");
                case UV -> text -> text.source("§7Miniature Car Wash").translation(MinecraftLocale.ZH_CN, "§7微型洗车房");
                default -> null;
            });
    public static final MachineDefinition[] PACKER = registerRecipeMachines("packer", GTRecipeTypes.PACKER_RECIPES,
            standardTooltip(
                    text -> text.source("§7Puts things into and Grabs things out of Boxes")
                            .translation(MinecraftLocale.ZH_CN, "§7取出盒外，放入盒中"),
                    text -> text.source("§7Boxinator").translation(MinecraftLocale.ZH_CN, "§7装箱者"),
                    text -> text.source("§7Amazon Warehouse").translation(MinecraftLocale.ZH_CN, "§7亚马逊仓库")));
    public static final MachineDefinition[] POLARIZER = registerRecipeMachines("polarizer",
            GTRecipeTypes.POLARIZER_RECIPES, standardTooltip(
                    text -> text.source("§7Bipolarising your Magnets").translation(MinecraftLocale.ZH_CN, "§7将你的磁体极化"),
                    text -> text.source("§7Magnetism Inducer").translation(MinecraftLocale.ZH_CN, "§7磁性引入机"),
                    text -> text.source("§7Magnetic Field Rearranger").translation(MinecraftLocale.ZH_CN, "§7磁场重置设备")));
    public static final MachineDefinition[] LASER_ENGRAVER = registerRecipeMachines("laser_engraver",
            GTRecipeTypes.LASER_ENGRAVER_RECIPES, defaultTankSizeFunction, true, tier -> switch (tier) {
                case LV, MV, HV, EV -> text -> text.source("§7Don't look directly at the Laser")
                        .translation(MinecraftLocale.ZH_CN, "§7请勿直视激光");
                case IV -> text -> text.source("§7With the Power of 2.04 MW")
                        .translation(MinecraftLocale.ZH_CN, "§7功率高达两百零四万瓦");
                case LuV -> text -> text.source("§7With the Power of 8.16 MW")
                        .translation(MinecraftLocale.ZH_CN, "§7功率高达八百一十六万瓦");
                case ZPM -> text -> text.source("§7With the Power of 32.64 MW")
                        .translation(MinecraftLocale.ZH_CN, "§7功率高达三千两百六十四万瓦");
                case UV -> text -> text.source("§7Exact Photon Cannon").translation(MinecraftLocale.ZH_CN,
                        "§7高精度光子加农炮");
                default -> null;
            });
    public static final MachineDefinition[] SIFTER = registerRecipeMachines("sifter", GTRecipeTypes.SIFTER_RECIPES,
            standardTooltip(
                    text -> text.source("§7Stay calm and keep sifting").translation(MinecraftLocale.ZH_CN,
                            "§7保持冷静，继续筛选"),
                    text -> text.source("§7Sponsored by TFC").translation(MinecraftLocale.ZH_CN, "§7由群峦传说赞助提供"),
                    text -> text.source("§7Pulsation Filter").translation(MinecraftLocale.ZH_CN, "§7脉冲过滤器")));
    public static final MachineDefinition[] THERMAL_CENTRIFUGE = registerRecipeMachines("thermal_centrifuge",
            GTRecipeTypes.THERMAL_CENTRIFUGE_RECIPES, tier -> switch (tier) {
                case LV, MV, HV, EV -> text -> text.source("§7Separating Ores more precisely")
                        .translation(MinecraftLocale.ZH_CN, "§7更精确地分离矿石");
                case IV -> text -> text.source("§7Blaze Sweatshop T-6350").translation(MinecraftLocale.ZH_CN,
                        "§7烈焰血汗工厂 T-6350");
                case LuV -> text -> text.source("§7Blaze Sweatshop T-6351").translation(MinecraftLocale.ZH_CN,
                        "§7烈焰血汗工厂 T-6351");
                case ZPM -> text -> text.source("§7Blaze Sweatshop T-6352").translation(MinecraftLocale.ZH_CN,
                        "§7烈焰血汗工厂 T-6352");
                case UV -> text -> text.source("§7Fire Cyclone").translation(MinecraftLocale.ZH_CN, "§7腾焰旋风");
                default -> null;
            });
    public static final MachineDefinition[] WIREMILL = registerRecipeMachines("wiremill",
            GTRecipeTypes.WIREMILL_RECIPES, standardTooltip(
                    text -> text.source("§7Produces Wires more efficiently")
                            .translation(MinecraftLocale.ZH_CN, "§7更高效地生产导线"),
                    text -> text.source("§7Ingot Elongator").translation(MinecraftLocale.ZH_CN, "§7锭材拉伸机"),
                    text -> text.source("§7Wire Transfigurator").translation(MinecraftLocale.ZH_CN, "§7导线易形者")));
    public static final MachineDefinition[] CIRCUIT_ASSEMBLER = registerRecipeMachines("circuit_assembler",
            GTRecipeTypes.CIRCUIT_ASSEMBLER_RECIPES, hvCappedTankSizeFunction, true, standardTooltip(
                    text -> text.source("§7Pick-n-Place all over the place")
                            .translation(MinecraftLocale.ZH_CN, "§7一拿一放，东拣西装"),
                    text -> text.source("§7Electronics Manufacturer").translation(MinecraftLocale.ZH_CN, "§7电子厂"),
                    text -> text.source("§7Computation Factory").translation(MinecraftLocale.ZH_CN, "§7计算机工厂")));
    public static final MachineDefinition[] MACERATOR = registerTieredMachines("macerator",
            (holder, tier) -> new SimpleTieredMachine(holder, tier, defaultTankSizeFunction), (tier, builder) -> builder
                    .primaryTooltip(switch (tier) {
                        case LV, MV -> text -> text.source("§7Shredding your Ores without Byproducts")
                                .translation(MinecraftLocale.ZH_CN, "§7粉碎矿石，但没有副产物");
                        case HV, EV -> text -> text.source("§7Shredding your Ores with Byproducts")
                                .translation(MinecraftLocale.ZH_CN, "§7粉碎矿石并获得副产物");
                        case IV -> text -> text.source("§7Blend-O-Matic 9001").translation(MinecraftLocale.ZH_CN,
                                "§7全自动破壁机 9001");
                        case LuV -> text -> text.source("§7Blend-O-Matic 9002").translation(MinecraftLocale.ZH_CN,
                                "§7全自动破壁机 9002");
                        case ZPM -> text -> text.source("§7Blend-O-Matic 9003").translation(MinecraftLocale.ZH_CN,
                                "§7全自动破壁机 9003");
                        case UV -> text -> text.source("§7Shape Eliminator").translation(MinecraftLocale.ZH_CN,
                                "§7形态毁灭者");
                        default -> null;
                    })
                    .localization(text -> text
                            .source("%s Macerator %s".formatted(VLVH[tier], VLVT[tier]))
                            .translation(MinecraftLocale.ZH_CN, tieredChineseName(tier, "研磨机")))
                    .editableUI(SimpleTieredMachine.EDITABLE_UI_CREATOR.apply(GTPM.id("macerator"),
                            GTRecipeTypes.MACERATOR_RECIPES))
                    .rotationState(RotationState.NON_Y_AXIS)
                    .recipeType(GTRecipeTypes.MACERATOR_RECIPES)
                    .addOutputLimit(ItemRecipeCapability.CAP, switch (tier) {
                        case 1, 2 -> 1;
                        case 3 -> 3;
                        default -> 4;
                    })
                    .recipeModifier(GTRecipeModifiers.OC_NON_PERFECT)
                    .workableTieredHullModel(GTPM.id("block/machines/macerator"))
                    .tooltips(workableTiered(tier, GTValues.V[tier], GTValues.V[tier] * 64,
                            GTRecipeTypes.MACERATOR_RECIPES, defaultTankSizeFunction.applyAsInt(tier), true))
                    .register(),
            ELECTRIC_TIERS);
    public static final MachineDefinition[] GAS_COLLECTOR = registerRecipeMachines("gas_collector",
            GTRecipeTypes.GAS_COLLECTOR_RECIPES, largeTankSizeFunction, true, tier -> switch (tier) {
                case LV, MV, HV, EV -> text -> text.source("§7Collects Gas from the air depending on the dimension")
                        .translation(MinecraftLocale.ZH_CN, "§7依照维度从空气中收集种类各异的气体");
                case IV, LuV -> text -> text.source("§7Collects Gas from the atmosphere depending on the dimension")
                        .translation(MinecraftLocale.ZH_CN, "§7依照维度从大气层中收集种类各异的气体");
                case ZPM -> text -> text.source("§7Collects Gas from the atmosphere depending on the dimension")
                        .translation(MinecraftLocale.ZH_CN, "§7收集不同维度的大气中种类各异的气体");
                case UV, UHV, UIV, UXV -> text -> text
                        .source("§7Collects Gas from the solar system depending on the dimension")
                        .translation(MinecraftLocale.ZH_CN, "§7根据维度从太阳系中收集气体");
                case UEV -> text -> text.source("§7Collects Gas from the solar system depending on the dimension")
                        .translation(MinecraftLocale.ZH_CN, "§7依照维度从太阳系中收集种类各异的气体");
                case OpV -> text -> text.source("§7Collects Gas from the universe depending on the dimension")
                        .translation(MinecraftLocale.ZH_CN, "§7依照维度从宇宙中收集种类各异的气体");
                default -> null;
            });
    public static final MachineDefinition[] ROCK_CRUSHER = registerTieredMachines("rock_crusher",
            RockCrusherMachine::new, (tier, builder) -> builder
                    .primaryTooltip(switch (tier) {
                        case LV, MV, HV, EV -> text -> text.source("§7Place Water and Lava horizontally adjacent")
                                .translation(MinecraftLocale.ZH_CN, "§7在水平相邻处放置熔岩和水");
                        case IV -> text -> text.source("§7Cryogenic Magma Solidifier R-8200")
                                .translation(MinecraftLocale.ZH_CN, "§7岩浆冷却固化器 R-8200");
                        case LuV -> text -> text.source("§7Cryogenic Magma Solidifier R-9200")
                                .translation(MinecraftLocale.ZH_CN, "§7岩浆冷却固化器 R-9200");
                        case ZPM -> text -> text.source("§7Cryogenic Magma Solidifier R-10200")
                                .translation(MinecraftLocale.ZH_CN, "§7岩浆冷却固化器 R-10200");
                        case UV, UHV, UEV, UIV, UXV, OpV -> text -> text.source("§7Volcanic Formation Chamber")
                                .translation(MinecraftLocale.ZH_CN, "§7火山成型室");
                        default -> null;
                    })
                    .localization(text -> text
                            .source("%s Rock Crusher %s".formatted(VLVH[tier], VLVT[tier]))
                            .translation(MinecraftLocale.ZH_CN, tieredChineseName(tier, "碎岩机")))
                    .editableUI(SimpleTieredMachine.EDITABLE_UI_CREATOR.apply(GTPM.id("rock_crusher"),
                            GTRecipeTypes.ROCK_BREAKER_RECIPES))
                    .rotationState(RotationState.NON_Y_AXIS)
                    .recipeType(GTRecipeTypes.ROCK_BREAKER_RECIPES)
                    .recipeModifier(GTRecipeModifiers.OC_NON_PERFECT)
                    .workableTieredHullModel(GTPM.id("block/machines/rock_crusher"))
                    .tooltips(workableTiered(tier, GTValues.V[tier], GTValues.V[tier] * 64,
                            GTRecipeTypes.ROCK_BREAKER_RECIPES, defaultTankSizeFunction.applyAsInt(tier), true))
                    .tooltips(explosion())
                    .register(),
            ELECTRIC_TIERS);
    public static final MachineDefinition[] AIR_SCRUBBER = registerTieredMachines("air_scrubber",
            AirScrubberMachine::new, (tier, builder) -> builder
                    .localization(text -> text
                            .source("%s Air Scrubber %s".formatted(VLVH[tier], VLVT[tier]))
                            .translation(MinecraftLocale.ZH_CN, tieredChineseName(tier, "空气净化器")))
                    .editableUI(SimpleTieredMachine.EDITABLE_UI_CREATOR.apply(GTPM.id("air_scrubber"),
                            GTRecipeTypes.AIR_SCRUBBER_RECIPES))
                    .rotationState(RotationState.NON_Y_AXIS)
                    .recipeType(GTRecipeTypes.AIR_SCRUBBER_RECIPES)
                    .recipeModifier(GTRecipeModifiers.OC_NON_PERFECT)
                    .workableTieredHullModel(GTPM.id("block/machines/air_scrubber"))
                    .tooltips(workableTiered(tier, GTValues.V[tier], GTValues.V[tier] * 64,
                            GTRecipeTypes.AIR_SCRUBBER_RECIPES, defaultTankSizeFunction.applyAsInt(tier), true))
                    .tooltips(explosion())
                    .register(),
            LOW_TIERS);

    private static MachineDefinition[] registerRecipeMachines(String name, GTRecipeType recipeType,
                                                              IntFunction<@Nullable Consumer<LocalizationDeclaration.Builder>> tooltip) {
        return registerRecipeMachines(name, recipeType, defaultTankSizeFunction, false, tooltip);
    }

    private static MachineDefinition[] registerRecipeMachines(String name, GTRecipeType recipeType,
                                                              Int2IntFunction tankScaling, boolean pollution,
                                                              IntFunction<@Nullable Consumer<LocalizationDeclaration.Builder>> tooltip) {
        return registerSimpleMachines(REGISTRATE, name, recipeType, tankScaling, pollution, tooltip, ELECTRIC_TIERS);
    }

    private static IntFunction<@Nullable Consumer<LocalizationDeclaration.Builder>> standardTooltip(
                                                                                                    Consumer<LocalizationDeclaration.Builder> basic,
                                                                                                    Consumer<LocalizationDeclaration.Builder> elite,
                                                                                                    Consumer<LocalizationDeclaration.Builder> ultimate) {
        return tier -> switch (tier) {
            case LV, MV, HV, EV -> basic;
            case IV, LuV, ZPM -> elite;
            case UV -> ultimate;
            default -> null;
        };
    }

    //////////////////////////////////////
    // **** Simple Generator ****//
    //////////////////////////////////////
    public static final MachineDefinition[] COMBUSTION = registerSimpleGenerator("combustion",
            GTRecipeTypes.COMBUSTION_GENERATOR_FUELS, genericGeneratorTankSizeFunction, 0.1f, GTValues.LV, GTValues.MV,
            GTValues.HV);
    public static final MachineDefinition[] STEAM_TURBINE = registerSimpleGenerator("steam_turbine",
            GTRecipeTypes.STEAM_TURBINE_FUELS, steamGeneratorTankSizeFunction, 0.0f, GTValues.LV, GTValues.MV,
            GTValues.HV);
    public static final MachineDefinition[] GAS_TURBINE = registerSimpleGenerator("gas_turbine",
            GTRecipeTypes.GAS_TURBINE_FUELS, genericGeneratorTankSizeFunction, 0.1f, GTValues.LV, GTValues.MV,
            GTValues.HV);

    //////////////////////////////////////
    // ******** Electric ********//
    //////////////////////////////////////
    public static final MachineDefinition[] TRANSFORMER = registerTransformerMachines("", 1);
    public static final MachineDefinition[] HI_AMP_TRANSFORMER_2A = registerTransformerMachines("Hi-Amp (2x)", 2);
    public static final MachineDefinition[] HI_AMP_TRANSFORMER_4A = registerTransformerMachines("Hi-Amp (4x)", 4);
    public static final MachineDefinition[] POWER_TRANSFORMER = registerTransformerMachines("Power", 16);

    public static final MachineDefinition[] ENERGY_CONVERTER_1A = registerConverter(1);
    public static final MachineDefinition[] ENERGY_CONVERTER_4A = registerConverter(4);
    public static final MachineDefinition[] ENERGY_CONVERTER_8A = registerConverter(8);
    public static final MachineDefinition[] ENERGY_CONVERTER_16A = registerConverter(16);

    public static final MachineDefinition LONG_DIST_ITEM_ENDPOINT = REGISTRATE
            .machine("long_distance_item_pipeline_endpoint", LDItemEndpointMachine::new)
            .localization(text -> text
                    .source("Long Distance Item Pipeline Endpoint")
                    .translation(MinecraftLocale.ZH_CN, "长距离物品管道接口"))
            .rotationState(RotationState.ALL)
            .tier(LV)
            .modelProperty(IS_FORMED, false)
            .overlayTieredHullModel("long_distance_item_pipeline_endpoint")
            .tooltips(LangHandler.getMultiLang("gtpm.machine.endpoint.tooltip"))
            .tooltipBuilder((stack, tooltip) -> {
                if (ConfigHolder.INSTANCE.machines.ldItemPipeMinDistance > 0) {
                    tooltip.add(Component.translatable("gtpm.machine.endpoint.tooltip.min_length",
                            ConfigHolder.INSTANCE.machines.ldItemPipeMinDistance));
                }
            })
            .register();

    public static final MachineDefinition LONG_DIST_FLUID_ENDPOINT = REGISTRATE
            .machine("long_distance_fluid_pipeline_endpoint", LDFluidEndpointMachine::new)
            .localization(text -> text
                    .source("Long Distance Fluid Pipeline Endpoint")
                    .translation(MinecraftLocale.ZH_CN, "长距离流体管道接口"))
            .rotationState(RotationState.ALL)
            .tier(LV)
            .modelProperty(IS_FORMED, false)
            .overlayTieredHullModel("long_distance_fluid_pipeline_endpoint")
            .tooltips(Component.translatable("gtpm.machine.endpoint.tooltip.0"),
                    Component.translatable("gtpm.machine.endpoint.tooltip.1"),
                    Component.translatable("gtpm.machine.endpoint.tooltip.2"))
            .tooltipBuilder((stack, tooltip) -> {
                if (ConfigHolder.INSTANCE.machines.ldFluidPipeMinDistance > 0) {
                    tooltip.add(Component.translatable("gtpm.machine.endpoint.tooltip.min_length",
                            ConfigHolder.INSTANCE.machines.ldFluidPipeMinDistance));
                }
            })
            .register();

    public static final MachineDefinition[] BATTERY_BUFFER_4 = registerBatteryBuffer(4);

    public static final MachineDefinition[] BATTERY_BUFFER_8 = registerBatteryBuffer(8);

    public static final MachineDefinition[] BATTERY_BUFFER_16 = registerBatteryBuffer(16);

    public static final MachineDefinition[] CHARGER_4 = registerCharger(4);

    public static final MachineDefinition[] PUMP = registerTieredMachines("pump", PumpMachine::new,
            (tier, builder) -> builder
                    .rotationState(RotationState.ALL)
                    .tieredHullModel(GTPM.id("block/machine/template/pump_machine"))
                    .localization(text -> text
                            .source("%s Pump %s".formatted(VLVH[tier], VLVT[tier]))
                            .translation(MinecraftLocale.ZH_CN, tieredChineseName(tier, "泵")))
                    .tooltips(Component.translatable("gtpm.machine.pump.tooltip"),
                            Component.translatable("gtpm.universal.tooltip.voltage_in",
                                    FormattingUtil.formatNumbers(GTValues.V[tier]),
                                    GTValues.VNF[tier]),
                            Component.translatable("gtpm.universal.tooltip.energy_storage_capacity",
                                    FormattingUtil.formatNumbers(GTValues.V[tier] * 64)),
                            Component.translatable("gtpm.universal.tooltip.fluid_storage_capacity",
                                    FormattingUtil.formatNumbers(16 * FluidType.BUCKET_VOLUME * Math.max(1, tier))),
                            Component.translatable("gtpm.universal.tooltip.working_area",
                                    PumpMachine.getMaxPumpRadius(tier) * 2,
                                    PumpMachine.getMaxPumpRadius(tier) * 2))
                    .register(),
            LV, MV, HV, EV);

    public static final MachineDefinition[] FISHER = registerTieredMachines("fisher", FisherMachine::new,
            (tier, builder) -> builder
                    .rotationState(RotationState.ALL)
                    .model(createFisherModel())
                    .localization(text -> text
                            .source("%s Fisher %s".formatted(VLVH[tier], VLVT[tier]))
                            .translation(MinecraftLocale.ZH_CN, tieredChineseName(tier, "捕鱼机")))
                    .tooltips(Component.translatable("gtpm.machine.fisher.tooltip"),
                            Component.translatable("gtpm.machine.fisher.speed", FisherMachine.calcMaxProgress(tier)),
                            Component.translatable("gtpm.machine.fisher.requirement", FisherMachine.WATER_CHECK_SIZE,
                                    FisherMachine.WATER_CHECK_SIZE),
                            Component.translatable("gtpm.universal.tooltip.voltage_in",
                                    FormattingUtil.formatNumbers(GTValues.V[tier]),
                                    GTValues.VNF[tier]),
                            Component.translatable("gtpm.universal.tooltip.energy_storage_capacity",
                                    FormattingUtil.formatNumbers(GTValues.V[tier] * 64)))
                    .register(),
            LV, MV, HV, EV, IV, LuV);

    public static final MachineDefinition[] BLOCK_BREAKER = registerTieredMachines("block_breaker",
            BlockBreakerMachine::new,
            (tier, builder) -> builder
                    .rotationState(RotationState.NON_Y_AXIS)
                    .workableTieredHullModel(GTPM.id("block/machines/block_breaker"))
                    .localization(text -> text
                            .source("%s Block Breaker %s".formatted(VLVH[tier], VLVT[tier]))
                            .translation(MinecraftLocale.ZH_CN,
                                    tieredChineseName(tier, tier == HV ? "方块破坏机" : "方块破坏器")))
                    .tooltips(Component.translatable("gtpm.machine.block_breaker.tooltip"),
                            Component.translatable("gtpm.machine.block_breaker.speed_bonus",
                                    (int) (BlockBreakerMachine.getEfficiencyMultiplier(tier) * 100)),
                            Component.translatable("gtpm.universal.tooltip.voltage_in",
                                    FormattingUtil.formatNumbers(GTValues.V[tier]),
                                    GTValues.VNF[tier]),
                            Component.translatable("gtpm.universal.tooltip.energy_storage_capacity",
                                    FormattingUtil.formatNumbers(GTValues.V[tier] * 64)))
                    .register(),
            LV, MV, HV, EV);

    public static final MachineDefinition[] MINER = registerTieredMachines("miner",
            (holder, tier) -> new MinerMachine(holder, tier, ConfigHolder.INSTANCE.machines.minerSpeed / (tier * 2),
                    tier * 8, tier),
            (tier, builder) -> builder
                    .rotationState(RotationState.NON_Y_AXIS)
                    .localization(text -> text
                            .source("%s Miner %s".formatted(VLVH[tier], VLVT[tier]))
                            .translation(MinecraftLocale.ZH_CN, tieredChineseName(tier, "采矿机")))
                    .recipeType(DUMMY_RECIPES)
                    .workableTieredHullModel(GTPM.id("block/machines/miner"))
                    .tooltipBuilder((stack, tooltip) -> {
                        int maxArea = IMiner.getWorkingArea(tier * 8);
                        long energyPerTick = GTValues.V[tier - 1];
                        int tickSpeed = ConfigHolder.INSTANCE.machines.minerSpeed / (tier * 2);
                        tooltip.add(Component.translatable("gtpm.machine.miner.tooltip", maxArea, maxArea));
                        tooltip.add(Component.translatable("gtpm.universal.tooltip.uses_per_tick", energyPerTick)
                                .append(Component.literal(", ").withStyle(ChatFormatting.GRAY))
                                .append(Component.translatable("gtpm.machine.miner.per_block", tickSpeed / 20)));
                        tooltip.add(Component.translatable("gtpm.universal.tooltip.voltage_in",
                                FormattingUtil.formatNumbers(GTValues.V[tier]),
                                GTValues.VNF[tier]));
                        tooltip.add(Component.translatable("gtpm.universal.tooltip.energy_storage_capacity",
                                FormattingUtil.formatNumbers(GTValues.V[tier] * 64L)));

                        tooltip.add(
                                Component.translatable("gtpm.universal.tooltip.working_area_max", maxArea, maxArea));
                    })
                    .register(),
            LV, MV, HV);

    public static final MachineDefinition[] WORLD_ACCELERATOR = registerTieredMachines("world_accelerator",
            WorldAcceleratorMachine::new,
            (tier, builder) -> builder
                    .rotationState(RotationState.NONE)
                    .localization(text -> text
                            .source("%s World Accelerator %s".formatted(VLVH[tier], VLVT[tier]))
                            .translation(MinecraftLocale.ZH_CN, tieredChineseName(tier, "世界加速器")))
                    .recipeType(DUMMY_RECIPES)
                    .modelProperty(GTMachineModelProperties.IS_RANDOM_TICK_MODE, true)
                    .modelProperty(GTMachineModelProperties.IS_WORKING_ENABLED, true)
                    .modelProperty(GTMachineModelProperties.IS_ACTIVE, false)
                    .model(createWorldAcceleratorModel(GTPM.id("block/machines/world_accelerator_te"),
                            GTPM.id("block/machines/world_accelerator")))
                    .tooltipBuilder((stack, tooltip) -> {
                        int randTickWorkingArea = 3 + (tier - 1) * 2;
                        tooltip.add(Component.translatable("gtpm.machine.world_accelerator.description"));

                        tooltip.add(Component.translatable("gtpm.universal.tooltip.voltage_in",
                                FormattingUtil.formatNumbers(GTValues.V[tier]),
                                GTValues.VNF[tier]));
                        tooltip.add(Component.translatable("gtpm.universal.tooltip.energy_storage_capacity",
                                FormattingUtil.formatNumbers(GTValues.V[tier] * 64L)));

                        tooltip.add(Component.translatable("gtpm.machine.world_accelerator.working_area"));
                        tooltip.add(Component.translatable("gtpm.machine.world_accelerator.working_area_tile"));
                        tooltip.add(Component.translatable("gtpm.machine.world_accelerator.working_area_random",
                                randTickWorkingArea, randTickWorkingArea));
                    })
                    .register(),
            LV, MV, HV, EV, IV, LuV, ZPM, UV);

    public static final MachineDefinition[] ITEM_COLLECTOR = registerTieredMachines("item_collector",
            ItemCollectorMachine::new,
            (tier, builder) -> builder
                    .rotationState(RotationState.NONE)
                    .localization(text -> text
                            .source("%s Item Collector %s".formatted(VLVH[tier], VLVT[tier]))
                            .translation(MinecraftLocale.ZH_CN, tieredChineseName(tier, "物品收集器")))
                    .recipeType(DUMMY_RECIPES)
                    .modelProperty(GTMachineModelProperties.IS_ACTIVE, false)
                    .modelProperty(GTMachineModelProperties.IS_WORKING_ENABLED, false)
                    .model(createItemCollectorModel(GTPM.id("block/machines/item_collector")))
                    .tooltips(
                            Component.translatable("gtpm.machine.item_collector.tooltip"),
                            Component.translatable("gtpm.machine.item_collector.gui.collect_range",
                                    IntMath.pow(2, tier + 2), IntMath.pow(2, tier + 2)),
                            Component.translatable("gtpm.universal.tooltip.voltage_in",
                                    FormattingUtil.formatNumbers(GTValues.V[tier]),
                                    GTValues.VNF[tier]),
                            Component.translatable("gtpm.universal.tooltip.energy_storage_capacity",
                                    FormattingUtil.formatNumbers(GTValues.V[tier] * 64L)))
                    .register(),
            LV, MV, HV, EV);

    //////////////////////////////////////
    // ********* Storage *********//
    //////////////////////////////////////

    public static final MachineDefinition[] BUFFER = registerTieredMachines("buffer",
            BufferMachine::new,
            (tier, builder) -> builder
                    .localization(text -> text
                            .source("%s Buffer %s".formatted(VLVH[tier], VLVT[tier]))
                            .translation(MinecraftLocale.ZH_CN, tieredChineseName(tier, "缓存器")))
                    .rotationState(RotationState.NONE)
                    .model(createSingleOverlayTieredHullMachineModel(GTPM.id("block/overlay/machine/overlay_buffer"),
                            GTPM.id("block/overlay/machine/overlay_buffer_emissive")))
                    .tooltips(
                            Component.translatable("gtpm.machine.buffer.tooltip"),
                            Component.translatable(
                                    "gtpm.universal.tooltip.item_storage_capacity",
                                    BufferMachine.getInventorySize(tier)),
                            Component.translatable(
                                    "gtpm.universal.tooltip.fluid_storage_capacity_mult",
                                    BufferMachine.getTankSize(tier), BufferMachine.TANK_SIZE))
                    .register(),
            LV, MV, HV);

    public static final BiConsumer<ItemStack, List<Component>> CREATIVE_TOOLTIPS = (stack, list) -> list.add(
            Component.translatable("gtpm.creative_tooltip.1")
                    .append(Component.translatable("gtpm.creative_tooltip.2")
                            .withStyle(TooltipHelper.RAINBOW_HSL_SLOW))
                    .append(Component.translatable("gtpm.creative_tooltip.3")));

    public static final MachineDefinition CREATIVE_ENERGY = REGISTRATE
            .machine("creative_energy", CreativeEnergyContainerMachine::new)
            .localization(text -> text
                    .source("Creative Energy")
                    .translation(MinecraftLocale.ZH_CN, "创造模式能量单元"))
            .rotationState(RotationState.NONE)
            .model(createSingleOverlayTieredHullMachineModel(GTModels.BLANK_TEXTURE,
                    GTPM.id("block/overlay/machine/overlay_energy_emitter")))
            .tooltipBuilder(CREATIVE_TOOLTIPS)
            .tier(MAX)
            .register();

    public static final MachineDefinition CREATIVE_COMPUTATION_PROVIDER = REGISTRATE
            .machine("creative_computation_provider", CreativeComputationProviderMachine::new)
            .localization(text -> text
                    .source("Creative Computation Provider")
                    .translation(MinecraftLocale.ZH_CN, "创造模式计算机"))
            .rotationState(RotationState.NONE)
            .model(createSingleOverlayTieredHullMachineModel(
                    GTPM.id("block/overlay/machine/overlay_data_hatch_optical"),
                    GTPM.id("block/overlay/machine/overlay_data_hatch_optical_emissive")))
            .tooltipBuilder(CREATIVE_TOOLTIPS)
            .tier(MAX)
            .register();

    public static final MachineDefinition CREATIVE_FLUID = REGISTRATE
            .machine("creative_tank", CreativeTankMachine::new)
            .localization(text -> text
                    .source("Creative Tank")
                    .translation(MinecraftLocale.ZH_CN, "创造模式储罐"))
            .rotationState(RotationState.ALL)
            .allowExtendedFacing(true)
            .tooltipBuilder((stack, list) -> {
                CREATIVE_TOOLTIPS.accept(stack, list);
                var storage = stack.get(GTDataComponents.LARGE_FLUID_CONTENT);
                if (storage != null) {
                    FluidStack f = storage.stored();
                    var creativeInfo = stack.get(GTDataComponents.CREATIVE_MACHINE_INFO);
                    int perCycle = creativeInfo != null ? creativeInfo.outputPerCycle() : 1;
                    list.add(1, Component.translatable("gtpm.universal.tooltip.fluid_stored", f.getHoverName(),
                            FormattingUtil.formatNumbers(perCycle)));
                }
            })
            .model(createBasicMachineModel(GTPM.id("block/machine/template/quantum/creative_container"))
                    .andThen(b -> b.addDynamicRenderer(DynamicRenderHelper::createQuantumTankRender)))
            .hasBER(true)
            .register();

    public static final MachineDefinition CREATIVE_ITEM = REGISTRATE
            .machine("creative_chest", CreativeChestMachine::new)
            .localization(text -> text
                    .source("Creative Chest")
                    .translation(MinecraftLocale.ZH_CN, "创造模式箱子"))
            .rotationState(RotationState.ALL)
            .allowExtendedFacing(true)
            .tooltipBuilder((stack, list) -> {
                CREATIVE_TOOLTIPS.accept(stack, list);
                var storage = stack.get(GTDataComponents.LARGE_ITEM_CONTENT);
                if (storage != null) {
                    ItemStack i = storage.stored();
                    var creativeInfo = stack.get(GTDataComponents.CREATIVE_MACHINE_INFO);
                    int perCycle = creativeInfo != null ? creativeInfo.outputPerCycle() : 1;
                    list.add(1, Component.translatable("gtpm.universal.tooltip.item_stored", i.getHoverName(),
                            FormattingUtil.formatNumbers(perCycle)));
                }
            })
            .model(createBasicMachineModel(GTPM.id("block/machine/template/quantum/creative_container"))
                    .andThen(b -> b.addDynamicRenderer(DynamicRenderHelper::createQuantumChestRender)))
            .hasBER(true)
            .register();

    public static final MachineDefinition[] SUPER_CHEST = registerQuantumChests("super_chest", LOW_TIERS);
    public static final MachineDefinition[] QUANTUM_CHEST = registerQuantumChests("quantum_chest", HIGH_TIERS);

    public static final MachineDefinition[] SUPER_TANK = registerQuantumTanks("super_tank", LOW_TIERS);
    public static final MachineDefinition[] QUANTUM_TANK = registerQuantumTanks("quantum_tank", HIGH_TIERS);

    public static MachineDefinition WOODEN_CRATE = crate(REGISTRATE, GTMaterials.Wood, 27)
            .localization(text -> text.source("Wooden Crate").translation(MinecraftLocale.ZH_CN, "木板条箱"))
            .register();
    public static MachineDefinition BRONZE_CRATE = crate(REGISTRATE, GTMaterials.Bronze, 54)
            .localization(text -> text.source("Bronze Crate").translation(MinecraftLocale.ZH_CN, "青铜板条箱"))
            .register();
    public static MachineDefinition STEEL_CRATE = crate(REGISTRATE, GTMaterials.Steel, 72)
            .localization(text -> text.source("Steel Crate").translation(MinecraftLocale.ZH_CN, "钢板条箱"))
            .register();
    public static MachineDefinition ALUMINIUM_CRATE = crate(REGISTRATE, GTMaterials.Aluminium, 90)
            .localization(text -> text.source("Aluminium Crate").translation(MinecraftLocale.ZH_CN, "铝板条箱"))
            .register();
    public static MachineDefinition STAINLESS_STEEL_CRATE = crate(REGISTRATE, GTMaterials.StainlessSteel, 108)
            .localization(text -> text.source("Stainless Steel Crate").translation(MinecraftLocale.ZH_CN, "不锈钢板条箱"))
            .register();
    public static MachineDefinition TITANIUM_CRATE = crate(REGISTRATE, GTMaterials.Titanium, 126)
            .localization(text -> text.source("Titanium Crate").translation(MinecraftLocale.ZH_CN, "钛板条箱"))
            .register();
    public static MachineDefinition TUNGSTENSTEEL_CRATE = crate(REGISTRATE, GTMaterials.TungstenSteel, 144)
            .localization(text -> text.source("Tungstensteel Crate").translation(MinecraftLocale.ZH_CN, "钨钢板条箱"))
            .register();

    public static MachineDefinition WOODEN_DRUM = registerDrum(GTMaterials.Wood,
            builder -> builder
                    .localization(text -> text.source("Wooden Barrel").translation(MinecraftLocale.ZH_CN, "木桶")),
            16 * FluidType.BUCKET_VOLUME);
    public static MachineDefinition BRONZE_DRUM = registerDrum(GTMaterials.Bronze,
            builder -> builder
                    .localization(text -> text.source("Bronze Drum").translation(MinecraftLocale.ZH_CN, "青铜桶")),
            32 * FluidType.BUCKET_VOLUME);
    public static MachineDefinition STEEL_DRUM = registerDrum(GTMaterials.Steel,
            builder -> builder.localization(text -> text.source("Steel Drum").translation(MinecraftLocale.ZH_CN, "钢桶")),
            64 * FluidType.BUCKET_VOLUME);
    public static MachineDefinition ALUMINIUM_DRUM = registerDrum(GTMaterials.Aluminium,
            builder -> builder
                    .localization(text -> text.source("Aluminium Drum").translation(MinecraftLocale.ZH_CN, "铝桶")),
            128 * FluidType.BUCKET_VOLUME);
    public static MachineDefinition STAINLESS_STEEL_DRUM = registerDrum(GTMaterials.StainlessSteel,
            builder -> builder.localization(text -> text.source("Stainless Steel Drum")
                    .translation(MinecraftLocale.ZH_CN, "不锈钢桶")),
            256 * FluidType.BUCKET_VOLUME);
    public static MachineDefinition GOLD_DRUM = registerDrum(GTMaterials.Gold,
            builder -> builder.localization(text -> text.source("Gold Drum").translation(MinecraftLocale.ZH_CN, "金桶")),
            32 * FluidType.BUCKET_VOLUME);
    public static MachineDefinition TITANIUM_DRUM = registerDrum(GTMaterials.Titanium,
            builder -> builder
                    .localization(text -> text.source("Titanium Drum").translation(MinecraftLocale.ZH_CN, "钛桶")),
            512 * FluidType.BUCKET_VOLUME);
    public static MachineDefinition TUNGSTENSTEEL_DRUM = registerDrum(GTMaterials.TungstenSteel,
            builder -> builder.localization(text -> text.source("Tungstensteel Drum")
                    .translation(MinecraftLocale.ZH_CN, "钨钢桶")),
            1024 * FluidType.BUCKET_VOLUME);

    //////////////////////////////////////
    // ********** Part **********//
    //////////////////////////////////////
    public static final MachineDefinition[] ITEM_IMPORT_BUS = registerTieredMachines("input_bus",
            (holder, tier) -> new ItemBusPartMachine(holder, tier, IN),
            (tier, builder) -> builder
                    .localization(text -> text
                            .source(VNF[tier] + " Input Bus")
                            .translation(MinecraftLocale.ZH_CN, VNF[tier] + "§r输入总线"))
                    .rotationState(RotationState.ALL)
                    .abilities(PartAbility.IMPORT_ITEMS)
                    .modelProperty(IS_FORMED, false)
                    .colorOverlayTieredHullModel(OVERLAY_ITEM_HATCH_INPUT, "overlay_pipe", "overlay_pipe_in_emissive")
                    .tooltips(MachineTooltips.itemInput(),
                            Component.translatable("gtpm.universal.tooltip.item_storage_capacity",
                                    (1 + Math.min(9, tier)) * (1 + Math.min(9, tier))))
                    .allowCoverOnFront(true)
                    .register(),
            ALL_TIERS);

    public static final MachineDefinition[] ITEM_EXPORT_BUS = registerTieredMachines("output_bus",
            (holder, tier) -> new ItemBusPartMachine(holder, tier, OUT),
            (tier, builder) -> builder
                    .localization(text -> text
                            .source(VNF[tier] + " Output Bus")
                            .translation(MinecraftLocale.ZH_CN, VNF[tier] + "§r输出总线"))
                    .rotationState(RotationState.ALL)
                    .abilities(PartAbility.EXPORT_ITEMS)
                    .modelProperty(IS_FORMED, false)
                    .colorOverlayTieredHullModel(OVERLAY_ITEM_HATCH_OUTPUT, "overlay_pipe", "overlay_pipe_out_emissive")
                    .tooltips(MachineTooltips.itemOutput(),
                            Component.translatable("gtpm.universal.tooltip.item_storage_capacity",
                                    (1 + Math.min(9, tier)) * (1 + Math.min(9, tier))))
                    .allowCoverOnFront(true)
                    .register(),
            ALL_TIERS);

    public final static MachineDefinition[] FLUID_IMPORT_HATCH = registerFluidHatches(
            "input_hatch", "Input Hatch", "fluid_hatch.import",
            IN, FluidHatchPartMachine.INITIAL_TANK_CAPACITY_1X, 1,
            ALL_TIERS, PartAbility.IMPORT_FLUIDS,
            PartAbility.IMPORT_FLUIDS_1X);

    public final static MachineDefinition[] FLUID_IMPORT_HATCH_4X = registerFluidHatches(
            "input_hatch_4x", "Quadruple Input Hatch", "fluid_hatch.import",
            IN, FluidHatchPartMachine.INITIAL_TANK_CAPACITY_4X, 4,
            MULTI_HATCH_TIERS, PartAbility.IMPORT_FLUIDS,
            PartAbility.IMPORT_FLUIDS_4X);

    public final static MachineDefinition[] FLUID_IMPORT_HATCH_9X = registerFluidHatches(
            "input_hatch_9x", "Nonuple Input Hatch", "fluid_hatch.import",
            IN, FluidHatchPartMachine.INITIAL_TANK_CAPACITY_9X, 9,
            MULTI_HATCH_TIERS, PartAbility.IMPORT_FLUIDS,
            PartAbility.IMPORT_FLUIDS_9X);

    public final static MachineDefinition[] FLUID_EXPORT_HATCH = registerFluidHatches(
            "output_hatch", "Output Hatch", "fluid_hatch.export",
            OUT, FluidHatchPartMachine.INITIAL_TANK_CAPACITY_1X, 1,
            ALL_TIERS, PartAbility.EXPORT_FLUIDS,
            PartAbility.EXPORT_FLUIDS_1X);

    public final static MachineDefinition[] FLUID_EXPORT_HATCH_4X = registerFluidHatches(
            "output_hatch_4x", "Quadruple Output Hatch", "fluid_hatch.export",
            OUT, FluidHatchPartMachine.INITIAL_TANK_CAPACITY_4X, 4,
            MULTI_HATCH_TIERS, PartAbility.EXPORT_FLUIDS,
            PartAbility.EXPORT_FLUIDS_4X);

    public final static MachineDefinition[] FLUID_EXPORT_HATCH_9X = registerFluidHatches(
            "output_hatch_9x", "Nonuple Output Hatch", "fluid_hatch.export",
            OUT, FluidHatchPartMachine.INITIAL_TANK_CAPACITY_9X, 9,
            MULTI_HATCH_TIERS, PartAbility.EXPORT_FLUIDS,
            PartAbility.EXPORT_FLUIDS_9X);

    public static final MachineDefinition[] ENERGY_INPUT_HATCH = registerTieredMachines("energy_input_hatch",
            (holder, tier) -> new EnergyHatchPartMachine(holder, tier, IN, 2),
            (tier, builder) -> builder
                    .localization(text -> text
                            .source(VNF[tier] + " Energy Hatch")
                            .translation(MinecraftLocale.ZH_CN, VNF[tier] + "§r能源仓"))
                    .rotationState(RotationState.ALL)
                    .abilities(PartAbility.INPUT_ENERGY)
                    .modelProperty(IS_FORMED, false)
                    .tooltips(Component.translatable("gtpm.universal.tooltip.voltage_in",
                            FormattingUtil.formatNumbers(V[tier]), VNF[tier]),
                            Component.translatable("gtpm.universal.tooltip.amperage_in", 2),
                            Component.translatable("gtpm.universal.tooltip.energy_storage_capacity",
                                    FormattingUtil
                                            .formatNumbers(EnergyHatchPartMachine.getHatchEnergyCapacity(tier, 2))),
                            Component.translatable("gtpm.machine.energy_hatch.input.tooltip"))
                    .overlayTieredHullModel("energy_input_hatch")
                    .register(),
            ALL_TIERS);

    public static final MachineDefinition[] ENERGY_OUTPUT_HATCH = registerTieredMachines("energy_output_hatch",
            (holder, tier) -> new EnergyHatchPartMachine(holder, tier, OUT, 2),
            (tier, builder) -> builder
                    .localization(text -> text
                            .source(VNF[tier] + " Dynamo Hatch")
                            .translation(MinecraftLocale.ZH_CN, VNF[tier] + "§r动力仓"))
                    .rotationState(RotationState.ALL)
                    .abilities(PartAbility.OUTPUT_ENERGY)
                    .modelProperty(IS_FORMED, false)
                    .tooltips(Component.translatable("gtpm.universal.tooltip.voltage_out",
                            FormattingUtil.formatNumbers(V[tier]), VNF[tier]),
                            Component.translatable("gtpm.universal.tooltip.amperage_out", 2),
                            Component.translatable("gtpm.universal.tooltip.energy_storage_capacity",
                                    FormattingUtil
                                            .formatNumbers(EnergyHatchPartMachine.getHatchEnergyCapacity(tier, 2))),
                            Component.translatable("gtpm.machine.energy_hatch.output.tooltip"))
                    .overlayTieredHullModel("energy_output_hatch")
                    .register(),
            ALL_TIERS);

    public static final MachineDefinition[] ENERGY_INPUT_HATCH_4A = registerTieredMachines("energy_input_hatch_4a",
            (holder, tier) -> new EnergyHatchPartMachine(holder, tier, IN, 4),
            (tier, builder) -> builder
                    .localization(text -> text
                            .source(VNF[tier] + " 4A Energy Hatch")
                            .translation(MinecraftLocale.ZH_CN, "4安" + VNF[tier] + "§r能源仓" + (tier == EV ? " " : "")))
                    .rotationState(RotationState.ALL)
                    .abilities(PartAbility.INPUT_ENERGY)
                    .modelProperty(IS_FORMED, false)
                    .tooltips(Component.translatable("gtpm.universal.tooltip.voltage_in",
                            FormattingUtil.formatNumbers(V[tier]), VNF[tier]),
                            Component.translatable("gtpm.universal.tooltip.amperage_in", 4),
                            Component.translatable("gtpm.universal.tooltip.energy_storage_capacity",
                                    FormattingUtil
                                            .formatNumbers(EnergyHatchPartMachine.getHatchEnergyCapacity(tier, 4))),
                            Component.translatable("gtpm.machine.energy_hatch.input_hi_amp.tooltip"))
                    .overlayTieredHullModel("energy_input_hatch_4a")
                    .register(),
            GTValues.tiersBetween(EV, GTCEuAPI.isHighTier() ? MAX : UHV));

    public static final MachineDefinition[] ENERGY_OUTPUT_HATCH_4A = registerTieredMachines("energy_output_hatch_4a",
            (holder, tier) -> new EnergyHatchPartMachine(holder, tier, OUT, 4),
            (tier, builder) -> builder
                    .localization(text -> text
                            .source(VNF[tier] + " 4A Dynamo Hatch")
                            .translation(MinecraftLocale.ZH_CN, "4安" + VNF[tier] + "§r动力仓"))
                    .rotationState(RotationState.ALL)
                    .abilities(PartAbility.OUTPUT_ENERGY)
                    .modelProperty(IS_FORMED, false)
                    .tooltips(Component.translatable("gtpm.universal.tooltip.voltage_out",
                            FormattingUtil.formatNumbers(V[tier]), VNF[tier]),
                            Component.translatable("gtpm.universal.tooltip.amperage_out", 4),
                            Component.translatable("gtpm.universal.tooltip.energy_storage_capacity",
                                    FormattingUtil
                                            .formatNumbers(EnergyHatchPartMachine.getHatchEnergyCapacity(tier, 4))),
                            Component.translatable("gtpm.machine.energy_hatch.output_hi_amp.tooltip"))
                    .overlayTieredHullModel("energy_output_hatch_4a")
                    .register(),
            GTValues.tiersBetween(EV, GTCEuAPI.isHighTier() ? MAX : UHV));

    public static final MachineDefinition[] ENERGY_INPUT_HATCH_16A = registerTieredMachines("energy_input_hatch_16a",
            (holder, tier) -> new EnergyHatchPartMachine(holder, tier, IN, 16),
            (tier, builder) -> builder
                    .localization(text -> text
                            .source(VNF[tier] + " 16A Energy Hatch")
                            .translation(MinecraftLocale.ZH_CN, "16安" + VNF[tier] + "§r能源仓"))
                    .rotationState(RotationState.ALL)
                    .abilities(PartAbility.INPUT_ENERGY)
                    .modelProperty(IS_FORMED, false)
                    .tooltips(Component.translatable("gtpm.universal.tooltip.voltage_in",
                            FormattingUtil.formatNumbers(V[tier]), VNF[tier]),
                            Component.translatable("gtpm.universal.tooltip.amperage_in", 16),
                            Component.translatable("gtpm.universal.tooltip.energy_storage_capacity",
                                    FormattingUtil
                                            .formatNumbers(EnergyHatchPartMachine.getHatchEnergyCapacity(tier, 16))),
                            Component.translatable("gtpm.machine.energy_hatch.input_hi_amp.tooltip"))
                    .overlayTieredHullModel("energy_input_hatch_16a")
                    .register(),
            GTValues.tiersBetween(EV, GTCEuAPI.isHighTier() ? MAX : UHV));

    public static final MachineDefinition[] ENERGY_OUTPUT_HATCH_16A = registerTieredMachines("energy_output_hatch_16a",
            (holder, tier) -> new EnergyHatchPartMachine(holder, tier, OUT, 16),
            (tier, builder) -> builder
                    .localization(text -> text
                            .source(VNF[tier] + " 16A Dynamo Hatch")
                            .translation(MinecraftLocale.ZH_CN, "16安" + VNF[tier] + "§r动力仓"))
                    .rotationState(RotationState.ALL)
                    .abilities(PartAbility.OUTPUT_ENERGY)
                    .modelProperty(IS_FORMED, false)
                    .tooltips(Component.translatable("gtpm.universal.tooltip.voltage_out",
                            FormattingUtil.formatNumbers(V[tier]), VNF[tier]),
                            Component.translatable("gtpm.universal.tooltip.amperage_out", 16),
                            Component.translatable("gtpm.universal.tooltip.energy_storage_capacity",
                                    FormattingUtil
                                            .formatNumbers(EnergyHatchPartMachine.getHatchEnergyCapacity(tier, 16))),
                            Component.translatable("gtpm.machine.energy_hatch.output_hi_amp.tooltip"))
                    .overlayTieredHullModel("energy_output_hatch_16a")
                    .register(),
            GTValues.tiersBetween(EV, GTCEuAPI.isHighTier() ? MAX : UHV));

    public static final MachineDefinition[] SUBSTATION_ENERGY_INPUT_HATCH = registerTieredMachines(
            "substation_input_hatch_64a",
            (holder, tier) -> new EnergyHatchPartMachine(holder, tier, IN, 64),
            (tier, builder) -> builder
                    .localization(text -> text
                            .source(VNF[tier] + " 64A Substation Energy Hatch")
                            .translation(MinecraftLocale.ZH_CN, "64安" + VNF[tier] + "§r变电能源仓"))
                    .rotationState(RotationState.ALL)
                    .abilities(PartAbility.SUBSTATION_INPUT_ENERGY)
                    .modelProperty(IS_FORMED, false)
                    .tooltips(Component.translatable("gtpm.universal.tooltip.voltage_in",
                            FormattingUtil.formatNumbers(V[tier]), VNF[tier]),
                            Component.translatable("gtpm.universal.tooltip.amperage_in", 64),
                            Component.translatable("gtpm.universal.tooltip.energy_storage_capacity",
                                    FormattingUtil
                                            .formatNumbers(EnergyHatchPartMachine.getHatchEnergyCapacity(tier, 64))),
                            Component.translatable("gtpm.machine.substation_hatch.input.tooltip"))
                    .overlayTieredHullModel("energy_input_hatch_64a")
                    .register(),
            GTValues.tiersBetween(EV, GTCEuAPI.isHighTier() ? MAX : UHV));

    public static final MachineDefinition[] SUBSTATION_ENERGY_OUTPUT_HATCH = registerTieredMachines(
            "substation_output_hatch_64a",
            (holder, tier) -> new EnergyHatchPartMachine(holder, tier, OUT, 64),
            (tier, builder) -> builder
                    .localization(text -> text
                            .source(VNF[tier] + " 64A Substation Dynamo Hatch")
                            .translation(MinecraftLocale.ZH_CN, "64安" + VNF[tier] + "§r变电动力仓"))
                    .rotationState(RotationState.ALL)
                    .abilities(PartAbility.SUBSTATION_OUTPUT_ENERGY)
                    .modelProperty(IS_FORMED, false)
                    .tooltips(Component.translatable("gtpm.universal.tooltip.voltage_out",
                            FormattingUtil.formatNumbers(V[tier]), VNF[tier]),
                            Component.translatable("gtpm.universal.tooltip.amperage_out", 64),
                            Component.translatable("gtpm.universal.tooltip.energy_storage_capacity",
                                    FormattingUtil
                                            .formatNumbers(EnergyHatchPartMachine.getHatchEnergyCapacity(tier, 64))),
                            Component.translatable("gtpm.machine.substation_hatch.output.tooltip"))
                    .overlayTieredHullModel("energy_output_hatch_64a")
                    .register(),
            GTValues.tiersBetween(EV, GTCEuAPI.isHighTier() ? MAX : UHV));

    public static final MachineDefinition[] MUFFLER_HATCH = registerTieredMachines("muffler_hatch",
            MufflerPartMachine::new,
            (tier, builder) -> builder
                    .localization(text -> text
                            .source("Muffler Hatch " + VNF[tier])
                            .translation(MinecraftLocale.ZH_CN, VNF[tier] + "§r消声仓"))
                    .rotationState(RotationState.ALL)
                    .abilities(PartAbility.MUFFLER)
                    .modelProperty(IS_FORMED, false)
                    .overlayTieredHullModel("muffler_hatch")
                    .tooltips(LangHandler.getFromMultiLang("gtpm.machine.muffler_hatch.tooltip", 0),
                            Component.translatable("gtpm.muffler.recovery_tooltip", Math.max(1, tier * 10)),
                            Component.translatable("gtpm.part_sharing.enabled"),
                            LangHandler.getFromMultiLang("gtpm.machine.muffler_hatch.tooltip", 1)
                                    .withStyle(ChatFormatting.DARK_RED))
                    .register(),
            ELECTRIC_TIERS);

    public static final MachineDefinition STEAM_IMPORT_BUS = REGISTRATE
            .machine("steam_input_bus", holder -> new SteamItemBusPartMachine(holder, IN))
            .rotationState(RotationState.ALL)
            .abilities(PartAbility.STEAM_IMPORT_ITEMS)
            .modelProperty(IS_FORMED, false)
            .colorOverlaySteamHullModel(OVERLAY_ITEM_HATCH_INPUT, "overlay_pipe", "overlay_pipe_in_emissive")
            .localization(text -> text
                    .source("Steam Input Bus")
                    .translation(MinecraftLocale.ZH_CN, "蒸汽输入总线"))
            .tooltips(MachineTooltips.itemInput(),
                    Component.translatable("gtpm.machine.steam_bus.tooltip"),
                    Component.translatable("gtpm.universal.tooltip.item_storage_capacity", 4))
            .allowCoverOnFront(true)
            .register();

    public static final MachineDefinition STEAM_EXPORT_BUS = REGISTRATE
            .machine("steam_output_bus", holder -> new SteamItemBusPartMachine(holder, OUT))
            .rotationState(RotationState.ALL)
            .abilities(PartAbility.STEAM_EXPORT_ITEMS)
            .modelProperty(IS_FORMED, false)
            .colorOverlaySteamHullModel(OVERLAY_ITEM_HATCH_OUTPUT, "overlay_pipe", "overlay_pipe_out_emissive")
            .localization(text -> text
                    .source("Steam Output Bus")
                    .translation(MinecraftLocale.ZH_CN, "蒸汽输出总线"))
            .tooltips(MachineTooltips.itemOutput(),
                    Component.translatable("gtpm.machine.steam_bus.tooltip"),
                    Component.translatable("gtpm.universal.tooltip.item_storage_capacity", 4))
            .allowCoverOnFront(true)
            .register();

    public static final MachineDefinition STEAM_HATCH = REGISTRATE
            .machine("steam_input_hatch", SteamHatchPartMachine::new)
            .localization(text -> text
                    .source("Steam Input Hatch")
                    .translation(MinecraftLocale.ZH_CN, "蒸汽输入仓"))
            .rotationState(RotationState.ALL)
            .abilities(PartAbility.STEAM)
            .modelProperty(IS_FORMED, false)
            .overlaySteamHullModel("steam_hatch")
            .tooltips(Component.translatable("gtpm.universal.tooltip.fluid_storage_capacity",
                    SteamHatchPartMachine.INITIAL_TANK_CAPACITY),
                    Component.translatable("gtpm.machine.steam.steam_hatch.tooltip"))
            .allowCoverOnFront(true)
            .register();

    public static final MachineDefinition COKE_OVEN_HATCH = REGISTRATE.machine("coke_oven_hatch", CokeOvenHatch::new)
            .localization(text -> text
                    .source("Coke Oven Hatch")
                    .translation(MinecraftLocale.ZH_CN, "焦炉仓"))
            .rotationState(RotationState.ALL)
            .abilities(PartAbility.COKE_OVEN_HATCH)
            .modelProperty(IS_FORMED, false)
            .tooltips(Component.translatable("gtpm.part_sharing.disabled"))
            .simpleModel(GTPM.id("block/machine/part/coke_oven_hatch"))
            .register();

    public static final MachineDefinition PUMP_HATCH = REGISTRATE.machine("pump_hatch", PumpHatchPartMachine::new)
            .localization(text -> text
                    .source("Pump Hatch")
                    .translation(MinecraftLocale.ZH_CN, "水泵仓"))
            .rotationState(RotationState.ALL)
            .abilities(PartAbility.PUMP_FLUID_HATCH)
            .modelProperty(IS_FORMED, false)
            .model(createBasicReplaceableTextureMachineModel(GTPM.id("block/machine/part/pump_hatch")))
            .register();

    public static final MachineDefinition MAINTENANCE_HATCH = REGISTRATE
            .machine("maintenance_hatch", (blockEntity) -> new MaintenanceHatchPartMachine(blockEntity, false))
            .localization(text -> text
                    .source("Maintenance Hatch")
                    .translation(MinecraftLocale.ZH_CN, "维护仓"))
            .rotationState(RotationState.ALL)
            .abilities(PartAbility.MAINTENANCE)
            .modelProperty(IS_FORMED, false)
            .tooltips(Component.translatable("gtpm.part_sharing.disabled"))
            .modelProperty(GTMachineModelProperties.IS_TAPED, false)
            .model(createMaintenanceModel(GTPM.id("block/machine/part/maintenance_hatch")))
            .tier(LV)
            .register();

    public static final MachineDefinition CONFIGURABLE_MAINTENANCE_HATCH = REGISTRATE
            .machine("configurable_maintenance_hatch",
                    (blockEntity) -> new MaintenanceHatchPartMachine(blockEntity, true))
            .localization(text -> text
                    .source("Configurable Maintenance Hatch")
                    .translation(MinecraftLocale.ZH_CN, "可配置维护仓"))
            .rotationState(RotationState.ALL)
            .abilities(PartAbility.MAINTENANCE)
            .tooltips(Component.translatable("gtpm.part_sharing.disabled"))
            .modelProperty(IS_FORMED, false)
            .modelProperty(GTMachineModelProperties.IS_TAPED, false)
            .model(createMaintenanceModel(GTPM.id("block/machine/part/configurable_maintenance_hatch")))
            .tier(HV)
            .register();

    public static final MachineDefinition CLEANING_MAINTENANCE_HATCH = REGISTRATE
            .machine("cleaning_maintenance_hatch",
                    holder -> new CleaningMaintenanceHatchPartMachine(holder, CleanroomType.CLEANROOM))
            .localization(text -> text
                    .source("Cleaning Maintenance Hatch")
                    .translation(MinecraftLocale.ZH_CN, "超净维护仓"))
            .rotationState(RotationState.ALL)
            .abilities(PartAbility.MAINTENANCE)
            .modelProperty(IS_FORMED, false)
            .tooltips(Component.translatable("gtpm.part_sharing.disabled"),
                    Component.translatable("gtpm.machine.maintenance_hatch_cleanroom_auto.tooltip.0"),
                    Component.translatable("gtpm.machine.maintenance_hatch_cleanroom_auto.tooltip.1"))
            .tooltipBuilder((stack, tooltips) -> {
                tooltips.add(Component.literal("  ").append(Component
                        .translatable(CleanroomType.CLEANROOM.translationKey()).withStyle(ChatFormatting.GREEN)));
            })
            .overlayTieredHullModel(GTPM.id("block/machine/part/cleaning_maintenance_hatch"))
            .tier(HV)
            .register();

    public static final MachineDefinition AUTO_MAINTENANCE_HATCH = REGISTRATE
            .machine("auto_maintenance_hatch", AutoMaintenanceHatchPartMachine::new)
            .localization(text -> text
                    .source("Auto Maintenance Hatch")
                    .translation(MinecraftLocale.ZH_CN, "自动维护仓"))
            .rotationState(RotationState.ALL)
            .abilities(PartAbility.MAINTENANCE)
            .modelProperty(IS_FORMED, false)
            .tooltips(Component.translatable("gtpm.part_sharing.disabled"))
            .overlayTieredHullModel(GTPM.id("block/machine/part/auto_maintenance_hatch"))
            .tier(HV)
            .register();

    public static final MachineDefinition[] ITEM_PASSTHROUGH_HATCH = registerTieredMachines("item_passthrough_hatch",
            (holder, tier) -> new ItemBusPartMachine(holder, tier, IO.BOTH),
            (tier, builder) -> builder
                    .localization(text -> text
                            .source("%s Item Passthrough Hatch".formatted(VNF[tier]))
                            .translation(MinecraftLocale.ZH_CN, VNF[tier] + "§r物品通行仓"))
                    .rotationState(RotationState.ALL)
                    .abilities(PartAbility.PASSTHROUGH_HATCH)
                    .modelProperty(IS_FORMED, false)
                    .overlayTieredHullModel("item_passthrough_hatch")
                    .tooltips(
                            Component.translatable("gtpm.universal.tooltip.item_storage_capacity",
                                    (1 + Math.min(9, tier)) * (1 + Math.min(9, tier))),
                            Component.translatable("gtpm.part_sharing.enabled"))
                    .register(),
            ELECTRIC_TIERS);

    public static final MachineDefinition[] FLUID_PASSTHROUGH_HATCH = registerTieredMachines("fluid_passthrough_hatch",
            (holder, tier) -> new FluidHatchPartMachine(holder, tier, IO.BOTH,
                    FluidHatchPartMachine.INITIAL_TANK_CAPACITY_1X, 1),
            (tier, builder) -> builder
                    .localization(text -> text
                            .source("%s Fluid Passthrough Hatch".formatted(VNF[tier]))
                            .translation(MinecraftLocale.ZH_CN, VNF[tier] + "§r流体通行仓"))
                    .rotationState(RotationState.ALL)
                    .abilities(PartAbility.PASSTHROUGH_HATCH)
                    .modelProperty(IS_FORMED, false)
                    .overlayTieredHullModel("fluid_passthrough_hatch")
                    .tooltips(
                            Component.translatable("gtpm.universal.tooltip.fluid_storage_capacity_mult", tier + 1,
                                    16 * FluidType.BUCKET_VOLUME),
                            Component.translatable("gtpm.part_sharing.enabled"))
                    .register(),
            ELECTRIC_TIERS);

    public static final MachineDefinition RESERVOIR_HATCH = REGISTRATE
            .machine("reservoir_hatch", ReservoirHatchPartMachine::new)
            .localization(text -> text
                    .source("Reservoir Hatch")
                    .translation(MinecraftLocale.ZH_CN, "水库仓"))
            .tier(EV)
            .rotationState(RotationState.ALL)
            .abilities(PartAbility.IMPORT_FLUIDS)
            .modelProperty(IS_FORMED, false)
            .tooltips(
                    Component.translatable("gtpm.universal.tooltip.fluid_storage_capacity",
                            FormattingUtil.formatNumbers(ReservoirHatchPartMachine.FLUID_AMOUNT)),
                    Component.translatable("gtpm.part_sharing.enabled"))
            .overlayTieredHullModel("reservoir_hatch")
            .register();

    public static final MachineDefinition[] DUAL_IMPORT_HATCH = registerTieredMachines(
            "dual_input_hatch",
            (holder, tier) -> new DualHatchPartMachine(holder, tier, IN),
            (tier, builder) -> builder
                    .localization(text -> text
                            .source("%s Dual Input Hatch".formatted(VNF[tier]))
                            .translation(MinecraftLocale.ZH_CN, VNF[tier] + "§r输入总成"))
                    .rotationState(RotationState.ALL)
                    .abilities(DUAL_INPUT_HATCH_ABILITIES)
                    .modelProperty(IS_FORMED, false)
                    .overlayTieredHullModel("dual_input_hatch")
                    .tooltips(
                            Component.translatable("gtpm.machine.dual_hatch.import.tooltip"),
                            Component.translatable(
                                    "gtpm.universal.tooltip.item_storage_capacity",
                                    (int) Math.pow((tier - 4), 2)),
                            Component.translatable(
                                    "gtpm.universal.tooltip.fluid_storage_capacity_mult",
                                    (tier - 4),
                                    DualHatchPartMachine.getTankCapacity(DualHatchPartMachine.INITIAL_TANK_CAPACITY,
                                            tier)),
                            Component.translatable("gtpm.part_sharing.enabled"))
                    .register(),
            DUAL_HATCH_TIERS);

    public static final MachineDefinition[] DUAL_EXPORT_HATCH = registerTieredMachines(
            "dual_output_hatch",
            (holder, tier) -> new DualHatchPartMachine(holder, tier, OUT),
            (tier, builder) -> builder
                    .localization(text -> text
                            .source("%s Dual Output Hatch".formatted(VNF[tier]))
                            .translation(MinecraftLocale.ZH_CN, VNF[tier] + "§r输出总成"))
                    .rotationState(RotationState.ALL)
                    .abilities(DUAL_OUTPUT_HATCH_ABILITIES)
                    .modelProperty(IS_FORMED, false)
                    .overlayTieredHullModel("dual_output_hatch")
                    .tooltips(
                            Component.translatable("gtpm.machine.dual_hatch.export.tooltip"),
                            Component.translatable(
                                    "gtpm.universal.tooltip.item_storage_capacity",
                                    (int) Math.pow((tier - 4), 2)),
                            Component.translatable(
                                    "gtpm.universal.tooltip.fluid_storage_capacity_mult",
                                    (tier - 4),
                                    DualHatchPartMachine.getTankCapacity(
                                            DualHatchPartMachine.INITIAL_TANK_CAPACITY, tier)),
                            Component.translatable("gtpm.part_sharing.enabled"))
                    .register(),
            DUAL_HATCH_TIERS);

    public static final MachineDefinition[] DIODE = registerTieredMachines("diode",
            DiodePartMachine::new,
            (tier, builder) -> builder
                    .localization(text -> text
                            .source("%s Diode".formatted(VNF[tier]))
                            .translation(MinecraftLocale.ZH_CN, VNF[tier] + "§r二极管"))
                    .rotationState(RotationState.ALL)
                    .abilities(PartAbility.PASSTHROUGH_HATCH)
                    .modelProperty(IS_FORMED, false)
                    .modelProperty(GTMachineModelProperties.DIODE_AMP_MODE, DiodePartMachine.AmpMode.MODE_1A)
                    .model(createDiodeModel())
                    .tooltips(Component.translatable("gtpm.machine.diode.tooltip_general"),
                            Component.translatable("gtpm.machine.diode.tooltip_starts_at"),
                            Component.translatable("gtpm.universal.tooltip.voltage_in_out",
                                    FormattingUtil.formatNumbers(GTValues.V[tier]),
                                    GTValues.VNF[tier]),
                            Component.translatable("gtpm.universal.tooltip.amperage_in_out_till",
                                    DiodePartMachine.MAX_AMPS))
                    .register(),
            ELECTRIC_TIERS);

    public static final MachineDefinition[] ROTOR_HOLDER = registerTieredMachines("rotor_holder",
            RotorHolderPartMachine::new,
            (tier, builder) -> builder
                    .localization(text -> text
                            .source("%s Rotor Holder".formatted(VNF[tier]))
                            .translation(MinecraftLocale.ZH_CN, VNF[tier] + "§r转子支架"))
                    .rotationState(RotationState.ALL)
                    .abilities(PartAbility.ROTOR_HOLDER)
                    .modelProperty(IS_FORMED, false)
                    .modelProperty(HAS_ROTOR, false)
                    .modelProperty(IS_ROTOR_SPINNING, false)
                    .modelProperty(IS_EMISSIVE_ROTOR, false)
                    .model(createRotorHolderModel())
                    .tooltips(LangHandler.getMultiLang("gtpm.machine.rotor_holder.tooltip"))
                    .tooltips(Component.translatable("gtpm.part_sharing.disabled"))
                    .register(),
            GTValues.tiersBetween(HV, GTCEuAPI.isHighTier() ? OpV : UV));

    public static final MachineDefinition[] LASER_INPUT_HATCH_256 = registerLaserHatch(IN, 256,
            PartAbility.INPUT_LASER);
    public static final MachineDefinition[] LASER_OUTPUT_HATCH_256 = registerLaserHatch(OUT, 256,
            PartAbility.OUTPUT_LASER);
    public static final MachineDefinition[] LASER_INPUT_HATCH_1024 = registerLaserHatch(IN, 1024,
            PartAbility.INPUT_LASER);
    public static final MachineDefinition[] LASER_OUTPUT_HATCH_1024 = registerLaserHatch(OUT, 1024,
            PartAbility.OUTPUT_LASER);
    public static final MachineDefinition[] LASER_INPUT_HATCH_4096 = registerLaserHatch(IN, 4096,
            PartAbility.INPUT_LASER);
    public static final MachineDefinition[] LASER_OUTPUT_HATCH_4096 = registerLaserHatch(OUT, 4096,
            PartAbility.OUTPUT_LASER);
    public static final MachineDefinition MONITOR = REGISTRATE.machine("monitor", MonitorPartMachine::new)
            .localization(text -> text
                    .source("Monitor")
                    .translation(MinecraftLocale.ZH_CN, "监控器"))
            .rotationState(RotationState.ALL)
            .model(createOverlayCasingMachineModel(GTPM.id("block/casings/solid/machine_casing_frost_proof"),
                    GTPM.id("block/machine/part/computer_monitor")))
            .tier(MV)
            .register();
    public static final MachineDefinition ADVANCED_MONITOR = REGISTRATE
            .machine("advanced_monitor", AdvancedMonitorPartMachine::new)
            .localization(text -> text
                    .source("Advanced Monitor")
                    .translation(MinecraftLocale.ZH_CN, "高级监控器"))
            .rotationState(RotationState.ALL)
            .model(createOverlayCasingMachineModel(GTPM.id("block/casings/solid/machine_casing_clean_stainless_steel"),
                    GTPM.id("block/machine/part/computer_monitor")))
            .tier(HV)
            .allowExtendedFacing(true)
            .register();

    public static void init() {
        GTMultiMachines.init();
        GCYMMachines.init();
        GTResearchMachines.init();

        if (GTPM.MODS.AE2.isLoaded()) {
            GTAEMachines.init();
        }
    }

    public static void bakeRenderStates(Registry<MachineDefinition> registry) {
        Iterator<MachineDefinition> iter = registry.iterator();
        while (iter.hasNext()) {
            MachineDefinition machine = iter.next();
            for (MachineRenderState renderState : machine.getStateDefinition().getPossibleStates()) {
                MachineDefinition.RENDER_STATE_REGISTRY.add(renderState);
            }
        }
    }

    public static MachineDefinition get(String name) {
        return GTRegistries.MACHINES.get(GTPM.id(name));
    }
}
