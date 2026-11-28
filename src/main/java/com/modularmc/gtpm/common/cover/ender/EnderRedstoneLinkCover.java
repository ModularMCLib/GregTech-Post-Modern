package com.modularmc.gtpm.common.cover.ender;

import com.modularmc.gtpm.api.capability.ICoverable;
import com.modularmc.gtpm.api.cover.CoverDefinition;
import com.modularmc.gtpm.api.gui.UITemplate;
import com.modularmc.gtpm.api.misc.virtualregistry.EntryTypes;
import com.modularmc.gtpm.api.misc.virtualregistry.VirtualEntry;
import com.modularmc.gtpm.api.misc.virtualregistry.entries.VirtualRedstone;
import com.modularmc.gtpm.api.sync_system.SyncDataHolder;
import com.modularmc.gtpm.api.sync_system.annotations.SaveField;
import com.modularmc.gtpm.api.sync_system.annotations.SyncToClient;

import com.modularmc.registrate.localization.MinecraftLocale;
import com.modularmc.registrate.localization.annotation.Trans;
import com.modularmc.registrate.localization.annotation.TransVariants;
import com.modularmc.registrate.localization.annotation.Translation;
import com.modularmc.registrate.localization.annotation.Variant;

import com.lowdragmc.lowdraglib2.gui.ui.UIElement;

import net.minecraft.core.Direction;

import lombok.Getter;

import java.util.UUID;

@Trans(key = "cover.ender_redstone_link.title",
       source = "Ender Redstone Link",
       translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "末影红石连接"))
@TransVariants(keyPrefix = "gtpm.ender_redstone_link_cover",
               value = {
                       @Variant(id = "title",
                                source = "Ender Redstone Link",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "末影红石连接")),
                       @Variant(id = "label",
                                source = "Redstone power: %d",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "红石信号强度：%d")),
                       @Variant(id = "tooltip",
                                source = "§7Transmits §fRedstone signals§7 with a §fWireless §dEnder§f Connection§7 as §fCover§7.",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "作§f覆盖板§7时利用§f无线§7§d末影§f连接§7传输§f红石信号§7。")),
               })
public class EnderRedstoneLinkCover extends AbstractEnderLinkCover<VirtualRedstone> {

    @Getter
    protected final SyncDataHolder syncDataHolder = new SyncDataHolder(this);

    @SaveField
    @SyncToClient
    private VirtualRedstone storage;
    @SaveField
    @SyncToClient
    private UUID uuid;

    public EnderRedstoneLinkCover(CoverDefinition definition, ICoverable coverHolder, Direction attachedSide) {
        super(definition, coverHolder, attachedSide);
        if (!coverHolder.isRemote()) {
            uuid = UUID.randomUUID();
            setVirtualEntry();
        } else uuid = null;
    }

    @Override
    public boolean canAttach() {
        return true;
    }

    @Override
    protected String identifier() {
        return "ERLink#";
    }

    @Override
    protected VirtualRedstone getEntry() {
        return storage;
    }

    @Override
    protected void setEntry(VirtualEntry entry) {
        if (storage != null) storage.removeMember(uuid);
        storage = (VirtualRedstone) entry;
        storage.addMember(uuid);
        syncDataHolder.markClientSyncFieldDirty("storage");
    }

    @Override
    protected EntryTypes<VirtualRedstone> getEntryType() {
        return EntryTypes.ENDER_REDSTONE;
    }

    @Override
    protected void transfer() {
        switch (io) {
            case IN -> storage.setSignal(uuid, getSignalInput());
            case OUT -> setRedstoneSignalOutput(storage.getSignal());
        }
    }

    @Override
    protected UIElement addVirtualEntryElement(VirtualEntry entry, int x, int y, int width, int height,
                                               boolean canClick) {
        return UITemplate.setBounds(new UIElement(), x, y, width, height);
    }

    @Override
    protected String getUITitle() {
        return "cover.ender_redstone_link.title";
    }

    @Override
    public boolean canConnectRedstone() {
        return true;
    }

    @Override
    public void onRemoved() {
        storage.removeMember(uuid);
        super.onRemoved();
    }

    protected int getSignalInput() {
        return coverHolder.getLevel().getSignal(coverHolder.getBlockPos().relative(attachedSide),
                attachedSide.getOpposite());
    }
}
