package org.jLOAF.preprocessing.filter.casebasefilter;

import org.jLOAF.Reasoning;
import org.jLOAF.casebase.Case;
import org.jLOAF.casebase.CaseBase;
import org.jLOAF.preprocessing.filter.CaseBaseFilter;
import org.jLOAF.reasoning.SimpleKNN;
import org.jLOAF.reasoning.WeightedKNN;
/**
 * preprocess the casebase by only adding a case into the casebase if the prediction of the action using the current casebase is wrong
 * return the new casebase
 * @author sacha gunaratne 
 * @since 2017 may
 * ***/
public class Sampling extends CaseBaseFilter {
	/**
	 * Constructor 
	 * @param f the CasebaseFilter to be passed to this filter
	 */
	public Sampling(CaseBaseFilter f) {
		super(f);
		
	}

	
	@Override
	public CaseBase filter(CaseBase initial) {
		if(filter!=null){
			initial=filter.filter(initial);
		}
		CaseBase cnew = new CaseBase();
		Reasoning r;
		int count = 0;
		int k = 1;
		int max_k = 7;
		
		for (Case c: initial.getCases()){
			if(count>0){
				
				//max k = 7
				if(count>0&&count<max_k && count%2==0){k=count;}
				r = new WeightedKNN(k,cnew);
				if(!(r.selectAction(c.getInput()).getName().equals(c.getAction().getName()))) {
					cnew.add(c);
					count++;
				}
			}
			else{
				cnew.add(c);
				count++;
			}
		}
		return cnew;
	}

}
