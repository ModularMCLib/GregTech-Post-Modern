package com.modularmc.gtpm.common.cover;

import com.modularmc.gtpm.api.blockentity.ConfigCopyHelper;
import com.modularmc.gtpm.api.capability.ICoverable;
import com.modularmc.gtpm.api.cover.CoverBehavior;
import com.modularmc.gtpm.api.cover.CoverDefinition;
import com.modularmc.gtpm.api.cover.filter.ItemFilter;
import com.modularmc.gtpm.api.cover.filter.SmartItemFilter;
import com.modularmc.gtpm.api.gui.GuiTextures;
import com.modularmc.gtpm.api.gui.UITemplate;
import com.modularmc.gtpm.api.gui.element.GTEnumSelectorElement;
import com.modularmc.gtpm.api.gui.element.GTLabelElement;
import com.modularmc.gtpm.api.gui.factory.CoverUIProvider;
import com.modularmc.gtpm.api.gui.factory.UICoverHolder;
import com.modularmc.gtpm.api.machine.MachineCoverContainer;
import com.modularmc.gtpm.api.machine.MetaMachine;
import com.modularmc.gtpm.api.sync_system.SyncFieldData;
import com.modularmc.gtpm.api.sync_system.annotations.SaveField;
import com.modularmc.gtpm.api.sync_system.annotations.SyncToClient;
import com.modularmc.gtpm.api.sync_system.rpc.RPCMethod;
import com.modularmc.gtpm.api.transfer.item.ItemHandlerDelegate;
import com.modularmc.gtpm.common.cover.data.FilterMode;
import com.modularmc.gtpm.common.cover.data.ManualIOMode;

import com.modularmc.registrate.localization.MinecraftLocale;
import com.modularmc.registrate.localization.annotation.TransVariants;
import com.modularmc.registrate.localization.annotation.Translation;
import com.modularmc.registrate.localization.annotation.Variant;

import com.lowdragmc.lowdraglib2.gui.ui.UI;
import com.lowdragmc.lowdraglib2.gui.ui.UIElement;
import com.lowdragmc.lowdraglib2.gui.ui.data.Horizontal;
import com.lowdragmc.lowdraglib2.gui.ui.data.Vertical;

import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandlerModifiable;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@TransVariants(keyPrefix = "cover",
               value = {
                       @Variant(id = "filter.blacklist.disabled",
                                source = "Whitelist",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "白名单")),
                       @Variant(id = "filter.blacklist.enabled",
                                source = "Blacklist",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "黑名单")),
                       @Variant(id = "item_filter.ignore_damage.disabled",
                                source = "Respect Damage",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "匹配耐久度")),
                       @Variant(id = "item_filter.ignore_damage.enabled",
                                source = "Ignore Damage",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "无视耐久度")),
                       @Variant(id = "item_filter.ignore_nbt.disabled",
                                source = "Respect NBT",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "匹配NBT")),
                       @Variant(id = "item_filter.ignore_nbt.enabled",
                                source = "Ignore NBT",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "忽略NBT")),
               })
