package org.jLOAF.sim.complex;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import org.jLOAF.inputs.ComplexInput;
import org.jLOAF.inputs.Input;
import org.jLOAF.sim.ComplexSimilarityMetricStrategy;


/***
 * Uses the Auction algorithm to solve the assignment problem.
 * https://agtb.wordpress.com/2009/07/13/auction-algorithm-for-bipartite-matching/
 * Followed along with this tutorial
 * @author sachagunaratne
 * ***/
public class AuctionMaximalMatching extends ComplexSimilarityMetricStrategy {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Uses the auction algorithm to match two sets of inputs together in the best possible manner.
	 */
	@Override
	public double similarity(Input i1, Input i2) {
		
		if(!(i1 instanceof ComplexInput) || !(i2 instanceof ComplexInput) ){
			throw new IllegalArgumentException("Mean.similarity(..): Not ComplexInputs");
		}
		
		ComplexInput cplx1 = (ComplexInput)i1;
		ComplexInput cplx2 = (ComplexInput)i2;
		
		Set<String> keys = cplx1.getChildNames();
		Set<String> keys2 = cplx2.getChildNames();
		
		if(keys.size()==0 && keys2.size()==0){
			//if they cannot see anything they are in a similar situation?
			return 1.0;
		}
		
		if(keys.size()==0 || keys2.size()==0){
			//if they cannot see anything they are in a similar situation?
			return 0.0;
		}
		
		//create a similarity matrix
		int row = 0;
		int col = 0;
		double [][] similarities = new double [keys.size()][keys2.size()];
		
		for(String s1: keys){
			for(String s2: keys2){
				similarities[row][col] = cplx1.get(s1).similarity(cplx2.get(s2));
				col++;
			}
			col=0;
			row++;
		}
		
		//create price, owner and bidders
		double [] price = new double [keys2.size()];
		int [] owner = new int [keys2.size()];
		Queue<Integer> bidders = new LinkedList<Integer>();
		
		//offset so we can do the check later
		for(int i=1;i<=keys.size();i++){
			bidders.add(i);
		}
		
		
		double sigma = 1.0/(keys2.size()+1);
		//double sigma = 0.01;
		
		//start loop
		while(!bidders.isEmpty()){
			//get bidders from queue
			int bidder = bidders.poll()-1;
			//gets the good that maximizes wij-pj
			int good = argmax(similarities,price, bidder);
			
			if(bidder==-1 ||good == -1){
				System.out.println("hi");
			}
			if(similarities[bidder][good]-price[good]>=0){
				//adds current owner to queue if it exists
				if(owner[good]!=0){
					bidders.add(owner[good]);
				}
				//sets ownder of good to current bidder
				owner[good]=bidder+1;
				//sets price of good to update price
				price[good] = price[good]+sigma;
			}
			
		}
		
		double total=0;
		int counter = 0;
		//get the total similarity based on matching
		for(int i=0;i<owner.length;i++){
			if(owner[i]!=0){
				total += similarities[owner[i]-1][i];
				counter++;
			}
		}
		
		if(keys.size()==0) return 1.0;
		
		return total/counter;
	}
	/***
	 * gets the index that maximizes the w[bidder,good]-price[good]
	 * @author sachagunaratne
	 * @return argmax index
	 * ***/
	private int argmax(double[][] similarities, double[] price, int bidder) {
		double max=0;
		int pos =0;
		for(int i=0;i<price.length;i++){
			if(max<similarities[bidder][i]-price[i]){max = similarities[bidder][i]-price[i];pos = i;}
		}
		return pos;
	}

}
