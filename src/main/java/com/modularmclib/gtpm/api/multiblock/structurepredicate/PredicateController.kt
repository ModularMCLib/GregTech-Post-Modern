package com.modularmclib.gtpm.api.multiblock.structurepredicate

import com.modularmclib.gtpm.GTPM
import com.modularmclib.gtpm.api.machine.MachineDefinition
import com.modularmclib.gtpm.api.machine.MetaMachine

import net.minecraft.resources.ResourceLocation

import com.mojang.serialization.MapCodec
import it.unimi.dsi.fastutil.objects.ObjectList

import java.util.function.Predicate

/** Marks the origin while retaining the same actual machine identity in Java and serialized forms. */
open class PredicateController : PatternPredicate {
	override fun hasPositionDependencies(): Boolean = javaClass == PredicateController::class.java && rulesHavePositionDependencies()

	var owner: MachineDefinition? = null
		private set
	private var machineId: ResourceLocation? = null

	constructor(definition: MachineDefinition) : super(PatternPredicate.blocks(definition.get())) {
		owner = definition
		machineId = definition.id
	}

	constructor(predicate: PatternPredicate) : super(predicate) {
		owner = (predicate as? PredicateController)?.owner
		machineId = (predicate as? PredicateController)?.machineId
	}

	constructor(machine: ResourceLocation) : super(BlockPredicate(ObjectList.of(machine))) {
		machineId = machine
		common.first().predicate = Predicate { state ->
			MetaMachine.getMachine(state.world, state.pos)?.definition?.id == machine
		}
		common.first().sealPreparedCallbacks()
	}

	companion object {
		val TYPE: PredicateType<PredicateController> by lazy {
			PredicateType(CODEC) { it }.also { PredicateRegistry.register(GTPM.id("controller"), it) }
		}
		val CODEC: MapCodec<PredicateController> = ResourceLocation.CODEC.fieldOf("machine").xmap(::PredicateController) { it.machineId!! }
	}
}
