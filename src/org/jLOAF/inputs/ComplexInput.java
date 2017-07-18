package org.jLOAF.inputs;

import java.util.Map;
import java.util.HashMap;
import java.util.Set;

import org.jLOAF.sim.ComplexSimilarityMetricStrategy;
import org.jLOAF.sim.SimilarityMetricStrategy;
/**
 * the complexInput is just a collection of inputs, that could either be atomic or complex,
 * this is where the composite pattern comes in handy, and al inputs share same methods.
 * @author Owner
 *
 */
public class ComplexInput extends Input {

	private static final long serialVersionUID = 1L;

	private Map<String,Input> collect;
	/**
	 * Constructor
	 * @param name the name of the input
	 * @param sim the similarityMetricStrategy used by this input for comparison purposes.
	 */
	public ComplexInput(String name, SimilarityMetricStrategy sim) {
		super(name);
		super.setSimilarityMetric(sim);
		collect = new HashMap<String,Input>();
	}
	/**
	 * adds an input to the collection of inputs this complexInput has
	 * @param i the input to be added to the collection of inputs this complexInput has
	 */
	public void add(Input i){
		collect.put(i.name, i);
	}
	/**
	 * returns the input that has the name passed to this function
	 * @param name the name of the input to be returned
	 * @return the input with the given name
	 */
	public Input get(String name){
		return collect.get(name);
	}
	/**
	 * returns a set with the names of the inputs this complexInput has
	 * @return a set with the names of the inputs this complexInput has
	 */
	public Set<String> getChildNames(){
		return collect.keySet();
	}
	
	@Override
	public String toString(){
		String s ="";
		for(Input i:collect.values()){
			s+=i.toString()+",";
		}
		return s;
	}
	/**
	 * returns the collection that has the inputs of this complexInput has in a form of hashmap
	 * @return the collection that has the inputs of this complexInput has in a form of hashmap
	 */
	
	public HashMap<String,Input> getChildren(){
		return (HashMap<String,Input>)collect;
	}
	
}
