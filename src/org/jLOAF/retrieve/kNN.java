package org.jLOAF.retrieve;

import java.util.ArrayList;
import java.util.List;

import org.jLOAF.casebase.Case;
import org.jLOAF.casebase.CaseBase;
import org.jLOAF.inputs.Input;

public class kNN implements Retrieval {

	private int k;
	private CaseBase cb;
	
	public kNN(int k, CaseBase cb){
		this.cb = cb;
		this.k = k;
	}
	
	@Override
	public List<Case> retrieve(Input i) {
		//FIXME so it works with k>1
		double[] bestSim = new double[1];
		bestSim[0] = -1;
		Case[] bestCases = new Case[1];
		bestCases[0] = null;
		
		for(Case c: cb.getCases()){
			double sim = i.similarity(c.getInput());
			//FIXME for k > 1
			if(sim > bestSim[0]){
				bestSim[0] = sim;
				bestCases[0] = c;
			}
		}
		
		List<Case> best = new ArrayList<Case>();
		best.add(bestCases[0]);
		
		return best;
	}

}
