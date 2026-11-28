package com.modularmc.gtpm.common.cover.detector;

import com.modularmc.gtpm.GTPM;
import com.modularmc.gtpm.api.blockentity.ConfigCopyHelper;
import com.modularmc.gtpm.api.capability.ICoverable;
import com.modularmc.gtpm.api.capability.IEnergyInfoProvider;
import com.modularmc.gtpm.api.cover.CoverDefinition;
import com.modularmc.gtpm.api.gui.GuiTextures;
import com.modularmc.gtpm.api.gui.UITemplate;
import com.modularmc.gtpm.api.gui.element.GTLabelElement;
import com.modularmc.gtpm.api.gui.element.GTLongInputElement;
import com.modularmc.gtpm.api.gui.element.GTToggleButtonElement;
import com.modularmc.gtpm.api.gui.factory.CoverUIProvider;
import com.modularmc.gtpm.api.gui.factory.UICoverHolder;
import com.modularmc.gtpm.api.sync_system.SyncFieldData;
import com.modularmc.gtpm.api.sync_system.annotations.SaveField;
import com.modularmc.gtpm.api.sync_system.annotations.SyncToClient;
import com.modularmc.gtpm.api.sync_system.rpc.RPCMethod;
import com.modularmc.gtpm.utils.GTMath;

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

import lombok.Getter;
import org.jetbrains.annotations.Nullable;

import java.math.BigInteger;

import static com.modularmc.gtpm.utils.RedstoneUtil.computeLatchedRedstoneBetweenValues;

@TransVariants(keyPrefix = "cover.advanced_energy_detector",
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
                                source = "By default, redstone is emitted when less than the minimum EU, and stops emitting when greater than the max EU",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "默认情况下，能量小于所设定的最小 EU 时覆盖板将发出红石信号，大于最大 EU 时则停止发出红石信号")),
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
                                source = "By default, redstone is emitted when less than the minimum EU, and stops emitting when greater than the max EU",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "默认情况下，能量小于所设定的最小 EU 时覆盖板将发出红石信号，大于最大 EU 时则停止发出红石信号")),
                       @Variant(id = "use_percent.disabled.0",
                                source = "Mode: Discrete EU",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "模式：EU设定值")),
                       @Variant(id = "use_percent.disabled.1",
                                source = "",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "")),
                       @Variant(id = "use_percent.disabled.2",
                                source = "Change between using discrete EU values or percentages for comparing min/max against an attached energy storage.",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "使用给定的EU数值或百分比来比较所附着的能量存储中能量的最大/最小值。")),
                       @Variant(id = "use_percent.enabled.0",
                                source = "Mode: Percentage",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "模式： 百分比")),
                       @Variant(id = "use_percent.enabled.1",
                                source = "",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "")),
                       @Variant(id = "use_percent.enabled.2",
                                source = "Change between using discrete EU values or percentages for comparing min/max against an attached energy storage.",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "使用给定的EU数值或百分比来比较所附着的能量存储中能量的最大/最小值。")),
                       @Variant(id = "label",
                                source = "Advanced Energy Detector",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "进阶能量探测覆盖板")),
                       @Variant(id = "max",
                                source = "Max",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "最大值")),
                       @Variant(id = "min",
                                source = "Min",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "最小值")),
               })
public class AdvancedEnergyDetectorCover extends EnergyDetectorCover implements CoverUIProvider {

    private static final int DEFAULT_MIN_PERCENT = 33;
    private static final int DEFAULT_MAX_PERCENT = 66;
    private static final BigInteger LONG_MAX_VALUE = BigInteger.valueOf(Long.MAX_VALUE);

    @SaveField
    @SyncToClient
    @Getter
    public long minValue, maxValue;

    @SaveField
    @SyncToClient
    @Getter
    private boolean usePercent;

    private @Nullable GTLongInputElement minValueInput;
    private @Nullable GTLongInputElement maxValueInput;

    public AdvancedEnergyDetectorCover(CoverDefinition definition, ICoverable coverHolder, Direction attachedSide) {
        super(definition, coverHolder, attachedSide);
        this.minValue = DEFAULT_MIN_PERCENT;
        this.maxValue = DEFAULT_MAX_PERCENT;
        this.usePercent = true;
    }

