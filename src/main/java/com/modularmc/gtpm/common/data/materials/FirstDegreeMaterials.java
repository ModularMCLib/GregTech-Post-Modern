package com.modularmc.gtpm.common.data.materials;

import com.modularmc.gtpm.GTPM;
import com.modularmc.gtpm.api.data.chemical.material.Material;
import com.modularmc.gtpm.api.data.chemical.material.properties.ArmorProperty;
import com.modularmc.gtpm.api.data.chemical.material.properties.BlastProperty.GasTier;
import com.modularmc.gtpm.api.data.chemical.material.properties.HazardProperty;
import com.modularmc.gtpm.api.data.chemical.material.properties.PropertyKey;
import com.modularmc.gtpm.api.data.chemical.material.properties.ToolProperty;
import com.modularmc.gtpm.api.fluids.FluidBuilder;
import com.modularmc.gtpm.api.fluids.FluidState;
import com.modularmc.gtpm.api.fluids.attribute.FluidAttributes;
import com.modularmc.gtpm.api.item.tool.GTToolType;
import com.modularmc.gtpm.common.data.GTMedicalConditions;

import com.modularmc.registrate.localization.MinecraftLocale;

import net.minecraft.world.item.enchantment.Enchantments;

import static com.modularmc.gtpm.api.GTValues.*;
import static com.modularmc.gtpm.api.data.chemical.material.info.MaterialFlags.*;
import static com.modularmc.gtpm.api.data.chemical.material.info.MaterialIconSet.*;
import static com.modularmc.gtpm.common.data.GTMaterials.*;

public class FirstDegreeMaterials {

