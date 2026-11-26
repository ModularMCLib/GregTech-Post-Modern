package com.modularmclib.gtpm.api.multiblock.pattern.layout

/** One active choice, retaining the actual path through enclosing repetitions. */
@JvmRecord
data class PatternChoiceInstance(val path: String, val descriptor: PatternChoiceDescriptor)
