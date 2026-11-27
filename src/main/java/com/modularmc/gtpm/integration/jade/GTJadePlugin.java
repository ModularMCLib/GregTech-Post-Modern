package com.modularmc.gtpm.integration.jade;

import com.modularmc.gtpm.GTPM;
import com.modularmc.gtpm.api.block.MetaMachineBlock;
import com.modularmc.gtpm.api.machine.MetaMachine;
import com.modularmc.gtpm.common.blockentity.FluidPipeBlockEntity;
import com.modularmc.gtpm.common.data.GTMaterialItems;
import com.modularmc.gtpm.integration.jade.provider.*;

import com.modularmc.registrate.localization.MinecraftLocale;
import com.modularmc.registrate.localization.annotation.TransVariants;
import com.modularmc.registrate.localization.annotation.Translation;
import com.modularmc.registrate.localization.annotation.Variant;
import com.modularmc.registrate.util.entry.ItemProviderEntry;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;

import snownee.jade.addon.harvest.HarvestToolProvider;
import snownee.jade.addon.harvest.SimpleToolHandler;
import snownee.jade.api.*;

import java.util.List;
import java.util.Objects;

@WailaPlugin
@TransVariants(keyPrefix = "config",
               value = {
                       @Variant(id = "jade.plugin_gtpm.auto_output_info",
                                source = "[GTPM] Auto Output Info",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "[GTCEu] 自动输出信息")),
                       @Variant(id = "jade.plugin_gtpm.battery_info",
                                source = "[GTPM] Battery info",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "[GTCEu] 电池信息")),
                       @Variant(id = "jade.plugin_gtpm.cable_info",
                                source = "[GTPM] Cable Info",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "[GTCEu] 线缆信息")),
                       @Variant(id = "jade.plugin_gtpm.controllable_provider",
                                source = "[GTPM] Controllable",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "[GTCEu] 是否停工")),
                       @Variant(id = "jade.plugin_gtpm.data_bank",
                                source = "[GTPM] Data Bank Info",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "[GTCEu] 数据库信息")),
                       @Variant(id = "jade.plugin_gtpm.electric_container_provider",
                                source = "[GTPM] Electric Container",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "[GTCEu] 电力槽")),
                       @Variant(id = "jade.plugin_gtpm.energy_converter_provider",
                                source = "[GTPM] Energy Converter Mode",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "[GTCEu] 能量转换器模式")),
                       @Variant(id = "jade.plugin_gtpm.exhaust_vent_info",
                                source = "[GTPM] Exhaust Vent Info",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "[GTCEu] 排气口信息")),
                       @Variant(id = "jade.plugin_gtpm.hazard_cleaner_provider",
                                source = "[GTPM] Hazard Cleaner",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "[GTCEu] 污染清理")),
                       @Variant(id = "jade.plugin_gtpm.ldp_endpoint",
                                source = "[GTPM] Long Distance Pipeline Endpoint Info",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "[GTCEu] 长距离管道接口信息")),
                       @Variant(id = "jade.plugin_gtpm.machine_mode",
                                source = "[GTPM] Machine Mode",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "[GTCEu] 机器模式")),
                       @Variant(id = "jade.plugin_gtpm.maintenance_info",
                                source = "[GTPM] Maintenance Info",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "[GTCEu] 维护信息")),
                       @Variant(id = "jade.plugin_gtpm.me_pattern_buffer",
                                source = "[GTPM] Pattern Buffer Info",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "[GTCEu] 样板总成信息")),
                       @Variant(id = "jade.plugin_gtpm.me_pattern_buffer_proxy",
                                source = "[GTPM] Pattern Buffer Proxy Info",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "[GTCEu] 样板总成镜像信息")),
                       @Variant(id = "jade.plugin_gtpm.multiblock_structure",
                                source = "[GTPM] MultiBlock Structure",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "[GTCEu] 多方块结构")),
                       @Variant(id = "jade.plugin_gtpm.parallel_info",
                                source = "[GTPM] Parallel Info",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "[GTCEu] 并行信息")),
                       @Variant(id = "jade.plugin_gtpm.primitive_pump",
                                source = "[GTPM] Primitive Pump Info",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "[GTCEu] 原始水泵信息")),
                       @Variant(id = "jade.plugin_gtpm.recipe_logic_provider",
                                source = "[GTPM] Recipe Logic",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "[GTCEu] 配方信息")),
                       @Variant(id = "jade.plugin_gtpm.recipe_output_info",
                                source = "[GTPM] Recipe Output Info",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "[GTCEu] 配方输出信息")),
                       @Variant(id = "jade.plugin_gtpm.stained_color",
                                source = "[GTPM] Stained Block Info",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "[GTCEu] 染色方块信息")),
                       @Variant(id = "jade.plugin_gtpm.steam_boiler_info",
                                source = "[GTPM] Steam Boiler Info",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "[GTCEu] 蒸汽锅炉信息")),
                       @Variant(id = "jade.plugin_gtpm.transformer",
                                source = "[GTPM] Transformer Info",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "[GTCEu] 变压器信息")),
                       @Variant(id = "jade.plugin_gtpm.workable_provider",
                                source = "[GTPM] Workable",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "[GTCEu] 工作信息")),
               })
public class GTJadePlugin implements IWailaPlugin {

