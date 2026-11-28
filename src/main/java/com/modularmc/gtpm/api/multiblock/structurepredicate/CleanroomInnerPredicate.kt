package com.modularmc.gtpm.api.multiblock.structurepredicate

import com.modularmc.gtpm.api.machine.MetaMachine
import com.modularmc.gtpm.api.machine.SimpleGeneratorMachine
import com.modularmc.gtpm.api.multiblock.MultiblockState
import com.modularmc.gtpm.common.machine.multiblock.electric.BedrockOreMinerMachine
import com.modularmc.gtpm.common.machine.multiblock.electric.FluidDrillMachine
import com.modularmc.gtpm.common.machine.multiblock.electric.LargeMinerMachine
import com.modularmc.gtpm.common.machine.multiblock.generator.LargeCombustionEngineMachine
import com.modularmc.gtpm.common.machine.multiblock.generator.LargeTurbineMachine
import com.modularmc.gtpm.common.machine.multiblock.part.MufflerPartMachine
import com.modularmc.gtpm.common.machine.multiblock.primitive.CokeOvenMachine
import com.modularmc.gtpm.common.machine.multiblock.primitive.PrimitiveBlastFurnaceMachine
import com.modularmc.gtpm.common.machine.multiblock.primitive.PrimitivePumpMachine
import com.modularmc.gtpm.common.machine.trait.CleanroomProviderTrait
import com.modularmc.gtpm.common.machine.trait.CleanroomReceiverTrait
import com.modularmc.gtpm.common.registry.GTRegistration.REGISTRATE

import net.minecraft.world.level.block.state.BlockState

import com.mojang.serialization.MapCodec
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet
import it.unimi.dsi.fastutil.objects.ObjectSet

/** Interior matching accepts air and records only actual cleanroom receiver traits on the world thread. */
enum class CleanroomInnerPredicate : StructurePredicate {
	INSTANCE,
	;

	override fun hasPositionDependencies(): Boolean = true

	override fun type(): StructurePredicateType<*> = TYPE
	override fun observeCandidate(candidateState: BlockState): PatternObservation = PatternObservation.of(true)
	private data class Position(val hasMachine: Boolean, val receiver: CleanroomReceiverTrait?) : PatternObservation.PositionMatch
	override fun observePosition(state: MultiblockState): PatternObservation {
		val machine = state.blockEntity as? MetaMachine
		if (machine != null && isMachineBanned(machine)) return PatternObservation.of(false)
		val receiver = machine?.getTraitOptional(CleanroomReceiverTrait.TYPE)?.orElse(null)
		return PatternObservation.of(true).withPositionMatch(Position(machine != null, receiver))
	}
	override fun acceptPosition(state: MultiblockState, observation: PatternObservation): Boolean {
		if (!observation.matched) return false
		val position = observation.positionMatch as Position
		if (position.hasMachine) {
			val receivers: ObjectSet<CleanroomReceiverTrait> = state.facts.getOrCreate("cleanroomReceiver") { ObjectOpenHashSet() }
			position.receiver?.let(receivers::add)
		}
		return true
	}
	override fun test(multiblockState: MultiblockState, mutateCount: Boolean): Boolean {
		val machine = multiblockState.blockEntity as? MetaMachine ?: return true
		if (isMachineBanned(machine)) return false
		if (mutateCount) {
			val receivers: ObjectSet<CleanroomReceiverTrait> = multiblockState.facts.getOrCreate("cleanroomReceiver") { ObjectOpenHashSet() }
			machine.getTraitOptional(CleanroomReceiverTrait.TYPE).ifPresent(receivers::add)
		}
		return true
	}
	override fun isAny(): Boolean = true
	override fun addCache(): Boolean = true

	companion object {
		val TYPE: StructurePredicateType<CleanroomInnerPredicate> by lazy { REGISTRATE.structurePredicate("cleanroom_inner").codec(CODEC).register() }

		@JvmField
		val CODEC: MapCodec<CleanroomInnerPredicate> = MapCodec.unit(INSTANCE)
		private fun isMachineBanned(machine: MetaMachine): Boolean = machine.getTrait(CleanroomProviderTrait.TYPE) != null || machine is MufflerPartMachine || machine is SimpleGeneratorMachine ||
			machine is LargeCombustionEngineMachine || machine is LargeTurbineMachine || machine is LargeMinerMachine ||
			machine is FluidDrillMachine || machine is BedrockOreMinerMachine || machine is CokeOvenMachine ||
			machine is PrimitiveBlastFurnaceMachine || machine is PrimitivePumpMachine
	}
}
