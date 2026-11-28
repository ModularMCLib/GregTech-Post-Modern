package com.modularmc.gtpm.common.data;

import com.modularmc.gtpm.api.registry.GTRegistries;
import com.modularmc.gtpm.api.sound.SoundEntry;

import com.modularmc.registrate.localization.MinecraftLocale;
import com.modularmc.registrate.localization.annotation.Trans;
import com.modularmc.registrate.localization.annotation.TransVariants;
import com.modularmc.registrate.localization.annotation.Translation;

import net.minecraft.core.registries.BuiltInRegistries;

import static com.modularmc.gtpm.common.registry.GTRegistration.REGISTRATE;

@TransVariants(keyPrefix = "gtpm.subtitle", useFieldValues = true)
public class GTSoundEntries {

    // Machine Sounds
    @Trans(source = "Forge Hammer thumping",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "锻造锤：DUANG"))
    private static final String FORGE_HAMMER_ID = "forge_hammer";
    public static final SoundEntry FORGE_HAMMER = REGISTRATE.sound(FORGE_HAMMER_ID).build();

    @Trans(source = "Macerator crushing",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "研磨机：破碎"))
    private static final String MACERATOR_ID = "macerator";
    public static final SoundEntry MACERATOR = REGISTRATE.sound(MACERATOR_ID).build();

    @Trans(source = "Chemical bubbling",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "化学反应釜：运行中"))
    private static final String CHEMICAL_ID = "chemical";
    public static final SoundEntry CHEMICAL = REGISTRATE.sound(CHEMICAL_ID).build();

    @Trans(source = "Assembler constructing",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "组装机：运行中"))
    private static final String ASSEMBLER_ID = "assembler";
    public static final SoundEntry ASSEMBLER = REGISTRATE.sound(ASSEMBLER_ID).build();

    @Trans(source = "Centrifuge spinning",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "离心机：旋转"))
    private static final String CENTRIFUGE_ID = "centrifuge";
    public static final SoundEntry CENTRIFUGE = REGISTRATE.sound(CENTRIFUGE_ID).build();

    @Trans(source = "Compressor squeezing",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "压缩机：挤压"))
    private static final String COMPRESSOR_ID = "compressor";
    public static final SoundEntry COMPRESSOR = REGISTRATE.sound(COMPRESSOR_ID).build();

    @Trans(source = "Electrolyzer sparking",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "电解机：爆出火花"))
    private static final String ELECTROLYZER_ID = "electrolyzer";
    public static final SoundEntry ELECTROLYZER = REGISTRATE.sound(ELECTROLYZER_ID).build();

    @Trans(source = "Mixer sloshing",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "搅拌机：搅动"))
    private static final String MIXER_ID = "mixer";
    public static final SoundEntry MIXER = REGISTRATE.sound(MIXER_ID).build();

    @Trans(source = "Replicator copying",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "复制机：运行"))
    private static final String REPLICATOR_ID = "replicator";
    public static final SoundEntry REPLICATOR = REGISTRATE.sound(REPLICATOR_ID).build();

    @Trans(source = "Arcs buzzing",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "电弧炉：嗡——"))
    private static final String ARC_ID = "arc";
    public static final SoundEntry ARC = REGISTRATE.sound(ARC_ID).build();

    @Trans(source = "Boiler heating",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "锅炉：液体沸腾"))
    private static final String BOILER_ID = "boiler";
    public static final SoundEntry BOILER = REGISTRATE.sound(BOILER_ID).build();

    @Trans(source = "Furnace heating",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "电炉：冶炼"))
    private static final String FURNACE_ID = "furnace";
    public static final SoundEntry FURNACE = REGISTRATE.sound(FURNACE_ID).build();

    @Trans(source = "Freezer humming",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "真空冷冻机：嗡——"))
    private static final String COOLING_ID = "cooling";
    public static final SoundEntry COOLING = REGISTRATE.sound(COOLING_ID).build();

    @Trans(source = "Fire crackling",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "火：爆裂"))
    private static final String FIRE_ID = "fire";
    public static final SoundEntry FIRE = REGISTRATE.sound(FIRE_ID).build();

    @Trans(source = "Bath fizzing",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "化学浸洗机：嘶嘶"))
    private static final String BATH_ID = "bath";
    public static final SoundEntry BATH = REGISTRATE.sound(BATH_ID).build();

    @Trans(source = "Motor humming",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "马达：转啊转"))
    private static final String MOTOR_ID = "motor";
    public static final SoundEntry MOTOR = REGISTRATE.sound(MOTOR_ID).build();

    @Trans(source = "Cutter whirring",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "切割机：嘶嘶叫"))
    private static final String CUT_ID = "cut";
    public static final SoundEntry CUT = REGISTRATE.sound(CUT_ID).build();

    @Trans(source = "Turbine whizzing",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "涡轮：呼啸"))
    private static final String TURBINE_ID = "turbine";
    public static final SoundEntry TURBINE = REGISTRATE.sound(TURBINE_ID).build();

    @Trans(source = "Combusting",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "内燃发电机：燃料燃烧"))
    private static final String COMBUSTION_ID = "combustion";
    public static final SoundEntry COMBUSTION = REGISTRATE.sound(COMBUSTION_ID).build();

    @Trans(source = "Computer beeps",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "计算机：哔哔"))
    private static final String COMPUTATION_ID = "computation";
    public static final SoundEntry COMPUTATION = REGISTRATE.sound(COMPUTATION_ID).build();

    @Trans(source = "Miner excavating",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "采矿机：开采"))
    private static final String MINER_ID = "miner";
    public static final SoundEntry MINER = REGISTRATE.sound(MINER_ID).build();

    @Trans(source = "s c i e n c e",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "化学反应釜：科！学！"))
    private static final String SCIENCE_ID = "science";
    public static final SoundEntry SCIENCE = REGISTRATE.sound(SCIENCE_ID).build();

    @Trans(source = "Jet roaring",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "喷气背包：轰轰"))
    private static final String JET_ENGINE_ID = "jet_engine";
    public static final SoundEntry JET_ENGINE = REGISTRATE.sound(JET_ENGINE_ID).build();

    @Trans(source = "Wrench rattling",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "扳手：嘎啦"))
    private static final String WRENCH_ID = "wrench";
    public static final SoundEntry WRENCH_TOOL = REGISTRATE.sound(WRENCH_ID).build();

    @Trans(source = "Soft tap",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "软锤：轻敲"))
    private static final String SOFT_HAMMER_ID = "soft_hammer";
    public static final SoundEntry SOFT_MALLET_TOOL = REGISTRATE.sound(SOFT_HAMMER_ID).build();

    @Trans(source = "Drilling",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "电钻：嗡嗡"))
    private static final String DRILL_ID = "drill";
    public static final SoundEntry DRILL_TOOL = REGISTRATE.sound(DRILL_ID).build();

    @Trans(source = "Plunger popping",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "搋子：清理流体"))
    private static final String PLUNGER_ID = "plunger";
    public static final SoundEntry PLUNGER_TOOL = REGISTRATE.sound(PLUNGER_ID).build();

    @Trans(source = "File rasping",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "锉：磨削"))
    private static final String FILE_ID = "file";
    public static final SoundEntry FILE_TOOL = REGISTRATE.sound(FILE_ID).build();

    @Trans(source = "Sawing",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "锯：切割"))
    private static final String SAW_ID = "saw";
    public static final SoundEntry SAW_TOOL = REGISTRATE.sound(SAW_ID).build();

    @Trans(source = "Screwing",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "螺丝刀：拧螺丝"))
    private static final String SCREWDRIVER_ID = "screwdriver";
    public static final SoundEntry SCREWDRIVER_TOOL = REGISTRATE.sound(SCREWDRIVER_ID).build();

    @Trans(source = "Chainsaw revving",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "链锯：拉动油门"))
    private static final String CHAINSAW_ID = "chainsaw";
    public static final SoundEntry CHAINSAW_TOOL = REGISTRATE.sound(CHAINSAW_ID).build();

    @Trans(source = "Wire snipped",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "剪线钳：剪断"))
    private static final String WIRECUTTER_ID = "wirecutter";
    public static final SoundEntry WIRECUTTER_TOOL = REGISTRATE.sound(WIRECUTTER_ID).build();

    @Trans(source = "Spraying",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "喷漆罐：喷涂"))
    private static final String SPRAY_CAN_ID = "spray_can";
    public static final SoundEntry SPRAY_CAN_TOOL = REGISTRATE.sound(SPRAY_CAN_ID).build();

    @Trans(source = "Scanning",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "便携式扫描仪：扫描"))
    private static final String PORTABLE_SCANNER_ID = "portable_scanner";
    public static final SoundEntry PORTABLE_SCANNER = REGISTRATE.sound(PORTABLE_SCANNER_ID).build();

    @Trans(source = "Mortar crushing",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "研钵：研磨"))
    private static final String MORTAR_ID = "mortar";
    public static final SoundEntry MORTAR_TOOL = REGISTRATE.sound(MORTAR_ID).build();

    @Trans(source = "Sus...",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "唱片：你有嫌疑"))
    private static final String SUS_ID = "sus";
    public static final SoundEntry SUS_RECORD = REGISTRATE.sound(SUS_ID).build();

    @Trans(source = "Portal opens",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "传送门：开启"))
    private static final String PORTAL_OPENING_ID = "portal_opening";
    public static final SoundEntry PORTAL_OPENING = REGISTRATE.sound(PORTAL_OPENING_ID).build();

    @Trans(source = "Portal closes",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "传送门：关闭"))
    private static final String PORTAL_CLOSING_ID = "portal_closing";
    public static final SoundEntry PORTAL_CLOSING = REGISTRATE.sound(PORTAL_CLOSING_ID).build();

    @Trans(source = "Destruction_Metal_Pole_L_Wave_2_0_0.wav",
           translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                       text = "流体管道：Destruction_Metal_Pole_L_Wave_2_0_0.wav"))
    private static final String METAL_PIPE_ID = "metal_pipe";
    public static final SoundEntry METAL_PIPE = REGISTRATE.sound(METAL_PIPE_ID).build();

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
