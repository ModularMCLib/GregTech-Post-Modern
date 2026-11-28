package com.modularmc.gtpm.api.multiblock.structurepredicate

import com.modularmc.gtpm.api.registry.GTRegistries

import net.minecraft.resources.ResourceLocation

/** Setup registers actual codec/compiler entries; reload compilation only reads the published registry. */
object PredicateRegistry {
	@JvmStatic
	fun require(id: ResourceLocation): PredicateType<*> = find(id) ?: error("Unknown predicate type: $id")

	fun find(id: ResourceLocation): PredicateType<*>? = GTRegistries.lookup(GTRegistries.PREDICATE_TYPES, id)
}
