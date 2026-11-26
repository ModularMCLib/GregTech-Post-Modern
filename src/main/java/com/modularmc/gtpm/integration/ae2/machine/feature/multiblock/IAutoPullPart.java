package com.modularmc.gtpm.integration.ae2.machine.feature.multiblock;

import com.modularmc.gtpm.api.machine.feature.multiblock.IMultiPart;

import appeng.api.stacks.GenericStack;

import java.util.function.Predicate;

public interface IAutoPullPart extends IMultiPart {

    boolean isAutoPull();

    void setAutoPull(boolean autoPull);

    void setAutoPullTest(Predicate<GenericStack> test);
}
