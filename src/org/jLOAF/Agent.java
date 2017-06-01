package org.jLOAF;

import org.jLOAF.action.Action;
import org.jLOAF.casebase.Case;
import org.jLOAF.casebase.CaseBase;
import org.jLOAF.inputs.Input;

public abstract class Agent {

	protected Reasoning r;
	protected MotorControl mc;
	protected Perception p;
	protected CaseBase cb;
	
	public Agent(Reasoning reasoning, MotorControl motorcontrol, Perception perception, CaseBase casebase){
		this.setR(reasoning);
		this.mc = motorcontrol;
		this.p = perception;
		this.cb = casebase;
	}

	public Reasoning getR() {
		return r;
	}

	public void setR(Reasoning r) {
		this.r = r;
	}
	/***
	 * Define implementation using r.selectAction(input) but output specifically the subclassed action type
	 * 
	 * ***/
	public abstract Action run(Input i);
	
	public abstract void train(CaseBase casebase);

}
