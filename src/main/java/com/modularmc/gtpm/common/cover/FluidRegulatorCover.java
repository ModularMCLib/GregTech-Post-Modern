package com.modularmc.gtpm.common.cover;

import com.modularmc.gtpm.api.blockentity.ConfigCopyHelper;
import com.modularmc.gtpm.api.capability.ICoverable;
import com.modularmc.gtpm.api.cover.CoverDefinition;
import com.modularmc.gtpm.api.cover.filter.FluidFilter;
import com.modularmc.gtpm.api.cover.filter.SimpleFluidFilter;
import com.modularmc.gtpm.api.gui.element.GTEnumSelectorElement;
import com.modularmc.gtpm.api.gui.element.GTIntInputElement;
import com.modularmc.gtpm.api.gui.factory.UICoverHolder;
import com.modularmc.gtpm.api.sync_system.SyncFieldData;
import com.modularmc.gtpm.api.sync_system.annotations.SaveField;
import com.modularmc.gtpm.api.sync_system.annotations.SyncToClient;
import com.modularmc.gtpm.api.sync_system.rpc.RPCMethod;
import com.modularmc.gtpm.api.transfer.fluid.IFluidHandlerModifiable;
import com.modularmc.gtpm.common.cover.data.BucketMode;
import com.modularmc.gtpm.common.cover.data.TransferMode;

import com.modularmc.registrate.localization.MinecraftLocale;
import com.modularmc.registrate.localization.annotation.Trans;
import com.modularmc.registrate.localization.annotation.TransVariants;
import com.modularmc.registrate.localization.annotation.Translation;
import com.modularmc.registrate.localization.annotation.Variant;

import com.lowdragmc.lowdraglib2.gui.ui.UIElement;

import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.IFluidHandler.FluidAction;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Trans(key = "cover.fluid_regulator.title",
       source = "Fluid Regulator Settings (%s)",
       translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "流体校准器设置（%s）"))
@TransVariants(keyPrefix = "cover.fluid_regulator",
               value = {
                       @Variant(id = "keep_exact",
                                source = "Keep Exact: %s",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "保持供应：%s")),
                       @Variant(id = "supply_exact",
                                source = "Supply Exact: %s",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "精确供应：%s")),
                       @Variant(id = "transfer_mode.description.0",
                                source = "§eTransfer Any§r - in this mode, cover will transfer as many fluids matching its filter as possible.",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "§e任意传输§r-在此模式下，覆盖板将传输任何与过滤器匹配的流体。")),
                       @Variant(id = "transfer_mode.description.1",
                                source = "§eSupply Exact§r - in this mode, cover will supply fluids in portions specified in the window underneath this button. If amount of fluids is less than portion size, fluids won't be moved.",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "§e精确供应-在此模式下，覆盖板会将此按钮下方窗口中指定的流体按指定量打包传输。若流体量小于指定量，流体不会被传输。")),
                       @Variant(id = "transfer_mode.description.2",
                                source = "§eKeep Exact§r - in this mode, cover will keep specified amount of fluids in the destination inventory, supplying additional amount of fluids if required.",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "§e保持供应-在此模式下，覆盖板将在目标容器中保持指定数量的液体，低于保持量时传输相应量的流体。")),
                       @Variant(id = "transfer_mode.description.3",
                                source = "§7Tip: shift click will multiply increase/decrease amounts by 10 and ctrl click will multiply by 100.",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN,
                                                            text = "§7小提示：按住Shift/Crtl把增加或减少的数量乘以10/100。")),
               })
public class FluidRegulatorCover extends PumpCover {

    private static final int MAX_STACK_SIZE = 2_048_000_000; // Capacity of quantum tank IX

    @SaveField
    @SyncToClient
    @Getter
    private TransferMode transferMode = TransferMode.TRANSFER_ANY;

    @SaveField
    @SyncToClient
    @Getter
    private BucketMode transferBucketMode = BucketMode.MILLI_BUCKET;
    @SaveField
    @SyncToClient
    @Getter
    protected int globalTransferLimit;
    protected int fluidTransferBuffered = 0;

    private @Nullable GTIntInputElement transferSizeInput;
    private @Nullable GTEnumSelectorElement<BucketMode> transferBucketModeInput;

    public FluidRegulatorCover(CoverDefinition definition, ICoverable coverHolder, Direction attachedSide, int tier,
                               int maxTransferRate) {
        super(definition, coverHolder, attachedSide, tier, maxTransferRate);
    }

    public FluidRegulatorCover(CoverDefinition definition, ICoverable coverHolder, Direction attachedSide, int tier) {
        this(definition, coverHolder, attachedSide, tier, PUMP_SCALING.applyAsInt(tier));
    }

    //////////////////////////////////////
    // ***** Transfer Logic ******//
    //////////////////////////////////////

    @Override
    protected int doTransferFluidsInternal(IFluidHandlerModifiable source, IFluidHandlerModifiable destination,
                                           int platformTransferLimit) {
        return switch (transferMode) {
            case TRANSFER_ANY -> transferAny(source, destination, platformTransferLimit);
            case TRANSFER_EXACT -> transferExact(source, destination, platformTransferLimit);
            case KEEP_EXACT -> keepExact(source, destination, platformTransferLimit);
        };
    }

