package org.jLOAF.reasoning;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.jLOAF.Reasoning;
import org.jLOAF.action.Action;
import org.jLOAF.casebase.CaseBase;
import org.jLOAF.inputs.Input;
import org.jLOAF.retrieve.BNetRemoteDiscrete;


public class BayesianNetwork implements Reasoning {
	BNetRemoteDiscrete bnet = null;
	Action actions[] = null;
	
	public BayesianNetwork(List<String> traces, int Xsize, Action actions[]){
		bnet = new BNetRemoteDiscrete(traces, Xsize, 1);
		this.actions = actions;
	}
	
	public BayesianNetwork(ArrayList<CaseBase> cbs,String filename, int Xsize){
		int case_num = 0;
		List<String> traces = new ArrayList<String>();
		//takes a collection of cases and then converts each CaseBase into a trace file 
		for(CaseBase cb: cbs){
			filename+=String.valueOf(case_num);
			CaseBase.saveAsTrace(cb, filename);
			traces.add(filename);
			case_num++;
		}
		//relaince on Xsize??
		bnet = new BNetRemoteDiscrete(traces, Xsize, 1);
	}
	
	@Override
	public Action selectAction(Input i) {
		int max =0;
		List <Double> input = convert(i);
		List<Double> output = bnet.run(input);
		for(int ii = 0;ii<output.size();ii++) {
	         if (output.get(ii)>output.get(max)) max = ii;
	    }
		
		return actions[max];
		
	}

	private List<Double> convert(Input i) {
		// TODO Auto-generated method stub
		
		return null;
	}

}
