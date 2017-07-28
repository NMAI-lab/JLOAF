package org.jLOAF.sim;

import org.jLOAF.sim.StateBased.StSims;

/**
 * Abstract SimilarityMetricStrategy for StateBasedInputs
 * @author sachagunaratne
 *
 */
public abstract class StateBasedSimilarity implements SimilarityMetricStrategy {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static SimilarityMetricStrategy getSim(String st) {
		// TODO Auto-generated method stub
		return StSims.valueOf(st).geSim();
	}

}
