package org.jLOAF.reasoning;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import org.jLOAF.Reasoning;
import org.jLOAF.action.Action;
import org.jLOAF.casebase.Case;
import org.jLOAF.casebase.CaseBase;
import org.jLOAF.inputs.Input;
import org.jLOAF.retrieve.Retrieval;
import org.jLOAF.retrieve.kNN;

public class SimpleKNN implements Reasoning {

	private Retrieval ret;
	
	public SimpleKNN(int k, CaseBase cb){
		ret = new kNN(k, cb);
	}
	
	@Override
	public Action selectAction(Input i) {
		List<Case> nn = ret.retrieve(i);
		
		return mostLikelyAction(nn);
	}
	
	public Action mostLikelyAction(List<Case> nn){
		Hashtable<String, Integer> nnactions = new Hashtable<String, Integer>();
		
		for(int i =0;i<nn.size();i++){
			if(!nnactions.containsKey(nn.get(i).getAction().getName())){//hashtable to account for number of times an action is chosen
				nnactions.put(nn.get(i).getAction().getName(), 1);
			}else{
				int value = (int) nnactions.get(nn.get(i).getAction().getName());
				nnactions.put(nn.get(i).getAction().getName(), value+1);
			}
		}
		return max(nnactions);
	}
	
	/**
	 * Sacha 2017 Mar
	 * Calculates the msot likely action given a matrix of actions and counts
	 * 
	 * **/
	public Action max(Hashtable<String, Integer> h){
		Enumeration<String> actions;
		
		actions = h.keys();
		int max = 0;
		String action = "";
		
		while(actions.hasMoreElements()){
			String val = (String) actions.nextElement();
			if((int) h.get(val)>max){
				max = (int) h.get(val);
				action = val;
			}
		}
		
		return new Action(action);
	}

}
