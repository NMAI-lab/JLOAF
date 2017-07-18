package org.jLOAF.weights;


/***
 * Weights can take on values from [0,inf] giving more weight to more important features and less weight to features
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
	 * Sets weights for each feature by name
	 * @param feat_name the name of the feature
	 * @param weight double value of the weight
	 * @return none
	 * ***/
	public void setFeatureWeight(String feat_name, double weight){
		if(feat_name==null) throw new IllegalArgumentException("Null parameter passed as feature name");
		weights.put(feat_name, weight);
	}
	
}
