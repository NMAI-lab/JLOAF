package org.jLOAF.sim.complex;

import java.util.Set;

import org.jLOAF.inputs.ComplexInput;
import org.jLOAF.inputs.Input;
import org.jLOAF.sim.ComplexSimilarityMetricStrategy;
import org.jLOAF.sim.SimilarityMetricStrategy;

/***
 * Returns the most pessimistic estimate for the similarity. (The min similarity)
 * ***/
public class Pessimistic extends ComplexSimilarityMetricStrategy {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public double similarity(Input i1, Input i2) {
		
		if(i1== null || i2 == null) return 0.0;
		
		if(!(i1 instanceof ComplexInput) || !(i2 instanceof ComplexInput) ){
			throw new IllegalArgumentException("Mean.similarity(..): Not ComplexInputs");
		}
		
		ComplexInput cplx1 = (ComplexInput)i1;
		ComplexInput cplx2 = (ComplexInput)i2;
		
		Set<String> keys = cplx1.getChildNames();
		Set<String> keys2 = cplx2.getChildNames();
		
		double min = 0;
		double sim =0;
		
		//dealing with mismatched sets with a penalty for not having a paired element
		if (keys.size()>=keys2.size()){
			for(String s: keys){
				if (cplx2.get(s)!=null){
					sim = cplx1.get(s).similarity(cplx2.get(s));
					if(min>sim)min=sim;
				}
			}
		}else if(keys.size()<keys2.size()){
			for(String s: keys2){
				if (cplx1.get(s)!=null){
					sim = cplx2.get(s).similarity(cplx1.get(s));
					if(min>sim)min=sim;
				}		
			}
		}
		
		return min;
	}
}
