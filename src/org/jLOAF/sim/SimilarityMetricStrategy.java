package org.jLOAF.sim;

import java.io.Serializable;

import org.jLOAF.inputs.Input;

/**
 * Interface that is the parent of all the similarityMetricStrategies
 * @author sachagunaratne
 *
 */
public interface SimilarityMetricStrategy extends Serializable {

	public double similarity(Input i1, Input i2);
}
