package com.modularmc.gtpm.common.data;

import com.modularmc.gtpm.common.particle.HazardParticleOptions;

import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.neoforge.registries.DeferredHolder;

import com.mojang.serialization.MapCodec;
import org.jetbrains.annotations.NotNull;

import static com.modularmc.gtpm.common.registry.GTRegistration.REGISTRATE;

public class GTParticleTypes {

    public static final DeferredHolder<ParticleType<?>, ParticleType<HazardParticleOptions>> HAZARD_PARTICLE = REGISTRATE
            .<ParticleType<?>, ParticleType<HazardParticleOptions>>generic("hazard", Registries.PARTICLE_TYPE,
                    () -> new ParticleType<>(false) {

                        @Override
                        public @NotNull MapCodec<HazardParticleOptions> codec() {
                            return HazardParticleOptions.CODEC;
                        }

                        @Override
                        public @NotNull StreamCodec<FriendlyByteBuf, HazardParticleOptions> streamCodec() {
                            return HazardParticleOptions.STREAM_CODEC;
                        }
                    })
            .register();
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> MUFFLER_PARTICLE = REGISTRATE
            .generic("muffler", Registries.PARTICLE_TYPE, () -> new SimpleParticleType(false))
            .register();

    public static void init() {}
}
