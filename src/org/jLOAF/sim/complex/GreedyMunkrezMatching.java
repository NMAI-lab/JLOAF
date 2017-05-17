package org.jLOAF.sim.complex;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jLOAF.inputs.ComplexInput;
import org.jLOAF.inputs.Input;
import org.jLOAF.sim.ComplexSimilarityMetricStrategy;
import org.jLOAF.sim.SimilarityMetricStrategy;
import org.jLOAF.util.*;

public class GreedyMunkrezMatching extends ComplexSimilarityMetricStrategy {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public double similarity(Input i1, Input i2) {
		
		if(!(i1 instanceof ComplexInput) || !(i2 instanceof ComplexInput) ){
			throw new IllegalArgumentException("Mean.similarity(..): Not ComplexInputs");
		}
		
		ComplexInput cplx1 = (ComplexInput)i1;
		ComplexInput cplx2 = (ComplexInput)i2;
		
		Set<String> keys = cplx1.getChildNames();
		Set<String> keys2 = cplx2.getChildNames();
		
		if(keys.size()==0 && keys2.size()==0){
			//if they cannot see anything they are in a similar situation?
			return 1.0;
		}
		
		List<Tuple> allPairs = new ArrayList<Tuple>();
		
		for (String s: keys){
			for (String s1: keys2){
				allPairs.add(new Tuple(s,s1,cplx1.get(s).similarity(cplx2.get(s1))));
			}
		}
		
		//sort ascending order because we are looking for largest sim
		Collections.sort(allPairs);
		
		double total_sim =0.0;
		Set<String> used_items = new HashSet<String>();
		
		for (Tuple p:allPairs){
			if(!used_items.contains(p.getKey1()) && !used_items.contains(p.getKey2())){	
				total_sim = total_sim + p.getSim();
				used_items.add(p.getKey1());
				used_items.add(p.getKey2());
			}
		}
		
		double largest_group_size = Math.max(keys.size(),keys2.size());
		
		if(largest_group_size==0) return 1;
		else return total_sim/largest_group_size;
	}
}
