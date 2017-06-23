package org.jLOAF.agents;

import org.jLOAF.Agent;
import org.jLOAF.action.Action;
import org.jLOAF.casebase.Case;
import org.jLOAF.casebase.CaseBase;
import org.jLOAF.inputs.Input;
import org.jLOAF.reasoning.SimpleKNN;
import org.jLOAF.reasoning.WeightedKNN;

public class GenericAgent extends Agent {
	
	public GenericAgent(){
		super(null,null,null,null);
	}

	@Override
	public Action run(Input i) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void train(CaseBase casebase) {
		this.cb = casebase;
		this.r = new WeightedKNN(5,cb);
		
	}
	
	
}
