package tests.junit.sim.atomic;

import org.jLOAF.inputs.AtomicInput;
import org.jLOAF.inputs.Feature;
import org.jLOAF.sim.atomic.EuclideanDistance;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class EuclideanDistanceTest {
	@Test
	public void TestEEquals(){
		AtomicInput i1 = new AtomicInput("h1",new Feature(1.6));
		AtomicInput i2 = new AtomicInput("h2",new Feature(1.6));
		EuclideanDistance e = new EuclideanDistance();
		assertEquals(e.similarity(i1, i2),1.0,0.1);
	}
	
	@Test
	public void TestEDNotEquals(){
		AtomicInput i1 = new AtomicInput("h1",new Feature(2.6));
		AtomicInput i2 = new AtomicInput("h2",new Feature(1.2));
		EuclideanDistance e = new EuclideanDistance();
		assertEquals(e.similarity(i1, i2),0.714,0.01);
	}
}
