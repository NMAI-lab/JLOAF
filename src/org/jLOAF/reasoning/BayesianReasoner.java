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
import org.jLOAF.matlab.BayesianNetworkRemote;
/**
 * This class creates a BayesianReasoner which has a BayesianNetworkRemote. 
 * @author sachagunaratne
 *
 */
public class BayesianReasoner extends Reasoning {
	BayesianNetworkRemote bnet = null;
	List<Action> actions;
	List<String> feature_names;
	double placeholder = 6.6;
	CaseBase cb;
	String output_filename;
	boolean train = true;
	
	public BayesianReasoner(CaseBase cb, String output_filename) {
		super(null);
		this.output_filename = output_filename;
		this.cb = cb;
	}
	
	public void setTrain(){
		train=true;
	}

	/**
	 * There is no returning a list of cases in this situation so we don't use this method
	 * @param nn A list of the top K cases
	 * @return null 
	 * */
	@Override
	public Action mostLikelyAction(List<Case> nn) {
		return null;
	}
	/**
	 * This function initially trains the agent when called the first time. It converts the CaseBase to a tracefile. Gets the feature names,
	 * action names, and passes it to the BayesianNetworkRemote(). 
	 * 
	 * It then will convert the input into a hashMap of inputs. It will put placeholderValues into the list of inputs so that if there are
	 * missing values they will be supplemented by the placeholder. 
	 * 
	 * The inputs are then matched with the index of the feature_names so that the correct feature_value is sent as evidence to Matlab. 
	 * 
	 * The result is a list of probabilities and the highest is chosen and the corresponding action is returned.  
	 * @param i An input
	 * @return Action The most probable action 
	 */
	@Override
	public Action selectAction(Input i){
		
		if(train){
			try {
				actions = CaseBase.getActionNames(cb);
				feature_names = CaseBase.saveAsTrace(cb,output_filename, false);
				int numFeatures = checkNumFeatures(output_filename);
				bnet = new BayesianNetworkRemote(output_filename,numFeatures,1);
			} catch (IOException e) {
				e.printStackTrace();
			}
			train = false;
		}
		
		i = ((StateBasedInput)i).getInput();
		HashMap<String, Double> temp = CaseBase.convert(i);
		List<Double> input = new ArrayList<Double>();
		
		for(int ii=0;ii<feature_names.size();ii++){input.add(placeholder);}
		
		for(String key: temp.keySet()){
			int index = feature_names.indexOf(key);
			input.remove(index);
			input.add(index, temp.get(key));	
		}
		
		List<Double> output = bnet.run(input);
		
		int max = 0;
		for(int ii = 0;ii<output.size();ii++) {
			if (output.get(ii)>output.get(max)) max = ii;
		}
		return actions.get(max);
	}
	
	/***
	 * Calculates the number of features by reading the csv file that was created using CaseBase.SaveAsTrace
	 * @author sachagunaratne
	 * @param filename The trace file
	 * @return numFeatures the number of features present in the tracefile
	 * ***/
	private int checkNumFeatures(String filename) throws IOException{
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
