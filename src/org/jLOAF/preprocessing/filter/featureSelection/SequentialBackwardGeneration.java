package org.jLOAF.preprocessing.filter.featureSelection;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.jLOAF.casebase.Case;
import org.jLOAF.casebase.CaseBase;
import org.jLOAF.performance.Statistics;
import org.jLOAF.preprocessing.filter.CaseBaseFilter;
import org.jLOAF.weights.Weights;

/** The Sequential Backward Generation wrapper algorithm described in
 * 
 * R. Kohavi, Wrapper for Performance Enhancement and Oblivious Decision
 * Graphs, PhD thesis, Stanford University, 1995.
 * 
 * The algorithm initially starts with with a complete feature set and then
 * "opens" that set by examining all possible feature sets that exist by removing
 * a single feature. The feature set with the highest evaluation is likewise
 * "opened". This process continues until there is not a significant increase 
 * in the evaluation of feature sets for k "openings".
 * 
 * @author Michael W. Floyd
 * @since 0.5
 */
public class SequentialBackwardGeneration extends FeatureSelectionAlgorithm{
	
	
	private int m_k;
	private double m_multiplier;
	private FeatureNode m_best;
	private ArrayList<FeatureNode> closed;
	
	

	public SequentialBackwardGeneration(CaseBaseFilter fs,int k, double epsilon){
		super(fs);
		//check parameters
		if(k < 1){
			throw new IllegalArgumentException("k-value must be greater than zero.");
		}
		if(epsilon < 0){
			throw new IllegalArgumentException("epsilon must be positive.");
		}
		
		
		m_k = k;
		m_multiplier = epsilon;
		
		m_best = null;
	
		closed = new ArrayList<FeatureNode>();
		
		
	}
	@Override
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


	/** This method determines the children of the current 
	 * feature set by finding all children with one less
	 * feature
	 * 
	 * @param current The current feature set being examined
	 * 
	 * @author Michael W. Floyd
	 * @since 0.5
	 */
	private void createChildren(FeatureNode currentNode) {
		Set<String> weights=currentNode.getWeights().getWeightedItems();
		
		for(String w : weights){
			if(currentNode.exists(w)){
				FeatureNode child = currentNode.remove(w);
				evaluate(child);
				placeInList(child);
			}
			
			
		}
		
	}

	

	
}
