package com.modularmc.gtpm.common.data;

import com.modularmc.gtpm.api.cover.filter.SimpleFluidFilter;
import com.modularmc.gtpm.api.cover.filter.SimpleItemFilter;
import com.modularmc.gtpm.api.cover.filter.SmartItemFilter;
import com.modularmc.gtpm.api.data.chemical.material.Material;
import com.modularmc.gtpm.api.item.datacomponents.*;
import com.modularmc.gtpm.api.placeholder.PlaceholderRenderData;
import com.modularmc.gtpm.api.registry.GTRegistries;
import com.modularmc.gtpm.api.sync_system.SyncFieldData;
import com.modularmc.gtpm.common.data.datacomponents.AEInputConfigCopyData;
import com.modularmc.gtpm.common.data.datacomponents.CapeData;
import com.modularmc.gtpm.common.data.datacomponents.FluidProspectionCache;
import com.modularmc.gtpm.common.data.datacomponents.MedicalConditionTrackerData;
import com.modularmc.gtpm.common.data.datacomponents.OreProspectionCache;
import com.modularmc.gtpm.common.data.datacomponents.PipeNetData;
import com.modularmc.gtpm.common.item.LampBlockItem;
import com.modularmc.gtpm.common.item.behavior.ItemMagnetBehavior;
import com.modularmc.gtpm.common.item.datacomponents.*;
import com.modularmc.gtpm.common.item.tool.behavior.ToolModeSwitchBehavior;
import com.modularmc.gtpm.utils.GlobalPosWithRot;
import com.modularmc.gtpm.utils.ResearchManager;

import net.minecraft.core.BlockPos;
import net.minecraft.core.UUIDUtil;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.Unit;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.neoforged.neoforge.fluids.SimpleFluidContent;
import net.neoforged.neoforge.registries.DeferredHolder;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;

import java.util.UUID;

import static com.modularmc.gtpm.common.registry.GTRegistration.REGISTRATE;

public class GTDataComponents {

    private static final StreamCodec<ByteBuf, Unit> UNIT_STREAM_CODEC = StreamCodec.unit(Unit.INSTANCE);
    // Tool-related
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<GTTool>> GT_TOOL = REGISTRATE
            .<GTTool>dataComponent("gt_tool",
                    builder -> builder.persistent(GTTool.CODEC).networkSynchronized(GTTool.STREAM_CODEC))
            .register();
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<ToolBehaviors>> TOOL_BEHAVIORS = REGISTRATE
            .<ToolBehaviors>dataComponent("tool_behaviors", builder -> builder.persistent(ToolBehaviors.CODEC)
                    .networkSynchronized(ToolBehaviors.STREAM_CODEC))
            .register();
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<AoESymmetrical>> AOE = REGISTRATE
            .<AoESymmetrical>dataComponent("aoe", builder -> builder.persistent(AoESymmetrical.CODEC)
                    .networkSynchronized(AoESymmetrical.STREAM_CODEC))
            .register();
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Unit>> DISALLOW_CONTAINER_ITEM = REGISTRATE
            .<Unit>dataComponent("disallow_container_item", builder -> builder.persistent(Unit.CODEC)
                    .networkSynchronized(UNIT_STREAM_CODEC))
            .register();
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Unit>> RELOCATE_MINED_BLOCKS = REGISTRATE
            .<Unit>dataComponent("relocate_mined_blocks", builder -> builder.persistent(Unit.CODEC)
                    .networkSynchronized(UNIT_STREAM_CODEC))
            .register();
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Unit>> RELOCATE_MOB_DROPS = REGISTRATE
            .<Unit>dataComponent("relocate_mob_drops", builder -> builder.persistent(Unit.CODEC)
                    .networkSynchronized(UNIT_STREAM_CODEC))
            .register();
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Boolean>> ACTIVE = REGISTRATE
            .<Boolean>dataComponent("active", builder -> builder.persistent(Codec.BOOL)
                    .networkSynchronized(ByteBufCodecs.BOOL))
            .register();
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<ToolModeSwitchBehavior.ModeType>> TOOL_MODE = REGISTRATE
            .<ToolModeSwitchBehavior.ModeType>dataComponent("tool_mode", builder -> builder
                    .persistent(ToolModeSwitchBehavior.ModeType.CODEC)
                    .networkSynchronized(ToolModeSwitchBehavior.ModeType.STREAM_CODEC))
            .register();
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<ItemEnchantments>> INNATE_ENCHANTMENTS = REGISTRATE
            .<ItemEnchantments>dataComponent("innate_enchantments", builder -> builder
                    .persistent(ItemEnchantments.CODEC)
                    .networkSynchronized(ItemEnchantments.STREAM_CODEC)
                    .cacheEncoding())
            .register();

