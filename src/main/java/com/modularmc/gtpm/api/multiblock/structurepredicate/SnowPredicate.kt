package com.modularmc.gtpm.api.multiblock.structurepredicate

import com.modularmc.gtpm.api.multiblock.MultiblockBlockInfo
import com.modularmc.gtpm.api.multiblock.MultiblockState
import com.modularmc.gtpm.common.registry.GTRegistration.REGISTRATE
import com.modularmc.gtpm.utils.GTUtil

import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockState

import com.mojang.serialization.MapCodec
import it.unimi.dsi.fastutil.objects.ObjectList

enum class SnowPredicate : StructurePredicate {
	INSTANCE,
	;

	override fun hasPositionDependencies(): Boolean = true
	override fun canReuseObservation(): Boolean = true

	override fun type(): StructurePredicateType<*> = TYPE
	override fun observeCandidate(candidateState: BlockState): PatternObservation = PatternObservation.of(GTUtil.isBlockSnow(candidateState))
	override fun test(multiblockState: MultiblockState, mutateCount: Boolean): Boolean = GTUtil.isBlockSnow(multiblockState.blockState)
	override fun candidates(): ObjectList<MultiblockBlockInfo> = ObjectList.of(MultiblockBlockInfo.fromBlockState(Blocks.SNOW_BLOCK.defaultBlockState()))
	override fun blockCandidates(): ObjectList<Block> = ObjectList.of(Blocks.SNOW_BLOCK)

	companion object {
		val TYPE: StructurePredicateType<SnowPredicate> by lazy { REGISTRATE.structurePredicate("snow", CODEC) }

		@JvmField
		val CODEC: MapCodec<SnowPredicate> = MapCodec.unit(INSTANCE)
	}
}
