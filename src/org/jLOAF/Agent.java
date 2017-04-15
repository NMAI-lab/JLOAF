package org.jLOAF;

import org.jLOAF.casebase.CaseBase;

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
}
