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
import org.jLOAF.matlab.BayesianNetworkRemote;

public class BayesianReasoner extends Reasoning {
	private CaseBase cb;
	private String filename = "Bayesian_csv.txt";
	BayesianNetworkRemote bnet = null;
	List<String> actions;
	
	public BayesianReasoner(CaseBase cb) {
		super(null);
		this.cb = cb;
		try {
			actions = CaseBase.getActionNames(cb);
			CaseBase.saveAsTrace(cb,filename, false);
			int numFeatures = checkNumFeatures();
			bnet = new BayesianNetworkRemote(filename,numFeatures,1);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	/***
	 * There is no returning a list of cases in this situation so we don't use this method
	 * ***/
	public Action mostLikelyAction(List<Case> nn) {
		return null;
	}
	
	@Override
	public Action selectAction(Input i){
		HashMap<String, Double> temp = CaseBase.convert(i);
		List<Double> input = new ArrayList<Double>();
		
		for(String key: temp.keySet()){
			input.add(temp.get(key));
		}
		
		List<Double> output = bnet.run(input);
		
		int max = 0;
		for(int ii = 0;ii<output.size();ii++) {
			if (output.get(ii)>output.get(max)) max = ii;
		}	
		return new Action(actions.get(max));
	}
	
	private int checkNumFeatures() throws IOException{
		BufferedReader br;
		String[] input = {};
		try {
			br = new BufferedReader(new FileReader(filename));
			String line = br.readLine();
			br.close();
			input = line.split(",");	
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
