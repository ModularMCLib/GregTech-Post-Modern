package com.modularmc.gtpm.integration.ae2;

import com.modularmc.gtpm.api.cover.filter.ItemFilter;
import com.modularmc.gtpm.api.placeholder.*;
import com.modularmc.gtpm.api.placeholder.exceptions.*;
import com.modularmc.gtpm.utils.GTStringUtils;

import com.modularmc.registrate.localization.MinecraftLocale;
import com.modularmc.registrate.localization.annotation.TransVariants;
import com.modularmc.registrate.localization.annotation.Translation;
import com.modularmc.registrate.localization.annotation.Variant;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;

import appeng.api.networking.GridHelper;
import appeng.api.networking.IGrid;
import appeng.api.networking.IGridNode;
import appeng.api.networking.IInWorldGridNodeHost;
import appeng.api.networking.crafting.CraftingJobStatus;
import appeng.api.networking.crafting.ICraftingCPU;
import appeng.api.networking.crafting.ICraftingService;
import appeng.api.stacks.AEFluidKey;
import appeng.api.stacks.AEItemKey;
import appeng.api.stacks.GenericStack;
import appeng.api.stacks.KeyCounter;
import appeng.me.helpers.IGridConnectedBlockEntity;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3i;

import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
@TransVariants(keyPrefix = "gtpm.placeholder_info",
               value = {
                       @Variant(id = "ae2crafting.0",
                                source = "Returns information about auto-crafting in the ME network of the block this cover is on.",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "返回覆盖板所贴附的方块所在的ME网络的自动合成信息")),
                       @Variant(id = "ae2crafting.1",
                                source = "Usage:",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "用法：")),
                       @Variant(id = "ae2crafting.10",
                                source = "  {ae2crafting get <index> time} -> the amount of time elapsed from the start of the craft (in nanoseconds), or 0 if the CPU is idle",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "{ae2crafting get <索引编号> time} -> 指定的CPU从开始制作至现在所经过的时间（以纳秒为单位），若处于空闲状态则返回0")),
                       @Variant(id = "ae2crafting.2",
                                source = "  {ae2crafting get amount} -> the amount of crafting CPUs in the ME network",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "  {ae2crafting get amount} -> ME网络中的合成CPU数量")),
                       @Variant(id = "ae2crafting.3",
                                source = "  {ae2crafting get <index> storage} -> the amount of crafting storage the specified CPU has",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "{ae2crafting get <索引编号> storage} -> 指定的CPU的存储容量")),
                       @Variant(id = "ae2crafting.4",
                                source = "  {ae2crafting get <index> threads} -> the amount of co-processors the specified CPU has",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "{ae2crafting get <索引编号> threads} -> 指定的CPU所拥有的并行处理单元数量")),
                       @Variant(id = "ae2crafting.5",
                                source = "  {ae2crafting get <index> name} -> the name of the specified crafting CPU",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "  {ae2crafting get <索引编号> name} -> 指定的合成CPU名称")),
                       @Variant(id = "ae2crafting.6",
                                source = "  {ae2crafting get <index> selectionMode} -> the selection mode of the specified crafting CPU (used for manual, automatic or both requests)",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "  {ae2crafting get <索引编号> selectionMode} -> 指定的CPU的自动分配模式（用于手动、自动或同时请求）")),
                       @Variant(id = "ae2crafting.7",
                                source = "  {ae2crafting get <index> amount} -> the amount of the item that was requested, or 0 if the CPU is idle",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "  {ae2crafting get <索引编号> amount} -> 指定的CPU中请求物品的数量，处于空闲状态则返回0")),
                       @Variant(id = "ae2crafting.8",
                                source = "  {ae2crafting get <index> item} -> the display name of the item that was requested, or 0 if the CPU is idle",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "  {ae2crafting get <索引编号> item} -> 指定的CPU中请求物品的显示名称，处于空闲状态则返回0")),
                       @Variant(id = "ae2crafting.9",
                                source = "  {ae2crafting get <index> progress} -> the crafting job progress, or 0 if the CPU is idle",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "  {ae2crafting get <索引编号> progress} -> 指定的CPU中当前合成任务的进度，处于空闲状态则返回0")),
                       @Variant(id = "ae2energy.0",
                                source = "Returns the energy currently stored in the ME network of the block this cover is on.",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "返回覆盖板所贴附的方块所在的ME网络的现存能源大小。")),
                       @Variant(id = "ae2energy.1",
                                source = "Usage:",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "用法：")),
                       @Variant(id = "ae2energy.2",
                                source = "  {ae2energy} -> the energy in the ME network (in AE units)",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "  {ae2energy} -> 此ME网络中的现存能源大小（以AE为单位）")),
                       @Variant(id = "ae2fluidCount.0",
                                source = "Same as fluidCount, but counts items in the ME network of the block this cover is attached to.",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "与fluidCount类似，但统计的是覆盖板所贴附的方块所在的ME网络的流体数量。")),
                       @Variant(id = "ae2fluidCount.1",
                                source = "Note that counting all fluids may cause lag!",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "注意：计算所有的流体可能会导致卡顿！")),
                       @Variant(id = "ae2fluidCount.2",
                                source = "Usage:",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "用法：")),
                       @Variant(id = "ae2fluidCount.3",
                                source = "  {fluidCount [fluidId]} -> the amount of all fluids, or the fluid with fluidId if specified",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "  {fluidCount [流体id]} -> 所有流体的总量，若指定了流体id则返回该流体的数量")),
                       @Variant(id = "ae2itemCount.0",
                                source = "Same as itemCount, but counts items in the ME network of the block this cover is attached to.",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "与itemCount类似，但统计的是覆盖板所贴附的方块所在的ME网络的物品数量。")),
                       @Variant(id = "ae2itemCount.1",
                                source = "Note that counting by filter or all items may cause lag!",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "注意：按过滤器筛选或计算所有物品可能会导致卡顿！")),
                       @Variant(id = "ae2itemCount.2",
                                source = "Usage:",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "用法：")),
                       @Variant(id = "ae2itemCount.3",
                                source = "  {itemCount} -> total item amount",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "  {itemCount} -> 总物品数量")),
                       @Variant(id = "ae2itemCount.4",
                                source = "  {itemCount <item_id>} -> amount of items with ids equal to item_id",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "  {itemCount <物品id>} -> 指定的物品的数量")),
                       @Variant(id = "ae2itemCount.5",
                                source = "  {itemCount filter <slot_id>} -> amount of items matching filter in specified slot of this cover",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "  {itemCount filter <槽位id>} -> 覆盖板指定槽位中，符合筛选条件的物品的数量")),
                       @Variant(id = "ae2maxPower.0",
                                source = "Returns the energy capacity of the ME network of the block this cover is on.",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "返回覆盖板所贴附的方块所在的ME网络的最大能源容量。")),
                       @Variant(id = "ae2maxPower.1",
                                source = "Usage:",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "用法：")),
                       @Variant(id = "ae2maxPower.2",
                                source = "  {ae2maxPower} -> the energy capacity of the ME network",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "  {ae2maxPower} -> 此ME网络的最大能源容量")),
                       @Variant(id = "ae2powerUsage.0",
                                source = "Returns the energy consumption of the ME network of the block this cover is on.",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "返回覆盖板所贴附的方块所在的ME网络的耗能大小。")),
                       @Variant(id = "ae2powerUsage.1",
                                source = "Usage:",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "用法：")),
                       @Variant(id = "ae2powerUsage.2",
                                source = "  {ae2powerUsage} -> the energy consumption of the ME network",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "  {ae2powerUsage} -> 此ME网络的耗能大小")),
                       @Variant(id = "ae2spatial.0",
                                source = "Returns information about spatial I/O in the ME network of the block this cover is on.",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "返回覆盖板所贴附的方块所在的ME网络的空间I/O（空间塔）信息。")),
                       @Variant(id = "ae2spatial.1",
                                source = "Usage:",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "用法：")),
                       @Variant(id = "ae2spatial.2",
                                source = "  {ae2spatial power} -> the amount of power required to initiate spatial I/O",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "  {ae2spatial power} -> 启动空间I/O的所需能源大小")),
                       @Variant(id = "ae2spatial.3",
                                source = "  {ae2spatial efficiency} -> the efficiency of the Spatial Containment Structure (SPS)",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "  {ae2spatial efficiency} -> 空间塔的效率")),
                       @Variant(id = "ae2spatial.4",
                                source = "  {ae2spatial size<X|Y|Z>} -> the size of the SPS along the specified axis (example: 'Size: {sizeX}x{sizeY}x{sizeZ}')",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "{ae2spatial size<X|Y|Z>} -> 沿指定轴的SCS规模（例：'尺寸：{X轴长度}×{Y轴长度}×{Z轴长度}'）")),
               })
