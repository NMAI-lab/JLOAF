package org.jLOAF.sim.atomic;

import org.jLOAF.inputs.AtomicInput;
import org.jLOAF.inputs.Input;
import org.jLOAF.sim.AtomicSimilarityMetricStrategy;
import org.jLOAF.sim.SimilarityMetricStrategy;

public class EuclideanDistance extends AtomicSimilarityMetricStrategy {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//calculates the Euclidean distance between objects - can only be used with weighted knn
	//the larger the better , can be used for any type of value ranges between [0,1]
	@Override
	public double similarity(Input i1, Input i2) {
		if(!(i1 instanceof AtomicInput) || !(i2 instanceof AtomicInput)){
			throw new IllegalArgumentException("PercentDifference.similarity(...): One of the arguments was not an AtomicInput.");
		}

		double val1 = ((AtomicInput)i1).getFeature().getValue();
		double val2 = ((AtomicInput)i2).getFeature().getValue();
		
		//double denom = val1+val2;
		double num = Math.abs(val1-val2);
		
		return 1/(1+num);
	}

}
