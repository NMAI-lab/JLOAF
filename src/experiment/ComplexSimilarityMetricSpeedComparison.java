package experiment;

import org.jLOAF.inputs.AtomicInput;
import org.jLOAF.inputs.ComplexInput;
import org.jLOAF.inputs.Feature;
import org.jLOAF.sim.ComplexSimilarityMetricStrategy;
import org.jLOAF.sim.atomic.EuclideanDistance;
import org.jLOAF.sim.complex.AuctionMaximalMatching;
import org.jLOAF.sim.complex.GreedyMunkrezMatching;

public class ComplexSimilarityMetricSpeedComparison {
	/***
	 * This main is used to calculate the speed at which the different similarity metrics can compute the similarity between two sets
	 * Currently it tests GMM and Auction algorithms 
	 * @author sachagunaratne
	 * ***/
	public static void main(String a[]){
		AtomicInput dist1 = new AtomicInput("dist", new Feature(25.8));
		AtomicInput dist2 = new AtomicInput("dist", new Feature(83.9));
		AtomicInput dir1 = new AtomicInput("dir", new Feature(-8));
		AtomicInput dir2 = new AtomicInput("dir", new Feature(14));
		
		ComplexInput ball1 = new ComplexInput("ball");
		ball1.add(dist1);
		ball1.add(dir1);
		
		ComplexInput ball2 = new ComplexInput("ball");
		ball2.add(dist2);
		ball2.add(dir2);
		
		ComplexSimilarityMetricStrategy simMetric1 = new AuctionMaximalMatching();
		ComplexSimilarityMetricStrategy simMetric2 = new GreedyMunkrezMatching();
		
		AtomicInput.setClassStrategy(new EuclideanDistance());
		System.out.println("Starting test...");
		long time = System.nanoTime();
		System.out.println(simMetric1.similarity(ball1, ball2));
		long done = System.nanoTime();
		System.out.println("time taken for auction: " + (done - time)/1000000.0 + " milliseconds");
		
		time = System.nanoTime();
		System.out.println(simMetric2.similarity(ball1, ball2));
		done = System.nanoTime();
		System.out.println("time taken for gmm: " + (done - time)/1000000.0 + " milliseconds");
	}
}
