package com.modularmc.gtpm.data.placeholder;

import com.modularmc.gtpm.GTPM;
import com.modularmc.gtpm.api.GTValues;
import com.modularmc.gtpm.api.capability.GTCapabilityHelper;
import com.modularmc.gtpm.api.capability.IEnergyContainer;
import com.modularmc.gtpm.api.capability.IEnergyInfoProvider;
import com.modularmc.gtpm.api.capability.IWorkable;
import com.modularmc.gtpm.api.cover.filter.ItemFilter;
import com.modularmc.gtpm.api.item.IComponentItem;
import com.modularmc.gtpm.api.item.component.IItemComponent;
import com.modularmc.gtpm.api.item.component.IMonitorModuleItem;
import com.modularmc.gtpm.api.machine.MetaMachine;
import com.modularmc.gtpm.api.machine.feature.multiblock.IMaintenanceMachine;
import com.modularmc.gtpm.api.misc.virtualregistry.EntryTypes;
import com.modularmc.gtpm.api.misc.virtualregistry.VirtualEnderRegistry;
import com.modularmc.gtpm.api.placeholder.*;
import com.modularmc.gtpm.api.placeholder.exceptions.*;
import com.modularmc.gtpm.client.renderer.placeholder.ModulePlaceholderRenderer;
import com.modularmc.gtpm.client.renderer.placeholder.QuadPlaceholderRenderer;
import com.modularmc.gtpm.client.renderer.placeholder.RectPlaceholderRenderer;
import com.modularmc.gtpm.common.blockentity.CableBlockEntity;
import com.modularmc.gtpm.common.data.GTDataComponents;
import com.modularmc.gtpm.common.item.datacomponents.BindingData;
import com.modularmc.gtpm.common.item.datacomponents.DataItem;
import com.modularmc.gtpm.common.item.datacomponents.FormatStringList;
import com.modularmc.gtpm.common.item.modules.ImageModuleBehaviour;
import com.modularmc.gtpm.common.machine.multiblock.electric.monitor.MonitorGroup;
import com.modularmc.gtpm.common.machine.multiblock.part.monitor.AdvancedMonitorPartMachine;
import com.modularmc.gtpm.integration.ae2.GTAEPlaceholders;
import com.modularmc.gtpm.utils.GTMath;
import com.modularmc.gtpm.utils.GTStringUtils;
import com.modularmc.gtpm.utils.GTTransferUtils;

import com.modularmc.registrate.localization.MinecraftLocale;
import com.modularmc.registrate.localization.annotation.TransVariants;
import com.modularmc.registrate.localization.annotation.Translation;
import com.modularmc.registrate.localization.annotation.Variant;

import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec2;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.IItemHandlerModifiable;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

