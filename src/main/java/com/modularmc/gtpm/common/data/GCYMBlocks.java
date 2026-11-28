package com.modularmc.gtpm.common.data;

import com.modularmc.gtpm.GTPM;
import com.modularmc.gtpm.api.block.ActiveBlock;

import com.modularmc.registrate.localization.MinecraftLocale;
import com.modularmc.registrate.util.entry.BlockEntry;

import net.minecraft.world.level.block.Block;

import static com.modularmc.gtpm.common.data.GTBlocks.createActiveCasing;
import static com.modularmc.gtpm.common.data.GTBlocks.createCasingBlock;

public class GCYMBlocks {

    public static void init() {}

    public static final BlockEntry<Block> CASING_NONCONDUCTING = createCasingBlock("nonconducting_casing",
            GTPM.id("block/casings/gcym/nonconducting_casing"),
            text -> text.source("Nonconducting Casing")
                    .translation(MinecraftLocale.ZH_CN, "绝缘机械方块"));
    public static final BlockEntry<Block> CASING_VIBRATION_SAFE = createCasingBlock("vibration_safe_casing",
            GTPM.id("block/casings/gcym/vibration_safe_casing"),
            text -> text.source("Vibration-Safe Casing")
                    .translation(MinecraftLocale.ZH_CN, "防震机械方块"));
    public static final BlockEntry<Block> CASING_WATERTIGHT = createCasingBlock("watertight_casing",
            GTPM.id("block/casings/gcym/watertight_casing"),
            text -> text.source("Watertight Casing")
                    .translation(MinecraftLocale.ZH_CN, "防水机械方块"));
    public static final BlockEntry<Block> CASING_SECURE_MACERATION = createCasingBlock("secure_maceration_casing",
            GTPM.id("block/casings/gcym/secure_maceration_casing"),
            text -> text.source("Secure Maceration Casing")
                    .translation(MinecraftLocale.ZH_CN, "安全研磨机械方块"));
    public static final BlockEntry<Block> CASING_HIGH_TEMPERATURE_SMELTING = createCasingBlock(
            "high_temperature_smelting_casing", GTPM.id("block/casings/gcym/high_temperature_smelting_casing"),
            text -> text.source("High Temperature Smelting Casing")
                    .translation(MinecraftLocale.ZH_CN, "高温冶炼机械方块"));
    public static final BlockEntry<Block> CASING_LASER_SAFE_ENGRAVING = createCasingBlock("laser_safe_engraving_casing",
            GTPM.id("block/casings/gcym/laser_safe_engraving_casing"),
            text -> text.source("Laser-Safe Engraving Casing")
                    .translation(MinecraftLocale.ZH_CN, "激光安全机械方块"));
    public static final BlockEntry<Block> CASING_LARGE_SCALE_ASSEMBLING = createCasingBlock(
            "large_scale_assembler_casing", GTPM.id("block/casings/gcym/large_scale_assembling_casing"),
            text -> text.source("Large-Scale Assembler Casing")
                    .translation(MinecraftLocale.ZH_CN, "大型组装机机械方块"));
    public static final BlockEntry<Block> CASING_SHOCK_PROOF = createCasingBlock("shock_proof_cutting_casing",
            GTPM.id("block/casings/gcym/shock_proof_cutting_casing"),
            text -> text.source("Shock Proof Cutting Casing")
                    .translation(MinecraftLocale.ZH_CN, "防震切割机械方块"));
    public static final BlockEntry<Block> CASING_STRESS_PROOF = createCasingBlock("stress_proof_casing",
            GTPM.id("block/casings/gcym/stress_proof_casing"),
            text -> text.source("Stress Proof Casing")
                    .translation(MinecraftLocale.ZH_CN, "耐压机械方块"));
    public static final BlockEntry<Block> CASING_CORROSION_PROOF = createCasingBlock("corrosion_proof_casing",
            GTPM.id("block/casings/gcym/corrosion_proof_casing"),
            text -> text.source("Corrosion Proof Casing")
                    .translation(MinecraftLocale.ZH_CN, "耐腐蚀机械方块"));
    public static final BlockEntry<Block> CASING_REACTION_SAFE = createCasingBlock("reaction_safe_mixing_casing",
            GTPM.id("block/casings/gcym/reaction_safe_mixing_casing"),
            text -> text.source("Reaction-Safe Mixing Casing")
                    .translation(MinecraftLocale.ZH_CN, "惰性搅拌机械方块"));
    public static final BlockEntry<Block> CASING_ATOMIC = createCasingBlock("atomic_casing",
            GTPM.id("block/casings/gcym/atomic_casing"),
            text -> text.source("Atomic Casing")
                    .translation(MinecraftLocale.ZH_CN, "原子机械方块"));
    public static final BlockEntry<Block> CASING_INDUSTRIAL_STEAM = createCasingBlock("industrial_steam_casing",
            GTPM.id("block/casings/gcym/industrial_steam_casing"),
            text -> text.source("Industrial Steam Casing")
                    .translation(MinecraftLocale.ZH_CN, "工业蒸汽机械方块"));

    public static final BlockEntry<ActiveBlock> SLICING_BLADES = createActiveCasing("slicing_blades",
            "block/variant/slicing_blades",
            text -> text.source("Slicing Blades")
                    .translation(MinecraftLocale.ZH_CN, "切片刀"));
    public static final BlockEntry<ActiveBlock> MOLYBDENUM_DISILICIDE_COIL_BLOCK = createActiveCasing(
            "molybdenum_disilicide_coil_block", "block/variant/molybdenum_disilicide_coil_block",
            text -> text.source("Molybdenum Disilicide Coil Block")
                    .translation(MinecraftLocale.ZH_CN, "二硅化钼线圈方块"));
    public static final BlockEntry<ActiveBlock> ELECTROLYTIC_CELL = createActiveCasing("electrolytic_cell",
            "block/variant/electrolytic_cell",
            text -> text.source("Electrolytic Cell")
                    .translation(MinecraftLocale.ZH_CN, "电解器"));
    public static final BlockEntry<ActiveBlock> CRUSHING_WHEELS = createActiveCasing("crushing_wheels",
            "block/variant/crushing_wheels",
            text -> text.source("Crushing Wheels")
                    .translation(MinecraftLocale.ZH_CN, "粉碎轮"));
    public static final BlockEntry<ActiveBlock> HEAT_VENT = createActiveCasing("heat_vent", "block/variant/heat_vent",
            text -> text.source("Heat Vent")
                    .translation(MinecraftLocale.ZH_CN, "散热片"));
}
