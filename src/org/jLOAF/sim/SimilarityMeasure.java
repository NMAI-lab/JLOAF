package org.jLOAF.sim;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.jLOAF.weights.SimilarityWeights;

public abstract class SimilarityMeasure {
	
	protected HashMap<String,Double> similarities;
	protected SimilarityWeights feat_weights;
	
	public SimilarityMeasure(SimilarityWeights featureweights){
		similarities = new HashMap<String, Double>();
		feat_weights = featureweights;
	}
	
	/***
	 * Calculates the weighted similarity based the features weights
	 * adds to the total and divides by the size of the keyset()
	 * 	 * ***/
	public double calculateWeightedSimilarity(){
		double totalDistance = 0.0;
		
		List<String> featureTypes = new ArrayList<String>(similarities.keySet());
		for(String currentFeature : featureTypes){
			double featureDistance = similarities.get(currentFeature);
				
			//apply the weight
			double currentWeight = this.feat_weights.getWeight(currentFeature);
			totalDistance += featureDistance*currentWeight;
		}
		
		return totalDistance/similarities.keySet().size();
		
	}
}
