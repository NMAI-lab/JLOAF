/** Represents an atomic input (a feature).
 * 
 * Author: Michael W. Floyd
 */
package org.jLOAF.inputs;

import org.jLOAF.sim.SimilarityMetricStrategy;

public class AtomicInput extends Input {

	private static final long serialVersionUID = 1L;
	
	protected Feature feat;
	
	public AtomicInput(String name, Feature f, SimilarityMetricStrategy sim) {
		super(name);
		super.setSimilarityMetric(sim);
		this.feat = f;
	}

	public Feature getFeature(){
		return this.feat;
	}
	public String toString(){
		return ""+feat.getValue();
	}

}
