package com.modularmc.gtpm.common.cover.detector;

import com.modularmc.gtpm.api.blockentity.ConfigCopyHelper;
import com.modularmc.gtpm.api.capability.ICoverable;
import com.modularmc.gtpm.api.cover.CoverDefinition;
import com.modularmc.gtpm.api.cover.filter.FilterHandler;
import com.modularmc.gtpm.api.cover.filter.FilterHandlers;
import com.modularmc.gtpm.api.cover.filter.ItemFilter;
import com.modularmc.gtpm.api.gui.GuiTextures;
import com.modularmc.gtpm.api.gui.UITemplate;
import com.modularmc.gtpm.api.gui.element.GTIntInputElement;
import com.modularmc.gtpm.api.gui.element.GTLabelElement;
import com.modularmc.gtpm.api.gui.element.GTToggleButtonElement;
import com.modularmc.gtpm.api.gui.factory.CoverUIProvider;
import com.modularmc.gtpm.api.gui.factory.UICoverHolder;
import com.modularmc.gtpm.api.sync_system.SyncFieldData;
import com.modularmc.gtpm.api.sync_system.annotations.SaveField;
import com.modularmc.gtpm.api.sync_system.annotations.SyncToClient;
import com.modularmc.gtpm.api.sync_system.rpc.RPCMethod;
import com.modularmc.gtpm.utils.RedstoneUtil;

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
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;

import lombok.Getter;

import java.util.List;

@TransVariants(keyPrefix = "cover.advanced_item_detector",
               value = {
                       @Variant(id = "invert.disabled.0",
                                source = "Output: Normal",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "输出：普通")),
                       @Variant(id = "invert.disabled.1",
                                source = "",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "")),
                       @Variant(id = "invert.disabled.2",
                                source = "Toggle to invert the redstone logic",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "切换以反转红石逻辑")),
                       @Variant(id = "invert.disabled.3",
                                source = "By default, redstone stops emitting when less than the minimum amount of items, and starts emitting when greater than the min amount of items up to the set maximum",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "默认情况下，物品数量介于所设定的最小值和最大值之间时覆盖板将发出红石信号，小于最小值时则停止发出红石信号")),
                       @Variant(id = "invert.enabled.0",
                                source = "Output: Inverted",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "输出：反相")),
                       @Variant(id = "invert.enabled.1",
                                source = "",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "")),
                       @Variant(id = "invert.enabled.2",
                                source = "Toggle to invert the redstone logic",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "切换以反转红石逻辑")),
                       @Variant(id = "invert.enabled.3",
                                source = "By default, redstone stops emitting when less than the minimum amount of items, and starts emitting when greater than the min amount of items up to the set maximum",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "默认情况下，物品数量介于所设定的最小值和最大值之间时覆盖板将发出红石信号，小于最小值时则停止发出红石信号")),
                       @Variant(id = "label",
                                source = "Advanced Item Detector",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "进阶物品探测覆盖板")),
                       @Variant(id = "max",
                                source = "Max Items",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "最大物品数量")),
                       @Variant(id = "min",
                                source = "Min Items",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "最小物品数量")),
               })
public class AdvancedItemDetectorCover extends ItemDetectorCover implements CoverUIProvider {

    private static final int DEFAULT_MIN = 64;
    private static final int DEFAULT_MAX = 512;

    @SaveField
    @SyncToClient
    @Getter
    private int minValue, maxValue;

    @SaveField
    @SyncToClient
    @Getter
    private boolean isLatched;
    @SaveField
    @SyncToClient
    @Getter
    protected final FilterHandler<ItemStack, ItemFilter> filterHandler;

    public AdvancedItemDetectorCover(CoverDefinition definition, ICoverable coverHolder, Direction attachedSide) {
        super(definition, coverHolder, attachedSide);

        this.minValue = DEFAULT_MIN;
        this.maxValue = DEFAULT_MAX;

        filterHandler = FilterHandlers.item(this);
    }

    @Override
    public List<ItemStack> getAdditionalDrops() {
        var list = super.getAdditionalDrops();
        if (!filterHandler.getFilterItem().isEmpty()) {
            list.add(filterHandler.getFilterItem());
        }
        return list;
    }

    @Override
    protected void update() {
        if (this.coverHolder.getOffsetTimer() % 20 != 0)
            return;

        ItemFilter filter = filterHandler.getFilter();
        IItemHandler handler = getItemHandler();
        if (handler == null)
            return;

        int storedItems = 0;

        for (int i = 0; i < handler.getSlots(); i++) {
            if (filter.test(handler.getStackInSlot(i)))
                storedItems += handler.getStackInSlot(i).getCount();
        }

        if (isLatched) {
            setRedstoneSignalOutput(RedstoneUtil.computeLatchedRedstoneBetweenValues(storedItems, maxValue, minValue,
                    isInverted(), redstoneSignalOutput));
        } else {
            setRedstoneSignalOutput(
                    RedstoneUtil.computeRedstoneBetweenValues(storedItems, maxValue, minValue, isInverted()));
        }
    }

