package org.jLOAF.preprocessing.filter.featureSelection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import org.jLOAF.performance.StatisticsBundle;
import org.jLOAF.weights.SimilarityWeights;
import org.jLOAF.weights.Weights;
/**
 * Feature Node Class just wraps the weights of features, and holds their statistics information,
 * and provide some methods that are used by filtering algorithms.
 * @author Ibrahim Ali Fawaz
 *
 */
public class FeatureNode implements Comparable<FeatureNode> {
		
	
	
	private Weights weights;
	
	private StatisticsBundle m_statsBest;
	private float evaluateNumber;
	/**
	 * compares this node's evaluateNumber with the passed node's evaluate number multiplied by a multiplyer
	 * @param m_best the node to be compared to this node
	 * @param multiplyer the multiplyer that will be multiplied by the give node's evaluateNumber
	 * @return true if this node's evaluateNumber is bigger than the passed node's evaluate number multiplied by a multiplyer
	 */
	public boolean bigger(FeatureNode m_best,double multiplyer) {
		// TODO Auto-generated method stub
		return evaluateNumber>m_best.getEvaluateNumber()*multiplyer;
	}
	/**
	 * 
	 * @return the weights of the node
	 */
	public Weights getWeights() {
		return weights;
	}
	/**
	 * sets the weights of this node
	 * @param weights the weights to be set for this node
	 */
	public void setWeights(Weights weights) {
		this.weights = weights;
	}
	/**
	 * 
	 * @return the evaluateNumber of this node
	 */
	public float getEvaluateNumber() {
		return evaluateNumber;
	}
	/**
	 * sets the evaluateNumber of this node
	 * @param evaluateNumber the evaluateNumber to be set to this node
	 */
	public void setEvaluateNumber(float evaluateNumber) {
		this.evaluateNumber = evaluateNumber;
	}
	/**
	 * 
	 * @return the statisticBundle of this node
	 */
	public StatisticsBundle getM_statsBest() {
		return m_statsBest;
	}
	/**
	 * sets the statisticBundle to this node
	 * @param m_statsBest the statisticBundle to be set for this node
	 */
	public void setM_statsBest(StatisticsBundle m_statsBest) {
		this.m_statsBest = m_statsBest;
	}
	/**
	 *checks if a given feature exists in the weights of this node
	 * @param w the feature to be checked
	 * @return true if the given feature exists in the weights of this node, false otherwise
	 */
	public boolean exists(String w) {
		
		return weights.getWeight(w)!=0;
	}
	/**
	 * removes a given feature from a copy of the weights of this node, then returns a new node with the new weights.
	 * @param w the feature to be removed for the weights of the new node
	 * @return a featureNode with new weights that don't have the passed feature
	 */
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
	/**
	 * adjusts  a copy of the weights of the given nodes, by i, and then creates a new node with the modified copy of the weights and returns it
	 * @param i the adjustment value of the weights of this node
	 * @param w the feature on which the adjustment will be made
	 * @return a new FeatureNode with new weights that have the weight of the feature given adjusted by i
	 */
	public FeatureNode adjustFeature(int i,String w) {
		SimilarityWeights newWeights = new SimilarityWeights();
		for(String w1:weights.getWeightedItems()){
			if(w1.equals(w)){
				newWeights.setFeatureWeight(w1, i);
			}else{
			newWeights.setFeatureWeight(w1, weights.getWeight(w1));
			}
		}
		FeatureNode newNode = new FeatureNode();
		newNode.setWeights(newWeights);
		return newNode;
	}
	/**
	 * 
	 * @param featureNode the node to be compared to this featureNode
	 * @return true if this node's evaluateNumber is bigger or equale to the given featureNode's evaluateNumber
	 */
	public boolean bigger(FeatureNode featureNode) {
		
		return evaluateNumber>=featureNode.getEvaluateNumber();
	}
	/**
	 * changes the weights of this featureNode to new weights with random values.
	 * @param allIn the weights that have the features to be copied and to be given new randomized values.
	 */
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
	
