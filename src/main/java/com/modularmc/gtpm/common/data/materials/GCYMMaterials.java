package com.modularmc.gtpm.common.data.materials;

import com.modularmc.gtpm.GTPM;
import com.modularmc.gtpm.api.GTValues;
import com.modularmc.gtpm.api.data.chemical.material.Material;
import com.modularmc.gtpm.api.data.chemical.material.properties.BlastProperty.GasTier;

import com.modularmc.registrate.localization.MinecraftLocale;

import static com.modularmc.gtpm.api.GTValues.*;
import static com.modularmc.gtpm.api.data.chemical.material.info.MaterialFlags.*;
import static com.modularmc.gtpm.api.data.chemical.material.info.MaterialIconSet.*;
import static com.modularmc.gtpm.common.data.GTMaterials.*;

public class GCYMMaterials {

    public static void register() {
        TantalumCarbide = new Material.Builder(GTPM.id("tantalum_carbide"))
                .localization(text -> text.source("Tantalum Carbide")
                        .translation(MinecraftLocale.ZH_CN, "碳化钽"))
                .ingot(4).fluid()
                .color(0x999900).iconSet(METALLIC)
                .appendFlags(STD_METAL, GENERATE_PLATE)
                .components(Tantalum, 1, Carbon, 1)
                .blast(b -> b.temp(4120, GasTier.MID)
                        .blastStats(VA[EV], 1200))
                .buildAndRegister();

        HSLASteel = new Material.Builder(GTPM.id("hsla_steel"))
                .localization(text -> text.source("HSLA Steel")
                        .translation(MinecraftLocale.ZH_CN, "HSLA钢"))
                .ingot(3).fluid()
                .color(0x686868).iconSet(METALLIC)
                .appendFlags(STD_METAL, GENERATE_PLATE, GENERATE_ROD, GENERATE_FRAME, GENERATE_SPRING)
                .components(Invar, 2, Vanadium, 1, Titanium, 1, Molybdenum, 1)
                .blast(b -> b.temp(1711, GasTier.LOW)
                        .blastStats(VA[GTValues.HV], 1000))
                .buildAndRegister();

        MolybdenumDisilicide = new Material.Builder(GTPM.id("molybdenum_disilicide"))
                .localization(text -> text.source("Molybdenum Disilicide")
                        .translation(MinecraftLocale.ZH_CN, "二硅化钼"))
                .ingot(2).fluid()
                .color(0x564A84).iconSet(METALLIC)
                .appendFlags(STD_METAL, GENERATE_SPRING, GENERATE_RING, GENERATE_PLATE, GENERATE_LONG_ROD)
                .components(Molybdenum, 1, Silicon, 2)
                .blast(b -> b.temp(2300, GasTier.MID)
                        .blastStats(VA[EV], 800))
                .buildAndRegister();

        Zeron100 = new Material.Builder(GTPM.id("zeron_100"))
                .localization(text -> text.source("Zeron-100")
                        .translation(MinecraftLocale.ZH_CN, "赛龙-100"))
                .ingot(5).fluid()
                .color(0x294972).iconSet(METALLIC)
                .appendFlags(STD_METAL, GENERATE_PLATE)
                .components(Iron, 10, Nickel, 2, Tungsten, 2, Niobium, 1, Cobalt, 1)
                .blast(b -> b.temp(3693, GasTier.MID)
                        .blastStats(VA[EV], 1000))
                .buildAndRegister();

        WatertightSteel = new Material.Builder(GTPM.id("watertight_steel"))
                .localization(text -> text.source("Watertight Steel")
                        .translation(MinecraftLocale.ZH_CN, "防水钢"))
                .ingot(4).fluid()
                .color(0x2B4B56).iconSet(METALLIC)
                .appendFlags(STD_METAL, GENERATE_PLATE, GENERATE_ROD, GENERATE_FRAME)
                .components(Iron, 7, Aluminium, 4, Nickel, 2, Chromium, 1, Sulfur, 1)
                .blast(b -> b.temp(3850, GasTier.MID)
                        .blastStats(VA[EV], 800))
                .buildAndRegister();

        IncoloyMA956 = new Material.Builder(GTPM.id("incoloy_ma_956"))
                .localization(text -> text.source("Incoloy MA-956")
                        .translation(MinecraftLocale.ZH_CN, "耐热铬铁合金 MA-956"))
                .ingot(5).fluid()
                .color(0x2D9B66).iconSet(METALLIC)
                .appendFlags(STD_METAL, GENERATE_PLATE, GENERATE_ROD, GENERATE_FRAME)
                .components(VanadiumSteel, 4, Manganese, 2, Aluminium, 5, Yttrium, 2)
                .blast(b -> b.temp(3652, GasTier.MID)
                        .blastStats(VA[EV], 800))
                .buildAndRegister();

        MaragingSteel300 = new Material.Builder(GTPM.id("maraging_steel_300"))
                .localization(text -> text.source("Maraging Steel 300")
                        .translation(MinecraftLocale.ZH_CN, "马氏体时效钢-300"))
                .ingot(4).fluid()
                .color(0x505B6E).iconSet(METALLIC)
                .appendFlags(STD_METAL, GENERATE_ROD, GENERATE_FRAME)
                .components(Iron, 16, Titanium, 1, Aluminium, 1, Nickel, 4, Cobalt, 2)
                .blast(b -> b.temp(4000, GasTier.HIGH)
                        .blastStats(VA[EV], 1000))
                .buildAndRegister();

        HastelloyX = new Material.Builder(GTPM.id("hastelloy_x"))
                .localization(text -> text.source("Hastelloy X")
                        .translation(MinecraftLocale.ZH_CN, "哈斯特洛依合金-X"))
                .ingot(5).fluid()
                .color(0x5784B8).iconSet(METALLIC)
                .appendFlags(STD_METAL, GENERATE_PLATE, GENERATE_FRAME)
                .components(Nickel, 8, Iron, 3, Tungsten, 4, Molybdenum, 2, Chromium, 1, Niobium, 1)
                .blast(b -> b.temp(4200, GasTier.HIGH)
                        .blastStats(VA[EV], 900))
                .buildAndRegister();

        Stellite100 = new Material.Builder(GTPM.id("stellite_100"))
                .localization(text -> text.source("Stellite-100")
                        .translation(MinecraftLocale.ZH_CN, "司太立-100"))
                .ingot(4).fluid()
                .color(0xCFCFEE).iconSet(METALLIC)
                .appendFlags(STD_METAL, GENERATE_PLATE)
                .components(Iron, 4, Chromium, 3, Tungsten, 2, Molybdenum, 1)
                .blast(b -> b.temp(3790, GasTier.HIGH)
                        .blastStats(VA[EV], 1000))
                .buildAndRegister();

        TitaniumCarbide = new Material.Builder(GTPM.id("titanium_carbide"))
                .localization(text -> text.source("Titanium Carbide")
                        .translation(MinecraftLocale.ZH_CN, "碳化钛"))
                .ingot(3).fluid()
                .color(0x90092F).iconSet(METALLIC)
                .appendFlags(STD_METAL, GENERATE_PLATE)
                .components(Titanium, 1, Carbon, 1)
                .blast(b -> b.temp(3430, GasTier.MID)
                        .blastStats(VA[EV], 1000))
                .buildAndRegister();

        TitaniumTungstenCarbide = new Material.Builder(GTPM.id("titanium_tungsten_carbide"))
                .localization(text -> text.source("Titanium Tungsten Carbide")
                        .translation(MinecraftLocale.ZH_CN, "碳化钛钨"))
                .ingot(6).fluid()
                .color(0x680B0B).iconSet(METALLIC)
                .appendFlags(STD_METAL, GENERATE_PLATE)
                .components(TitaniumCarbide, 2, TungstenCarbide, 1)
                .blast(b -> b.temp(3800, GasTier.HIGH)
                        .blastStats(VA[EV], 1000))
                .buildAndRegister();

        HastelloyC276 = new Material.Builder(GTPM.id("hastelloy_c_276"))
                .localization(text -> text.source("Hastelloy C-276")
                        .translation(MinecraftLocale.ZH_CN, "哈斯特洛依合金 C-276"))
                .ingot(6).fluid()
                .color(0xAB2F2F).iconSet(METALLIC)
                .appendFlags(STD_METAL, GENERATE_PLATE, GENERATE_FRAME)
                .components(Nickel, 12, Molybdenum, 8, Chromium, 7, Tungsten, 1, Cobalt, 1, Copper, 1)
                .blast(b -> b.temp(3800, GasTier.HIGH)
                        .blastStats(VA[EV], 1000))
                .buildAndRegister();
    }
}
