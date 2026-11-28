package com.modularmc.gtpm.api.registry;

import com.modularmc.gtpm.GTPM;
import com.modularmc.gtpm.api.capability.recipe.RecipeCapability;
import com.modularmc.gtpm.api.cover.CoverDefinition;
import com.modularmc.gtpm.api.data.DimensionMarker;
import com.modularmc.gtpm.api.data.chemical.Element;
import com.modularmc.gtpm.api.data.chemical.material.Material;
import com.modularmc.gtpm.api.data.chemical.material.registry.MaterialRegistry;
import com.modularmc.gtpm.api.data.tag.TagPrefix;
import com.modularmc.gtpm.api.data.worldgen.GTOreDefinition;
import com.modularmc.gtpm.api.data.worldgen.bedrockfluid.BedrockFluidDefinition;
import com.modularmc.gtpm.api.data.worldgen.bedrockore.BedrockOreDefinition;
import com.modularmc.gtpm.api.item.tool.behavior.ToolBehaviorType;
import com.modularmc.gtpm.api.machine.MachineDefinition;
import com.modularmc.gtpm.api.multiblock.autobuild.MaterialSources;
import com.modularmc.gtpm.api.multiblock.structurepredicate.PredicateType;
import com.modularmc.gtpm.api.multiblock.structurepredicate.StructurePredicateType;
import com.modularmc.gtpm.api.recipe.GTRecipeType;
import com.modularmc.gtpm.api.recipe.category.GTRecipeCategory;
import com.modularmc.gtpm.api.recipe.chance.logic.ChanceLogic;
import com.modularmc.gtpm.api.recipe.condition.RecipeConditionType;
import com.modularmc.gtpm.api.sound.SoundEntry;

import net.minecraft.client.Minecraft;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.RegisterEvent;
import net.neoforged.neoforge.registries.RegistryBuilder;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.UnmodifiableView;
import org.jspecify.annotations.Nullable;

import java.util.*;

import static com.google.common.base.Preconditions.checkArgument;

public final class GTRegistries {

    // spotless:off
    private static final LinkedHashMap<ResourceLocation, Registry<?>> LOAD_ORDER = new LinkedHashMap<>();

    // server (datapack) registries' keys
    public static final ResourceKey<Registry<GTOreDefinition>> ORE_VEIN_REGISTRY = makeRegistryKey(GTPM.id("ore_vein"));
    public static final ResourceKey<Registry<BedrockFluidDefinition>> BEDROCK_FLUID_REGISTRY = makeRegistryKey(GTPM.id("bedrock_fluid"));
    public static final ResourceKey<Registry<BedrockOreDefinition>> BEDROCK_ORE_REGISTRY = makeRegistryKey(GTPM.id("bedrock_ore"));

    // static registries' keys
    public static final ResourceKey<Registry<Element>> ELEMENT_REGISTRY = makeRegistryKey(GTPM.id("element"));
    public static final ResourceKey<Registry<TagPrefix>> TAG_PREFIX_REGISTRY = makeRegistryKey(GTPM.id("tag_prefix"));
    public static final ResourceKey<Registry<Material>> MATERIAL_REGISTRY = makeRegistryKey(GTPM.id("material"));
    public static final ResourceKey<Registry<MachineDefinition>> MACHINE_REGISTRY = makeRegistryKey(GTPM.id("machine"));
    public static final ResourceKey<Registry<CoverDefinition>> COVER_REGISTRY = makeRegistryKey(GTPM.id("cover"));

