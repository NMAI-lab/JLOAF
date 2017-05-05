package tests.junit.sim.atomic;

import org.jLOAF.inputs.AtomicInput;
import org.jLOAF.inputs.Feature;
import org.jLOAF.sim.atomic.Equality;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class EqualityTest {

	@Test
	public void testEquality(){
		AtomicInput i1 = new AtomicInput("h1",new Feature(1.6));
		AtomicInput i2 = new AtomicInput("h2",new Feature(1.6)); 
		Equality e = new Equality();
		assertEquals(e.similarity(i1, i2),1.0, 0.1);
	}
	
	@Test
	public void testNotEquality(){
		AtomicInput i1 = new AtomicInput("h1",new Feature(1.6));
		AtomicInput i2 = new AtomicInput("h2",new Feature(1.2)); 
		Equality e = new Equality();
		assertEquals(e.similarity(i1, i2),0.0, 0.1);
	}
}
