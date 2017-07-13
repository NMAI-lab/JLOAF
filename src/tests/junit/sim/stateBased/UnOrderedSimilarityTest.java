package tests.junit.sim.stateBased;

import static org.junit.Assert.assertTrue;

import org.jLOAF.casebase.Case;
import org.jLOAF.inputs.StateBasedInput;
import org.jLOAF.sim.StateBasedSimilarity;
import org.jLOAF.sim.StateBased.OrderedSimilarity;
import org.jLOAF.sim.StateBased.UnorderedSimilarity;
import org.junit.Before;
import org.junit.Test;
/*
 * this test is written to prove that two traces will be considered the same if they have the same elements, 
 * even if the order was not the same.
 * @author ibrahim Ali Fawaz
 */
public class UnOrderedSimilarityTest extends StateBasedSimilarityTest{
	
	@Before 
	public void initAll(){
		initialize(new UnorderedSimilarity());
		
	}
	@Test
	public void testSameSizeDifferentOrder(){
		//temp is the stateBasedInput of the last case in the casebase, which has the longest trace of size 17.
		StateBasedInput temp = (StateBasedInput)((Case)cb.getCases().toArray()[cb.getSize()-1]).getInput();
		//st will be an input which is of the same trace as temp.
		StateBasedInput st =temp.copyTraceupTo(16);
		//st1 will have the same elements of temp's trace but it will have its first two elements, the same as temp's last two elements
		StateBasedInput st0 =temp.copyTraceupTo(14);
		StateBasedInput st1 = new StateBasedInput("a",(StateBasedSimilarity)st0.getSimilarityMetricStrategy());
		st1.setInput(temp.getInput(16));
		st1.setCase(new Case(st0,temp.getAction(15)));
		//now since the order doesn't matter, temp should have the same similarity between the two inputs
		System.out.println(temp.similarity(st)+" "+temp.similarity(st1));
		assertTrue(temp.similarity(st)==temp.similarity(st1));
	}

}