public class GTAEPlaceholders {

    private GTAEPlaceholders() {}

    private static IGrid getGrid(PlaceholderContext ctx) throws PlaceholderException {
        IInWorldGridNodeHost nodeHost = GridHelper.getNodeHost(ctx.level(), ctx.pos());
        if (nodeHost != null) {
            IGridNode node = nodeHost.getGridNode(ctx.side());
            if (node != null) return node.getGrid();
        }
        BlockEntity blockEntity = ctx.level().getBlockEntity(ctx.pos());
        if (blockEntity instanceof IGridConnectedBlockEntity gridMachine) {
            IGrid nullable = gridMachine.getMainNode().getGrid();
            if (nullable == null) throw new NoMENetworkException();
            return nullable;
        }
        throw new NoMENetworkException();
    }

    private static long countItems(String id, IGrid grid) {
        var key = ResourceLocation.parse(id);
        if (key == null) return 0;
        if (!BuiltInRegistries.ITEM.containsKey(key)) return 0;
        Item item = BuiltInRegistries.ITEM.get(key);
        GenericStack stack = GenericStack.fromItemStack(new ItemStack(item, 1));
        if (stack == null) return 0;
        return grid.getStorageService().getInventory().getAvailableStacks().get(stack.what());
    }

    private static long countItems(@Nullable ItemFilter filter, IGrid grid) {
        KeyCounter stacks = grid.getStorageService().getCachedInventory();
        long count = 0;
        for (var stack : stacks) {
            if (stack.getKey() instanceof AEItemKey &&
                    (filter == null || filter.test(stack.getKey().wrapForDisplayOrFilter())))
                count += stack.getLongValue();
        }
        return count;
    }

