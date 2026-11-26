package com.modularmclib.gtpm.common.machine.multiblock.part;

import com.modularmclib.gtpm.GTPM;
import com.modularmclib.gtpm.api.GTValues;
import com.modularmclib.gtpm.api.blockentity.BlockEntityCreationInfo;
import com.modularmclib.gtpm.api.capability.IDataAccessMachine;
import com.modularmclib.gtpm.api.capability.IMonitorComponent;
import com.modularmclib.gtpm.api.capability.recipe.IO;
import com.modularmclib.gtpm.api.gui.GuiTextures;
import com.modularmclib.gtpm.api.gui.UITemplate;
import com.modularmclib.gtpm.api.gui.element.GTItemSlotElement;
import com.modularmclib.gtpm.api.gui.fancy.FancyMachineUIWidget;
import com.modularmclib.gtpm.api.gui.texture.IGuiTexture;
import com.modularmclib.gtpm.api.machine.feature.IDataInfoProvider;
import com.modularmclib.gtpm.api.machine.feature.IRecipeLogicMachine;
import com.modularmclib.gtpm.api.machine.multiblock.MultiblockControllerMachine;
import com.modularmclib.gtpm.api.machine.multiblock.part.TieredPartMachine;
import com.modularmclib.gtpm.api.machine.trait.NotifiableItemStackHandler;
import com.modularmclib.gtpm.api.recipe.GTRecipeDefinition;
import com.modularmclib.gtpm.api.recipe.GTRecipeType;
import com.modularmclib.gtpm.api.sync_system.annotations.SaveField;
import com.modularmclib.gtpm.common.data.GTDataComponents;
import com.modularmclib.gtpm.common.item.behavior.PortableScannerBehavior;
import com.modularmclib.gtpm.common.machine.multiblock.electric.research.DataBankMachine;
import com.modularmclib.gtpm.utils.ResearchManager;

import com.lowdragmc.lowdraglib2.gui.ui.UIElement;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;

