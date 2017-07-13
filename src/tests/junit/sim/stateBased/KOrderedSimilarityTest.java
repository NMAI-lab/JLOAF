package tests.junit.sim.stateBased;

import static org.junit.Assert.*;

import org.jLOAF.action.AtomicAction;
import org.jLOAF.casebase.Case;
import org.jLOAF.casebase.CaseBase;
import org.jLOAF.inputs.AtomicInput;
import org.jLOAF.inputs.Feature;
import org.jLOAF.inputs.StateBasedInput;
import org.jLOAF.sim.SimilarityMetricStrategy;
import org.jLOAF.sim.StateBasedSimilarity;
import org.jLOAF.sim.StateBased.KOrderedSimilarity;
import org.jLOAF.sim.atomic.Equality;
import org.junit.Before;
import org.junit.Test;

/*
 * this class is to test the korderedSimilarity metric for stateBasedInputs 
 * when k is specified, only the last k elements in the traces matter 
 */
public class KOrderedSimilarityTest extends StateBasedSimilarityTest {
	
	

	@Before 
	public void initAll(){
		initialize(new KOrderedSimilarity(1));
		
	}
	
	@Test
	public void testKIsOne(){
		
		StateBasedInput si = new StateBasedInput("a",sim1);
		AtomicInput i = new AtomicInput("A",f1,sim);
		//AtomicAction a = new AtomicAction("play2");
		//Case c = new Case(i,a);
		si.setInput(i);
		//si.setCase(c);
		//in this case the currentInput of the statedBasedInput is i of feature f1,
		//this will be the most similar to the input of the case that has an input of feature f1, everything else will be ignored since k is only 1.
		Case c0= getMostSimilar(si);
		//here the most similar one is going to be input a.
		//given that the stateBasedInput takes the name of the currentInput
		assertEquals(c0.getInput().getName(),"i1");
		
		
		
	}
	@Test
	public void testKIsTwo(){
		StateBasedSimilarity sim2 = new KOrderedSimilarity(2);
		StateBasedInput si = new StateBasedInput("a",sim2);
		AtomicInput i = new AtomicInput("A",f1,sim);
		AtomicAction a = new AtomicAction("a4");
		StateBasedInput si2 = new StateBasedInput("a1",sim2);
		si2.setInput(i);
		Case c = new Case(si2,a);
		si.setInput(i);
		si.setCase(c);//in this case the trace of the input from oldest to most recent is : null-A(f1)-play3-A(f1)
		//in this case the currentInput of the statedBasedInput is i of feature f1 and action play2,
		//this will be the most similar to the case that has the first two element of its input's trace the same as i.
		AtomicInput i0 = new AtomicInput("test",f1,sim);
		AtomicAction a0 = new AtomicAction("a4");
		cb.createThenAdd(i0, a0, sim2);//the most two recent elements of the trace for the created input would be : whatever the last case's action was with a current .
		//in our cases if you look at the last add of a case in stateBasedSimilarityTest you would find it is also play3 
		
		Case c0 =getMostSimilar(si);
		//here the most similar one is going to be input test, because even though input a has the same first input as our testing stateBasedInput.
		//the latest Action is different
		//given that the stateBasedInput takes the name of the currentInput
		
		assertEquals(c0.getInput().getName(),"test");
		
		
		
		
	}
	
}
