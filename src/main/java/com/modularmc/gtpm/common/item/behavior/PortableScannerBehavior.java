package com.modularmc.gtpm.common.item.behavior;

import com.modularmc.gtpm.api.GTValues;
import com.modularmc.gtpm.api.block.property.GTBlockStateProperties;
import com.modularmc.gtpm.api.blockentity.PipeBlockEntity;
import com.modularmc.gtpm.api.capability.*;
import com.modularmc.gtpm.api.data.worldgen.bedrockfluid.BedrockFluidVeinSavedData;
import com.modularmc.gtpm.api.gui.misc.ProspectorMode;
import com.modularmc.gtpm.api.item.component.IAddInformation;
import com.modularmc.gtpm.api.item.component.IInteractionItem;
import com.modularmc.gtpm.api.machine.MetaMachine;
import com.modularmc.gtpm.api.machine.feature.IDataInfoProvider;
import com.modularmc.gtpm.api.machine.feature.IMufflableMachine;
import com.modularmc.gtpm.api.machine.feature.IWorkLogicMachine;
import com.modularmc.gtpm.api.machine.trait.RecipeLogic;
import com.modularmc.gtpm.api.recipe.GTRecipe;
import com.modularmc.gtpm.api.recipe.RecipeHelper;
import com.modularmc.gtpm.api.sync_system.SyncFieldData;
import com.modularmc.gtpm.api.sync_system.managed.ManagedSyncBlockEntity;
import com.modularmc.gtpm.common.blockentity.FluidPipeBlockEntity;
import com.modularmc.gtpm.common.capability.EnvironmentalHazardSavedData;
import com.modularmc.gtpm.common.capability.LocalizedHazardSavedData;
import com.modularmc.gtpm.common.data.GTDataComponents;
import com.modularmc.gtpm.common.data.GTSoundEntries;
import com.modularmc.gtpm.common.network.packets.prospecting.SPacketProspectBedrockFluid;
import com.modularmc.gtpm.utils.FormattingUtil;
import com.modularmc.gtpm.utils.GTUtil;

import com.modularmc.registrate.localization.MinecraftLocale;
import com.modularmc.registrate.localization.annotation.Trans;
import com.modularmc.registrate.localization.annotation.TransVariants;
import com.modularmc.registrate.localization.annotation.Translation;
import com.modularmc.registrate.localization.annotation.Variant;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.network.PacketDistributor;

