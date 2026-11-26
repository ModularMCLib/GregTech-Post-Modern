package com.modularmclib.gtpm.api.machine.trait;

import com.modularmclib.gtpm.api.capability.recipe.IO;
import com.modularmclib.gtpm.api.capability.recipe.IRecipeHandler;
import com.modularmclib.gtpm.utils.ISubscription;

public interface IRecipeHandlerTrait<K> extends IRecipeHandler<K> {

    IO getHandlerIO();

    /**
     * add listener for notification when it changed.
     */
    ISubscription addChangedListener(Runnable listener);
}
