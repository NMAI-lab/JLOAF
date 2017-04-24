package org.jLOAF.sim.complex;

import java.util.Set;

import org.jLOAF.inputs.ComplexInput;
import org.jLOAF.inputs.Input;
import org.jLOAF.sim.SimilarityMetricStrategy;

public class Mean implements SimilarityMetricStrategy {

	@Override
	public double similarity(Input i1, Input i2) {
		//null check
		if(i1==null || i2 ==null) return 0.0;
		
		if(!(i1 instanceof ComplexInput) || !(i2 instanceof ComplexInput) ){
			throw new IllegalArgumentException("Mean.similarity(..): Not ComplexInputs");
		}
		
		ComplexInput cplx1 = (ComplexInput)i1;
		ComplexInput cplx2 = (ComplexInput)i2;
		
		Set<String> keys = cplx1.getChildNames();
		Set<String> keys2 = cplx2.getChildNames();
		
//		if(!keys.equals(keys2)){
//			//System.out.println("Mean.similarity(...):Likely a problem since not same features.");
//			//not the best way to do this atm
//			//how to deal with 
//			return 1000;
//		}
		
		double total = 0;
		
		if (keys.size()>=keys2.size()){
			for(String s: keys){
				if (cplx2.get(s)!=null){
					total += cplx1.get(s).similarity(cplx2.get(s));
				}else{
					//penalty
					total +=10;
				}	
			}
		}else if(keys.size()<keys2.size()){
			for(String s: keys2){
				if (cplx1.get(s)!=null){
					total += cplx2.get(s).similarity(cplx1.get(s));
				}else{
					//penalty
					total +=10;
				}		
			}
		}
		
		if(keys.size()==0 && keys2.size()==0){
			//if they cannot see anything they are in a similar situation?
			return 0.0;
		}
		
		return total/keys.size();
		
	}

}
