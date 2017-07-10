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
public class KOrderedSimilarityTest {
	private CaseBase cb;
	StateBasedSimilarity sim1 ;
	Feature f1 ;
	Feature f3 ;Feature f2; 
	SimilarityMetricStrategy sim;
	

	@Before 
	public void initAll(){
		cb = new CaseBase();
		 sim = new Equality();
		sim1 = new KOrderedSimilarity(1);
		 f1 = new Feature(1);
		 f2 = new Feature(2);
		 f3 = new Feature(3);
		AtomicInput i1 = new AtomicInput("a",f1,sim);
		AtomicInput i2 = new AtomicInput("a1",f2,sim);
		AtomicInput i3 = new AtomicInput("a2",f3,sim);
		AtomicAction a1 = new AtomicAction("play");
		AtomicAction a2 = new AtomicAction("play1");
		AtomicAction a3 = new AtomicAction("play2");
		cb.createThenAdd(i1, a1, sim1);
		cb.createThenAdd(i2, a2, sim1);
		cb.createThenAdd(i3, a3, sim1);
		
	}
	
	@Test
	public void testKIsOne(){
		StateBasedSimilarity sim2 = new KOrderedSimilarity(1);
		StateBasedInput si = new StateBasedInput("a",sim2);
		AtomicInput i = new AtomicInput("A",f1,sim);
		//AtomicAction a = new AtomicAction("play2");
		//Case c = new Case(i,a);
		si.setInput(i);
		//si.setCase(c);
		//in this case the currentInput of the statedBasedInput is i of feature f1,
		//this will be the most similar to the input of the case that has an input of feature f1, everything else will be ignored since k is only 1.
		double max=0;
		Case c0 =null;
		for(Case c1:cb.getCases()){
			double sim =si.similarity(c1.getInput());
			if(max<sim){
				max=sim;
				c0=c1;
			}
		}
		//here the most similar one is going to be input a.
		//given that the stateBasedInput takes the name of the currentInput
		assertEquals(c0.getInput().getName(),"a");
		
		
		
	}
	@Test
	public void testKIsTwo(){
		StateBasedSimilarity sim2 = new KOrderedSimilarity(2);
		StateBasedInput si = new StateBasedInput("a",sim2);
		AtomicInput i = new AtomicInput("A",f1,sim);
		AtomicAction a = new AtomicAction("play2");
		StateBasedInput si2 = new StateBasedInput("a1",sim2);
		si2.setInput(i);
		Case c = new Case(si2,a);
		si.setInput(i);
		si.setCase(c);
		//in this case the currentInput of the statedBasedInput is i of feature f1 and action play2,
		//this will be the most similar to the case that has the first two element of its input's trace the same as i.
		AtomicInput i0 = new AtomicInput("test",f1,sim);
		AtomicAction a0 = new AtomicAction("play2");
		cb.createThenAdd(i0, a0, sim2);
		double max=0;
		Case c0 =null;
		for(Case c1:cb.getCases()){
			double sim =si.similarity(c1.getInput());
			if(max<sim){
				max=sim;
				c0=c1;
			}
		}
		//here the most similar one is going to be input test, because even though input a has the same first input as our testing stateBasedInput.
		//the latest Action is different
		//given that the stateBasedInput takes the name of the currentInput
		assertEquals(c0.getInput().getName(),"test");
		
		
		
	}
	
}
