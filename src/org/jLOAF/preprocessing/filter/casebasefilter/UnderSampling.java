package org.jLOAF.preprocessing.filter.casebasefilter;

import java.util.HashMap;
import java.util.List;

import org.jLOAF.action.Action;
import org.jLOAF.casebase.Case;
import org.jLOAF.casebase.CaseBase;
import org.jLOAF.inputs.StateBasedInput;
import org.jLOAF.preprocessing.filter.CaseBaseFilter;
import org.jLOAF.sim.StateBasedSimilarity;

public class UnderSampling extends CaseBaseFilter {
	double underSamplePercent = 0;
	public UnderSampling(CaseBaseFilter f) {
		super(f);
	}

	@Override
	public CaseBase filter(CaseBase initial) {
		if(filter!=null){
			initial=filter.filter(initial);
		}
		List<Action> actions = CaseBase.getActionNames(initial);
		HashMap<String, CaseBase> casebases = new HashMap<String, CaseBase>();
		HashMap<String, Integer> sizes = new HashMap<String, Integer>();
		//separates the classes into different casebases
		for(Action a: actions) {
			CaseBase temp = new CaseBase();
			for(Case c: initial.getCases()) {
				if(c.getAction().getName().equals(a.getName())) {
					temp.add(c);
				}
			}
			casebases.put(a.getName(),temp);
			sizes.put(a.getName(),temp.getSize());
		}
		
		//make each casebase the same size? Using oversampling and undersampling
		//undersampling removes cases from the large cases
		//oversampling involves making synthetic data using SMOTE
		
		//find largest casebase
				
		int min = sizes.get(actions.get(0).getName());
		int index =0;
		int count =0;
		for(Action a:actions){
			if(sizes.get(a.getName())<min){min=sizes.get(a.getName());index=count;}
			count++;
		}
  		String smallest = actions.get(index).getName();
		double underSamplePercent =0;
		count=0;
		for(Action a:actions){
			if(!a.getName().equals(smallest)){
				for(Case c: casebases.get(a.getName()).getCases()){
					initial.remove(c);
					if(count>=(sizes.get(a.getName())-min)*underSamplePercent){
						break;
					}
					count++;
				}
			}
			count=0;
		}
		
		return initial;
	}

}
