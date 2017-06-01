package org.jLOAF.action;

import java.io.Serializable;

public class Action implements Serializable{

	private static final long serialVersionUID = 1L;
	
	protected String name;
	
	public Action(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}

	public double similarity(Action action) {
		if(!this.getName().equals(action.getName())){
			return 0;
		}
		return 1;
	}
	

}
