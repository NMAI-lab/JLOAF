package org.jLOAF.retrieve;

import org.jLOAF.casebase.Case;

public class Distance implements Comparable<Distance>  {
	private double distance;
	private Case c;
	
	public Distance(double d, Case c){
		distance=d;
		this.c = c;
	}
	
	public double getDistance() {
		return distance;
	}
	public void setDistance(double distance) {
		this.distance = distance;
	}
	public Case getCase() {
		return c;
	}
	public void setCase(Case c) {
		this.c = c;
	}
	
	@Override
	public int compareTo(Distance arg0) {
		// TODO Auto-generated method stub
		double compare = ((Distance) arg0).getDistance();
		if(this.distance==compare){
			return 0;
		}else{
			return this.distance < compare ? 1: -1;
		}
	}
	
	
	
	
	
}
