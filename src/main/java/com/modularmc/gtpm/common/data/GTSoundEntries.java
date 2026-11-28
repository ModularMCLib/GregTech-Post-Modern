package com.modularmc.gtpm.common.data;

import com.modularmc.gtpm.api.registry.GTRegistries;
import com.modularmc.gtpm.api.sound.SoundEntry;

import com.modularmc.registrate.localization.MinecraftLocale;

import net.minecraft.core.registries.BuiltInRegistries;

import static com.modularmc.gtpm.common.registry.GTRegistration.REGISTRATE;

public class GTSoundEntries {

    // Machine Sounds
    public static final SoundEntry FORGE_HAMMER = REGISTRATE.sound("forge_hammer")
            .localization(text -> text.source("Forge Hammer thumping")
                    .translation(MinecraftLocale.ZH_CN, "锻造锤：DUANG"))
            .build();

    public static final SoundEntry MACERATOR = REGISTRATE.sound("macerator")
            .localization(text -> text.source("Macerator crushing")
                    .translation(MinecraftLocale.ZH_CN, "研磨机：破碎"))
            .build();

    public static final SoundEntry CHEMICAL = REGISTRATE.sound("chemical")
            .localization(text -> text.source("Chemical bubbling")
                    .translation(MinecraftLocale.ZH_CN, "化学反应釜：运行中"))
            .build();

    public static final SoundEntry ASSEMBLER = REGISTRATE.sound("assembler")
            .localization(text -> text.source("Assembler constructing")
                    .translation(MinecraftLocale.ZH_CN, "组装机：运行中"))
            .build();

    public static final SoundEntry CENTRIFUGE = REGISTRATE.sound("centrifuge")
            .localization(text -> text.source("Centrifuge spinning")
                    .translation(MinecraftLocale.ZH_CN, "离心机：旋转"))
            .build();

    public static final SoundEntry COMPRESSOR = REGISTRATE.sound("compressor")
            .localization(text -> text.source("Compressor squeezing")
                    .translation(MinecraftLocale.ZH_CN, "压缩机：挤压"))
            .build();

    public static final SoundEntry ELECTROLYZER = REGISTRATE.sound("electrolyzer")
            .localization(text -> text.source("Electrolyzer sparking")
                    .translation(MinecraftLocale.ZH_CN, "电解机：爆出火花"))
            .build();

    public static final SoundEntry MIXER = REGISTRATE.sound("mixer")
            .localization(text -> text.source("Mixer sloshing")
                    .translation(MinecraftLocale.ZH_CN, "搅拌机：搅动"))
            .build();

    public static final SoundEntry REPLICATOR = REGISTRATE.sound("replicator")
            .localization(text -> text.source("Replicator copying")
                    .translation(MinecraftLocale.ZH_CN, "复制机：运行"))
            .build();

    public static final SoundEntry ARC = REGISTRATE.sound("arc")
            .localization(text -> text.source("Arcs buzzing")
                    .translation(MinecraftLocale.ZH_CN, "电弧炉：嗡——"))
            .build();

    public static final SoundEntry BOILER = REGISTRATE.sound("boiler")
            .localization(text -> text.source("Boiler heating")
                    .translation(MinecraftLocale.ZH_CN, "锅炉：液体沸腾"))
            .build();

    public static final SoundEntry FURNACE = REGISTRATE.sound("furnace")
            .localization(text -> text.source("Furnace heating")
                    .translation(MinecraftLocale.ZH_CN, "电炉：冶炼"))
            .build();

    public static final SoundEntry COOLING = REGISTRATE.sound("cooling")
            .localization(text -> text.source("Freezer humming")
                    .translation(MinecraftLocale.ZH_CN, "真空冷冻机：嗡——"))
            .build();

    public static final SoundEntry FIRE = REGISTRATE.sound("fire")
            .localization(text -> text.source("Fire crackling")
                    .translation(MinecraftLocale.ZH_CN, "火：爆裂"))
            .build();

    public static final SoundEntry BATH = REGISTRATE.sound("bath")
            .localization(text -> text.source("Bath fizzing")
                    .translation(MinecraftLocale.ZH_CN, "化学浸洗机：嘶嘶"))
            .build();

    public static final SoundEntry MOTOR = REGISTRATE.sound("motor")
            .localization(text -> text.source("Motor humming")
                    .translation(MinecraftLocale.ZH_CN, "马达：转啊转"))
            .build();

    public static final SoundEntry CUT = REGISTRATE.sound("cut")
            .localization(text -> text.source("Cutter whirring")
                    .translation(MinecraftLocale.ZH_CN, "切割机：嘶嘶叫"))
            .build();

    public static final SoundEntry TURBINE = REGISTRATE.sound("turbine")
            .localization(text -> text.source("Turbine whizzing")
                    .translation(MinecraftLocale.ZH_CN, "涡轮：呼啸"))
            .build();

    public static final SoundEntry COMBUSTION = REGISTRATE.sound("combustion")
            .localization(text -> text.source("Combusting")
                    .translation(MinecraftLocale.ZH_CN, "内燃发电机：燃料燃烧"))
            .build();

    public static final SoundEntry COMPUTATION = REGISTRATE.sound("computation")
            .localization(text -> text.source("Computer beeps")
                    .translation(MinecraftLocale.ZH_CN, "计算机：哔哔"))
            .build();

