package com.modularmc.gtpm.common.data.materials;

import com.modularmc.gtpm.GTPM;
import com.modularmc.gtpm.api.data.chemical.material.Material;
import com.modularmc.gtpm.api.data.chemical.material.properties.HazardProperty;
import com.modularmc.gtpm.api.data.chemical.material.properties.ToolProperty;
import com.modularmc.gtpm.api.fluids.FluidBuilder;
import com.modularmc.gtpm.api.fluids.attribute.FluidAttributes;
import com.modularmc.gtpm.api.item.tool.GTToolType;
import com.modularmc.gtpm.common.data.GTMedicalConditions;

import com.modularmc.registrate.localization.MinecraftLocale;

import static com.modularmc.gtpm.api.data.chemical.material.info.MaterialFlags.*;
import static com.modularmc.gtpm.api.data.chemical.material.info.MaterialIconSet.FINE;
import static com.modularmc.gtpm.api.data.chemical.material.info.MaterialIconSet.ROUGH;
import static com.modularmc.gtpm.common.data.GTMaterials.*;

public class OrganicChemistryMaterials {

    /**
     * ID RANGE: 1000-1068 (incl.)
     */
    public static void register() {
        SiliconeRubber = new Material.Builder(GTPM.id("silicone_rubber"))
                .localization(text -> text.source("Silicone Rubber")
                        .translation(MinecraftLocale.ZH_CN, "硅橡胶"))
                .polymer()
                .liquid(new FluidBuilder().temperature(900))
                .toolStats(
                        ToolProperty.Builder.of(1.0F, 1.0F, 512, 1, GTToolType.SOFT_MALLET, GTToolType.PLUNGER).build())
                .color(0xF0F0F0).secondaryColor(0xE8E8E0)
                .flags(GENERATE_GEAR, GENERATE_RING, GENERATE_FOIL)
                .components(Carbon, 2, Hydrogen, 6, Oxygen, 1, Silicon, 1)
                .buildAndRegister();

        Nitrobenzene = new Material.Builder(GTPM.id("nitrobenzene"))
                .localization(text -> text.source("Nitrobenzene")
                        .translation(MinecraftLocale.ZH_CN, "硝基苯"))
                .gas()
                .color(0x704936)
                .flags(DISABLE_DECOMPOSITION)
                .components(Carbon, 6, Hydrogen, 5, Nitrogen, 1, Oxygen, 2)
                .hazard(HazardProperty.HazardTrigger.INHALATION, GTMedicalConditions.CARCINOGEN)
                .buildAndRegister();

        RawRubber = new Material.Builder(GTPM.id("raw_rubber"))
                .localization(text -> text.source("Raw Rubber")
                        .translation(MinecraftLocale.ZH_CN, "生橡胶"))
                .polymer()
                .color(0x54503D).secondaryColor(0x54403D)
                .components(Carbon, 5, Hydrogen, 8)
                .buildAndRegister();

        RawStyreneButadieneRubber = new Material.Builder(GTPM.id("raw_styrene_butadiene_rubber"))
                .localization(text -> text.source("Raw Styrene Butadiene Rubber")
                        .translation(MinecraftLocale.ZH_CN, "生丁苯橡胶"))
                .dust()
                .color(0x54403D).secondaryColor(0x241520)
                .flags(DISABLE_DECOMPOSITION, FLAMMABLE)
                .components(Carbon, 20, Hydrogen, 26)
                .buildAndRegister()
                .setFormula("(C4H6)3C8H8", true);

        StyreneButadieneRubber = new Material.Builder(GTPM.id("styrene_butadiene_rubber"))
                .localization(text -> text.source("Styrene Butadiene Rubber")
                        .translation(MinecraftLocale.ZH_CN, "丁苯橡胶"))
                .polymer()
                .liquid(new FluidBuilder().temperature(1000))
                .toolStats(
                        ToolProperty.Builder.of(1.0F, 1.0F, 512, 1, GTToolType.SOFT_MALLET, GTToolType.PLUNGER).build())
                .color(0x34312b).secondaryColor(0x110B09)
                .flags(GENERATE_FOIL, GENERATE_RING)
                .components(Carbon, 20, Hydrogen, 26)
                .buildAndRegister()
                .setFormula("(C4H6)3C8H8", true);

        PolyvinylAcetate = new Material.Builder(GTPM.id("polyvinyl_acetate"))
                .localization(text -> text.source("Polyvinyl Acetate")
                        .translation(MinecraftLocale.ZH_CN, "聚乙酸乙烯酯"))
                .fluid()
                .color(0xFF9955)
                .flags(DISABLE_DECOMPOSITION)
                .components(Carbon, 4, Hydrogen, 6, Oxygen, 2)
                .buildAndRegister();

        ReinforcedEpoxyResin = new Material.Builder(GTPM.id("reinforced_epoxy_resin"))
                .localization(text -> text.source("Reinforced Epoxy Resin")
                        .translation(MinecraftLocale.ZH_CN, "强化环氧树脂"))
                .polymer()
                .liquid(new FluidBuilder().temperature(600))
                .color(0x9ecaad).secondaryColor(0xb1b2a1).iconSet(ROUGH)
                .appendFlags(STD_METAL)
                .components(Carbon, 6, Hydrogen, 4, Oxygen, 1)
                .buildAndRegister();

        PolyvinylChloride = new Material.Builder(GTPM.id("polyvinyl_chloride"))
                .localization(text -> text.source("Polyvinyl Chloride")
                        .translation(MinecraftLocale.ZH_CN, "聚氯乙烯"))
                .polymer()
                .liquid(new FluidBuilder().temperature(373))
                .color(0xFF9955).secondaryColor(0x6ca5bf)
                .appendFlags(EXT_METAL, GENERATE_FOIL, GENERATE_RING)
                .components(Carbon, 2, Hydrogen, 3, Chlorine, 1)
                .itemPipeProperties(512, 4)
                .buildAndRegister();

        PolyphenyleneSulfide = new Material.Builder(GTPM.id("polyphenylene_sulfide"))
                .localization(text -> text.source("Polyphenylene Sulfide")
                        .translation(MinecraftLocale.ZH_CN, "聚苯硫醚"))
                .polymer()
                .liquid(new FluidBuilder().temperature(500))
                .color(0x5e5e08).secondaryColor(0x2c373c)
                .appendFlags(EXT_METAL, GENERATE_FOIL)
                .components(Carbon, 6, Hydrogen, 4, Sulfur, 1)
                .buildAndRegister();

        GlycerylTrinitrate = new Material.Builder(GTPM.id("glyceryl_trinitrate"))
                .localization(text -> text.source("Glyceryl Trinitrate")
                        .translation(MinecraftLocale.ZH_CN, "硝酸甘油"))
                .liquid(new FluidBuilder().customStill())
                .flags(FLAMMABLE, EXPLOSIVE)
                .components(Carbon, 3, Hydrogen, 5, Nitrogen, 3, Oxygen, 9)
                .buildAndRegister();

        Polybenzimidazole = new Material.Builder(GTPM.id("polybenzimidazole"))
                .localization(text -> text.source("Polybenzimidazole")
                        .translation(MinecraftLocale.ZH_CN, "聚苯并咪唑"))
                .polymer()
                .liquid(new FluidBuilder().temperature(1450))
                .color(0x464441).secondaryColor(0x382e1b)
                .flags(GENERATE_FOIL)
                .toolStats(
                        ToolProperty.Builder.of(1.0F, 1.0F, 1024, 1, GTToolType.SOFT_MALLET, GTToolType.PLUNGER)
                                .build())
                .components(Carbon, 20, Hydrogen, 12, Nitrogen, 4)
                .fluidPipeProperties(1000, 350, true)
                .buildAndRegister();

        Polydimethylsiloxane = new Material.Builder(GTPM.id("polydimethylsiloxane"))
                .localization(text -> text.source("Polydimethylsiloxane")
                        .translation(MinecraftLocale.ZH_CN, "聚二甲基硅氧烷"))
                .dust()
                .color(0xF5F5F5).secondaryColor(0x9d9fa1)
                .flags(DISABLE_DECOMPOSITION, FLAMMABLE)
                .components(Carbon, 2, Hydrogen, 6, Oxygen, 1, Silicon, 1)
                .buildAndRegister();

        Polyethylene = new Material.Builder(GTPM.id("polyethylene"))
                .localization(text -> text.source("Polyethylene")
                        .translation(MinecraftLocale.ZH_CN, "聚乙烯"))
                .polymer(1)
                .liquid(new FluidBuilder().temperature(408))
                .color(0xC8C8C8)
                .flags(GENERATE_FOIL)
                .toolStats(
                        ToolProperty.Builder.of(1.0F, 1.0F, 256, 1, GTToolType.SOFT_MALLET, GTToolType.PLUNGER).build())
                .components(Carbon, 2, Hydrogen, 4)
                .fluidPipeProperties(370, 60, true)
                .buildAndRegister();

        Epoxy = new Material.Builder(GTPM.id("epoxy"))
                .localization(text -> text.source("Epoxy")
                        .translation(MinecraftLocale.ZH_CN, "环氧树脂"))
                .polymer(1)
                .liquid(new FluidBuilder().temperature(400))
                .color(0xf6fabd).secondaryColor(0xC88C14).iconSet(ROUGH)
                .appendFlags(STD_METAL)
                .components(Carbon, 21, Hydrogen, 25, Chlorine, 1, Oxygen, 5)
                .buildAndRegister();

        Polycaprolactam = new Material.Builder(GTPM.id("polycaprolactam"))
                .localization(text -> text.source("Polycaprolactam")
                        .translation(MinecraftLocale.ZH_CN, "聚己内酰胺"))
                .polymer(1)
                .liquid(new FluidBuilder().temperature(493))
                .color(0x3f3d2d).secondaryColor(0x43432e)
                .appendFlags(STD_METAL, GENERATE_FOIL)
                .components(Carbon, 6, Hydrogen, 11, Nitrogen, 1, Oxygen, 1)
                .buildAndRegister();

        Polytetrafluoroethylene = new Material.Builder(GTPM.id("polytetrafluoroethylene"))
                .localization(text -> text.source("Polytetrafluoroethylene")
                        .translation(MinecraftLocale.ZH_CN, "聚四氟乙烯"))
                .polymer(1)
                .liquid(new FluidBuilder().temperature(600))
                .color(0x6e6e6e).secondaryColor(0x202020)
                .appendFlags(STD_METAL, GENERATE_FRAME, GENERATE_FOIL)
                .toolStats(
                        ToolProperty.Builder.of(1.0F, 1.0F, 512, 1, GTToolType.SOFT_MALLET, GTToolType.PLUNGER).build())
                .components(Carbon, 2, Fluorine, 4)
                .fluidPipeProperties(600, 100, true, true, false, false)
                .buildAndRegister();

        Sugar = new Material.Builder(GTPM.id("sugar"))
                .localization(text -> text.source("Sugar")
                        .translation(MinecraftLocale.ZH_CN, "糖"))
                .gem(1)
                .color(0xFFFFFF).secondaryColor(0x545468).iconSet(FINE)
                .flags(DISABLE_DECOMPOSITION)
                .components(Carbon, 6, Hydrogen, 12, Oxygen, 6)
                .buildAndRegister();

        Methane = new Material.Builder(GTPM.id("methane"))
                .localization(text -> text.source("Methane")
                        .translation(MinecraftLocale.ZH_CN, "甲烷"))
                .gas(new FluidBuilder()
                        .translation("gtpm.fluid.gas_generic"))
                .color(0xFF0078)
                .components(Carbon, 1, Hydrogen, 4)
                .buildAndRegister();

        Epichlorohydrin = new Material.Builder(GTPM.id("epichlorohydrin"))
                .localization(text -> text.source("Epichlorohydrin")
                        .translation(MinecraftLocale.ZH_CN, "环氧氯丙烷"))
                .liquid(new FluidBuilder().customStill())
                .color(0x712400)
                .components(Carbon, 3, Hydrogen, 5, Chlorine, 1, Oxygen, 1)
                .buildAndRegister();

        Monochloramine = new Material.Builder(GTPM.id("monochloramine"))
                .localization(text -> text.source("Monochloramine")
                        .translation(MinecraftLocale.ZH_CN, "氯胺"))
                .gas()
                .color(0x3F9F80)
                .components(Nitrogen, 1, Hydrogen, 2, Chlorine, 1)
                .buildAndRegister();

        Chloroform = new Material.Builder(GTPM.id("chloroform"))
                .localization(text -> text.source("Chloroform")
                        .translation(MinecraftLocale.ZH_CN, "氯仿"))
                .fluid()
                .color(0x892CA0)
                .components(Carbon, 1, Hydrogen, 1, Chlorine, 3)
                .hazard(HazardProperty.HazardTrigger.INHALATION, GTMedicalConditions.POISON)
                .buildAndRegister();

        Cumene = new Material.Builder(GTPM.id("cumene"))
                .localization(text -> text.source("Cumene")
                        .translation(MinecraftLocale.ZH_CN, "异丙苯"))
                .gas()
                .color(0x552200)
                .flags(DISABLE_DECOMPOSITION)
                .components(Carbon, 9, Hydrogen, 12)
                .buildAndRegister();

        Tetrafluoroethylene = new Material.Builder(GTPM.id("tetrafluoroethylene"))
                .localization(text -> text.source("Tetrafluoroethylene")
                        .translation(MinecraftLocale.ZH_CN, "四氟乙烯"))
                .gas()
                .color(0x7D7D7D)
                .flags(DISABLE_DECOMPOSITION)
                .components(Carbon, 2, Fluorine, 4)
                .buildAndRegister();

        Chloromethane = new Material.Builder(GTPM.id("chloromethane"))
                .localization(text -> text.source("Chloromethane")
                        .translation(MinecraftLocale.ZH_CN, "氯甲烷"))
                .gas()
                .color(0xC82CA0)
                .flags(DISABLE_DECOMPOSITION)
                .components(Carbon, 1, Hydrogen, 3, Chlorine, 1)
                .buildAndRegister();

        AllylChloride = new Material.Builder(GTPM.id("allyl_chloride"))
                .localization(text -> text.source("Allyl Chloride")
                        .translation(MinecraftLocale.ZH_CN, "烯丙基氯"))
                .fluid()
                .color(0x87DEAA)
                .components(Carbon, 2, Methane, 1, HydrochloricAcid, 1)
                .buildAndRegister()
                .setFormula("C3H5Cl", true);

        Isoprene = new Material.Builder(GTPM.id("isoprene"))
                .localization(text -> text.source("Isoprene")
                        .translation(MinecraftLocale.ZH_CN, "异戊二烯"))
                .fluid()
                .color(0x141414)
                .components(Carbon, 5, Hydrogen, 8)
                .buildAndRegister();

        Propane = new Material.Builder(GTPM.id("propane"))
                .localization(text -> text.source("Propane")
                        .translation(MinecraftLocale.ZH_CN, "丙烷"))
                .gas()
                .color(0xFAE250)
                .flags(DISABLE_DECOMPOSITION)
                .components(Carbon, 3, Hydrogen, 8)
                .buildAndRegister();

        Propene = new Material.Builder(GTPM.id("propene"))
                .localization(text -> text.source("Propene")
                        .translation(MinecraftLocale.ZH_CN, "丙烯"))
                .gas()
                .color(0xFFDD55)
                .components(Carbon, 3, Hydrogen, 6)
                .buildAndRegister();

        Ethane = new Material.Builder(GTPM.id("ethane"))
                .localization(text -> text.source("Ethane")
                        .translation(MinecraftLocale.ZH_CN, "乙烷"))
                .gas()
                .color(0xC8C8FF)
                .components(Carbon, 2, Hydrogen, 6)
                .buildAndRegister();

        Butene = new Material.Builder(GTPM.id("butene"))
                .localization(text -> text.source("Butene")
                        .translation(MinecraftLocale.ZH_CN, "丁烯"))
                .gas()
                .color(0xCF5005)
                .flags(DISABLE_DECOMPOSITION)
                .components(Carbon, 4, Hydrogen, 8)
                .buildAndRegister();

        Butane = new Material.Builder(GTPM.id("butane"))
                .localization(text -> text.source("Butane")
                        .translation(MinecraftLocale.ZH_CN, "丁烷"))
                .gas()
                .color(0xB6371E)
                .flags(DISABLE_DECOMPOSITION)
                .components(Carbon, 4, Hydrogen, 10)
                .buildAndRegister();

        DissolvedCalciumAcetate = new Material.Builder(GTPM.id("dissolved_calcium_acetate"))
                .localization(text -> text.source("Dissolved Calcium Acetate")
                        .translation(MinecraftLocale.ZH_CN, "乙酸钙溶液"))
                .fluid()
                .color(0xDCC8B4)
                .flags(DISABLE_DECOMPOSITION)
                .components(Calcium, 1, Carbon, 4, Oxygen, 4, Hydrogen, 6, Water, 1)
                .buildAndRegister();

        VinylAcetate = new Material.Builder(GTPM.id("vinyl_acetate"))
                .localization(text -> text.source("Vinyl Acetate")
                        .translation(MinecraftLocale.ZH_CN, "乙酸乙烯酯"))
                .fluid()
                .color(0xE1B380)
                .flags(DISABLE_DECOMPOSITION)
                .components(Carbon, 4, Hydrogen, 6, Oxygen, 2)
                .buildAndRegister();

        MethylAcetate = new Material.Builder(GTPM.id("methyl_acetate"))
                .localization(text -> text.source("Methyl Acetate")
                        .translation(MinecraftLocale.ZH_CN, "乙酸甲酯"))
                .fluid()
                .color(0xEEC6AF)
                .flags(DISABLE_DECOMPOSITION)
                .components(Carbon, 3, Hydrogen, 6, Oxygen, 2)
                .buildAndRegister();

        Ethenone = new Material.Builder(GTPM.id("ethenone"))
                .localization(text -> text.source("Ethenone")
                        .translation(MinecraftLocale.ZH_CN, "乙烯酮"))
                .fluid()
                .color(0x141446)
                .flags(DISABLE_DECOMPOSITION)
                .components(Carbon, 2, Hydrogen, 2, Oxygen, 1)
                .buildAndRegister();

        Tetranitromethane = new Material.Builder(GTPM.id("tetranitromethane"))
                .localization(text -> text.source("Tetranitromethane")
                        .translation(MinecraftLocale.ZH_CN, "四硝基甲烷"))
                .fluid()
                .color(0x0F2828)
                .flags(DISABLE_DECOMPOSITION)
                .components(Carbon, 1, Nitrogen, 4, Oxygen, 8)
                .hazard(HazardProperty.HazardTrigger.INHALATION, GTMedicalConditions.WEAK_POISON)
                .buildAndRegister();

        Dimethylamine = new Material.Builder(GTPM.id("dimethylamine"))
                .localization(text -> text.source("Dimethylamine")
                        .translation(MinecraftLocale.ZH_CN, "二甲胺"))
                .gas()
                .color(0x554469)
                .flags(DISABLE_DECOMPOSITION)
                .components(Carbon, 2, Hydrogen, 7, Nitrogen, 1)
                .buildAndRegister();

        Dimethylhydrazine = new Material.Builder(GTPM.id("dimethylhydrazine"))
                .localization(text -> text.source("Dimethylhydrazine")
                        .translation(MinecraftLocale.ZH_CN, "二甲基肼"))
                .fluid()
                .color(0x000055)
                .flags(DISABLE_DECOMPOSITION)
                .components(Carbon, 2, Hydrogen, 8, Nitrogen, 2)
                .hazard(HazardProperty.HazardTrigger.INHALATION, GTMedicalConditions.CARCINOGEN)
                .buildAndRegister();

        DinitrogenTetroxide = new Material.Builder(GTPM.id("dinitrogen_tetroxide"))
                .localization(text -> text.source("Dinitrogen Tetroxide")
                        .translation(MinecraftLocale.ZH_CN, "四氧化二氮"))
                .gas()
                .color(0x004184)
                .components(Nitrogen, 2, Oxygen, 4)
                .buildAndRegister();

        Dimethyldichlorosilane = new Material.Builder(GTPM.id("dimethyldichlorosilane"))
                .localization(text -> text.source("Dimethyldichlorosilane")
                        .translation(MinecraftLocale.ZH_CN, "二甲基二氯硅烷"))
                .gas()
                .color(0x441650)
                .flags(DISABLE_DECOMPOSITION)
                .components(Carbon, 2, Hydrogen, 6, Chlorine, 2, Silicon, 1)
                .buildAndRegister();

        Styrene = new Material.Builder(GTPM.id("styrene"))
                .localization(text -> text.source("Styrene")
                        .translation(MinecraftLocale.ZH_CN, "苯乙烯"))
                .fluid()
                .color(0xD2C8BE)
                .flags(DISABLE_DECOMPOSITION)
                .components(Carbon, 8, Hydrogen, 8)
                .buildAndRegister();

        Butadiene = new Material.Builder(GTPM.id("butadiene"))
                .localization(text -> text.source("Butadiene")
                        .translation(MinecraftLocale.ZH_CN, "丁二烯"))
                .gas()
                .color(0xB55A10)
                .flags(DISABLE_DECOMPOSITION)
                .components(Carbon, 4, Hydrogen, 6)
                .buildAndRegister();

        Dichlorobenzene = new Material.Builder(GTPM.id("dichlorobenzene"))
                .localization(text -> text.source("Dichlorobenzene")
                        .translation(MinecraftLocale.ZH_CN, "二氯苯"))
                .fluid()
                .color(0x004455)
                .flags(DISABLE_DECOMPOSITION)
                .components(Carbon, 6, Hydrogen, 4, Chlorine, 2)
                .buildAndRegister();

        AceticAcid = new Material.Builder(GTPM.id("acetic_acid"))
                .localization(text -> text.source("Acetic Acid")
                        .translation(MinecraftLocale.ZH_CN, "乙酸"))
                .liquid(new FluidBuilder().attribute(FluidAttributes.ACID))
                .color(0xC8B4A0)
                .flags(DISABLE_DECOMPOSITION)
                .components(Carbon, 2, Hydrogen, 4, Oxygen, 2)
                .buildAndRegister();

        Phenol = new Material.Builder(GTPM.id("phenol"))
                .localization(text -> text.source("Phenol")
                        .translation(MinecraftLocale.ZH_CN, "苯酚"))
                .fluid()
                .color(0x784421)
                .flags(DISABLE_DECOMPOSITION)
                .components(Carbon, 6, Hydrogen, 6, Oxygen, 1)
                .hazard(HazardProperty.HazardTrigger.INHALATION, GTMedicalConditions.CARCINOGEN)
                .buildAndRegister();

        BisphenolA = new Material.Builder(GTPM.id("bisphenol_a"))
                .localization(text -> text.source("Bisphenol A")
                        .translation(MinecraftLocale.ZH_CN, "双酚A"))
                .fluid()
                .color(0xD4AA00)
                .flags(DISABLE_DECOMPOSITION)
                .components(Carbon, 15, Hydrogen, 16, Oxygen, 2)
                .buildAndRegister();

        VinylChloride = new Material.Builder(GTPM.id("vinyl_chloride"))
                .localization(text -> text.source("Vinyl Chloride")
                        .translation(MinecraftLocale.ZH_CN, "氯乙烯"))
                .gas()
                .color(0xE1F0F0)
                .flags(DISABLE_DECOMPOSITION)
                .components(Carbon, 2, Hydrogen, 3, Chlorine, 1)
                .hazard(HazardProperty.HazardTrigger.INHALATION, GTMedicalConditions.CARCINOGEN)
                .buildAndRegister();

        Ethylene = new Material.Builder(GTPM.id("ethylene"))
                .localization(text -> text.source("Ethylene")
                        .translation(MinecraftLocale.ZH_CN, "乙烯"))
                .gas()
                .color(0xE1E1E1)
                .flags(DISABLE_DECOMPOSITION)
                .components(Carbon, 2, Hydrogen, 4)
                .buildAndRegister();

        Benzene = new Material.Builder(GTPM.id("benzene"))
                .localization(text -> text.source("Benzene")
                        .translation(MinecraftLocale.ZH_CN, "苯"))
                .fluid()
                .color(0x1A1A1A)
                .flags(DISABLE_DECOMPOSITION)
                .components(Carbon, 6, Hydrogen, 6)
                .hazard(HazardProperty.HazardTrigger.INHALATION, GTMedicalConditions.CARCINOGEN)
                .buildAndRegister();

        Acetone = new Material.Builder(GTPM.id("acetone"))
                .localization(text -> text.source("Acetone")
                        .translation(MinecraftLocale.ZH_CN, "丙酮"))
                .fluid()
                .color(0xAFAFAF)
                .flags(DISABLE_DECOMPOSITION)
                .components(Carbon, 3, Hydrogen, 6, Oxygen, 1)
                .buildAndRegister();

        Glycerol = new Material.Builder(GTPM.id("glycerol"))
                .localization(text -> text.source("Glycerol")
                        .translation(MinecraftLocale.ZH_CN, "甘油"))
                .fluid()
                .color(0x87DE87)
                .components(Carbon, 3, Hydrogen, 8, Oxygen, 3)
                .buildAndRegister();

        Methanol = new Material.Builder(GTPM.id("methanol"))
                .localization(text -> text.source("Methanol")
                        .translation(MinecraftLocale.ZH_CN, "甲醇"))
                .fluid()
                .color(0xAA8800)
                .components(Carbon, 1, Hydrogen, 4, Oxygen, 1)
                .hazard(HazardProperty.HazardTrigger.INHALATION, GTMedicalConditions.METHANOL_POISONING)
                .buildAndRegister();

        Ethanol = new Material.Builder(GTPM.id("ethanol"))
                .localization(text -> text.source("Ethanol")
                        .translation(MinecraftLocale.ZH_CN, "乙醇"))
                .liquid(new FluidBuilder().customStill())
                .components(Carbon, 2, Hydrogen, 6, Oxygen, 1)
                .flags(DISABLE_DECOMPOSITION)
                // TODO ethanol intoxication .hazard(HazardProperty.HazardTrigger.INHALATION,
                .buildAndRegister();

        Toluene = new Material.Builder(GTPM.id("toluene"))
                .localization(text -> text.source("Toluene")
                        .translation(MinecraftLocale.ZH_CN, "甲苯"))
                .liquid(new FluidBuilder().customStill())
                .flags(DISABLE_DECOMPOSITION)
                .components(Carbon, 7, Hydrogen, 8)
                .buildAndRegister();

        DiphenylIsophtalate = new Material.Builder(GTPM.id("diphenyl_isophthalate"))
                .localization(text -> text.source("Diphenyl Isophthalate")
                        .translation(MinecraftLocale.ZH_CN, "间苯二甲酸二苯酯"))
                .fluid()
                .color(0x246E57)
                .flags(DISABLE_DECOMPOSITION)
                .components(Carbon, 20, Hydrogen, 14, Oxygen, 4)
                .buildAndRegister();

        PhthalicAcid = new Material.Builder(GTPM.id("phthalic_acid"))
                .localization(text -> text.source("Phthalic Acid")
                        .translation(MinecraftLocale.ZH_CN, "邻苯二甲酸"))
                .liquid(new FluidBuilder().attribute(FluidAttributes.ACID))
                .color(0xD1D1D1)
                .flags(DISABLE_DECOMPOSITION)
                .components(Carbon, 8, Hydrogen, 6, Oxygen, 4)
                .buildAndRegister()
                .setFormula("C6H4(CO2H)2", true);

        Dimethylbenzene = new Material.Builder(GTPM.id("dimethylbenzene"))
                .localization(text -> text.source("Dimethylbenzene")
                        .translation(MinecraftLocale.ZH_CN, "二甲苯"))
                .fluid()
                .color(0x669C40)
                .flags(DISABLE_DECOMPOSITION)
                .components(Carbon, 8, Hydrogen, 10)
                .buildAndRegister()
                .setFormula("C6H4(CH3)2", true);

        Diaminobenzidine = new Material.Builder(GTPM.id("diaminobenzidine"))
                .localization(text -> text.source("Diaminobenzidine")
                        .translation(MinecraftLocale.ZH_CN, "二氨基联苯胺"))
                .fluid()
                .color(0x337D59)
                .flags(DISABLE_DECOMPOSITION)
                .components(Carbon, 12, Hydrogen, 14, Nitrogen, 4)
                .buildAndRegister()
                .setFormula("(C6H3(NH2)2)2", true);

        Dichlorobenzidine = new Material.Builder(GTPM.id("dichlorobenzidine"))
                .localization(text -> text.source("Dichlorobenzidine")
                        .translation(MinecraftLocale.ZH_CN, "二氯联苯胺"))
                .fluid()
                .color(0xA1DEA6)
                .flags(DISABLE_DECOMPOSITION)
                .components(Carbon, 12, Hydrogen, 10, Chlorine, 2, Nitrogen, 2)
                .buildAndRegister()
                .setFormula("(C6H3Cl(NH2))2", true);

        Nitrochlorobenzene = new Material.Builder(GTPM.id("nitrochlorobenzene"))
                .localization(text -> text.source("Nitrochlorobenzene")
                        .translation(MinecraftLocale.ZH_CN, "硝基氯苯"))
                .fluid()
                .color(0x8FB51A)
                .flags(DISABLE_DECOMPOSITION)
                .components(Carbon, 6, Hydrogen, 4, Chlorine, 1, Nitrogen, 1, Oxygen, 2)
                .buildAndRegister();

        Chlorobenzene = new Material.Builder(GTPM.id("chlorobenzene"))
                .localization(text -> text.source("Chlorobenzene")
                        .translation(MinecraftLocale.ZH_CN, "氯苯"))
                .fluid()
                .color(0x326A3E)
                .flags(DISABLE_DECOMPOSITION)
                .components(Carbon, 6, Hydrogen, 5, Chlorine, 1)
                .hazard(HazardProperty.HazardTrigger.INHALATION, GTMedicalConditions.CARCINOGEN)
                .buildAndRegister();

        Octane = new Material.Builder(GTPM.id("octane"))
                .localization(text -> text.source("Octane")
                        .translation(MinecraftLocale.ZH_CN, "辛烷"))
                .fluid()
                .flags(DISABLE_DECOMPOSITION)
                .color(0x8A0A09)
                .components(Carbon, 8, Hydrogen, 18)
                .buildAndRegister();

        EthylTertButylEther = new Material.Builder(GTPM.id("ethyl_tertbutyl_ether"))
                .localization(text -> text.source("Ethyl Tertbutyl Ether")
                        .translation(MinecraftLocale.ZH_CN, "乙基叔丁基醚"))
                .fluid()
                .flags(DISABLE_DECOMPOSITION)
                .color(0xB15C06)
                .components(Carbon, 6, Hydrogen, 14, Oxygen, 1)
                .buildAndRegister();

        Ethylbenzene = new Material.Builder(GTPM.id("ethylbenzene"))
                .localization(text -> text.source("Ethylbenzene")
                        .translation(MinecraftLocale.ZH_CN, "乙苯"))
                .fluid()
                .flags(DISABLE_DECOMPOSITION)
                .components(Carbon, 8, Hydrogen, 10)
                .hazard(HazardProperty.HazardTrigger.INHALATION, GTMedicalConditions.CARCINOGEN)
                .buildAndRegister();

        Naphthalene = new Material.Builder(GTPM.id("naphthalene"))
                .localization(text -> text.source("Naphthalene")
                        .translation(MinecraftLocale.ZH_CN, "萘"))
                .fluid()
                .flags(DISABLE_DECOMPOSITION)
                .color(0xF4F4D7)
                .components(Carbon, 10, Hydrogen, 8)
                .buildAndRegister();

        Rubber = new Material.Builder(GTPM.id("rubber"))
                .localization(text -> text.source("Rubber")
                        .translation(MinecraftLocale.ZH_CN, "橡胶"))
                .polymer(0)
                .liquid(new FluidBuilder().temperature(400))
                .color(0x353529).secondaryColor(0x080808)
                .toolStats(
                        ToolProperty.Builder.of(1.0F, 1.0F, 256, 1, GTToolType.SOFT_MALLET, GTToolType.PLUNGER).build())
                .flags(GENERATE_RING, GENERATE_FOIL)
                .components(Carbon, 5, Hydrogen, 8)
                .buildAndRegister();

        Cyclohexane = new Material.Builder(GTPM.id("cyclohexane"))
                .localization(text -> text.source("Cyclohexane")
                        .translation(MinecraftLocale.ZH_CN, "环己烷"))
                .fluid()
                .color(0xe8b113).secondaryColor(0x602a10)
                .components(Carbon, 6, Hydrogen, 12)
                .buildAndRegister();

        NitrosylChloride = new Material.Builder(GTPM.id("nitrosyl_chloride"))
                .localization(text -> text.source("Nitrosyl Chloride")
                        .translation(MinecraftLocale.ZH_CN, "亚硝酰氯"))
                .gas()
                .flags(FLAMMABLE)
                .color(0xF3F100)
                .components(Nitrogen, 1, Oxygen, 1, Chlorine, 1)
                .buildAndRegister();

        CyclohexanoneOxime = new Material.Builder(GTPM.id("cyclohexanone_oxime"))
                .localization(text -> text.source("Cyclohexanone Oxime")
                        .translation(MinecraftLocale.ZH_CN, "环己酮肟"))
                .dust()
                .flags(DISABLE_DECOMPOSITION, FLAMMABLE)
                .color(0xEBEBF0).iconSet(ROUGH)
                .components(Carbon, 6, Hydrogen, 11, Nitrogen, 1, Oxygen, 1)
                .buildAndRegister()
                .setFormula("C6H11NO", true);

        Caprolactam = new Material.Builder(GTPM.id("caprolactam"))
                .localization(text -> text.source("Caprolactam")
                        .translation(MinecraftLocale.ZH_CN, "己内酰胺"))
                .dust()
                .flags(DISABLE_DECOMPOSITION, FLAMMABLE)
                .color(0xfffef8).secondaryColor(0xbab7a2)
                .components(Carbon, 6, Hydrogen, 11, Nitrogen, 1, Oxygen, 1)
                .buildAndRegister()
                .setFormula("(CH2)5C(O)NH", true);

        Butyraldehyde = new Material.Builder(GTPM.id("butyraldehyde"))
                .localization(text -> text.source("Butyraldehyde")
                        .translation(MinecraftLocale.ZH_CN, "丁醛"))
                .fluid()
                .color(0x554A3F)
                .flags(DISABLE_DECOMPOSITION)
                .components(Carbon, 4, Hydrogen, 8, Oxygen, 1)
                .hazard(HazardProperty.HazardTrigger.INHALATION, GTMedicalConditions.NAUSEA, false)
                .buildAndRegister();

        PolyvinylButyral = new Material.Builder(GTPM.id("polyvinyl_butyral"))
                .localization(text -> text.source("Polyvinyl Butyral")
                        .translation(MinecraftLocale.ZH_CN, "聚乙烯醇缩丁醛"))
                .ingot().fluid()
                .color(0x3e7051).secondaryColor(0x535648)
                .flags(GENERATE_PLATE, DISABLE_DECOMPOSITION, NO_SMASHING)
                .components(Butyraldehyde, 1, PolyvinylAcetate, 1)
                .buildAndRegister();

        Biphenyl = new Material.Builder(GTPM.id("biphenyl"))
                .localization(text -> text.source("Biphenyl")
                        .translation(MinecraftLocale.ZH_CN, "联苯"))
                .dust()
                .color(0x8B8C4F).iconSet(FINE)
                .flags(DISABLE_DECOMPOSITION)
                .components(Carbon, 12, Hydrogen, 10)
                .buildAndRegister()
                .setFormula("(C6H5)2", true);

        PolychlorinatedBiphenyl = new Material.Builder(GTPM.id("polychlorinated_biphenyl"))
                .localization(text -> text.source("Polychlorinated Biphenyl")
                        .translation(MinecraftLocale.ZH_CN, "多氯联苯"))
                .fluid()
                .color(0xCACC0E)
                .flags(DISABLE_DECOMPOSITION)
                .components(Carbon, 12, Hydrogen, 8, Chlorine, 2)
                .hazard(HazardProperty.HazardTrigger.INHALATION, GTMedicalConditions.CARCINOGEN)
                .buildAndRegister()
                .setFormula("(C6H4Cl)2", true);

        AceticAnhydride = new Material.Builder(GTPM.id("acetic_anhydride"))
                .localization(text -> text.source("Acetic Anhydride")
                        .translation(MinecraftLocale.ZH_CN, "乙酸酐"))
                .fluid()
                .color(0xE0D182)
                .flags(DISABLE_DECOMPOSITION)
                .components(Carbon, 4, Hydrogen, 6, Oxygen, 3)
                .buildAndRegister()
                .setFormula("(CH3CO)2O", true);

        AminoPhenol = new Material.Builder(GTPM.id("aminophenol"))
                .localization(text -> text.source("Aminophenol")
                        .translation(MinecraftLocale.ZH_CN, "氨基苯酚"))
                .fluid()
                .color(0x784421)
                .flags(DISABLE_DECOMPOSITION)
                .components(Carbon, 6, Hydrogen, 7, Nitrogen, 1, Oxygen, 1)
                .buildAndRegister()
                .setFormula("H2NC6H4OH", true);

        Paracetamol = new Material.Builder(GTPM.id("paracetamol"))
                .localization(text -> text.source("Paracetamol")
                        .translation(MinecraftLocale.ZH_CN, "对乙酰氨基酚"))
                .dust()
                .color(0xF2EDCB)
                .flags(DISABLE_DECOMPOSITION)
                .components(Carbon, 8, Hydrogen, 9, Nitrogen, 1, Oxygen, 2)
                .buildAndRegister();

        AmmoniumFormate = new Material.Builder(GTPM.id("ammonium_formate"))
                .localization(text -> text.source("Ammonium Formate")
                        .translation(MinecraftLocale.ZH_CN, "甲酸铵"))
                .gas()
                .color(0x93badb)
                .components(Carbon, 1, Hydrogen, 5, Nitrogen, 1, Oxygen, 2)
                .hazard(HazardProperty.HazardTrigger.INHALATION, GTMedicalConditions.IRRITANT)
                .buildAndRegister();

        Formamide = new Material.Builder(GTPM.id("formamide"))
                .localization(text -> text.source("Formamide")
                        .translation(MinecraftLocale.ZH_CN, "甲酰胺"))
                .liquid()
                .color(0x5cccb6)
                .components(Carbon, 1, Hydrogen, 3, Nitrogen, 1, Oxygen, 1)
                .hazard(HazardProperty.HazardTrigger.INHALATION, GTMedicalConditions.CHEMICAL_BURNS)
                .buildAndRegister();
    }
}
