package com.modularmc.gtpm.api.machine.trait;

import com.modularmc.gtpm.api.capability.recipe.IO;
import com.modularmc.gtpm.api.capability.recipe.IRecipeHandler;
import com.modularmc.gtpm.utils.ISubscription;

public interface IRecipeHandlerTrait<K> extends IRecipeHandler<K> {

    IO getHandlerIO();

    /**
     * add listener for notification when it changed.
     */
    ISubscription addChangedListener(Runnable listener);
}
