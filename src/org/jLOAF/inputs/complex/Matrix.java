package org.jLOAF.inputs.complex;

import org.jLOAF.inputs.ComplexInput;
import org.jLOAF.inputs.Feature;
import org.jLOAF.inputs.Input;
import org.jLOAF.inputs.atomic.MatrixCell;
import org.jLOAF.sim.SimilarityMetricStrategy;

public class Matrix extends ComplexInput {

	private static final long serialVersionUID = 1L;
	
	private int rows = 0;
	private int cols = 0;
	
	public Matrix(String name, double[][] matrix, SimilarityMetricStrategy sim) {
		super(name,sim);
		this.rows = matrix.length;
		this.cols = matrix[0].length;
		for(int ii = 0; ii < this.rows; ii++){
			for(int jj = 0; jj < this.cols; jj++){
				MatrixCell mc = new MatrixCell(ii+"-"+jj, new Feature(matrix[ii][jj]), sim);
				this.add(mc);
			}
		}
	}
}
