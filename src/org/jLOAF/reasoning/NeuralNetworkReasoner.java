package org.jLOAF.reasoning;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
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
import org.jLOAF.matlab.NeuralNetworkOrderKRemote;
import org.jLOAF.matlab.NeuralNetworkRemote;
import org.jLOAF.retrieve.Retrieval;

/**
 * This class creates a NeuralNetworkReasoner that has a NeuralNetworkRemote instance.
 * @author sachagunaratne
 *
 */
public class NeuralNetworkReasoner extends Reasoning {


	NeuralNetworkRemote nnet; 
	List<Action> actions;
	List<String> feature_names;
	double placeholder = 6.6;
	String transition_file = "Data/transition_file.txt";
	public NeuralNetworkReasoner(CaseBase cb, String output_filename) {
		super(null);
		try {
			actions = CaseBase.getActionNames(cb);
			feature_names = CaseBase.saveAsTrace(cb,transition_file, false);
			int numFeatures = checkNumFeatures(transition_file);
			convertToNNet(transition_file, output_filename);
			nnet = new NeuralNetworkRemote(output_filename,numFeatures,actions.size());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/***
	 * Converts the class labels to one hot encoding form from ordinal form.
	 * @param String input_filename String outpu_filename
	 * @return nothing
	 * 
	 *  @author sachagunaratne
	 * **/
	private void convertToNNet(String input_filename, String output_filename) throws IOException {
		BufferedReader br=null;
		FileWriter fw = null;
		String[] input = {};
		String line ="";
		try {
			br = new BufferedReader(new FileReader(input_filename));
			fw = new FileWriter(output_filename);
			while((line = br.readLine())!=null){
				input = line.split(",");
				//write original input
				for(int i=0;i<input.length-1;i++){
					fw.write(input[i]);
					fw.write(",");
				}
				//convert the action to  one hot encoding
				for(int jj=1;jj<actions.size()+1;jj++){
					if(input[input.length-1].equals(String.valueOf((double)jj))){
						fw.write("1");
						fw.write(",");
					}else{
						fw.write("0");
						fw.write(",");
					}
				}
				fw.write("\n");
			}
			fw.close();
			br.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Action mostLikelyAction(List<Case> nn) {
		// TODO Auto-generated method stub
		return null;
	}

	/***
	 * Calculates the number of features by reading the csv file that was created using CaseBase.SaveAsTrace
	 * @author sachagunaratne
	 * @param Filename the trace file
	 * @return int numFeature the number of features in the trace file
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
	
	/**
	 * This method takes an input, converts it into a list and passes it to NeuralNetworkRemote to get the most likely action.
	 * 
	 * @param i An Input
	 * @return Action The most probable action
	 */
	@Override
	public Action selectAction(Input i){
		i = ((StateBasedInput)i).getInput();
		HashMap<String, Double> temp = CaseBase.convert(i);
		List<Double> input = new ArrayList<Double>();

		for(int ii=0;ii<feature_names.size();ii++){input.add(placeholder);}

		for(String key: temp.keySet()){
			int index = feature_names.indexOf(key);
			input.remove(index);
			input.add(index, temp.get(key)+1.0);	
		}

		List<Double> output = nnet.run(input);

		int max = 0;
		for(int ii = 0;ii<output.size();ii++) {
			if (output.get(ii)>output.get(max)) max = ii;
		}

		return actions.get(max);
	}
}
