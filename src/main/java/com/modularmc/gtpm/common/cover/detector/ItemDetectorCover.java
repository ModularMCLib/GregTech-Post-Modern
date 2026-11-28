package com.modularmc.gtpm.common.cover.detector;

import com.modularmc.gtpm.api.capability.ICoverable;
import com.modularmc.gtpm.api.cover.CoverDefinition;
import com.modularmc.gtpm.data.recipe.CustomTags;
import com.modularmc.gtpm.utils.GTTransferUtils;
import com.modularmc.gtpm.utils.RedstoneUtil;

import com.modularmc.registrate.localization.MinecraftLocale;
import com.modularmc.registrate.localization.annotation.TransVariants;
import com.modularmc.registrate.localization.annotation.Translation;
import com.modularmc.registrate.localization.annotation.Variant;

import net.minecraft.core.Direction;
import net.neoforged.neoforge.items.IItemHandler;

import org.jetbrains.annotations.Nullable;

@TransVariants(keyPrefix = "gtpm.cover.item_detector",
               value = {
                       @Variant(id = "message_item_storage_inverted",
                                source = "Monitoring Inverted Item Storage",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "正在以反相模式监控物品存储")),
                       @Variant(id = "message_item_storage_normal",
                                source = "Monitoring Normal Item Storage",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "正在以普通模式监控物品存储")),
               })
public class ItemDetectorCover extends DetectorCover {

    public ItemDetectorCover(CoverDefinition definition, ICoverable coverHolder, Direction attachedSide) {
        super(definition, coverHolder, attachedSide);
    }

    @Override
    public boolean canAttach() {
        return super.canAttach() && getItemHandler() != null;
    }

    @Override
    protected void update() {
        if (this.coverHolder.getOffsetTimer() % 20 != 0)
            return;

        IItemHandler handler = getItemHandler();
        if (handler == null)
            return;

        int storedItems = 0;
        int itemCapacity = handler.getSlots() * handler.getSlotLimit(0);

        if (itemCapacity == 0)
            return;

        for (int i = 0; i < handler.getSlots(); i++) {
            if (handler.getStackInSlot(i).is(CustomTags.SKIP_ITEM_DETECTOR)) continue;
            storedItems += handler.getStackInSlot(i).getCount();
        }

        setRedstoneSignalOutput(RedstoneUtil.computeRedstoneValue(storedItems, itemCapacity, isInverted()));
    }

    @Nullable
    protected IItemHandler getItemHandler() {
        return GTTransferUtils.getItemHandler(coverHolder.getLevel(), coverHolder.getBlockPos(), attachedSide)
                .orElse(null);
    }
}