public class ItemFilterCover extends CoverBehavior
                             implements CoverUIProvider {

    protected ItemFilter itemFilter;
    @SaveField
    @SyncToClient
    @Getter
    protected FilterMode filterMode = FilterMode.FILTER_INSERT;
    private FilteredItemHandlerWrapper itemFilterWrapper;
    @SaveField
    @Getter
    protected ManualIOMode allowFlow = ManualIOMode.DISABLED;

    public ItemFilterCover(CoverDefinition definition, ICoverable coverHolder, Direction attachedSide) {
        super(definition, coverHolder, attachedSide);
    }

    public ItemFilter getItemFilter() {
        if (itemFilter == null) {
            itemFilter = ItemFilter.loadFilter(attachItem);
            if (itemFilter instanceof SmartItemFilter smart && coverHolder instanceof MachineCoverContainer mcc) {
                var machine = MetaMachine.getMachine(mcc.getLevel(), mcc.getBlockPos());
                if (machine != null) smart.setModeFromMachine(machine.getDefinition().getName());
            }
        }
        return itemFilter;
    }

    public void setFilterMode(FilterMode filterMode) {
        this.filterMode = filterMode;
        syncDataHolder.markClientSyncFieldDirty("filterMode");
    }

    public void setAllowFlow(ManualIOMode allowFlow) {
        this.allowFlow = allowFlow;
    }

    @Override
    public boolean canAttach() {
        return super.canAttach() && coverHolder.getItemHandlerCap(attachedSide, false) != null;
    }

    @Override
    public @Nullable IItemHandlerModifiable getItemHandlerCap(IItemHandlerModifiable defaultValue) {
        if (defaultValue == null) {
            return null;
        }
        if (itemFilterWrapper == null || itemFilterWrapper.delegate != defaultValue) {
            this.itemFilterWrapper = new FilteredItemHandlerWrapper(defaultValue);
        }
        return itemFilterWrapper;
    }

    @Override
    public void onAttached(ItemStack itemStack, @Nullable ServerPlayer player) {
        super.onAttached(itemStack, player);
    }

    @Override
    public boolean canCreateUI(Player player, UICoverHolder holder) {
        return holder.getCover() == this;
    }

    @Override
    public UI createUI(Player player, UICoverHolder holder) {
        UIElement root = new UIElement();
        UITemplate.setBounds(root, 0, 0, 176, 167);
        root.style(style -> style.backgroundTexture(GuiTextures.BACKGROUND));

        root.addChild(createLabel());
        root.addChild(GTEnumSelectorElement.selectable(35, 25, 18, 18, FilterMode.VALUES, this::getFilterMode,
                mode -> setFilterMode(player, holder, mode)));
        root.addChild(GTEnumSelectorElement.selectable(35, 45, 18, 18, ManualIOMode.VALUES, this::getAllowFlow,
                mode -> setManualIO(player, holder, mode)));
        root.addChild(getItemFilter().openConfigurator(62, 25));
        root.addChild(UITemplate.bindPlayerInventory(player.getInventory(), GuiTextures.SLOT, 7, 85, true));
        return UI.of(root);
    }

    private GTLabelElement createLabel() {
        GTLabelElement label = new GTLabelElement(60, 5, 111, 10, attachItem.getDescriptionId(), true);
        label.textStyle(style -> style
                .textColor(0x404040)
                .textShadow(false)
                .textAlignHorizontal(Horizontal.LEFT)
                .textAlignVertical(Vertical.CENTER));
        return label;
    }

    private void setFilterMode(Player player, UICoverHolder holder, FilterMode mode) {
        setFilterMode(mode);
        sendConfigAction(player, holder);
    }

    private void setManualIO(Player player, UICoverHolder holder, ManualIOMode mode) {
        setAllowFlow(mode);
        sendConfigAction(player, holder);
    }

    @RPCMethod
    public void configureItemFilter(FilterMode filterMode, ManualIOMode manualIOMode) {
        setFilterMode(filterMode);
        setAllowFlow(manualIOMode);
    }

    private void sendConfigAction(Player player, UICoverHolder holder) {
        if (player.level().isClientSide()) {
            holder.getCover().rpcToServer("configureItemFilter", getFilterMode(), getAllowFlow());
        }
    }

    private class FilteredItemHandlerWrapper extends ItemHandlerDelegate {

        public FilteredItemHandlerWrapper(IItemHandlerModifiable delegate) {
            super(delegate);
        }

        @Override
        public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
            if (filterMode == FilterMode.FILTER_EXTRACT) {
                if (allowFlow == ManualIOMode.DISABLED) {
                    return stack;
                }
                if (allowFlow == ManualIOMode.UNFILTERED) {
                    return super.insertItem(slot, stack, simulate);
                }
            }
            if (!getItemFilter().test(stack)) {
                return stack;
            }
            return super.insertItem(slot, stack, simulate);
        }

        @Override
        public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
            if (filterMode == FilterMode.FILTER_INSERT) {
                if (allowFlow == ManualIOMode.DISABLED) {
                    return ItemStack.EMPTY;
                }
                if (allowFlow == ManualIOMode.UNFILTERED) {
                    return super.extractItem(slot, amount, simulate);
                }
            }
            ItemStack result = super.extractItem(slot, amount, true);
            if (result.isEmpty() || !getItemFilter().test(result)) {
                return ItemStack.EMPTY;
            }
            return simulate ? result : super.extractItem(slot, amount, false);
        }
    }

    @Override
    public SyncFieldData copyConfig(HolderLookup.Provider registries) {
        return ConfigCopyHelper.withFields(super.copyConfig(registries), fields -> fields
                .put(SyncFieldData.key("manualIO"),
                        getAllowFlow().ordinal())
                .put(SyncFieldData.key("filterMode"),
                        getFilterMode().ordinal())
                .put(SyncFieldData.key("filter"), ItemStack.OPTIONAL_CODEC, attachItem, registries));
    }

    @Override
    public void pasteConfig(ServerPlayer player, HolderLookup.Provider registries, SyncFieldData config) {
        setAllowFlow(ManualIOMode.values()[ConfigCopyHelper.getInt(config, "manualIO")]);
        setFilterMode(FilterMode.values()[ConfigCopyHelper.getInt(config, "filterMode")]);
        itemFilter = ItemFilter.loadFilter(ConfigCopyHelper.getItem(registries, config, "filter"));
        super.pasteConfig(player, registries, config);
    }
}
