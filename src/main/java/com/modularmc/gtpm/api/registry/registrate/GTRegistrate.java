package com.modularmc.gtpm.api.registry.registrate;

import com.modularmc.gtpm.GTPM;
import com.modularmc.gtpm.api.block.MetaMachineBlock;
import com.modularmc.gtpm.api.blockentity.BlockEntityCreationInfo;
import com.modularmc.gtpm.api.data.worldgen.GTOreDefinition;
import com.modularmc.gtpm.api.data.worldgen.bedrockfluid.BedrockFluidDefinition;
import com.modularmc.gtpm.api.item.MetaMachineItem;
import com.modularmc.gtpm.api.machine.MachineDefinition;
import com.modularmc.gtpm.api.machine.MetaMachine;
import com.modularmc.gtpm.api.machine.MultiblockMachineDefinition;
import com.modularmc.gtpm.api.multiblock.autobuild.MaterialRequestProvider;
import com.modularmc.gtpm.api.multiblock.autobuild.MaterialSources.Registration;
import com.modularmc.gtpm.api.multiblock.structurepredicate.PatternPredicate;
import com.modularmc.gtpm.api.multiblock.structurepredicate.PredicateRegistry;
import com.modularmc.gtpm.api.multiblock.structurepredicate.PredicateType;
import com.modularmc.gtpm.api.multiblock.structurepredicate.StructurePredicate;
import com.modularmc.gtpm.api.multiblock.structurepredicate.StructurePredicateType;
import com.modularmc.gtpm.api.registry.GTRegistries;
import com.modularmc.gtpm.api.registry.registrate.provider.GTLanguageProvider;

import com.modularmc.registrate.AbstractRegistrate;
import com.modularmc.registrate.builders.base.Builder;
import com.modularmc.registrate.builders.base.NoConfigBuilder;
import com.modularmc.registrate.localization.LocalizationDeclaration;
import com.modularmc.registrate.localization.MinecraftLocale;
import com.modularmc.registrate.providers.RegistrateLangProvider;
import com.modularmc.registrate.providers.core.GeneratorType;
import com.modularmc.registrate.providers.core.ProviderType;
import com.modularmc.registrate.util.entry.ItemEntry;
import com.modularmc.registrate.util.entry.RegistryEntry;
import com.modularmc.registrate.util.nullness.NonNullFunction;
import com.modularmc.registrate.util.nullness.NonNullSupplier;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.registries.DeferredHolder;

