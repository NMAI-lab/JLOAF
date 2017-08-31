package org.jLOAF;

import org.jLOAF.action.Action;
import org.jLOAF.casebase.Case;
import org.jLOAF.casebase.CaseBase;
import org.jLOAF.inputs.Input;
import org.jLOAF.reasoning.BayesianReasoner;
/*
 * this abstract class defines couple of methods for all the learning agents that extend it,
 * any domain that needs to use the jloaf framework, has to have an agent that extends this class.
 * 
 */
public abstract class Agent {

	protected Reasoning r;
	protected MotorControl mc;
	protected Perception p;
	protected CaseBase cb;
	/**
	 * Constructor creates the agent with the specified arguments.
	 * @param reasoning the reasoner which will be used by the agent in the learning process.
	 * @param motorcontrol a tool that helps that user to control the domain's environment  , usually null in our studied cases
	 * @param perception the sensors that examins the environment.
	 * @param casebae the list of all previous cases, which the agent learnt.
	 */
	public Agent(Reasoning reasoning, MotorControl motorcontrol, Perception perception, CaseBase casebase){
		this.setR(reasoning);
		this.mc = motorcontrol;
		this.p = perception;
		this.cb = casebase;
	}
		/**
		 * returns the reasoner used by the agent
		 * @return the reasoner used by the agent
		 */
	public Reasoning getR() {
		return r;
	}
	/**
	 * sets the reasoner to be used by the agent
	 * @param r the reasoner to be used by the agent
	 */
	public void setR(Reasoning r) {
		this.r = r;
	}
	/**
	 * returns the action that it thinks its the best for a given input, of course, using the reasoner assigned to the agent.
	 * @param i the current state of the environment 
	 * @return an action that should be similar to what an expert would've reacted to the same input
	 */
	public abstract Action run(Input i);
	/**
	 * trains the agent with a given reasoner.
	 * @param casebase the casebase to be trained by the agent.
	 */
	public void train(Reasoning r){
		if(r instanceof BayesianReasoner){
			((BayesianReasoner) r).setTrain();
		}
		this.setR(r);
	}

}
