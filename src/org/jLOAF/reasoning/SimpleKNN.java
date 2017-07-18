package org.jLOAF.reasoning;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import org.jLOAF.Reasoning;
import org.jLOAF.action.Action;
import org.jLOAF.casebase.Case;
import org.jLOAF.casebase.CaseBase;
import org.jLOAF.inputs.Input;
import org.jLOAF.retrieve.Distance;
import org.jLOAF.retrieve.Retrieval;
import org.jLOAF.retrieve.kNN;
/**
 * Creates a SimpleKNN reasoner.
 * @author sachagunaratne
 *
 */
public class SimpleKNN extends Reasoning {

	
	
	public SimpleKNN(int k, CaseBase cb){
		super(new kNN(k, cb));
	}
	
	
	/**
	 * Takes the top k cases and chooses the majority class as the class and returns it.
	 *  
	 * @param nn A list of the top k Cases
	 * @return Action The most likely action
	 */
	@Override
	public Action mostLikelyAction(List<Case> nn){
		Hashtable<String, Double> nnactions = new Hashtable<String, Double>();
		String max_action;
		List<Action> a = new ArrayList<Action>();
		
		for(int i =0;i<nn.size();i++){
			if(!nnactions.containsKey(nn.get(i).getAction().getName())){//hashtable to account for number of times an action is chosen
				nnactions.put(nn.get(i).getAction().getName(), 1.0);
			}else{
				double value = nnactions.get(nn.get(i).getAction().getName());
				nnactions.put(nn.get(i).getAction().getName(), (value+1));
			}
		}
		//System.out.println(nnactions);
		max_action = max(nnactions);
		//run through all the cases and only select the first action with that name, as it will be the closest in terms of distance
		for(Case c: nn){
			if(c.getAction().getName().equals(max_action)){
				a.add(c.getAction());
				break;
			}
		}
		
		return a.get(0);
	}
	
	

}
