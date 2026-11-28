package com.modularmc.gtpm.common.data;

import com.modularmc.gtpm.common.valueprovider.AddedFloat;
import com.modularmc.gtpm.common.valueprovider.CastedFloat;
import com.modularmc.gtpm.common.valueprovider.FlooredInt;
import com.modularmc.gtpm.common.valueprovider.MultipliedFloat;

import net.minecraft.core.registries.Registries;
import net.minecraft.util.valueproviders.FloatProviderType;
import net.minecraft.util.valueproviders.IntProviderType;
import net.neoforged.neoforge.registries.DeferredHolder;

import static com.modularmc.gtpm.common.registry.GTRegistration.REGISTRATE;

public class GTValueProviderTypes {

    public static final DeferredHolder<IntProviderType<?>, IntProviderType<FlooredInt>> FLOORED = REGISTRATE
            .<IntProviderType<?>, IntProviderType<FlooredInt>>generic("floored", Registries.INT_PROVIDER_TYPE,
                    () -> () -> FlooredInt.CODEC)
            .register();

    public static final DeferredHolder<FloatProviderType<?>, FloatProviderType<MultipliedFloat>> MULTIPLIED = REGISTRATE
            .<FloatProviderType<?>, FloatProviderType<MultipliedFloat>>generic("multiplied", Registries.FLOAT_PROVIDER_TYPE,
                    () -> () -> MultipliedFloat.CODEC)
            .register();
    public static final DeferredHolder<FloatProviderType<?>, FloatProviderType<AddedFloat>> ADDED = REGISTRATE
            .<FloatProviderType<?>, FloatProviderType<AddedFloat>>generic("added", Registries.FLOAT_PROVIDER_TYPE,
                    () -> () -> AddedFloat.CODEC)
            .register();
    public static final DeferredHolder<FloatProviderType<?>, FloatProviderType<CastedFloat>> CASTED = REGISTRATE
            .<FloatProviderType<?>, FloatProviderType<CastedFloat>>generic("casted", Registries.FLOAT_PROVIDER_TYPE,
                    () -> () -> CastedFloat.CODEC)
            .register();

    public static void init() {}
}