import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class DataAccessHatchMachine extends TieredPartMachine
                                    implements IDataAccessMachine, IDataInfoProvider, IMonitorComponent {

    private final Set<GTRecipeDefinition> recipeDefinitions;
    @Getter
    private final boolean isCreative;
    @SaveField
    public final NotifiableItemStackHandler importItems;

    public DataAccessHatchMachine(BlockEntityCreationInfo info, int tier, boolean isCreative) {
        super(info, tier);
        this.isCreative = isCreative;
        this.recipeDefinitions = isCreative ? Collections.emptySet() : new ObjectOpenHashSet<>();
        this.importItems = attachTrait(createImportItemHandler());
    }

    protected NotifiableItemStackHandler createImportItemHandler() {
        if (isCreative) return new NotifiableItemStackHandler(0, IO.BOTH);
        return new NotifiableItemStackHandler(getInventorySize(), IO.BOTH) {

            @Override
            public void onContentsChanged() {
                super.onContentsChanged();
                rebuildData(isFormed() && getControllers().first() instanceof DataBankMachine);
            }

            @Override
            public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
                if (acceptsDataItem(stack)) {
                    return super.insertItem(slot, stack, simulate);
                }
                return stack;
            }

            @Override
            public void setStackInSlot(int index, ItemStack stack) {
                if (!stack.isEmpty() && !acceptsDataItem(stack)) {
                    throw new IllegalArgumentException("Data access hatch received an invalid data item: " + stack);
                }
                super.setStackInSlot(index, stack);
            }
        };
    }

    private boolean acceptsDataItem(ItemStack stack) {
        boolean isDataBank = isFormed() && getControllers().first() instanceof DataBankMachine;
        return ResearchManager.isStackDataItem(stack, isDataBank) &&
                (stack.has(GTDataComponents.RESEARCH_ITEM) || stack.has(GTDataComponents.MONITOR_TARGET));
    }

    @Override
    public UIElement createUIWidget(FancyMachineUIWidget widget) {
        int rowSize = (int) Math.sqrt(getInventorySize());
        int pageSize = rowSize * 18;
        UIElement root = UITemplate.setBounds(new UIElement(), 0, 0, pageSize, pageSize);
        for (int y = 0; y < rowSize; y++) {
            for (int x = 0; x < rowSize; x++) {
                int index = y * rowSize + x;
                root.addChild(createDataSlot(index, x * 18, y * 18));
            }
        }
        return root;
    }

    private GTItemSlotElement createDataSlot(int index, int x, int y) {
        GTItemSlotElement slot = new GTItemSlotElement(importItems, index)
                .setBackgroundTexture(GuiTextures.SLOT)
                .setCanPutItems(true)
                .setCanTakeItems(true);
        UITemplate.setBounds(slot, x, y, 18, 18);
        return slot;
    }

    protected int getInventorySize() {
        return switch (getTier()) {
            case GTValues.LuV -> 16;
            case GTValues.EV -> 9;
            case GTValues.HV -> 4;
            default -> 1;
        };
    }

    private void rebuildData(boolean isDataBank) {
        if (isCreative || getLevel() == null || getLevel().isClientSide) return;
        recipeDefinitions.clear();
        for (int i = 0; i < this.importItems.getSlots(); i++) {
            ItemStack stack = this.importItems.getStackInSlot(i);
            ResearchManager.ResearchItem researchData = stack.get(GTDataComponents.RESEARCH_ITEM);
            boolean isValid = ResearchManager.isStackDataItem(stack, isDataBank);
            if (researchData != null && isValid) {
                Collection<GTRecipeDefinition> collection = researchData.recipeType()
                        .getDataStickEntry(researchData.researchId());
                if (collection != null) {
                    recipeDefinitions.addAll(collection);
                }
            }
        }
        notifyDataAccessControllers();
    }

    @Override
    public boolean isRecipeAvailable(@NotNull GTRecipeType recipeType, @NotNull ResourceLocation recipeId) {
        return isCreative ||
                recipeDefinitions.stream().anyMatch(definition -> isSameRecipe(definition, recipeType, recipeId));
    }

    private static boolean isSameRecipe(GTRecipeDefinition definition, GTRecipeType recipeType,
                                        ResourceLocation recipeId) {
        return definition.recipeType == recipeType &&
                definition.getId() != null &&
                definition.getId().equals(recipeId);
    }

    @Override
    public List<Component> getDataInfo(PortableScannerBehavior.DisplayMode mode) {
        if (mode == PortableScannerBehavior.DisplayMode.SHOW_ALL ||
                mode == PortableScannerBehavior.DisplayMode.SHOW_RECIPE_INFO) {
            if (recipeDefinitions.isEmpty())
                return Collections.emptyList();
            List<Component> list = new ArrayList<>();

            list.add(Component.translatable("behavior.data_item.title"));
            list.add(Component.empty());
            ResearchManager.addResearchOutputNames(recipeDefinitions, list::add);
            return list;
        }
        return new ArrayList<>();
    }

    @Override
    public boolean canShared(MultiblockControllerMachine controller, String structureName) {
        return isCreative;
    }

    @Override
    public void addedToController(MultiblockControllerMachine controller, String structureName) {
        rebuildData(controller instanceof DataBankMachine);
        super.addedToController(controller, structureName);
    }

    @Override
    public IGuiTexture getComponentIcon() {
        return GuiTextures.resource(GTPM.id("textures/item/data_module.png")).getSubTexture(0, 0, 1, 1 / 13f);
    }

    @Override
    public IItemHandler getDataItems() {
        return importItems.storage;
    }

    private void notifyDataAccessControllers() {
        if (!isFormed()) return;
        for (MultiblockControllerMachine controller : getControllers()) {
            if (controller instanceof IDataAccessMachine dataAccessMachine) {
                dataAccessMachine.notifyListeners();
            } else if (controller instanceof IRecipeLogicMachine recipeLogicMachine) {
                recipeLogicMachine.getRecipeLogic().onRecipeHandlerChanged();
            }
        }
    }

    @Override
    public int getPageWidth() {
        return (int) Math.sqrt(getInventorySize()) * 18;
    }

    @Override
    public int getPageHeight() {
        return getPageWidth();
    }
}
