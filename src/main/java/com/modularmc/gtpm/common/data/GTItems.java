package com.modularmc.gtpm.common.data;

import com.modularmc.gtpm.GTPM;
import com.modularmc.gtpm.api.GTCEuAPI;
import com.modularmc.gtpm.api.GTValues;
import com.modularmc.gtpm.api.cover.filter.*;
import com.modularmc.gtpm.api.data.chemical.ChemicalHelper;
import com.modularmc.gtpm.api.data.chemical.material.ItemMaterialData;
import com.modularmc.gtpm.api.data.chemical.material.MarkerMaterial;
import com.modularmc.gtpm.api.data.chemical.material.MarkerMaterials;
import com.modularmc.gtpm.api.data.chemical.material.Material;
import com.modularmc.gtpm.api.data.chemical.material.properties.PropertyKey;
import com.modularmc.gtpm.api.data.chemical.material.stack.ItemMaterialInfo;
import com.modularmc.gtpm.api.data.chemical.material.stack.MaterialEntry;
import com.modularmc.gtpm.api.data.chemical.material.stack.MaterialStack;
import com.modularmc.gtpm.api.data.tag.TagPrefix;
import com.modularmc.gtpm.api.gui.misc.ProspectorMode;
import com.modularmc.gtpm.api.item.ComponentItem;
import com.modularmc.gtpm.api.item.IComponentItem;
import com.modularmc.gtpm.api.item.TagPrefixItem;
import com.modularmc.gtpm.api.item.armor.ArmorComponentItem;
import com.modularmc.gtpm.api.item.component.*;
import com.modularmc.gtpm.common.data.materials.GTFoods;
import com.modularmc.gtpm.common.item.armor.*;
import com.modularmc.gtpm.common.item.armor.GTArmorMaterials;
import com.modularmc.gtpm.common.item.behavior.*;
import com.modularmc.gtpm.common.item.behavior.MachineConfigCopyBehaviour;
import com.modularmc.gtpm.common.item.datacomponents.DataItem;
import com.modularmc.gtpm.common.item.modules.ImageModuleBehaviour;
import com.modularmc.gtpm.common.item.modules.TextModuleBehaviour;
import com.modularmc.gtpm.config.GTPMConfigHolder;
import com.modularmc.gtpm.data.lang.LangHandler;
import com.modularmc.gtpm.data.recipe.CustomTags;
import com.modularmc.gtpm.utils.FormattingUtil;
import com.modularmc.gtpm.utils.GTUtil;
import com.modularmc.gtpm.utils.TagUtil;
import com.modularmc.gtpm.utils.memoization.GTMemoizer;

import com.modularmc.registrate.builders.ItemBuilder;
import com.modularmc.registrate.localization.MinecraftLocale;
import com.modularmc.registrate.localization.annotation.Trans;
import com.modularmc.registrate.localization.annotation.TransVariants;
import com.modularmc.registrate.localization.annotation.Translation;
import com.modularmc.registrate.localization.annotation.Variant;
import com.modularmc.registrate.providers.core.ProviderType;
import com.modularmc.registrate.util.entry.ItemEntry;
import com.modularmc.registrate.util.nullness.NonNullBiConsumer;
import com.modularmc.registrate.util.nullness.NonNullConsumer;
import com.modularmc.registrate.util.nullness.NonNullFunction;

import net.minecraft.client.color.item.ItemColor;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.renderer.item.ItemPropertyFunction;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.Stats;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.fluids.FluidUtil;
import net.neoforged.neoforge.registries.datamaps.builtin.FurnaceFuel;
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps;

import com.google.common.base.Preconditions;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Supplier;

import static com.modularmc.gtpm.common.data.GTCreativeModeTabs.ITEM;
import static com.modularmc.gtpm.common.data.GTCreativeModeTabs.TOOL;
import static com.modularmc.gtpm.common.data.models.GTModels.createTextureModel;
import static com.modularmc.gtpm.common.data.models.GTModels.overrideModel;
import static com.modularmc.gtpm.common.registry.GTRegistration.REGISTRATE;
import static com.modularmc.gtpm.utils.FormattingUtil.toEnglishName;

@TransVariants(keyPrefix = "item.gtpm",
               value = {
                       @Variant(id = "axe_extruder_mold.tooltip",
                                source = "§7Extruder Shape for making Axes",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7用来制作斧的模头")),
                       @Variant(id = "bottle.purple.drink.tooltip",
                                source = "§7How about Lemonade. Or some Ice Tea? I got Purple Drink!",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "§7来点柠檬汽水，还是冰红茶？我手里这瓶可是紫色饮料！")),
                       @Variant(id = "casing_casting_mold.tooltip",
                                source = "§7Mold for making Item Casings",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7用来制作外壳的模具")),
                       @Variant(id = "casing_extruder_mold.tooltip",
                                source = "§7Extruder Shape for making Item Casings",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7用来制作外壳的模头")),
                       @Variant(id = "circuit.integrated.gui",
                                source = "§7Programmed Circuit Configuration",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7编程电路配置")),
                       @Variant(id = "file_extruder_mold.tooltip",
                                source = "§7Extruder Shape for making Files",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7用来制作锉的模头")),
                       @Variant(id = "foam_sprayer.tooltip.0",
                                source = "§7Sprays Construction Foam",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7喷射建筑泡沫")),
                       @Variant(id = "foam_sprayer.tooltip.1",
                                source = "Use on a frame to foam connected frames",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "右击框架来给框架喷涂泡沫")),
                       @Variant(id = "foam_sprayer.tooltip.2",
                                source = "Foam can be colored",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "泡沫可以染色")),
                       @Variant(id = "hammer_extruder_mold.tooltip",
                                source = "§7Extruder Shape for making Hammers",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7用来制作锤的模头")),
                       @Variant(id = "hoe_extruder_mold.tooltip",
                                source = "§7Extruder Shape for making Hoes",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7用来制作锄的模头")),
                       @Variant(id = "long_rod_extruder_mold.tooltip",
                                source = "§7Extruder Shape for making Long Rods",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7用来制作长杆的模头")),
                       @Variant(id = "machine_controller.tooltip",
                                source = "§7Turns Machines §fON/OFF§7 as §fCover§7.",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "§7作§f覆盖板§7时可以§f开/关§7机器。")),
                       @Variant(id = "max_voltage_coil.tooltip",
                                source = "Maximum Coil",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "至高线圈")),
                       @Variant(id = "opv_voltage_coil.tooltip",
                                source = "Legendary Coil",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "传奇线圈")),
                       @Variant(id = "pickaxe_extruder_mold.tooltip",
                                source = "§7Extruder Shape for making Pickaxes",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7用来制作镐的模头")),
                       @Variant(id = "saw_extruder_mold.tooltip",
                                source = "§7Extruder Shape for making Saws",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7用来制作锯的模头")),
                       @Variant(id = "shovel_extruder_mold.tooltip",
                                source = "§7Extruder Shape for making Shovels",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7用来制作锹的模头")),
                       @Variant(id = "sword_extruder_mold.tooltip",
                                source = "§7Extruder Shape for making Swords",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7用来制作剑刃的模头")),
                       @Variant(id = "uev_voltage_coil.tooltip",
                                source = "Unreal Coil",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "超凡线圈")),
                       @Variant(id = "uhv_voltage_coil.tooltip",
                                source = "Ultra Coil",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "极致线圈")),
                       @Variant(id = "uiv_voltage_coil.tooltip",
                                source = "Insane Coil",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "疯狂线圈")),
                       @Variant(id = "ulv_tantalum_battery.tooltip",
                                source = "§7Reusable Battery",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7可充电电池")),
                       @Variant(id = "uxv_voltage_coil.tooltip",
                                source = "Epic Coil",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "史诗线圈")),
                       @Variant(id = "conveyor.module.tooltip",
                                source = "§7Transfers §fItems§7 at specific rates as §fCover§7.",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "§7作§f覆盖板§7时以特定速率传输§f物品§7。")),
                       @Variant(id = "electric.pump.tooltip",
                                source = "§7Transfers §fFluids§7 at specific rates as §fCover§7.",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "§7作§f覆盖板§7时以特定速率传输§f流体§7。")),
                       @Variant(id = "fluid.regulator.tooltip",
                                source = "§7Limits §fFluids§7 to specific quantities as §fCover§7.",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "§7作§f覆盖板§7时§7限制§f§f流体§7的流量。")),
                       @Variant(id = "robot.arm.tooltip",
                                source = "§7Limits §fItems§7 to specific quantities as §fCover§7.",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "作§f覆盖板§7时限制§f物品§7到特定数量。")),
                       @Variant(id = "solar_panel.tooltip.0",
                                source = "§7May the Sun be with you.",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7愿太阳与你同在。")),
                       @Variant(id = "solar_panel.tooltip.1",
                                source = "Produces §fEnergy§7 from the §eSun§7 as §fCover§7.",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "§7作§f覆盖板§7时利用§e太阳§7生产§f能量§7。")),
               })
@Trans(key = "gtpm.fluid.empty",
       source = "Empty",
       translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "空"))
public class GTItems {

