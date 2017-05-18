package tests.junit.sim.complex;
import static org.junit.Assert.*;

import org.jLOAF.inputs.AtomicInput;
import org.jLOAF.inputs.ComplexInput;
import org.jLOAF.inputs.Feature;
import org.jLOAF.sim.atomic.EuclideanDistance;
import org.jLOAF.sim.complex.WeightedMean;
import org.jLOAF.weights.SimilarityWeights;
import org.junit.Before;
import org.junit.Test;
public class WeightedMeanTest {
	private ComplexInput i1,i2;
	String feature1,feature2,feature3;
	WeightedMean wm;
	
	 @Before
	    public void initInputs() {
		 SimilarityWeights sw = new SimilarityWeights(1.0);
		  feature1 = "orange";
		  feature2="apple";
		 sw.setFeatureWeight(feature1, 5);
		 sw.setFeatureWeight(feature2, 10);
		 
		  wm = new WeightedMean(sw);
		  i1 = new ComplexInput("I1",wm);
		  i2 = new ComplexInput("I2",wm);
		 
	      
	    }
	
	
	@Test
	public void TestWMOne(){
		AtomicInput j1 = new AtomicInput(feature1,new Feature(2.0),new EuclideanDistance());
		AtomicInput k1 = new AtomicInput(feature2, new Feature(2.0),new EuclideanDistance());
	
		AtomicInput j2 = new AtomicInput(feature1,new Feature(2.0),new EuclideanDistance());
		AtomicInput k2 = new AtomicInput(feature2, new Feature(1.0),new EuclideanDistance());
		
		
		i1.add(j1);i1.add(k1);i2.add(j2);i2.add(k2);
		double similarity1= i1.similarity(i2);
		 j2 = new AtomicInput(feature1,new Feature(1.0),new EuclideanDistance());

		 k2 = new AtomicInput(feature2, new Feature(2.0),new EuclideanDistance());
		i2.add(j2);i2.add(k2);
		double similarity2=i1.similarity(i2);
		System.out.println(similarity2+"  "+
				similarity1);
		assertTrue(similarity2>similarity1);
		
		
	
	
	
	
	}
	

}
