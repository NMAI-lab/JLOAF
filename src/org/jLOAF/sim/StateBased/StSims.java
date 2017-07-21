package org.jLOAF.sim.StateBased;

import org.jLOAF.sim.StateBasedSimilarity;
/**
* this enum is only used for testing purposes, where it assigns instances of stateBasedSimilarities to the strings that represent them.
* @see org.JLOAF.performance.TestingConfig
* @author Ibrahim Ali Fawaz
*
*/
public enum StSims {
	koredered(new KOrderedSimilarity(5)),ordered(new OrderedSimilarity()),unoredered(new UnorderedSimilarity())
	,weighted(new WeightedStateBasedSimilarity());
	
	private StateBasedSimilarity sim;

	StSims(StateBasedSimilarity sim){
		this.sim=sim;
	}

	public StateBasedSimilarity geSim(){
		return sim;
	}
}
