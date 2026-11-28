package com.modularmc.gtpm.common.data.machines.tooltip;

import com.modularmc.registrate.localization.MinecraftLocale;
import com.modularmc.registrate.localization.annotation.Trans;
import com.modularmc.registrate.localization.annotation.TransVariants;
import com.modularmc.registrate.localization.annotation.Translation;
import com.modularmc.registrate.localization.annotation.Variant;

/** Machine description and primary tooltip translations, keyed by machine ID. */
final class MachineDescriptionTooltips {

    private MachineDescriptionTooltips() {}

    @TransVariants(keyPrefix = "gtpm.machine",
                   value = {
                           @Variant(id = "locked_safe.malfunctioning",
                                    source = "§cMalfunctioning!",
                                    translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§c故障中！")),
                           @Variant(id = "locked_safe.requirements",
                                    source = "§7Replacements required:",
                                    translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7需要更换以下部件：")),
                           @Variant(id = "workbench.tooltip.0",
                                    source = "Better than Forestry",
                                    translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "比林业的好")),
                           @Variant(id = "workbench.tooltip.1",
                                    source = "Has Item Storage, Tool Storage, pulls from adjacent Inventories, and saves Recipes.",
                                    translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                                text = "具有物品存储、工具存储、从相邻的存储容器中读取物品和保存配方功能。")),
                           @Variant(id = "workbench.tab.workbench",
                                    source = "Crafting",
                                    translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "合成")),
                           @Variant(id = "workbench.tab.item_list",
                                    source = "Storage",
                                    translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "存储空间")),
                           @Variant(id = "workbench.tab.crafting",
                                    source = "Crafting",
                                    translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "合成")),
                           @Variant(id = "workbench.tab.container",
                                    source = "Container",
                                    translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "存储")),
                           @Variant(id = "workbench.storage_note.0",
                                    source = "(Available items from connected",
                                    translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "（相邻的物品容器中")),
                           @Variant(id = "workbench.storage_note.1",
                                    source = "inventories usable for crafting)",
                                    translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "可参与合成的物品）")),
                   })
    private static final class LegacyMachineTranslations {}

    @Trans(source = "§7An early way to get Steam Power",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7获取蒸汽能源的早期手段"))
    static final String LP_STEAM_SOLID_BOILER = "gtpm.machine.lp_steam_solid_boiler.tooltip";

    @Trans(source = "§7Faster than the Small Steam Solid Fuel Boiler",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7比小型固体燃料锅炉更快"))
    static final String HP_STEAM_SOLID_BOILER = "gtpm.machine.hp_steam_solid_boiler.tooltip";

    @Trans(source = "§7Steam Power by Sun",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7来自太阳的蒸汽能源"))
    static final String LP_STEAM_SOLAR_BOILER = "gtpm.machine.lp_steam_solar_boiler.tooltip";

    @Trans(source = "§7Steam Power by Sun",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7来自太阳的蒸汽能源"))
    static final String HP_STEAM_SOLAR_BOILER = "gtpm.machine.hp_steam_solar_boiler.tooltip";

    @Trans(source = "§7A Boiler running off Liquids",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7消耗液体的锅炉"))
    static final String LP_STEAM_LIQUID_BOILER = "gtpm.machine.lp_steam_liquid_boiler.tooltip";

    @Trans(source = "§7Faster than Small Steam Liquid Boiler",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7比小型液体燃料锅炉更快"))
    static final String HP_STEAM_LIQUID_BOILER = "gtpm.machine.hp_steam_liquid_boiler.tooltip";

    @Trans(source = "§7Extracting your first Rubber",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7提取你的第一块橡胶"))
    static final String LP_STEAM_EXTRACTOR = "gtpm.machine.lp_steam_extractor.tooltip";

    @Trans(source = "§7Extracting your first Rubber",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7提取你的第一块橡胶"))
    static final String HP_STEAM_EXTRACTOR = "gtpm.machine.hp_steam_extractor.tooltip";

    @Trans(source = "§7Macerating your Ores without Byproducts",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7粉碎你的矿石，但没有副产物"))
    static final String LP_STEAM_MACERATOR = "gtpm.machine.lp_steam_macerator.tooltip";

    @Trans(source = "§7Macerating your Ores without Byproducts",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7粉碎你的矿石，但没有副产物"))
    static final String HP_STEAM_MACERATOR = "gtpm.machine.hp_steam_macerator.tooltip";

    @Trans(source = "§7Compressing Items",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7压缩物品"))
    static final String LP_STEAM_COMPRESSOR = "gtpm.machine.lp_steam_compressor.tooltip";

    @Trans(source = "§7Compressing Items",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7压缩物品"))
    static final String HP_STEAM_COMPRESSOR = "gtpm.machine.hp_steam_compressor.tooltip";

    @Trans(source = "§7Forge Hammer",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7锻造锤"))
    static final String LP_STEAM_FORGE_HAMMER = "gtpm.machine.lp_steam_forge_hammer.tooltip";

    @Trans(source = "§7Forge Hammer",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7锻造锤"))
    static final String HP_STEAM_FORGE_HAMMER = "gtpm.machine.hp_steam_forge_hammer.tooltip";

    @Trans(source = "§7Smelting things with compressed Steam",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7利用蒸汽冶炼物品"))
    static final String LP_STEAM_FURNACE = "gtpm.machine.lp_steam_furnace.tooltip";

    @Trans(source = "§7Smelting things with compressed Steam",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7利用蒸汽冶炼物品"))
    static final String HP_STEAM_FURNACE = "gtpm.machine.hp_steam_furnace.tooltip";

    @Trans(source = "§7Combination Smelter",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7融合冶炼炉"))
    static final String LP_STEAM_ALLOY_SMELTER = "gtpm.machine.lp_steam_alloy_smelter.tooltip";

    @Trans(source = "§7Combination Smelter",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7融合冶炼炉"))
    static final String HP_STEAM_ALLOY_SMELTER = "gtpm.machine.hp_steam_alloy_smelter.tooltip";

    @Trans(source = "§7Place Water and Lava horizontally adjacent",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7在水平相邻处放置熔岩和水"))
    static final String LP_STEAM_ROCK_CRUSHER = "gtpm.machine.lp_steam_rock_crusher.tooltip";

    @Trans(source = "§7Place Water and Lava horizontally adjacent",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7在水平相邻处放置熔岩和水"))
    static final String HP_STEAM_ROCK_CRUSHER = "gtpm.machine.hp_steam_rock_crusher.tooltip";

    @Trans(source = "§7Mines ores below the Miner!",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7只挖掘机器下方的矿石！"))
    static final String STEAM_MINER = "gtpm.machine.steam_miner.tooltip";

    @Trans(source = "§7Requires flammable Liquids",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7需要液体燃料"))
    static final String COMBUSTION_GENERATOR = "gtpm.machine.combustion_generator.tooltip";

    @Trans(source = "§7Converts Steam into EU",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7将蒸汽转化为 EU"))
    static final String STEAM_TURBINE = "gtpm.machine.steam_turbine.tooltip";

    @Trans(source = "§7Requires flammable Gases",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7需要气体燃料"))
    static final String GAS_TURBINE = "gtpm.machine.gas_turbine.tooltip";

    @Trans(source = "Hit with a Soft Mallet to change Amperage flow.",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "用软锤右击以改变电流。"))
    static final String DIODE_TOOLTIP_TOOL_USAGE = "gtpm.machine.diode.tooltip_tool_usage";

    @Trans(source = "§f%d §7ticks per Bucket",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "每桶§f%d§7tick"))
    static final String PUMP_TOOLTIP_BUCKETS = "gtpm.machine.pump.tooltip_buckets";

    @Trans(source = "Just for filling space",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "只是为了填充空间"))
    static final String HPCA_EMPTY_COMPONENT = "gtpm.machine.hpca.empty_component.tooltip";

    @Trans(source = "Free cooling! Is anything free?",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "免费冷却！有免费的吗？"))
    static final String HPCA_HEAT_SINK_COMPONENT = "gtpm.machine.hpca.heat_sink_component.tooltip";

    @Trans(source = "Less free, more effective cooling",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "不再免费，但效果更好"))
    static final String HPCA_ACTIVE_COOLER_COMPONENT = "gtpm.machine.hpca.active_cooler_component.tooltip";

    @Trans(source = "Baby's first computation",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "宝宝的第一次计算"))
    static final String HPCA_COMPUTATION_COMPONENT = "gtpm.machine.hpca.computation_component.tooltip";

    @Trans(source = "Computation big leagues",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "计算大联盟"))
    static final String HPCA_ADVANCED_COMPUTATION_COMPONENT = "gtpm.machine.hpca.advanced_computation_component.tooltip";

    @Trans(source = "So that's where the \"Array\" in HPCA comes from",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "这就是高性能计算阵列中“阵列”一词的来源"))
    static final String HPCA_BRIDGE_COMPONENT = "gtpm.machine.hpca.bridge_component.tooltip";

    @Trans(source = "Free recyclable materials",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "免费的可回收材料"))
    static final String HPCA_COMPUTATION_COMPONENT_DAMAGED = "gtpm.machine.hpca.computation_component.damaged.tooltip";

    @Trans(source = "It only cost an arm and a leg",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "只花了一只胳膊和一条腿"))
    static final String HPCA_ADVANCED_COMPUTATION_COMPONENT_DAMAGED = "gtpm.machine.hpca.advanced_computation_component.damaged.tooltip";

    @Trans(source = "§7Allows automation access for the Coke Oven.",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7允许自动化焦炉的物品吞吐。"))
    static final String COKE_OVEN_HATCH = "gtpm.machine.coke_oven_hatch.tooltip";

    @Trans(source = "Converts logs into Brittle Charcoal in a 9x4x9 area beneath it.",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "将下方最大9x4x9区域内的原木烧制成脆木炭块。"))
    static final String CHARCOAL_PILE_DESCRIPTION_0 = "gtpm.multiblock.charcoal_pile.description.0";

    @Trans(source = "",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = ""))
    static final String CHARCOAL_PILE_DESCRIPTION_1 = "gtpm.multiblock.charcoal_pile.description.1";

    @Trans(source = "The floor of the pit must be made from bricks, and any ground-related block can be used for the walls and roof. No air can be inside the pit.",
           translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                       text = "木炭堆底部必须由砖块构成，壁面和顶面则可选用泥土、沙子等任何土地类方块。堆内必须填满，不可存在空气。"))
    static final String CHARCOAL_PILE_DESCRIPTION_2 = "gtpm.multiblock.charcoal_pile.description.2";

    @Trans(source = "",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = ""))
    static final String CHARCOAL_PILE_DESCRIPTION_3 = "gtpm.multiblock.charcoal_pile.description.3";

    @Trans(source = "Larger pits take more time to process logs, but are more efficient.",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "木炭堆的尺寸越大，所需的处理时间就越长，但效率更高。"))
    static final String CHARCOAL_PILE_DESCRIPTION_4 = "gtpm.multiblock.charcoal_pile.description.4";

    @Trans(source = "This is a machine that monitors machines proxied by the Digital Interface Cover. You can easily monitor the Fluids, Items, Energy, and States of machines proxied in Energy Network.",
           translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                       text = "这台机器可以监控由数字接口覆盖板代理的机器。您可以方便地监视能源网络中代理的机器的流体、物品、能量和状态。"))
    static final String CENTRAL_MONITOR_0 = "gtpm.multiblock.central_monitor.tooltip.0";

    @Trans(source = "You can build the central monitor screen from 3X2 to %dX%d (width X height).",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "监控器屏幕的搭建尺寸为3x2到%dx%d （宽x高）。"))
    static final String CENTRAL_MONITOR_1 = "gtpm.multiblock.central_monitor.tooltip.1";

    @Trans(source = "The default height is 3. You can adjust the screen height in the GUI before the structure is formed.",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "默认高度为3，在结构形成之前，您可以在GUI中调整屏幕高度。"))
    static final String CENTRAL_MONITOR_2 = "gtpm.multiblock.central_monitor.tooltip.2";

    @Trans(source = "Energy consumption: %d EU/s for each screen.",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "能源消耗：每块屏幕%d EU/s。"))
    static final String CENTRAL_MONITOR_3 = "gtpm.multiblock.central_monitor.tooltip.3";

    @Trans(source = "The GUI can be opened with a right-click of a screwdriver.",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "用螺丝刀右击即可打开GUI。"))
    static final String MONITOR_SCREEN_0 = "gtpm.multiblock.monitor_screen.tooltip.0";

    @Trans(source = "The proxy mode of Digital Interface Cover can delegate machines' capabilities and GUI. (Yes, you can connect pipes directly on the screen.)",
           translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                       text = "数字接口覆盖板的代理模式可托管机器的功能与界面（是的，你可以在屏幕上直接连接管道）。"))
    static final String MONITOR_SCREEN_1 = "gtpm.multiblock.monitor_screen.tooltip.1";

    @Trans(source = "The screen also supports plugins.",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "该屏幕也支持插件。"))
    static final String MONITOR_SCREEN_2 = "gtpm.multiblock.monitor_screen.tooltip.2";

    @Trans(source = "A Multiblock Macerator at the Steam Age. Requires at least 14 Bronze Casings to form. Cannot use normal Input/Output busses, nor Fluid Hatches other than the Steam Hatch.",
           translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                       text = "蒸汽时代的多方块研磨机。需要至少14块青铜机械方块成型。仅可使用输入/输出总线（蒸汽），并且只能用蒸汽仓供给蒸汽。"))
    static final String STEAM_GRINDER_DESCRIPTION = "gtpm.multiblock.steam_grinder.description";

    @Trans(source = "A Multi Smelter at the Steam Age. Requires at least 6 Bronze Casings to form. Cannot use normal Input/Output busses, nor Fluid Hatches other than the Steam Hatch. Steam Hatch must be on the bottom layer, no more than one.",
           translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                       text = "蒸汽时代的多方块熔炉。需要至少6块青铜机械方块成型。仅可使用输入/输出总线（蒸汽），并且只能用蒸汽仓供给蒸汽。蒸汽仓必须放置于结构底层，数量至多为一个。"))
    static final String STEAM_OVEN_DESCRIPTION = "gtpm.multiblock.steam_oven.description";

    @Trans(source = "The Primitive Blast Furnace (PBF) is a multiblock structure used for cooking steel in the early game. Although not very fast, it will provide you with steel for your first setups.",
           translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                       text = "土高炉是一种在游戏前期用来生产钢的多方块结构。尽管它不是很快，却能为你的发展提供钢材。"))
    static final String PRIMITIVE_BLAST_FURNACE_BRONZE_DESCRIPTION = "gtpm.multiblock.primitive_blast_furnace.bronze.description";

    @Trans(source = "The Coke Oven is a multiblock structure used for getting coke and creosote in the early game. It doesn't require fuel and has an internal tank of 32 buckets for creosote. Its inventory can be accessed via its Coke Oven Hatch.",
           translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                       text = "焦炉是一种多方块结构，用于在早期生产焦煤和杂酚油，无需燃料即可工作，内部至多可容纳 32 桶杂酚油。其存储可通过焦炉仓进行访问。"))
    static final String COKE_OVEN_DESCRIPTION = "gtpm.multiblock.coke_oven.description";

    @Trans(source = "The Vacuum Freezer is a multiblock structure mainly used for freezing Hot Ingots into regular Ingots. However, it can also freeze other substances, such as Water.",
           translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                       text = "真空冷冻机是一种多方块结构，主要用于将热锭冷却为成品锭。此外，它还可以冻结水等其他物质。"))
    static final String VACUUM_FREEZER_DESCRIPTION = "gtpm.multiblock.vacuum_freezer.description";

    @Trans(source = "The Implosion Compressor is a multiblock structure that uses explosives to turn gem dusts into their corresponding gems.",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "聚爆压缩机是一种多方块结构，能够借助炸药将宝石粉转化为相应的宝石。"))
    static final String IMPLOSION_COMPRESSOR_DESCRIPTION = "gtpm.multiblock.implosion_compressor.description";

    @Trans(source = "The Pyrolyse Oven is a multiblock structure used for turning Logs into Charcoal and Creosote Oil, or Ash and Heavy Oil.",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "热解炉是一种用于将原木处理为木炭、杂酚油、灰烬或重油的多方块结构。"))
    static final String PYROLYSE_OVEN_DESCRIPTION = "gtpm.multiblock.pyrolyse_oven.description";

    @Trans(source = "The Oil Cracking Unit is a multiblock structure used for turning Light and Heavy Fuel into their Cracked variants.",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "裂化机是一种用于将轻燃油和重燃油裂化为相应裂化物的多方块结构。"))
    static final String CRACKER_DESCRIPTION = "gtpm.multiblock.cracker.description";

    @Trans(source = "The Large Combustion Engine is a multiblock structure that acts as a Combustion Generator for EV power.",
           translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                       text = "大型内燃引擎是一种产出§5EV§r能量的多方块结构，工作方式与内燃发电机类似。"))
    static final String LARGE_COMBUSTION_ENGINE_DESCRIPTION = "gtpm.multiblock.large_combustion_engine.description";

    @Trans(source = "The Extreme Combustion Engine is a multiblock structure that acts as a Combustion Generator for IV power.",
           translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                       text = "极限内燃引擎是一种产出§9IV§r能量的多方块结构，工作方式与内燃发电机类似。"))
    static final String EXTREME_COMBUSTION_ENGINE_DESCRIPTION = "gtpm.multiblock.extreme_combustion_engine.description";

    @Trans(source = "The Distillation Tower is a multiblock structure used for distilling the various types of Oil and some of their byproducts. Each layer must have exactly one output hatch, starting from the second one. The bottom layer can output items and insert fluids in any position.",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "蒸馏塔是一种用于蒸馏不同石油及其副产品的多方块结构。"))
    static final String DISTILLATION_TOWER_DESCRIPTION = "gtpm.multiblock.distillation_tower.description";

    @Trans(source = "The Electric Blast Furnace (EBF) is a multiblock structure used for smelting alloys, cooking metals and refining ores. It is required for obtaining high-tier alloys and metals, such as aluminium, stainless steel, titanium, and naquadah alloy.",
           translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                       text = "电力高炉是一种用来熔炼合金、熔化金属或冶炼矿石的多方块结构。也可用于获取高级的合金和金属，例如铝、不锈钢、钛或硅岩合金。"))
    static final String ELECTRIC_BLAST_FURNACE_DESCRIPTION = "gtpm.multiblock.electric_blast_furnace.description";

    @Trans(source = "The Multi Smelter is a multiblock structure used for smelting massive amounts of items at once. Different tiers of coils provide a speed boost and energy efficiency gain. 32 is the base value of items smelted per operation, and can be multiplied by using higher level coils.",
           translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                       text = "工业熔炉是一种用于一次性烧制大量物品的多方块结构。不同级别的线圈会提供速度增幅和能量效率增幅。每次运作的基础烧制数量为32，且可以使用高等级的线圈增加烧制数量。"))
    static final String MULTI_FURNACE_DESCRIPTION = "gtpm.multiblock.multi_furnace.description";

    @Trans(source = "Large Boilers are multiblocks that generate steam from an energy source and water. Said energy source is either any Solid Fuel with a Burn Time, or a Diesel/Semi-Fluid Fuel. Can be throttled back in increments of 5%% to reduce Steam output and Fuel consumption.",
           translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                       text = "大型锅炉是种用水和能量源生产蒸汽的多方块结构。这里说的“能量源”通常是指任何可以烧的固体燃料，或者柴油/半流质燃料。能够以每次5%%的幅度节流来减少蒸汽产出和燃料消耗。"))
    static final String LARGE_BOILER_DESCRIPTION = "gtpm.multiblock.large_boiler.description";

    @Trans(source = "Large Turbines are multiblocks that generate power from steam, gases, and plasma by having them spin the turbine's rotor. Energy output is based on rotor efficiency and current speed of turbine. Gearbox casings are used in the center of the structure.",
           translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                       text = "大型涡轮是一种使用蒸汽、燃气或等离子体转动涡轮转子来发电的多方块结构。转子效率和转子转速影响能量的输出。"))
    static final String LARGE_TURBINE_DESCRIPTION = "gtpm.multiblock.large_turbine.description";

    @Trans(source = "The Assembly Line is a large multiblock structure consisting of 5 to 16 \"slices\". In theory, it's large Assembling Machine, used for creating advanced crafting components.",
           translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                       text = "装配线是由5到16“片”组成的大型多方块结构。理论上，它是一个大型组装机，用于生产高级元件。"))
    static final String ASSEMBLY_LINE_DESCRIPTION = "gtpm.multiblock.assembly_line.description";

    @Trans(source = "The Fusion Reactor MK 1 is a large multiblock structure used for fusing elements into heavier ones. It can only use LuV, ZPM, and UV Energy Hatches. For every Hatch it has, its buffer increases by 10M EU, and has a maximum of 160M.",
           translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                       text = "核聚变反应堆MK-I是台大型多方块结构，用于融合元素形成更重的元素。它仅可使用LuV，ZPM或UV等级的能源仓。每个能源仓可增加10MEU的能量缓存，最大能量缓存为160MEU。"))
    static final String LUV_FUSION_REACTOR_DESCRIPTION = "gtpm.multiblock.luv_fusion_reactor.description";

    @Trans(source = "The Fusion Reactor MK 2 is a large multiblock structure used for fusing elements into heavier ones. It can only use ZPM and UV Energy Hatches. For every Hatch it has, its buffer increases by 20M EU, and has a maximum of 320M.",
           translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                       text = "核聚变反应堆MK-II是台大型多方块结构，用于融合元素形成更重的元素。它仅可使用ZPM或UV等级的能源仓。每个能源仓可增加20MEU的能量缓存，最大能量缓存为320MEU。"))
    static final String ZPM_FUSION_REACTOR_DESCRIPTION = "gtpm.multiblock.zpm_fusion_reactor.description";

    @Trans(source = "The Fusion Reactor MK 3 is a large multiblock structure used for fusing elements into heavier ones. It can only use UV Energy Hatches. For every Hatch it has, its buffer increases by 40M EU, and has a maximum of 640M.",
           translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                       text = "核聚变反应堆MK-III是台大型多方块结构，用于融合元素形成更重的元素。它仅可使用UV等级的能源仓。每个能源仓可增加40MEU的能量缓存，最大能量缓存为640MEU。"))
    static final String UV_FUSION_REACTOR_DESCRIPTION = "gtpm.multiblock.uv_fusion_reactor.description";

    @Trans(source = "The Large Chemical Reactor performs chemical reactions at 100%% energy efficiency. Overclocks multiply both speed and energy by 4. The multiblock requires exactly 1 Cupronickel Coil Block, which must be placed adjacent to the PTFE Pipe casing located in the center.",
           translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                       text = "大型化学反应釜能够以 100%% 的能效进行化学反应。超频将使得处理速度与耗能提升 4 倍。该多方块结构需要在中心位置的聚四氟乙烯管道方块旁放置 1 个白铜线圈方块。"))
    static final String LARGE_CHEMICAL_REACTOR_DESCRIPTION = "gtpm.multiblock.large_chemical_reactor.description";

    @Trans(source = "The Primitive Water Pump is a pre-Steam Era multiblock that collects water once per second, depending on the Biome it is in. It can use a Pump, ULV, or LV Output Hatch, increasing the amount of water per tier. Follows the formula: Biome Coefficient * Hatch Multiplier.",
           translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                       text = "原始水泵是一台前蒸汽时代的多方块设施，每秒进行一次水资源收集，每秒的收集量取决于其所处的生物群系。水泵输出仓以及 §8ULV§r 和 §7LV§r 的输出仓均可用于该设施，输出仓的等级越高，水资源的产出量也越高。产出量遵循的公式为：生物群系系数 * 输出仓倍数。"))
    static final String PRIMITIVE_WATER_PUMP_DESCRIPTION = "gtpm.multiblock.primitive_water_pump.description";

    @Trans(source = "Biome Coefficient:",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "生物群系系数："))
    static final String PRIMITIVE_WATER_PUMP_EXTRA1_0 = "gtpm.multiblock.primitive_water_pump.extra1.0";

    @Trans(source = "  Ocean, River: 1000 mB/s",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "  海洋，河流：1000 mB/s"))
    static final String PRIMITIVE_WATER_PUMP_EXTRA1_1 = "gtpm.multiblock.primitive_water_pump.extra1.1";

    @Trans(source = "  Swamp: 800 mB/s",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "  沼泽：800 mB/s"))
    static final String PRIMITIVE_WATER_PUMP_EXTRA1_2 = "gtpm.multiblock.primitive_water_pump.extra1.2";

    @Trans(source = "  Jungle: 350 mB/s",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "  丛林：350 mB/s"))
    static final String PRIMITIVE_WATER_PUMP_EXTRA1_3 = "gtpm.multiblock.primitive_water_pump.extra1.3";

    @Trans(source = "  Snowy: 300 mB/s",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "  覆雪群系：300 mB/s"))
    static final String PRIMITIVE_WATER_PUMP_EXTRA1_4 = "gtpm.multiblock.primitive_water_pump.extra1.4";

    @Trans(source = "  Plains, Forest: 250 mB/s",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "  平原，森林：250 mB/s"))
    static final String PRIMITIVE_WATER_PUMP_EXTRA1_5 = "gtpm.multiblock.primitive_water_pump.extra1.5";

    @Trans(source = "  Taiga: 175 mB/s",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "  针叶林：175 mB/s"))
    static final String PRIMITIVE_WATER_PUMP_EXTRA1_6 = "gtpm.multiblock.primitive_water_pump.extra1.6";

    @Trans(source = "  Beach: 170 mB/s",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "  沙滩：170 mB/s"))
    static final String PRIMITIVE_WATER_PUMP_EXTRA1_7 = "gtpm.multiblock.primitive_water_pump.extra1.7";

    @Trans(source = "  Other: 100 mB/s",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "  其他：100 mB/s"))
    static final String PRIMITIVE_WATER_PUMP_EXTRA1_8 = "gtpm.multiblock.primitive_water_pump.extra1.8";

    @Trans(source = "Hatch Multipliers:",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "输出乘数："))
    static final String PRIMITIVE_WATER_PUMP_EXTRA2_0 = "gtpm.multiblock.primitive_water_pump.extra2.0";

    @Trans(source = "  Pump Hatch: 1x",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "  水泵仓：1x"))
    static final String PRIMITIVE_WATER_PUMP_EXTRA2_1 = "gtpm.multiblock.primitive_water_pump.extra2.1";

    @Trans(source = "  ULV Output Hatch: 2x",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§8ULV§r输出仓：2x"))
    static final String PRIMITIVE_WATER_PUMP_EXTRA2_2 = "gtpm.multiblock.primitive_water_pump.extra2.2";

    @Trans(source = "  LV Output Hatch: 4x",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7LV§r输出仓：4x"))
    static final String PRIMITIVE_WATER_PUMP_EXTRA2_3 = "gtpm.multiblock.primitive_water_pump.extra2.3";

    @Trans(source = "",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = ""))
    static final String PRIMITIVE_WATER_PUMP_EXTRA2_4 = "gtpm.multiblock.primitive_water_pump.extra2.4";

    @Trans(source = "While raining in the Pump's Biome, the total water production will be increased by 50%%.",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "当水泵所处的生物群系下雨时，总产水量将增加50%%。"))
    static final String PRIMITIVE_WATER_PUMP_EXTRA2_5 = "gtpm.multiblock.primitive_water_pump.extra2.5";

    @Trans(source = "Endervoir at Home",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "末影接口蓄水库-家用款"))
    static final String PRIMITIVE_WATER_PUMP = "gtpm.machine.primitive_water_pump.tooltip";

    @Trans(source = "Making your first Steel",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "为你带来第一块钢"))
    static final String PRIMITIVE_BLAST_FURNACE_BRONZE = "gtpm.machine.primitive_blast_furnace.bronze.tooltip";

    @Trans(source = "Where's the electric smoker?",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "电力高炉有了，那电力烟熏炉呢？"))
    static final String ELECTRIC_BLAST_FURNACE = "gtpm.machine.electric_blast_furnace.tooltip";

    @Trans(source = "Aluminium Ice Box",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "啊哈我的大冰箱！"))
    static final String VACUUM_FREEZER = "gtpm.machine.vacuum_freezer.tooltip";

    @Trans(source = "The only Machine you want to go Boom",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "唯一一台你想要爆一爆的机器"))
    static final String IMPLOSION_COMPRESSOR = "gtpm.machine.implosion_compressor.tooltip";

    @Trans(source = "Electric Coke Oven",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "电力焦炉"))
    static final String PYROLYSE_OVEN = "gtpm.machine.pyrolyse_oven.tooltip";

    @Trans(source = "Fluid Refinery",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "流体精炼厂"))
    static final String DISTILLATION_TOWER = "gtpm.machine.distillation_tower.tooltip";

    @Trans(source = "Just like the Oven at Home",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "就像家里的烤箱一样"))
    static final String MULTI_FURNACE = "gtpm.machine.multi_furnace.tooltip";

    @Trans(source = "Fuel Ignition Chamber",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "燃料点火室"))
    static final String LARGE_COMBUSTION_ENGINE = "gtpm.machine.large_combustion_engine.tooltip";

    @Trans(source = "Extreme Chemical Energy Releaser",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "极限化学能释放者"))
    static final String EXTREME_COMBUSTION_ENGINE = "gtpm.machine.extreme_combustion_engine.tooltip";

    @Trans(source = "Makes Oil useful",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "有它，石油才有用"))
    static final String CRACKER = "gtpm.machine.cracker.tooltip";

    @Trans(source = "Do not put your Head in it",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "别把头伸进去"))
    static final String LARGE_TURBINE_STEAM = "gtpm.machine.large_turbine.steam.tooltip";

    @Trans(source = "Not a Jet Engine",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "并不是喷气发动机"))
    static final String LARGE_TURBINE_GAS = "gtpm.machine.large_turbine.gas.tooltip";

    @Trans(source = "Plasma Energy Siphon",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "等离子能量虹吸器"))
    static final String LARGE_TURBINE_PLASMA = "gtpm.machine.large_turbine.plasma.tooltip";

    @Trans(source = "We need more Steam!",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "再多来点蒸汽！"))
    static final String LARGE_BOILER_BRONZE = "gtpm.machine.large_boiler.bronze.tooltip";

    @Trans(source = "Charcoal Incinerator",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "木炭吞噬者"))
    static final String LARGE_BOILER_STEEL = "gtpm.machine.large_boiler.steel.tooltip";

    @Trans(source = "Where's the Magic Super Fuel?",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "我超级魔法燃料呢？"))
    static final String LARGE_BOILER_TITANIUM = "gtpm.machine.large_boiler.titanium.tooltip";

    @Trans(source = "How do you even fuel this thing?",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "你手上有几座煤矿也不够它吃的吧？"))
    static final String LARGE_BOILER_TUNGSTENSTEEL = "gtpm.machine.large_boiler.tungstensteel.tooltip";

    @Trans(source = "Making better fuels for Steel and Power",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "为炼钢和发电提供更优质的燃料"))
    static final String COKE_OVEN = "gtpm.machine.coke_oven.tooltip";

    @Trans(source = "Not a multiblock Assembling Machine!",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "真不是什么多方块组装机！"))
    static final String ASSEMBLY_LINE = "gtpm.machine.assembly_line.tooltip";

    @Trans(source = "Atomic Alloy Smelter",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "原子级合金熔炼机"))
    static final String FUSION_REACTOR_LUV = "gtpm.machine.fusion_reactor.luv.tooltip";

    @Trans(source = "A SUN DOWN ON EARTH",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "一 轮 红 日 落 大 地"))
    static final String FUSION_REACTOR_ZPM = "gtpm.machine.fusion_reactor.zpm.tooltip";

    @Trans(source = "A WHITE DWARF DOWN ON YOUR BASE",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "化 作 白 矮 星 砸 向 你"))
    static final String FUSION_REACTOR_UV = "gtpm.machine.fusion_reactor.uv.tooltip";

    @Trans(source = "Black Box Reactor",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "黑盒反应器"))
    static final String LARGE_CHEMICAL_REACTOR = "gtpm.machine.large_chemical_reactor.tooltip";

    @Trans(source = "Not to be confused with Multi-Smelter",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "不要与工业熔炉混淆"))
    static final String STEAM_OVEN = "gtpm.machine.steam_oven.tooltip";

    @Trans(source = "A multiblock Macerator without the Byproducts",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "多方块研磨机，但是没有副产物"))
    static final String STEAM_GRINDER = "gtpm.machine.steam_grinder.tooltip";

    @Trans(source = "Digging Ore instead of You",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "替你挖矿"))
    static final String LARGE_MINER_EV = "gtpm.machine.large_miner.ev.tooltip";

    @Trans(source = "Biome Excavator",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "挖掉整个生物群系"))
    static final String LARGE_MINER_IV = "gtpm.machine.large_miner.iv.tooltip";

    @Trans(source = "Terrestrial Harvester",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "掀翻整片大陆"))
    static final String LARGE_MINER_LUV = "gtpm.machine.large_miner.luv.tooltip";

    @Trans(source = "But can it run Doom?",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "但它能运行《毁灭战士》吗？"))
    static final String CENTRAL_MONITOR = "gtpm.machine.central_monitor.tooltip";

    @Trans(source = "When a few Machines just doesn't cut it",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "当几台机器无法解决问题时"))
    static final String PROCESSING_ARRAY = "gtpm.machine.processing_array.tooltip";

    @Trans(source = "Parallelize the World",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "这个世界需要更多并行"))
    static final String ADVANCED_PROCESSING_ARRAY = "gtpm.machine.advanced_processing_array.tooltip";

    @Trans(source = "Keeping those pesky particles out",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "将那些讨厌的灰尘拒之门外"))
    static final String CLEANROOM = "gtpm.machine.cleanroom.tooltip";

    @Trans(source = "Underground fuel bakery",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "不烤面包烤燃料的地下面包房"))
    static final String CHARCOAL_PILE = "gtpm.machine.charcoal_pile.tooltip";

    @Trans(source = "The Data Bank is a multiblock structure used for sharing Assembly Line Research Data between multiple Assembly Lines. Additionally, it enables Assembly Lines to read more complex research data on Data Modules.",
           translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                       text = "数据库是一个用于在多个装配线间分享研究数据的多方块结构。此外，它也能让装配线读取数据模块里更复杂的研究数据。"))
    static final String DATA_BANK_DESCRIPTION = "gtpm.multiblock.data_bank.description";

    @Trans(source = "§7Transmitting power at distance",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7远距离发射能量"))
    static final String LASER_HATCH_SOURCE = "gtpm.machine.laser_hatch.source.tooltip";

    @Trans(source = "§7Receiving power from distance",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7远距离接收能量"))
    static final String LASER_HATCH_TARGET = "gtpm.machine.laser_hatch.target.tooltip";

    @Trans(source = "The Research Station is a multiblock structure used for researching much more complex Assembly Line Research Data. Any Research requiring a Data Orb or Data Module must be scanned in the Research Station. Requires Compute Work Units (CWU/t) to research recipes, which is supplied by High Performance Computing Arrays (HPCAs).",
           translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                       text = "研究站是个多方块结构，用于研究更复杂的装配线研究数据。任何需要数据球或数据模块的研究必须使用研究站而非扫描仪来扫描。研究站需要计算工作单位（CWU/t）来进行工作，这由高性能计算阵列（HPCA）提供。"))
    static final String RESEARCH_STATION_DESCRIPTION = "gtpm.multiblock.research_station.description";

    @Trans(source = "The Network Switch is a multiblock structure used for distributing Computation from many sources to many destinations. It can accept any number of Computation Data Reception or Transmission Hatches. It is necessary for Research Data which requires much higher Computation, as the Research Station can only accept one Computation Data Reception Hatch. HPCAs must have a Bridge Component for the Network Switch to be able to access their Computation.",
           translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                       text = "网络交换机是个多方块结构，可以接受任意数量的算力数据靶仓输入，并从任意数量的算力数据源仓输出。对于需要更高算力的研究数据配方，网络交换机的存在是必要的，这是因为研究站只接受一个算力数据靶仓。当与HPCA连接时，HPCA必须具有桥接组件才可以令网络交换机接收其算力。"))
    static final String NETWORK_SWITCH_DESCRIPTION = "gtpm.multiblock.network_switch.description";

    @Trans(source = "The High Performance Computing Array (HPCA) is a multiblock structure used for creating Compute Work Units (CWU/t) for more complex Assembly Line Research Data. The structure has a flexible 3x3 area which can be filled in any way with HPCA components. Different components can provide different amounts of Computation, Cooling, as well as Energy Cost, Coolant Cost, and Heat Production. When used with a Bridge Component, the HPCA can connect to Network Switches for combining and routing Computation from multiple sources to one or more destinations.",
           translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                       text = "高性能计算阵列（HPCA）是个多方块结构，用于为更复杂的装配线研究数据提供计算工作单元（CWU/t）。该结构中有3×3的区域可以以任意方式填充HPCA组件，不同的组件提供不同数量的算力，冷却与能源成本，并且影响热量的产出与冷却液的消耗。当存在桥接组件时，HPCA可以连接入网络交换机，让不同来源的算力整合并输出到一个或多个目的地。"))
    static final String HPCA_DESCRIPTION = "gtpm.multiblock.hpca.description";

    @Trans(source = "Transmitting power at distance",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "远距离发射能量"))
    static final String LASER_SOURCE_HATCH_0 = "gtpm.machine.laser_source_hatch.tooltip.0";

    @Trans(source = "§cLaser cables must be in a straight line!§7",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§c激光传导线缆必须直线摆放！§7"))
    static final String LASER_SOURCE_HATCH_1 = "gtpm.machine.laser_source_hatch.tooltip.1";

    @Trans(source = "Receiving power from distance",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "远距离接收能量"))
    static final String LASER_TARGET_HATCH_0 = "gtpm.machine.laser_target_hatch.tooltip.0";

    @Trans(source = "§cLaser cables must be in a straight line!§7",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§c激光传导线缆必须直线摆放！§7"))
    static final String LASER_TARGET_HATCH_1 = "gtpm.machine.laser_target_hatch.tooltip.1";

    @Trans(source = "For maintaining Multiblocks",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "用于维护多方块结构"))
    static final String MAINTENANCE_HATCH = "gtpm.machine.maintenance_hatch.tooltip";

    @Trans(source = "For automatically maintaining Multiblocks",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "可以自动维护多方块结构"))
    static final String MAINTENANCE_HATCH_FULL_AUTO = "gtpm.machine.maintenance_hatch_full_auto.tooltip";

    @Trans(source = "Primitive Fluid Output for Water Pump",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "原始水泵专用流体输出口"))
    static final String PUMP_HATCH = "gtpm.machine.pump_hatch.tooltip";

    @Trans(source = "Specialized Access Bus that only holds valid items",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "仅可容纳有效物品的特型访问总线"))
    static final String MACHINE_HATCH = "gtpm.machine.machine_hatch.tooltip";

    @Trans(source = "Sends Items from one Side to the other",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "将物品从一侧发送到另一侧"))
    static final String PASSTHROUGH_HATCH_ITEM = "gtpm.machine.passthrough_hatch_item.tooltip";

    @Trans(source = "Sends Fluids from one Side to the other",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "将流体从一侧输送到另一侧"))
    static final String PASSTHROUGH_HATCH_FLUID = "gtpm.machine.passthrough_hatch_fluid.tooltip";

    @Trans(source = "Research Data Input for Multiblocks",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "为多方块结构输入研究数据"))
    static final String DATA_RECEIVER_HATCH = "gtpm.machine.data_receiver_hatch.tooltip";

    @Trans(source = "Research Data Output for Multiblocks",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "从多方块结构输出研究数据"))
    static final String DATA_TRANSMITTER_HATCH = "gtpm.machine.data_transmitter_hatch.tooltip";

    @Trans(source = "Computation Data Output for Multiblocks",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "从多方块结构输出算力数据"))
    static final String COMPUTATION_TRANSMITTER_HATCH = "gtpm.machine.computation_transmitter_hatch.tooltip";

    @Trans(source = "Computation Data Input for Multiblocks",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "为多方块结构输入算力数据"))
    static final String COMPUTATION_RECEIVER_HATCH = "gtpm.machine.computation_receiver_hatch.tooltip";

    @Trans(source = "Advanced Holding Mechanism for Research Station",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "研究站的先进支撑机构"))
    static final String OBJECT_HOLDER = "gtpm.machine.object_holder.tooltip";

    @Trans(source = "§7Produces §f%d L §7of Steam with §f1 Coal",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§7每块§1煤炭§7可以生产§f%dL§7的蒸汽"))
    static final String LARGE_BOILER_RATE_TOOLTIP = "gtpm.multiblock.large_boiler.rate_tooltip";
}
