package org.jLOAF.sim.StateBased;

import org.jLOAF.sim.StateBasedSimilarity;
/**
* this enum is only used for testing purposes, where it assigns instances of stateBasedSimilarities to the strings that represent them.
* @see org.JLOAF.performance.TestingConfig
* @author Ibrahim Ali Fawaz
*
*/
public enum StSims {
	korderd(new KOrderedSimilarity(5)),korderd_r(new KOrderedSimilarity(1)),ordered(new OrderedSimilarity()),unordered(new UnorderedSimilarity())
	,weighted(new WeightedStateBasedSimilarity());
	
	private StateBasedSimilarity sim;

	StSims(StateBasedSimilarity sim){
		this.sim=sim;
	}

	public StateBasedSimilarity geSim(){
		return sim;
	}
}
