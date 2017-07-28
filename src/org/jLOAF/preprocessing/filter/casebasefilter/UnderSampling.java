package org.jLOAF.preprocessing.filter.casebasefilter;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.jLOAF.action.Action;
import org.jLOAF.casebase.Case;
import org.jLOAF.casebase.CaseBase;
import org.jLOAF.inputs.StateBasedInput;
import org.jLOAF.preprocessing.filter.CaseBaseFilter;
import org.jLOAF.sim.StateBasedSimilarity;
/**
 * This class undersamples the majority class by removing a percentage of the majority class cases based on the value of
 * underSamplePercent which can range from [0,1] where 1 represents equalizing the majority and minority classes, and 0 represents not removing
 * cases.
 *  * @author sachagunaratne
 *
 */
public class UnderSampling extends CaseBaseFilter {
	double underSamplePercent = 0.5;
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
		
		//find smallest casebase
		int min = sizes.get(actions.get(0).getName());
		int index =0;
		int count =0;
		for(Action a:actions){
			if(sizes.get(a.getName())<min){min=sizes.get(a.getName());index=count;}
			count++;
		}
  		String smallest = actions.get(index).getName();

  		//undersample based on the percentage
  		//1->majority class is as same as the minority class
  		//0->No undersampling performed
//		count=0;
//		for(Action a:actions){
//			if(!a.getName().equals(smallest)){
//				for(Case c: casebases.get(a.getName()).getCases()){
//					initial.remove(c);
//					if(count>=(sizes.get(a.getName())-min)*underSamplePercent){
//						break;
//					}
//					count++;
//				}
//			}
//			count=0;
//		}
		
  		Random r = new Random();
  		//random undersampling
		for(Action a:actions){
			if(!a.getName().equals(smallest)){
				CaseBase temp = casebases.get(a.getName());
				for(int i=0;i<temp.getSize();i++){
					Case c = (Case)temp.getCases().toArray()[r.nextInt(temp.getSize())];
					initial.remove(c);
					temp.remove(c);
					if(i>=(sizes.get(a.getName())-min)*underSamplePercent){
						break;
					}			
				}
			}
		}
		
		return initial;
	}

}
