package org.jLOAF.weights;

/***
 * Weights can take on values from [0,1] giving more weight to more important features and less weight to features
 * that are less important. The weights are valued this way because the higher the similarity the better.
 * @author micheal floyd, sacha gunaratne
 * @since 2017 may
 * ***/

public class DistanceWeights extends Weights {
	/***
	 * Sets default_weight and instantiates the object
	 * ***/
	public DistanceWeights(){
		
	}
	/***
	 * Sets weights for each feature by name
	 * @param feature name, weight
	 * @return none
	 * ***/
	public void setFeatureWeight(String feat_name, double weight){
		if(feat_name==null) throw new IllegalArgumentException("Null parameter passed as feature name");
		if(weight<0 || weight>1) throw new IllegalArgumentException("weight should be >0 or <1 (inclusive)");
		weights.put(feat_name, weight);
	}
	

}