import com.mojang.serialization.JsonOps;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@TransVariants(keyPrefix = "behavior.portable_scanner",
               value = {
                       @Variant(id = "block_name",
                                source = "Name: %s MetaData: %s",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "名称：%s；元数据：%s")),
                       @Variant(id = "debug_cpu_load",
                                source = "Average CPU load of ~%sns over %s ticks with worst time of %sns.",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "%2$s刻内平均CPU负载约%1$sns，最差情况为%3$sns。")),
                       @Variant(id = "debug_cpu_load_seconds",
                                source = "This is %s seconds.",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "负载秒数为%s秒。")),
                       @Variant(id = "debug_lag_count",
                                source = "Caused %s Lag Spike Warnings (anything taking longer than %sms) on the Server.",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "已在服务器中造成%s次延迟尖峰警告（任何大于%sms的情况）。")),
                       @Variant(id = "debug_machine",
                                source = "Meta-ID: %s",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "元ID：%s")),
                       @Variant(id = "debug_machine_invalid",
                                source = " invalid!",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = " 无效！")),
                       @Variant(id = "debug_machine_invalid_null",
                                source = " invalid! MetaTileEntity = null!",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "无效！MetaTileEntity == null！")),
                       @Variant(id = "debug_machine_valid",
                                source = " valid",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "无效！")),
                       @Variant(id = "machine_power_loss",
                                source = "Shut down due to power loss.",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "因断电而停止工作。")),
                       @Variant(id = "multiblock_energy_input",
                                source = "Max Energy Income: %s EU/t Tier: %s",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "最大输入：%s EU/t，电压等级 %s")),
                       @Variant(id = "multiblock_energy_output",
                                source = "Max Energy Output: %s EU/t Tier: %s",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "最大输出：%s EU/t，电压等级 %s")),
                       @Variant(id = "multiblock_maintenance",
                                source = "Problems: %s",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "故障：%s")),
                       @Variant(id = "multiblock_parallel",
                                source = "Multi Processing: %s",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "并行处理：%s")),
                       @Variant(id = "tank",
                                source = "Tank %s: %s mB / %s mB %s",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "流体储量（槽位%s）：%s mB / %s mB %s")),
                       @Variant(id = "tanks_empty",
                                source = "All Tanks Empty",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "未存储流体")),
                       @Variant(id = "workable_progress",
                                source = "Progress: %s s / %s s",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "处理进度：%s s / %s s")),
                       @Variant(id = "workable_stored_energy",
                                source = "Stored Energy: %s EU / %s EU",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "能量缓存：%s EU / %s EU")),
                       @Variant(id = "bedrock_fluid.amount",
                                source = "Fluid In Deposit: %s %s - %s%%",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "流体储量：%s %s - %s%%")),
                       @Variant(id = "bedrock_fluid.amount_unknown",
                                source = "Fluid In Deposit: %s%%",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "流体储量：%s%%")),
                       @Variant(id = "bedrock_fluid.nothing",
                                source = "Fluid In Deposit: §6Nothing§r",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "流体储量：§6无§r")),
                       @Variant(id = "block_hardness",
                                source = "Hardness: %s Blast Resistance: %s",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "硬度：%s；爆炸抗性：%s")),
                       @Variant(id = "divider",
                                source = "=========================",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "=========================")),
                       @Variant(id = "energy_container_in",
                                source = "Max IN: %s (%s) EU at %s A",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "输入上限：%s（%s）EU，%s A")),
                       @Variant(id = "energy_container_out",
                                source = "Max OUT: %s (%s) EU at %s A",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "输出上限：%s（%s）EU，%s A")),
                       @Variant(id = "energy_container_storage",
                                source = "Energy: %s EU / %s EU",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "能量：%s EU / %s EU")),
                       @Variant(id = "environmental_hazard",
                                source = "Environmental Hazard In Chunk: %s§r - %s ppm",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "区块内的环境污染：%s§r - %s ppm")),
                       @Variant(id = "environmental_hazard.nothing",
                                source = "Environmental Hazard In Chunk: §6Nothing§r",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "区块内的环境污染：§6无§r")),
                       @Variant(id = "local_hazard",
                                source = "Local Hazard In Area: %s§r - %s ppm",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "区域内的其他污染：%s§r - %s ppm")),
                       @Variant(id = "local_hazard.nothing",
                                source = "Local Hazard In Area: §6Nothing§r",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "区域内的其他污染：§6无§r")),
                       @Variant(id = "machine_disabled",
                                source = "Disabled.",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "已停止。")),
                       @Variant(id = "machine_front_facing",
                                source = "Front Facing: %s",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "控制器面向：%s")),
                       @Variant(id = "machine_progress",
                                source = "Progress/Load: %s / %s",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "处理进度/总计：%s / %s")),
                       @Variant(id = "machine_upwards_facing",
                                source = "Upwards Facing: %s",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "控制器正面朝向：%s")),
                       @Variant(id = "mode.caption",
                                source = "Display mode: %s",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "显示模式：%s")),
                       @Variant(id = "mode.show_all_info",
                                source = "Show all info (excluding internal info)",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "显示所有信息")),
                       @Variant(id = "mode.show_block_info",
                                source = "Show block info",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "显示方块信息")),
                       @Variant(id = "mode.show_electrical_info",
                                source = "Show electrical info",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "显示电器信息")),
                       @Variant(id = "mode.show_environmental_info",
                                source = "Show environmental info",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "显示环境信息")),
                       @Variant(id = "mode.show_internal_info",
                                source = "Show internal debugging info",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "显示内部调试信息")),
                       @Variant(id = "mode.show_machine_info",
                                source = "Show machine info",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "显示机器信息")),
                       @Variant(id = "mode.show_recipe_info",
                                source = "Show recipe info",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "显示配方信息")),
                       @Variant(id = "muffled",
                                source = "Muffled.",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "已静音。")),
                       @Variant(id = "position",
                                source = "----- X: %s Y: %s Z: %s D: %s -----",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "----- X：%s；Y：%s；Z：%s；D：%s -----")),
                       @Variant(id = "state",
                                source = "%s: %s",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "%s：%s")),
                       @Variant(id = "workable_consumption",
                                source = "Probably Uses: %s EU/t at %s A",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "预计耗能：%s EU/t，%s A")),
                       @Variant(id = "workable_production",
                                source = "Probably Produces: %s EU/t at %s A",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "预计产能：%s EU/t，%s A")),
               })