    // Material-related
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Material>> ITEM_MATERIAL = REGISTRATE
            .<Material>dataComponent("item_material", builder -> builder
                    .persistent(GTRegistries.MATERIALS.byNameCodec())
                    .networkSynchronized(ByteBufCodecs.registry(GTRegistries.MATERIAL_REGISTRY)))
            .register();

    // Armor-related
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<GTArmor>> ARMOR_DATA = REGISTRATE
            .<GTArmor>dataComponent("armor",
                    builder -> builder.persistent(GTArmor.CODEC).networkSynchronized(GTArmor.STREAM_CODEC))
            .register();
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Unit>> FLY_MODE = REGISTRATE
            .<Unit>dataComponent("fly_mode", builder -> builder.persistent(Unit.CODEC)
                    .networkSynchronized(UNIT_STREAM_CODEC))
            .register();

    // component item-related
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<ResearchManager.ResearchItem>> RESEARCH_ITEM = REGISTRATE
            .<ResearchManager.ResearchItem>dataComponent("research_item", builder -> builder.persistent(ResearchManager.ResearchItem.CODEC)
                    .networkSynchronized(ResearchManager.ResearchItem.STREAM_CODEC))
            .register();
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<DataItem>> DATA_ITEM = REGISTRATE
            .<DataItem>dataComponent("data_item", builder -> builder.persistent(DataItem.CODEC)
                    .networkSynchronized(DataItem.STREAM_CODEC))
            .register();
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<ItemMagnetBehavior.MagnetComponent>> MAGNET = REGISTRATE
            .<ItemMagnetBehavior.MagnetComponent>dataComponent("magnet",
                    builder -> builder.persistent(ItemMagnetBehavior.MagnetComponent.CODEC)
                            .networkSynchronized(ItemMagnetBehavior.MagnetComponent.STREAM_CODEC))
            .register();
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Byte>> SCANNER_MODE = REGISTRATE
            .<Byte>dataComponent("scanner_mode",
                    builder -> builder.persistent(Codec.BYTE).networkSynchronized(ByteBufCodecs.BYTE))
            .register();

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<SimpleFluidFilter>> SIMPLE_FLUID_FILTER = REGISTRATE
            .<SimpleFluidFilter>dataComponent("simple_fluid_filter", builder -> builder.persistent(SimpleFluidFilter.CODEC))
            .register();
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<SimpleItemFilter>> SIMPLE_ITEM_FILTER = REGISTRATE
            .<SimpleItemFilter>dataComponent("simple_item_filter", builder -> builder.persistent(SimpleItemFilter.CODEC))
            .register();
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<String>> TAG_FILTER_EXPRESSION = REGISTRATE
            .<String>dataComponent("tag_filter_expression",
                    builder -> builder.persistent(Codec.STRING).networkSynchronized(ByteBufCodecs.STRING_UTF8))
            .register();
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<SmartItemFilter.SmartFilteringMode>> SMART_ITEM_FILTER = REGISTRATE
            .<SmartItemFilter.SmartFilteringMode>dataComponent("smart_item_filter",
                    builder -> builder.persistent(SmartItemFilter.SmartFilteringMode.CODEC))
            .register();
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> CIRCUIT_CONFIG = REGISTRATE
            .<Integer>dataComponent("circuit_config", builder -> builder.persistent(Codec.INT))
            .register();

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<SimpleFluidContent>> FLUID_CONTENT = REGISTRATE
            .<SimpleFluidContent>dataComponent("fluid_content", builder -> builder.persistent(SimpleFluidContent.CODEC)
                    .networkSynchronized(SimpleFluidContent.STREAM_CODEC))
            .register();
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<SimpleEnergyContent>> ENERGY_CONTENT = REGISTRATE
            .<SimpleEnergyContent>dataComponent("energy_content", builder -> builder.persistent(SimpleEnergyContent.CODEC)
                    .networkSynchronized(SimpleEnergyContent.STREAM_CODEC))
            .register();

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<BindingData>> BINDING_DATA = REGISTRATE
            .<BindingData>dataComponent("binding_data", builder -> builder.persistent(BindingData.CODEC)
                    .networkSynchronized(BindingData.STREAM_CODEC))
            .register();
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<BlockPos>> DATA_COPY_POS = REGISTRATE
            .<BlockPos>dataComponent("data_copy_pos", builder -> builder.persistent(BlockPos.CODEC)
                    .networkSynchronized(BlockPos.STREAM_CODEC))
            .register();
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<MachineConfigCopyData>> DATA_COPY_TAG = REGISTRATE
            .<MachineConfigCopyData>dataComponent("data_copy_tag", builder -> builder.persistent(MachineConfigCopyData.CODEC)
                    .networkSynchronized(MachineConfigCopyData.STREAM_CODEC))
            .register();
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<AEInputConfigCopyData>> AE_INPUT_CONFIG_COPY_DATA = REGISTRATE
            .<AEInputConfigCopyData>dataComponent("ae_input_config_copy_data", builder -> builder
                    .persistent(AEInputConfigCopyData.CODEC)
                    .networkSynchronized(AEInputConfigCopyData.STREAM_CODEC))
            .register();

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<TextLineList>> FORMAT_STRING_LIST = REGISTRATE
            .<TextLineList>dataComponent("format_string_list", builder -> builder.persistent(TextLineList.CODEC))
            .register();
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<FormatStringList>> COMPUTER_MONITOR_DATA = REGISTRATE
            .<FormatStringList>dataComponent("computer_monitor_cover_data", builder -> builder.persistent(FormatStringList.CODEC)
                    .networkSynchronized(FormatStringList.STREAM_CODEC))
            .register();
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<ComputerMonitorConfig>> COMPUTER_MONITOR_CONFIG = REGISTRATE
            .<ComputerMonitorConfig>dataComponent("computer_monitor_cover_config",
                    builder -> builder.persistent(ComputerMonitorConfig.CODEC)
                            .networkSynchronized(ComputerMonitorConfig.STREAM_CODEC))
            .register();
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> COMPUTER_MONITOR_P = REGISTRATE
            .<Integer>dataComponent("computer_monitor_cover_p", builder -> builder.persistent(Codec.INT)
                    .networkSynchronized(ByteBufCodecs.VAR_INT))
            .register();
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<TextLineList>> TEXT_LINE_LIST = REGISTRATE
            .<TextLineList>dataComponent("text_line_list", builder -> builder.persistent(TextLineList.CODEC))
            .register();
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<String>> IMAGE_MODULE_URL = REGISTRATE
            .<String>dataComponent("image_module_url", builder -> builder.persistent(Codec.STRING)
                    .networkSynchronized(ByteBufCodecs.STRING_UTF8))
            .register();
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<GlobalPosWithRot>> MONITOR_TARGET = REGISTRATE
            .<GlobalPosWithRot>dataComponent("monitor_target", builder -> builder.persistent(GlobalPosWithRot.CODEC)
                    .networkSynchronized(GlobalPosWithRot.STREAM_CODEC))
            .register();
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<UUID>> ENDER_REDSTONE_LINK_TRANSMITTER_UUID = REGISTRATE
            .<UUID>dataComponent("ender_redstone_link_transmitter_uuid",
                    builder -> builder.persistent(UUIDUtil.CODEC)
                            .networkSynchronized(UUIDUtil.STREAM_CODEC))
            .register();
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<UUID>> PLACEHOLDER_UUID = REGISTRATE
            .<UUID>dataComponent("placeholder_uuid",
                    builder -> builder.persistent(UUIDUtil.CODEC)
                            .networkSynchronized(UUIDUtil.STREAM_CODEC))
            .register();
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<PlaceholderRenderData.Rect>> PLACEHOLDER_RECT_RENDER_DATA = REGISTRATE
            .<PlaceholderRenderData.Rect>dataComponent("placeholder_rect_render_data",
                    builder -> builder.persistent(PlaceholderRenderData.Rect.CODEC))
            .register();
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<PlaceholderRenderData.Quad>> PLACEHOLDER_QUAD_RENDER_DATA = REGISTRATE
            .<PlaceholderRenderData.Quad>dataComponent("placeholder_quad_render_data",
                    builder -> builder.persistent(PlaceholderRenderData.Quad.CODEC))
            .register();
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<ItemStack>> PLACEHOLDER_ITEM_STACK = REGISTRATE
            .<ItemStack>dataComponent("placeholder_item_stack", builder -> builder.persistent(ItemStack.OPTIONAL_CODEC)
                    .networkSynchronized(ItemStack.STREAM_CODEC))
            .register();

