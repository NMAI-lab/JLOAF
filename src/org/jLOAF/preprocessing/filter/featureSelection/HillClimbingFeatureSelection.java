package org.jLOAF.preprocessing.filter.featureSelection;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.jLOAF.casebase.CaseBase;
import org.jLOAF.performance.Statistics;
import org.jLOAF.weights.Weights;

public class HillClimbingFeatureSelection extends FeatureSelection {
	private FeatureNode currentNode ;
	private double goalValue;
	private int currentIndex=0;
	private double maxWeight=10;
	
	
	public HillClimbingFeatureSelection(FeatureSelection fs,int goalValue){
		super(fs);
		
		currentNode = new FeatureNode();
		
		this.goalValue=goalValue;
	}
	
	
	public FeatureNode filterFeatures(Weights allIn){
		
		currentNode.setWeights(allIn);
		evaluate(currentNode);
		double acc= currentNode.getEvaluateNumber();
		if(acc==goalValue ){
			return currentNode;
		}
		FeatureNode s =currentNode;
		currentNode =getHighestBetweenLocalNeighbours(currentNode);
			if(s==currentNode||currentNode==null){
				return s;
			}
			
			return filterFeatures(currentNode.getWeights());
		
	}
	private FeatureNode getHighestBetweenLocalNeighbours(FeatureNode currentNode2) {
		
		ArrayList<String> weights =(ArrayList<String>) currentNode2.getWeights().getWeightedItems();
		String w = weights.get(currentIndex);
				FeatureNode newNode=currentNode2.adjustFeature(2, w);
				if(currentNode2.getWeights().getWeight(w)==maxWeight){
					currentIndex++;
				
				}
			
		
		if(currentIndex==weights.size()){
			return null;
		}
		if(newNode.bigger(currentNode2)){
			return newNode;
		}
		return currentNode2;
		
	}

	

	

}
