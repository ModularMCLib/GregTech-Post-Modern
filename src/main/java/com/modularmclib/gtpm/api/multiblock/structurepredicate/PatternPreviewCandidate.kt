package com.modularmclib.gtpm.api.multiblock.structurepredicate

import com.modularmclib.gtpm.api.multiblock.MultiblockBlockInfo

/** A representative full block state and only the facts of its selected predicate branch. */
@JvmRecord
data class PatternPreviewCandidate(val blockInfo: MultiblockBlockInfo, val observation: PatternObservation)
