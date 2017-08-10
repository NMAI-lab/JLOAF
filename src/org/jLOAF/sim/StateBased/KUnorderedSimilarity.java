package org.jLOAF.sim.StateBased;

import java.util.HashMap;

import org.jLOAF.inputs.Input;
import org.jLOAF.inputs.StateBasedInput;
import org.jLOAF.sim.StateBasedSimilarity;

public class KUnorderedSimilarity extends StateBasedSimilarity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int k;
	public KUnorderedSimilarity(int k){
		this.k=k;
	}

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
	/**
	 * puts the trace's elements in a hashMap, where the keys of the hashmap are the names of those elements, and the values are the count of those
	 * keys, i.e how many times they have appeared in the trace
	 * @param sizeSt1 the size of the trace
	 * @param i1Run the hashMap that will hold the counts
	 * @param st1 the stateBasedInput that holds the trace
	 */
	private void putInRun(int sizeSt1, HashMap<String, Integer> i1Run,StateBasedInput st1) {
		for(int i=0;i<Math.min(sizeSt1,k);i=i+2){
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
	/**
	 * takes two HashMaps, where they represent the counts of two traces' elements, and compare only the counts of the same elements.
	 * if there are elements in one HasMap that don't exist in the other, a penalty gets deducted from the similarity.
	 * @param i1Run the HashMap for the first trace
	 * @param i2Run the HashMap for the second trace
	 * @return the difference between the counts of the two traces's elements, where 1.0 means they are the same. and zero means, the have nothing in common.
	 */
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
		return similarity/(i1Run.keySet().size()+i2Run.keySet().size()-count);
	}

}
