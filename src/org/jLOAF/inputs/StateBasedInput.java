package org.jLOAF.inputs;

import java.util.ArrayList;

import org.jLOAF.sim.SimilarityMetricStrategy;

public class StateBasedInput extends ComplexInput {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ArrayList<Object> trace;
	public StateBasedInput(String name, SimilarityMetricStrategy sim) {
		super(name, sim);
		trace= new ArrayList<Object>();
		
	}
	
	
	public void addTrace(Object o){
		trace.add(o);
	}
	public void setTrace(ArrayList<Object> trace){
		this.trace=trace;
	}
	public ArrayList<Object> getTrace(){
		return trace;
	}
	public Object get(int index){
		return trace.get(index);
	}
	public int size(){
		return trace.size();
	}

}
