package org.jLOAF.sim.complex;

import java.util.Set;

import org.jLOAF.inputs.ComplexInput;
import org.jLOAF.inputs.Input;
import org.jLOAF.sim.SimilarityMetricStrategy;

public class Mean implements SimilarityMetricStrategy {

	@Override
	public double similarity(Input i1, Input i2) {
		if(!(i1 instanceof ComplexInput) || !(i2 instanceof ComplexInput) ){
			throw new IllegalArgumentException("Mean.similarity(..): Not ComplexInputs");
		}
		
		ComplexInput cplx1 = (ComplexInput)i1;
		ComplexInput cplx2 = (ComplexInput)i2;
		
		Set<String> keys = cplx1.getChildNames();
		Set<String> keys2 = cplx2.getChildNames();
		
		if(!keys.equals(keys2)){
			System.out.println("Mean.similarity(...):Likely a problem since not same features.");
		}
		
		double total = 0;
		
		for(String s: keys){
			total += cplx1.get(s).similarity(cplx2.get(s));
		}
		
		return total/keys.size();
		
	}

}
