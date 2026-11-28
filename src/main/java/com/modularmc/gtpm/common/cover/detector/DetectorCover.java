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

import com.modularmc.registrate.localization.MinecraftLocale;
import com.modularmc.registrate.localization.annotation.TransVariants;
import com.modularmc.registrate.localization.annotation.Translation;
import com.modularmc.registrate.localization.annotation.Variant;

import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;

import lombok.Getter;
import lombok.Setter;

@TransVariants(keyPrefix = "cover",
               value = {
                       @Variant(id = "advanced_detector.latch.disabled.0",
                                source = "Behavior: Continuous",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "行为：线性")),
                       @Variant(id = "advanced_detector.latch.disabled.1",
                                source = "",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "")),
                       @Variant(id = "advanced_detector.latch.disabled.2",
                                source = "Change the redstone behavior of this Cover.",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "更改此覆盖板的红石行为。")),
                       @Variant(id = "advanced_detector.latch.disabled.3",
                                source = "§eContinuous§7 - Default; values less than the minimum output 0; values higher than the maximum output 15; values between min and max output between 0 and 15",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "§e线性§7 - 默认模式；物品数量小于最小值时不发出红石信号，大于最大值时发出信号强度15的红石信号，数量介于二者之间发出信号强度在0 - 15之间的红石信号")),
                       @Variant(id = "advanced_detector.latch.disabled.4",
                                source = "§eLatched§7 - output 15 until above max, then output 0 until below min",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "§e锁存§7 - 发出满级红石信号，直到物品数量大于最大值；然后停止发出红石信号，直到数量低于最小值")),
                       @Variant(id = "advanced_detector.latch.enabled.0",
                                source = "Behavior: Latched",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "行为：锁存")),
                       @Variant(id = "advanced_detector.latch.enabled.1",
                                source = "",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "")),
                       @Variant(id = "advanced_detector.latch.enabled.2",
                                source = "Change the redstone behavior of this Cover.",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "更改此覆盖板的红石行为。")),
                       @Variant(id = "advanced_detector.latch.enabled.3",
                                source = "§eContinuous§7 - Default; values less than the minimum output 0; values higher than the maximum output 15; values between min and max output between 0 and 15",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "§e线性§7 - 默认模式；物品数量小于最小值时不发出红石信号，大于最大值时发出信号强度15的红石信号，数量介于二者之间发出信号强度在0 - 15之间的红石信号")),
                       @Variant(id = "advanced_detector.latch.enabled.4",
                                source = "§eLatched§7 - output 15 until above max, then output 0 until below min",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "§e锁存§7 - 发出满级红石信号，直到物品数量大于最大值；然后停止发出红石信号，直到数量低于最小值")),
                       @Variant(id = "detector_base.message_inverted_state",
                                source = "Monitoring Status: Inverted",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "监控状态：反相")),
                       @Variant(id = "detector_base.message_normal_state",
                                source = "Monitoring Status: Normal",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "监控状态：普通")),
               })
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
