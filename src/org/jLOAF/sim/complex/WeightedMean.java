package org.jLOAF.sim.complex;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.jLOAF.inputs.ComplexInput;
import org.jLOAF.inputs.Input;
import org.jLOAF.sim.ComplexSimilarityMetricStrategy;
import org.jLOAF.sim.SimilarityMetricStrategy;
import org.jLOAF.weights.SimilarityWeights;

/***
 * This method calculates the weighted mean based on the weights of each feature.
 * The weights are applied to the similarity between each feature and added to the total.
 * 
 * ***/
public class WeightedMean extends ComplexSimilarityMetricStrategy {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected HashMap<String,Double> similarities;
	protected SimilarityWeights feat_weights;
	
	public WeightedMean(SimilarityWeights featureweights) {
		similarities = new HashMap<String, Double>();
		feat_weights = featureweights;
		
	}
	
	public SimilarityWeights getSimilarityWeights(){
		return feat_weights;
	}

	@Override
	public double similarity(Input i1, Input i2) {
		//null check
		if(i1==null || i2 ==null) return 0.0;
		
		if(!(i1 instanceof ComplexInput) || !(i2 instanceof ComplexInput)){
			throw new IllegalArgumentException("Mean.similarity(..): Not ComplexInputs");
		}
		
		ComplexInput cplx1 = (ComplexInput)i1;
		ComplexInput cplx2 = (ComplexInput)i2;
		
		Set<String> keys = cplx1.getChildNames();
		Set<String> keys2 = cplx2.getChildNames();
		
		if(keys.size()==0 && keys2.size()==0){
			//if they cannot see anything they are in a similar situation?
			return 1.0;
		}
		
		//dealing with mismatched sets with a penalty for not having a paired element
		if (keys.size()>=keys2.size()){
			for(String s: keys){
				if (cplx2.get(s)!=null){
					similarities.put(s, cplx1.get(s).similarity(cplx2.get(s)));
				}	
			}
		}else if(keys.size()<keys2.size()){
			for(String s: keys2){
				if (cplx1.get(s)!=null){
					similarities.put(s, cplx1.get(s).similarity(cplx2.get(s)));
				}		
			}
		}
		return calculateWeightedSimilarity();
	}
	
	/***
	 * Calculates the weighted similarity based on the features weights.
	 * Adds to the total and divides by the size of the keyset()
	 * @author michael floyd
	 * 	 * ***/
	public double calculateWeightedSimilarity(){
		double totalDistance = 0.0;
		
		List<String> featureTypes = new ArrayList<String>(similarities.keySet());
			int i=0;
		for(String currentFeature : featureTypes){
			double featureDistance = similarities.get(currentFeature);
				
			//apply the weight
			
			
			double currentWeight = this.feat_weights.getWeight(currentFeature);
			if(currentWeight==0){
				i++;
			}
			totalDistance += featureDistance*currentWeight;
		}
				int divider=similarities.keySet().size()-i;
				if(divider==0)
				{
					divider=1;
				}
		return totalDistance/divider;
		
	}
	/**
	 * Sets the feature weights of the current object. 
	 * @param featweights
	 */
	public void setFeaturesWeights(HashMap<String,Double> featweights){
		for(String feature:featweights.keySet()){
		feat_weights.setFeatureWeight(feature, featweights.get(feature));
		}
		
	}
}
