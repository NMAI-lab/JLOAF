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
import org.jLOAF.matlab.DynamicBayesianNetworkRemote;

public class DynamicBayesianReasoner extends Reasoning {
	
	private String filename = "C:/Users/sachagunaratne/Documents/GitHub/JLOAF-VacuumCleaner/Bayesian_csv.txt";
	DynamicBayesianNetworkRemote bnet = null;
	List<Action> actions;
	int EmIter = 10;
	
	int state = 0;//initial state variable
	int new_state =0;//new state variable
	int action = 0;//action variable
	int timestep = 0;
	
	public DynamicBayesianReasoner(CaseBase cb) {
		super(null);
		try {
			actions = CaseBase.getActionNames(cb);
			CaseBase.saveAsTrace(cb,filename, false);
			int numFeatures = checkNumFeatures();
			bnet = new DynamicBayesianNetworkRemote(filename,numFeatures,EmIter);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public Action selectAction(Input i){
		HashMap<String, Double> temp = CaseBase.convert(i);
		List<Double> X = new ArrayList<Double>();
		for(String key: temp.keySet()){
			X.add(temp.get(key)+1.0);
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
		
		action = bnet.getAction(input); //get the output of actions for the specific perceptions and state

		if(timestep >0){
			state = new_state;//update the state
		}
		
		timestep++;
		
		return actions.get(action-1);
	}
	
	public void replaceLastAction(String action){
		this.action = Integer.parseInt(action);	
	}

	@Override
	public Action mostLikelyAction(List<Case> nn) {
		return null;
	}
	
	/***
	 * Calculates the number of features by reading the csv file that was created using CaseBase.SaveAsTrace
	 * @author sachagunaratne
	 * ***/
	private int checkNumFeatures() throws IOException{
		BufferedReader br=null;
		String[] input = {};
		String line ="";
		try {
			br = new BufferedReader(new FileReader(filename));
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
}
