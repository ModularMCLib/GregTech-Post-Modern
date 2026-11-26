package com.modularmc.gtpm.core.mixins.registrate;

import com.modularmc.registrate.providers.ProviderType;
import com.modularmc.registrate.providers.RegistrateDataProvider;

import com.google.common.collect.BiMap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = RegistrateDataProvider.class, remap = false)
public interface RegistrateDataProviderAccessor {

    @Accessor("TYPES")
    static BiMap<String, ProviderType<?>> gtceu$getTypes() {
        throw new AssertionError();
    }
}
