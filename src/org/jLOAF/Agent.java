package org.jLOAF;

import org.jLOAF.casebase.CaseBase;

public abstract class Agent {

	protected Reasoning r;
	protected MotorControl mc;
	protected Perception p;
	protected CaseBase cb;
	
	public Agent(Reasoning reasoning, MotorControl motorcontrol, Perception perception, CaseBase casebase){
		this.r = reasoning;
		this.mc = motorcontrol;
		this.p = perception;
		this.cb = casebase;
	}
}
