package com.modularmclib.gtpm.common.machine.multiblock.part;

import com.modularmclib.gtpm.api.GTValues;
import com.modularmclib.gtpm.api.blockentity.BlockEntityCreationInfo;
import com.modularmclib.gtpm.api.gui.UITemplate;
import com.modularmclib.gtpm.api.gui.element.GTIntInputElement;
import com.modularmclib.gtpm.api.gui.fancy.FancyMachineUIWidget;
import com.modularmclib.gtpm.api.machine.feature.IRecipeLogicMachine;
import com.modularmclib.gtpm.api.machine.multiblock.MultiblockControllerMachine;
import com.modularmclib.gtpm.api.machine.multiblock.part.TieredPartMachine;
import com.modularmclib.gtpm.api.sync_system.annotations.SaveField;
import com.modularmclib.gtpm.api.sync_system.annotations.ServerFieldChangeListener;
import com.modularmclib.gtpm.api.sync_system.annotations.SyncBoth;

import com.lowdragmc.lowdraglib2.gui.ui.UIElement;

import net.minecraft.util.Mth;

import lombok.Getter;

public class ParallelHatchPartMachine extends TieredPartMachine {

    private static final int MIN_PARALLEL = 1;
    private static final int PAGE_WIDTH = 100;
    private static final int PAGE_HEIGHT = 20;

    private final int maxParallel;

    @SaveField
    @SyncBoth
    @Getter
    private int currentParallel = 1;

    public ParallelHatchPartMachine(BlockEntityCreationInfo info, int tier) {
        super(info, tier);
        this.maxParallel = (int) Math.pow(4, tier - GTValues.EV);
        this.currentParallel = maxParallel;
    }

    public void setCurrentParallel(int parallelAmount) {
        parallelAmount = Mth.clamp(parallelAmount, MIN_PARALLEL, this.maxParallel);
        if (this.currentParallel == parallelAmount) {
            return;
        }
        this.currentParallel = parallelAmount;
        markControllerRecipesDirty();
    }

    @ServerFieldChangeListener(fieldName = "currentParallel")
    protected void markControllerRecipesDirty() {
        for (MultiblockControllerMachine controller : this.getControllers()) {
            if (controller instanceof IRecipeLogicMachine rlm) {
                rlm.getRecipeLogic().markLastRecipeDirty();
            }
        }
    }

    @Override
    public UIElement createUIWidget(FancyMachineUIWidget widget) {
        UIElement root = UITemplate.setBounds(new UIElement(), 0, 0, PAGE_WIDTH, PAGE_HEIGHT);
        root.addChild(new GTIntInputElement(0, 0, PAGE_WIDTH, PAGE_HEIGHT, this::getCurrentParallel,
                value -> currentParallel = value)
                .setMin(MIN_PARALLEL)
                .setMax(maxParallel));
        return root;
    }

    @Override
    public boolean canShared(MultiblockControllerMachine controller, String structureName) {
        return false;
    }

    @Override
    public int getPageWidth() {
        return PAGE_WIDTH;
    }

    @Override
    public int getPageHeight() {
        return PAGE_HEIGHT;
    }
}
