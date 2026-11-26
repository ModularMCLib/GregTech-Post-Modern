package com.modularmc.gtpm.api.machine.feature;

import com.modularmc.gtpm.GTPM;
import com.modularmc.gtpm.api.capability.IControllable;
import com.modularmc.gtpm.api.gui.GuiTextures;
import com.modularmc.gtpm.api.gui.UITemplate;
import com.modularmc.gtpm.api.gui.element.GTImageElement;
import com.modularmc.gtpm.api.gui.factory.MachineUIHolder;
import com.modularmc.gtpm.api.gui.fancy.ConfiguratorPanel;
import com.modularmc.gtpm.api.gui.fancy.FancyMachineUIWidget;
import com.modularmc.gtpm.api.gui.fancy.IFancyTooltip;
import com.modularmc.gtpm.api.gui.fancy.IFancyUIProvider;
import com.modularmc.gtpm.api.gui.fancy.TabsWidget;
import com.modularmc.gtpm.api.gui.fancy.TooltipsPanel;
import com.modularmc.gtpm.api.gui.texture.IGuiTexture;
import com.modularmc.gtpm.api.machine.fancyconfigurator.AutoOutputConfigurator;
import com.modularmc.gtpm.api.machine.fancyconfigurator.BatchModeConfigurator;
import com.modularmc.gtpm.api.machine.fancyconfigurator.CircuitFancyConfigurator;
import com.modularmc.gtpm.api.machine.fancyconfigurator.CombinedDirectionalFancyConfigurator;
import com.modularmc.gtpm.api.machine.fancyconfigurator.DistinctPartConfigurator;
import com.modularmc.gtpm.api.machine.fancyconfigurator.MachineModeFancyConfigurator;
import com.modularmc.gtpm.api.machine.fancyconfigurator.StructureCheckConfigurator;
import com.modularmc.gtpm.api.machine.fancyconfigurator.VoidingModeConfigurator;
import com.modularmc.gtpm.api.machine.fancyconfigurator.WorkingEnabledConfigurator;
import com.modularmc.gtpm.api.machine.feature.multiblock.BatchModeMachine;
import com.modularmc.gtpm.api.machine.feature.multiblock.DistinctPart;
import com.modularmc.gtpm.api.machine.multiblock.MultiblockControllerMachine;
import com.modularmc.gtpm.client.gui.fancy.PreviewScene;
import com.modularmc.gtpm.common.machine.trait.AutoOutputTrait;

import com.modularmc.registrate.util.RegistrateDistExecutor;

import com.lowdragmc.lowdraglib2.gui.ui.UI;
import com.lowdragmc.lowdraglib2.gui.ui.UIElement;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;

import java.util.List;

/**
 * Bridges machine-owned Fancy pages into the LDLib2 machine UI opening path.
 *
 * <p>
 * This interface defines the machine-owned page contract used by the LDLib2 Fancy shell.
 */
public interface IFancyUIMachine extends IUIMachine, IFancyUIProvider {

    /**
     * Builds the machine's concrete LDLib2 Fancy page body.
     *
     * <p>
     * Implementations provide the real page element explicitly so the migration does not install a placeholder or
     * silently preserve the old {@code SceneWidget} preview path.
     */
    @Override
    default UIElement createUIWidget(FancyMachineUIWidget widget) {
        var editableUI = self().getDefinition().getEditableUI();
        if (editableUI != null) return editableUI.createUI(getUIResourceManager(), self()).rootElement;

        UIElement root = UITemplate.setBounds(new UIElement(), 0, 0,
                100, 100);
        if (self().isRemote()) {
            root.addChild(new GTImageElement(26, 60, 48, 16, GuiTextures.SCENE));
            RegistrateDistExecutor.unsafeRunWhenOn(Dist.CLIENT,
                    () -> () -> root.addChild(PreviewScene.createScene(
                            self(), 100, 100)));
        }
        return root;
    }

    /**
     * Accepts the opened machine holder only while it still resolves to this machine instance.
     */
    @Override
    default int getPageWidth() {
        var editableUI = self().getDefinition().getEditableUI();
        return editableUI == null ? 100 : editableUI.getSize(getUIResourceManager()).width();
    }

