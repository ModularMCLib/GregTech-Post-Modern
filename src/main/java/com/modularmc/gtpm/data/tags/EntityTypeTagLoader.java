package com.modularmc.gtpm.data.tags;

import com.modularmc.gtpm.data.recipe.CustomTags;

import com.modularmc.registrate.providers.RegistrateTagsProvider;

import net.minecraft.world.entity.EntityType;

public class EntityTypeTagLoader {

    public static void init(RegistrateTagsProvider.IntrinsicImpl<EntityType<?>> provider) {
        provider.addTag(CustomTags.HEAT_IMMUNE)
                .add(EntityType.BLAZE, EntityType.MAGMA_CUBE)
                .add(EntityType.WITHER_SKELETON, EntityType.WITHER);
        provider.addTag(CustomTags.CHEMICAL_IMMUNE)
                .add(EntityType.SKELETON, EntityType.STRAY, EntityType.BOGGED);
        provider.addTag(CustomTags.IRON_GOLEMS).add(EntityType.IRON_GOLEM);
        provider.addTag(CustomTags.SPIDERS)
                .add(EntityType.SPIDER, EntityType.CAVE_SPIDER);
    }
}
