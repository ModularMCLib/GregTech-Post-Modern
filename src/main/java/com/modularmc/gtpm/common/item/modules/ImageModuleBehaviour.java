package com.modularmc.gtpm.common.item.modules;

import com.modularmc.gtpm.api.gui.GuiTextures;
import com.modularmc.gtpm.api.gui.UITemplate;
import com.modularmc.gtpm.api.gui.element.GTButtonElement;
import com.modularmc.gtpm.api.gui.element.GTTextFieldElement;
import com.modularmc.gtpm.api.item.IComponentItem;
import com.modularmc.gtpm.api.item.component.IMonitorModuleItem;
import com.modularmc.gtpm.client.renderer.monitor.IMonitorRenderer;
import com.modularmc.gtpm.client.renderer.monitor.MonitorImageRenderer;
import com.modularmc.gtpm.common.data.GTDataComponents;
import com.modularmc.gtpm.common.machine.multiblock.electric.CentralMonitorMachine;
import com.modularmc.gtpm.common.machine.multiblock.electric.monitor.MonitorGroup;

import com.lowdragmc.lowdraglib2.gui.ui.UIElement;

import net.minecraft.world.item.ItemStack;

import java.util.UUID;
import java.util.function.Consumer;

public class ImageModuleBehaviour implements IMonitorModuleItem {

    @Override
    public IMonitorRenderer getRenderer(ItemStack stack) {
        return new MonitorImageRenderer(stack.getOrDefault(GTDataComponents.IMAGE_MODULE_URL, ""));
    }

    @Override
    public UIElement createConfigurationElement(ItemStack stack, CentralMonitorMachine machine, MonitorGroup group,
                                                Consumer<Runnable> actionSender) {
        UIElement builder = new UIElement();
        UITemplate.setBounds(builder, 0, 0, 248, 42);

        GTTextFieldElement textField = new GTTextFieldElement(0, 0, 220, 12);
        textField.setAnyString();
        textField.setText(stack.getOrDefault(GTDataComponents.IMAGE_MODULE_URL, ""), false);
        ImageEditSession editSession = ImageEditSession.open(machine, group, stack);

        GTButtonElement saveButton = new GTButtonElement(228, 0, 20, 20,
                GuiTextures.group(GuiTextures.VANILLA_BUTTON, GuiTextures.BUTTON_CHECK),
                event -> {
                    if (!machine.isRemote()) return;
                    sendUrlChange(machine, editSession, textField.getValue(), actionSender);
                });
        saveButton.noText();

        builder.addChildren(textField, saveButton);
        return builder;
    }

    private static void sendUrlChange(CentralMonitorMachine machine, ImageEditSession editSession, String requestedUrl,
                                      Consumer<Runnable> actionSender) {
        String expectedUrl = editSession.expectedModule.get(GTDataComponents.IMAGE_MODULE_URL);
        if (requestedUrl.equals(expectedUrl)) {
            return;
        }
        actionSender.accept(() -> machine.rpcToServer("setCentralMonitorImageModuleUrl",
                editSession.holderIncarnation,
                editSession.groupIdentity,
                editSession.moduleSlotIncarnation,
                editSession.expectedModule,
                requestedUrl));
    }

    private static final class ImageEditSession {

        private final UUID holderIncarnation;
        private final UUID groupIdentity;
        private final UUID moduleSlotIncarnation;
        private final ItemStack expectedModule;

        private ImageEditSession(UUID holderIncarnation, UUID groupIdentity, UUID moduleSlotIncarnation,
                                 ItemStack expectedModule) {
            this.holderIncarnation = holderIncarnation;
            this.groupIdentity = groupIdentity;
            this.moduleSlotIncarnation = moduleSlotIncarnation;
            this.expectedModule = expectedModule;
        }

        private static ImageEditSession open(CentralMonitorMachine machine, MonitorGroup group, ItemStack module) {
            return new ImageEditSession(
                    machine.getCentralMonitorActionIncarnation(),
                    group.getIdentity(),
                    group.getModuleSlotIncarnation(),
                    captureExpectedModule(module));
        }
    }

    /** Captures editable module data without the server-derived display text. */
    public static ItemStack captureExpectedModule(ItemStack module) {
        ItemStack moduleCopy = module.copy();
        moduleCopy.remove(GTDataComponents.TEXT_LINE_LIST.get());
        return moduleCopy;
    }

    /** Compares stable module data so server-derived display changes do not invalidate an editor. */
    public static boolean matchesExpectedModule(ItemStack current, ItemStack expected) {
        return ItemStack.matches(captureExpectedModule(current), expected);
    }

    public static boolean isImageModule(ItemStack stack) {
        return stack.getItem() instanceof IComponentItem componentItem &&
                componentItem.getComponents().stream().anyMatch(ImageModuleBehaviour.class::isInstance);
    }

    @Override
    public String getType() {
        return "image";
    }

    public String getUrl(ItemStack stack) {
        return stack.get(GTDataComponents.IMAGE_MODULE_URL);
    }

    public void setUrl(ItemStack stack, String url) {
        stack.set(GTDataComponents.IMAGE_MODULE_URL, url);
    }
}
