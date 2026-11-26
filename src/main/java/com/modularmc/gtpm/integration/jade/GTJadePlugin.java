package com.modularmc.gtpm.integration.jade;

import com.modularmc.gtpm.GTPM;
import com.modularmc.gtpm.api.block.MetaMachineBlock;
import com.modularmc.gtpm.api.machine.MetaMachine;
import com.modularmc.gtpm.common.blockentity.FluidPipeBlockEntity;
import com.modularmc.gtpm.common.data.GTMaterialItems;
import com.modularmc.gtpm.integration.jade.provider.*;

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
}
