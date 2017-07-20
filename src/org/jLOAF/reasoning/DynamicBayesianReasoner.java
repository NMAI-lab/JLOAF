package org.jLOAF.reasoning;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.jLOAF.Reasoning;
import org.jLOAF.action.Action;
import org.jLOAF.casebase.Case;
import org.jLOAF.casebase.CaseBase;
import org.jLOAF.inputs.Input;
import org.jLOAF.inputs.StateBasedInput;
import org.jLOAF.matlab.DynamicBayesianNetworkRemote;
/**
 * This class creates a DynamicBayesianReasoner which has a DynamicBayesianNetworkRemote. 
 * @author sachagunaratne
 *
 */
public class DynamicBayesianReasoner extends Reasoning {
	
	DynamicBayesianNetworkRemote bnet = null;
	List<Action> actions;
	List<String> feature_names;
	int EmIter = 10;
	double placeholder = 6.6;
	CaseBase cb;
	String output_filename;
	boolean train = true;
	
	int state = 0;//initial state variable
	int new_state =0;//new state variable
	int action = 0;//action variable
	int timestep = 0;
	
	public DynamicBayesianReasoner(CaseBase cb, String output_filename) {
		super(null);
		this.output_filename = output_filename;
		this.cb = cb;
	}
	
	
	/**
	 * This method takes and input, sets the inputs in the correct index based on the indices of the feature names, and then depending on the timesetep
	 * will get the intiialState or newState, add it onto the list of inputs and pass it on to the getAction function.
	 * This will return the most probable action as an integer.
	 * The state is updated and the most probable action is returned
	 *  
	 * @param i An input
	 * @return An Action The most probable action
	 */
	@Override
	public Action selectAction(Input i){
		if(train){
			try {
				actions = CaseBase.getActionNames(cb);
				feature_names = CaseBase.saveAsTrace(cb,output_filename, false);
				int numFeatures = checkNumFeatures(output_filename);
				bnet = new DynamicBayesianNetworkRemote(output_filename,numFeatures,EmIter);
			} catch (IOException e) {
				e.printStackTrace();
			}
			train=false;
		}
		i = ((StateBasedInput)i).getInput();
		HashMap<String, Double> temp = CaseBase.convert(i);
		List<Double> X = new ArrayList<Double>();
		
		for(int ii=0;ii<feature_names.size();ii++){X.add(placeholder);}
		
		for(String key: temp.keySet()){
			int index = feature_names.indexOf(key);
			X.remove(index);
			X.add(index, temp.get(key)+1.0);	
		}
		
		List<Double> input = new ArrayList<Double>();
		
		if (timestep ==0){
			state = bnet.getInitialState(X); //get initial state
			input.add((double) state); //add to the list of perceptions as evidence
		}else{
			new_state = bnet.getNewState(X, action, state);// get new state based on past action, new perception and past state
			input.add((double) new_state);// to the list of perceptions as evidence
		}
		
		for(int ii = 0;ii<X.size();ii++){
			input.add((X.get(ii))); //add perceptions to the evidence
		}
		
		action = bnet.getAction(input)-1; //get the output of actions for the specific perceptions and state

		if(timestep >0){
			state = new_state;//update the state
		}
		
		timestep++;
		
		return actions.get(action);
	}
	/**
	 * Replaces the action of the agent with the correct action. 
	 * @param action
	 */
	public void replaceLastAction(String action){
		int index=0;
		for(Action a: actions){
			if(a.getName().equals(action)){
				this.action = index;
			}
			index++;
		}
	}

	/**
	 * There is no returning a list of cases in this situation so we don't use this method
	 * @param nn A list of the top K cases
	 * @return null 
	 * *
	@Override
	public Action mostLikelyAction(List<Case> nn) {
		return null;
	}
	
	/***
	 * Calculates the number of features by reading the csv file that was created using CaseBase.SaveAsTrace
	 * @author sachagunaratne
	 * @param filename The trace file
	 * @return numFeatures the number of features present in the tracefile
	 * ***/
	private int checkNumFeatures(String output_filename) throws IOException{
		BufferedReader br=null;
		String[] input = {};
		String line ="";
		try {
			br = new BufferedReader(new FileReader(output_filename));
			line = br.readLine();
			input = line.split(",");	
			br.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		if(input.length==0){
			return 0;
		}else{
			return input.length-1;
		}	
	}


	@Override
	public Action mostLikelyAction(List<Case> nn) {
		// TODO Auto-generated method stub
		return null;
	}
}
