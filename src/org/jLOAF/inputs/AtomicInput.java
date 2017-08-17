/** Represents an atomic input (a feature).
 * 
 * Author: Michael W. Floyd
 */
package org.jLOAF.inputs;

import org.jLOAF.sim.AtomicSimilarityMetricStrategy;
import org.jLOAF.sim.SimilarityMetricStrategy;
/**
 * the atomicInput, is the atomic level of the input class, it only has one feature, and a name.
 * @author Owner
 *
 */
public class AtomicInput extends Input {

	private static final long serialVersionUID = 1L;
	
	protected Feature feat;
	/**
	 * Constructor
	 * @param name the name of the input
	 * @param f the feature taken by this input
	 * @param sim the similarityMetricStrategy used by this input for the comparison purposes.
	 */
	public AtomicInput(String name, Feature f, AtomicSimilarityMetricStrategy sim) {
		super(name);
		super.setSimilarityMetric(sim);
		this.feat = f;
	}
	/**
	 * 
	 * @return the feature taken by this input
	 */
	public Feature getFeature(){
		return this.feat;
	}
	@Override
	public String toString(){
		return ""+feat.getValue();
	}
	/**
	 * sets the feature of this  AtomicInput
	 * @param f the feature of this Atomic Input
	 */
	public void setFeature(Feature f){
		this.feat = f;
	}

}