    private int transferExact(IFluidHandler source, IFluidHandler destination, int platformTransferLimit) {
        int fluidLeftToTransfer = platformTransferLimit;

        for (int slot = 0; slot < source.getTanks(); slot++) {
            if (fluidLeftToTransfer <= 0)
                break;

            FluidStack sourceFluid = source.getFluidInTank(slot).copy();
            int supplyAmount = getFilteredFluidAmount(sourceFluid);

            // If the remaining transferrable amount in this operation is not enough to transfer the full stack size,
            // the remaining amount for this operation will be buffered and added to the next operation's maximum.
            if (fluidLeftToTransfer + fluidTransferBuffered < supplyAmount) {
                this.fluidTransferBuffered += fluidLeftToTransfer;
                fluidLeftToTransfer = 0;
                break;
            }

            if (sourceFluid.isEmpty() || supplyAmount <= 0)
                continue;

            sourceFluid.setAmount(supplyAmount);
            FluidStack drained = source.drain(sourceFluid, FluidAction.SIMULATE);

            if (drained.isEmpty() || drained.getAmount() < supplyAmount)
                continue;

            int insertableAmount = destination.fill(drained.copy(), FluidAction.SIMULATE);
            if (insertableAmount != supplyAmount)
                continue;

            drained.setAmount(insertableAmount);
            drained = source.drain(drained, FluidAction.EXECUTE);

            if (!drained.isEmpty()) {
                destination.fill(drained, FluidAction.EXECUTE);
                fluidLeftToTransfer -= (drained.getAmount() - fluidTransferBuffered);
            }

            fluidTransferBuffered = 0;
        }

        return platformTransferLimit - fluidLeftToTransfer;
    }

    private int keepExact(IFluidHandlerModifiable source, IFluidHandlerModifiable destination,
                          int platformTransferLimit) {
        int fluidLeftToTransfer = platformTransferLimit;

        var sourceAmounts = enumerateDistinctFluids(source, TransferDirection.EXTRACT);
        var destinationAmounts = enumerateDistinctFluids(destination, TransferDirection.INSERT);

        for (FluidStack fluidStack : sourceAmounts.keySet()) {
            if (fluidLeftToTransfer <= 0) break;

            int amountToKeep = getFilteredFluidAmount(fluidStack);
            long amountInDest = destinationAmounts.getOrDefault(fluidStack, 0);
            if (amountInDest >= amountToKeep) continue;

            FluidStack fluidToMove = fluidStack.copy();
            fluidToMove.setAmount(Math.min(fluidLeftToTransfer, (int) (amountToKeep - amountInDest)));
            if (fluidToMove.getAmount() <= 0) continue;

            FluidStack drained = source.drain(fluidToMove, FluidAction.SIMULATE);
            int fillableAmount = destination.fill(drained, FluidAction.SIMULATE);
            if (fillableAmount <= 0) continue;

            fluidToMove.setAmount(Math.min(fluidToMove.getAmount(), fillableAmount));

            drained = source.drain(fluidToMove, FluidAction.EXECUTE);
            int movedAmount = destination.fill(drained, FluidAction.EXECUTE);

            fluidLeftToTransfer -= movedAmount;
        }

        return platformTransferLimit - fluidLeftToTransfer;
    }

    public void setTransferBucketMode(BucketMode transferBucketMode) {
        var oldMultiplier = this.transferBucketMode.multiplier;
        var newMultiplier = transferBucketMode.multiplier;

        if (this.transferBucketMode == transferBucketMode) {
            configureTransferBucketModeInput(oldMultiplier, newMultiplier);
            return;
        }
        this.transferBucketMode = transferBucketMode;
        syncDataHolder.markClientSyncFieldDirty("transferBucketMode");
        configureTransferBucketModeInput(oldMultiplier, newMultiplier);
    }

    public void setTransferMode(TransferMode transferMode) {
        if (this.transferMode == transferMode) {
            configureTransferSizeInput();
            return;
        }
        this.transferMode = transferMode;

        configureTransferSizeInput();

        if (!coverHolder.isRemote()) {
            syncDataHolder.markClientSyncFieldDirty("transferMode");
            configureFilter();
        }
    }

    @Override
    protected void configureFilter() {
        if (filterHandler.getFilter() instanceof SimpleFluidFilter filter) {
            filter.setMaxStackSize(transferMode == TransferMode.TRANSFER_ANY ? 1 : MAX_STACK_SIZE);
        }

        configureTransferSizeInput();
    }

    private int getFilteredFluidAmount(FluidStack fluidStack) {
        if (!filterHandler.isFilterPresent())
            return globalTransferLimit;

        FluidFilter filter = filterHandler.getFilter();
        return (filter.supportsAmounts() ? filter.testFluidAmount(fluidStack) : globalTransferLimit);
    }

    ///////////////////////////
    // ***** GUI ******//
    ///////////////////////////

