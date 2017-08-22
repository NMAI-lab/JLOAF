package org.jLOAF.inputs.atomic;

import org.jLOAF.inputs.AtomicInput;
import org.jLOAF.inputs.Feature;
import org.jLOAF.inputs.Input;
import org.jLOAF.sim.AtomicSimilarityMetricStrategy;
import org.jLOAF.sim.SimilarityMetricStrategy;

public class MatrixCell extends AtomicInput {

	private static final long serialVersionUID = 1L;
	
	public MatrixCell(String name, Feature f,AtomicSimilarityMetricStrategy sim) {
		super(name, f, sim);
	}
}
