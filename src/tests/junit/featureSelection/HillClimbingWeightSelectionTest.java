package tests.junit.featureSelection;

import static org.junit.Assert.assertTrue;

import org.jLOAF.preprocessing.filter.CaseBaseFilter;
import org.jLOAF.preprocessing.filter.featureSelection.GeneticAlgorithmWeightSelector;
import org.jLOAF.preprocessing.filter.featureSelection.HillClimbingFeatureSelection;
import org.junit.Before;
import org.junit.Test;

public class HillClimbingWeightSelectionTest extends FeatureSelectionTest{
	
	
	@Before
	public void doInitialize(){
		initialize();
	}
	
	@Test
	public void testWeightSelection(){
		CaseBaseFilter sq = new HillClimbingFeatureSelection(null,0.9);
		sq.filter(cb);
		System.out.println(sim2.getWeight("a"));
		System.out.println(sim2.getWeight("a1"));
		assertTrue(sim2.getWeight("a")>sim2.getWeight("a1"));//the weight of feature a should be greater then the weight of feature a1
		
		
	}

}
