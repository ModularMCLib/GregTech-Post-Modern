package com.modularmc.gtpm.common.data.materials;

import com.modularmc.gtpm.GTPM;
import com.modularmc.gtpm.api.data.chemical.material.Material;
import com.modularmc.gtpm.api.data.chemical.material.properties.BlastProperty.GasTier;
import com.modularmc.gtpm.api.data.chemical.material.properties.HazardProperty;
import com.modularmc.gtpm.api.data.chemical.material.properties.PropertyKey;
import com.modularmc.gtpm.api.data.chemical.material.properties.ToolProperty;
import com.modularmc.gtpm.api.fluids.FluidBuilder;
import com.modularmc.gtpm.api.fluids.attribute.FluidAttributes;
import com.modularmc.gtpm.api.item.tool.GTToolType;
import com.modularmc.gtpm.common.data.GTMedicalConditions;

import com.modularmc.registrate.localization.MinecraftLocale;

import net.minecraft.world.item.enchantment.Enchantments;

import static com.modularmc.gtpm.api.GTValues.*;
import static com.modularmc.gtpm.api.data.chemical.material.info.MaterialFlags.*;
import static com.modularmc.gtpm.api.data.chemical.material.info.MaterialIconSet.*;
import static com.modularmc.gtpm.common.data.GTMaterials.*;

public class SecondDegreeMaterials {

