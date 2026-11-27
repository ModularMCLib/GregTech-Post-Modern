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
import static com.modularmc.gtpm.api.data.chemical.material.info.MaterialIconSet.*;
import static com.modularmc.gtpm.api.data.tag.TagPrefix.dustSmall;
import static com.modularmc.gtpm.api.data.tag.TagPrefix.dustTiny;
import static com.modularmc.gtpm.common.data.GTMaterials.*;

public class UnknownCompositionMaterials {

    public static void register() {
        WoodGas = new Material.Builder(GTPM.id("wood_gas"))
                .localization(text -> text.source("Wood Gas")
                        .translation(MinecraftLocale.ZH_CN, "木煤气"))
                .gas()
                .color(0xDECD87).secondaryColor(0xdeb287)
                .buildAndRegister();

        WoodVinegar = new Material.Builder(GTPM.id("wood_vinegar"))
                .localization(text -> text.source("Wood Vinegar")
                        .translation(MinecraftLocale.ZH_CN, "木醋酸"))
                .fluid()
                .color(0xD45500).secondaryColor(0x905800)
                .buildAndRegister();

        WoodTar = new Material.Builder(GTPM.id("wood_tar"))
                .localization(text -> text.source("Wood Tar")
                        .translation(MinecraftLocale.ZH_CN, "木焦油"))
                .fluid()
                .color(0x3a271a).secondaryColor(0x28170B)
                .flags(STICKY, FLAMMABLE).buildAndRegister();

        CharcoalByproducts = new Material.Builder(GTPM.id("charcoal_byproducts"))
                .localization(text -> text.source("Charcoal Byproducts")
                        .translation(MinecraftLocale.ZH_CN, "木炭副产物"))
                .fluid().color(0x784421).buildAndRegister();

        Biomass = new Material.Builder(GTPM.id("biomass"))
                .localization(text -> text.source("Biomass")
                        .translation(MinecraftLocale.ZH_CN, "生物质"))
                .liquid(new FluidBuilder().customStill()).color(0x00FF00).buildAndRegister();

        BioDiesel = new Material.Builder(GTPM.id("bio_diesel"))
                .localization(text -> text.source("Bio Diesel")
                        .translation(MinecraftLocale.ZH_CN, "生物柴油"))
                .fluid().color(0xFF8000)
                .flags(FLAMMABLE, EXPLOSIVE).buildAndRegister();

        FermentedBiomass = new Material.Builder(GTPM.id("fermented_biomass"))
                .localization(text -> text.source("Fermented Biomass")
                        .translation(MinecraftLocale.ZH_CN, "发酵生物质"))
                .liquid(new FluidBuilder().temperature(300))
                .color(0x445500)
                .buildAndRegister();

        Creosote = new Material.Builder(GTPM.id("creosote"))
                .localization(text -> text.source("Creosote")
                        .translation(MinecraftLocale.ZH_CN, "杂酚油"))
                .liquid(new FluidBuilder().block().customStill().burnTime(6400)).color(0x804000)
                .flags(STICKY).buildAndRegister();

        Diesel = new Material.Builder(GTPM.id("diesel"))
                .localization(text -> text.source("Diesel")
                        .translation(MinecraftLocale.ZH_CN, "柴油"))
                .liquid(new FluidBuilder().customStill()).flags(FLAMMABLE, EXPLOSIVE).buildAndRegister();

        RocketFuel = new Material.Builder(GTPM.id("rocket_fuel"))
                .localization(text -> text.source("Rocket Fuel")
                        .translation(MinecraftLocale.ZH_CN, "火箭燃料"))
                .fluid().flags(FLAMMABLE, EXPLOSIVE).color(0xBDB78C).buildAndRegister();

        Glue = new Material.Builder(GTPM.id("glue"))
                .localization(text -> text.source("Glue")
                        .translation(MinecraftLocale.ZH_CN, "胶水"))
                .liquid(new FluidBuilder().customStill()).flags(STICKY).buildAndRegister();

        Lubricant = new Material.Builder(GTPM.id("lubricant"))
                .localization(text -> text.source("Lubricant")
                        .translation(MinecraftLocale.ZH_CN, "润滑油"))
                .liquid(new FluidBuilder().customStill()).buildAndRegister();

        McGuffium239 = new Material.Builder(GTPM.id("mc_guffium_239"))
                .localization(text -> text.source("Mc Guffium 239")
                        .translation(MinecraftLocale.ZH_CN, "麦高芬-239"))
                .liquid(new FluidBuilder().customStill()).buildAndRegister();

        IndiumConcentrate = new Material.Builder(GTPM.id("indium_concentrate"))
                .localization(text -> text.source("Indium Concentrate")
                        .translation(MinecraftLocale.ZH_CN, "铟富集溶液"))
                .liquid(new FluidBuilder().attribute(FluidAttributes.ACID))
                .color(0x0E2950).buildAndRegister();

        SeedOil = new Material.Builder(GTPM.id("seed_oil"))
                .localization(text -> text.source("Seed Oil")
                        .translation(MinecraftLocale.ZH_CN, "种子油"))
                .liquid(new FluidBuilder().customStill())
                .color(0xFFFFFF)
                .flags(STICKY, FLAMMABLE).buildAndRegister();

        DrillingFluid = new Material.Builder(GTPM.id("drilling_fluid"))
                .localization(text -> text.source("Drilling Fluid")
                        .translation(MinecraftLocale.ZH_CN, "钻井液"))
                .fluid().color(0xFFFFAA).buildAndRegister();

        ConstructionFoam = new Material.Builder(GTPM.id("construction_foam"))
                .localization(text -> text.source("Construction Foam")
                        .translation(MinecraftLocale.ZH_CN, "建筑泡沫"))
                .fluid().color(0x808080).buildAndRegister();

        SulfuricHeavyFuel = new Material.Builder(GTPM.id("sulfuric_heavy_fuel"))
                .localization(text -> text.source("Sulfuric Heavy Fuel")
                        .translation(MinecraftLocale.ZH_CN, "含硫重燃油"))
                .liquid(new FluidBuilder().customStill()).flags(FLAMMABLE).buildAndRegister();

        HeavyFuel = new Material.Builder(GTPM.id("heavy_fuel"))
                .localization(text -> text.source("Heavy Fuel")
                        .translation(MinecraftLocale.ZH_CN, "重燃油"))
                .liquid(new FluidBuilder().customStill()).flags(FLAMMABLE).buildAndRegister();

        LightlyHydroCrackedHeavyFuel = new Material.Builder(GTPM.id("lightly_hydro_cracked_heavy_fuel"))
                .localization(text -> text.source("Lightly Hydro-Cracked Heavy Fuel")
                        .translation(MinecraftLocale.ZH_CN, "轻度加氢裂化重燃油"))
                .liquid(new FluidBuilder()
                        .temperature(775)
                        .customStill())
                .color(0xFFFF00).flags(FLAMMABLE).buildAndRegister();

        SeverelyHydroCrackedHeavyFuel = new Material.Builder(GTPM.id("severely_hydro_cracked_heavy_fuel"))
                .localization(text -> text.source("Severely Hydro-Cracked Heavy Fuel")
                        .translation(MinecraftLocale.ZH_CN, "重度加氢裂化重燃油"))
                .liquid(new FluidBuilder()
                        .temperature(775)
                        .customStill())
                .color(0xFFFF00).flags(FLAMMABLE).buildAndRegister();

        LightlySteamCrackedHeavyFuel = new Material.Builder(GTPM.id("lightly_steam_cracked_heavy_fuel"))
                .localization(text -> text.source("Lightly Steam-Cracked Heavy Fuel")
                        .translation(MinecraftLocale.ZH_CN, "轻度蒸汽裂化重燃油"))
                .liquid(new FluidBuilder()
                        .temperature(775)
                        .customStill())
                .flags(FLAMMABLE).buildAndRegister();

        SeverelySteamCrackedHeavyFuel = new Material.Builder(GTPM.id("severely_steam_cracked_heavy_fuel"))
                .localization(text -> text.source("Severely Steam-Cracked Heavy Fuel")
                        .translation(MinecraftLocale.ZH_CN, "重度蒸汽裂化重燃油"))
                .liquid(new FluidBuilder()
                        .temperature(775)
                        .customStill())
                .flags(FLAMMABLE).buildAndRegister();

        SulfuricLightFuel = new Material.Builder(GTPM.id("sulfuric_light_fuel"))
                .localization(text -> text.source("Sulfuric Light Fuel")
                        .translation(MinecraftLocale.ZH_CN, "含硫轻燃油"))
                .liquid(new FluidBuilder()
                        .customStill())
                .flags(FLAMMABLE).buildAndRegister();

        LightFuel = new Material.Builder(GTPM.id("light_fuel"))
                .localization(text -> text.source("Light Fuel")
                        .translation(MinecraftLocale.ZH_CN, "轻燃油"))
                .liquid(new FluidBuilder().customStill()).flags(FLAMMABLE).buildAndRegister();

        LightlyHydroCrackedLightFuel = new Material.Builder(GTPM.id("lightly_hydro_cracked_light_fuel"))
                .localization(text -> text.source("Lightly Hydro-Cracked Light Fuel")
                        .translation(MinecraftLocale.ZH_CN, "轻度加氢裂化轻燃油"))
                .liquid(new FluidBuilder()
                        .temperature(775)
                        .customStill())
                .color(0xB7AF08).flags(FLAMMABLE).buildAndRegister();

        SeverelyHydroCrackedLightFuel = new Material.Builder(GTPM.id("severely_hydro_cracked_light_fuel"))
                .localization(text -> text.source("Severely Hydro-Cracked Light Fuel")
                        .translation(MinecraftLocale.ZH_CN, "重度加氢裂化轻燃油"))
                .liquid(new FluidBuilder()
                        .temperature(775)
                        .customStill())
                .color(0xB7AF08).flags(FLAMMABLE).buildAndRegister();

        LightlySteamCrackedLightFuel = new Material.Builder(GTPM.id("lightly_steam_cracked_light_fuel"))
                .localization(text -> text.source("Lightly Steam-Cracked Light Fuel")
                        .translation(MinecraftLocale.ZH_CN, "轻度蒸汽裂化轻燃油"))
                .liquid(new FluidBuilder()
                        .temperature(775)
                        .customStill())
                .flags(FLAMMABLE).buildAndRegister();

        SeverelySteamCrackedLightFuel = new Material.Builder(GTPM.id("severely_steam_cracked_light_fuel"))
                .localization(text -> text.source("Severely Steam-Cracked Light Fuel")
                        .translation(MinecraftLocale.ZH_CN, "重度蒸汽裂化轻燃油"))
                .liquid(new FluidBuilder()
                        .temperature(775)
                        .customStill())
                .flags(FLAMMABLE).buildAndRegister();

        SulfuricNaphtha = new Material.Builder(GTPM.id("sulfuric_naphtha"))
                .localization(text -> text.source("Sulfuric Naphtha")
                        .translation(MinecraftLocale.ZH_CN, "含硫石脑油"))
                .liquid(new FluidBuilder().customStill()).flags(FLAMMABLE).buildAndRegister();

        Naphtha = new Material.Builder(GTPM.id("naphtha"))
                .localization(text -> text.source("Naphtha")
                        .translation(MinecraftLocale.ZH_CN, "石脑油"))
                .liquid(new FluidBuilder().customStill()).flags(FLAMMABLE).buildAndRegister();

        LightlyHydroCrackedNaphtha = new Material.Builder(GTPM.id("lightly_hydro_cracked_naphtha"))
                .localization(text -> text.source("Lightly Hydro-Cracked Naphtha")
                        .translation(MinecraftLocale.ZH_CN, "轻度加氢裂化石脑油"))
                .liquid(new FluidBuilder()
                        .temperature(775)
                        .customStill())
                .color(0xBFB608).flags(FLAMMABLE).buildAndRegister();

        SeverelyHydroCrackedNaphtha = new Material.Builder(GTPM.id("severely_hydro_cracked_naphtha"))
                .localization(text -> text.source("Severely Hydro-Cracked Naphtha")
                        .translation(MinecraftLocale.ZH_CN, "重度加氢裂化石脑油"))
                .liquid(new FluidBuilder()
                        .temperature(775)
                        .customStill())
                .color(0xBFB608).flags(FLAMMABLE).buildAndRegister();

        LightlySteamCrackedNaphtha = new Material.Builder(GTPM.id("lightly_steam_cracked_naphtha"))
                .localization(text -> text.source("Lightly Steam-Cracked Naphtha")
                        .translation(MinecraftLocale.ZH_CN, "轻度蒸汽裂化石脑油"))
                .liquid(new FluidBuilder()
                        .temperature(775)
                        .customStill())
                .color(0xBFB608).flags(FLAMMABLE).buildAndRegister();

        SeverelySteamCrackedNaphtha = new Material.Builder(GTPM.id("severely_steam_cracked_naphtha"))
                .localization(text -> text.source("Severely Steam-Cracked Naphtha")
                        .translation(MinecraftLocale.ZH_CN, "重度蒸汽裂化石脑油"))
                .liquid(new FluidBuilder()
                        .temperature(775)
                        .customStill())
                .color(0xBFB608).flags(FLAMMABLE).buildAndRegister();

        SulfuricGas = new Material.Builder(GTPM.id("sulfuric_gas"))
                .localization(text -> text.source("Sulfuric Gas")
                        .translation(MinecraftLocale.ZH_CN, "含硫炼油气"))
                .gas(new FluidBuilder().customStill())
                .color(0xECDCCC).buildAndRegister();

        RefineryGas = new Material.Builder(GTPM.id("refinery_gas"))
                .localization(text -> text.source("Refinery Gas")
                        .translation(MinecraftLocale.ZH_CN, "炼油气"))
                .gas(new FluidBuilder().customStill())
                .color(0xB4B4B4)
                .flags(FLAMMABLE)
                .buildAndRegister();

        LightlyHydroCrackedGas = new Material.Builder(GTPM.id("lightly_hydro_cracked_gas"))
                .localization(text -> text.source("Lightly Hydro-Cracked Gas")
                        .translation(MinecraftLocale.ZH_CN, "轻度加氢裂化炼油气"))
                .gas(new FluidBuilder().temperature(775))
                .color(0xA0A0A0)
                .flags(FLAMMABLE)
                .buildAndRegister();

        SeverelyHydroCrackedGas = new Material.Builder(GTPM.id("severely_hydro_cracked_gas"))
                .localization(text -> text.source("Severely Hydro-Cracked Gas")
                        .translation(MinecraftLocale.ZH_CN, "重度加氢裂化炼油气"))
                .gas(new FluidBuilder().temperature(775))
                .color(0xC8C8C8)
                .flags(FLAMMABLE)
                .buildAndRegister();

        LightlySteamCrackedGas = new Material.Builder(GTPM.id("lightly_steam_cracked_gas"))
                .localization(text -> text.source("Lightly Steam-Cracked Gas")
                        .translation(MinecraftLocale.ZH_CN, "轻度蒸汽裂化炼油气"))
                .gas(new FluidBuilder().temperature(775))
                .color(0xE0E0E0)
                .flags(FLAMMABLE)
                .buildAndRegister();

        SeverelySteamCrackedGas = new Material.Builder(GTPM.id("severely_steam_cracked_gas"))
                .localization(text -> text.source("Severely Steam-Cracked Gas")
                        .translation(MinecraftLocale.ZH_CN, "重度蒸汽裂化炼油气"))
                .gas(new FluidBuilder().temperature(775))
                .color(0xE0E0E0).flags(FLAMMABLE).buildAndRegister();

        HydroCrackedEthane = new Material.Builder(GTPM.id("hydro_cracked_ethane"))
                .localization(text -> text.source("Hydro-Cracked Ethane")
                        .translation(MinecraftLocale.ZH_CN, "加氢裂化乙烷"))
                .gas(new FluidBuilder().temperature(775))
                .color(0x9696BC).flags(FLAMMABLE).buildAndRegister();

        HydroCrackedEthylene = new Material.Builder(GTPM.id("hydro_cracked_ethylene"))
                .localization(text -> text.source("Hydro-Cracked Ethylene")
                        .translation(MinecraftLocale.ZH_CN, "加氢裂化乙烯"))
                .gas(new FluidBuilder().temperature(775))
                .color(0xA3A3A0).flags(FLAMMABLE).buildAndRegister();

        HydroCrackedPropene = new Material.Builder(GTPM.id("hydro_cracked_propene"))
                .localization(text -> text.source("Hydro-Cracked Propene")
                        .translation(MinecraftLocale.ZH_CN, "加氢裂化丙烯"))
                .gas(new FluidBuilder().temperature(775))
                .color(0xBEA540).flags(FLAMMABLE).buildAndRegister();

        HydroCrackedPropane = new Material.Builder(GTPM.id("hydro_cracked_propane"))
                .localization(text -> text.source("Hydro-Cracked Propane")
                        .translation(MinecraftLocale.ZH_CN, "加氢裂化丙烷"))
                .gas(new FluidBuilder().temperature(775))
                .color(0xBEA540).flags(FLAMMABLE).buildAndRegister();

        HydroCrackedButane = new Material.Builder(GTPM.id("hydro_cracked_butane"))
                .localization(text -> text.source("Hydro-Cracked Butane")
                        .translation(MinecraftLocale.ZH_CN, "加氢裂化丁烷"))
                .gas(new FluidBuilder().temperature(775))
                .color(0x852C18).flags(FLAMMABLE).buildAndRegister();

        HydroCrackedButene = new Material.Builder(GTPM.id("hydro_cracked_butene"))
                .localization(text -> text.source("Hydro-Cracked Butene")
                        .translation(MinecraftLocale.ZH_CN, "加氢裂化丁烯"))
                .gas(new FluidBuilder().temperature(775))
                .color(0x993E05).flags(FLAMMABLE).buildAndRegister();

        HydroCrackedButadiene = new Material.Builder(GTPM.id("hydro_cracked_butadiene"))
                .localization(text -> text.source("Hydro-Cracked Butadiene")
                        .translation(MinecraftLocale.ZH_CN, "加氢裂化丁二烯"))
                .gas(new FluidBuilder().temperature(775))
                .color(0xAD5203).flags(FLAMMABLE).buildAndRegister();

        SteamCrackedEthane = new Material.Builder(GTPM.id("steam_cracked_ethane"))
                .localization(text -> text.source("Steam-Cracked Ethane")
                        .translation(MinecraftLocale.ZH_CN, "蒸汽裂化乙烷"))
                .gas(new FluidBuilder().temperature(775))
                .color(0x9696BC).flags(FLAMMABLE).buildAndRegister();

        SteamCrackedEthylene = new Material.Builder(GTPM.id("steam_cracked_ethylene"))
                .localization(text -> text.source("Steam-Cracked Ethylene")
                        .translation(MinecraftLocale.ZH_CN, "蒸汽裂化乙烯"))
                .gas(new FluidBuilder().temperature(775))
                .color(0xA3A3A0).flags(FLAMMABLE).buildAndRegister();

        SteamCrackedPropene = new Material.Builder(GTPM.id("steam_cracked_propene"))
                .localization(text -> text.source("Steam-Cracked Propene")
                        .translation(MinecraftLocale.ZH_CN, "蒸汽裂化丙烯"))
                .gas(new FluidBuilder().temperature(775))
                .color(0xBEA540).flags(FLAMMABLE).buildAndRegister();

        SteamCrackedPropane = new Material.Builder(GTPM.id("steam_cracked_propane"))
                .localization(text -> text.source("Steam-Cracked Propane")
                        .translation(MinecraftLocale.ZH_CN, "蒸汽裂化丙烷"))
                .gas(new FluidBuilder().temperature(775))
                .color(0xBEA540).flags(FLAMMABLE).buildAndRegister();

        SteamCrackedButane = new Material.Builder(GTPM.id("steam_cracked_butane"))
                .localization(text -> text.source("Steam-Cracked Butane")
                        .translation(MinecraftLocale.ZH_CN, "蒸汽裂化丁烷"))
                .gas(new FluidBuilder().temperature(775))
                .color(0x852C18).flags(FLAMMABLE).buildAndRegister();

        SteamCrackedButene = new Material.Builder(GTPM.id("steam_cracked_butene"))
                .localization(text -> text.source("Steam-Cracked Butene")
                        .translation(MinecraftLocale.ZH_CN, "蒸汽裂化丁烯"))
                .gas(new FluidBuilder().temperature(775))
                .color(0x993E05).flags(FLAMMABLE).buildAndRegister();

        SteamCrackedButadiene = new Material.Builder(GTPM.id("steam_cracked_butadiene"))
                .localization(text -> text.source("Steam-Cracked Butadiene")
                        .translation(MinecraftLocale.ZH_CN, "蒸汽裂化丁二烯"))
                .gas(new FluidBuilder().temperature(775))
                .color(0xAD5203).flags(FLAMMABLE).buildAndRegister();

        LPG = new Material.Builder(GTPM.id("lpg"))
                .localization(text -> text.source("LPG")
                        .translation(MinecraftLocale.ZH_CN, "液化石油气"))
                .liquid(new FluidBuilder().customStill())
                .color(0xFCFCAC).flags(FLAMMABLE, EXPLOSIVE).buildAndRegister();

        RawGrowthMedium = new Material.Builder(GTPM.id("raw_growth_medium"))
                .localization(text -> text.source("Raw Growth Medium")
                        .translation(MinecraftLocale.ZH_CN, "培养基原液"))
                .fluid().color(0xA47351).buildAndRegister();

        SterileGrowthMedium = new Material.Builder(GTPM.id("sterilized_growth_medium"))
                .localization(text -> text.source("Sterilized Growth Medium")
                        .translation(MinecraftLocale.ZH_CN, "无菌培养基"))
                .fluid().color(0xAC876E).buildAndRegister();

        Oil = new Material.Builder(GTPM.id("oil"))
                .localization(text -> text.source("Oil")
                        .translation(MinecraftLocale.ZH_CN, "石油"))
                .liquid(new FluidBuilder().block().customStill())
                .color(0x0A0A0A)
                .flags(STICKY, FLAMMABLE)
                .buildAndRegister();

        HeavyOil = new Material.Builder(GTPM.id("heavy_oil"))
                .localization(text -> text.source("Heavy Oil")
                        .translation(MinecraftLocale.ZH_CN, "重油"))
                .liquid(new FluidBuilder().block().customStill())
                .color(0x0A0A0A)
                .flags(STICKY, FLAMMABLE)
                .buildAndRegister();

        RawOil = new Material.Builder(GTPM.id("raw_oil"))
                .localization(text -> text.source("Raw Oil")
                        .translation(MinecraftLocale.ZH_CN, "原油"))
                .liquid(new FluidBuilder().block().customStill())
                .color(0x0A0A0A)
                .flags(STICKY, FLAMMABLE)
                .buildAndRegister();

        LightOil = new Material.Builder(GTPM.id("light_oil"))
                .localization(text -> text.source("Light Oil")
                        .translation(MinecraftLocale.ZH_CN, "轻油"))
                .liquid(new FluidBuilder().block().customStill())
                .color(0x0A0A0A)
                .flags(STICKY, FLAMMABLE)
                .buildAndRegister();

        NaturalGas = new Material.Builder(GTPM.id("natural_gas"))
                .localization(text -> text.source("Natural Gas")
                        .translation(MinecraftLocale.ZH_CN, "天然气"))
                .gas(new FluidBuilder().block().customStill())
                .flags(FLAMMABLE, EXPLOSIVE).buildAndRegister();

        Bacteria = new Material.Builder(GTPM.id("bacteria"))
                .localization(text -> text.source("Bacteria")
                        .translation(MinecraftLocale.ZH_CN, "细菌"))
                .fluid().color(0x808000).buildAndRegister();

        BacterialSludge = new Material.Builder(GTPM.id("bacterial_sludge"))
                .localization(text -> text.source("Bacterial Sludge")
                        .translation(MinecraftLocale.ZH_CN, "细菌浆液"))
                .fluid().color(0x355E3B).buildAndRegister();

        EnrichedBacterialSludge = new Material.Builder(GTPM.id("enriched_bacterial_sludge"))
                .localization(text -> text.source("Enriched Bacterial Sludge")
                        .translation(MinecraftLocale.ZH_CN, "富集细菌浆液"))
                .fluid().color(0x7FFF00).buildAndRegister();

        Mutagen = new Material.Builder(GTPM.id("mutagen"))
                .localization(text -> text.source("Mutagen")
                        .translation(MinecraftLocale.ZH_CN, "诱变剂"))
                .fluid().color(0x00FF7F).buildAndRegister();

        GelatinMixture = new Material.Builder(GTPM.id("gelatin_mixture"))
                .localization(text -> text.source("Gelatin Mixture")
                        .translation(MinecraftLocale.ZH_CN, "明胶混合物"))
                .fluid().color(0x588BAE).buildAndRegister();

        RawGasoline = new Material.Builder(GTPM.id("raw_gasoline"))
                .localization(text -> text.source("Raw Gasoline")
                        .translation(MinecraftLocale.ZH_CN, "粗汽油"))
                .fluid().color(0xFF6400).flags(FLAMMABLE).buildAndRegister();

        Gasoline = new Material.Builder(GTPM.id("gasoline"))
                .localization(text -> text.source("Gasoline")
                        .translation(MinecraftLocale.ZH_CN, "汽油"))
                .fluid().color(0xFAA500).flags(FLAMMABLE, EXPLOSIVE).buildAndRegister();

        HighOctaneGasoline = new Material.Builder(GTPM.id("high_octane_gasoline"))
                .localization(text -> text.source("High Octane Gasoline")
                        .translation(MinecraftLocale.ZH_CN, "高辛烷值汽油"))
                .fluid().color(0xFFA500).flags(FLAMMABLE, EXPLOSIVE).buildAndRegister();

        CoalGas = new Material.Builder(GTPM.id("coal_gas"))
                .localization(text -> text.source("Coal Gas")
                        .translation(MinecraftLocale.ZH_CN, "煤气"))
                .gas().color(0x333333).buildAndRegister();

        CoalTar = new Material.Builder(GTPM.id("coal_tar"))
                .localization(text -> text.source("Coal Tar")
                        .translation(MinecraftLocale.ZH_CN, "煤焦油"))
                .fluid().color(0x1A1A1A).flags(STICKY, FLAMMABLE).buildAndRegister();

        Gunpowder = new Material.Builder(GTPM.id("gunpowder"))
                .localization(text -> text.source("Gunpowder")
                        .translation(MinecraftLocale.ZH_CN, "火药"))
                .dust(0)
                .color(0xa4a4a4).secondaryColor(0x767676).iconSet(ROUGH)
                .flags(FLAMMABLE, EXPLOSIVE, NO_SMELTING, NO_SMASHING)
                .components(Saltpeter, 2, Sulfur, 1, Carbon, 3)
                .buildAndRegister();

        Oilsands = new Material.Builder(GTPM.id("oilsands"))
                .localization(text -> text.source("Oilsands")
                        .translation(MinecraftLocale.ZH_CN, "油砂"))
                .dust(1).ore()
                .color(0xe3c78a).secondaryColor(0x161e22).iconSet(SAND)
                .flags(FLAMMABLE)
                .buildAndRegister();

        RareEarth = new Material.Builder(GTPM.id("rare_earth"))
                .localization(text -> text.source("Rare Earth")
                        .translation(MinecraftLocale.ZH_CN, "稀土"))
                .dust(0)
                .color(0xffdc88).secondaryColor(0xe99673).iconSet(FINE)
                .buildAndRegister();

        Stone = new Material.Builder(GTPM.id("stone"))
                .localization(text -> text.source("Stone")
                        .translation(MinecraftLocale.ZH_CN, "石头"))
                .dust(2)
                .color(0x8f8f8f).secondaryColor(0x898989).iconSet(ROUGH)
                .flags(MORTAR_GRINDABLE, GENERATE_GEAR, NO_SMASHING, NO_SMELTING)
                .buildAndRegister();

        Lava = new Material.Builder(GTPM.id("lava"))
                .localization(text -> text.source("Lava")
                        .translation(MinecraftLocale.ZH_CN, "熔岩"))
                .fluid().color(0xFF4000).buildAndRegister();

        Netherite = new Material.Builder(GTPM.id("netherite"))
                .localization(text -> text.source("Netherite")
                        .translation(MinecraftLocale.ZH_CN, "下界合金"))
                .ingot().color(0x4b4042).secondaryColor(0x474447)
                .flags(FIRE_RESISTANT)
                .toolStats(ToolProperty.Builder.of(10.0F, 4.0F, 2032, 4)
                        .enchantability(21).build())
                .buildAndRegister();

        Glowstone = new Material.Builder(GTPM.id("glowstone"))
                .localization(text -> text.source("Glowstone")
                        .translation(MinecraftLocale.ZH_CN, "荧石"))
                .dust(1)
                .liquid(new FluidBuilder().temperature(500))
                .color(0xfcb34c).secondaryColor(0xce7533).iconSet(SHINY)
                .flags(NO_SMASHING, GENERATE_PLATE, EXCLUDE_PLATE_COMPRESSOR_RECIPE,
                        EXCLUDE_BLOCK_CRAFTING_BY_HAND_RECIPES)
                .buildAndRegister();

        NetherStar = new Material.Builder(GTPM.id("nether_star"))
                .localization(text -> text.source("Nether Star")
                        .translation(MinecraftLocale.ZH_CN, "下界之星"))
                .gem(4)
                .color(0xfeffc6).secondaryColor(0x7fd7e2)
                .iconSet(NETHERSTAR)
                .flags(NO_SMASHING, NO_SMELTING, GENERATE_LENS)
                .buildAndRegister();

        Endstone = new Material.Builder(GTPM.id("endstone"))
                .localization(text -> text.source("Endstone")
                        .translation(MinecraftLocale.ZH_CN, "末地石"))
                .dust(1)
                .color(0xf6fabd).secondaryColor(0xc5be8b).iconSet(ROUGH)
                .flags(NO_SMASHING)
                .buildAndRegister();

        Netherrack = new Material.Builder(GTPM.id("netherrack"))
                .localization(text -> text.source("Netherrack")
                        .translation(MinecraftLocale.ZH_CN, "地狱岩"))
                .dust(1)
                .color(0x7c4249).secondaryColor(0x400b0b).iconSet(ROUGH)
                .flags(NO_SMASHING, FLAMMABLE)
                .buildAndRegister();

        CetaneBoostedDiesel = new Material.Builder(GTPM.id("cetane_boosted_diesel"))
                .localization(text -> text.source("Cetane Boosted Diesel")
                        .translation(MinecraftLocale.ZH_CN, "高十六烷值柴油"))
                .liquid(new FluidBuilder().customStill())
                .color(0xC8FF00)
                .flags(FLAMMABLE, EXPLOSIVE)
                .buildAndRegister();

        Collagen = new Material.Builder(GTPM.id("collagen"))
                .localization(text -> text.source("Collagen")
                        .translation(MinecraftLocale.ZH_CN, "胶原"))
                .dust(1)
                .color(0xffadb7).secondaryColor(0x80471C).iconSet(ROUGH)
                .buildAndRegister();

        Gelatin = new Material.Builder(GTPM.id("gelatin"))
                .localization(text -> text.source("Gelatin")
                        .translation(MinecraftLocale.ZH_CN, "明胶"))
                .dust(1)
                .color(0xfaf7cb).secondaryColor(0x693d00).iconSet(ROUGH)
                .buildAndRegister();

        Agar = new Material.Builder(GTPM.id("agar"))
                .localization(text -> text.source("Agar")
                        .translation(MinecraftLocale.ZH_CN, "琼脂"))
                .dust(1)
                .color(0xbdd168).secondaryColor(0x403218).iconSet(ROUGH)
                .buildAndRegister();

        Milk = new Material.Builder(GTPM.id("milk"))
                .localization(text -> text.source("Milk")
                        .translation(MinecraftLocale.ZH_CN, "牛奶"))
                .liquid(new FluidBuilder()
                        .temperature(295)
                        .customStill())
                .color(0xfffbf0).secondaryColor(0xf6eac8).iconSet(FINE)
                .buildAndRegister();

        Cocoa = new Material.Builder(GTPM.id("cocoa"))
                .localization(text -> text.source("Cocoa")
                        .translation(MinecraftLocale.ZH_CN, "可可"))
                .dust(0)
                .color(0x976746).secondaryColor(0x301a0a).iconSet(FINE)
                .buildAndRegister();

        Wheat = new Material.Builder(GTPM.id("wheat"))
                .localization(text -> text.source("Wheat")
                        .translation(MinecraftLocale.ZH_CN, "小麦"))
                .dust(0)
                .color(0xdcbb65).secondaryColor(0x565138).iconSet(FINE)
                .buildAndRegister();

        Meat = new Material.Builder(GTPM.id("meat"))
                .localization(text -> text.source("Meat")
                        .translation(MinecraftLocale.ZH_CN, "肉"))
                .dust(1)
                .color(0xe85048).secondaryColor(0x470a06).iconSet(SAND)
                .buildAndRegister();

        Wood = new Material.Builder(GTPM.id("wood"))
                .localization(text -> text.source("Wood")
                        .translation(MinecraftLocale.ZH_CN, "木"))
                .wood()
                .color(0xc29f6d).secondaryColor(0x643200).iconSet(WOOD)
                .fluidPipeProperties(340, 5, false)
                .toolStats(ToolProperty.Builder.of(1.0F, 1.0F, 128, 1, GTToolType.SOFT_MALLET).build())
                .flags(GENERATE_PLATE, GENERATE_ROD, GENERATE_BOLT_SCREW, GENERATE_LONG_ROD, FLAMMABLE, GENERATE_GEAR,
                        GENERATE_FRAME)
                .buildAndRegister();

        Paper = new Material.Builder(GTPM.id("paper"))
                .localization(text -> text.source("Paper")
                        .translation(MinecraftLocale.ZH_CN, "纸"))
                .dust(0)
                .color(0xF9F9F9).secondaryColor(0xECECEC).iconSet(DULL)
                .flags(GENERATE_PLATE, FLAMMABLE, NO_SMELTING, NO_SMASHING,
                        MORTAR_GRINDABLE, EXCLUDE_PLATE_COMPRESSOR_RECIPE)
                .buildAndRegister();

        FishOil = new Material.Builder(GTPM.id("fish_oil"))
                .localization(text -> text.source("Fish Oil")
                        .translation(MinecraftLocale.ZH_CN, "鱼油"))
                .fluid()
                .color(0xDCC15D)
                .flags(STICKY, FLAMMABLE)
                .buildAndRegister();

        RubySlurry = new Material.Builder(GTPM.id("ruby_slurry"))
                .localization(text -> text.source("Ruby Slurry")
                        .translation(MinecraftLocale.ZH_CN, "红宝石浆液"))
                .fluid().color(0xff6464).buildAndRegister();

        SapphireSlurry = new Material.Builder(GTPM.id("sapphire_slurry"))
                .localization(text -> text.source("Sapphire Slurry")
                        .translation(MinecraftLocale.ZH_CN, "蓝宝石浆液"))
                .fluid().color(0x6464c8).buildAndRegister();

        GreenSapphireSlurry = new Material.Builder(GTPM.id("green_sapphire_slurry"))
                .localization(text -> text.source("Green Sapphire Slurry")
                        .translation(MinecraftLocale.ZH_CN, "绿色蓝宝石浆液"))
                .fluid().color(0x64c882).buildAndRegister();

        // These colors are much nicer looking than those in MC's EnumDyeColor
        DyeBlack = new Material.Builder(GTPM.id("black_dye"))
                .localization(text -> text.source("Black Dye")
                        .translation(MinecraftLocale.ZH_CN, "黑色染料"))
                .fluid().color(0x202020).buildAndRegister();

        DyeRed = new Material.Builder(GTPM.id("red_dye"))
                .localization(text -> text.source("Red Dye")
                        .translation(MinecraftLocale.ZH_CN, "红色染料"))
                .fluid().color(0xFF0000).buildAndRegister();

        DyeGreen = new Material.Builder(GTPM.id("green_dye"))
                .localization(text -> text.source("Green Dye")
                        .translation(MinecraftLocale.ZH_CN, "绿色染料"))
                .fluid().color(0x00FF00).buildAndRegister();

        DyeBrown = new Material.Builder(GTPM.id("brown_dye"))
                .localization(text -> text.source("Brown Dye")
                        .translation(MinecraftLocale.ZH_CN, "棕色染料"))
                .fluid().color(0x604000).buildAndRegister();

        DyeBlue = new Material.Builder(GTPM.id("blue_dye"))
                .localization(text -> text.source("Blue Dye")
                        .translation(MinecraftLocale.ZH_CN, "蓝色染料"))
                .fluid().color(0x0020FF).buildAndRegister();

        DyePurple = new Material.Builder(GTPM.id("purple_dye"))
                .localization(text -> text.source("Purple Dye")
                        .translation(MinecraftLocale.ZH_CN, "紫色染料"))
                .fluid().color(0x800080).buildAndRegister();

        DyeCyan = new Material.Builder(GTPM.id("cyan_dye"))
                .localization(text -> text.source("Cyan Dye")
                        .translation(MinecraftLocale.ZH_CN, "青色染料"))
                .fluid().color(0x00FFFF).buildAndRegister();

        DyeLightGray = new Material.Builder(GTPM.id("light_gray_dye"))
                .localization(text -> text.source("Light Gray Dye")
                        .translation(MinecraftLocale.ZH_CN, "淡灰色染料"))
                .fluid().color(0xC0C0C0).buildAndRegister();

        DyeGray = new Material.Builder(GTPM.id("gray_dye"))
                .localization(text -> text.source("Gray Dye")
                        .translation(MinecraftLocale.ZH_CN, "灰色染料"))
                .fluid().color(0x808080).buildAndRegister();

        DyePink = new Material.Builder(GTPM.id("pink_dye"))
                .localization(text -> text.source("Pink Dye")
                        .translation(MinecraftLocale.ZH_CN, "粉红色染料"))
                .fluid().color(0xFFC0C0).buildAndRegister();

        DyeLime = new Material.Builder(GTPM.id("lime_dye"))
                .localization(text -> text.source("Lime Dye")
                        .translation(MinecraftLocale.ZH_CN, "黄绿色染料"))
                .fluid().color(0x80FF80).buildAndRegister();

        DyeYellow = new Material.Builder(GTPM.id("yellow_dye"))
                .localization(text -> text.source("Yellow Dye")
                        .translation(MinecraftLocale.ZH_CN, "黄色染料"))
                .fluid().color(0xFFFF00).buildAndRegister();

        DyeLightBlue = new Material.Builder(GTPM.id("light_blue_dye"))
                .localization(text -> text.source("Light Blue Dye")
                        .translation(MinecraftLocale.ZH_CN, "淡蓝色染料"))
                .fluid().color(0x6080FF).buildAndRegister();

        DyeMagenta = new Material.Builder(GTPM.id("magenta_dye"))
                .localization(text -> text.source("Magenta Dye")
                        .translation(MinecraftLocale.ZH_CN, "品红色染料"))
                .fluid().color(0xFF00FF).buildAndRegister();

        DyeOrange = new Material.Builder(GTPM.id("orange_dye"))
                .localization(text -> text.source("Orange Dye")
                        .translation(MinecraftLocale.ZH_CN, "橙色染料"))
                .fluid().color(0xFF8000).buildAndRegister();

        DyeWhite = new Material.Builder(GTPM.id("white_dye"))
                .localization(text -> text.source("White Dye")
                        .translation(MinecraftLocale.ZH_CN, "白色染料"))
                .fluid().color(0xFFFFFF).buildAndRegister();

        ImpureEnrichedNaquadahSolution = new Material.Builder(GTPM.id("impure_enriched_naquadah_solution"))
                .localization(text -> text.source("Impure Enriched Naquadah Solution")
                        .translation(MinecraftLocale.ZH_CN, "含杂富集硅岩溶液"))
                .fluid().color(0x388438).buildAndRegister();

        EnrichedNaquadahSolution = new Material.Builder(GTPM.id("enriched_naquadah_solution"))
                .localization(text -> text.source("Enriched Naquadah Solution")
                        .translation(MinecraftLocale.ZH_CN, "富集硅岩溶液"))
                .fluid().color(0x3AAD3A).buildAndRegister();

        AcidicEnrichedNaquadahSolution = new Material.Builder(GTPM.id("acidic_enriched_naquadah_solution"))
                .localization(text -> text.source("Acidic Enriched Naquadah Solution")
                        .translation(MinecraftLocale.ZH_CN, "酸性富集硅岩溶液"))
                .liquid(new FluidBuilder().attribute(FluidAttributes.ACID))
                .color(0x3DD63D).buildAndRegister();

        EnrichedNaquadahWaste = new Material.Builder(GTPM.id("enriched_naquadah_waste"))
                .localization(text -> text.source("Enriched Naquadah Waste")
                        .translation(MinecraftLocale.ZH_CN, "富集硅岩废液"))
                .fluid().color(0x355B35).buildAndRegister();

        ImpureNaquadriaSolution = new Material.Builder(GTPM.id("impure_naquadria_solution"))
                .localization(text -> text.source("Impure Naquadria Solution")
                        .translation(MinecraftLocale.ZH_CN, "含杂超能硅岩溶液"))
                .fluid().color(0x518451).buildAndRegister();

        NaquadriaSolution = new Material.Builder(GTPM.id("naquadria_solution"))
                .localization(text -> text.source("Naquadria Solution")
                        .translation(MinecraftLocale.ZH_CN, "超能硅岩溶液"))
                .fluid().color(0x61AD61).buildAndRegister();

        AcidicNaquadriaSolution = new Material.Builder(GTPM.id("acidic_naquadria_solution"))
                .localization(text -> text.source("Acidic Naquadria Solution")
                        .translation(MinecraftLocale.ZH_CN, "酸性超能硅岩溶液"))
                .liquid(new FluidBuilder().attribute(FluidAttributes.ACID))
                .color(0x70D670).buildAndRegister();

        NaquadriaWaste = new Material.Builder(GTPM.id("naquadria_waste"))
                .localization(text -> text.source("Naquadria Waste")
                        .translation(MinecraftLocale.ZH_CN, "超能硅岩废液"))
                .fluid().color(0x425B42).buildAndRegister();

        Lapotron = new Material.Builder(GTPM.id("lapotron"))
                .localization(text -> text.source("Lapotron")
                        .translation(MinecraftLocale.ZH_CN, "兰波顿"))
                .gem()
                .color(0x7497ea).secondaryColor(0x1c0b39).iconSet(DIAMOND)
                .flags(DISABLE_MATERIAL_RECIPES)
                .ignoredTagPrefixes(dustTiny, dustSmall)
                .buildAndRegister();

        TreatedWood = new Material.Builder(GTPM.id("treated_wood"))
                .localization(text -> text.source("Treated Wood")
                        .translation(MinecraftLocale.ZH_CN, "防腐木"))
                .wood()
                .color(0x644218).secondaryColor(0x4e0b00).iconSet(WOOD)
                .fluidPipeProperties(340, 10, false)
                .flags(GENERATE_PLATE, FLAMMABLE, GENERATE_ROD, GENERATE_FRAME)
                .buildAndRegister();

        UUMatter = new Material.Builder(GTPM.id("uu_matter"))
                .localization(text -> text.source("UU-Matter")
                        .translation(MinecraftLocale.ZH_CN, "UU物质"))
                .liquid(new FluidBuilder()
                        .temperature(300)
                        .customStill())
                .buildAndRegister();

        PCBCoolant = new Material.Builder(GTPM.id("pcb_coolant"))
                .localization(text -> text.source("PCB Coolant")
                        .translation(MinecraftLocale.ZH_CN, "多氯联苯冷却液 "))
                .fluid().color(0xD5D69C)
                .hazard(HazardProperty.HazardTrigger.INHALATION, GTMedicalConditions.CARCINOGEN)
                .buildAndRegister();

        Sculk = new Material.Builder(GTPM.id("sculk"))
                .localization(text -> text.source("Sculk")
                        .translation(MinecraftLocale.ZH_CN, "幽匿"))
                .dust(1)
                .color(0x015a5c).secondaryColor(0x001616).iconSet(ROUGH)
                .buildAndRegister();

        Wax = new Material.Builder(GTPM.id("wax"))
                .localization(text -> text.source("Wax")
                        .translation(MinecraftLocale.ZH_CN, "蜡"))
                .gem().fluid()
                .color(0xfabf29)
                .flags(NO_SMELTING)
                .buildAndRegister();

        BauxiteSlurry = new Material.Builder(GTPM.id("bauxite_slurry"))
                .localization(text -> text.source("Bauxite Slurry")
                        .translation(MinecraftLocale.ZH_CN, "铝土浆液"))
                .fluid()
                .color(0x051650)
                .buildAndRegister();

        CrackedBauxiteSlurry = new Material.Builder(GTPM.id("cracked_bauxite_slurry"))
                .localization(text -> text.source("Cracked Bauxite Slurry")
                        .translation(MinecraftLocale.ZH_CN, "裂化铝土浆液"))
                .liquid(775)
                .color(0x052C50)
                .buildAndRegister();

        BauxiteSludge = new Material.Builder(GTPM.id("bauxite_sludge"))
                .localization(text -> text.source("Bauxite Sludge")
                        .translation(MinecraftLocale.ZH_CN, "铝土泥渣"))
                .fluid()
                .color(0x563D2D)
                .buildAndRegister();

        DecalcifiedBauxiteSludge = new Material.Builder(GTPM.id("decalcified_bauxite_sludge"))
                .localization(text -> text.source("Decalcified Bauxite Sludge")
                        .translation(MinecraftLocale.ZH_CN, "脱钙铝土泥渣"))
                .fluid()
                .color(0x6F2DA8)
                .buildAndRegister();

        BauxiteSlag = new Material.Builder(GTPM.id("bauxite_slag"))
                .localization(text -> text.source("Bauxite Slag")
                        .translation(MinecraftLocale.ZH_CN, "铝土矿渣"))
                .dust()
                .color(0x6F2DA8).iconSet(SAND)
                .buildAndRegister();
    }
}
