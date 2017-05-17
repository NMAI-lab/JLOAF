package tests.junit.sim.complex;

import static org.junit.Assert.*;

import org.jLOAF.inputs.AtomicInput;
import org.jLOAF.inputs.ComplexInput;
import org.jLOAF.inputs.Feature;
import org.jLOAF.sim.ComplexSimilarityMetricStrategy;
import org.jLOAF.sim.SimilarityMetricStrategy;
import org.jLOAF.sim.atomic.EuclideanDistance;
import org.jLOAF.sim.complex.GreedyMunkrezMatching;
import org.junit.Test;

public class GreedyMunkrezMatchingTest {
	
	@Test
	public void TestGMM(){
		SimilarityMetricStrategy sim = new EuclideanDistance();
		AtomicInput dist1 = new AtomicInput("dist", new Feature(25.8), sim);
		AtomicInput dist2 = new AtomicInput("dist", new Feature(83.9), sim);
		AtomicInput dir1 = new AtomicInput("dir", new Feature(-8), sim);
		AtomicInput dir2 = new AtomicInput("dir", new Feature(14), sim);
		
		ComplexSimilarityMetricStrategy simMetric1 = new GreedyMunkrezMatching();
		ComplexSimilarityMetricStrategy simMetric2 = new GreedyMunkrezMatching();
		
		ComplexInput ball1 = new ComplexInput("ball",simMetric1);
		ball1.add(dist1);
		ball1.add(dir1);
		
		ComplexInput ball2 = new ComplexInput("ball", simMetric2);
		ball2.add(dist2);
		ball2.add(dir2);
		
		assertEquals(ball1.similarity(ball2),0.0444,0.1);
	}
}
