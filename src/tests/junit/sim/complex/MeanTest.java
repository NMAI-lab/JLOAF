package tests.junit.sim.complex;

import static org.junit.Assert.assertEquals;

import org.jLOAF.inputs.AtomicInput;
import org.jLOAF.inputs.ComplexInput;
import org.jLOAF.inputs.Feature;
import org.jLOAF.sim.ComplexSimilarityMetricStrategy;
import org.jLOAF.sim.SimilarityMetricStrategy;
import org.jLOAF.sim.atomic.EuclideanDistance;
import org.jLOAF.sim.complex.AuctionMaximalMatching;
import org.jLOAF.sim.complex.Mean;
import org.junit.Test;

public class MeanTest {
	/***
	 * This class gets the keys of each complexInput.
	 * The keys are used to get the same value from the two sets.
	 * If there is a key in one set but not in the other a penalty is added to the total 
	 * 
	 * set1: <flag5, flag2, flag1>
	 * set2: <flag1, flag2>
	 * ------------------------------------
	 * cycle 0
	 * 
	 * flag5 doesn't exist in set2
	 * 
	 * total = -10
	 * -----------------------------------
	 * cycle 1
	 * 
	 * match flag 2 with flag 2
	 * 
	 * total = -10+0.0557
	 *  -----------------------------------
	 * cycle 2
	 * 
	 * match flag 1 with flag 1
	 * 
	 * total = -10+0.0557+0.0581
	 * -----------------------------------
	 * end cycles
	 * 
	 * total = total/largest set = -9.886/3 = -3.295
	 ***/
	@Test
	public void TestMeanDifferentSizeSets(){
		
		SimilarityMetricStrategy sim = new EuclideanDistance();
		AtomicInput dist1 = new AtomicInput("dist", new Feature(25.8), sim);
		AtomicInput dist2 = new AtomicInput("dist", new Feature(83.9), sim);
		AtomicInput dir1 = new AtomicInput("dir", new Feature(-8), sim);
		AtomicInput dir2 = new AtomicInput("dir", new Feature(14), sim);
		AtomicInput dist5 = new AtomicInput("dist", new Feature(5.9), sim);
		AtomicInput dir5 = new AtomicInput("dir", new Feature(-1), sim);
		
		ComplexSimilarityMetricStrategy simMetric1 = new Mean();
		ComplexSimilarityMetricStrategy simMetric2 = new Mean();
		
		ComplexInput flag1 = new ComplexInput("flag1",simMetric2);
		flag1.add(dist1);
		flag1.add(dir1);
		
		ComplexInput flag2 = new ComplexInput("flag2", simMetric2);
		flag2.add(dist2);
		flag2.add(dir2);
		
		ComplexInput flag5 = new ComplexInput("flag5", simMetric2);
		flag2.add(dist5);
		flag2.add(dir5);
		
		ComplexInput flags = new ComplexInput("flag", simMetric1);
		flags.add(flag1);
		flags.add(flag2);
		flags.add(flag5);
		
		AtomicInput dist3 = new AtomicInput("dist", new Feature(50.2), sim);
		AtomicInput dist4 = new AtomicInput("dist", new Feature(16), sim);
		AtomicInput dir3 = new AtomicInput("dir", new Feature(-20), sim);
		AtomicInput dir4 = new AtomicInput("dir", new Feature(45), sim);
		
		
		ComplexInput flag3 = new ComplexInput("flag1",simMetric2);
		flag3.add(dist3);
		flag3.add(dir3);
		
		ComplexInput flag4 = new ComplexInput("flag2", simMetric2);
		flag4.add(dist4);
		flag4.add(dir4);
		
		ComplexInput flags1 = new ComplexInput("flag", simMetric1);
		flags1.add(flag3);
		flags1.add(flag4);
		
		assertEquals(flags.similarity(flags1),-3.295,0.001);
		
	}
	
	/****
	 * In this test, there are no elements in common.
	 * Therefore there will be a penalty applied each time of -10. 
	 * This will be divided by teh largest set of 3 so the total will end up being
	 * -10.
	 * 
	 * **/
	@Test
	public void TestMeanNoElementsInCommon(){
		
		SimilarityMetricStrategy sim = new EuclideanDistance();
		AtomicInput dist1 = new AtomicInput("dist", new Feature(25.8), sim);
		AtomicInput dist2 = new AtomicInput("dist", new Feature(83.9), sim);
		AtomicInput dir1 = new AtomicInput("dir", new Feature(-8), sim);
		AtomicInput dir2 = new AtomicInput("dir", new Feature(14), sim);
		AtomicInput dist5 = new AtomicInput("dist", new Feature(5.9), sim);
		AtomicInput dir5 = new AtomicInput("dir", new Feature(-1), sim);
		
		ComplexSimilarityMetricStrategy simMetric1 = new Mean();
		ComplexSimilarityMetricStrategy simMetric2 = new Mean();
		
		ComplexInput flag1 = new ComplexInput("flag1",simMetric2);
		flag1.add(dist1);
		flag1.add(dir1);
		
		ComplexInput flag2 = new ComplexInput("flag2", simMetric2);
		flag2.add(dist2);
		flag2.add(dir2);
		
		ComplexInput flag5 = new ComplexInput("flag3", simMetric2);
		flag2.add(dist5);
		flag2.add(dir5);
		
		ComplexInput flags = new ComplexInput("flag", simMetric1);
		flags.add(flag1);
		flags.add(flag2);
		flags.add(flag5);
		
		AtomicInput dist3 = new AtomicInput("dist", new Feature(50.2), sim);
		AtomicInput dist4 = new AtomicInput("dist", new Feature(16), sim);
		AtomicInput dir3 = new AtomicInput("dir", new Feature(-20), sim);
		AtomicInput dir4 = new AtomicInput("dir", new Feature(45), sim);
		
		
		ComplexInput flag3 = new ComplexInput("flag4",simMetric2);
		flag3.add(dist3);
		flag3.add(dir3);
		
		ComplexInput flag4 = new ComplexInput("flag5", simMetric2);
		flag4.add(dist4);
		flag4.add(dir4);
		
		ComplexInput flags1 = new ComplexInput("flag", simMetric1);
		flags1.add(flag3);
		flags1.add(flag4);
		
		assertEquals(flags.similarity(flags1),-10,0.001);
		
	}
}
