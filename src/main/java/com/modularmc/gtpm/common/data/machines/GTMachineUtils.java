package com.modularmc.gtpm.common.data.machines;

import com.modularmc.gtpm.GTPM;
import com.modularmc.gtpm.api.GTCEuAPI;
import com.modularmc.gtpm.api.GTValues;
import com.modularmc.gtpm.api.block.MetaMachineBlock;
import com.modularmc.gtpm.api.blockentity.BlockEntityCreationInfo;
import com.modularmc.gtpm.api.capability.compat.FeCompat;
import com.modularmc.gtpm.api.capability.recipe.FluidRecipeCapability;
import com.modularmc.gtpm.api.capability.recipe.IO;
import com.modularmc.gtpm.api.capability.recipe.ItemRecipeCapability;
import com.modularmc.gtpm.api.data.RotationState;
import com.modularmc.gtpm.api.data.chemical.material.Material;
import com.modularmc.gtpm.api.data.chemical.material.properties.FluidPipeProperties;
import com.modularmc.gtpm.api.data.chemical.material.properties.PropertyKey;
import com.modularmc.gtpm.api.data.medicalcondition.MedicalCondition;
import com.modularmc.gtpm.api.data.tag.TagPrefix;
import com.modularmc.gtpm.api.fluids.PropertyFluidFilter;
import com.modularmc.gtpm.api.machine.*;
import com.modularmc.gtpm.api.machine.multiblock.MultiblockControllerMachine;
import com.modularmc.gtpm.api.machine.multiblock.PartAbility;
import com.modularmc.gtpm.api.machine.property.GTMachineModelProperties;
import com.modularmc.gtpm.api.machine.steam.SimpleSteamMachine;
import com.modularmc.gtpm.api.machine.trait.WorkLogic;
import com.modularmc.gtpm.api.multiblock.pattern.dsl.PatternBuilder;
import com.modularmc.gtpm.api.multiblock.util.RelativeDirection;
import com.modularmc.gtpm.api.recipe.GTRecipeType;
import com.modularmc.gtpm.api.registry.registrate.GTRegistrate;
import com.modularmc.gtpm.api.registry.registrate.MachineBuilder;
import com.modularmc.gtpm.api.registry.registrate.MultiblockMachineBuilder;
import com.modularmc.gtpm.client.renderer.machine.*;
import com.modularmc.gtpm.common.block.BoilerFireboxType;
import com.modularmc.gtpm.common.data.GTDataComponents;
import com.modularmc.gtpm.common.data.GTMaterialItems;
import com.modularmc.gtpm.common.data.GTMaterials;
import com.modularmc.gtpm.common.data.GTMedicalConditions;
import com.modularmc.gtpm.common.data.GTRecipeModifiers;
import com.modularmc.gtpm.common.data.GTRecipeTypes;
import com.modularmc.gtpm.common.data.models.GTMachineModels;
import com.modularmc.gtpm.common.item.DrumMachineItem;
import com.modularmc.gtpm.common.item.QuantumTankMachineItem;
import com.modularmc.gtpm.common.machine.electric.BatteryBufferMachine;
import com.modularmc.gtpm.common.machine.electric.ConverterMachine;
import com.modularmc.gtpm.common.machine.electric.TransformerMachine;
import com.modularmc.gtpm.common.machine.multiblock.electric.MultiblockTankMachine;
import com.modularmc.gtpm.common.machine.multiblock.generator.LargeCombustionEngineMachine;
import com.modularmc.gtpm.common.machine.multiblock.generator.LargeTurbineMachine;
import com.modularmc.gtpm.common.machine.multiblock.part.*;
import com.modularmc.gtpm.common.machine.multiblock.steam.LargeBoilerMachine;
import com.modularmc.gtpm.common.machine.storage.CrateMachine;
import com.modularmc.gtpm.common.machine.storage.DrumMachine;
import com.modularmc.gtpm.common.machine.storage.QuantumChestMachine;
import com.modularmc.gtpm.common.machine.storage.QuantumTankMachine;
import com.modularmc.gtpm.config.ConfigHolder;
import com.modularmc.gtpm.utils.FormattingUtil;

import com.modularmc.registrate.localization.LocalizationDeclaration;
import com.modularmc.registrate.localization.MinecraftLocale;
import com.modularmc.registrate.localization.annotation.TransVariants;
import com.modularmc.registrate.localization.annotation.Translation;
import com.modularmc.registrate.localization.annotation.Variant;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.fluids.SimpleFluidContent;

import it.unimi.dsi.fastutil.Pair;
import it.unimi.dsi.fastutil.ints.Int2IntFunction;
import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.IntFunction;
import java.util.function.Supplier;

import static com.modularmc.gtpm.api.GTValues.*;
import static com.modularmc.gtpm.api.capability.recipe.IO.*;
import static com.modularmc.gtpm.api.machine.property.GTMachineModelProperties.IS_FORMED;
import static com.modularmc.gtpm.common.data.GTRecipeTypes.DUMMY_RECIPES;
import static com.modularmc.gtpm.common.data.models.GTMachineModels.*;
import static com.modularmc.gtpm.common.registry.GTRegistration.REGISTRATE;
import static com.modularmc.gtpm.utils.FormattingUtil.toEnglishName;

