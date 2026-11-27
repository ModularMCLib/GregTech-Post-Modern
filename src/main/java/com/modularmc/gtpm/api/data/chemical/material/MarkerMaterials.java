package com.modularmc.gtpm.api.data.chemical.material;

import com.modularmc.gtpm.GTPM;

import com.modularmc.registrate.localization.MinecraftLocale;

import net.minecraft.world.item.DyeColor;

import com.google.common.collect.HashBiMap;

public class MarkerMaterials {

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void register() {
        Color.Colorless.toString();
        Empty.toString();
    }

    /**
     * Marker materials without category
     */
    public static final MarkerMaterial Empty = new MarkerMaterial(GTPM.id("empty"));

    /**
     * Color materials
     */
    public static class Color {

        /**
         * Can be used only by direct specifying
         * Means absence of color on TagPrefix
         * Often a default value for color prefixes
         */
        public static final MarkerMaterial Colorless = new MarkerMaterial(GTPM.id("colorless"));

        public static final MarkerMaterial White = new MarkerMaterial(GTPM.id("white"), text -> text
                .source("White").translation(MinecraftLocale.ZH_CN, "白色"));
        public static final MarkerMaterial Orange = new MarkerMaterial(GTPM.id("orange"), text -> text
                .source("Orange").translation(MinecraftLocale.ZH_CN, "橙色"));
        public static final MarkerMaterial Magenta = new MarkerMaterial(GTPM.id("magenta"), text -> text
                .source("Magenta").translation(MinecraftLocale.ZH_CN, "品红色"));
        public static final MarkerMaterial LightBlue = new MarkerMaterial(GTPM.id("light_blue"), text -> text
                .source("Light Blue").translation(MinecraftLocale.ZH_CN, "淡蓝色"));
        public static final MarkerMaterial Yellow = new MarkerMaterial(GTPM.id("yellow"), text -> text
                .source("Yellow").translation(MinecraftLocale.ZH_CN, "黄色"));
        public static final MarkerMaterial Lime = new MarkerMaterial(GTPM.id("lime"), text -> text
                .source("Lime").translation(MinecraftLocale.ZH_CN, "黄绿色"));
        public static final MarkerMaterial Pink = new MarkerMaterial(GTPM.id("pink"), text -> text
                .source("Pink").translation(MinecraftLocale.ZH_CN, "粉红色"));
        public static final MarkerMaterial Gray = new MarkerMaterial(GTPM.id("gray"), text -> text
                .source("Gray").translation(MinecraftLocale.ZH_CN, "灰色"));
        public static final MarkerMaterial LightGray = new MarkerMaterial(GTPM.id("light_gray"), text -> text
                .source("Light Gray").translation(MinecraftLocale.ZH_CN, "淡灰色"));
        public static final MarkerMaterial Cyan = new MarkerMaterial(GTPM.id("cyan"), text -> text
                .source("Cyan").translation(MinecraftLocale.ZH_CN, "青色"));
        public static final MarkerMaterial Purple = new MarkerMaterial(GTPM.id("purple"), text -> text
                .source("Purple").translation(MinecraftLocale.ZH_CN, "紫色"));
        public static final MarkerMaterial Blue = new MarkerMaterial(GTPM.id("blue"), text -> text
                .source("Blue").translation(MinecraftLocale.ZH_CN, "蓝色"));
        public static final MarkerMaterial Brown = new MarkerMaterial(GTPM.id("brown"), text -> text
                .source("Brown").translation(MinecraftLocale.ZH_CN, "棕色"));
        public static final MarkerMaterial Green = new MarkerMaterial(GTPM.id("green"), text -> text
                .source("Green").translation(MinecraftLocale.ZH_CN, "绿色"));
        public static final MarkerMaterial Red = new MarkerMaterial(GTPM.id("red"), text -> text
                .source("Red").translation(MinecraftLocale.ZH_CN, "红色"));
        public static final MarkerMaterial Black = new MarkerMaterial(GTPM.id("black"), text -> text
                .source("Black").translation(MinecraftLocale.ZH_CN, "黑色"));

        /**
         * Arrays containing all possible color values (without Colorless!)
         */
        public static final MarkerMaterial[] VALUES = new MarkerMaterial[] {
                White, Orange, Magenta, LightBlue, Yellow, Lime, Pink, Gray, LightGray, Cyan, Purple, Blue, Brown,
                Green, Red, Black
        };

        /**
         * Gets color by it's name
         * Name format is equal to DyeColor
         */
        public static MarkerMaterial valueOf(String string) {
            for (MarkerMaterial color : VALUES) {
                if (color.getName().equals(string)) {
                    return color;
                }
            }
            return null;
        }

        /**
         * Contains associations between MC DyeColor and Color MarkerMaterial
         */
        public static final HashBiMap<DyeColor, MarkerMaterial> COLORS = HashBiMap.create();

        static {
            for (var color : DyeColor.values()) {
                COLORS.put(color, Color.valueOf(color.getName()));
            }
        }
    }
}
