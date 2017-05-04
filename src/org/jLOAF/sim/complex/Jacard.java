package org.jLOAF.sim.complex;

import java.util.Set;

import org.jLOAF.inputs.ComplexInput;
import org.jLOAF.inputs.Input;
import org.jLOAF.sim.SimilarityMetricStrategy;
/***
 * This defines similarity of two sets by the following equation - A Intersecton B /AUB
 * Compares the names of each feature in the sets.
 * ***/
public class Jacard implements SimilarityMetricStrategy {

	@Override
	public double similarity(Input i1, Input i2) {
		if(i1==null || i2 ==null) return 0.0;
		
		if(!(i1 instanceof ComplexInput) || !(i2 instanceof ComplexInput) ){
			throw new IllegalArgumentException("Mean.similarity(..): Not ComplexInputs");
		}
		
		ComplexInput cplx1 = (ComplexInput)i1;
		ComplexInput cplx2 = (ComplexInput)i2;
		
		Set<String> keys = cplx1.getChildNames();
		Set<String> keys2 = cplx2.getChildNames();
		
		double set1Size = keys.size();
		double set2Size = keys2.size();
		
		//makes keys the intersection of keys and keys2
		keys.retainAll(keys2);
		
		double intersectionSize = keys.size();
		
		return intersectionSize/(set1Size+set2Size-intersectionSize);
	}

}
