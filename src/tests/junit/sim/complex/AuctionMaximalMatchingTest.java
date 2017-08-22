package tests.junit.sim.complex;

import static org.junit.Assert.assertEquals;

import org.jLOAF.inputs.AtomicInput;
import org.jLOAF.inputs.ComplexInput;
import org.jLOAF.inputs.Feature;
import org.jLOAF.sim.AtomicSimilarityMetricStrategy;
import org.jLOAF.sim.ComplexSimilarityMetricStrategy;
import org.jLOAF.sim.SimilarityMetricStrategy;
import org.jLOAF.sim.atomic.EuclideanDistance;
import org.jLOAF.sim.complex.AuctionMaximalMatching;
import org.jLOAF.sim.complex.GreedyMunkrezMatching;
import org.jLOAF.sim.complex.Mean;
import org.junit.Test;

public class AuctionMaximalMatchingTest {
	/****
	 * This tests the exact same set and compare it to itself.
	 * It returns 1.0 which is what is expected.
	 * ***/
	@Test
	public void TestAMMSameSet(){
		
		AtomicSimilarityMetricStrategy sim = new EuclideanDistance();
		AtomicInput dist1 = new AtomicInput("dist", new Feature(25.8), sim);
		AtomicInput dist2 = new AtomicInput("dist", new Feature(83.9), sim);
		AtomicInput dir1 = new AtomicInput("dir", new Feature(-8), sim);
		AtomicInput dir2 = new AtomicInput("dir", new Feature(14), sim);
		
		ComplexSimilarityMetricStrategy simMetric1 = new AuctionMaximalMatching();
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
	 * Each flag(i) is compared with every flag in the list above and the similarities are calculated and stored in a 2D array as follows:
	 * 
	 * 		| flag1|flag2 |
	 * flag3|0.0581|0.0287|
	 * flag4|0.0556|0.0229|
	 * 
	 * The inputs are divided into two: bidders and good where bidders are the items in set1 and goods are items in set2
	 * There is a setup of an empty list of owners for each good, a array of prices which are all zeros for each good and a queue of bidders
	 * 
	 * The similarities are the weights
	 * 
	 * while there are bidders in the queue:
	 * The first bidder is popped off the queue and then the good that maximizes weight-price for that bidder is determined.
	 * If the value of weight-price for that bidder and good exceed zero 
	 * then the bidder is added as the owner of that good, 
	 * the price is updated for that good
	 * and if there is already an owenr for the good he is put back in the queue of bidders
	 * --------------------------------------------------------------
	 * cycle 0
	 * 
	 * 
	 * current bidder = flag2
	 * good = flag3
	 * 
	 * owner(flag3) = flag2
	 * price(flag3) = price(flag3)+ 1/(3)
	 * 
	 * bidders = <flag1>
	 * 
	 * --------------------------------------------------------------
	 * cycle 1
	 * 
	 * 
	 * current bidder = flag1
	 * good = flag4
	 * 
	 * owner(flag1) = flag4
	 * price(flag1) = price(flag4)+ 1/(3)
	 * 
	 * bidders = <>
	 * 
	 * -------------------------------------------------------------
	 * 
	 * total = flag1->flag4 + flag2->flag3
	 * total = 0.0556+0.0287 = 0.0843
	 * 
	 * total is divided by the number of owners
	 * 
	 * 0.0843/2 = 0.0421
	 * ***/
	@Test
	public void TestAMMDifferentSet(){
		AtomicSimilarityMetricStrategy sim = new EuclideanDistance();
		AtomicInput dist1 = new AtomicInput("dist", new Feature(25.8), sim);
		AtomicInput dist2 = new AtomicInput("dist", new Feature(83.9), sim);
		AtomicInput dir1 = new AtomicInput("dir", new Feature(-8), sim);
		AtomicInput dir2 = new AtomicInput("dir", new Feature(14), sim);

		ComplexSimilarityMetricStrategy simMetric1 = new AuctionMaximalMatching();
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

		assertEquals(flags.similarity(flags1),0.0421,0.001);
	}
	/***
	 * This test tests the functionality when the set sizes are different.
	 * There will be size matchings since there are 3 in one set and 2 in the other. 3*2 =6
	 * The table will be larger and there will be 3 bidders for 2 goods
	 * Therefore there will be a replacement of the bidders in the queue and owners will be replaced as the price increases
	 * This seems to provide a higher similarity than GMM does for the same situation
	 * 0.0569 vs 0.0379
	 */
	@Test
	public void TestAMMDifferentSetDiffSize(){
		AtomicSimilarityMetricStrategy sim = new EuclideanDistance();
		AtomicInput dist1 = new AtomicInput("dist", new Feature(25.8), sim);
		AtomicInput dist2 = new AtomicInput("dist", new Feature(83.9), sim);
		AtomicInput dir1 = new AtomicInput("dir", new Feature(-8), sim);
		AtomicInput dir2 = new AtomicInput("dir", new Feature(14), sim);
		AtomicInput dist5 = new AtomicInput("dist", new Feature(5.9), sim);
		AtomicInput dir5 = new AtomicInput("dir", new Feature(-1), sim);
		
		ComplexSimilarityMetricStrategy simMetric1 = new AuctionMaximalMatching();
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
		
		assertEquals(flags.similarity(flags1),0.0569,0.001);
	}
}
