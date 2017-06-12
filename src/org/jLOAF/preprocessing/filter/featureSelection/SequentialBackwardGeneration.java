package org.jLOAF.preprocessing.filter.featureSelection;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.jLOAF.casebase.Case;
import org.jLOAF.casebase.CaseBase;
import org.jLOAF.performance.Statistics;

public class SequentialBackwardGeneration {
	
	
	private int m_k;
	private double m_multiplier;
	private FeatureNode m_best;
	private ArrayList<FeatureNode> closed;
	private ArrayList<FeatureNode> open;
	private Statistics st;
	private CaseBase testCases;

	public SequentialBackwardGeneration(Statistics st,CaseBase testCases,int k, double epsilon){
		//check parameters
		if(k < 1){
			throw new IllegalArgumentException("k-value must be greater than zero.");
		}
		if(epsilon < 0){
			throw new IllegalArgumentException("epsilon must be positive.");
		}
		
		m_k = k;
		m_multiplier = 1 + (epsilon/100);
		this.st =st;
		this.testCases=testCases;
		m_best = null;
	
		closed = new ArrayList<FeatureNode>();
		open = new ArrayList<FeatureNode>();
		
	}
	
	public FeatureNode selectFeatures(FeatureNode allIn){
		evaluate(allIn);
		m_best=allIn;
		FeatureNode currentNode = allIn;
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

	private void evaluate(FeatureNode allIn) {
			st.getStatisticsHashMap().clear();
			for(Case test:testCases.getCases()){
				st.predictedCorrectActionName(test);
			}
		allIn.setM_statsBest(st.getStatisticsBundle());
		allIn.setEvaluateNumber(st.getStatisticsBundle().getPrimaryStatistic());
		st= new Statistics(st.getAgent());
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

	private void placeInList(FeatureNode child) {
boolean placed = false;
		
		for(int ii=0;ii<open.size();ii++){
			//if it has a higher evaluation that the current item, add it here
			if(child.bigger(open.get(ii))){
				open.add(ii,child);
				placed = true;
				break;
			}
		}
		
		//if it was not placed somewhere in the list, add to the end
		if(!placed){
			open.add(child);
		
		}
		
	}
}