    @Override
    protected void update() {
        if (coverHolder.getOffsetTimer() % 20 != 0) return;

        IEnergyInfoProvider energyInfoProvider = getEnergyInfoProvider();
        if (energyInfoProvider == null) return;

        IEnergyInfoProvider.EnergyInfo energyInfo = energyInfoProvider.getEnergyInfo();
        boolean isBigInt = energyInfoProvider.supportsBigIntEnergyValues();

        if (isBigInt) {
            if (usePercent) {
                if (energyInfo.capacity().compareTo(BigInteger.ZERO) > 0) {
                    float ratio = GTMath.ratio(energyInfo.stored(), energyInfo.capacity());
                    setRedstoneSignalOutput(computeLatchedRedstoneBetweenValues(ratio * 100, maxValue,
                            minValue, isInverted(), redstoneSignalOutput));
                } else {
                    setRedstoneSignalOutput(isInverted() ? 15 : 0);
                }
            } else {
                setRedstoneSignalOutput(computeLatchedRedstoneBetweenValues(energyInfo.stored(),
                        BigInteger.valueOf(this.maxValue), BigInteger.valueOf(this.minValue),
                        isInverted(), redstoneSignalOutput));
            }
        } else {
            if (usePercent) {
                if (energyInfo.capacity().longValue() > 0) {
                    float ratio = energyInfo.stored().floatValue() / energyInfo.capacity().floatValue();
                    setRedstoneSignalOutput(computeLatchedRedstoneBetweenValues(ratio * 100, maxValue,
                            minValue, isInverted(), redstoneSignalOutput));
                } else {
                    setRedstoneSignalOutput(isInverted() ? 15 : 0);
                }
            } else {
                setRedstoneSignalOutput(computeLatchedRedstoneBetweenValues(energyInfo.stored().longValue(),
                        this.maxValue, this.minValue,
                        isInverted(), redstoneSignalOutput));
            }
        }
    }

    public void setMinValue(long minValue) {
        long clamped = Math.max(minValue, 0L);
        if (this.minValue != clamped) {
            this.minValue = clamped;
        }
    }

    public void setMaxValue(long maxValue) {
        long clamped = Math.max(maxValue, 0L);
        if (this.maxValue != clamped) {
            this.maxValue = clamped;
        }
    }

    public void setUsePercent(boolean usePercent) {
        setUsePercent(usePercent, false);
    }

    private void setUsePercentFromUI(boolean usePercent) {
        setUsePercent(usePercent, true);
    }

