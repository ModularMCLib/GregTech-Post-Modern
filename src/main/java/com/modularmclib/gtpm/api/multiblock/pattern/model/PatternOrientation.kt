package com.modularmclib.gtpm.api.multiblock.pattern.model

/** Orientation capabilities declared by a pattern resource. */
data class PatternOrientation(val allowMirror: Boolean, val allowExtendedFacing: Boolean) {
	companion object {
		@JvmStatic
		fun defaults(): PatternOrientation = PatternOrientation(false, false)
	}
}
