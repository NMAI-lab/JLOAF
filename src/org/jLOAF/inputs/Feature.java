package org.jLOAF.inputs;

import java.io.Serializable;
/**
 * this class is just a representation of a double value that is taken by an atomic ation or an atomic input.
 * @author Owner
 *
 */
public class Feature implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private double val;
	/**
	 * Constructor
	 * @param value the value the feature represents.
	 */
	public Feature(double value){
		this.val = value;
	}
	/**
	 * 
	 * @return the value that the feature represents.
	 */
	public double getValue(){
		return this.val;
	}
}
