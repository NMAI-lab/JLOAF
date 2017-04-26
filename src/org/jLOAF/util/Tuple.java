package org.jLOAF.util;

import org.jLOAF.retrieve.Distance;

public class Tuple implements Comparable<Tuple> {
	
	private String key1;
	private String key2;
	private double sim;
	
	public Tuple(String key1, String key2, double sim){
		this.key1 = key1;
		this.key2=key2;
		this.sim = sim;
	}

	public String getKey2() {
		return key2;
	}

	public double getSim() {
		return sim;
	}

	public String getKey1() {
		return key1;
	}

	@Override
	public int compareTo(Tuple arg0) {
		double compare = ((Tuple) arg0).getSim();
		if(this.sim==compare){
			return 0;
		}else{
			return this.sim < compare ? 1: -1;
		}	}

}