@TransVariants(keyPrefix = "gtpm",
               value = {
                       @Variant(id = "fluid_pipe.max_temperature",
                                source = "§cTemperature Limit: §f%s",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§c温度上限：§f%s")),
                       @Variant(id = "machine.energy_converter.description",
                                source = "Converts Energy between EU and FE",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "在EU和FE之间转换能量形式。")),
                       @Variant(id = "machine.energy_converter.tooltip_conversion_eu",
                                source = "§aEU Conversion: §f%dA %d EU (%s§f) -> %d Native",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "§aEU转换：§f%dA %d EU（%s§f）§7 -> %d FE")),
                       @Variant(id = "machine.energy_converter.tooltip_conversion_native",
                                source = "§cNative Conversion: §f%d FE -> %dA %d EU (%s§f)",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "§cFE转换：§f%d FE -> %dA %d EU （%s§f）")),
                       @Variant(id = "machine.energy_converter.tooltip_tool_usage",
                                source = "Starts as §fFE Converter§7, use Soft Mallet to change",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "默认§f转换为FE§r，使用软锤切换。")),
                       @Variant(id = "machine.large_combustion_engine.tooltip.boost_extreme",
                                source = "Supply §f80 mB/s§7 of Liquid Oxygen to produce up to §f%s EU/t§7 at §f2x§7 fuel consumption.",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "提供§f80 mB/s§7的液态氧，并消耗§f双倍§7燃料以产生高达§f%s§7EU/t的功率。")),
                       @Variant(id = "machine.large_combustion_engine.tooltip.boost_regular",
                                source = "Supply §f20 mB/s§7 of Oxygen to produce up to §f%s EU/t§7 at §f2x§7 fuel consumption.",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "提供§f20 mB/s§7的氧气，并消耗§f双倍§7燃料以产生高达§f%s§7EU/t的功率。")),
                       @Variant(id = "machine.laser_hatch.both.tooltip",
                                source = "§cLaser Cables must be in a straight line!§7",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "§c激光传导线缆必须直线摆放！§7")),
                       @Variant(id = "machine.multiblock.tank.tooltip",
                                source = "Fill and drain through the controller or tank valves.",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "通过控制器或储罐阀门进行吞吐。")),
                       @Variant(id = "machine.quantum_chest.tooltip",
                                source = "§7Better than Storage Drawers",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7比储物抽屉好")),
                       @Variant(id = "machine.quantum_tank.tooltip",
                                source = "§7Compact place to store all your fluids",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7小小身体，大大肚量")),
                       @Variant(id = "machine.tank_valve.tooltip",
                                source = "Use to fill and drain multiblock tanks. Auto outputs when facing down.",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "用于吞吐多方块储罐中的流体。正面向下时可自动输出。")),
                       @Variant(id = "machine.transformer.description",
                                source = "§7Transforms Energy between voltage tiers",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7转换能量的电压等级")),
                       @Variant(id = "machine.transformer.tooltip_tool_usage",
                                source = "Starts as §fTransform Down§7, use Screwdriver to change",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "默认§f降压§r，使用螺丝刀切换。")),
                       @Variant(id = "machine.transformer.tooltip_transform_down",
                                source = "§aTransform Down: §f%dA %s EU (%s§f) -> %dA %s EU (%s§f)",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "§a降压：§f%dA %s EU（%s§f）-> %dA %s EU（%s§f）")),
                       @Variant(id = "machine.transformer.tooltip_transform_up",
                                source = "§cTransform Up: §f%dA %s EU (%s§f) -> %dA %s EU (%s§f)",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "§c升压：§f%dA %s EU（%s§f）-> %dA %s EU（%s§f）")),
                       @Variant(id = "multiblock.large_boiler.explosion_tooltip",
                                source = "Will explode if provided Fuel with no Water",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "无水时提供燃料将爆炸")),
                       @Variant(id = "multiblock.large_boiler.heat_time_tooltip",
                                source = "§7Takes §f%d seconds §7to boiling up",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7需要§f%d秒§7预热")),
                       @Variant(id = "multiblock.large_boiler.max_temperature",
                                source = "Max Temperature: %dK, Steam Production: %dmB/t",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "最高温度：%dK，蒸汽产量：%dmB/t")),
                       @Variant(id = "multiblock.turbine.efficiency_tooltip",
                                source = "Each Rotor Holder above %s§7 adds §f10%% efficiency and multiplies EU/t by 2§7.",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "转子支架等级超过%s§r时，每级§7增加§f10%%效率，并翻倍输出功率§7。")),
                       @Variant(id = "universal.tooltip.amperage_in_till",
                                source = "§eAmperage IN up to: §f%dA",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§e输入电流上限：§f%dA")),
                       @Variant(id = "universal.tooltip.amperage_out_till",
                                source = "§eAmperage OUT up to: §f%dA",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§e输出电流上限：§f%dA")),
                       @Variant(id = "universal.tooltip.base_production_eut",
                                source = "§eBase Production: §f%d EU/t",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "§e基础产能功率：§f%d EU/t")),
                       @Variant(id = "universal.tooltip.item_storage_total",
                                source = "§6Item Capacity: §f%d items",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§6物品容量：§f%d物品")),
                       @Variant(id = "universal.tooltip.terrain_resist",
                                source = "This Machine will not explode when exposed to the Elements",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "该机器不会在暴露于恶劣天气时爆炸")),
                       @Variant(id = "universal.tooltip.uses_per_hour_lubricant",
                                source = "Uses §f%d mB/hr §7of §6Lubricant §7while working",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "工作时消耗§f%d mB/hr§7§6润滑油§7")),
               })
public class GTMachineUtils {

    public static final int[] ALL_TIERS = GTValues.tiersBetween(ULV, GTCEuAPI.isHighTier() ? MAX : UHV);
    public static final int[] ELECTRIC_TIERS = GTValues.tiersBetween(LV, GTCEuAPI.isHighTier() ? OpV : UV);
    public static final int[] LOW_TIERS = GTValues.tiersBetween(LV, EV);
    public static final int[] HIGH_TIERS = GTValues.tiersBetween(IV, GTCEuAPI.isHighTier() ? OpV : UHV);
    public static final int[] MULTI_HATCH_TIERS = GTValues.tiersBetween(EV, GTCEuAPI.isHighTier() ? MAX : UHV);
    public static final int[] DUAL_HATCH_TIERS = GTValues.tiersBetween(LuV, GTCEuAPI.isHighTier() ? MAX : UHV);

    public static final Int2IntFunction defaultTankSizeFunction = tier -> (tier <= GTValues.LV ? 8 :
            tier == GTValues.MV ? 12 : tier == GTValues.HV ? 16 : tier == GTValues.EV ? 32 : 64) *
            FluidType.BUCKET_VOLUME;
    public static final Int2IntFunction hvCappedTankSizeFunction = tier -> (tier <= GTValues.LV ? 8 :
            tier == GTValues.MV ? 12 : 16) * FluidType.BUCKET_VOLUME;
    public static final Int2IntFunction largeTankSizeFunction = tier -> (tier <= GTValues.LV ? 32 :
            tier == GTValues.MV ? 48 : 64) * FluidType.BUCKET_VOLUME;
    public static final Int2IntFunction steamGeneratorTankSizeFunction = tier -> Math.min(16 * (1 << (tier - 1)), 64) *
            FluidType.BUCKET_VOLUME;
    public static final Int2IntFunction genericGeneratorTankSizeFunction = tier -> Math.min(4 * (1 << (tier - 1)),
            16) * FluidType.BUCKET_VOLUME;

    public static Object2IntMap<MachineDefinition> DRUM_CAPACITY = new Object2IntArrayMap<>();

    public static final PartAbility[] DUAL_INPUT_HATCH_ABILITIES = new PartAbility[] {
            PartAbility.IMPORT_ITEMS, PartAbility.IMPORT_FLUIDS,
    };

    public static final PartAbility[] DUAL_OUTPUT_HATCH_ABILITIES = new PartAbility[] {
            PartAbility.EXPORT_ITEMS, PartAbility.EXPORT_FLUIDS,
    };

    private static String chineseVoltageName(int tier) {
        String name = switch (tier) {
            case ULV -> "超低压";
            case LV -> "低压";
            case MV -> "中压";
            case HV -> "高压";
            case EV -> "超高压";
            case IV -> "强导压";
            case LuV -> "剧差压";
            case ZPM -> "零点压";
            case UV -> "极限压";
            case UHV -> "极高压";
            case UEV -> "极超压";
            case UIV -> "极巨压";
            case UXV -> "极顶压";
            case OpV -> "过载压";
            case MAX -> "上限压";
            default -> throw new IllegalArgumentException("Unknown tier: " + tier);
        };
        return VCF[tier].replace("§l", "") + name + "§r";
    }

    public static String tieredChineseName(int tier, String name) {
        String prefix = switch (tier) {
            case ULV -> "原始";
            case LV -> "基础";
            case MV, HV, EV -> "进阶";
            case IV, LuV, ZPM -> "精英";
            case UV -> "终极";
            case UHV, UEV, UIV, UXV -> "史诗";
            case OpV -> "传奇";
            case MAX -> "MAX";
            default -> throw new IllegalArgumentException("Unknown tier: " + tier);
        };
        String color = tier <= LV ? "" : VCF[tier];
        String suffix = VLVT[tier];
        return color + prefix + name + (suffix.startsWith("§") ? suffix : " " + suffix);
    }

    public static MachineDefinition[] registerSimpleMachines(String name, GTRecipeType recipeType,
                                                             Int2IntFunction tankScalingFunction,
                                                             boolean hasPollutionDebuff) {
        return registerSimpleMachines(REGISTRATE, name, recipeType, tankScalingFunction, hasPollutionDebuff);
    }

    public static MachineDefinition[] registerSimpleMachines(GTRegistrate registrate, String name,
                                                             GTRecipeType recipeType,
                                                             Int2IntFunction tankScalingFunction,
                                                             boolean hasPollutionDebuff) {
        return registerSimpleMachines(registrate, name, recipeType, tankScalingFunction, hasPollutionDebuff,
                ELECTRIC_TIERS);
    }

    public static MachineDefinition[] registerSimpleMachines(String name, GTRecipeType recipeType,
                                                             Int2IntFunction tankScalingFunction) {
        return registerSimpleMachines(REGISTRATE, name, recipeType, tankScalingFunction);
    }

    public static MachineDefinition[] registerSimpleMachines(GTRegistrate registrate, String name,
                                                             GTRecipeType recipeType,
                                                             Int2IntFunction tankScalingFunction) {
        return registerSimpleMachines(registrate, name, recipeType, tankScalingFunction, false);
    }