    private void setUsePercent(boolean usePercent, boolean convertValues) {
        var wasPercent = this.usePercent;
        if (this.usePercent != usePercent) {
            this.usePercent = usePercent;
            if (convertValues) {
                convertMinMaxValues(wasPercent);
            }
        }

        initializeMinMaxInputs();
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
        UITemplate.setBounds(root, 0, 0, 176, 187);
        root.style(style -> style.backgroundTexture(GuiTextures.BACKGROUND));

        root.addChild(createLabel(10, 5, 156, 10, "cover.advanced_energy_detector.label"));
        root.addChild(createLabel(10, 55, 25, 10, "cover.advanced_energy_detector.min"));
        root.addChild(createLabel(10, 80, 25, 10, "cover.advanced_energy_detector.max"));
        minValueInput = new GTLongInputElement(40, 50, 126, 20, this::getMinValue,
                value -> setMinValue(player, holder, value));
        maxValueInput = new GTLongInputElement(40, 75, 126, 20, this::getMaxValue,
                value -> setMaxValue(player, holder, value));
        initializeMinMaxInputs();
        root.addChild(minValueInput);
        root.addChild(maxValueInput);
        root.addChild(new GTToggleButtonElement(9, 20, 20, 20, GuiTextures.INVERT_REDSTONE_BUTTON,
                this::isInverted, inverted -> setInverted(player, holder, inverted))
                .isMultiLang()
                .setTooltipText("cover.advanced_energy_detector.invert"));
        root.addChild(new GTToggleButtonElement(147, 20, 20, 20, GuiTextures.ENERGY_DETECTOR_COVER_MODE_BUTTON,
                this::isUsePercent, usePercent -> setUsePercent(player, holder, usePercent))
                .isMultiLang()
                .setTooltipText("cover.advanced_energy_detector.use_percent"));
        root.addChild(UITemplate.bindPlayerInventory(player.getInventory(), GuiTextures.SLOT, 7, 105, true));
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

    private void setMinValue(Player player, UICoverHolder holder, long value) {
        setMinValue(value);
        sendConfigAction(player, holder);
    }

    private void setMaxValue(Player player, UICoverHolder holder, long value) {
        setMaxValue(value);
        sendConfigAction(player, holder);
    }

    private void setUsePercent(Player player, UICoverHolder holder, boolean usePercent) {
        setUsePercentFromUI(usePercent);
        sendConfigAction(player, holder);
    }

    private void setInverted(Player player, UICoverHolder holder, boolean inverted) {
        setInverted(inverted);
        sendConfigAction(player, holder);
    }

    @RPCMethod
    public void configureAdvancedEnergyDetector(long min, long max, boolean usePercent, boolean inverted) {
        setUsePercent(usePercent);
        setMinValue(min);
        setMaxValue(max);
        setInverted(inverted);
    }

    private void sendConfigAction(Player player, UICoverHolder holder) {
        if (player.level().isClientSide()) {
            holder.getCover().rpcToServer("configureAdvancedEnergyDetector", getMinValue(), getMaxValue(),
                    isUsePercent(), isInverted());
        }
    }

    private void initializeMinMaxInputs() {
        if (minValueInput == null || maxValueInput == null)
            return;

        long max = usePercent ? 100L : getEnergyCapacity();
        minValueInput.setMin(0L).setMax(max);
        maxValueInput.setMin(0L).setMax(max);
    }

    private void convertMinMaxValues(boolean wasPercent) {
        long energyCapacity = getEnergyCapacity();
        if (usePercent && !wasPercent) {
            setMinValue(energyToPercent(minValue, energyCapacity));
            setMaxValue(energyToPercent(maxValue, energyCapacity));
        } else if (!usePercent && wasPercent) {
            setMinValue(percentToEnergy(minValue, energyCapacity));
            setMaxValue(percentToEnergy(maxValue, energyCapacity));
        }
    }

    private long getEnergyCapacity() {
        IEnergyInfoProvider energyInfoProvider = getEnergyInfoProvider();
        if (energyInfoProvider == null) {
            GTPM.LOGGER.error("Advanced energy detector cover has no energy info provider at {}.",
                    coverHolder.getBlockPos());
            throw new IllegalStateException("Advanced energy detector cover has no energy info provider.");
        }

        BigInteger energyCapacity = energyInfoProvider.getEnergyInfo().capacity();
        if (energyCapacity.compareTo(LONG_MAX_VALUE) > 0) {
            return Long.MAX_VALUE;
        }
        return energyCapacity.longValue();
    }

    private static long energyToPercent(long value, long energyCapacity) {
        if (energyCapacity <= 0) {
            return 0;
        }
        return GTMath.clamp((long) (((double) value / energyCapacity) * 100), 0, 100);
    }

    private static long percentToEnergy(long value, long energyCapacity) {
        if (energyCapacity <= 0) {
            return 0;
        }
        return GTMath.clamp((long) Math.ceil((value / 100.0) * energyCapacity), 0, energyCapacity);
    }

    @Override
    public SyncFieldData copyConfig(HolderLookup.Provider registries) {
        return ConfigCopyHelper.withFields(super.copyConfig(registries), fields -> fields
                .put(SyncFieldData.key("min"),
                        minValue)
                .put(SyncFieldData.key("max"),
                        maxValue)
                .put(SyncFieldData.key("percent"),
                        usePercent));
    }

    @Override
    public void pasteConfig(ServerPlayer player, HolderLookup.Provider registries, SyncFieldData config) {
        setMinValue(ConfigCopyHelper.getLong(config, "min"));
        setMaxValue(ConfigCopyHelper.getLong(config, "max"));
        setUsePercent(ConfigCopyHelper.getBoolean(config, "percent"));
        super.pasteConfig(player, registries, config);
    }
}
