package com.modularmc.gtpm.common.cover.detector;

import com.modularmc.gtpm.api.blockentity.ConfigCopyHelper;
import com.modularmc.gtpm.api.capability.IControllable;
import com.modularmc.gtpm.api.capability.ICoverable;
import com.modularmc.gtpm.api.cover.CoverBehavior;
import com.modularmc.gtpm.api.cover.CoverDefinition;
import com.modularmc.gtpm.api.machine.TickableSubscription;
import com.modularmc.gtpm.api.sync_system.SyncFieldData;
import com.modularmc.gtpm.api.sync_system.annotations.SaveField;
import com.modularmc.gtpm.api.sync_system.annotations.SyncToClient;
import com.modularmc.gtpm.common.data.item.GTItemAbilities;
import com.modularmc.gtpm.utils.ExtendedUseOnContext;

import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;

import lombok.Getter;
import lombok.Setter;

public abstract class DetectorCover extends CoverBehavior implements IControllable {

    @SaveField
    @Getter
    @Setter
    protected boolean isWorkingEnabled = true;
    protected TickableSubscription subscription;

    @SaveField
    @SyncToClient
    @Getter
    private boolean isInverted;

    public DetectorCover(CoverDefinition definition, ICoverable coverHolder, Direction attachedSide) {
        super(definition, coverHolder, attachedSide);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        subscription = coverHolder.subscribeServerTick(subscription, this::update);
    }

    @Override
    public void onRemoved() {
        super.onRemoved();
        if (subscription != null) {
            subscription.unsubscribe();
        }
    }

    public void setInverted(boolean inverted) {
        isInverted = inverted;
    }

    protected abstract void update();

    private void toggleInvertedWithNotification() {
        setInverted(!isInverted());

        if (!this.coverHolder.isRemote()) {
            this.coverHolder.notifyBlockUpdate();
        }
    }

    @Override
    public InteractionResult onScrewdriverClick(ExtendedUseOnContext context) {
        InteractionResult superResult = super.onScrewdriverClick(context);
        if (superResult != InteractionResult.PASS) {
            return superResult;
        }
        if (!context.getItemInHand().canPerformAction(GTItemAbilities.SCREWDRIVER_CONFIGURE)) {
            return InteractionResult.FAIL;
        }

        if (!this.coverHolder.isRemote()) {
            toggleInvertedWithNotification();

            String translationKey = isInverted() ? "cover.detector_base.message_inverted_state" :
                    "cover.detector_base.message_normal_state";
            context.getPlayer().sendSystemMessage(Component.translatable(translationKey));
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    public boolean canConnectRedstone() {
        return true;
    }

    @Override
    public boolean canPipePassThrough() {
        return false;
    }

    @Override
    public SyncFieldData copyConfig(HolderLookup.Provider registries) {
        return ConfigCopyHelper.withFields(super.copyConfig(registries), fields -> fields
                .put(SyncFieldData.key("inverted"),
                        isInverted));
    }

    @Override
    public void pasteConfig(ServerPlayer player, HolderLookup.Provider registries, SyncFieldData config) {
        setInverted(ConfigCopyHelper.getBoolean(config, "inverted"));
        super.pasteConfig(player, registries, config);
    }
}
