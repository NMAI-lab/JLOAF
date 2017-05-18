package tests.junit.sim.complex;

import static org.junit.Assert.assertTrue;

import org.jLOAF.inputs.AtomicInput;
import org.jLOAF.inputs.ComplexInput;
import org.jLOAF.inputs.Feature;
import org.jLOAF.sim.atomic.EuclideanDistance;
import org.jLOAF.sim.complex.Pessimistic;
import org.jLOAF.sim.complex.WeightedMean;
import org.jLOAF.weights.SimilarityWeights;
import org.junit.Before;
import org.junit.Test;
/*
 * the pessimistic similarity will return the minimum similarity between two complex inputs, in this case the minum similarity
 * would be between the features that are not similar, and in our case it 0.5(k1 and k2 )
 */
public class PessimisticTest {
	private ComplexInput i1,i2;
	String feature1,feature2,feature3;
	Pessimistic p;
	
	 @Before
	    public void initInputs() {
		 
		  feature1 = "orange";
		  feature2="apple";
		
		 
		  p=new Pessimistic();
		  i1 = new ComplexInput("I1",p);
		  i2 = new ComplexInput("I2",p);
		 
	      
	    }
	
	
	@Test
	public void TestWMOne(){
		AtomicInput j1 = new AtomicInput(feature1,new Feature(2.0),new EuclideanDistance());
		AtomicInput k1 = new AtomicInput(feature2, new Feature(2.0),new EuclideanDistance());
	
		AtomicInput j2 = new AtomicInput(feature1,new Feature(2.0),new EuclideanDistance());
		AtomicInput k2 = new AtomicInput(feature2, new Feature(1.0),new EuclideanDistance());
		
		
		i1.add(j1);i1.add(k1);i2.add(j2);i2.add(k2);
		double similarity1= i1.similarity(i2);
		 
	
		assertTrue(similarity1==0.5);
		
		
	
	
	
	
	}
	

}
