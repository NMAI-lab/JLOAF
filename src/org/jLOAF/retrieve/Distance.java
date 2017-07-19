package org.jLOAF.retrieve;

import org.jLOAF.casebase.Case;
/**
 * This class creates a comparable Distance class that contains a double distance and a case.
 * @author sachagunaratne
 *
 */
public class Distance implements Comparable<Distance>  {
	private double distance;
	private Case c;
	
	public Distance(double d, Case c){
		distance=d;
		this.c = c;
	}
	
	/**
	 * Returns distance
	 * @return distance
	 */
	public double getDistance() {
		return distance;
	}
	/**
	 * Sets the distance
	 * @param distance
	 */
	public void setDistance(double distance) {
		this.distance = distance;
	}
	/**
	 * Returns a case
	 * @return Case
	 */
	public Case getCase() {
		return c;
	}
	/**
	 * Sets a case
	 * @param c
	 */
	public void setCase(Case c) {
		this.c = c;
	}
	
	/**
	 * Takes a distance object and compares it to the current distance object.
	 * This definition is required if sorting is to be performed. 
	 * @param arg0 A distance object
	 */
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
