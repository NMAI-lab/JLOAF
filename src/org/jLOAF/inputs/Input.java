/** Class that represents an agent's inputs.
 * 
 * Author: Michael W. Floyd
 * 
 */

package org.jLOAF.inputs;

import java.io.Serializable;
import java.util.HashMap;

import org.jLOAF.casebase.CaseBase;
import org.jLOAF.sim.SimilarityMetricStrategy;
/*
 * the input class is the parent of all input classes, it is an abstract class that defines some methods for its children.
 * an input is usually the state of an environment, or a trace of the environment states and actions.
 * Also the input class follows the composite pattern.
 */
public abstract class Input implements Serializable{

	private static final long serialVersionUID = 1L;
	
	protected String name;
	
	protected SimilarityMetricStrategy simStrategy;
	/**
	 * Constructor 
	 * @param name the name of the input.
	 */
	public Input(String name){
		this.name = name;
	}
	
	/**
	 * returns the name of the input
	 * @return the name of the input
	 */
	public String getName(){
		return this.name;
	}
	/**
	 * returns the similarity betweem this input and the input passed to it,using the similarityMetricStrategy class
	 * @param i the input to be compared with this input
	 * @return a double that represents the similarity between the two inputs.
	 */
	public double similarity(Input i){
		if(!this.getClass().equals(i.getClass())){
			throw new IllegalArgumentException("Inputs not of the same class");
		}
		return simStrategy.similarity(this, i);
	}
	
	/**
	 * sets the similarity Strategy used by this input
	 * @param s the similarity strategy to be used by the input
	 */
	public void setSimilarityMetric(SimilarityMetricStrategy s) {
		this.simStrategy = s;
	}
	/**
	 * 
	 * @return the similarity strategy used by this input.
	 */
	public SimilarityMetricStrategy getSimilarityMetricStrategy(){
		return simStrategy;
	}

	public static double getFeature(Input input, String feature) {
		
			HashMap<String,Double> value = CaseBase.convert(((StateBasedInput)input).getInput());
			
			return value.get(feature);
		}
	
}
