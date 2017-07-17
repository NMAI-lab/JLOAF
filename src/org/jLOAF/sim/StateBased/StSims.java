package org.jLOAF.sim.StateBased;

import org.jLOAF.sim.StateBasedSimilarity;

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
