package org.jLOAF.sim.complex;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jLOAF.inputs.ComplexInput;
import org.jLOAF.inputs.Input;
import org.jLOAF.sim.ComplexSimilarityMetricStrategy;

public class AuctionMaximalMatching extends ComplexSimilarityMetricStrategy {

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
		
		//create a similarity matrix
		int row = 0;
		int col = 0;
		double [][] similarities = new double [keys.size()][keys2.size()];
		String [] buyers = new String[keys.size()];
		String [] objects = new String[keys2.size()];
		
		for(String s1: keys){
			buyers[row] = s1;
			for(String s2: keys2){
				similarities[row][col] = cplx1.get(s1).similarity(cplx2.get(s2));
				objects[col] = s2;
				col++;
			}
			row++;
		}
		//setup
		double [] price = new double [keys2.size()];
		int [] ji;
		double [] vi;
		double [] ui;
		double eta = 5;
		double [] buyerBid;
		
		List<String> buyersList = new ArrayList<String>(keys);
		List<String> objectsList = new ArrayList<String>(keys2);
		List<String> assignments = new ArrayList<String>();
		
		//start loop
		//phase 1
		//calculate step 1: maximize
		ji = argmax(similarities,price);
		vi = max(similarities, price);
		ui = maxWithout(similarities, price,ji);
		
		//step 2: calculate bids
		buyerBid = getBids(similarities,ui,ji,eta);
		
		//phase 2
		//step 3: calculate the new prices
		for(int i=0;i<ji.length;i++){
			if(ji[i]==i){price[ji[i]]=setMaxPrice(buyerBid,ji, i);}
		}
		//step 4:remove maximum bidder from bidder set and add to assignment set
		int maxBidder = getMaxBidder(vi);
		int maxBidItem = ji[maxBidder];
		
		buyersList.remove(buyers[maxBidder]);
		assignments.add(buyers[maxBidder] + " " + objects[maxBidItem]);
		
		return 0;
	}
	
	private int getMaxBidder(double[] vi) {
		double max =0;
		int pos=0;
		for(int i=0;i<vi.length;i++){
			if(max<vi[i]){max=vi[i];pos=i;}
		}
		return pos;
	}

	/**
	 * sets the price to the max of the set of bidders on object j
	 * ****/
	private double setMaxPrice(double[] buyerBid, int[] ji, int index) {
		double max=0;
		int val = ji[index];
		for(int i=0;i<buyerBid.length;i++){
			if(ji[i]==val){
				if(max<buyerBid[i]){max = buyerBid[i];}
			}
		}
		return max;
	}

	private double[] getBids(double[][] similarities, double[] ui, int[] ji, double eta) {
		double [] bids = new double [ui.length];
		for(int i=0;i<ui.length;i++){
			bids[i] = similarities[i][ji[i]]-ui[i]+eta;
			
		}
		return null;
	}

	private double[] maxWithout(double[][] similarities, double[] price, int [] ji) {
		double[] max=new double [similarities.length];
		for(int i=0;i<similarities.length;i++){
			for(int j=0;j<price.length;j++){
				if(j!=ji[i]){
					if(max[i]<similarities[i][j]-price[j]){max[i] = similarities[i][j]-price[j];}
				}
			}
		}
		return max;
	}

	private double[] max(double[][] similarities, double[] price) {
		double [] max=new double [similarities.length];
		for(int i=0;i<similarities.length;i++){
			for(int j=0;j<price.length;j++){
				if(max[i]<similarities[i][j]-price[j]){max[i] = similarities[i][j]-price[j];}
			}
		}
		return max;
	}

	private int[] argmax(double[][] similarities, double[] price) {
		double [] max = new double [similarities.length];
		int [] pos = new int [similarities.length];
		for(int i=0;i<similarities.length;i++){
			for(int j=0;j<price.length;j++){
				if(max[i]<similarities[i][j]-price[j]){ pos[i] = j; max[i] = similarities[i][j]-price[j];}
			}
		}
		return pos;
	}

}
