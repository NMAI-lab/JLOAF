package org.jLOAF.sim.complex;

import java.util.Set;

import org.jLOAF.inputs.ComplexInput;
import org.jLOAF.inputs.Input;
import org.jLOAF.sim.ComplexSimilarityMetricStrategy;
import org.jLOAF.sim.SimilarityMetricStrategy;
import org.jLOAF.util.HungarianDouble;

/***
 * Sacha Gunaratne 2017 May
 * 
 * Uses the hungarian matching algorithm to calculate a minimum dist matching between two sets 
 * the sets can be of the same size or not. 
 * This is an assignment problem
 * ***/

public class Hungarian_alg extends ComplexSimilarityMetricStrategy {

	@Override
	public double similarity(Input i1, Input i2) {
		
		if(!(i1 instanceof ComplexInput) || !(i2 instanceof ComplexInput) ){
			throw new IllegalArgumentException("Mean.similarity(..): Not ComplexInputs");
		}
		
		ComplexInput cplx1 = (ComplexInput)i1;
		ComplexInput cplx2 = (ComplexInput)i2;
		
		Set<String> keys = cplx1.getChildNames();
		Set<String> keys2 = cplx2.getChildNames();
		
		int row = 0;
		int col = 0;
		double sim_matrix [][];
		
		if(keys.size()>=keys2.size()){
			sim_matrix = new double [keys.size()][keys.size()];
		}else{
			sim_matrix = new double [keys2.size()][keys2.size()];
		}
		
		//creates the sim matrix with the keys are rows and keys2 as col
		for (String s: keys){
			for (String s1: keys2){
				sim_matrix[row][col] = cplx1.get(s).similarity(cplx2.get(s1));
				col++;
			}
			col=0;
			row++;
		}
		
		//run Hungarian matching algorithm to calculate minimum dist 
		return getMinSim(sim_matrix);
	}
	
	public double getMinSim(double [][] matrix){
		HungarianDouble hd = new HungarianDouble(matrix);
		return hd.getTotal();
	}

}
