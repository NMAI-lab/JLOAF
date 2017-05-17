package org.jLOAF.weights;


/***
 * Weights can take on values from [1,inf] giving more weight to more important features and less weight to features
 * that are less important. The weights are valued this way because the higher the similarity the better.
 * @author micheal floyd, sacha gunaratne
 * @since 2017 may
 * ***/

public class SimilarityWeights extends Weights {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/***
	 * Sets default_weight and instantiates the object
	 * ***/
	public SimilarityWeights(double default_weight){
		if(default_weight<1) throw new IllegalArgumentException("defualt weight should be >=1");
		this.default_weight =default_weight;
	}
	/***
	 * Sets weights for each feature by name
	 * @param feature name, weight
	 * @return none
	 * ***/
	public void setFeatureWeight(String feat_name, double weight){
		if(feat_name==null) throw new IllegalArgumentException("Null parameter passed as feature name");
		if(weight<1) throw new IllegalArgumentException("weight should be >=1");
		weights.put(feat_name, weight);
	}
	
}
