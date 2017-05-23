package org.jLOAF;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import org.jLOAF.action.Action;
import org.jLOAF.casebase.Case;
import org.jLOAF.inputs.Input;
import org.jLOAF.retrieve.Retrieval;

public abstract class Reasoning {
	protected Retrieval ret;

	public Reasoning(Retrieval r){
		this.ret =r;
	}
	
	public Action selectAction(Input i) {
		List<Case> nn = ret.retrieve(i);
		
		return mostLikelyAction(nn);
	}
	/*
	 * takes a list of Actions and returns the most likely one
	 * @param nn a final list of most closest cases to the current examined case
	 * @return an action to be performed
	 */
	public abstract Action mostLikelyAction(List<Case> nn);
	
	/*
	 * 
	 * Calculates the msot likely action given a matrix of actions and counts
	 * @author Sacha gunaratne
	 * @since May 2017
	 * **/
	
	//need to implement a weighted knn with distance 
	public String max(Hashtable<String, Double> h){
		Enumeration<String> actions;
		
		actions = h.keys();
		double max = 0;
		String action = "";
		
		while(actions.hasMoreElements()){
			String val = (String) actions.nextElement();
			if( h.get(val)>max){
				max = h.get(val);
				action = val;
			}
		}
		
		return action;
	}

	
	
}
