package com.modularmc.gtpm.api.machine;

import com.modularmc.gtpm.api.GTValues;
import com.modularmc.gtpm.api.blockentity.BlockEntityCreationInfo;
import com.modularmc.gtpm.api.capability.GTCapabilityHelper;
import com.modularmc.gtpm.api.capability.recipe.IO;
import com.modularmc.gtpm.api.gui.GuiTextures;
import com.modularmc.gtpm.api.gui.UITemplate;
import com.modularmc.gtpm.api.gui.editor.EditableMachineUI;
import com.modularmc.gtpm.api.gui.editor.MachineUIXmlTemplates;
import com.modularmc.gtpm.api.gui.element.GTItemSlotElement;
import com.modularmc.gtpm.api.gui.fancy.ConfiguratorPanel;
import com.modularmc.gtpm.api.machine.fancyconfigurator.AutoOutputConfigurator;
import com.modularmc.gtpm.api.machine.fancyconfigurator.CircuitFancyConfigurator;
import com.modularmc.gtpm.api.machine.fancyconfigurator.WorkingEnabledConfigurator;
import com.modularmc.gtpm.api.machine.feature.IFancyUIMachine;
import com.modularmc.gtpm.api.machine.feature.IHasCircuitSlot;
import com.modularmc.gtpm.api.machine.trait.NotifiableItemStackHandler;
import com.modularmc.gtpm.api.recipe.GTRecipeType;
import com.modularmc.gtpm.api.sync_system.annotations.SaveField;
import com.modularmc.gtpm.api.sync_system.annotations.SyncToClient;
import com.modularmc.gtpm.api.transfer.item.CustomItemStackHandler;
import com.modularmc.gtpm.common.item.behavior.IntCircuitBehaviour;
import com.modularmc.gtpm.common.machine.trait.AutoOutputTrait;
import com.modularmc.gtpm.config.GTPMConfigHolder;
import com.modularmc.gtpm.data.lang.LangHandler;
import com.modularmc.gtpm.utils.ISubscription;

import net.minecraft.Util;
import net.minecraft.resources.ResourceLocation;

import it.unimi.dsi.fastutil.ints.Int2IntFunction;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.BiFunction;

/**
 * All simple single machines are implemented here.
 */
public class SimpleTieredMachine extends WorkableTieredMachine
                                 implements IFancyUIMachine, IHasCircuitSlot {

    @Getter
    @SaveField
    protected final CustomItemStackHandler chargerInventory;
    @Getter
    @SaveField
    protected final NotifiableItemStackHandler circuitInventory;
    @Nullable
    protected TickableSubscription batterySubs;
    @Nullable
    protected ISubscription energySubs;
    @SaveField
    @SyncToClient
    public final AutoOutputTrait autoOutput;

    public SimpleTieredMachine(BlockEntityCreationInfo info, int tier, Int2IntFunction tankScalingFunction) {
        super(info, tier, tankScalingFunction);

        this.autoOutput = attachTrait(new AutoOutputTrait(List.of(exportItems), List.of(exportFluids)));

        this.chargerInventory = new CustomItemStackHandler() {

            public int getSlotLimit(int slot) {
                return 1;
            }
        };
        chargerInventory.setFilter(item -> GTCapabilityHelper.getElectricItem(item) != null ||
                (GTPMConfigHolder.INSTANCE.compat.energy.nativeEUToFE &&
                        GTCapabilityHelper.getForgeEnergyItem(item) != null));

        this.circuitInventory = attachTrait(new NotifiableItemStackHandler(1, IO.IN, IO.NONE)
                .shouldDropInventoryInWorld(!GTPMConfigHolder.INSTANCE.machines.ghostCircuit)
                .setFilter(IntCircuitBehaviour::isIntegratedCircuit));
    }

    //////////////////////////////////////
    // ***** Initialization ******//
    //////////////////////////////////////

    @Override
    public void onLoad() {
        super.onLoad();
        if (!isRemote()) {
            updateBatterySubscription();
            energySubs = energyContainer.addChangedListener(this::updateBatterySubscription);
            chargerInventory.setOnContentsChanged(this::updateBatterySubscription);
        }
    }

    @Override
    public void onUnload() {
        super.onUnload();
        if (energySubs != null) {
            energySubs.unsubscribe();
            energySubs = null;
        }
    }

    protected void updateBatterySubscription() {
        if (energyContainer.dischargeOrRechargeEnergyContainers(chargerInventory, 0, true)) {
            batterySubs = subscribeServerTick(batterySubs, this::chargeBattery);
        } else if (batterySubs != null) {
            batterySubs.unsubscribe();
            batterySubs = null;
        }
    }

    protected void chargeBattery() {
        if (!energyContainer.dischargeOrRechargeEnergyContainers(chargerInventory, 0, false)) {
            updateBatterySubscription();
        }
    }

    //////////////////////////////////////
    // ********** MISC ***********//
    //////////////////////////////////////
    @Override
    public void onMachineDestroyed() {
        super.onMachineDestroyed();
        chargerInventory.dropInventoryInWorld(getLevel(), getBlockPos());
    }

    /// //////////////////////////////////
    // ****** RECIPE LOGIC *******//

    /// //////////////////////////////////

    @Override
    public long getDisplayRecipeVoltage() {
        return GTValues.V[this.tier];
    }

    //////////////////////////////////////
    // *********** GUI ***********//
    //////////////////////////////////////

    @Override
    public void attachConfigurators(ConfiguratorPanel configuratorPanel) {
        configuratorPanel.attachConfigurators(new WorkingEnabledConfigurator(
                this));
        AutoOutputConfigurator.attachConfigurators(configuratorPanel, autoOutput);
        if (isCircuitSlotEnabled()) {
            configuratorPanel.attachConfigurators(new CircuitFancyConfigurator(
                    this));
        }
    }

    @SuppressWarnings("UnstableApiUsage")
    public static BiFunction<ResourceLocation, GTRecipeType, EditableMachineUI> EDITABLE_UI_CREATOR = Util
            .memoize((path, recipeType) -> new EditableMachineUI("simple", path,
                    () -> MachineUIXmlTemplates.createSimpleMachineXml(
                            recipeType.getRecipeUI().createTemplateDocument()),
                    (root, machine) -> {
                        if (!(machine instanceof SimpleTieredMachine tieredMachine)) {
                            throw new IllegalArgumentException("Simple machine XML cannot bind " +
                                    machine.getClass().getName());
                        }
                        tieredMachine.bindRecipeElements(root);
                        tieredMachine.bindBatterySlot(MachineUIXmlTemplates.requireElement(root,
                                MachineUIXmlTemplates.BATTERY_SLOT_ID, GTItemSlotElement.class));
                    }));

    /**
     * Binds a parsed LDLib2 battery slot to this machine's charger inventory.
     */
    protected GTItemSlotElement bindBatterySlot(GTItemSlotElement slot) {
        return slot.bind(chargerInventory, 0)
                .setCanPutItems(true)
                .setCanTakeItems(true)
                .setOnAddedTooltips((slotElement, tooltips) -> tooltips.addAll(
                        LangHandler.getMultiLang("gtpm.gui.charger_slot.tooltip",
                                GTValues.VNF[tier], GTValues.VNF[tier])));
    }

    /**
     * Create an LDLib2 battery slot element.
     */
    protected GTItemSlotElement createBatterySlot() {
        var slot = bindBatterySlot(new GTItemSlotElement())
                .setBackgroundTexture(GuiTextures.group(GuiTextures.SLOT, GuiTextures.CHARGER_OVERLAY));
        return UITemplate.setBounds(slot, 0, 0, 18, 18);
    }
}