    public static MachineDefinition[] registerSimpleMachines(String name, GTRecipeType recipeType) {
        return registerSimpleMachines(REGISTRATE, name, recipeType);
    }

    public static MachineDefinition[] registerSimpleMachines(GTRegistrate registrate, String name,
                                                             GTRecipeType recipeType) {
        return registerSimpleMachines(registrate, name, recipeType, defaultTankSizeFunction);
    }

    public static MachineDefinition[] registerSimpleMachines(GTRegistrate registrate,
                                                             String name,
                                                             GTRecipeType recipeType,
                                                             Int2IntFunction tankScalingFunction,
                                                             boolean hasPollutionDebuff,
                                                             int... tiers) {
        return registerSimpleMachines(registrate, name, recipeType, tankScalingFunction, hasPollutionDebuff,
                tier -> null, tiers);
    }

    public static MachineDefinition[] registerSimpleMachines(GTRegistrate registrate,
                                                             String name,
                                                             GTRecipeType recipeType,
                                                             Int2IntFunction tankScalingFunction,
                                                             boolean hasPollutionDebuff,
                                                             IntFunction<@Nullable Consumer<LocalizationDeclaration.Builder>> primaryTooltip,
                                                             int... tiers) {
        return registerTieredMachines(registrate, name,
                (info, tier) -> new SimpleTieredMachine(info, tier, tankScalingFunction), (tier, builder) -> {
                    if (hasPollutionDebuff) {
                        builder.recipeModifiers(GTRecipeModifiers.ENVIRONMENT_REQUIREMENT
                                .apply(GTMedicalConditions.CARBON_MONOXIDE_POISONING, 100 * tier),
                                GTRecipeModifiers.OC_NON_PERFECT)
                                .conditionalTooltip(defaultEnvironmentRequirement(),
                                        ConfigHolder.INSTANCE.gameplay.environmentalHazards);
                    } else {
                        builder.recipeModifier(GTRecipeModifiers.OC_NON_PERFECT);
                    }
                    return builder
                            .primaryTooltip(primaryTooltip.apply(tier))
                            .localization(text -> {
                                text.source("%s %s %s".formatted(VLVH[tier], toEnglishName(name), VLVT[tier]));
                                var recipeName = recipeType.getLocalization();
                                if (recipeName != null) {
                                    String chinese = recipeName.translations().get(MinecraftLocale.ZH_CN);
                                    if (chinese != null) {
                                        text.translation(MinecraftLocale.ZH_CN, tieredChineseName(tier, chinese));
                                    }
                                }
                            })
                            .editableUI(SimpleTieredMachine.EDITABLE_UI_CREATOR.apply(GTPM.id(name), recipeType))
                            .rotationState(RotationState.NON_Y_AXIS)
                            .recipeType(recipeType)
                            .workableTieredHullModel(GTPM.id("block/machines/" + name))
                            .tooltips(workableTiered(tier, GTValues.V[tier], GTValues.V[tier] * 64, recipeType,
                                    tankScalingFunction.applyAsInt(tier), true))
                            .register();
                },
                tiers);
    }

    public static MachineDefinition[] registerTieredMachines(String name,
                                                             BiFunction<BlockEntityCreationInfo, Integer, MetaMachine> factory,
                                                             BiFunction<Integer, MachineBuilder<MachineDefinition, ?>, MachineDefinition> builder,
                                                             int... tiers) {
        return registerTieredMachines(REGISTRATE, name, factory, builder, tiers);
    }

    public static MachineDefinition[] registerTieredMachines(GTRegistrate registrate,
                                                             String name,
                                                             BiFunction<BlockEntityCreationInfo, Integer, MetaMachine> factory,
                                                             BiFunction<Integer, MachineBuilder<MachineDefinition, ?>, MachineDefinition> builder,
                                                             int... tiers) {
        MachineDefinition[] definitions = new MachineDefinition[GTValues.TIER_COUNT];
        for (int tier : tiers) {
            var register = registrate
                    .machine(GTValues.VN[tier].toLowerCase(Locale.ROOT) + "_" + name,
                            info -> factory.apply(info, tier))
                    .tier(tier);
            definitions[tier] = builder.apply(tier, register);
        }
        return definitions;
    }

    public static Pair<MachineDefinition, MachineDefinition> registerSteamMachines(String name,
                                                                                   BiFunction<BlockEntityCreationInfo, Boolean, MetaMachine> factory,
                                                                                   BiFunction<Boolean, MachineBuilder<MachineDefinition, ?>, MachineDefinition> builder) {
        return registerSteamMachines(REGISTRATE, name, factory, builder);
    }

    public static Pair<MachineDefinition, MachineDefinition> registerSteamMachines(GTRegistrate registrate, String name,
                                                                                   BiFunction<BlockEntityCreationInfo, Boolean, MetaMachine> factory,
                                                                                   BiFunction<Boolean, MachineBuilder<MachineDefinition, ?>, MachineDefinition> builder) {
        String chinese = switch (name) {
            case "steam_alloy_smelter" -> "蒸汽合金炉";
            case "steam_compressor" -> "蒸汽压缩机";
            case "steam_extractor" -> "蒸汽提取机";
            case "steam_forge_hammer" -> "蒸汽锻造锤";
            case "steam_furnace" -> "蒸汽熔炉";
            case "steam_liquid_boiler" -> "蒸汽液体燃料锅炉";
            case "steam_macerator" -> "蒸汽研磨机";
            case "steam_miner" -> "蒸汽采矿机";
            case "steam_rock_crusher" -> "蒸汽碎岩机";
            case "steam_solar_boiler" -> "蒸汽太阳能锅炉";
            case "steam_solid_boiler" -> "蒸汽固体燃料锅炉";
            default -> null;
        };
        MachineDefinition lowTier = builder.apply(false,
                registrate.machine("lp_%s".formatted(name), holder -> factory.apply(holder, false))
                        .localization(text -> {
                            text.source("Low Pressure " + FormattingUtil.toEnglishName(name));
                            if (chinese != null) text.translation(MinecraftLocale.ZH_CN, "低压" + chinese);
                        })
                        .tier(0));
        MachineDefinition highTier = builder.apply(true,
                registrate.machine("hp_%s".formatted(name), holder -> factory.apply(holder, true))
                        .localization(text -> {
                            text.source("High Pressure " + FormattingUtil.toEnglishName(name));
                            if (chinese != null) text.translation(MinecraftLocale.ZH_CN, "高压" + chinese);
                        })
                        .tier(1));
        return Pair.of(lowTier, highTier);
    }

    public static MachineDefinition[] registerFluidHatches(String name, String displayName, String tooltip,
                                                           IO io, int initialCapacity, int slots,
                                                           int[] tiers, PartAbility... abilities) {
        return registerFluidHatches(REGISTRATE, name, displayName, tooltip, io, initialCapacity, slots, tiers,
                abilities);
    }

