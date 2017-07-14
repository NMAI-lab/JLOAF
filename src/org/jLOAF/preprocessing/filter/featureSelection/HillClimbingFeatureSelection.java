package org.jLOAF.preprocessing.filter.featureSelection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.jLOAF.casebase.CaseBase;
import org.jLOAF.performance.Statistics;
import org.jLOAF.preprocessing.filter.CaseBaseFilter;
import org.jLOAF.weights.Weights;

public class HillClimbingFeatureSelection extends FeatureSelection {
	private FeatureNode currentNode ;
	private double goalValue;
	private int currentIndex=0;
	private double maxWeight=10;
	private int getStuck=0;
	private int escape;
	/*
	 * Constructor
	 * @param fs a caseBaseFilter to be passed to a filter method, as part of the chain of responsibility design patter
	 * @param goalValue the best performance the user would like to get for a set of features
	 */
	public HillClimbingFeatureSelection(CaseBaseFilter fs){
		super(fs);
		
		currentNode = new FeatureNode();
		
	
	}
	
	
	public FeatureNode filterFeatures(Weights allIn){
		
		currentNode.setWeights(allIn);
		evaluate(currentNode);
		double acc= currentNode.getEvaluateNumber();
		
		FeatureNode s =currentNode;
		currentNode =getHighestBetweenLocalNeighbours(currentNode);
			
			if(s==currentNode){
				getStuck++;
			}
			if(getStuck>10||currentNode==null){
				return s;
			}
			
			return filterFeatures(currentNode.getWeights());
		
	}
	private FeatureNode getHighestBetweenLocalNeighbours(FeatureNode currentNode2) {
		
		
		ArrayList<String> weights = new ArrayList<String>(currentNode2.getWeights().getWeightedItems());
		if(currentIndex>=weights.size()){
			return null;
		}
				if(escape==0){
					escape=(int)currentNode2.getWeights().getWeight(weights.get(currentIndex));
				}
			escape+=2;
		String w = weights.get(currentIndex);
				FeatureNode newNode=currentNode2.adjustFeature(escape, w);
				if(newNode.getWeights().getWeight(w)>maxWeight||escape>=maxWeight){
					currentIndex++;
					escape=0;
				}
			
		
		
		evaluate(newNode);
		if(newNode.bigger(currentNode2)){
			return newNode;
		}
		return currentNode2;
		
	}

	

	

}
