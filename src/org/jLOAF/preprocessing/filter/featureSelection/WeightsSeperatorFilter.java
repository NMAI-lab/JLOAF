package org.jLOAF.preprocessing.filter.featureSelection;

import org.jLOAF.casebase.Case;
import org.jLOAF.casebase.CaseBase;
import org.jLOAF.inputs.ComplexInput;
import org.jLOAF.inputs.Input;
import org.jLOAF.preprocessing.filter.CaseBaseFilter;
import org.jLOAF.weights.SimilarityWeights;

public class WeightsSeperatorFilter extends CaseBaseFilter {

	public WeightsSeperatorFilter(CaseBaseFilter f) {
		super(f);
		
	}

	@Override
	public CaseBase filter(CaseBase initial) {
		Input i=null;
		for(Case c:initial.getCases()){
		 i= c.getInput();
			break;
		}
		
		ComplexInput i1 =null;
			try{
				 i1= (ComplexInput)i;	
			}catch(ClassCastException e){
				
				return initial ;
			}
			
			if(i1.getSimilarityMetricStrategy() instanceof SimilarityWeights){
				SimilarityWeights sim =(SimilarityWeights)i1.getSimilarityMetricStrategy();
			for(Case c:initial.getCases()){
				ComplexInput i2 =null;
				try{
					i2=(ComplexInput)c.getInput();
				}catch (ClassCastException e){
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