    public static MachineDefinition[] registerFluidHatches(GTRegistrate registrate, String name, String displayName,
                                                           String tooltip,
                                                           IO io, int initialCapacity, int slots,
                                                           int[] tiers, PartAbility... abilities) {
        final String pipeOverlay;
        if (slots >= 9) {
            pipeOverlay = "overlay_pipe_9x";
        } else if (slots >= 4) {
            pipeOverlay = "overlay_pipe_4x";
        } else {
            pipeOverlay = null;
        }
        final String ioOverlay = io == OUT ? OVERLAY_FLUID_HATCH_OUTPUT : OVERLAY_FLUID_HATCH_INPUT;
        final String emissiveOverlay = io == OUT ? "overlay_pipe_out_emissive" : "overlay_pipe_in_emissive";
        return registerTieredMachines(registrate, name,
                (holder, tier) -> new FluidHatchPartMachine(holder, tier, io, initialCapacity, slots),
                (tier, builder) -> {
                    builder.localization(text -> {
                        text.source(VNF[tier] + ' ' + displayName);
                        String chinese = switch (displayName) {
                            case "Input Hatch" -> "输入仓";
                            case "Quadruple Input Hatch" -> "四重输入仓";
                            case "Nonuple Input Hatch" -> "九重输入仓";
                            case "Output Hatch" -> "输出仓";
                            case "Quadruple Output Hatch" -> "四重输出仓";
                            case "Nonuple Output Hatch" -> "九重输出仓";
                            default -> null;
                        };
                        if (chinese != null) text.translation(MinecraftLocale.ZH_CN, VNF[tier] + "§r" + chinese);
                    })
                            .rotationState(RotationState.ALL)
                            .colorOverlayTieredHullModel(ioOverlay, pipeOverlay, emissiveOverlay)
                            .abilities(abilities)
                            .modelProperty(IS_FORMED, false)
                            .tooltips(Component.translatable("gtpm.machine." + tooltip + ".tooltip"))
                            .allowCoverOnFront(true);

                    if (slots == 1) {
                        builder.tooltips(Component.translatable("gtpm.universal.tooltip.fluid_storage_capacity",
                                FormattingUtil
                                        .formatNumbers(FluidHatchPartMachine.getTankCapacity(initialCapacity, tier))));
                    } else {
                        builder.tooltips(Component.translatable("gtpm.universal.tooltip.fluid_storage_capacity_mult",
                                slots, FormattingUtil
                                        .formatNumbers(FluidHatchPartMachine.getTankCapacity(initialCapacity, tier))));
                    }
                    return builder.register();
                },
                tiers);
    }

    public static MachineDefinition[] registerTransformerMachines(String langName, int baseAmp) {
        return registerTransformerMachines(REGISTRATE, langName, baseAmp);
    }

    public static MachineDefinition[] registerTransformerMachines(GTRegistrate registrate, String langName,
                                                                  int baseAmp) {
        return registerTieredMachines(registrate, "transformer_%da".formatted(baseAmp),
                (holder, tier) -> new TransformerMachine(holder, tier, baseAmp),
                (tier, builder) -> builder
                        .rotationState(RotationState.ALL)
                        .itemColor((itemStack, index) -> switch (index) {
                            case 1 -> ConfigHolder.INSTANCE.client.getDefaultPaintingColor();
                            case 2 -> VC[tier + 1];
                            case 3 -> VC[tier];
                            default -> -1;
                        })
                        .modelProperty(GTMachineModelProperties.IS_TRANSFORM_UP, false)
                        .model(createTransformerModel(baseAmp))
                        .localization(text -> {
                            text.source(
                                    "%s %sTransformer".formatted(VCF[tier] + VOLTAGE_NAMES[tier] + ChatFormatting.RESET,
                                            langName.isEmpty() ? "" : langName + " "));
                            String type = switch (langName) {
                                case "" -> "变压器";
                                case "Hi-Amp (2x)" -> "进阶变压器";
                                case "Hi-Amp (4x)" -> "高流变压器";
                                case "Power" -> "高能变压器";
                                default -> null;
                            };
                            if (type != null) {
                                text.translation(MinecraftLocale.ZH_CN,
                                        "%s%s（%s§r）".formatted(chineseVoltageName(tier), type,
                                                VNF[tier].replace("§l", "")));
                            }
                        })
                        .tooltips(Component.translatable("gtpm.machine.transformer.description"),
                                Component.translatable("gtpm.machine.transformer.tooltip_tool_usage"),
                                Component.translatable("gtpm.machine.transformer.tooltip_transform_down",
                                        baseAmp, FormattingUtil.formatNumbers(GTValues.V[tier + 1]),
                                        GTValues.VNF[tier + 1],
                                        baseAmp * 4, FormattingUtil.formatNumbers(GTValues.V[tier]),
                                        GTValues.VNF[tier]),
                                Component.translatable("gtpm.machine.transformer.tooltip_transform_up",
                                        baseAmp * 4, FormattingUtil.formatNumbers(GTValues.V[tier]), GTValues.VNF[tier],
                                        baseAmp, FormattingUtil.formatNumbers(GTValues.V[tier + 1]),
                                        GTValues.VNF[tier + 1]))
                        .register(),
                GTValues.tiersBetween(ULV, GTCEuAPI.isHighTier() ? OpV : UV));
        // UHV not needed, as a UV transformer transforms up to UHV
    }

    public static MachineDefinition[] registerSimpleGenerator(String name,
                                                              GTRecipeType recipeType,
                                                              Int2IntFunction tankScalingFunction,
                                                              float hazardStrengthPerOperation,
                                                              int... tiers) {
        return registerSimpleGenerator(REGISTRATE, name, recipeType, tankScalingFunction, hazardStrengthPerOperation,
                tiers);
    }

    public static MachineDefinition[] registerSimpleGenerator(GTRegistrate registrate, String name,
                                                              GTRecipeType recipeType,
                                                              Int2IntFunction tankScalingFunction,
                                                              float hazardStrengthPerOperation,
                                                              int... tiers) {
        return registerTieredMachines(registrate, name,
                (holder, tier) -> new SimpleGeneratorMachine(holder, tier, hazardStrengthPerOperation * tier,
                        tankScalingFunction),
                (tier, builder) -> builder
                        .localization(text -> {
                            text.source("%s %s Generator %s".formatted(VLVH[tier], toEnglishName(name), VLVT[tier]));
                            String chinese = switch (name) {
                                case "combustion" -> "内燃发电机";
                                case "steam_turbine" -> "蒸汽轮机";
                                case "gas_turbine" -> "燃气轮机";
                                default -> null;
                            };
                            if (chinese != null)
                                text.translation(MinecraftLocale.ZH_CN, tieredChineseName(tier, chinese));
                        })
                        .editableUI(SimpleGeneratorMachine.EDITABLE_UI_CREATOR.apply(GTPM.id(name), recipeType))
                        .rotationState(RotationState.ALL)
                        .recipeType(recipeType)
                        .recipeModifier(SimpleGeneratorMachine::recipeModifier, true)
                        .addOutputLimit(ItemRecipeCapability.CAP, 0)
                        .addOutputLimit(FluidRecipeCapability.CAP, 0)
                        .simpleGeneratorModel(GTPM.id("block/generators/" + name))
                        .tooltips(workableTiered(tier, GTValues.V[tier], GTValues.V[tier] * 64, recipeType,
                                tankScalingFunction.applyAsInt(tier), false))
                        .register(),
                tiers);
    }

    public static Pair<MachineDefinition, MachineDefinition> registerSimpleSteamMachines(String name,
                                                                                         GTRecipeType recipeType) {
        return registerSimpleSteamMachines(REGISTRATE, name, recipeType);
    }

    public static Pair<MachineDefinition, MachineDefinition> registerSimpleSteamMachines(GTRegistrate registrate,
                                                                                         String name,
                                                                                         GTRecipeType recipeType) {
        return registerSteamMachines(registrate, "steam_" + name, SimpleSteamMachine::new,
                (pressure, builder) -> builder
                        .rotationState(RotationState.ALL)
                        .recipeType(recipeType)
                        .recipeModifier(SimpleSteamMachine::recipeModifier)
                        .modelProperty(GTMachineModelProperties.VENT_DIRECTION, RelativeDirection.BACK)
                        .workableSteamHullModel(pressure, GTPM.id("block/machines/" + name))
                        .register());
    }

    public static MachineDefinition[] registerBatteryBuffer(int batterySlotSize) {
        return registerBatteryBuffer(REGISTRATE, batterySlotSize);
    }

