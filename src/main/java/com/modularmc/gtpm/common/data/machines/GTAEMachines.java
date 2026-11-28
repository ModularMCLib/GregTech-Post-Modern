package com.modularmc.gtpm.common.data.machines;

import com.modularmc.gtpm.GTPM;
import com.modularmc.gtpm.api.data.RotationState;
import com.modularmc.gtpm.api.machine.MachineDefinition;
import com.modularmc.gtpm.api.machine.multiblock.PartAbility;
import com.modularmc.gtpm.common.data.machines.tooltip.MachineTooltips;
import com.modularmc.gtpm.integration.ae2.machine.*;

import com.modularmc.registrate.localization.MinecraftLocale;
import com.modularmc.registrate.localization.annotation.TransVariants;
import com.modularmc.registrate.localization.annotation.Translation;
import com.modularmc.registrate.localization.annotation.Variant;

import net.minecraft.network.chat.Component;

import static com.modularmc.gtpm.api.GTValues.EV;
import static com.modularmc.gtpm.api.GTValues.LuV;
import static com.modularmc.gtpm.common.registry.GTRegistration.REGISTRATE;

@TransVariants(keyPrefix = "gtpm.machine",
               value = {
                       @Variant(id = "fluid_hatch.export.tooltip",
                                source = "Fluid Output for Multiblocks",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "为多方块结构输出流体")),
                       @Variant(id = "fluid_hatch.import.tooltip",
                                source = "Fluid Input for Multiblocks",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "为多方块结构输入流体")),
                       @Variant(id = "me_import_fluid_hatch.configs.tooltip",
                                source = "Keeps 16 fluid types in stock",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "可标记16种流体")),
                       @Variant(id = "me_import_item_hatch.configs.tooltip",
                                source = "Keeps 16 item types in stock",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "可标记16种物品")),
                       @Variant(id = "me.copy_paste.tooltip",
                                source = "Sneak right-click with Data Stick to copy settings, right-click to apply",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "手持闪存潜行右键点击以复制设置，右键点击以应用")),
                       @Variant(id = "me.export.tooltip",
                                source = "Has infinite capacity before connecting to ME network.",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "在连接到ME网络之前，它具有无限容量。")),
                       @Variant(id = "me.fluid_export.tooltip",
                                source = "Stores fluids directly into an ME network.",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "将流体直接存储到ME网络中。")),
                       @Variant(id = "me.fluid_import.tooltip",
                                source = "Fetches fluids from an ME network automatically.",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "自动从ME网络获取流体。")),
                       @Variant(id = "me.item_export.tooltip",
                                source = "Stores items directly into an ME network.",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "将物品直接存储到ME网络中。")),
                       @Variant(id = "me.item_import.tooltip",
                                source = "Fetches items from an ME network automatically.",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "自动从ME网络获取物品。")),
                       @Variant(id = "me.stocking_fluid.tooltip.0",
                                source = "Retrieves fluids directly from the ME network",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "直接从ME网络抽取流体")),
                       @Variant(id = "me.stocking_fluid.tooltip.1",
                                source = "Auto-Pull from ME mode will automatically stock the first 16 fluids in the ME system, updated every 5 seconds.",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "ME自动拉取模式将自动标记ME网络中的前16种流体，每5秒更新一次。")),
                       @Variant(id = "me.stocking_item.tooltip.0",
                                source = "Retrieves items directly from the ME network",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "直接从ME网络抽取物品")),
                       @Variant(id = "me.stocking_item.tooltip.1",
                                source = "Auto-Pull from ME mode will automatically stock the first 16 items in the ME system, updated every 5 seconds.",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "ME自动拉取模式将自动标记ME网络中的前16种物品，每5秒更新一次。")),
               })
@SuppressWarnings("unused")
public class GTAEMachines {

    public final static MachineDefinition ITEM_IMPORT_BUS_ME = REGISTRATE
            .machine("me_input_bus", MEInputBusPartMachine::new)
            .localization(text -> text
                    .source("ME Input Bus")
                    .translation(MinecraftLocale.ZH_CN, "ME输入总线"))
            .tier(EV)
            .rotationState(RotationState.ALL)
            .abilities(PartAbility.IMPORT_ITEMS)
            .colorOverlayTieredHullModel(GTPM.id("block/overlay/appeng/me_input_bus"))
            .tooltips(
                    MachineTooltips.itemInput(),
                    Component.translatable("gtpm.machine.me.item_import.tooltip"),
                    Component.translatable("gtpm.machine.me.copy_paste.tooltip"),
                    Component.translatable("gtpm.part_sharing.enabled"))
            .register();

