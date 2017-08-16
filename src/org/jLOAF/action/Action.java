package org.jLOAF.action;

import java.io.Serializable;
/**
 * this Action class follows the composite pattern, where it has two children, an atomicAction and a complexAction.
 * the Action is considered an output of a given input.
 */
public class Action implements Serializable{

	private static final long serialVersionUID = 1L;
	
	protected String name;
	/**
	 * Constructor that sets a name for an Action.
	 */
	public Action(String name){
		this.name = name;
	}
	/**
	 * returns the name of the action
	 * @return the name of the action
	 */
	public String getName(){
		return this.name;
	}
	/**
	 * calculates the similarity between two actions,by comparing their names.
	 * @param action the action compared to this action.
	 * @return the similarity between two actions where the 1 is the most similar and zero is the least.
	 * 
	 */
	public double similarity(Action action) {
		if(!this.getName().equals(action.getName())){
			return 0;
		}
		return 1;
	}

}