    @Override
    public void register(IWailaCommonRegistration registration) {
        register(registration, new ElectricContainerBlockProvider(),
                new WorkLogicMachineProvider(),
                new ControllableBlockProvider(),
                new RecipeLogicProvider(),
                new ParallelProvider(),
                new RecipeOutputProvider(),
                new MultiblockStructureProvider(),
                new MaintenanceBlockProvider(),
                new ExhaustVentBlockProvider(),
                new SteamBoilerBlockProvider(),
                new AutoOutputBlockProvider(),
                new CableBlockProvider(),
                new MachineModeProvider(),
                new StainedColorProvider(),
                new HazardCleanerBlockProvider(),
                new TransformerBlockProvider(),
                new PrimitivePumpBlockProvider(),
                new DataBankBlockProvider(),
                new EnergyConverterModeProvider(),
                new BatteryStorageInfoProvider(),
                new LDPEndpointProvider());

        if (GTPM.MODS.AE2.isLoaded()) {
            register(registration, new MEPatternBufferProvider(), new MEPatternBufferProxyProvider());
        }

        registration.registerItemStorage(GTItemStorageProvider.INSTANCE, MetaMachine.class);
        registration.registerFluidStorage(GTFluidStorageProvider.INSTANCE, MetaMachine.class);
        registration.registerFluidStorage(FluidPipeStorageProvider.INSTANCE, FluidPipeBlockEntity.class);
    }

    @Override
    public void registerClient(IWailaClientRegistration registration) {
        register(registration,
                new ElectricContainerBlockProvider(),
                new WorkLogicMachineProvider(),
                new ControllableBlockProvider(),
                new RecipeLogicProvider(),
                new ParallelProvider(),
                new RecipeOutputProvider(),
                new MultiblockStructureProvider(),
                new MaintenanceBlockProvider(),
                new ExhaustVentBlockProvider(),
                new SteamBoilerBlockProvider(),
                new AutoOutputBlockProvider(),
                new MachineModeProvider(),
                new StainedColorProvider(),
                new HazardCleanerBlockProvider(),
                new TransformerBlockProvider(),
                new PrimitivePumpBlockProvider(),
                new DataBankBlockProvider(),
                new LDPEndpointProvider(),
                new EnergyConverterModeProvider(),
                new BatteryStorageInfoProvider(),
                new CableBlockProvider());

        if (GTPM.MODS.AE2.isLoaded()) {
            register(registration, new MEPatternBufferProvider(), new MEPatternBufferProxyProvider());
        }

        registration.registerItemStorageClient(GTItemStorageProvider.INSTANCE);
        registration.registerFluidStorageClient(GTFluidStorageProvider.INSTANCE);
        registration.registerFluidStorageClient(FluidPipeStorageProvider.INSTANCE);
    }

    @SafeVarargs
    public static void register(IWailaCommonRegistration reg, IServerDataProvider<BlockAccessor>... providers) {
        for (var provider : providers) {
            Class<? extends BlockEntity> clazz = BlockEntity.class;

            if (provider instanceof MachineInfoProvider<?, ?> machineInfoProvider)
                clazz = machineInfoProvider.machineType;
            if (provider instanceof MachineTraitProvider<?, ?>) clazz = MetaMachine.class;

            reg.registerBlockDataProvider(provider, clazz);
        }
    }

    public static void register(IWailaClientRegistration reg, IBlockComponentProvider... providers) {
        for (var provider : providers) {
            Class<? extends Block> clazz = Block.class;
            if (provider instanceof MachineInfoProvider<?, ?> || provider instanceof MachineTraitProvider<?, ?>) {
                clazz = MetaMachineBlock.class;
            }
            reg.registerBlockComponent(provider, clazz);
        }
    }

    static {
        GTMaterialItems.TOOL_ITEMS.columnMap().forEach((type, map) -> {
            if (type.toolDefinition.getTool().rules().isEmpty() || map.isEmpty()) return;

            List<Item> tools = map
                    .values()
                    .stream()
                    .filter(Objects::nonNull)
                    .filter(ItemProviderEntry::isBound)
                    .map(ItemProviderEntry::asItem)
                    .toList();
            if (tools.isEmpty()) return;
            HarvestToolProvider.registerHandler(SimpleToolHandler.create(GTPM.id(type.name), tools, true));
        });
    }

    @TransVariants(keyPrefix = "gtpm.top",
                   value = {
                           @Variant(id = "working_disabled", source = "Working Disabled",
                                    translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "已暂停工作")),
                           @Variant(id = "fuel_min_consume", source = "Needs",
                                    translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "需要")),
                           @Variant(id = "fuel_none", source = "No fuel",
                                    translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "无燃料")),
                           @Variant(id = "obstructed_structure", source = "Structure Obstructed",
                                    translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "结构受阻")),
                           @Variant(id = "filter.label", source = "Filter:",
                                    translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "过滤：")),
                           @Variant(id = "link_cover.color", source = "Color:",
                                    translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "颜色：")),
                           @Variant(id = "mode.export", source = "Exporting",
                                    translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "输出")),
                           @Variant(id = "mode.import", source = "Importing",
                                    translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "输入")),
                           @Variant(id = "unit.items", source = "Items",
                                    translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "物品")),
                           @Variant(id = "unit.fluid_milibuckets", source = "L",
                                    translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "L")),
                           @Variant(id = "unit.fluid_buckets", source = "kL",
                                    translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "kL")),
                           @Variant(id = "energy_stored", source = " / %d EU",
                                    translations = @Translation(locale = MinecraftLocale.ZH_CN, text = " / %d EU")),
                           @Variant(id = "progress_computation", source = " / %s CWU",
                                    translations = @Translation(locale = MinecraftLocale.ZH_CN, text = " / %s CWU")),
                           @Variant(id = "progress_sec", source = " / %s s",
                                    translations = @Translation(locale = MinecraftLocale.ZH_CN, text = " / %s s")),
                           @Variant(id = "progress_tick", source = " / %s t",
                                    translations = @Translation(locale = MinecraftLocale.ZH_CN, text = " / %s t")),
                   })
    private static final class LegacyLabels {}
}