    // machine info
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<LargeItemContent>> LARGE_ITEM_CONTENT = REGISTRATE
            .<LargeItemContent>dataComponent("large_item_content", builder -> builder
                    .persistent(LargeItemContent.CODEC)
                    .networkSynchronized(LargeItemContent.STREAM_CODEC))
            .register();
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<LargeFluidContent>> LARGE_FLUID_CONTENT = REGISTRATE
            .<LargeFluidContent>dataComponent("large_fluid_content", builder -> builder
                    .persistent(LargeFluidContent.CODEC)
                    .networkSynchronized(LargeFluidContent.STREAM_CODEC))
            .register();
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<CreativeMachineInfo>> CREATIVE_MACHINE_INFO = REGISTRATE
            .<CreativeMachineInfo>dataComponent("creative_machine_info", builder -> builder
                    .persistent(CreativeMachineInfo.CODEC)
                    .networkSynchronized(CreativeMachineInfo.STREAM_CODEC))
            .register();
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Unit>> TAPED = REGISTRATE
            .<Unit>dataComponent("taped",
                    builder -> builder.persistent(Unit.CODEC).networkSynchronized(UNIT_STREAM_CODEC))
            .register();

    // misc
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<FacadeWrapper>> FACADE = REGISTRATE
            .<FacadeWrapper>dataComponent("facade", builder -> builder.persistent(FacadeWrapper.CODEC)
                    .networkSynchronized(FacadeWrapper.STREAM_CODEC))
            .register();
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<LampBlockItem.LampData>> LAMP_DATA = REGISTRATE
            .<LampBlockItem.LampData>dataComponent("lamp", builder -> builder.persistent(LampBlockItem.LampData.CODEC)
                    .networkSynchronized(LampBlockItem.LampData.STREAM_CODEC))
            .register();
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Boolean>> LIGHTER_OPEN = REGISTRATE
            .<Boolean>dataComponent("lighter_open",
                    builder -> builder.persistent(Codec.BOOL).networkSynchronized(ByteBufCodecs.BOOL))
            .register();

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<SyncFieldData>> ITEM_DATA = REGISTRATE
            .<SyncFieldData>dataComponent("item_data", builder -> builder.persistent(SyncFieldData.CODEC)
                    .networkSynchronized(SyncFieldData.STREAM_CODEC))
            .register();
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<FluidProspectionCache>> FLUID_PROSPECTION_CACHE = REGISTRATE
            .<FluidProspectionCache>dataComponent("fluid_prospection_cache", builder -> builder
                    .persistent(FluidProspectionCache.CODEC)
                    .networkSynchronized(FluidProspectionCache.STREAM_CODEC))
            .register();
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<OreProspectionCache>> ORE_PROSPECTION_CACHE = REGISTRATE
            .<OreProspectionCache>dataComponent("ore_prospection_cache", builder -> builder
                    .persistent(OreProspectionCache.CODEC)
                    .networkSynchronized(OreProspectionCache.STREAM_CODEC))
            .register();
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<MedicalConditionTrackerData>> MEDICAL_CONDITION_TRACKER = REGISTRATE
            .<MedicalConditionTrackerData>dataComponent("medical_condition_tracker", builder -> builder
                    .persistent(MedicalConditionTrackerData.CODEC)
                    .networkSynchronized(MedicalConditionTrackerData.STREAM_CODEC))
            .register();
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<PipeNetData.Nodes>> PIPE_NET_NODES = REGISTRATE
            .<PipeNetData.Nodes>dataComponent("pipe_net_nodes", builder -> builder
                    .persistent(PipeNetData.Nodes.CODEC)
                    .networkSynchronized(PipeNetData.Nodes.STREAM_CODEC))
            .register();
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<PipeNetData.Wire>> PIPE_NET_WIRE = REGISTRATE
            .<PipeNetData.Wire>dataComponent("pipe_net_wire", builder -> builder
                    .persistent(PipeNetData.Wire.CODEC)
                    .networkSynchronized(PipeNetData.Wire.STREAM_CODEC))
            .register();
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<PipeNetData.FluidPipe>> PIPE_NET_FLUID_PIPE = REGISTRATE
            .<PipeNetData.FluidPipe>dataComponent("pipe_net_fluid_pipe", builder -> builder
                    .persistent(PipeNetData.FluidPipe.CODEC)
                    .networkSynchronized(PipeNetData.FluidPipe.STREAM_CODEC))
            .register();
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<PipeNetData.ItemPipe>> PIPE_NET_ITEM_PIPE = REGISTRATE
            .<PipeNetData.ItemPipe>dataComponent("pipe_net_item_pipe", builder -> builder
                    .persistent(PipeNetData.ItemPipe.CODEC)
                    .networkSynchronized(PipeNetData.ItemPipe.STREAM_CODEC))
            .register();
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<PipeNetData.DuctPipe>> PIPE_NET_DUCT_PIPE = REGISTRATE
            .<PipeNetData.DuctPipe>dataComponent("pipe_net_duct_pipe", builder -> builder
                    .persistent(PipeNetData.DuctPipe.CODEC)
                    .networkSynchronized(PipeNetData.DuctPipe.STREAM_CODEC))
            .register();
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<CapeData.Registry>> CAPE_REGISTRY = REGISTRATE
            .<CapeData.Registry>dataComponent("cape_registry", builder -> builder
                    .persistent(CapeData.Registry.CODEC)
                    .networkSynchronized(CapeData.Registry.STREAM_CODEC))
            .register();

    public static void init() {}
}
