package org.jLOAF.inputs;

import java.util.Map;
import java.util.HashMap;
import java.util.Set;

import org.jLOAF.sim.ComplexSimilarityMetricStrategy;
import org.jLOAF.sim.SimilarityMetricStrategy;

public class ComplexInput extends Input {

	private static final long serialVersionUID = 1L;

	private Map<String,Input> collect;
	
	private static SimilarityMetricStrategy s_simstrategy;
	
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
	
	@Override
	public double similarity(Input i) {
		//See if the user has defined similarity for each specific input, for all inputs
		//  of a specific type, of defered to superclass
		if(this.simStrategy != null){
			return simStrategy.similarity(this, i);
		}else if(ComplexInput.isClassStrategySet()){
			return ComplexInput.similarity(this, i);
		}else{
			//normally we would defer to superclass, but super
			// is abstract
			System.err.println("Problem. In ComplexInput no similarity metric set!");
			return 0;
		}
	}

	private static double similarity(Input complexInput, Input i) {
		return ComplexInput.s_simstrategy.similarity(complexInput, i);
	}

	public static boolean isClassStrategySet(){
		if(ComplexInput.s_simstrategy == null){
			return false;
		}else{
			return true;
		}
	}

	public static void setClassStrategy(ComplexSimilarityMetricStrategy s){
		ComplexInput.s_simstrategy = s;
	}
	
}