    public final static MachineDefinition STOCKING_IMPORT_BUS_ME = REGISTRATE
            .machine("me_stocking_input_bus", MEStockingBusPartMachine::new)
            .localization(text -> text
                    .source("ME Stocking Input Bus")
                    .translation(MinecraftLocale.ZH_CN, "ME库存输入总线"))
            .tier(LuV)
            .rotationState(RotationState.ALL)
            .abilities(PartAbility.IMPORT_ITEMS)
            .colorOverlayTieredHullModel(GTPM.id("block/overlay/appeng/me_input_bus"))
            .tooltips(
                    MachineTooltips.itemInput(),
                    Component.translatable("gtpm.machine.me.stocking_item.tooltip.0"),
                    Component.translatable("gtpm.machine.me_import_item_hatch.configs.tooltip"),
                    Component.translatable("gtpm.machine.me.copy_paste.tooltip"),
                    Component.translatable("gtpm.machine.me.stocking_item.tooltip.1"),
                    Component.translatable("gtpm.part_sharing.enabled"))
            .register();

    public final static MachineDefinition ITEM_EXPORT_BUS_ME = REGISTRATE
            .machine("me_output_bus", MEOutputBusPartMachine::new)
            .localization(text -> text
                    .source("ME Output Bus")
                    .translation(MinecraftLocale.ZH_CN, "ME输出总线"))
            .tier(EV)
            .rotationState(RotationState.ALL)
            .abilities(PartAbility.EXPORT_ITEMS)
            .colorOverlayTieredHullModel(GTPM.id("block/overlay/appeng/me_output_bus"))
            .tooltips(
                    MachineTooltips.itemOutput(),
                    Component.translatable("gtpm.machine.me.item_export.tooltip"),
                    Component.translatable("gtpm.machine.me.export.tooltip"),
                    Component.translatable("gtpm.part_sharing.enabled"))
            .register();

    public final static MachineDefinition FLUID_IMPORT_HATCH_ME = REGISTRATE
            .machine("me_input_hatch", MEInputHatchPartMachine::new)
            .localization(text -> text
                    .source("ME Input Hatch")
                    .translation(MinecraftLocale.ZH_CN, "ME输入仓"))
            .tier(EV)
            .rotationState(RotationState.ALL)
            .abilities(PartAbility.IMPORT_FLUIDS)
            .colorOverlayTieredHullModel(GTPM.id("block/overlay/appeng/me_input_hatch"))
            .tooltips(
                    Component.translatable("gtpm.machine.fluid_hatch.import.tooltip"),
                    Component.translatable("gtpm.machine.me.fluid_import.tooltip"),
                    Component.translatable("gtpm.machine.me.copy_paste.tooltip"),
                    Component.translatable("gtpm.part_sharing.enabled"))
            .register();

    public final static MachineDefinition STOCKING_IMPORT_HATCH_ME = REGISTRATE
            .machine("me_stocking_input_hatch", MEStockingHatchPartMachine::new)
            .localization(text -> text
                    .source("ME Stocking Input Hatch")
                    .translation(MinecraftLocale.ZH_CN, "ME库存输入仓"))
            .tier(LuV)
            .rotationState(RotationState.ALL)
            .abilities(PartAbility.IMPORT_FLUIDS)
            .colorOverlayTieredHullModel(GTPM.id("block/overlay/appeng/me_input_hatch"))
            .tooltips(
                    Component.translatable("gtpm.machine.fluid_hatch.import.tooltip"),
                    Component.translatable("gtpm.machine.me.stocking_fluid.tooltip.0"),
                    Component.translatable("gtpm.machine.me_import_fluid_hatch.configs.tooltip"),
                    Component.translatable("gtpm.machine.me.copy_paste.tooltip"),
                    Component.translatable("gtpm.machine.me.stocking_fluid.tooltip.1"),
                    Component.translatable("gtpm.part_sharing.enabled"))
            .register();

