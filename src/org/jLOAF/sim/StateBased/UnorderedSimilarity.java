package org.jLOAF.sim.StateBased;

import java.util.HashMap;

import org.jLOAF.inputs.Input;
import org.jLOAF.inputs.StateBasedInput;
import org.jLOAF.sim.StateBasedSimilarity;
/*
 * this class represents a similarity metric that compares two stateBased Inputs based on the actions and inputs of their traces, regardless of 
 * the order they are in
 * @author Ibrahim Ali Fawaz
 */
public class UnorderedSimilarity extends StateBasedSimilarity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public double similarity(Input i1, Input i2) {
		if(i1==null || i2 ==null) return 0.0;
		
		if(!(i1 instanceof StateBasedInput) || !(i2 instanceof StateBasedInput)){
			throw new IllegalArgumentException("this metric is only used for StateBased inputs");
		}
		HashMap<String,Integer> i1Run = new HashMap<String,Integer>();
		HashMap<String,Integer> i2Run= new HashMap<String,Integer>();
		StateBasedInput st1 = (StateBasedInput)i1;
		StateBasedInput st2 = (StateBasedInput)i2;
		int sizeSt1 = st1.getSize();
		int sizeSt2=st2.getSize();
		
		putInRun(sizeSt1,i1Run,st1);
		putInRun(sizeSt2,i2Run,st2);
		
		
		return compare(i1Run,i2Run);
	}

	private void putInRun(int sizeSt1, HashMap<String, Integer> i1Run,StateBasedInput st1) {
		for(int i=0;i<sizeSt1;i=i+2){
			String a="";
			if(i+1!=sizeSt1){
			 a = st1.getAction(i+1).getName();
			
			
			if(i1Run.get(a)==null){
				i1Run.put(a,1);
			}else{
			i1Run.put(a, i1Run.get(a)+1);
			}
			}
			String in = st1.getInput(i).toString();
			if(i1Run.get(in)==null){
				i1Run.put(in,1);
			}else{
			i1Run.put(in, i1Run.get(in)+1);
			}
		}
		
	}

	private double compare(HashMap<String, Integer> i1Run, HashMap<String, Integer> i2Run) {
		
		double similarity=0;
		int count =0;
		
		for(String s :i1Run.keySet()){
			if(i2Run.get(s)!=null){
				similarity+=Math.min(i2Run.get(s),i1Run.get(s))/Math.max(i2Run.get(s),i1Run.get(s));
			count++;
			}else{
			
			}
		}
		return similarity/(2*i1Run.keySet().size()+i2Run.keySet().size()-2*count);
	}

}
