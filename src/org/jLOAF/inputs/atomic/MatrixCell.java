package org.jLOAF.inputs.atomic;

import org.jLOAF.inputs.AtomicInput;
import org.jLOAF.inputs.Feature;
import org.jLOAF.inputs.Input;
import org.jLOAF.sim.SimilarityMetricStrategy;

public class MatrixCell extends AtomicInput {

	private static final long serialVersionUID = 1L;
	private static SimilarityMetricStrategy simMet;
	
	public MatrixCell(String name, Feature f) {
		super(name, f);
	}

	@Override
	public double similarity(Input i) {
		//See if the user has defined similarity for each specific input, for all inputs
		//  of a specific type, of defered to superclass
		if(this.simStrategy != null){
			return simStrategy.similarity(this, i);
		}else if(MatrixCell.isClassStrategySet()){
			return MatrixCell.similarity(this, i);
		}else{
			return super.similarity(i);
		}
	}

	private static double similarity(Input cellInput, Input i) {
		return MatrixCell.simMet.similarity(cellInput, i);
	}

	public static boolean isClassStrategySet(){
		if(MatrixCell.simMet == null){
			return false;
		}else{
			return true;
		}
	}

	public static void setClassSimilarityMetric(SimilarityMetricStrategy s){
		MatrixCell.simMet = s;
	}
}
