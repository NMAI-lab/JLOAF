package org.jLOAF.inputs.complex;

import org.jLOAF.inputs.ComplexInput;
import org.jLOAF.inputs.Feature;
import org.jLOAF.inputs.Input;
import org.jLOAF.inputs.atomic.MatrixCell;
import org.jLOAF.sim.SimilarityMetricStrategy;

public class Matrix extends ComplexInput {

	private static final long serialVersionUID = 1L;

	private static SimilarityMetricStrategy simMet;
	
	private int rows = 0;
	private int cols = 0;
	
	public Matrix(String name, double[][] matrix) {
		super(name);
		this.rows = matrix.length;
		this.cols = matrix[0].length;
		for(int ii = 0; ii < this.rows; ii++){
			for(int jj = 0; jj < this.cols; jj++){
				MatrixCell mc = new MatrixCell(ii+"-"+jj, new Feature(matrix[ii][jj]));
				this.add(mc);
			}
		}
	}

	@Override
	public double similarity(Input i) {
		//See if the user has defined similarity for each specific input, for all inputs
		//  of a specific type, of defered to superclass
		if(this.simStrategy != null){
			return simStrategy.similarity(this, i);
		}else if(Matrix.isClassStrategySet()){
			return Matrix.similarity(this, i);
		}else{
			return super.similarity(i);
		}
	}

	private static double similarity(Input complexInput, Input i) {
		return Matrix.simMet.similarity(complexInput, i);
	}

	public static boolean isClassStrategySet(){
		if(Matrix.simMet == null){
			return false;
		}else{
			return true;
		}
	}

	public static void setClassSimilarityMetric(SimilarityMetricStrategy s){
		Matrix.simMet = s;
	}
}