    //////////////////////////////////////
    // ******* Misc Items ********//
    //////////////////////////////////////
    static {
        REGISTRATE.creativeModeTab(ITEM);
    }
    public static ItemEntry<Item> COMPRESSED_CLAY = REGISTRATE.item("compressed_clay", Item::new)
            .localization(text -> text
                    .source("Compressed Clay")
                    .translation(MinecraftLocale.ZH_CN, "压缩黏土"))
            .onRegister(materialInfo(new ItemMaterialInfo(new MaterialStack(GTMaterials.Clay, GTValues.M)))).register();
    public static ItemEntry<Item> COMPRESSED_COKE_CLAY = REGISTRATE.item("compressed_coke_clay", Item::new)
            .localization(text -> text
                    .source("Compressed Coke Clay")
                    .translation(MinecraftLocale.ZH_CN, "压缩焦黏土"))
            .onRegister(materialInfo(new ItemMaterialInfo(new MaterialStack(GTMaterials.Clay, GTValues.M)))).register();
    public static ItemEntry<Item> COMPRESSED_FIRECLAY = REGISTRATE.item("compressed_fireclay", Item::new)
            .localization(text -> text
                    .source("Compressed Fireclay")
                    .translation(MinecraftLocale.ZH_CN, "压缩耐火黏土"))
            .onRegister(materialInfo(new ItemMaterialInfo(new MaterialStack(GTMaterials.Fireclay, GTValues.M))))
            .register();
    @Trans(key = "item.gtpm.firebrick.tooltip",
           source = "§7Heat resistant",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7耐热"))
    public static ItemEntry<Item> FIRECLAY_BRICK = REGISTRATE.item("firebrick", Item::new)
            .localization(text -> text
                    .source("Firebrick")
                    .translation(MinecraftLocale.ZH_CN, "耐火砖"))
            .onRegister(materialInfo(new ItemMaterialInfo(new MaterialStack(GTMaterials.Fireclay, GTValues.M))))
            .register();
    public static ItemEntry<Item> COKE_OVEN_BRICK = REGISTRATE.item("coke_oven_brick", Item::new)
            .localization(text -> text
                    .source("Coke Oven Brick")
                    .translation(MinecraftLocale.ZH_CN, "焦炉砖"))
            .onRegister(materialInfo(new ItemMaterialInfo(new MaterialStack(GTMaterials.Clay, GTValues.M)))).register();
    public static ItemEntry<Item> WOODEN_FORM_EMPTY = REGISTRATE.item("empty_wooden_form", Item::new)
            .localization(text -> text
                    .source("Empty Wooden Form")
                    .translation(MinecraftLocale.ZH_CN, "空木制模具"))
            .register();
    public static ItemEntry<ComponentItem> WOODEN_FORM_BRICK = REGISTRATE
            .item("brick_wooden_form", ComponentItem::new)
            .localization(text -> text
                    .source("Brick Wooden Form")
                    .translation(MinecraftLocale.ZH_CN, "木制砖模具"))
            .properties(p -> p.stacksTo(1))
            .onRegister(attach((IRecipeRemainder) ItemStack::copy)).register();

    @Trans(key = "item.gtpm.empty_mold.tooltip",
           source = "§7Raw Plate to make Molds and Extrude Shapes",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7用以制作模具与压模器的模头"))
    public static ItemEntry<Item> SHAPE_EMPTY = REGISTRATE.item("empty_mold", Item::new)
            .localization(text -> text
                    .source("Empty Mold")
                    .translation(MinecraftLocale.ZH_CN, "空模板"))
            .onRegister(materialInfo(new ItemMaterialInfo(new MaterialStack(GTMaterials.Steel, GTValues.M * 4))))
            .register();

    @SuppressWarnings("unchecked")
    public static final ItemEntry<Item>[] SHAPE_MOLDS = new ItemEntry[18];
    @Trans(key = "item.gtpm.plate_casting_mold.tooltip",
           source = "§7Mold for making Plates",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7用来制作板的模具"))
    public static final ItemEntry<Item> SHAPE_MOLD_PLATE;
    @Trans(key = "item.gtpm.gear_casting_mold.tooltip",
           source = "§7Mold for making Gears",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7用来制作齿轮的模具"))
    public static final ItemEntry<Item> SHAPE_MOLD_GEAR;
    @Trans(key = "item.gtpm.bottle_casting_mold.tooltip",
           source = "§7Mold for making Bottles",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7用来制作瓶的模具"))
    public static final ItemEntry<Item> SHAPE_MOLD_BOTTLE;
    @Trans(key = "item.gtpm.ingot_casting_mold.tooltip",
           source = "§7Mold for making Ingots",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7用来制作锭的模具"))
    public static final ItemEntry<Item> SHAPE_MOLD_INGOT;
    @Trans(key = "item.gtpm.ball_casting_mold.tooltip",
           source = "§7Mold for making Balls",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7用来制作球的模具"))
    public static final ItemEntry<Item> SHAPE_MOLD_BALL;
    @Trans(key = "item.gtpm.block_casting_mold.tooltip",
           source = "§7Mold for making Blocks",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7用来制作块的模具"))
    public static final ItemEntry<Item> SHAPE_MOLD_BLOCK;
    @Trans(key = "item.gtpm.nugget_casting_mold.tooltip",
           source = "§7Mold for making Nuggets",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7用来制作粒的模具"))
    public static final ItemEntry<Item> SHAPE_MOLD_NUGGET;
    @Trans(key = "item.gtpm.cylinder_casting_mold.tooltip",
           source = "§7Mold for shaping Cylinders",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7用来制作圆柱的模具"))
    public static final ItemEntry<Item> SHAPE_MOLD_CYLINDER;
    @Trans(key = "item.gtpm.anvil_casting_mold.tooltip",
           source = "§7Mold for shaping Anvils",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7用来制作砧的模具"))
    public static final ItemEntry<Item> SHAPE_MOLD_ANVIL;
    @Trans(key = "item.gtpm.name_casting_mold.tooltip",
           source = "§7Mold for naming Items in the Forming Press (rename Mold with Anvil)",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7用来在冲压机床中为物品命名的模具（用铁砧重命名模具）"))
    public static final ItemEntry<Item> SHAPE_MOLD_NAME;
    @Trans(key = "item.gtpm.small_gear_casting_mold.tooltip",
           source = "§7Mold for making small Gears",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7用来制作小型齿轮的模具"))
    public static final ItemEntry<Item> SHAPE_MOLD_GEAR_SMALL;
    @Trans(key = "item.gtpm.rotor_casting_mold.tooltip",
           source = "§7Mold for making Rotors",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7用来制作转子的模具"))
    public static final ItemEntry<Item> SHAPE_MOLD_ROTOR;
    public static final ItemEntry<Item> SHAPE_MOLD_PILL;
    @Trans(key = "item.gtpm.tiny_pipe_casting_mold.tooltip",
           source = "§7Mold for making tiny Pipes",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7用来制作微型管道的模具"))
    public static final ItemEntry<Item> SHAPE_MOLD_PIPE_TINY;
    @Trans(key = "item.gtpm.small_pipe_casting_mold.tooltip",
           source = "§7Mold for making small Pipes",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7用来制作小型管道的模具"))
    public static final ItemEntry<Item> SHAPE_MOLD_PIPE_SMALL;
    @Trans(key = "item.gtpm.normal_pipe_casting_mold.tooltip",
           source = "§7Mold for making Pipes",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7用来制作管道的模具"))
    public static final ItemEntry<Item> SHAPE_MOLD_PIPE_NORMAL;
    @Trans(key = "item.gtpm.large_pipe_casting_mold.tooltip",
           source = "§7Mold for making large Pipes",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7用来制作大型管道的模具"))
    public static final ItemEntry<Item> SHAPE_MOLD_PIPE_LARGE;
    @Trans(key = "item.gtpm.huge_pipe_casting_mold.tooltip",
           source = "§7Mold for making full Block Pipes",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7用来制作巨型管道的模具"))
    public static final ItemEntry<Item> SHAPE_MOLD_PIPE_HUGE;

    static {
        SHAPE_MOLDS[0] = SHAPE_MOLD_PLATE = REGISTRATE.item("plate_casting_mold", Item::new)
                .localization(text -> text
                        .source("Casting Mold (Plate)")
                        .translation(MinecraftLocale.ZH_CN, "模具（板）"))
                .onRegister(materialInfo(new ItemMaterialInfo(new MaterialStack(GTMaterials.Steel, GTValues.M * 4))))
                .register();
        SHAPE_MOLDS[1] = SHAPE_MOLD_GEAR = REGISTRATE.item("gear_casting_mold", Item::new)
                .localization(text -> text
                        .source("Casting Mold (Gear)")
                        .translation(MinecraftLocale.ZH_CN, "模具（齿轮）"))
                .onRegister(materialInfo(new ItemMaterialInfo(new MaterialStack(GTMaterials.Steel, GTValues.M * 4))))
                .register();
        SHAPE_MOLDS[2] = SHAPE_MOLD_BOTTLE = REGISTRATE.item("bottle_casting_mold", Item::new)
                .localization(text -> text
                        .source("Casting Mold (Bottle)")
                        .translation(MinecraftLocale.ZH_CN, "模具（瓶子）"))
                .onRegister(materialInfo(new ItemMaterialInfo(new MaterialStack(GTMaterials.Steel, GTValues.M * 4))))
                .register();
        SHAPE_MOLDS[3] = SHAPE_MOLD_INGOT = REGISTRATE.item("ingot_casting_mold", Item::new)
                .localization(text -> text
                        .source("Casting Mold (Ingot)")
                        .translation(MinecraftLocale.ZH_CN, "模具（锭）"))
                .onRegister(materialInfo(new ItemMaterialInfo(new MaterialStack(GTMaterials.Steel, GTValues.M * 4))))
                .register();
        SHAPE_MOLDS[4] = SHAPE_MOLD_BALL = REGISTRATE.item("ball_casting_mold", Item::new)
                .localization(text -> text
                        .source("Casting Mold (Ball)")
                        .translation(MinecraftLocale.ZH_CN, "模具（球）"))
                .onRegister(materialInfo(new ItemMaterialInfo(new MaterialStack(GTMaterials.Steel, GTValues.M * 4))))
                .register();
        SHAPE_MOLDS[5] = SHAPE_MOLD_BLOCK = REGISTRATE.item("block_casting_mold", Item::new)
                .localization(text -> text
                        .source("Casting Mold (Block)")
                        .translation(MinecraftLocale.ZH_CN, "模具（块）"))
                .onRegister(materialInfo(new ItemMaterialInfo(new MaterialStack(GTMaterials.Steel, GTValues.M * 4))))
                .register();
        SHAPE_MOLDS[6] = SHAPE_MOLD_NUGGET = REGISTRATE.item("nugget_casting_mold", Item::new)
                .localization(text -> text
                        .source("Casting Mold (Nugget)")
                        .translation(MinecraftLocale.ZH_CN, "模具（粒）"))
                .onRegister(materialInfo(new ItemMaterialInfo(new MaterialStack(GTMaterials.Steel, GTValues.M * 4))))
                .register();
        SHAPE_MOLDS[7] = SHAPE_MOLD_CYLINDER = REGISTRATE.item("cylinder_casting_mold", Item::new)
                .localization(text -> text
                        .source("Casting Mold (Cylinder)")
                        .translation(MinecraftLocale.ZH_CN, "模具（圆柱）"))
                .onRegister(materialInfo(new ItemMaterialInfo(new MaterialStack(GTMaterials.Steel, GTValues.M * 4))))
                .register();
        SHAPE_MOLDS[8] = SHAPE_MOLD_ANVIL = REGISTRATE.item("anvil_casting_mold", Item::new)
                .localization(text -> text
                        .source("Casting Mold (Anvil)")
                        .translation(MinecraftLocale.ZH_CN, "模具（砧）"))
                .onRegister(materialInfo(new ItemMaterialInfo(new MaterialStack(GTMaterials.Steel, GTValues.M * 4))))
                .register();
        SHAPE_MOLDS[9] = SHAPE_MOLD_NAME = REGISTRATE.item("name_casting_mold", Item::new)
                .localization(text -> text
                        .source("Casting Mold (Name)")
                        .translation(MinecraftLocale.ZH_CN, "模具（命名）"))
                .onRegister(materialInfo(new ItemMaterialInfo(new MaterialStack(GTMaterials.Steel, GTValues.M * 4))))
                .register();
        SHAPE_MOLDS[10] = SHAPE_MOLD_GEAR_SMALL = REGISTRATE.item("small_gear_casting_mold", Item::new)
                .localization(text -> text
                        .source("Casting Mold (Small Gear)")
                        .translation(MinecraftLocale.ZH_CN, "模具（小型齿轮）"))
                .onRegister(materialInfo(new ItemMaterialInfo(new MaterialStack(GTMaterials.Steel, GTValues.M * 4))))
                .register();
        SHAPE_MOLDS[11] = SHAPE_MOLD_ROTOR = REGISTRATE.item("rotor_casting_mold", Item::new)
                .localization(text -> text
                        .source("Casting Mold (Rotor)")
                        .translation(MinecraftLocale.ZH_CN, "模具（转子）"))
                .onRegister(materialInfo(new ItemMaterialInfo(new MaterialStack(GTMaterials.Steel, GTValues.M * 4))))
                .register();
        SHAPE_MOLDS[12] = SHAPE_MOLD_PILL = REGISTRATE.item("pill_casting_mold", Item::new)
                .localization(text -> text
                        .source("Casting Mold (Pill)")
                        .translation(MinecraftLocale.ZH_CN, "模具（药片）"))
                .onRegister(materialInfo(new ItemMaterialInfo(new MaterialStack(GTMaterials.Steel, GTValues.M * 4))))
                .register();
        SHAPE_MOLDS[13] = SHAPE_MOLD_PIPE_TINY = REGISTRATE.item("tiny_pipe_casting_mold", Item::new)
                .localization(text -> text
                        .source("Casting Mold (Tiny Pipe)")
                        .translation(MinecraftLocale.ZH_CN, "模具（微型管道）"))
                .onRegister(materialInfo(new ItemMaterialInfo(new MaterialStack(GTMaterials.Steel, GTValues.M * 4))))
                .register();
        SHAPE_MOLDS[14] = SHAPE_MOLD_PIPE_SMALL = REGISTRATE.item("small_pipe_casting_mold", Item::new)
                .localization(text -> text
                        .source("Casting Mold (Small Pipe)")
                        .translation(MinecraftLocale.ZH_CN, "模具（小型管道）"))
                .onRegister(materialInfo(new ItemMaterialInfo(new MaterialStack(GTMaterials.Steel, GTValues.M * 4))))
                .register();
        SHAPE_MOLDS[15] = SHAPE_MOLD_PIPE_NORMAL = REGISTRATE.item("normal_pipe_casting_mold", Item::new)
                .localization(text -> text
                        .source("Casting Mold (Normal Pipe)")
                        .translation(MinecraftLocale.ZH_CN, "模具（标准管道）"))
                .onRegister(materialInfo(new ItemMaterialInfo(new MaterialStack(GTMaterials.Steel, GTValues.M * 4))))
                .register();
        SHAPE_MOLDS[16] = SHAPE_MOLD_PIPE_LARGE = REGISTRATE.item("large_pipe_casting_mold", Item::new)
                .localization(text -> text
                        .source("Casting Mold (Large Pipe)")
                        .translation(MinecraftLocale.ZH_CN, "模具（大型管道）"))
                .onRegister(materialInfo(new ItemMaterialInfo(new MaterialStack(GTMaterials.Steel, GTValues.M * 4))))
                .register();
        SHAPE_MOLDS[17] = SHAPE_MOLD_PIPE_HUGE = REGISTRATE.item("huge_pipe_casting_mold", Item::new)
                .localization(text -> text
                        .source("Casting Mold (Huge Pipe)")
                        .translation(MinecraftLocale.ZH_CN, "模具（巨型管道）"))
                .onRegister(materialInfo(new ItemMaterialInfo(new MaterialStack(GTMaterials.Steel, GTValues.M * 4))))
                .register();
    }

    @SuppressWarnings("unchecked")
    public static final ItemEntry<Item>[] SHAPE_EXTRUDERS = new ItemEntry[27];
    @Trans(key = "item.gtpm.plate_extruder_mold.tooltip",
           source = "§7Extruder Shape for making Plates",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7用来制作板的模头"))
    public static ItemEntry<Item> SHAPE_EXTRUDER_PLATE;
    @Trans(key = "item.gtpm.rod_extruder_mold.tooltip",
           source = "§7Extruder Shape for making Rods",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7用来制作杆的模头"))
    public static ItemEntry<Item> SHAPE_EXTRUDER_ROD;
    @Trans(key = "item.gtpm.bolt_extruder_mold.tooltip",
           source = "§7Extruder Shape for making Bolts",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7用来制作螺栓的模头"))
    public static ItemEntry<Item> SHAPE_EXTRUDER_BOLT;
    @Trans(key = "item.gtpm.ring_extruder_mold.tooltip",
           source = "§7Extruder Shape for making Rings",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7用来制作环的模头"))
    public static ItemEntry<Item> SHAPE_EXTRUDER_RING;
    @Trans(key = "item.gtpm.cell_extruder_mold.tooltip",
           source = "§7Extruder Shape for making Cells",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7用来制作单元的模头"))
    public static ItemEntry<Item> SHAPE_EXTRUDER_CELL;
    @Trans(key = "item.gtpm.ingot_extruder_mold.tooltip",
           source = "§7Extruder Shape for, wait, can't we just use a Furnace?",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7用来……等会，用熔炉不好吗？"))
    public static ItemEntry<Item> SHAPE_EXTRUDER_INGOT;
    @Trans(key = "item.gtpm.wire_extruder_mold.tooltip",
           source = "§7Extruder Shape for making Wires",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7用来制作导线的模头"))
    public static ItemEntry<Item> SHAPE_EXTRUDER_WIRE;
    @Trans(key = "item.gtpm.tiny_pipe_extruder_mold.tooltip",
           source = "§7Extruder Shape for making tiny Pipes",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7用来制作微型管道的模头"))
    public static ItemEntry<Item> SHAPE_EXTRUDER_PIPE_TINY;
    @Trans(key = "item.gtpm.small_pipe_extruder_mold.tooltip",
           source = "§7Extruder Shape for making small Pipes",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7用来制作小型管道的模头"))
    public static ItemEntry<Item> SHAPE_EXTRUDER_PIPE_SMALL;
    @Trans(key = "item.gtpm.normal_pipe_extruder_mold.tooltip",
           source = "§7Extruder Shape for making Pipes",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7用来制作管道的模头"))
    public static ItemEntry<Item> SHAPE_EXTRUDER_PIPE_NORMAL;
    @Trans(key = "item.gtpm.large_pipe_extruder_mold.tooltip",
           source = "§7Extruder Shape for making large Pipes",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7用来制作大型管道的模头"))
    public static ItemEntry<Item> SHAPE_EXTRUDER_PIPE_LARGE;
    @Trans(key = "item.gtpm.huge_pipe_extruder_mold.tooltip",
           source = "§7Extruder Shape for making full Block Pipes",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7用来制作巨型管道的模头"))
    public static ItemEntry<Item> SHAPE_EXTRUDER_PIPE_HUGE;
    @Trans(key = "item.gtpm.block_extruder_mold.tooltip",
           source = "§7Extruder Shape for making Blocks",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7用来制作块的模头"))
    public static ItemEntry<Item> SHAPE_EXTRUDER_BLOCK;
    @Trans(key = "item.gtpm.gear_extruder_mold.tooltip",
           source = "§7Extruder Shape for making Gears",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7用来制作齿轮的模头"))
    public static ItemEntry<Item> SHAPE_EXTRUDER_GEAR;
    @Trans(key = "item.gtpm.bottle_extruder_mold.tooltip",
           source = "§7Extruder Shape for making Bottles",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7用来制作瓶子的模头"))
    public static ItemEntry<Item> SHAPE_EXTRUDER_BOTTLE;
    @Trans(key = "item.gtpm.foil_extruder_mold.tooltip",
           source = "§7Extruder Shape for making Foils from Non-Metals",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7用来制作箔的模头"))
    public static ItemEntry<Item> SHAPE_EXTRUDER_FOIL;
    @Trans(key = "item.gtpm.small_gear_extruder_mold.tooltip",
           source = "§7Extruder Shape for making Small Gears",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7用来制作小型齿轮的模头"))
    public static ItemEntry<Item> SHAPE_EXTRUDER_GEAR_SMALL;
    @Trans(key = "item.gtpm.rotor_extruder_mold.tooltip",
           source = "§7Extruder Shape for making Rotors",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7用来制作转子的模头"))
    public static ItemEntry<Item> SHAPE_EXTRUDER_ROTOR;

    static {
        SHAPE_EXTRUDERS[0] = SHAPE_EXTRUDER_PLATE = REGISTRATE.item("plate_extruder_mold", Item::new)
                .localization(text -> text
                        .source("Extruder Mold (Plate)")
                        .translation(MinecraftLocale.ZH_CN, "模头（板）"))
                .onRegister(materialInfo(new ItemMaterialInfo(new MaterialStack(GTMaterials.Steel, GTValues.M * 4))))
                .register();
        SHAPE_EXTRUDERS[1] = SHAPE_EXTRUDER_ROD = REGISTRATE.item("rod_extruder_mold", Item::new)
                .localization(text -> text
                        .source("Extruder Mold (Rod)")
                        .translation(MinecraftLocale.ZH_CN, "模头（杆）"))
                .onRegister(materialInfo(new ItemMaterialInfo(new MaterialStack(GTMaterials.Steel, GTValues.M * 4))))
                .register();
        SHAPE_EXTRUDERS[2] = SHAPE_EXTRUDER_BOLT = REGISTRATE.item("bolt_extruder_mold", Item::new)
                .localization(text -> text
                        .source("Extruder Mold (Bolt)")
                        .translation(MinecraftLocale.ZH_CN, "模头（螺栓）"))
                .onRegister(materialInfo(new ItemMaterialInfo(new MaterialStack(GTMaterials.Steel, GTValues.M * 4))))
                .register();
        SHAPE_EXTRUDERS[3] = SHAPE_EXTRUDER_RING = REGISTRATE.item("ring_extruder_mold", Item::new)
                .localization(text -> text
                        .source("Extruder Mold (Ring)")
                        .translation(MinecraftLocale.ZH_CN, "模头（环）"))
                .onRegister(materialInfo(new ItemMaterialInfo(new MaterialStack(GTMaterials.Steel, GTValues.M * 4))))
                .register();
        SHAPE_EXTRUDERS[4] = SHAPE_EXTRUDER_CELL = REGISTRATE.item("cell_extruder_mold", Item::new)
                .localization(text -> text
                        .source("Extruder Mold (Cell)")
                        .translation(MinecraftLocale.ZH_CN, "模头（单元）"))
                .onRegister(materialInfo(new ItemMaterialInfo(new MaterialStack(GTMaterials.Steel, GTValues.M * 4))))
                .register();
        SHAPE_EXTRUDERS[5] = SHAPE_EXTRUDER_INGOT = REGISTRATE.item("ingot_extruder_mold", Item::new)
                .localization(text -> text
                        .source("Extruder Mold (Ingot)")
                        .translation(MinecraftLocale.ZH_CN, "模头（锭）"))
                .onRegister(materialInfo(new ItemMaterialInfo(new MaterialStack(GTMaterials.Steel, GTValues.M * 4))))
                .register();
        SHAPE_EXTRUDERS[6] = SHAPE_EXTRUDER_WIRE = REGISTRATE.item("wire_extruder_mold", Item::new)
                .localization(text -> text
                        .source("Extruder Mold (Wire)")
                        .translation(MinecraftLocale.ZH_CN, "模头（导线）"))
                .onRegister(materialInfo(new ItemMaterialInfo(new MaterialStack(GTMaterials.Steel, GTValues.M * 4))))
                .register();
        SHAPE_EXTRUDERS[7] = SHAPE_EXTRUDER_PIPE_TINY = REGISTRATE.item("tiny_pipe_extruder_mold", Item::new)
                .localization(text -> text
                        .source("Extruder Mold (Tiny Pipe)")
                        .translation(MinecraftLocale.ZH_CN, "模头（微型管道）"))
                .onRegister(materialInfo(new ItemMaterialInfo(new MaterialStack(GTMaterials.Steel, GTValues.M * 4))))
                .register();
        SHAPE_EXTRUDERS[8] = SHAPE_EXTRUDER_PIPE_SMALL = REGISTRATE.item("small_pipe_extruder_mold", Item::new)
                .localization(text -> text
                        .source("Extruder Mold (Small Pipe)")
                        .translation(MinecraftLocale.ZH_CN, "模头（小型管道）"))
                .onRegister(materialInfo(new ItemMaterialInfo(new MaterialStack(GTMaterials.Steel, GTValues.M * 4))))
                .register();
        SHAPE_EXTRUDERS[9] = SHAPE_EXTRUDER_PIPE_NORMAL = REGISTRATE.item("normal_pipe_extruder_mold", Item::new)
                .localization(text -> text
                        .source("Extruder Mold (Normal Pipe)")
                        .translation(MinecraftLocale.ZH_CN, "模头（标准管道）"))
                .onRegister(materialInfo(new ItemMaterialInfo(new MaterialStack(GTMaterials.Steel, GTValues.M * 4))))
                .register();
        SHAPE_EXTRUDERS[10] = SHAPE_EXTRUDER_PIPE_LARGE = REGISTRATE.item("large_pipe_extruder_mold", Item::new)
                .localization(text -> text
                        .source("Extruder Mold (Large Pipe)")
                        .translation(MinecraftLocale.ZH_CN, "模头（大型管道）"))
                .onRegister(materialInfo(new ItemMaterialInfo(new MaterialStack(GTMaterials.Steel, GTValues.M * 4))))
                .register();
        SHAPE_EXTRUDERS[11] = SHAPE_EXTRUDER_PIPE_HUGE = REGISTRATE.item("huge_pipe_extruder_mold", Item::new)
                .localization(text -> text
                        .source("Extruder Mold (Huge Pipe)")
                        .translation(MinecraftLocale.ZH_CN, "模头（巨型管道）"))
                .onRegister(materialInfo(new ItemMaterialInfo(new MaterialStack(GTMaterials.Steel, GTValues.M * 4))))
                .register();
        SHAPE_EXTRUDERS[12] = SHAPE_EXTRUDER_BLOCK = REGISTRATE.item("block_extruder_mold", Item::new)
                .localization(text -> text
                        .source("Extruder Mold (Block)")
                        .translation(MinecraftLocale.ZH_CN, "模头（块）"))
                .onRegister(materialInfo(new ItemMaterialInfo(new MaterialStack(GTMaterials.Steel, GTValues.M * 4))))
                .register();
        // Extruder Shapes index 13-20 (inclusive), id 44-51 (inclusive) are unused
        SHAPE_EXTRUDERS[21] = SHAPE_EXTRUDER_GEAR = REGISTRATE.item("gear_extruder_mold", Item::new)
                .localization(text -> text
                        .source("Extruder Mold (Gear)")
                        .translation(MinecraftLocale.ZH_CN, "模头（齿轮）"))
                .onRegister(materialInfo(new ItemMaterialInfo(new MaterialStack(GTMaterials.Steel, GTValues.M * 4))))
                .register();
        SHAPE_EXTRUDERS[22] = SHAPE_EXTRUDER_BOTTLE = REGISTRATE.item("bottle_extruder_mold", Item::new)
                .localization(text -> text
                        .source("Extruder Mold (Bottle)")
                        .translation(MinecraftLocale.ZH_CN, "模头（瓶子）"))
                .onRegister(materialInfo(new ItemMaterialInfo(new MaterialStack(GTMaterials.Steel, GTValues.M * 4))))
                .register();
        SHAPE_EXTRUDERS[23] = SHAPE_EXTRUDER_FOIL = REGISTRATE.item("foil_extruder_mold", Item::new)
                .localization(text -> text
                        .source("Extruder Mold (Foil)")
                        .translation(MinecraftLocale.ZH_CN, "模头（箔）"))
                .onRegister(materialInfo(new ItemMaterialInfo(new MaterialStack(GTMaterials.Steel, GTValues.M * 4))))
                .register();
        SHAPE_EXTRUDERS[24] = SHAPE_EXTRUDER_GEAR_SMALL = REGISTRATE.item("small_gear_extruder_mold", Item::new)
                .localization(text -> text
                        .source("Extruder Mold (Small Gear)")
                        .translation(MinecraftLocale.ZH_CN, "模头（小型齿轮）"))
                .onRegister(materialInfo(new ItemMaterialInfo(new MaterialStack(GTMaterials.Steel, GTValues.M * 4))))
                .register();
        SHAPE_EXTRUDERS[26] = SHAPE_EXTRUDER_ROTOR = REGISTRATE.item("rotor_extruder_mold", Item::new)
                .localization(text -> text
                        .source("Extruder Mold (Rotor)")
                        .translation(MinecraftLocale.ZH_CN, "模头（转子）"))
                .onRegister(materialInfo(new ItemMaterialInfo(new MaterialStack(GTMaterials.Steel, GTValues.M * 4))))
                .register();
    }
    @Trans(key = "item.gtpm.empty_spray_can.tooltip",
           source = "§7Can be filled with sprays of various colors",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7可充入各种颜色的喷剂"))
    public static ItemEntry<Item> SPRAY_EMPTY = REGISTRATE.item("empty_spray_can", Item::new)
            .localization(text -> text
                    .source("Spray Can (Empty)")
                    .translation(MinecraftLocale.ZH_CN, "空喷漆罐"))
            .register();
    public static ItemEntry<ComponentItem> SPRAY_SOLVENT = REGISTRATE.item("solvent_spray_can", ComponentItem::new)
            .localization(text -> text
                    .source("Spray Can (Solvent)")
                    .translation(MinecraftLocale.ZH_CN, "喷漆罐（溶剂）"))
            .properties(p -> p.stacksTo(1))
            .onRegister(attach(new ColorSprayBehaviour(() -> SPRAY_EMPTY.asStack(), 1024, -1))).register();

    public static ItemEntry<ComponentItem> PORTABLE_SCANNER = REGISTRATE.item("portable_scanner", ComponentItem::new)
            .localization(text -> text
                    .source("Portable Scanner")
                    .translation(MinecraftLocale.ZH_CN, "便携式扫描仪"))
            .properties(p -> p.stacksTo(1))
            .onRegister(attach(ElectricStats.createElectricItem(100_000L, GTValues.MV), new PortableScannerBehavior(0)))
            .register();

    public static ItemEntry<ComponentItem> PORTABLE_DEBUG_SCANNER = REGISTRATE
            .item("portable_debug_scanner", ComponentItem::new)
            .localization(text -> text
                    .source("Portable Debug Scanner")
                    .translation(MinecraftLocale.ZH_CN, "便携式调试扫描仪"))
            .properties(p -> p.stacksTo(1))
            .onRegister(
                    attach(ElectricStats.createElectricItem(1_000_000L, GTValues.MV), new PortableScannerBehavior(1)))
            .register();

    @OnlyIn(Dist.CLIENT)
    public static ItemColor cellColor() {
        return (itemStack, index) -> {
            if (index == 1) {
                return FluidUtil.getFluidContained(itemStack)
                        .map(f -> f.getFluid() == Fluids.LAVA ? 0xFFFF7000 : GTUtil.getFluidColor(f))
                        .orElse(-1);
            }
            return -1;
        };
    }

    public static ICustomDescriptionId cellName() {
        return new ICustomDescriptionId() {

            @Override
            public Component getItemName(ItemStack stack) {
                Component prefix = FluidUtil.getFluidContained(stack).map(FluidStack::getHoverName)
                        .orElse(Component.translatable("gtpm.fluid.empty"));
                return Component.translatable(stack.getDescriptionId(), prefix);
            }
        };
    }

    public static ItemEntry<ComponentItem> FLUID_CELL = REGISTRATE.item("fluid_cell", ComponentItem::new)
            .localization(text -> text
                    .source("%s Fluid Cell")
                    .translation(MinecraftLocale.ZH_CN, "%s单元"))
            .setData(ProviderType.ITEM_MODEL, NonNullBiConsumer.noop())
            .color(() -> GTItems::cellColor)
            .onRegister(attach(
                    ThermalFluidStats.create(FluidType.BUCKET_VOLUME, 1800, true, false, false, false, false),
                    new ItemFluidContainer(), cellName()))
            .register();
    public static ItemEntry<ComponentItem> FLUID_CELL_UNIVERSAL = REGISTRATE
            .item("universal_fluid_cell", ComponentItem::new)
            .localization(text -> text
                    .source("%s Universal Cell")
                    .translation(MinecraftLocale.ZH_CN, "%s通用单元"))
            .color(() -> GTItems::cellColor)
            .setData(ProviderType.ITEM_MODEL, NonNullBiConsumer.noop())
            .onRegister(attach(cellName(),
                    ThermalFluidStats.create(FluidType.BUCKET_VOLUME, 1800, true, false, false, false, true),
                    new ItemFluidContainer()))
            .register();
    public static ItemEntry<ComponentItem> FLUID_CELL_LARGE_STEEL = createFluidCell(GTMaterials.Steel, 8, 4, 64);
    public static ItemEntry<ComponentItem> FLUID_CELL_LARGE_ALUMINIUM = createFluidCell(GTMaterials.Aluminium, 32, 4,
            64);
    public static ItemEntry<ComponentItem> FLUID_CELL_LARGE_STAINLESS_STEEL = createFluidCell(
            GTMaterials.StainlessSteel, 64, 6, 64);
    public static ItemEntry<ComponentItem> FLUID_CELL_LARGE_TITANIUM = createFluidCell(GTMaterials.Titanium, 128, 6,
            64);
    public static ItemEntry<ComponentItem> FLUID_CELL_LARGE_TUNGSTEN_STEEL = createFluidCell(GTMaterials.TungstenSteel,
            512, 8, 32);

    public static ItemEntry<ComponentItem> FLUID_CELL_GLASS_VIAL = REGISTRATE.item("glass_vial", ComponentItem::new)
            .localization(text -> text
                    .source("%s Glass Vial")
                    .translation(MinecraftLocale.ZH_CN, "%s玻璃试管"))
            .color(() -> GTItems::cellColor)
            .setData(ProviderType.ITEM_MODEL, NonNullBiConsumer.noop())
            .onRegister(
                    attach(cellName(),
                            ThermalFluidStats.create(FluidType.BUCKET_VOLUME, 1200, false, true, false, false,
                                    true),
                            new ItemFluidContainer()))
            .register();

    public static ItemEntry<ComponentItem> createFluidCell(Material mat, int capacity, int matSize, int stackSize) {
        var prop = mat.getProperty(PropertyKey.FLUID_PIPE);
        Preconditions.checkArgument(prop != null,
                "Material { %s } does not have Fluid Pipe properties, but is being used to create a Fluid Cell",
                mat.getName());
        return REGISTRATE
                .item("%s_fluid_cell".formatted(mat.getName()), ComponentItem::new)
                .localization(text -> {
                    text.source("%s " +
                            (mat == GTMaterials.TungstenSteel ? "Tungstensteel" : toEnglishName(mat.getName())) +
                            " Cell");
                    var localization = mat.getLocalization();
                    if (localization != null) {
                        String chinese = localization.translations().get(MinecraftLocale.ZH_CN);
                        if (chinese != null) text.translation(MinecraftLocale.ZH_CN, "%s" + chinese + "单元");
                    }
                })
                .color(() -> GTItems::cellColor)
                .setData(ProviderType.ITEM_MODEL, NonNullBiConsumer.noop())
                .properties(p -> p.stacksTo(stackSize))
                .onRegister(attach(cellName(),
                        ThermalFluidStats.create(FluidType.BUCKET_VOLUME * capacity, prop, true),
                        new ItemFluidContainer()))
                .onRegister(materialInfo(new ItemMaterialInfo(new MaterialStack(mat, GTValues.M * matSize))))
                .register();
    }

    public static ItemEntry<ComponentItem> TOOL_MATCHES = REGISTRATE.item("matches", ComponentItem::new)
            .localization(text -> text
                    .source("Matches")
                    .translation(MinecraftLocale.ZH_CN, "火柴"))
            .setData(ProviderType.ITEM_MODEL, NonNullBiConsumer.noop())
            .onRegister(attach(new LighterBehavior(false, false, false)))
            .tag(Tags.Items.TOOLS_IGNITER)
            .register();
    public static ItemEntry<ComponentItem> TOOL_MATCHBOX = REGISTRATE.item("matchbox", ComponentItem::new)
            .localization(text -> text
                    .source("Matchbox")
                    .translation(MinecraftLocale.ZH_CN, "火柴盒"))
            .properties(p -> p.stacksTo(1))
            .setData(ProviderType.ITEM_MODEL, NonNullBiConsumer.noop())
            .onRegister(attach(new LighterBehavior(false, true, false, () -> new ItemStack(Items.PAPER, 1), 16)))
            .tag(Tags.Items.TOOLS_IGNITER)
            .register();
    public static ItemEntry<ComponentItem> TOOL_LIGHTER_INVAR = REGISTRATE.item("invar_lighter", ComponentItem::new)
            .localization(text -> text
                    .source("Invar Lighter")
                    .translation(MinecraftLocale.ZH_CN, "殷钢打火机"))
            .properties(p -> p.stacksTo(1))
            .setData(ProviderType.ITEM_MODEL, NonNullBiConsumer.noop())
            .onRegister(attach(new LighterBehavior(true, true, true)))
            .onRegister(attach(new FilteredFluidContainer(100, true, x -> x.is(CustomTags.LIGHTER_FLUIDS)),
                    new ItemFluidContainer()))
            .onRegister(modelPredicate(GTPM.id("lighter_open"),
                    (itemStack) -> itemStack.getOrDefault(GTDataComponents.LIGHTER_OPEN, false) ? 1.0f : 0.0f))
            .tag(Tags.Items.TOOLS_IGNITER)
            .register();
    public static ItemEntry<ComponentItem> TOOL_LIGHTER_PLATINUM = REGISTRATE
            .item("platinum_lighter", ComponentItem::new)
            .localization(text -> text
                    .source("Platinum Lighter")
                    .translation(MinecraftLocale.ZH_CN, "铂打火机"))
            .properties(p -> p.stacksTo(1).rarity(Rarity.UNCOMMON))
            .setData(ProviderType.ITEM_MODEL, NonNullBiConsumer.noop())
            .onRegister(attach(new LighterBehavior(true, true, true)))
            .onRegister(attach(new FilteredFluidContainer(1000, true, x -> x.is(CustomTags.LIGHTER_FLUIDS)),
                    new ItemFluidContainer()))
            .onRegister(modelPredicate(GTPM.id("lighter_open"),
                    (itemStack) -> itemStack.getOrDefault(GTDataComponents.LIGHTER_OPEN, false) ? 1.0f : 0.0f))
            .tag(Tags.Items.TOOLS_IGNITER)
            .register();

    public static ItemEntry<Item> CARBON_FIBERS = REGISTRATE.item("carbon_fibers", Item::new)
            .localization(text -> text
                    .source("Raw Carbon Fibers")
                    .translation(MinecraftLocale.ZH_CN, "生碳纤维"))
            .register();
    public static ItemEntry<Item> CARBON_MESH = REGISTRATE.item("carbon_fiber_mesh", Item::new)
            .localization(text -> text
                    .source("Carbon Fiber Mesh")
                    .translation(MinecraftLocale.ZH_CN, "碳纤维网"))
            .register();
    public static ItemEntry<Item> CARBON_FIBER_PLATE = REGISTRATE.item("carbon_fiber_plate", Item::new)
            .localization(text -> text
                    .source("Carbon Fiber Plate")
                    .translation(MinecraftLocale.ZH_CN, "碳板"))
            .register();
    @Trans(key = "item.gtpm.duct_tape.tooltip",
           source = "§7If you can't fix it with this, use more of it!",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7如果一卷修不好，那就再来一卷！"))
    public static ItemEntry<ComponentItem> DUCT_TAPE = REGISTRATE
            .item("duct_tape", ComponentItem::new)
            .localization(text -> text
                    .source("BrainTech Aerospace Advanced Reinforced Duct Tape FAL-84")
                    .translation(MinecraftLocale.ZH_CN, "布莱恩科技航空专用强化胶带FAL-84型"))
            .onRegister(attach(new TapeBehaviour()))
            .register();
    @Trans(key = "item.gtpm.basic_tape.tooltip",
           source = "§7Not strong enough for mechanical issues\nCan be used to pick up crates without dropping their items",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7强度不足，无法解决机械故障\n可以带走板条箱，且不丢失物品"))
    public static ItemEntry<ComponentItem> BASIC_TAPE = REGISTRATE
            .item("basic_tape", ComponentItem::new)
            .localization(text -> text
                    .source("Tape")
                    .translation(MinecraftLocale.ZH_CN, "胶带"))
            .onRegister(attach(new TapeBehaviour()))
            .register();

    @Trans(key = "item.gtpm.neutron_reflector.tooltip",
           source = "§7Indestructible",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7坚不可摧"))
    public static ItemEntry<Item> NEUTRON_REFLECTOR = REGISTRATE.item("neutron_reflector", Item::new)
            .localization(text -> text
                    .source("Iridium Neutron Reflector")
                    .translation(MinecraftLocale.ZH_CN, "铱中子反射板"))
            .register();

    @Trans(key = "item.gtpm.lv_battery_hull.tooltip",
           source = "§7An empty LV Battery Hull",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7一个空的§7LV§7电池外壳"))
    public static ItemEntry<Item> BATTERY_HULL_LV = REGISTRATE.item("lv_battery_hull", Item::new)
            .localization(text -> text
                    .source("Small Battery Hull")
                    .translation(MinecraftLocale.ZH_CN, "小型电池外壳"))
            .onRegister(materialInfo(new ItemMaterialInfo(new MaterialStack(GTMaterials.BatteryAlloy, GTValues.M))))
            .register();
    @Trans(key = "item.gtpm.mv_battery_hull.tooltip",
           source = "§7An empty §bMV §7Battery Hull",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7一个空的§bMV§7电池外壳"))
    public static ItemEntry<Item> BATTERY_HULL_MV = REGISTRATE.item("mv_battery_hull", Item::new)
            .localization(text -> text
                    .source("Medium Battery Hull")
                    .translation(MinecraftLocale.ZH_CN, "中型电池外壳"))
            .onRegister(materialInfo(new ItemMaterialInfo(new MaterialStack(GTMaterials.BatteryAlloy, GTValues.M * 3))))
            .register();
    @Trans(key = "item.gtpm.hv_battery_hull.tooltip",
           source = "§7An empty §6HV §7Battery Hull",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7一个空的§6HV§7电池外壳"))
    public static ItemEntry<Item> BATTERY_HULL_HV = REGISTRATE.item("hv_battery_hull", Item::new)
            .localization(text -> text
                    .source("Large Battery Hull")
                    .translation(MinecraftLocale.ZH_CN, "大型电池外壳"))
            .onRegister(materialInfo(new ItemMaterialInfo(new MaterialStack(GTMaterials.BatteryAlloy, GTValues.M * 9))))
            .register();
    @Trans(key = "item.gtpm.ev_battery_hull.tooltip",
           source = "§7An empty §5EV §7Battery Hull",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7一个空的§5EV§7电池外壳"))
    public static ItemEntry<Item> BATTERY_HULL_SMALL_VANADIUM = REGISTRATE.item("ev_battery_hull", Item::new)
            .localization(text -> text
                    .source("Small Vanadium Battery Hull")
                    .translation(MinecraftLocale.ZH_CN, "小型钒电池外壳"))
            .onRegister(materialInfo(new ItemMaterialInfo(new MaterialStack(GTMaterials.RedSteel, GTValues.M * 2))))
            .register();
    @Trans(key = "item.gtpm.iv_battery_hull.tooltip",
           source = "§7An empty §1IV §7Battery Hull",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7一个空的§1IV§7电池外壳"))
    public static ItemEntry<Item> BATTERY_HULL_MEDIUM_VANADIUM = REGISTRATE.item("iv_battery_hull", Item::new)
            .localization(text -> text
                    .source("Medium Vanadium Battery Hull")
                    .translation(MinecraftLocale.ZH_CN, "中型钒电池外壳"))
            .onRegister(materialInfo(new ItemMaterialInfo(new MaterialStack(GTMaterials.RoseGold, GTValues.M * 6))))
            .register();
    @Trans(key = "item.gtpm.luv_battery_hull.tooltip",
           source = "§7An empty §dLuV §7Battery Hull",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7一个空的§dLuV§r电池外壳"))
    public static ItemEntry<Item> BATTERY_HULL_LARGE_VANADIUM = REGISTRATE.item("luv_battery_hull", Item::new)
            .localization(text -> text
                    .source("Large Vanadium Battery Hull")
                    .translation(MinecraftLocale.ZH_CN, "大型钒电池外壳"))
            .onRegister(materialInfo(new ItemMaterialInfo(new MaterialStack(GTMaterials.BlueSteel, GTValues.M * 18))))
            .register();
    @Trans(key = "item.gtpm.zpm_battery_hull.tooltip",
           source = "§7An empty §fZPM §7Battery Hull",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7一个空的§fZPM§7电池外壳"))
    public static ItemEntry<Item> BATTERY_HULL_MEDIUM_NAQUADRIA = REGISTRATE.item("zpm_battery_hull", Item::new)
            .localization(text -> text
                    .source("Medium Naquadria Battery Hull")
                    .translation(MinecraftLocale.ZH_CN, "中型超能硅岩电池外壳"))
            .onRegister(materialInfo(new ItemMaterialInfo(new MaterialStack(GTMaterials.Europium, GTValues.M * 6))))
            .register();
    @Trans(key = "item.gtpm.uv_battery_hull.tooltip",
           source = "§7An empty §3UV §7Battery Hull",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7一个空的§3UV§7电池外壳"))
    public static ItemEntry<Item> BATTERY_HULL_LARGE_NAQUADRIA = REGISTRATE.item("uv_battery_hull", Item::new)
            .localization(text -> text
                    .source("Large Naquadria Battery Hull")
                    .translation(MinecraftLocale.ZH_CN, "大型超能硅岩电池外壳"))
            .onRegister(materialInfo(new ItemMaterialInfo(new MaterialStack(GTMaterials.Americium, GTValues.M * 18))))
            .register();

    public static ItemEntry<ComponentItem> BATTERY_ULV_TANTALUM = REGISTRATE
            .item("tantalum_capacitor", ComponentItem::new)
            .localization(text -> text
                    .source("Tantalum Capacitor")
                    .translation(MinecraftLocale.ZH_CN, "钽电容"))
            .onRegister(attach(ElectricStats.createRechargeableBattery(1000, GTValues.ULV)))
            .tag(CustomTags.ULV_BATTERIES).register();

    @Trans(key = "item.gtpm.lv_sodium_battery.tooltip",
           source = "§7Reusable Battery",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7可充电电池"))
    public static ItemEntry<ComponentItem> BATTERY_LV_SODIUM = REGISTRATE
            .item("lv_sodium_battery", ComponentItem::new)
            .localization(text -> text
                    .source("Small Sodium Battery")
                    .translation(MinecraftLocale.ZH_CN, "小型钠电池"))
            .model(() -> overrideModel(GTPM.id("battery"), 8))
            .onRegister(modelPredicate(GTPM.id("battery"), ElectricStats::getStoredPredicate))
            .onRegister(attach(ElectricStats.createRechargeableBattery(80000, GTValues.LV)))
            .tag(CustomTags.LV_BATTERIES).register();
    @Trans(key = "item.gtpm.mv_sodium_battery.tooltip",
           source = "§7Reusable Battery",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7可充电电池"))
    public static ItemEntry<ComponentItem> BATTERY_MV_SODIUM = REGISTRATE
            .item("mv_sodium_battery", ComponentItem::new)
            .localization(text -> text
                    .source("Medium Sodium Battery")
                    .translation(MinecraftLocale.ZH_CN, "中型钠电池"))
            .model(() -> overrideModel(GTPM.id("battery"), 8))
            .onRegister(modelPredicate(GTPM.id("battery"), ElectricStats::getStoredPredicate))
            .onRegister(attach(ElectricStats.createRechargeableBattery(360000, GTValues.MV)))
            .tag(CustomTags.MV_BATTERIES).register();
    @Trans(key = "item.gtpm.hv_sodium_battery.tooltip",
           source = "§7Reusable Battery",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7可充电电池"))
    public static ItemEntry<ComponentItem> BATTERY_HV_SODIUM = REGISTRATE
            .item("hv_sodium_battery", ComponentItem::new)
            .localization(text -> text
                    .source("Large Sodium Battery")
                    .translation(MinecraftLocale.ZH_CN, "大型钠电池"))
            .model(() -> overrideModel(GTPM.id("battery"), 8))
            .onRegister(modelPredicate(GTPM.id("battery"), ElectricStats::getStoredPredicate))
            .onRegister(attach(ElectricStats.createRechargeableBattery(1200000, GTValues.HV)))
            .tag(CustomTags.HV_BATTERIES).register();

    @Trans(key = "item.gtpm.lv_lithium_battery.tooltip",
           source = "§7Reusable Battery",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7可充电电池"))
    public static ItemEntry<ComponentItem> BATTERY_LV_LITHIUM = REGISTRATE
            .item("lv_lithium_battery", ComponentItem::new)
            .localization(text -> text
                    .source("Small Lithium Battery")
                    .translation(MinecraftLocale.ZH_CN, "小型锂电池"))
            .model(() -> overrideModel(GTPM.id("battery"), 8))
            .onRegister(modelPredicate(GTPM.id("battery"), ElectricStats::getStoredPredicate))
            .onRegister(attach(ElectricStats.createRechargeableBattery(120000, GTValues.LV)))
            .tag(CustomTags.LV_BATTERIES).register();
    @Trans(key = "item.gtpm.mv_lithium_battery.tooltip",
           source = "§7Reusable Battery",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7可充电电池"))
    public static ItemEntry<ComponentItem> BATTERY_MV_LITHIUM = REGISTRATE
            .item("mv_lithium_battery", ComponentItem::new)
            .localization(text -> text
                    .source("Medium Lithium Battery")
                    .translation(MinecraftLocale.ZH_CN, "中型锂电池"))
            .model(() -> overrideModel(GTPM.id("battery"), 8))
            .onRegister(modelPredicate(GTPM.id("battery"), ElectricStats::getStoredPredicate))
            .onRegister(attach(ElectricStats.createRechargeableBattery(420000, GTValues.MV)))
            .tag(CustomTags.MV_BATTERIES).register();
    @Trans(key = "item.gtpm.hv_lithium_battery.tooltip",
           source = "§7Reusable Battery",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7可充电电池"))
    public static ItemEntry<ComponentItem> BATTERY_HV_LITHIUM = REGISTRATE
            .item("hv_lithium_battery", ComponentItem::new)
            .localization(text -> text
                    .source("Large Lithium Battery")
                    .translation(MinecraftLocale.ZH_CN, "大型锂电池"))
            .model(() -> overrideModel(GTPM.id("battery"), 8))
            .onRegister(modelPredicate(GTPM.id("battery"), ElectricStats::getStoredPredicate))
            .onRegister(attach(ElectricStats.createRechargeableBattery(1800000, GTValues.HV)))
            .tag(CustomTags.HV_BATTERIES).register();

    @Trans(key = "item.gtpm.lv_cadmium_battery.tooltip",
           source = "§7Reusable Battery",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7可充电电池"))
    public static ItemEntry<ComponentItem> BATTERY_LV_CADMIUM = REGISTRATE
            .item("lv_cadmium_battery", ComponentItem::new)
            .localization(text -> text
                    .source("Small Cadmium Battery")
                    .translation(MinecraftLocale.ZH_CN, "小型镉电池"))
            .model(() -> overrideModel(GTPM.id("battery"), 8))
            .onRegister(modelPredicate(GTPM.id("battery"), ElectricStats::getStoredPredicate))
            .onRegister(attach(ElectricStats.createRechargeableBattery(100000, GTValues.LV)))
            .tag(CustomTags.LV_BATTERIES).register();
    @Trans(key = "item.gtpm.mv_cadmium_battery.tooltip",
           source = "§7Reusable Battery",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7可充电电池"))
    public static ItemEntry<ComponentItem> BATTERY_MV_CADMIUM = REGISTRATE
            .item("mv_cadmium_battery", ComponentItem::new)
            .localization(text -> text
                    .source("Medium Cadmium Battery")
                    .translation(MinecraftLocale.ZH_CN, "中型镉电池"))
            .model(() -> overrideModel(GTPM.id("battery"), 8))
            .onRegister(modelPredicate(GTPM.id("battery"), ElectricStats::getStoredPredicate))
            .onRegister(attach(ElectricStats.createRechargeableBattery(400000, GTValues.MV)))
            .tag(CustomTags.MV_BATTERIES).register();
    @Trans(key = "item.gtpm.hv_cadmium_battery.tooltip",
           source = "§7Reusable Battery",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7可充电电池"))
    public static ItemEntry<ComponentItem> BATTERY_HV_CADMIUM = REGISTRATE
            .item("hv_cadmium_battery", ComponentItem::new)
            .localization(text -> text
                    .source("Large Cadmium Battery")
                    .translation(MinecraftLocale.ZH_CN, "大型镉电池"))
            .model(() -> overrideModel(GTPM.id("battery"), 8))
            .onRegister(modelPredicate(GTPM.id("battery"), ElectricStats::getStoredPredicate))
            .onRegister(attach(ElectricStats.createRechargeableBattery(1600000, GTValues.HV)))
            .tag(CustomTags.HV_BATTERIES).register();

    @Trans(key = "item.gtpm.energy_crystal.tooltip",
           source = "§7Reusable Battery",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7可充电电池"))
    public static ItemEntry<ComponentItem> ENERGIUM_CRYSTAL = REGISTRATE.item("energy_crystal", ComponentItem::new)
            .localization(text -> text
                    .source("Energium Crystal")
                    .translation(MinecraftLocale.ZH_CN, "能量水晶"))
            .model(() -> overrideModel(GTPM.id("battery"), 8))
            .onRegister(modelPredicate(GTPM.id("battery"), ElectricStats::getStoredPredicate))
            .onRegister(attach(ElectricStats.createRechargeableBattery(6_400_000L, GTValues.HV)))
            .tag(CustomTags.HV_BATTERIES).register();
    @Trans(key = "item.gtpm.lapotron_crystal.tooltip",
           source = "§7Reusable Battery",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7可充电电池"))
    public static ItemEntry<ComponentItem> LAPOTRON_CRYSTAL = REGISTRATE.item("lapotron_crystal", ComponentItem::new)
            .localization(text -> text
                    .source("Lapotron Crystal")
                    .translation(MinecraftLocale.ZH_CN, "兰波顿水晶"))
            .model(() -> overrideModel(GTPM.id("battery"), 8))
            .onRegister(modelPredicate(GTPM.id("battery"), ElectricStats::getStoredPredicate))
            .onRegister(attach(ElectricStats.createRechargeableBattery(25_000_000L, GTValues.EV)))
            .tag(CustomTags.EV_BATTERIES).register();

    @Trans(key = "item.gtpm.ev_vanadium_battery.tooltip",
           source = "§7Reusable Battery",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7可充电电池"))
    public static ItemEntry<ComponentItem> BATTERY_EV_VANADIUM = REGISTRATE
            .item("ev_vanadium_battery", ComponentItem::new)
            .localization(text -> text
                    .source("Small Vanadium Battery")
                    .translation(MinecraftLocale.ZH_CN, "小型钒电池"))
            .model(() -> overrideModel(GTPM.id("battery"), 8))
            .onRegister(modelPredicate(GTPM.id("battery"), ElectricStats::getStoredPredicate))
            .onRegister(attach(ElectricStats.createRechargeableBattery(10_240_000L, GTValues.EV)))
            .tag(CustomTags.EV_BATTERIES).register();
    @Trans(key = "item.gtpm.iv_vanadium_battery.tooltip",
           source = "§7Reusable Battery",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7可充电电池"))
    public static ItemEntry<ComponentItem> BATTERY_IV_VANADIUM = REGISTRATE
            .item("iv_vanadium_battery", ComponentItem::new)
            .localization(text -> text
                    .source("Medium Vanadium Battery")
                    .translation(MinecraftLocale.ZH_CN, "中型钒电池"))
            .model(() -> overrideModel(GTPM.id("battery"), 8))
            .onRegister(modelPredicate(GTPM.id("battery"), ElectricStats::getStoredPredicate))
            .onRegister(attach(ElectricStats.createRechargeableBattery(40_960_000L, GTValues.IV)))
            .tag(CustomTags.IV_BATTERIES).register();
    @Trans(key = "item.gtpm.luv_vanadium_battery.tooltip",
           source = "§7Reusable Battery",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7可充电电池"))
    public static ItemEntry<ComponentItem> BATTERY_LuV_VANADIUM = REGISTRATE
            .item("luv_vanadium_battery", ComponentItem::new)
            .localization(text -> text
                    .source("Large Vanadium Battery")
                    .translation(MinecraftLocale.ZH_CN, "大型钒电池"))
            .model(() -> overrideModel(GTPM.id("battery"), 8))
            .onRegister(modelPredicate(GTPM.id("battery"), ElectricStats::getStoredPredicate))
            .onRegister(attach(ElectricStats.createRechargeableBattery(163_840_000L, GTValues.LuV)))
            .tag(CustomTags.LuV_BATTERIES).register();

    @Trans(key = "item.gtpm.zpm_naquadria_battery.tooltip",
           source = "§7Reusable Battery",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7可充电电池"))
    public static ItemEntry<ComponentItem> BATTERY_ZPM_NAQUADRIA = REGISTRATE
            .item("zpm_naquadria_battery", ComponentItem::new)
            .localization(text -> text
                    .source("Medium Naquadria Battery")
                    .translation(MinecraftLocale.ZH_CN, "中型超能硅岩电池"))
            .model(() -> overrideModel(GTPM.id("battery"), 8))
            .onRegister(modelPredicate(GTPM.id("battery"), ElectricStats::getStoredPredicate))
            .onRegister(attach(ElectricStats.createRechargeableBattery(655_360_000L, GTValues.ZPM)))
            .tag(CustomTags.ZPM_BATTERIES).register();
    @Trans(key = "item.gtpm.uv_naquadria_battery.tooltip",
           source = "§7Reusable Battery",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7可充电电池"))
    public static ItemEntry<ComponentItem> BATTERY_UV_NAQUADRIA = REGISTRATE
            .item("uv_naquadria_battery", ComponentItem::new)
            .localization(text -> text
                    .source("Large Naquadria Battery")
                    .translation(MinecraftLocale.ZH_CN, "大型超能硅岩电池"))
            .model(() -> overrideModel(GTPM.id("battery"), 8))
            .onRegister(modelPredicate(GTPM.id("battery"), ElectricStats::getStoredPredicate))
            .onRegister(attach(ElectricStats.createRechargeableBattery(2_621_440_000L, GTValues.UV)))
            .tag(CustomTags.UV_BATTERIES).register();

    @Trans(key = "item.gtpm.lapotronic_energy_orb.tooltip",
           source = "§7Reusable Battery",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7可充电电池"))
    public static ItemEntry<ComponentItem> ENERGY_LAPOTRONIC_ORB = REGISTRATE
            .item("lapotronic_energy_orb", ComponentItem::new)
            .localization(text -> text
                    .source("Lapotronic Energy Orb")
                    .translation(MinecraftLocale.ZH_CN, "兰波顿能量球"))
            .model(() -> overrideModel(GTPM.id("battery"), 8))
            .onRegister(modelPredicate(GTPM.id("battery"), ElectricStats::getStoredPredicate))
            .onRegister(attach(ElectricStats.createRechargeableBattery(250_000_000L, GTValues.IV)))
            .tag(CustomTags.IV_BATTERIES).register();
    @Trans(key = "item.gtpm.lapotronic_energy_orb_cluster.tooltip",
           source = "§7Reusable Battery",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7可充电电池"))
    public static ItemEntry<ComponentItem> ENERGY_LAPOTRONIC_ORB_CLUSTER = REGISTRATE
            .item("lapotronic_energy_orb_cluster", ComponentItem::new)
            .localization(text -> text
                    .source("Lapotronic Energy Orb Cluster")
                    .translation(MinecraftLocale.ZH_CN, "兰波顿能量球簇"))
            .model(() -> overrideModel(GTPM.id("battery"), 8))
            .onRegister(modelPredicate(GTPM.id("battery"), ElectricStats::getStoredPredicate))
            .onRegister(attach(ElectricStats.createRechargeableBattery(1_000_000_000L, GTValues.LuV)))
            .tag(CustomTags.LuV_BATTERIES).register();

    @Trans(key = "item.gtpm.energy_module.tooltip",
           source = "§7Reusable Battery",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7可充电电池"))
    public static ItemEntry<ComponentItem> ENERGY_MODULE = REGISTRATE.item("energy_module", ComponentItem::new)
            .localization(text -> text
                    .source("Energy Module")
                    .translation(MinecraftLocale.ZH_CN, "能量模块"))
            .model(() -> overrideModel(GTPM.id("battery"), 8))
            .onRegister(modelPredicate(GTPM.id("battery"), ElectricStats::getStoredPredicate))
            .onRegister(attach(ElectricStats.createRechargeableBattery(4_000_000_000L, GTValues.ZPM)))
            .tag(CustomTags.ZPM_BATTERIES).register();
    @Trans(key = "item.gtpm.energy_cluster.tooltip",
           source = "§7Reusable Battery",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7可充电电池"))
    public static ItemEntry<ComponentItem> ENERGY_CLUSTER = REGISTRATE.item("energy_cluster", ComponentItem::new)
            .localization(text -> text
                    .source("Energy Cluster")
                    .translation(MinecraftLocale.ZH_CN, "能量簇"))
            .model(() -> overrideModel(GTPM.id("battery"), 8))
            .onRegister(modelPredicate(GTPM.id("battery"), ElectricStats::getStoredPredicate))
            .onRegister(attach(ElectricStats.createRechargeableBattery(20_000_000_000L, GTValues.UV)))
            .tag(CustomTags.UV_BATTERIES).register();

    public static ItemEntry<ComponentItem> ZERO_POINT_MODULE = REGISTRATE
            .item("zero_point_module", ComponentItem::new)
            .localization(text -> text
                    .source("Zero Point Module")
                    .translation(MinecraftLocale.ZH_CN, "零点模块"))
            .model(() -> overrideModel(GTPM.id("battery"), 8))
            .onRegister(modelPredicate(GTPM.id("battery"), ElectricStats::getStoredPredicate))
            .onRegister(attach(ElectricStats.createBattery(2000000000000L, GTValues.ZPM, true))).register();
    @Trans(key = "item.gtpm.max_battery.tooltip",
           source = "§7Fill this to win Minecraft",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7填满就能通关Minecraft"))
    public static ItemEntry<ComponentItem> ULTIMATE_BATTERY = REGISTRATE.item("max_battery", ComponentItem::new)
            .localization(text -> text
                    .source("Ultimate Battery")
                    .translation(MinecraftLocale.ZH_CN, "终极电池"))
            .model(() -> overrideModel(GTPM.id("battery"), 8))
            .onRegister(modelPredicate(GTPM.id("battery"), ElectricStats::getStoredPredicate))
            .onRegister(attach(ElectricStats.createRechargeableBattery(Long.MAX_VALUE, GTValues.UHV)))
            .tag(CustomTags.UHV_BATTERIES).register();

    public static ItemEntry<Item> ELECTRIC_MOTOR_LV = REGISTRATE.item("lv_electric_motor", Item::new)
            .localization(text -> text
                    .source("LV Electric Motor")
                    .translation(MinecraftLocale.ZH_CN, "§7LV§r电动马达"))
            .tag(CustomTags.ELECTRIC_MOTORS)
            .register();
    public static ItemEntry<Item> ELECTRIC_MOTOR_MV = REGISTRATE.item("mv_electric_motor", Item::new)
            .localization(text -> text
                    .source("MV Electric Motor")
                    .translation(MinecraftLocale.ZH_CN, "§bMV§r电动马达"))
            .tag(CustomTags.ELECTRIC_MOTORS)
            .register();
    public static ItemEntry<Item> ELECTRIC_MOTOR_HV = REGISTRATE.item("hv_electric_motor", Item::new)
            .localization(text -> text
                    .source("HV Electric Motor")
                    .translation(MinecraftLocale.ZH_CN, "§6HV§r电动马达"))
            .tag(CustomTags.ELECTRIC_MOTORS)
            .register();
    public static ItemEntry<Item> ELECTRIC_MOTOR_EV = REGISTRATE.item("ev_electric_motor", Item::new)
            .localization(text -> text
                    .source("EV Electric Motor")
                    .translation(MinecraftLocale.ZH_CN, "§5EV§r电动马达"))
            .tag(CustomTags.ELECTRIC_MOTORS)
            .register();
    public static ItemEntry<Item> ELECTRIC_MOTOR_IV = REGISTRATE.item("iv_electric_motor", Item::new)
            .localization(text -> text
                    .source("IV Electric Motor")
                    .translation(MinecraftLocale.ZH_CN, "§9IV§r电动马达"))
            .tag(CustomTags.ELECTRIC_MOTORS)
            .register();
    public static ItemEntry<Item> ELECTRIC_MOTOR_LuV = REGISTRATE.item("luv_electric_motor", Item::new)
            .localization(text -> text
                    .source("LuV Electric Motor")
                    .translation(MinecraftLocale.ZH_CN, "§dLuV§r电动马达"))
            .tag(CustomTags.ELECTRIC_MOTORS)
            .register();
    public static ItemEntry<Item> ELECTRIC_MOTOR_ZPM = REGISTRATE.item("zpm_electric_motor", Item::new)
            .localization(text -> text
                    .source("ZPM Electric Motor")
                    .translation(MinecraftLocale.ZH_CN, "§cZPM§r电动马达"))
            .tag(CustomTags.ELECTRIC_MOTORS)
            .register();
    public static ItemEntry<Item> ELECTRIC_MOTOR_UV = REGISTRATE.item("uv_electric_motor", Item::new)
            .localization(text -> text
                    .source("UV Electric Motor")
                    .translation(MinecraftLocale.ZH_CN, "§3UV§r电动马达"))
            .tag(CustomTags.ELECTRIC_MOTORS)
            .register();
    public static ItemEntry<Item> ELECTRIC_MOTOR_UHV = GTCEuAPI.isHighTier() ?
            REGISTRATE.item("uhv_electric_motor", Item::new)
                    .localization(text -> text
                            .source("UHV Electric Motor")
                            .translation(MinecraftLocale.ZH_CN, "§4UHV§r电动马达"))
                    .register() :
            null;
    public static ItemEntry<Item> ELECTRIC_MOTOR_UEV = GTCEuAPI.isHighTier() ?
            REGISTRATE.item("uev_electric_motor", Item::new)
                    .localization(text -> text
                            .source("UEV Electric Motor")
                            .translation(MinecraftLocale.ZH_CN, "§aUEV§r电动马达"))
                    .register() :
            null;
    public static ItemEntry<Item> ELECTRIC_MOTOR_UIV = GTCEuAPI.isHighTier() ?
            REGISTRATE.item("uiv_electric_motor", Item::new)
                    .localization(text -> text
                            .source("UIV Electric Motor")
                            .translation(MinecraftLocale.ZH_CN, "§2UIV§r电动马达"))
                    .register() :
            null;
    public static ItemEntry<Item> ELECTRIC_MOTOR_UXV = GTCEuAPI.isHighTier() ?
            REGISTRATE.item("uxv_electric_motor", Item::new)
                    .localization(text -> text
                            .source("UXV Electric Motor")
                            .translation(MinecraftLocale.ZH_CN, "§eUXV§r电动马达"))
                    .register() :
            null;
    public static ItemEntry<Item> ELECTRIC_MOTOR_OpV = GTCEuAPI.isHighTier() ?
            REGISTRATE.item("opv_electric_motor", Item::new)
                    .localization(text -> text
                            .source("OpV Electric Motor")
                            .translation(MinecraftLocale.ZH_CN, "§9§lOpV§r电动马达"))
                    .register() :
            null;

    public static ItemEntry<ComponentItem> ELECTRIC_PUMP_LV = REGISTRATE.item("lv_electric_pump", ComponentItem::new)
            .localization(text -> text
                    .source("LV Electric Pump")
                    .translation(MinecraftLocale.ZH_CN, "§7LV§r电动泵"))
            .onRegister(attach(new CoverPlaceBehavior(GTCovers.PUMPS[0])))
            .onRegister(attach(new TooltipBehavior(lines -> {
                lines.add(Component.translatable("item.gtpm.electric.pump.tooltip"));
                lines.add(Component.translatable("gtpm.universal.tooltip.fluid_transfer_rate",
                        FormattingUtil.formatNumbers(1280 / 20)));
            })))
            .tag(CustomTags.ELECTRIC_PUMPS)
            .register();

    public static ItemEntry<ComponentItem> ELECTRIC_PUMP_MV = REGISTRATE.item("mv_electric_pump", ComponentItem::new)
            .localization(text -> text
                    .source("MV Electric Pump")
                    .translation(MinecraftLocale.ZH_CN, "§bMV§r电动泵"))
            .onRegister(attach(new CoverPlaceBehavior(GTCovers.PUMPS[1])))
            .onRegister(attach(new TooltipBehavior(lines -> {
                lines.add(Component.translatable("item.gtpm.electric.pump.tooltip"));
                lines.add(Component.translatable("gtpm.universal.tooltip.fluid_transfer_rate",
                        FormattingUtil.formatNumbers(1280 * 4 / 20)));
            })))
            .tag(CustomTags.ELECTRIC_PUMPS)
            .register();
    public static ItemEntry<ComponentItem> ELECTRIC_PUMP_HV = REGISTRATE.item("hv_electric_pump", ComponentItem::new)
            .localization(text -> text
                    .source("HV Electric Pump")
                    .translation(MinecraftLocale.ZH_CN, "§6HV§r电动泵"))
            .onRegister(attach(new CoverPlaceBehavior(GTCovers.PUMPS[2])))
            .onRegister(attach(new TooltipBehavior(lines -> {
                lines.add(Component.translatable("item.gtpm.electric.pump.tooltip"));
                lines.add(Component.translatable("gtpm.universal.tooltip.fluid_transfer_rate",
                        FormattingUtil.formatNumbers(1280 * 16 / 20)));
            })))
            .tag(CustomTags.ELECTRIC_PUMPS)
            .register();
    public static ItemEntry<ComponentItem> ELECTRIC_PUMP_EV = REGISTRATE.item("ev_electric_pump", ComponentItem::new)
            .localization(text -> text
                    .source("EV Electric Pump")
                    .translation(MinecraftLocale.ZH_CN, "§5EV§r电动泵"))
            .onRegister(attach(new CoverPlaceBehavior(GTCovers.PUMPS[3])))
            .onRegister(attach(new TooltipBehavior(lines -> {
                lines.add(Component.translatable("item.gtpm.electric.pump.tooltip"));
                lines.add(Component.translatable("gtpm.universal.tooltip.fluid_transfer_rate",
                        FormattingUtil.formatNumbers(1280 * 64 / 20)));
            })))
            .tag(CustomTags.ELECTRIC_PUMPS)
            .register();
    public static ItemEntry<ComponentItem> ELECTRIC_PUMP_IV = REGISTRATE.item("iv_electric_pump", ComponentItem::new)
            .localization(text -> text
                    .source("IV Electric Pump")
                    .translation(MinecraftLocale.ZH_CN, "§9IV§r电动泵"))
            .onRegister(attach(new CoverPlaceBehavior(GTCovers.PUMPS[4])))
            .onRegister(attach(new TooltipBehavior(lines -> {
                lines.add(Component.translatable("item.gtpm.electric.pump.tooltip"));
                lines.add(Component.translatable("gtpm.universal.tooltip.fluid_transfer_rate",
                        FormattingUtil.formatNumbers(1280 * 64 * 4 / 20)));
            })))
            .tag(CustomTags.ELECTRIC_PUMPS)
            .register();
    public static ItemEntry<ComponentItem> ELECTRIC_PUMP_LuV = REGISTRATE
            .item("luv_electric_pump", ComponentItem::new)
            .localization(text -> text
                    .source("LuV Electric Pump")
                    .translation(MinecraftLocale.ZH_CN, "§dLuV§r电动泵"))
            .onRegister(attach(new CoverPlaceBehavior(GTCovers.PUMPS[5])))
            .onRegister(attach(new TooltipBehavior(lines -> {
                lines.add(Component.translatable("item.gtpm.electric.pump.tooltip"));
                lines.add(Component.translatable("gtpm.universal.tooltip.fluid_transfer_rate",
                        FormattingUtil.formatNumbers(1280 * 64 * 16 / 20)));
            })))
            .tag(CustomTags.ELECTRIC_PUMPS)
            .register();
    public static ItemEntry<ComponentItem> ELECTRIC_PUMP_ZPM = REGISTRATE
            .item("zpm_electric_pump", ComponentItem::new)
            .localization(text -> text
                    .source("ZPM Electric Pump")
                    .translation(MinecraftLocale.ZH_CN, "§cZPM§r电动泵"))
            .onRegister(attach(new CoverPlaceBehavior(GTCovers.PUMPS[6])))
            .onRegister(attach(new TooltipBehavior(lines -> {
                lines.add(Component.translatable("item.gtpm.electric.pump.tooltip"));
                lines.add(Component.translatable("gtpm.universal.tooltip.fluid_transfer_rate",
                        FormattingUtil.formatNumbers(1280 * 64 * 64 / 20)));
            })))
            .tag(CustomTags.ELECTRIC_PUMPS)
            .register();
    public static ItemEntry<ComponentItem> ELECTRIC_PUMP_UV = REGISTRATE.item("uv_electric_pump", ComponentItem::new)
            .localization(text -> text
                    .source("UV Electric Pump")
                    .translation(MinecraftLocale.ZH_CN, "§3UV§r电动泵"))
            .onRegister(attach(new CoverPlaceBehavior(GTCovers.PUMPS[7])))
            .onRegister(attach(new TooltipBehavior(lines -> {
                lines.add(Component.translatable("item.gtpm.electric.pump.tooltip"));
                lines.add(
                        Component.translatable("gtpm.universal.tooltip.fluid_transfer_rate",
                                FormattingUtil.formatNumbers(1280 * 64 * 64 * 4 / 20)));
            })))
            .tag(CustomTags.ELECTRIC_PUMPS)
            .register();

    public static ItemEntry<ComponentItem> ELECTRIC_PUMP_UHV = GTCEuAPI.isHighTier() ?
            REGISTRATE.item("uhv_electric_pump", ComponentItem::new)
                    .localization(text -> text
                            .source("UHV Electric Pump")
                            .translation(MinecraftLocale.ZH_CN, "§4UHV§r电动泵"))
                    .onRegister(attach(new CoverPlaceBehavior(GTCovers.PUMPS[8])))
                    .onRegister(attach(new TooltipBehavior(lines -> {
                        lines.add(Component.translatable("item.gtpm.electric.pump.tooltip"));
                        lines.add(Component.translatable("gtpm.universal.tooltip.fluid_transfer_rate",
                                1280 * 64 * 64 * 4 / 20));
                    })))
                    .register() :
            null;

    public static ItemEntry<ComponentItem> ELECTRIC_PUMP_UEV = GTCEuAPI.isHighTier() ?
            REGISTRATE.item("uev_electric_pump", ComponentItem::new)
                    .localization(text -> text
                            .source("UEV Electric Pump")
                            .translation(MinecraftLocale.ZH_CN, "§aUEV§r电动泵"))
                    .onRegister(attach(new CoverPlaceBehavior(GTCovers.PUMPS[9])))
                    .onRegister(attach(new TooltipBehavior(lines -> {
                        lines.add(Component.translatable("item.gtpm.electric.pump.tooltip"));
                        lines.add(Component.translatable("gtpm.universal.tooltip.fluid_transfer_rate",
                                1280 * 64 * 64 * 4 / 20));
                    })))
                    .register() :
            null;

    public static ItemEntry<ComponentItem> ELECTRIC_PUMP_UIV = GTCEuAPI.isHighTier() ?
            REGISTRATE.item("uiv_electric_pump", ComponentItem::new)
                    .localization(text -> text
                            .source("UIV Electric Pump")
                            .translation(MinecraftLocale.ZH_CN, "§2UIV§r电动泵"))
                    .onRegister(attach(new CoverPlaceBehavior(GTCovers.PUMPS[10])))
                    .onRegister(attach(new TooltipBehavior(lines -> {
                        lines.add(Component.translatable("item.gtpm.electric.pump.tooltip"));
                        lines.add(Component.translatable("gtpm.universal.tooltip.fluid_transfer_rate",
                                1280 * 64 * 64 * 4 / 20));
                    })))
                    .register() :
            null;

    public static ItemEntry<ComponentItem> ELECTRIC_PUMP_UXV = GTCEuAPI.isHighTier() ?
            REGISTRATE.item("uxv_electric_pump", ComponentItem::new)
                    .localization(text -> text
                            .source("UXV Electric Pump")
                            .translation(MinecraftLocale.ZH_CN, "§eUXV§r§r电动泵"))
                    .onRegister(attach(new CoverPlaceBehavior(GTCovers.PUMPS[11])))
                    .onRegister(attach(new TooltipBehavior(lines -> {
                        lines.add(Component.translatable("item.gtpm.electric.pump.tooltip"));
                        lines.add(Component.translatable("gtpm.universal.tooltip.fluid_transfer_rate",
                                1280 * 64 * 64 * 4 / 20));
                    })))
                    .register() :
            null;

    public static ItemEntry<ComponentItem> ELECTRIC_PUMP_OpV = GTCEuAPI.isHighTier() ?
            REGISTRATE.item("opv_electric_pump", ComponentItem::new)
                    .localization(text -> text
                            .source("OpV Electric Pump")
                            .translation(MinecraftLocale.ZH_CN, "§9§lOpV§r电动泵"))
                    .onRegister(attach(new CoverPlaceBehavior(GTCovers.PUMPS[12])))
                    .onRegister(attach(new TooltipBehavior(lines -> {
                        lines.add(Component.translatable("item.gtpm.electric.pump.tooltip"));
                        lines.add(Component.translatable("gtpm.universal.tooltip.fluid_transfer_rate",
                                1280 * 64 * 64 * 4 / 20));
                    })))
                    .register() :
            null;

    public static ItemEntry<ComponentItem> FLUID_REGULATOR_LV = REGISTRATE
            .item("lv_fluid_regulator", ComponentItem::new)
            .localization(text -> text
                    .source("LV Fluid Regulator")
                    .translation(MinecraftLocale.ZH_CN, "§7LV§r流体校准器"))
            .onRegister(attach(new CoverPlaceBehavior(GTCovers.FLUID_REGULATORS[0])))
            .onRegister(attach(new TooltipBehavior(lines -> {
                lines.add(Component.translatable("item.gtpm.fluid.regulator.tooltip"));
                lines.add(Component.translatable("gtpm.universal.tooltip.fluid_transfer_rate", 1280 / 20));
            })))
            .tag(CustomTags.FLUID_REGULATORS)
            .register();
    public static ItemEntry<ComponentItem> FLUID_REGULATOR_MV = REGISTRATE
            .item("mv_fluid_regulator", ComponentItem::new)
            .localization(text -> text
                    .source("MV Fluid Regulator")
                    .translation(MinecraftLocale.ZH_CN, "§bMV§r流体校准器"))
            .onRegister(attach(new CoverPlaceBehavior(GTCovers.FLUID_REGULATORS[1])))
            .onRegister(attach(new TooltipBehavior(lines -> {
                lines.add(Component.translatable("item.gtpm.fluid.regulator.tooltip"));
                lines.add(Component.translatable("gtpm.universal.tooltip.fluid_transfer_rate", 1280 * 4 / 20));
            })))
            .tag(CustomTags.FLUID_REGULATORS)
            .register();
    public static ItemEntry<ComponentItem> FLUID_REGULATOR_HV = REGISTRATE
            .item("hv_fluid_regulator", ComponentItem::new)
            .localization(text -> text
                    .source("HV Fluid Regulator")
                    .translation(MinecraftLocale.ZH_CN, "§6HV§r流体校准器"))
            .onRegister(attach(new CoverPlaceBehavior(GTCovers.FLUID_REGULATORS[2])))
            .onRegister(attach(new TooltipBehavior(lines -> {
                lines.add(Component.translatable("item.gtpm.fluid.regulator.tooltip"));
                lines.add(Component.translatable("gtpm.universal.tooltip.fluid_transfer_rate", 1280 * 16 / 20));
            })))
            .tag(CustomTags.FLUID_REGULATORS)
            .register();
    public static ItemEntry<ComponentItem> FLUID_REGULATOR_EV = REGISTRATE
            .item("ev_fluid_regulator", ComponentItem::new)
            .localization(text -> text
                    .source("EV Fluid Regulator")
                    .translation(MinecraftLocale.ZH_CN, "§5EV§r流体校准器"))
            .onRegister(attach(new CoverPlaceBehavior(GTCovers.FLUID_REGULATORS[3])))
            .onRegister(attach(new TooltipBehavior(lines -> {
                lines.add(Component.translatable("item.gtpm.fluid.regulator.tooltip"));
                lines.add(Component.translatable("gtpm.universal.tooltip.fluid_transfer_rate", 1280 * 64 / 20));
            })))
            .tag(CustomTags.FLUID_REGULATORS)
            .register();
    public static ItemEntry<ComponentItem> FLUID_REGULATOR_IV = REGISTRATE
            .item("iv_fluid_regulator", ComponentItem::new)
            .localization(text -> text
                    .source("IV Fluid Regulator")
                    .translation(MinecraftLocale.ZH_CN, "§9IV§r流体校准器"))
            .onRegister(attach(new CoverPlaceBehavior(GTCovers.FLUID_REGULATORS[4])))
            .onRegister(attach(new TooltipBehavior(lines -> {
                lines.add(Component.translatable("item.gtpm.fluid.regulator.tooltip"));
                lines.add(Component.translatable("gtpm.universal.tooltip.fluid_transfer_rate", 1280 * 64 * 4 / 20));
            })))
            .tag(CustomTags.FLUID_REGULATORS)
            .register();
    public static ItemEntry<ComponentItem> FLUID_REGULATOR_LuV = REGISTRATE
            .item("luv_fluid_regulator", ComponentItem::new)
            .localization(text -> text
                    .source("LuV Fluid Regulator")
                    .translation(MinecraftLocale.ZH_CN, "§dLuV§r流体校准器"))
            .onRegister(attach(new CoverPlaceBehavior(GTCovers.FLUID_REGULATORS[5])))
            .onRegister(attach(new TooltipBehavior(lines -> {
                lines.add(Component.translatable("item.gtpm.fluid.regulator.tooltip"));
                lines.add(Component.translatable("gtpm.universal.tooltip.fluid_transfer_rate", 1280 * 64 * 16 / 20));
            })))
            .tag(CustomTags.FLUID_REGULATORS)
            .register();
    public static ItemEntry<ComponentItem> FLUID_REGULATOR_ZPM = REGISTRATE
            .item("zpm_fluid_regulator", ComponentItem::new)
            .localization(text -> text
                    .source("ZPM Fluid Regulator")
                    .translation(MinecraftLocale.ZH_CN, "§cZPM§r流体校准器"))
            .onRegister(attach(new CoverPlaceBehavior(GTCovers.FLUID_REGULATORS[6])))
            .onRegister(attach(new TooltipBehavior(lines -> {
                lines.add(Component.translatable("item.gtpm.fluid.regulator.tooltip"));
                lines.add(Component.translatable("gtpm.universal.tooltip.fluid_transfer_rate", 1280 * 64 * 64 / 20));
            })))
            .tag(CustomTags.FLUID_REGULATORS)
            .register();
    public static ItemEntry<ComponentItem> FLUID_REGULATOR_UV = REGISTRATE
            .item("uv_fluid_regulator", ComponentItem::new)
            .localization(text -> text
                    .source("UV Fluid Regulator")
                    .translation(MinecraftLocale.ZH_CN, "§3UV§r流体校准器"))
            .onRegister(attach(new CoverPlaceBehavior(GTCovers.FLUID_REGULATORS[7])))
            .onRegister(attach(new TooltipBehavior(lines -> {
                lines.add(Component.translatable("item.gtpm.fluid.regulator.tooltip"));
                lines.add(
                        Component.translatable("gtpm.universal.tooltip.fluid_transfer_rate", 1280 * 64 * 64 * 4 / 20));
            })))
            .tag(CustomTags.FLUID_REGULATORS)
            .register();
    public static ItemEntry<ComponentItem> FLUID_REGULATOR_UHV = GTCEuAPI.isHighTier() ?
            REGISTRATE.item("uhv_fluid_regulator", ComponentItem::new)
                    .localization(text -> text
                            .source("UHV Fluid Regulator")
                            .translation(MinecraftLocale.ZH_CN, "§4UHV§r流体校准器"))
                    .onRegister(attach(new CoverPlaceBehavior(GTCovers.FLUID_REGULATORS[8])))
                    .onRegister(attach(new TooltipBehavior(lines -> {
                        lines.add(Component.translatable("item.gtpm.fluid.regulator.tooltip"));
                        lines.add(Component.translatable("gtpm.universal.tooltip.fluid_transfer_rate",
                                1280 * 64 * 64 * 4 / 20));
                    })))
                    .register() :
            null;
    public static ItemEntry<ComponentItem> FLUID_REGULATOR_UEV = GTCEuAPI.isHighTier() ?
            REGISTRATE.item("uev_fluid_regulator", ComponentItem::new)
                    .localization(text -> text
                            .source("UEV Fluid Regulator")
                            .translation(MinecraftLocale.ZH_CN, "§aUEV§r流体校准器"))
                    .onRegister(attach(new CoverPlaceBehavior(GTCovers.FLUID_REGULATORS[9])))
                    .onRegister(attach(new TooltipBehavior(lines -> {
                        lines.add(Component.translatable("item.gtpm.fluid.regulator.tooltip"));
                        lines.add(Component.translatable("gtpm.universal.tooltip.fluid_transfer_rate",
                                1280 * 64 * 64 * 4 / 20));
                    })))
                    .register() :
            null;
    public static ItemEntry<ComponentItem> FLUID_REGULATOR_UIV = GTCEuAPI.isHighTier() ?
            REGISTRATE.item("uiv_fluid_regulator", ComponentItem::new)
                    .localization(text -> text
                            .source("UIV Fluid Regulator")
                            .translation(MinecraftLocale.ZH_CN, "§2UIV§r流体校准器"))
                    .onRegister(attach(new CoverPlaceBehavior(GTCovers.FLUID_REGULATORS[10])))
                    .onRegister(attach(new TooltipBehavior(lines -> {
                        lines.add(Component.translatable("item.gtpm.fluid.regulator.tooltip"));
                        lines.add(Component.translatable("gtpm.universal.tooltip.fluid_transfer_rate",
                                1280 * 64 * 64 * 4 / 20));
                    })))
                    .register() :
            null;
    public static ItemEntry<ComponentItem> FLUID_REGULATOR_UXV = GTCEuAPI.isHighTier() ?
            REGISTRATE.item("uxv_fluid_regulator", ComponentItem::new)
                    .localization(text -> text
                            .source("UXV Fluid Regulator")
                            .translation(MinecraftLocale.ZH_CN, "§eUXV§r流体校准器"))
                    .onRegister(attach(new CoverPlaceBehavior(GTCovers.FLUID_REGULATORS[11])))
                    .onRegister(attach(new TooltipBehavior(lines -> {
                        lines.add(Component.translatable("item.gtpm.fluid.regulator.tooltip"));
                        lines.add(Component.translatable("gtpm.universal.tooltip.fluid_transfer_rate",
                                1280 * 64 * 64 * 4 / 20));
                    })))
                    .register() :
            null;
    public static ItemEntry<ComponentItem> FLUID_REGULATOR_OpV = GTCEuAPI.isHighTier() ?
            REGISTRATE.item("opv_fluid_regulator", ComponentItem::new)
                    .localization(text -> text
                            .source("OpV Fluid Regulator")
                            .translation(MinecraftLocale.ZH_CN, "§9§lOpV§r流体校准器"))
                    .onRegister(attach(new CoverPlaceBehavior(GTCovers.FLUID_REGULATORS[12])))
                    .onRegister(attach(new TooltipBehavior(lines -> {
                        lines.add(Component.translatable("item.gtpm.fluid.regulator.tooltip"));
                        lines.add(Component.translatable("gtpm.universal.tooltip.fluid_transfer_rate",
                                1280 * 64 * 64 * 4 / 20));
                    })))
                    .register() :
            null;

    public static ItemEntry<ComponentItem> DYNAMITE = REGISTRATE.item("dynamite", ComponentItem::new)
            .localization(text -> text
                    .source("Dynamite")
                    .translation(MinecraftLocale.ZH_CN, "炸药"))
            .onRegister(attach(new DynamiteBehaviour()))
            .tab(TOOL.getKey())
            .register();

    public static ItemEntry<ComponentItem> CONVEYOR_MODULE_LV = REGISTRATE
            .item("lv_conveyor_module", ComponentItem::new)
            .localization(text -> text
                    .source("LV Conveyor Module")
                    .translation(MinecraftLocale.ZH_CN, "§7LV§r传送带"))
            .onRegister(attach(new CoverPlaceBehavior(GTCovers.CONVEYORS[0])))
            .onRegister(attach(new TooltipBehavior(lines -> {
                lines.add(Component.translatable("item.gtpm.conveyor.module.tooltip"));
                lines.add(Component.translatable("gtpm.universal.tooltip.item_transfer_rate", 8));
            })))
            .tag(CustomTags.CONVEYOR_MODULES)
            .register();
    public static ItemEntry<ComponentItem> CONVEYOR_MODULE_MV = REGISTRATE
            .item("mv_conveyor_module", ComponentItem::new)
            .localization(text -> text
                    .source("MV Conveyor Module")
                    .translation(MinecraftLocale.ZH_CN, "§bMV§r传送带"))
            .onRegister(attach(new CoverPlaceBehavior(GTCovers.CONVEYORS[1])))
            .onRegister(attach(new TooltipBehavior(lines -> {
                lines.add(Component.translatable("item.gtpm.conveyor.module.tooltip"));
                lines.add(Component.translatable("gtpm.universal.tooltip.item_transfer_rate", 32));
            })))
            .tag(CustomTags.CONVEYOR_MODULES)
            .register();
    public static ItemEntry<ComponentItem> CONVEYOR_MODULE_HV = REGISTRATE
            .item("hv_conveyor_module", ComponentItem::new)
            .localization(text -> text
                    .source("HV Conveyor Module")
                    .translation(MinecraftLocale.ZH_CN, "§6HV§r传送带"))
            .onRegister(attach(new CoverPlaceBehavior(GTCovers.CONVEYORS[2])))
            .onRegister(attach(new TooltipBehavior(lines -> {
                lines.add(Component.translatable("item.gtpm.conveyor.module.tooltip"));
                lines.add(Component.translatable("gtpm.universal.tooltip.item_transfer_rate", 64));
            })))
            .tag(CustomTags.CONVEYOR_MODULES)
            .register();
    public static ItemEntry<ComponentItem> CONVEYOR_MODULE_EV = REGISTRATE
            .item("ev_conveyor_module", ComponentItem::new)
            .localization(text -> text
                    .source("EV Conveyor Module")
                    .translation(MinecraftLocale.ZH_CN, "§5EV§r传送带"))
            .onRegister(attach(new CoverPlaceBehavior(GTCovers.CONVEYORS[3])))
            .onRegister(attach(new TooltipBehavior(lines -> {
                lines.add(Component.translatable("item.gtpm.conveyor.module.tooltip"));
                lines.add(Component.translatable("gtpm.universal.tooltip.item_transfer_rate_stacks", 3));
            })))
            .tag(CustomTags.CONVEYOR_MODULES)
            .register();
    public static ItemEntry<ComponentItem> CONVEYOR_MODULE_IV = REGISTRATE
            .item("iv_conveyor_module", ComponentItem::new)
            .localization(text -> text
                    .source("IV Conveyor Module")
                    .translation(MinecraftLocale.ZH_CN, "§9IV§r传送带"))
            .onRegister(attach(new CoverPlaceBehavior(GTCovers.CONVEYORS[4])))
            .onRegister(attach(new TooltipBehavior(lines -> {
                lines.add(Component.translatable("item.gtpm.conveyor.module.tooltip"));
                lines.add(Component.translatable("gtpm.universal.tooltip.item_transfer_rate_stacks", 8));
            })))
            .tag(CustomTags.CONVEYOR_MODULES)
            .register();
    public static ItemEntry<ComponentItem> CONVEYOR_MODULE_LuV = REGISTRATE
            .item("luv_conveyor_module", ComponentItem::new)
            .localization(text -> text
                    .source("LuV Conveyor Module")
                    .translation(MinecraftLocale.ZH_CN, "§dLuV§r传送带"))
            .onRegister(attach(new CoverPlaceBehavior(GTCovers.CONVEYORS[5])))
            .onRegister(attach(new TooltipBehavior(lines -> {
                lines.add(Component.translatable("item.gtpm.conveyor.module.tooltip"));
                lines.add(Component.translatable("gtpm.universal.tooltip.item_transfer_rate_stacks", 16));
            })))
            .tag(CustomTags.CONVEYOR_MODULES)
            .register();
    public static ItemEntry<ComponentItem> CONVEYOR_MODULE_ZPM = REGISTRATE
            .item("zpm_conveyor_module", ComponentItem::new)
            .localization(text -> text
                    .source("ZPM Conveyor Module")
                    .translation(MinecraftLocale.ZH_CN, "§cZPM§r传送带"))
            .onRegister(attach(new CoverPlaceBehavior(GTCovers.CONVEYORS[6])))
            .onRegister(attach(new TooltipBehavior(lines -> {
                lines.add(Component.translatable("item.gtpm.conveyor.module.tooltip"));
                lines.add(Component.translatable("gtpm.universal.tooltip.item_transfer_rate_stacks", 16));
            })))
            .tag(CustomTags.CONVEYOR_MODULES)
            .register();
    public static ItemEntry<ComponentItem> CONVEYOR_MODULE_UV = REGISTRATE
            .item("uv_conveyor_module", ComponentItem::new)
            .localization(text -> text
                    .source("UV Conveyor Module")
                    .translation(MinecraftLocale.ZH_CN, "§3UV§r传送带"))
            .onRegister(attach(new CoverPlaceBehavior(GTCovers.CONVEYORS[7])))
            .onRegister(attach(new TooltipBehavior(lines -> {
                lines.add(Component.translatable("item.gtpm.conveyor.module.tooltip"));
                lines.add(Component.translatable("gtpm.universal.tooltip.item_transfer_rate_stacks", 16));
            })))
            .tag(CustomTags.CONVEYOR_MODULES)
            .register();
    public static ItemEntry<ComponentItem> CONVEYOR_MODULE_UHV = GTCEuAPI.isHighTier() ?
            REGISTRATE.item("uhv_conveyor_module", ComponentItem::new)
                    .localization(text -> text
                            .source("UHV Conveyor Module")
                            .translation(MinecraftLocale.ZH_CN, "§4UHV§r传送带"))
                    .onRegister(attach(new CoverPlaceBehavior(GTCovers.CONVEYORS[8])))
                    .onRegister(attach(new TooltipBehavior(lines -> {
                        lines.add(Component.translatable("item.gtpm.conveyor.module.tooltip"));
                        lines.add(Component.translatable("gtpm.universal.tooltip.item_transfer_rate_stacks", 16));
                    })))
                    .register() :
            null;
    public static ItemEntry<ComponentItem> CONVEYOR_MODULE_UEV = GTCEuAPI.isHighTier() ?
            REGISTRATE.item("uev_conveyor_module", ComponentItem::new)
                    .localization(text -> text
                            .source("UEV Conveyor Module")
                            .translation(MinecraftLocale.ZH_CN, "§aUEV§r传送带"))
                    .onRegister(attach(new CoverPlaceBehavior(GTCovers.CONVEYORS[9])))
                    .onRegister(attach(new TooltipBehavior(lines -> {
                        lines.add(Component.translatable("item.gtpm.conveyor.module.tooltip"));
                        lines.add(Component.translatable("gtpm.universal.tooltip.item_transfer_rate_stacks", 16));
                    })))
                    .register() :
            null;
    public static ItemEntry<ComponentItem> CONVEYOR_MODULE_UIV = GTCEuAPI.isHighTier() ?
            REGISTRATE.item("uiv_conveyor_module", ComponentItem::new)
                    .localization(text -> text
                            .source("UIV Conveyor Module")
                            .translation(MinecraftLocale.ZH_CN, "§2UIV§r传送带"))
                    .onRegister(attach(new CoverPlaceBehavior(GTCovers.CONVEYORS[10])))
                    .onRegister(attach(new TooltipBehavior(lines -> {
                        lines.add(Component.translatable("item.gtpm.conveyor.module.tooltip"));
                        lines.add(Component.translatable("gtpm.universal.tooltip.item_transfer_rate_stacks", 16));
                    })))
                    .register() :
            null;
    public static ItemEntry<ComponentItem> CONVEYOR_MODULE_UXV = GTCEuAPI.isHighTier() ?
            REGISTRATE.item("uxv_conveyor_module", ComponentItem::new)
                    .localization(text -> text
                            .source("UXV Conveyor Module")
                            .translation(MinecraftLocale.ZH_CN, "§eUXV§r传送带"))
                    .onRegister(attach(new CoverPlaceBehavior(GTCovers.CONVEYORS[11])))
                    .onRegister(attach(new TooltipBehavior(lines -> {
                        lines.add(Component.translatable("item.gtpm.conveyor.module.tooltip"));
                        lines.add(Component.translatable("gtpm.universal.tooltip.item_transfer_rate_stacks", 16));
                    })))
                    .register() :
            null;
    public static ItemEntry<ComponentItem> CONVEYOR_MODULE_OpV = GTCEuAPI.isHighTier() ?
            REGISTRATE.item("opv_conveyor_module", ComponentItem::new)
                    .localization(text -> text
                            .source("OpV Conveyor Module")
                            .translation(MinecraftLocale.ZH_CN, "§9§lOpV§r传送带"))
                    .onRegister(attach(new CoverPlaceBehavior(GTCovers.CONVEYORS[12])))
                    .onRegister(attach(new TooltipBehavior(lines -> {
                        lines.add(Component.translatable("item.gtpm.conveyor.module.tooltip"));
                        lines.add(Component.translatable("gtpm.universal.tooltip.item_transfer_rate_stacks", 16));
                    })))
                    .register() :
            null;

    public static ItemEntry<Item> ELECTRIC_PISTON_LV = REGISTRATE.item("lv_electric_piston", Item::new)
            .localization(text -> text
                    .source("LV Electric Piston")
                    .translation(MinecraftLocale.ZH_CN, "§7LV§r电力活塞"))
            .tag(CustomTags.ELECTRIC_PISTONS)
            .register();
    public static ItemEntry<Item> ELECTRIC_PISTON_MV = REGISTRATE.item("mv_electric_piston", Item::new)
            .localization(text -> text
                    .source("MV Electric Piston")
                    .translation(MinecraftLocale.ZH_CN, "§bMV§r电力活塞"))
            .tag(CustomTags.ELECTRIC_PISTONS)
            .register();
    public static ItemEntry<Item> ELECTRIC_PISTON_HV = REGISTRATE.item("hv_electric_piston", Item::new)
            .localization(text -> text
                    .source("HV Electric Piston")
                    .translation(MinecraftLocale.ZH_CN, "§6HV§r电力活塞"))
            .tag(CustomTags.ELECTRIC_PISTONS)
            .register();
    public static ItemEntry<Item> ELECTRIC_PISTON_EV = REGISTRATE.item("ev_electric_piston", Item::new)
            .localization(text -> text
                    .source("EV Electric Piston")
                    .translation(MinecraftLocale.ZH_CN, "§5EV§r电力活塞"))
            .tag(CustomTags.ELECTRIC_PISTONS)
            .register();
    public static ItemEntry<Item> ELECTRIC_PISTON_IV = REGISTRATE.item("iv_electric_piston", Item::new)
            .localization(text -> text
                    .source("IV Electric Piston")
                    .translation(MinecraftLocale.ZH_CN, "§9IV§r电力活塞"))
            .tag(CustomTags.ELECTRIC_PISTONS)
            .register();
    public static ItemEntry<Item> ELECTRIC_PISTON_LuV = REGISTRATE.item("luv_electric_piston", Item::new)
            .localization(text -> text
                    .source("LuV Electric Piston")
                    .translation(MinecraftLocale.ZH_CN, "§dLuV§r电力活塞"))
            .tag(CustomTags.ELECTRIC_PISTONS)
            .register();
    public static ItemEntry<Item> ELECTRIC_PISTON_ZPM = REGISTRATE.item("zpm_electric_piston", Item::new)
            .localization(text -> text
                    .source("ZPM Electric Piston")
                    .translation(MinecraftLocale.ZH_CN, "§cZPM§r电力活塞"))
            .tag(CustomTags.ELECTRIC_PISTONS)
            .register();
    public static ItemEntry<Item> ELECTRIC_PISTON_UV = REGISTRATE.item("uv_electric_piston", Item::new)
            .localization(text -> text
                    .source("UV Electric Piston")
                    .translation(MinecraftLocale.ZH_CN, "§3UV§r电力活塞"))
            .tag(CustomTags.ELECTRIC_PISTONS)
            .register();
    public static ItemEntry<Item> ELECTRIC_PISTON_UHV = GTCEuAPI.isHighTier() ?
            REGISTRATE.item("uhv_electric_piston", Item::new)
                    .localization(text -> text
                            .source("UHV Electric Piston")
                            .translation(MinecraftLocale.ZH_CN, "§4UHV§r电力活塞"))
                    .register() :
            null;
    public static ItemEntry<Item> ELECTRIC_PISTON_UEV = GTCEuAPI.isHighTier() ?
            REGISTRATE.item("uev_electric_piston", Item::new)
                    .localization(text -> text
                            .source("UEV Electric Piston")
                            .translation(MinecraftLocale.ZH_CN, "§aUEV§r电力活塞"))
                    .register() :
            null;
    public static ItemEntry<Item> ELECTRIC_PISTON_UIV = GTCEuAPI.isHighTier() ?
            REGISTRATE.item("uiv_electric_piston", Item::new)
                    .localization(text -> text
                            .source("UIV Electric Piston")
                            .translation(MinecraftLocale.ZH_CN, "§2UIV§r电力活塞"))
                    .register() :
            null;
    public static ItemEntry<Item> ELECTRIC_PISTON_UXV = GTCEuAPI.isHighTier() ?
            REGISTRATE.item("uxv_electric_piston", Item::new)
                    .localization(text -> text
                            .source("UXV Electric Piston")
                            .translation(MinecraftLocale.ZH_CN, "§eUXV§r电力活塞"))
                    .register() :
            null;
    public static ItemEntry<Item> ELECTRIC_PISTON_OpV = GTCEuAPI.isHighTier() ?
            REGISTRATE.item("opv_electric_piston", Item::new)
                    .localization(text -> text
                            .source("OpV Electric Piston")
                            .translation(MinecraftLocale.ZH_CN, "§9§lOpV§r电力活塞"))
                    .register() :
            null;

    public static ItemEntry<ComponentItem> ROBOT_ARM_LV = REGISTRATE.item("lv_robot_arm", ComponentItem::new)
            .localization(text -> text
                    .source("LV Robot Arm")
                    .translation(MinecraftLocale.ZH_CN, "§7LV§r机械臂"))
            .onRegister(attach(new CoverPlaceBehavior(GTCovers.ROBOT_ARMS[0])))
            .onRegister(attach(new TooltipBehavior(lines -> {
                lines.add(Component.translatable("item.gtpm.robot.arm.tooltip"));
                lines.add(Component.translatable("gtpm.universal.tooltip.item_transfer_rate", 8));
            })))
            .tag(CustomTags.ROBOT_ARMS)
            .register();
    public static ItemEntry<ComponentItem> ROBOT_ARM_MV = REGISTRATE.item("mv_robot_arm", ComponentItem::new)
            .localization(text -> text
                    .source("MV Robot Arm")
                    .translation(MinecraftLocale.ZH_CN, "§bMV§r机械臂"))
            .onRegister(attach(new CoverPlaceBehavior(GTCovers.ROBOT_ARMS[1])))
            .onRegister(attach(new TooltipBehavior(lines -> {
                lines.add(Component.translatable("item.gtpm.robot.arm.tooltip"));
                lines.add(Component.translatable("gtpm.universal.tooltip.item_transfer_rate", 32));
            })))
            .tag(CustomTags.ROBOT_ARMS)
            .register();
    public static ItemEntry<ComponentItem> ROBOT_ARM_HV = REGISTRATE.item("hv_robot_arm", ComponentItem::new)
            .localization(text -> text
                    .source("HV Robot Arm")
                    .translation(MinecraftLocale.ZH_CN, "§6HV§r机械臂"))
            .onRegister(attach(new CoverPlaceBehavior(GTCovers.ROBOT_ARMS[2])))
            .onRegister(attach(new TooltipBehavior(lines -> {
                lines.add(Component.translatable("item.gtpm.robot.arm.tooltip"));
                lines.add(Component.translatable("gtpm.universal.tooltip.item_transfer_rate", 64));
            })))
            .tag(CustomTags.ROBOT_ARMS)
            .register();
    public static ItemEntry<ComponentItem> ROBOT_ARM_EV = REGISTRATE.item("ev_robot_arm", ComponentItem::new)
            .localization(text -> text
                    .source("EV Robot Arm")
                    .translation(MinecraftLocale.ZH_CN, "§5EV§r机械臂"))
            .onRegister(attach(new CoverPlaceBehavior(GTCovers.ROBOT_ARMS[3])))
            .onRegister(attach(new TooltipBehavior(lines -> {
                lines.add(Component.translatable("item.gtpm.robot.arm.tooltip"));
                lines.add(Component.translatable("gtpm.universal.tooltip.item_transfer_rate_stacks", 3));
            })))
            .tag(CustomTags.ROBOT_ARMS)
            .register();
    public static ItemEntry<ComponentItem> ROBOT_ARM_IV = REGISTRATE.item("iv_robot_arm", ComponentItem::new)
            .localization(text -> text
                    .source("IV Robot Arm")
                    .translation(MinecraftLocale.ZH_CN, "§9IV§r机械臂"))
            .onRegister(attach(new CoverPlaceBehavior(GTCovers.ROBOT_ARMS[4])))
            .onRegister(attach(new TooltipBehavior(lines -> {
                lines.add(Component.translatable("item.gtpm.robot.arm.tooltip"));
                lines.add(Component.translatable("gtpm.universal.tooltip.item_transfer_rate_stacks", 8));
            })))
            .tag(CustomTags.ROBOT_ARMS)
            .register();
    public static ItemEntry<ComponentItem> ROBOT_ARM_LuV = REGISTRATE.item("luv_robot_arm", ComponentItem::new)
            .localization(text -> text
                    .source("LuV Robot Arm")
                    .translation(MinecraftLocale.ZH_CN, "§dLuV§r机械臂"))
            .onRegister(attach(new CoverPlaceBehavior(GTCovers.ROBOT_ARMS[5])))
            .onRegister(attach(new TooltipBehavior(lines -> {
                lines.add(Component.translatable("item.gtpm.robot.arm.tooltip"));
                lines.add(Component.translatable("gtpm.universal.tooltip.item_transfer_rate_stacks", 16));
            })))
            .tag(CustomTags.ROBOT_ARMS)
            .register();
    public static ItemEntry<ComponentItem> ROBOT_ARM_ZPM = REGISTRATE.item("zpm_robot_arm", ComponentItem::new)
            .localization(text -> text
                    .source("ZPM Robot Arm")
                    .translation(MinecraftLocale.ZH_CN, "§cZPM§r机械臂"))
            .onRegister(attach(new CoverPlaceBehavior(GTCovers.ROBOT_ARMS[6])))
            .onRegister(attach(new TooltipBehavior(lines -> {
                lines.add(Component.translatable("item.gtpm.robot.arm.tooltip"));
                lines.add(Component.translatable("gtpm.universal.tooltip.item_transfer_rate_stacks", 16));
            })))
            .tag(CustomTags.ROBOT_ARMS)
            .register();
    public static ItemEntry<ComponentItem> ROBOT_ARM_UV = REGISTRATE.item("uv_robot_arm", ComponentItem::new)
            .localization(text -> text
                    .source("UV Robot Arm")
                    .translation(MinecraftLocale.ZH_CN, "§3UV§r机械臂"))
            .onRegister(attach(new CoverPlaceBehavior(GTCovers.ROBOT_ARMS[7])))
            .onRegister(attach(new TooltipBehavior(lines -> {
                lines.add(Component.translatable("item.gtpm.robot.arm.tooltip"));
                lines.add(Component.translatable("gtpm.universal.tooltip.item_transfer_rate_stacks", 16));
            })))
            .tag(CustomTags.ROBOT_ARMS)
            .register();
    public static ItemEntry<ComponentItem> ROBOT_ARM_UHV = GTCEuAPI.isHighTier() ?
            REGISTRATE.item("uhv_robot_arm", ComponentItem::new)
                    .localization(text -> text
                            .source("UHV Robot Arm")
                            .translation(MinecraftLocale.ZH_CN, "§4UHV§r机械臂"))
                    .onRegister(attach(new CoverPlaceBehavior(GTCovers.ROBOT_ARMS[8])))
                    .onRegister(attach(new TooltipBehavior(lines -> {
                        lines.add(Component.translatable("item.gtpm.robot.arm.tooltip"));
                        lines.add(Component.translatable("gtpm.universal.tooltip.item_transfer_rate_stacks", 16));
                    })))
                    .register() :
            null;
    public static ItemEntry<ComponentItem> ROBOT_ARM_UEV = GTCEuAPI.isHighTier() ?
            REGISTRATE.item("uev_robot_arm", ComponentItem::new)
                    .localization(text -> text
                            .source("UEV Robot Arm")
                            .translation(MinecraftLocale.ZH_CN, "§aUEV§r机械臂"))
                    .onRegister(attach(new CoverPlaceBehavior(GTCovers.ROBOT_ARMS[9])))
                    .onRegister(attach(new TooltipBehavior(lines -> {
                        lines.add(Component.translatable("item.gtpm.robot.arm.tooltip"));
                        lines.add(Component.translatable("gtpm.universal.tooltip.item_transfer_rate_stacks", 16));
                    })))
                    .register() :
            null;
    public static ItemEntry<ComponentItem> ROBOT_ARM_UIV = GTCEuAPI.isHighTier() ?
            REGISTRATE.item("uiv_robot_arm", ComponentItem::new)
                    .localization(text -> text
                            .source("UIV Robot Arm")
                            .translation(MinecraftLocale.ZH_CN, "§2UIV§r机械臂"))
                    .onRegister(attach(new CoverPlaceBehavior(GTCovers.ROBOT_ARMS[10])))
                    .onRegister(attach(new TooltipBehavior(lines -> {
                        lines.add(Component.translatable("item.gtpm.robot.arm.tooltip"));
                        lines.add(Component.translatable("gtpm.universal.tooltip.item_transfer_rate_stacks", 16));
                    })))
                    .register() :
            null;
    public static ItemEntry<ComponentItem> ROBOT_ARM_UXV = GTCEuAPI.isHighTier() ?
            REGISTRATE.item("uxv_robot_arm", ComponentItem::new)
                    .localization(text -> text
                            .source("UXV Robot Arm")
                            .translation(MinecraftLocale.ZH_CN, "§eUXV§r机械臂"))
                    .onRegister(attach(new CoverPlaceBehavior(GTCovers.ROBOT_ARMS[11])))
                    .onRegister(attach(new TooltipBehavior(lines -> {
                        lines.add(Component.translatable("item.gtpm.robot.arm.tooltip"));
                        lines.add(Component.translatable("gtpm.universal.tooltip.item_transfer_rate_stacks", 16));
                    })))
                    .register() :
            null;
    public static ItemEntry<ComponentItem> ROBOT_ARM_OpV = GTCEuAPI.isHighTier() ?
            REGISTRATE.item("opv_robot_arm", ComponentItem::new)
                    .localization(text -> text
                            .source("OpV Robot Arm")
                            .translation(MinecraftLocale.ZH_CN, "§9§lOpV§r机械臂"))
                    .onRegister(attach(new CoverPlaceBehavior(GTCovers.ROBOT_ARMS[12])))
                    .onRegister(attach(new TooltipBehavior(lines -> {
                        lines.add(Component.translatable("item.gtpm.robot.arm.tooltip"));
                        lines.add(Component.translatable("gtpm.universal.tooltip.item_transfer_rate_stacks", 16));
                    })))
                    .register() :
            null;

    public static ItemEntry<Item> FIELD_GENERATOR_LV = REGISTRATE.item("lv_field_generator", Item::new)
            .localization(text -> text
                    .source("LV Field Generator")
                    .translation(MinecraftLocale.ZH_CN, "§7LV§r力场发生器"))
            .tag(CustomTags.FIELD_GENERATORS)
            .register();
    public static ItemEntry<Item> FIELD_GENERATOR_MV = REGISTRATE.item("mv_field_generator", Item::new)
            .localization(text -> text
                    .source("MV Field Generator")
                    .translation(MinecraftLocale.ZH_CN, "§bMV§r力场发生器"))
            .tag(CustomTags.FIELD_GENERATORS)
            .register();
    public static ItemEntry<Item> FIELD_GENERATOR_HV = REGISTRATE.item("hv_field_generator", Item::new)
            .localization(text -> text
                    .source("HV Field Generator")
                    .translation(MinecraftLocale.ZH_CN, "§6HV§r力场发生器"))
            .tag(CustomTags.FIELD_GENERATORS)
            .register();
    public static ItemEntry<Item> FIELD_GENERATOR_EV = REGISTRATE.item("ev_field_generator", Item::new)
            .localization(text -> text
                    .source("EV Field Generator")
                    .translation(MinecraftLocale.ZH_CN, "§5EV§r力场发生器"))
            .tag(CustomTags.FIELD_GENERATORS)
            .register();
    public static ItemEntry<Item> FIELD_GENERATOR_IV = REGISTRATE.item("iv_field_generator", Item::new)
            .localization(text -> text
                    .source("IV Field Generator")
                    .translation(MinecraftLocale.ZH_CN, "§9IV§r力场发生器"))
            .tag(CustomTags.FIELD_GENERATORS)
            .register();
    public static ItemEntry<Item> FIELD_GENERATOR_LuV = REGISTRATE.item("luv_field_generator", Item::new)
            .localization(text -> text
                    .source("LuV Field Generator")
                    .translation(MinecraftLocale.ZH_CN, "§dLuV§r力场发生器"))
            .tag(CustomTags.FIELD_GENERATORS)
            .register();
    public static ItemEntry<Item> FIELD_GENERATOR_ZPM = REGISTRATE.item("zpm_field_generator", Item::new)
            .localization(text -> text
                    .source("ZPM Field Generator")
                    .translation(MinecraftLocale.ZH_CN, "§cZPM§r力场发生器"))
            .tag(CustomTags.FIELD_GENERATORS)
            .register();
    public static ItemEntry<Item> FIELD_GENERATOR_UV = REGISTRATE.item("uv_field_generator", Item::new)
            .localization(text -> text
                    .source("UV Field Generator")
                    .translation(MinecraftLocale.ZH_CN, "§3UV§r力场发生器"))
            .tag(CustomTags.FIELD_GENERATORS)
            .register();
    public static ItemEntry<Item> FIELD_GENERATOR_UHV = GTCEuAPI.isHighTier() ?
            REGISTRATE.item("uhv_field_generator", Item::new)
                    .localization(text -> text
                            .source("UHV Field Generator")
                            .translation(MinecraftLocale.ZH_CN, "§4UHV§r力场发生器"))
                    .register() :
            null;
    public static ItemEntry<Item> FIELD_GENERATOR_UEV = GTCEuAPI.isHighTier() ?
            REGISTRATE.item("uev_field_generator", Item::new)
                    .localization(text -> text
                            .source("UEV Field Generator")
                            .translation(MinecraftLocale.ZH_CN, "§aUEV§r力场发生器"))
                    .register() :
            null;
    public static ItemEntry<Item> FIELD_GENERATOR_UIV = GTCEuAPI.isHighTier() ?
            REGISTRATE.item("uiv_field_generator", Item::new)
                    .localization(text -> text
                            .source("UIV Field Generator")
                            .translation(MinecraftLocale.ZH_CN, "§2UIV§r力场发生器"))
                    .register() :
            null;
    public static ItemEntry<Item> FIELD_GENERATOR_UXV = GTCEuAPI.isHighTier() ?
            REGISTRATE.item("uxv_field_generator", Item::new)
                    .localization(text -> text
                            .source("UXV Field Generator")
                            .translation(MinecraftLocale.ZH_CN, "§eUXV§r力场发生器"))
                    .register() :
            null;
    public static ItemEntry<Item> FIELD_GENERATOR_OpV = GTCEuAPI.isHighTier() ?
            REGISTRATE.item("opv_field_generator", Item::new)
                    .localization(text -> text
                            .source("OpV Field Generator")
                            .translation(MinecraftLocale.ZH_CN, "§9§lOpV§r力场发生器"))
                    .register() :
            null;

    public static ItemEntry<Item> EMITTER_LV = REGISTRATE.item("lv_emitter", Item::new)
            .localization(text -> text
                    .source("LV Emitter")
                    .translation(MinecraftLocale.ZH_CN, "§7LV§r发射器"))
            .tag(CustomTags.EMITTERS)
            .register();
    public static ItemEntry<Item> EMITTER_MV = REGISTRATE.item("mv_emitter", Item::new)
            .localization(text -> text
                    .source("MV Emitter")
                    .translation(MinecraftLocale.ZH_CN, "§bMV§r发射器"))
            .tag(CustomTags.EMITTERS)
            .register();
    public static ItemEntry<Item> EMITTER_HV = REGISTRATE.item("hv_emitter", Item::new)
            .localization(text -> text
                    .source("HV Emitter")
                    .translation(MinecraftLocale.ZH_CN, "§6HV§r发射器"))
            .tag(CustomTags.EMITTERS)
            .register();
    public static ItemEntry<Item> EMITTER_EV = REGISTRATE.item("ev_emitter", Item::new)
            .localization(text -> text
                    .source("EV Emitter")
                    .translation(MinecraftLocale.ZH_CN, "§5EV§r发射器"))
            .tag(CustomTags.EMITTERS)
            .register();
    public static ItemEntry<Item> EMITTER_IV = REGISTRATE.item("iv_emitter", Item::new)
            .localization(text -> text
                    .source("IV Emitter")
                    .translation(MinecraftLocale.ZH_CN, "§9IV§r发射器"))
            .tag(CustomTags.EMITTERS)
            .register();
    public static ItemEntry<Item> EMITTER_LuV = REGISTRATE.item("luv_emitter", Item::new)
            .localization(text -> text
                    .source("LuV Emitter")
                    .translation(MinecraftLocale.ZH_CN, "§dLuV§r发射器"))
            .tag(CustomTags.EMITTERS)
            .register();
    public static ItemEntry<Item> EMITTER_ZPM = REGISTRATE.item("zpm_emitter", Item::new)
            .localization(text -> text
                    .source("ZPM Emitter")
                    .translation(MinecraftLocale.ZH_CN, "§cZPM§r发射器"))
            .tag(CustomTags.EMITTERS)
            .register();
    public static ItemEntry<Item> EMITTER_UV = REGISTRATE.item("uv_emitter", Item::new)
            .localization(text -> text
                    .source("UV Emitter")
                    .translation(MinecraftLocale.ZH_CN, "§3UV§r发射器"))
            .tag(CustomTags.EMITTERS)
            .register();
    public static ItemEntry<Item> EMITTER_UHV = GTCEuAPI.isHighTier() ? REGISTRATE.item("uhv_emitter", Item::new)
            .localization(text -> text
                    .source("UHV Emitter")
                    .translation(MinecraftLocale.ZH_CN, "§4UHV§r发射器"))
            .register() : null;
    public static ItemEntry<Item> EMITTER_UEV = GTCEuAPI.isHighTier() ? REGISTRATE.item("uev_emitter", Item::new)
            .localization(text -> text
                    .source("UEV Emitter")
                    .translation(MinecraftLocale.ZH_CN, "§aUEV§r发射器"))
            .register() : null;
    public static ItemEntry<Item> EMITTER_UIV = GTCEuAPI.isHighTier() ? REGISTRATE.item("uiv_emitter", Item::new)
            .localization(text -> text
                    .source("UIV Emitter")
                    .translation(MinecraftLocale.ZH_CN, "§2UIV§r发射器"))
            .register() : null;
    public static ItemEntry<Item> EMITTER_UXV = GTCEuAPI.isHighTier() ? REGISTRATE.item("uxv_emitter", Item::new)
            .localization(text -> text
                    .source("UXV Emitter")
                    .translation(MinecraftLocale.ZH_CN, "§eUXV§r发射器"))
            .register() : null;
    public static ItemEntry<Item> EMITTER_OpV = GTCEuAPI.isHighTier() ? REGISTRATE.item("opv_emitter", Item::new)
            .localization(text -> text
                    .source("OpV Emitter")
                    .translation(MinecraftLocale.ZH_CN, "§9OpV§r发射器"))
            .register() : null;

    public static ItemEntry<Item> SENSOR_LV = REGISTRATE.item("lv_sensor", Item::new)
            .localization(text -> text
                    .source("LV Sensor")
                    .translation(MinecraftLocale.ZH_CN, "§7LV§r传感器"))
            .tag(CustomTags.SENSORS)
            .register();
    public static ItemEntry<Item> SENSOR_MV = REGISTRATE.item("mv_sensor", Item::new)
            .localization(text -> text
                    .source("MV Sensor")
                    .translation(MinecraftLocale.ZH_CN, "§bMV§r传感器"))
            .tag(CustomTags.SENSORS)
            .register();
    public static ItemEntry<Item> SENSOR_HV = REGISTRATE.item("hv_sensor", Item::new)
            .localization(text -> text
                    .source("HV Sensor")
                    .translation(MinecraftLocale.ZH_CN, "§6HV§r传感器"))
            .tag(CustomTags.SENSORS)
            .register();
    public static ItemEntry<Item> SENSOR_EV = REGISTRATE.item("ev_sensor", Item::new)
            .localization(text -> text
                    .source("EV Sensor")
                    .translation(MinecraftLocale.ZH_CN, "§5EV§r传感器"))
            .tag(CustomTags.SENSORS)
            .register();
    public static ItemEntry<Item> SENSOR_IV = REGISTRATE.item("iv_sensor", Item::new)
            .localization(text -> text
                    .source("IV Sensor")
                    .translation(MinecraftLocale.ZH_CN, "§9IV§r传感器"))
            .tag(CustomTags.SENSORS)
            .register();
    public static ItemEntry<Item> SENSOR_LuV = REGISTRATE.item("luv_sensor", Item::new)
            .localization(text -> text
                    .source("LuV Sensor")
                    .translation(MinecraftLocale.ZH_CN, "§dLuV§r传感器"))
            .tag(CustomTags.SENSORS)
            .register();
    public static ItemEntry<Item> SENSOR_ZPM = REGISTRATE.item("zpm_sensor", Item::new)
            .localization(text -> text
                    .source("ZPM Sensor")
                    .translation(MinecraftLocale.ZH_CN, "§cZPM§r传感器"))
            .tag(CustomTags.SENSORS)
            .register();
    public static ItemEntry<Item> SENSOR_UV = REGISTRATE.item("uv_sensor", Item::new)
            .localization(text -> text
                    .source("UV Sensor")
                    .translation(MinecraftLocale.ZH_CN, "§3UV§r传感器"))
            .tag(CustomTags.SENSORS)
            .register();
    public static ItemEntry<Item> SENSOR_UHV = GTCEuAPI.isHighTier() ? REGISTRATE.item("uhv_sensor", Item::new)
            .localization(text -> text
                    .source("UHV Sensor")
                    .translation(MinecraftLocale.ZH_CN, "§4UHV§r传感器"))
            .register() : null;
    public static ItemEntry<Item> SENSOR_UEV = GTCEuAPI.isHighTier() ? REGISTRATE.item("uev_sensor", Item::new)
            .localization(text -> text
                    .source("UEV Sensor")
                    .translation(MinecraftLocale.ZH_CN, "§aUEV§r传感器"))
            .register() : null;
    public static ItemEntry<Item> SENSOR_UIV = GTCEuAPI.isHighTier() ? REGISTRATE.item("uiv_sensor", Item::new)
            .localization(text -> text
                    .source("UIV Sensor")
                    .translation(MinecraftLocale.ZH_CN, "§2UIV§r传感器"))
            .register() : null;
    public static ItemEntry<Item> SENSOR_UXV = GTCEuAPI.isHighTier() ? REGISTRATE.item("uxv_sensor", Item::new)
            .localization(text -> text
                    .source("UXV Sensor")
                    .translation(MinecraftLocale.ZH_CN, "§eUXV§r传感器"))
            .register() : null;
    public static ItemEntry<Item> SENSOR_OpV = GTCEuAPI.isHighTier() ? REGISTRATE.item("opv_sensor", Item::new)
            .localization(text -> text
                    .source("OpV Sensor")
                    .translation(MinecraftLocale.ZH_CN, "§9§lOpV§r传感器"))
            .register() : null;

    @Trans(key = "item.gtpm.data_stick.tooltip",
           source = "§7A Low Capacity Data Storage",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7小容量的数据容器"))
    public static ItemEntry<ComponentItem> TOOL_DATA_STICK = REGISTRATE.item("data_stick", ComponentItem::new)
            .localization(text -> text
                    .source("Data Stick")
                    .translation(MinecraftLocale.ZH_CN, "闪存"))
            .onRegister(attach(DataItemBehavior.INSTANCE))
            .properties(p -> p.component(GTDataComponents.DATA_ITEM, new DataItem(false, 8)))
            .register();
    @Trans(key = "item.gtpm.data_orb.tooltip",
           source = "§7A High Capacity Data Storage",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7大容量的数据容器"))
    public static ItemEntry<ComponentItem> TOOL_DATA_ORB = REGISTRATE.item("data_orb", ComponentItem::new)
            .localization(text -> text
                    .source("Data Orb")
                    .translation(MinecraftLocale.ZH_CN, "数据球"))
            .onRegister(attach(DataItemBehavior.INSTANCE))
            .properties(p -> p.component(GTDataComponents.DATA_ITEM, new DataItem(false, 64)))
            .register();
    public static ItemEntry<ComponentItem> TOOL_DATA_MODULE = REGISTRATE.item("data_module", ComponentItem::new)
            .localization(text -> text
                    .source("Data Module")
                    .translation(MinecraftLocale.ZH_CN, "数据模块"))
            .onRegister(attach(DataItemBehavior.INSTANCE))
            .properties(p -> p.component(GTDataComponents.DATA_ITEM, new DataItem(true, 256)))
            .register();

    @Trans(key = "item.glass_lens",
           source = "Glass Lens (White)",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "玻璃透镜（白色）"))
    public static final Map<MarkerMaterial, ItemEntry<Item>> GLASS_LENSES = new HashMap<>();

    static {
        for (int i = 0; i < MarkerMaterials.Color.VALUES.length; i++) {
            MarkerMaterial color = MarkerMaterials.Color.VALUES[i];
            if (color != MarkerMaterials.Color.White) {
                GLASS_LENSES.put(color, REGISTRATE.item(String.format("%s_glass_lens", color.getName()), Item::new)
                        .localization(text -> {
                            text.source("Glass Lens (%s)".formatted(toEnglishName(color.getName())));
                            var localization = color.getLocalization();
                            if (localization != null) {
                                String chinese = localization.translations().get(MinecraftLocale.ZH_CN);
                                if (chinese != null)
                                    text.translation(MinecraftLocale.ZH_CN, "玻璃透镜（%s）".formatted(chinese));
                            }
                        })
                        .transform(unificationItem(TagPrefix.lens, color))
                        .register());
            }
        }
    }

    @Trans(key = "item.gtpm.silicon_boule.tooltip",
           source = "§7Raw Circuit",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7电路原料"))
    public static ItemEntry<Item> SILICON_BOULE = REGISTRATE.item("silicon_boule", Item::new)
            .localization(text -> text
                    .source("Monocrystalline Silicon Boule")
                    .translation(MinecraftLocale.ZH_CN, "单晶硅"))
            .register();
    @Trans(key = "item.gtpm.phosphorus_boule.tooltip",
           source = "§7Raw Circuit",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7电路原料"))
    public static ItemEntry<Item> PHOSPHORUS_BOULE = REGISTRATE.item("phosphorus_boule", Item::new)
            .localization(text -> text
                    .source("Phosphorus-doped Monocrystalline Silicon Boule")
                    .translation(MinecraftLocale.ZH_CN, "磷掺杂的单晶硅"))
            .register();
    @Trans(key = "item.gtpm.naquadah_boule.tooltip",
           source = "§7Raw Circuit",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7电路原料"))
    public static ItemEntry<Item> NAQUADAH_BOULE = REGISTRATE.item("naquadah_boule", Item::new)
            .localization(text -> text
                    .source("Naquadah-doped Monocrystalline Silicon Boule")
                    .translation(MinecraftLocale.ZH_CN, "硅岩掺杂的单晶硅"))
            .register();
    @Trans(key = "item.gtpm.neutronium_boule.tooltip",
           source = "§7Raw Circuit",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7电路原料"))
    public static ItemEntry<Item> NEUTRONIUM_BOULE = REGISTRATE.item("neutronium_boule", Item::new)
            .localization(text -> text
                    .source("Neutronium-doped Monocrystalline Silicon Boule")
                    .translation(MinecraftLocale.ZH_CN, "中子素掺杂的单晶硅"))
            .register();
    @Trans(key = "item.gtpm.silicon_wafer.tooltip",
           source = "§7Raw Circuit",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7电路原料"))
    public static ItemEntry<Item> SILICON_WAFER = REGISTRATE.item("silicon_wafer", Item::new)
            .localization(text -> text
                    .source("Silicon Wafer")
                    .translation(MinecraftLocale.ZH_CN, "硅晶圆"))
            .register();
    @Trans(key = "item.gtpm.phosphorus_wafer.tooltip",
           source = "§7Raw Circuit",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7电路原料"))
    public static ItemEntry<Item> PHOSPHORUS_WAFER = REGISTRATE.item("phosphorus_wafer", Item::new)
            .localization(text -> text
                    .source("Phosphorus-doped Wafer")
                    .translation(MinecraftLocale.ZH_CN, "磷掺杂的晶圆"))
            .register();
    @Trans(key = "item.gtpm.naquadah_wafer.tooltip",
           source = "§7Raw Circuit",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7电路原料"))
    public static ItemEntry<Item> NAQUADAH_WAFER = REGISTRATE.item("naquadah_wafer", Item::new)
            .localization(text -> text
                    .source("Naquadah-doped Wafer")
                    .translation(MinecraftLocale.ZH_CN, "硅岩掺杂的晶圆"))
            .register();
    @Trans(key = "item.gtpm.neutronium_wafer.tooltip",
           source = "§7Raw Circuit",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7电路原料"))
    public static ItemEntry<Item> NEUTRONIUM_WAFER = REGISTRATE.item("neutronium_wafer", Item::new)
            .localization(text -> text
                    .source("Neutronium-doped Wafer")
                    .translation(MinecraftLocale.ZH_CN, "中子素掺杂的晶圆"))
            .register();

    @Trans(key = "item.gtpm.cpu_wafer.tooltip",
           source = "§7Raw Processing Unit",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7中央处理器原料"))
    public static ItemEntry<Item> CENTRAL_PROCESSING_UNIT_WAFER = REGISTRATE.item("cpu_wafer", Item::new)
            .localization(text -> text
                    .source("CPU Wafer")
                    .translation(MinecraftLocale.ZH_CN, "CPU晶圆"))
            .register();
    @Trans(key = "item.gtpm.ram_wafer.tooltip",
           source = "§7Raw Memory",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7内存原料"))
    public static ItemEntry<Item> RANDOM_ACCESS_MEMORY_WAFER = REGISTRATE.item("ram_wafer", Item::new)
            .localization(text -> text
                    .source("RAM Wafer")
                    .translation(MinecraftLocale.ZH_CN, "RAM晶圆"))
            .register();
    @Trans(key = "item.gtpm.ilc_wafer.tooltip",
           source = "§7Raw Integrated Circuit",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7集成电路原料"))
    public static ItemEntry<Item> INTEGRATED_LOGIC_CIRCUIT_WAFER = REGISTRATE.item("ilc_wafer", Item::new)
            .localization(text -> text
                    .source("ILC Wafer")
                    .translation(MinecraftLocale.ZH_CN, "ILC晶圆"))
            .register();
    @Trans(key = "item.gtpm.nano_cpu_wafer.tooltip",
           source = "§7Raw Nano Circuit",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7纳米电路原料"))
    public static ItemEntry<Item> NANO_CENTRAL_PROCESSING_UNIT_WAFER = REGISTRATE.item("nano_cpu_wafer", Item::new)
            .localization(text -> text
                    .source("Nano CPU Wafer")
                    .translation(MinecraftLocale.ZH_CN, "纳米CPU晶圆"))
            .register();
    @Trans(key = "item.gtpm.qbit_cpu_wafer.tooltip",
           source = "§7Raw Qubit Circuit",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7量子位电路原料"))
    public static ItemEntry<Item> QUBIT_CENTRAL_PROCESSING_UNIT_WAFER = REGISTRATE.item("qbit_cpu_wafer", Item::new)
            .localization(text -> text
                    .source("Qubit CPU Wafer")
                    .translation(MinecraftLocale.ZH_CN, "量子位CPU晶圆"))
            .register();
    @Trans(key = "item.gtpm.simple_soc_wafer.tooltip",
           source = "§7Raw Simple Circuit",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7简易电路原料"))
    public static ItemEntry<Item> SIMPLE_SYSTEM_ON_CHIP_WAFER = REGISTRATE.item("simple_soc_wafer", Item::new)
            .localization(text -> text
                    .source("Simple SoC Wafer")
                    .translation(MinecraftLocale.ZH_CN, "简易SoC晶圆"))
            .register();
    @Trans(key = "item.gtpm.soc_wafer.tooltip",
           source = "§7Raw Basic Circuit",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7基础电路原料"))
    public static ItemEntry<Item> SYSTEM_ON_CHIP_WAFER = REGISTRATE.item("soc_wafer", Item::new)
            .localization(text -> text
                    .source("SoC Wafer")
                    .translation(MinecraftLocale.ZH_CN, "SoC晶圆"))
            .register();
    @Trans(key = "item.gtpm.advanced_soc_wafer.tooltip",
           source = "§7Raw Advanced Circuit",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7先进电路原料"))
    public static ItemEntry<Item> ADVANCED_SYSTEM_ON_CHIP_WAFER = REGISTRATE.item("advanced_soc_wafer", Item::new)
            .localization(text -> text
                    .source("ASoC Wafer")
                    .translation(MinecraftLocale.ZH_CN, "ASoC晶圆"))
            .register();
    @Trans(key = "item.gtpm.highly_advanced_soc_wafer.tooltip",
           source = "§7Raw Highly Advanced Circuit",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7高度先进的电路原料"))
    public static ItemEntry<Item> HIGHLY_ADVANCED_SOC_WAFER = REGISTRATE.item("highly_advanced_soc_wafer", Item::new)
            .localization(text -> text
                    .source("HASoC Wafer")
                    .translation(MinecraftLocale.ZH_CN, "HASoC晶圆"))
            .register();
    @Trans(key = "item.gtpm.nand_memory_wafer.tooltip",
           source = "§7Raw Logic Gate",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7逻辑门原料"))
    public static ItemEntry<Item> NAND_MEMORY_CHIP_WAFER = REGISTRATE.item("nand_memory_wafer", Item::new)
            .localization(text -> text
                    .source("NAND Memory Wafer")
                    .translation(MinecraftLocale.ZH_CN, "NAND存储器晶圆"))
            .register();
    @Trans(key = "item.gtpm.nor_memory_wafer.tooltip",
           source = "§7Raw Logic Gate",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7逻辑门原料"))
    public static ItemEntry<Item> NOR_MEMORY_CHIP_WAFER = REGISTRATE.item("nor_memory_wafer", Item::new)
            .localization(text -> text
                    .source("NOR Memory Wafer")
                    .translation(MinecraftLocale.ZH_CN, "NOR存储器晶圆"))
            .register();
    @Trans(key = "item.gtpm.ulpic_wafer.tooltip",
           source = "§7Raw Ultra Low Power Circuit",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7超低功率集成电路原料"))
    public static ItemEntry<Item> ULTRA_LOW_POWER_INTEGRATED_CIRCUIT_WAFER = REGISTRATE.item("ulpic_wafer", Item::new)
            .localization(text -> text
                    .source("ULPIC Wafer")
                    .translation(MinecraftLocale.ZH_CN, "ULPIC晶圆"))
            .register();
    @Trans(key = "item.gtpm.lpic_wafer.tooltip",
           source = "§7Raw Low Power Circuit",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7低功率集成电路原料"))
    public static ItemEntry<Item> LOW_POWER_INTEGRATED_CIRCUIT_WAFER = REGISTRATE.item("lpic_wafer", Item::new)
            .localization(text -> text
                    .source("LPIC Wafer")
                    .translation(MinecraftLocale.ZH_CN, "LPIC晶圆"))
            .register();
    @Trans(key = "item.gtpm.mpic_wafer.tooltip",
           source = "§7Raw Power Circuit",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7功率集成电路原料"))
    public static ItemEntry<Item> POWER_INTEGRATED_CIRCUIT_WAFER = REGISTRATE.item("mpic_wafer", Item::new)
            .localization(text -> text
                    .source("MPIC Wafer")
                    .translation(MinecraftLocale.ZH_CN, "MPIC晶圆"))
            .register();
    @Trans(key = "item.gtpm.hpic_wafer.tooltip",
           source = "§7Raw High Power Circuit",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7高功率集成电路原料"))
    public static ItemEntry<Item> HIGH_POWER_INTEGRATED_CIRCUIT_WAFER = REGISTRATE.item("hpic_wafer", Item::new)
            .localization(text -> text
                    .source("HPIC Wafer")
                    .translation(MinecraftLocale.ZH_CN, "HPIC晶圆"))
            .register();
    @Trans(key = "item.gtpm.uhpic_wafer.tooltip",
           source = "§7Raw Ultra High Power Circuit",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7超高功率集成电路原料"))
    public static ItemEntry<Item> ULTRA_HIGH_POWER_INTEGRATED_CIRCUIT_WAFER = REGISTRATE.item("uhpic_wafer", Item::new)
            .localization(text -> text
                    .source("UHPIC Wafer")
                    .translation(MinecraftLocale.ZH_CN, "UHPIC晶圆"))
            .register();

    @Trans(key = "item.gtpm.engraved_crystal_chip.tooltip",
           source = "§7Needed for Circuits",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7晶体电路所需"))
    public static ItemEntry<Item> ENGRAVED_CRYSTAL_CHIP = REGISTRATE.item("engraved_crystal_chip", Item::new)
            .localization(text -> text
                    .source("Engraved Crystal Chip")
                    .translation(MinecraftLocale.ZH_CN, "刻蚀水晶芯片"))
            .register();
    public static ItemEntry<Item> ENGRAVED_LAPOTRON_CHIP = REGISTRATE.item("engraved_lapotron_crystal_chip", Item::new)
            .localization(text -> text
                    .source("Engraved Lapotron Crystal Chip")
                    .translation(MinecraftLocale.ZH_CN, "刻蚀兰波顿芯片"))
            .register();

    @Trans(key = "item.gtpm.cpu_chip.tooltip",
           source = "§7Central Processing Unit",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7中央处理器"))
    public static ItemEntry<Item> CENTRAL_PROCESSING_UNIT = REGISTRATE.item("cpu_chip", Item::new)
            .localization(text -> text
                    .source("CPU Chip")
                    .translation(MinecraftLocale.ZH_CN, "CPU芯片"))
            .register();
    @Trans(key = "item.gtpm.ram_chip.tooltip",
           source = "§7Random Access Memory",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7随机存取存储器"))
    public static ItemEntry<Item> RANDOM_ACCESS_MEMORY = REGISTRATE.item("ram_chip", Item::new)
            .localization(text -> text
                    .source("RAM Chip")
                    .translation(MinecraftLocale.ZH_CN, "RAM芯片"))
            .register();
    @Trans(key = "item.gtpm.ilc_chip.tooltip",
           source = "§7Integrated Logic Circuit",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7集成逻辑电路"))
    public static ItemEntry<Item> INTEGRATED_LOGIC_CIRCUIT = REGISTRATE.item("ilc_chip", Item::new)
            .localization(text -> text
                    .source("IC Chip")
                    .translation(MinecraftLocale.ZH_CN, "IC芯片"))
            .register();
    @Trans(key = "item.gtpm.nano_cpu_chip.tooltip",
           source = "§7Nano Central Processing Unit",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7纳米中央处理器"))
    public static ItemEntry<Item> NANO_CENTRAL_PROCESSING_UNIT = REGISTRATE.item("nano_cpu_chip", Item::new)
            .localization(text -> text
                    .source("Nano CPU Chip")
                    .translation(MinecraftLocale.ZH_CN, "纳米CPU芯片"))
            .register();
    @Trans(key = "item.gtpm.qbit_cpu_chip.tooltip",
           source = "§7Qubit Central Processing Unit",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7量子位中央处理器"))
    public static ItemEntry<Item> QUBIT_CENTRAL_PROCESSING_UNIT = REGISTRATE.item("qbit_cpu_chip", Item::new)
            .localization(text -> text
                    .source("Qubit CPU Chip")
                    .translation(MinecraftLocale.ZH_CN, "量子位CPU芯片"))
            .register();
    @Trans(key = "item.gtpm.simple_soc.tooltip",
           source = "§7Simple System on Chip",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7简易系统级芯片"))
    public static ItemEntry<Item> SIMPLE_SYSTEM_ON_CHIP = REGISTRATE.item("simple_soc", Item::new)
            .localization(text -> text
                    .source("Simple SoC")
                    .translation(MinecraftLocale.ZH_CN, "简易SoC"))
            .register();
    @Trans(key = "item.gtpm.soc.tooltip",
           source = "§7System on Chip",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7系统级芯片"))
    public static ItemEntry<Item> SYSTEM_ON_CHIP = REGISTRATE.item("soc", Item::new)
            .localization(text -> text
                    .source("SoC")
                    .translation(MinecraftLocale.ZH_CN, "SoC"))
            .register();
    @Trans(key = "item.gtpm.advanced_soc.tooltip",
           source = "§7Advanced System on Chip",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7先进系统级芯片"))
    public static ItemEntry<Item> ADVANCED_SYSTEM_ON_CHIP = REGISTRATE.item("advanced_soc", Item::new)
            .localization(text -> text
                    .source("ASoC")
                    .translation(MinecraftLocale.ZH_CN, "ASoC"))
            .register();
    @Trans(key = "item.gtpm.highly_advanced_soc.tooltip",
           source = "§7Highly Advanced System on Chip",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7高度先进的系统级芯片"))
    public static ItemEntry<Item> HIGHLY_ADVANCED_SOC = REGISTRATE.item("highly_advanced_soc", Item::new)
            .localization(text -> text
                    .source("HASoC")
                    .translation(MinecraftLocale.ZH_CN, "HASoC"))
            .register();
    @Trans(key = "item.gtpm.nand_memory_chip.tooltip",
           source = "§7NAND Logic Gate",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7与非逻辑门"))
    public static ItemEntry<Item> NAND_MEMORY_CHIP = REGISTRATE.item("nand_memory_chip", Item::new)
            .localization(text -> text
                    .source("NAND Memory Chip")
                    .translation(MinecraftLocale.ZH_CN, "NAND存储器芯片"))
            .register();
    @Trans(key = "item.gtpm.nor_memory_chip.tooltip",
           source = "§7NOR Logic Gate",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7或非逻辑门"))
    public static ItemEntry<Item> NOR_MEMORY_CHIP = REGISTRATE.item("nor_memory_chip", Item::new)
            .localization(text -> text
                    .source("NOR Memory Chip")
                    .translation(MinecraftLocale.ZH_CN, "NOR存储器芯片"))
            .register();
    @Trans(key = "item.gtpm.ulpic_chip.tooltip",
           source = "§7Ultra Low Power IC",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7超低功率集成电路"))
    public static ItemEntry<Item> ULTRA_LOW_POWER_INTEGRATED_CIRCUIT = REGISTRATE.item("ulpic_chip", Item::new)
            .localization(text -> text
                    .source("ULPIC Chip")
                    .translation(MinecraftLocale.ZH_CN, "ULPIC芯片"))
            .register();
    @Trans(key = "item.gtpm.lpic_chip.tooltip",
           source = "§7Low Power IC",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7低功率集成电路"))
    public static ItemEntry<Item> LOW_POWER_INTEGRATED_CIRCUIT = REGISTRATE.item("lpic_chip", Item::new)
            .localization(text -> text
                    .source("LPIC Chip")
                    .translation(MinecraftLocale.ZH_CN, "LPIC芯片"))
            .register();
    @Trans(key = "item.gtpm.mpic_chip.tooltip",
           source = "§7Power IC",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7功率集成电路"))
    public static ItemEntry<Item> POWER_INTEGRATED_CIRCUIT = REGISTRATE.item("mpic_chip", Item::new)
            .localization(text -> text
                    .source("MPIC Chip")
                    .translation(MinecraftLocale.ZH_CN, "MPIC芯片"))
            .register();
    @Trans(key = "item.gtpm.hpic_chip.tooltip",
           source = "§7High Power IC",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7高功率集成电路"))
    public static ItemEntry<Item> HIGH_POWER_INTEGRATED_CIRCUIT = REGISTRATE.item("hpic_chip", Item::new)
            .localization(text -> text
                    .source("HPIC Chip")
                    .translation(MinecraftLocale.ZH_CN, "HPIC芯片"))
            .register();
    @Trans(key = "item.gtpm.uhpic_chip.tooltip",
           source = "§7Ultra High Power IC",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7超高功率集成电路"))
    public static ItemEntry<Item> ULTRA_HIGH_POWER_INTEGRATED_CIRCUIT = REGISTRATE.item("uhpic_chip", Item::new)
            .localization(text -> text
                    .source("UHPIC Chip")
                    .translation(MinecraftLocale.ZH_CN, "UHPIC芯片"))
            .register();

    @Trans(key = "item.gtpm.raw_crystal_chip.tooltip",
           source = "§7Raw Crystal Processor",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7晶体处理器原料"))
    public static ItemEntry<Item> RAW_CRYSTAL_CHIP = REGISTRATE.item("raw_crystal_chip", Item::new)
            .localization(text -> text
                    .source("Raw Crystal Chip")
                    .translation(MinecraftLocale.ZH_CN, "晶体芯片原料"))
            .register();
    @Trans(key = "item.gtpm.raw_crystal_chip_parts.tooltip",
           source = "§7Raw Crystal Processor Parts",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7晶体处理器部件原料"))
    public static ItemEntry<Item> RAW_CRYSTAL_CHIP_PART = REGISTRATE.item("raw_crystal_chip_parts", Item::new)
            .localization(text -> text
                    .source("Raw Crystal Chip Parts")
                    .translation(MinecraftLocale.ZH_CN, "晶体芯片部件原料"))
            .register();
    @Trans(key = "item.gtpm.crystal_cpu.tooltip",
           source = "§7Crystal Processing Unit",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7晶体处理器"))
    public static ItemEntry<Item> CRYSTAL_CENTRAL_PROCESSING_UNIT = REGISTRATE.item("crystal_cpu", Item::new)
            .localization(text -> text
                    .source("Crystal CPU")
                    .translation(MinecraftLocale.ZH_CN, "晶体CPU"))
            .register();
    @Trans(key = "item.gtpm.crystal_soc.tooltip",
           source = "§7Crystal System on Chip",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7晶体系统级芯片"))
    public static ItemEntry<Item> CRYSTAL_SYSTEM_ON_CHIP = REGISTRATE.item("crystal_soc", Item::new)
            .localization(text -> text
                    .source("Crystal SoC")
                    .translation(MinecraftLocale.ZH_CN, "晶体SoC"))
            .register();

    @Trans(key = "item.gtpm.resin_circuit_board.tooltip",
           source = "§7A Coated Board",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7电路的基底"))
    public static ItemEntry<Item> COATED_BOARD = REGISTRATE.item("resin_circuit_board", Item::new)
            .localization(text -> text
                    .source("Resin Circuit Board")
                    .translation(MinecraftLocale.ZH_CN, "覆膜电路基板"))
            .register();
    @Trans(key = "item.gtpm.phenolic_circuit_board.tooltip",
           source = "§7A Good Board",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7更好的基板"))
    public static ItemEntry<Item> PHENOLIC_BOARD = REGISTRATE.item("phenolic_circuit_board", Item::new)
            .localization(text -> text
                    .source("Phenolic Circuit Board")
                    .translation(MinecraftLocale.ZH_CN, "酚醛树脂电路基板"))
            .register();
    @Trans(key = "item.gtpm.plastic_circuit_board.tooltip",
           source = "§7A Good Board",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7更好的基板"))
    public static ItemEntry<Item> PLASTIC_BOARD = REGISTRATE.item("plastic_circuit_board", Item::new)
            .localization(text -> text
                    .source("Plastic Circuit Board")
                    .translation(MinecraftLocale.ZH_CN, "塑料电路基板"))
            .register();
    @Trans(key = "item.gtpm.epoxy_circuit_board.tooltip",
           source = "§7An Advanced Board",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7进阶电路基板"))
    public static ItemEntry<Item> EPOXY_BOARD = REGISTRATE.item("epoxy_circuit_board", Item::new)
            .localization(text -> text
                    .source("Epoxy Circuit Board")
                    .translation(MinecraftLocale.ZH_CN, "环氧树脂基板"))
            .register();
    @Trans(key = "item.gtpm.fiber_reinforced_circuit_board.tooltip",
           source = "§7An Extreme Board",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7超级电路基板"))
    public static ItemEntry<Item> FIBER_BOARD = REGISTRATE.item("fiber_reinforced_circuit_board", Item::new)
            .localization(text -> text
                    .source("Fiber-Reinforced Circuit Board")
                    .translation(MinecraftLocale.ZH_CN, "纤维强化电路基板"))
            .register();
    @Trans(key = "item.gtpm.multilayer_fiber_reinforced_circuit_board.tooltip",
           source = "§7An Elite Board",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7精英电路基板"))
    public static ItemEntry<Item> MULTILAYER_FIBER_BOARD = REGISTRATE
            .item("multilayer_fiber_reinforced_circuit_board", Item::new)
            .localization(text -> text
                    .source("Multi-layer Fiber-Reinforced Circuit Board")
                    .translation(MinecraftLocale.ZH_CN, "多层纤维强化电路基板"))
            .register();
    @Trans(key = "item.gtpm.wetware_circuit_board.tooltip",
           source = "§7The Board that keeps life",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7维生之板"))
    public static ItemEntry<Item> WETWARE_BOARD = REGISTRATE.item("wetware_circuit_board", Item::new)
            .localization(text -> text
                    .source("Wetware Circuit Board")
                    .translation(MinecraftLocale.ZH_CN, "湿件电路基板"))
            .register();

    @Trans(key = "item.gtpm.resin_printed_circuit_board.tooltip",
           source = "§7A Basic Circuit Board",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7基础电路基板"))
    public static ItemEntry<Item> BASIC_CIRCUIT_BOARD = REGISTRATE.item("resin_printed_circuit_board", Item::new)
            .localization(text -> text
                    .source("Resin Printed Circuit Board")
                    .translation(MinecraftLocale.ZH_CN, "覆膜印刷电路基板"))
            .register();
    @Trans(key = "item.gtpm.phenolic_printed_circuit_board.tooltip",
           source = "§7A Good Circuit Board",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7优质电路基板"))
    public static ItemEntry<Item> GOOD_CIRCUIT_BOARD = REGISTRATE.item("phenolic_printed_circuit_board", Item::new)
            .localization(text -> text
                    .source("Phenolic Printed Circuit Board")
                    .translation(MinecraftLocale.ZH_CN, "酚醛树脂印刷电路基板"))
            .register();
    @Trans(key = "item.gtpm.plastic_printed_circuit_board.tooltip",
           source = "§7A Good Circuit Board",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7优质电路基板"))
    public static ItemEntry<Item> PLASTIC_CIRCUIT_BOARD = REGISTRATE.item("plastic_printed_circuit_board", Item::new)
            .localization(text -> text
                    .source("Plastic Printed Circuit Board")
                    .translation(MinecraftLocale.ZH_CN, "塑料印刷电路基板"))
            .register();
    @Trans(key = "item.gtpm.epoxy_printed_circuit_board.tooltip",
           source = "§7An Advanced Circuit Board",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7更高级的电路基板"))
    public static ItemEntry<Item> ADVANCED_CIRCUIT_BOARD = REGISTRATE.item("epoxy_printed_circuit_board", Item::new)
            .localization(text -> text
                    .source("Epoxy Printed Circuit Board")
                    .translation(MinecraftLocale.ZH_CN, "环氧树脂印刷电路基板"))
            .register();
    @Trans(key = "item.gtpm.fiber_reinforced_printed_circuit_board.tooltip",
           source = "§7A More Advanced Circuit Board",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7更高级的电路基板"))
    public static ItemEntry<Item> EXTREME_CIRCUIT_BOARD = REGISTRATE
            .item("fiber_reinforced_printed_circuit_board", Item::new)
            .localization(text -> text
                    .source("Fiber-Reinforced Printed Circuit Board")
                    .translation(MinecraftLocale.ZH_CN, "纤维强化印刷电路基板"))
            .register();
    @Trans(key = "item.gtpm.multilayer_fiber_reinforced_printed_circuit_board.tooltip",
           source = "§7An Elite Circuit Board",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7精英级电路基板"))
    public static ItemEntry<Item> ELITE_CIRCUIT_BOARD = REGISTRATE
            .item("multilayer_fiber_reinforced_printed_circuit_board", Item::new)
            .localization(text -> text
                    .source("Multi-layer Fiber-Reinforced Printed Circuit Board")
                    .translation(MinecraftLocale.ZH_CN, "多层纤维强化印刷电路基板"))
            .register();
    @Trans(key = "item.gtpm.wetware_printed_circuit_board.tooltip",
           source = "§7The Board that keeps life",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7维生之板"))
    public static ItemEntry<Item> WETWARE_CIRCUIT_BOARD = REGISTRATE.item("wetware_printed_circuit_board", Item::new)
            .localization(text -> text
                    .source("Wetware Printed Circuit Board")
                    .translation(MinecraftLocale.ZH_CN, "湿件印刷电路基板"))
            .register();

    @TransVariants(keyPrefix = "item.gtpm.vacuum_tube",
                   value = {
                           @Variant(id = "tooltip.0",
                                    source = "§7Technically a Diode",
                                    translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7技术上是二极管")),
                           @Variant(id = "tooltip.1",
                                    source = "§cULV-Tier",
                                    translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§cULV等级")),
                   })
    public static ItemEntry<Item> VACUUM_TUBE = REGISTRATE.item("vacuum_tube", Item::new)
            .localization(text -> text
                    .source("Vacuum Tube")
                    .translation(MinecraftLocale.ZH_CN, "真空管"))
            .tag(CustomTags.ULV_CIRCUITS).register();
    public static ItemEntry<Item> GLASS_TUBE = REGISTRATE.item("glass_tube", Item::new)
            .localization(text -> text
                    .source("Glass Tube")
                    .translation(MinecraftLocale.ZH_CN, "玻璃管"))
            .onRegister(materialInfo(new ItemMaterialInfo(new MaterialStack(GTMaterials.Glass, GTValues.M))))
            .register();
    @Trans(key = "item.gtpm.transistor.tooltip",
           source = "§7Basic Electronic Component",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7基础电子元件"))
    public static ItemEntry<Item> TRANSISTOR = REGISTRATE.item("transistor", Item::new)
            .localization(text -> text
                    .source("Transistor")
                    .translation(MinecraftLocale.ZH_CN, "晶体管"))
            .tag(CustomTags.TRANSISTORS)
            .register();
    @Trans(key = "item.gtpm.resistor.tooltip",
           source = "§7Basic Electronic Component",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7基础电子元件"))
    public static ItemEntry<Item> RESISTOR = REGISTRATE.item("resistor", Item::new)
            .localization(text -> text
                    .source("Resistor")
                    .translation(MinecraftLocale.ZH_CN, "电阻"))
            .tag(CustomTags.RESISTORS)
            .register();
    @Trans(key = "item.gtpm.capacitor.tooltip",
           source = "§7Basic Electronic Component",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7基础电子元件"))
    public static ItemEntry<Item> CAPACITOR = REGISTRATE.item("capacitor", Item::new)
            .localization(text -> text
                    .source("Capacitor")
                    .translation(MinecraftLocale.ZH_CN, "电容"))
            .tag(CustomTags.CAPACITORS)
            .register();
    @Trans(key = "item.gtpm.diode.tooltip",
           source = "§7Basic Electronic Component",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7基础电子元件"))
    public static ItemEntry<Item> DIODE = REGISTRATE.item("diode", Item::new)
            .localization(text -> text
                    .source("Diode")
                    .translation(MinecraftLocale.ZH_CN, "二极管"))
            .tag(CustomTags.DIODES)
            .register();
    @Trans(key = "item.gtpm.inductor.tooltip",
           source = "§7A Small Coil",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7一个小线圈"))
    public static ItemEntry<Item> INDUCTOR = REGISTRATE.item("inductor", Item::new)
            .localization(text -> text
                    .source("Inductor")
                    .translation(MinecraftLocale.ZH_CN, "电感"))
            .tag(CustomTags.INDUCTORS)
            .register();
    @Trans(key = "item.gtpm.smd_transistor.tooltip",
           source = "§7Electronic Component",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7电子元件"))
    public static ItemEntry<Item> SMD_TRANSISTOR = REGISTRATE.item("smd_transistor", Item::new)
            .localization(text -> text
                    .source("SMD Transistor")
                    .translation(MinecraftLocale.ZH_CN, "贴片晶体管"))
            .tag(CustomTags.TRANSISTORS).register();
    @Trans(key = "item.gtpm.smd_resistor.tooltip",
           source = "§7Electronic Component",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7电子元件"))
    public static ItemEntry<Item> SMD_RESISTOR = REGISTRATE.item("smd_resistor", Item::new)
            .localization(text -> text
                    .source("SMD Resistor")
                    .translation(MinecraftLocale.ZH_CN, "贴片电阻"))
            .tag(CustomTags.RESISTORS).register();
    @Trans(key = "item.gtpm.smd_capacitor.tooltip",
           source = "§7Electronic Component",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7电子元件"))
    public static ItemEntry<Item> SMD_CAPACITOR = REGISTRATE.item("smd_capacitor", Item::new)
            .localization(text -> text
                    .source("SMD Capacitor")
                    .translation(MinecraftLocale.ZH_CN, "贴片电容"))
            .tag(CustomTags.CAPACITORS).register();
    @Trans(key = "item.gtpm.smd_diode.tooltip",
           source = "§7Electronic Component",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7电子元件"))
    public static ItemEntry<Item> SMD_DIODE = REGISTRATE.item("smd_diode", Item::new)
            .localization(text -> text
                    .source("SMD Diode")
                    .translation(MinecraftLocale.ZH_CN, "贴片二极管"))
            .tag(CustomTags.DIODES).register();
    @Trans(key = "item.gtpm.smd_inductor.tooltip",
           source = "§7Electronic Component",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7电子元件"))
    public static ItemEntry<Item> SMD_INDUCTOR = REGISTRATE.item("smd_inductor", Item::new)
            .localization(text -> text
                    .source("SMD Inductor")
                    .translation(MinecraftLocale.ZH_CN, "贴片电感"))
            .tag(CustomTags.INDUCTORS).register();
    @Trans(key = "item.gtpm.advanced_smd_transistor.tooltip",
           source = "§7Advanced Electronic Component",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7高级电子元件"))
    public static ItemEntry<Item> ADVANCED_SMD_TRANSISTOR = REGISTRATE.item("advanced_smd_transistor", Item::new)
            .localization(text -> text
                    .source("Advanced SMD Transistor")
                    .translation(MinecraftLocale.ZH_CN, "高级贴片晶体管"))
            .register();
    @Trans(key = "item.gtpm.advanced_smd_resistor.tooltip",
           source = "§7Advanced Electronic Component",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7高级电子元件"))
    public static ItemEntry<Item> ADVANCED_SMD_RESISTOR = REGISTRATE.item("advanced_smd_resistor", Item::new)
            .localization(text -> text
                    .source("Advanced SMD Resistor")
                    .translation(MinecraftLocale.ZH_CN, "高级贴片电阻"))
            .register();
    @Trans(key = "item.gtpm.advanced_smd_capacitor.tooltip",
           source = "§7Advanced Electronic Component",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7高级电子元件"))
    public static ItemEntry<Item> ADVANCED_SMD_CAPACITOR = REGISTRATE.item("advanced_smd_capacitor", Item::new)
            .localization(text -> text
                    .source("Advanced SMD Capacitor")
                    .translation(MinecraftLocale.ZH_CN, "高级贴片电容"))
            .register();
    @Trans(key = "item.gtpm.advanced_smd_diode.tooltip",
           source = "§7Advanced Electronic Component",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7高级电子元件"))
    public static ItemEntry<Item> ADVANCED_SMD_DIODE = REGISTRATE.item("advanced_smd_diode", Item::new)
            .localization(text -> text
                    .source("Advanced SMD Diode")
                    .translation(MinecraftLocale.ZH_CN, "高级贴片二极管"))
            .register();
    @Trans(key = "item.gtpm.advanced_smd_inductor.tooltip",
           source = "§7Advanced Electronic Component",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7高级电子元件"))
    public static ItemEntry<Item> ADVANCED_SMD_INDUCTOR = REGISTRATE.item("advanced_smd_inductor", Item::new)
            .localization(text -> text
                    .source("Advanced SMD Inductor")
                    .translation(MinecraftLocale.ZH_CN, "高级贴片电感"))
            .register();

    // T1: Electronic
    @TransVariants(keyPrefix = "item.gtpm.basic_electronic_circuit",
                   value = {
                           @Variant(id = "tooltip.0",
                                    source = "§7Your First Circuit",
                                    translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7你的第一块电路")),
                           @Variant(id = "tooltip.1",
                                    source = "§cLV-Tier Circuit",
                                    translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§cLV级电路")),
                   })
    public static ItemEntry<Item> ELECTRONIC_CIRCUIT_LV = REGISTRATE.item("basic_electronic_circuit", Item::new)
            .localization(text -> text
                    .source("Basic Electronic Circuit")
                    .translation(MinecraftLocale.ZH_CN, "基础电子电路"))
            .tag(CustomTags.LV_CIRCUITS)
            .register();
    @TransVariants(keyPrefix = "item.gtpm.good_electronic_circuit",
                   value = {
                           @Variant(id = "tooltip.0",
                                    source = "§7Your Second Circuit",
                                    translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7你的第二块电路")),
                           @Variant(id = "tooltip.1",
                                    source = "§cMV-Tier Circuit",
                                    translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§cMV级电路")),
                   })
    public static ItemEntry<Item> ELECTRONIC_CIRCUIT_MV = REGISTRATE.item("good_electronic_circuit", Item::new)
            .localization(text -> text
                    .source("Good Electronic Circuit")
                    .translation(MinecraftLocale.ZH_CN, "优质电子电路"))
            .tag(CustomTags.MV_CIRCUITS)
            .register();

    // T2: Integrated
    @TransVariants(keyPrefix = "item.gtpm.basic_integrated_circuit",
                   value = {
                           @Variant(id = "tooltip.0",
                                    source = "§7Smaller and more powerful",
                                    translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7更小也更强")),
                           @Variant(id = "tooltip.1",
                                    source = "§6LV-Tier Circuit",
                                    translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§6LV级电路")),
                   })
    public static ItemEntry<Item> INTEGRATED_CIRCUIT_LV = REGISTRATE.item("basic_integrated_circuit", Item::new)
            .localization(text -> text
                    .source("Basic Integrated Circuit")
                    .translation(MinecraftLocale.ZH_CN, "基础集成电路"))
            .tag(CustomTags.LV_CIRCUITS)
            .register();
    @TransVariants(keyPrefix = "item.gtpm.good_integrated_circuit",
                   value = {
                           @Variant(id = "tooltip.0",
                                    source = "§7Smaller and more powerful",
                                    translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7更小也更强")),
                           @Variant(id = "tooltip.1",
                                    source = "§6MV-Tier Circuit",
                                    translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§6MV级电路")),
                   })
    public static ItemEntry<Item> INTEGRATED_CIRCUIT_MV = REGISTRATE.item("good_integrated_circuit", Item::new)
            .localization(text -> text
                    .source("Good Integrated Circuit")
                    .translation(MinecraftLocale.ZH_CN, "优质集成电路"))
            .tag(CustomTags.MV_CIRCUITS)
            .register();
    @TransVariants(keyPrefix = "item.gtpm.advanced_integrated_circuit",
                   value = {
                           @Variant(id = "tooltip.0",
                                    source = "§7Smaller and more powerful",
                                    translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7更小也更强")),
                           @Variant(id = "tooltip.1",
                                    source = "§6HV-Tier Circuit",
                                    translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§6HV级电路")),
                   })
    public static ItemEntry<Item> INTEGRATED_CIRCUIT_HV = REGISTRATE.item("advanced_integrated_circuit", Item::new)
            .localization(text -> text
                    .source("Advanced Integrated Circuit")
                    .translation(MinecraftLocale.ZH_CN, "进阶集成电路"))
            .tag(CustomTags.HV_CIRCUITS)
            .register();

    // ULV/LV easier circuits
    @TransVariants(keyPrefix = "item.gtpm.nand_chip",
                   value = {
                           @Variant(id = "tooltip.0",
                                    source = "§7A Superior Simple Circuit",
                                    translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7卓越的简单电路")),
                           @Variant(id = "tooltip.1",
                                    source = "§6ULV-Tier Circuit",
                                    translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§6ULV级电路")),
                   })
    public static ItemEntry<Item> NAND_CHIP_ULV = REGISTRATE.item("nand_chip", Item::new)
            .localization(text -> text
                    .source("NAND Chip")
                    .translation(MinecraftLocale.ZH_CN, "NAND芯片"))
            .tag(CustomTags.ULV_CIRCUITS).register();
    @TransVariants(keyPrefix = "item.gtpm.microchip_processor",
                   value = {
                           @Variant(id = "tooltip.0",
                                    source = "§7A Superior Basic Circuit",
                                    translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7卓越的基础电路")),
                           @Variant(id = "tooltip.1",
                                    source = "§eLV-Tier Circuit",
                                    translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§eLV级电路")),
                   })
    public static ItemEntry<Item> MICROPROCESSOR_LV = REGISTRATE.item("microchip_processor", Item::new)
            .localization(text -> text
                    .source("Microchip Processor")
                    .translation(MinecraftLocale.ZH_CN, "微芯片处理器"))
            .tag(CustomTags.LV_CIRCUITS)
            .register();

    // T3: Processor
    @TransVariants(keyPrefix = "item.gtpm.micro_processor",
                   value = {
                           @Variant(id = "tooltip.0",
                                    source = "§7Amazing Computation Speed!",
                                    translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7运算速度惊人！")),
                           @Variant(id = "tooltip.1",
                                    source = "§eMV-Tier Circuit",
                                    translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§eMV级电路")),
                   })
    public static ItemEntry<Item> PROCESSOR_MV = REGISTRATE.item("micro_processor", Item::new)
            .localization(text -> text
                    .source("Microprocessor")
                    .translation(MinecraftLocale.ZH_CN, "微型处理器"))
            .tag(CustomTags.MV_CIRCUITS).register();
    @TransVariants(keyPrefix = "item.gtpm.micro_processor_assembly",
                   value = {
                           @Variant(id = "tooltip.0",
                                    source = "§7Amazing Computation Speed!",
                                    translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7运算速度惊人！")),
                           @Variant(id = "tooltip.1",
                                    source = "§eHV-Tier Circuit",
                                    translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§eHV级电路")),
                   })
    public static ItemEntry<Item> PROCESSOR_ASSEMBLY_HV = REGISTRATE.item("micro_processor_assembly", Item::new)
            .localization(text -> text
                    .source("Microprocessor Assembly")
                    .translation(MinecraftLocale.ZH_CN, "微型处理器集群"))
            .tag(CustomTags.HV_CIRCUITS)
            .register();
    @TransVariants(keyPrefix = "item.gtpm.micro_processor_computer",
                   value = {
                           @Variant(id = "tooltip.0",
                                    source = "§7Amazing Computation Speed!",
                                    translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7运算速度惊人！")),
                           @Variant(id = "tooltip.1",
                                    source = "§eEV-Tier Circuit",
                                    translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§eEV级电路")),
                   })
    public static ItemEntry<Item> WORKSTATION_EV = REGISTRATE.item("micro_processor_computer", Item::new)
            .localization(text -> text
                    .source("Microprocessor Supercomputer")
                    .translation(MinecraftLocale.ZH_CN, "微型处理器超级计算机"))
            .tag(CustomTags.EV_CIRCUITS)
            .register();
    @TransVariants(keyPrefix = "item.gtpm.micro_processor_mainframe",
                   value = {
                           @Variant(id = "tooltip.0",
                                    source = "§7Amazing Computation Speed!",
                                    translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7运算速度惊人！")),
                           @Variant(id = "tooltip.1",
                                    source = "§eIV-Tier Circuit",
                                    translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§eIV级电路")),
                   })
    public static ItemEntry<Item> MAINFRAME_IV = REGISTRATE.item("micro_processor_mainframe", Item::new)
            .localization(text -> text
                    .source("Microprocessor Mainframe")
                    .translation(MinecraftLocale.ZH_CN, "微型处理器主机"))
            .tag(CustomTags.IV_CIRCUITS)
            .register();

    // T4: Nano
    @TransVariants(keyPrefix = "item.gtpm.nano_processor",
                   value = {
                           @Variant(id = "tooltip.0",
                                    source = "§7Smaller than ever",
                                    translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7比小更小")),
                           @Variant(id = "tooltip.1",
                                    source = "§bHV-Tier Circuit",
                                    translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§bHV级电路")),
                   })
    public static ItemEntry<Item> NANO_PROCESSOR_HV = REGISTRATE.item("nano_processor", Item::new)
            .localization(text -> text
                    .source("Nanoprocessor")
                    .translation(MinecraftLocale.ZH_CN, "纳米处理器"))
            .tag(CustomTags.HV_CIRCUITS).register();
    @TransVariants(keyPrefix = "item.gtpm.nano_processor_assembly",
                   value = {
                           @Variant(id = "tooltip.0",
                                    source = "§7Smaller than ever",
                                    translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7比小更小")),
                           @Variant(id = "tooltip.1",
                                    source = "§bEV-Tier Circuit",
                                    translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§bEV级电路")),
                   })
    public static ItemEntry<Item> NANO_PROCESSOR_ASSEMBLY_EV = REGISTRATE.item("nano_processor_assembly", Item::new)
            .localization(text -> text
                    .source("Nanoprocessor Assembly")
                    .translation(MinecraftLocale.ZH_CN, "纳米处理器集群"))
            .tag(CustomTags.EV_CIRCUITS)
            .register();
    @TransVariants(keyPrefix = "item.gtpm.nano_processor_computer",
                   value = {
                           @Variant(id = "tooltip.0",
                                    source = "§7Smaller than ever",
                                    translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7比小更小")),
                           @Variant(id = "tooltip.1",
                                    source = "§bIV-Tier Circuit",
                                    translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§bIV级电路")),
                   })
    public static ItemEntry<Item> NANO_COMPUTER_IV = REGISTRATE.item("nano_processor_computer", Item::new)
            .localization(text -> text
                    .source("Nanoprocessor Supercomputer")
                    .translation(MinecraftLocale.ZH_CN, "纳米处理器超级计算机"))
            .tag(CustomTags.IV_CIRCUITS)
            .register();
    @TransVariants(keyPrefix = "item.gtpm.nano_processor_mainframe",
                   value = {
                           @Variant(id = "tooltip.0",
                                    source = "§7Smaller than ever",
                                    translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7比小更小")),
                           @Variant(id = "tooltip.1",
                                    source = "§bLuV-Tier Circuit",
                                    translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§bLuV级电路")),
                   })
    public static ItemEntry<Item> NANO_MAINFRAME_LuV = REGISTRATE.item("nano_processor_mainframe", Item::new)
            .localization(text -> text
                    .source("Nanoprocessor Mainframe")
                    .translation(MinecraftLocale.ZH_CN, "纳米处理器主机"))
            .tag(CustomTags.LuV_CIRCUITS)
            .register();

    // T5: Quantum
    @TransVariants(keyPrefix = "item.gtpm.quantum_processor",
                   value = {
                           @Variant(id = "tooltip.0",
                                    source = "§7Quantum Computing comes to life!",
                                    translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7量子计算终成现实！")),
                           @Variant(id = "tooltip.1",
                                    source = "§aEV-Tier Circuit",
                                    translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§aEV级电路")),
                   })
    public static ItemEntry<Item> QUANTUM_PROCESSOR_EV = REGISTRATE.item("quantum_processor", Item::new)
            .localization(text -> text
                    .source("Quantum Processor")
                    .translation(MinecraftLocale.ZH_CN, "量子处理器"))
            .tag(CustomTags.EV_CIRCUITS)
            .register();
    @TransVariants(keyPrefix = "item.gtpm.quantum_processor_assembly",
                   value = {
                           @Variant(id = "tooltip.0",
                                    source = "§7Quantum Computing comes to life!",
                                    translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7量子计算终成现实！")),
                           @Variant(id = "tooltip.1",
                                    source = "§aIV-Tier Circuit",
                                    translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§aIV级电路")),
                   })
    public static ItemEntry<Item> QUANTUM_ASSEMBLY_IV = REGISTRATE.item("quantum_processor_assembly", Item::new)
            .localization(text -> text
                    .source("Quantum Processor Assembly")
                    .translation(MinecraftLocale.ZH_CN, "量子处理器集群"))
            .tag(CustomTags.IV_CIRCUITS)
            .register();
    @TransVariants(keyPrefix = "item.gtpm.quantum_processor_computer",
                   value = {
                           @Variant(id = "tooltip.0",
                                    source = "§7Quantum Computing comes to life!",
                                    translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7量子计算终成现实！")),
                           @Variant(id = "tooltip.1",
                                    source = "§aLuV-Tier Circuit",
                                    translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§aLuV级电路")),
                   })
    public static ItemEntry<Item> QUANTUM_COMPUTER_LuV = REGISTRATE.item("quantum_processor_computer", Item::new)
            .localization(text -> text
                    .source("Quantum Processor Supercomputer")
                    .translation(MinecraftLocale.ZH_CN, "量子处理器超级计算机"))
            .tag(CustomTags.LuV_CIRCUITS)
            .register();
    @TransVariants(keyPrefix = "item.gtpm.quantum_processor_mainframe",
                   value = {
                           @Variant(id = "tooltip.0",
                                    source = "§7Quantum Computing comes to life!",
                                    translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7量子计算终成现实！")),
                           @Variant(id = "tooltip.1",
                                    source = "§aZPM-Tier Circuit",
                                    translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§aZPM级电路")),
                   })
    public static ItemEntry<Item> QUANTUM_MAINFRAME_ZPM = REGISTRATE.item("quantum_processor_mainframe", Item::new)
            .localization(text -> text
                    .source("Quantum Processor Mainframe")
                    .translation(MinecraftLocale.ZH_CN, "量子处理器主机"))
            .tag(CustomTags.ZPM_CIRCUITS)
            .register();

    // T6: Crystal
    @TransVariants(keyPrefix = "item.gtpm.crystal_processor",
                   value = {
                           @Variant(id = "tooltip.0",
                                    source = "§7Taking Advantage of Crystal Engraving",
                                    translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7得益于晶体蚀刻技术")),
                           @Variant(id = "tooltip.1",
                                    source = "§9IV-Tier Circuit",
                                    translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§9IV级电路")),
                   })
    public static ItemEntry<Item> CRYSTAL_PROCESSOR_IV = REGISTRATE.item("crystal_processor", Item::new)
            .localization(text -> text
                    .source("Crystal Processor")
                    .translation(MinecraftLocale.ZH_CN, "晶体处理器"))
            .tag(CustomTags.IV_CIRCUITS)
            .register();
    @TransVariants(keyPrefix = "item.gtpm.crystal_processor_assembly",
                   value = {
                           @Variant(id = "tooltip.0",
                                    source = "§7Taking Advantage of Crystal Engraving",
                                    translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7得益于晶体蚀刻技术")),
                           @Variant(id = "tooltip.1",
                                    source = "§9LuV-Tier Circuit",
                                    translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§9LuV级电路")),
                   })
    public static ItemEntry<Item> CRYSTAL_ASSEMBLY_LuV = REGISTRATE.item("crystal_processor_assembly", Item::new)
            .localization(text -> text
                    .source("Crystal Processor Assembly")
                    .translation(MinecraftLocale.ZH_CN, "晶体处理器集群"))
            .tag(CustomTags.LuV_CIRCUITS)
            .register();
    @TransVariants(keyPrefix = "item.gtpm.crystal_processor_computer",
                   value = {
                           @Variant(id = "tooltip.0",
                                    source = "§7Taking Advantage of Crystal Engraving",
                                    translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7得益于晶体蚀刻技术")),
                           @Variant(id = "tooltip.1",
                                    source = "§9ZPM-Tier Circuit",
                                    translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§9ZPM级电路")),
                   })
    public static ItemEntry<Item> CRYSTAL_COMPUTER_ZPM = REGISTRATE.item("crystal_processor_computer", Item::new)
            .localization(text -> text
                    .source("Crystal Processor Supercomputer")
                    .translation(MinecraftLocale.ZH_CN, "晶体处理器超级计算机"))
            .tag(CustomTags.ZPM_CIRCUITS)
            .register();
    @TransVariants(keyPrefix = "item.gtpm.crystal_processor_mainframe",
                   value = {
                           @Variant(id = "tooltip.0",
                                    source = "§7Taking Advantage of Crystal Engraving",
                                    translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7得益于晶体蚀刻技术")),
                           @Variant(id = "tooltip.1",
                                    source = "§9UV-Tier Circuit",
                                    translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§9UV级电路")),
                   })
    public static ItemEntry<Item> CRYSTAL_MAINFRAME_UV = REGISTRATE.item("crystal_processor_mainframe", Item::new)
            .localization(text -> text
                    .source("Crystal Processor Mainframe")
                    .translation(MinecraftLocale.ZH_CN, "晶体处理器主机"))
            .tag(CustomTags.UV_CIRCUITS)
            .register();

    // T7: Wetware
    @TransVariants(keyPrefix = "item.gtpm.wetware_processor",
                   value = {
                           @Variant(id = "tooltip.0",
                                    source = "§7You have a feeling like it's watching you",
                                    translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7祂在看我吗？")),
                           @Variant(id = "tooltip.1",
                                    source = "§4LuV-Tier Circuit",
                                    translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§4LuV级电路")),
                   })
    public static ItemEntry<Item> WETWARE_PROCESSOR_LuV = REGISTRATE.item("wetware_processor", Item::new)
            .localization(text -> text
                    .source("Wetware Processor")
                    .translation(MinecraftLocale.ZH_CN, "湿件处理器"))
            .tag(CustomTags.LuV_CIRCUITS)
            .register();
    @TransVariants(keyPrefix = "item.gtpm.wetware_processor_assembly",
                   value = {
                           @Variant(id = "tooltip.0",
                                    source = "§7Can run Minecraft",
                                    translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                                text = "§7可以玩Minecraft")),
                           @Variant(id = "tooltip.1",
                                    source = "§4ZPM-Tier Circuit",
                                    translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§4ZPM级电路")),
                   })
    public static ItemEntry<Item> WETWARE_PROCESSOR_ASSEMBLY_ZPM = REGISTRATE
            .item("wetware_processor_assembly", Item::new)
            .localization(text -> text
                    .source("Wetware Processor Assembly")
                    .translation(MinecraftLocale.ZH_CN, "湿件处理器集群"))
            .tag(CustomTags.ZPM_CIRCUITS).register();
    @TransVariants(keyPrefix = "item.gtpm.wetware_processor_computer",
                   value = {
                           @Variant(id = "tooltip.0",
                                    source = "§7Ultimate fusion of Flesh and Machine",
                                    translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7肉体与机器的终极结合")),
                           @Variant(id = "tooltip.1",
                                    source = "§4UV-Tier Circuit",
                                    translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§4UV级电路")),
                   })
    public static ItemEntry<Item> WETWARE_SUPER_COMPUTER_UV = REGISTRATE.item("wetware_processor_computer", Item::new)
            .localization(text -> text
                    .source("Wetware Processor Supercomputer")
                    .translation(MinecraftLocale.ZH_CN, "湿件处理器超级计算机"))
            .tag(CustomTags.UV_CIRCUITS)
            .register();
    @TransVariants(keyPrefix = "item.gtpm.wetware_processor_mainframe",
                   value = {
                           @Variant(id = "tooltip.0",
                                    source = "§7The best Man has ever seen",
                                    translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7千载独步，惟公一人")),
                           @Variant(id = "tooltip.1",
                                    source = "§4UHV-Tier Circuit",
                                    translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§4UHV级电路")),
                   })
    public static ItemEntry<Item> WETWARE_MAINFRAME_UHV = REGISTRATE.item("wetware_processor_mainframe", Item::new)
            .localization(text -> text
                    .source("Wetware Processor Mainframe")
                    .translation(MinecraftLocale.ZH_CN, "湿件处理器主机"))
            .tag(CustomTags.UHV_CIRCUITS)
            .register();

    public static ItemEntry<Item> COMPONENT_GRINDER_DIAMOND = REGISTRATE.item("diamond_grinding_head", Item::new)
            .localization(text -> text
                    .source("Diamond Grinding Head")
                    .translation(MinecraftLocale.ZH_CN, "钻石研磨头"))
            .onRegister(materialInfo(new ItemMaterialInfo(new MaterialStack(GTMaterials.Steel, GTValues.M * 8),
                    new MaterialStack(GTMaterials.Diamond, GTValues.M * 5))))
            .register();
    public static ItemEntry<Item> COMPONENT_GRINDER_TUNGSTEN = REGISTRATE.item("tungsten_grinding_head", Item::new)
            .localization(text -> text
                    .source("Tungsten Grinding Head")
                    .translation(MinecraftLocale.ZH_CN, "钨研磨头"))
            .onRegister(materialInfo(new ItemMaterialInfo(new MaterialStack(GTMaterials.Tungsten, GTValues.M * 4),
                    new MaterialStack(GTMaterials.VanadiumSteel, GTValues.M * 8),
                    new MaterialStack(GTMaterials.Diamond, GTValues.M))))
            .register();

    public static ItemEntry<Item> IRON_MINECART_WHEELS = REGISTRATE.item("iron_minecart_wheels", Item::new)
            .localization(text -> text
                    .source("Iron Minecart Wheels")
                    .translation(MinecraftLocale.ZH_CN, "铁矿车车轮"))
            .onRegister(materialInfo(new ItemMaterialInfo(new MaterialStack(GTMaterials.Iron, GTValues.M))))
            .register();
    public static ItemEntry<Item> STEEL_MINECART_WHEELS = REGISTRATE.item("steel_minecart_wheels", Item::new)
            .localization(text -> text
                    .source("Steel Minecart Wheels")
                    .translation(MinecraftLocale.ZH_CN, "钢矿车车轮"))
            .onRegister(materialInfo(new ItemMaterialInfo(new MaterialStack(GTMaterials.Steel, GTValues.M))))
            .register();

    @Trans(key = "item.gtpm.quantum_eye.tooltip",
           source = "§7Improved Ender Eye",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7改良的末影之眼"))
    public static ItemEntry<Item> QUANTUM_EYE = REGISTRATE.item("quantum_eye", Item::new)
            .localization(text -> text
                    .source("Quantum Eye")
                    .translation(MinecraftLocale.ZH_CN, "量子之眼"))
            .register();
    @Trans(key = "item.gtpm.quantum_star.tooltip",
           source = "§7Improved Nether Star",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7改良的下界之星"))
    public static ItemEntry<Item> QUANTUM_STAR = REGISTRATE.item("quantum_star", Item::new)
            .localization(text -> text
                    .source("Quantum Star")
                    .translation(MinecraftLocale.ZH_CN, "量子之星"))
            .register();
    @Trans(key = "item.gtpm.gravi_star.tooltip",
           source = "§7Ultimate Nether Star",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7终极下界之星"))
    public static ItemEntry<Item> GRAVI_STAR = REGISTRATE.item("gravi_star", Item::new)
            .localization(text -> text
                    .source("Gravi-Star")
                    .translation(MinecraftLocale.ZH_CN, "重力之星"))
            .register();

    /////////////////////////////////////////
    // *********** COVERS ***********//
    /////////////////////////////////////////

    @TransVariants(keyPrefix = "item.gtpm.item_filter",
                   value = {
                           @Variant(id = "tooltip.0",
                                    source = "§7Filters §fItem§7 I/O as §fCover§7.",
                                    translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                                text = "§7作§f覆盖板§7时过滤§f物品§7的输入/输出。")),
                           @Variant(id = "tooltip.1",
                                    source = "Can be used as a §fConveyor Module§7 and §fRobotic Arm§7 upgrade.",
                                    translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                                text = "亦可为§f传送带§7和§f机械臂§7提供此功能。")),
                   })
    public static ItemEntry<ComponentItem> ITEM_FILTER = REGISTRATE.item("item_filter", ComponentItem::new)
            .localization(text -> text
                    .source("Item Filter")
                    .translation(MinecraftLocale.ZH_CN, "物品过滤卡"))
            .onRegister(attach(new ItemFilterBehaviour(SimpleItemFilter::loadFilter),
                    new CoverPlaceBehavior(GTCovers.ITEM_FILTER)))
            .onRegister(materialInfo(new ItemMaterialInfo(new MaterialStack(GTMaterials.Zinc, GTValues.M * 2),
                    new MaterialStack(GTMaterials.Steel, GTValues.M))))
            .register();
    @TransVariants(keyPrefix = "item.gtpm",
                   value = {
                           @Variant(id = "item_tag_filter.tooltip.0",
                                    source = "§7Filters §fItem§7 I/O with §fItem Tags§7 as §fCover§7.",
                                    translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                                text = "§7作§f覆盖板§7时以§f物品标签§7过滤§f物品§7的输入/输出。")),
                           @Variant(id = "item_tag_filter.tooltip.1",
                                    source = "Can be used as a §fConveyor Module§7 and §fRobotic Arm§7 upgrade.",
                                    translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                                text = "亦可为§f传送带§7和§f机械臂§7提供此功能。")),
                           @Variant(id = "tag_filter.tooltip.0",
                                    source = "§7Filters §fItem§7 I/O with §fTag§7 as §fCover§7.",
                                    translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                                text = "作§f覆盖板§7时以§f标签§7过滤§f物品§7的输入/输出。")),
                           @Variant(id = "tag_filter.tooltip.1",
                                    source = "Can be used as a §fConveyor Module§7 and §fRobotic Arm§7 upgrade.",
                                    translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                                text = "亦可为§f传送带§7和§f机械臂§7提供此功能。")),
                   })
    public static ItemEntry<ComponentItem> TAG_FILTER = REGISTRATE
            .item("item_tag_filter", ComponentItem::new)
            .localization(text -> text
                    .source("Item Tag Filter")
                    .translation(MinecraftLocale.ZH_CN, "物品标签过滤卡"))
            .onRegister(attach(new ItemFilterBehaviour(TagItemFilter::loadFilter),
                    new CoverPlaceBehavior(GTCovers.ITEM_FILTER)))
            .onRegister(materialInfo(new ItemMaterialInfo(new MaterialStack(GTMaterials.Zinc, GTValues.M * 2))))
            .register();
    @TransVariants(keyPrefix = "item.gtpm.smart_item_filter",
                   value = {
                           @Variant(id = "tooltip.0",
                                    source = "§7Filters §fItem§7 I/O with §fMachine Recipes§7 as §fCover§7.",
                                    translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                                text = "§7作§f覆盖板§7时以§f机器的配方§7过滤§f物品§7的输入/输出。")),
                           @Variant(id = "tooltip.1",
                                    source = "Can be used as a §fConveyor Module§7 and §fRobotic Arm§7 upgrade.",
                                    translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                                text = "亦可为§f传送带§7和§f机械臂§7提供此功能。")),
                   })
    public static ItemEntry<ComponentItem> SMART_ITEM_FILTER = REGISTRATE
            .item("smart_item_filter", ComponentItem::new)
            .localization(text -> text
                    .source("Smart Item Filter")
                    .translation(MinecraftLocale.ZH_CN, "智能物品过滤卡"))
            .onRegister(attach(new ItemFilterBehaviour(SmartItemFilter::loadFilter),
                    new CoverPlaceBehavior(GTCovers.ITEM_FILTER)))
            .onRegister(materialInfo(new ItemMaterialInfo(new MaterialStack(GTMaterials.Zinc, GTValues.M * 3 / 2))))
            .register();
    @TransVariants(keyPrefix = "item.gtpm.fluid_filter",
                   value = {
                           @Variant(id = "tooltip.0",
                                    source = "§7Filters §fFluid§7 I/O as §fCover§7.",
                                    translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                                text = "§7作§f覆盖板§7时过滤§流体§7的输入/输出。")),
                           @Variant(id = "tooltip.1",
                                    source = "Can be used as an §fElectric Pump§7 and §fFluid Regulator§7 upgrade.",
                                    translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                                text = "亦可为§f电动泵§7和§f流体校准器§7提供此功能。")),
                   })
    public static ItemEntry<ComponentItem> FLUID_FILTER = REGISTRATE.item("fluid_filter", ComponentItem::new)
            .localization(text -> text
                    .source("Fluid Filter")
                    .translation(MinecraftLocale.ZH_CN, "流体过滤卡"))
            .onRegister(attach(new FluidFilterBehaviour(SimpleFluidFilter::loadFilter),
                    new CoverPlaceBehavior(GTCovers.FLUID_FILTER)))
            .onRegister(materialInfo(new ItemMaterialInfo(new MaterialStack(GTMaterials.Zinc, GTValues.M * 2))))
            .register();
    @TransVariants(keyPrefix = "item.gtpm.fluid_tag_filter",
                   value = {
                           @Variant(id = "tooltip.0",
                                    source = "§7Filters §fFluid§7 I/O with §fFluid Tags§7 as §fCover§7.",
                                    translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                                text = "§7作§f覆盖板§7时以§f流体标签§7过滤§f流体§7的输入/输出。")),
                           @Variant(id = "tooltip.1",
                                    source = "Can be used as an §fElectric Pump§7 and §fFluid Regulator§7 upgrade.",
                                    translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                                text = "§7可用于升级§f电动泵§7与§f流体校准器§7。")),
                   })
    public static ItemEntry<ComponentItem> TAG_FLUID_FILTER = REGISTRATE.item("fluid_tag_filter", ComponentItem::new)
            .localization(text -> text
                    .source("Fluid Tag Filter")
                    .translation(MinecraftLocale.ZH_CN, "流体标签过滤卡"))
            .onRegister(attach(new FluidFilterBehaviour(TagFluidFilter::loadFilter),
                    new CoverPlaceBehavior(GTCovers.FLUID_FILTER)))
            .onRegister(materialInfo(new ItemMaterialInfo(new MaterialStack(GTMaterials.Zinc, GTValues.M * 3 / 2))))
            .register();
    public static ItemEntry<ComponentItem> COVER_WIRELESS_TRANSMITTER = REGISTRATE
            .item("wireless_transmitter_cover", ComponentItem::new)
            .localization(text -> text
                    .source("Wireless Transmitter")
                    .translation(MinecraftLocale.ZH_CN, "无线发信器"))
            .onRegister(attach(new CoverPlaceBehavior(GTCovers.WIRELESS_TRANSMITTER)))
            .register();

    public static ItemEntry<ComponentItem> COVER_MACHINE_CONTROLLER = REGISTRATE
            .item("machine_controller_cover", ComponentItem::new)
            .localization(text -> text
                    .source("Machine Controller")
                    .translation(MinecraftLocale.ZH_CN, "机器控制覆盖板"))
            .onRegister(attach(new CoverPlaceBehavior(GTCovers.MACHINE_CONTROLLER)))
            .register();

    @Trans(key = "item.gtpm.activity_detector_cover.tooltip",
           source = "§7Gives out §fActivity Status§7 as Redstone as §fCover§7.",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7作§f覆盖板§7时依照§f机器活跃状态§7发出红石信号。"))
    public static ItemEntry<ComponentItem> COVER_ACTIVITY_DETECTOR = REGISTRATE
            .item("activity_detector_cover", ComponentItem::new)
            .localization(text -> text
                    .source("Activity Detector")
                    .translation(MinecraftLocale.ZH_CN, "活跃探测覆盖板"))
            .onRegister(attach(new CoverPlaceBehavior(GTCovers.ACTIVITY_DETECTOR)))
            .register();
    @Trans(key = "item.gtpm.advanced_activity_detector_cover.tooltip",
           source = "§7Gives out §fMachine Progress§7 as Redstone as §fCover§7.",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7作§f覆盖板§7时依照§f机器处理进度§7发出红石信号。"))
    public static ItemEntry<ComponentItem> COVER_ACTIVITY_DETECTOR_ADVANCED = REGISTRATE
            .item("advanced_activity_detector_cover", ComponentItem::new)
            .localization(text -> text
                    .source("Advanced Activity Detector")
                    .translation(MinecraftLocale.ZH_CN, "进阶活跃状态探测覆盖板"))
            .onRegister(attach(new CoverPlaceBehavior(GTCovers.ACTIVITY_DETECTOR_ADVANCED)))
            .register();
    @Trans(key = "item.gtpm.fluid_detector_cover.tooltip",
           source = "§7Gives out §fFluid Amount§7 as Redstone as §fCover§7.",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7作§f覆盖板§7时依照§f流体储量§7发出红石信号。"))
    public static ItemEntry<ComponentItem> COVER_FLUID_DETECTOR = REGISTRATE
            .item("fluid_detector_cover", ComponentItem::new)
            .localization(text -> text
                    .source("Fluid Detector")
                    .translation(MinecraftLocale.ZH_CN, "流体探测覆盖板"))
            .onRegister(attach(new CoverPlaceBehavior(GTCovers.FLUID_DETECTOR)))
            .register();
    @Trans(key = "item.gtpm.advanced_fluid_detector_cover.tooltip",
           source = "§7Gives §fRS-Latch§7 controlled §fFluid Storage Status§7 as Redstone as §fCover§7.",
           translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                       text = "作§f覆盖板§7时依照由§fRS锁存器§7控制的§f流体存储状态§7发出红石信号。"))
    public static ItemEntry<ComponentItem> COVER_FLUID_DETECTOR_ADVANCED = REGISTRATE
            .item("advanced_fluid_detector_cover", ComponentItem::new)
            .localization(text -> text
                    .source("Advanced Fluid Detector")
                    .translation(MinecraftLocale.ZH_CN, "进阶流体探测覆盖板"))
            .onRegister(attach(new CoverPlaceBehavior(GTCovers.FLUID_DETECTOR_ADVANCED)))
            .register();
    @Trans(key = "item.gtpm.item_detector_cover.tooltip",
           source = "§7Gives out §fItem Amount§7 as Redstone as §fCover§7.",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7作§f覆盖板§7时依照§f物品储量§7发出红石信号。"))
    public static ItemEntry<ComponentItem> COVER_ITEM_DETECTOR = REGISTRATE
            .item("item_detector_cover", ComponentItem::new)
            .localization(text -> text
                    .source("Item Detector")
                    .translation(MinecraftLocale.ZH_CN, "物品探测覆盖板"))
            .onRegister(attach(new CoverPlaceBehavior(GTCovers.ITEM_DETECTOR)))
            .register();
    @Trans(key = "item.gtpm.advanced_item_detector_cover.tooltip",
           source = "§7Gives §fRS-Latch§7 controlled §fItem Storage Status§7 as Redstone as §fCover§7.",
           translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                       text = "作§f覆盖板§7时依照由§fRS锁存器§7控制的§f物品存储状态§7发出红石信号。"))
    public static ItemEntry<ComponentItem> COVER_ITEM_DETECTOR_ADVANCED = REGISTRATE
            .item("advanced_item_detector_cover", ComponentItem::new)
            .localization(text -> text
                    .source("Advanced Item Detector")
                    .translation(MinecraftLocale.ZH_CN, "进阶物品探测覆盖板"))
            .onRegister(attach(new CoverPlaceBehavior(GTCovers.ITEM_DETECTOR_ADVANCED)))
            .register();
    @Trans(key = "item.gtpm.energy_detector_cover.tooltip",
           source = "§7Gives out §fEnergy Amount§7 as Redstone as §fCover§7.",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7作§f覆盖板§7时依照§f能量储量§7发出红石信号。"))
    public static ItemEntry<ComponentItem> COVER_ENERGY_DETECTOR = REGISTRATE
            .item("energy_detector_cover", ComponentItem::new)
            .localization(text -> text
                    .source("Energy Detector")
                    .translation(MinecraftLocale.ZH_CN, "能量探测覆盖板"))
            .onRegister(attach(new CoverPlaceBehavior(GTCovers.ENERGY_DETECTOR)))
            .register();
    @Trans(key = "item.gtpm.advanced_energy_detector_cover.tooltip",
           source = "§7Gives §fRS-Latch§7 controlled §fEnergy Status§7 as Redstone as §fCover§7.",
           translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                       text = "§7作§f覆盖板§7时依照由§fRS锁存器§7控制的§f能量状态§7发出红石信号。"))
    public static ItemEntry<ComponentItem> COVER_ENERGY_DETECTOR_ADVANCED = REGISTRATE
            .item("advanced_energy_detector_cover", ComponentItem::new)
            .localization(text -> text
                    .source("Advanced Energy Detector")
                    .translation(MinecraftLocale.ZH_CN, "进阶能量探测覆盖板"))
            .onRegister(attach(new CoverPlaceBehavior(GTCovers.ENERGY_DETECTOR_ADVANCED)))
            .register();
    public static ItemEntry<ComponentItem> COVER_MAINTENANCE_DETECTOR = REGISTRATE
            .item("maintenance_detector_cover", ComponentItem::new)
            .localization(text -> text
                    .source("Maintenance Detector")
                    .translation(MinecraftLocale.ZH_CN, "维护探测覆盖板"))
            .onRegister(attach(new CoverPlaceBehavior(GTCovers.MAINTENANCE_DETECTOR)))
            .register();

    @Trans(key = "item.gtpm.computer_monitor_cover.tooltip",
           source = "§7Displays §fData§7 as §fCover§7.",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7作§f覆盖板§7时显示§f数据§7。"))
    public static ItemEntry<ComponentItem> COVER_SCREEN = REGISTRATE
            .item("computer_monitor_cover", ComponentItem::new)
            .localization(text -> text
                    .source("Computer Monitor")
                    .translation(MinecraftLocale.ZH_CN, "电脑屏幕覆盖板"))
            .onRegister(attach(new CoverPlaceBehavior(GTCovers.COMPUTER_MONITOR)))
            .register();
    public static ItemEntry<ComponentItem> COVER_STORAGE = REGISTRATE
            .item("storage_cover", ComponentItem::new)
            .localization(text -> text
                    .source("Storage Cover")
                    .translation(MinecraftLocale.ZH_CN, "存储覆盖板"))
            .onRegister(attach(new CoverPlaceBehavior(GTCovers.COVER_STORAGE)))
            .register();
    @Trans(key = "item.gtpm.shutter_module_cover.tooltip",
           source = "§fBlocks Transfer§7 through attached Side as §fCover§7.",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7作§f覆盖板§7时§f阻止§7机器侧面的任何§f传输§7行为。"))
    public static ItemEntry<ComponentItem> COVER_SHUTTER = REGISTRATE
            .item("shutter_module_cover", ComponentItem::new)
            .localization(text -> text
                    .source("Shutter Module")
                    .translation(MinecraftLocale.ZH_CN, "扇板"))
            .onRegister(attach(new CoverPlaceBehavior(GTCovers.SHUTTER)))
            .register();

    @Trans(key = "item.gtpm.infinite_water_cover.tooltip",
           source = "§7Fills attached containers with §9Water§7 as §fCover§7.",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7作§f覆盖板§7时为所附着的容器供§9水§7。"))
    public static ItemEntry<ComponentItem> COVER_INFINITE_WATER = REGISTRATE
            .item("infinite_water_cover", ComponentItem::new)
            .localization(text -> text
                    .source("Infinite Water Cover")
                    .translation(MinecraftLocale.ZH_CN, "无限水覆盖板"))
            .onRegister(attach(new TooltipBehavior(lines -> {
                lines.add(Component.translatable("gtpm.universal.tooltip.produces_fluid", 16_000 / 20));
            }), new CoverPlaceBehavior(GTCovers.INFINITE_WATER))).register();

    @Trans(key = "item.gtpm.ender_fluid_link_cover.tooltip",
           source = "§7Transports §fFluids§7 with a §fWireless §dEnder§f Connection§7 as §fCover§7.",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7作§f覆盖板§7时利用§f无线§7§d末影§f连接§7传输§f流体§7。"))
    public static ItemEntry<ComponentItem> COVER_ENDER_FLUID_LINK = REGISTRATE
            .item("ender_fluid_link_cover", ComponentItem::new)
            .localization(text -> text
                    .source("Ender Fluid Link")
                    .translation(MinecraftLocale.ZH_CN, "末影流体连接覆盖板"))
            .onRegister(attach(new CoverPlaceBehavior(GTCovers.ENDER_FLUID_LINK)))
            .register();

    public static ItemEntry<ComponentItem> COVER_ENDER_ITEM_LINK = REGISTRATE
            .item("ender_item_link_cover", ComponentItem::new)
            .localization(text -> text
                    .source("Ender Item Link")
                    .translation(MinecraftLocale.ZH_CN, "末影物品连接覆盖板"))
            .onRegister(attach(new CoverPlaceBehavior(GTCovers.ENDER_ITEM_LINK)))
            .register();
    public static ItemEntry<ComponentItem> COVER_ENDER_REDSTONE_LINK = REGISTRATE
            .item("ender_redstone_link_cover", ComponentItem::new)
            .localization(text -> text
                    .source("Ender Redstone Link")
                    .translation(MinecraftLocale.ZH_CN, "末影红石连接覆盖板"))
            .onRegister(attach(new CoverPlaceBehavior(GTCovers.ENDER_REDSTONE_LINK)))
            .register();

    @TransVariants(keyPrefix = "item.gtpm.fluid_voiding_cover",
                   value = {
                           @Variant(id = "tooltip.0",
                                    source = "§7Voids §fFluids§7 as §fCover§7.",
                                    translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                                text = "§7作§f覆盖板§7时允许销毁§f流体§7。")),
                           @Variant(id = "tooltip.1",
                                    source = "Activate with §fSoft Mallet§7 after placement.",
                                    translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                                text = "§7加装后使用§f软锤§7激活")),
                   })
    public static ItemEntry<ComponentItem> COVER_FLUID_VOIDING = REGISTRATE
            .item("fluid_voiding_cover", ComponentItem::new)
            .localization(text -> text
                    .source("Fluid Voiding Cover")
                    .translation(MinecraftLocale.ZH_CN, "流体销毁覆盖板"))
            .onRegister(attach(new CoverPlaceBehavior(GTCovers.FLUID_VOIDING)))
            .register();
    @TransVariants(keyPrefix = "item.gtpm.advanced_fluid_voiding_cover",
                   value = {
                           @Variant(id = "tooltip.0",
                                    source = "§7Voids §fFluids§7 with amount control as §fCover§7.",
                                    translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                                text = "§7作§f覆盖板§7时允许按数量销毁§f流体§7。")),
                           @Variant(id = "tooltip.1",
                                    source = "Activate with §fSoft Mallet§7 after placement.",
                                    translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                                text = "加装后使用§f软锤§7激活")),
                   })
    public static ItemEntry<ComponentItem> COVER_FLUID_VOIDING_ADVANCED = REGISTRATE
            .item("advanced_fluid_voiding_cover", ComponentItem::new)
            .localization(text -> text
                    .source("Advanced Fluid Voiding Cover")
                    .translation(MinecraftLocale.ZH_CN, "进阶流体销毁覆盖板"))
            .onRegister(attach(new CoverPlaceBehavior(GTCovers.FLUID_VOIDING_ADVANCED)))
            .register();
    @TransVariants(keyPrefix = "item.gtpm.item_voiding_cover",
                   value = {
                           @Variant(id = "tooltip.0",
                                    source = "§7Voids §fItems§7 as §fCover§7.",
                                    translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                                text = "§7作§f覆盖板§7时销毁物品。")),
                           @Variant(id = "tooltip.1",
                                    source = "Activate with §fSoft Mallet§7 after placement.",
                                    translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                                text = "§7加装后使用§f软锤§7激活")),
                   })
    public static ItemEntry<ComponentItem> COVER_ITEM_VOIDING = REGISTRATE
            .item("item_voiding_cover", ComponentItem::new)
            .localization(text -> text
                    .source("Item Voiding Cover")
                    .translation(MinecraftLocale.ZH_CN, "物品销毁覆盖板"))
            .onRegister(attach(new CoverPlaceBehavior(GTCovers.ITEM_VOIDING)))
            .register();
    @TransVariants(keyPrefix = "item.gtpm.advanced_item_voiding_cover",
                   value = {
                           @Variant(id = "tooltip.0",
                                    source = "§7Voids §fItems§7 with amount control as §fCover§7.",
                                    translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                                text = "§7作§f覆盖板§7时销毁物品。")),
                           @Variant(id = "tooltip.1",
                                    source = "Activate with §fSoft Mallet§7 after placement.",
                                    translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                                text = "加装后使用§f软锤§7激活")),
                   })
    public static ItemEntry<ComponentItem> COVER_ITEM_VOIDING_ADVANCED = REGISTRATE
            .item("advanced_item_voiding_cover", ComponentItem::new)
            .localization(text -> text
                    .source("Advanced Item Voiding Cover")
                    .translation(MinecraftLocale.ZH_CN, "进阶物品销毁覆盖板"))
            .onRegister(attach(new CoverPlaceBehavior(GTCovers.ITEM_VOIDING_ADVANCED)))
            .register();

    @TransVariants(keyPrefix = "item.gtpm.facade_cover",
                   value = {
                           @Variant(id = "tooltip.0",
                                    source = "§7Decorative Outfit §fCover§7.",
                                    translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                                text = "§7可作为§f覆盖板§7加装的装饰性外壳。")),
                           @Variant(id = "tooltip.1",
                                    source = "§7Crafted using an Iron Plate and any block",
                                    translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                                text = "§7使用铁板与任意方块合成")),
                   })
    public static ItemEntry<ComponentItem> COVER_FACADE = REGISTRATE.item("facade_cover", ComponentItem::new)
            .localization(text -> text
                    .source("%s Cover Facade")
                    .translation(MinecraftLocale.ZH_CN, "%s覆盖伪装板"))
            .onRegister(attach(new FacadeItemBehaviour(), new CoverPlaceBehavior(GTCovers.FACADE)))
            .setData(ProviderType.ITEM_MODEL, NonNullBiConsumer.noop())
            .register();

    // Solar Panels: ID 331-346
    public static ItemEntry<ComponentItem> COVER_SOLAR_PANEL = REGISTRATE.item("solar_panel", ComponentItem::new)
            .localization(text -> text
                    .source("Solar Panel")
                    .translation(MinecraftLocale.ZH_CN, "太阳能板"))
            .onRegister(attach(new TooltipBehavior(lines -> {
                lines.add(Component.translatable("gtpm.universal.tooltip.voltage_out", 1, GTValues.VNF[GTValues.ULV]));
            }))).onRegister(attach(new CoverPlaceBehavior(GTCovers.SOLAR_PANEL_BASIC)))
            .register();
    public static ItemEntry<ComponentItem> COVER_SOLAR_PANEL_ULV = REGISTRATE
            .item("ulv_solar_panel", ComponentItem::new)
            .localization(text -> text
                    .source("Ultra Low Voltage Solar Panel")
                    .translation(MinecraftLocale.ZH_CN, "超低压太阳能板（§8ULV§r）"))
            .onRegister(attach(new TooltipBehavior(lines -> {
                lines.addAll(LangHandler.getMultiLang("item.gtpm.solar_panel.tooltip"));
                lines.add(Component.translatable("gtpm.universal.tooltip.voltage_out", GTValues.V[GTValues.ULV],
                        GTValues.VNF[GTValues.ULV]));
            }))).onRegister(attach(new CoverPlaceBehavior(GTCovers.SOLAR_PANEL[0])))
            .register();
    public static ItemEntry<ComponentItem> COVER_SOLAR_PANEL_LV = REGISTRATE
            .item("lv_solar_panel", ComponentItem::new)
            .localization(text -> text
                    .source("Low Voltage Solar Panel")
                    .translation(MinecraftLocale.ZH_CN, "低压太阳能板（§7LV§r）"))
            .onRegister(attach(new TooltipBehavior(lines -> {
                lines.addAll(LangHandler.getMultiLang("item.gtpm.solar_panel.tooltip"));
                lines.add(Component.translatable("gtpm.universal.tooltip.voltage_out", GTValues.V[GTValues.LV],
                        GTValues.VNF[GTValues.LV]));
            }))).onRegister(attach(new CoverPlaceBehavior(GTCovers.SOLAR_PANEL[1])))
            .register();
    public static ItemEntry<ComponentItem> COVER_SOLAR_PANEL_MV = REGISTRATE
            .item("mv_solar_panel", ComponentItem::new)
            .localization(text -> text
                    .source("Medium Voltage Solar Panel")
                    .translation(MinecraftLocale.ZH_CN, "中压太阳能板（§bMV§r）"))
            .onRegister(attach(new TooltipBehavior(lines -> {
                lines.addAll(LangHandler.getMultiLang("item.gtpm.solar_panel.tooltip"));
                lines.add(Component.translatable("gtpm.universal.tooltip.voltage_out", GTValues.V[GTValues.MV],
                        GTValues.VNF[GTValues.MV]));
            }))).onRegister(attach(new CoverPlaceBehavior(GTCovers.SOLAR_PANEL[2])))
            .register();
    public static ItemEntry<ComponentItem> COVER_SOLAR_PANEL_HV = REGISTRATE
            .item("hv_solar_panel", ComponentItem::new)
            .localization(text -> text
                    .source("High Voltage Solar Panel")
                    .translation(MinecraftLocale.ZH_CN, "高压太阳能板（§6HV§r）"))
            .onRegister(attach(new TooltipBehavior(lines -> {
                lines.addAll(LangHandler.getMultiLang("item.gtpm.solar_panel.tooltip"));
                lines.add(Component.translatable("gtpm.universal.tooltip.voltage_out", GTValues.V[GTValues.HV],
                        GTValues.VNF[GTValues.HV]));
            }))).onRegister(attach(new CoverPlaceBehavior(GTCovers.SOLAR_PANEL[3])))
            .register();
    public static ItemEntry<ComponentItem> COVER_SOLAR_PANEL_EV = REGISTRATE
            .item("ev_solar_panel", ComponentItem::new)
            .localization(text -> text
                    .source("Extreme Voltage Solar Panel")
                    .translation(MinecraftLocale.ZH_CN, "超高压太阳能板（§5EV§r）"))
            .onRegister(attach(new TooltipBehavior(lines -> {
                lines.addAll(LangHandler.getMultiLang("item.gtpm.solar_panel.tooltip"));
                lines.add(Component.translatable("gtpm.universal.tooltip.voltage_out", GTValues.V[GTValues.EV],
                        GTValues.VNF[GTValues.EV]));
            }))).onRegister(attach(new CoverPlaceBehavior(GTCovers.SOLAR_PANEL[4])))
            .register();
    public static ItemEntry<ComponentItem> COVER_SOLAR_PANEL_IV = REGISTRATE
            .item("iv_solar_panel", ComponentItem::new)
            .localization(text -> text
                    .source("Insane Voltage Solar Panel")
                    .translation(MinecraftLocale.ZH_CN, "强导压太阳能板（§9IV§r）"))
            .onRegister(attach(new TooltipBehavior(lines -> {
                lines.addAll(LangHandler.getMultiLang("item.gtpm.solar_panel.tooltip"));
                lines.add(Component.translatable("gtpm.universal.tooltip.voltage_out", GTValues.V[GTValues.IV],
                        GTValues.VNF[GTValues.IV]));
            }))).onRegister(attach(new CoverPlaceBehavior(GTCovers.SOLAR_PANEL[5])))
            .register();
    public static ItemEntry<ComponentItem> COVER_SOLAR_PANEL_LuV = REGISTRATE
            .item("luv_solar_panel", ComponentItem::new)
            .localization(text -> text
                    .source("Ludicrous Voltage Solar Panel")
                    .translation(MinecraftLocale.ZH_CN, "剧差压太阳能板（§dLuV§r）"))
            .onRegister(attach(new TooltipBehavior(lines -> {
                lines.addAll(LangHandler.getMultiLang("item.gtpm.solar_panel.tooltip"));
                lines.add(Component.translatable("gtpm.universal.tooltip.voltage_out", GTValues.V[GTValues.LuV],
                        GTValues.VNF[GTValues.LuV]));
            }))).onRegister(attach(new CoverPlaceBehavior(GTCovers.SOLAR_PANEL[6])))
            .register();
    public static ItemEntry<ComponentItem> COVER_SOLAR_PANEL_ZPM = REGISTRATE
            .item("zpm_solar_panel", ComponentItem::new)
            .localization(text -> text
                    .source("Zero Point Module Solar Panel")
                    .translation(MinecraftLocale.ZH_CN, "零点压太阳能板（§cZPM§r）"))
            .onRegister(attach(new TooltipBehavior(lines -> {
                lines.addAll(LangHandler.getMultiLang("item.gtpm.solar_panel.tooltip"));
                lines.add(Component.translatable("gtpm.universal.tooltip.voltage_out", GTValues.V[GTValues.ZPM],
                        GTValues.VNF[GTValues.ZPM]));
            }))).onRegister(attach(new CoverPlaceBehavior(GTCovers.SOLAR_PANEL[7])))
            .register();
    public static ItemEntry<ComponentItem> COVER_SOLAR_PANEL_UV = REGISTRATE
            .item("uv_solar_panel", ComponentItem::new)
            .localization(text -> text
                    .source("Ultimate Voltage Solar Panel")
                    .translation(MinecraftLocale.ZH_CN, "极限压太阳能板（§3UV§r）"))
            .onRegister(attach(new TooltipBehavior(lines -> {
                lines.addAll(LangHandler.getMultiLang("item.gtpm.solar_panel.tooltip"));
                lines.add(Component.translatable("gtpm.universal.tooltip.voltage_out", GTValues.V[GTValues.UV],
                        GTValues.VNF[GTValues.UV]));
            }))).onRegister(attach(new CoverPlaceBehavior(GTCovers.SOLAR_PANEL[8])))
            .register();

    // Plugin
    public static ItemEntry<Item> PLUGIN_TEXT;
    public static ItemEntry<Item> PLUGIN_ONLINE_PIC;
    public static ItemEntry<Item> PLUGIN_FAKE_GUI;
    public static ItemEntry<Item> PLUGIN_ADVANCED_MONITOR;

    @TransVariants(keyPrefix = "item.gtpm.programmed_circuit",
                   value = {
                           @Variant(id = "tooltip.0",
                                    source = "Use to open configuration GUI",
                                    translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "右击打开配置页面")),
                           @Variant(id = "tooltip.1",
                                    source = "Shift-Right-Click on a machine",
                                    translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "潜行右击有虚拟电路槽的")),
                           @Variant(id = "tooltip.2",
                                    source = "with a circuit slot to set it to",
                                    translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "机器，将其设置为本电路")),
                           @Variant(id = "tooltip.3",
                                    source = "this circuit's value.",
                                    translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "的值。")),
                   })
    public static ItemEntry<ComponentItem> PROGRAMMED_CIRCUIT = REGISTRATE
            .item("programmed_circuit", ComponentItem::new)
            .localization(text -> text
                    .source("Programmed Circuit")
                    .translation(MinecraftLocale.ZH_CN, "编程电路"))
            .model(() -> overrideModel(GTPM.id("circuit"), 33))
            .onRegister(modelPredicate(GTPM.id("circuit"),
                    (itemStack) -> IntCircuitBehaviour.getCircuitConfiguration(itemStack) / 100f))
            .onRegister(attach(new IntCircuitBehaviour()))
            .tag(CustomTags.SKIP_ITEM_DETECTOR)
            .register();

    // public static ItemEntry<ComponentItem> FOAM_SPRAYER = REGISTRATE.item("foam_sprayer",
    // ComponentItem::new).onRegister(attach(new FoamSprayerBehavior()).setMaxStackSize(1);
    @Trans(key = "item.gtpm.gelled_toluene.tooltip",
           source = "§7Raw Explosive",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7炸药原料"))
    public static ItemEntry<Item> GELLED_TOLUENE = REGISTRATE.item("gelled_toluene", Item::new)
            .localization(text -> text
                    .source("Gelled Toluene")
                    .translation(MinecraftLocale.ZH_CN, "胶凝甲苯"))
            .register();

    public static ItemEntry<ComponentItem> BOTTLE_PURPLE_DRINK = REGISTRATE.item("purple_drink", ComponentItem::new)
            .localization(text -> text
                    .source("Purple Drink")
                    .translation(MinecraftLocale.ZH_CN, "紫色饮料"))
            .onRegister(attach(new FoodStats(GTFoods.DRINK, true, Items.GLASS_BOTTLE::getDefaultInstance)))
            .register();
    public static ItemEntry<ComponentItem> DOUGH = REGISTRATE.item("dough", ComponentItem::new)
            .localization(text -> text
                    .source("Dough")
                    .translation(MinecraftLocale.ZH_CN, "面团"))
            .onRegister(attach(new FoodStats(
                    new FoodProperties.Builder().nutrition(1)
                            .effect(() -> new MobEffectInstance(MobEffects.HUNGER, 400), .40f)
                            .effect(() -> new MobEffectInstance(MobEffects.POISON, 100), .05f)
                            .build())))
            .tag(CustomTags.WHEAT_DOUGHS)
            .register();
    public static ItemEntry<ComponentItem> PLANT_BALL = REGISTRATE.item("plant_ball", ComponentItem::new)
            .localization(text -> text
                    .source("Plant Ball")
                    .translation(MinecraftLocale.ZH_CN, "植物球"))
            .dataMap(NeoForgeDataMaps.FURNACE_FUELS, new FurnaceFuel(75)).register();
    public static ItemEntry<ComponentItem> STICKY_RESIN = REGISTRATE.item("sticky_resin", ComponentItem::new)
            .localization(text -> text
                    .source("Sticky Resin")
                    .translation(MinecraftLocale.ZH_CN, "黏性树脂"))
            .dataMap(NeoForgeDataMaps.FURNACE_FUELS, new FurnaceFuel(200)).register();
    public static ItemEntry<ComponentItem> BIO_CHAFF = REGISTRATE.item("bio_chaff", ComponentItem::new)
            .localization(text -> text
                    .source("Bio Chaff")
                    .translation(MinecraftLocale.ZH_CN, "生物糠"))
            .dataMap(NeoForgeDataMaps.FURNACE_FUELS, new FurnaceFuel(200)).register();
    public static ItemEntry<Item> ENERGIUM_DUST = REGISTRATE.item("energium_dust", Item::new)
            .localization(text -> text
                    .source("Energium Dust")
                    .translation(MinecraftLocale.ZH_CN, "能量粉"))
            .register();

    public static ItemEntry<ComponentItem> POWER_UNIT_LV = REGISTRATE.item("lv_power_unit", ComponentItem::new)
            .localization(text -> text
                    .source("LV Power Unit")
                    .translation(MinecraftLocale.ZH_CN, "§7LV§r能量单元"))
            .properties(p -> p.stacksTo(8))
            .model(() -> (ctx, prov) -> prov.generateFlatItem(ctx.get(), prov.modLoc("item/tools/power_unit_lv")))
            .onRegister(attach(ElectricStats.createElectricItem(100000L, GTValues.LV)))
            .register();
    public static ItemEntry<ComponentItem> POWER_UNIT_MV = REGISTRATE.item("mv_power_unit", ComponentItem::new)
            .localization(text -> text
                    .source("MV Power Unit")
                    .translation(MinecraftLocale.ZH_CN, "§bMV§r能量单元"))
            .properties(p -> p.stacksTo(8))
            .model(() -> (ctx, prov) -> prov.generateFlatItem(ctx.get(), prov.modLoc("item/tools/power_unit_mv")))
            .onRegister(attach(ElectricStats.createElectricItem(400000L, GTValues.MV)))
            .register();
    public static ItemEntry<ComponentItem> POWER_UNIT_HV = REGISTRATE.item("hv_power_unit", ComponentItem::new)
            .localization(text -> text
                    .source("HV Power Unit")
                    .translation(MinecraftLocale.ZH_CN, "§6HV§r能量单元"))
            .properties(p -> p.stacksTo(8))
            .model(() -> (ctx, prov) -> prov.generateFlatItem(ctx.get(), prov.modLoc("item/tools/power_unit_hv")))
            .onRegister(attach(ElectricStats.createElectricItem(1600000L, GTValues.HV)))
            .register();
    public static ItemEntry<ComponentItem> POWER_UNIT_EV = REGISTRATE.item("ev_power_unit", ComponentItem::new)
            .localization(text -> text
                    .source("EV Power Unit")
                    .translation(MinecraftLocale.ZH_CN, "§5EV§r能量单元"))
            .properties(p -> p.stacksTo(8))
            .model(() -> (ctx, prov) -> prov.generateFlatItem(ctx.get(), prov.modLoc("item/tools/power_unit_ev")))
            .onRegister(attach(ElectricStats.createElectricItem(6400000L, GTValues.EV)))
            .register();
    public static ItemEntry<ComponentItem> POWER_UNIT_IV = REGISTRATE.item("iv_power_unit", ComponentItem::new)
            .localization(text -> text
                    .source("IV Power Unit")
                    .translation(MinecraftLocale.ZH_CN, "§9IV§r能量单元"))
            .properties(p -> p.stacksTo(8))
            .model(() -> (ctx, prov) -> prov.generateFlatItem(ctx.get(), prov.modLoc("item/tools/power_unit_iv")))
            .onRegister(attach(ElectricStats.createElectricItem(25600000L, GTValues.IV)))
            .register();

    public static ItemEntry<Item> MASK_FILTER = REGISTRATE.item("mask_filter", Item::new)
            .localization(text -> text
                    .source("Gas Mask Filter")
                    .translation(MinecraftLocale.ZH_CN, "防毒面具过滤罐"))
            .properties(p -> p.stacksTo(1))
            .register();
    // TODO add more medications for specific conditions & then remove them from paracetamol
    public static ItemEntry<ComponentItem> PARACETAMOL_PILL = REGISTRATE.item("paracetamol_pill", ComponentItem::new)
            .localization(text -> text
                    .source("Paracetamol Pill")
                    .translation(MinecraftLocale.ZH_CN, "对乙酰氨基酚药片"))
            .properties(p -> p.food(GTFoods.ANTIDOTE))
            .onRegister(attach(new AntidoteBehavior(10,
                    GTMedicalConditions.CHEMICAL_BURNS,
                    GTMedicalConditions.WEAK_POISON,
                    GTMedicalConditions.NAUSEA,
                    GTMedicalConditions.IRRITANT,
                    GTMedicalConditions.METHANOL_POISONING,
                    GTMedicalConditions.CARBON_MONOXIDE_POISONING)))
            .register();
    public static ItemEntry<ComponentItem> RAD_AWAY_PILL = REGISTRATE.item("rad_away_pill", ComponentItem::new)
            .localization(text -> text
                    .source("RadAway™ Pill")
                    .translation(MinecraftLocale.ZH_CN, "消辐宁™药片"))
            .properties(p -> p.food(GTFoods.ANTIDOTE))
            .onRegister(attach(new AntidoteBehavior(50, GTMedicalConditions.CARCINOGEN)))
            .register();

    @Trans(key = "item.gtpm.nano_saber.tooltip",
           source = "§7Ryujin no ken wo kurae!",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7尝尝龙神之剑！"))
    public static ItemEntry<ComponentItem> NANO_SABER = REGISTRATE.item("nano_saber", ComponentItem::new)
            .localization(text -> text
                    .source("Nano Saber")
                    .translation(MinecraftLocale.ZH_CN, "纳米剑"))
            .properties(p -> p.stacksTo(1))
            .onRegister(attach(new NanoSaberBehavior(), ElectricStats.createElectricItem(4_000_000L, GTValues.HV)))
            .model(() -> (ctx, prov) -> {
                var rootModel = prov.generateFlatItem(ctx.get(), prov.modLoc("item/nano_saber/normal"));
                prov.getBuilder("item/nano_saber/active")
                        .parent(new ModelFile.UncheckedModelFile("item/handheld"))
                        .texture("layer0", prov.modLoc("item/nano_saber/active"));

                rootModel.override().predicate(NanoSaberBehavior.OVERRIDE_KEY_LOCATION, 1.0f)
                        .model(new ModelFile.UncheckedModelFile(prov.modLoc("item/nano_saber/active")))
                        .end();
            })
            .onRegister(modelPredicate(NanoSaberBehavior.OVERRIDE_KEY_LOCATION,
                    () -> () -> (stack, level, entity, layer) -> NanoSaberBehavior.isItemActive(stack) ? 1.0f : 0.0f))
            .register();
    public static ItemEntry<ComponentItem> PROSPECTOR_LV = REGISTRATE.item("prospector.lv", ComponentItem::new)
            .localization(text -> text
                    .source("Ore Prospector (LV)")
                    .translation(MinecraftLocale.ZH_CN, "探矿仪（§7LV§r）"))
            .properties(p -> p.stacksTo(1))
            .onRegister(attach(ElectricStats.createElectricItem(100_000L, GTValues.LV),
                    new ProspectorScannerBehavior(2, GTValues.V[GTValues.LV] / 16L, ProspectorMode.ORE)))
            .register();
    public static ItemEntry<ComponentItem> PROSPECTOR_HV = REGISTRATE.item("prospector.hv", ComponentItem::new)
            .localization(text -> text
                    .source("Advanced Prospector (HV)")
                    .translation(MinecraftLocale.ZH_CN, "进阶探矿仪（§6HV§r）"))
            .properties(p -> p.stacksTo(1))
            .onRegister(attach(ElectricStats.createElectricItem(1_600_000L, GTValues.HV),
                    new ProspectorScannerBehavior(3, GTValues.V[GTValues.HV] / 16L, ProspectorMode.ORE,
                            ProspectorMode.FLUID,
                            GTPMConfigHolder.INSTANCE.machines.doBedrockOres ? ProspectorMode.BEDROCK_ORE : null)))
            .register();
    public static ItemEntry<ComponentItem> PROSPECTOR_LuV = REGISTRATE.item("prospector.luv", ComponentItem::new)
            .localization(text -> text
                    .source("Super Prospector (LuV)")
                    .translation(MinecraftLocale.ZH_CN, "超级探矿仪（§dLuV§r）"))
            .properties(p -> p.stacksTo(1))
            .onRegister(attach(ElectricStats.createElectricItem(1_000_000_000L, GTValues.LuV),
                    new ProspectorScannerBehavior(5, GTValues.V[GTValues.LuV] / 16L, ProspectorMode.ORE,
                            ProspectorMode.FLUID,
                            GTPMConfigHolder.INSTANCE.machines.doBedrockOres ? ProspectorMode.BEDROCK_ORE : null)))
            .register();

    public static ItemEntry<ComponentItem> ITEM_MAGNET_LV = REGISTRATE.item("lv_item_magnet", ComponentItem::new)
            .localization(text -> text
                    .source("LV Item Magnet")
                    .translation(MinecraftLocale.ZH_CN, "§7LV§r物品磁铁"))
            .properties(p -> p.stacksTo(1)
                    .component(GTDataComponents.MAGNET,
                            new ItemMagnetBehavior.MagnetComponent(true, ItemMagnetBehavior.Filter.SIMPLE))
                    .component(GTDataComponents.SIMPLE_ITEM_FILTER,
                            new SimpleItemFilter(true, true, Collections.emptyList())))
            .onRegister(attach(ElectricStats.createElectricItem(100_000L, GTValues.LV), new ItemMagnetBehavior(8)))
            .register();
    public static ItemEntry<ComponentItem> ITEM_MAGNET_HV = REGISTRATE.item("hv_item_magnet", ComponentItem::new)
            .localization(text -> text
                    .source("HV Item Magnet")
                    .translation(MinecraftLocale.ZH_CN, "§6HV§r物品磁铁"))
            .properties(p -> p.stacksTo(1)
                    .component(GTDataComponents.MAGNET,
                            new ItemMagnetBehavior.MagnetComponent(true, ItemMagnetBehavior.Filter.SIMPLE))
                    .component(GTDataComponents.SIMPLE_ITEM_FILTER,
                            new SimpleItemFilter(true, true, Collections.emptyList())))
            .onRegister(attach(ElectricStats.createElectricItem(1_600_000L, GTValues.HV), new ItemMagnetBehavior(32)))
            .register();

    public static ItemEntry<Item> WIRELESS;
    public static ItemEntry<Item> CAMERA;
    @Trans(key = "item.gtpm.terminal.tooltip",
           source = "Shift + R-Click on a controller to automatically build the multiblock",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "潜行右击多方块控制器以自动搭建此多方块结构"))
    public static ItemEntry<ComponentItem> TERMINAL = REGISTRATE.item("terminal", ComponentItem::new)
            .localization(text -> text
                    .source("Terminal")
                    .translation(MinecraftLocale.ZH_CN, "终端"))
            .properties(p -> p.stacksTo(1))
            .onRegister(attach(new TerminalBehavior()))
            .register();
    public static ItemEntry<ComponentItem> MACHINE_MEMORY_CARD = REGISTRATE
            .item("machine_memory_card", ComponentItem::new)
            .localization(text -> text
                    .source("Machine Memory Card")
                    .translation(MinecraftLocale.ZH_CN, "机器内存卡"))
            .properties(p -> p.stacksTo(1))
            .onRegister(attach(new MachineConfigCopyBehaviour()))
            .register();

    public static final ItemEntry<DyeItem>[] DYE_ONLY_ITEMS = new ItemEntry[DyeColor.values().length];
    static {
        DyeColor[] colors = DyeColor.values();
        for (int i = 0; i < colors.length; i++) {
            var dyeColor = colors[i];
            DYE_ONLY_ITEMS[i] = REGISTRATE
                    .item("chemical_%s_dye".formatted(dyeColor.getName()), (props) -> new DyeItem(dyeColor, props))
                    .localization(text -> {
                        text.source("Chemical %s Dye".formatted(toEnglishName(dyeColor.getName())));
                        var localization = MarkerMaterials.Color.COLORS.get(dyeColor).getLocalization();
                        if (localization != null) {
                            String chinese = localization.translations().get(MinecraftLocale.ZH_CN);
                            if (chinese != null) text.translation(MinecraftLocale.ZH_CN, "%s化学染料".formatted(chinese));
                        }
                    })
                    .tag(TagUtil.createItemTag("dyes/" + dyeColor.getName()))
                    .register();
        }
    }

    public static final ItemEntry<ComponentItem>[] SPRAY_CAN_DYES = new ItemEntry[DyeColor.values().length];
    static {
        for (int i = 0; i < DyeColor.values().length; i++) {
            var dyeColor = DyeColor.values()[i];
            SPRAY_CAN_DYES[i] = REGISTRATE.item("%s_dye_spray_can".formatted(dyeColor.getName()), ComponentItem::new)
                    .localization(text -> {
                        text.source("Spray Can (%s)".formatted(toEnglishName(dyeColor.getName())));
                        var localization = MarkerMaterials.Color.COLORS.get(dyeColor).getLocalization();
                        if (localization != null) {
                            String chinese = localization.translations().get(MinecraftLocale.ZH_CN);
                            if (chinese != null) text.translation(MinecraftLocale.ZH_CN, "喷漆罐（%s）".formatted(chinese));
                        }
                    })
                    .properties(p -> p.stacksTo(1))
                    .onRegister(attach(new ColorSprayBehaviour(() -> SPRAY_EMPTY.asStack(), 512, i))).register();
        }
    }

    @Trans(key = "item.gtpm.turbine_rotor.tooltip",
           source = "Turbine Rotors for your power station",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "供涡轮机使用的涡轮转子"))
    public static ItemEntry<ComponentItem> TURBINE_ROTOR = REGISTRATE.item("turbine_rotor", ComponentItem::new)
            .localization(text -> text
                    .source("%s Turbine Rotor")
                    .translation(MinecraftLocale.ZH_CN, "%s涡轮转子"))
            .properties(p -> p.stacksTo(1))
            .model(() -> (ctx, prov) -> createTextureModel(ctx, prov, GTPM.id("item/tools/turbine")))
            .color(() -> IMaterialPartItem::getItemStackColor)
            .onRegister(attach(new TurbineRotorBehaviour())).register();

    @Trans(key = "item.gtpm.neuro_processing_unit.tooltip",
           source = "§7Neuro CPU",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7神经CPU"))
    public static ItemEntry<Item> NEURO_PROCESSOR = REGISTRATE.item("neuro_processing_unit", Item::new)
            .localization(text -> text
                    .source("Neuro Processing Unit")
                    .translation(MinecraftLocale.ZH_CN, "神经处理单元"))
            .register();
    @Trans(key = "item.gtpm.stem_cells.tooltip",
           source = "§7Raw Intelligence",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7智慧原料"))
    public static ItemEntry<Item> STEM_CELLS = REGISTRATE.item("stem_cells", Item::new)
            .localization(text -> text
                    .source("Stem Cells")
                    .translation(MinecraftLocale.ZH_CN, "干细胞"))
            .register();
    @Trans(key = "item.gtpm.petri_dish.tooltip",
           source = "§7For cultivating Cells",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7用来培育细胞"))
    public static ItemEntry<Item> PETRI_DISH = REGISTRATE.item("petri_dish", Item::new)
            .localization(text -> text
                    .source("Petri Dish")
                    .translation(MinecraftLocale.ZH_CN, "培养皿"))
            .register();

    @Trans(key = "item.gtpm.ulv_voltage_coil.tooltip",
           source = "Primitive Coil",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "初级线圈"))
    public static ItemEntry<ComponentItem> VOLTAGE_COIL_ULV = REGISTRATE.item("ulv_voltage_coil", ComponentItem::new)
            .localization(text -> text
                    .source("Ultra Low Voltage Coil")
                    .translation(MinecraftLocale.ZH_CN, "超低压线圈"))
            .onRegister(materialInfo(new ItemMaterialInfo(new MaterialStack(GTMaterials.Lead, GTValues.M * 2),
                    new MaterialStack(GTMaterials.IronMagnetic, GTValues.M / 2))))
            .register();
    @Trans(key = "item.gtpm.lv_voltage_coil.tooltip",
           source = "Basic Coil",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "基础线圈"))
    public static ItemEntry<ComponentItem> VOLTAGE_COIL_LV = REGISTRATE.item("lv_voltage_coil", ComponentItem::new)
            .localization(text -> text
                    .source("Low Voltage Coil")
                    .translation(MinecraftLocale.ZH_CN, "低压线圈"))
            .onRegister(materialInfo(new ItemMaterialInfo(new MaterialStack(GTMaterials.Steel, GTValues.M * 2),
                    new MaterialStack(GTMaterials.IronMagnetic, GTValues.M / 2))))
            .register();
    @Trans(key = "item.gtpm.mv_voltage_coil.tooltip",
           source = "Good Coil",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "优质线圈"))
    public static ItemEntry<ComponentItem> VOLTAGE_COIL_MV = REGISTRATE.item("mv_voltage_coil", ComponentItem::new)
            .localization(text -> text
                    .source("Medium Voltage Coil")
                    .translation(MinecraftLocale.ZH_CN, "中压线圈"))
            .onRegister(materialInfo(new ItemMaterialInfo(new MaterialStack(GTMaterials.Aluminium, GTValues.M * 2),
                    new MaterialStack(GTMaterials.SteelMagnetic, GTValues.M / 2))))
            .register();
    @Trans(key = "item.gtpm.hv_voltage_coil.tooltip",
           source = "Advanced Coil",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "高级线圈"))
    public static ItemEntry<ComponentItem> VOLTAGE_COIL_HV = REGISTRATE.item("hv_voltage_coil", ComponentItem::new)
            .localization(text -> text
                    .source("High Voltage Coil")
                    .translation(MinecraftLocale.ZH_CN, "高压线圈"))
            .onRegister(materialInfo(new ItemMaterialInfo(new MaterialStack(GTMaterials.BlackSteel, GTValues.M * 2),
                    new MaterialStack(GTMaterials.SteelMagnetic, GTValues.M / 2))))
            .register();
    @Trans(key = "item.gtpm.ev_voltage_coil.tooltip",
           source = "Extreme Coil",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "极限线圈"))
    public static ItemEntry<ComponentItem> VOLTAGE_COIL_EV = REGISTRATE.item("ev_voltage_coil", ComponentItem::new)
            .localization(text -> text
                    .source("Extreme Voltage Coil")
                    .translation(MinecraftLocale.ZH_CN, "超高压线圈"))
            .onRegister(materialInfo(new ItemMaterialInfo(new MaterialStack(GTMaterials.Platinum, GTValues.M * 2),
                    new MaterialStack(GTMaterials.NeodymiumMagnetic, GTValues.M / 2))))
            .register();
    @Trans(key = "item.gtpm.iv_voltage_coil.tooltip",
           source = "Elite Coil",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "精英线圈"))
    public static ItemEntry<ComponentItem> VOLTAGE_COIL_IV = REGISTRATE.item("iv_voltage_coil", ComponentItem::new)
            .localization(text -> text
                    .source("Insane Voltage Coil")
                    .translation(MinecraftLocale.ZH_CN, "强导压线圈"))
            .onRegister(materialInfo(new ItemMaterialInfo(new MaterialStack(GTMaterials.Iridium, GTValues.M * 2),
                    new MaterialStack(GTMaterials.NeodymiumMagnetic, GTValues.M / 2))))
            .register();
    @Trans(key = "item.gtpm.luv_voltage_coil.tooltip",
           source = "Master Coil",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "大师线圈"))
    public static ItemEntry<ComponentItem> VOLTAGE_COIL_LuV = REGISTRATE.item("luv_voltage_coil", ComponentItem::new)
            .localization(text -> text
                    .source("Ludicrous Voltage Coil")
                    .translation(MinecraftLocale.ZH_CN, "剧差压线圈"))
            .onRegister(materialInfo(new ItemMaterialInfo(new MaterialStack(GTMaterials.Osmiridium, GTValues.M * 2),
                    new MaterialStack(GTMaterials.SamariumMagnetic, GTValues.M / 2))))
            .register();
    @Trans(key = "item.gtpm.zpm_voltage_coil.tooltip",
           source = "Super Coil",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "超级线圈"))
    public static ItemEntry<ComponentItem> VOLTAGE_COIL_ZPM = REGISTRATE.item("zpm_voltage_coil", ComponentItem::new)
            .localization(text -> text
                    .source("Zero Point Module Voltage Coil")
                    .translation(MinecraftLocale.ZH_CN, "零点压线圈"))
            .onRegister(materialInfo(new ItemMaterialInfo(new MaterialStack(GTMaterials.Europium, GTValues.M * 2),
                    new MaterialStack(GTMaterials.SamariumMagnetic, GTValues.M / 2))))
            .register();
    @Trans(key = "item.gtpm.uv_voltage_coil.tooltip",
           source = "Ultimate Coil",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "终极线圈"))
    public static ItemEntry<ComponentItem> VOLTAGE_COIL_UV = REGISTRATE.item("uv_voltage_coil", ComponentItem::new)
            .localization(text -> text
                    .source("Ultimate Voltage Coil")
                    .translation(MinecraftLocale.ZH_CN, "极限压线圈"))
            .onRegister(materialInfo(new ItemMaterialInfo(new MaterialStack(GTMaterials.Tritanium, GTValues.M * 2),
                    new MaterialStack(GTMaterials.SamariumMagnetic, GTValues.M / 2))))
            .register();

    @Trans(key = "metaitem.clipboard.tooltip",
           source = "Can be written on (without any writing Instrument). Right-click on Wall to place, and Shift-Right-Click to remove",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "可以在上面书写（无需任何书写工具）。手持右键在墙面上放置，潜行右击取下。"))
    public static ItemEntry<Item> CLIPBOARD;

    public static ItemEntry<ArmorComponentItem> NIGHTVISION_GOGGLES = REGISTRATE
            .item("nightvision_goggles",
                    (p) -> new ArmorComponentItem(GTArmorMaterials.GOGGLES, ArmorItem.Type.HELMET, p)
                            .setArmorLogic(new NightvisionGoggles(2,
                                    80_000L * (long) Math.max(1,
                                            Math.pow(1, GTPMConfigHolder.INSTANCE.tools.voltageTierNightVision - 1)),
                                    GTPMConfigHolder.INSTANCE.tools.voltageTierNightVision, ArmorItem.Type.HELMET)))
            .localization(text -> text
                    .source("Nightvision Goggles")
                    .translation(MinecraftLocale.ZH_CN, "夜视护目镜"))
            .transform(addArmorClientExtensions())
            .tag(ItemTags.HEAD_ARMOR)
            .register();

    public static ItemEntry<ArmorComponentItem> NANO_CHESTPLATE = REGISTRATE
            .item("nanomuscle_chestplate",
                    (p) -> new ArmorComponentItem(GTArmorMaterials.ARMOR, ArmorItem.Type.CHESTPLATE, p)
                            .setArmorLogic(new NanoMuscleSuite(ArmorItem.Type.CHESTPLATE,
                                    512,
                                    6_400_000L * (long) Math.max(1,
                                            Math.pow(4, GTPMConfigHolder.INSTANCE.tools.voltageTierNanoSuit - 3)),
                                    GTPMConfigHolder.INSTANCE.tools.voltageTierNanoSuit)))
            .localization(text -> text
                    .source("NanoMuscle™ Suite Chestplate")
                    .translation(MinecraftLocale.ZH_CN, "纳米肌体™套装胸甲"))
            .properties(p -> p.rarity(Rarity.UNCOMMON))
            .transform(addArmorClientExtensions())
            .tag(ItemTags.CHEST_ARMOR)
            .register();
    public static ItemEntry<ArmorComponentItem> NANO_LEGGINGS = REGISTRATE
            .item("nanomuscle_leggings",
                    (p) -> new ArmorComponentItem(GTArmorMaterials.ARMOR, ArmorItem.Type.LEGGINGS, p)
                            .setArmorLogic(new NanoMuscleSuite(ArmorItem.Type.LEGGINGS,
                                    512,
                                    6_400_000L * (long) Math.max(1,
                                            Math.pow(4, GTPMConfigHolder.INSTANCE.tools.voltageTierNanoSuit - 3)),
                                    GTPMConfigHolder.INSTANCE.tools.voltageTierNanoSuit)))
            .localization(text -> text
                    .source("NanoMuscle™ Suite Leggings")
                    .translation(MinecraftLocale.ZH_CN, "纳米肌体™套装护腿"))
            .properties(p -> p.rarity(Rarity.UNCOMMON))
            .transform(addArmorClientExtensions())
            .tag(ItemTags.LEG_ARMOR)
            .register();
    public static ItemEntry<ArmorComponentItem> NANO_BOOTS = REGISTRATE
            .item("nanomuscle_boots", (p) -> new ArmorComponentItem(GTArmorMaterials.ARMOR, ArmorItem.Type.BOOTS, p)
                    .setArmorLogic(new NanoMuscleSuite(ArmorItem.Type.BOOTS,
                            512,
                            6_400_000L * (long) Math.max(1,
                                    Math.pow(4, GTPMConfigHolder.INSTANCE.tools.voltageTierNanoSuit - 3)),
                            GTPMConfigHolder.INSTANCE.tools.voltageTierNanoSuit)))
            .localization(text -> text
                    .source("NanoMuscle™ Suite Boots")
                    .translation(MinecraftLocale.ZH_CN, "纳米肌体™套装靴子"))
            .properties(p -> p.rarity(Rarity.UNCOMMON))
            .tag(ItemTags.FOOT_ARMOR)
            .tag(CustomTags.STEP_BOOTS)
            .register();
    public static ItemEntry<ArmorComponentItem> NANO_HELMET = REGISTRATE
            .item("nanomuscle_helmet", (p) -> new ArmorComponentItem(GTArmorMaterials.ARMOR, ArmorItem.Type.HELMET, p)
                    .setArmorLogic(new NanoMuscleSuite(ArmorItem.Type.HELMET,
                            512,
                            6_400_000L * (long) Math.max(1,
                                    Math.pow(4, GTPMConfigHolder.INSTANCE.tools.voltageTierNanoSuit - 3)),
                            GTPMConfigHolder.INSTANCE.tools.voltageTierNanoSuit)))
            .localization(text -> text
                    .source("NanoMuscle™ Suite Helmet")
                    .translation(MinecraftLocale.ZH_CN, "纳米肌体™套装头盔"))
            .properties(p -> p.rarity(Rarity.UNCOMMON))
            .transform(addArmorClientExtensions())
            .tag(ItemTags.HEAD_ARMOR)
            .register();

    public static ItemEntry<ArmorComponentItem> FACE_MASK = REGISTRATE
            .item("face_mask",
                    (p) -> new ArmorComponentItem(GTArmorMaterials.BAD_PPE_EQUIPMENT, ArmorItem.Type.HELMET, p)
                            .setArmorLogic(new HazmatSuit(ArmorItem.Type.HELMET, "bad_hazmat")))
            .localization(text -> text
                    .source("Face Mask")
                    .translation(MinecraftLocale.ZH_CN, "面罩"))
            .transform(addArmorClientExtensions())
            .tag(ItemTags.HEAD_ARMOR)
            .tag(CustomTags.PPE_ARMOR)
            .onRegister(attach(new TooltipBehavior(tooltips -> {
                tooltips.add(Component.translatable("gtpm.hazard_trigger.protection.description"));
                tooltips.add(Component.translatable("gtpm.hazard_trigger.inhalation"));
            })))
            .register();
    public static ItemEntry<ArmorComponentItem> RUBBER_GLOVES = REGISTRATE
            .item("rubber_gloves",
                    (p) -> new ArmorComponentItem(GTArmorMaterials.BAD_PPE_EQUIPMENT, ArmorItem.Type.HELMET, p)
                            .setArmorLogic(new HazmatSuit(ArmorItem.Type.CHESTPLATE, "bad_hazmat")))
            .localization(text -> text
                    .source("Rubber Gloves")
                    .translation(MinecraftLocale.ZH_CN, "橡胶手套"))
            .transform(addArmorClientExtensions())
            .tag(ItemTags.CHEST_ARMOR)
            .tag(CustomTags.PPE_ARMOR)
            .onRegister(attach(new TooltipBehavior(tooltips -> {
                tooltips.add(Component.translatable("gtpm.hazard_trigger.protection.description"));
                tooltips.add(Component.translatable("gtpm.hazard_trigger.skin_contact"));
            })))
            .register();
    public static ItemEntry<ArmorComponentItem> HAZMAT_CHESTPLATE = REGISTRATE
            .item("hazmat_chestpiece",
                    (p) -> new ArmorComponentItem(GTArmorMaterials.GOOD_PPE_EQUIPMENT, ArmorItem.Type.CHESTPLATE, p)
                            .setArmorLogic(new HazmatSuit(ArmorItem.Type.CHESTPLATE, "hazmat")))
            .localization(text -> text
                    .source("Hazardous Materials Suit Chestpiece")
                    .translation(MinecraftLocale.ZH_CN, "防化胸甲"))
            .properties(p -> p.rarity(Rarity.UNCOMMON))
            .transform(addArmorClientExtensions())
            .tag(ItemTags.CHEST_ARMOR)
            .tag(CustomTags.PPE_ARMOR)
            .register();
    public static ItemEntry<ArmorComponentItem> HAZMAT_LEGGINGS = REGISTRATE
            .item("hazmat_leggings",
                    (p) -> new ArmorComponentItem(GTArmorMaterials.GOOD_PPE_EQUIPMENT, ArmorItem.Type.LEGGINGS, p)
                            .setArmorLogic(new HazmatSuit(ArmorItem.Type.LEGGINGS, "hazmat")))
            .localization(text -> text
                    .source("Hazardous Materials Suit Leggings")
                    .translation(MinecraftLocale.ZH_CN, "防化护腿"))
            .properties(p -> p.rarity(Rarity.UNCOMMON))
            .transform(addArmorClientExtensions())
            .tag(ItemTags.LEG_ARMOR)
            .tag(CustomTags.PPE_ARMOR)
            .register();
    public static ItemEntry<ArmorComponentItem> HAZMAT_BOOTS = REGISTRATE
            .item("hazmat_boots",
                    (p) -> new ArmorComponentItem(GTArmorMaterials.GOOD_PPE_EQUIPMENT, ArmorItem.Type.BOOTS, p)
                            .setArmorLogic(new HazmatSuit(ArmorItem.Type.BOOTS, "hazmat")))
            .localization(text -> text
                    .source("Hazardous Materials Suit Boots")
                    .translation(MinecraftLocale.ZH_CN, "防化靴子"))
            .properties(p -> p.rarity(Rarity.UNCOMMON))
            .transform(addArmorClientExtensions())
            .tag(ItemTags.FOOT_ARMOR)
            .tag(CustomTags.PPE_ARMOR)
            .register();
    public static ItemEntry<ArmorComponentItem> HAZMAT_HELMET = REGISTRATE
            .item("hazmat_headpiece",
                    (p) -> new ArmorComponentItem(GTArmorMaterials.GOOD_PPE_EQUIPMENT, ArmorItem.Type.HELMET, p)
                            .setArmorLogic(new HazmatSuit(ArmorItem.Type.HELMET, "hazmat")))
            .localization(text -> text
                    .source("Hazardous Materials Suit Headpiece")
                    .translation(MinecraftLocale.ZH_CN, "防化头盔"))
            .properties(p -> p.rarity(Rarity.UNCOMMON))
            .transform(addArmorClientExtensions())
            .tag(ItemTags.HEAD_ARMOR)
            .tag(CustomTags.PPE_ARMOR)
            .register();

    public static ItemEntry<ArmorComponentItem> QUANTUM_CHESTPLATE = REGISTRATE
            .item("quarktech_chestplate",
                    (p) -> new ArmorComponentItem(GTArmorMaterials.ARMOR, ArmorItem.Type.CHESTPLATE, p)
                            .setArmorLogic(new QuarkTechSuite(ArmorItem.Type.CHESTPLATE,
                                    8192,
                                    100_000_000L * (long) Math.max(1,
                                            Math.pow(4, GTPMConfigHolder.INSTANCE.tools.voltageTierQuarkTech - 5)),
                                    GTPMConfigHolder.INSTANCE.tools.voltageTierQuarkTech)))
            .localization(text -> text
                    .source("QuarkTech™ Suite Chestplate")
                    .translation(MinecraftLocale.ZH_CN, "夸克高科™套装胸甲"))
            .properties(p -> p.rarity(Rarity.RARE))
            .transform(addArmorClientExtensions())
            .tag(ItemTags.CHEST_ARMOR)
            .tag(ItemTags.FREEZE_IMMUNE_WEARABLES)
            .tag(CustomTags.PPE_ARMOR)
            .register();
    public static ItemEntry<ArmorComponentItem> QUANTUM_LEGGINGS = REGISTRATE
            .item("quarktech_leggings",
                    (p) -> new ArmorComponentItem(GTArmorMaterials.ARMOR, ArmorItem.Type.LEGGINGS, p)
                            .setArmorLogic(new QuarkTechSuite(ArmorItem.Type.LEGGINGS,
                                    8192,
                                    100_000_000L * (long) Math.max(1,
                                            Math.pow(4, GTPMConfigHolder.INSTANCE.tools.voltageTierQuarkTech - 5)),
                                    GTPMConfigHolder.INSTANCE.tools.voltageTierQuarkTech)))
            .localization(text -> text
                    .source("QuarkTech™ Suite Leggings")
                    .translation(MinecraftLocale.ZH_CN, "夸克高科™套装护腿"))
            .properties(p -> p.rarity(Rarity.RARE))
            .transform(addArmorClientExtensions())
            .tag(ItemTags.LEG_ARMOR)
            .tag(CustomTags.PPE_ARMOR)
            .register();
    public static ItemEntry<ArmorComponentItem> QUANTUM_BOOTS = REGISTRATE
            .item("quarktech_boots", (p) -> new ArmorComponentItem(GTArmorMaterials.ARMOR, ArmorItem.Type.BOOTS, p)
                    .setArmorLogic(new QuarkTechSuite(ArmorItem.Type.BOOTS,
                            8192,
                            100_000_000L * (long) Math.max(1,
                                    Math.pow(4, GTPMConfigHolder.INSTANCE.tools.voltageTierQuarkTech - 5)),
                            GTPMConfigHolder.INSTANCE.tools.voltageTierQuarkTech)))
            .localization(text -> text
                    .source("QuarkTech™ Suite Boots")
                    .translation(MinecraftLocale.ZH_CN, "夸克高科™套装靴子"))
            .properties(p -> p.rarity(Rarity.RARE))
            .transform(addArmorClientExtensions())
            .tag(ItemTags.FOOT_ARMOR)
            .tag(CustomTags.PPE_ARMOR)
            .tag(CustomTags.STEP_BOOTS)
            .register();
    public static ItemEntry<ArmorComponentItem> QUANTUM_HELMET = REGISTRATE
            .item("quarktech_helmet", (p) -> new ArmorComponentItem(GTArmorMaterials.ARMOR, ArmorItem.Type.HELMET, p)
                    .setArmorLogic(new QuarkTechSuite(ArmorItem.Type.HELMET,
                            8192,
                            100_000_000L * (long) Math.max(1,
                                    Math.pow(4, GTPMConfigHolder.INSTANCE.tools.voltageTierQuarkTech - 5)),
                            GTPMConfigHolder.INSTANCE.tools.voltageTierQuarkTech)))
            .localization(text -> text
                    .source("QuarkTech™ Suite Helmet")
                    .translation(MinecraftLocale.ZH_CN, "夸克高科™套装头盔"))
            .properties(p -> p.rarity(Rarity.RARE))
            .transform(addArmorClientExtensions())
            .tag(ItemTags.HEAD_ARMOR)
            .tag(CustomTags.PPE_ARMOR)
            .register();

    public static ItemEntry<ArmorComponentItem> LIQUID_FUEL_JETPACK = REGISTRATE
            .item("liquid_fuel_jetpack",
                    (p) -> new ArmorComponentItem(GTArmorMaterials.JETPACK, ArmorItem.Type.CHESTPLATE, p)
                            .setArmorLogic(new PowerlessJetpack()))
            .localization(text -> text
                    .source("Liquid Fuel Jetpack")
                    .translation(MinecraftLocale.ZH_CN, "液体燃料喷气背包"))
            .transform(addArmorClientExtensions())
            .tag(ItemTags.CHEST_ARMOR)
            .setData(ProviderType.ITEM_MODEL, NonNullBiConsumer.noop())
            .register();
    public static ItemEntry<ArmorComponentItem> ELECTRIC_JETPACK = REGISTRATE
            .item("electric_jetpack",
                    (p) -> new ArmorComponentItem(GTArmorMaterials.JETPACK, ArmorItem.Type.CHESTPLATE, p)
                            .setArmorLogic(new Jetpack(15,
                                    1_000_000L * (long) Math.max(1,
                                            Math.pow(4, GTPMConfigHolder.INSTANCE.tools.voltageTierImpeller - 2)),
                                    GTPMConfigHolder.INSTANCE.tools.voltageTierImpeller)))
            .localization(text -> text
                    .source("Electric Jetpack")
                    .translation(MinecraftLocale.ZH_CN, "电力喷气背包"))
            .properties(p -> p.rarity(Rarity.UNCOMMON))
            .transform(addArmorClientExtensions())
            .tag(ItemTags.CHEST_ARMOR)
            .model(() -> overrideModel(GTPM.id("electric_jetpack"), 8))
            .onRegister(modelPredicate(GTPM.id("electric_jetpack"), ElectricStats::getStoredPredicate))
            .register();

    public static ItemEntry<ArmorComponentItem> ELECTRIC_JETPACK_ADVANCED = REGISTRATE
            .item("advanced_electric_jetpack",
                    (p) -> new ArmorComponentItem(GTArmorMaterials.JETPACK, ArmorItem.Type.CHESTPLATE, p)
                            .setArmorLogic(new AdvancedJetpack(256,
                                    6_400_000L * (long) Math.max(1,
                                            Math.pow(4, GTPMConfigHolder.INSTANCE.tools.voltageTierAdvImpeller - 4)),
                                    GTPMConfigHolder.INSTANCE.tools.voltageTierAdvImpeller)))
            .localization(text -> text
                    .source("Advanced Electric Jetpack")
                    .translation(MinecraftLocale.ZH_CN, "进阶电力喷气背包"))
            .properties(p -> p.rarity(Rarity.RARE))
            .transform(addArmorClientExtensions())
            .tag(ItemTags.CHEST_ARMOR)
            .register();
    public static ItemEntry<ArmorComponentItem> NANO_CHESTPLATE_ADVANCED = REGISTRATE
            .item("advanced_nanomuscle_chestplate",
                    (p) -> new ArmorComponentItem(GTArmorMaterials.ARMOR, ArmorItem.Type.CHESTPLATE, p)
                            .setArmorLogic(new AdvancedNanoMuscleSuite(512,
                                    12_800_000L * (long) Math.max(1,
                                            Math.pow(4, GTPMConfigHolder.INSTANCE.tools.voltageTierAdvNanoSuit - 3)),
                                    GTPMConfigHolder.INSTANCE.tools.voltageTierAdvNanoSuit)))
            .localization(text -> text
                    .source("Advanced NanoMuscle™ Suite Chestplate")
                    .translation(MinecraftLocale.ZH_CN, "纳米肌体™进阶套装胸甲"))
            .properties(p -> p.rarity(Rarity.RARE))
            .transform(addArmorClientExtensions())
            .tag(ItemTags.CHEST_ARMOR)
            .tag(CustomTags.PPE_ARMOR)
            .register();
    public static ItemEntry<ArmorComponentItem> QUANTUM_CHESTPLATE_ADVANCED = REGISTRATE
            .item("advanced_quarktech_chestplate", (p) -> new ArmorComponentItem(GTArmorMaterials.ARMOR,
                    ArmorItem.Type.CHESTPLATE, p)
                    .setArmorLogic(new AdvancedQuarkTechSuite(8192,
                            1_000_000_000L *
                                    (long) Math.max(1,
                                            Math.pow(4, GTPMConfigHolder.INSTANCE.tools.voltageTierAdvQuarkTech - 6)),
                            GTPMConfigHolder.INSTANCE.tools.voltageTierAdvQuarkTech)))
            .localization(text -> text
                    .source("Advanced QuarkTech™ Suite Chestplate")
                    .translation(MinecraftLocale.ZH_CN, "夸克高科™进阶套装胸甲"))
            .properties(p -> p.rarity(Rarity.EPIC))
            .transform(addArmorClientExtensions())
            .tag(ItemTags.CHEST_ARMOR)
            .tag(ItemTags.FREEZE_IMMUNE_WEARABLES)
            .tag(CustomTags.PPE_ARMOR)
            .register();

    public static ItemEntry<Item> POWER_THRUSTER = REGISTRATE.item("power_thruster", Item::new)
            .localization(text -> text
                    .source("Power Thruster")
                    .translation(MinecraftLocale.ZH_CN, "电力推进器"))
            .properties(p -> p.rarity(Rarity.UNCOMMON)).register();
    public static ItemEntry<Item> POWER_THRUSTER_ADVANCED = REGISTRATE.item("advanced_power_thruster", Item::new)
            .localization(text -> text
                    .source("Advanced Power Thruster")
                    .translation(MinecraftLocale.ZH_CN, "进阶电力推进器"))
            .properties(p -> p.rarity(Rarity.RARE))
            .register();
    public static ItemEntry<Item> GRAVITATION_ENGINE = REGISTRATE.item("gravitation_engine_unit", Item::new)
            .localization(text -> text
                    .source("Gravitation Engine Unit")
                    .translation(MinecraftLocale.ZH_CN, "重力引擎单元"))
            .properties(p -> p.rarity(Rarity.EPIC))
            .register();

    public static ItemEntry<Item> SUS_RECORD = REGISTRATE
            .item("sus_record", Item::new)
            .localization(text -> text
                    .source("Music Disc")
                    .translation(MinecraftLocale.ZH_CN, "音乐唱片"))
            .properties(p -> p.jukeboxPlayable(GTJukeboxSongs.SUS))
            .register();
    @Trans(key = "item.gtpm.nan_certificate.tooltip",
           source = "Challenge Accepted!",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "已接受挑战！"))
    public static ItemEntry<Item> NAN_CERTIFICATE = REGISTRATE.item("nan_certificate", Item::new)
            .localization(text -> text
                    .source("Certificate of Not Being a Noob Anymore")
                    .translation(MinecraftLocale.ZH_CN, "不再是菜鸟的证明"))
            .properties(p -> p.rarity(Rarity.EPIC))
            .register();

    public static ItemEntry<ComponentItem> FERTILIZER = REGISTRATE.item("fertilizer", ComponentItem::new)
            .localization(text -> text
                    .source("Fertilizer")
                    .translation(MinecraftLocale.ZH_CN, "肥料"))
            .onRegister(attach(new FertilizerBehavior())).register();
    @Trans(key = "item.gtpm.blacklight.tooltip",
           source = "Long-Wave §dUltraviolet§7 light source",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7长波§d紫外线§7光源"))
    public static ItemEntry<Item> BLACKLIGHT = REGISTRATE.item("blacklight", Item::new)
            .localization(text -> text
                    .source("Blacklight")
                    .translation(MinecraftLocale.ZH_CN, "黑光灯"))
            .register();

    public static ItemEntry<BoatItem> RUBBER_BOAT = REGISTRATE
            .item("rubber_boat", p -> new BoatItem(false, GTEnumProxies.RUBBER_BOAT_PROXY.getValue(), p))
            .localization(text -> text
                    .source("Rubber Boat")
                    .translation(MinecraftLocale.ZH_CN, "橡胶木船"))
            .register();

    public static ItemEntry<BoatItem> TREATED_WOOD_BOAT = REGISTRATE
            .item("treated_wood_boat", p -> new BoatItem(false, GTEnumProxies.TREATED_WOOD_BOAT_PROXY.getValue(), p))
            .localization(text -> text
                    .source("Treated Wood Boat")
                    .translation(MinecraftLocale.ZH_CN, "防腐木船"))
            .register();

    public static ItemEntry<BoatItem> RUBBER_CHEST_BOAT = REGISTRATE
            .item("rubber_chest_boat", p -> new BoatItem(true, GTEnumProxies.RUBBER_BOAT_PROXY.getValue(), p))
            .localization(text -> text
                    .source("Rubber Boat with Chest")
                    .translation(MinecraftLocale.ZH_CN, "橡胶木运输船"))
            .register();

    public static ItemEntry<BoatItem> TREATED_WOOD_CHEST_BOAT = REGISTRATE
            .item("treated_wood_chest_boat",
                    p -> new BoatItem(true, GTEnumProxies.TREATED_WOOD_BOAT_PROXY.getValue(), p))
            .localization(text -> text
                    .source("Treated Wood Boat with Chest")
                    .translation(MinecraftLocale.ZH_CN, "防腐木运输船"))
            .register();

    public static ItemEntry<ComponentItem> TEXT_MODULE = REGISTRATE.item("text_module", ComponentItem::new)
            .localization(text -> text
                    .source("Text Module")
                    .translation(MinecraftLocale.ZH_CN, "文本模块"))
            .onRegister(attach(new TextModuleBehaviour()))
            .register();

    public static ItemEntry<ComponentItem> IMAGE_MODULE = REGISTRATE.item("image_module", ComponentItem::new)
            .localization(text -> text
                    .source("Image Module")
                    .translation(MinecraftLocale.ZH_CN, "图像模块"))
            .onRegister(attach(new ImageModuleBehaviour()))
            .register();

    public static void init() {
        // GTMaterialItems.generateMaterialItems();
        // GTMaterialItems.generateTools();
        // GTMaterialItems.generateArmors();
    }

    public static <T extends ItemLike> NonNullConsumer<T> materialInfo(ItemMaterialInfo materialInfo) {
        return item -> ItemMaterialData.registerMaterialInfo(item, materialInfo);
    }

    public static <P, T extends Item,
            S2 extends ItemBuilder<T, P>> NonNullFunction<S2, S2> unificationItem(@NotNull TagPrefix tagPrefix,
                                                                                  @NotNull Material mat) {
        return builder -> {
            builder.onRegister(item -> {
                Supplier<ItemLike> supplier = GTMemoizer.memoize(() -> item);
                MaterialEntry entry = new MaterialEntry(tagPrefix, mat);
                GTMaterialItems.toUnify.put(entry, supplier);
                ItemMaterialData.registerMaterialEntry(supplier, entry);
            });
            return builder;
        };
    }

    public static <T extends Item> void cauldronInteraction(T item) {
        if (item instanceof TagPrefixItem tagPrefixItem &&
                GTMaterialItems.purifyMap.containsKey(tagPrefixItem.tagPrefix)) {
            CauldronInteraction.WATER.map().put(item, (state, world, pos, player, hand, stack) -> {
                if (!world.isClientSide) {
                    Item stackItem = stack.getItem();
                    if (stackItem instanceof TagPrefixItem prefixItem) {
                        if (!GTMaterialItems.purifyMap.containsKey(prefixItem.tagPrefix))
                            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
                        if (!state.hasProperty(LayeredCauldronBlock.LEVEL)) {
                            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
                        }

                        int level = state.getValue(LayeredCauldronBlock.LEVEL);
                        if (level == 0)
                            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;

                        player.setItemInHand(hand,
                                ChemicalHelper.get(GTMaterialItems.purifyMap.get(prefixItem.tagPrefix),
                                        prefixItem.material, stack.getCount()));
                        player.awardStat(Stats.USE_CAULDRON);
                        player.awardStat(Stats.ITEM_USED.get(stackItem));
                        LayeredCauldronBlock.lowerFillLevel(state, world, pos);

                    }
                }

                return ItemInteractionResult.sidedSuccess(world.isClientSide);
            });

        }
    }

    public static <T extends IComponentItem> NonNullConsumer<T> attach(IItemComponent components) {
        return item -> item.attachComponents(components);
    }

    public static <T extends IComponentItem> NonNullConsumer<T> attach(IItemComponent... components) {
        return item -> item.attachComponents(components);
    }

    public static <T extends Item> NonNullConsumer<T> modelPredicate(ResourceLocation predicate,
                                                                     StackProperty property) {
        return item -> {
            if (GTPM.isClientSide()) {
                ItemProperties.register(item, predicate, (itemStack, c, l, i) -> property.apply(itemStack));
            }
        };
    }

    @SuppressWarnings("deprecation")
    public static <T extends Item> NonNullConsumer<T> modelPredicate(ResourceLocation predicate,
                                                                     Supplier<Supplier<ItemPropertyFunction>> property) {
        return item -> {
            if (GTPM.isClientSide()) {
                ItemProperties.register(item, predicate, property.get().get());
            }
        };
    }

    @SuppressWarnings("removal")
    public static <P,
            S2 extends ItemBuilder<ArmorComponentItem, P>> NonNullFunction<S2, S2> addArmorClientExtensions() {
        return builder -> {
            builder.clientExtension(() -> () -> new IClientItemExtensions() {

                @Override
                public @NotNull HumanoidModel<?> getHumanoidArmorModel(LivingEntity livingEntity, ItemStack itemStack,
                                                                       EquipmentSlot equipmentSlot,
                                                                       HumanoidModel<?> original) {
                    return ((ArmorComponentItem) itemStack.getItem()).getArmorLogic().getArmorModel(livingEntity,
                            itemStack, equipmentSlot, original);
                }
            });
            return builder;
        };
    }

    @FunctionalInterface
    public interface StackProperty {

        float apply(ItemStack stack);
    }

    @TransVariants(keyPrefix = "gtpm.hazard_trigger",
                   value = {
                           @Variant(id = "inhalation",
                                    source = "Inhalation",
                                    translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "吸入")),
                           @Variant(id = "protection.description",
                                    source = "Protects from:",
                                    translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "免受：")),
                           @Variant(id = "skin_contact",
                                    source = "Skin contact",
                                    translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "皮肤接触")),
                   })
    private static final class GtpmTranslations {}
}
