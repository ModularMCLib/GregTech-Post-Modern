package com.modularmc.gtpm.core.mixins;

import com.modularmc.gtpm.api.capability.GTCapabilityHelper;
import com.modularmc.gtpm.api.capability.IMedicalConditionTracker;
import com.modularmc.gtpm.config.GTPMConfigHolder;
import com.modularmc.gtpm.core.IFireImmuneEntity;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class EntityMixin implements IFireImmuneEntity {

    @Shadow
    public abstract EntityType<?> getType();

    @Unique
    private boolean gtceu$fireImmune = false;
    @Unique
    private boolean gtceu$isEntityInit = false;

    @ModifyReturnValue(method = "fireImmune", at = @At("RETURN"))
    private boolean gtceu$changeFireImmune(boolean original) {
        return original || gtceu$fireImmune;
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void gtceu$onEntityInit(EntityType<?> entityType, Level level, CallbackInfo ci) {
        gtceu$isEntityInit = true;
    }

    public void gtceu$setFireImmune(boolean isImmune) {
        this.gtceu$fireImmune = isImmune;
    }

    @ModifyReturnValue(method = "getMaxAirSupply", at = @At("RETURN"))
    private int gtceu$hazardModifyMaxAir(int original) {
        if (!gtceu$isEntityInit) return original;
        if (!GTPMConfigHolder.INSTANCE.gameplay.hazardsEnabled) return original;

        if (((Entity) (Object) this) instanceof Player player) {
            IMedicalConditionTracker tracker = GTCapabilityHelper.getMedicalConditionTracker(player);
            if (tracker.getMaxAirSupply() != -1) {
                return tracker.getMaxAirSupply();
            }
        }
        return original;
    }
}