    public static final ResourceKey<Registry<GTRecipeType>> RECIPE_TYPE_REGISTRY = makeRegistryKey(GTPM.id("recipe_type"));
    public static final ResourceKey<Registry<GTRecipeCategory>> RECIPE_CATEGORY_REGISTRY = makeRegistryKey(GTPM.id("recipe_category"));
    public static final ResourceKey<Registry<RecipeCapability<?>>> RECIPE_CAPABILITY_REGISTRY = makeRegistryKey(GTPM.id("recipe_capability"));
    public static final ResourceKey<Registry<RecipeConditionType<?>>> RECIPE_CONDITION_REGISTRY = makeRegistryKey(GTPM.id("recipe_condition"));
    public static final ResourceKey<Registry<ChanceLogic>> CHANCE_LOGIC_REGISTRY = makeRegistryKey(GTPM.id("chance_logic"));

    public static final ResourceKey<Registry<ToolBehaviorType<?>>> TOOL_BEHAVIOR_REGISTRY = makeRegistryKey(GTPM.id("tool_behavior"));
    public static final ResourceKey<Registry<SoundEntry>> SOUND_REGISTRY = makeRegistryKey(GTPM.id("sound"));
    public static final ResourceKey<Registry<DimensionMarker>> DIMENSION_MARKER_REGISTRY = makeRegistryKey(GTPM.id("dimension_marker"));

    public static final ResourceKey<Registry<StructurePredicateType<?>>> STRUCTURE_PREDICATE_REGISTRY = makeRegistryKey(GTPM.id("block_predicate"));
    public static final ResourceKey<Registry<PredicateType<?>>> PREDICATE_TYPE_REGISTRY = makeRegistryKey(GTPM.id("predicate_type"));
    public static final ResourceKey<Registry<MaterialSources.Registration>> MATERIAL_SOURCE_REGISTRY = makeRegistryKey(GTPM.id("material_source"));

    // GT Registries
    public static final Registry<Element> ELEMENTS = makeRegistry(ELEMENT_REGISTRY);
    public static final MaterialRegistry MATERIALS = makeMaterialRegistry();
    public static final Registry<TagPrefix> TAG_PREFIXES = makeRegistry(TAG_PREFIX_REGISTRY);

    public static final Registry<SoundEntry> SOUNDS = makeRegistry(SOUND_REGISTRY, false);
    public static final Registry<ChanceLogic> CHANCE_LOGICS = makeRegistry(CHANCE_LOGIC_REGISTRY);
    public static final Registry<RecipeCapability<?>> RECIPE_CAPABILITIES = makeRegistry(RECIPE_CAPABILITY_REGISTRY);
    public static final Registry<RecipeConditionType<?>> RECIPE_CONDITIONS = makeRegistry(RECIPE_CONDITION_REGISTRY);
    public static final Registry<GTRecipeCategory> RECIPE_CATEGORIES = makeRegistry(RECIPE_CATEGORY_REGISTRY);

    public static final Registry<MachineDefinition> MACHINES = makeRegistry(MACHINE_REGISTRY);
    public static final Registry<CoverDefinition> COVERS = makeRegistry(COVER_REGISTRY);

    public static final Registry<ToolBehaviorType<?>> TOOL_BEHAVIORS = makeRegistry(TOOL_BEHAVIOR_REGISTRY);
    public static final Registry<DimensionMarker> DIMENSION_MARKERS = makeRegistry(DIMENSION_MARKER_REGISTRY, false);

    public static final Registry<StructurePredicateType<?>> STRUCTURE_PREDICATE_TYPES = makeRegistry(STRUCTURE_PREDICATE_REGISTRY);
    public static final Registry<PredicateType<?>> PREDICATE_TYPES = makeRegistry(PREDICATE_TYPE_REGISTRY);
    public static final Registry<MaterialSources.Registration> MATERIAL_SOURCES = makeRegistry(MATERIAL_SOURCE_REGISTRY);
    // spotless:on

    public static <T> ResourceKey<Registry<T>> makeRegistryKey(ResourceLocation registryId) {
        return ResourceKey.createRegistryKey(registryId);
    }

    public static <T> MappedRegistry<T> makeRegistry(ResourceKey<Registry<T>> key) {
        return makeRegistry(key, true);
    }

