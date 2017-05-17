package org.jLOAF.sim;

import java.io.Serializable;

import org.jLOAF.inputs.Input;

public interface SimilarityMetricStrategy extends Serializable {

	public double similarity(Input i1, Input i2);
}
