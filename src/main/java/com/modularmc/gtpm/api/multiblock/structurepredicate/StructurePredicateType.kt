package com.modularmc.gtpm.api.multiblock.structurepredicate

import com.modularmc.gtpm.api.addon.AddonFinder
import com.modularmc.gtpm.api.addon.IGTAddon
import com.modularmc.gtpm.api.registry.GTRegistries
import com.modularmc.gtpm.api.registry.registrate.GTRegistrate

import net.minecraft.resources.ResourceLocation

import com.mojang.serialization.MapCodec

/** Registry identity and the actual leaf codec are shared with the runtime compiler registry. */
@JvmRecord
data class StructurePredicateType<T : StructurePredicate>(val id: ResourceLocation, val codec: MapCodec<T>) {
	class Builder(private val registrate: GTRegistrate, private val name: String) {
		fun <T : StructurePredicate> codec(codec: MapCodec<T>): Registration<T> = Registration(registrate, name, codec)
	}

	class Registration<T : StructurePredicate>(private val registrate: GTRegistrate, private val name: String, private val codec: MapCodec<T>) {
		fun register(): StructurePredicateType<T> {
			val type = StructurePredicateType(registrate.makeResourceLocation(name), codec)
			registrate.predicate(name).definition(PredicateType(codec, ::PatternPredicate)).register()
			registrate.generic(name, GTRegistries.STRUCTURE_PREDICATE_REGISTRY) { type }.register()
			return type
		}
	}

	companion object {
		@JvmStatic
		fun byId(id: ResourceLocation): StructurePredicateType<*>? = GTRegistries.STRUCTURE_PREDICATE_TYPES.get(id)

		/** Definitions own their registration; addons still register before machine definitions use their codecs. */
		@JvmStatic
		fun init() {
			BlockPredicate.TYPE
			BlockStatePredicate.TYPE
			BlockTagPredicate.TYPE
			FluidPredicate.TYPE
			FluidTagPredicate.TYPE
			AbilityPredicate.TYPE
			AutoRecipeAbilityPredicate.TYPE
			AutoMaintenancePredicate.TYPE
			ActiveTransformerHatchPredicate.TYPE
			CentralMonitorComponentPredicate.TYPE
			CharcoalLogOrAirPredicate.TYPE
			CleanroomDoorPredicate.TYPE
			CleanroomInnerPredicate.TYPE
			CleanroomBasePredicate.TYPE
			HeatingCoilPredicate.TYPE
			CleanroomFilterPredicate.TYPE
			FramePredicate.TYPE
			PowerSubstationBatteryPredicate.TYPE
			SnowPredicate.TYPE
			DataHatchOrPredicate.TYPE
			RotorHolderPredicate.TYPE
			RestrictedPredicate.TYPE
			ConcatenatedPredicate.TYPE
			AnyPredicate.TYPE
			AirPredicate.TYPE
			PredicateController.TYPE
			RuntimePredicateCodec.TYPE
			CombinedPredicate.ALL_OF_TYPE
			CombinedPredicate.ANY_OF_TYPE
			CombinedPredicate.ONE_OF_TYPE
			CombinedPredicate.NOT_TYPE
			AddonFinder.getAddonList().forEach(IGTAddon::registerStructurePredicateTypes)
		}
	}
}
