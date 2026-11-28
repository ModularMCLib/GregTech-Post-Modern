package com.modularmc.gtpm.common.data;

import com.modularmc.gtpm.common.entity.*;

import com.modularmc.registrate.localization.MinecraftLocale;
import com.modularmc.registrate.localization.annotation.TransVariants;
import com.modularmc.registrate.localization.annotation.Translation;
import com.modularmc.registrate.localization.annotation.Variant;
import com.modularmc.registrate.util.entry.EntityEntry;

import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.MobCategory;

import static com.modularmc.gtpm.common.registry.GTRegistration.REGISTRATE;

@TransVariants(keyPrefix = "entity.gtpm",
               value = {
                       @Variant(id = "dynamite",
                                source = "Dynamite",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "炸药")),
                       @Variant(id = "industrial_tnt",
                                source = "Industrial TNT",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "工业TNT")),
                       @Variant(id = "powderbarrel",
                                source = "Powderbarrel",
                                translations = @Translation(locale = MinecraftLocale.ZH_CN, text = "火药桶")),
               })
public class GTEntityTypes {

    public static final EntityEntry<DynamiteEntity> DYNAMITE = REGISTRATE
            .<DynamiteEntity>entity("dynamite", DynamiteEntity::new, MobCategory.MISC)
            .lang("Dynamite")
            .properties(builder -> builder.sized(0.25F, 0.25F).fireImmune().clientTrackingRange(4).updateInterval(10))
            .tag(EntityTypeTags.IMPACT_PROJECTILES)
            .register();

    public static final EntityEntry<PowderbarrelEntity> POWDERBARREL = REGISTRATE
            .<PowderbarrelEntity>entity("powderbarrel", PowderbarrelEntity::new, MobCategory.MISC)
            .lang("Powderbarrel")
            .properties(builder -> builder.sized(0.98F, 0.98F).fireImmune().clientTrackingRange(10).updateInterval(10))
            .register();

    public static final EntityEntry<IndustrialTNTEntity> INDUSTRIAL_TNT = REGISTRATE
            .<IndustrialTNTEntity>entity("industrial_tnt", IndustrialTNTEntity::new, MobCategory.MISC)
            .lang("Industrial TNT")
            .properties(builder -> builder.sized(0.98F, 0.98F).fireImmune().clientTrackingRange(10).updateInterval(10))
            .register();

    public static void init() {}
}