    public static final SoundEntry MINER = REGISTRATE.sound("miner")
            .localization(text -> text.source("Miner excavating")
                    .translation(MinecraftLocale.ZH_CN, "采矿机：开采"))
            .build();

    public static final SoundEntry SCIENCE = REGISTRATE.sound("science")
            .localization(text -> text.source("s c i e n c e")
                    .translation(MinecraftLocale.ZH_CN, "化学反应釜：科！学！"))
            .build();

    public static final SoundEntry JET_ENGINE = REGISTRATE.sound("jet_engine")
            .localization(text -> text.source("Jet roaring")
                    .translation(MinecraftLocale.ZH_CN, "喷气背包：轰轰"))
            .build();

    public static final SoundEntry WRENCH_TOOL = REGISTRATE.sound("wrench")
            .localization(text -> text.source("Wrench rattling")
                    .translation(MinecraftLocale.ZH_CN, "扳手：嘎啦"))
            .build();

    public static final SoundEntry SOFT_MALLET_TOOL = REGISTRATE.sound("soft_hammer")
            .localization(text -> text.source("Soft tap")
                    .translation(MinecraftLocale.ZH_CN, "软锤：轻敲"))
            .build();

    public static final SoundEntry DRILL_TOOL = REGISTRATE.sound("drill")
            .localization(text -> text.source("Drilling")
                    .translation(MinecraftLocale.ZH_CN, "电钻：嗡嗡"))
            .build();

    public static final SoundEntry PLUNGER_TOOL = REGISTRATE.sound("plunger")
            .localization(text -> text.source("Plunger popping")
                    .translation(MinecraftLocale.ZH_CN, "搋子：清理流体"))
            .build();

    public static final SoundEntry FILE_TOOL = REGISTRATE.sound("file")
            .localization(text -> text.source("File rasping")
                    .translation(MinecraftLocale.ZH_CN, "锉：磨削"))
            .build();

    public static final SoundEntry SAW_TOOL = REGISTRATE.sound("saw")
            .localization(text -> text.source("Sawing")
                    .translation(MinecraftLocale.ZH_CN, "锯：切割"))
            .build();

    public static final SoundEntry SCREWDRIVER_TOOL = REGISTRATE.sound("screwdriver")
            .localization(text -> text.source("Screwing")
                    .translation(MinecraftLocale.ZH_CN, "螺丝刀：拧螺丝"))
            .build();

    public static final SoundEntry CHAINSAW_TOOL = REGISTRATE.sound("chainsaw")
            .localization(text -> text.source("Chainsaw revving")
                    .translation(MinecraftLocale.ZH_CN, "链锯：拉动油门"))
            .build();

    public static final SoundEntry WIRECUTTER_TOOL = REGISTRATE.sound("wirecutter")
            .localization(text -> text.source("Wire snipped")
                    .translation(MinecraftLocale.ZH_CN, "剪线钳：剪断"))
            .build();

    public static final SoundEntry SPRAY_CAN_TOOL = REGISTRATE.sound("spray_can")
            .localization(text -> text.source("Spraying")
                    .translation(MinecraftLocale.ZH_CN, "喷漆罐：喷涂"))
            .build();

    public static final SoundEntry PORTABLE_SCANNER = REGISTRATE.sound("portable_scanner")
            .localization(text -> text.source("Scanning")
                    .translation(MinecraftLocale.ZH_CN, "便携式扫描仪：扫描"))
            .build();

    public static final SoundEntry MORTAR_TOOL = REGISTRATE.sound("mortar")
            .localization(text -> text.source("Mortar crushing")
                    .translation(MinecraftLocale.ZH_CN, "研钵：研磨"))
            .build();

    public static final SoundEntry SUS_RECORD = REGISTRATE.sound("sus")
            .localization(text -> text.source("Sus...")
                    .translation(MinecraftLocale.ZH_CN, "唱片：你有嫌疑"))
            .build();

    public static final SoundEntry PORTAL_OPENING = REGISTRATE.sound("portal_opening")
            .localization(text -> text.source("Portal opens")
                    .translation(MinecraftLocale.ZH_CN, "传送门：开启"))
            .build();

    public static final SoundEntry PORTAL_CLOSING = REGISTRATE.sound("portal_closing")
            .localization(text -> text.source("Portal closes")
                    .translation(MinecraftLocale.ZH_CN, "传送门：关闭"))
            .build();

    public static final SoundEntry METAL_PIPE = REGISTRATE.sound("metal_pipe")
            .localization(text -> text.source("Destruction_Metal_Pole_L_Wave_2_0_0.wav")
                    .translation(MinecraftLocale.ZH_CN, "流体管道：Destruction_Metal_Pole_L_Wave_2_0_0.wav"))
            .build();

    public static void init() {
        GTRegistries.SOUNDS.forEach(SoundEntry::prepare);
        registerSounds();
    }

    private static void registerSounds() {
        for (SoundEntry entry : GTRegistries.SOUNDS) {
            entry.register(soundEvent -> GTRegistries.register(BuiltInRegistries.SOUND_EVENT, soundEvent.getLocation(),
                    soundEvent));
        }
    }
}