    public static void register() {
        Almandine = new Material.Builder(GTPM.id("almandine"))
                .localization(text -> text.source("Almandine")
                        .translation(MinecraftLocale.ZH_CN, "铁铝榴石"))
                .gem(1).ore(3, 1)
                .color(0xa21717).secondaryColor(0x4b1e0c)
                .components(Aluminium, 2, Iron, 3, Silicon, 3, Oxygen, 12)
                .buildAndRegister();

        Andradite = new Material.Builder(GTPM.id("andradite"))
                .localization(text -> text.source("Andradite")
                        .translation(MinecraftLocale.ZH_CN, "钙铁榴石"))
                .gem(1)
                .color(0xffce26).secondaryColor(0x647d59).iconSet(RUBY)
                .components(Calcium, 3, Iron, 2, Silicon, 3, Oxygen, 12)
                .buildAndRegister();

        AnnealedCopper = new Material.Builder(GTPM.id("annealed_copper"))
                .localization(text -> text.source("Annealed Copper")
                        .translation(MinecraftLocale.ZH_CN, "退火铜"))
                .ingot()
                .liquid(new FluidBuilder().temperature(1358))
                .color(0xf2c079).secondaryColor(0xe45534).iconSet(BRIGHT)
                .appendFlags(EXT2_METAL, MORTAR_GRINDABLE, GENERATE_FINE_WIRE)
                .components(Copper, 1)
                .cableProperties(V[MV], 1, 1)
                .buildAndRegister();
        Copper.getProperty(PropertyKey.INGOT).setArcSmeltingInto(AnnealedCopper);

        Asbestos = new Material.Builder(GTPM.id("asbestos"))
                .localization(text -> text.source("Asbestos")
                        .translation(MinecraftLocale.ZH_CN, "石棉"))
                .dust(1).ore(3, 1)
                .color(0xE6E6E6).secondaryColor(0xdbd7bf)
                .hazard(HazardProperty.HazardTrigger.INHALATION, GTMedicalConditions.ASBESTOSIS)
                .components(Magnesium, 3, Silicon, 2, Hydrogen, 4, Oxygen, 9)
                .buildAndRegister();

        Ash = new Material.Builder(GTPM.id("ash"))
                .localization(text -> text.source("Ash")
                        .translation(MinecraftLocale.ZH_CN, "灰烬"))
                .dust(1)
                .color(0xd1d1d1).secondaryColor(0x8b8989)
                .flags(DISABLE_DECOMPOSITION)
                .components(Carbon, 1)
                .buildAndRegister();

        Hematite = new Material.Builder(GTPM.id("hematite"))
                .localization(text -> text.source("Hematite")
                        .translation(MinecraftLocale.ZH_CN, "赤铁矿"))
                .dust().ore()
                .color(0xff7161).secondaryColor(0x330817)
                .components(Iron, 2, Oxygen, 3)
                .buildAndRegister();

        BatteryAlloy = new Material.Builder(GTPM.id("battery_alloy"))
                .localization(text -> text.source("Battery Alloy")
                        .translation(MinecraftLocale.ZH_CN, "电池合金"))
                .ingot(1)
                .liquid(new FluidBuilder().temperature(660))
                .color(0xcac0ff).secondaryColor(0x5b0020)
                .appendFlags(EXT_METAL)
                .components(Lead, 4, Antimony, 1)
                .buildAndRegister();

        BlueTopaz = new Material.Builder(GTPM.id("blue_topaz"))
                .localization(text -> text.source("Blue Topaz")
                        .translation(MinecraftLocale.ZH_CN, "蓝黄玉"))
                .gem(3).ore(2, 1)
                .color(0xdbfeff).secondaryColor(0xa0c4d7).iconSet(GEM_HORIZONTAL)
                .appendFlags(EXT_METAL, NO_SMASHING, NO_SMELTING, HIGH_SIFTER_OUTPUT)
                .components(Aluminium, 2, Silicon, 1, Oxygen, 4, Fluorine, 2)
                .buildAndRegister();

        Bone = new Material.Builder(GTPM.id("bone"))
                .localization(text -> text.source("Bone")
                        .translation(MinecraftLocale.ZH_CN, "骨头"))
                .dust(1)
                .color(0xfcfbed).secondaryColor(0xa0a38b)
                .flags(MORTAR_GRINDABLE, EXCLUDE_BLOCK_CRAFTING_BY_HAND_RECIPES, DISABLE_DECOMPOSITION)
                .components(Calcium, 3)
                .buildAndRegister();

        Brass = new Material.Builder(GTPM.id("brass"))
                .localization(text -> text.source("Brass")
                        .translation(MinecraftLocale.ZH_CN, "黄铜"))
                .ingot(1)
                .liquid(new FluidBuilder().temperature(1160))
                .color(0xffe36e).secondaryColor(0x935828).iconSet(SHINY)
                .appendFlags(EXT2_METAL, MORTAR_GRINDABLE)
                .components(Zinc, 1, Copper, 3)
                .rotorStats(130, 120, 3.0f, 152)
                .itemPipeProperties(2048, 1)
                .buildAndRegister();

        Bronze = new Material.Builder(GTPM.id("bronze"))
                .localization(text -> text.source("Bronze")
                        .translation(MinecraftLocale.ZH_CN, "青铜"))
                .ingot()
                .liquid(new FluidBuilder().temperature(1357))
                .color(0xffc370).secondaryColor(0x806752).iconSet(METALLIC)
                .appendFlags(EXT2_METAL, MORTAR_GRINDABLE, GENERATE_ROTOR, GENERATE_FRAME, GENERATE_SMALL_GEAR,
                        GENERATE_FOIL, GENERATE_GEAR)
                .components(Tin, 1, Copper, 3)
                .toolStats(ToolProperty.Builder.of(3.0F, 2.0F, 192, 2)
                        .enchantability(18).addTypes(GTToolType.MORTAR).build())
                .armorStats(ArmorProperty.Builder.of(17, new int[] { 3, 7, 6, 2 })
                        .enchantability(8).build())
                .rotorStats(115, 105, 2.5f, 192)
                .fluidPipeProperties(1696, 20, true)
                .buildAndRegister();

        Goethite = new Material.Builder(GTPM.id("goethite"))
                .localization(text -> text.source("Goethite")
                        .translation(MinecraftLocale.ZH_CN, "针铁矿"))
                .dust(1).ore()
                .color(0x97873a).secondaryColor(0x313131).iconSet(METALLIC)
                .flags(DECOMPOSITION_BY_CENTRIFUGING, BLAST_FURNACE_CALCITE_TRIPLE)
                .components(Iron, 1, Hydrogen, 1, Oxygen, 2)
                .buildAndRegister();

        Calcite = new Material.Builder(GTPM.id("calcite"))
                .localization(text -> text.source("Calcite")
                        .translation(MinecraftLocale.ZH_CN, "方解石"))
                .dust(1).ore()
                .color(0xfffef8).secondaryColor(0xbbaf62)
                .components(Calcium, 1, Carbon, 1, Oxygen, 3)
                .buildAndRegister();

        Cassiterite = new Material.Builder(GTPM.id("cassiterite"))
                .localization(text -> text.source("Cassiterite")
                        .translation(MinecraftLocale.ZH_CN, "锡石"))
                .dust(1).ore(2, 1)
                .color(0x89847e).secondaryColor(0x3b3b35).iconSet(ROUGH)
                .components(Tin, 1, Oxygen, 2)
                .buildAndRegister();

        CassiteriteSand = new Material.Builder(GTPM.id("cassiterite_sand"))
                .localization(text -> text.source("Cassiterite Sand")
                        .translation(MinecraftLocale.ZH_CN, "锡石矿砂"))
                .dust(1).ore(2, 1)
                .color(0x89847e).secondaryColor(0x3b3b35).iconSet(SAND)
                .components(Tin, 1, Oxygen, 2)
                .buildAndRegister();

        Chalcopyrite = new Material.Builder(GTPM.id("chalcopyrite"))
                .localization(text -> text.source("Chalcopyrite")
                        .translation(MinecraftLocale.ZH_CN, "黄铜矿"))
                .dust(1).ore()
                .color(0x96c185).secondaryColor(0xe3af1a)
                .components(Copper, 1, Iron, 1, Sulfur, 2)
                .buildAndRegister();

        Charcoal = new Material.Builder(GTPM.id("charcoal"))
                .localization(text -> text.source("Charcoal")
                        .translation(MinecraftLocale.ZH_CN, "木炭"))
                .gem(1, 1600) // default charcoal burn time in vanilla
                .color(0x7d6f58).secondaryColor(0x13110d).iconSet(FINE)
                .flags(FLAMMABLE, NO_SMELTING, NO_SMASHING, MORTAR_GRINDABLE)
                .components(Carbon, 1)
                .buildAndRegister();

        Chromite = new Material.Builder(GTPM.id("chromite"))
                .localization(text -> text.source("Chromite")
                        .translation(MinecraftLocale.ZH_CN, "铬铁矿"))
                .dust(1).ore()
                .color(0xc5c1a8).secondaryColor(0x4c1a69).iconSet(METALLIC)
                .components(Iron, 1, Chromium, 2, Oxygen, 4)
                .buildAndRegister();

        Cinnabar = new Material.Builder(GTPM.id("cinnabar"))
                .localization(text -> text.source("Cinnabar")
                        .translation(MinecraftLocale.ZH_CN, "朱砂"))
                .gem(1).ore()
                .color(0xff335f).secondaryColor(0x3f0110).iconSet(EMERALD)
                .flags(CRYSTALLIZABLE, DECOMPOSITION_BY_CENTRIFUGING)
                .components(Mercury, 1, Sulfur, 1)
                .buildAndRegister();

        Water = new Material.Builder(GTPM.id("water"))
                .localization(text -> text.source("Water")
                        .translation(MinecraftLocale.ZH_CN, "水"))
                .liquid(new FluidBuilder().temperature(300))
                .color(0x0000FF)
                .flags(DISABLE_DECOMPOSITION)
                .components(Hydrogen, 2, Oxygen, 1)
                .buildAndRegister();

        Coal = new Material.Builder(GTPM.id("coal"))
                .localization(text -> text.source("Coal")
                        .translation(MinecraftLocale.ZH_CN, "煤炭"))
                .gem(1, 1600).ore(2, 1) // default coal burn time in vanilla
                .color(0x393e41).secondaryColor(0x101015).iconSet(LIGNITE)
                .flags(FLAMMABLE, NO_SMELTING, NO_SMASHING, MORTAR_GRINDABLE, EXCLUDE_BLOCK_CRAFTING_BY_HAND_RECIPES,
                        DISABLE_DECOMPOSITION)
                .components(Carbon, 1)
                .buildAndRegister();

        Cobaltite = new Material.Builder(GTPM.id("cobaltite"))
                .localization(text -> text.source("Cobaltite")
                        .translation(MinecraftLocale.ZH_CN, "辉钴矿"))
                .dust(1).ore()
                .color(0x1975ff).secondaryColor(0x56071f).iconSet(METALLIC)
                .components(Cobalt, 1, Arsenic, 1, Sulfur, 1)
                .buildAndRegister();

        Cooperite = new Material.Builder(GTPM.id("cooperite"))
                .localization(text -> text.source("Sheldonite")
                        .translation(MinecraftLocale.ZH_CN, "谢尔顿矿"))
                .dust(1).ore()
                .color(0xe9ffa7).secondaryColor(0x665f2f).iconSet(METALLIC)
                .components(Platinum, 3, Nickel, 1, Sulfur, 1, Palladium, 1)
                .buildAndRegister();

        Cupronickel = new Material.Builder(GTPM.id("cupronickel"))
                .localization(text -> text.source("Cupronickel")
                        .translation(MinecraftLocale.ZH_CN, "白铜"))
                .ingot(1)
                .liquid(new FluidBuilder().temperature(1542))
                .color(0xffda8a).secondaryColor(0xcd2b00).iconSet(METALLIC)
                .appendFlags(EXT_METAL, GENERATE_SPRING, GENERATE_FINE_WIRE)
                .components(Copper, 1, Nickel, 1)
                .itemPipeProperties(2048, 1)
                .cableProperties(V[MV], 1, 1)
                .buildAndRegister();

        DarkAsh = new Material.Builder(GTPM.id("dark_ash"))
                .localization(text -> text.source("Dark Ash")
                        .translation(MinecraftLocale.ZH_CN, "黑色灰烬"))
                .dust(1)
                .color(0x8b8989).secondaryColor(0x555353)
                .flags(DISABLE_DECOMPOSITION)
                .components(Carbon, 1)
                .buildAndRegister();

        Diamond = new Material.Builder(GTPM.id("diamond"))
                .localization(text -> text.source("Diamond")
                        .translation(MinecraftLocale.ZH_CN, "钻石"))
                .gem(3).ore()
                .color(0xC8FFFF).iconSet(DIAMOND)
                .flags(GENERATE_BOLT_SCREW, GENERATE_LENS, GENERATE_GEAR, NO_SMASHING, NO_SMELTING,
                        HIGH_SIFTER_OUTPUT, DISABLE_DECOMPOSITION, EXCLUDE_BLOCK_CRAFTING_BY_HAND_RECIPES,
                        GENERATE_LONG_ROD)
                .components(Carbon, 1)
                .toolStats(ToolProperty.Builder.of(6.0F, 7.0F, 768, 3)
                        .attackSpeed(0.1F).enchantability(18).build())
                .buildAndRegister();

        Electrum = new Material.Builder(GTPM.id("electrum"))
                .localization(text -> text.source("Electrum")
                        .translation(MinecraftLocale.ZH_CN, "琥珀金"))
                .ingot()
                .liquid(new FluidBuilder().temperature(1285))
                .color(0xffff8b).secondaryColor(0xff8533).iconSet(SHINY)
                .appendFlags(EXT2_METAL, MORTAR_GRINDABLE, GENERATE_FINE_WIRE, GENERATE_RING)
                .components(Silver, 1, Gold, 1)
                .itemPipeProperties(1024, 2)
                .cableProperties(V[HV], 2, 2)
                .buildAndRegister();

        Emerald = new Material.Builder(GTPM.id("emerald"))
                .localization(text -> text.source("Emerald")
                        .translation(MinecraftLocale.ZH_CN, "绿宝石"))
                .gem().ore(2, 1)
                .color(0x17ff6c).secondaryColor(0x003f00).iconSet(EMERALD)
                .appendFlags(EXT_METAL, NO_SMASHING, NO_SMELTING, HIGH_SIFTER_OUTPUT,
                        EXCLUDE_BLOCK_CRAFTING_BY_HAND_RECIPES, GENERATE_LENS)
                .components(Beryllium, 3, Aluminium, 2, Silicon, 6, Oxygen, 18)
                .buildAndRegister();

        Galena = new Material.Builder(GTPM.id("galena"))
                .localization(text -> text.source("Galena")
                        .translation(MinecraftLocale.ZH_CN, "方铅矿"))
                .dust(3).ore()
                .color(0xf3e8fa).secondaryColor(0x331d42).iconSet(METALLIC)
                .flags(NO_SMELTING)
                .components(Lead, 1, Sulfur, 1)
                .buildAndRegister();

        Garnierite = new Material.Builder(GTPM.id("garnierite"))
                .localization(text -> text.source("Garnierite")
                        .translation(MinecraftLocale.ZH_CN, "硅镁镍矿"))
                .dust(3).ore()
                .color(0x32c880).secondaryColor(0x344028).iconSet(METALLIC)
                .components(Nickel, 1, Oxygen, 1)
                .buildAndRegister();

        GreenSapphire = new Material.Builder(GTPM.id("green_sapphire"))
                .localization(text -> text.source("Green Sapphire")
                        .translation(MinecraftLocale.ZH_CN, "绿色蓝宝石"))
                .gem().ore()
                .color(0x9ae6b0).secondaryColor(0x64C882).iconSet(GEM_HORIZONTAL)
                .appendFlags(EXT_METAL, NO_SMASHING, NO_SMELTING, HIGH_SIFTER_OUTPUT)
                .components(Aluminium, 2, Oxygen, 3)
                .buildAndRegister();

        Grossular = new Material.Builder(GTPM.id("grossular"))
                .localization(text -> text.source("Grossular")
                        .translation(MinecraftLocale.ZH_CN, "钙铝榴石"))
                .gem(1).ore(3, 1)
                .color(0xffb777).secondaryColor(0x856f48).iconSet(RUBY)
                .components(Calcium, 3, Aluminium, 2, Silicon, 3, Oxygen, 12)
                .buildAndRegister();

        Ice = new Material.Builder(GTPM.id("ice"))
                .localization(text -> text.source("Ice")
                        .translation(MinecraftLocale.ZH_CN, "冰"))
                .dust(0)
                .liquid(new FluidBuilder()
                        .temperature(273)
                        .customStill())
                .color(0xeef6ff, false).secondaryColor(0x6389c9).iconSet(SHINY)
                .flags(NO_SMASHING, EXCLUDE_BLOCK_CRAFTING_BY_HAND_RECIPES, DISABLE_DECOMPOSITION)
                .components(Hydrogen, 2, Oxygen, 1)
                .buildAndRegister();

        Ilmenite = new Material.Builder(GTPM.id("ilmenite"))
                .localization(text -> text.source("Ilmenite")
                        .translation(MinecraftLocale.ZH_CN, "钛铁矿"))
                .dust(3).ore()
                .color(0x2b2a24).secondaryColor(0x2b1700).iconSet(METALLIC)
                .flags(DISABLE_DECOMPOSITION)
                .components(Iron, 1, Titanium, 1, Oxygen, 3)
                .buildAndRegister();

        Rutile = new Material.Builder(GTPM.id("rutile"))
                .localization(text -> text.source("Rutile")
                        .translation(MinecraftLocale.ZH_CN, "金红石"))
                .gem()
                .color(0x892506).secondaryColor(0x330101).iconSet(GEM_HORIZONTAL)
                .flags(DISABLE_DECOMPOSITION)
                .components(Titanium, 1, Oxygen, 2)
                .buildAndRegister();

        Bauxite = new Material.Builder(GTPM.id("bauxite"))
                .localization(text -> text.source("Bauxite")
                        .translation(MinecraftLocale.ZH_CN, "铝土矿"))
                .dust(1).ore()
                .color(0xcfb853).secondaryColor(0xe6220c)
                .flags(DISABLE_DECOMPOSITION)
                .components(Aluminium, 2, Oxygen, 3)
                .buildAndRegister();

        Invar = new Material.Builder(GTPM.id("invar"))
                .localization(text -> text.source("Invar")
                        .translation(MinecraftLocale.ZH_CN, "殷钢"))
                .ingot()
                .liquid(new FluidBuilder().temperature(1916))
                .color(0xe2e8e1).secondaryColor(0x495d57).iconSet(METALLIC)
                .appendFlags(EXT2_METAL, MORTAR_GRINDABLE, GENERATE_FRAME, GENERATE_GEAR)
                .components(Iron, 2, Nickel, 1)
                .toolStats(ToolProperty.Builder.of(4.0F, 3.0F, 384, 2)
                        .addTypes(GTToolType.MORTAR)
                        .enchantability(18)
                        .enchantment(Enchantments.BANE_OF_ARTHROPODS, 3)
                        .enchantment(Enchantments.EFFICIENCY, 1).build())
                .rotorStats(130, 115, 3.0f, 512)
                .buildAndRegister();

        Kanthal = new Material.Builder(GTPM.id("kanthal"))
                .localization(text -> text.source("Kanthal")
                        .translation(MinecraftLocale.ZH_CN, "坎塔尔合金"))
                .ingot()
                .liquid(new FluidBuilder().temperature(1708))
                .color(0xC2D2DF).secondaryColor(0x4c4238).iconSet(METALLIC)
                .appendFlags(EXT_METAL, GENERATE_SPRING)
                .components(Iron, 1, Aluminium, 1, Chromium, 1)
                .cableProperties(V[HV], 4, 3)
                .blast(b -> b.temp(1800, GasTier.LOW)
                        .blastStats(VA[HV], 900))
                .buildAndRegister();

        Lazurite = new Material.Builder(GTPM.id("lazurite"))
                .localization(text -> text.source("Lazurite")
                        .translation(MinecraftLocale.ZH_CN, "蓝金石"))
                .gem(1).ore(6, 4)
                .color(0x2836f1).secondaryColor(0x183ca3).iconSet(LAPIS)
                .flags(GENERATE_PLATE, NO_SMASHING, NO_SMELTING, CRYSTALLIZABLE, GENERATE_ROD,
                        DECOMPOSITION_BY_ELECTROLYZING)
                .components(Aluminium, 6, Silicon, 6, Calcium, 8, Sodium, 8)
                .buildAndRegister();

        Magnalium = new Material.Builder(GTPM.id("magnalium"))
                .localization(text -> text.source("Magnalium")
                        .translation(MinecraftLocale.ZH_CN, "镁铝合金"))
                .ingot()
                .liquid(new FluidBuilder().temperature(929))
                .color(0x98b9e9).secondaryColor(0x2f0b51).iconSet(METALLIC)
                .appendFlags(EXT2_METAL)
                .components(Magnesium, 1, Aluminium, 2)
                .rotorStats(100, 105, 2.0f, 256)
                .itemPipeProperties(1024, 2)
                .buildAndRegister();

        Magnesite = new Material.Builder(GTPM.id("magnesite"))
                .localization(text -> text.source("Magnesite")
                        .translation(MinecraftLocale.ZH_CN, "菱镁矿"))
                .dust().ore()
                .color(0xfbfbf6).secondaryColor(0x80705e).iconSet(ROUGH)
                .components(Magnesium, 1, Carbon, 1, Oxygen, 3)
                .buildAndRegister();

        Magnetite = new Material.Builder(GTPM.id("magnetite"))
                .localization(text -> text.source("Magnetite")
                        .translation(MinecraftLocale.ZH_CN, "磁铁矿"))
                .dust().ore()
                .color(0x9d9d9d).secondaryColor(0x06070e).iconSet(METALLIC)
                .components(Iron, 3, Oxygen, 4)
                .buildAndRegister();

        Molybdenite = new Material.Builder(GTPM.id("molybdenite"))
                .localization(text -> text.source("Molybdenite")
                        .translation(MinecraftLocale.ZH_CN, "辉钼矿"))
                .dust().ore()
                .color(0xe3ddc3).secondaryColor(0x191919).iconSet(METALLIC)
                .components(Molybdenum, 1, Sulfur, 2)
                .buildAndRegister();

        Nichrome = new Material.Builder(GTPM.id("nichrome"))
                .localization(text -> text.source("Nichrome")
                        .translation(MinecraftLocale.ZH_CN, "镍铬合金"))
                .ingot()
                .liquid(new FluidBuilder().temperature(1818))
                .color(0xaf94b2).secondaryColor(0x5b4c6a).iconSet(METALLIC)
                .appendFlags(EXT_METAL, GENERATE_SPRING)
                .components(Nickel, 4, Chromium, 1)
                .cableProperties(V[EV], 4, 4)
                .blast(b -> b.temp(2700, GasTier.LOW)
                        .blastStats(VA[EV], 1300)
                        .vacuumStats(VA[HV]))
                .buildAndRegister();

        NiobiumNitride = new Material.Builder(GTPM.id("niobium_nitride"))
                .localization(text -> text.source("Niobium Nitride")
                        .translation(MinecraftLocale.ZH_CN, "氮化铌"))
                .ingot().fluid()
                .color(0x574457).secondaryColor(0x332e3c).iconSet(BRIGHT)
                .appendFlags(EXT_METAL, GENERATE_FOIL)
                .components(Niobium, 1, Nitrogen, 1)
                .cableProperties(V[LuV], 1, 1)
                .blast(2846, GasTier.MID)
                .buildAndRegister();

        NiobiumTitanium = new Material.Builder(GTPM.id("niobium_titanium"))
                .localization(text -> text.source("Niobium Titanium")
                        .translation(MinecraftLocale.ZH_CN, "铌钛合金"))
                .ingot()
                .liquid(new FluidBuilder().temperature(2345))
                .color(0xd2d9f9).secondaryColor(0x262528).iconSet(METALLIC)
                .appendFlags(EXT2_METAL, GENERATE_SPRING, GENERATE_SPRING_SMALL, GENERATE_FOIL, GENERATE_FINE_WIRE)
                .components(Niobium, 1, Titanium, 1)
                .fluidPipeProperties(5900, 175, true)
                .cableProperties(V[LuV], 4, 2)
                .blast(b -> b.temp(4500, GasTier.HIGH)
                        .blastStats(VA[HV], 1500)
                        .vacuumStats(VA[HV], 200))
                .buildAndRegister();

        Obsidian = new Material.Builder(GTPM.id("obsidian"))
                .localization(text -> text.source("Obsidian")
                        .translation(MinecraftLocale.ZH_CN, "黑曜石"))
                .dust(3)
                .color(0x3b2754).secondaryColor(0x000001).iconSet(SHINY)
                .flags(NO_SMASHING, EXCLUDE_BLOCK_CRAFTING_RECIPES, GENERATE_PLATE, GENERATE_DENSE)
                .components(Magnesium, 1, Iron, 1, Silicon, 2, Oxygen, 4)
                .buildAndRegister();

        Phosphate = new Material.Builder(GTPM.id("phosphate"))
                .localization(text -> text.source("Phosphate")
                        .translation(MinecraftLocale.ZH_CN, "磷酸盐"))
                .dust(1)
                .color(0xe8dabd).secondaryColor(0xa48b56)
                .flags(NO_SMASHING, NO_SMELTING, FLAMMABLE, EXPLOSIVE)
                .components(Phosphorus, 1, Oxygen, 4)
                .buildAndRegister();

        PlatinumRaw = new Material.Builder(GTPM.id("platinum_raw"))
                .localization(text -> text.source("Platinum Raw")
                        .translation(MinecraftLocale.ZH_CN, "粗铂"))
                .dust()
                .color(0xa09a7b).secondaryColor(0x4e4e45).iconSet(METALLIC)
                .flags(DISABLE_DECOMPOSITION)
                .components(Platinum, 1, Chlorine, 2)
                .buildAndRegister();

        SterlingSilver = new Material.Builder(GTPM.id("sterling_silver"))
                .localization(text -> text.source("Sterling Silver")
                        .translation(MinecraftLocale.ZH_CN, "标准纯银"))
                .ingot()
                .liquid(new FluidBuilder().temperature(1258))
                .color(0xfaf4dc).secondaryColor(0x484434).iconSet(SHINY)
                .appendFlags(EXT2_METAL, GENERATE_GEAR)
                .components(Copper, 1, Silver, 4)
                .toolStats(ToolProperty.Builder.of(3.0F, 8.0F, 768, 2)
                        .attackSpeed(0.3F).enchantability(33)
                        .enchantment(Enchantments.SMITE, 3).build())
                .rotorStats(100, 160, 2.0f, 196)
                .itemPipeProperties(1024, 2)
                .blast(b -> b.temp(1700, GasTier.LOW)
                        .blastStats(VA[MV], 1000))
                .buildAndRegister();

        RoseGold = new Material.Builder(GTPM.id("rose_gold"))
                .localization(text -> text.source("Rose Gold")
                        .translation(MinecraftLocale.ZH_CN, "玫瑰金"))
                .ingot()
                .liquid(new FluidBuilder().temperature(1341))
                .color(0xecd5b8).secondaryColor(0xd85f2d).iconSet(SHINY)
                .appendFlags(EXT2_METAL, GENERATE_RING, GENERATE_GEAR)
                .components(Copper, 1, Gold, 4)
                .toolStats(ToolProperty.Builder.of(12.0F, 2.0F, 768, 2)
                        .enchantability(33)
                        .enchantment(Enchantments.FORTUNE, 2).build())
                .rotorStats(100, 170, 2.0f, 152)
                .itemPipeProperties(1024, 2)
                .blast(b -> b.temp(1600, GasTier.LOW)
                        .blastStats(VA[MV], 1000))
                .buildAndRegister();

        BlackBronze = new Material.Builder(GTPM.id("black_bronze"))
                .localization(text -> text.source("Black Bronze")
                        .translation(MinecraftLocale.ZH_CN, "黑青铜"))
                .ingot()
                .liquid(new FluidBuilder().temperature(1328))
                .color(0x8b7c70).secondaryColor(0x4b3d32).iconSet(METALLIC)
                .appendFlags(EXT2_METAL, GENERATE_GEAR)
                .components(Gold, 1, Silver, 1, Copper, 3)
                .rotorStats(100, 155, 2.0f, 256)
                .itemPipeProperties(1024, 2)
                .blast(b -> b.temp(2000, GasTier.LOW)
                        .blastStats(VA[MV], 1000))
                .buildAndRegister();

        BismuthBronze = new Material.Builder(GTPM.id("bismuth_bronze"))
                .localization(text -> text.source("Bismuth Bronze")
                        .translation(MinecraftLocale.ZH_CN, "铋青铜"))
                .ingot()
                .liquid(new FluidBuilder().temperature(1036))
                .color(0xffd26f).secondaryColor(0x895f3d).iconSet(METALLIC)
                .appendFlags(EXT2_METAL)
                .components(Bismuth, 1, Zinc, 1, Copper, 3)
                .rotorStats(130, 120, 3.0f, 256)
                .blast(b -> b.temp(1100, GasTier.LOW)
                        .blastStats(VA[MV], 1000))
                .buildAndRegister();

        Biotite = new Material.Builder(GTPM.id("biotite"))
                .localization(text -> text.source("Biotite")
                        .translation(MinecraftLocale.ZH_CN, "黑云母"))
                .dust(1)
                .color(0x343b34).secondaryColor(0x121200).iconSet(METALLIC)
                .components(Potassium, 1, Magnesium, 3, Aluminium, 3, Fluorine, 2, Silicon, 3, Oxygen, 10)
                .buildAndRegister();

        Powellite = new Material.Builder(GTPM.id("powellite"))
                .localization(text -> text.source("Powellite")
                        .translation(MinecraftLocale.ZH_CN, "钼钙矿"))
                .dust().ore()
                .color(0xd8cfac).secondaryColor(0xbc7a2c)
                .components(Calcium, 1, Molybdenum, 1, Oxygen, 4)
                .buildAndRegister();

        Pyrite = new Material.Builder(GTPM.id("pyrite"))
                .localization(text -> text.source("Pyrite")
                        .translation(MinecraftLocale.ZH_CN, "黄铁矿"))
                .dust(1).ore()
                .color(0xfffee6).secondaryColor(0xb69f4e).iconSet(ROUGH)
                .flags(BLAST_FURNACE_CALCITE_DOUBLE)
                .components(Iron, 1, Sulfur, 2)
                .buildAndRegister();

        Pyrolusite = new Material.Builder(GTPM.id("pyrolusite"))
                .localization(text -> text.source("Pyrolusite")
                        .translation(MinecraftLocale.ZH_CN, "软锰矿"))
                .dust().ore()
                .color(0xc7b5ab).secondaryColor(0x595756)
                .components(Manganese, 1, Oxygen, 2)
                .buildAndRegister();

        Pyrope = new Material.Builder(GTPM.id("pyrope"))
                .localization(text -> text.source("Pyrope")
                        .translation(MinecraftLocale.ZH_CN, "镁铝榴石"))
                .gem().ore(3, 1)
                .color(0xe81958).secondaryColor(0x811e00).iconSet(RUBY)
                .components(Aluminium, 2, Magnesium, 3, Silicon, 3, Oxygen, 12)
                .buildAndRegister();

        RockSalt = new Material.Builder(GTPM.id("rock_salt"))
                .localization(text -> text.source("Rock Salt")
                        .translation(MinecraftLocale.ZH_CN, "岩盐"))
                .gem(1).ore(2, 1)
                .color(0xffeae1).secondaryColor(0xF0C8C8).iconSet(FINE)
                .flags(NO_SMASHING)
                .components(Potassium, 1, Chlorine, 1)
                .buildAndRegister();

        RTMAlloy = new Material.Builder(GTPM.id("rtm_alloy"))
                .localization(text -> text.source("RTM Alloy")
                        .translation(MinecraftLocale.ZH_CN, "钌钨钼合金"))
                .ingot().fluid()
                .color(0x30306B).iconSet(SHINY)
                .components(Ruthenium, 4, Tungsten, 2, Molybdenum, 1)
                .flags(GENERATE_SPRING)
                .cableProperties(V[EV], 6, 2)
                .blast(b -> b.temp(3000, GasTier.MID)
                        .blastStats(VA[EV], 1400)
                        .vacuumStats(VA[HV], 250))
                .buildAndRegister();

        Ruridit = new Material.Builder(GTPM.id("ruridit"))
                .localization(text -> text.source("Ruridit")
                        .translation(MinecraftLocale.ZH_CN, "钌铱合金"))
                .ingot(3)
                .fluid()
                .color(0x88b5b9).secondaryColor(0x4e885c).iconSet(BRIGHT)
                .flags(GENERATE_FINE_WIRE, GENERATE_GEAR, GENERATE_LONG_ROD, GENERATE_FRAME, GENERATE_BOLT_SCREW)
                .components(Ruthenium, 2, Iridium, 1)
                .blast(b -> b.temp(4500, GasTier.HIGH)
                        .blastStats(VA[EV], 1600)
                        .vacuumStats(VA[HV], 300))
                .buildAndRegister();

        Ruby = new Material.Builder(GTPM.id("ruby"))
                .localization(text -> text.source("Ruby")
                        .translation(MinecraftLocale.ZH_CN, "红宝石"))
                .gem().ore()
                .color(0xd72310).secondaryColor(0x960b6d).iconSet(RUBY)
                .appendFlags(EXT_METAL, NO_SMASHING, NO_SMELTING, HIGH_SIFTER_OUTPUT, GENERATE_LENS)
                .components(Chromium, 1, Aluminium, 2, Oxygen, 3)
                .buildAndRegister();

        Salt = new Material.Builder(GTPM.id("salt"))
                .localization(text -> text.source("Salt")
                        .translation(MinecraftLocale.ZH_CN, "盐"))
                .gem(1).ore(2, 1)
                .color(0xFAFAFA).iconSet(FINE)
                .flags(NO_SMASHING)
                .components(Sodium, 1, Chlorine, 1)
                .buildAndRegister();

        Saltpeter = new Material.Builder(GTPM.id("saltpeter"))
                .localization(text -> text.source("Saltpeter")
                        .translation(MinecraftLocale.ZH_CN, "硝石"))
                .dust(1).ore(2, 1)
                .color(0xE6E6E6).secondaryColor(0xe6e1cf).iconSet(FINE)
                .flags(NO_SMASHING, NO_SMELTING, FLAMMABLE)
                .components(Potassium, 1, Nitrogen, 1, Oxygen, 3)
                .buildAndRegister();

        Sapphire = new Material.Builder(GTPM.id("sapphire"))
                .localization(text -> text.source("Sapphire")
                        .translation(MinecraftLocale.ZH_CN, "蓝宝石"))
                .gem().ore()
                .color(0x3235e3).secondaryColor(0x211455).iconSet(EMERALD)
                .appendFlags(EXT_METAL, NO_SMASHING, NO_SMELTING, HIGH_SIFTER_OUTPUT, GENERATE_LENS)
                .components(Aluminium, 2, Oxygen, 3)
                .buildAndRegister();

        Scheelite = new Material.Builder(GTPM.id("scheelite"))
                .localization(text -> text.source("Scheelite")
                        .translation(MinecraftLocale.ZH_CN, "白钨矿"))
                .dust(3).ore()
                .color(0xd7e8b3).secondaryColor(0x143cae)
                .flags(DISABLE_DECOMPOSITION)
                .components(Calcium, 1, Tungsten, 1, Oxygen, 4)
                .buildAndRegister()
                .setFormula("Ca(WO3)O", true);

        Sodalite = new Material.Builder(GTPM.id("sodalite"))
                .localization(text -> text.source("Sodalite")
                        .translation(MinecraftLocale.ZH_CN, "方钠石"))
                .gem(1).ore(6, 4)
                .color(0x3d54ff).secondaryColor(0x210d78).iconSet(LAPIS)
                .flags(GENERATE_PLATE, GENERATE_ROD, NO_SMASHING, NO_SMELTING, CRYSTALLIZABLE,
                        DECOMPOSITION_BY_ELECTROLYZING)
                .components(Aluminium, 3, Silicon, 3, Sodium, 4, Chlorine, 1)
                .buildAndRegister();

        AluminiumSulfite = new Material.Builder(GTPM.id("aluminium_sulfite"))
                .localization(text -> text.source("Aluminium Sulfite")
                        .translation(MinecraftLocale.ZH_CN, "亚硫酸铝"))
                .dust()
                .color(0xd4ecf9).secondaryColor(0xa6b9b6)
                .components(Aluminium, 2, Sulfur, 3, Oxygen, 9)
                .buildAndRegister().setFormula("Al2(SO3)3", true);

        Tantalite = new Material.Builder(GTPM.id("tantalite"))
                .localization(text -> text.source("Tantalite")
                        .translation(MinecraftLocale.ZH_CN, "钽铁矿"))
                .dust(3).ore()
                .color(0x4e6b94).secondaryColor(0x632300).iconSet(METALLIC)
                .components(Manganese, 1, Tantalum, 2, Oxygen, 6)
                .buildAndRegister();

        Coke = new Material.Builder(GTPM.id("coke"))
                .localization(text -> text.source("Coke")
                        .translation(MinecraftLocale.ZH_CN, "焦煤"))
                .gem(2, 3200) // 2x burn time of coal
                .color(0x575e5b).secondaryColor(0x1f1f29).iconSet(LIGNITE)
                .flags(FLAMMABLE, NO_SMELTING, NO_SMASHING, MORTAR_GRINDABLE)
                .components(Carbon, 1)
                .buildAndRegister();

        SolderingAlloy = new Material.Builder(GTPM.id("soldering_alloy"))
                .localization(text -> text.source("Soldering Alloy")
                        .translation(MinecraftLocale.ZH_CN, "焊锡"))
                .ingot(1)
                .liquid(new FluidBuilder().temperature(544))
                .color(0x8c8ca7).secondaryColor(0x8675a7)
                .components(Tin, 6, Lead, 3, Antimony, 1)
                .buildAndRegister();

        Spessartine = new Material.Builder(GTPM.id("spessartine"))
                .localization(text -> text.source("Spessartine")
                        .translation(MinecraftLocale.ZH_CN, "锰铝榴石"))
                .gem().ore(3, 1)
                .color(0xffa81e).secondaryColor(0xb33700).iconSet(RUBY)
                .components(Aluminium, 2, Manganese, 3, Silicon, 3, Oxygen, 12)
                .buildAndRegister();

        Sphalerite = new Material.Builder(GTPM.id("sphalerite"))
                .localization(text -> text.source("Sphalerite")
                        .translation(MinecraftLocale.ZH_CN, "闪锌矿"))
                .dust(1).ore()
                .color(0xffdc88).secondaryColor(0x0f1605)
                .flags(DISABLE_DECOMPOSITION)
                .components(Zinc, 1, Sulfur, 1)
                .buildAndRegister();

        StainlessSteel = new Material.Builder(GTPM.id("stainless_steel"))
                .localization(text -> text.source("Stainless Steel")
                        .translation(MinecraftLocale.ZH_CN, "不锈钢"))
                .ingot(3)
                .liquid(new FluidBuilder().temperature(2011))
                .color(0xededfd).secondaryColor(0x19191d).iconSet(SHINY)
                .appendFlags(EXT2_METAL, GENERATE_ROTOR, GENERATE_SMALL_GEAR, GENERATE_FRAME, GENERATE_LONG_ROD,
                        GENERATE_FOIL, GENERATE_GEAR)
                .components(Iron, 6, Chromium, 1, Manganese, 1, Nickel, 1)
                .toolStats(ToolProperty.Builder.of(7.0F, 5.0F, 1024, 3)
                        .enchantability(14).build())
                .rotorStats(160, 115, 4.0f, 480)
                .fluidPipeProperties(2428, 75, true, true, true, false)
                .blast(b -> b.temp(1700, GasTier.LOW)
                        .blastStats(VA[HV], 1100))
                .buildAndRegister();

        Steel = new Material.Builder(GTPM.id("steel"))
                .localization(text -> text.source("Steel")
                        .translation(MinecraftLocale.ZH_CN, "钢"))
                .ingot(3)
                .liquid(new FluidBuilder().temperature(2046))
                .color(0xa7a7a7).secondaryColor(0x121c37).iconSet(METALLIC)
                .appendFlags(EXT2_METAL, MORTAR_GRINDABLE, GENERATE_ROTOR, GENERATE_SMALL_GEAR, GENERATE_SPRING,
                        GENERATE_SPRING_SMALL, GENERATE_FRAME, DISABLE_DECOMPOSITION, GENERATE_FINE_WIRE, GENERATE_GEAR,
                        GENERATE_DENSE)
                .components(Iron, 1)
                .toolStats(ToolProperty.Builder.of(5.0F, 3.0F, 512, 3)
                        .addTypes(GTToolType.MORTAR)
                        .enchantability(14).build())
                .armorStats(ArmorProperty.Builder.of(19, new int[] { 3, 7, 5, 2 })
                        .enchantability(14).build())
                .rotorStats(130, 105, 3.0f, 512)
                .fluidPipeProperties(1855, 50, true)
                .cableProperties(V[EV], 2, 2)
                .blast(b -> b.temp(1000)
                        .blastStats(VA[MV], 800)) // no gas tier for steel
                .buildAndRegister();

        Stibnite = new Material.Builder(GTPM.id("stibnite"))
                .localization(text -> text.source("Stibnite")
                        .translation(MinecraftLocale.ZH_CN, "辉锑矿"))
                .dust().ore()
                .color(0x656565).secondaryColor(0x0a1432).iconSet(METALLIC)
                .flags(DECOMPOSITION_BY_CENTRIFUGING)
                .components(Antimony, 2, Sulfur, 3)
                .buildAndRegister();

        Tetrahedrite = new Material.Builder(GTPM.id("tetrahedrite"))
                .localization(text -> text.source("Tetrahedrite")
                        .translation(MinecraftLocale.ZH_CN, "黝铜矿"))
                .dust().ore()
                .color(0xa3a09b).secondaryColor(0x143313)
                .components(Copper, 2, Iron, 1, Antimony, 1, Sulfur, 3)
                .buildAndRegister();

        TinAlloy = new Material.Builder(GTPM.id("tin_alloy"))
                .localization(text -> text.source("Tin Alloy")
                        .translation(MinecraftLocale.ZH_CN, "锡铁合金"))
                .ingot()
                .liquid(new FluidBuilder().temperature(1258))
                .color(0xC8C8C8).secondaryColor(0x8b8b8b).iconSet(METALLIC)
                .appendFlags(EXT2_METAL)
                .components(Tin, 1, Iron, 1)
                .fluidPipeProperties(1572, 20, true)
                .buildAndRegister();

        Topaz = new Material.Builder(GTPM.id("topaz"))
                .localization(text -> text.source("Topaz")
                        .translation(MinecraftLocale.ZH_CN, "黄玉"))
                .gem(3).ore()
                .color(0xe8d73a).secondaryColor(0xf4680f).iconSet(GEM_HORIZONTAL)
                .appendFlags(EXT_METAL, NO_SMASHING, NO_SMELTING, HIGH_SIFTER_OUTPUT)
                .components(Aluminium, 2, Silicon, 1, Oxygen, 5, Fluorine, 1, Hydrogen, 1)
                .buildAndRegister();

        Tungstate = new Material.Builder(GTPM.id("tungstate"))
                .localization(text -> text.source("Tungstate")
                        .translation(MinecraftLocale.ZH_CN, "钨酸锂"))
                .dust(3).ore()
                .color(0xe0ffc4).secondaryColor(0xab4400)
                .flags(DISABLE_DECOMPOSITION)
                .components(Tungsten, 1, Lithium, 2, Oxygen, 4)
                .buildAndRegister()
                .setFormula("Li2(WO3)O", true);

        Ultimet = new Material.Builder(GTPM.id("ultimet"))
                .localization(text -> text.source("Ultimet")
                        .translation(MinecraftLocale.ZH_CN, "哈氏合金"))
                .ingot(4)
                .liquid(new FluidBuilder().temperature(1980))
                .color(0x9f9fb1).secondaryColor(0x385086).iconSet(SHINY)
                .appendFlags(EXT2_METAL, GENERATE_GEAR, GENERATE_FRAME)
                .components(Cobalt, 5, Chromium, 2, Nickel, 1, Molybdenum, 1)
                .toolStats(ToolProperty.Builder.of(10.0F, 7.0F, 2048, 4)
                        .attackSpeed(0.1F).enchantability(21).build())
                .rotorStats(160, 130, 4.0f, 2048)
                .itemPipeProperties(128, 16)
                .blast(b -> b.temp(2700, GasTier.MID)
                        .blastStats(VA[HV], 1300))
                .buildAndRegister();

        Uraninite = new Material.Builder(GTPM.id("uraninite"))
                .localization(text -> text.source("Uraninite")
                        .translation(MinecraftLocale.ZH_CN, "晶质铀矿"))
                .dust(3).ore(true)
                .color(0xffd52e).secondaryColor(0x17212b).iconSet(METALLIC)
                .flags(DISABLE_DECOMPOSITION)
                .components(Uranium238, 1, Oxygen, 2)
                .buildAndRegister()
                .setFormula("UO2", true);

        Uvarovite = new Material.Builder(GTPM.id("uvarovite"))
                .localization(text -> text.source("Uvarovite")
                        .translation(MinecraftLocale.ZH_CN, "钙铬榴石"))
                .gem()
                .color(0x2ded4c).secondaryColor(0x00697c).iconSet(RUBY)
                .components(Calcium, 3, Chromium, 2, Silicon, 3, Oxygen, 12)
                .buildAndRegister();

        VanadiumGallium = new Material.Builder(GTPM.id("vanadium_gallium"))
                .localization(text -> text.source("Vanadium Gallium")
                        .translation(MinecraftLocale.ZH_CN, "钒镓合金"))
                .ingot()
                .liquid(new FluidBuilder().temperature(1712))
                .color(0x89aeec).secondaryColor(0x00379d).iconSet(SHINY)
                .appendFlags(STD_METAL, GENERATE_FOIL, GENERATE_SPRING, GENERATE_SPRING_SMALL)
                .components(Vanadium, 3, Gallium, 1)
                .cableProperties(V[ZPM], 4, 2)
                .blast(b -> b.temp(4500, GasTier.HIGH)
                        .blastStats(VA[EV], 1200)
                        .vacuumStats(VA[HV]))
                .buildAndRegister();

        WroughtIron = new Material.Builder(GTPM.id("wrought_iron"))
                .localization(text -> text.source("Wrought Iron")
                        .translation(MinecraftLocale.ZH_CN, "锻铁"))
                .ingot()
                .liquid(new FluidBuilder().temperature(2011))
                .color(0xbcbcbc).secondaryColor(0x521c0b).iconSet(METALLIC)
                .appendFlags(EXT_METAL, GENERATE_GEAR, GENERATE_FOIL, MORTAR_GRINDABLE, GENERATE_RING,
                        GENERATE_LONG_ROD, GENERATE_BOLT_SCREW, DISABLE_DECOMPOSITION, BLAST_FURNACE_CALCITE_TRIPLE)
                .components(Iron, 1)
                .toolStats(ToolProperty.Builder.of(2.0F, 2.0F, 384, 2)
                        .addTypes(GTToolType.MORTAR)
                        .attackSpeed(-0.2F).enchantability(5).build())
                .rotorStats(145, 105, 3.5f, 384)
                .buildAndRegister();
        Iron.getProperty(PropertyKey.INGOT).setSmeltingInto(WroughtIron);
        Iron.getProperty(PropertyKey.INGOT).setArcSmeltingInto(WroughtIron);

        Wulfenite = new Material.Builder(GTPM.id("wulfenite"))
                .localization(text -> text.source("Wulfenite")
                        .translation(MinecraftLocale.ZH_CN, "钼铅矿"))
                .dust(3).ore()
                .color(0xff9000).secondaryColor(0xFF0000)
                .components(Lead, 1, Molybdenum, 1, Oxygen, 4)
                .buildAndRegister();

        YellowLimonite = new Material.Builder(GTPM.id("yellow_limonite"))
                .localization(text -> text.source("Yellow Limonite")
                        .translation(MinecraftLocale.ZH_CN, "黄褐铁矿"))
                .dust().ore()
                .color(0xf5e315).secondaryColor(0xc06f33).iconSet(METALLIC)
                .flags(DECOMPOSITION_BY_CENTRIFUGING, BLAST_FURNACE_CALCITE_DOUBLE)
                .components(Iron, 1, Hydrogen, 1, Oxygen, 2)
                .buildAndRegister();

        YttriumBariumCuprate = new Material.Builder(GTPM.id("yttrium_barium_cuprate"))
                .localization(text -> text.source("Yttrium Barium Cuprate")
                        .translation(MinecraftLocale.ZH_CN, "钇钡铜氧化物"))
                .ingot()
                .liquid(new FluidBuilder().temperature(1799))
                .color(0x796d72).secondaryColor(0x260a3a).iconSet(METALLIC)
                .appendFlags(EXT_METAL, GENERATE_FINE_WIRE, GENERATE_SPRING, GENERATE_SPRING_SMALL, GENERATE_FOIL,
                        GENERATE_BOLT_SCREW)
                .components(Yttrium, 1, Barium, 2, Copper, 3, Oxygen, 7)
                .cableProperties(V[UV], 4, 4)
                .blast(b -> b.temp(4500, GasTier.HIGH)
                        .blastStats(VA[IV], 1000)
                        .vacuumStats(VA[EV], 150))
                .buildAndRegister();

        NetherQuartz = new Material.Builder(GTPM.id("nether_quartz"))
                .localization(text -> text.source("Nether Quartz")
                        .translation(MinecraftLocale.ZH_CN, "下界石英"))
                .gem(1).ore(2, 1)
                .color(0xf8efe3).secondaryColor(0xe6c1bb).iconSet(QUARTZ)
                .flags(GENERATE_PLATE, NO_SMELTING, CRYSTALLIZABLE, EXCLUDE_BLOCK_CRAFTING_BY_HAND_RECIPES,
                        DISABLE_DECOMPOSITION)
                .components(Silicon, 1, Oxygen, 2)
                .buildAndRegister();

        CertusQuartz = new Material.Builder(GTPM.id("certus_quartz"))
                .localization(text -> text.source("Certus Quartz")
                        .translation(MinecraftLocale.ZH_CN, "赛特斯石英"))
                .gem(1).ore(2, 1)
                .color(0xc2d6ff).secondaryColor(0x86bacf).iconSet(CERTUS)
                .flags(GENERATE_PLATE, NO_SMELTING, CRYSTALLIZABLE, DISABLE_DECOMPOSITION)
                .components(Silicon, 1, Oxygen, 2)
                .buildAndRegister();

        Quartzite = new Material.Builder(GTPM.id("quartzite"))
                .localization(text -> text.source("Quartzite")
                        .translation(MinecraftLocale.ZH_CN, "石英岩"))
                .gem(1).ore(2, 1)
                .color(0xf2f5ed).secondaryColor(0xb8e2b8).iconSet(QUARTZ)
                .flags(NO_SMELTING, CRYSTALLIZABLE, DISABLE_DECOMPOSITION, GENERATE_PLATE)
                .components(Silicon, 1, Oxygen, 2)
                .buildAndRegister();

        Graphite = new Material.Builder(GTPM.id("graphite"))
                .localization(text -> text.source("Graphite")
                        .translation(MinecraftLocale.ZH_CN, "石墨"))
                .ore()
                .color(0xa8a89e).secondaryColor(0x172602)
                .flags(NO_SMELTING, FLAMMABLE, DISABLE_DECOMPOSITION)
                .components(Carbon, 1)
                .buildAndRegister();

        Graphene = new Material.Builder(GTPM.id("graphene"))
                .localization(text -> text.source("Graphene")
                        .translation(MinecraftLocale.ZH_CN, "石墨烯"))
                .dust().ingot()
                .color(0x808080).secondaryColor(0x3d3838).iconSet(SHINY)
                .flags(DISABLE_DECOMPOSITION, GENERATE_FOIL)
                .components(Carbon, 1)
                .cableProperties(V[IV], 1, 1)
                .buildAndRegister();

        TungsticAcid = new Material.Builder(GTPM.id("tungstic_acid"))
                .localization(text -> text.source("Tungstic Acid")
                        .translation(MinecraftLocale.ZH_CN, "钨酸"))
                .dust()
                .color(0xfffc03).secondaryColor(0x886217).iconSet(SHINY)
                .flags(DISABLE_DECOMPOSITION)
                .components(Hydrogen, 2, Tungsten, 1, Oxygen, 4)
                .buildAndRegister();

        Osmiridium = new Material.Builder(GTPM.id("osmiridium"))
                .localization(text -> text.source("Osmiridium")
                        .translation(MinecraftLocale.ZH_CN, "铱锇合金"))
                .ingot(3)
                .liquid(new FluidBuilder().temperature(3012))
                .color(0x47adb6).secondaryColor(0x241a44).iconSet(METALLIC)
                .appendFlags(EXT2_METAL, GENERATE_SMALL_GEAR, GENERATE_RING, GENERATE_ROTOR, GENERATE_ROUND,
                        GENERATE_FINE_WIRE, GENERATE_GEAR)
                .components(Iridium, 3, Osmium, 1)
                .rotorStats(130, 130, 3.0f, 3152)
                .itemPipeProperties(64, 32)
                .blast(b -> b.temp(4500, GasTier.HIGH)
                        .blastStats(VA[LuV], 900)
                        .vacuumStats(VA[EV], 200))
                .buildAndRegister();

        LithiumChloride = new Material.Builder(GTPM.id("lithium_chloride"))
                .localization(text -> text.source("Lithium Chloride")
                        .translation(MinecraftLocale.ZH_CN, "氯化锂"))
                .dust()
                .color(0xDEDEFA).iconSet(FINE)
                .components(Lithium, 1, Chlorine, 1)
                .buildAndRegister();

        CalciumChloride = new Material.Builder(GTPM.id("calcium_chloride"))
                .localization(text -> text.source("Calcium Chloride")
                        .translation(MinecraftLocale.ZH_CN, "氯化钙"))
                .dust()
                .color(0xFFFFFF).secondaryColor(0xe7e7d7).iconSet(FINE)
                .components(Calcium, 1, Chlorine, 2)
                .buildAndRegister();

        Bornite = new Material.Builder(GTPM.id("bornite"))
                .localization(text -> text.source("Bornite")
                        .translation(MinecraftLocale.ZH_CN, "斑铜矿"))
                .dust(1).ore()
                .color(0xffe05a).secondaryColor(0x442602).iconSet(ROUGH)
                .components(Copper, 5, Iron, 1, Sulfur, 4)
                .buildAndRegister();

        Chalcocite = new Material.Builder(GTPM.id("chalcocite"))
                .localization(text -> text.source("Chalcocite")
                        .translation(MinecraftLocale.ZH_CN, "辉铜矿"))
                .dust().ore()
                .color(0x657882).secondaryColor(0x33302e).iconSet(EMERALD)
                .components(Copper, 2, Sulfur, 1)
                .buildAndRegister();

        GalliumArsenide = new Material.Builder(GTPM.id("gallium_arsenide"))
                .localization(text -> text.source("Gallium Arsenide")
                        .translation(MinecraftLocale.ZH_CN, "砷化镓"))
                .ingot(1)
                .liquid(new FluidBuilder().temperature(1511))
                .color(0x938fff).secondaryColor(0x8c548c)
                .appendFlags(STD_METAL, DECOMPOSITION_BY_CENTRIFUGING)
                .components(Arsenic, 1, Gallium, 1)
                .blast(b -> b.temp(1200, GasTier.LOW)
                        .blastStats(VA[MV], 1200))
                .buildAndRegister();

        Potash = new Material.Builder(GTPM.id("potash"))
                .localization(text -> text.source("Potash")
                        .translation(MinecraftLocale.ZH_CN, "钾碱"))
                .dust(1)
                .color(0xffa772).secondaryColor(0x922f1b).iconSet(FINE)
                .components(Potassium, 2, Oxygen, 1)
                .buildAndRegister();

        SodaAsh = new Material.Builder(GTPM.id("soda_ash"))
                .localization(text -> text.source("Soda Ash")
                        .translation(MinecraftLocale.ZH_CN, "纯碱"))
                .dust(1)
                .color(0xffffff).secondaryColor(0xDCDCFF)
                .components(Sodium, 2, Carbon, 1, Oxygen, 3)
                .buildAndRegister();

        IndiumGalliumPhosphide = new Material.Builder(GTPM.id("indium_gallium_phosphide"))
                .localization(text -> text.source("Indium Gallium Phosphide")
                        .translation(MinecraftLocale.ZH_CN, "磷化铟镓"))
                .ingot(1)
                .liquid(new FluidBuilder().temperature(350))
                .color(0xa77bd7).secondaryColor(0x4e546b)
                .appendFlags(STD_METAL, DECOMPOSITION_BY_CENTRIFUGING)
                .components(Indium, 1, Gallium, 1, Phosphorus, 1)
                .buildAndRegister();

        NickelZincFerrite = new Material.Builder(GTPM.id("nickel_zinc_ferrite"))
                .localization(text -> text.source("Nickel Zinc Ferrite")
                        .translation(MinecraftLocale.ZH_CN, "镍锌铁氧体"))
                .ingot(0)
                .liquid(new FluidBuilder().temperature(1410))
                .color(0x3f2821).secondaryColor(0x2c2725)
                .flags(GENERATE_RING)
                .components(Nickel, 1, Zinc, 1, Iron, 4, Oxygen, 8)
                .buildAndRegister();

        SiliconDioxide = new Material.Builder(GTPM.id("silicon_dioxide"))
                .localization(text -> text.source("Silicon Dioxide")
                        .translation(MinecraftLocale.ZH_CN, "二氧化硅"))
                .dust(1)
                .color(0xf2f2f2).secondaryColor(0xb2c4c7).iconSet(QUARTZ)
                .flags(NO_SMASHING, NO_SMELTING)
                .components(Silicon, 1, Oxygen, 2)
                .hazard(HazardProperty.HazardTrigger.INHALATION, GTMedicalConditions.SILICOSIS, false)
                .buildAndRegister();

        MagnesiumChloride = new Material.Builder(GTPM.id("magnesium_chloride"))
                .localization(text -> text.source("Magnesium Chloride")
                        .translation(MinecraftLocale.ZH_CN, "氯化镁"))
                .dust(1)
                .color(0xeee4e9).secondaryColor(0xD40D5C)
                .flags(DISABLE_DECOMPOSITION)
                .components(Magnesium, 1, Chlorine, 2)
                .buildAndRegister();

        SodiumSulfide = new Material.Builder(GTPM.id("sodium_sulfide"))
                .localization(text -> text.source("Sodium Sulfide")
                        .translation(MinecraftLocale.ZH_CN, "硫化钠"))
                .dust(1)
                .color(0xffd83d).secondaryColor(0xc54a00)
                .components(Sodium, 2, Sulfur, 1)
                .buildAndRegister();

        PhosphorusPentoxide = new Material.Builder(GTPM.id("phosphorus_pentoxide"))
                .localization(text -> text.source("Phosphorus Pentoxide")
                        .translation(MinecraftLocale.ZH_CN, "五氧化二磷"))
                .dust(1)
                .color(0xe89188).secondaryColor(0x220202)
                .flags(DECOMPOSITION_BY_CENTRIFUGING)
                .components(Phosphorus, 4, Oxygen, 10)
                .buildAndRegister();

        Quicklime = new Material.Builder(GTPM.id("quicklime"))
                .localization(text -> text.source("Quicklime")
                        .translation(MinecraftLocale.ZH_CN, "生石灰"))
                .dust(1)
                .color(0xecfff3).secondaryColor(0x7d8e83)
                .components(Calcium, 1, Oxygen, 1)
                .hazard(HazardProperty.HazardTrigger.SKIN_CONTACT, GTMedicalConditions.CHEMICAL_BURNS)
                .buildAndRegister();

        SodiumBisulfate = new Material.Builder(GTPM.id("sodium_bisulfate"))
                .localization(text -> text.source("Sodium Bisulfate")
                        .translation(MinecraftLocale.ZH_CN, "硫酸氢钠"))
                .dust(1)
                .color(0xfeffed).secondaryColor(0xf1f0a3)
                .flags(DISABLE_DECOMPOSITION)
                .components(Sodium, 1, Hydrogen, 1, Sulfur, 1, Oxygen, 4)
                .buildAndRegister();

        FerriteMixture = new Material.Builder(GTPM.id("ferrite_mixture"))
                .localization(text -> text.source("Ferrite Mixture")
                        .translation(MinecraftLocale.ZH_CN, "铁氧体混合物"))
                .dust(1)
                .color(0xB4B4B4).secondaryColor(0x763200).iconSet(METALLIC)
                .flags(DECOMPOSITION_BY_CENTRIFUGING)
                .components(Nickel, 1, Zinc, 1, Iron, 4)
                .buildAndRegister();

        Magnesia = new Material.Builder(GTPM.id("magnesia"))
                .localization(text -> text.source("Magnesia")
                        .translation(MinecraftLocale.ZH_CN, "氧化镁"))
                .dust(1)
                .color(0x998282).secondaryColor(0x594d19)
                .components(Magnesium, 1, Oxygen, 1)
                .buildAndRegister();

        PlatinumGroupSludge = new Material.Builder(GTPM.id("platinum_group_sludge"))
                .localization(text -> text.source("Platinum Group Sludge")
                        .translation(MinecraftLocale.ZH_CN, "铂系矿泥"))
                .dust(1)
                .color(0x343228).secondaryColor(0x001E00).iconSet(FINE)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        Realgar = new Material.Builder(GTPM.id("realgar"))
                .localization(text -> text.source("Realgar")
                        .translation(MinecraftLocale.ZH_CN, "雄黄"))
                .gem().ore()
                .color(0xff3d33).secondaryColor(0x3f0110).iconSet(EMERALD)
                .flags(DECOMPOSITION_BY_CENTRIFUGING)
                .components(Arsenic, 4, Sulfur, 4)
                .buildAndRegister();

        SodiumBicarbonate = new Material.Builder(GTPM.id("sodium_bicarbonate"))
                .localization(text -> text.source("Sodium Bicarbonate")
                        .translation(MinecraftLocale.ZH_CN, "碳酸氢钠"))
                .dust(1)
                .color(0xFFFFFF).secondaryColor(0xa7d2df).iconSet(ROUGH)
                .flags(DISABLE_DECOMPOSITION)
                .components(Sodium, 1, Hydrogen, 1, Carbon, 1, Oxygen, 3)
                .buildAndRegister();

        PotassiumDichromate = new Material.Builder(GTPM.id("potassium_dichromate"))
                .localization(text -> text.source("Potassium Dichromate")
                        .translation(MinecraftLocale.ZH_CN, "重铬酸钾"))
                .dust(1)
                .color(0xff6000).secondaryColor(0xFF0000)
                .components(Potassium, 2, Chromium, 2, Oxygen, 7)
                .hazard(HazardProperty.HazardTrigger.INHALATION, GTMedicalConditions.POISON)
                .buildAndRegister();

        ChromiumTrioxide = new Material.Builder(GTPM.id("chromium_trioxide"))
                .localization(text -> text.source("Chromium Trioxide")
                        .translation(MinecraftLocale.ZH_CN, "三氧化铬"))
                .dust(1)
                .color(0xFFE4E1)
                .components(Chromium, 1, Oxygen, 3)
                .hazard(HazardProperty.HazardTrigger.SKIN_CONTACT, GTMedicalConditions.IRRITANT)
                .buildAndRegister();

        AntimonyTrioxide = new Material.Builder(GTPM.id("antimony_trioxide"))
                .localization(text -> text.source("Antimony Trioxide")
                        .translation(MinecraftLocale.ZH_CN, "三氧化二锑"))
                .dust(1)
                .color(0xf5f5ff).secondaryColor(0xc4c4d6)
                .components(Antimony, 2, Oxygen, 3)
                .buildAndRegister();

        Zincite = new Material.Builder(GTPM.id("zincite"))
                .localization(text -> text.source("Zincite")
                        .translation(MinecraftLocale.ZH_CN, "红锌矿"))
                .dust(1)
                .color(0xff9f49).secondaryColor(0xff0000)
                .components(Zinc, 1, Oxygen, 1)
                .buildAndRegister();

        CupricOxide = new Material.Builder(GTPM.id("cupric_oxide"))
                .localization(text -> text.source("Cupric Oxide")
                        .translation(MinecraftLocale.ZH_CN, "氧化铜"))
                .dust(1)
                .color(0x8df7cf).secondaryColor(0x57696e)
                .components(Copper, 1, Oxygen, 1)
                .buildAndRegister();

        CobaltOxide = new Material.Builder(GTPM.id("cobalt_oxide"))
                .localization(text -> text.source("Cobalt Oxide")
                        .translation(MinecraftLocale.ZH_CN, "氧化钴"))
                .dust(1)
                .color(0x3cb099).secondaryColor(0x3b5c66)
                .components(Cobalt, 1, Oxygen, 1)
                .buildAndRegister();

        ArsenicTrioxide = new Material.Builder(GTPM.id("arsenic_trioxide"))
                .localization(text -> text.source("Arsenic Trioxide")
                        .translation(MinecraftLocale.ZH_CN, "三氧化二砷"))
                .dust(1)
                .color(0xf9f3f3).secondaryColor(0x3b5c66).iconSet(ROUGH)
                .components(Arsenic, 2, Oxygen, 3)
                .buildAndRegister();

        Massicot = new Material.Builder(GTPM.id("massicot"))
                .localization(text -> text.source("Massicot")
                        .translation(MinecraftLocale.ZH_CN, "铅黄"))
                .dust(1)
                .color(0xFFDD55).secondaryColor(0x000000)
                .components(Lead, 1, Oxygen, 1)
                .buildAndRegister();

        Ferrosilite = new Material.Builder(GTPM.id("ferrosilite"))
                .localization(text -> text.source("Ferrosilite")
                        .translation(MinecraftLocale.ZH_CN, "铁辉石"))
                .dust(1)
                .color(0x968c80).secondaryColor(0x97732a)
                .components(Iron, 1, Silicon, 1, Oxygen, 3)
                .buildAndRegister();

        MetalMixture = new Material.Builder(GTPM.id("metal_mixture"))
                .localization(text -> text.source("Metal Mixture")
                        .translation(MinecraftLocale.ZH_CN, "金属混合物"))
                .dust(1)
                .color(0x697077).secondaryColor(0x502d16).iconSet(METALLIC)
                .flags(DISABLE_DECOMPOSITION)
                .buildAndRegister();

        SodiumHydroxide = new Material.Builder(GTPM.id("sodium_hydroxide"))
                .localization(text -> text.source("Sodium Hydroxide")
                        .translation(MinecraftLocale.ZH_CN, "氢氧化钠"))
                .dust(1)
                .color(0xf5feff).secondaryColor(0xa4ebf1)
                .flags(DISABLE_DECOMPOSITION)
                .components(Sodium, 1, Oxygen, 1, Hydrogen, 1)
                .hazard(HazardProperty.HazardTrigger.SKIN_CONTACT, GTMedicalConditions.CHEMICAL_BURNS)
                .buildAndRegister();

        SodiumPersulfate = new Material.Builder(GTPM.id("sodium_persulfate"))
                .localization(text -> text.source("Sodium Persulfate")
                        .translation(MinecraftLocale.ZH_CN, "过硫酸钠"))
                .liquid(new FluidBuilder().customStill())
                .components(Sodium, 2, Sulfur, 2, Oxygen, 8)
                .buildAndRegister();

        Bastnasite = new Material.Builder(GTPM.id("bastnasite"))
                .localization(text -> text.source("Bastnasite")
                        .translation(MinecraftLocale.ZH_CN, "氟碳镧铈矿"))
                .dust().ore(2, 1)
                .color(0xcaab60).secondaryColor(0xc8502d).iconSet(FINE)
                .components(Cerium, 1, Carbon, 1, Fluorine, 1, Oxygen, 3)
                .buildAndRegister();

        Pentlandite = new Material.Builder(GTPM.id("pentlandite"))
                .localization(text -> text.source("Pentlandite")
                        .translation(MinecraftLocale.ZH_CN, "镍黄铁矿"))
                .dust().ore()
                .color(0xe3cf13).secondaryColor(0x29315b)
                .components(Nickel, 9, Sulfur, 8)
                .buildAndRegister();

        Spodumene = new Material.Builder(GTPM.id("spodumene"))
                .localization(text -> text.source("Spodumene")
                        .translation(MinecraftLocale.ZH_CN, "锂辉石"))
                .dust().ore()
                .color(0xffbcbc).secondaryColor(0xc490ff)
                .components(Lithium, 1, Aluminium, 1, Silicon, 2, Oxygen, 6)
                .buildAndRegister();

        Lepidolite = new Material.Builder(GTPM.id("lepidolite"))
                .localization(text -> text.source("Lepidolite")
                        .translation(MinecraftLocale.ZH_CN, "锂云母"))
                .dust().ore(2, 1)
                .color(0xffdae4).secondaryColor(0x75376f).iconSet(FINE)
                .components(Potassium, 1, Lithium, 3, Aluminium, 4, Fluorine, 2, Oxygen, 10)
                .buildAndRegister();

        GlauconiteSand = new Material.Builder(GTPM.id("glauconite_sand"))
                .localization(text -> text.source("Glauconite Sand")
                        .translation(MinecraftLocale.ZH_CN, "海绿石砂"))
                .dust().ore(3, 1)
                .color(0x1da351).secondaryColor(0x1a6e8f).iconSet(SAND)
                .components(Potassium, 1, Magnesium, 2, Aluminium, 2, Silicon, 3, Oxygen, 12, Hydrogen, 2, Water, 1)
                .buildAndRegister();

        Malachite = new Material.Builder(GTPM.id("malachite"))
                .localization(text -> text.source("Malachite")
                        .translation(MinecraftLocale.ZH_CN, "孔雀石"))
                .gem().ore()
                .color(0x00f1b0).secondaryColor(0x107a47).iconSet(LAPIS)
                .components(Copper, 2, Carbon, 1, Hydrogen, 2, Oxygen, 5)
                .buildAndRegister();

        Mica = new Material.Builder(GTPM.id("mica"))
                .localization(text -> text.source("Mica")
                        .translation(MinecraftLocale.ZH_CN, "云母"))
                .dust().ore(2, 1)
                .color(0xecfeff).secondaryColor(0xc2a03c).iconSet(FINE)
                .components(Potassium, 1, Aluminium, 3, Silicon, 3, Fluorine, 2, Oxygen, 10)
                .buildAndRegister();

        Barite = new Material.Builder(GTPM.id("barite"))
                .localization(text -> text.source("Barite")
                        .translation(MinecraftLocale.ZH_CN, "重晶石"))
                .dust().ore()
                .color(0xe8e2d1).secondaryColor(0xf4b74b)
                .components(Barium, 1, Sulfur, 1, Oxygen, 4)
                .buildAndRegister();

        Alunite = new Material.Builder(GTPM.id("alunite"))
                .localization(text -> text.source("Alunite")
                        .translation(MinecraftLocale.ZH_CN, "明矾石"))
                .dust().ore(3, 1)
                .color(0xfbd677).secondaryColor(0xe11e0a).iconSet(METALLIC)
                .components(Potassium, 1, Aluminium, 2, Silicon, 2, Hydrogen, 6, Oxygen, 14)
                .buildAndRegister();

        Talc = new Material.Builder(GTPM.id("talc"))
                .localization(text -> text.source("Talc")
                        .translation(MinecraftLocale.ZH_CN, "滑石"))
                .dust().ore(2, 1)
                .color(0xebffe9).secondaryColor(0x6fe19b).iconSet(FINE)
                .components(Magnesium, 3, Silicon, 4, Hydrogen, 2, Oxygen, 12)
                .buildAndRegister();

        Soapstone = new Material.Builder(GTPM.id("soapstone"))
                .localization(text -> text.source("Soapstone")
                        .translation(MinecraftLocale.ZH_CN, "皂石"))
                .dust(1).ore(3, 1)
                .color(0x5a7261).secondaryColor(0x464c4b).iconSet(ROUGH)
                .components(Magnesium, 3, Silicon, 4, Hydrogen, 2, Oxygen, 12)
                .buildAndRegister();

        Kyanite = new Material.Builder(GTPM.id("kyanite"))
                .localization(text -> text.source("Kyanite")
                        .translation(MinecraftLocale.ZH_CN, "蓝晶石"))
                .dust().ore()
                .color(0xd5ffff).secondaryColor(0x5a69d6).iconSet(FLINT)
                .components(Aluminium, 2, Silicon, 1, Oxygen, 5)
                .buildAndRegister();

        IronMagnetic = new Material.Builder(GTPM.id("magnetic_iron"))
                .localization(text -> text.source("Magnetic Iron")
                        .translation(MinecraftLocale.ZH_CN, "磁化铁"))
                .ingot()
                .color(0xeeeeee).secondaryColor(0x979797).iconSet(MAGNETIC)
                .flags(GENERATE_BOLT_SCREW, IS_MAGNETIC)
                .components(Iron, 1)
                .ingotSmeltInto(Iron)
                .arcSmeltInto(WroughtIron)
                .macerateInto(Iron)
                .buildAndRegister();
        Iron.getProperty(PropertyKey.INGOT).setMagneticMaterial(IronMagnetic);

        TungstenCarbide = new Material.Builder(GTPM.id("tungsten_carbide"))
                .localization(text -> text.source("Tungsten Carbide")
                        .translation(MinecraftLocale.ZH_CN, "碳化钨"))
                .ingot(4).fluid()
                .color(0x635480).secondaryColor(0x392e44).iconSet(METALLIC)
                .appendFlags(EXT2_METAL, GENERATE_FOIL, GENERATE_GEAR, GENERATE_SMALL_GEAR, GENERATE_FRAME,
                        DECOMPOSITION_BY_CENTRIFUGING)
                .components(Tungsten, 1, Carbon, 1)
                .toolStats(ToolProperty.Builder.of(60.0F, 2.0F, 1024, 4)
                        .enchantability(21).build())
                .rotorStats(160, 155, 4.0f, 1280)
                .fluidPipeProperties(3837, 200, true)
                .blast(b -> b.temp(3058, GasTier.MID)
                        .blastStats(VA[EV], 1500)
                        .vacuumStats(VA[HV]))
                .buildAndRegister();

        CarbonDioxide = new Material.Builder(GTPM.id("carbon_dioxide"))
                .localization(text -> text.source("Carbon Dioxide")
                        .translation(MinecraftLocale.ZH_CN, "二氧化碳"))
                .gas()
                .color(0xA9D0F5)
                .components(Carbon, 1, Oxygen, 2)
                .buildAndRegister();

        TitaniumTetrachloride = new Material.Builder(GTPM.id("titanium_tetrachloride"))
                .localization(text -> text.source("Titanium Tetrachloride")
                        .translation(MinecraftLocale.ZH_CN, "四氯化钛"))
                .liquid(new FluidBuilder().customStill())
                .color(0xD40D5C)
                .flags(DISABLE_DECOMPOSITION)
                .components(Titanium, 1, Chlorine, 4)
                .buildAndRegister();

        NitrogenDioxide = new Material.Builder(GTPM.id("nitrogen_dioxide"))
                .localization(text -> text.source("Nitrogen Dioxide")
                        .translation(MinecraftLocale.ZH_CN, "二氧化氮"))
                .gas()
                .color(0x85FCFF)
                .components(Nitrogen, 1, Oxygen, 2)
                .hazard(HazardProperty.HazardTrigger.INHALATION, GTMedicalConditions.POISON, 10)
                .buildAndRegister();

        HydrogenSulfide = new Material.Builder(GTPM.id("hydrogen_sulfide"))
                .localization(text -> text.source("Hydrogen Sulfide")
                        .translation(MinecraftLocale.ZH_CN, "硫化氢"))
                .gas(new FluidBuilder().customStill())
                .components(Hydrogen, 2, Sulfur, 1)
                .hazard(HazardProperty.HazardTrigger.INHALATION, GTMedicalConditions.POISON, 5)
                .buildAndRegister();

        NitricAcid = new Material.Builder(GTPM.id("nitric_acid"))
                .localization(text -> text.source("Nitric Acid")
                        .translation(MinecraftLocale.ZH_CN, "硝酸"))
                .liquid(new FluidBuilder().attribute(FluidAttributes.ACID))
                .color(0xCCCC00)
                .flags(DISABLE_DECOMPOSITION)
                .components(Hydrogen, 1, Nitrogen, 1, Oxygen, 3)
                .buildAndRegister();

        SulfuricAcid = new Material.Builder(GTPM.id("sulfuric_acid"))
                .localization(text -> text.source("Sulfuric Acid")
                        .translation(MinecraftLocale.ZH_CN, "硫酸"))
                .liquid(new FluidBuilder().attribute(FluidAttributes.ACID).customStill())
                .flags(DISABLE_DECOMPOSITION)
                .components(Hydrogen, 2, Sulfur, 1, Oxygen, 4)
                .buildAndRegister();

        PhosphoricAcid = new Material.Builder(GTPM.id("phosphoric_acid"))
                .localization(text -> text.source("Phosphoric Acid")
                        .translation(MinecraftLocale.ZH_CN, "磷酸"))
                .liquid(new FluidBuilder().attribute(FluidAttributes.ACID))
                .color(0xDCDC01)
                .flags(DISABLE_DECOMPOSITION)
                .components(Hydrogen, 3, Phosphorus, 1, Oxygen, 4)
                .buildAndRegister();

        SulfurTrioxide = new Material.Builder(GTPM.id("sulfur_trioxide"))
                .localization(text -> text.source("Sulfur Trioxide")
                        .translation(MinecraftLocale.ZH_CN, "三氧化硫"))
                .gas()
                .color(0xA0A014)
                .components(Sulfur, 1, Oxygen, 3)
                .hazard(HazardProperty.HazardTrigger.INHALATION, GTMedicalConditions.POISON, 1)
                .buildAndRegister();

        SulfurDioxide = new Material.Builder(GTPM.id("sulfur_dioxide"))
                .localization(text -> text.source("Sulfur Dioxide")
                        .translation(MinecraftLocale.ZH_CN, "二氧化硫"))
                .gas()
                .color(0x0E4880)
                .components(Sulfur, 1, Oxygen, 2)
                .hazard(HazardProperty.HazardTrigger.INHALATION, GTMedicalConditions.POISON, 1)
                .buildAndRegister();

        CarbonMonoxide = new Material.Builder(GTPM.id("carbon_monoxide"))
                .localization(text -> text.source("Carbon Monoxide")
                        .translation(MinecraftLocale.ZH_CN, "一氧化碳"))
                .gas()
                .color(0x0E4880)
                .components(Carbon, 1, Oxygen, 1)
                .hazard(HazardProperty.HazardTrigger.INHALATION, GTMedicalConditions.CARBON_MONOXIDE_POISONING)
                .buildAndRegister();

        HypochlorousAcid = new Material.Builder(GTPM.id("hypochlorous_acid"))
                .localization(text -> text.source("Hypochlorous Acid")
                        .translation(MinecraftLocale.ZH_CN, "次氯酸"))
                .liquid(new FluidBuilder().attribute(FluidAttributes.ACID))
                .color(0x6F8A91)
                .components(Hydrogen, 1, Chlorine, 1, Oxygen, 1)
                .buildAndRegister();

        Ammonia = new Material.Builder(GTPM.id("ammonia"))
                .localization(text -> text.source("Ammonia")
                        .translation(MinecraftLocale.ZH_CN, "氨"))
                .gas()
                .color(0x4465a2).secondaryColor(0x3F3480)
                .components(Nitrogen, 1, Hydrogen, 3)
                .buildAndRegister();

        HydrofluoricAcid = new Material.Builder(GTPM.id("hydrofluoric_acid"))
                .localization(text -> text.source("Hydrofluoric Acid")
                        .translation(MinecraftLocale.ZH_CN, "氢氟酸"))
                .liquid(new FluidBuilder().attribute(FluidAttributes.ACID))
                .color(0x0088AA)
                .components(Hydrogen, 1, Fluorine, 1)
                // TODO HF poisoning .hazard(HazardProperty.HazardTrigger.ANY)
                .buildAndRegister();

        NitricOxide = new Material.Builder(GTPM.id("nitric_oxide"))
                .localization(text -> text.source("Nitric Oxide")
                        .translation(MinecraftLocale.ZH_CN, "一氧化氮"))
                .gas()
                .color(0x7DC8F0)
                .components(Nitrogen, 1, Oxygen, 1)
                .hazard(HazardProperty.HazardTrigger.INHALATION, GTMedicalConditions.POISON, 1)
                .buildAndRegister();

        Iron3Chloride = new Material.Builder(GTPM.id("iron_iii_chloride"))
                .localization(text -> text.source("Iron III Chloride")
                        .translation(MinecraftLocale.ZH_CN, "氯化铁"))
                .liquid()
                .color(0x060B0B)
                .flags(DECOMPOSITION_BY_ELECTROLYZING)
                .components(Iron, 1, Chlorine, 3)
                .buildAndRegister();

        Iron2Chloride = new Material.Builder(GTPM.id("iron_ii_chloride"))
                .localization(text -> text.source("Iron II Chloride")
                        .translation(MinecraftLocale.ZH_CN, "氯化亚铁"))
                .liquid()
                .color(0xe8e0be)
                .flags(DECOMPOSITION_BY_ELECTROLYZING)
                .components(Iron, 1, Chlorine, 2)
                .buildAndRegister();

        UraniumHexafluoride = new Material.Builder(GTPM.id("uranium_hexafluoride"))
                .localization(text -> text.source("Uranium Hexafluoride")
                        .translation(MinecraftLocale.ZH_CN, "六氟化铀"))
                .gas()
                .color(0x42D126)
                .flags(DISABLE_DECOMPOSITION)
                .components(Uranium238, 1, Fluorine, 6)
                .buildAndRegister()
                .setFormula("UF6", true);

        EnrichedUraniumHexafluoride = new Material.Builder(GTPM.id("enriched_uranium_hexafluoride"))
                .localization(text -> text.source("Enriched Uranium Hexafluoride")
                        .translation(MinecraftLocale.ZH_CN, "富集六氟化铀"))
                .gas()
                .color(0x4BF52A)
                .flags(DISABLE_DECOMPOSITION)
                .components(Uranium235, 1, Fluorine, 6)
                .buildAndRegister();

        DepletedUraniumHexafluoride = new Material.Builder(GTPM.id("depleted_uranium_hexafluoride"))
                .localization(text -> text.source("Depleted Uranium Hexafluoride")
                        .translation(MinecraftLocale.ZH_CN, "枯竭六氟化铀"))
                .gas()
                .color(0x74BA66)
                .flags(DISABLE_DECOMPOSITION)
                .components(Uranium238, 1, Fluorine, 6)
                .buildAndRegister();

        NitrousOxide = new Material.Builder(GTPM.id("nitrous_oxide"))
                .localization(text -> text.source("Nitrous Oxide")
                        .translation(MinecraftLocale.ZH_CN, "一氧化二氮"))
                .gas()
                .color(0x7DC8FF)
                .components(Nitrogen, 2, Oxygen, 1)
                .hazard(HazardProperty.HazardTrigger.INHALATION, GTMedicalConditions.POISON, .5f)
                .buildAndRegister();

        EnderPearl = new Material.Builder(GTPM.id("ender_pearl"))
                .localization(text -> text.source("Ender Pearl")
                        .translation(MinecraftLocale.ZH_CN, "末影珍珠"))
                .gem(1)
                .color(0x8cf4e2).secondaryColor(0x032620).iconSet(SHINY)
                .flags(NO_SMASHING, NO_SMELTING, GENERATE_PLATE)
                .components(Beryllium, 1, Potassium, 4, Nitrogen, 5)
                .buildAndRegister();

        PotassiumFeldspar = new Material.Builder(GTPM.id("potassium_feldspar"))
                .localization(text -> text.source("Potassium Feldspar")
                        .translation(MinecraftLocale.ZH_CN, "钾长石"))
                .dust(1)
                .color(0xffe3bc).secondaryColor(0xd4918a).iconSet(FINE)
                .components(Potassium, 1, Aluminium, 1, Silicon, 3, Oxygen, 8)
                .buildAndRegister();

        NeodymiumMagnetic = new Material.Builder(GTPM.id("magnetic_neodymium"))
                .localization(text -> text.source("Magnetic Neodymium")
                        .translation(MinecraftLocale.ZH_CN, "磁化钕"))
                .ingot()
                .color(0x9a8b94).secondaryColor(0x2c2c2c).iconSet(MAGNETIC)
                .flags(GENERATE_ROD, IS_MAGNETIC)
                .components(Neodymium, 1)
                .ingotSmeltInto(Neodymium)
                .arcSmeltInto(Neodymium)
                .macerateInto(Neodymium)
                .buildAndRegister();
        Neodymium.getProperty(PropertyKey.INGOT).setMagneticMaterial(NeodymiumMagnetic);

        HydrochloricAcid = new Material.Builder(GTPM.id("hydrochloric_acid"))
                .localization(text -> text.source("Hydrochloric Acid")
                        .translation(MinecraftLocale.ZH_CN, "盐酸"))
                .liquid(new FluidBuilder().attribute(FluidAttributes.ACID).customStill())
                .components(Hydrogen, 1, Chlorine, 1)
                .buildAndRegister();

        Steam = new Material.Builder(GTPM.id("steam"))
                .localization(text -> text.source("Steam")
                        .translation(MinecraftLocale.ZH_CN, "蒸汽"))
                .gas(new FluidBuilder()
                        .state(FluidState.GAS)
                        .temperature(373)
                        .customStill())
                .flags(DISABLE_DECOMPOSITION)
                .components(Hydrogen, 2, Oxygen, 1)
                .buildAndRegister();

        DistilledWater = new Material.Builder(GTPM.id("distilled_water"))
                .localization(text -> text.source("Distilled Water")
                        .translation(MinecraftLocale.ZH_CN, "蒸馏水"))
                .fluid()
                .color(0x4A94FF)
                .flags(DISABLE_DECOMPOSITION)
                .components(Hydrogen, 2, Oxygen, 1)
                .buildAndRegister();

        SodiumPotassium = new Material.Builder(GTPM.id("sodium_potassium"))
                .localization(text -> text.source("Sodium Potassium")
                        .translation(MinecraftLocale.ZH_CN, "钠钾合金"))
                .fluid()
                .color(0x64FCB4)
                .flags(DECOMPOSITION_BY_CENTRIFUGING)
                .components(Sodium, 1, Potassium, 1)
                .buildAndRegister();

        SamariumMagnetic = new Material.Builder(GTPM.id("magnetic_samarium"))
                .localization(text -> text.source("Magnetic Samarium")
                        .translation(MinecraftLocale.ZH_CN, "磁化钐"))
                .ingot()
                .color(0xc5c5b3).secondaryColor(0x183e3f).iconSet(MAGNETIC)
                .flags(GENERATE_LONG_ROD, IS_MAGNETIC)
                .components(Samarium, 1)
                .ingotSmeltInto(Samarium)
                .arcSmeltInto(Samarium)
                .macerateInto(Samarium)
                .buildAndRegister();
        Samarium.getProperty(PropertyKey.INGOT).setMagneticMaterial(SamariumMagnetic);

        ManganesePhosphide = new Material.Builder(GTPM.id("manganese_phosphide"))
                .localization(text -> text.source("Manganese Phosphide")
                        .translation(MinecraftLocale.ZH_CN, "磷化锰"))
                .ingot()
                .liquid(new FluidBuilder().temperature(1368))
                .color(0xE1B454).secondaryColor(0x223033).iconSet(METALLIC)
                .flags(DECOMPOSITION_BY_ELECTROLYZING)
                .components(Manganese, 1, Phosphorus, 1)
                .cableProperties(V[LV], 2, 0, true, 78)
                .blast(1200, GasTier.LOW)
                .buildAndRegister();

        MagnesiumDiboride = new Material.Builder(GTPM.id("magnesium_diboride"))
                .localization(text -> text.source("Magnesium Diboride")
                        .translation(MinecraftLocale.ZH_CN, "二硼化镁"))
                .ingot()
                .liquid(new FluidBuilder().temperature(1103))
                .color(0x603c1a).secondaryColor(0x423e39).iconSet(METALLIC)
                .flags(DECOMPOSITION_BY_ELECTROLYZING)
                .components(Magnesium, 1, Boron, 2)
                .cableProperties(V[MV], 4, 0, true, 78)
                .blast(b -> b.temp(2500, GasTier.LOW)
                        .blastStats(VA[HV], 1000)
                        .vacuumStats(VA[MV], 200))
                .buildAndRegister();

        MercuryBariumCalciumCuprate = new Material.Builder(GTPM.id("mercury_barium_calcium_cuprate"))
                .localization(text -> text.source("Mercury Barium Calcium Cuprate")
                        .translation(MinecraftLocale.ZH_CN, "汞钡钙铜氧化物"))
                .ingot()
                .liquid(new FluidBuilder().temperature(1075))
                .color(0x928547).secondaryColor(0x3f2e2e).iconSet(SHINY)
                .flags(DECOMPOSITION_BY_ELECTROLYZING)
                .components(Mercury, 1, Barium, 2, Calcium, 2, Copper, 3, Oxygen, 8)
                .cableProperties(V[HV], 4, 0, true, 78)
                .blast(b -> b.temp(3300, GasTier.LOW)
                        .blastStats(VA[HV], 1500)
                        .vacuumStats(VA[HV]))
                .buildAndRegister();

        UraniumTriplatinum = new Material.Builder(GTPM.id("uranium_triplatinum"))
                .localization(text -> text.source("Uranium Triplatinum")
                        .translation(MinecraftLocale.ZH_CN, "三铂化铀"))
                .ingot()
                .liquid(new FluidBuilder().temperature(1882))
                .color(0x457045).secondaryColor(0x66ff00).iconSet(RADIOACTIVE)
                .flags(DECOMPOSITION_BY_CENTRIFUGING)
                .components(Uranium238, 1, Platinum, 3)
                .cableProperties(V[EV], 6, 0, true, 30)
                .blast(b -> b.temp(4400, GasTier.MID)
                        .blastStats(VA[EV], 1000)
                        .vacuumStats(VA[EV], 200))
                .buildAndRegister()
                .setFormula("UPt3", true);

        SamariumIronArsenicOxide = new Material.Builder(GTPM.id("samarium_iron_arsenic_oxide"))
                .localization(text -> text.source("Samarium Iron Arsenic Oxide")
                        .translation(MinecraftLocale.ZH_CN, "钐铁砷氧化物"))
                .ingot()
                .liquid(new FluidBuilder().temperature(1347))
                .color(0x850e85).secondaryColor(0x332f33).iconSet(SHINY)
                .flags(DECOMPOSITION_BY_CENTRIFUGING)
                .components(Samarium, 1, Iron, 1, Arsenic, 1, Oxygen, 1)
                .cableProperties(V[IV], 6, 0, true, 30)
                .blast(b -> b.temp(5200, GasTier.MID)
                        .blastStats(VA[EV], 1500)
                        .vacuumStats(VA[IV], 200))
                .buildAndRegister();

        IndiumTinBariumTitaniumCuprate = new Material.Builder(GTPM.id("indium_tin_barium_titanium_cuprate"))
                .localization(text -> text.source("Indium Tin Barium Titanium Cuprate")
                        .translation(MinecraftLocale.ZH_CN, "铟锡钡钛铜氧化物"))
                .ingot()
                .liquid(new FluidBuilder().temperature(1012))
                .color(0x686760).secondaryColor(0x673300).iconSet(METALLIC)
                .flags(DECOMPOSITION_BY_ELECTROLYZING, GENERATE_FINE_WIRE)
                .components(Indium, 4, Tin, 2, Barium, 2, Titanium, 1, Copper, 7, Oxygen, 14)
                .cableProperties(V[LuV], 8, 0, true, 5)
                .blast(b -> b.temp(6000, GasTier.HIGH)
                        .blastStats(VA[IV], 1000)
                        .vacuumStats(VA[LuV]))
                .buildAndRegister();

        UraniumRhodiumDinaquadide = new Material.Builder(GTPM.id("uranium_rhodium_dinaquadide"))
                .localization(text -> text.source("Uranium Rhodium Dinaquadide")
                        .translation(MinecraftLocale.ZH_CN, "铀铑硅岩合金"))
                .ingot()
                .liquid(new FluidBuilder().temperature(3410))
                .color(0x232020).secondaryColor(0xff009c).iconSet(RADIOACTIVE)
                .flags(DECOMPOSITION_BY_CENTRIFUGING, GENERATE_FINE_WIRE)
                .components(Uranium238, 1, Rhodium, 1, Naquadah, 2)
                .cableProperties(V[ZPM], 8, 0, true, 5)
                .blast(b -> b.temp(9000, GasTier.HIGH)
                        .blastStats(VA[IV], 1500)
                        .vacuumStats(VA[ZPM], 200))
                .buildAndRegister()
                .setFormula("URhNq2", true);

        EnrichedNaquadahTriniumEuropiumDuranide = new Material.Builder(
                GTPM.id("enriched_naquadah_trinium_europium_duranide"))
                .localization(text -> text.source("Enriched Naquadah Trinium Europium Duranide")
                        .translation(MinecraftLocale.ZH_CN, "富集硅岩凯金铕铿铀合金"))
                .ingot()
                .liquid(new FluidBuilder().temperature(5930))
                .color(0xc6b083).secondaryColor(0x45063d).iconSet(METALLIC)
                .flags(DECOMPOSITION_BY_CENTRIFUGING, GENERATE_FINE_WIRE)
                .components(NaquadahEnriched, 4, Trinium, 3, Europium, 2, Duranium, 1)
                .cableProperties(V[UV], 16, 0, true, 3)
                .blast(b -> b.temp(9900, GasTier.HIGH)
                        .blastStats(VA[LuV], 1200)
                        .vacuumStats(VA[UV], 200))
                .buildAndRegister();

        RutheniumTriniumAmericiumNeutronate = new Material.Builder(GTPM.id("ruthenium_trinium_americium_neutronate"))
                .localization(text -> text.source("Ruthenium Trinium Americium Neutronate")
                        .translation(MinecraftLocale.ZH_CN, "钌凯金镅中子素氧化物"))
                .ingot()
                .liquid(new FluidBuilder().temperature(23691))
                .color(0x897b76).secondaryColor(0x00c0ff).iconSet(RADIOACTIVE)
                .flags(DECOMPOSITION_BY_ELECTROLYZING)
                .components(Ruthenium, 1, Trinium, 2, Americium, 1, Neutronium, 2, Oxygen, 8)
                .cableProperties(V[UHV], 24, 0, true, 3)
                .blast(b -> b.temp(10800, GasTier.HIGHER)
                        .blastStats(VA[ZPM], 1000)
                        .vacuumStats(VA[UHV], 200))
                .buildAndRegister();

        InertMetalMixture = new Material.Builder(GTPM.id("inert_metal_mixture"))
                .localization(text -> text.source("Inert Metal Mixture")
                        .translation(MinecraftLocale.ZH_CN, "惰性金属混合物"))
                .dust()
                .color(0x2b0645).secondaryColor(0x6a1600).iconSet(METALLIC)
                .flags(DISABLE_DECOMPOSITION)
                .components(Rhodium, 1, Ruthenium, 1, Oxygen, 4)
                .buildAndRegister();

        RhodiumSulfate = new Material.Builder(GTPM.id("rhodium_sulfate"))
                .localization(text -> text.source("Rhodium Sulfate")
                        .translation(MinecraftLocale.ZH_CN, "硫酸铑"))
                .liquid(new FluidBuilder().temperature(1128))
                .color(0xEEAA55)
                .flags(DISABLE_DECOMPOSITION)
                .components(Rhodium, 2, Sulfur, 3, Oxygen, 12)
                .buildAndRegister()
                .setFormula("Rh2(SO4)3", true);

        RutheniumTetroxide = new Material.Builder(GTPM.id("ruthenium_tetroxide"))
                .localization(text -> text.source("Ruthenium Tetroxide")
                        .translation(MinecraftLocale.ZH_CN, "四氧化钌"))
                .dust()
                .color(0xbeb809).secondaryColor(0x4e4e4d)
                .flags(DISABLE_DECOMPOSITION)
                .components(Ruthenium, 1, Oxygen, 4)
                .buildAndRegister();

        OsmiumTetroxide = new Material.Builder(GTPM.id("osmium_tetroxide"))
                .localization(text -> text.source("Osmium Tetroxide")
                        .translation(MinecraftLocale.ZH_CN, "四氧化锇"))
                .dust()
                .color(0x578d9f).secondaryColor(0x394117).iconSet(METALLIC)
                .flags(DISABLE_DECOMPOSITION)
                .components(Osmium, 1, Oxygen, 4)
                // TODO Osmium tetroxide poisoning .hazard(HazardProperty.HazardTrigger.ANY)
                .buildAndRegister();

        IridiumChloride = new Material.Builder(GTPM.id("iridium_chloride"))
                .localization(text -> text.source("Iridium Chloride")
                        .translation(MinecraftLocale.ZH_CN, "氯化铱"))
                .dust()
                .color(0x41460c).secondaryColor(0x00542e).iconSet(FINE)
                .flags(DISABLE_DECOMPOSITION)
                .components(Iridium, 1, Chlorine, 3)
                .buildAndRegister();

        FluoroantimonicAcid = new Material.Builder(GTPM.id("fluoroantimonic_acid"))
                .localization(text -> text.source("Fluoroantimonic Acid")
                        .translation(MinecraftLocale.ZH_CN, "氟锑酸"))
                .liquid(new FluidBuilder().attribute(FluidAttributes.ACID).customStill())
                .components(Hydrogen, 2, Antimony, 1, Fluorine, 7)
                .buildAndRegister();

        TitaniumTrifluoride = new Material.Builder(GTPM.id("titanium_trifluoride"))
                .localization(text -> text.source("Titanium Trifluoride")
                        .translation(MinecraftLocale.ZH_CN, "三氟化钛"))
                .dust()
                .color(0x8F00FF).secondaryColor(0x341465).iconSet(SHINY)
                .flags(DISABLE_DECOMPOSITION)
                .components(Titanium, 1, Fluorine, 3)
                .buildAndRegister();

        CalciumPhosphide = new Material.Builder(GTPM.id("calcium_phosphide"))
                .localization(text -> text.source("Calcium Phosphide")
                        .translation(MinecraftLocale.ZH_CN, "磷化钙"))
                .dust()
                .color(0xFFF5DE).secondaryColor(0xf6baba).iconSet(METALLIC)
                .components(Calcium, 1, Phosphorus, 1)
                .buildAndRegister();

        IndiumPhosphide = new Material.Builder(GTPM.id("indium_phosphide"))
                .localization(text -> text.source("Indium Phosphide")
                        .translation(MinecraftLocale.ZH_CN, "磷化铟"))
                .dust()
                .color(0x734d77).secondaryColor(0x2c272d).iconSet(SHINY)
                .flags(DISABLE_DECOMPOSITION)
                .components(Indium, 1, Phosphorus, 1)
                .buildAndRegister();

        BariumSulfide = new Material.Builder(GTPM.id("barium_sulfide"))
                .localization(text -> text.source("Barium Sulfide")
                        .translation(MinecraftLocale.ZH_CN, "硫化钡"))
                .dust()
                .color(0x80784a).secondaryColor(0x2c333b).iconSet(METALLIC)
                .components(Barium, 1, Sulfur, 1)
                .buildAndRegister();

        TriniumSulfide = new Material.Builder(GTPM.id("trinium_sulfide"))
                .localization(text -> text.source("Trinium Sulfide")
                        .translation(MinecraftLocale.ZH_CN, "硫化凯金"))
                .dust()
                .color(0xE68066).secondaryColor(0x6f143a).iconSet(SHINY)
                .flags(DISABLE_DECOMPOSITION)
                .components(Trinium, 1, Sulfur, 1)
                .buildAndRegister();

        ZincSulfide = new Material.Builder(GTPM.id("zinc_sulfide"))
                .localization(text -> text.source("Zinc Sulfide")
                        .translation(MinecraftLocale.ZH_CN, "硫化锌"))
                .dust()
                .color(0xfff4d5).secondaryColor(0xdadada)
                .components(Zinc, 1, Sulfur, 1)
                .buildAndRegister();

        GalliumSulfide = new Material.Builder(GTPM.id("gallium_sulfide"))
                .localization(text -> text.source("Gallium Sulfide")
                        .translation(MinecraftLocale.ZH_CN, "硫化镓"))
                .dust()
                .color(0xffee5d).secondaryColor(0xedf008).iconSet(SHINY)
                .components(Gallium, 1, Sulfur, 1)
                .buildAndRegister();

        AntimonyTrifluoride = new Material.Builder(GTPM.id("antimony_trifluoride"))
                .localization(text -> text.source("Antimony Trifluoride")
                        .translation(MinecraftLocale.ZH_CN, "三氟化锑"))
                .dust()
                .color(0xfffbef).secondaryColor(0xF7EABC).iconSet(METALLIC)
                .flags(DISABLE_DECOMPOSITION)
                .components(Antimony, 1, Fluorine, 3)
                .buildAndRegister();

        EnrichedNaquadahSulfate = new Material.Builder(GTPM.id("enriched_naquadah_sulfate"))
                .localization(text -> text.source("Enriched Naquadah Sulfate")
                        .translation(MinecraftLocale.ZH_CN, "硫酸富集硅岩"))
                .dust()
                .color(0xff8023).secondaryColor(0x044610).iconSet(METALLIC)
                .flags(DISABLE_DECOMPOSITION)
                .components(NaquadahEnriched, 1, Sulfur, 1, Oxygen, 4)
                .buildAndRegister();

        NaquadriaSulfate = new Material.Builder(GTPM.id("naquadria_sulfate"))
                .localization(text -> text.source("Naquadria Sulfate")
                        .translation(MinecraftLocale.ZH_CN, "硫酸超能硅岩"))
                .dust()
                .color(0x85ff5a).secondaryColor(0x006633).iconSet(SHINY)
                .flags(DISABLE_DECOMPOSITION)
                .components(Naquadria, 1, Sulfur, 1, Oxygen, 4)
                .buildAndRegister();

        Pyrochlore = new Material.Builder(GTPM.id("pyrochlore"))
                .localization(text -> text.source("Pyrochlore")
                        .translation(MinecraftLocale.ZH_CN, "烧绿石"))
                .dust().ore()
                .color(0x5b4838).secondaryColor(0x331400).iconSet(METALLIC)
                .components(Calcium, 2, Niobium, 2, Oxygen, 6, Fluorine, 1)
                .buildAndRegister();

        PotassiumHydroxide = new Material.Builder(GTPM.id("potassium_hydroxide"))
                .localization(text -> text.source("Potassium Hydroxide")
                        .translation(MinecraftLocale.ZH_CN, "氢氧化钾"))
                .dust(1)
                .color(0xd1c299).secondaryColor(0x85623a).iconSet(METALLIC)
                .hazard(HazardProperty.HazardTrigger.SKIN_CONTACT, GTMedicalConditions.CHEMICAL_BURNS)
                .components(Potassium, 1, Oxygen, 1, Hydrogen, 1)
                .buildAndRegister();

        PotassiumIodide = new Material.Builder(GTPM.id("potassium_iodide"))
                .localization(text -> text.source("Potassium Iodide")
                        .translation(MinecraftLocale.ZH_CN, "碘化钾"))
                .dust()
                .color(0xa66c71).secondaryColor(0x802d67).iconSet(METALLIC)
                .components(Potassium, 1, Iodine, 1)
                .buildAndRegister();

        PotassiumCarbonate = new Material.Builder(GTPM.id("potassium_carbonate"))
                .localization(text -> text.source("Potassium Carbonate")
                        .translation(MinecraftLocale.ZH_CN, "碳酸钾"))
                .dust()
                .color(0xa66c71).secondaryColor(0x802d67).iconSet(METALLIC)
                .components(Potassium, 2, Carbon, 1, Oxygen, 3)
                .buildAndRegister();

        PotassiumFerrocyanide = new Material.Builder(GTPM.id("potassium_ferrocyanide"))
                .localization(text -> text.source("Potassium Ferrocyanide")
                        .translation(MinecraftLocale.ZH_CN, "亚铁氰化钾"))
                .dust()
                .color(0xc9a842).secondaryColor(0x947110).iconSet(DULL)
                .components(Potassium, 4, Iron, 1, Carbon, 6, Nitrogen, 6)
                .buildAndRegister()
                .setFormula("K4[Fe(CN)6]", true);

        CalciumFerrocyanide = new Material.Builder(GTPM.id("calcium_ferrocyanide"))
                .localization(text -> text.source("Calcium Ferrocyanide")
                        .translation(MinecraftLocale.ZH_CN, "亚铁氰化钙"))
                .dust()
                .color(0xc9a842).secondaryColor(0x947110).iconSet(DULL)
                .components(Calcium, 2, Iron, 1, Carbon, 6, Nitrogen, 6)
                .buildAndRegister()
                .setFormula("Ca2[Fe(CN)6]", true);

        CalciumHydroxide = new Material.Builder(GTPM.id("calcium_hydroxide"))
                .localization(text -> text.source("Calcium Hydroxide")
                        .translation(MinecraftLocale.ZH_CN, "氢氧化钙"))
                .dust()
                .color(0x72dbd4).secondaryColor(0x138a80).iconSet(ROUGH)
                .components(Calcium, 1, Oxygen, 2, Hydrogen, 2)
                .hazard(HazardProperty.HazardTrigger.SKIN_CONTACT, GTMedicalConditions.CHEMICAL_BURNS)
                .buildAndRegister()
                .setFormula("Ca(OH)2", true);

        CalciumCarbonate = new Material.Builder(GTPM.id("calcium_carbonate"))
                .localization(text -> text.source("Calcium Carbonate")
                        .translation(MinecraftLocale.ZH_CN, "碳酸钙"))
                .dust()
                .color(0xd9ca9c).secondaryColor(0xad913b)
                .components(Calcium, 1, Carbon, 1, Oxygen, 3)
                .buildAndRegister();

        PotassiumCyanide = new Material.Builder(GTPM.id("potassium_cyanide"))
                .localization(text -> text.source("Potassium Cyanide")
                        .translation(MinecraftLocale.ZH_CN, "氰化钾"))
                .dust()
                .color(0x93badb).secondaryColor(0x0c5696).iconSet(ROUGH)
                .components(Potassium, 1, Carbon, 1, Nitrogen, 1)
                .hazard(HazardProperty.HazardTrigger.ANY, GTMedicalConditions.CHEMICAL_BURNS, true)
                .buildAndRegister();

        HydrogenCyanide = new Material.Builder(GTPM.id("hydrogen_cyanide"))
                .localization(text -> text.source("Hydrogen Cyanide")
                        .translation(MinecraftLocale.ZH_CN, "氰化氢"))
                .gas()
                .color(0x72dbd4)
                .components(Hydrogen, 1, Carbon, 1, Nitrogen, 1)
                .hazard(HazardProperty.HazardTrigger.ANY, GTMedicalConditions.CHEMICAL_BURNS, true)
                .buildAndRegister();

        FormicAcid = new Material.Builder(GTPM.id("formic_acid"))
                .localization(text -> text.source("Formic Acid")
                        .translation(MinecraftLocale.ZH_CN, "甲酸"))
                .liquid(new FluidBuilder().attribute(FluidAttributes.ACID))
                .color(0xa6a6a6)
                .components(Carbon, 1, Hydrogen, 2, Oxygen, 2)
                .hazard(HazardProperty.HazardTrigger.INHALATION, GTMedicalConditions.CHEMICAL_BURNS)
                .buildAndRegister();

        PotassiumSulfate = new Material.Builder(GTPM.id("potassium_sulfate"))
                .localization(text -> text.source("Potassium Sulfate")
                        .translation(MinecraftLocale.ZH_CN, "硫酸钾"))
                .dust()
                .color(0xebab34).secondaryColor(0xb5570e)
                .flags(DECOMPOSITION_BY_ELECTROLYZING)
                .components(Potassium, 2, Sulfur, 1, Oxygen, 4)
                .buildAndRegister();

        PrussianBlue = new Material.Builder(GTPM.id("prussian_blue"))
                .localization(text -> text.source("Prussian Blue")
                        .translation(MinecraftLocale.ZH_CN, "普鲁士蓝"))
                .dust()
                .color(0x102e5e).secondaryColor(0x010c42)
                .flags(DISABLE_DECOMPOSITION)
                .components(Iron, 7, Carbon, 18, Nitrogen, 18)
                .buildAndRegister()
                .setFormula("Fe4[Fe(CN)6]3", true);

        Formaldehyde = new Material.Builder(GTPM.id("formaldehyde"))
                .localization(text -> text.source("Formaldehyde")
                        .translation(MinecraftLocale.ZH_CN, "甲醛"))
                .liquid()
                .color(0xddeced)
                .flags(DECOMPOSITION_BY_ELECTROLYZING)
                .components(Carbon, 1, Hydrogen, 2, Oxygen, 1)
                .hazard(HazardProperty.HazardTrigger.INHALATION, GTMedicalConditions.POISON)
                .buildAndRegister();

        Glycolonitrile = new Material.Builder(GTPM.id("glycolonitrile"))
                .localization(text -> text.source("Glycolonitrile")
                        .translation(MinecraftLocale.ZH_CN, "乙腈"))
                .liquid()
                .color(0x5b8c8f)
                .flags(DISABLE_DECOMPOSITION)
                .components(Carbon, 2, Hydrogen, 3, Nitrogen, 1, Oxygen, 1)
                .buildAndRegister();

        DiethylenetriaminePentaacetonitrile = new Material.Builder(GTPM.id("diethylenetriamine_pentaacetonitrile"))
                .localization(text -> text.source("Diethylenetriamine Pentaacetonitrile")
                        .translation(MinecraftLocale.ZH_CN, "二乙烯三胺五乙腈"))
                .liquid()
                .color(0xcbbfd6)
                .flags(DISABLE_DECOMPOSITION)
                .components(Carbon, 14, Hydrogen, 18, Nitrogen, 8)
                .buildAndRegister();

        DiethylenetriaminepentaaceticAcid = new Material.Builder(GTPM.id("diethylenetriaminepentaacetic_acid"))
                .localization(text -> text.source("Diethylenetriaminepentaacetic Acid")
                        .translation(MinecraftLocale.ZH_CN, "二乙烯三胺五乙酸"))
                .dust()
                .color(0xe8c93c).secondaryColor(0xc99118)
                .flags(DISABLE_DECOMPOSITION)
                .components(Carbon, 14, Hydrogen, 23, Nitrogen, 3, Oxygen, 10)
                .buildAndRegister();

        SodiumNitrite = new Material.Builder(GTPM.id("sodium_nitrite"))
                .localization(text -> text.source("Sodium Nitrite")
                        .translation(MinecraftLocale.ZH_CN, "亚硝酸钠"))
                .dust()
                .color(0xcfbf65).secondaryColor(0x85600b)
                .flags(DECOMPOSITION_BY_ELECTROLYZING)
                .components(Sodium, 1, Nitrogen, 1, Oxygen, 2)
                .buildAndRegister();

        HydrogenPeroxide = new Material.Builder(GTPM.id("hydrogen_peroxide"))
                .localization(text -> text.source("Hydrogen Peroxide")
                        .translation(MinecraftLocale.ZH_CN, "过氧化氢"))
                .liquid()
                .color(0x0cbdd7)
                .components(Hydrogen, 2, Oxygen, 2)
                .hazard(HazardProperty.HazardTrigger.ANY, GTMedicalConditions.CHEMICAL_BURNS, true)
                .buildAndRegister();

        IlmeniteSlag = new Material.Builder(GTPM.id("ilmenite_slag"))
                .localization(text -> text.source("Ilmenite Slag")
                        .translation(MinecraftLocale.ZH_CN, "钛铁矿渣"))
                .dust()
                .color(0x8B0000).iconSet(SAND)
                .buildAndRegister();
    }
}
