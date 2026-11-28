package com.modularmc.gtpm.common.data;

import com.modularmc.gtpm.common.effect.GTPoisonEffect;

import com.modularmc.registrate.localization.MinecraftLocale;
import com.modularmc.registrate.util.entry.RegistryEntry;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

import static com.modularmc.gtpm.common.registry.GTRegistration.REGISTRATE;

public class GTMobEffects {

    public static final RegistryEntry<MobEffect, GTPoisonEffect> WEAK_POISON = REGISTRATE
            .generic("weak_poison", Registries.MOB_EFFECT, () -> new GTPoisonEffect(MobEffectCategory.HARMFUL, 0x6D7917))
            .localization(MobEffect::getDescriptionId,
                    text -> text.source("Weak Poison").translation(MinecraftLocale.ZH_CN, "轻度中毒"))
            .register();

    public static void init() {}
}