    public static MachineDefinition[] registerBatteryBuffer(GTRegistrate registrate, int batterySlotSize) {
        return registerTieredMachines(registrate, "battery_buffer_" + batterySlotSize + "x",
                (holder, tier) -> new BatteryBufferMachine(holder, tier, batterySlotSize),
                (tier, builder) -> builder
                        .rotationState(RotationState.ALL)
                        .model(GTMachineModels.createBatteryBufferModel(batterySlotSize))
                        .localization(text -> text
                                .source("%s %sx Battery Buffer".formatted(
                                        VCF[tier] + VOLTAGE_NAMES[tier] + ChatFormatting.RESET,
                                        batterySlotSize))
                                .translation(MinecraftLocale.ZH_CN,
                                        "%sx%s电池箱（%s§r）".formatted(batterySlotSize, chineseVoltageName(tier),
                                                VNF[tier])))
                        .tooltips(
                                Component.translatable("gtpm.universal.tooltip.item_storage_capacity",
                                        batterySlotSize),
                                Component.translatable("gtpm.universal.tooltip.voltage_in_out",
                                        FormattingUtil.formatNumbers(GTValues.V[tier]),
                                        GTValues.VNF[tier]),
                                Component.translatable("gtpm.universal.tooltip.amperage_in_till",
                                        batterySlotSize * BatteryBufferMachine.AMPS_PER_BATTERY_NORMAL),
                                Component.translatable("gtpm.universal.tooltip.amperage_out_till", batterySlotSize))
                        .register(),
                ALL_TIERS);
    }

    public static MachineDefinition[] registerCharger(int itemSlotSize) {
        return registerCharger(REGISTRATE, itemSlotSize);
    }

    public static MachineDefinition[] registerCharger(GTRegistrate registrate, int itemSlotSize) {
        return registerTieredMachines(registrate, "charger_" + itemSlotSize + "x",
                (holder, tier) -> new BatteryBufferMachine(holder, tier, itemSlotSize,
                        BatteryBufferMachine.AMPS_PER_BATTERY_CHARGER, 0),
                (tier, builder) -> builder
                        .rotationState(RotationState.ALL)
                        .modelProperty(GTMachineModelProperties.CHARGER_STATE, BatteryBufferMachine.State.IDLE)
                        .model(GTMachineModels.createChargerModel())
                        .localization(text -> text
                                .source("%s %sx Turbo Charger".formatted(
                                        VCF[tier] + VOLTAGE_NAMES[tier] + ChatFormatting.RESET,
                                        itemSlotSize))
                                .translation(MinecraftLocale.ZH_CN,
                                        "%sx%s闪聚充电箱（%s§r）".formatted(itemSlotSize, chineseVoltageName(tier),
                                                VNF[tier])))
                        .tooltips(Component.translatable("gtpm.universal.tooltip.item_storage_capacity", itemSlotSize),
                                Component.translatable("gtpm.universal.tooltip.voltage_in_out",
                                        FormattingUtil.formatNumbers(GTValues.V[tier]),
                                        GTValues.VNF[tier]),
                                Component.translatable("gtpm.universal.tooltip.amperage_in_till",
                                        itemSlotSize * BatteryBufferMachine.AMPS_PER_BATTERY_CHARGER))
                        .register(),
                ALL_TIERS);
    }

    public static MachineDefinition[] registerConverter(int amperage) {
        return registerConverter(REGISTRATE, amperage);
    }

    public static MachineDefinition[] registerConverter(GTRegistrate registrate, int amperage) {
        final var tab = registrate.creativeModeTab();

        if (!ConfigHolder.INSTANCE.compat.energy.enableFEConverters) {
            // REGISTRATE.creativeModeTab(null);
            registrate.creativeModeTab(() -> null);
        }

        MachineDefinition[] converters = registerTieredMachines(registrate, amperage + "a_energy_converter",
                (holder, tier) -> new ConverterMachine(holder, tier, amperage),
                (tier, builder) -> builder
                        .rotationState(RotationState.ALL)
                        .localization(text -> text
                                .source("%s %s§eA§r Energy Converter".formatted(
                                        VCF[tier] + VN[tier] + ChatFormatting.RESET,
                                        amperage))
                                .translation(MinecraftLocale.ZH_CN, "%d§e安§r%s§r能量转换器".formatted(amperage, VNF[tier])))
                        .modelProperty(GTMachineModelProperties.IS_FE_TO_EU, false)
                        .model(GTMachineModels.createConverterModel(amperage))
                        .tooltips(Component.translatable("gtpm.machine.energy_converter.description"),
                                Component.translatable("gtpm.machine.energy_converter.tooltip_tool_usage"),
                                Component.translatable("gtpm.machine.energy_converter.tooltip_conversion_native",
                                        FeCompat.toFeLong(V[tier] * amperage,
                                                FeCompat.ratio(true)),
                                        amperage, V[tier], GTValues.VNF[tier]),
                                Component.translatable("gtpm.machine.energy_converter.tooltip_conversion_eu", amperage,
                                        V[tier], GTValues.VNF[tier],
                                        FeCompat.toFeLong(V[tier] * amperage,
                                                FeCompat.ratio(false))))
                        .register(),
                ALL_TIERS);

        if (!ConfigHolder.INSTANCE.compat.energy.enableFEConverters) {
            // REGISTRATE.creativeModeTab(MACHINE);
            registrate.creativeModeTab(() -> tab);
        }
        return converters;
    }

    public static MachineDefinition[] registerLaserHatch(IO io, int amperage, PartAbility ability) {
        return registerLaserHatch(REGISTRATE, io, amperage, ability);
    }

    public static MachineDefinition[] registerLaserHatch(GTRegistrate registrate, IO io, int amperage,
                                                         PartAbility ability) {
        String name = io == IN ? "target" : "source";
        return registerTieredMachines(registrate, amperage + "a_laser_" + name + "_hatch",
                (holder, tier) -> new LaserHatchPartMachine(holder, io, tier, amperage), (tier, builder) -> builder
                        .localization(text -> text
                                .source(VNF[tier] + "§r " + FormattingUtil.formatNumbers(amperage) + "§eA§r Laser " +
                                        FormattingUtil.toEnglishName(name) + " Hatch")
                                .translation(MinecraftLocale.ZH_CN, "%d§e安§r%s§r激光%s仓".formatted(amperage, VNF[tier],
                                        io == IN ? "靶" : "源")))
                        .rotationState(RotationState.ALL)
                        .tooltips(Component.translatable("gtpm.machine.laser_hatch." + name + ".tooltip"),
                                Component.translatable("gtpm.machine.laser_hatch.both.tooltip"),
                                Component.translatable("gtpm.universal.tooltip.voltage_" + (io == IN ? "in" : "out"),
                                        FormattingUtil.formatNumbers(V[tier]), VNF[tier]),
                                Component.translatable("gtpm.universal.tooltip.amperage_in", amperage),
                                Component.translatable("gtpm.universal.tooltip.energy_storage_capacity",
                                        FormattingUtil
                                                .formatNumbers(
                                                        EnergyHatchPartMachine.getHatchEnergyCapacity(tier, amperage))),
                                Component.translatable("gtpm.part_sharing.disabled"))
                        .abilities(ability)
                        .modelProperty(IS_FORMED, false)
                        .overlayTieredHullModel("laser_" + name + "_hatch")
                        .register(),
                HIGH_TIERS);
    }

    public static MachineDefinition registerCrate(Material material, int capacity, String lang) {
        return registerCrate(REGISTRATE, material, capacity, lang);
    }

    public static MachineDefinition registerCrate(GTRegistrate registrate, Material material, int capacity,
                                                  String lang) {
        return crate(registrate, material, capacity).langValue(lang).register();
    }

    public static MachineBuilder<MachineDefinition, ?> crate(GTRegistrate registrate, Material material, int capacity) {
        final boolean wooden = material.hasProperty(PropertyKey.WOOD);

        return registrate.machine(material.getName() + "_crate", holder -> new CrateMachine(holder, material, capacity))
                .rotationState(RotationState.NONE)
                .tooltips(Component.translatable("gtpm.universal.tooltip.item_storage_capacity", capacity))
                .modelProperty(GTMachineModelProperties.IS_TAPED, false)
                .model(GTMachineModels.createCrateModel(wooden))
                .paintingColor(wooden ? 0xFFFFFF : material.getMaterialRGB())
                .itemColor((s, t) -> wooden ? 0xFFFFFF : material.getMaterialRGB());
    }

