package org.jLOAF.sim;

import java.io.Serializable;

import org.jLOAF.inputs.Input;

/**
 * Interface that is the parent of all the similarityMetricStrategies
 * @author sachagunaratne
 *
 */
public interface SimilarityMetricStrategy extends Serializable {
	/**
	 * Takes two inputs and compares how similar they are. 
	 * @param i1 An input
	 * @param i2 An input
	 * @return sim A double value of similarity
	 */
	public double similarity(Input i1, Input i2);
}
