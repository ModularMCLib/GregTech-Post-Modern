package com.modularmc.gtpm.common.item.behavior;

import com.modularmc.gtpm.api.item.component.IRecipeRemainder;

import com.modularmc.registrate.localization.MinecraftLocale;
import com.modularmc.registrate.localization.annotation.Trans;
import com.modularmc.registrate.localization.annotation.TransVariants;
import com.modularmc.registrate.localization.annotation.Translation;
import com.modularmc.registrate.localization.annotation.Variant;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.fluids.FluidUtil;
import net.neoforged.neoforge.fluids.capability.IFluidHandler.FluidAction;

@Trans(key = "metaitem.generic.fluid_container.tooltip",
       source = "%d/%dL %s",
       translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "%d/%dL %s"))
@TransVariants(keyPrefix = "fluid_cell",
               value = {
                       @Variant(id = "empty",
                                source = "Empty",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "空")),
               })
public class ItemFluidContainer implements IRecipeRemainder {

    @Override
    public ItemStack getRecipeRemained(ItemStack itemStack) {
        return FluidUtil.getFluidHandler(itemStack).map(handler -> {
            var drained = handler.drain(FluidType.BUCKET_VOLUME, FluidAction.SIMULATE);
            if (drained.getAmount() != FluidType.BUCKET_VOLUME) return ItemStack.EMPTY;
            handler.drain(FluidType.BUCKET_VOLUME, FluidAction.EXECUTE);
            return ItemStack.EMPTY;
        }).orElse(itemStack);
    }
}
