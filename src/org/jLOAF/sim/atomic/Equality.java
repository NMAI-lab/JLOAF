/** A very simple similarity metric that just tests two
 * AtomicInput items to see if their Features are the same.
 * 
 * Author: Michael W. Floyd
 */

package org.jLOAF.sim.atomic;

import org.jLOAF.inputs.AtomicInput;
import org.jLOAF.inputs.Input;
import org.jLOAF.sim.AtomicSimilarityMetricStrategy;
import org.jLOAF.sim.SimilarityMetricStrategy;

public class Equality extends AtomicSimilarityMetricStrategy {



	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** The similarity is bound between 0.0 and 1.0. If the
	 * feature values are the same the similarity is 1.0, 
	 * otherwise the similarity is 0.0.
	 * 
	 */
	@Override
	public double similarity(Input i1, Input i2) {
		if(!(i1 instanceof AtomicInput) || !(i2 instanceof AtomicInput)){
			throw new IllegalArgumentException("Equality.similarity(...): One of the arguments was not an AtomicInput.");
		}

		double val1 = ((AtomicInput)i1).getFeature().getValue();
		double val2 = ((AtomicInput)i2).getFeature().getValue();
		
		if(val1 == val2){
			return 1.0;
		}else{
			return 0.0;
		}
	}

}
