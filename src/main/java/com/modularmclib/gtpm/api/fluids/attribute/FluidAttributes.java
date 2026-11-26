package com.modularmclib.gtpm.api.fluids.attribute;

import com.modularmclib.gtpm.GTPM;

import net.minecraft.network.chat.Component;

public final class FluidAttributes {

    /**
     * Attribute for acidic fluids.
     */
    public static final FluidAttribute ACID = new FluidAttribute(GTPM.id("acid"),
            list -> list.accept(Component.translatable("gtpm.fluid.type_acid.tooltip")),
            list -> list.accept(Component.translatable("gtpm.fluid_pipe.acid_proof")));

    private FluidAttributes() {}
}