    public final static MachineDefinition FLUID_EXPORT_HATCH_ME = REGISTRATE
            .machine("me_output_hatch", MEOutputHatchPartMachine::new)
            .localization(text -> text
                    .source("ME Output Hatch")
                    .translation(MinecraftLocale.ZH_CN, "ME输出仓"))
            .tier(EV)
            .rotationState(RotationState.ALL)
            .abilities(PartAbility.EXPORT_FLUIDS)
            .colorOverlayTieredHullModel(GTPM.id("block/overlay/appeng/me_output_hatch"))
            .tooltips(
                    Component.translatable("gtpm.machine.fluid_hatch.export.tooltip"),
                    Component.translatable("gtpm.machine.me.fluid_export.tooltip"),
                    Component.translatable("gtpm.machine.me.export.tooltip"),
                    Component.translatable("gtpm.part_sharing.enabled"))
            .register();
    public static final MachineDefinition ME_PATTERN_BUFFER = REGISTRATE
            .machine("me_pattern_buffer", MEPatternBufferPartMachine::new)
            .tier(LuV)
            .rotationState(RotationState.ALL)
            .abilities(PartAbility.IMPORT_ITEMS, PartAbility.IMPORT_FLUIDS)
            .rotationState(RotationState.ALL)
            .colorOverlayTieredHullModel(GTPM.id("block/overlay/appeng/me_buffer_hatch"))
            .localization(text -> text
                    .source("ME Pattern Buffer")
                    .translation(MinecraftLocale.ZH_CN, "ME样板总成"))
            .tooltips(
                    Component.translatable("block.gtpm.pattern_buffer.desc.0"),
                    Component.translatable("block.gtpm.pattern_buffer.desc.1"),
                    Component.translatable("block.gtpm.pattern_buffer.desc.2"),
                    Component.translatable("gtpm.part_sharing.enabled"))
            .register();
    public static final MachineDefinition ME_PATTERN_BUFFER_PROXY = REGISTRATE
            .machine("me_pattern_buffer_proxy", MEPatternBufferProxyPartMachine::new)
            .tier(LuV)
            .rotationState(RotationState.ALL)
            .abilities(PartAbility.IMPORT_ITEMS, PartAbility.IMPORT_FLUIDS)
            .rotationState(RotationState.ALL)
            .colorOverlayTieredHullModel(GTPM.id("block/overlay/appeng/me_buffer_hatch_proxy"))
            .localization(text -> text
                    .source("ME Pattern Buffer Proxy")
                    .translation(MinecraftLocale.ZH_CN, "ME样板总成镜像"))
            .tooltips(
                    Component.translatable("block.gtpm.pattern_buffer_proxy.desc.0"),
                    Component.translatable("block.gtpm.pattern_buffer_proxy.desc.1"),
                    Component.translatable("block.gtpm.pattern_buffer_proxy.desc.2"),
                    Component.translatable("gtpm.part_sharing.enabled"))
            .register();

    public static void init() {}

    @TransVariants(keyPrefix = "block.gtpm",
                   value = {
                           @Variant(id = "pattern_buffer_proxy.desc.0",
                                    source = "§fAllows linking many machines to a singular §6ME Pattern Buffer§f.",
                                    translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                                text = "§f可以将单单一个§6ME样板总成§f连接到众多机器。")),
                           @Variant(id = "pattern_buffer_proxy.desc.1",
                                    source = "§fAll connected proxies will share the patterns held within the §6original buffer§f.",
                                    translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                                text = "§f所有连接的总成镜像将共享§6原始总成§f中的样板。")),
                           @Variant(id = "pattern_buffer_proxy.desc.2",
                                    source = "§fLet the factory grow!",
                                    translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "§f让工厂蓬勃发展！")),
                           @Variant(id = "pattern_buffer.desc.0",
                                    source = "§fAllows direct §6AE2 pattern storage §ffor GregTech Multiblocks.",
                                    translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                                text = "§f对于GT多方块结构的集成式§6AE2样板供应器§f。")),
                           @Variant(id = "pattern_buffer.desc.1",
                                    source = "§fAE2 Patterns can utilize anything stored in the §6shared inventory §fwidget.",
                                    translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                                text = "§fAE2样板可以利用§6共享库存§f中的一切。")),
                           @Variant(id = "pattern_buffer.desc.2",
                                    source = "§fLink §6Pattern Buffer Proxies §fwith a §bdatastick §fto link machines together!",
                                    translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                                text = "§f使用§b闪存§f绑定§6ME样板总成镜像§f，以使机器相互连接！")),
                   })
    private static final class BlockTranslations {}
}
