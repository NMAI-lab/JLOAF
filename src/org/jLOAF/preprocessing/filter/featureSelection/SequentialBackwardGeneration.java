package org.jLOAF.preprocessing.filter.featureSelection;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.jLOAF.casebase.Case;
import org.jLOAF.casebase.CaseBase;
import org.jLOAF.performance.Statistics;
import org.jLOAF.weights.Weights;

public class SequentialBackwardGeneration extends FeatureSelection{
	
	
	private int m_k;
	private double m_multiplier;
	private FeatureNode m_best;
	private ArrayList<FeatureNode> closed;
	
	

	public SequentialBackwardGeneration(FeatureSelection fs,int k, double epsilon){
		super(fs);
		//check parameters
		if(k < 1){
			throw new IllegalArgumentException("k-value must be greater than zero.");
		}
		if(epsilon < 0){
			throw new IllegalArgumentException("epsilon must be positive.");
		}
		
		
		m_k = k;
		m_multiplier = 1 + (epsilon/100);
		
		m_best = null;
	
		closed = new ArrayList<FeatureNode>();
		
		
	}
	
	public FeatureNode filterFeatures(Weights allIn){
		FeatureNode currentNode = new FeatureNode();
			currentNode.setWeights(allIn);
		evaluate(currentNode);
		m_best=currentNode;
	
				//we use this to store the number of weight we have "opened" since
				//the best weight set
				int numSinceBest = 0;
				//now we begin the best-first search
				while(numSinceBest < m_k){
					closed.add(currentNode);
					
					
					createChildren(currentNode);
					
					if(open.isEmpty()){
						break;
					}
					
					currentNode =open.remove(0);
					
					if(currentNode.bigger(m_best,m_multiplier)){
						
						m_best=currentNode;
						numSinceBest=0;
					}else{
						numSinceBest++;
					}
					
				}
		
				
				
		return m_best;
		
	}

	

	private void createChildren(FeatureNode currentNode) {
		Set<String> weights=currentNode.getWeights().getWeightedItems();
		
		for(String w : weights){
			if(currentNode.exists(w)){
				FeatureNode child = currentNode.remove(w);
				placeInList(child);
			}
			
			
		}
		
	}

	

	
}
