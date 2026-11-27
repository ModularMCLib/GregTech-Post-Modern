package com.modularmc.gtpm.data.tags;

import com.modularmc.gtpm.data.recipe.CustomTags;

import com.modularmc.registrate.providers.RegistrateTagsProvider;

import net.minecraft.world.entity.EntityType;

public class EntityTypeTagLoader {

    public static void init(RegistrateTagsProvider.IntrinsicTags<EntityType<?>> provider) {
        provider.tag(CustomTags.HEAT_IMMUNE)
                .add(EntityType.BLAZE, EntityType.MAGMA_CUBE)
                .add(EntityType.WITHER_SKELETON, EntityType.WITHER);
        provider.tag(CustomTags.CHEMICAL_IMMUNE)
                .add(EntityType.SKELETON, EntityType.STRAY, EntityType.BOGGED);
        provider.tag(CustomTags.IRON_GOLEMS).add(EntityType.IRON_GOLEM);
        provider.tag(CustomTags.SPIDERS)
                .add(EntityType.SPIDER, EntityType.CAVE_SPIDER);
    }
}
