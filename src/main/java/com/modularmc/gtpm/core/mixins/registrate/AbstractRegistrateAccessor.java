package com.modularmc.gtpm.core.mixins.registrate;

import com.modularmc.registrate.AbstractRegistrate;
import com.modularmc.registrate.providers.ProviderType;
import com.modularmc.registrate.providers.RegistrateProvider;
import com.modularmc.registrate.util.nullness.NonNullConsumer;
import com.modularmc.registrate.util.nullness.NonNullSupplier;

import com.google.common.collect.ListMultimap;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = AbstractRegistrate.class, remap = false)
public interface AbstractRegistrateAccessor {

    @Accessor
    ListMultimap<ProviderType<?>, @NotNull NonNullConsumer<? extends RegistrateProvider>> getDatagens();

    @Accessor
    NonNullSupplier<Boolean> getDoDatagen();
}
