package org.jLOAF.preprocessing.filter.featureSelection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import org.jLOAF.performance.StatisticsBundle;
import org.jLOAF.weights.SimilarityWeights;
import org.jLOAF.weights.Weights;

public class FeatureNode implements Comparable<FeatureNode> {
		
	
	
	private Weights weights;
	
	private StatisticsBundle m_statsBest;
	private float evaluateNumber;
	public boolean bigger(FeatureNode m_best,double multiplyer) {
		// TODO Auto-generated method stub
		return evaluateNumber>m_best.getEvaluateNumber()*multiplyer;
	}
	public Weights getWeights() {
		return weights;
	}
	public void setWeights(Weights weights) {
		this.weights = weights;
	}
	public float getEvaluateNumber() {
		return evaluateNumber;
	}
	public void setEvaluateNumber(float evaluateNumber) {
		this.evaluateNumber = evaluateNumber;
	}
	public StatisticsBundle getM_statsBest() {
		return m_statsBest;
	}
	public void setM_statsBest(StatisticsBundle m_statsBest) {
		this.m_statsBest = m_statsBest;
	}
	
	public boolean exists(String w) {
		// TODO Auto-generated method stub
		return weights.getWeight(w)!=0;
	}
	public FeatureNode remove(String w) {
		SimilarityWeights newWeights = new SimilarityWeights();
		for(String w1:weights.getWeightedItems()){
			if(!w1.equals(w)){
				newWeights.setFeatureWeight(w1, weights.getWeight(w1));
			}
		}
		FeatureNode newNode = new FeatureNode();
		newNode.setWeights(newWeights);
		return newNode;
	}
	public FeatureNode adjustFeature(int i,String w) {
		SimilarityWeights newWeights = new SimilarityWeights();
		for(String w1:weights.getWeightedItems()){
			if(w1.equals(w)){
				newWeights.setFeatureWeight(w1, weights.getWeight(w1)+i);
			}
			newWeights.setFeatureWeight(w1, weights.getWeight(w1));
			
		}
		FeatureNode newNode = new FeatureNode();
		newNode.setWeights(newWeights);
		return newNode;
	}
	public boolean bigger(FeatureNode featureNode) {
		
		return evaluateNumber>featureNode.getEvaluateNumber();
	}
	public void randomizeWeights(Weights allIn) {
		Random r = new Random();
		SimilarityWeights weight= new SimilarityWeights();
		for(String w:allIn.getWeightedItems()){
			weight.setFeatureWeight(w, r.nextDouble()*10);
		}
		this.weights=weight;
	}
	
	@Override
	public int compareTo(FeatureNode o) {
		if(this.getEvaluateNumber()>o.getEvaluateNumber()){
			return -1;
		}else if(this.getEvaluateNumber()==o.getEvaluateNumber()){
			return 0;
		}else {
			return 1;
		}
	}
	
	
}
	
