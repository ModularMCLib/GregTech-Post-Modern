package com.modularmc.gtpm.common.data.machines.tooltip;

import com.modularmc.registrate.localization.MinecraftLocale;
import com.modularmc.registrate.localization.annotation.Trans;
import com.modularmc.registrate.localization.annotation.TransVariants;
import com.modularmc.registrate.localization.annotation.Translation;
import com.modularmc.registrate.localization.annotation.Variant;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;

import java.util.List;
import java.util.function.Supplier;

/** Reusable, styled item tooltip groups for machine registration. */
@TransVariants(keyPrefix = "gtpm.universal.tooltip",
               value = {
                       @Variant(id = "amperage_in",
                                source = "§eAmperage IN: §f%dA",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§e输入电流：§f%dA")),
                       @Variant(id = "amperage_in_out",
                                source = "§eAmperage IN/OUT: §f%dA",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§e输入/输出电流：§f%dA")),
                       @Variant(id = "base_production_fluid",
                                source = "§eBase Production: §f%d mB/t",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "§e基础产出速率：§f%d mB/t")),
                       @Variant(id = "deprecated",
                                source = "§4§lWARNING:§r§4 DEPRECATED. WILL BE REMOVED IN A FUTURE VERSION.§r",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "§4§l警告：§r§4已弃用。将在未来版本中移除。§r")),
                       @Variant(id = "energy_storage_capacity",
                                source = "§cEnergy Capacity: §r%d EU",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§c能量缓存：§r%d EU")),
                       @Variant(id = "fluid_storage_capacity",
                                source = "§9Fluid Capacity: §f%d mB",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§9流体容量：§f%d mB")),
                       @Variant(id = "fluid_storage_capacity_mult",
                                source = "§9Fluid Capacity: §f%d §7Tanks, §f%d mB §7each",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "§9流体容量：§r共§f%d§7个流体槽，每个§f%d mB§7")),
                       @Variant(id = "fluid_stored",
                                source = "§2Fluid Stored: §f%s, %d mB",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "§2内部流体：%2$d mB§f%1$s")),
                       @Variant(id = "fluid_transfer_rate",
                                source = "§bTransfer Rate: §f%d mB/t",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§b传输速率：§f%d mB/t")),
                       @Variant(id = "item_storage_capacity",
                                source = "§6Item Slots: §f%d",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§6物品槽位数量：§f%d")),
                       @Variant(id = "item_stored",
                                source = "§dItem Stored: §f%s, %d items",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "§d内部物品：§f%2$d个%1$s")),
                       @Variant(id = "item_transfer_rate",
                                source = "§bTransfer Rate: §f%d items/s",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§b传输速率：§f%d物品/s")),
                       @Variant(id = "item_transfer_rate_stacks",
                                source = "§bTransfer Rate: §f%d stacks/s",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§b传输速率：§f%d组/s")),
                       @Variant(id = "max_voltage_in",
                                source = "§aMax Voltage IN: §f%d (%s§f)",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "§a最大输入电压：§f%d（%s§f）")),
                       @Variant(id = "max_voltage_in_out",
                                source = "§aMax Voltage IN/OUT: §f%d EU/t (%s§f)",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "§a最大输入/输出电压：§f%d EU/t（%s§f）")),
                       @Variant(id = "max_voltage_out",
                                source = "§aMax Voltage OUT: §f%d (%s§f)",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "§a最大输出电压：§f%d（%s§f）")),
                       @Variant(id = "parallel",
                                source = "§dMax Parallel: §f%d",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§d最大并行：§f%d")),
                       @Variant(id = "produces_fluid",
                                source = "§eProduces: §f%d mB/t",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§e产出速率：§f%d mB/t")),
                       @Variant(id = "requires_redstone",
                                source = "§4Requires Redstone power",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§4需要红石信号")),
                       @Variant(id = "uses_per_op",
                                source = "Uses §f%d EU/operation",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "每次工作时消耗§f%d EU§7")),
                       @Variant(id = "uses_per_second",
                                source = "Uses §f%d EU/s §7while working",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "工作时消耗§f%d EU/s§7")),
                       @Variant(id = "voltage_in",
                                source = "§aVoltage IN: §f%d EU/t (%s§f)",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "§a输入电压：§f%d EU/t（%s§f）")),
                       @Variant(id = "voltage_in_out",
                                source = "§aVoltage IN/OUT: §f%d EU/t (%s§f)",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "§a输入/输出电压：§f%d EU/t（%s§f）")),
                       @Variant(id = "voltage_out",
                                source = "§aVoltage OUT: §f%d EU/t (%s§f)",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "§a输出电压：§f%d EU/t（%s§f）")),
                       @Variant(id = "working_area",
                                source = "§bWorking Area: §f%dx%d",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§b工作范围：§f%dx%d")),
                       @Variant(id = "working_area_chunks",
                                source = "§bWorking Area: §f%dx%d Chunks",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "§b工作范围：§f%dx%d 区块")),
                       @Variant(id = "working_area_chunks_max",
                                source = "§bMax Working Area: §f%dx%d Chunks",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "§b最大工作范围：§f%dx%d 区块")),
               })
