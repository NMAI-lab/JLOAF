package org.jLOAF.preprocessing.filter.casebasefilter;

import org.jLOAF.Reasoning;
import org.jLOAF.casebase.Case;
import org.jLOAF.casebase.CaseBase;
import org.jLOAF.preprocessing.filter.CaseBaseFilter;
import org.jLOAF.reasoning.WeightedKNN;

public class Sampling implements CaseBaseFilter {
	/***
	 * preprocess the casebase by only adding a case into the casebase if the prediction of the action using the current casebase is wrong
	 * return the new casebase
	 * sacha gunaratne 2017 may
	 * ***/
	@Override
	public CaseBase filter(CaseBase initial) {
		CaseBase cnew = new CaseBase();
		Reasoning r;
		int count = 0;
		int k = 1;
		int max_k = 7;
		
		for (Case c: initial.getCases()){
			if(count>0){
				cnew.add(c);
				//max k = 7
				if(count>0&&count<max_k)k=count;
				r = new WeightedKNN(k,cnew);
				if(r.selectAction(c.getInput()).equals(c.getAction())) {cnew.remove(c);count++;}
			}
			else{
				cnew.add(c);
				count++;
			}
		}
		return cnew;
	}

}
