package com.modularmclib.gtpm.api.misc.virtualregistry;

import com.modularmclib.gtpm.GTPM;
import com.modularmclib.gtpm.api.sync_system.FieldCodecs;
import com.modularmclib.gtpm.api.sync_system.SyncDataHolder;
import com.modularmclib.gtpm.api.sync_system.SyncFieldData;
import com.modularmclib.gtpm.api.sync_system.annotations.SaveField;
import com.modularmclib.gtpm.api.sync_system.managed.ISyncManaged;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.world.level.saveddata.SavedData;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;

public class VirtualEnderRegistry extends SavedData implements ISyncManaged {

    static {
        FieldCodecs.registerFactory(VirtualRegistryMap.class, VirtualRegistryMap::new);
    }

    private static final String DATA_ID = GTPM.MOD_ID + ".virtual_entry_data";
    private static volatile VirtualEnderRegistry data;
    @SaveField
    private final VirtualRegistryMap publicEntries = new VirtualRegistryMap();
    @SaveField
    private final Map<UUID, VirtualRegistryMap> privateEntries = new Object2ObjectOpenHashMap<>();
    private SyncDataHolder syncDataHolder;

    @Override
    public SyncDataHolder getSyncDataHolder() {
        if (syncDataHolder == null) syncDataHolder = new SyncDataHolder(this);
        return syncDataHolder;
    }

    @Override
    public @Nullable ISyncManaged getParentSyncObject() {
        return null;
    }

    public VirtualEnderRegistry() {}

    public VirtualEnderRegistry(CompoundTag name, HolderLookup.@NotNull Provider registries) {
        SyncFieldData decoded = SyncFieldData.CODEC.parse(registries.createSerializationContext(NbtOps.INSTANCE), name)
                .getOrThrow();
        getSyncDataHolder().deserializeFieldData(registries, decoded, false);
    }

    public static VirtualEnderRegistry getInstance() {
        if (data == null) {
            var server = GTPM.getMinecraftServer();
            if (server != null) {
                data = server.overworld().getDataStorage().computeIfAbsent(
                        new SavedData.Factory<>(VirtualEnderRegistry::new, VirtualEnderRegistry::new), DATA_ID);
            }
        }

        return data;
    }

    /**
     * To be called on server stopped event
     */
    public static void release() {
        if (data != null) {
            data = null;
            GTPM.LOGGER.debug("VirtualEnderRegistry has been unloaded");
        }
    }

    public <T extends VirtualEntry> T getEntry(@Nullable UUID owner, EntryTypes<T> type, String name) {
        return getRegistry(owner).getEntry(type, name);
    }

    public void addEntry(@Nullable UUID owner, String name, VirtualEntry entry) {
        getRegistry(owner).addEntry(name, entry);
    }

    public boolean hasEntry(@Nullable UUID owner, EntryTypes<?> type, String name) {
        return getRegistry(owner).contains(type, name);
    }

    public @NotNull <T extends VirtualEntry> T getOrCreateEntry(@Nullable UUID owner, EntryTypes<T> type, String name) {
        if (!hasEntry(owner, type, name)) addEntry(owner, name, type.createInstance());
        return getEntry(owner, type, name);
    }

    /**
     * Removes an entry from the registry. Use with caution!
     *
     * @param owner The uuid of the player the entry is private to, or null if the entry is public
     * @param type  Type of the registry to remove from
     * @param name  The name of the entry
     */
    public void deleteEntry(@Nullable UUID owner, EntryTypes<?> type, String name) {
        var registry = getRegistry(owner);
        if (registry.contains(type, name)) {
            registry.deleteEntry(type, name);
            return;
        }
        GTPM.LOGGER.warn("Attempted to delete {} entry {} of type {}, which does not exist",
                owner == null ? "public" : String.format("private [%s]", owner), name, type);
    }

    public <T extends VirtualEntry> void deleteEntryIf(@Nullable UUID owner, EntryTypes<T> type, String name,
                                                       Predicate<T> shouldDelete) {
        T entry = getEntry(owner, type, name);
        if (entry != null && shouldDelete.test(entry)) deleteEntry(owner, type, name);
    }

    public Set<String> getEntryNames(UUID owner, EntryTypes<?> type) {
        return getRegistry(owner).getEntryNames(type);
    }

    private VirtualRegistryMap getRegistry(UUID owner) {
        return owner == null ? publicEntries : privateEntries.computeIfAbsent(owner, key -> new VirtualRegistryMap());
    }

    @NotNull
    @Override
    public final CompoundTag save(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        SyncFieldData fields = getSyncDataHolder().serializeToSaveFieldData(registries);
        return tag.merge((CompoundTag) SyncFieldData.CODEC
                .encodeStart(registries.createSerializationContext(NbtOps.INSTANCE), fields)
                .getOrThrow());
    }

    @Override
    public boolean isDirty() {
        // can't think of a good way to mark dirty other than always return true;
        return true;
    }
}
