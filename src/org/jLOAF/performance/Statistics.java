package org.jLOAF.performance;

import java.util.HashMap;

import org.jLOAF.Agent;
import org.jLOAF.action.Action;
import org.jLOAF.casebase.Case;
/***
 * Creates statistics for testing performance of imitating agents
 * Sacha Gunaratne 2017
 * ***/
public class Statistics {
	
	private Agent agent;
	private HashMap  <String, HashMap<String, Integer>> confusion_matrix;
	
	/***
	 * Creates a statistics object with an agent, and initializes a confusion matrix
	 * ***/
	public Statistics(Agent agent){
		this.agent = agent;
		this.confusion_matrix= new HashMap<String, HashMap<String, Integer>>();
	}
	
	/***
	 * Takes a case as an input, the agent predicts an action and then the correct and predicted actions are
	 * added to the confusion matrix
	 * ***/
	public void predictedCorrectActionName(Case input){
		Action correctAction = input.getAction();
		Action predictedAction = agent.getR().selectAction(input.getInput());
		addPair2ConfusionMatrix(correctAction.getName(),predictedAction.getName());
	}
	
	/***
	 * Adds predicted and correct action pairs to the confusion matrix
	 * ***/
	private void addPair2ConfusionMatrix(String correct, String predicted){
		
		if (!confusion_matrix.containsKey(correct)){
			confusion_matrix.put(correct, new HashMap<String, Integer>());
		}
		if (!confusion_matrix.get(correct).containsKey(predicted)){
			confusion_matrix.get(correct).put(predicted, 0);
		}
		int value = confusion_matrix.get(correct).get(predicted).intValue();
		confusion_matrix.get(correct).put(predicted, value + 1);
	}

}