@TransVariants(keyPrefix = "gtpm",
               value = {
                       @Variant(id = "placeholder_info.active.0",
                                source = "Returns a 1 if the block the cover is attached to is currently running a recipe, 0 otherwise.",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "若覆盖板所贴附的方块处于工作状态，则返回1；否则返回0。")),
                       @Variant(id = "placeholder_info.active.1",
                                source = "Usage:",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "用法：")),
                       @Variant(id = "placeholder_info.active.2",
                                source = "  {active} -> whether there's a currently running recipe",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "{active} -> 当前是否有正在运行的配方")),
                       @Variant(id = "placeholder_info.amperage.0",
                                source = "Returns the amperage in the wire/cable the cover is on.",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "返回覆盖板所贴附的的导线/线缆的电流大小。")),
                       @Variant(id = "placeholder_info.amperage.1",
                                source = "Usage:",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "用法：")),
                       @Variant(id = "placeholder_info.amperage.2",
                                source = "  {amperage} -> the amperate in the wire/cable",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "  {amperage} -> 此导线/线缆的电流")),
                       @Variant(id = "placeholder_info.bf.0",
                                source = "Usage:",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "用法：")),
                       @Variant(id = "placeholder_info.bf.1",
                                source = "  {bf <data_item_slot_index> <code>} -> empty string",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "  {bf <数据容器的槽位索引> <代码>} -> 空字符串")),
                       @Variant(id = "placeholder_info.block.0",
                                source = "Returns the block symbol (█).",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "返回方块符号‘█’。")),
                       @Variant(id = "placeholder_info.block.1",
                                source = "Usage:",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "用法：")),
                       @Variant(id = "placeholder_info.block.2",
                                source = "  {block} -> '█'",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "  {block} -> '█'")),
                       @Variant(id = "placeholder_info.blockNbt.0",
                                source = "Returns the data components of the block entity",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "返回方块实体的数据组件")),
                       @Variant(id = "placeholder_info.blockNbt.1",
                                source = "Usage:",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "用法：")),
                       @Variant(id = "placeholder_info.blockNbt.2",
                                source = "  {blockNbt} -> full block entity data component map",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "  {blockNbt} -> 方块实体的完整数据组件列表")),
                       @Variant(id = "placeholder_info.blockNbt.3",
                                source = "  {blockNbt <component_id>} -> the value of a data component",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "  {blockNbt <组件 ID>} -> 指定数据组件的值")),
                       @Variant(id = "placeholder_info.bufferText.0",
                                source = "Returns the text from a buffer accessible by ComputerCraft",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "返回ComputerCraft可访问的缓冲区中的文本")),
                       @Variant(id = "placeholder_info.bufferText.1",
                                source = "Usage:",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "用法：")),
                       @Variant(id = "placeholder_info.bufferText.2",
                                source = "  {bufferText <line>} -> text from the buffer on the specified line (line is 1-100)",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "  {bufferText <行>} -> 缓冲区中指定行的文本（行号为1-100）")),
                       @Variant(id = "placeholder_info.calc.0",
                                source = "Returns the result of a math function or operation.",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "返回数学函数或运算的结果。")),
                       @Variant(id = "placeholder_info.calc.1",
                                source = "Usage:",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "用法：")),
                       @Variant(id = "placeholder_info.calc.2",
                                source = "  {calc <any_string>} -> any_string",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "  {calc <任意字符串>} -> 任意字符串")),
                       @Variant(id = "placeholder_info.calc.3",
                                source = "  {calc <round|floor|ceil|sqrt|~> <arg>} -> the result of the specified operation",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "  {calc <round|floor|ceil|sqrt|~> <参数>} -> 指定运算的结果")),
                       @Variant(id = "placeholder_info.calc.4",
                                source = "  {calc <first_arg> <+|-|*|/|//|>>|<<|%> <second_arg>} -> the result of the specified operation",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "  {calc <first_arg> <+|-|*|/|//|>>|<<|%> <second_arg>} -> 指定运算的结果")),
                       @Variant(id = "placeholder_info.click.0",
                                source = "Returns whether the targeted advanced monitor was clicked before the current tick",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "返回在当前tick之前，目标高级监控器是否被点击")),
                       @Variant(id = "placeholder_info.click.1",
                                source = "Usage:",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "用法：")),
                       @Variant(id = "placeholder_info.click.2",
                                source = "  {click} -> \"1\" if the targeted advanced monitor was clicked, \"0\" otherwise",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "  {click} -> 如果目标高级监控器被点击，返回\"1\"，否则返回\"0\"")),
                       @Variant(id = "placeholder_info.click.3",
                                source = "  {click x} -> the x position of the last click (between 0 and 1)",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "  {click x} -> 最后一次点击时，x的坐标（0到1之间）")),
                       @Variant(id = "placeholder_info.click.4",
                                source = "  {click y} -> the y position of the last click (between 0 and 1)",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "  {click y} -> 最后一次点击时，y的坐标（0到1之间）")),
                       @Variant(id = "placeholder_info.cmd.0",
                                source = "Executes Minecraft commands and returns their output.",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "执行Minecraft命令并返回其输出结果。")),
                       @Variant(id = "placeholder_info.cmd.1",
                                source = "Requires a data item bound to a player, bind any data item to yourself by right-clicking with it.",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "需要一个数据容器以绑定玩家，通过右键点击以绑定你自己")),
                       @Variant(id = "placeholder_info.cmd.2",
                                source = "Usage:",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "用法：")),
                       @Variant(id = "placeholder_info.cmd.3",
                                source = "  {cmd <slot_index> <command>} -> command output",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "  {cmd <槽位索引> <指令>} -> 指令输出结果")),
                       @Variant(id = "placeholder_info.cmp.0",
                                source = "Returns a 1 or 0 based on the expression in it's arguments",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "根据参数中的表达式返回1或0")),
                       @Variant(id = "placeholder_info.cmp.1",
                                source = "Usage:",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "用法：")),
                       @Variant(id = "placeholder_info.cmp.2",
                                source = "  {cmp <a> <operator> <b>} -> 1 or 0, operator is one of >, <, >=, <=, ==, !=",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "  {cmp <a> <运算符> <b>} -> 1或0，运算符可是以下任意一个：>、<、>=、<=、==、!=")),
                       @Variant(id = "placeholder_info.color.0",
                                source = "Returns the text from the second argument, colored with the color from the first argument. All default minecraft chat colors can be used.",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "返回用指定颜色（第一个参数）上色的文本（第二个参数）。可以用所有默认的Minecraft聊天颜色。")),
                       @Variant(id = "placeholder_info.color.1",
                                source = "Usage:",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "用法：")),
                       @Variant(id = "placeholder_info.color.2",
                                source = "  {color <color> <text>} -> colored text",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "  {color <颜色> <文本>} -> 上色的文本")),
                       @Variant(id = "placeholder_info.combine.0",
                                source = "Combines all of it's arguments into a single string (by escaping all spaces between the arguments)",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "组合所有参数为单一字符串（通过转义参数之间的所有空格）")),
                       @Variant(id = "placeholder_info.combine.1",
                                source = "Example: {combine abc def ghi jkl mno} -> \"abc\\ def\\ ghi\\ jkl\\ mno\"",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "例如：{combine abc def ghi jkl mno} -> \"abc\\ def\\ ghi\\ jkl\\ mno\"")),
                       @Variant(id = "placeholder_info.combine.2",
                                source = "Usage:",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "用法：")),
                       @Variant(id = "placeholder_info.combine.3",
                                source = "  {combine [arg1] [arg2] [arg3] ...} -> a string that will be treated as a single argument in further placeholders",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "  {combine [参数1] [参数2] [参数3] ...} -> 单一字符串，将在后续占位符中被视为单个参数")),
                       @Variant(id = "placeholder_info.count.0",
                                source = "Returns how many of the provided arguments are equal to the first (compared as strings, so \"0\" != \"0.0\")",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "返回有多少参数与第一个参数相等（作为字符串比较，所以\"0\" != \"0.0\"）")),
                       @Variant(id = "placeholder_info.count.1",
                                source = "Usage:",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "用法：")),
                       @Variant(id = "placeholder_info.count.2",
                                source = "  {count <arg1> [arg2] [arg3] [arg4] ...} -> the amount of arguments that are equal to the first",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "  {count <参数1> [参数2] [参数3] [参数4] ...} -> 与第一个参数相等的参数的数量")),
                       @Variant(id = "placeholder_info.data.0",
                                source = "Stores or retrieves some data from a data item (data stick/orb/module) in one of the slots.",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "向槽位中的数据容器（闪存/数据球/数据模块）写入数据或读取数据。")),
                       @Variant(id = "placeholder_info.data.1",
                                source = "If you leave the <index> argument empty, it will be replaced with the value p (p is an integer from 0 to (capacity - 1) that is stored in the data item nbt).",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "如果将<索引编号>参数留空，则会用值p代替（p是存储在数据容器的NBT中的整数，范围为0到容量 - 1）。")),
                       @Variant(id = "placeholder_info.data.2",
                                source = "Usage:",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "用法：")),
                       @Variant(id = "placeholder_info.data.3",
                                source = "  {data get <slot> <index>} -> the data stored in the item in the specified slot",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "  {data get <插槽> <索引编号>} -> 指定的插槽内物品存储的数据")),
                       @Variant(id = "placeholder_info.data.4",
                                source = "  {data set <slot> <index> <value>} -> sets the data stored in the item in the specified slot, returns an empty string",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "  {data set <槽位> <索引编号> <值>} -> 设置指定插槽内物品存储的数据，返回一个空字符串")),
                       @Variant(id = "placeholder_info.data.5",
                                source = "  {data getp <slot>} -> p",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "  {data getp <槽位>} -> p")),
                       @Variant(id = "placeholder_info.data.6",
                                source = "  {data setp <slot> <value>} -> sets p, returns an empty string",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "  {data setp <槽位> <值>} -> 设置p，返回一个空字符串")),
                       @Variant(id = "placeholder_info.data.7",
                                source = "  {data inc <slot>} -> increments p by 1, if p becomes more than or equal to capacity, sets p to 0",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "  {data inc <槽位>} -> 将p加1，如果p大于等于容量，则把p设为0")),
                       @Variant(id = "placeholder_info.data.8",
                                source = "  {data dec <slot>} -> decrements p by 1, if p becomes less than 0, sets p to (capacity - 1)",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "  {data dec <槽位>} -> 将p减1，如果p小于0，则把p设为容量-1")),
                       @Variant(id = "placeholder_info.data.9",
                                source = "If the slot argument is equal to 0, this placeholder will manipulate the data stick that is currently targeted by this text module inside a data hatch.",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "在中央监控器中，槽位参数为 0 时，读写数据访问仓当前选中的数据物品。")),
                       @Variant(id = "placeholder_info.ender.0",
                                source = "Interacts with ender link covers",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "与末影连接覆盖板交互")),
                       @Variant(id = "placeholder_info.ender.1",
                                source = "Can interact with private channels if provided with a data item bound to a player",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "如果提供一个绑定了玩家的数据容器，就可以与私人频道交互")),
                       @Variant(id = "placeholder_info.ender.10",
                                source = "The player_data_item_slot argument may be left empty (not 0, empty string)",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "数据容器(玩家)槽位参数可留空（不是0，空字符串）")),
                       @Variant(id = "placeholder_info.ender.2",
                                source = "Usage:",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "用法：")),
                       @Variant(id = "placeholder_info.ender.3",
                                source = "  {ender item <channel> [player_data_item_slot]} -> item count",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "  {ender item <频道> [数据容器(玩家)槽位]} -> 物品数量")),
                       @Variant(id = "placeholder_info.ender.4",
                                source = "  {ender itemPull <channel> [player_data_item_slot]} -> pull 1 item from the ender link's buffer",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "  {ender itemPull <频道> [数据容器(玩家)槽位]} -> 从末影连接缓存中拉取一个物品")),
                       @Variant(id = "placeholder_info.ender.5",
                                source = "  {ender itemPush <channel> [player_data_item_slot]} -> push 1 item to the ender link's buffer",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "  {ender itemPush <频道> [数据容器(玩家)槽位] -> 推送一个物品至末影连接缓存")),
                       @Variant(id = "placeholder_info.ender.6",
                                source = "  {ender itemId <channel> [player_data_item_slot]} -> the id of the item in the ender link's buffer (ex. \"26 minecraft:dirt\")",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "  {ender itemId <频道> [数据容器(玩家)槽位]} -> 末影连接缓存里物品的id（例：\"26 minecraft:dirt\"）")),
                       @Variant(id = "placeholder_info.ender.7",
                                source = "  {ender fluid <channel> [player_data_item_slot]} -> fluid count",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "  {ender fluid <channel> [数据容器(玩家)槽位]} -> 流体量")),
                       @Variant(id = "placeholder_info.ender.8",
                                source = "  {ender redstone <channel> [player_data_item_slot] -> redstone signal level",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "  {ender redstone <频道> [数据容器(玩家)槽位] -> 红石信号等级")),
                       @Variant(id = "placeholder_info.ender.9",
                                source = "  {ender redstone <channel> <player_data_item_slot> <signal> -> sets the redstone signal outputed to the ender redstone link, returns empty string",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "  {ender redstone <频道> <数据容器(玩家)槽位> <信号等级>} -> 设置末影红石连接输出的红石信号，返回空字符串")),
                       @Variant(id = "placeholder_info.energy.0",
                                source = "Returns the amount of energy stored.",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "返回已存储的能量值")),
                       @Variant(id = "placeholder_info.energy.1",
                                source = "Usage:",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "用法：")),
                       @Variant(id = "placeholder_info.energy.2",
                                source = "  {energy} -> the amount of energy stored",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "  {energy} -> 已存储的能量值")),
                       @Variant(id = "placeholder_info.energyCapacity.0",
                                source = "Returns the max amount of energy that can be stored",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "返回最大可存储的能量值")),
                       @Variant(id = "placeholder_info.energyCapacity.1",
                                source = "Usage:",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "用法：")),
                       @Variant(id = "placeholder_info.energyCapacity.2",
                                source = "{energyCapacity} -> the energy capacity",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "{energyCapacity} -> 能量容量")),
                       @Variant(id = "placeholder_info.eval.0",
                                source = "Returns the result of evaluating the provided string which may placeholders",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "返回对提供的字符串执行的结果，其中可能包含占位符")),
                       @Variant(id = "placeholder_info.eval.1",
                                source = "Usage:",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "用法：")),
                       @Variant(id = "placeholder_info.eval.2",
                                source = "  {eval abcdefg} -> abcdefg",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "  {eval abcdefg} -> abcdefg")),
                       @Variant(id = "placeholder_info.eval.3",
                                source = "  {eval \"repeating a: {repeat 5 \\\"a \\\"}\" -> repeating a: a a a a a ",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "  {eval \"repeating a: {repeat 5 \\\"a \\\"}\" -> repeating a: a a a a a ")),
                       @Variant(id = "placeholder_info.eval.4",
                                source = "  {eval \\\"\"{some random text}\"\\\" -> {some random text}",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "  {eval \\\"\"{some random text}\"\\\" -> {some random text}")),
                       @Variant(id = "placeholder_info.eval.5",
                                source = "  {eval \"text \"\\\"\"{something with spaces}\"\\\"\" more text\" -> text {something with spaces} more text",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "  {eval \"text \"\\\"\"{something with spaces}\"\\\"\" more text\" -> text {something with spaces} more text")),
                       @Variant(id = "placeholder_info.fluidCount.0",
                                source = "Returns the amount of fluids (can be filtered).",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "返回流体量（可被过滤）。")),
                       @Variant(id = "placeholder_info.fluidCount.1",
                                source = "Usage:",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "用法：")),
                       @Variant(id = "placeholder_info.fluidCount.2",
                                source = "  {fluidCount [fluidId]} -> the amount of all fluids, or the fluid with fluidId if specified",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "  {fluidCount [流体id]} -> 所有流体的总量，若指定了流体id则返回该流体的数量")),
                       @Variant(id = "placeholder_info.formatInt.0",
                                source = "Returns a string representation of the provided integer",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "返回指定整数的字符串形式")),
                       @Variant(id = "placeholder_info.formatInt.1",
                                source = "Example: {formatInt 1236457} -> 1.24M",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "例：{formatInt 1236457} -> 1.24M")),
                       @Variant(id = "placeholder_info.formatInt.2",
                                source = "Usage:",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "用法：")),
                       @Variant(id = "placeholder_info.formatInt.3",
                                source = "  {formatInt <arg>} -> string representation of the int",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "  {formatInt <参数>} -> 指定的整数的字符串形式")),
                       @Variant(id = "placeholder_info.fromAscii.0",
                                source = "Returns the character represented by the provided ASCII code",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "返回指定ASCII码所代表的字符")),
                       @Variant(id = "placeholder_info.fromAscii.1",
                                source = "Usage:",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "用法：")),
                       @Variant(id = "placeholder_info.fromAscii.2",
                                source = "  {fromAscii <char_code>} -> a character",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "  {fromAscii <字符编码>} -> 字符")),
                       @Variant(id = "placeholder_info.if.0",
                                source = "Returns one of the arguments depending on the condition. The condition is considered true if it is not an empty string and is not equal to 0.",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "根据条件返回参数之一。如果该参数不是空字符串且不等于0，则认为该条件为真。")),
                       @Variant(id = "placeholder_info.if.1",
                                source = "Usage:",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "用法：")),
                       @Variant(id = "placeholder_info.if.2",
                                source = "  {if <condition> <returned_if_true> [returned_if_false]}",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "  {if <条件> <如果为真则被返回> [如果为假则被返回]}")),
                       @Variant(id = "placeholder_info.item.0",
                                source = "Returns the amount and id of the item in a specified slot",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "返回指定的插槽内物品的数量和ID")),
                       @Variant(id = "placeholder_info.item.1",
                                source = "Usage:",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "用法：")),
                       @Variant(id = "placeholder_info.item.2",
                                source = "  {item <slot>} -> \"31 minecraft:diamond\" (for example)",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "  {item <插槽>} -> \"31 minecraft:diamond\"（例）")),
                       @Variant(id = "placeholder_info.itemCount.0",
                                source = "Returns the amount of items (can be filtered).",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "返回物品的数量（可被过滤）。")),
                       @Variant(id = "placeholder_info.itemCount.1",
                                source = "Usage:",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "用法：")),
                       @Variant(id = "placeholder_info.itemCount.2",
                                source = "  {itemCount} -> total item amount",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "  {itemCount} -> 总物品数量")),
                       @Variant(id = "placeholder_info.itemCount.3",
                                source = "  {itemCount <item_id>} -> amount of items with ids equal to item_id",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "  {itemCount <物品id>} -> 指定的物品的数量")),
                       @Variant(id = "placeholder_info.itemCount.4",
                                source = "  {itemCount filter <slot_id>} -> amount of items matching filter in specified slot of this cover",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "  {itemCount filter <槽位id>} -> 覆盖板指定槽位中，符合筛选条件的物品的数量")),
                       @Variant(id = "placeholder_info.maintenance.0",
                                source = "Returns a 1 if there are maintenance problems in the block the cover is attached to, 0 otherwise.",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "若覆盖板所贴附的方块有维护问题返回1，否则返回0。")),
                       @Variant(id = "placeholder_info.maintenance.1",
                                source = "Example: 'Maintenance status: {if {maintenance} FIXING\\ REQUIRED OK}'",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "例：'维护状态：{if {maintenance} 需要维护 已维护}'")),
                       @Variant(id = "placeholder_info.maintenance.2",
                                source = "Usage:",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "用法：")),
                       @Variant(id = "placeholder_info.maintenance.3",
                                source = "  {maintenance} -> whether there are maintenance problems",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "  {maintenance} -> 是否存在维护问题")),
                       @Variant(id = "placeholder_info.maxProgress.0",
                                source = "Returns the maximum progress of the currently running recipe of the block this cover is attached to.",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "返回覆盖板所贴附的方块当前正在运行的配方的最大进度。")),
                       @Variant(id = "placeholder_info.maxProgress.1",
                                source = "Example: 'Progress: {calc {calc {progress} / {maxProgress}} * 100}%'",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "例: '进度: {calc {calc {progress} / {maxProgress}} * 100}%'")),
                       @Variant(id = "placeholder_info.maxProgress.2",
                                source = "Usage:",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "用法：")),
                       @Variant(id = "placeholder_info.maxProgress.3",
                                source = "  {maxProgress} -> the max progress of the currently running recipe",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "  {maxProgress} -> 当前正在运行的配方的最大进度")),
                       @Variant(id = "placeholder_info.module.0",
                                source = "Renders the module in the specified slot onto the central monitor (does not work in a cover)",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "将指定的插槽内的模块渲染到中央监控器上（不适用于覆盖板）")),
                       @Variant(id = "placeholder_info.module.1",
                                source = "Usage:",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "用法：")),
                       @Variant(id = "placeholder_info.module.2",
                                source = "  {module <slot> <x> <y>} -> empty string",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "  {module <插槽> <x> <y>} -> 空字符串")),
                       @Variant(id = "placeholder_info.nbt.0",
                                source = "Returns the nbt data of the item in the specified slot",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "返回指定的插槽内物品的NBT数据")),
                       @Variant(id = "placeholder_info.nbt.1",
                                source = "Usage:",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "用法：")),
                       @Variant(id = "placeholder_info.nbt.2",
                                source = "  {nbt <slot> [key1] [key2] [key3] ...} -> item_nbt[key1][key2][key3][...]",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "  {nbt <槽位> [键1] [键2] [键3] …} -> 物品NBT [键1] [键2] [键3] […]")),
                       @Variant(id = "placeholder_info.obf.0",
                                source = "Returns the text from the first argument, obfuscated.",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "返回混淆处理过的文本（第一个参数）。")),
                       @Variant(id = "placeholder_info.obf.1",
                                source = "Usage:",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "用法：")),
                       @Variant(id = "placeholder_info.obf.2",
                                source = "  {obf <text>} -> obfuscated text",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "  {obf <文本>} -> 混淆过的文本")),
                       @Variant(id = "placeholder_info.previousText.0",
                                source = "Returns the text that was previously displayed by this cover at the specified line (before line-wrapping).",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "返回此覆盖板在指定行（换行之前）之前显示的文本。")),
                       @Variant(id = "placeholder_info.previousText.1",
                                source = "Usage:",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "用法：")),
                       @Variant(id = "placeholder_info.previousText.2",
                                source = "  {previousText <line>} -> the text previously displayed on the specified line (index starts at 1)",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "  {previousText <行>} -> 在指定行之前显示的文本（行编号从1开始）")),
                       @Variant(id = "placeholder_info.progress.0",
                                source = "Returns the progress of the currently running recipe of the block this cover is attached to.",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "返回覆盖板所贴附的方块当前正在运行的配方的进度。")),
                       @Variant(id = "placeholder_info.progress.1",
                                source = "Note that progress is an integer between 0 and {maxProgress}",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "注意：此进度是个在0和{maxProgress}之间的整数")),
                       @Variant(id = "placeholder_info.progress.2",
                                source = "Usage:",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "用法：")),
                       @Variant(id = "placeholder_info.progress.3",
                                source = "  {progress} -> the progress of the currently running recipe",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "  {progress} -> 当前正在运行的配方的进度")),
                       @Variant(id = "placeholder_info.quad.0",
                                source = "Draws a quad (must specify parameters for all 4 vertices)",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "绘制一个四边形（必须为所有四个顶点指定参数）")),
                       @Variant(id = "placeholder_info.quad.1",
                                source = "Usage:",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "用法：")),
                       @Variant(id = "placeholder_info.quad.2",
                                source = "  {quad <x1> <y1> <x2> <y2> <x3> <y3> <x4> <y4> <color1> <color2> <color3> <color4>} -> empty string",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "  {quad <x1> <y1> <x2> <y2> <x3> <y3> <x4> <y4> <color1> <color2> <color3> <color4>} -> 空字符串")),
                       @Variant(id = "placeholder_info.random.0",
                                source = "Returns a random number in the specified interval (inclusive).",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "返回指定区间（含边界）内的随机数。")),
                       @Variant(id = "placeholder_info.random.1",
                                source = "Usage:",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "用法：")),
                       @Variant(id = "placeholder_info.random.2",
                                source = "  {random <min> <max>} -> a random number between min and max (inclusive)",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "  {random <最小值> <最大值>} -> 介于最小值和最大值之间的随机数（含边界）")),
                       @Variant(id = "placeholder_info.rect.0",
                                source = "Draws a rectangle at the specified position with the specified coordinates and size",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "以指定大小，在指定坐标绘制一个矩形")),
                       @Variant(id = "placeholder_info.rect.1",
                                source = "Usage:",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "用法：")),
                       @Variant(id = "placeholder_info.rect.2",
                                source = "  {rect <x> <y> <width> <height> <colorARGB>} -> empty string",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "  {rect <x> <y> <宽> <高> <ARGB颜色>} -> 空字符串")),
                       @Variant(id = "placeholder_info.rect.3",
                                source = "  {rect 0.5 0.25 2 1 0xFFFFFFFF} -> draws a white rectangle at (0.5, 0.25) with the size (2, 1)",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "  {rect 0.5 0.25 2 1 0xFFFFFFFF} -> 在(0.5, 0.25)处绘制一个大小为(2, 1)的白色矩形")),
                       @Variant(id = "placeholder_info.redstone.0",
                                source = "Returns the redstone signal strength or sets the redstone output strength",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "返回或设置红石信号强度")),
                       @Variant(id = "placeholder_info.redstone.1",
                                source = "Usage:",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "用法：")),
                       @Variant(id = "placeholder_info.redstone.2",
                                source = "  {redstone get <up|down|north|south|east|west>} -> redstone signal strength (0-15) at the specified side",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "  {redstone get <up|down|north|south|east|west>} -> 指定面的红石信号强度（0-15）")),
                       @Variant(id = "placeholder_info.redstone.3",
                                source = "  {redstone get link <slot_index> <freq_slot_index>} -> redstone signal strength of a Create redstone link frequency specified by a linked controller in slot #slot_index. freq_slot_index is the index of the frequency inside the controller (from left to right, 0-6)",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "  {redstone get link <槽位索引> <频率槽位索引>} -> 指定的中央监视器槽位（槽位索引）中无线红石遥控器链接的机械动力红石频率上的信号强度。频率槽位索引是无线红石遥控器内部频率的索引（从左到右，0–6）")),
                       @Variant(id = "placeholder_info.redstone.4",
                                source = "  {redstone set <power>} -> empty string, sets the redstone output strength from this cover's side",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "  {redstone set <强度>} -> 空字符串，设置红石信号输出强度，从此覆盖板方向输出")),
                       @Variant(id = "placeholder_info.redstone.5",
                                source = "  {redstone set link <slot_index> <freq_slot_index> <power>} -> empty string, broadcasts the specified redstone power on the specified Create redstone link frequency",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "  {redstone set link <槽位索引> <频率槽位索引> <信号强度>} -> 空字符串，在指定的无线红石遥控器链接的机械动力红石频率上，设为指定的红石信号强度")),
                       @Variant(id = "placeholder_info.repeat.0",
                                source = "Returns the text from the second arguments, repeated the amount of times specified in the first argument.",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "返回重复若干次（第一个参数）的文本（第二个参数）。")),
                       @Variant(id = "placeholder_info.repeat.1",
                                source = "Usage:",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "用法：")),
                       @Variant(id = "placeholder_info.repeat.2",
                                source = "  {repeat <amount> <text>} -> text repeated the specified amount of times",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "  {repeat <数量> <文本>} -> 重复指定次数的文本")),
                       @Variant(id = "placeholder_info.select.0",
                                source = "Returns the argument at the specified index (starting from 0)",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "返回指定索引处的参数（从0开始）")),
                       @Variant(id = "placeholder_info.select.1",
                                source = "Usage:",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "用法：")),
                       @Variant(id = "placeholder_info.select.2",
                                source = "  {select <index> [arg1] [arg2] [arg3] ... -> argument at the specified index",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "  {select <索引编号> [参数1] [参数2] [参数3] ... -> 指定索引处的参数")),
                       @Variant(id = "placeholder_info.setImage.0",
                                source = "Sets the image URL in an image module in the specified slot",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "为指定插槽中的图像模块中设置图像URL")),
                       @Variant(id = "placeholder_info.setImage.1",
                                source = "Usage:",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "用法：")),
                       @Variant(id = "placeholder_info.setImage.2",
                                source = "  {setImage <slot> <url>} -> empty string",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "  {setImage <槽位> <URL>} -> 空字符串")),
                       @Variant(id = "placeholder_info.setTargetSlot.0",
                                source = "Sets the index of the targeted data hatch slot.",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "切换数据访问仓中用于定位目标的槽位。")),
                       @Variant(id = "placeholder_info.setTargetSlot.1",
                                source = "The change will take effect immediately after this placeholder executes.",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "切换成功后立即生效。")),
                       @Variant(id = "placeholder_info.setTargetSlot.2",
                                source = "(Further placeholders will reference the new target)",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "后续占位符读取新目标。")),
                       @Variant(id = "placeholder_info.setTargetSlot.3",
                                source = "Usage:",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "用法：")),
                       @Variant(id = "placeholder_info.setTargetSlot.4",
                                source = "  {setTargetSlot <slot>} -> empty string",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "  {setTargetSlot <槽位>} -> 空字符串")),
                       @Variant(id = "placeholder_info.strike.0",
                                source = "Returns the text from the first text, displaying it as if it was crossed out",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "返回有删除线的文本（第一个参数），就像被划掉一样")),
                       @Variant(id = "placeholder_info.strike.1",
                                source = "Usage:",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "用法：")),
                       @Variant(id = "placeholder_info.strike.2",
                                source = "  {strike <text>} -> crossed-out text",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "  {strike <文本>} -> 有删除线的文本")),
                       @Variant(id = "placeholder_info.subList.0",
                                source = "Returns arguments from with indexes from l (inclusive) to r (exclusive) (starting from 0)",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "返回参数列表中索引范围为左（含边界）至右（不含）的元素（从0开始计）")),
                       @Variant(id = "placeholder_info.subList.1",
                                source = "Usage:",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "用法：")),
                       @Variant(id = "placeholder_info.subList.2",
                                source = "  {subList <left> <right> [arg0] [arg1] ...} -> all arguments with indexes from l to r separated by spaces",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "  {subList <左> <右> [参数0] [参数1] ...} -> 从<左>到<右>的所有参数，以空格分隔")),
                       @Variant(id = "placeholder_info.targetSlot.0",
                                source = "Returns the index of the targeted data hatch slot",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "返回数据访问仓当前选中的目标槽位，从 1 开始计数。")),
                       @Variant(id = "placeholder_info.targetSlot.1",
                                source = "Usage:",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "用法：")),
                       @Variant(id = "placeholder_info.targetSlot.2",
                                source = "  {targetSlot} -> <slot> (from 1 to 4/9/16)",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "  {targetSlot} -> <槽位>")),
                       @Variant(id = "placeholder_info.tick.0",
                                source = "Returns the amount of ticks passed from when this cover was placed.",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "返回从安装此覆盖板时起经过的tick。")),
                       @Variant(id = "placeholder_info.tick.1",
                                source = "Usage:",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "用法：")),
                       @Variant(id = "placeholder_info.tick.2",
                                source = "  {tick} -> the amount of ticks",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "  {tick} -> 自安装启度过的tick")),
                       @Variant(id = "placeholder_info.tm.0",
                                source = "Returns the ™ symbol",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "返回™符号")),
                       @Variant(id = "placeholder_info.tm.1",
                                source = "Usage:",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "用法：")),
                       @Variant(id = "placeholder_info.tm.2",
                                source = "  {tm} -> the ™ symbol",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "  {tm} -> ™符号")),
                       @Variant(id = "placeholder_info.toAscii.0",
                                source = "Returns the ASCII code of the provided character",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "返回指定字符的ASCII码")),
                       @Variant(id = "placeholder_info.toAscii.1",
                                source = "Usage:",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "用法：")),
                       @Variant(id = "placeholder_info.toAscii.2",
                                source = "  {toAscii <character>} -> ASCII code of the character",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "  {toAscii <字符>} -> 此字符的ASCII码")),
                       @Variant(id = "placeholder_info.toChars.0",
                                source = "Returns the characters of the provided string with spaces between them",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "返回将提供的字符串按字符分隔并以空格连接的结果")),
                       @Variant(id = "placeholder_info.toChars.1",
                                source = "Example: {toChars example} -> 'e x a m p l e'",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "例：{toChars example} -> 'e x a m p l e'")),
                       @Variant(id = "placeholder_info.toChars.2",
                                source = "Usage:",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "用法：")),
                       @Variant(id = "placeholder_info.toChars.3",
                                source = "  {toChars <arg>} -> characters",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "  {toChars <参数>} -> 一串字符")),
                       @Variant(id = "placeholder_info.underline.0",
                                source = "Returns the text from the first argument, underlined",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "返回有下划线的文本（第一个参数）")),
                       @Variant(id = "placeholder_info.underline.1",
                                source = "Usage:",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "用法：")),
                       @Variant(id = "placeholder_info.underline.2",
                                source = "  {underline <text>} -> underlined text",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "  {underline <文本>} -> 有下划线的文本")),
                       @Variant(id = "placeholder_info.voltage.0",
                                source = "Returns the voltage in the wire/cable the cover is on.",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "返回覆盖板所贴附的的导线/线缆的电压大小。")),
                       @Variant(id = "placeholder_info.voltage.1",
                                source = "Usage:",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "用法：")),
                       @Variant(id = "placeholder_info.voltage.2",
                                source = "  {voltage} -> the voltage in the wire/cable",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "  {voltage} -> 此导线/线缆的电压")),
                       @Variant(id = "computer_monitor_cover.error.bf_invalid",
                                source = "Invalid character at %d",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "无效字符位于%d")),
                       @Variant(id = "tooltip.player_name.placeholder_processor",
                                source = "Placeholder processor",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "占位符处理器")),
               })
