package org.jLOAF.sim.complex;


import java.util.List;
import java.util.Set;

import org.jLOAF.inputs.ComplexInput;
import org.jLOAF.inputs.Input;

import org.jLOAF.sim.ComplexSimilarityMetricStrategy;
import org.jLOAF.util.InputSorter;


/**
 * Sorts features in a meaningful way. Features are paired with the
 * Input of the same type, with the same index, in the other Input. For example,
 * if an input has many features, then the feature which was seen by the agent at
 * a certain position will be compared with another feature that was seen by the agent
 * at a similar position
 * 
 * If there are an uneven number of Features of a given Input, then remaining
 * Features will be matched with a null value.
 * 
 * @author Ibrahim Ali Fawaz
 * @since May 2017
 * 
 */
public class OrderIndexMatchingAlgorithm extends ComplexSimilarityMetricStrategy {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// an InputSorter object to be used for the sorting of the given input
	private InputSorter fdcp;
	/**
	 * Constructor
	 * @param sortingInput the name of the input  holding the inputs to be sorted
	 * @param feature the feature by which the selected input will be sorted.
	 */
	public OrderIndexMatchingAlgorithm(String sortingInput,String feature){
		
	 fdcp = new InputSorter(sortingInput,feature);
		
	}

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
		List<String> features1 = fdcp.getList(cplx1);
		List<String> features2 = fdcp.getList(cplx2);
		
		
		
		int f1Size = features1.size();
		int f2Size = features2.size();
		
		boolean f1MoreFeatures = f1Size>=f2Size;
		
		double totalSimilarity=0;
		
		int numPairs=0;
		
		
		
		if(f1MoreFeatures){
			//make pairs
			for(int ii=0; ii<f2Size; ii++){
				totalSimilarity+=cplx1.get(features1.get(ii)).similarity(cplx2.get(features2.get(ii)));
				numPairs++;
			}
			
			//now make pairs with the leftovers
			for(int jj = f2Size; jj<f1Size; jj++){
				
				totalSimilarity-=2.0;
				numPairs++;
			}
		}else{
			//make pairs
			for(int ii=0; ii<f1Size; ii++){
				totalSimilarity+=cplx1.get(features1.get(ii)).similarity(cplx2.get(features2.get(ii)));
				numPairs++;
			}
			
			//now make pairs with the leftovers
			for(int jj = f1Size; jj<f2Size; jj++){
				totalSimilarity-=2.0;
				numPairs++;
			}
		}
		
		
		return totalSimilarity/numPairs;
		
	
	
	
	}

}
