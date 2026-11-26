package com.modularmc.gtpm.api.machine.trait;

import com.modularmc.gtpm.api.capability.recipe.IO;

public interface ICapabilityTrait {

    IO getCapabilityIO();

    default boolean canCapInput() {
        return getCapabilityIO().support(IO.IN);
    }

    default boolean canCapOutput() {
        return getCapabilityIO().support(IO.OUT);
    }
}
