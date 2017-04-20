package org.jLOAF.reasoning;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import org.jLOAF.Reasoning;
import org.jLOAF.action.Action;
import org.jLOAF.casebase.Case;
import org.jLOAF.casebase.CaseBase;
import org.jLOAF.inputs.Input;
import org.jLOAF.retrieve.Distance;
import org.jLOAF.retrieve.Retrieval;
import org.jLOAF.retrieve.kNN;

public class WeightedKNN implements Reasoning {

	private Retrieval ret;
	
	public WeightedKNN(int k, CaseBase cb){
		ret = new kNN(k, cb);
	}
	
	@Override
	public Action selectAction(Input i) {
		List<Case> nn = ret.retrieve(i);
		
		return mostLikelyAction(nn);
	}
	
	public Action mostLikelyAction(List<Case> nn){
		Hashtable<String, Double> nnactions = new Hashtable<String, Double>();
		Distance [] dist_closest = ret.getDist();
		String max_action;
		List<Action> a = new ArrayList<Action>();
		
		for(int i =0;i<nn.size();i++){
			if(!nnactions.containsKey(nn.get(i).getAction().getName())){//hashtable to account for number of times an action is chosen
				nnactions.put(nn.get(i).getAction().getName(), 1.0/Math.pow(dist_closest[i].getDistance(),2));
			}else{
				double value = nnactions.get(nn.get(i).getAction().getName());
				nnactions.put(nn.get(i).getAction().getName(), value+(1.0/Math.pow(dist_closest[i].getDistance(),2)));
			}
		}
		System.out.println(nnactions);
		max_action =  max(nnactions);
		
		//run through all the cases and only select the first action with that name, as it will be the closest in terms of distance
		for(Case c: nn){
			if(c.getAction().getName().equals(max_action)){
				a.add(c.getAction());
				break;
			}
		}
		if(a.size()==0){
			System.out.println("failing");
		}
		return a.get(0);
	}
	
	/**
	 * Sacha 2017 Mar
	 * Calculates the msot likely action given a matrix of actions and counts
	 * 
	 * **/
	
	//need to implement a weighted knn with distance 
	public String max(Hashtable<String, Double> h){
		Enumeration<String> actions;
		
		actions = h.keys();
		double max = 0;
		String action = "";
		
		while(actions.hasMoreElements()){
			String val = (String) actions.nextElement();
			if( h.get(val)>max){
				max = h.get(val);
				action = val;
			}
		}
		
		return action;
	}

}
