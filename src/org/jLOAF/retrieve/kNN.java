package org.jLOAF.retrieve;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jLOAF.casebase.Case;
import org.jLOAF.casebase.CaseBase;
import org.jLOAF.inputs.Input;
/**
 * This class creates a KNN class that implements Retrieval. 
 * @author sachagunaratne
 *
 */
public class kNN implements Retrieval {

	private int k;
	private CaseBase cb;
	private Distance [] dist_closest;
	
	public kNN(int k, CaseBase cb){
		this.cb = cb;
		this.k = k;
		this.dist_closest = new Distance [k];
	}
	
	/**
	 * Takes an input, compares its similarity to each case in the casebase, creates an array of distance objects and sorts it.
	 * Takes the top -k Similar Cases. 
	 * @param i An Input
	 * @return A list of the top K cases (Most similar)
	 */
	@Override
	public List<Case> retrieve(Input i) {
		//creates a new array of distance objects
		Distance [] dist = new Distance[cb.getSize()];
		
		int count =0;
		
		//creates a new distance object with the distance between the current case and the case in the case base, as well as the case in the case base
		for(Case c: cb.getCases()){
			dist[count] = new Distance(i.similarity(c.getInput()),c);
			count++;
		}
		
		//sorts the array in descending order
		Arrays.sort(dist);
		
		//could use a sorting algorithm to sort an arraylist of cases in order
		//add 
		for(int ji=0;ji<k;ji++){
			dist_closest[ji] = dist[ji];
		}
		
		//gets the k closest neighbours
		List<Case> best = new ArrayList<Case>();
		for(int ii=0;ii<k;ii++) best.add(dist[ii].getCase());
		
		return best;
	}
	
	/**
	 * Returns the top_k closest distance values in an array. 
	 * @return dist_closest
	 */
	public Distance [] getDist(){
		
		return dist_closest;
	}

}
