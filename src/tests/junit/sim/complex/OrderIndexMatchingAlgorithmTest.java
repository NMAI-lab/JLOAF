package tests.junit.sim.complex;

import static org.junit.Assert.assertEquals;

import org.jLOAF.inputs.AtomicInput;
import org.jLOAF.inputs.ComplexInput;
import org.jLOAF.inputs.Feature;
import org.jLOAF.sim.AtomicSimilarityMetricStrategy;
import org.jLOAF.sim.ComplexSimilarityMetricStrategy;
import org.jLOAF.sim.SimilarityMetricStrategy;
import org.jLOAF.sim.atomic.EuclideanDistance;
import org.jLOAF.sim.complex.GreedyMunkrezMatching;
import org.jLOAF.sim.complex.Mean;
import org.jLOAF.sim.complex.OrderIndexMatchingAlgorithm;
import org.junit.Before;
import org.junit.Test;

public class OrderIndexMatchingAlgorithmTest {

	private ComplexSimilarityMetricStrategy simMetric1 ;
	private ComplexSimilarityMetricStrategy simMetric2; 
	private ComplexInput flags;
	private ComplexInput flags1 ;
	private AtomicSimilarityMetricStrategy  sim;
	
	
	@Before 
	public void initiate(){
		simMetric1= new OrderIndexMatchingAlgorithm("flag","dist");
		simMetric2= new Mean();
		flags = new ComplexInput("flag", simMetric1);
		flags1= new ComplexInput("flag", simMetric1);
		sim = new EuclideanDistance();
		
		
	}
	/*
	 * 
	 * this test takes the exact same sets and compares them to each other, 
	 * after it sorts the "falg" inputs in ascending order based on the 
	 * distance feature, that is specified in (new OrderIndexMatchingAlgoritm("flag","dist")
	 * the output should be 1 in this case.
	 * @author Ibrahim Ali Fawaz
	 */
	
