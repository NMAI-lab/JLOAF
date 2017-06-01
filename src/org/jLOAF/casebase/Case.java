package org.jLOAF.casebase;

import java.io.Serializable;

import org.jLOAF.action.Action;
import org.jLOAF.inputs.Input;

public class Case implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Input in;
	private Action act;
	
	public Case(Input input, Action action){
		this.in = input;
		this.act = action;
	}
	
	public Input getInput(){
		return this.in;
	}
	
	public Action getAction(){
		return this.act;
	}
}
