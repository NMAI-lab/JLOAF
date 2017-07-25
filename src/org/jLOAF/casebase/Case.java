package org.jLOAF.casebase;

import java.io.Serializable;

import org.jLOAF.action.Action;
import org.jLOAF.inputs.Input;
/*
 * case class contians an input and an output(Action). 
 * one case represents a state of an environment and the action that corresponds to it, which is done by an expert
 */
public class Case implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Input in;
	private Action act;
	/*
	 * Constructor
	 * @param input the state of the environment
	 * @param action the action that corresponds to the state, done by an expert
	 */
	public Case(Input input, Action action){
		this.in = input;
		this.act = action;
	}
	/*
	 * returns the input of the case, which is a stateBasedInput
	 * @return the input of the case, which is a stateBasedInput.
	 */
	public Input getInput(){
		return this.in;
	}
	/*
	 * returns the action of the case.
	 * @return the action of the case.
	 */
	public Action getAction(){
		return this.act;
	}
	/**
	 * sets the Action of this case
	 * @param mostLikelyAction the action to be set for this Case
	 */
	public void setAction(Action mostLikelyAction) {
		act =mostLikelyAction;
		
	}
}
