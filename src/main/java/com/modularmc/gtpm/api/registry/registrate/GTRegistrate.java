package com.modularmc.gtpm.api.registry.registrate;

import com.modularmc.gtpm.GTPM;
import com.modularmc.gtpm.api.block.MetaMachineBlock;
import com.modularmc.gtpm.api.blockentity.BlockEntityCreationInfo;
import com.modularmc.gtpm.api.capability.recipe.RecipeCapability;
import com.modularmc.gtpm.api.cover.CoverDefinition;
import com.modularmc.gtpm.api.data.DimensionMarker;
import com.modularmc.gtpm.api.data.chemical.Element;
import com.modularmc.gtpm.api.data.worldgen.GTOreDefinition;
import com.modularmc.gtpm.api.data.worldgen.bedrockfluid.BedrockFluidDefinition;
import com.modularmc.gtpm.api.item.MetaMachineItem;
import com.modularmc.gtpm.api.item.tool.behavior.ToolBehaviorType;
import com.modularmc.gtpm.api.machine.MachineDefinition;
import com.modularmc.gtpm.api.machine.MetaMachine;
import com.modularmc.gtpm.api.machine.MultiblockMachineDefinition;
import com.modularmc.gtpm.api.multiblock.autobuild.MaterialRequestProvider;
import com.modularmc.gtpm.api.multiblock.autobuild.MaterialSources.Registration;
import com.modularmc.gtpm.api.multiblock.structurepredicate.PredicateType;
import com.modularmc.gtpm.api.multiblock.structurepredicate.StructurePredicateType;
import com.modularmc.gtpm.api.recipe.GTRecipeType;
import com.modularmc.gtpm.api.recipe.category.GTRecipeCategory;
import com.modularmc.gtpm.api.recipe.chance.logic.ChanceLogic;
import com.modularmc.gtpm.api.recipe.condition.RecipeConditionType;
import com.modularmc.gtpm.api.registry.GTRegistries;
import com.modularmc.gtpm.api.registry.registrate.provider.GTLanguageProvider;
import com.modularmc.gtpm.client.renderer.cover.ICoverRenderer;
import com.modularmc.gtpm.common.recipe.condition.DimensionCondition;

import com.modularmc.registrate.AbstractRegistrate;
import com.modularmc.registrate.builders.base.Builder;
import com.modularmc.registrate.builders.base.NoConfigBuilder;
import com.modularmc.registrate.localization.LocalizationDeclaration;
import com.modularmc.registrate.localization.MinecraftLocale;
import com.modularmc.registrate.localization.TranslationKey;
import com.modularmc.registrate.providers.RegistrateLangProvider;
import com.modularmc.registrate.providers.core.GeneratorType;
import com.modularmc.registrate.providers.core.ProviderType;
import com.modularmc.registrate.util.entry.ItemEntry;
import com.modularmc.registrate.util.entry.RegistryEntry;
import com.modularmc.registrate.util.nullness.NonNullFunction;
import com.modularmc.registrate.util.nullness.NonNullSupplier;