    public static MachineDefinition registerDrum(Material material, int capacity, String lang) {
        return registerDrum(REGISTRATE, material, capacity, lang);
    }

    public static MachineDefinition registerDrum(GTRegistrate registrate, Material material, int capacity,
                                                 String lang) {
        return registerDrum(registrate, material, builder -> builder.langValue(lang), capacity);
    }

    public static MachineDefinition registerDrum(Material material,
                                                 Consumer<MachineBuilder<MachineDefinition, ?>> configure,
                                                 int capacity) {
        return registerDrum(REGISTRATE, material, configure, capacity);
    }

    private static MachineDefinition registerDrum(GTRegistrate registrate, Material material,
                                                  Consumer<MachineBuilder<MachineDefinition, ?>> configure,
                                                  int capacity) {
        boolean wooden = material.hasProperty(PropertyKey.WOOD);
        var builder = registrate
                .machine(material.getName() + "_drum", MachineDefinition::new,
                        MetaMachineBlock::new,
                        (holder, prop) -> DrumMachineItem.create(holder, prop, material),
                        info -> new DrumMachine(info, material, capacity))
                .rotationState(RotationState.NONE)
                .simpleModel(GTPM.id("block/machine/template/drum/" + (wooden ? "wooden" : "metal") + "_drum"))
                .tooltipBuilder((stack, list) -> {
                    TANK_TOOLTIPS.accept(stack, list);
                    if (material.hasProperty(PropertyKey.FLUID_PIPE)) {
                        FluidPipeProperties pipeprops = material.getProperty(PropertyKey.FLUID_PIPE);
                        pipeprops.appendTooltips(list, false, true);
                    }
                })
                .tooltips(Component.translatable("gtpm.machine.quantum_tank.tooltip"),
                        Component.translatable("gtpm.universal.tooltip.fluid_storage_capacity",
                                FormattingUtil.formatNumbers(capacity)))
                .paintingColor(wooden ? 0xFFFFFF : material.getMaterialRGB())
                .itemColor((s, i) -> wooden ? 0xFFFFFF : material.getMaterialRGB());
        configure.accept(builder);
        var definition = builder.register();
        DRUM_CAPACITY.put(definition, capacity);
        return definition;
    }

    public static MachineDefinition[] registerQuantumTanks(String name, int... tiers) {
        return registerQuantumTanks(REGISTRATE, name, tiers);
    }

    public static MachineDefinition[] registerQuantumTanks(GTRegistrate registrate, String name, int... tiers) {
        MachineDefinition[] definitions = new MachineDefinition[GTValues.TIER_COUNT];
        for (int tier : tiers) {
            long maxAmount = 4000 * FluidType.BUCKET_VOLUME * (long) Math.pow(2, tier - 1);
            var register = registrate.machine(
                    GTValues.VN[tier].toLowerCase(Locale.ROOT) + "_" + name,
                    MachineDefinition::new,
                    MetaMachineBlock::new, QuantumTankMachineItem::new,
                    (holder) -> new QuantumTankMachine(holder, tier, maxAmount))
                    .localization(text -> {
                        text.source(toEnglishName(name) + " " + LVT[tier]);
                        String chinese = switch (name) {
                            case "quantum_tank" -> "量子缸";
                            case "super_tank" -> "超级缸";
                            default -> null;
                        };
                        if (chinese != null) text.translation(MinecraftLocale.ZH_CN, chinese + " " + LVT[tier]);
                    })
                    .blockProp(Block.Properties::dynamicShape)
                    .rotationState(RotationState.ALL)
                    .allowExtendedFacing(true)
                    .model(createTieredHullMachineModel(GTPM.id("block/machine/template/quantum/quantum_tank"))
                            .andThen(b -> b.addDynamicRenderer(DynamicRenderHelper::createQuantumTankRender)))
                    .hasBER(true)
                    .tooltipBuilder(TANK_TOOLTIPS)
                    .tooltips(Component.translatable("gtpm.machine.quantum_tank.tooltip"),
                            Component.translatable("gtpm.universal.tooltip.fluid_storage_capacity",
                                    FormattingUtil.formatNumbers(maxAmount)))
                    .tier(tier)
                    .register();
            QuantumTankMachine.TANK_CAPACITY.put(register, maxAmount);
            definitions[tier] = register;
        }
        return definitions;
    }

    public static MachineDefinition[] registerQuantumChests(String name, int... tiers) {
        return registerQuantumChests(REGISTRATE, name, tiers);
    }

    public static MachineDefinition[] registerQuantumChests(GTRegistrate registrate, String name, int... tiers) {
        return registerTieredMachines(registrate, name,
                (holder, tier) -> new QuantumChestMachine(holder, tier,
                        tier == MAX ? Long.MAX_VALUE : 4_000_000 * (long) Math.pow(2, tier - 1)),
                (tier, builder) -> builder
                        .localization(text -> {
                            text.source(toEnglishName(name) + " " + LVT[tier]);
                            String chinese = switch (name) {
                                case "quantum_chest" -> "量子箱";
                                case "super_chest" -> "超级箱";
                                default -> null;
                            };
                            if (chinese != null) text.translation(MinecraftLocale.ZH_CN, chinese + " " + LVT[tier]);
                        })
                        .blockProp(Block.Properties::dynamicShape)
                        .rotationState(RotationState.ALL)
                        .allowExtendedFacing(true)
                        .model(createTieredHullMachineModel(GTPM.id("block/machine/template/quantum/quantum_chest"))
                                .andThen(
                                        b -> b.addDynamicRenderer(DynamicRenderHelper::createQuantumChestRender)))
                        .hasBER(true)
                        .tooltipBuilder(CHEST_TOOLTIPS)
                        .tooltips(Component.translatable("gtpm.machine.quantum_chest.tooltip"),
                                Component.translatable("gtpm.universal.tooltip.item_storage_total",
                                        FormattingUtil.formatNumbers(tier == MAX ? Long.MAX_VALUE :
                                                4_000_000 * (long) Math.pow(2, tier - 1))))
                        .register(),
                tiers);
    }

    //////////////////////////////////////
    // ********** Misc **********//
    //////////////////////////////////////
    // multi register helpers

    static final ResourceLocation MULTIBLOCK_TANK_OVERLAY = GTPM.id("block/multiblock/multiblock_tank");
    static final ResourceLocation TANK_VALVE_OVERLAY = GTPM.id("block/multiblock/tank_valve");

    public static MultiblockMachineDefinition registerMultiblockTank(String name, String displayName, int capacity,
                                                                     Supplier<Block> casing,
                                                                     Supplier<MetaMachineBlock> valve,
                                                                     @Nullable PropertyFluidFilter filter,
                                                                     BiConsumer<MultiblockMachineBuilder<?, ?>, ResourceLocation> rendererSetup) {
        return registerMultiblockTank(REGISTRATE, name, displayName, capacity, casing, valve, filter, rendererSetup);
    }

    public static MultiblockMachineDefinition registerMultiblockTank(GTRegistrate registrate, String name,
                                                                     String displayName, int capacity,
                                                                     Supplier<Block> casing,
                                                                     Supplier<MetaMachineBlock> valve,
                                                                     @Nullable PropertyFluidFilter filter,
                                                                     BiConsumer<MultiblockMachineBuilder<?, ?>, ResourceLocation> rendererSetup) {
        var builder = multiblockTank(registrate, name, capacity, casing, filter).langValue(displayName);
        rendererSetup.accept(builder, MULTIBLOCK_TANK_OVERLAY);
        return builder.register();
    }

