package org.jLOAF.util;

import org.jLOAF.retrieve.Distance;
/**
 * Creates a Tuple object that contains two Strings and a double.
 * This is used in GreddyMunkrezMAtchingAlgorithm 
 * @author sachagunaratne
 *
 */
public class Tuple implements Comparable<Tuple> {
	
	private String key1;
	private String key2;
	private double sim;
	
	public Tuple(String key1, String key2, double sim){
		this.key1 = key1;
		this.key2=key2;
		this.sim = sim;
	}
	/**
	 * 
	 * @return key2
	 */
	public String getKey2() {
		return key2;
	}
	
	/**
	 * 
	 * @return sim
	 */
	public double getSim() {
		return sim;
	}
	/**
	 * 
	 * @return key1
	 */
	public String getKey1() {
		return key1;
	}
	/**
	 * Used to comapare this instance to another tupe object. 
	 * @param arg0 A tuple object. 
	 */
	@Override
	public int compareTo(Tuple arg0) {
		double compare = ((Tuple) arg0).getSim();
		if(this.sim==compare){
			return 0;
		}else{
			return this.sim < compare ? 1: -1;
		}	}

}
