package org.jLOAF.sim;

import org.jLOAF.inputs.Input;

public interface SimilarityMetricStrategy {

	public double similarity(Input i1, Input i2);
}
