package com.modularmclib.gtpm.api.multiblock;

import java.util.function.Predicate;

public record PatternCondition(Predicate<MultiblockState> condition, String translateKey) {}