    public static <T> MappedRegistry<T> makeRegistry(ResourceKey<Registry<T>> key, boolean sync) {
        MappedRegistry<T> registry = (MappedRegistry<T>) new RegistryBuilder<>(key)
                .sync(sync)
                .create();
        LOAD_ORDER.put(key.location(), registry);
        return registry;
    }

    private static MaterialRegistry makeMaterialRegistry() {
        MaterialRegistry registry = new MaterialRegistry(MATERIAL_REGISTRY);
        LOAD_ORDER.put(MATERIAL_REGISTRY.location(), registry);
        return registry;
    }

    private static final Table<Registry<?>, ResourceLocation, Object> TO_REGISTER = HashBasedTable.create();
    private static boolean isFrozen = true;

    public static <V, T extends V> T register(Registry<V> registry, ResourceLocation name, T value) {
        if (!isFrozen) {
            Registry.register(registry, name, value);
        } else {
            if (registry == PREDICATE_TYPES) {
                // The runtime compiler can resolve staged entries, so a second declaration must not replace one.
                checkArgument(lookup(PREDICATE_TYPES, name) == null, "Predicate type is already registered: %s", name);
            }
            TO_REGISTER.put(registry, name, value);
        }
        return value;
    }

    /**
     * Reads an entry from the existing registration staging table before RegisterEvent publishes it, or from
     * the actual registry afterwards. This does not change the registry's registration or freeze lifecycle.
     */
    public static <T> @Nullable T lookup(Registry<T> registry, ResourceLocation id) {
        Object pending = TO_REGISTER.get(registry, id);
        if (pending != null) {
            // register(Registry<V>, ..., T extends V) guarantees that each staging row has this registry's type.
            @SuppressWarnings("unchecked")
            T value = (T) pending;
            return value;
        }
        return registry.get(id);
    }

    // ignore the generics and hope the registered objects are still correctly typed :3
    @SuppressWarnings({ "rawtypes", "unchecked" })
    private static void actuallyRegister(RegisterEvent event) {
        for (Registry reg : TO_REGISTER.rowKeySet()) {
            event.register(reg.key(), helper -> TO_REGISTER.row(reg).forEach(helper::register));
        }
        TO_REGISTER.clear();
    }

    private static void onUnfreeze(RegisterEvent event) {
        isFrozen = false;
    }

    @ApiStatus.Internal
    public static void freezeRegistrations() {
        isFrozen = true;
    }

    public static void init(IEventBus eventBus) {
        eventBus.addListener(EventPriority.HIGHEST, GTRegistries::onUnfreeze);
        eventBus.addListener(EventPriority.LOW, GTRegistries::actuallyRegister);
    }

    @UnmodifiableView
    public static List<ResourceLocation> getRegistrationOrder() {
        return List.copyOf(LOAD_ORDER.keySet());
    }

    @UnmodifiableView
    public static Collection<Registry<?>> getRegistries() {
        return LOAD_ORDER.values();
    }

    private static final RegistryAccess BLANK = RegistryAccess.fromRegistryOfRegistries(BuiltInRegistries.REGISTRY);
    private static RegistryAccess FROZEN = BLANK;

    /**
     * You shouldn't call it, you should probably not even look at it just to be extra safe
     *
     * @param registryAccess the new value to set to the frozen registry access
     */
    @ApiStatus.Internal
    public static void updateFrozenRegistry(RegistryAccess registryAccess) {
        FROZEN = registryAccess;
    }

    public static RegistryAccess builtinRegistry() {
        if (GTPM.isClientThread()) {
            return ClientHelpers.getClientRegistries();
        }
        return FROZEN;
    }

    private static class ClientHelpers {

        private static RegistryAccess getClientRegistries() {
            if (Minecraft.getInstance().getConnection() != null) {
                return Minecraft.getInstance().getConnection().registryAccess();
            } else {
                return FROZEN;
            }
        }
    }
}
