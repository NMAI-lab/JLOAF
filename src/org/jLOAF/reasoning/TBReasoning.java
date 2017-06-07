package org.jLOAF.reasoning;

import java.util.ArrayList;
import java.util.List;

import org.jLOAF.Reasoning;
import org.jLOAF.action.Action;
import org.jLOAF.casebase.Case;
import org.jLOAF.casebase.CaseBase;
import org.jLOAF.inputs.Input;
import org.jLOAF.inputs.StateBasedInput;


public class TBReasoning extends Reasoning {
	
	private CaseBase cb;
	double CPT=0.99;
	double PPT=0.99;
	double PAT=0;
	public TBReasoning(CaseBase cb) {
		super(null);
		this.cb=cb;
		}

	/*public Action stateRetrieve(Input i,ArrayList<Case> nn,int time){
		double threshold;
		if(time == 0){
			threshold = CPT;
		}else{
			threshold = PPT;
		}
		
		ArrayList <Case> possible = new ArrayList<Case>();
		ArrayList <Action> nnactions = new ArrayList<Action>();
		double bestSim = -1;
		Case bestRun = null;
		int currSize=((StateBasedInput)i).size();
		for(Case train: nn){
			
			double sim=-1;
			
			int trainSize=((StateBasedInput)train.getInput()).size();
			if(time >=currSize || time >= trainSize){
				sim=-1;
			}else{
				sim=((Input)((StateBasedInput) i).get(currSize-1-time)).similarity(((Input)((StateBasedInput) train.getInput()).get(trainSize-1-time)));
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
			return actionRetrieve(i,possible, time+1);
		}
	}
	*/
	public Action retrieve(Input i, ArrayList<Case> nn, int time) {
		double threshold = PAT;
		
		ArrayList <Case> possible = new ArrayList<Case>();
		ArrayList <Action> nnactions = new ArrayList<Action>();
		
		double bestSim = -1;
		Case bestRun = null;
		int currSize=((StateBasedInput)i).size();
		for(Case train: nn){
		
			double sim=-1;
			
			int trainSize=((StateBasedInput)train.getInput()).size();
			if(time >=currSize || time >= trainSize){
				sim=-1;
			}else{
				if(time%2==0){
					sim=((Input)((StateBasedInput) i).get(currSize-1-time)).similarity(((Input)((StateBasedInput) train.getInput()).get(trainSize-1-time)));
					threshold = PPT;
				}else{
					sim=((Action)((StateBasedInput) i).get(currSize-1-time)).similarity(((Action)((StateBasedInput) train.getInput()).get(trainSize-1-time)));
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

	@Override
	public Action selectAction(Input i){
		return retrieve(i,new ArrayList<Case>(cb.getCases()),0);
	}
	
	
	@Override
	public Action mostLikelyAction(List<Case> nn) {
		// TODO Auto-generated method stub
		return null;
	}

	

	
}