import com.mojang.serialization.MapCodec;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.Nullable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class GTRegistrate extends AbstractRegistrate<GTRegistrate> {

    private static final Map<String, GTRegistrate> EXISTING_REGISTRATES = new ConcurrentHashMap<>();

    private final AtomicBoolean registered = new AtomicBoolean(false);
    private final Map<String, LocalizationDeclaration> defaultLocalizations = new Object2ObjectLinkedOpenHashMap<>();
    private @Nullable PackOutput languageOutput;

    protected GTRegistrate(String modId) {
        super(modId);
        sourceLocale(MinecraftLocale.EN_US);
    }

    public ResourceLocation makeResourceLocation(String path) {
        return ResourceLocation.fromNamespaceAndPath(this.getModid(), path);
    }

    /**
     * Get or create a new {@link GTRegistrate} and register event listeners for registration and data generation.
     * A new {@code GTRegistrate} instance is only made if one doesn't already exist in the cache.
     *
     * @param modId The mod ID for which objects will be registered
     * @return The {@link GTRegistrate} instance
     */
    public static GTRegistrate create(String modId) {
        return create(modId, true);
    }

    /**
     * Get or create a new {@link GTRegistrate} and conditionally register event listeners.
     * A new {@code GTRegistrate} instance is only made if one doesn't already exist in the cache.
     * <br>
     * Note that if you do not allow event listeners to be registered automatically, you <strong>must</strong>
     * call {@link #registerEventListeners(IEventBus)} yourself with your {@link IEventBus mod event bus}.
     *
     * @param modId          The mod ID for which objects will be registered
     * @param registerEvents Whether to register required event listeners.
     * @return The {@link GTRegistrate} instance
     */
    public static GTRegistrate create(String modId, boolean registerEvents) {
        GTRegistrate registrate = EXISTING_REGISTRATES.computeIfAbsent(modId, GTRegistrate::new);
        if (registerEvents) {
            ModList.get().getModContainerById(modId).map(ModContainer::getEventBus)
                    .ifPresentOrElse(registrate::registerEventListeners,
                            () -> GTPM.LOGGER.error("No mod event bus for GTRegistrate namespace {}", modId));
        }
        return registrate;
    }

    /**
     * Gets a material namespace's Registrate. CommonProxy binds it to the owning mod bus after material registration.
     *
     * @param modId The mod ID for which objects will be registered
     * @return The {@link GTRegistrate} instance
     */
    @ApiStatus.Internal
    public static GTRegistrate createIgnoringListenerErrors(String modId) {
        return create(modId, false);
    }

    @Override
    public GTRegistrate registerEventListeners(IEventBus bus) {
        if (registered.getAndSet(true)) {
            // early exit if event listeners are already registered
            return this;
        }
        return super.registerEventListeners(bus);
    }

    @Override
    protected void onData(GatherDataEvent event) {
        languageOutput = event.getGenerator().getPackOutput();
        super.onData(event);
    }

    /**
     * Records the first default for a material or one of its tags during registration in a DataGen run.
     * Language callbacks can replace the default before RegistrateLib resolves the declarations.
     */
    @ApiStatus.Internal
    public void addDefaultLocalization(String key, Supplier<LocalizationDeclaration> declaration) {
        defaultLocalizations.computeIfAbsent(key, ignored -> declaration.get());
    }

    @Override
    public <T> void genData(GeneratorType<? extends T> type, T generator) {
        if (type == ProviderType.LANG && generator instanceof RegistrateLangProvider language &&
                languageOutput != null) {
            // Preserve addon replacements before Registrate-Lib checks for conflicting declarations.
            var staged = new GTLanguageProvider(this, languageOutput);
            defaultLocalizations
                    .forEach((key, declaration) -> GTLanguageProvider.addLocalization(staged, this, key, declaration));
            super.genData(ProviderType.LANG, staged);
            staged.submitTo(language);
            return;
        }
        super.genData(type, generator);
    }

    protected <P> NoConfigBuilder<CreativeModeTab, CreativeModeTab, P> createCreativeModeTab(P parent, String name,
                                                                                             Consumer<CreativeModeTab.Builder> config) {
        return this.generic(parent, name, Registries.CREATIVE_MODE_TAB, () -> {
            var builder = CreativeModeTab.builder()
                    .icon(() -> getAll(Registries.ITEM).stream().findFirst().map(ItemEntry::cast)
                            .map(ItemEntry::asStack).orElse(new ItemStack(Items.AIR)));
            config.accept(builder);
            return builder.build();
        });
    }

    public <DEFINITION extends MachineDefinition> MachineBuilder<DEFINITION, ?> machine(String name,
                                                                                        Function<ResourceLocation, DEFINITION> definitionFactory,
                                                                                        BiFunction<BlockBehaviour.Properties, DEFINITION, MetaMachineBlock> blockFactory,
                                                                                        BiFunction<MetaMachineBlock, Item.Properties, MetaMachineItem> itemFactory,
                                                                                        Function<BlockEntityCreationInfo, MetaMachine> blockEntityFactory) {
        return new MachineBuilder<>(this, name, definitionFactory,
                blockFactory, itemFactory, blockEntityFactory);
    }

    public MachineBuilder<MachineDefinition, ?> machine(String name,
                                                        Function<BlockEntityCreationInfo, MetaMachine> blockEntityFactory) {
        return new MachineBuilder<>(this, name, MachineDefinition::new,
                MetaMachineBlock::new, MetaMachineItem::new, blockEntityFactory);
    }

    public MultiblockMachineBuilder<MultiblockMachineDefinition, ?> multiblock(String name,
                                                                               BiFunction<BlockBehaviour.Properties, MultiblockMachineDefinition, MetaMachineBlock> blockFactory,
                                                                               BiFunction<MetaMachineBlock, Item.Properties, MetaMachineItem> itemFactory,
                                                                               Function<BlockEntityCreationInfo, MetaMachine> blockEntityFactory) {
        return new MultiblockMachineBuilder<>(this, name,
                blockFactory, itemFactory, blockEntityFactory);
    }

    public MultiblockMachineBuilder<MultiblockMachineDefinition, ?> multiblock(String name,
                                                                               Function<BlockEntityCreationInfo, MetaMachine> blockEntityFactory) {
        return new MultiblockMachineBuilder<>(this, name, MetaMachineBlock::new, MetaMachineItem::new,
                blockEntityFactory);
    }

    /**
     * Registers a structure predicate and its runtime compiler under this mod's namespace.
     * The returned type can be used by definitions before the registry event publishes it.
     */
    public <T extends StructurePredicate> StructurePredicateType<T> structurePredicate(String name, MapCodec<T> codec) {
        ResourceLocation id = makeResourceLocation(name);
        StructurePredicateType<T> type = new StructurePredicateType<>(id, codec);
        PredicateRegistry.register(id, new PredicateType<>(codec, PatternPredicate::new));
        generic(name, GTRegistries.STRUCTURE_PREDICATE_REGISTRY, () -> type).register();
        return type;
    }

    /**
     * Creates a material-source registration under this mod's namespace. Lower priorities supply materials first
     * and receive returned materials last.
     */
    public NoConfigBuilder<Registration, Registration, GTRegistrate> materialSource(String name, int priority,
                                                                                    MaterialRequestProvider provider) {
        return generic(name, GTRegistries.MATERIAL_SOURCE_REGISTRY,
                () -> new Registration(priority, provider));
    }

    /** Declares an ore vein key in this mod's namespace for use during datapack bootstrap. */
    public DatapackKeyBuilder<GTOreDefinition> oreVein(String name) {
        return new DatapackKeyBuilder<>(this,
                ResourceKey.create(GTRegistries.ORE_VEIN_REGISTRY, makeResourceLocation(name)));
    }

    /** Declares a bedrock fluid key in this mod's namespace for use during datapack bootstrap. */
    public DatapackKeyBuilder<BedrockFluidDefinition> bedrockFluid(String name) {
        return new DatapackKeyBuilder<>(this,
                ResourceKey.create(GTRegistries.BEDROCK_FLUID_REGISTRY, makeResourceLocation(name)));
    }

    public SoundEntryBuilder sound(String name) {
        return sound(makeResourceLocation(name));
    }

    public SoundEntryBuilder sound(ResourceLocation name) {
        return new SoundEntryBuilder(this, name);
    }

    // Blocks
    @Override
    public <T extends Block> GTBlockBuilder<T, GTRegistrate> block(NonNullFunction<BlockBehaviour.Properties, T> factory) {
        return block(this, factory);
    }

    @Override
    public <T extends Block> GTBlockBuilder<T, GTRegistrate> block(String name,
                                                                   NonNullFunction<BlockBehaviour.Properties, T> factory) {
        return block(this, name, factory);
    }

    @Override
    public <T extends Block, P> GTBlockBuilder<T, P> block(P parent,
                                                           NonNullFunction<BlockBehaviour.Properties, T> factory) {
        return block(parent, currentName(), factory);
    }

    @Override
    public <T extends Block, P> GTBlockBuilder<T, P> block(P parent, String name,
                                                           NonNullFunction<BlockBehaviour.Properties, T> factory) {
        return (GTBlockBuilder<T, P>) entry(name,
                callback -> GTBlockBuilder.createGTBlock(this, parent, name, callback, factory));
    }

    private @Nullable RegistryEntry<CreativeModeTab, ? extends CreativeModeTab> currentTab;
    private static final Map<RegistryEntry<?, ?>, RegistryEntry<CreativeModeTab, ? extends CreativeModeTab>> TAB_LOOKUP = new IdentityHashMap<>();

    public @Nullable RegistryEntry<CreativeModeTab, ? extends CreativeModeTab> creativeModeTab() {
        return this.currentTab;
    }

    public void creativeModeTab(Supplier<RegistryEntry<CreativeModeTab, ? extends CreativeModeTab>> currentTab) {
        this.currentTab = currentTab.get();
    }

    public void resetCreativeModeTab() {
        this.currentTab = null;
    }

    public void creativeModeTab(RegistryEntry<CreativeModeTab, ? extends CreativeModeTab> currentTab) {
        this.currentTab = currentTab;
    }

    public boolean isInCreativeTab(RegistryEntry<?, ?> entry,
                                   RegistryEntry<CreativeModeTab, ? extends CreativeModeTab> tab) {
        return TAB_LOOKUP.get(entry) == tab;
    }

    public void setCreativeTab(RegistryEntry<?, ?> entry,
                               @Nullable RegistryEntry<CreativeModeTab, ? extends CreativeModeTab> tab) {
        TAB_LOOKUP.put(entry, tab);
    }

    @Override
    protected <R, T extends R> RegistryEntry<R, T> accept(String name, ResourceKey<? extends Registry<R>> type,
                                                          Builder<R, T, ?, ?> builder,
                                                          NonNullSupplier<? extends T> creator,
                                                          NonNullFunction<DeferredHolder<R, T>, ? extends RegistryEntry<R, T>> entryFactory) {
        RegistryEntry<R, T> entry = super.accept(name, type, builder, creator, entryFactory);

        if (this.currentTab != null) {
            TAB_LOOKUP.put(entry, this.currentTab);
        }

        return entry;
    }

    public <P> NoConfigBuilder<CreativeModeTab, CreativeModeTab, P> defaultCreativeTab(P parent, String name,
                                                                                       Consumer<CreativeModeTab.Builder> config) {
        return createCreativeModeTab(parent, name, config);
    }
}
