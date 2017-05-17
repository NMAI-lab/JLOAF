package tests.junit.sim.complex;

import static org.junit.Assert.*;

import org.jLOAF.inputs.AtomicInput;
import org.jLOAF.inputs.ComplexInput;
import org.jLOAF.inputs.Feature;
import org.jLOAF.sim.ComplexSimilarityMetricStrategy;
import org.jLOAF.sim.SimilarityMetricStrategy;
import org.jLOAF.sim.atomic.EuclideanDistance;
import org.jLOAF.sim.complex.GreedyMunkrezMatching;
import org.jLOAF.sim.complex.Mean;
import org.junit.Before;
import org.junit.Test;

public class GreedyMunkrezMatchingTest {
	
	/***
	 * This test takes the exact same set and compares it to itself.
	 * The algorithm figures out which pairing of the two flags to make and outputs a similarity value of 1.0 
	 * which is what we would expect. 
	 * @author sachagunaratne
	 * ****/
	@Test
	public void TestGMMSameSet(){
		SimilarityMetricStrategy sim = new EuclideanDistance();
		AtomicInput dist1 = new AtomicInput("dist", new Feature(25.8), sim);
		AtomicInput dist2 = new AtomicInput("dist", new Feature(83.9), sim);
		AtomicInput dir1 = new AtomicInput("dir", new Feature(-8), sim);
		AtomicInput dir2 = new AtomicInput("dir", new Feature(14), sim);
		
		ComplexSimilarityMetricStrategy simMetric1 = new GreedyMunkrezMatching();
		ComplexSimilarityMetricStrategy simMetric2 = new Mean();
		
		ComplexInput flag1 = new ComplexInput("flag1",simMetric2);
		flag1.add(dist1);
		flag1.add(dir1);
		
		ComplexInput flag2 = new ComplexInput("flag2", simMetric2);
		flag2.add(dist2);
		flag2.add(dir2);
		
		ComplexInput flags = new ComplexInput("flag", simMetric1);
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
		
		ComplexInput flags1 = new ComplexInput("flag", simMetric1);
		flags1.add(flag3);
		flags1.add(flag4);
		
		assertEquals(flags.similarity(flags1),1.0,0.1);
	}
	
	@Test
	/***
	 * This test takes two different sets and compares them to each other.
	 * The algorithm figures out which pairing of the two flags to make and outputs a similarity value of 0.0405
	 * which is what we would expect. 
	 * The matching functions like this:
	 * flags has two complexInputs: flag1 and flag2
	 * flags1 has two complexInputs: flag3 and flag4
	 * Each flag(i) has two atomicInputs: dist and dir
	 * 
	 * 		  dist	dir
	 * flag1: 25.8  -8
	 * flag2: 83.9  14
	 * flag3: 50.2  -20
	 * flag4: 16    45
	 * 
	 * Each flag(i) is compared with every flag in the list above and the similarities are calculated and stored in tuple object.
	 * The tuple object holds each flags name and the similarity between them
	 * Each tuple object is stored in a list:
	 * 
	 * 0.0581 flag3,flag1
	 * 0.0229 flag2,flag4
	 * 0.0556 flag4,flag1
	 * 0.0287 flag2,flag3
	 * 
	 * This list of tuples is then sorted in descending order:
	 * 
	 * 0.0581 flag3,flag1
	 * 0.0556 flag4,flag1
	 * 0.0287 flag2,flag3
	 * 0.0229 flag2,flag4
	 * 
	 * The items are then popped off the top of the list and the similarity is added to a total and the names of each flag is added to a used_item list.
	 * This happens only if both the flag names are not in the used_item list and until the list of tuples is empty
	 * -------------------------------------------
	 * cycle 0
	 * 
	 * total = 0.0581, used_item => <flag3,flag1>
	 * 
	 * 0.0556 flag4,flag1
	 * 0.0287 flag2,flag3
	 * 0.0229 flag2,flag4
	 * --------------------------------------------
	 * cycle 1
	 * 
	 * total = 0.0581, used_item => <flag3,flag1>
	 * 
	 * 0.0287 flag2,flag3
	 * 0.0229 flag2,flag4
	 * -------------------------------------------
	 * cycle 2
	 * 
	 * total = 0.0581, used_item => <flag3,flag1>
	 * 
	 * 0.0229 flag2,flag4
	 * 
	 * ------------------------------------------
	 * 
	 * cycle 3
	 * 
	 * total = 0.0581+0.0229, used_item => <flag3,flag1,flag2,flag4>
	 * 
	 * ------------------------------------------
	 * 
	 * The total is then divided by the size of the largest group
	 * 
	 * 0.0810/2 = 0.0405
	 * 
	 * Which is the value that is returned. 
	 * 
	 * @author sachagunaratne
	 * ****/
	public void TestGMMDifferentSet(){
		SimilarityMetricStrategy sim = new EuclideanDistance();
		AtomicInput dist1 = new AtomicInput("dist", new Feature(25.8), sim);
		AtomicInput dist2 = new AtomicInput("dist", new Feature(83.9), sim);
		AtomicInput dir1 = new AtomicInput("dir", new Feature(-8), sim);
		AtomicInput dir2 = new AtomicInput("dir", new Feature(14), sim);
		
		ComplexSimilarityMetricStrategy simMetric1 = new GreedyMunkrezMatching();
		ComplexSimilarityMetricStrategy simMetric2 = new Mean();
		
		ComplexInput flag1 = new ComplexInput("flag1",simMetric2);
		flag1.add(dist1);
		flag1.add(dir1);
		
		ComplexInput flag2 = new ComplexInput("flag2", simMetric2);
		flag2.add(dist2);
		flag2.add(dir2);
		
		ComplexInput flags = new ComplexInput("flag", simMetric1);
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
		
		ComplexInput flags1 = new ComplexInput("flag", simMetric1);
		flags1.add(flag3);
		flags1.add(flag4);
		
		assertEquals(flags.similarity(flags1),0.0405,0.001);
	}
	/***
	 * This test tests the functionality when the set sizes are different.
	 * There will be size matchings since there are 3 in one set and 2 in the other. 3*2 =6
	 * These matchings will be placed in the list of tuples and ordered. They will then be popped off and because there will be 3 values added to the total 
	 * the total will be divided by 3. (The largest group size)
	 * ***/
	@Test
	public void TestGMMDifferentSetDiffSize(){
		SimilarityMetricStrategy sim = new EuclideanDistance();
		AtomicInput dist1 = new AtomicInput("dist", new Feature(25.8), sim);
		AtomicInput dist2 = new AtomicInput("dist", new Feature(83.9), sim);
		AtomicInput dir1 = new AtomicInput("dir", new Feature(-8), sim);
		AtomicInput dir2 = new AtomicInput("dir", new Feature(14), sim);
		AtomicInput dist5 = new AtomicInput("dist", new Feature(5.9), sim);
		AtomicInput dir5 = new AtomicInput("dir", new Feature(-1), sim);
		
		ComplexSimilarityMetricStrategy simMetric1 = new GreedyMunkrezMatching();
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
		
		
		ComplexInput flag3 = new ComplexInput("flag3",simMetric2);
		flag3.add(dist3);
		flag3.add(dir3);
		
		ComplexInput flag4 = new ComplexInput("flag4", simMetric2);
		flag4.add(dist4);
		flag4.add(dir4);
		
		ComplexInput flags1 = new ComplexInput("flag", simMetric1);
		flags1.add(flag3);
		flags1.add(flag4);
		
		assertEquals(flags.similarity(flags1),0.0379,0.001);
	}
}
