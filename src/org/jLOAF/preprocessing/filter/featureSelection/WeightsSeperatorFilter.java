package org.jLOAF.preprocessing.filter.featureSelection;

import org.jLOAF.casebase.Case;
import org.jLOAF.casebase.CaseBase;
import org.jLOAF.inputs.ComplexInput;
import org.jLOAF.inputs.Input;
import org.jLOAF.inputs.StateBasedInput;
import org.jLOAF.preprocessing.filter.CaseBaseFilter;
import org.jLOAF.sim.complex.WeightedMean;
import org.jLOAF.weights.SimilarityWeights;

public class WeightsSeperatorFilter extends CaseBaseFilter {

	public WeightsSeperatorFilter(CaseBaseFilter f) {
		super(f);
		
	}

	@Override
	public CaseBase filter(CaseBase initial) {
		Input i=null;
		for(Case c:initial.getCases()){
		 i= ((StateBasedInput)c.getInput()).getInput();
			break;
		}
		
		ComplexInput i1 =null;
			try{
				 i1= (ComplexInput)i;	
			}catch(ClassCastException e){
				System.out.println(e + "first");
				return initial;
			}
			
			if(i1.getSimilarityMetricStrategy() instanceof WeightedMean){
				SimilarityWeights sim =((WeightedMean)i1.getSimilarityMetricStrategy()).getSimilarityWeights();
			for(Case c:initial.getCases()){
				ComplexInput i2 =null;
				try{
					i2=(ComplexInput)((StateBasedInput)c.getInput()).getInput();;
				}catch (ClassCastException e){
					System.out.println(e);
					return initial;
				}
				for(String w:i2.getChildNames()){
					if(sim.getWeight(w)==0){
						i2.getChildren().remove(w);
					}
				}
				
			}
		}
		
		return initial;
	}

}
