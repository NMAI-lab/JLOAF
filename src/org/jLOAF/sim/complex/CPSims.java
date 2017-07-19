package org.jLOAF.sim.complex;

import org.jLOAF.sim.ComplexSimilarityMetricStrategy;
import org.jLOAF.sim.StateBasedSimilarity;
import org.jLOAF.sim.StateBased.KOrderedSimilarity;
import org.jLOAF.sim.StateBased.OrderedSimilarity;
import org.jLOAF.sim.StateBased.UnorderedSimilarity;
import org.jLOAF.sim.StateBased.WeightedStateBasedSimilarity;
/**
 * Contains the enumerated version of the ComplexSimMetricStrategies relating to set Matching
 * @author sachagunaratne
 *
 */
public enum CPSims {
	auctionSim(new  AuctionMaximalMatching()),greedy(new GreedyMunkrezMatching()),indexMatching(new OrderIndexMatchingAlgorithm("flags","flag_dir"));
	
	private ComplexSimilarityMetricStrategy sim;

	CPSims(ComplexSimilarityMetricStrategy sim){
		this.sim=sim;
	}

	public ComplexSimilarityMetricStrategy geSim(){
		return sim;
	}
}

