package org.jLOAF.inputs;

import java.io.Serializable;

public class Feature implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private double val;
	
	public Feature(double value){
		this.val = value;
	}
	
	public double getValue(){
		return this.val;
	}
}