public final class MachineTooltips {

    @Trans(key = MachineTooltips.HEATING_COILS,
           source = "Heating Coils",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "线圈加成"))
    private static final String HEATING_COILS = "gtpm.machine.tooltip.section.heating_coils";

    @Trans(key = MachineTooltips.PARALLELIZABLE,
           source = "Can parallelize with Parallel Control Hatches.",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "通过并行控制仓让机器同时处理多个相同配方。"))
    private static final String PARALLELIZABLE = "gtpm.multiblock.parallelizable.tooltip";

    @Trans(key = MachineTooltips.EXACTLY_ONE_ENERGY_HATCH,
           source = "§fAccepts Exactly §6One §fEnergy Hatch.",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§f只允许有且仅有§6一个§f能源仓。"))
    private static final String EXACTLY_ONE_ENERGY_HATCH = "gtpm.multiblock.exact_hatch_1.tooltip";

    @Trans(key = MachineTooltips.ITEM_BUS_IMPORT,
           source = "Item Input for Multiblocks",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "为多方块结构输入物品"))
    private static final String ITEM_BUS_IMPORT = "gtpm.machine.item_bus.import.tooltip";

    @Trans(key = MachineTooltips.ITEM_BUS_EXPORT,
           source = "Item Output for Multiblocks",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "为多方块结构输出物品"))
    private static final String ITEM_BUS_EXPORT = "gtpm.machine.item_bus.export.tooltip";

    private static final String BLAST_FURNACE = "gtpm.machine.electric_blast_furnace.tooltip";

    @TransVariants(keyPrefix = BLAST_FURNACE,
                   value = {
                           @Variant(id = "0",
                                    source = "For every §f900K§7 above the recipe temperature, a multiplicative §f95%%§7 energy multiplier is applied pre-overclocking.",
                                    translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                                text = "每超过配方温度§f900K§7，耗能均将乘以§f95%%§7（不计超频）。")),
                           @Variant(id = "1",
                                    source = "For every §f1800K§7 above the recipe temperature, one overclock becomes §f100%% efficient§7 (perfect overclock).",
                                    translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                                text = "每超过配方温度§f1800K§7，将一次超频的效率变为§f100%%§7（无损超频）。")),
                           @Variant(id = "2",
                                    source = "For every voltage tier above §bMV§7, temperature is increased by §f100K§7.",
                                    translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                                text = "超过§fMV§7后的每一级电压，都会将温度提高§f100K§7。"))
                   })
    public static final Supplier<List<Component>> ELECTRIC_BLAST_FURNACE = () -> section(HEATING_COILS,
            Component.translatable(BLAST_FURNACE + ".0"),
            Component.translatable(BLAST_FURNACE + ".1"),
            Component.translatable(BLAST_FURNACE + ".2"));

    private MachineTooltips() {}

    public static Component itemInput() {
        return Component.translatable(ITEM_BUS_IMPORT);
    }

    public static Component itemOutput() {
        return Component.translatable(ITEM_BUS_EXPORT);
    }

    public static Component singleEnergyHatch() {
        return Component.translatable(EXACTLY_ONE_ENERGY_HATCH);
    }

    public static Component parallelizable() {
        return Component.translatable(PARALLELIZABLE);
    }

    @TransVariants(keyPrefix = "gtpm.machine",
                   value = {
                           @Variant(id = "available_recipe_map_1.tooltip",
                                    source = "Available Recipe Types: %s",
                                    translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "可用配方类型：%s")),
                           @Variant(id = "available_recipe_map_2.tooltip",
                                    source = "Available Recipe Types: %s, %s",
                                    translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "可用配方类型：%s，%s")),
                           @Variant(id = "available_recipe_map_3.tooltip",
                                    source = "Available Recipe Types: %s, %s, %s",
                                    translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                                text = "可用配方类型：%s，%s，%s")),
                           @Variant(id = "available_recipe_map_4.tooltip",
                                    source = "Available Recipe Types: %s, %s, %s, %s",
                                    translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                                text = "可用配方类型：%s，%s，%s，%s")),
                   })
    private static final String AVAILABLE_RECIPES = "gtpm.machine.available_recipe_map_";

    public static Component recipeTypes(Component... names) {
        if (names.length < 1 || names.length > 4) {
            throw new IllegalArgumentException("Expected one to four recipe types");
        }
        return Component.translatable(AVAILABLE_RECIPES + names.length + ".tooltip", (Object[]) names);
    }

    private static List<Component> section(String heading, Component... details) {
        var result = new ObjectArrayList<Component>(1 + details.length);
        appendSection(result, heading, details);
        return List.copyOf(result);
    }

    private static void appendSection(List<Component> tooltip, String heading, Component... details) {
        tooltip.add(Component.translatable(heading).withStyle(ChatFormatting.GOLD, ChatFormatting.BOLD));
        for (Component detail : details) {
            MutableComponent line = Component.literal("  ").withStyle(ChatFormatting.GRAY).append(detail);
            tooltip.add(line);
        }
    }
}
