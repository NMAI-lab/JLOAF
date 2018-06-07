package org.jLOAF.reasoning;

import java.util.ArrayList;
import java.util.List;

import org.jLOAF.Reasoning;
import org.jLOAF.action.Action;
import org.jLOAF.casebase.Case;
import org.jLOAF.casebase.CaseBase;
import org.jLOAF.inputs.Input;
import org.jLOAF.inputs.StateBasedInput;

/**
 * Creates a TBReasoning reasoner. 
 * @author sachagunaratne
 *
 */
public class TBReasoning extends Reasoning {
	
	private CaseBase cb;
	double PT=0.99;
	double ST=0.99;
	double PAT=0;
	public TBReasoning(CaseBase cb) {
		super(null);
		this.cb=cb;
		}
	
	/**
	 * This function implements the Temporal Backtracking algorithm. It dynamically backtracks through each run, comparing input and action
	 * to the current input until there is consensus on the most likely action performed. 
	 * @author Ali Fawaz
	 * @param i A stateBasedInput
	 * @param nn The currentList of best cases
	 * @param time The time step
	 * @return Action The most likely action
	 */
	public Action retrieve(StateBasedInput i, ArrayList<Case> nn, int time) {
		double threshold = PAT;
		
		ArrayList <Case> possible = new ArrayList<Case>();
		ArrayList <Action> nnactions = new ArrayList<Action>();
		
		double bestSim = -1;
		Case bestRun = null;
		
		for(Case train: nn){
		
			double sim=-1;
			
			if( i.getInput(time)==null|| ((StateBasedInput)train.getInput()).getInput(time)==null){
				sim=-1;
			}else{
				if(time%2==0){
					sim=i.getInput(time).similarity(((StateBasedInput) train.getInput()).getInput(time));
					threshold = PT;
				}else{
					sim=i.getAction(time).similarity(((StateBasedInput) train.getInput()).getAction(time));
					threshold = ST;
				}
				
				if(sim > bestSim){
					bestSim = sim;
					bestRun = train;
				}
				if(sim > threshold){
					possible.add(train);
					if(!nnactions.contains(train.getAction())){
						nnactions.add(train.getAction());
					}
				}
			}
				
		}
			
		if(bestRun == null){
			return nn.get(0).getAction();
		}
		if(possible.size() == 0){
			return bestRun.getAction();
		}else if(nnactions.size() == 1){
			return nnactions.get(0);
		}else{
			return retrieve(i,possible, time+1);
		}
			
			
			
			
			
		
			
		
	}
	/**
	 * Returns the most likely action
	 * @param i An Input
	 * @return Action The most likely action
	 */
	@Override
	public Action selectAction(Input i){
		StateBasedInput i1=(StateBasedInput)i;
		return retrieve(i1,new ArrayList<Case>(cb.getCases()),0);
	}
	
	
	@Override
	public Action mostLikelyAction(List<Case> nn) {
		// TODO Auto-generated method stub
		return null;
	}

	

	
}