    public void setMinValue(int minValue) {
        int upperBound = Math.max(0, maxValue - 1);
        int clamped = Mth.clamp(minValue, 0, upperBound);
        if (this.minValue != clamped) {
            this.minValue = clamped;
        }
    }

    public void setMaxValue(int maxValue) {
        int clamped = Math.max(maxValue, 0);
        if (this.maxValue != clamped) {
            this.maxValue = clamped;
        }
    }

    public void setLatched(boolean latched) {
        isLatched = latched;
    }

    //////////////////////////////////////
    // *********** GUI ***********//
    //////////////////////////////////////

    @Override
    public boolean canCreateUI(Player player, UICoverHolder holder) {
        return holder.getCover() == this;
    }

    @Override
    public UI createUI(Player player, UICoverHolder holder) {
        UIElement root = new UIElement();
        UITemplate.setBounds(root, 0, 0, 176, 252);
        root.style(style -> style.backgroundTexture(GuiTextures.BACKGROUND));

        root.addChild(createLabel(10, 5, 156, 10, "cover.advanced_item_detector.label"));
        root.addChild(createLabel(10, 55, 65, 10, "cover.advanced_item_detector.min"));
        root.addChild(createLabel(10, 80, 65, 10, "cover.advanced_item_detector.max"));
        root.addChild(new GTIntInputElement(80, 50, 86, 20, this::getMinValue,
                value -> setMinValue(player, holder, value)));
        root.addChild(new GTIntInputElement(80, 75, 86, 20, this::getMaxValue,
                value -> setMaxValue(player, holder, value)));
        root.addChild(new GTToggleButtonElement(9, 20, 20, 20, GuiTextures.INVERT_REDSTONE_BUTTON,
                this::isInverted, inverted -> setInverted(player, holder, inverted))
                .isMultiLang()
                .setTooltipText("cover.advanced_item_detector.invert"));
        root.addChild(new GTToggleButtonElement(31, 21, 18, 18, GuiTextures.BUTTON_LOCK,
                this::isLatched, latched -> setLatched(player, holder, latched))
                .setShouldUseBaseBackground()
                .isMultiLang()
                .setTooltipText("cover.advanced_detector.latch"));
        root.addChild(filterHandler.createFilterSlotUI(148, 100));
        root.addChild(filterHandler.createFilterConfigUI(10, 100, 156, 60));
        root.addChild(UITemplate.bindPlayerInventory(player.getInventory(), GuiTextures.SLOT, 7, 170, true));
        return UI.of(root);
    }

    private GTLabelElement createLabel(int x, int y, int width, int height, String text) {
        GTLabelElement label = new GTLabelElement(x, y, width, height, text, true);
        label.textStyle(style -> style
                .textColor(0x404040)
                .textShadow(false)
                .textAlignHorizontal(Horizontal.LEFT)
                .textAlignVertical(Vertical.CENTER));
        return label;
    }

    private void setMinValue(Player player, UICoverHolder holder, int value) {
        setMinValue(value);
        sendConfigAction(player, holder);
    }

    private void setMaxValue(Player player, UICoverHolder holder, int value) {
        setMaxValue(value);
        sendConfigAction(player, holder);
    }

    private void setLatched(Player player, UICoverHolder holder, boolean latched) {
        setLatched(latched);
        sendConfigAction(player, holder);
    }

    private void setInverted(Player player, UICoverHolder holder, boolean inverted) {
        setInverted(inverted);
        sendConfigAction(player, holder);
    }

    @RPCMethod
    public void configureAdvancedItemDetector(int min, int max, boolean latched, boolean inverted) {
        setMinValue(min);
        setMaxValue(max);
        setLatched(latched);
        setInverted(inverted);
    }

    private void sendConfigAction(Player player, UICoverHolder holder) {
        if (player.level().isClientSide()) {
            holder.getCover().rpcToServer("configureAdvancedItemDetector", getMinValue(), getMaxValue(), isLatched(),
                    isInverted());
        }
    }

    @Override
    public SyncFieldData copyConfig(HolderLookup.Provider registries) {
        return ConfigCopyHelper.withFields(super.copyConfig(registries), fields -> fields
                .put(SyncFieldData.key("min"),
                        minValue)
                .put(SyncFieldData.key("max"),
                        maxValue)
                .put(SyncFieldData.key("latched"),
                        isLatched)
                .put(SyncFieldData.key("filter"), ItemStack.OPTIONAL_CODEC, filterHandler.getFilterItem(), registries));
    }

    @Override
    public void pasteConfig(ServerPlayer player, HolderLookup.Provider registries, SyncFieldData config) {
        setMinValue(ConfigCopyHelper.getInt(config, "min"));
        setMaxValue(ConfigCopyHelper.getInt(config, "max"));
        setLatched(ConfigCopyHelper.getBoolean(config, "latched"));
        filterHandler
                .setFilterItem(ConfigCopyHelper.getItem(registries, config, "filter"));
        super.pasteConfig(player, registries, config);
    }
}