    static MultiblockMachineBuilder<?, ?> multiblockTank(GTRegistrate registrate, String name, int capacity,
                                                         Supplier<Block> casing, @Nullable PropertyFluidFilter filter) {
        return registrate
                .multiblock(name, holder -> new MultiblockTankMachine(holder, capacity, filter))
                .tooltips(
                        Component.translatable("gtpm.machine.multiblock.tank.tooltip"),
                        Component.translatable("gtpm.universal.tooltip.fluid_storage_capacity", capacity),
                        (filter != null) ? Component.translatable("gtpm.fluid_pipe.max_temperature",
                                FormattingUtil.formatTemperature(filter.getMaxFluidTemperature())) : null)
                .rotationState(RotationState.ALL)
                .recipeType(DUMMY_RECIPES)
                .pattern(MultiblockControllerMachine.DEFAULT_STRUCTURE,
                        definition -> PatternBuilder.start(definition)
                                .appendDefinition()
                                .build())
                .appearanceBlock(casing);
    }

    public static MachineDefinition registerTankValve(String name, String displayName, boolean isMetal,
                                                      BiConsumer<MachineBuilder<?, ?>, ResourceLocation> rendererSetup) {
        return registerTankValve(REGISTRATE, name, displayName, isMetal, rendererSetup);
    }

    public static MachineDefinition registerTankValve(GTRegistrate registrate, String name, String displayName,
                                                      boolean isMetal,
                                                      BiConsumer<MachineBuilder<?, ?>, ResourceLocation> rendererSetup) {
        var builder = tankValve(registrate, name, isMetal).langValue(displayName);
        rendererSetup.accept(builder, TANK_VALVE_OVERLAY);
        return builder.register();
    }

    static MachineBuilder<MachineDefinition, ?> tankValve(GTRegistrate registrate, String name, boolean isMetal) {
        return registrate
                .machine(name, holder -> new TankValvePartMachine(holder, isMetal))
                .tooltips(Component.translatable("gtpm.machine.tank_valve.tooltip"),
                        Component.translatable("gtpm.part_sharing.disabled"))
                .rotationState(RotationState.ALL);
    }

    public static MultiblockMachineDefinition[] registerTieredMultis(String name,
                                                                     BiFunction<BlockEntityCreationInfo, Integer, MultiblockControllerMachine> factory,
                                                                     BiFunction<Integer, MultiblockMachineBuilder<?, ?>, MultiblockMachineDefinition> builder,
                                                                     int... tiers) {
        return registerTieredMultis(REGISTRATE, name, factory, builder, tiers);
    }

    public static MultiblockMachineDefinition[] registerTieredMultis(GTRegistrate registrate, String name,
                                                                     BiFunction<BlockEntityCreationInfo, Integer, MultiblockControllerMachine> factory,
                                                                     BiFunction<Integer, MultiblockMachineBuilder<?, ?>, MultiblockMachineDefinition> builder,
                                                                     int... tiers) {
        MultiblockMachineDefinition[] definitions = new MultiblockMachineDefinition[GTValues.TIER_COUNT];
        for (int tier : tiers) {
            var register = registrate
                    .multiblock(GTValues.VN[tier].toLowerCase(Locale.ROOT) + "_" + name,
                            holder -> factory.apply(holder, tier))
                    .tier(tier);
            definitions[tier] = builder.apply(tier, register);
        }
        return definitions;
    }

    public static MultiblockMachineDefinition registerLargeBoiler(String name, Supplier<? extends Block> casing,
                                                                  Supplier<? extends Block> pipe,
                                                                  Supplier<? extends Block> fireBox,
                                                                  ResourceLocation texture, BoilerFireboxType firebox,
                                                                  int maxTemperature, int heatSpeed) {
        return registerLargeBoiler(REGISTRATE, name, casing, pipe, fireBox, texture, firebox, maxTemperature,
                heatSpeed);
    }

    public static MultiblockMachineDefinition registerLargeBoiler(GTRegistrate registrate, String name,
                                                                  Supplier<? extends Block> casing,
                                                                  Supplier<? extends Block> pipe,
                                                                  Supplier<? extends Block> fireBox,
                                                                  ResourceLocation texture, BoilerFireboxType firebox,
                                                                  int maxTemperature, int heatSpeed) {
        return largeBoiler(registrate, name, casing, fireBox, texture, firebox, maxTemperature, heatSpeed)
                .langValue("Large %s Boiler".formatted(FormattingUtil.toEnglishName(name)))
                .register();
    }

    static MultiblockMachineBuilder<?, ?> largeBoiler(GTRegistrate registrate, String name,
                                                      Supplier<? extends Block> casing,
                                                      Supplier<? extends Block> fireBox,
                                                      ResourceLocation texture, BoilerFireboxType firebox,
                                                      int maxTemperature, int heatSpeed) {
        // spotless:off
        return registrate
                .multiblock("%s_large_boiler".formatted(name),
                        holder -> new LargeBoilerMachine(holder, maxTemperature, heatSpeed))
                .allowExtendedFacing(false)
                .rotationState(RotationState.NON_Y_AXIS)
                .recipeType(GTRecipeTypes.LARGE_BOILER_RECIPES)
                .recipeModifier(LargeBoilerMachine::recipeModifier, true)
                .appearanceBlock(casing)
                .partAppearance((controller, part, side) ->
                        controller.self().getBlockPos().below().getY() == part.self().getBlockPos().getY() ?
                                         fireBox.get().defaultBlockState() : casing.get().defaultBlockState())
                .pattern(MultiblockControllerMachine.DEFAULT_STRUCTURE, definition -> PatternBuilder.start(definition)
                        .appendDefinition()
                        .build())
                .recoveryItems(
                        () -> new ItemLike[] {
                                GTMaterialItems.MATERIAL_ITEMS.get(TagPrefix.dustTiny, GTMaterials.Ash).get() })
                .modelProperty(GTMachineModelProperties.RECIPE_LOGIC_STATUS, WorkLogic.Status.IDLE)
                .model(createWorkableCasingMachineModel(texture,
                        GTPM.id("block/multiblock/generator/large_%s_boiler".formatted(name)))
                        .andThen(b -> b.addDynamicRenderer(() -> DynamicRenderHelper.makeBoilerPartRender(firebox, casing))))
                .tooltips(
                        Component.translatable("gtpm.multiblock.large_boiler.max_temperature", maxTemperature + 274,
                                maxTemperature),
                        Component.translatable("gtpm.multiblock.large_boiler.heat_time_tooltip",
                                maxTemperature / heatSpeed / 20),
                        Component.translatable("gtpm.multiblock.large_boiler.explosion_tooltip")
                                .withStyle(ChatFormatting.DARK_RED));
        // spotless:on
    }

    public static MultiblockMachineDefinition registerLargeCombustionEngine(String name, int tier,
                                                                            Supplier<? extends Block> casing,
                                                                            Supplier<? extends Block> gear,
                                                                            Supplier<? extends Block> intake,
                                                                            ResourceLocation casingTexture,
                                                                            ResourceLocation overlayModel) {
        return registerLargeCombustionEngine(REGISTRATE, name, tier, casing, gear, intake, casingTexture, overlayModel);
    }

    public static MultiblockMachineDefinition registerLargeCombustionEngine(GTRegistrate registrate,
                                                                            String name, int tier,
                                                                            Supplier<? extends Block> casing,
                                                                            Supplier<? extends Block> gear,
                                                                            Supplier<? extends Block> intake,
                                                                            ResourceLocation casingTexture,
                                                                            ResourceLocation overlayModel) {
        return largeCombustionEngine(registrate, name, tier, casing, casingTexture, overlayModel).register();
    }

