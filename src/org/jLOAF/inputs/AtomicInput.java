/** Represents an atomic input (a feature).
 * 
 * Author: Michael W. Floyd
 */
package org.jLOAF.inputs;

import org.jLOAF.sim.SimilarityMetricStrategy;

public class AtomicInput extends Input {

	private static final long serialVersionUID = 1L;

	private static SimilarityMetricStrategy s_simstrategy;
	
	protected Feature feat;
	
	public AtomicInput(String name, Feature f) {
		super(name);
		this.feat = f;
	}

	
	public Feature getFeature(){
		return this.feat;
	}
	

	@Override
	public double similarity(Input i) {
		//See if the user has defined similarity for each specific input, for all inputs
		//  of a specific type, of defered to superclass
		if(this.simStrategy != null){
			return simStrategy.similarity(this, i);
		}else if(AtomicInput.isClassStrategySet()){
			return AtomicInput.similarity(this, i);
		}else{
			//normally we would defer to superclass, but super
			// is abstract
			System.err.println("Problem. In AtomicInput no similarity metric set!");
			return 0;
		}
	}

	private static double similarity(Input atomicInput, Input i) {
		return AtomicInput.s_simstrategy.similarity(atomicInput, i);
	}

	public static boolean isClassStrategySet(){
		if(AtomicInput.s_simstrategy == null){
			return false;
		}else{
			return true;
		}
	}
	
	public static void setClassStrategy(SimilarityMetricStrategy s){
		AtomicInput.s_simstrategy = s;
	}

}
