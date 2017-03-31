package org.jLOAF.sim.atomic;

import org.jLOAF.inputs.AtomicInput;
import org.jLOAF.inputs.Input;
import org.jLOAF.sim.SimilarityMetricStrategy;

public class EuclideanDistance implements SimilarityMetricStrategy {
	//calculates the euclidean distance between objects - can only be used with weighted knn
	@Override
	public double similarity(Input i1, Input i2) {
		if(!(i1 instanceof AtomicInput) || !(i2 instanceof AtomicInput)){
			throw new IllegalArgumentException("PercentDifference.similarity(...): One of the arguments was not an AtomicInput.");
		}

		double val1 = ((AtomicInput)i1).getFeature().getValue();
		double val2 = ((AtomicInput)i2).getFeature().getValue();
		
		double denom = val1+val2;
		double num = Math.abs(val1-val2);
		
		if(denom==0) return 0.0;
		
		return num/denom;
	}

}
