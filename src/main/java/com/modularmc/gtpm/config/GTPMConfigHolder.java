package com.modularmc.gtpm.config;

import com.modularmc.gtpm.GTPM;
import com.modularmc.gtpm.api.GTCEuAPI;
import com.modularmc.gtpm.client.bloom.BloomType;

import com.modularmc.registrate.localization.MinecraftLocale;
import com.modularmc.registrate.localization.annotation.Trans;
import com.modularmc.registrate.localization.annotation.TransVariants;
import com.modularmc.registrate.localization.annotation.Translation;

import net.minecraft.commands.Commands;

import dev.toma.configuration.Configuration;
import dev.toma.configuration.config.Config;
import dev.toma.configuration.config.ConfigHolder;
import dev.toma.configuration.config.Configurable;
import dev.toma.configuration.config.format.ConfigFormats;
import org.jetbrains.annotations.ApiStatus;

@Config(id = GTPM.MOD_ID, filename = GTPM.MOD_ID + "/" + GTPM.MOD_ID)
@Trans(key = "config.screen." + GTPM.MOD_ID,
       source = "GregTech",
       translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "格雷科技"))
@TransVariants(keyPrefix = GTPMConfigHolder.OPTION_KEY_PREFIX)
public class GTPMConfigHolder {

    static final String OPTION_KEY_PREFIX = "config." + GTPM.MOD_ID + ".option";

    @ApiStatus.Internal
    public static ConfigHolder<GTPMConfigHolder> HOLDER = Configuration.registerConfig(GTPMConfigHolder.class,
            ConfigFormats.YAML);

    public static GTPMConfigHolder INSTANCE = HOLDER.getConfigInstance();