@Trans(key = "behavior.prospector.not_enough_energy",
       source = "Not Enough Energy!",
       translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "能量不足！"))
public class PortableScannerBehavior implements IInteractionItem, IAddInformation {

    private int debugLevel = 0;

    @Getter
    public enum DisplayMode {

        SHOW_ALL("behavior.portable_scanner.mode.show_all_info"),
        SHOW_BLOCK_INFO("behavior.portable_scanner.mode.show_block_info"),
        SHOW_MACHINE_INFO("behavior.portable_scanner.mode.show_machine_info"),
        SHOW_ELECTRICAL_INFO("behavior.portable_scanner.mode.show_electrical_info"),
        SHOW_RECIPE_INFO("behavior.portable_scanner.mode.show_recipe_info"),
        SHOW_ENVIRONMENTAL_INFO("behavior.portable_scanner.mode.show_environmental_info"),
        SHOW_INTERNAL_JAVA_INFO("behavior.portable_scanner.mode.show_internal_info");

        private final String langKey;

        DisplayMode(String langKey) {
            this.langKey = langKey;
        }
    }

    public PortableScannerBehavior(int debugLevel) {
        this.debugLevel = debugLevel;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        var player = context.getPlayer();
        var level = context.getLevel();
        var pos = context.getClickedPos();
        var stack = context.getItemInHand();

        if (!level.isClientSide() && !level.isEmptyBlock(pos) && player != null) {

            List<Component> list = new ArrayList<>();
            int energyCost = addScannerInfo(player, level, pos, getMode(stack), list);

            if (player.isCreative()) {
                energyCost = 0;
            }

            if (energyCost > 0 && !drainEnergy(stack, energyCost, true)) {
                player.sendSystemMessage(Component.translatable("behavior.prospector.not_enough_energy"));
                return InteractionResult.CONSUME;
            }

            drainEnergy(stack, energyCost, false);
            for (var line : list) {
                player.sendSystemMessage(line);
            }

            GTSoundEntries.PORTABLE_SCANNER.play(level, null, player.position(), 1.0f, 1.0f);

            return InteractionResult.CONSUME;
        }

        return InteractionResult.CONSUME;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(ItemStack item, Level level, Player player,
                                                  InteractionHand usedHand) {
        if (player.isShiftKeyDown()) {
            if (!level.isClientSide) {
                setNextMode(item);
                var mode = getMode(item);
                player.sendSystemMessage(Component.translatable("behavior.portable_scanner.mode.caption",
                        Component.translatable(mode.getLangKey())));
            }
            return InteractionResultHolder.success(item);
        }
        return IInteractionItem.super.use(item, level, player, usedHand);
    }

    protected boolean drainEnergy(@NotNull ItemStack stack, int amount, boolean simulate) {
        IElectricItem electricItem = GTCapabilityHelper.getElectricItem(stack);
        if (electricItem == null) return false;
        return electricItem.discharge(amount, Integer.MAX_VALUE, true, false, simulate) >= amount;
    }

    protected void setNextMode(ItemStack stack) {
        stack.update(GTDataComponents.SCANNER_MODE, (byte) 0,
                mode -> (byte) ((mode + 1) % DisplayMode.values().length));
    }

    @NotNull
    protected DisplayMode getMode(ItemStack stack) {
        if (stack == ItemStack.EMPTY) {
            return DisplayMode.SHOW_ALL;
        }
        return DisplayMode.values()[stack.getOrDefault(GTDataComponents.SCANNER_MODE, (byte) 0) %
                DisplayMode.values().length];
    }

    public int addScannerInfo(Player player, Level level, BlockPos pos, DisplayMode mode, List<Component> list) {
        BlockEntity tileEntity = level.getBlockEntity(pos);
        int energyCost = 0;

        BlockState state = level.getBlockState(pos);
        Block block = state.getBlock();

        // General information
        if (mode == DisplayMode.SHOW_ALL || mode == DisplayMode.SHOW_BLOCK_INFO) {

            // Coordinates of the block
            list.add(Component.translatable("behavior.portable_scanner.position",
                    Component.translatable(FormattingUtil.formatNumbers(pos.getX()))
                            .withStyle(ChatFormatting.AQUA),
                    Component.translatable(FormattingUtil.formatNumbers(pos.getY()))
                            .withStyle(ChatFormatting.AQUA),
                    Component.translatable(FormattingUtil.formatNumbers(pos.getZ()))
                            .withStyle(ChatFormatting.AQUA),
                    Component.translatable(level.dimension().location().toString())
                            .withStyle(ChatFormatting.AQUA)));

            // Hardness and blast resistance
            list.add(Component.translatable("behavior.portable_scanner.block_hardness",
                    Component.translatable(
                            FormattingUtil.formatNumbers(block.defaultDestroyTime()))
                            .withStyle(ChatFormatting.YELLOW),
                    Component.translatable(FormattingUtil.formatNumbers(block.getExplosionResistance()))
                            .withStyle(ChatFormatting.YELLOW)));

            // Possible block states
            if (debugLevel > 2) {
                state.getProperties().forEach((property) -> {
                    list.add(Component.translatable("behavior.portable_scanner.state",
                            Component.translatable(property.getName()),
                            Component.translatable(state.getValue(property).toString())
                                    .withStyle(ChatFormatting.AQUA)));
                });
            }
        }

        if (tileEntity instanceof MetaMachine machine) {

            list.add(Component.translatable(state.getBlock().getDescriptionId()).withStyle(ChatFormatting.BLUE));

            // General machine information
            if (mode == DisplayMode.SHOW_ALL || mode == DisplayMode.SHOW_MACHINE_INFO) {
                if (machine.getOwner() != null) {
                    machine.getOwner().displayInfo(list);
                }

                if (machine.getDefinition().isAllowExtendedFacing()) {
                    list.add(Component.translatable("behavior.portable_scanner.divider"));

                    list.add(Component.translatable("behavior.portable_scanner.machine_front_facing",
                            machine.getFrontFacing().getSerializedName()));
                    list.add(Component.translatable("behavior.portable_scanner.machine_upwards_facing",
                            machine.getBlockState().getValue(GTBlockStateProperties.UPWARDS_FACING)
                                    .getSerializedName()));
                }

                // Fluid tanks
                IFluidHandler fluidHandler = tileEntity.getLevel().getCapability(Capabilities.FluidHandler.BLOCK,
                        tileEntity.getBlockPos(), null);
                if (fluidHandler != null) {
                    list.add(Component.translatable("behavior.portable_scanner.divider"));
                    boolean allTanksEmpty = true;

                    for (int i = 0; i < fluidHandler.getTanks(); i++) {
                        FluidStack fluidStack = fluidHandler.getFluidInTank(i);

                        if (fluidStack.isEmpty()) {
                            continue;
                        }

                        energyCost += 500;
                        allTanksEmpty = false;
                        list.add(Component.translatable("behavior.portable_scanner.tank", i,
                                Component.translatable(FormattingUtil.formatNumbers(fluidStack.getAmount()))
                                        .withStyle(ChatFormatting.GREEN),
                                Component.translatable(FormattingUtil.formatNumbers(fluidHandler.getTankCapacity(i)))
                                        .withStyle(ChatFormatting.YELLOW),
                                ((MutableComponent) fluidStack.getHoverName()).withStyle(ChatFormatting.GOLD)));
                    }

                    if (allTanksEmpty) {
                        list.add(Component.translatable("behavior.portable_scanner.tanks_empty"));
                    }
                }

                // Sound muffling
                if (machine instanceof IMufflableMachine mufflableMachine) {
                    energyCost += 500;
                    if (mufflableMachine.isMuffled()) {
                        list.add(Component.translatable("behavior.portable_scanner.divider"));
                        list.add(Component.translatable("behavior.portable_scanner.muffled")
                                .withStyle(ChatFormatting.GREEN));
                    }
                }
            }

            // Energy related information
            if (mode == DisplayMode.SHOW_ALL || mode == DisplayMode.SHOW_ELECTRICAL_INFO) {

                // Energy container
                IEnergyContainer energyContainer = tileEntity.getLevel()
                        .getCapability(GTCapability.CAPABILITY_ENERGY_CONTAINER, tileEntity.getBlockPos(), null);
                if (energyContainer != null) {
                    if (energyContainer.getInputVoltage() > 0) {
                        list.add(Component.translatable("behavior.portable_scanner.divider"));
                        list.add(Component.translatable("behavior.portable_scanner.energy_container_in",
                                Component.translatable(FormattingUtil.formatNumbers(energyContainer.getInputVoltage()))
                                        .withStyle(ChatFormatting.RED),
                                Component.translatable(
                                        GTValues.VN[GTUtil.getTierByVoltage(energyContainer.getInputVoltage())])
                                        .withStyle(ChatFormatting.RED),
                                Component.translatable(FormattingUtil.formatNumbers(energyContainer.getInputAmperage()))
                                        .withStyle(ChatFormatting.RED)));
                    }
                    if (energyContainer.getOutputVoltage() > 0) {
                        list.add(Component.translatable("behavior.portable_scanner.divider"));
                        list.add(Component.translatable("behavior.portable_scanner.energy_container_out",
                                Component.translatable(FormattingUtil.formatNumbers(energyContainer.getOutputVoltage()))
                                        .withStyle(ChatFormatting.RED),
                                Component.translatable(
                                        GTValues.VN[GTUtil.getTierByVoltage(energyContainer.getOutputVoltage())])
                                        .withStyle(ChatFormatting.RED),
                                Component.translatable(
                                        FormattingUtil.formatNumbers(energyContainer.getOutputAmperage()))
                                        .withStyle(ChatFormatting.RED)));
                    }
                    list.add(Component.translatable("behavior.portable_scanner.energy_container_storage",
                            Component.translatable(FormattingUtil.formatNumbers(energyContainer.getEnergyStored()))
                                    .withStyle(ChatFormatting.GREEN),
                            Component.translatable(FormattingUtil.formatNumbers(energyContainer.getEnergyCapacity()))
                                    .withStyle(ChatFormatting.YELLOW)));
                }
            }

            // Recipe related information
            if (mode == DisplayMode.SHOW_ALL || mode == DisplayMode.SHOW_RECIPE_INFO) {

                // Workable progress info
                if (machine instanceof IWorkable workableMachine) {
                    energyCost += 400;
                    if (!workableMachine.isWorkingEnabled()) {
                        list.add(Component.translatable("behavior.portable_scanner.divider"));
                        list.add(Component.translatable("behavior.portable_scanner.machine_disabled")
                                .withStyle(ChatFormatting.RED));
                    }
                    if (workableMachine.getMaxProgress() > 0) {
                        list.add(Component.translatable("behavior.portable_scanner.divider"));
                        list.add(Component.translatable("behavior.portable_scanner.machine_progress",
                                Component.translatable(FormattingUtil.formatNumbers(workableMachine.getProgress()))
                                        .withStyle(ChatFormatting.GREEN),
                                Component.translatable(FormattingUtil.formatNumbers(workableMachine.getMaxProgress()))
                                        .withStyle(ChatFormatting.YELLOW)));
                    }
                }

                // Recipe logic for EU production/consumption
                RecipeLogic recipeLogic = machine.getTrait(RecipeLogic.TYPE);
                boolean waitingDisplayed = false;
                if (machine instanceof IWorkLogicMachine workLogicMachine) {
                    var workLogic = workLogicMachine.getWorkLogic();
                    if (workLogic.isWaiting()) {
                        list.add(Component.translatable("behavior.portable_scanner.divider"));
                        list.add(Component.translatable("gtpm.multiblock.waiting"));
                        list.addAll(workLogic.getFancyTooltip());
                        waitingDisplayed = true;
                    }
                }
                if (recipeLogic != null) {
                    GTRecipe recipe = recipeLogic.getLastRecipe();
                    if (recipe != null && !waitingDisplayed) {
                        list.add(Component.translatable("behavior.portable_scanner.divider"));
                        var EUt = RecipeHelper.getRealEUtWithIO(recipe);
                        long displayVoltage = Math.max(1, recipeLogic.getRLMachine().getDisplayRecipeVoltage());

                        list.add(Component.translatable(
                                EUt > 0 ? "behavior.portable_scanner.workable_consumption" :
                                        "behavior.portable_scanner.workable_production",
                                // TODO is this supposed to show voltage or total EU/t?
                                Component.translatable(FormattingUtil.formatNumbers(Math.abs(EUt)))
                                        .withStyle(ChatFormatting.RED),
                                Component.translatable(
                                        FormattingUtil.formatNumbers(Math.abs(EUt) / displayVoltage))
                                        .withStyle(ChatFormatting.RED)));
                    }
                }
            }

            // machine-specific info
            IDataInfoProvider provider = null;
            if (tileEntity instanceof IDataInfoProvider) provider = (IDataInfoProvider) tileEntity;

            if (provider != null) {
                list.add(Component.translatable("behavior.portable_scanner.divider"));
                list.addAll(provider.getDataInfo(mode));
            }

        } else if (tileEntity instanceof PipeBlockEntity<?, ?> pipe) {

            // Pipes need special name handling
            list.add(pipe.getPipeBlock().getName().withStyle(ChatFormatting.BLUE));

            // Pipe-specific info
            if (tileEntity instanceof IDataInfoProvider dataInfoProvider) {
                list.add(Component.translatable("behavior.portable_scanner.divider"));
                list.addAll(dataInfoProvider.getDataInfo(mode));
            }

            if (tileEntity instanceof FluidPipeBlockEntity) {
                // Getting fluid info always costs 500
                energyCost += 500;
            }
        } else if (tileEntity instanceof IDataInfoProvider dataInfoProvider) {
            list.add(Component.translatable("behavior.portable_scanner.divider"));
            list.addAll(dataInfoProvider.getDataInfo(mode));
        } else {
            list.add(Component.translatable(state.getBlock().getDescriptionId()).withStyle(ChatFormatting.BLUE));
        }

        // Environmental information
        if (mode == DisplayMode.SHOW_ALL || mode == DisplayMode.SHOW_ENVIRONMENTAL_INFO) {

            // Bedrock fluids
            if (level instanceof ServerLevel serverLevel) {
                list.add(Component.translatable("behavior.portable_scanner.divider"));
                var veinData = BedrockFluidVeinSavedData.getOrCreate(serverLevel);
                int chunkX = pos.getX() >> 4;
                int chunkZ = pos.getZ() >> 4;
                Fluid fluid = veinData.getFluidInChunk(chunkX, chunkZ);

                if (fluid != null) {
                    FluidStack stack = new FluidStack(fluid,
                            veinData.getOperationsRemaining(chunkX, chunkZ));
                    double fluidPercent = stack.getAmount() * 100.0 / BedrockFluidVeinSavedData.MAXIMUM_VEIN_OPERATIONS;

                    var fluidInfo = ProspectorMode.FluidInfo
                            .fromVeinWorldEntry(veinData.getFluidVeinWorldEntry(chunkX, chunkZ));
                    var packet = new SPacketProspectBedrockFluid(level.dimension(), pos, fluidInfo);
                    PacketDistributor.sendToPlayer((ServerPlayer) player, packet);

                    if (player.isCreative()) {
                        list.add(Component.translatable("behavior.portable_scanner.bedrock_fluid.amount",
                                ((MutableComponent) stack.getHoverName())
                                        .withStyle(ChatFormatting.GOLD),
                                Component.translatable(String.valueOf(
                                        veinData.getFluidYield(chunkX, chunkZ)))
                                        .withStyle(ChatFormatting.GOLD),
                                Component.translatable(String.valueOf(fluidPercent))
                                        .withStyle(ChatFormatting.YELLOW)));
                    } else {
                        list.add(Component.translatable("behavior.portable_scanner.bedrock_fluid.amount_unknown",
                                Component.translatable(String.valueOf(fluidPercent))
                                        .withStyle(ChatFormatting.YELLOW)));
                    }
                } else {
                    list.add(Component.translatable("behavior.portable_scanner.bedrock_fluid.nothing"));
                }

                // Pollution
                var environmental = EnvironmentalHazardSavedData.getOrCreate(serverLevel);
                var environmentHazardZone = environmental.getZoneByContainedPos(pos);
                if (environmentHazardZone != null) {
                    list.add(Component.translatable("behavior.portable_scanner.environmental_hazard",
                            Component.translatable("gtpm.medical_condition." + environmentHazardZone.condition().name),
                            Component.literal(FormattingUtil.formatNumbers(environmentHazardZone.strength()))));
                } else {
                    list.add(Component.translatable("behavior.portable_scanner.environmental_hazard.nothing"));
                }

                var local = LocalizedHazardSavedData.getOrCreate(serverLevel);
                var localHazardZone = local.getZoneByContainedPos(pos);
                if (localHazardZone != null) {
                    list.add(Component.translatable("behavior.portable_scanner.local_hazard",
                            Component.translatable("gtpm.medical_condition." + localHazardZone.condition().name),
                            Component.literal(FormattingUtil.formatNumbers(localHazardZone.strength()))));
                } else {
                    list.add(Component.translatable("behavior.portable_scanner.local_hazard.nothing"));
                }
            }
        }

        // Add optional debug info
        if (tileEntity instanceof IDataInfoProvider dataInfoProvider) {
            List<Component> debugInfo = dataInfoProvider.getDebugInfo(player, debugLevel, mode);
            if (debugInfo != null) {
                list.addAll(debugInfo);
            }
        }

        if (mode == DisplayMode.SHOW_INTERNAL_JAVA_INFO &&
                tileEntity instanceof ManagedSyncBlockEntity syncBlockEntity) {
            MetaMachine machine = (syncBlockEntity instanceof MetaMachine m) ? m : null;
            PipeBlockEntity<?, ?> pipe = (syncBlockEntity instanceof PipeBlockEntity<?, ?> p) ? p : null;

            list.add(Component.literal(syncBlockEntity.toString()));
            if (pipe != null) {
                var net = pipe.getPipeNet();
                list.add(Component.literal(net == null ? "null" : net.toString()));
            }
            list.add(Component.translatable("behavior.portable_scanner.divider"));

            list.add(Component.literal("Covers"));
            ICoverable coverable = machine != null ? machine.getCoverContainer() :
                    (pipe != null ? pipe.getCoverContainer() : null);
            if (coverable != null) {
                for (var dir : GTUtil.DIRECTIONS) {
                    var cover = coverable.getCoverAtSide(dir);
                    list.add(Component.literal(dir.getName() + ": " + (cover != null ? cover.toString() : "null")));
                }
            }

            list.add(Component.translatable("behavior.portable_scanner.divider"));
            list.add(Component.literal("Save data"));
            list.add(Component.literal(SyncFieldData.CODEC
                    .encodeStart(level.registryAccess().createSerializationContext(JsonOps.INSTANCE),
                            syncBlockEntity.getSyncDataHolder()
                                    .serializeToFieldData(level.registryAccess(), false, false))
                    .getOrThrow()
                    .toString()));

            list.add(Component.translatable("behavior.portable_scanner.divider"));
            list.add(Component.literal("Update packet"));
            list.add(Component.literal(SyncFieldData.CODEC
                    .encodeStart(level.registryAccess().createSerializationContext(JsonOps.INSTANCE),
                            syncBlockEntity.getSyncDataHolder()
                                    .serializeToFieldData(level.registryAccess(), true, true))
                    .getOrThrow()
                    .toString()));
        }

        return energyCost;
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents,
                                TooltipFlag isAdvanced) {
        tooltipComponents.add(Component.translatable("metaitem.behavior.mode_switch.tooltip"));
        tooltipComponents.add(Component.translatable("behavior.portable_scanner.mode.caption",
                Component.translatable(getMode(stack).getLangKey()).withStyle(ChatFormatting.AQUA)));
    }

    @Trans(key = "metaitem.behavior.mode_switch.tooltip",
           source = "Use while sneaking to switch mode",
           translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "潜行右击以切换模式"))
    private static final class MetaitemTranslations {}
}
