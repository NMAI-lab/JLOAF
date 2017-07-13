package tests.junit.sim.stateBased;

import static org.junit.Assert.*;

import org.jLOAF.casebase.Case;
import org.jLOAF.inputs.StateBasedInput;
import org.jLOAF.sim.StateBasedSimilarity;
import org.jLOAF.sim.StateBased.KOrderedSimilarity;
import org.jLOAF.sim.StateBased.OrderedSimilarity;
import org.junit.Before;
import org.junit.Test;
/*
 * this test class is to test the orderedSimilarity metric for stateBasedInput,
 * the order here matters, and any change of the order will cause a big difference in the results of the similarity
 * @author Ibrahim ALi Fawaz
 */
public class OrderedSimilarityTest extends StateBasedSimilarityTest {

	
	
	@Before 
	public void initAll(){
		initialize(new OrderedSimilarity());
		
	}
	
	
	@Test
	public void testSameTrace(){
		//let's supposed we have a stateBased Input that has the same trace as another stateBasedInput, they should be the most similar,
		//compared to any other input.
		//if we take the trace of the last case's input of the caseBase and we copy it into another input
		//here for simplicity, we know that the last inputs size is 17.
		//check stateBasedSimilarityTest to make sure of that.
		StateBasedInput st =((StateBasedInput)((Case)cb.getCases().toArray()[cb.getSize()-1]).getInput()).copyTraceupTo(16);
			Case c0 =getMostSimilar (st);
			// in this case the most similar input to it would be 
			assertTrue(c0.getInput().getName().equals("i9"));
			
		
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
		//now since the order matters, temp will be more similar to st than to st1
		
		assertTrue(temp.similarity(st)>temp.similarity(st1));
	}
	
	@Test
	public void bigSizeDifference(){
		//lets compare two inputs where the size difference is more than 5, this will give a similarity of zero
		StateBasedInput temp = (StateBasedInput)((Case)cb.getCases().toArray()[cb.getSize()-1]).getInput();
		StateBasedInput st = temp.copyTraceupTo(4);
		assertTrue(st.similarity(temp)==0);
		
		
		
	}
}
