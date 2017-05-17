package org.jLOAF.inputs;

import java.util.Map;
import java.util.HashMap;
import java.util.Set;

import org.jLOAF.sim.ComplexSimilarityMetricStrategy;
import org.jLOAF.sim.SimilarityMetricStrategy;

public class ComplexInput extends Input {

	private static final long serialVersionUID = 1L;

	private Map<String,Input> collect;
	
	public ComplexInput(String name) {
		super(name);
		collect = new HashMap<String,Input>();
	}

	public void add(Input i){
		collect.put(i.name, i);
	}
	
	public Input get(String name){
		return collect.get(name);
	}
	
	public Set<String> getChildNames(){
		return collect.keySet();
	}

	
	
}
