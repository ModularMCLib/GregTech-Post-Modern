package com.modularmclib.gtpm.api.machine.trait;

import com.modularmclib.gtpm.api.capability.recipe.IO;

public interface ICapabilityTrait {

    IO getCapabilityIO();

    default boolean canCapInput() {
        return getCapabilityIO().support(IO.IN);
    }

    default boolean canCapOutput() {
        return getCapabilityIO().support(IO.OUT);
    }
}
