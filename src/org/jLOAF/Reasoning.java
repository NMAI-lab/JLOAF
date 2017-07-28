package org.jLOAF;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import org.jLOAF.action.Action;
import org.jLOAF.casebase.Case;
import org.jLOAF.casebase.CaseBase;
import org.jLOAF.inputs.Input;
import org.jLOAF.reasoning.Reasoners;
import org.jLOAF.retrieve.Retrieval;
/*
 * A reasoner object is used by the agent to find actions based on given inputs.
 * this class is the parent of all reasoner that extend it, it provides Common method that are used by the children.
 * 
 */
public abstract class Reasoning {
	protected Retrieval ret;
	/*
	 * constructor
	 * @param r a retrieval that is used by the reasoner to retrieve actions from a casebase.
	 */
	public Reasoning(Retrieval r){
		this.ret =r;
	}
		/*
		 * selects an action, using the retrival it has, of a given input.
		 * @return an action that is well suited for a given input, based on the retrival and the casebase.
		 */
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
	/*
	 * a static method only used for testing purposes, it returns a reasoner based on a string representation of its name.
	 * @param a the name of the reasoner to be returned.
	 * @cb the casebase that will be studied by the agent, and used by the reasner to select actions
	 * @return a reasoner based on a string representation of its name 
	 */
	public static Reasoning getReasoner(String a,CaseBase cb){
			Reasoners.valueOf("cb").setCaseBase(cb);
			if(a==null){
				return null;
			}
		return Reasoners.valueOf(a).getR();
	}
	
}
