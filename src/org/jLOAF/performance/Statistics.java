package org.jLOAF.performance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
	
	/** Used to get the current classification accuracy of the Agent
	 * 
	 * @return The classification accuracy
	 *
	 * @author Michael W. Floyd
	 * @since 0.3
	 *
	 */
	public float getClassificationAccuracy(){
		int correct = 0;
		int total = 0;
		
		Set<String> allRows = this.confusion_matrix.keySet();
		
		//go through each of the rows in the matrix
		for(String currentRow: allRows){
			Map<String,Integer> thisRow = this.confusion_matrix.get(currentRow);
			//now we get all the non-zero cells for this row
			Set<String> nonzeroCols = thisRow.keySet();
			for(String col: nonzeroCols){
				Integer cellValue = thisRow.get(col);
				//if the label is the same it represents a correct match
				if(currentRow.equals(col)){
					correct += cellValue.intValue();
				}
				total += cellValue.intValue();
			}
		}
		
		//make sure we don't divide by zero
		if(total == 0){
			return 0.0f;
		}
		return (float)correct/(float)total;
	}
	
	/** Used to get the current recall of the Agent for a specific type of
	 * action.
	 * 
	 * @param action The action to get the recall for
	 * @return The recall
	 *
	 * @author Michael W. Floyd
	 * @since 0.3
	 */
	public float getPrecision(String action){
		if(action == null){
			throw new IllegalArgumentException("Null String given to getPrecision()");
		}
		
		int correct = 0;
		int total = 0;
		
		//go through each row
		Set<String> allRows = this.confusion_matrix.keySet();
		for(String currentRow: allRows){
			Map<String,Integer> rowMap = this.confusion_matrix.get(currentRow);
			Integer cellVal = rowMap.get(action);
			
			//see if there was an entry in this col for that action
			if(cellVal != null){
				//see if it was a correct match
				if(currentRow.equals(action)){
					correct += cellVal.intValue();
				}
				total += cellVal.intValue();
			}
		}
		
		if(total == 0){
			return 0.0f;
		}
		
		return (float)correct/(float)total;
	}
	
	/** Used to get the current recall of the Agent for a specific type of
	 * action.
	 * 
	 * @param action The action to get the recall for
	 * @return The recall
	 *
	 * @author Michael W. Floyd
	 * @since 0.3
	 */
	public float getRecall(String action){
		if(action == null){
			throw new IllegalArgumentException("Null String given to getRecall()");
		}
		
		Map<String,Integer> actionRow = this.confusion_matrix.get(action);
		
		//we never had an expected action of this type
		if(actionRow == null){
			return 0.0f;
		}
		
		int correct = 0;
		int total = 0;
		
		//get all the actions the agent performed when it should have been the expected action
		Set<String> colEntries = actionRow.keySet();
		for(String currentCol: colEntries){
			Integer cellVal = actionRow.get(currentCol);
			//if they are the same, it means a correct match
			if(action.equals(currentCol)){
				correct += cellVal.intValue();
			}
			total += cellVal.intValue();
			
		}
		
		return (float)correct/(float)total;
	}

	/** Used to get the current f1-measure of the Agent for a specific type of
	 * action.
	 *  
	 * @param action The action to get the f1 for
	 * @return The f1 value
	 * 
	 * @author Michael W. Floyd
	 * @since 0.3
	 */
	public float getF1(String action){
		if(action == null){
			throw new IllegalArgumentException("Null value given to getF1");
		}
		
		float prec = getPrecision(action);
		float recall = getRecall(action);
		float numer = 2*prec*recall;
		float denom = prec + recall;
		
		if(denom == 0.0f){
			return 0.0f;
		}
		return numer/denom;
	}
	
	/** Used to get the current global f1-measure of the Agent.
	 *  
	 * @return The global f1 value
	 * 
	 * @author Michael W. Floyd
	 * @since 0.3
	 */
	public float getGlobalF1(){
		int num = 0;
		float sumF1 = 0;
		
		//make a list of all actions in the table
		Set<String> allRow = this.confusion_matrix.keySet();
		List<String> completeList = new ArrayList<String>(allRow);
		
		//update the complete list with ones that were never matched properly
		//this is necessary since the metric is an average of all actions (even zeros)
		for(String currentRow: allRow){
			Map<String,Integer> cols = this.confusion_matrix.get(currentRow);
			Set<String> colNames = cols.keySet();
			for(String col: colNames){
				if(!completeList.contains(col)){
					completeList.add(col);
				}
			}
		}
		
		//now we sum the f-measures for each action type
		for(String currentAction: completeList){
			sumF1 += getF1(currentAction);
			num++;
		}

		//make sure we don't divide by zero
		if(num == 0){
			return 0.0f;
		}
		return sumF1/num;
	}

}