    public static void register() {
        Glass = new Material.Builder(GTPM.id("glass"))
                .localization(text -> text.source("Glass")
                        .translation(MinecraftLocale.ZH_CN, "玻璃"))
                .gem(0)
                .liquid(new FluidBuilder()
                        .temperature(1200)
                        .customStill())
                .color(0xffffff).iconSet(GLASS)
                .flags(GENERATE_LENS, NO_SMASHING, EXCLUDE_BLOCK_CRAFTING_RECIPES, DECOMPOSITION_BY_CENTRIFUGING)
                .components(SiliconDioxide, 1)
                .buildAndRegister();

        Perlite = new Material.Builder(GTPM.id("perlite"))
                .localization(text -> text.source("Perlite")
                        .translation(MinecraftLocale.ZH_CN, "珍珠岩"))
                .dust(1)
                .color(0xeee0e0).secondaryColor(0xc1b9a9)
                .components(Obsidian, 2, Water, 1)
                .buildAndRegister();

        ActivatedCarbon = new Material.Builder(GTPM.id("activated_carbon"))
                .localization(text -> text.source("Activated Carbon")
                        .translation(MinecraftLocale.ZH_CN, "活性炭"))
                .dust(1)
                .color(0x212125).secondaryColor(0x15151a)
                .components(Carbon, 1)
                .flags(DECOMPOSITION_BY_CENTRIFUGING)
                .buildAndRegister();

        Borax = new Material.Builder(GTPM.id("borax"))
                .localization(text -> text.source("Borax")
                        .translation(MinecraftLocale.ZH_CN, "硼砂"))
                .dust(1)
                .color(0xFAFAFA).secondaryColor(0xd7e7e7).iconSet(FINE)
                .components(Sodium, 2, Boron, 4, Water, 10, Oxygen, 7)
                .buildAndRegister();

        SaltWater = new Material.Builder(GTPM.id("salt_water"))
                .localization(text -> text.source("Salt Water")
                        .translation(MinecraftLocale.ZH_CN, "盐水"))
                .fluid()
                .color(0x0000C8)
                .flags(DISABLE_DECOMPOSITION)
                .components(Salt, 1, Water, 1)
                .buildAndRegister();

        Olivine = new Material.Builder(GTPM.id("olivine"))
                .localization(text -> text.source("Olivine")
                        .translation(MinecraftLocale.ZH_CN, "橄榄石"))
                .gem().ore(2, 1)
                .color(0xa7e404).secondaryColor(0x166439).iconSet(RUBY)
                .appendFlags(EXT_METAL, NO_SMASHING, NO_SMELTING, HIGH_SIFTER_OUTPUT)
                .components(Magnesium, 1, Iron, 1, SiliconDioxide, 2)
                .buildAndRegister();

        Opal = new Material.Builder(GTPM.id("opal"))
                .localization(text -> text.source("Opal")
                        .translation(MinecraftLocale.ZH_CN, "猫眼石"))
                .gem().ore()
                .color(0xf9e3ea).secondaryColor(0x16bbe0).iconSet(OPAL)
                .appendFlags(EXT_METAL, NO_SMASHING, NO_SMELTING, HIGH_SIFTER_OUTPUT, DECOMPOSITION_BY_CENTRIFUGING)
                .components(SiliconDioxide, 1)
                .buildAndRegister();

        Amethyst = new Material.Builder(GTPM.id("amethyst"))
                .localization(text -> text.source("Amethyst")
                        .translation(MinecraftLocale.ZH_CN, "紫水晶"))
                .gem(3).ore()
                .color(0xcfa0f3).secondaryColor(0x734fbc).iconSet(RUBY)
                .appendFlags(EXT_METAL, NO_SMASHING, NO_SMELTING, HIGH_SIFTER_OUTPUT, GENERATE_LENS)
                .components(SiliconDioxide, 4, Iron, 1)
                .buildAndRegister();

        EchoShard = new Material.Builder(GTPM.id("echo_shard"))
                .localization(text -> text.source("Echo Shard")
                        .translation(MinecraftLocale.ZH_CN, "回响碎片"))
                .gem(3)
                .color(0x002b2d).iconSet(RUBY)
                .appendFlags(EXT_METAL, NO_SMASHING, NO_SMELTING, GENERATE_ROD)
                .components(SiliconDioxide, 3, Sculk, 2)
                .buildAndRegister();

        Lapis = new Material.Builder(GTPM.id("lapis"))
                .localization(text -> text.source("Lapis")
                        .translation(MinecraftLocale.ZH_CN, "青金石"))
                .gem(1).ore(6, 4)
                .color(0x85a9ff).secondaryColor(0x2a7fff).iconSet(LAPIS)
                .flags(NO_SMASHING, NO_SMELTING, CRYSTALLIZABLE, NO_WORKING, DECOMPOSITION_BY_ELECTROLYZING,
                        EXCLUDE_BLOCK_CRAFTING_BY_HAND_RECIPES,
                        GENERATE_PLATE, GENERATE_ROD)
                .components(Lazurite, 12, Sodalite, 2, Pyrite, 1, Calcite, 1)
                .buildAndRegister();

        Blaze = new Material.Builder(GTPM.id("blaze"))
                .localization(text -> text.source("Blaze")
                        .translation(MinecraftLocale.ZH_CN, "烈焰"))
                .dust(1)
                .liquid(new FluidBuilder()
                        .temperature(4000)
                        .customStill())
                .color(0xfff94d, false).secondaryColor(0xff330c).iconSet(FINE)
                .flags(NO_SMELTING, MORTAR_GRINDABLE, DECOMPOSITION_BY_CENTRIFUGING) // todo burning flag
                .components(DarkAsh, 1, Sulfur, 1)
                .buildAndRegister();

        Apatite = new Material.Builder(GTPM.id("apatite"))
                .localization(text -> text.source("Apatite")
                        .translation(MinecraftLocale.ZH_CN, "磷灰石"))
                .gem(1).ore(4, 2)
                .color(0x06cdf1).secondaryColor(0x701c07).iconSet(DIAMOND)
                .flags(NO_SMASHING, NO_SMELTING, CRYSTALLIZABLE, DISABLE_DECOMPOSITION)
                .components(Calcium, 5, Phosphate, 3, Chlorine, 1)
                .buildAndRegister();

        BlackSteel = new Material.Builder(GTPM.id("black_steel"))
                .localization(text -> text.source("Black Steel")
                        .translation(MinecraftLocale.ZH_CN, "黑钢"))
                .ingot().fluid()
                .color(0x666666).secondaryColor(0x1a120e).iconSet(METALLIC)
                .appendFlags(EXT_METAL, GENERATE_FINE_WIRE, GENERATE_GEAR, GENERATE_FRAME)
                .components(Nickel, 1, BlackBronze, 1, Steel, 3)
                .cableProperties(V[EV], 3, 2)
                .blast(1758, GasTier.LOW)
                .buildAndRegister();

        DamascusSteel = new Material.Builder(GTPM.id("damascus_steel"))
                .localization(text -> text.source("Damascus Steel")
                        .translation(MinecraftLocale.ZH_CN, "大马士革钢"))
                .ingot(3).fluid()
                .color(0x6E6E6E).secondaryColor(0x302222).iconSet(METALLIC)
                .appendFlags(EXT_METAL, GENERATE_BOLT_SCREW, GENERATE_LONG_ROD, GENERATE_GEAR)
                .components(Steel, 1)
                .toolStats(ToolProperty.Builder.of(6.0F, 4.0F, 1024, 3)
                        .addTypes(GTToolType.MORTAR)
                        .attackSpeed(0.3F).enchantability(33)
                        .enchantment(Enchantments.LOOTING, 3)
                        .enchantment(Enchantments.FORTUNE, 3).build())
                .blast(1500, GasTier.LOW)
                .buildAndRegister();

        TungstenSteel = new Material.Builder(GTPM.id("tungsten_steel"))
                .localization(text -> text.source("Tungstensteel")
                        .translation(MinecraftLocale.ZH_CN, "钨钢"))
                .ingot(4).fluid()
                .color(0x687ece).secondaryColor(0x03192f).iconSet(METALLIC)
                .appendFlags(EXT2_METAL, GENERATE_ROTOR, GENERATE_SMALL_GEAR, GENERATE_DENSE, GENERATE_FRAME,
                        GENERATE_SPRING, GENERATE_FOIL, GENERATE_FINE_WIRE, GENERATE_GEAR)
                .components(Steel, 1, Tungsten, 1)
                .toolStats(ToolProperty.Builder.of(9.0F, 7.0F, 2048, 4)
                        .enchantability(14).build())
                .rotorStats(160, 120, 4.0f, 2560)
                .fluidPipeProperties(3587, 225, true)
                .cableProperties(V[IV], 3, 2)
                .blast(b -> b.temp(3000, GasTier.MID)
                        .blastStats(VA[EV], 1000)
                        .vacuumStats(VA[HV]))
                .buildAndRegister();

        CobaltBrass = new Material.Builder(GTPM.id("cobalt_brass"))
                .localization(text -> text.source("Cobalt Brass")
                        .translation(MinecraftLocale.ZH_CN, "钴黄铜"))
                .ingot()
                .liquid(new FluidBuilder().temperature(1202))
                .color(0xbaa365).secondaryColor(0x596338).iconSet(METALLIC)
                .appendFlags(EXT2_METAL, GENERATE_GEAR)
                .components(Brass, 7, Aluminium, 1, Cobalt, 1)
                .toolStats(ToolProperty.Builder.of(2.5F, 2.0F, 1024, 2)
                        .addTypes(GTToolType.MORTAR)
                        .attackSpeed(5.8F).enchantability(5).build())
                .rotorStats(100, 120, 2.0f, 256)
                .itemPipeProperties(2048, 1)
                .buildAndRegister();

        TricalciumPhosphate = new Material.Builder(GTPM.id("tricalcium_phosphate"))
                .localization(text -> text.source("Tricalcium Phosphate")
                        .translation(MinecraftLocale.ZH_CN, "磷酸三钙"))
                .dust().ore(3, 1)
                .color(0xfffddb).secondaryColor(0xFFFF00).iconSet(FLINT)
                .flags(NO_SMASHING, NO_SMELTING, FLAMMABLE, EXPLOSIVE, DECOMPOSITION_BY_CENTRIFUGING)
                .components(Calcium, 3, Phosphate, 2)
                .buildAndRegister();

        GarnetRed = new Material.Builder(GTPM.id("red_garnet"))
                .localization(text -> text.source("Red Garnet")
                        .translation(MinecraftLocale.ZH_CN, "红石榴石"))
                .gem().ore(4, 1)
                .color(0x950c15).secondaryColor(0x510b04).iconSet(RUBY)
                .appendFlags(EXT_METAL, NO_SMASHING, NO_SMELTING, HIGH_SIFTER_OUTPUT, DECOMPOSITION_BY_CENTRIFUGING)
                .components(Pyrope, 3, Almandine, 5, Spessartine, 8)
                .buildAndRegister();

        GarnetYellow = new Material.Builder(GTPM.id("yellow_garnet"))
                .localization(text -> text.source("Yellow Garnet")
                        .translation(MinecraftLocale.ZH_CN, "黄石榴石"))
                .gem().ore(4, 1)
                .color(0xf6ff09).secondaryColor(0xe7a800).iconSet(RUBY)
                .appendFlags(EXT_METAL, NO_SMASHING, NO_SMELTING, HIGH_SIFTER_OUTPUT, DECOMPOSITION_BY_CENTRIFUGING)
                .components(Andradite, 5, Grossular, 8, Uvarovite, 3)
                .buildAndRegister();

        Marble = new Material.Builder(GTPM.id("marble"))
                .localization(text -> text.source("Marble")
                        .translation(MinecraftLocale.ZH_CN, "大理石"))
                .dust()
                .color(0xf0f5f4).secondaryColor(0xb3b3b3).iconSet(ROUGH)
                .flags(NO_SMASHING, DECOMPOSITION_BY_CENTRIFUGING)
                .components(Magnesium, 1, Calcite, 7)
                .buildAndRegister();

        Deepslate = new Material.Builder(GTPM.id("deepslate"))
                .localization(text -> text.source("Deepslate")
                        .translation(MinecraftLocale.ZH_CN, "深板岩"))
                .dust()
                .color(0x797979).secondaryColor(0x2f2f37).iconSet(ROUGH)
                .flags(NO_SMASHING, DECOMPOSITION_BY_CENTRIFUGING)
                .components(SiliconDioxide, 4, Biotite, 1)
                .buildAndRegister();

        RedGranite = new Material.Builder(GTPM.id("red_granite"))
                .localization(text -> text.source("Red Granite")
                        .translation(MinecraftLocale.ZH_CN, "红花岗岩"))
                .dust()
                .color(0xFF0080).iconSet(ROUGH)
                .flags(NO_SMASHING)
                .components(Aluminium, 2, PotassiumFeldspar, 1, Oxygen, 3)
                .buildAndRegister();

        VanadiumMagnetite = new Material.Builder(GTPM.id("vanadium_magnetite"))
                .localization(text -> text.source("Vanadium Magnetite")
                        .translation(MinecraftLocale.ZH_CN, "钒磁铁矿"))
                .dust().ore()
                .color(0x505d70).secondaryColor(0x170322).iconSet(METALLIC)
                .flags(DECOMPOSITION_BY_CENTRIFUGING)
                .components(Magnetite, 1, Vanadium, 1)
                .buildAndRegister();

        QuartzSand = new Material.Builder(GTPM.id("quartz_sand"))
                .localization(text -> text.source("Quartz Sand")
                        .translation(MinecraftLocale.ZH_CN, "石英砂"))
                .dust(1)
                .color(0xf8efe3).secondaryColor(0xe6c1bb).iconSet(SAND)
                .flags(DISABLE_DECOMPOSITION)
                .components(CertusQuartz, 1, Quartzite, 1)
                .buildAndRegister();

        Pollucite = new Material.Builder(GTPM.id("pollucite"))
                .localization(text -> text.source("Pollucite")
                        .translation(MinecraftLocale.ZH_CN, "铯榴石"))
                .dust().ore()
                .color(0xeed9e1).secondaryColor(0x72a6a7)
                .components(Caesium, 2, Aluminium, 2, Silicon, 4, Water, 2, Oxygen, 12)
                .buildAndRegister();

        Bentonite = new Material.Builder(GTPM.id("bentonite"))
                .localization(text -> text.source("Bentonite")
                        .translation(MinecraftLocale.ZH_CN, "膨润土"))
                .dust().ore(3, 1)
                .color(0xede8a3).secondaryColor(0xcdb44c).iconSet(ROUGH)
                .flags(DISABLE_DECOMPOSITION)
                .components(Sodium, 1, Magnesium, 6, Silicon, 12, Hydrogen, 6, Water, 5, Oxygen, 36)
                .buildAndRegister();

        FullersEarth = new Material.Builder(GTPM.id("fullers_earth"))
                .localization(text -> text.source("Fuller's Earth")
                        .translation(MinecraftLocale.ZH_CN, "漂白土"))
                .dust().ore(2, 1)
                .color(0xf3efbb).secondaryColor(0xb8d066).iconSet(FINE)
                .components(Magnesium, 2, Silicon, 4, Oxygen, 14, Hydrogen, 4, Water, 1)
                .buildAndRegister();

        Pitchblende = new Material.Builder(GTPM.id("pitchblende"))
                .localization(text -> text.source("Pitchblende")
                        .translation(MinecraftLocale.ZH_CN, "沥青铀矿"))
                .dust(3).ore(true)
                .color(0xffd647).secondaryColor(0x0d1e2f)
                .flags(DECOMPOSITION_BY_CENTRIFUGING)
                .components(Uraninite, 3, Thorium, 1, Lead, 1)
                .buildAndRegister()
                .setFormula("(UO2)3ThPb", true);

        Monazite = new Material.Builder(GTPM.id("monazite"))
                .localization(text -> text.source("Monazite")
                        .translation(MinecraftLocale.ZH_CN, "独居石"))
                .gem(1).ore(4, 2, true)
                .color(0xd0ee98).secondaryColor(0x520505).iconSet(DIAMOND)
                .flags(NO_SMASHING, NO_SMELTING, CRYSTALLIZABLE)
                .components(RareEarth, 1, Phosphate, 1)
                .buildAndRegister();

        Mirabilite = new Material.Builder(GTPM.id("mirabilite"))
                .localization(text -> text.source("Mirabilite")
                        .translation(MinecraftLocale.ZH_CN, "芒硝"))
                .dust()
                .color(0xf9e7e7).secondaryColor(0xb57a7a)
                .components(Sodium, 2, Sulfur, 1, Water, 10, Oxygen, 4)
                .buildAndRegister();

        Trona = new Material.Builder(GTPM.id("trona"))
                .localization(text -> text.source("Trona")
                        .translation(MinecraftLocale.ZH_CN, "天然碱"))
                .dust(1).ore(2, 1)
                .color(0xe6e6a5).secondaryColor(0x87875F).iconSet(METALLIC)
                .flags(DISABLE_DECOMPOSITION)
                .components(Sodium, 3, Carbon, 2, Hydrogen, 1, Water, 2, Oxygen, 6)
                .buildAndRegister();

        Gypsum = new Material.Builder(GTPM.id("gypsum"))
                .localization(text -> text.source("Gypsum")
                        .translation(MinecraftLocale.ZH_CN, "石膏"))
                .dust(1).ore()
                .color(0xfffaec).secondaryColor(0x71570a)
                .components(Calcium, 1, Sulfur, 1, Water, 2, Oxygen, 4)
                .buildAndRegister();

        Zeolite = new Material.Builder(GTPM.id("zeolite"))
                .localization(text -> text.source("Zeolite")
                        .translation(MinecraftLocale.ZH_CN, "沸石"))
                .dust().ore(3, 1)
                .color(0xf2e3e0).secondaryColor(0xeabeb4)
                .components(Sodium, 2, Aluminium, 2, Silicon, 3, Oxygen, 10, Water, 2)
                .buildAndRegister();

        Concrete = new Material.Builder(GTPM.id("concrete"))
                .localization(text -> text.source("Concrete")
                        .translation(MinecraftLocale.ZH_CN, "混凝土"))
                .dust()
                .liquid(new FluidBuilder().temperature(286))
                .color(0xfaf3e8).secondaryColor(0xbbbaba).iconSet(ROUGH)
                .flags(NO_SMASHING, EXCLUDE_BLOCK_CRAFTING_BY_HAND_RECIPES)
                .components(Stone, 1)
                .buildAndRegister();

        SteelMagnetic = new Material.Builder(GTPM.id("magnetic_steel"))
                .localization(text -> text.source("Magnetic Steel")
                        .translation(MinecraftLocale.ZH_CN, "磁化钢"))
                .ingot()
                .color(0xa7a7a7).secondaryColor(0x121c37).iconSet(MAGNETIC)
                .flags(GENERATE_ROD, IS_MAGNETIC, GENERATE_DENSE)
                .components(Steel, 1)
                .ingotSmeltInto(Steel)
                .arcSmeltInto(Steel)
                .macerateInto(Steel)
                .buildAndRegister();
        Steel.getProperty(PropertyKey.INGOT).setMagneticMaterial(SteelMagnetic);

        VanadiumSteel = new Material.Builder(GTPM.id("vanadium_steel"))
                .localization(text -> text.source("Vanadium Steel")
                        .translation(MinecraftLocale.ZH_CN, "钒钢"))
                .ingot(3)
                .liquid(new FluidBuilder().temperature(2073))
                .color(0xb59fcc).secondaryColor(0x19140d).iconSet(SHINY)
                .appendFlags(EXT2_METAL, GENERATE_FOIL, GENERATE_GEAR)
                .components(Vanadium, 1, Chromium, 1, Steel, 7)
                .toolStats(ToolProperty.Builder.of(3.0F, 3.0F, 1536, 3)
                        .attackSpeed(-0.2F).enchantability(5).build())
                .rotorStats(130, 115, 3.0f, 1920)
                .fluidPipeProperties(2073, 50, true, true, false, false)
                .blast(1453, GasTier.LOW)
                .buildAndRegister();

        Potin = new Material.Builder(GTPM.id("potin"))
                .localization(text -> text.source("Potin")
                        .translation(MinecraftLocale.ZH_CN, "粗青铜合金"))
                .ingot()
                .liquid(new FluidBuilder().temperature(1084))
                .color(0xaaada3).secondaryColor(0x5e3320).iconSet(METALLIC)
                .appendFlags(EXT2_METAL, GENERATE_GEAR)
                .components(Copper, 6, Tin, 2, Lead, 1)
                .fluidPipeProperties(1456, 40, true)
                .buildAndRegister();

        BorosilicateGlass = new Material.Builder(GTPM.id("borosilicate_glass"))
                .localization(text -> text.source("Borosilicate Glass")
                        .translation(MinecraftLocale.ZH_CN, "硼硅玻璃"))
                .ingot(1)
                .liquid(new FluidBuilder().temperature(1921))
                .color(0xFAFAFA).secondaryColor(0xfaf5c0).iconSet(SHINY)
                .flags(GENERATE_FINE_WIRE, GENERATE_PLATE)
                .components(Boron, 1, SiliconDioxide, 7)
                .buildAndRegister();

        Andesite = new Material.Builder(GTPM.id("andesite"))
                .localization(text -> text.source("Andesite")
                        .translation(MinecraftLocale.ZH_CN, "安山岩"))
                .dust()
                .color(0xa8aa9a).iconSet(ROUGH)
                .flags(DECOMPOSITION_BY_CENTRIFUGING)
                .components(Asbestos, 4, Saltpeter, 1)
                .removeHazard()
                .buildAndRegister();

        NaquadahAlloy = new Material.Builder(GTPM.id("naquadah_alloy"))
                .localization(text -> text.source("Naquadah Alloy")
                        .translation(MinecraftLocale.ZH_CN, "硅岩合金"))
                .ingot(5).fluid()
                .color(0x323232).secondaryColor(0x301131).iconSet(METALLIC)
                .appendFlags(EXT2_METAL, GENERATE_SPRING, GENERATE_RING, GENERATE_ROTOR, GENERATE_SMALL_GEAR,
                        GENERATE_FRAME, GENERATE_DENSE, GENERATE_FOIL, GENERATE_GEAR)
                .components(Naquadah, 2, Osmiridium, 1, Trinium, 1)
                .toolStats(ToolProperty.Builder.of(40.0F, 12.0F, 3072, 5)
                        .attackSpeed(0.3F).enchantability(33).magnetic().build())
                .rotorStats(190, 120, 5.0f, 5120)
                .cableProperties(V[UV], 2, 4)
                .blast(b -> b.temp(7200, GasTier.HIGH)
                        .blastStats(VA[LuV], 1000)
                        .vacuumStats(VA[IV], 300))
                .buildAndRegister();

        SulfuricNickelSolution = new Material.Builder(GTPM.id("sulfuric_nickel_solution"))
                .localization(text -> text.source("Sulfuric Nickel Solution")
                        .translation(MinecraftLocale.ZH_CN, "硫酸镍溶液"))
                .liquid(new FluidBuilder().attribute(FluidAttributes.ACID))
                .color(0x3EB640)
                .components(Nickel, 1, Oxygen, 1, SulfuricAcid, 1)
                .buildAndRegister();

        SulfuricCopperSolution = new Material.Builder(GTPM.id("sulfuric_copper_solution"))
                .localization(text -> text.source("Sulfuric Copper Solution")
                        .translation(MinecraftLocale.ZH_CN, "硫酸铜溶液"))
                .liquid(new FluidBuilder().attribute(FluidAttributes.ACID))
                .color(0x48A5C0)
                .components(Copper, 1, Oxygen, 1, SulfuricAcid, 1)
                .buildAndRegister();

        LeadZincSolution = new Material.Builder(GTPM.id("lead_zinc_solution"))
                .localization(text -> text.source("Lead Zinc Solution")
                        .translation(MinecraftLocale.ZH_CN, "铅锌溶液"))
                .liquid(new FluidBuilder().customStill())
                .flags(DECOMPOSITION_BY_CENTRIFUGING)
                .components(Lead, 1, Silver, 1, Zinc, 1, Sulfur, 3, Water, 1)
                .buildAndRegister();

        NitrationMixture = new Material.Builder(GTPM.id("nitration_mixture"))
                .localization(text -> text.source("Nitration Mixture")
                        .translation(MinecraftLocale.ZH_CN, "硝酸混酸"))
                .liquid(new FluidBuilder().attribute(FluidAttributes.ACID))
                .color(0xE6E2AB)
                .flags(DISABLE_DECOMPOSITION)
                .components(NitricAcid, 1, SulfuricAcid, 1)
                .buildAndRegister();

        DilutedSulfuricAcid = new Material.Builder(GTPM.id("diluted_sulfuric_acid"))
                .localization(text -> text.source("Diluted Sulfuric Acid")
                        .translation(MinecraftLocale.ZH_CN, "稀硫酸"))
                .liquid(new FluidBuilder().attribute(FluidAttributes.ACID))
                .color(0xC07820)
                .flags(DISABLE_DECOMPOSITION)
                .components(SulfuricAcid, 2, Water, 1)
                .buildAndRegister();

        DilutedHydrochloricAcid = new Material.Builder(GTPM.id("diluted_hydrochloric_acid"))
                .localization(text -> text.source("Diluted Hydrochloric Acid")
                        .translation(MinecraftLocale.ZH_CN, "稀盐酸"))
                .liquid(new FluidBuilder().attribute(FluidAttributes.ACID))
                .color(0x99A7A3)
                .flags(DISABLE_DECOMPOSITION)
                .components(HydrochloricAcid, 1, Water, 1)
                .buildAndRegister();

        Flint = new Material.Builder(GTPM.id("flint"))
                .localization(text -> text.source("Flint")
                        .translation(MinecraftLocale.ZH_CN, "燧石"))
                .gem(1)
                .color(0xc7c7c7).secondaryColor(0x212121).iconSet(FLINT)
                .flags(NO_SMASHING, MORTAR_GRINDABLE, DECOMPOSITION_BY_CENTRIFUGING)
                .components(SiliconDioxide, 1)
                .toolStats(ToolProperty.Builder.of(0.0F, 1.0F, 64, 1)
                        .types(GTToolType.MORTAR, GTToolType.KNIFE, GTToolType.AXE, GTToolType.PICKAXE, GTToolType.HOE,
                                GTToolType.SWORD, GTToolType.SHOVEL)
                        .enchantability(5).ignoreCraftingTools()
                        .enchantment(Enchantments.FIRE_ASPECT, 1).build())
                .buildAndRegister();

        Air = new Material.Builder(GTPM.id("air"))
                .localization(text -> text.source("Air")
                        .translation(MinecraftLocale.ZH_CN, "空气"))
                .gas(new FluidBuilder().customStill())
                .color(0xA9D0F5)
                .flags(DISABLE_DECOMPOSITION)
                .components(Nitrogen, 78, Oxygen, 21, Argon, 9)
                .buildAndRegister();

        LiquidAir = new Material.Builder(GTPM.id("liquid_air"))
                .localization(text -> text.source("Liquid Air")
                        .translation(MinecraftLocale.ZH_CN, "液态空气"))
                .liquid(new FluidBuilder().temperature(97))
                .color(0xA9D0F5)
                .flags(DISABLE_DECOMPOSITION)
                .components(Nitrogen, 70, Oxygen, 22, CarbonDioxide, 5, Helium, 2, Argon, 1, Ice, 1)
                .buildAndRegister();

        NetherAir = new Material.Builder(GTPM.id("nether_air"))
                .localization(text -> text.source("Nether Air")
                        .translation(MinecraftLocale.ZH_CN, "下界空气"))
                .gas()
                .color(0x4C3434)
                .flags(DISABLE_DECOMPOSITION)
                .components(CarbonMonoxide, 78, HydrogenSulfide, 21, Neon, 9)
                .hazard(HazardProperty.HazardTrigger.INHALATION, GTMedicalConditions.CHEMICAL_BURNS)
                .buildAndRegister();

        LiquidNetherAir = new Material.Builder(GTPM.id("liquid_nether_air"))
                .localization(text -> text.source("Liquid Nether Air")
                        .translation(MinecraftLocale.ZH_CN, "液态下界空气"))
                .liquid(new FluidBuilder().temperature(58))
                .color(0x4C3434)
                .flags(DISABLE_DECOMPOSITION)
                .components(CarbonMonoxide, 144, CoalGas, 20, HydrogenSulfide, 15, SulfurDioxide, 15, Helium3, 5, Neon,
                        1, Ash, 1)
                .buildAndRegister();

        EnderAir = new Material.Builder(GTPM.id("ender_air"))
                .localization(text -> text.source("Ender Air")
                        .translation(MinecraftLocale.ZH_CN, "末地空气"))
                .gas()
                .color(0x283454)
                .flags(DISABLE_DECOMPOSITION)
                .components(NitrogenDioxide, 78, Deuterium, 21, Xenon, 9)
                .buildAndRegister();

        LiquidEnderAir = new Material.Builder(GTPM.id("liquid_ender_air"))
                .localization(text -> text.source("Liquid Ender Air")
                        .translation(MinecraftLocale.ZH_CN, "液态末地空气"))
                .liquid(new FluidBuilder().temperature(36))
                .color(0x283454)
                .flags(DISABLE_DECOMPOSITION)
                .components(NitrogenDioxide, 122, Deuterium, 50, Helium, 15, Tritium, 10, Krypton, 1, Xenon, 1, Radon,
                        1, EnderPearl, 1)
                .buildAndRegister();

        AquaRegia = new Material.Builder(GTPM.id("aqua_regia"))
                .localization(text -> text.source("Aqua Regia")
                        .translation(MinecraftLocale.ZH_CN, "王水"))
                .liquid(new FluidBuilder().attribute(FluidAttributes.ACID))
                .color(0xFFB132)
                .flags(DISABLE_DECOMPOSITION)
                .components(NitricAcid, 1, HydrochloricAcid, 2)
                .buildAndRegister();

        PlatinumSludgeResidue = new Material.Builder(GTPM.id("platinum_sludge_residue"))
                .localization(text -> text.source("Platinum Sludge Residue")
                        .translation(MinecraftLocale.ZH_CN, "铂泥渣"))
                .dust()
                .color(0x5e4b40).secondaryColor(0x4b403d).iconSet(FINE)
                .flags(DECOMPOSITION_BY_CENTRIFUGING)
                .components(SiliconDioxide, 2, Gold, 3)
                .buildAndRegister();

        PalladiumRaw = new Material.Builder(GTPM.id("palladium_raw"))
                .localization(text -> text.source("Palladium Raw")
                        .translation(MinecraftLocale.ZH_CN, "粗钯"))
                .dust()
                .color(0x5d4e1a).secondaryColor(0x33352d).iconSet(METALLIC)
                .flags(DISABLE_DECOMPOSITION)
                .components(Palladium, 1, Ammonia, 1)
                .buildAndRegister();

        RarestMetalMixture = new Material.Builder(GTPM.id("rarest_metal_mixture"))
                .localization(text -> text.source("Rarest Metal Mixture")
                        .translation(MinecraftLocale.ZH_CN, "稀有金属混合物"))
                .dust()
                .color(0xca8832).secondaryColor(0xb21900).iconSet(SHINY)
                .flags(DISABLE_DECOMPOSITION)
                .components(Iridium, 1, Osmium, 1, Oxygen, 4, Water, 1)
                .buildAndRegister();

        AmmoniumChloride = new Material.Builder(GTPM.id("ammonium_chloride"))
                .localization(text -> text.source("Ammonium Chloride")
                        .translation(MinecraftLocale.ZH_CN, "氯化铵"))
                .dust()
                .color(0x60a1c5).secondaryColor(0x48619c)
                .components(Ammonia, 1, HydrochloricAcid, 1)
                .buildAndRegister()
                .setFormula("NH4Cl", true);

        AcidicOsmiumSolution = new Material.Builder(GTPM.id("acidic_osmium_solution"))
                .localization(text -> text.source("Acidic Osmium Solution")
                        .translation(MinecraftLocale.ZH_CN, "酸性锇溶液"))
                .liquid(new FluidBuilder().attribute(FluidAttributes.ACID))
                .color(0xDAC5C5)
                .flags(DISABLE_DECOMPOSITION)
                .components(Osmium, 1, Oxygen, 4, Water, 1, HydrochloricAcid, 1)
                .buildAndRegister();

        RhodiumPlatedPalladium = new Material.Builder(GTPM.id("rhodium_plated_palladium"))
                .localization(text -> text.source("Rhodium Plated Palladium")
                        .translation(MinecraftLocale.ZH_CN, "镀铑钯"))
                .ingot().fluid()
                .color(0xd1d1d1).secondaryColor(0x000000).iconSet(SHINY)
                .appendFlags(EXT2_METAL, GENERATE_ROTOR, GENERATE_DENSE, GENERATE_SMALL_GEAR)
                .components(Palladium, 3, Rhodium, 1)
                .rotorStats(130, 155, 3.0f, 1024)
                .blast(b -> b.temp(4500, GasTier.HIGH)
                        .blastStats(VA[IV], 1200)
                        .vacuumStats(VA[EV], 300))
                .buildAndRegister();

        Clay = new Material.Builder(GTPM.id("clay"))
                .localization(text -> text.source("Clay")
                        .translation(MinecraftLocale.ZH_CN, "黏土"))
                .dust(1)
                .color(0xbec9e8).secondaryColor(0x373944).iconSet(ROUGH)
                .flags(MORTAR_GRINDABLE, EXCLUDE_BLOCK_CRAFTING_BY_HAND_RECIPES)
                .components(Sodium, 2, Lithium, 1, Aluminium, 2, Silicon, 2, Water, 6)
                .buildAndRegister();

        Redstone = new Material.Builder(GTPM.id("redstone"))
                .localization(text -> text.source("Redstone")
                        .translation(MinecraftLocale.ZH_CN, "红石"))
                .dust().ore(5, 1, true)
                .liquid(new FluidBuilder().temperature(500))
                .color(0xff0000).secondaryColor(0x340605).iconSet(ROUGH)
                .flags(GENERATE_PLATE, NO_SMASHING, NO_SMELTING, EXCLUDE_BLOCK_CRAFTING_BY_HAND_RECIPES,
                        EXCLUDE_PLATE_COMPRESSOR_RECIPE, DECOMPOSITION_BY_CENTRIFUGING)
                .components(Silicon, 1, Pyrite, 5, Ruby, 1, Mercury, 3)
                .removeHazard()
                .buildAndRegister();

        Dichloroethane = new Material.Builder(GTPM.id("dichloroethane"))
                .localization(text -> text.source("Dichloroethane")
                        .translation(MinecraftLocale.ZH_CN, "二氯乙烷"))
                .liquid()
                .color(0xafc979)
                .flags(DECOMPOSITION_BY_ELECTROLYZING)
                .components(Carbon, 2, Hydrogen, 4, Chlorine, 2)
                .buildAndRegister();

        Diethylenetriamine = new Material.Builder(GTPM.id("diethylenetriamine"))
                .localization(text -> text.source("Diethylenetriamine")
                        .translation(MinecraftLocale.ZH_CN, "二乙烯三胺"))
                .liquid()
                .color(0xa9d9a7)
                .flags(DISABLE_DECOMPOSITION)
                .components(Carbon, 4, Hydrogen, 13, Nitrogen, 3)
                .hazard(HazardProperty.HazardTrigger.ANY, GTMedicalConditions.CHEMICAL_BURNS)
                .buildAndRegister();

        Tuff = new Material.Builder(GTPM.id("tuff"))
                .localization(text -> text.source("Tuff")
                        .translation(MinecraftLocale.ZH_CN, "凝灰岩"))
                .dust()
                .color(0x75756a).secondaryColor(0x8a8a80).iconSet(ROUGH)
                .flags(NO_SMASHING)
                .components(Ash, 2, PotassiumFeldspar, 1)
                .buildAndRegister();
    }
}