    @Override
    protected @NotNull String getUITitle() {
        return "cover.fluid_regulator.title";
    }

    @Override
    protected void buildAdditionalUI(UIElement root, Player player, UICoverHolder holder) {
        root.addChild(GTEnumSelectorElement.selectable(146, 45, 20, 20, TransferMode.values(),
                this::getTransferMode, mode -> setTransferMode(player, holder, mode)));

        this.transferSizeInput = new GTIntInputElement(35, 45, 84, 20,
                this::getCurrentBucketModeTransferSize,
                value -> setCurrentBucketModeTransferSize(player, holder, value)).setMin(0)
                .setMax(Integer.MAX_VALUE);
        configureTransferSizeInput();
        root.addChild(this.transferSizeInput);

        this.transferBucketModeInput = GTEnumSelectorElement.selectable(121, 45, 20, 20, BucketMode.values(),
                this::getTransferBucketMode, mode -> setTransferBucketMode(player, holder, mode));
        root.addChild(this.transferBucketModeInput);
        configureTransferBucketModeInput(transferBucketMode.multiplier, transferBucketMode.multiplier);
        configureTransferSizeInput();
    }

    private int getCurrentBucketModeTransferSize() {
        return this.globalTransferLimit / this.transferBucketMode.multiplier;
    }

    private void setCurrentBucketModeTransferSize(int transferSize) {
        long transferLimit = (long) transferSize * this.transferBucketMode.multiplier;
        setGlobalTransferLimit((int) Math.min(transferLimit, MAX_STACK_SIZE));
    }

    public void setGlobalTransferLimit(int transferLimit) {
        int clamped = Math.min(Math.max(transferLimit, 0), MAX_STACK_SIZE);
        if (this.globalTransferLimit != clamped) {
            this.globalTransferLimit = clamped;
            syncDataHolder.markClientSyncFieldDirty("globalTransferLimit");
        }
        configureTransferSizeInput();
    }

    private void configureTransferBucketModeInput(int oldMultiplier, int newMultiplier) {
        if (transferSizeInput == null) return;

        if (oldMultiplier > newMultiplier) {
            transferSizeInput.setValue(getCurrentBucketModeTransferSize());
        }
        this.transferSizeInput.setMax(MAX_STACK_SIZE / this.transferBucketMode.multiplier);
        if (newMultiplier > oldMultiplier) {
            transferSizeInput.setValue(getCurrentBucketModeTransferSize());
        }
    }

    private void configureTransferSizeInput() {
        if (this.transferSizeInput == null || transferBucketModeInput == null)
            return;

        this.transferSizeInput.setVisible(shouldShowTransferSize());
        this.transferBucketModeInput.setVisible(shouldShowTransferSize());
    }

    private boolean shouldShowTransferSize() {
        if (this.transferMode == TransferMode.TRANSFER_ANY)
            return false;

        if (!this.filterHandler.isFilterPresent())
            return true;

        return !this.filterHandler.getFilter().supportsAmounts();
    }

    private void setTransferMode(Player player, UICoverHolder holder, TransferMode mode) {
        setTransferMode(mode);
        sendConfigAction(player, holder);
    }

    private void setTransferBucketMode(Player player, UICoverHolder holder, BucketMode mode) {
        setTransferBucketMode(mode);
        sendConfigAction(player, holder);
    }

    private void setCurrentBucketModeTransferSize(Player player, UICoverHolder holder, int transferSize) {
        setCurrentBucketModeTransferSize(transferSize);
        sendConfigAction(player, holder);
    }

    @RPCMethod
    public void configureFluidRegulator(TransferMode transferMode, int transferLimit, BucketMode transferBucketMode) {
        setTransferMode(transferMode);
        setGlobalTransferLimit(transferLimit);
        setTransferBucketMode(transferBucketMode);
    }

    private void sendConfigAction(Player player, UICoverHolder holder) {
        if (player.level().isClientSide()) {
            holder.getCover().rpcToServer("configureFluidRegulator", getTransferMode(), getGlobalTransferLimit(),
                    getTransferBucketMode());
        }
    }

    @Override
    public SyncFieldData copyConfig(HolderLookup.Provider registries) {
        return ConfigCopyHelper.withFields(super.copyConfig(registries), fields -> fields
                .put(SyncFieldData.key("transferMode"),
                        transferMode.ordinal())
                .put(SyncFieldData.key("transferLimit"),
                        globalTransferLimit)
                .put(SyncFieldData.key("transferBucket"),
                        transferBucketMode.ordinal()));
    }

    @Override
    public void pasteConfig(ServerPlayer player, HolderLookup.Provider registries, SyncFieldData config) {
        setTransferMode(TransferMode.values()[ConfigCopyHelper.getInt(config, "transferMode")]);
        setGlobalTransferLimit(ConfigCopyHelper.getInt(config, "transferLimit"));
        setTransferBucketMode(BucketMode.values()[ConfigCopyHelper.getInt(config, "transferBucket")]);
        super.pasteConfig(player, registries, config);
    }
}
