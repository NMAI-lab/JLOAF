package tests.junit.sim.complex;

import static org.junit.Assert.*;

import org.jLOAF.inputs.ComplexInput;
import org.jLOAF.sim.ComplexSimilarityMetricStrategy;
import org.jLOAF.sim.complex.Jacard;
import org.jLOAF.sim.complex.Mean;
import org.jLOAF.sim.complex.SorensonDice;
import org.junit.Test;

public class SorensonDiceTest {
	/***
	 * The size of the intersection between flags and flags1 is 3
	 * The size of flag is 6 and the size of flags1 is 5
	 * so the solution is 3/(0.5*(5+6)) = 3/5.5 = 0.545
	 * In this instance there is no requirement for the comparison of the atomicInputs because all it looks at is the size of the sets and the intersections
	 * between the two sets
	 * ***/
	@Test
	public void TestSorensonDice(){
		
		ComplexSimilarityMetricStrategy simMetric1 = new SorensonDice();
		ComplexSimilarityMetricStrategy simMetric2 = new Mean();
		
		ComplexInput flag1 = new ComplexInput("flag1",simMetric2);
		ComplexInput flag2 = new ComplexInput("flag2", simMetric2);
		ComplexInput flag5 = new ComplexInput("flag5", simMetric2);
		ComplexInput flag7 = new ComplexInput("flag7", simMetric2);
		ComplexInput flag6 = new ComplexInput("flag6", simMetric2);
		ComplexInput flag4 = new ComplexInput("flag4", simMetric2);

		
		ComplexInput flags = new ComplexInput("flag", simMetric1);
		flags.add(flag1);
		flags.add(flag2);
		flags.add(flag7);
		flags.add(flag6);
		flags.add(flag4);
		flags.add(flag5);

		ComplexInput flag3 = new ComplexInput("flag3",simMetric2);
		ComplexInput flag8 = new ComplexInput("flag4", simMetric2);
		ComplexInput flag9 = new ComplexInput("flag7", simMetric2);
		ComplexInput flag10 = new ComplexInput("flag9", simMetric2);
		ComplexInput flag11 = new ComplexInput("flag5", simMetric2);
		
		ComplexInput flags1 = new ComplexInput("flag", simMetric1);
		flags1.add(flag3);
		flags1.add(flag8);
		flags1.add(flag9);
		flags1.add(flag10);
		flags1.add(flag11);
		
		assertEquals(flags.similarity(flags1),0.545,0.001);
		
	}
}