    static MultiblockMachineBuilder<?, ?> largeCombustionEngine(GTRegistrate registrate, String name, int tier,
                                                                Supplier<? extends Block> casing,
                                                                ResourceLocation casingTexture,
                                                                ResourceLocation overlayModel) {
        return registrate.multiblock(name, holder -> new LargeCombustionEngineMachine(holder, tier))
                .rotationState(RotationState.ALL)
                .recipeType(GTRecipeTypes.COMBUSTION_GENERATOR_FUELS)
                .generator(true)
                .recipeModifier(LargeCombustionEngineMachine::recipeModifier, true)
                .appearanceBlock(casing)
                .pattern(MultiblockControllerMachine.DEFAULT_STRUCTURE,
                        definition -> PatternBuilder.start(definition)
                                .appendDefinition()
                                .build())
                .recoveryItems(
                        () -> new ItemLike[] {
                                GTMaterialItems.MATERIAL_ITEMS.get(TagPrefix.dustTiny, GTMaterials.Ash).get() })
                .workableCasingModel(casingTexture, overlayModel)
                .tooltips(
                        Component.translatable("gtpm.universal.tooltip.base_production_eut", V[tier]),
                        Component.translatable("gtpm.universal.tooltip.uses_per_hour_lubricant",
                                FluidType.BUCKET_VOLUME),
                        tier > EV ?
                                Component.translatable("gtpm.machine.large_combustion_engine.tooltip.boost_extreme",
                                        V[tier] * 4) :
                                Component.translatable("gtpm.machine.large_combustion_engine.tooltip.boost_regular",
                                        V[tier] * 3));
    }

    public static MultiblockMachineDefinition registerLargeTurbine(String name, int tier, GTRecipeType recipeType,
                                                                   Supplier<? extends Block> casing,
                                                                   Supplier<? extends Block> gear,
                                                                   ResourceLocation casingTexture,
                                                                   ResourceLocation overlayModel) {
        return registerLargeTurbine(REGISTRATE, name, tier, recipeType, casing, gear, casingTexture, overlayModel);
    }

    public static MultiblockMachineDefinition registerLargeTurbine(GTRegistrate registrate,
                                                                   String name, int tier, GTRecipeType recipeType,
                                                                   Supplier<? extends Block> casing,
                                                                   Supplier<? extends Block> gear,
                                                                   ResourceLocation casingTexture,
                                                                   ResourceLocation overlayModel) {
        return registerLargeTurbine(registrate, name, tier, recipeType, casing, gear, casingTexture, overlayModel,
                true);
    }

    public static MultiblockMachineDefinition registerLargeTurbine(String name, int tier, GTRecipeType recipeType,
                                                                   Supplier<? extends Block> casing,
                                                                   Supplier<? extends Block> gear,
                                                                   ResourceLocation casingTexture,
                                                                   ResourceLocation overlayModel,
                                                                   boolean needsMuffler) {
        return registerLargeTurbine(REGISTRATE, name, tier, recipeType, casing, gear, casingTexture, overlayModel,
                needsMuffler);
    }

    public static MultiblockMachineDefinition registerLargeTurbine(GTRegistrate registrate,
                                                                   String name, int tier, GTRecipeType recipeType,
                                                                   Supplier<? extends Block> casing,
                                                                   Supplier<? extends Block> gear,
                                                                   ResourceLocation casingTexture,
                                                                   ResourceLocation overlayModel,
                                                                   boolean needsMuffler) {
        return registerLargeTurbine(registrate, name, tier, recipeType, casing, gear, casingTexture, overlayModel,
                needsMuffler, null);
    }

    public static MultiblockMachineDefinition registerLargeTurbine(
                                                                   GTRegistrate registrate, String name, int tier,
                                                                   GTRecipeType recipeType,
                                                                   Supplier<? extends Block> casing,
                                                                   Supplier<? extends Block> gear,
                                                                   ResourceLocation casingTexture,
                                                                   ResourceLocation overlayModel, boolean needsMuffler,
                                                                   @Nullable Consumer<LocalizationDeclaration.Builder> localization) {
        var builder = registrate.multiblock(name, holder -> new LargeTurbineMachine(holder, tier));
        if (localization != null) {
            builder.localization(localization);
        }
        return builder
                .rotationState(RotationState.ALL)
                .recipeType(recipeType)
                .generator(true)
                .recipeModifier(LargeTurbineMachine::recipeModifier, true)
                .appearanceBlock(casing)
                .pattern(MultiblockControllerMachine.DEFAULT_STRUCTURE,
                        definition -> PatternBuilder.start(definition)
                                .appendDefinition()
                                .build())
                .recoveryItems(
                        () -> new ItemLike[] {
                                GTMaterialItems.MATERIAL_ITEMS.get(TagPrefix.dustTiny, GTMaterials.Ash).get() })
                .workableCasingModel(casingTexture, overlayModel)
                .tooltips(
                        Component.translatable("gtpm.universal.tooltip.base_production_eut", V[tier] * 2),
                        Component.translatable("gtpm.multiblock.turbine.efficiency_tooltip", VNF[tier]))
                .register();
    }

    // Tooltips
    public static Component explosion() {
        if (ConfigHolder.INSTANCE.machines.shouldWeatherOrTerrainExplosion)
            return Component.translatable("gtpm.universal.tooltip.terrain_resist");
        return null;
    }

    public static Component environmentRequirement(MedicalCondition condition) {
        return Component.translatable("gtpm.recipe.environmental_hazard.reverse",
                Component.translatable("gtpm.medical_condition." + condition.name));
    }

    public static Component defaultEnvironmentRequirement() {
        return environmentRequirement(GTMedicalConditions.CARBON_MONOXIDE_POISONING);
    }

    public static BiConsumer<ItemStack, List<Component>> TANK_TOOLTIPS = (stack, list) -> {
        FluidStack stored;
        long storedAmount = 0;

        var largeContent = stack.get(GTDataComponents.LARGE_FLUID_CONTENT);
        if (largeContent != null) {
            stored = largeContent.stored();
            storedAmount = largeContent.amount();
        } else {
            stored = stack.getOrDefault(GTDataComponents.FLUID_CONTENT, SimpleFluidContent.EMPTY).copy();
        }

        if (storedAmount == 0 && !stored.isEmpty()) storedAmount = stored.getAmount();
        list.add(1, Component.translatable("gtpm.universal.tooltip.fluid_stored", stored.getHoverName(),
                FormattingUtil.formatNumbers(storedAmount)));
    };

    public static BiConsumer<ItemStack, List<Component>> CHEST_TOOLTIPS = (stack, list) -> {
        var storage = stack.get(GTDataComponents.LARGE_ITEM_CONTENT);
        if (storage != null) {
            ItemStack itemStack = storage.stored();
            long storedAmount = storage.amount();
            list.add(1, Component.translatable("gtpm.universal.tooltip.item_stored", itemStack.getHoverName(),
                    FormattingUtil.formatNumbers(storedAmount)));
        }
    };

    public static Component[] workableTiered(int tier, long voltage, long energyCapacity, GTRecipeType recipeType,
                                             long tankCapacity, boolean input) {
        List<Component> tooltipComponents = new ArrayList<>();
        tooltipComponents.add(input ?
                Component.translatable("gtpm.universal.tooltip.voltage_in",
                        FormattingUtil.formatNumbers(voltage), GTValues.VNF[tier]) :
                Component.translatable("gtpm.universal.tooltip.voltage_out",
                        FormattingUtil.formatNumbers(voltage), GTValues.VNF[tier]));
        tooltipComponents.add(Component.translatable(
                "gtpm.universal.tooltip.energy_storage_capacity",
                FormattingUtil.formatNumbers(energyCapacity)));
        if (recipeType.getMaxInputs(FluidRecipeCapability.CAP) > 0 ||
                recipeType.getMaxOutputs(FluidRecipeCapability.CAP) > 0) {
            tooltipComponents.add(Component.translatable(
                    "gtpm.universal.tooltip.fluid_storage_capacity",
                    FormattingUtil.formatNumbers(tankCapacity)));
        }
        return tooltipComponents.toArray(Component[]::new);
    }

    public static void init() {}
}