    private static long countFluids(@Nullable String id, IGrid grid) {
        if (id == null) {
            KeyCounter stacks = grid.getStorageService().getCachedInventory();
            long count = 0;
            for (var stack : stacks) {
                if (stack.getKey() instanceof AEFluidKey) count += stack.getLongValue();
            }
            return count;
        }
        var fluidId = ResourceLocation.parse(id);
        if (!BuiltInRegistries.FLUID.containsKey(fluidId)) return 0;
        Fluid fluid = BuiltInRegistries.FLUID.get(fluidId);
        GenericStack stack = GenericStack.fromFluidStack(new FluidStack(fluid, 1));
        if (stack == null) return 0;
        return grid.getStorageService().getInventory().getAvailableStacks().get(stack.what());
    }

    private static Vector3i getSpatialSize(IGrid grid) {
        BlockPos start = grid.getSpatialService().getMin();
        BlockPos end = grid.getSpatialService().getMax();
        BlockPos tmp = end.subtract(start);
        return new Vector3i(tmp.getX(), tmp.getY(), tmp.getZ()).absolute();
    }

    public static void init() {
        PlaceholderHandler.addPlaceholder(new Placeholder("ae2itemCount") {

            @Override
            public MultiLineComponent apply(PlaceholderContext ctx,
                                            List<MultiLineComponent> args) throws PlaceholderException {
                IGrid grid = getGrid(ctx);
                if (args.isEmpty()) return MultiLineComponent.literal(countItems((ItemFilter) null, grid));
                if (args.size() == 1)
                    return MultiLineComponent
                            .literal(countItems(GTStringUtils.componentsToString(args.getFirst()), grid));
                if (GTStringUtils.equals(args.getFirst(), "filter")) {
                    int slot = PlaceholderUtils.toInt(args.get(1));
                    try {
                        PlaceholderUtils.checkRange("slot index", 1, 8, slot);
                        if (ctx.itemStackHandler() == null) throw new NotSupportedException();
                        return MultiLineComponent.literal(countItems(
                                ItemFilter.loadFilter(ctx.itemStackHandler().getStackInSlot(slot - 1)), grid));
                    } catch (NullPointerException e) {
                        throw new MissingItemException("filter", slot);
                    }
                }
                throw new InvalidArgsException();
            }
        });
        PlaceholderHandler.addPlaceholder(new Placeholder("ae2fluidCount") {

            @Override
            public MultiLineComponent apply(PlaceholderContext ctx,
                                            List<MultiLineComponent> args) throws PlaceholderException {
                IGrid grid = getGrid(ctx);
                if (args.isEmpty()) return MultiLineComponent.literal(countFluids(null, grid));
                if (args.size() == 1)
                    return MultiLineComponent
                            .literal(countFluids(GTStringUtils.componentsToString(args.getFirst()), grid));
                throw new WrongNumberOfArgsException(1, args.size());
            }
        });
        PlaceholderHandler.addPlaceholder(new Placeholder("ae2power") {

            @Override
            public MultiLineComponent apply(PlaceholderContext ctx,
                                            List<MultiLineComponent> args) throws PlaceholderException {
                IGrid grid = getGrid(ctx);
                PlaceholderUtils.checkArgs(args, 0);
                return MultiLineComponent.literal(grid.getEnergyService().getStoredPower());
            }
        });
        PlaceholderHandler.addPlaceholder(new Placeholder("ae2maxPower") {

            @Override
            public MultiLineComponent apply(PlaceholderContext ctx,
                                            List<MultiLineComponent> args) throws PlaceholderException {
                IGrid grid = getGrid(ctx);
                PlaceholderUtils.checkArgs(args, 0);
                return MultiLineComponent.literal(grid.getEnergyService().getMaxStoredPower());
            }
        });
        PlaceholderHandler.addPlaceholder(new Placeholder("ae2powerUsage") {

            @Override
            public MultiLineComponent apply(PlaceholderContext ctx,
                                            List<MultiLineComponent> args) throws PlaceholderException {
                IGrid grid = getGrid(ctx);
                PlaceholderUtils.checkArgs(args, 0);
                return MultiLineComponent.literal(grid.getEnergyService().getAvgPowerUsage());
            }
        });
        PlaceholderHandler.addPlaceholder(new Placeholder("ae2spatial") {

            @Override
            public MultiLineComponent apply(PlaceholderContext ctx,
                                            List<MultiLineComponent> args) throws PlaceholderException {
                IGrid grid = getGrid(ctx);
                PlaceholderUtils.checkArgs(args, 1);
                if (GTStringUtils.equals(args.getFirst(), "power")) {
                    return MultiLineComponent.literal(grid.getSpatialService().requiredPower());
                } else if (GTStringUtils.equals(args.getFirst(), "efficiency")) {
                    return MultiLineComponent.literal(grid.getSpatialService().currentEfficiency());
                } else if (GTStringUtils.equals(args.getFirst(), "sizeX")) {
                    return MultiLineComponent.literal(getSpatialSize(grid).x);
                } else if (GTStringUtils.equals(args.getFirst(), "sizeY")) {
                    return MultiLineComponent.literal(getSpatialSize(grid).y);
                } else if (GTStringUtils.equals(args.getFirst(), "sizeZ")) {
                    return MultiLineComponent.literal(getSpatialSize(grid).z);
                } else throw new InvalidArgsException();
            }
        });
        PlaceholderHandler.addPlaceholder(new Placeholder("ae2crafting") {

            @Override
            public MultiLineComponent apply(PlaceholderContext ctx,
                                            List<MultiLineComponent> args) throws PlaceholderException {
                IGrid grid = getGrid(ctx);
                PlaceholderUtils.checkArgs(args, 1, true);
                ICraftingService crafting = grid.getCraftingService();
                if (GTStringUtils.equals(args.getFirst(), "get")) {
                    if (GTStringUtils.equals(args.get(1), "amount"))
                        return MultiLineComponent.literal(crafting.getCpus().size());
                    int index = PlaceholderUtils.toInt(args.get(1));
                    int i = 0;
                    for (ICraftingCPU cpu : crafting.getCpus()) {
                        if (index - 1 == i) {
                            CraftingJobStatus job = cpu.getJobStatus();
                            if (GTStringUtils.equals(args.get(2), "storage"))
                                return MultiLineComponent.literal(cpu.getAvailableStorage());
                            else if (GTStringUtils.equals(args.get(2), "threads"))
                                return MultiLineComponent.literal(cpu.getCoProcessors());
                            else if (GTStringUtils.equals(args.get(2), "name"))
                                return MultiLineComponent
                                        .of(cpu.getName() == null ? Component.literal("Crafting CPU " + i) :
                                                cpu.getName().copy());
                            else if (GTStringUtils.equals(args.get(2), "selectionMode"))
                                return MultiLineComponent.literal(cpu.getSelectionMode().name());
                            else if (job == null) return MultiLineComponent.literal(0);
                            else if (GTStringUtils.equals(args.get(2), "amount"))
                                return MultiLineComponent.literal(job.crafting().amount());
                            else if (GTStringUtils.equals(args.get(2), "item"))
                                return MultiLineComponent.of(job.crafting().what().getDisplayName().copy());
                            else if (GTStringUtils.equals(args.get(2), "progress"))
                                return MultiLineComponent.literal(job.progress());
                            else if (GTStringUtils.equals(args.get(2), "time"))
                                return MultiLineComponent.literal(job.elapsedTimeNanos());
                            else throw new InvalidArgsException();
                        }
                        i++;
                    }
                    throw new OutOfRangeException("cpu number", 1, crafting.getCpus().size(), index);
                } // else if (GTStringUtils.equals(args.get(0), "request")) {} gonna implement that someday :)
                throw new InvalidArgsException();
            }
        });
    }
}
