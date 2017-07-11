package tests.junit.featureSelection;

import static org.junit.Assert.*;

import org.jLOAF.preprocessing.filter.CaseBaseFilter;
import org.jLOAF.preprocessing.filter.featureSelection.SequentialBackwardGeneration;
import org.junit.Before;
import org.junit.Test;

public class SequentialBackwardGenerationTest extends FeatureSelectionTest{
	
		
	
	@Before
	public void doInitialize(){
		
		initialize();
	}
	
	/*
	 * @see KOrderedSimilarityTest
	 */
	@Test 
	public void initAll(){
			
		/*
		 * -----------------------------------------------------------------------------------------
		 */
		//now we come the sequentialBackwardGeneration algorithm to test.
		CaseBaseFilter sq = new SequentialBackwardGeneration(null,2,0.9);//2 is just k, which is how many nodes you would like to test after a bestNode has found,
		//it is suggested that k be a larger number for casesbase of big size.
		//0.9 is the epsilon that should be around 0.9 to see how much of improvement an adjusted list of feature does to the performance.
		sq.filter(cb);
		//now after we filter the casebase, we need to check that the weight of the a1 input is zero
		//the filter changes the weight map in the similarityWeights class.
		System.out.println(sim2.getWeight("a")+ " "+sim2.getWeight("a1"));
		assertTrue(sim2.getWeight("a")==1.0);
		assertTrue(sim2.getWeight("a1")==0.0);
		
		
		
		
	}
}