	@Test
	public void TestOIMAwithSameSets(){
		
		AtomicInput dist1 = new AtomicInput("dist", new Feature(25.8), sim);
		AtomicInput dist2 = new AtomicInput("dist", new Feature(83.9), sim);
		AtomicInput dir1 = new AtomicInput("dir", new Feature(-8), sim);
		AtomicInput dir2 = new AtomicInput("dir", new Feature(14), sim);
		
		
		
		
		ComplexInput flag1 = new ComplexInput("flag1",simMetric2);
		flag1.add(dist1);
		flag1.add(dir1);
		
		ComplexInput flag2 = new ComplexInput("flag2", simMetric2);
		flag2.add(dist2);
		flag2.add(dir2);
		
		
		flags.add(flag1);
		flags.add(flag2);
		
		AtomicInput dist3 = new AtomicInput("dist", new Feature(25.8), sim);
		AtomicInput dist4 = new AtomicInput("dist", new Feature(83.9), sim);
		AtomicInput dir3 = new AtomicInput("dir", new Feature(-8), sim);
		AtomicInput dir4 = new AtomicInput("dir", new Feature(14), sim);
		
		
		ComplexInput flag3 = new ComplexInput("flag1",simMetric2);
		flag3.add(dist3);
		flag3.add(dir3);
		
		ComplexInput flag4 = new ComplexInput("flag2", simMetric2);
		flag4.add(dist4);
		flag4.add(dir4);
		
		
		flags1.add(flag3);
		flags1.add(flag4);
		
		assertEquals(flags.similarity(flags1),1.0,0.1);
		
		
	}
	@Test
	/*
	 * this test takes two complex input, each having two other complex inputs, then
	 * it will sort the two "flag"inputs based on the distance of each flag in them
	 * and then it will compare the closest flags with each other, and the farthest flags with each other
	 * For the direction feature, their order will stay the same.
	 * thus it is going to compare flag1 and flag4 , and flag2 and flag3 together.
	 * so we get for flag1 with flag4 :0.0556
	 * for flag 2 and flag3:0.0287
	 * so the total result should be : 0.0556+0.0287/2 =0.0412
	 * @author Ibrahim Ali Fawaz
	 */
	public void TestOIMDifferentSet(){
		
		AtomicInput dist1 = new AtomicInput("dist", new Feature(25.8), sim);
		AtomicInput dist2 = new AtomicInput("dist", new Feature(83.9), sim);
		AtomicInput dir1 = new AtomicInput("dir", new Feature(-8), sim);
		AtomicInput dir2 = new AtomicInput("dir", new Feature(14), sim);
		
		
		
		ComplexInput flag1 = new ComplexInput("flag1",simMetric2);
		flag1.add(dist1);
		flag1.add(dir1);
		
		ComplexInput flag2 = new ComplexInput("flag2", simMetric2);
		flag2.add(dist2);
		flag2.add(dir2);
		
		
		flags.add(flag1);
		flags.add(flag2);
		
		AtomicInput dist3 = new AtomicInput("dist", new Feature(50.2), sim);
		AtomicInput dist4 = new AtomicInput("dist", new Feature(16), sim);
		AtomicInput dir3 = new AtomicInput("dir", new Feature(-20), sim);
		AtomicInput dir4 = new AtomicInput("dir", new Feature(45), sim);
		
		
		ComplexInput flag3 = new ComplexInput("flag3",simMetric2);
		flag3.add(dist3);
		flag3.add(dir3);
		
		ComplexInput flag4 = new ComplexInput("flag4", simMetric2);
		flag4.add(dist4);
		flag4.add(dir4);
		
		
		flags1.add(flag3);
		flags1.add(flag4);
		
		assertEquals(flags.similarity(flags1),0.0421,0.001);
	}
	/***
	 * This test tests the functionality when the set sizes are different.
	 * There will be size a penalty since they are of difference sizes.
	 * the penalty is -2.0
	 * and the total will be divided by 3.
	 * but now you compare flag5 with flag 4 and flag 1 with flag 3
	 * @author Ibrahim ALI Fawaz
	 * ***/
	@Test
	public void TestOIMDifferentSetDiffSize(){
		
		AtomicInput dist1 = new AtomicInput("dist", new Feature(25.8), sim);
		AtomicInput dist2 = new AtomicInput("dist", new Feature(83.9), sim);
		AtomicInput dir1 = new AtomicInput("dir", new Feature(-8), sim);
		AtomicInput dir2 = new AtomicInput("dir", new Feature(14), sim);
		AtomicInput dist5 = new AtomicInput("dist", new Feature(5.9), sim);
		AtomicInput dir5 = new AtomicInput("dir", new Feature(-1), sim);
		
		
		
		ComplexInput flag1 = new ComplexInput("flag1",simMetric2);
		flag1.add(dist1);
		flag1.add(dir1);
		
		ComplexInput flag2 = new ComplexInput("flag2", simMetric2);
		flag2.add(dist2);
		flag2.add(dir2);
		
		ComplexInput flag5 = new ComplexInput("flag5", simMetric2);
		flag5.add(dist5);
		flag5.add(dir5);
		
		
		flags.add(flag1);
		flags.add(flag2);
		flags.add(flag5);
		
		AtomicInput dist3 = new AtomicInput("dist", new Feature(50.2), sim);
		AtomicInput dist4 = new AtomicInput("dist", new Feature(16), sim);
		AtomicInput dir3 = new AtomicInput("dir", new Feature(-20), sim);
		AtomicInput dir4 = new AtomicInput("dir", new Feature(45), sim);
		
		
		ComplexInput flag3 = new ComplexInput("flag3",simMetric2);
		flag3.add(dist3);
		flag3.add(dir3);
		
		ComplexInput flag4 = new ComplexInput("flag4", simMetric2);
		flag4.add(dist4);
		flag4.add(dir4);
		
		
		flags1.add(flag3);
		flags1.add(flag4);
		
		assertEquals(flags.similarity(flags1),-0.628,0.001);
	}
}
