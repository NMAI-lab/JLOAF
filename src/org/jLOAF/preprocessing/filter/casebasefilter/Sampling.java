package org.jLOAF.preprocessing.filter.casebasefilter;

import org.jLOAF.Reasoning;
import org.jLOAF.casebase.Case;
import org.jLOAF.casebase.CaseBase;
import org.jLOAF.preprocessing.filter.CaseBaseFilter;
import org.jLOAF.reasoning.SimpleKNN;
import org.jLOAF.reasoning.WeightedKNN;
/**
 * Preprocess the CaseBase by only adding a case into the CaseBase if the prediction of the action using the current CaseBase is wrong
 * return the new CaseBase
 * @author sacha gunaratne 
 * @since 2017 may
 * ***/
public class Sampling extends CaseBaseFilter {
	/**
	 * Constructor 
	 * @param f the CasebaseFilter to be passed to this filter
	 * @see org.JLOAF.preprocessing.filter.CaseBaseFilter
	 */
	public Sampling(CaseBaseFilter f) {
		super(f);
		
	}

	/**
	 * Takes a CaseBase, adds one case to a new CaseBase. Trains a reasoner on the new CaseBase. Uses it to see if it can predict the class of a case
	 * from the old CaseBase. If it can then don't add that case to the new CaseBase. Else do so. Repeat.
	 * @param initial A CaseBase
	 * @return CaseBase filtered CaseBase
	 */
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
