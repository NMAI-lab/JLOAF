package org.jLOAF.preprocessing.standardization;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.apache.commons.math3.stat.descriptive.SummaryStatistics;
import org.jLOAF.action.Action;
import org.jLOAF.casebase.Case;
import org.jLOAF.casebase.CaseBase;
import org.jLOAF.inputs.AtomicInput;
import org.jLOAF.inputs.ComplexInput;
import org.jLOAF.inputs.Feature;
import org.jLOAF.inputs.Input;
import org.jLOAF.inputs.StateBasedInput;
import org.jLOAF.preprocessing.filter.CaseBaseFilter;
import org.jLOAF.sim.SimilarityMetricStrategy;
import org.jLOAF.sim.atomic.EuclideanDistance;

/***
 * This function will take a CaseBase and normalize each feature (Column) to zero mean and unit variance. 
 * @author sachagunaratne
 * ***/
public class Standardization extends CaseBaseFilter {
	SummaryStatistics ss = new SummaryStatistics();
	
	public Standardization(CaseBaseFilter f) {
		super(f);
	}

	@Override
	public CaseBase filter(CaseBase initial) {
		HashMap<String,List<Double>> inputs = getFeatures(initial);	
		List<Double> temp = new ArrayList<Double>();
		
		for(String key: inputs.keySet()){
			temp = inputs.get(key);
			addValuestoSS(temp);
			temp = standardized(temp, ss.getMean(),ss.getStandardDeviation());
			inputs.put(key, temp);
			ss.clear();
		}
		
		SimilarityMetricStrategy Atomic_strat = new EuclideanDistance();
		
		int count = 0;
		for(Case c: initial.getCases()){
			Set<String> childNames = ((ComplexInput)((StateBasedInput)c.getInput()).getInput()).getChildNames();
			for(String key: childNames){
				Set <String> secondChildNames = ((ComplexInput)((ComplexInput)((StateBasedInput)c.getInput()).getInput()).getChildren().get(key)).getChildNames();
				for(String key2: secondChildNames){
					for(String key3: inputs.keySet()){
						((ComplexInput)((ComplexInput)((StateBasedInput)c.getInput()).getInput()).getChildren().get(key2)).getChildren().put(key3, new AtomicInput(key3,new Feature(inputs.get(key3).get(count)),Atomic_strat));
					}
				}
			}
			count++;
			
		}
		
		
		return initial;
	}
	/**
	 * Takes a list, a mean, and standard deviation. Standardizes the list.
	 * @param List<Double>, double mean, double std
	 * @return List<Double>
	 * @author sachagunaratne
	 * ***/
	private List<Double> standardized(List<Double> temp, double mean, double standardDeviation) {
		List<Double> standardize_list = new ArrayList<Double>();
		for(double val: temp){
			standardize_list.add(val-mean/standardDeviation);
		}
		return standardize_list;
	}

	/***
	 * This function adds values to SummaryStatistics
	 * @param List<Double>
	 * @return nothing
	 * @author sachagunaratne
	 * ***/
	private void addValuestoSS(List<Double> list){
		//add values to ss
		for(double val:list){
			ss.addValue(val);
		}
	}
	

	/***
	 * This function will read a CaseBase and create a HashMap of feature names as keys and a list of feature values as values. 
	 * @param CaseBase
	 * @return HashMap<String,List<Double>>
	 * @author sachagunaratne
	 * ***/
	private HashMap<String,List<Double>> getFeatures(CaseBase casebase){
		HashMap<String, Double> input = new HashMap<String, Double>();
		HashMap<String,List<Double>> inputs= new HashMap<String,List<Double>>();
		int count=0;
		
		for(Case cb: casebase.getCases()){
			Input i = ((StateBasedInput)cb.getInput()).getInput();
			input = CaseBase.convert(i);
			
			for(String key: input.keySet()){
				if (!inputs.containsKey(key)){
					List<Double> temp_list = new ArrayList<Double>();
					temp_list.add(input.get(key));
					inputs.put(key,temp_list);
				}else{
					List<Double> actual_list = inputs.get(key);
					actual_list.add(input.get(key));
					inputs.replace(key, actual_list);
				}
			}
			count++;
		}
		return inputs;
	}

}
