package com.modularmc.gtpm.data.recipe;

import com.modularmc.gtpm.api.data.chemical.material.Material;
import com.modularmc.gtpm.common.registry.GTRegistration;
import com.modularmc.gtpm.utils.TagUtil;

import com.modularmc.registrate.localization.LocalizationDeclaration;
import com.modularmc.registrate.localization.MinecraftLocale;
import com.modularmc.registrate.localization.annotation.Trans;
import com.modularmc.registrate.localization.annotation.TransVariants;
import com.modularmc.registrate.localization.annotation.Translation;
import com.modularmc.registrate.localization.annotation.Variant;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.common.Tags;

import org.jetbrains.annotations.ApiStatus;

@TransVariants(keyPrefix = "tag.item.c",
               value = {
                       @Variant(id = "silicon",
                                source = "Silicon",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "硅")),
                       @Variant(id = "silicon.plates",
                                source = "Silicon Plates",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "硅板")),
                       @Variant(id = "treated_wood",
                                source = "Treated Wood",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "防腐木")),
               })
public class CustomTags {

    // Added Vanilla tags
    @Trans(key = "tag.item.c.pistons",
           source = "Pistons",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "活塞"))
    public static final TagKey<Item> PISTONS = TagUtil.createItemTag("pistons");
    // common/mod compat tags
    @Trans(key = "tag.item.c.doughs",
           source = "Doughs",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "面团"))
    public static final TagKey<Item> DOUGHS = TagUtil.createItemTag("doughs");
    @Trans(key = "tag.item.c.doughs.wheat",
           source = "Wheat Doughs",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "小麦面团"))
    public static final TagKey<Item> WHEAT_DOUGHS = TagUtil.createItemTag("doughs/wheat");
    @Trans(key = "tag.item.c.grains",
           source = "Grains",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "谷物"))
    public static final TagKey<Item> GRAINS = TagUtil.createItemTag("grains");
    @Trans(key = "tag.item.c.grains.wheat",
           source = "Wheat Grains",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "小麦谷粒"))
    public static final TagKey<Item> WHEAT_GRAINS = TagUtil.createItemTag("grains/wheat");

    // Why did I put them all here? Why not? Science isn't about why, it's about why NOT!
    // Why is so much of our science dangerous? Why don't you marry safe science if you love it so much!?
    // IN FACT, WHY NOT INVENT A SAFETY DOOR THAT WON'T HIT YOU ON THE BUTT ON THE WAY OUT BECAUSE YOU'RE FIRED!!!
    // Not you test subject, you're doing fine.
    // YES, YOU. BOX. YOUR STUFF. OUT THE FRONT DOOR. PARKING LOT. CAR. GOODBYE!
    @Trans(key = "tag.item.c.coral_blocks.alive",
           source = "Living Coral Blocks",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "活珊瑚块"))
    public static final TagKey<Item> ALIVE_CORAL_BLOCK_ITEMS = TagUtil.createItemTag("coral_blocks/alive");
    @Trans(key = "tag.item.c.coral_blocks.dead",
           source = "Dead Coral Blocks",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "失活珊瑚块"))
    public static final TagKey<Item> DEAD_CORAL_BLOCK_ITEMS = TagUtil.createItemTag("coral_blocks/dead");
    @Trans(key = "tag.item.c.coral_blocks",
           source = "Coral Blocks",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "珊瑚块"))
    public static final TagKey<Item> CORAL_BLOCK_ITEMS = TagUtil.createItemTag("coral_blocks");
    @Trans(key = "tag.item.c.coral_plants.alive",
           source = "Living Coral Plants",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "活珊瑚植株"))
    public static final TagKey<Item> ALIVE_CORAL_PLANT_ITEMS = TagUtil.createItemTag("coral_plants/alive");
    @Trans(key = "tag.item.c.coral_plants.dead",
           source = "Dead Coral Plants",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "失活珊瑚植株"))
    public static final TagKey<Item> DEAD_CORAL_PLANT_ITEMS = TagUtil.createItemTag("coral_plants/dead");
    @Trans(key = "tag.item.c.coral_plants",
           source = "Coral Plants",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "珊瑚植株"))
    public static final TagKey<Item> CORAL_PLANT_ITEMS = TagUtil.createItemTag("coral_plants");
    @Trans(key = "tag.item.c.coral_fans.alive",
           source = "Living Coral Fans",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "活珊瑚扇"))
    public static final TagKey<Item> ALIVE_CORAL_FAN_ITEMS = TagUtil.createItemTag("coral_fans/alive");
    @Trans(key = "tag.item.c.coral_fans.dead",
           source = "Dead Coral Fans",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "失活珊瑚扇"))
    public static final TagKey<Item> DEAD_CORAL_FAN_ITEMS = TagUtil.createItemTag("coral_fans/dead");
    @Trans(key = "tag.item.c.coral_fans",
           source = "Coral Fans",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "珊瑚扇"))
    public static final TagKey<Item> CORAL_FAN_ITEMS = TagUtil.createItemTag("coral_fans");
    @Trans(key = "tag.item.c.corals.alive",
           source = "Living Corals",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "活珊瑚"))
    public static final TagKey<Item> ALIVE_CORAL_ITEMS = TagUtil.createItemTag("corals/alive");
    @Trans(key = "tag.item.c.corals.dead",
           source = "Dead Corals",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "失活珊瑚"))
    public static final TagKey<Item> DEAD_CORAL_ITEMS = TagUtil.createItemTag("corals/dead");
    @Trans(key = "tag.item.c.corals",
           source = "Corals",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "珊瑚"))
    public static final TagKey<Item> CORAL_ITEMS = TagUtil.createItemTag("corals");

    // Added Gregtech tags
    @Trans(key = "tag.item.c.transistors",
           source = "Transistors",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "晶体管"))
    public static final TagKey<Item> TRANSISTORS = TagUtil.createItemTag("transistors");
    @Trans(key = "tag.item.c.resistors",
           source = "Resistors",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "电阻"))
    public static final TagKey<Item> RESISTORS = TagUtil.createItemTag("resistors");
    @Trans(key = "tag.item.c.capacitors",
           source = "Capacitors",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "电容"))
    public static final TagKey<Item> CAPACITORS = TagUtil.createItemTag("capacitors");
    @Trans(key = "tag.item.c.diodes",
           source = "Diodes",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "二极管"))
    public static final TagKey<Item> DIODES = TagUtil.createItemTag("diodes");
    @Trans(key = "tag.item.c.inductors",
           source = "Inductors",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "电感"))
    public static final TagKey<Item> INDUCTORS = TagUtil.createItemTag("inductors");

    @Trans(key = "tag.item.c.circuits",
           source = "Circuits",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "电路"))
    public static final TagKey<Item> CIRCUITS = TagUtil.createItemTag("circuits");
    @Trans(key = "tag.item.c.circuits.ulv",
           source = "ULV Circuits",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "ULV电路"))
    public static final TagKey<Item> ULV_CIRCUITS = TagUtil.createItemTag("circuits/ulv");
    @Trans(key = "tag.item.c.circuits.lv",
           source = "LV Circuits",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "LV电路"))
    public static final TagKey<Item> LV_CIRCUITS = TagUtil.createItemTag("circuits/lv");
    @Trans(key = "tag.item.c.circuits.mv",
           source = "MV Circuits",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "MV电路"))
    public static final TagKey<Item> MV_CIRCUITS = TagUtil.createItemTag("circuits/mv");
    @Trans(key = "tag.item.c.circuits.hv",
           source = "HV Circuits",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "HV电路"))
    public static final TagKey<Item> HV_CIRCUITS = TagUtil.createItemTag("circuits/hv");
    @Trans(key = "tag.item.c.circuits.ev",
           source = "EV Circuits",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "EV电路"))
    public static final TagKey<Item> EV_CIRCUITS = TagUtil.createItemTag("circuits/ev");
    @Trans(key = "tag.item.c.circuits.iv",
           source = "IV Circuits",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "IV电路"))
    public static final TagKey<Item> IV_CIRCUITS = TagUtil.createItemTag("circuits/iv");
    @Trans(key = "tag.item.c.circuits.luv",
           source = "LuV Circuits",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "LuV电路"))
    public static final TagKey<Item> LuV_CIRCUITS = TagUtil.createItemTag("circuits/luv");
    @Trans(key = "tag.item.c.circuits.zpm",
           source = "ZPM Circuits",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "ZPM电路"))
    public static final TagKey<Item> ZPM_CIRCUITS = TagUtil.createItemTag("circuits/zpm");
    @Trans(key = "tag.item.c.circuits.uv",
           source = "UV Circuits",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "UV电路"))
    public static final TagKey<Item> UV_CIRCUITS = TagUtil.createItemTag("circuits/uv");
    @Trans(key = "tag.item.c.circuits.uhv",
           source = "UHV Circuits",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "UHV电路"))
    public static final TagKey<Item> UHV_CIRCUITS = TagUtil.createItemTag("circuits/uhv");
    @Trans(key = "tag.item.c.circuits.uev",
           source = "UEV Circuits",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "UEV电路"))
    public static final TagKey<Item> UEV_CIRCUITS = TagUtil.createItemTag("circuits/uev");
    @Trans(key = "tag.item.c.circuits.uiv",
           source = "UIV Circuits",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "UIV电路"))
    public static final TagKey<Item> UIV_CIRCUITS = TagUtil.createItemTag("circuits/uiv");
    @Trans(key = "tag.item.c.circuits.uxv",
           source = "UXV Circuits",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "UXV电路"))
    public static final TagKey<Item> UXV_CIRCUITS = TagUtil.createItemTag("circuits/uxv");
    @Trans(key = "tag.item.c.circuits.opv",
           source = "OpV Circuits",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "OpV电路"))
    public static final TagKey<Item> OpV_CIRCUITS = TagUtil.createItemTag("circuits/opv");
    @Trans(key = "tag.item.c.circuits.max",
           source = "MAX Circuits",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "MAX电路"))
    public static final TagKey<Item> MAX_CIRCUITS = TagUtil.createItemTag("circuits/max");

    @SuppressWarnings("unchecked")
    public static final TagKey<Item>[] CIRCUITS_ARRAY = new TagKey[] {
            ULV_CIRCUITS,
            LV_CIRCUITS,
            MV_CIRCUITS,
            HV_CIRCUITS,
            EV_CIRCUITS,
            IV_CIRCUITS,
            LuV_CIRCUITS,
            ZPM_CIRCUITS,
            UV_CIRCUITS,
            UHV_CIRCUITS,
            UEV_CIRCUITS,
            UIV_CIRCUITS,
            UXV_CIRCUITS,
            OpV_CIRCUITS,
            MAX_CIRCUITS
    };

    @Trans(key = "tag.item.c.batteries",
           source = "Batteries",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "电池"))
    public static final TagKey<Item> BATTERIES = TagUtil.createItemTag("batteries");
    @Trans(key = "tag.item.c.batteries.ulv",
           source = "ULV Batteries",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "ULV电池"))
    public static final TagKey<Item> ULV_BATTERIES = TagUtil.createItemTag("batteries/ulv");
    @Trans(key = "tag.item.c.batteries.lv",
           source = "LV Batteries",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "LV电池"))
    public static final TagKey<Item> LV_BATTERIES = TagUtil.createItemTag("batteries/lv");
    @Trans(key = "tag.item.c.batteries.mv",
           source = "MV Batteries",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "MV电池"))
    public static final TagKey<Item> MV_BATTERIES = TagUtil.createItemTag("batteries/mv");
    @Trans(key = "tag.item.c.batteries.hv",
           source = "HV Batteries",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "HV电池"))
    public static final TagKey<Item> HV_BATTERIES = TagUtil.createItemTag("batteries/hv");
    @Trans(key = "tag.item.c.batteries.ev",
           source = "EV Batteries",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "EV电池"))
    public static final TagKey<Item> EV_BATTERIES = TagUtil.createItemTag("batteries/ev");
    @Trans(key = "tag.item.c.batteries.iv",
           source = "IV Batteries",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "IV电池"))
    public static final TagKey<Item> IV_BATTERIES = TagUtil.createItemTag("batteries/iv");
    @Trans(key = "tag.item.c.batteries.luv",
           source = "LuV Batteries",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "LuV电池"))
    public static final TagKey<Item> LuV_BATTERIES = TagUtil.createItemTag("batteries/luv");
    @Trans(key = "tag.item.c.batteries.zpm",
           source = "ZPM Batteries",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "ZPM电池"))
    public static final TagKey<Item> ZPM_BATTERIES = TagUtil.createItemTag("batteries/zpm");
    @Trans(key = "tag.item.c.batteries.uv",
           source = "UV Batteries",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "UV电池"))
    public static final TagKey<Item> UV_BATTERIES = TagUtil.createItemTag("batteries/uv");
    @Trans(key = "tag.item.c.batteries.uhv",
           source = "UHV Batteries",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "UHV电池"))
    public static final TagKey<Item> UHV_BATTERIES = TagUtil.createItemTag("batteries/uhv");

    @SuppressWarnings("unchecked")
    public static final TagKey<Item>[] BATTERIES_ARRAY = new TagKey[] {
            ULV_BATTERIES,
            LV_BATTERIES,
            MV_BATTERIES,
            HV_BATTERIES,
            EV_BATTERIES,
            IV_BATTERIES,
            LuV_BATTERIES,
            ZPM_BATTERIES,
            UV_BATTERIES,
            UHV_BATTERIES
    };

    @Trans(key = "tag.item.c.electric_motors",
           source = "Electric Motors",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "电动马达"))
    public static final TagKey<Item> ELECTRIC_MOTORS = TagUtil.createItemTag("electric_motors");
    @Trans(key = "tag.item.c.electric_pumps",
           source = "Electric Pumps",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "电动泵"))
    public static final TagKey<Item> ELECTRIC_PUMPS = TagUtil.createItemTag("electric_pumps");
    @Trans(key = "tag.item.c.fluid_regulators",
           source = "Fluid Regulators",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "流体校准器"))
    public static final TagKey<Item> FLUID_REGULATORS = TagUtil.createItemTag("fluid_regulators");
    @Trans(key = "tag.item.c.conveyor_modules",
           source = "Conveyor Modules",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "传送带"))
    public static final TagKey<Item> CONVEYOR_MODULES = TagUtil.createItemTag("conveyor_modules");
    @Trans(key = "tag.item.c.electric_pistons",
           source = "Electric Pistons",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "电力活塞"))
    public static final TagKey<Item> ELECTRIC_PISTONS = TagUtil.createItemTag("electric_pistons");
    @Trans(key = "tag.item.c.robot_arms",
           source = "Robot Arms",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "机械臂"))
    public static final TagKey<Item> ROBOT_ARMS = TagUtil.createItemTag("robot_arms");
    @Trans(key = "tag.item.c.field_generators",
           source = "Field Generators",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "力场发生器"))
    public static final TagKey<Item> FIELD_GENERATORS = TagUtil.createItemTag("field_generators");
    @Trans(key = "tag.item.c.emitters",
           source = "Emitters",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "发射器"))
    public static final TagKey<Item> EMITTERS = TagUtil.createItemTag("emitters");
    @Trans(key = "tag.item.c.sensors",
           source = "Sensors",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "传感器"))
    public static final TagKey<Item> SENSORS = TagUtil.createItemTag("sensors");

    @Trans(key = "tag.item.c.ppe_armor",
           source = "Personal Protective Equipment",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "个人防护装备"))
    public static final TagKey<Item> PPE_ARMOR = TagUtil.createItemTag("ppe_armor");
    @Trans(key = "tag.item.c.step_boots",
           source = "Step-Assist Boots",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "辅助跨步靴"))
    public static final TagKey<Item> STEP_BOOTS = TagUtil.createItemTag("step_boots");
    @Trans(key = "tag.item.c.rubber_logs",
           source = "Rubber Logs",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "橡胶原木"))
    public static final TagKey<Item> RUBBER_LOGS = TagUtil.createItemTag("rubber_logs");

    public static final TagKey<Block> CLEANROOM_DOORS = TagUtil.createModBlockTag("cleanroom_doors");

    @Trans(key = "tag.item.c.chemical_bath_washable",
           source = "Chemical Bath Washable Ores",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "可化学洗浴矿石"))
    public static final TagKey<Item> CHEM_BATH_WASHABLE = TagUtil.createItemTag("chemical_bath_washable");
    private static final LocalizationDeclaration CHEM_BATH_WASHABLE_NAME = new LocalizationDeclaration.Builder()
            .source("Ores Washable in %s")
            .translation(MinecraftLocale.ZH_CN, "可用%s洗浴的矿石")
            .build();

    @ApiStatus.Internal
    public static ResourceLocation chemicalBathTag(Material washFluid) {
        return CHEM_BATH_WASHABLE.location().withSuffix("/" + washFluid.getName());
    }

    @ApiStatus.Internal
    public static void localizeChemicalBathTag(Material washFluid) {
        var key = chemicalBathTag(washFluid).toLanguageKey("tag.item").replace('/', '.');
        GTRegistration.REGISTRATE.addDefaultLocalization(key,
                () -> washFluid.formatLocalization(CHEM_BATH_WASHABLE_NAME));
    }

    @Trans(key = "tag.item.c.skip_item_detector",
           source = "Items Ignored by Item Detectors",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "物品检测器忽略的物品"))
    public static final TagKey<Item> SKIP_ITEM_DETECTOR = TagUtil.createItemTag("skip_item_detector");

    // Mineability tags
    public static final TagKey<Block> MINEABLE_WITH_WRENCH = TagUtil.createBlockTag("mineable/wrench");
    public static final TagKey<Block> MINEABLE_WITH_WIRE_CUTTER = TagUtil.createBlockTag("mineable/wire_cutter");
    public static final TagKey<Block> MINEABLE_WITH_SAW = TagUtil.createBlockTag("mineable/saw");
    public static final TagKey<Block> MINEABLE_WITH_HAMMER = TagUtil.createBlockTag("mineable/hammer");
    public static final TagKey<Block> MINEABLE_WITH_CROWBAR = TagUtil.createBlockTag("mineable/crowbar");
    public static final TagKey<Block> MINEABLE_WITH_KNIFE = TagUtil.createBlockTag("mineable/knife");
    public static final TagKey<Block> MINEABLE_WITH_SHEARS = TagUtil.createBlockTag("mineable/shears");
    public static final TagKey<Block> MINEABLE_WITH_CONFIG_VALID_PICKAXE_WRENCH = TagUtil
            .createModBlockTag("mineable/pickaxe_or_wrench");
    public static final TagKey<Block> MINEABLE_WITH_CONFIG_VALID_PICKAXE_WIRE_CUTTER = TagUtil
            .createModBlockTag("mineable/pickaxe_or_wire_cutter");

    public static final TagKey<Block> NEEDS_DURANIUM_TOOL = TagUtil.createModBlockTag("needs_duranium_tool");
    public static final TagKey<Block> NEEDS_NEUTRONIUM_TOOL = TagUtil.createModBlockTag("needs_neutronium_tool");
    public static final TagKey<Block> INCORRECT_FOR_DURANIUM_TOOL = TagUtil
            .createModBlockTag("incorrect_for_duranium_tool");
    public static final TagKey<Block> INCORRECT_FOR_NEUTRONIUM_TOOL = TagUtil
            .createModBlockTag("incorrect_for_neutronium_tool");

    // Tool tags
    @Trans(key = "tag.item.c.tools.igniter",
           source = "Igniters",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "打火器"))
    public static final TagKey<Item> TOOLS_IGNITER = TagUtil.createItemTag("tools/igniter");
    @Trans(key = "tag.item.c.tools.butchery_knife",
           source = "Butchery Knives",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "屠刀"))
    public static final TagKey<Item> TOOLS_BUTCHERY_KNIFE = TagUtil.createItemTag("tools/butchery_knife");
    @Trans(key = "tag.item.c.tools.buzzsaw",
           source = "Buzzsaws",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "圆锯"))
    public static final TagKey<Item> TOOLS_BUZZSAW = TagUtil.createItemTag("tools/buzzsaw");
    @Trans(key = "tag.item.c.tools.chainsaw",
           source = "Chainsaws",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "链锯"))
    public static final TagKey<Item> TOOLS_CHAINSAW = TagUtil.createItemTag("tools/chainsaw");
    @Trans(key = "tag.item.c.tools.crowbar",
           source = "Crowbars",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "撬棍"))
    public static final TagKey<Item> TOOLS_CROWBAR = TagUtil.createItemTag("tools/crowbar");
    @Trans(key = "tag.item.c.tools.drill",
           source = "Drills",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "电钻"))
    public static final TagKey<Item> TOOLS_DRILL = TagUtil.createItemTag("tools/drill");
    @Trans(key = "tag.item.c.tools.file",
           source = "Files",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "锉"))
    public static final TagKey<Item> TOOLS_FILE = TagUtil.createItemTag("tools/file");
    @Trans(key = "tag.item.c.tools.hammer",
           source = "Hammers",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "锻造锤"))
    public static final TagKey<Item> TOOLS_HAMMER = TagUtil.createItemTag("tools/hammer");
    @Trans(key = "tag.item.c.tools.knife",
           source = "Knives",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "刀"))
    public static final TagKey<Item> TOOLS_KNIFE = TagUtil.createItemTag("tools/knife");
    @Trans(key = "tag.item.c.tools.mallet",
           source = "Soft Mallets",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "软锤"))
    public static final TagKey<Item> TOOLS_MALLET = TagUtil.createItemTag("tools/mallet");
    @Trans(key = "tag.item.c.tools.mining_hammer",
           source = "Mining Hammers",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "采矿锤"))
    public static final TagKey<Item> TOOLS_MINING_HAMMER = TagUtil.createItemTag("tools/mining_hammer");
    @Trans(key = "tag.item.c.tools.mortar",
           source = "Mortars",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "研钵"))
    public static final TagKey<Item> TOOLS_MORTAR = TagUtil.createItemTag("tools/mortar");
    @Trans(key = "tag.item.c.tools.plunger",
           source = "Plungers",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "搋子"))
    public static final TagKey<Item> TOOLS_PLUNGER = TagUtil.createItemTag("tools/plunger");
    @Trans(key = "tag.item.c.tools.saw",
           source = "Saws",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "锯"))
    public static final TagKey<Item> TOOLS_SAW = TagUtil.createItemTag("tools/saw");
    @Trans(key = "tag.item.c.tools.screwdriver",
           source = "Screwdrivers",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "螺丝刀"))
    public static final TagKey<Item> TOOLS_SCREWDRIVER = TagUtil.createItemTag("tools/screwdriver");
    @Trans(key = "tag.item.c.tools.scythe",
           source = "Scythes",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "镰刀"))
    public static final TagKey<Item> TOOLS_SCYTHE = TagUtil.createItemTag("tools/scythe");
    public static final TagKey<Item> TOOLS_SHEAR = Tags.Items.TOOLS_SHEAR;
    @Trans(key = "tag.item.c.tools.spade",
           source = "Spades",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "铲"))
    public static final TagKey<Item> TOOLS_SPADE = TagUtil.createItemTag("tools/spade");
    @Trans(key = "tag.item.c.tools.wire_cutter",
           source = "Wire Cutters",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "剪线钳"))
    public static final TagKey<Item> TOOLS_WIRE_CUTTER = TagUtil.createItemTag("tools/wire_cutter");
    public static final TagKey<Item> TOOLS_WRENCH = Tags.Items.TOOLS_WRENCH;

    // Crafting Tool Tags
    @Trans(key = "tag.item.c.crafting_tools.crowbar",
           source = "Crafting Crowbars",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "合成用撬棍"))
    public static final TagKey<Item> CRAFTING_CROWBARS = TagUtil.createItemTag("crafting_tools/crowbar");
    @Trans(key = "tag.item.c.crafting_tools.file",
           source = "Crafting Files",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "合成用锉"))
    public static final TagKey<Item> CRAFTING_FILES = TagUtil.createItemTag("crafting_tools/file");
    @Trans(key = "tag.item.c.crafting_tools.hammer",
           source = "Crafting Hammers",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "合成用锻造锤"))
    public static final TagKey<Item> CRAFTING_HAMMERS = TagUtil.createItemTag("crafting_tools/hammer");
    @Trans(key = "tag.item.c.crafting_tools.knife",
           source = "Crafting Knives",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "合成用刀"))
    public static final TagKey<Item> CRAFTING_KNIVES = TagUtil.createItemTag("crafting_tools/knife");
    @Trans(key = "tag.item.c.crafting_tools.mallet",
           source = "Crafting Mallets",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "合成用软锤"))
    public static final TagKey<Item> CRAFTING_MALLETS = TagUtil.createItemTag("crafting_tools/mallet");
    @Trans(key = "tag.item.c.crafting_tools.mortar",
           source = "Crafting Mortars",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "合成用研钵"))
    public static final TagKey<Item> CRAFTING_MORTARS = TagUtil.createItemTag("crafting_tools/mortar");
    @Trans(key = "tag.item.c.crafting_tools.saw",
           source = "Crafting Saws",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "合成用锯"))
    public static final TagKey<Item> CRAFTING_SAWS = TagUtil.createItemTag("crafting_tools/saw");
    @Trans(key = "tag.item.c.crafting_tools.screwdriver",
           source = "Crafting Screwdrivers",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "合成用螺丝刀"))
    public static final TagKey<Item> CRAFTING_SCREWDRIVERS = TagUtil.createItemTag("crafting_tools/screwdriver");
    @Trans(key = "tag.item.c.crafting_tools.wire_cutter",
           source = "Crafting Wire Cutters",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "合成用剪线钳"))
    public static final TagKey<Item> CRAFTING_WIRE_CUTTERS = TagUtil.createItemTag("crafting_tools/wire_cutter");
    @Trans(key = "tag.item.c.crafting_tools.wrench",
           source = "Crafting Wrenches",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "合成用扳手"))
    public static final TagKey<Item> CRAFTING_WRENCHES = TagUtil.createItemTag("crafting_tools/wrench");

    @SuppressWarnings("unchecked")
    public static final TagKey<Block>[] TOOL_TIERS = new TagKey[] {
            Tags.Blocks.NEEDS_WOOD_TOOL,
            BlockTags.NEEDS_STONE_TOOL,
            BlockTags.NEEDS_IRON_TOOL,
            BlockTags.NEEDS_DIAMOND_TOOL,
            Tags.Blocks.NEEDS_NETHERITE_TOOL,
            NEEDS_DURANIUM_TOOL,
            NEEDS_NEUTRONIUM_TOOL,
    };

    @SuppressWarnings("unchecked")
    public static final TagKey<Block>[] INCORRECT_TOOL_TIERS = new TagKey[] {
            BlockTags.INCORRECT_FOR_WOODEN_TOOL,
            BlockTags.INCORRECT_FOR_STONE_TOOL,
            BlockTags.INCORRECT_FOR_IRON_TOOL,
            BlockTags.INCORRECT_FOR_DIAMOND_TOOL,
            BlockTags.INCORRECT_FOR_NETHERITE_TOOL,
            INCORRECT_FOR_DURANIUM_TOOL,
            INCORRECT_FOR_NEUTRONIUM_TOOL,
    };

    public static final TagKey<Block> ENDSTONE_ORE_REPLACEABLES = TagUtil.createBlockTag("end_stone_ore_replaceables");

    public static final TagKey<Block> VERY_FAST_WALKABLE_BLOCKS = TagUtil.createBlockTag("very_fast_walkable_blocks");
    public static final TagKey<Block> FAST_WALKABLE_BLOCKS = TagUtil.createBlockTag("fast_walkable_blocks");
    public static final TagKey<Block> SLOW_WALKABLE_BLOCKS = TagUtil.createBlockTag("slow_walkable_blocks");

    public static final TagKey<Block> CLEANROOM_FLOORS = TagUtil.createModBlockTag("cleanroom_floors");
    public static final TagKey<Block> CHARCOAL_PILE_IGNITER_WALLS = TagUtil.createModBlockTag(
            "charcoal_pile_igniter_walls");

    public static final TagKey<Block> TALL_PLANTS = TagUtil.createModBlockTag("tall_plants");

    public static final TagKey<Biome> HAS_RUBBER_TREE = TagUtil.createModTag(Registries.BIOME, "has_rubber_tree");

    public static final TagKey<EntityType<?>> HEAT_IMMUNE = TagUtil.createModTag(Registries.ENTITY_TYPE, "heat_immune");
    public static final TagKey<EntityType<?>> CHEMICAL_IMMUNE = TagUtil.createModTag(Registries.ENTITY_TYPE,
            "chemical_immune");
    public static final TagKey<EntityType<?>> IRON_GOLEMS = TagUtil.createTag(Registries.ENTITY_TYPE, "iron_golems",
            false);
    public static final TagKey<EntityType<?>> SPIDERS = TagUtil.createTag(Registries.ENTITY_TYPE, "spiders", false);

    @Trans(key = "tag.fluid.gtpm.lighter_fluids",
           source = "Lighter Fluids",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "打火机燃料"))
    public static final TagKey<Fluid> LIGHTER_FLUIDS = TagUtil.createModFluidTag("lighter_fluids");
    public static final TagKey<Fluid> POTION_FLUIDS = TagUtil.createFluidTag("potion");

    @Trans(key = "tag.fluid.c.molten",
           source = "Molten Fluids",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "熔融流体"))
    public static final TagKey<Fluid> MOLTEN_FLUIDS = TagUtil.createFluidTag("molten");
    @Trans(key = "tag.fluid.c.liquid",
           source = "Liquid Fluids",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "液态流体"))
    public static final TagKey<Fluid> LIQUID_FLUIDS = TagUtil.createFluidTag("liquid");
    @Trans(key = "tag.fluid.c.plasmatic",
           source = "Plasma Fluids",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "等离子体流体"))
    public static final TagKey<Fluid> PLASMA_FLUIDS = TagUtil.createFluidTag("plasmatic");

    @Trans(key = "tag.fluid.gtpm.hpca_coolants",
           source = "HPCA Coolants",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "高性能计算机冷却液"))
    public static final TagKey<Fluid> HPCA_COOLANTS = TagUtil.createModFluidTag("hpca_coolants");
}
