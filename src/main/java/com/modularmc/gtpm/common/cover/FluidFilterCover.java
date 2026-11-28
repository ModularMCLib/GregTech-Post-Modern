package com.modularmc.gtpm.common.cover;

import com.modularmc.gtpm.api.blockentity.ConfigCopyHelper;
import com.modularmc.gtpm.api.capability.ICoverable;
import com.modularmc.gtpm.api.cover.CoverBehavior;
import com.modularmc.gtpm.api.cover.CoverDefinition;
import com.modularmc.gtpm.api.cover.filter.FluidFilter;
import com.modularmc.gtpm.api.gui.GuiTextures;
import com.modularmc.gtpm.api.gui.UITemplate;
import com.modularmc.gtpm.api.gui.element.GTEnumSelectorElement;
import com.modularmc.gtpm.api.gui.element.GTLabelElement;
import com.modularmc.gtpm.api.gui.factory.CoverUIProvider;
import com.modularmc.gtpm.api.gui.factory.UICoverHolder;
import com.modularmc.gtpm.api.sync_system.SyncFieldData;
import com.modularmc.gtpm.api.sync_system.annotations.SaveField;
import com.modularmc.gtpm.api.sync_system.annotations.SyncToClient;
import com.modularmc.gtpm.api.sync_system.rpc.RPCMethod;
import com.modularmc.gtpm.api.transfer.fluid.FluidHandlerDelegate;
import com.modularmc.gtpm.api.transfer.fluid.IFluidHandlerModifiable;
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
import net.neoforged.neoforge.fluids.FluidStack;

import lombok.Getter;
import org.jetbrains.annotations.Nullable;

@TransVariants(keyPrefix = "cover.fluid_filter",
               value = {
                       @Variant(id = "config_amount.0",
                                source = "Scroll wheel up increases amount, down decreases.",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "向上或向下滚动鼠标滚轮以增加或减少流体量。")),
                       @Variant(id = "config_amount.1",
                                source = "Shift[§6x10§r],Ctrl[§ex100§r],Shift+Ctrl[§ax1000§r]",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "Shift[§6x10§r]，Ctrl[§ex100§r]，Shift+Ctrl[§ax1000§r]")),
                       @Variant(id = "config_amount.2",
                                source = "Right click increases amount, left click decreases.",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "亦可通过右击或左击来增减流体量。")),
                       @Variant(id = "config_amount.3",
                                source = "Hold shift to double/halve.",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "按住Shift时点击鼠标则将使流体量翻倍或减半。")),
                       @Variant(id = "config_amount.4",
                                source = "Middle click to clear",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "点击中键以清除")),
                       @Variant(id = "mode.filter_both",
                                source = "Filter Fill & Drain",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "过滤两者")),
                       @Variant(id = "mode.filter_drain",
                                source = "Filter Drain",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "过滤输出")),
                       @Variant(id = "mode.filter_fill",
                                source = "Filter Fill",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "过滤输入")),
                       @Variant(id = "title",
                                source = "Fluid Filter",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "流体过滤卡")),
               })
public class FluidFilterCover extends CoverBehavior
                              implements CoverUIProvider {

    protected FluidFilter fluidFilter;
    @SaveField
    @SyncToClient
    @Getter
    protected FilterMode filterMode = FilterMode.FILTER_INSERT;
    private FilteredFluidHandlerWrapper fluidFilterWrapper;
    @SaveField
    @Getter
    protected ManualIOMode allowFlow = ManualIOMode.DISABLED;

    public FluidFilterCover(CoverDefinition definition, ICoverable coverHolder, Direction attachedSide) {
        super(definition, coverHolder, attachedSide);
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
        return super.canAttach() && coverHolder.getFluidHandlerCap(attachedSide, false) != null;
    }

    public FluidFilter getFluidFilter() {
        if (fluidFilter == null) {
            fluidFilter = FluidFilter.loadFilter(attachItem);
        }
        return fluidFilter;
    }

    @Override
    public @Nullable IFluidHandlerModifiable getFluidHandlerCap(@Nullable IFluidHandlerModifiable defaultValue) {
        if (defaultValue == null) {
            return null;
        }

        if (fluidFilterWrapper == null || fluidFilterWrapper.delegate != defaultValue) {
            this.fluidFilterWrapper = new FilteredFluidHandlerWrapper(defaultValue);
        }

        return fluidFilterWrapper;
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
        root.addChild(getFluidFilter().openConfigurator(62, 25));
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
    public void configureFluidFilter(FilterMode filterMode, ManualIOMode manualIOMode) {
        setFilterMode(filterMode);
        setAllowFlow(manualIOMode);
    }

    private void sendConfigAction(Player player, UICoverHolder holder) {
        if (player.level().isClientSide()) {
            holder.getCover().rpcToServer("configureFluidFilter", getFilterMode(), getAllowFlow());
        }
    }

    private class FilteredFluidHandlerWrapper extends FluidHandlerDelegate {

        public FilteredFluidHandlerWrapper(IFluidHandlerModifiable delegate) {
            super(delegate);
        }

        @Override
        public int fill(FluidStack resource, FluidAction action) {
            if (filterMode == FilterMode.FILTER_EXTRACT) {
                if (allowFlow == ManualIOMode.DISABLED) {
                    return 0;
                }
                if (allowFlow == ManualIOMode.UNFILTERED) {
                    return super.fill(resource, action);
                }
            }
            if (!getFluidFilter().test(resource)) {
                return 0;
            }
            return super.fill(resource, action);
        }

        @Override
        public FluidStack drain(FluidStack resource, FluidAction action) {
            if (filterMode == FilterMode.FILTER_INSERT) {
                if (allowFlow == ManualIOMode.DISABLED) {
                    return FluidStack.EMPTY;
                }
                if (allowFlow == ManualIOMode.UNFILTERED) {
                    return super.drain(resource, action);
                }
            }
            if (!getFluidFilter().test(resource)) {
                return FluidStack.EMPTY;
            }
            return super.drain(resource, action);
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
        fluidFilter = FluidFilter.loadFilter(ConfigCopyHelper.getItem(registries, config, "filter"));
        super.pasteConfig(player, registries, config);
    }
}
