package com.modularmc.gtpm.api.multiblock;

import java.util.function.Predicate;

public record PatternCondition(Predicate<MultiblockState> condition, String translateKey) {}
