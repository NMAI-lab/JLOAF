package tests.junit.sim.atomic;

import org.jLOAF.inputs.AtomicInput;
import org.jLOAF.inputs.Feature;
import org.jLOAF.sim.SimilarityMetricStrategy;
import org.jLOAF.sim.atomic.EuclideanDistance;
import org.jLOAF.sim.atomic.PercentDifference;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class EuclideanDistanceTest {
	SimilarityMetricStrategy sim = new EuclideanDistance();
	@Test
	public void TestEEquals(){
		AtomicInput i1 = new AtomicInput("h1",new Feature(1.6),sim);
		AtomicInput i2 = new AtomicInput("h2",new Feature(1.6),sim);
		assertEquals(i1.similarity(i2),1.0,0.1);
	}
	
	@Test
	public void TestEDNotEquals(){
		AtomicInput i1 = new AtomicInput("h1",new Feature(2.6),sim);
		AtomicInput i2 = new AtomicInput("h2",new Feature(1.2),sim);
		assertEquals(i1.similarity(i2),0.416,0.01);
	}
}
