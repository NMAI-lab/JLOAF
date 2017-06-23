package org.jLOAF.weights;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Set;

public abstract class Weights implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	double default_weight;
	public double getDefault_weight() {
		return default_weight;
	}

	HashMap<String,Double> weights = new HashMap<String,Double>();
	
	public HashMap<String, Double> getWeights() {
		return weights;
	}

	/***
	 * returns the weight for a given feature
	 * @param feature name
	 * @return weight
	 * ***/
	public double getWeight(String feat_name){
		if(feat_name==null) throw new IllegalArgumentException("Null parameter passed as argument");
		if(weights.get(feat_name)==null) return 0;
		return weights.get(feat_name);
	}
	
	public Set<String> getWeightedItems(){
		return weights.keySet();
	}
}
