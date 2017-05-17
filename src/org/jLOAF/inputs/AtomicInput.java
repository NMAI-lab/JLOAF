/** Represents an atomic input (a feature).
 * 
 * Author: Michael W. Floyd
 */
package org.jLOAF.inputs;

public class AtomicInput extends Input {

	private static final long serialVersionUID = 1L;
	
	protected Feature feat;
	
	public AtomicInput(String name, Feature f) {
		super(name);
		this.feat = f;
	}

	public Feature getFeature(){
		return this.feat;
	}

}
