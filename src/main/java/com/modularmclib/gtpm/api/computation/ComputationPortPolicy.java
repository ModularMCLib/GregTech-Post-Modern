package com.modularmclib.gtpm.api.computation;

/**
 * Describes which physical routes a computation port accepts.
 */
public record ComputationPortPolicy(boolean acceptsOptical, boolean acceptsAdjacent) {

    public static final ComputationPortPolicy OPTICAL_ONLY = new ComputationPortPolicy(true, false);
    public static final ComputationPortPolicy ADJACENT_ONLY = new ComputationPortPolicy(false, true);
    public static final ComputationPortPolicy OPTICAL_AND_ADJACENT = new ComputationPortPolicy(true, true);
}