    @Configurable
    @Trans(source = "recipes",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "配方"))
    public RecipeConfigs recipes = new RecipeConfigs();
    @Configurable
    @Trans(source = "worldgen",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "世界生成"))
    public WorldGenConfigs worldgen = new WorldGenConfigs();
    @Configurable
    @Trans(source = "machines",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "机器"))
    public MachineConfigs machines = new MachineConfigs();
    @Configurable
    @Trans(source = "client",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "客户端"))
    public ClientConfigs client = new ClientConfigs();
    @Configurable
    @Configurable.Comment("Config options for Tools and Armor")
    @Trans(source = "tools",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "工具"))
    public ToolConfigs tools = new ToolConfigs();
    @Configurable
    @Configurable.Comment("Config options for Game Mechanics")
    @Trans(source = "gameplay",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "游戏玩法"))
    public GameplayConfigs gameplay = new GameplayConfigs();
    @Configurable
    @Configurable.Comment("Config options for Mod Compatibility")
    @Trans(source = "compat",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "兼容"))
    public CompatibilityConfigs compat = new CompatibilityConfigs();
    @Configurable
    @Trans(source = "dev",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "开发"))
    public DeveloperConfigs dev = new DeveloperConfigs();

    @TransVariants(keyPrefix = OPTION_KEY_PREFIX)
    public static class RecipeConfigs {

        @Configurable
        @Configurable.Comment({ "Whether to generate Flawed and Chipped Gems for materials and recipes involving them.",
                "Useful for mods like TerraFirmaCraft.", "Default: false" })
        @Trans(source = "generateLowQualityGems",
               translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "生成劣质宝石"))
        public boolean generateLowQualityGems = false; // default false
        @Configurable
        @Configurable.Comment({ "Whether to remove Block/Ingot compression and decompression in the Crafting Table.",
                "Default: true" })
        @Trans(source = "disableManualCompression",
               translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "关闭手工压缩"))
        public boolean disableManualCompression = true; // default true
        @Configurable
        @Configurable.Comment({
                "Change the recipe of Rods in the Lathe to 1 Rod and 2 Small Piles of Dust, instead of 2 Rods.",
                "Default: true" })
        @Trans(source = "harderRods",
               translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "更难的杆配方"))
        public boolean harderRods = true; // default true
        @Configurable
        @Configurable.Comment({
                "Whether to make crafting recipes for Bricks, Firebricks, Nether Bricks, and Coke Bricks harder.",
                "Default: false" })
        @Trans(source = "harderBrickRecipes",
               translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "更难的砖块配方"))
        public boolean harderBrickRecipes = false; // default false
        @Configurable
        @Configurable.Comment({ "Whether to nerf Wood crafting to 2 Planks from 1 Log, and 2 Sticks from 2 Planks.",
                "Default: false" })
        @Trans(source = "nerfWoodCrafting",
               translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "削弱木板木棍合成"))
        public boolean nerfWoodCrafting = false; // default false
        @Configurable
        @Configurable.Comment({ "Whether to make Wood related recipes harder.", "Excludes sticks and planks.",
                "Default: false" })
        @Trans(source = "hardWoodRecipes",
               translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "更难的木头配方"))
        public boolean hardWoodRecipes = false; // default false
        @Configurable
        @Configurable.Comment({ "Recipes for Buckets, Cauldrons, Hoppers, and Iron Bars" +
                " require Iron Plates, Rods, and more.", "Default: true" })
        @Trans(source = "hardIronRecipes",
               translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "更难的铁制品配方"))
        public boolean hardIronRecipes = true; // default true
        @Configurable
        @Configurable.Comment({ "Whether to make Redstone related recipes harder.", "Default: false" })
        @Trans(source = "hardRedstoneRecipes",
               translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "更难的红石配方"))
        public boolean hardRedstoneRecipes = false; // default false
        @Configurable
        @Configurable.Comment({ "Whether to make Vanilla Tools and Armor recipes harder.",
                "Excludes Flint and Steel, and Buckets.", "Default: false" })
        @Trans(source = "hardToolArmorRecipes",
               translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "更难的工具盔甲配方"))
        public boolean hardToolArmorRecipes = false; // default false
        @Configurable
        @Configurable.Comment({ "Whether to make miscellaneous recipes harder.", "Default: false" })
        @Trans(source = "hardMiscRecipes",
               translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "更难的杂项配方"))
        public boolean hardMiscRecipes = false; // default false
        @Configurable
        @Configurable.Comment({ "Whether to make Glass related recipes harder. Default: true" })
        @Trans(source = "hardGlassRecipes",
               translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "更难的玻璃配方"))
        public boolean hardGlassRecipes = true; // default true
        @Configurable
        @Configurable.Comment({ "Whether to nerf the Paper crafting recipe.", "Default: true" })
        @Trans(source = "nerfPaperCrafting",
               translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "削弱纸配方"))
        public boolean nerfPaperCrafting = true; // default true
        @Configurable
        @Configurable.Comment({ "Recipes for items like Iron Doors, Trapdoors, Anvil" +
                " require Iron Plates, Rods, and more.", "Default: true" })
        @Trans(source = "hardAdvancedIronRecipes",
               translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "更难的高级铁制品配方"))
        public boolean hardAdvancedIronRecipes = true; // default true
        @Configurable
        @Configurable.Comment({ "Whether to make coloring blocks like Concrete or Glass harder.", "Default: false" })
        @Trans(source = "hardDyeRecipes",
               translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "更难的染料配方"))
        public boolean hardDyeRecipes = false; // default false
        @Configurable
        @Configurable.Comment({ "Whether to remove charcoal smelting recipes from the vanilla furnace.",
                "Default: true" })
        @Trans(source = "harderCharcoalRecipe",
               translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "更难的木炭配方"))
        public boolean harderCharcoalRecipe = true; // default true
        @Configurable
        @Configurable.Comment({ "Whether to make the Flint and Steel recipe require steel parts.", "Default: true." })
        @Trans(source = "flintAndSteelRequireSteel",
               translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "打火石配方是否需要钢"))
        public boolean flintAndSteelRequireSteel = true; // default true
        @Configurable
        @Configurable.Comment({ "Whether to remove Vanilla Block Recipes from the Crafting Table.", "Default: false" })
        @Trans(source = "removeVanillaBlockRecipes",
               translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "移除原版方块配方"))
        public boolean removeVanillaBlockRecipes = false; // default false
        @Configurable
        @Configurable.Comment({ "Whether to remove Vanilla TNT Recipe from the Crafting Table.", "Default: true" })
        @Trans(source = "removeVanillaTNTRecipe",
               translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "移除原版TNT配方"))
        public boolean removeVanillaTNTRecipe = true; // default true
        @Configurable
        @Configurable.Comment({ "How many Multiblock Casings to make per craft. Either 1, 2, or 3.", "Default: 2" })
        @Configurable.Range(min = 1, max = 3)
        @Trans(source = "casingsPerCraft",
               translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "每次合成机械方块数量"))
        public int casingsPerCraft = 2;
        @Configurable
        @Configurable.Comment({
                "Whether to nerf the output amounts of the first circuit in a set to 1 (from 2) and SoC to 2 (from 4).",
                "Default: false" })
        @Trans(source = "harderCircuitRecipes",
               translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "更难的电路板配方"))
        public boolean harderCircuitRecipes = false;
        @Configurable
        @Configurable.Comment({ "Whether to nerf machine controller recipes.", "Default: false" })
        @Trans(source = "hardMultiRecipes",
               translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "更难的多方块控制器配方"))
        public boolean hardMultiRecipes = false; // default false
        @Configurable
        @Configurable.Comment({
                "Whether tools should have enchants or not. Like the flint sword getting fire aspect.",
                "Default: false" })
        @Trans(source = "enchantedTools",
               translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "附魔工具"))
        public boolean enchantedTools = false;

        @Configurable
        @Configurable.Comment({ "Whether to enable macerator decomposition recycling", "Default: true" })
        @Trans(source = "enableMaceratorRecycling",
               translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "启用研磨机回收"))
        public boolean enableMaceratorRecycling = true;
        @Configurable
        @Configurable.Comment({ "Percentage yield of macerator decomposition recycling outputs, 1.0 means 100%",
                "Default: 1.0f" })
        @Configurable.DecimalRange(min = 0.0f, max = 1.0f)
        @Trans(source = "maceratorRecyclingYield",
               translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "研磨机回收率"))
        public float maceratorRecyclingYield = 1.0f;
        @Configurable
        @Configurable.Comment({ "Whether to enable arc furnace decomposition recycling", "Default: true" })
        @Trans(source = "enableArcRecycling",
               translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "启用电弧炉回收"))
        public boolean enableArcRecycling = true;
        @Configurable
        @Configurable.Comment({ "Percentage yield of arc furnace decomposition recycling outputs, 1.0 means 100%",
                "Default: 1.0f" })
        @Configurable.DecimalRange(min = 0.0f, max = 1.0f)
        @Trans(source = "arcRecyclingYield",
               translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "电弧炉回收率"))
        public float arcRecyclingYield = 1.0f;
        @Configurable
        @Configurable.Comment({ "Whether to enable extractor decomposition recycling", "Default: true" })
        @Trans(source = "enableExtractorRecycling",
               translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "启用提取机回收"))
        public boolean enableExtractorRecycling = true;
        @Configurable
        @Configurable.Comment({ "Percentage yield of extractor decomposition recycling outputs, 1.0 means 100%",
                "Default: 1.0f" })
        @Configurable.DecimalRange(min = 0.0f, max = 1.0f)
        @Trans(source = "extractorRecyclingYield",
               translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "提取机回收率"))
        public float extractorRecyclingYield = 1.0f;
    }

    @TransVariants(keyPrefix = OPTION_KEY_PREFIX)
    public static class CompatibilityConfigs {

        @Configurable
        @Configurable.Comment("Config options regarding GTEU compatibility with other energy systems")
        @Trans(source = "energy",
               translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "能量"))
        public EnergyCompatConfig energy = new EnergyCompatConfig();

        @Configurable
        @Configurable.Comment("Config options regarding GTPM compatibility with AE2")
        @Trans(source = "ae2",
               translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "ae2"))
        public AE2CompatConfig ae2 = new AE2CompatConfig();

        @Configurable
        @Configurable.Comment("Config options regarding GTPM compatibility with minimap mods")
        @Trans(source = "minimap",
               translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "小地图"))
        public MinimapCompatConfig minimap = new MinimapCompatConfig();

        @Configurable
        @Configurable.Comment({ "Whether to hide facades of all blocks in JEI and creative search menu.",
                "Default: true" })
        // todo: implement or purge
        @Trans(source = "hideFacadesInRecipeViewer",
               translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "配方浏览器中隐藏伪装板"))
        public boolean hideFacadesInRecipeViewer = true;

        @Configurable
        @Configurable.Comment({ "Whether to hide filled cells in JEI and creative search menu.", "Default: true" })
        // todo: implement or purge
        @Trans(source = "hideFilledCellsInRecipeViewer",
               translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "配方浏览器中隐藏装满的单元"))
        public boolean hideFilledCellsInRecipeViewer = true;

        @Configurable
        @Configurable.Comment({ "Whether to hide the ore processing diagrams in JEI", "Default: false" })
        @Trans(source = "hideOreProcessingDiagrams",
               translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "隐藏矿物处理流程图"))
        public boolean hideOreProcessingDiagrams = false;

        @Configurable
        @Configurable.Comment({
                "Whether Gregtech should remove smelting recipes from the vanilla furnace for ingots requiring the Electric Blast Furnace.",
                "Default: true" })
        // todo: implement or purge
        @Trans(source = "removeSmeltingForEBFMetals",
               translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "移除需要电力高炉冶炼的金属的熔炉配方"))
        public boolean removeSmeltingForEBFMetals = true;

        @Configurable
        @Configurable.Comment({ "Whether dimension markers should show the dimension tier value.", "Default: false" })
        @Trans(source = "showDimensionTier",
               translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "显示维度等级"))
        public boolean showDimensionTier = false;

        @TransVariants(keyPrefix = OPTION_KEY_PREFIX)
        public static class EnergyCompatConfig {

            @Configurable
            @Configurable.Comment({ "Enable Native GTEU to Forge Energy (RF and alike) on GT Cables and Wires.",
                    "This does not enable nor disable Converters.", "Default: true" })
            @Trans(source = "nativeEUToFE",
                   translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "线缆直接给FE机器供能"))
            public boolean nativeEUToFE = true;

            @Configurable
            @Configurable.Comment({ "Enable GTEU to FE (and vice versa) Converters.", "Default: false" })
            @Trans(source = "enableFEConverters",
                   translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "启用FE转换器"))
            public boolean enableFEConverters = false;

            @Configurable
            @Configurable.Comment({ "Forge Energy to GTEU ratio for converting FE to EU.", "Only affects converters.",
                    "Default: 4 FE == 1 EU" })
            @Configurable.Range(min = 1, max = Integer.MAX_VALUE)
            @Trans(source = "feToEuRatio",
                   translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "FE到GTEU的转换比例"))
            public int feToEuRatio = 4;

            @Configurable
            @Configurable.Comment({ "GTEU to Forge Energy ratio for converting EU to FE.",
                    "Affects native conversion and Converters.", "Default: 4 FE == 1 EU" })
            @Configurable.Range(min = 1, max = Integer.MAX_VALUE)
            @Trans(source = "euToFeRatio",
                   translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "GTEU到FE的转换比例"))
            public int euToFeRatio = 4;
        }

        @TransVariants(keyPrefix = OPTION_KEY_PREFIX)
        public static class AE2CompatConfig {

            @Configurable
            @Configurable.Comment({ "The interval between ME Hatch/Bus interact ME network.",
                    "It may cause lag if the interval is too small.", "Default: 2 sec" })
            @Configurable.Range(min = 1) // Do Not Set a Maximum, if someone wants >80 ticks let them.
            @Trans(source = "updateIntervals",
                   translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "更新间隔"))
            public int updateIntervals = 40;

            @Configurable
            @Configurable.Comment({ "The energy consumption of ME Hatch/Bus.", "Default: 4.0AE/t" })
            @Configurable.DecimalRange(min = 0.0, max = Integer.MAX_VALUE)
            @Trans(source = "meHatchEnergyUsage",
                   translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "ME仓能耗"))
            public double meHatchEnergyUsage = 4.0;
        }

        @TransVariants(keyPrefix = OPTION_KEY_PREFIX)
        public static class MinimapCompatConfig {

            @Configurable
            @Configurable.Comment({
                    "Toggle specific map mod integration on/off (need to restart for this to take effect)" })
            @Trans(source = "toggle",
                   translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "切换"))
            public Toggle toggle = new Toggle();

            @Configurable
            @Configurable.Comment({ "The radius, in blocks, that picking up a surface rock will search for veins in.",
                    "0 to disable.", "Default: 24" })
            @Configurable.Range(min = 0)
            @Trans(source = "surfaceRockProspectRange",
                   translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "地表岩石探测半径"))
            public int surfaceRockProspectRange = 24;
            @Configurable
            @Configurable.Comment({ "The radius, in blocks, that clicking an ore block will search for veins in.",
                    "0 to disable", "Default: 24" })
            @Configurable.Range(min = 0)
            @Trans(source = "oreBlockProspectRange",
                   translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "矿石方块探测半径"))
            public int oreBlockProspectRange = 24;

            @Configurable
            @Configurable.Comment("The map scale at which displayed ores will stop scaling.")
            @Configurable.DecimalRange(min = 0.1, max = 16)
            // todo: implement or purge
            @Trans(source = "oreScaleStop",
                   translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "最大图标缩放比例"))
            public float oreScaleStop = 1;

            @Configurable
            @Configurable.Comment("The size, in pixels, of ore icons on the map")
            @Configurable.Range(min = 4)
            @Trans(source = "oreIconSize",
                   translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "矿石图标大小"))
            public int oreIconSize = 32;

            @Configurable
            @Configurable.Comment("The string prepending ore names in the ore vein tooltip")
            @Trans(source = "oreNamePrefix",
                   translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "矿石名称前缀"))
            public String oreNamePrefix = "- ";

            @Configurable
            @Configurable.Comment({ "The color to draw a box around the ore icon with.",
                    "Accepts either an ARGB hex color prefixed with # or the string 'material' to use the ore's material color" })
            @Trans(source = "borderColor",
                   translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "边框颜色"))
            public String borderColor = "#00000000";

            @Configurable
            @Configurable.Comment({ "Which part of the screen to anchor buttons to", "Default: BOTTOM_LEFT" })
            @Trans(source = "buttonAnchor",
                   translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "按钮位置"))
            public Anchor buttonAnchor = Anchor.BOTTOM_LEFT;

            @Configurable
            @Configurable.Comment({ "Which direction the buttons will go", "Default: VERTICAL" })
            @Trans(source = "direction",
                   translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "方向"))
            public Direction direction = Direction.VERTICAL;

            @Configurable
            @Configurable.Comment({ "How horizontally far away from the anchor to place the buttons", "Default: 20" })
            @Trans(source = "xOffset",
                   translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "x偏移"))
            public int xOffset = 20;

            @Configurable
            @Configurable.Comment({ "How vertically far away from the anchor to place the buttons", "Default: 0" })
            @Trans(source = "yOffset",
                   translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "y偏移"))
            public int yOffset = 0;

            @TransVariants(keyPrefix = OPTION_KEY_PREFIX)
            public static class Toggle {

                @Configurable
                @Configurable.Comment({ "FTB Chunks integration enabled" })
                @Trans(source = "ftbChunksIntegration",
                       translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "FTB区块-集成"))
                public boolean ftbChunksIntegration = false;

                @Configurable
                @Configurable.Comment({ "Journey Map integration enabled" })
                @Trans(source = "journeyMapIntegration",
                       translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "旅行地图-集成"))
                public boolean journeyMapIntegration = true;

                @Configurable
                @Configurable.Comment({ "Xaerox's map integration enabled" })
                @Trans(source = "xaerosMapIntegration",
                       translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "Xaero地图-集成"))
                public boolean xaerosMapIntegration = true;
            }

            public enum Anchor {

                TOP_LEFT,
                TOP_CENTER,
                TOP_RIGHT,
                RIGHT_CENTER,
                BOTTOM_RIGHT,
                BOTTOM_CENTER,
                BOTTOM_LEFT,
                LEFT_CENTER;

                public boolean isCentered() {
                    return this == TOP_CENTER || this == RIGHT_CENTER || this == BOTTOM_CENTER || this == LEFT_CENTER;
                }

                public Direction usualDirection() {
                    return switch (this) {
                        case TOP_CENTER, BOTTOM_CENTER -> Direction.HORIZONTAL;
                        case RIGHT_CENTER, LEFT_CENTER -> Direction.VERTICAL;
                        default -> null;
                    };
                }
            }

            public enum Direction {
                VERTICAL,
                HORIZONTAL
            }

            public int getBorderColor(int materialColor) {
                if (borderColor.equals("material")) {
                    return materialColor;
                }
                // please java may I have an unsigned int
                try {
                    long tmp = Long.decode(borderColor);
                    if (tmp > 0x7FFFFFFF) {
                        tmp -= 0x100000000L;
                    }
                    return (int) tmp;
                } catch (NumberFormatException e) {
                    return 0x00000000;
                }
            }
        }
    }

    @TransVariants(keyPrefix = OPTION_KEY_PREFIX)
    public static class WorldGenConfigs {

        @Configurable
        @Configurable.Comment({ "Rubber Tree spawn chance (decimal % per chunk)", "Default: 0.5" })
        @Configurable.DecimalRange(min = 0f, max = 1f)
        @Trans(source = "rubberTreeSpawnChance",
               translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "橡胶树生成权重"))
        public float rubberTreeSpawnChance = 0.5f;

        @Configurable
        @Configurable.Comment({ "Should Sand-like ores fall?", "This includes gravel, sand, and red sand ores.",
                "Default: false (no falling ores)" })
        @Trans(source = "sandOresFall",
               translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "沙子矿石受重力吗"))
        public boolean sandOresFall = false;

        @Configurable
        @Configurable.Comment({
                "Whether to increase number of rolls for dungeon chests. Increases dungeon loot drastically.",
                "Default: true", "WARNING: Currently unimplemented." })
        @Trans(source = "increaseDungeonLoot",
               translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "更多的地牢战利品"))
        public boolean increaseDungeonLoot = true;
        @Configurable
        @Configurable.Comment({ "Allow GregTech to add additional GregTech Items as loot in various structures.",
                "Default: true" })
        @Trans(source = "addLoot",
               translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "添加战利品"))
        public boolean addLoot = true;

        @Configurable
        @Trans(source = "oreVeins",
               translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "矿脉"))
        public OreVeinConfigs oreVeins = new OreVeinConfigs();

        @TransVariants(keyPrefix = OPTION_KEY_PREFIX)
        public static class OreVeinConfigs {

            @Configurable
            @Configurable.Range(min = 1, max = 32)
            @Configurable.Comment({
                    "The grid size (in chunks) for ore vein generation",
                    "Default: 3"
            })
            @Trans(source = "oreVeinGridSize",
                   translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "矿脉网格大小"))
            public int oreVeinGridSize = 3;
            @Configurable
            @Configurable.Range(min = 0, max = 32 * 16)
            @Configurable.Comment({
                    "The maximum random offset (in blocks) from the grid for generating an ore vein.",
                    "Default: 12"
            })
            @Trans(source = "oreVeinRandomOffset",
                   translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "矿脉随机偏移"))
            public int oreVeinRandomOffset = 12;
            @Configurable
            @Configurable.Comment({ "Prevents regular vanilla ores from being generated outside GregTech ore veins",
                    "Default: true" })
            @Trans(source = "removeVanillaOreGen",
                   translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "移除原版矿石"))
            public boolean removeVanillaOreGen = true;
            @Configurable
            @Configurable.Comment({ "Prevents vanilla's large ore veins from being generated", "Default: true" })
            @Trans(source = "removeVanillaLargeOreVeins",
                   translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "移除原版大型矿脉"))
            public boolean removeVanillaLargeOreVeins = true;
            @Configurable
            @Configurable.Comment({ "Distance between bedrock ore veins in chunks, if enabled.", "Default: 16" })
            @Trans(source = "bedrockOreDistance",
                   translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "基岩矿石距离"))
            public int bedrockOreDistance = 16;
            @Configurable
            @Configurable.Comment({ "Make bedrock ore/fluid veins infinite?", "Default: false" })
            @Trans(source = "infiniteBedrockOresFluids",
                   translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "无限基岩矿石流体"))
            public boolean infiniteBedrockOresFluids = false;
            @Configurable
            @Configurable.Comment({ "Generate ores indicators above ore veins", "Default: true" })
            @Trans(source = "oreIndicators",
                   translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "矿脉指示物"))
            public boolean oreIndicators = true;
            @Configurable
            @Configurable.Comment({
                    "Sets the maximum number of chunks that may be cached for ore vein generation.",
                    "Higher values may improve world generation performance, but at the cost of more RAM usage.",
                    "If you substantially increase the ore vein grid size, random vein offset, or have very large (custom) veins, you may need to increase this value as well.",
                    "Default: 512 (requires restarting the server / re-opening the world)"
            })
            @Trans(source = "oreGenerationChunkCacheSize",
                   translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "矿物生成区块缓存大小"))
            public int oreGenerationChunkCacheSize = 512;
            @Configurable
            @Configurable.Comment({
                    "Sets the maximum number of chunks for which ore indicators may be cached.",
                    "If you register any custom veins with very large indicator ranges (or modify existing ones that way), you may need to increase this value.",
                    "Default: 2048 (requires restarting the server / re-opening the world)"
            })
            @Trans(source = "oreIndicatorChunkCacheSize",
                   translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "矿脉指示物区块缓存大小"))
            public int oreIndicatorChunkCacheSize = 2048;
        }
    }

    @TransVariants(keyPrefix = OPTION_KEY_PREFIX)
    public static class MachineConfigs {

        @Configurable
        @Configurable.Comment({
                "Whether to require a Wrench, Wirecutter, or other GregTech tools to break machines, casings, wires, and more.",
                "Default: true" })
        @Trans(source = "requireGTToolsForBlocks",
               translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "方块需要格雷工具"))
        public boolean requireGTToolsForBlocks = true;
        @Configurable
        @Configurable.Comment({
                "Whether machines explode in rainy weather or when placed next to certain terrain, such as fire or lava",
                "Default: false" })
        @Trans(source = "shouldWeatherOrTerrainExplosion",
               translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "机器是否因天气或地形爆炸"))
        public boolean shouldWeatherOrTerrainExplosion = false;
        @Configurable
        @Configurable.Comment({ "Energy use multiplier for electric items.", "Default: 100" })
        @Trans(source = "energyUsageMultiplier",
               translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "能源消耗倍率"))
        public int energyUsageMultiplier = 100;

        @Configurable
        @Configurable.Comment({ "Energy use multiplier for prospectors.", "Default: 100" })
        @Trans(source = "prospectorEnergyUseMultiplier",
               translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "探矿仪能源消耗乘数"))
        public int prospectorEnergyUseMultiplier = 100;
        @Configurable
        @Configurable.Comment({ "Whether machines or boilers damage the terrain when they explode.",
                "Note machines and boilers always explode when overloaded with power or met with special conditions, regardless of this config.",
                "Default: true" })
        @Trans(source = "doesExplosionDamagesTerrain",
               translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "机器爆炸是否破坏地形"))
        public boolean doesExplosionDamagesTerrain = true;
        @Configurable
        @Configurable.Comment({
                "Enables Safe Active Transformers, removing their ability to explode if unformed while transmitting/receiving power.",
                "Default: false" })
        @Trans(source = "harmlessActiveTransformers",
               translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "无害的有源变压器"))
        public boolean harmlessActiveTransformers = false;
        @Configurable
        @Configurable.Comment({ "Whether to play machine sounds while machines are active.", "Default: true" })
        @Trans(source = "machineSounds",
               translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "机器音效"))
        public boolean machineSounds = true;
        @Configurable
        @Configurable.Comment({ "Duration in ticks that batching will try to reach.", "Default: 100" })
        @Trans(source = "batchDuration",
               translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "批处理持续时间"))
        public int batchDuration = 100;
        @Configurable
        @Configurable.Comment({ "Whether Steam Multiblocks should use Steel instead of Bronze.", "Default: false" })
        @Trans(source = "steelSteamMultiblocks",
               translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "用钢的蒸汽多方块结构"))
        public boolean steelSteamMultiblocks = false;
        @Configurable
        @Configurable.Comment({ "Whether to enable the cleanroom, required for various recipes.", "Default: true" })
        @Trans(source = "enableCleanroom",
               translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "启用超净间"))
        public boolean enableCleanroom = true;
        @Configurable
        @Configurable.Comment({ "Whether multiblocks should ignore all cleanroom requirements.",
                "This does nothing if enableCleanroom is false.", "Default: false" })
        @Trans(source = "cleanMultiblocks",
               translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "多方块结构是否洁净"))
        public boolean cleanMultiblocks = false;
        @Configurable
        @Configurable.Comment({
                "Whether the miner should attempt to replace the block mined with a cobbled version of the ore",
                "Default: true" })
        @Trans(source = "replaceWithCobbleVersion",
               translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "以特色圆石替换"))
        public boolean replaceWithCobbleVersion = true;
        @Configurable
        @Configurable.Comment({ "Block to replace mined ores with in the miner and multiblock miner.",
                "Default: minecraft:cobblestone" })
        @Trans(source = "replaceMinedBlocksWith",
               translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "用...替换采矿机采过的方块"))
        public String replaceMinedBlocksWith = "minecraft:cobblestone";
        @Configurable
        @Configurable.Comment({ "Whether to enable Assembly Line research for recipes.", "Default: true" })
        @Trans(source = "enableResearch",
               translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "启用研究"))
        public boolean enableResearch = true;
        @Configurable
        @Configurable.Comment({ "Whether to enable the Maintenance Hatch, required for Multiblocks.", "Default: true" })
        @Trans(source = "enableMaintenance",
               translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "启用维护仓"))
        public boolean enableMaintenance = true;
        @Configurable
        @Configurable.Comment({
                "How often to check for maintenance, rolling a 1/6000 chance every X ticks (before secondary effects like Configurable Maintenance Hatch).",
                "In default settings, this equates to a 5% chance every hour of a machine running.",
                "Default: 1000 (ticks)" })
        @Trans(source = "maintenanceCheckRate",
               translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "检查维护的频率"))
        public int maintenanceCheckRate = 1000;

        @Configurable
        @Configurable.Comment({
                "Whether to enable World Accelerators, which accelerate ticks for surrounding Tile Entities, Crops, etc.",
                "Default: true" })
        @Trans(source = "enableWorldAccelerators",
               translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "启用世界加速器"))
        public boolean enableWorldAccelerators = true;

        @Configurable
        @Configurable.Comment({ "List of TileEntities that the World Accelerator should not accelerate.",
                "GregTech TileEntities are always blocked.",
                "Entries must be in a fully qualified format. For example: appeng.tile.networking.TileController",
                "Default: none" })
        @Trans(source = "worldAcceleratorBlacklist",
               translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "世界加速器黑名单"))
        public String[] worldAcceleratorBlacklist = new String[0];

        @Configurable
        @Configurable.Comment({
                "Whether to use GT6-style pipe and cable connections, meaning they will not auto-connect " +
                        "unless placed directly onto another pipe or cable.",
                "Default: true" })
        @Trans(source = "gt6StylePipesCables",
               translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "类GT6管道/线缆"))
        public boolean gt6StylePipesCables = true;
        @Configurable
        @Configurable.Comment({ "Whether the machine's circuit slot need to be inserted a real circuit." })
        @Trans(source = "ghostCircuit",
               translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "虚拟电路"))
        public boolean ghostCircuit = true;
        @Configurable
        @Configurable.Comment({ "Whether to add a \"Bedrock Ore Miner\" (also enables bedrock ore generation)",
                "Default: false" })
        @Trans(source = "doBedrockOres",
               translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "添加基岩矿石"))
        public boolean doBedrockOres = false;
        @Configurable
        @Configurable.Comment({ "What Kind of material should the bedrock ore miner output?", "Default: \"raw\"" })
        @Trans(source = "bedrockOreDropTagPrefix",
               translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "基岩矿石掉落Tag"))
        public String bedrockOreDropTagPrefix = "raw";
        @Configurable
        @Configurable.Range(min = 120, max = 800)
        @Configurable.Comment({ "The base amount of ticks per block for electric singleblock ore miners",
                "Default: 320" })
        @Trans(source = "minerSpeed",
               translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "采矿机速度"))
        public int minerSpeed = 320;
        @Configurable
        @Configurable.Comment({ "Makes nearly every GCYM Multiblock require blocks which set their maximum voltages.",
                "Default: false" })
        // todo: implement or purge
        @Trans(source = "enableTieredCasings",
               translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "启用分级机械方块"))
        public boolean enableTieredCasings = false;
        @Configurable
        @Configurable.Comment({ "Minimum distance between Long Distance Item Pipe Endpoints", "Default: 50" })
        @Trans(source = "ldItemPipeMinDistance",
               translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "长距物品管道最小距离"))
        public int ldItemPipeMinDistance = 50;
        @Configurable
        @Configurable.Comment({ "Minimum distance betweeb Long Distance Fluid Pipe Endpoints", "Default: 50" })
        @Trans(source = "ldFluidPipeMinDistance",
               translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "长距流体管道最小距离"))
        public int ldFluidPipeMinDistance = 50;

        @Configurable
        @Configurable.Comment({ "Whether ONLY owners can open a machine gui", "Default: false" })
        @Trans(source = "onlyOwnerGUI",
               translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "仅所有者可开启GUI"))
        public boolean onlyOwnerGUI = false;
        @Configurable
        @Configurable.Comment({ "Whether ONLY owners can break a machine", "Default: false" })
        @Trans(source = "onlyOwnerBreak",
               translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "仅所有者可破坏"))
        public boolean onlyOwnerBreak = false;
        @Configurable
        @Configurable.Comment({ "Minimum op level to bypass the ownership checks", "Default: 2" })
        @Configurable.Range(min = Commands.LEVEL_ALL, max = Commands.LEVEL_OWNERS)
        @Trans(source = "ownerOPBypass",
               translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "跳过所有权检查的最低权限等级"))
        public int ownerOPBypass = Commands.LEVEL_GAMEMASTERS;

        /**
         * <strong>Addons mods should not reference this config directly.</strong>
         * Use {@link GTCEuAPI#isHighTier()} instead.
         */
        @Configurable
        @Configurable.Comment({ "If High Tier (>UV-tier) GT content should be registered.",
                "Items and Machines enabled with this config will have missing recipes by default.",
                "This is intended for modpack developers only, and is not playable without custom tweaks or addons.",
                "Other mods can override this to true, regardless of the config file.",
                "Default: false" })
        @Trans(source = "highTierContent",
               translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "极高电压内容"))
        public boolean highTierContent = false;

        @Configurable
        @Configurable.Comment({ "Whether the Assembly Line should require the item inputs to be in order.",
                "Default: true" })
        @Trans(source = "orderedAssemblyLineItems",
               translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "有序的装配线-物品"))
        public boolean orderedAssemblyLineItems = true;
        @Configurable
        @Configurable.Comment({ "Whether the Assembly Line should require the fluid inputs to be in order.",
                "(Requires Ordered Assembly Line Item Inputs to be enabled.)", "Default: true" })
        @Trans(source = "orderedAssemblyLineFluids",
               translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "有序的装配线-流体"))
        public boolean orderedAssemblyLineFluids = true;

        @Configurable
        @Configurable.Comment({
                "Default maximum parallel of steam multiblocks",
                "Default: 8"
        })
        @Trans(source = "steamMultiParallelAmount",
               translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "蒸汽多方块机器并行数"))
        public int steamMultiParallelAmount = 8;

        @Configurable
        @Configurable.Comment("Small Steam Boiler Options")
        @Trans(source = "smallBoilers",
               translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "小型锅炉"))
        public SmallBoilers smallBoilers = new SmallBoilers();
        @Configurable
        @Configurable.Comment("Large Steam Boiler Options")
        @Trans(source = "largeBoilers",
               translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "大型锅炉"))
        public LargeBoilers largeBoilers = new LargeBoilers();

        @TransVariants(keyPrefix = OPTION_KEY_PREFIX)
        public static class SmallBoilers {

            @Configurable
            @Configurable.Comment({ "The amount of steam a Steam Solid Boiler produces per second at max temperature.",
                    "Default: 120" })
            @Trans(source = "solidBoilerBaseOutput",
                   translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "固体锅炉-基础输出"))
            public int solidBoilerBaseOutput = 120;
            @Configurable
            @Configurable.Comment({
                    "The amount of steam a High Pressure Steam Solid Boiler produces per second at max temperature.",
                    "Default: 300" })
            @Trans(source = "hpSolidBoilerBaseOutput",
                   translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "高压固体锅炉-基础输出"))
            public int hpSolidBoilerBaseOutput = 300;
            @Configurable
            @Configurable.Comment({ "The amount of steam a Steam Liquid Boiler produces per second at max temperature.",
                    "Default: 240" })
            @Trans(source = "liquidBoilerBaseOutput",
                   translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "液体锅炉-基础输出"))
            public int liquidBoilerBaseOutput = 240;
            @Configurable
            @Configurable.Comment({
                    "The amount of steam a High Pressure Steam Liquid Boiler produces per second at max temperature.",
                    "Default: 600" })
            @Trans(source = "hpLiquidBoilerBaseOutput",
                   translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "高压液体锅炉-基础输出"))
            public int hpLiquidBoilerBaseOutput = 600;
            @Configurable
            @Configurable.Comment({ "The amount of steam a Steam Solar Boiler produces per second at max temperature.",
                    "Default: 120" })
            @Trans(source = "solarBoilerBaseOutput",
                   translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "太阳能锅炉-基础输出"))
            public int solarBoilerBaseOutput = 120;
            @Configurable
            @Configurable.Comment({
                    "The amount of steam a High Pressure Steam Solar Boiler produces per second at max temperature.",
                    "Default: 360" })
            @Trans(source = "hpSolarBoilerBaseOutput",
                   translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "高压太阳能锅炉-基础输出"))
            public int hpSolarBoilerBaseOutput = 360;
        }

        @TransVariants(keyPrefix = OPTION_KEY_PREFIX)
        public static class LargeBoilers {

            @Configurable
            @Configurable.Comment({ "The conversion rate between water and steam in Large Boilers.", "Default: 160" })
            @Trans(source = "steamPerWater",
                   translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "蒸汽-水转化率"))
            public int steamPerWater = 160;
            @Configurable
            @Configurable.Comment({ "The max temperature of the Large Bronze Boiler.", "Default: 800" })
            @Trans(source = "bronzeBoilerMaxTemperature",
                   translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "青铜锅炉-最高温度"))
            public int bronzeBoilerMaxTemperature = 800;
            @Configurable
            @Configurable.Comment({ "The heat speed of the Large Bronze Boiler.", "Default: 1" })
            @Trans(source = "bronzeBoilerHeatSpeed",
                   translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "青铜锅炉-加热速度"))
            public int bronzeBoilerHeatSpeed = 1;
            @Configurable
            @Configurable.Comment({ "The max temperature of the Large Steel Boiler.", "Default: 1800" })
            @Trans(source = "steelBoilerMaxTemperature",
                   translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "钢锅炉-最高温度"))
            public int steelBoilerMaxTemperature = 1800;
            @Configurable
            @Configurable.Comment({ "The heat speed of the Large Steel Boiler.", "Default: 1" })
            @Trans(source = "steelBoilerHeatSpeed",
                   translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "钢锅炉-加热速度"))
            public int steelBoilerHeatSpeed = 1;
            @Configurable
            @Configurable.Comment({ "The max temperature of the Large Titanium Boiler.", "Default: 3200" })
            @Trans(source = "titaniumBoilerMaxTemperature",
                   translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "钛锅炉-最高温度"))
            public int titaniumBoilerMaxTemperature = 3200;
            @Configurable
            @Configurable.Comment({ "The heat speed of the Large Titanium Boiler.", "Default: 1" })
            @Trans(source = "titaniumBoilerHeatSpeed",
                   translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "钛锅炉-加热速度"))
            public int titaniumBoilerHeatSpeed = 1;
            @Configurable
            @Configurable.Comment({ "The max temperature of the Large Tungstensteel Boiler.", "Default: 6400" })
            @Trans(source = "tungstensteelBoilerMaxTemperature",
                   translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "钨钢锅炉-最高温度"))
            public int tungstensteelBoilerMaxTemperature = 6400;
            @Configurable
            @Configurable.Comment({ "The heat speed of the Large Tungstensteel Boiler.", "Default: 2" })
            @Trans(source = "tungstensteelBoilerHeatSpeed",
                   translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "钨钢锅炉-加热速度"))
            public int tungstensteelBoilerHeatSpeed = 2;
        }
    }

    @TransVariants(keyPrefix = OPTION_KEY_PREFIX)
    public static class ToolConfigs {

        @Configurable
        @Configurable.Comment({ "Random chance for electric tools to take actual damage", "Default: 10%" })
        @Configurable.Range(min = 0, max = 100)
        @Trans(source = "rngDamageElectricTools",
               translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "电动工具耐久度消耗概率"))
        public int rngDamageElectricTools = 10;
        @Configurable
        @Configurable.Comment({ "Amount of blocks that can be spray painted at once", "Default: 16" })
        @Configurable.Range(min = 1, max = 512)
        @Trans(source = "sprayCanChainLength",
               translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "喷雾罐单次可喷涂长度"))
        public int sprayCanChainLength = 16;
        @Configurable
        @Configurable.Comment({ "Delay in ticks between each log being broken when tree felling", "Default: 2" })
        @Configurable.Range(min = 1, max = 400)
        @Trans(source = "treeFellingDelay",
               translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "砍树延迟"))
        public int treeFellingDelay = 2;
        @Configurable
        @Configurable.Comment("NanoSaber Options")
        @Trans(source = "nanoSaber",
               translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "纳米剑"))
        public NanoSaber nanoSaber = new NanoSaber();
        @Configurable
        @Configurable.Comment("NightVision Goggles Voltage Tier. Default: 1 (LV)")
        @Configurable.Range(min = 0, max = 14)
        @Trans(source = "voltageTierNightVision",
               translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "夜视仪电压等级"))
        public int voltageTierNightVision = 1;
        @Configurable
        @Configurable.Comment("NanoSuit Voltage Tier. Default: 3 (HV)")
        @Configurable.Range(min = 0, max = 14)
        @Trans(source = "voltageTierNanoSuit",
               translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "纳米套电压等级"))
        public int voltageTierNanoSuit = 3;
        @Configurable
        @Configurable.Comment({ "Advanced NanoSuit Chestplate Voltage Tier.", "Default: 3 (HV)" })
        @Configurable.Range(min = 0, max = 14)
        @Trans(source = "voltageTierAdvNanoSuit",
               translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "进阶纳米胸甲电压等级"))
        public int voltageTierAdvNanoSuit = 3;
        @Configurable
        @Configurable.Comment({ "QuarkTech Suit Voltage Tier.", "Default: 5 (IV)" })
        @Configurable.Range(min = 0, max = 14)
        @Trans(source = "voltageTierQuarkTech",
               translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "夸克套电压等级"))
        public int voltageTierQuarkTech = 5;
        @Configurable
        @Configurable.Comment({ "Advanced QuarkTech Suit Chestplate Voltage Tier.", "Default: 6 (LuV)" })
        @Configurable.Range(min = 0, max = 14)
        @Trans(source = "voltageTierAdvQuarkTech",
               translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "进阶夸克胸甲电压等级"))
        public int voltageTierAdvQuarkTech = 6;
        @Configurable
        @Configurable.Comment({ "Electric Impeller Jetpack Voltage Tier.", "Default: 2 (MV)" })
        @Configurable.Range(min = 0, max = 14)
        @Trans(source = "voltageTierImpeller",
               translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "喷气背包电压等级"))
        public int voltageTierImpeller = 2;
        @Configurable
        @Configurable.Comment({ "Advanced Electric Jetpack Voltage Tier.", "Default: 3 (HV)" })
        @Configurable.Range(min = 0, max = 14)
        @Trans(source = "voltageTierAdvImpeller",
               translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "进阶喷气背包电压等级"))
        public int voltageTierAdvImpeller = 3;

        @TransVariants(keyPrefix = OPTION_KEY_PREFIX)
        public static class NanoSaber {

            @Configurable
            @Configurable.DecimalRange(min = 0, max = 100)
            @Configurable.Comment({ "The additional damage added when the NanoSaber is powered.", "Default: 20.0" })
            @Trans(source = "nanoSaberDamageBoost",
                   translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "纳米剑伤害增幅"))
            public double nanoSaberDamageBoost = 20;
            @Configurable
            @Configurable.DecimalRange(min = 0, max = 100)
            @Configurable.Comment({ "The base damage of the NanoSaber.", "Default: 5.0" })
            @Trans(source = "nanoSaberBaseDamage",
                   translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "纳米剑基础伤害"))
            public double nanoSaberBaseDamage = 5;
            @Configurable
            @Configurable.Comment({ "Should Zombies spawn with charged, active NanoSabers on hard difficulty?",
                    "Default: true" })
            @Trans(source = "zombieSpawnWithSabers",
                   translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "带纳米剑的僵尸生成"))
            public boolean zombieSpawnWithSabers = true;
            @Configurable
            @Configurable.Range(min = 1, max = 512)
            @Configurable.Comment({ "The EU/t consumption of the NanoSaber.", "Default: 64" })
            @Trans(source = "energyConsumption",
                   translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "能耗"))
            public int energyConsumption = 64;
        }
    }

    @TransVariants(keyPrefix = OPTION_KEY_PREFIX)
    public static class GameplayConfigs {

        @Configurable
        @Configurable.Comment({ "Enable hazardous materials", "Default: true" })
        @Trans(source = "hazardsEnabled",
               translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "启用危险物质"))
        public boolean hazardsEnabled = true;
        @Configurable
        @Configurable.Comment({ "Whether hazards are applied to all valid items, or just GT's.",
                "true = all, false = GT only.", "Default: true" })
        @Trans(source = "universalHazards",
               translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "普遍化的危险物质"))
        public boolean universalHazards = true;
        @Configurable
        @Configurable.Comment({ "Whether environmental hazards like pollution or radiation are active",
                "Default: false" })
        @Trans(source = "environmentalHazards",
               translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "环境污染"))
        public boolean environmentalHazards = false;
        @Configurable
        @Configurable.Comment({ "How much environmental hazards decay per chunk, per tick.",
                "Default: 0.001" })
        @Trans(source = "environmentalHazardDecayRate",
               translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "环境污染衰减速率"))
        public float environmentalHazardDecayRate = 0.001f;
        @Configurable
        @Configurable.Comment({ "List of domains that are allowed in the image module" })
        @Trans(source = "allowedImageDomains",
               translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "图片域名白名单"))
        public String[] allowedImageDomains = new String[] { "imgur.com", "discord.com", "github.com",
                "raw.githubusercontent.com" };
    }

    @TransVariants(keyPrefix = OPTION_KEY_PREFIX)
    public static class ClientConfigs {

        @Configurable
        @Configurable.Comment({ "Whether or not to display all temperatures in Celsius instead of Kelvin" })
        @Trans(source = "temperaturesInCelsius",
               translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "使用摄氏度"))
        public boolean temperaturesInCelsius = false;
        @Configurable
        @Configurable.Comment({ "Whether or not to enable Emissive Textures for GregTech Machines.", "Default: true" })
        @Trans(source = "machinesEmissiveTextures",
               translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "启用机器泛光纹理"))
        public boolean machinesEmissiveTextures = true;
        @Configurable
        @Configurable.Comment({
                "Whether most machines will have block entity renderers, mainly used for rendering certain covers. (Restart required)",
                "Disable if experiencing performance issues.", "Default: true" })
        @Trans(source = "machinesHaveBERsByDefault",
               translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "机器是否默认启用方块实体渲染"))
        public boolean machinesHaveBERsByDefault = true;
        @Configurable
        @Configurable.Comment({ "Whether or not sounds should be played when using tools outside of crafting.",
                "Default: true" })
        @Trans(source = "toolUseSounds",
               translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "使用工具时播放声音"))
        public boolean toolUseSounds = true;
        @Configurable
        @Configurable.Comment({ "Whether or not sounds should be played when crafting with tools.", "Default: true" })
        @Trans(source = "toolCraftingSounds",
               translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "使用工具合成时播放声音"))
        public boolean toolCraftingSounds = true;
        @Configurable
        @Configurable.Comment({ "The default color to overlay onto machines.",
                "#FFFFFF is no coloring (default).",
                "#D2DCFF is the classic blue from GT5." })
        @Configurable.StringPattern(value = "#[0-9a-fA-F]{1,6}")
        @Configurable.Gui.ColorValue
        @Trans(source = "defaultPaintingColor",
               translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "机器默认喷漆颜色"))
        public String defaultPaintingColor = "#FFFFFF";
        @Configurable
        @Configurable.Comment({ "The default color to overlay onto Machine (and other) UIs.",
                "#FFFFFF is no coloring (like GTCE) (default).",
                "#D2DCFF is the classic blue from GT5." })
        @Configurable.StringPattern(value = "#[0-9a-fA-F]{1,6}")
        @Configurable.Gui.ColorValue
        @Trans(source = "defaultUIColor",
               translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "默认UI颜色"))
        public String defaultUIColor = "#FFFFFF";
        @Configurable
        @Configurable.Comment({ "Use VBO cache for multiblock preview.",
                "Disable if you have issues with rendering multiblocks.", "Default: true" })
        @Trans(source = "useVBO",
               translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "使用VBO"))
        public boolean useVBO = true;
        @Configurable
        @Configurable.Comment({ "Duration of the multiblock in-world preview (s)", "Default: 10" })
        @Configurable.Range(min = 1, max = 999)
        @Trans(source = "inWorldPreviewDuration",
               translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "世界中预览时长"))
        public int inWorldPreviewDuration = 10;
        @Configurable
        @Configurable.Comment({ "Duration of UI animations in ms", "Default: 300" })
        @Configurable.Range(min = 1)
        @Trans(source = "animationTime",
               translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "动画时间"))
        public int animationTime = 300;
        @Configurable
        @Trans(source = "armorHud",
               translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "盔甲HUD"))
        public ArmorHud armorHud = new ArmorHud();
        @Configurable
        @Trans(source = "renderer",
               translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "渲染器"))
        public RendererOptions renderer = new RendererOptions();
        @Configurable
        @Configurable.Comment("Config options for bloom and other post-processing effects")
        @Trans(source = "bloom",
               translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "泛光"))
        public BloomOptions bloom = new BloomOptions();
        @Configurable
        @Trans(source = "tankItemFluidPreview",
               translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "储罐流体预览"))
        public TankItemFluidPreview tankItemFluidPreview = new TankItemFluidPreview();

        public int getDefaultPaintingColor() {
            // OR with full alpha to differentiate from a machine that's painted white (map color 0xffffff)
            return Long.decode(defaultPaintingColor).intValue() | 0xff000000;
        }

        @TransVariants(keyPrefix = OPTION_KEY_PREFIX)
        public static class ArmorHud {

            @Configurable
            @Configurable.Comment({ "Sets HUD location", "1 - left-upper corner", "2 - right-upper corner",
                    "3 - left-bottom corner", "4 - right-bottom corner", "Default: 1" })
            @Configurable.Range(min = 1, max = 4)
            @Trans(source = "hudLocation",
                   translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "HUD位置"))
            public int hudLocation = 1;
            @Configurable
            @Configurable.Comment({ "Horizontal offset of HUD.", "Default: 0" })
            @Configurable.Range(min = 0, max = 100)
            @Trans(source = "hudOffsetX",
                   translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "HUD偏移X"))
            public int hudOffsetX = 0;
            @Configurable
            @Configurable.Comment({ "Vertical offset of HUD.", "Default: 0" })
            @Configurable.Range(min = 0, max = 100)
            @Trans(source = "hudOffsetY",
                   translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "HUD偏移Y"))
            public int hudOffsetY = 0;
        }

        @TransVariants(keyPrefix = OPTION_KEY_PREFIX)
        public static class BloomOptions {

            @Configurable
            @Configurable.Comment({ "Bloom Algorithm",
                    "Requires reloading all chunks ",
                    "UNITY - Unity-like Bloom (rescale)",
                    "UNREAL - Unreal-like Bloom (gaussian blur)",
                    "DISABLED - No bloom",
                    "Default: UNREAL" })
            // @Configurable.Validator(BloomEventListeners.BloomTypeUpdateCallback.class) // for Configuration 4.x
            @Trans(source = "type",
                   translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "泛光算法"))
            public BloomType type = BloomType.UNREAL;

            @Configurable
            @Configurable.Comment({ "Whether or not to add bloom to emissive textures", "Default: true" })
            @Trans(source = "emissiveTexturesHaveBloom",
                   translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "自发光纹理启用泛光"))
            public boolean emissiveTexturesHaveBloom = true;

            @Configurable
            @Configurable.Comment({
                    "The brightness after bloom should not exceed this value. It can be used to limit the brightness of highlights (e.g., daytime)",
                    "This value should be greater than minBrightness.",
                    "OUTPUT = BACKGROUND + BLOOM * strength * (base + min + (1 - BACKGROUND_BRIGHTNESS) * ({max} - min)))",
                    "Default: 0.5" })
            @Configurable.DecimalRange(min = 0)
            @Trans(source = "maxBrightness",
                   translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "泛光最高亮度"))
            public float maxBrightness = 0.5f;

            @Configurable
            @Configurable.Comment({
                    "The brightness after bloom should not smaller than this value. It can be used to limit the brightness of dusky parts (e.g., night/caves)",
                    "This value should be lower than maxBrightness.",
                    "OUTPUT = BACKGROUND + BLOOM * strength * (base + {min} + (1 - BACKGROUND_BRIGHTNESS) * (max - {min})))",
                    "Default: 0.2" })
            @Configurable.DecimalRange(min = 0)
            @Trans(source = "minBrightness",
                   translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "泛光最低亮度"))
            public float minBrightness = 0.2f;

            @Configurable
            @Configurable.Comment({ "The base brightness of the bloom.", "It is similar to strength",
                    "This value should be lower than maxBrightness.",
                    "OUTPUT = BACKGROUND + BLOOM * strength * ({base} + min + (1 - BACKGROUND_BRIGHTNESS) * (max - min)))",
                    "Default: 0.1" })
            @Configurable.DecimalRange(min = 0)
            @Trans(source = "baseBrightness",
                   translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "泛光基础亮度"))
            public float baseBrightness = 0.1f;

            @Configurable
            @Configurable.Comment({ "Bloom Strength",
                    "OUTPUT = BACKGROUND + BLOOM * {strength} * (base + min + (1 - BACKGROUND_BRIGHTNESS) * (max - min)))",
                    "Default: 1.5" })
            @Configurable.DecimalRange(min = 0)
            @Trans(source = "strength",
                   translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "泛光强度"))
            public float strength = 1.5f;

            @Configurable
            @Configurable.Comment({ "Blur Step (bloom range)", "Default: 1" })
            @Configurable.DecimalRange(min = 0)
            @Trans(source = "step",
                   translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "模糊步长"))
            public float step = 1.0f;
        }

        @TransVariants(keyPrefix = OPTION_KEY_PREFIX)
        public static class RendererOptions {

            @Configurable
            @Configurable.Comment({ "Render fluids in multiblocks that support them?", "Default: true" })
            @Trans(source = "renderFluids",
                   translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "渲染流体"))
            public boolean renderFluids = true;

            @Configurable
            @Configurable.Comment({ "Render growing plants in multiblocks that support them?", "Default: true" })
            @Trans(source = "renderGrowingPlants",
                   translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "渲染作物"))
            public boolean renderGrowingPlants = true;

            @Configurable
            @Configurable.Comment({ "Whether or not to color material/ore block highlights in the material color",
                    "Default: true" })
            @Trans(source = "coloredMaterialBlockOutline",
                   translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "材料块的彩色描边"))
            public boolean coloredMaterialBlockOutline = true;

            @Configurable
            @Configurable.Comment({ "Whether or not to color tiered machine highlights in the tier color",
                    "Default: true" })
            @Trans(source = "coloredTieredMachineOutline",
                   translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "彩色电力机器轮廓"))
            public boolean coloredTieredMachineOutline = true;

            @Configurable
            @Configurable.Comment({
                    "Whether or not to color wire/cable highlights based on voltage tier or material color",
                    "Default: true" })
            @Trans(source = "coloredWireOutline",
                   translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "彩色线缆轮廓"))
            public boolean coloredWireOutline = true;
        }

        @TransVariants(keyPrefix = OPTION_KEY_PREFIX)
        public static class TankItemFluidPreview {

            @Configurable
            @Configurable.Comment({ "Set true to render the including fluid icons to GT Drums" })
            @Trans(source = "drum",
                   translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "桶"))
            public boolean drum = false;
            @Configurable
            @Configurable.Comment({ "Set true to render the including fluid icons to Super (Quantum) Tanks" })
            @Trans(source = "quantumTank",
                   translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "超级/量子缸"))
            public boolean quantumTank = false;
        }
    }

    @TransVariants(keyPrefix = OPTION_KEY_PREFIX)
    public static class DeveloperConfigs {

        @Configurable
        @Configurable.Comment({ "Debug general events? (will print recipe conficts etc. to server's debug.log)",
                "Default: false" })
        @Trans(source = "debug",
               translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "调试"))
        public boolean debug = false;
        @Configurable
        @Configurable.Comment({ "Debug ore vein placement? (will print placed veins to server's debug.log)",
                "Default: false (no placement printout in debug.log)" })
        @Trans(source = "debugWorldgen",
               translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "矿石位置Debug模式"))
        public boolean debugWorldgen = false;
        @Configurable
        @Configurable.Comment({ "Generate ores in superflat worlds?", "Default: false" })
        @Trans(source = "doSuperflatOres",
               translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "超平坦世界生成矿石"))
        public boolean doSuperflatOres = false;
        @Configurable
        @Configurable.Comment({ "Dump all registered GT recipes?", "Default: false" })
        @Trans(source = "dumpRecipes",
               translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "导出配方"))
        public boolean dumpRecipes = false;
        @Configurable
        @Configurable.Comment({ "Dump all registered GT models/blockstates/etc?", "Default: false" })
        @Trans(source = "dumpAssets",
               translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "导出资源"))
        public boolean dumpAssets = false;
        @Configurable
        @Configurable.Comment({ "Executes ./gradlew :processResources when F3+T is pressed",
                "Only works in a development environment", "Default: false" })
        @Trans(source = "autoRebuildResources",
               translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "自动重建资源"))
        public boolean autoRebuildResources = false;
    }
}