import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.JukeboxSong;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.registries.DeferredHolder;

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

    /** Starts a structure predicate and its runtime compiler under this mod's namespace. */
    public StructurePredicateType.Builder structurePredicate(String name) {
        return new StructurePredicateType.Builder(this, name);
    }

    public GTRegistryEntryBuilder.Definition<PredicateType<?>> predicate(String name) {
        return registryEntry(name, GTRegistries.PREDICATE_TYPES);
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
        return datapackKey(GTRegistries.ORE_VEIN_REGISTRY, name);
    }

    /** Declares a bedrock fluid key in this mod's namespace for use during datapack bootstrap. */
    public DatapackKeyBuilder<BedrockFluidDefinition> bedrockFluid(String name) {
        return datapackKey(GTRegistries.BEDROCK_FLUID_REGISTRY, name);
    }

    /** Declares an ID for an entry bound later by the corresponding datapack bootstrap. */
    public <T> DatapackKeyBuilder<T> datapackKey(ResourceKey<? extends Registry<T>> registry, String name) {
        return new DatapackKeyBuilder<>(this, ResourceKey.create(registry, makeResourceLocation(name)));
    }

    public DatapackKeyBuilder<DamageType> damageType(String name) {
        var type = ResourceKey.create(Registries.DAMAGE_TYPE, makeResourceLocation(name));
        return new DatapackKeyBuilder<>(this, type, type.location().toLanguageKey("death.attack"));
    }

    public DatapackKeyBuilder<JukeboxSong> jukeboxSong(String name, String recordItem) {
        var song = ResourceKey.create(Registries.JUKEBOX_SONG, makeResourceLocation(name));
        return new DatapackKeyBuilder<>(this, song, makeResourceLocation(recordItem).toLanguageKey("item", "desc"));
    }

    /** Starts a recipe type declaration in this mod's namespace. */
    public GTRecipeType.Builder recipeType(String name) {
        return new GTRecipeType.Builder(this, name);
    }

    /** Starts a category with a name independent of the recipe type's default category. */
    public GTRecipeCategory.Builder recipeCategory(String name) {
        return new GTRecipeCategory.Builder(this, makeResourceLocation(name));
    }

    /** Starts a definition that can be referenced before the registry event. */
    public <R> GTRegistryEntryBuilder.Definition<R> registryEntry(String name, Registry<R> registry) {
        var id = makeResourceLocation(name);
        return new GTRegistryEntryBuilder.Definition<>(this, registry, id,
                id.toLanguageKey(registry.key().location().getPath()));
    }

    public RecipeConditionType.Builder recipeCondition(String name) {
        return new RecipeConditionType.Builder(this, makeResourceLocation(name));
    }

    /** Declares translations shared by recipe conditions or supplied for an optional integration. */
    public GTRegistrate recipeConditionLocalization(String path, Consumer<LocalizationDeclaration.Builder> configure) {
        addLocalization(new TranslationKey(getModid(), "recipe.condition." + path), configure);
        return this;
    }

    public GTRegistryEntryBuilder.Definition<RecipeCapability<?>> recipeCapability(String name) {
        return registryEntry(name, GTRegistries.RECIPE_CAPABILITIES);
    }

    public GTRegistryEntryBuilder.Definition<ChanceLogic> chanceLogic(String name) {
        var id = makeResourceLocation(name);
        return new GTRegistryEntryBuilder.Definition<>(this, GTRegistries.CHANCE_LOGICS, id,
                id.getNamespace() + ".chance_logic." + id.getPath());
    }

    public GTRegistryEntryBuilder<CoverDefinition> cover(String name, CoverDefinition.CoverBehaviourProvider behavior,
                                                        Supplier<Supplier<ICoverRenderer>> renderer) {
        var id = makeResourceLocation(name);
        return new GTRegistryEntryBuilder<>(this, GTRegistries.COVERS, id, new CoverDefinition(id, behavior, renderer),
                id.toLanguageKey("cover"));
    }

    /** Uses the dimension's registry ID so consumers can resolve the marker by dimension. */
    public GTRegistryEntryBuilder<DimensionMarker> dimensionMarker(ResourceKey<Level> dimension, int tier,
                                                                   Supplier<? extends ItemLike> icon,
                                                                   @Nullable Component overrideName) {
        var id = dimension.location();
        var marker = new DimensionMarker(tier, icon,
                overrideName == null ? DimensionCondition.getDimensionName(dimension) : overrideName);
        return new GTRegistryEntryBuilder<>(this, GTRegistries.DIMENSION_MARKERS, id, marker,
                id.toLanguageKey("dimension_marker"));
    }

    public Element.Builder element(String name) {
        return new Element.Builder(this, name);
    }

    public GTRegistryEntryBuilder.Definition<ToolBehaviorType<?>> toolBehavior(String name) {
        return registryEntry(name, GTRegistries.TOOL_BEHAVIORS);
    }

    public <T> NoConfigBuilder<DataComponentType<?>, DataComponentType<T>, GTRegistrate> dataComponent(
            String name, Consumer<DataComponentType.Builder<T>> configure) {
        return generic(name, Registries.DATA_COMPONENT_TYPE, () -> {
            var builder = DataComponentType.<T>builder();
            configure.accept(builder);
            return builder.build();
        });
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
