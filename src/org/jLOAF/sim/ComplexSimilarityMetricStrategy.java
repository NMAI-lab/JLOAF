package org.jLOAF.sim;

import org.jLOAF.sim.complex.CPSims;

/**
 * Abstract SimilarityMetricStrategy for ComplexInputs
 * @author sachagunaratne
 *
 */
public abstract class ComplexSimilarityMetricStrategy implements SimilarityMetricStrategy {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static SimilarityMetricStrategy getSim(String st) {
		// TODO Auto-generated method stub
		return CPSims.valueOf(st).geSim();
	}


}