    /**
     * Wraps this machine page provider in the shared LDLib2 Fancy shell.
     */
    @Override
    default int getPageHeight() {
        var editableUI = self().getDefinition().getEditableUI();
        return editableUI == null ? 100 : editableUI.getSize(getUIResourceManager()).height();
    }

    /**
     * Creates side pages that must capture the player and validated holder for this particular menu opening.
     */
    @Override
    default UI createUI(Player player, MachineUIHolder holder) {
        return UI.of(new FancyMachineUIWidget(this, player.getInventory(), holder, getPageWidth(), getPageHeight()));
    }

    default ResourceManager getUIResourceManager() {
        return self().isRemote() ? Minecraft.getInstance().getResourceManager() :
                self().getLevel().getServer().getResourceManager();
    }

    /**
     * Uses the machine definition item as the title bar and tab icon.
     */
    @Override
    default IGuiTexture getTabIcon() {
        return GuiTextures.itemStack(self().getDefinition().getItem());
    }

    /**
     * Registers local LDLib2 side tabs for machine metadata pages.
     */
    @Override
    default void attachSideTabs(TabsWidget tabs) {
        if (this instanceof IRecipeLogicMachine recipeLogicMachine &&
                recipeLogicMachine.getRecipeTypes().length > 1) {
            tabs.attachCachedSubTab(GTPM.id("machine_mode"),
                    () -> new MachineModeFancyConfigurator(recipeLogicMachine));
        }
        tabs.attachCachedSubTab(GTPM.id("directional"),
                () -> new CombinedDirectionalFancyConfigurator(self(), tabs.getPlayer()));
    }

    /**
     * Registers common LDLib2 machine configurator buttons.
     */
    @Override
    default void attachConfigurators(ConfiguratorPanel configuratorPanel) {
        if (this instanceof IVoidable voidable) {
            VoidingModeConfigurator.attachConfigurators(configuratorPanel, voidable);
        }
        if (this instanceof BatchModeMachine batchModeMachine) {
            BatchModeConfigurator.attachConfigurators(configuratorPanel, batchModeMachine);
        }
        AutoOutputTrait autoOutputTrait = self().getTrait(AutoOutputTrait.TYPE);
        if (autoOutputTrait != null) {
            AutoOutputConfigurator.attachConfigurators(configuratorPanel, autoOutputTrait);
        }
        if (this instanceof IHasCircuitSlot circuitHolder && circuitHolder.isCircuitSlotEnabled()) {
            configuratorPanel.attachConfigurators(new CircuitFancyConfigurator(circuitHolder));
        }
        if (this instanceof DistinctPart distinctPart) {
            DistinctPartConfigurator.attachConfigurators(configuratorPanel, distinctPart);
        }
        if (this instanceof MultiblockControllerMachine controller) {
            configuratorPanel.attachConfigurators(new StructureCheckConfigurator(controller));
        }
        if (this instanceof IControllable controllable) {
            configuratorPanel.attachConfigurators(new WorkingEnabledConfigurator(controllable));
        }
    }

    /**
     * Registers the machine tooltip and all trait-provided Fancy tooltips with the LDLib2 tooltip panel.
     */
    @Override
    default void attachTooltips(TooltipsPanel tooltipsPanel) {
        tooltipsPanel.attachTooltips(self());
        self().getTraitHolder().getAllTraits().stream()
                .filter(IFancyTooltip.class::isInstance)
                .map(IFancyTooltip.class::cast)
                .forEach(tooltipsPanel::attachTooltips);
    }

    /**
     * Shows the machine's translated definition name when hovering its tab.
     */
    @Override
    default List<Component> getTabTooltips() {
        return List.of(Component.translatable(self().getDefinition().getDescriptionId()));
    }

    /**
     * Uses the machine's translated definition name as the Fancy title.
     */
    @Override
    default Component getTitle() {
        return Component.translatable(self().getDefinition().getDescriptionId());
    }
}
