package tests.junit.sim.atomic;

import org.jLOAF.inputs.AtomicInput;
import org.jLOAF.inputs.Feature;
import org.jLOAF.sim.AtomicSimilarityMetricStrategy;
import org.jLOAF.sim.SimilarityMetricStrategy;
import org.jLOAF.sim.atomic.PercentDifference;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class PercentDifferenceTest {
	
	AtomicSimilarityMetricStrategy sim = new PercentDifference();
	@Test
	public void TestPDNotSame(){
		
		AtomicInput i1 = new AtomicInput("h1",new Feature(1.6),sim);
		AtomicInput i2 = new AtomicInput("h2",new Feature(1.2),sim);
		assertEquals(i1.similarity(i2),0.857,0.001);
	}
	
	@Test
	public void TestPDDividebyZero(){
		AtomicInput i1 = new AtomicInput("h1",new Feature(1.6),sim);
		AtomicInput i2 = new AtomicInput("h2",new Feature(-1.6),sim);
		assertEquals(i1.similarity(i2),0.0,0.001);
	}
	
}
