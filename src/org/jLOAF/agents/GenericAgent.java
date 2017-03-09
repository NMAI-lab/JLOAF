package org.jLOAF.agents;

import org.jLOAF.Agent;
import org.jLOAF.action.Action;
import org.jLOAF.casebase.CaseBase;
import org.jLOAF.inputs.Input;
import org.jLOAF.reasoning.SimpleKNN;

public class GenericAgent extends Agent {
	
	public GenericAgent(CaseBase cb, int k){
		super(null,null,null,cb);
		
		this.setR(new SimpleKNN(k,cb));	
	}
}