public class GTPlaceholders {

    public static int countItems(String id, @Nullable IItemHandler itemHandler) {
        if (itemHandler == null) return 0;
        int cnt = 0;
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            ItemStack itemStack = itemHandler.getStackInSlot(i);
            String itemId = "%s:%s".formatted(itemStack.getItem().getCreatorModId(itemStack),
                    itemStack.getItem().toString());
            if (itemId.equals(id)) cnt += itemStack.getCount();
        }
        return cnt;
    }

    public static int countFluids(@Nullable String id, @Nullable IFluidHandler fluidHandler) {
        if (fluidHandler == null) return 0;
        int count = 0;
        for (int i = 0; i < fluidHandler.getTanks(); i++) {
            FluidStack fluidStack = fluidHandler.getFluidInTank(i);
            String fluidId = BuiltInRegistries.FLUID.getKey(fluidStack.getFluid()).toString();

            if (id == null || fluidId.equals(id)) {
                count += fluidStack.getAmount();
            }
        }
        return count;
    }

    public static int countItems(@Nullable ItemFilter filter, @Nullable IItemHandler itemHandler) {
        if (itemHandler == null)
            return -1;
        int cnt = 0;
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            if (filter == null || filter.test(itemHandler.getStackInSlot(i)))
                cnt += itemHandler.getStackInSlot(i).getCount();
        }
        return cnt;
    }

    public static void initPlaceholders() {
        GTPM.runOnClient(() -> GTPlaceholders::initRenderers);
        if (GTPM.MODS.AE2.isLoaded()) {
            GTAEPlaceholders.init();
        }

        PlaceholderHandler.addPlaceholder(new Placeholder("energy") {

            @Override
            public MultiLineComponent apply(PlaceholderContext ctx,
                                            List<MultiLineComponent> args) throws PlaceholderException {
                PlaceholderUtils.checkArgs(args, 0);
                if (ctx.level().getBlockEntity(ctx.pos()) instanceof IEnergyInfoProvider energyInfoProvider) {
                    return MultiLineComponent.literal(energyInfoProvider.getEnergyInfo().stored().longValue());
                }
                IEnergyContainer energy = GTCapabilityHelper.getEnergyContainer(ctx.level(), ctx.pos(), ctx.side());
                return MultiLineComponent.literal(energy != null ? energy.getEnergyStored() : 0);
            }
        });
        PlaceholderHandler.addPlaceholder(new Placeholder("energyCapacity") {

            @Override
            public MultiLineComponent apply(PlaceholderContext ctx,
                                            List<MultiLineComponent> args) throws PlaceholderException {
                PlaceholderUtils.checkArgs(args, 0);
                if (ctx.level().getBlockEntity(ctx.pos()) instanceof IEnergyInfoProvider energyInfoProvider) {
                    return MultiLineComponent.literal(energyInfoProvider.getEnergyInfo().capacity().longValue());
                }
                IEnergyContainer energy = GTCapabilityHelper.getEnergyContainer(ctx.level(), ctx.pos(), ctx.side());
                return MultiLineComponent.literal(energy != null ? energy.getEnergyCapacity() : 0);
            }
        });
        PlaceholderHandler.addPlaceholder(new Placeholder("calc") {

            @Override
            public MultiLineComponent apply(PlaceholderContext ctx, List<MultiLineComponent> args) {
                List<String> stringArgs = new ArrayList<>();
                args.forEach((components) -> stringArgs.add(GTStringUtils.componentsToString(components)));
                return MultiLineComponent.literal(GTStringUtils.calc(stringArgs));
            }
        });
        PlaceholderHandler.addPlaceholder(new Placeholder("itemCount") {

            @Override
            public MultiLineComponent apply(PlaceholderContext ctx,
                                            List<MultiLineComponent> args) throws PlaceholderException {
                IItemHandler itemHandler = GTCapabilityHelper.getItemHandler(ctx.level(), ctx.pos(), ctx.side());
                if (args.isEmpty()) {
                    return MultiLineComponent.literal(countItems((ItemFilter) null, itemHandler));
                }
                if (args.size() == 1) {
                    return MultiLineComponent.literal(countItems(
                            GTStringUtils.componentsToString(args.getFirst()), itemHandler));
                }
                if (GTStringUtils.equals(args.getFirst(), "filter")) {
                    int slot = PlaceholderUtils.toInt(args.get(1));
                    if (ctx.itemStackHandler() == null)
                        throw new NotSupportedException();
                    PlaceholderUtils.checkRange("slot index", 1, ctx.itemStackHandler().getSlots(), slot);
                    try {
                        return MultiLineComponent.literal(countItems(
                                ItemFilter.loadFilter(ctx.itemStackHandler().getStackInSlot(slot - 1)), itemHandler));
                    } catch (NullPointerException e) {
                        throw new MissingItemException("filter", slot);
                    }
                }
                throw new InvalidArgsException();
            }
        });
        PlaceholderHandler.addPlaceholder(new Placeholder("fluidCount") {

            @Override
            public MultiLineComponent apply(PlaceholderContext ctx,
                                            List<MultiLineComponent> args) throws PlaceholderException {
                IFluidHandler fluidHandler = GTCapabilityHelper.getFluidHandler(ctx.level(), ctx.pos(), ctx.side());
                if (args.isEmpty()) return MultiLineComponent.literal(countFluids(null, fluidHandler));
                if (args.size() == 1)
                    return MultiLineComponent
                            .literal(countFluids(GTStringUtils.componentsToString(args.getFirst()), fluidHandler));
                PlaceholderUtils.checkArgs(args, 1);
                return MultiLineComponent.empty(); // unreachable
            }
        });
        PlaceholderHandler.addPlaceholder(new Placeholder("if") {

            @Override
            public MultiLineComponent apply(PlaceholderContext ctx,
                                            List<MultiLineComponent> args) throws PlaceholderException {
                PlaceholderUtils.checkArgs(args, 2, true);
                try {
                    if (GTStringUtils.toDouble(args.get(0)) != 0) {
                        return new MultiLineComponent(args.get(1)).setIgnoreSpaces(true);
                    } else if (args.size() > 2) {
                        return new MultiLineComponent(args.get(2)).setIgnoreSpaces(true);
                    } else return MultiLineComponent.empty();
                } catch (NumberFormatException e) {
                    return args.get(1);
                }
            }
        });
        PlaceholderHandler.addPlaceholder(new Placeholder("color") {

            @Override
            public MultiLineComponent apply(PlaceholderContext ctx,
                                            List<MultiLineComponent> args) throws PlaceholderException {
                PlaceholderUtils.checkArgs(args, 2);
                ChatFormatting color = ChatFormatting.getByName(GTStringUtils.componentsToString(args.getFirst()));
                if (color == null) throw new InvalidArgsException();
                return new MultiLineComponent(args.get(1).stream().map(c -> c.withStyle(color)).toList());
            }
        });
        PlaceholderHandler.addPlaceholder(new Placeholder("underline") {

            @Override
            public MultiLineComponent apply(PlaceholderContext ctx,
                                            List<MultiLineComponent> args) throws PlaceholderException {
                PlaceholderUtils.checkArgs(args, 1);
                return new MultiLineComponent(
                        args.getFirst().stream().map(c -> c.withStyle(ChatFormatting.UNDERLINE)).toList());
            }
        });
        PlaceholderHandler.addPlaceholder(new Placeholder("strike") {

            @Override
            public MultiLineComponent apply(PlaceholderContext ctx,
                                            List<MultiLineComponent> args) throws PlaceholderException {
                PlaceholderUtils.checkArgs(args, 1);
                return new MultiLineComponent(
                        args.getFirst().stream().map(c -> c.withStyle(ChatFormatting.STRIKETHROUGH)).toList());
            }
        });
        PlaceholderHandler.addPlaceholder(new Placeholder("obf") {

            @Override
            public MultiLineComponent apply(PlaceholderContext ctx,
                                            List<MultiLineComponent> args) throws PlaceholderException {
                PlaceholderUtils.checkArgs(args, 1);
                return new MultiLineComponent(
                        args.getFirst().stream().map(c -> c.withStyle(ChatFormatting.OBFUSCATED)).toList());
            }
        });
        PlaceholderHandler.addPlaceholder(new Placeholder("random") {

            @Override
            public MultiLineComponent apply(PlaceholderContext ctx,
                                            List<MultiLineComponent> args) throws PlaceholderException {
                PlaceholderUtils.checkArgs(args, 2);
                return MultiLineComponent.literal(GTValues.RNG.nextIntBetweenInclusive(
                        PlaceholderUtils.toInt(args.getFirst()), PlaceholderUtils.toInt(args.get(1))));
            }
        });
        PlaceholderHandler.addPlaceholder(new Placeholder("repeat") {

            @Override
            public MultiLineComponent apply(PlaceholderContext ctx,
                                            List<MultiLineComponent> args) throws PlaceholderException {
                PlaceholderUtils.checkArgs(args, 2);
                int count = PlaceholderUtils.toInt(args.getFirst());
                PlaceholderUtils.checkRange("n", 0, 50000, count);
                MultiLineComponent out = MultiLineComponent.empty();
                for (int i = 0; i < count; i++) out.append(args.get(1));
                return out.setIgnoreSpaces(true);
            }
        });
        PlaceholderHandler.addPlaceholder(new Placeholder("block") {

            @Override
            public MultiLineComponent apply(PlaceholderContext ctx,
                                            List<MultiLineComponent> args) throws PlaceholderException {
                PlaceholderUtils.checkArgs(args, 0);
                return MultiLineComponent.literal("█");
            }
        });
        PlaceholderHandler.addPlaceholder(new Placeholder("tick") {

            @Override
            public MultiLineComponent apply(PlaceholderContext ctx,
                                            List<MultiLineComponent> args) throws PlaceholderException {
                PlaceholderUtils.checkArgs(args, 0);
                return MultiLineComponent.literal(ctx.level().getGameTime());
            }
        });
        PlaceholderHandler.addPlaceholder(new Placeholder("select") {

            @Override
            public MultiLineComponent apply(PlaceholderContext ctx,
                                            List<MultiLineComponent> args) throws PlaceholderException {
                PlaceholderUtils.checkArgs(args, 1, true);
                int i = PlaceholderUtils.toInt(args.getFirst());
                PlaceholderUtils.checkArgs(args, i + 1, true);
                return new MultiLineComponent(args.get(i + 1)).setIgnoreSpaces(true);
            }
        });
        PlaceholderHandler.addPlaceholder(new Placeholder("redstone") {

            @Override
            public MultiLineComponent apply(PlaceholderContext ctx,
                                            List<MultiLineComponent> args) throws PlaceholderException {
                PlaceholderUtils.checkArgs(args, 2, true);
                if (GTStringUtils.equals(args.getFirst(), "get")) {
                    Direction direction = Direction.byName(GTStringUtils.componentsToString(args.get(1)));
                    if (direction == null)
                        throw new InvalidArgsException();
                    return MultiLineComponent.literal(ctx.level()
                            .getSignal(ctx.pos().relative(direction), direction));
                } else if (GTStringUtils.equals(args.get(0), "set")) {
                    int power = PlaceholderUtils.toInt(args.get(1));
                    PlaceholderUtils.checkRange("redstone power", 0, 15, power);
                    if (ctx.cover() == null) throw new NotSupportedException();
                    ctx.cover().setRedstoneSignalOutput(power);
                    return MultiLineComponent.empty();
                }
                throw new InvalidArgsException();
            }
        });
        PlaceholderHandler.addPlaceholder(new Placeholder("previousText") {

            @Override
            public MultiLineComponent apply(PlaceholderContext ctx,
                                            List<MultiLineComponent> args) throws PlaceholderException {
                PlaceholderUtils.checkArgs(args, 1);
                int i = PlaceholderUtils.toInt(args.getFirst());
                if (ctx.previousText() == null) throw new NotSupportedException();
                PlaceholderUtils.checkRange("line", 1, ctx.previousText().size(), i);
                return MultiLineComponent.of(ctx.previousText().get(i - 1)).setIgnoreSpaces(true);
            }
        });
        PlaceholderHandler.addPlaceholder(new Placeholder("progress") {

            @Override
            public MultiLineComponent apply(PlaceholderContext ctx,
                                            List<MultiLineComponent> args) throws PlaceholderException {
                PlaceholderUtils.checkArgs(args, 0);
                IWorkable workable = GTCapabilityHelper.getWorkable(ctx.level(),
                        ctx.pos(), ctx.side());
                if (workable == null) throw new NotSupportedException();
                return MultiLineComponent.literal(workable.getProgress());
            }
        });
        PlaceholderHandler.addPlaceholder(new Placeholder("maxProgress") {

            @Override
            public MultiLineComponent apply(PlaceholderContext ctx,
                                            List<MultiLineComponent> args) throws PlaceholderException {
                PlaceholderUtils.checkArgs(args, 0);
                IWorkable workable = GTCapabilityHelper.getWorkable(ctx.level(),
                        ctx.pos(), ctx.side());
                if (workable == null) throw new NotSupportedException();
                return MultiLineComponent.literal(workable.getMaxProgress());
            }
        });
        PlaceholderHandler.addPlaceholder(new Placeholder("maintenance") {

            @Override
            public MultiLineComponent apply(PlaceholderContext ctx,
                                            List<MultiLineComponent> args) throws PlaceholderException {
                PlaceholderUtils.checkArgs(args, 0);
                IMaintenanceMachine maintenance = GTCapabilityHelper.getMaintenanceMachine(ctx.level(),
                        ctx.pos(), ctx.side());
                if (maintenance == null) throw new NotSupportedException();
                return MultiLineComponent.literal(maintenance.hasMaintenanceProblems() ? 1 : 0);
            }
        });
        PlaceholderHandler.addPlaceholder(new Placeholder("active") {

            @Override
            public MultiLineComponent apply(PlaceholderContext ctx,
                                            List<MultiLineComponent> args) throws PlaceholderException {
                PlaceholderUtils.checkArgs(args, 0);
                IWorkable workable = GTCapabilityHelper.getWorkable(ctx.level(),
                        ctx.pos(), ctx.side());
                if (workable == null) throw new NotSupportedException();
                return MultiLineComponent.literal(workable.isActive() ? 1 : 0);
            }
        });
        PlaceholderHandler.addPlaceholder(new Placeholder("voltage") {

            @Override
            public MultiLineComponent apply(PlaceholderContext ctx,
                                            List<MultiLineComponent> args) throws PlaceholderException {
                PlaceholderUtils.checkArgs(args, 0);
                if (ctx.level().getBlockEntity(ctx.pos()) instanceof CableBlockEntity cable) {
                    return MultiLineComponent.literal(cable.getAverageVoltage());
                }
                throw new NotSupportedException();
            }
        });
        PlaceholderHandler.addPlaceholder(new Placeholder("amperage") {

            @Override
            public MultiLineComponent apply(PlaceholderContext ctx,
                                            List<MultiLineComponent> args) throws PlaceholderException {
                PlaceholderUtils.checkArgs(args, 0);
                if (ctx.level().getBlockEntity(ctx.pos()) instanceof CableBlockEntity cable) {
                    return MultiLineComponent.literal(cable.getAverageAmperage());
                }
                throw new NotSupportedException();
            }
        });
        PlaceholderHandler.addPlaceholder(new Placeholder("count") {

            @Override
            public MultiLineComponent apply(PlaceholderContext ctx,
                                            List<MultiLineComponent> args) throws PlaceholderException {
                PlaceholderUtils.checkArgs(args, 1, true);
                String arg1 = GTStringUtils.componentsToString(args.getFirst());
                int cnt = -1;
                for (List<MutableComponent> arg : args) {
                    if (GTStringUtils.equals(arg, arg1)) cnt++;
                }
                return MultiLineComponent.literal(cnt);
            }
        });
        PlaceholderHandler.addPlaceholder(new Placeholder("data") {

            @Override
            public MultiLineComponent apply(PlaceholderContext ctx,
                                            List<MultiLineComponent> args) throws PlaceholderException {
                PlaceholderUtils.checkArgs(args, 2, true);
                int slot = PlaceholderUtils.toInt(args.get(1));
                int inventorySlot;
                IItemHandler inventory;
                if (slot == 0) {
                    MonitorGroup group = ctx.monitorGroup();
                    if (group == null) throw new NotSupportedException();
                    inventory = group.getTargetDataItems(ctx.monitorLevel());
                    if (inventory == null) throw new NotSupportedException();
                    inventorySlot = group.getDataSlot();
                    PlaceholderUtils.checkRange("target slot", 0, inventory.getSlots() - 1, inventorySlot);
                } else {
                    inventory = ctx.itemStackHandler();
                    if (inventory == null) throw new NotSupportedException();
                    PlaceholderUtils.checkRange("slot index", 1, inventory.getSlots(), slot);
                    inventorySlot = slot - 1;
                }

                ItemStack stack = inventory.getStackInSlot(inventorySlot);
                DataItem component = stack.get(GTDataComponents.DATA_ITEM);
                FormatStringList data = stack.get(GTDataComponents.COMPUTER_MONITOR_DATA);
                if (component == null || data == null) throw new MissingItemException("any data item", slot);

                int capacity = component.capacity();
                int pointer = Math.floorMod(stack.getOrDefault(GTDataComponents.COMPUTER_MONITOR_P, 0), capacity);
                String operation = args.getFirst().toString();
                if (operation.equals("getp")) return MultiLineComponent.literal(pointer);
                if (operation.equals("get")) {
                    int index = getDataIndex(args.size() > 2 ? args.get(2) : null, pointer, capacity);
                    String value = index < data.lines().size() ? data.lines().get(index) : "";
                    return MultiLineComponent.literal(value).setIgnoreSpaces(true);
                }

                if (!(inventory instanceof IItemHandlerModifiable writable)) throw new NotSupportedException();
                ItemStack updated = stack.copy();
                switch (operation) {
                    case "set" -> {
                        PlaceholderUtils.checkArgs(args, 3, true);
                        boolean hasIndex = args.size() > 3;
                        int index = getDataIndex(hasIndex ? args.get(2) : null, pointer, capacity);
                        FormatStringList.Mutable contents = data.mutable();
                        while (contents.size() <= index) {
                            contents.add("");
                        }
                        contents.set(index, args.get(hasIndex ? 3 : 2).toString());
                        updated.set(GTDataComponents.COMPUTER_MONITOR_DATA, contents.toImmutable());
                    }
                    case "setp" -> {
                        PlaceholderUtils.checkArgs(args, 3, true);
                        // Older scripts can supply an unused index before the new pointer.
                        int value = PlaceholderUtils.toInt(args.get(args.size() > 3 ? 3 : 2));
                        updated.set(GTDataComponents.COMPUTER_MONITOR_P, Math.floorMod(value, capacity));
                    }
                    case "inc" -> updated.set(GTDataComponents.COMPUTER_MONITOR_P, (pointer + 1) % capacity);
                    case "dec" -> updated.set(GTDataComponents.COMPUTER_MONITOR_P,
                            Math.floorMod(pointer - 1, capacity));
                    default -> throw new InvalidArgsException();
                }

                if (!ItemStack.matches(stack, updated)) writable.setStackInSlot(inventorySlot, updated);
                return MultiLineComponent.empty();
            }

            private int getDataIndex(@Nullable MultiLineComponent arg, int pointer,
                                     int capacity) throws PlaceholderException {
                if (arg == null || arg.equalsString("")) return pointer;
                int index = PlaceholderUtils.toInt(arg);
                PlaceholderUtils.checkRange("index", 0, capacity - 1, index);
                return index;
            }
        });
        PlaceholderHandler.addPlaceholder(new Placeholder("combine") {

            @Override
            public MultiLineComponent apply(PlaceholderContext ctx, List<MultiLineComponent> args) {
                MultiLineComponent out = MultiLineComponent.empty();
                for (int i = 0; i < args.size(); i++) {
                    out.append(args.get(i));
                    if (i != args.size() - 1) out.append(" ");
                }
                return out.setIgnoreSpaces(true);
            }
        });
        PlaceholderHandler.addPlaceholder(new Placeholder("nbt") {

            @Override
            public MultiLineComponent apply(PlaceholderContext ctx,
                                            List<MultiLineComponent> args) throws PlaceholderException {
                PlaceholderUtils.checkArgs(args, 1, true);
                int slot = GTStringUtils.toInt(args.getFirst());
                if (ctx.itemStackHandler() == null) throw new NotSupportedException();
                PlaceholderUtils.checkRange("slot index", 1, ctx.itemStackHandler().getSlots(), slot);
                return MultiLineComponent
                        .literal(ctx.itemStackHandler().getStackInSlot(slot - 1).getComponents().toString());
            }
        });
        PlaceholderHandler.addPlaceholder(new Placeholder("toChars") {

            @Override
            public MultiLineComponent apply(PlaceholderContext ctx,
                                            List<MultiLineComponent> args) throws PlaceholderException {
                PlaceholderUtils.checkArgs(args, 1);
                if (args.getFirst().isEmpty()) return MultiLineComponent.empty();
                StringBuilder out = new StringBuilder();
                for (char c : GTStringUtils.componentsToString(args.getFirst()).toCharArray())
                    out.append(c).append(' ');
                return MultiLineComponent.literal(out.substring(0, out.length() - 2));
            }
        });
        PlaceholderHandler.addPlaceholder(new Placeholder("toAscii") {

            @Override
            public MultiLineComponent apply(PlaceholderContext ctx,
                                            List<MultiLineComponent> args) throws PlaceholderException {
                PlaceholderUtils.checkArgs(args, 1);
                String arg = args.getFirst().toString();
                if (arg.length() != 1) throw new InvalidArgsException();
                return MultiLineComponent.literal((int) arg.toCharArray()[0]);
            }
        });
        PlaceholderHandler.addPlaceholder(new Placeholder("fromAscii") {

            @Override
            public MultiLineComponent apply(PlaceholderContext ctx,
                                            List<MultiLineComponent> args) throws PlaceholderException {
                PlaceholderUtils.checkArgs(args, 1);
                return MultiLineComponent.literal((char) PlaceholderUtils.toInt(args.getFirst())).setIgnoreSpaces(true);
            }
        });
        PlaceholderHandler.addPlaceholder(new Placeholder("subList") {

            @Override
            public MultiLineComponent apply(PlaceholderContext ctx,
                                            List<MultiLineComponent> args) throws PlaceholderException {
                PlaceholderUtils.checkArgs(args, 2, true);
                int l = PlaceholderUtils.toInt(args.getFirst());
                int r = PlaceholderUtils.toInt(args.get(1));
                PlaceholderUtils.checkRange("start index", 0, args.size(), l);
                PlaceholderUtils.checkRange("end index", 0, args.size(), r);
                MultiLineComponent out = MultiLineComponent.empty();
                for (int i = l; i < r - 1; i++) out.append(args.get(i)).append(' ');
                out.append(args.get(r - 1));
                out.setIgnoreSpaces(true);
                return out;
            }
        });
        PlaceholderHandler.addPlaceholder(new Placeholder("cmp") {

            @Override
            public MultiLineComponent apply(PlaceholderContext ctx,
                                            List<MultiLineComponent> args) throws PlaceholderException {
                PlaceholderUtils.checkArgs(args, 3);
                double a = PlaceholderUtils.toDouble(args.getFirst());
                double b = PlaceholderUtils.toDouble(args.get(2));
                return switch (args.get(1).toString()) {
                    case ">" -> MultiLineComponent.literal(a > b ? 1 : 0);
                    case "<" -> MultiLineComponent.literal(a < b ? 1 : 0);
                    case ">=" -> MultiLineComponent.literal(a >= b ? 1 : 0);
                    case "<=" -> MultiLineComponent.literal(a <= b ? 1 : 0);
                    case "==" -> MultiLineComponent.literal(a == b ? 1 : 0);
                    case "!=" -> MultiLineComponent.literal(a != b ? 1 : 0);
                    default -> throw new InvalidArgsException();
                };
            }
        });

        PlaceholderHandler.addPlaceholder(new Placeholder("bf") {

            @Override
            public MultiLineComponent apply(PlaceholderContext ctx,
                                            List<MultiLineComponent> args) throws PlaceholderException {
                PlaceholderUtils.checkArgs(args, 2);
                int slot = PlaceholderUtils.toInt(args.getFirst());
                PlaceholderUtils.checkRange("slot index", 1, 8, slot);
                if (ctx.itemStackHandler() == null) throw new NotSupportedException();

                ItemStack stack = ctx.itemStackHandler().getStackInSlot(slot - 1);
                FormatStringList immutableData = stack.get(GTDataComponents.COMPUTER_MONITOR_DATA);
                if (immutableData == null) {
                    throw new MissingItemException("any data item", slot);
                }
                FormatStringList.Mutable monitorData = immutableData.mutable();

                int operationsLeft = 5000;
                int p = 0, cnt = 0, num = 0;
                String rawCode = args.get(1).toString().replaceAll("[^+\\-><\\[\\]]", "");
                StringBuilder codeBuilder = new StringBuilder();
                // optimize BF code ("[----[+++]-<<-]" -> "[4-[3+]1-2<1-]")
                @Nullable
                Character cur = null;
                for (char i : rawCode.toCharArray()) {
                    if (cur != null) {
                        if (cur == i) cnt++;
                        else {
                            codeBuilder.append(cnt).append(cur);
                            cur = null;
                            cnt = 0;
                        }
                    }
                    if (cur == null) {
                        if (List.of('+', '-', '<', '>').contains(i)) {
                            cur = i;
                            cnt = 1;
                        } else codeBuilder.append(i);
                    }
                }
                if (cur != null) codeBuilder.append(cnt).append(cur);
                String code = codeBuilder.toString();
                Stack<Integer> loops = new Stack<>();
                if (!getData(ctx).getBoolean("completed")) {
                    p = getData(ctx).getInt("pointer");
                    getData(ctx).getInt("index");
                    num = getData(ctx).getInt("num");
                }
                getData(ctx).putBoolean("completed", true);
                for (int i = 0; i < code.length(); i++) {
                    if (operationsLeft <= 0) {
                        getData(ctx).putBoolean("completed", false);
                        getData(ctx).putInt("pointer", p);
                        getData(ctx).putInt("index", i);
                        getData(ctx).putInt("num", num);
                        break;
                    }
                    while (monitorData.size() <= p) {
                        monitorData.add("0");
                    }
                    if (monitorData.get(p).isEmpty()) {
                        monitorData.set(i, "0");
                    }
                    try {
                        switch (code.charAt(i)) {
                            case '+' -> monitorData.set(p,
                                    String.valueOf((Integer.parseInt(monitorData.get(p)) + num) % 256));
                            case '-' -> {
                                int tmp = Integer.parseInt(monitorData.get(p)) - num;
                                if (tmp < 0) tmp = (256 - ((-tmp) % 256)) % 256;
                                monitorData.set(p, String.valueOf(tmp));
                            }
                            case '>' -> p += num;
                            case '<' -> p -= num;
                            case '[' -> loops.push(i);
                            case ']' -> {
                                if (Integer.parseInt(monitorData.get(p)) == 0) {
                                    loops.pop();
                                } else {
                                    i = loops.peek();
                                }
                            }
                            default -> throw new PlaceholderException(Component
                                    .translatable("gtpm.computer_monitor_cover.error.bf_invalid", i).getString());
                        }
                    } catch (Exception e) {

                    }
                    if (Character.isDigit(code.charAt(i))) {
                        num = num * 10 + code.charAt(i) - '0';
                    } else num = 0;
                    operationsLeft--;
                }
                return MultiLineComponent.empty();
            }
        });

        PlaceholderHandler.addPlaceholder(new Placeholder("cmd") {

            @Override
            public MultiLineComponent apply(PlaceholderContext ctx,
                                            List<MultiLineComponent> args) throws PlaceholderException {
                PlaceholderUtils.checkArgs(args, 2);
                if (ctx.itemStackHandler() == null) throw new NotSupportedException();
                int slot = PlaceholderUtils.toInt(args.getFirst());
                PlaceholderUtils.checkRange("slot index", 1, ctx.itemStackHandler().getSlots(), slot);
                ItemStack stack = ctx.itemStackHandler().getStackInSlot(slot - 1);

                BindingData bindingData = stack.get(GTDataComponents.BINDING_DATA);
                if (bindingData == null) {
                    throw new MissingItemException("any data item bound to player", slot);
                }

                Component displayName = bindingData.getBoundPlayerName(ctx.level());
                if (displayName == null) {
                    displayName = Component.translatable("gtpm.tooltip.player_name.placeholder_processor");
                }

                if (!(ctx.level() instanceof ServerLevel serverLevel)) {
                    throw new NotSupportedException();
                }
                MinecraftServer server = serverLevel.getServer();
                Player player = ctx.level().getPlayerByUUID(bindingData.uuid());

                MultiLineComponent output = MultiLineComponent.empty();
                CommandSource customSource = new CommandSource() {

                    @Override
                    public void sendSystemMessage(@NotNull Component message) {
                        output.append(List.of(message)).appendNewline();
                    }

                    @Override
                    public boolean acceptsSuccess() {
                        return true;
                    }

                    @Override
                    public boolean acceptsFailure() {
                        return true;
                    }

                    @Override
                    public boolean shouldInformAdmins() {
                        return false;
                    }
                };

                CommandSourceStack source = new CommandSourceStack(
                        customSource,
                        ctx.pos().getCenter(),
                        Vec2.ZERO,
                        serverLevel,
                        bindingData.permissionLevel(),
                        displayName.getString(),
                        displayName,
                        server,
                        player);
                server.getCommands().performPrefixedCommand(source, args.get(1).toString());
                return output;
            }
        });
        PlaceholderHandler.addPlaceholder(new Placeholder("tm") {

            @Override
            public MultiLineComponent apply(PlaceholderContext ctx, List<MultiLineComponent> args) {
                return MultiLineComponent.literal("™");
            }
        });
        PlaceholderHandler.addPlaceholder(new Placeholder("formatInt") {

            @Override
            public MultiLineComponent apply(PlaceholderContext ctx,
                                            List<MultiLineComponent> args) throws PlaceholderException {
                PlaceholderUtils.checkArgs(args, 1);
                long n = PlaceholderUtils.toLong(args.getFirst());
                Map<Long, String> suffixes = Map.of(
                        1L, "",
                        1000L, "K",
                        1000000L, "M",
                        1000000000L, "B",
                        1000000000000L, "T");
                long max = 1;
                for (long i : suffixes.keySet()) {
                    if (n >= i && max < i) max = i;
                }
                return MultiLineComponent.literal(String.format(Locale.ROOT, "%.2f%s",
                        ((double) n) / max, suffixes.get(max)));
            }
        });
        PlaceholderHandler.addPlaceholder(new Placeholder("click") {

            @Override
            public MultiLineComponent apply(PlaceholderContext ctx,
                                            List<MultiLineComponent> args) throws PlaceholderException {
                if (!(MetaMachine.getMachine(ctx.level(), ctx.pos()) instanceof AdvancedMonitorPartMachine monitor))
                    throw new NotSupportedException();
                monitor.resetClicked();
                if (args.isEmpty()) return MultiLineComponent.literal(monitor.isClicked() ? 1 : 0);
                PlaceholderUtils.checkArgs(args, 1);
                if (args.getFirst().equalsString("x")) return MultiLineComponent.literal(monitor.getClickPosX());
                if (args.getFirst().equalsString("y")) return MultiLineComponent.literal(monitor.getClickPosY());
                throw new InvalidArgsException();
            }
        });
        PlaceholderHandler.addPlaceholder(new Placeholder("ender") {

            @Override
            public MultiLineComponent apply(PlaceholderContext ctx,
                                            List<MultiLineComponent> args) throws PlaceholderException {
                PlaceholderUtils.checkArgs(args, 2, true);
                String type = args.get(0).toString();
                String channel = args.get(1).toString();
                UUID owner = null;
                if (args.size() > 2 && !args.get(2).toString().isEmpty()) {
                    if (ctx.itemStackHandler() == null) throw new NotSupportedException();
                    int slot = PlaceholderUtils.toInt(args.get(2));
                    PlaceholderUtils.checkRange("slot index", 1, ctx.itemStackHandler().getSlots(), slot);
                    ItemStack stack = ctx.itemStackHandler().getStackInSlot(slot - 1);

                    if (stack.has(GTDataComponents.BINDING_DATA)) {
                        owner = stack.get(GTDataComponents.BINDING_DATA).uuid();
                    }
                }
                VirtualEnderRegistry ender = VirtualEnderRegistry.getInstance();
                switch (type) {
                    case "redstone" -> {
                        channel = "ERLink#" + channel;
                        if (!ender.hasEntry(owner, EntryTypes.ENDER_REDSTONE, channel))
                            return MultiLineComponent.literal(0);
                        if (args.size() > 4) {
                            if (ctx.itemStackHandler() == null) throw new NotSupportedException();
                            int slot = PlaceholderUtils.toInt(args.get(3));
                            PlaceholderUtils.checkRange("slot index", 1, ctx.itemStackHandler().getSlots(), slot);
                            ItemStack stack = ctx.itemStackHandler().getStackInSlot(slot - 1);
                            UUID uuid;
                            if (stack.has(GTDataComponents.ENDER_REDSTONE_LINK_TRANSMITTER_UUID)) {
                                uuid = stack.get(GTDataComponents.ENDER_REDSTONE_LINK_TRANSMITTER_UUID);
                            } else {
                                uuid = UUID.randomUUID();
                                stack.set(GTDataComponents.ENDER_REDSTONE_LINK_TRANSMITTER_UUID, uuid);
                            }
                            int power = PlaceholderUtils.toInt(args.get(4));
                            PlaceholderUtils.checkRange("redstone signal", 0, 15, power);
                            ender.getEntry(owner, EntryTypes.ENDER_REDSTONE, channel).setSignal(uuid, power);
                            return MultiLineComponent.empty();
                        }
                        return MultiLineComponent
                                .literal(ender.getEntry(owner, EntryTypes.ENDER_REDSTONE, channel).getSignal());
                    }
                    case "item" -> {
                        channel = "EILink#" + channel;
                        if (!ender.hasEntry(owner, EntryTypes.ENDER_ITEM, channel))
                            return MultiLineComponent.literal(0);
                        IItemHandler items = ender.getEntry(owner, EntryTypes.ENDER_ITEM, channel).getHandler();
                        int count = 0;
                        for (int i = 0; i < items.getSlots(); i++) count += items.getStackInSlot(i).getCount();
                        return MultiLineComponent.literal(count);
                    }
                    case "itemId" -> {
                        channel = "EILink#" + channel;
                        if (!ender.hasEntry(owner, EntryTypes.ENDER_ITEM, channel))
                            return MultiLineComponent.literal(ItemStack.EMPTY.toString());
                        IItemHandler items = ender.getEntry(owner, EntryTypes.ENDER_ITEM, channel).getHandler();
                        if (items.getSlots() == 0) return MultiLineComponent.literal(ItemStack.EMPTY.toString());
                        return MultiLineComponent.literal(items.getStackInSlot(0).toString());
                    }
                    case "itemPull" -> {
                        channel = "EILink#" + channel;
                        if (!ender.hasEntry(owner, EntryTypes.ENDER_ITEM, channel))
                            return MultiLineComponent.empty();
                        IItemHandler items = ender.getEntry(owner, EntryTypes.ENDER_ITEM, channel).getHandler();
                        if (ctx.itemStackHandler() != null)
                            GTTransferUtils.transferItemsFiltered(items, ctx.itemStackHandler(), stack -> true, 1);
                        else throw new NotSupportedException();
                        return MultiLineComponent.empty();
                    }
                    case "itemPush" -> {
                        channel = "EILink#" + channel;
                        if (!ender.hasEntry(owner, EntryTypes.ENDER_ITEM, channel))
                            return MultiLineComponent.empty();
                        IItemHandler items = ender.getEntry(owner, EntryTypes.ENDER_ITEM, channel).getHandler();
                        if (ctx.itemStackHandler() != null)
                            GTTransferUtils.transferItemsFiltered(ctx.itemStackHandler(), items, stack -> true, 1);
                        else throw new NotSupportedException();
                        return MultiLineComponent.empty();
                    }
                    case "fluid" -> {
                        channel = "EFLink#" + channel;
                        if (!ender.hasEntry(owner, EntryTypes.ENDER_FLUID, channel))
                            return MultiLineComponent.literal(0);
                        return MultiLineComponent.literal(
                                ender.getEntry(owner, EntryTypes.ENDER_FLUID, channel).getFluidTank().getFluidAmount());
                    }
                    default -> throw new InvalidArgsException();
                }
            }
        });
        PlaceholderHandler.addPlaceholder(new Placeholder("eval") {

            @Override
            public MultiLineComponent apply(PlaceholderContext ctx,
                                            List<MultiLineComponent> args) throws PlaceholderException {
                PlaceholderUtils.checkArgs(args, 1);
                return PlaceholderHandler.processPlaceholders(args.getFirst().toString(), ctx);
            }
        });

        PlaceholderHandler.addPlaceholder(new Placeholder("module") {

            @Override
            public MultiLineComponent apply(PlaceholderContext ctx,
                                            List<MultiLineComponent> args) throws PlaceholderException {
                PlaceholderUtils.checkArgs(args, 3);
                int slot = PlaceholderUtils.toInt(args.get(0));
                double x = PlaceholderUtils.toDouble(args.get(1));
                double y = PlaceholderUtils.toDouble(args.get(2));
                if (ctx.itemStackHandler() == null) throw new NotSupportedException();
                PlaceholderUtils.checkRange("slot index", 1, ctx.itemStackHandler().getSlots(), slot);
                ItemStack stack = ctx.itemStackHandler().getStackInSlot(slot);
                if (stack.getItem() instanceof IComponentItem componentItem) {
                    for (IItemComponent component : componentItem.getComponents()) {
                        if (component instanceof IMonitorModuleItem module) module.tickInPlaceholder(stack, ctx);
                    }
                }
                return MultiLineComponent.empty().addGraphics(new GraphicsComponent(
                        x, y, x, y,
                        "module",
                        PlaceholderRenderData.itemStack(stack)));
            }
        });
        PlaceholderHandler.addPlaceholder(new Placeholder("setImage") {

            @Override
            public MultiLineComponent apply(PlaceholderContext ctx,
                                            List<MultiLineComponent> args) throws PlaceholderException {
                PlaceholderUtils.checkArgs(args, 2);
                int slot = PlaceholderUtils.toInt(args.get(0));
                String url = args.get(1).toString();
                if (ctx.itemStackHandler() == null) throw new NotSupportedException();
                PlaceholderUtils.checkRange("slot index", 1, ctx.itemStackHandler().getSlots(), slot);
                ItemStack stack = ctx.itemStackHandler().getStackInSlot(slot);
                if (stack.getItem() instanceof IComponentItem componentItem) {
                    for (IItemComponent component : componentItem.getComponents()) {
                        if (component instanceof ImageModuleBehaviour module) {
                            module.setUrl(stack, url);
                        }
                    }
                }
                return MultiLineComponent.empty();
            }
        });
        PlaceholderHandler.addPlaceholder(new Placeholder("rect") {

            @Override
            public MultiLineComponent apply(PlaceholderContext ctx,
                                            List<MultiLineComponent> args) throws PlaceholderException {
                PlaceholderUtils.checkArgs(args, 5);
                double x = PlaceholderUtils.toDouble(args.get(0));
                double y = PlaceholderUtils.toDouble(args.get(1));
                double width = PlaceholderUtils.toDouble(args.get(2));
                double height = PlaceholderUtils.toDouble(args.get(3));
                if (x < 0) x = 0;
                if (y < 0) y = 0;
                return MultiLineComponent.empty().addGraphics(new GraphicsComponent(
                        x, y, x + width, y + height,
                        "rect",
                        PlaceholderRenderData.rect((float) width, (float) height,
                                0xFF000000 | PlaceholderUtils.toInt(args.get(4)))));
            }
        });
        PlaceholderHandler.addPlaceholder(new Placeholder("quad") {

            @Override
            public MultiLineComponent apply(PlaceholderContext ctx,
                                            List<MultiLineComponent> args) throws PlaceholderException {
                PlaceholderUtils.checkArgs(args, 12);
                float x1 = PlaceholderUtils.toFloat(args.get(0));
                float y1 = PlaceholderUtils.toFloat(args.get(1));
                float x2 = PlaceholderUtils.toFloat(args.get(2));
                float y2 = PlaceholderUtils.toFloat(args.get(3));
                float x3 = PlaceholderUtils.toFloat(args.get(4));
                float y3 = PlaceholderUtils.toFloat(args.get(5));
                float x4 = PlaceholderUtils.toFloat(args.get(6));
                float y4 = PlaceholderUtils.toFloat(args.get(7));
                return MultiLineComponent.empty().addGraphics(new GraphicsComponent(
                        GTMath.min(x1, x2, x3, x4), GTMath.min(y1, y2, y3, y4), GTMath.max(x1, x2, x3, x4),
                        GTMath.max(y1, y2, y3, y4),
                        "quad",
                        PlaceholderRenderData.quad(
                                0, 0,
                                x2 - x1, y2 - y1,
                                x3 - x1, y3 - y1,
                                x4 - x1, y4 - y1,
                                0xFF000000 + PlaceholderUtils.toInt(args.get(8)),
                                0xFF000000 + PlaceholderUtils.toInt(args.get(9)),
                                0xFF000000 + PlaceholderUtils.toInt(args.get(10)),
                                0xFF000000 + PlaceholderUtils.toInt(args.get(11)))));
            }
        });
        PlaceholderHandler.addPlaceholder(new Placeholder("item") {

            @Override
            public MultiLineComponent apply(PlaceholderContext ctx,
                                            List<MultiLineComponent> args) throws PlaceholderException {
                PlaceholderUtils.checkArgs(args, 1);
                int slot = PlaceholderUtils.toInt(args.getFirst());
                if (ctx.itemStackHandler() == null) throw new NotSupportedException();
                PlaceholderUtils.checkRange("slot index", 1, ctx.itemStackHandler().getSlots(), slot);
                return MultiLineComponent.literal(ctx.itemStackHandler().getStackInSlot(slot - 1).toString());
            }
        });
        PlaceholderHandler.addPlaceholder(new Placeholder("targetSlot") {

            @Override
            public MultiLineComponent apply(PlaceholderContext ctx,
                                            List<MultiLineComponent> args) throws PlaceholderException {
                PlaceholderUtils.checkArgs(args, 0);
                MonitorGroup group = ctx.monitorGroup();
                if (group == null) throw new NotSupportedException();
                return MultiLineComponent.literal(group.getDataSlot() + 1);
            }
        });
        PlaceholderHandler.addPlaceholder(new Placeholder("setTargetSlot") {

            @Override
            public MultiLineComponent apply(PlaceholderContext ctx,
                                            List<MultiLineComponent> args) throws PlaceholderException {
                PlaceholderUtils.checkArgs(args, 1);
                MonitorGroup group = ctx.monitorGroup();
                if (group == null) throw new NotSupportedException();
                IItemHandler dataItems = group.getTargetDataItems(ctx.monitorLevel());
                if (dataItems == null) throw new NotSupportedException();
                int slot = PlaceholderUtils.toInt(args.getFirst());
                PlaceholderUtils.checkRange("slot index", 1, dataItems.getSlots(), slot);
                group.setDataSlot(slot - 1);
                return MultiLineComponent.empty();
            }
        });
        PlaceholderHandler.addPlaceholder(new Placeholder("blockNbt") {

            @Override
            public MultiLineComponent apply(PlaceholderContext ctx,
                                            List<MultiLineComponent> args) throws PlaceholderException {
                BlockEntity blockEntity = ctx.level().getBlockEntity(ctx.pos());
                if (blockEntity == null) return MultiLineComponent.empty();
                var components = blockEntity.components();
                if (args.isEmpty()) {
                    return MultiLineComponent.literal(components.toString());
                }

                ResourceLocation componentId = ResourceLocation.tryParse(args.getFirst().toString());
                if (componentId == null) {
                    throw new InvalidArgsException();
                }
                var componentType = BuiltInRegistries.DATA_COMPONENT_TYPE.get(componentId);
                if (componentType == null || !components.has(componentType)) {
                    return MultiLineComponent.empty();
                }
                return MultiLineComponent.literal(String.valueOf(components.get(componentType)));
            }
        });
    }

    @OnlyIn(Dist.CLIENT)
    public static void initRenderers() {
        PlaceholderHandler.addRenderer("module", new ModulePlaceholderRenderer());
        PlaceholderHandler.addRenderer("rect", new RectPlaceholderRenderer());
        PlaceholderHandler.addRenderer("quad", new QuadPlaceholderRenderer());
    }
}
