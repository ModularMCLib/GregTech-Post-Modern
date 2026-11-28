package com.modularmc.gtpm.common.data;

import com.modularmc.gtpm.common.entity.*;

import com.modularmc.registrate.localization.MinecraftLocale;
import com.modularmc.registrate.util.entry.EntityEntry;

import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.MobCategory;

import static com.modularmc.gtpm.common.registry.GTRegistration.REGISTRATE;

public class GTEntityTypes {

    public static final EntityEntry<DynamiteEntity> DYNAMITE = REGISTRATE
            .<DynamiteEntity>entity("dynamite", DynamiteEntity::new, MobCategory.MISC)
            .localization(text -> text.source("Dynamite").translation(MinecraftLocale.ZH_CN, "炸药"))
            .properties(builder -> builder.sized(0.25F, 0.25F).fireImmune().clientTrackingRange(4).updateInterval(10))
            .tag(EntityTypeTags.IMPACT_PROJECTILES)
            .register();

    public static final EntityEntry<PowderbarrelEntity> POWDERBARREL = REGISTRATE
            .<PowderbarrelEntity>entity("powderbarrel", PowderbarrelEntity::new, MobCategory.MISC)
            .localization(text -> text.source("Powderbarrel").translation(MinecraftLocale.ZH_CN, "火药桶"))
            .properties(builder -> builder.sized(0.98F, 0.98F).fireImmune().clientTrackingRange(10).updateInterval(10))
            .register();

    public static final EntityEntry<IndustrialTNTEntity> INDUSTRIAL_TNT = REGISTRATE
            .<IndustrialTNTEntity>entity("industrial_tnt", IndustrialTNTEntity::new, MobCategory.MISC)
            .localization(text -> text.source("Industrial TNT").translation(MinecraftLocale.ZH_CN, "工业TNT"))
            .properties(builder -> builder.sized(0.98F, 0.98F).fireImmune().clientTrackingRange(10).updateInterval(10))
            .register();

    public static void init() {}
}
