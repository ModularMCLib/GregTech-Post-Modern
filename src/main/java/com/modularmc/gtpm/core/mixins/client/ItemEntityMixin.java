package com.modularmc.gtpm.core.mixins.client;

import com.modularmc.gtpm.api.GTValues;
import com.modularmc.gtpm.api.data.chemical.ChemicalHelper;
import com.modularmc.gtpm.api.data.tag.TagPrefix;
import com.modularmc.gtpm.common.data.GTSoundEntries;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEntity.class)
public class ItemEntityMixin {

    @Unique
    private boolean gtceu$wasOnGround = ((Entity) (Object) this).onGround();

    @Inject(method = "tick", at = @At(value = "HEAD"))
    public void beforeTick(CallbackInfo ci) {
        ItemEntity entity = (ItemEntity) (Object) this;
        if (GTValues.FOOLS.getAsBoolean() && !gtceu$wasOnGround && entity.onGround() &&
                ChemicalHelper.getPrefix(entity.getItem().getItem()) == TagPrefix.rodLong) {
            GTSoundEntries.METAL_PIPE.playFrom(entity, 10, 1);
        }
        gtceu$wasOnGround = entity.onGround();
    }
}
