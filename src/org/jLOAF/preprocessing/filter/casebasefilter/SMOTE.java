package org.jLOAF.preprocessing.filter.casebasefilter;

import java.util.HashMap;
import java.util.List;

import org.jLOAF.action.Action;
import org.jLOAF.casebase.Case;
import org.jLOAF.casebase.CaseBase;
import org.jLOAF.inputs.StateBasedInput;
import org.jLOAF.preprocessing.filter.CaseBaseFilter;
import org.jLOAF.sim.StateBasedSimilarity;

public class SMOTE extends CaseBaseFilter {

	public SMOTE(CaseBaseFilter f) {
		super(f);
		
	}

	@Override
	public CaseBase filter(CaseBase initial) {
		if(filter!=null){
			initial=filter.filter(initial);
		}
		List<Action> actions = CaseBase.getActionNames(initial);
		HashMap<String, CaseBase> casebases = new HashMap<String, CaseBase>();
		//separates the classes into different casebases
		for(Action a: actions) {
			CaseBase temp = new CaseBase();
			for(Case c: initial.getCases()) {
				if(c.getAction().equals(a)) {
					temp.createThenAdd(c.getInput(), c.getAction(),(StateBasedSimilarity)c.getInput().getSimilarityMetricStrategy() );
				}
			}
			casebases.put(a.getName(),temp);
		}
		
		//make each casebase the same size? Using oversampling and undersampling
		//undersampling removes cases from the large cases
		//oversampling involves making synthetic data using SMOTE
		
		return null;
	}

}
