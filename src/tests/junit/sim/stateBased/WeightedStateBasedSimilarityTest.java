package tests.junit.sim.stateBased;

import static org.junit.Assert.assertTrue;

import org.jLOAF.casebase.Case;
import org.jLOAF.inputs.StateBasedInput;
import org.jLOAF.sim.StateBasedSimilarity;
import org.jLOAF.sim.StateBased.UnorderedSimilarity;
import org.jLOAF.sim.StateBased.WeightedStateBasedSimilarity;
import org.junit.Before;
import org.junit.Test;
/*
 * this test will ensure that in the weightedStateBasedSimilarity only the most current elements matter,
 * and the example that was used to prove that is shown below
 * @author Ibrahim Ali Fawaz
 */
public class WeightedStateBasedSimilarityTest extends StateBasedSimilarityTest {

	
	
	@Before 
	public void initAll(){
		initialize(new WeightedStateBasedSimilarity());
		
	}
	
	@Test
	public void testSameSizeDifferentOrder(){
		//temp is the stateBasedInput of the last case in the casebase, which has the longest trace of size 17.
		StateBasedInput temp = (StateBasedInput)((Case)cb.getCases().toArray()[cb.getSize()-1]).getInput();
		//st will be an input which is of the same trace as temp.
		StateBasedInput st =temp.copyTraceupTo(16);
		//st1 will have the same elements of temp up to the 14th element
		StateBasedInput st0 =temp.copyTraceupTo(14);
		
		//now since only the first 5-10 elemets matter in this weightedStateBasedSimilarity similarity, the difference between the
		//two similarities calculated below shouldn't be more than 0.1
		
		assertTrue(temp.similarity(st)-temp.similarity(st0)<0.1);
	}
}
