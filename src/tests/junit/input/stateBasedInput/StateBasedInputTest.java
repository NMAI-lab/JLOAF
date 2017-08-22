package tests.junit.input.stateBasedInput;

import static org.junit.Assert.*;

import org.jLOAF.action.AtomicAction;
import org.jLOAF.casebase.Case;
import org.jLOAF.casebase.CaseBase;
import org.jLOAF.inputs.AtomicInput;
import org.jLOAF.inputs.Feature;
import org.jLOAF.inputs.StateBasedInput;
import org.jLOAF.sim.AtomicSimilarityMetricStrategy;
import org.jLOAF.sim.SimilarityMetricStrategy;
import org.jLOAF.sim.StateBasedSimilarity;
import org.jLOAF.sim.StateBased.KOrderedSimilarity;
import org.jLOAF.sim.atomic.Equality;
import org.junit.Before;
import org.junit.Test;

public class StateBasedInputTest {
	
	
	protected CaseBase cb;
	protected StateBasedSimilarity sim1 ;
	protected Feature f1 ;
	protected Feature f3 ;Feature f2; 
	protected AtomicSimilarityMetricStrategy sim;
	
	@Before
	public void initialize(){
		cb = new CaseBase();
		 sim = new Equality();
		sim1 = new KOrderedSimilarity(1);
		 f1 = new Feature(1);
		 f2 = new Feature(2);
		 f3 = new Feature(3);
		AtomicInput i1 = new AtomicInput("i1",f1,sim);
		AtomicInput i2 = new AtomicInput("i2",f2,sim);
		AtomicInput i3 = new AtomicInput("i3",f3,sim);
		AtomicAction a1 = new AtomicAction("a1");
		AtomicAction a2 = new AtomicAction("a2");
		AtomicAction a3 = new AtomicAction("a3");
		AtomicAction a4 =new AtomicAction("a4");
		AtomicInput i4 = new AtomicInput("i4",f1,sim);
		AtomicInput i5 = new AtomicInput("i5",f2,sim);
		AtomicInput i6 = new AtomicInput("i6",f3,sim);
		AtomicInput i7 = new AtomicInput("i7",f1,sim);
		AtomicInput i8 = new AtomicInput("i8",f2,sim);
		AtomicInput i9 = new AtomicInput("i9",f3,sim);
		cb.createThenAdd(i1, a1, sim1);
		cb.createThenAdd(i2, a2, sim1);
		cb.createThenAdd(i3, a3, sim1);
		cb.createThenAdd(i4, a2, sim1);
		cb.createThenAdd(i5, a3, sim1);
		cb.createThenAdd(i6, a1, sim1);
		cb.createThenAdd(i7, a1, sim1);
		cb.createThenAdd(i8, a2, sim1);
		cb.createThenAdd(i9, a4, sim1);
		
		// to sum up what happens above, whenever the createThenAdd function gets called, it creates a trace and then sets it as an input for the case created,
		// thus all cases have their input as a stateBasedInput.
		//and example, let's take the third case created, it has action a3 and the input has a trace: i1,a1,i2,a2,i3. 
		
	}
	
	
	
	
	
	
	/*
	 * to get an action, you should pass an odd number, since it is always known for the action to be in an odd position
	 * an example would be this trace:
	 * input - action- input 
	 * in this case action is in position 1. and it goes like this
	 */
	@Test
	public void testGetAction(){
		//as said let's say we want to get the FORTH element of the the input of the third case, which is an action, it has index three.
		//first we get the third case from the caseBase:
		Case c = (Case)cb.getCases().toArray()[2];
		//not to get the third element which an action ought to be a1 which is the action play.
		assertTrue(((StateBasedInput)c.getInput()).getAction(3).getName().equals("a1"));
		//the Forth element of the 4th case's input is now a2 because the trace of it's input would be:i1,a1,i2,a2,i3,a3,i4
		Case c1 =(Case)cb.getCases().toArray()[3];
		//in this case the action would be a2..
		assertEquals(((StateBasedInput)c1.getInput()).getAction(3).getName(),"a2");
		//if we check trace of the last case's input in our, we should get from latest to soonest:i1,a1,i2,a2,i3,a3,i4,a2,i5,a3,i6,a1,i7,a1,i8,a2,i9
		//now the 10th element, i.e 9th indexed, should be action a2 :
		Case c2 = (Case)cb.getCases().toArray()[cb.getSize()-1];
		assertEquals(((StateBasedInput)c2.getInput()).getAction(9).getName(),"a2");
		
	
		
		
	}
	
	@Test
	public void tesGetSize(){
		//if we check trace of the last case's input in our cb, we should get,from latest to soonest, :i1,a1,i2,a2,i3,a3,i4,a2,i5,a3,i6,a1,i7,a1,i8,a2,i9
		// the size of this trace is, if we count, 17.
		//it is a fact that the size of a state based Input is alwasy an odd number, given the way a stateBased Input is represented
		Case c = (Case)cb.getCases().toArray()[cb.getSize()-1];
		assertTrue(((StateBasedInput)c.getInput()).getSize()==17);
	}
	/*
	 * to get an input, you should pass an even number, since it is always known for the input to be in an even position
	 * an example would be this trace:
	 * input - action- input 
	 * in this case input is in position 0 and 2. and it goes like this.
	 */
	@Test
	public void testGetInput(){
		//if we check trace of the last case's input in our, we should get from latest to soonest:i1,a1,i2,a2,i3,a3,i4,a2,i5,a3,i6,a1,i7,a1,i8,a2,i9
		//now the 9th element, i.e 8th indexed, should be input i5 :
		Case c = (Case)cb.getCases().toArray()[cb.getSize()-1];
		assertEquals(((StateBasedInput)c.getInput()).getInput(8).getName(),"i5");
		//and the last element is always an input which is i1.
		assertEquals(((StateBasedInput)c.getInput()).getInput(((StateBasedInput)c.getInput()).getSize()-1).getName(),"i1");
		
		
		
		
	}
	@Test
	public void testGetTrace(){
		
		Case c = (Case)cb.getCases().toArray()[cb.getSize()-1];
		StateBasedInput in = (StateBasedInput)c.getInput();
		StateBasedInput temp = in.copyTraceupTo(10);//this will copy the trace up to the element of index 10
		//so it means that the size of the temp input should be 11.
		assertTrue(temp.getSize()==11);
		//now let's check them element by element:
		assertEquals(temp.getInput(0).getName(),in.getInput(0).getName());
		assertEquals(temp.getInput(2).getName(),in.getInput(2).getName());
		assertEquals(temp.getInput(4).getName(),in.getInput(4).getName());
		assertEquals(temp.getInput(6).getName(),in.getInput(6).getName());
		assertEquals(temp.getInput(8).getName(),in.getInput(8).getName());
		assertEquals(temp.getInput(10).getName(),in.getInput(10).getName());
		assertEquals(temp.getAction(9).getName(),in.getAction(9).getName());
		assertEquals(temp.getAction(7).getName(),in.getAction(7).getName());
		assertEquals(temp.getAction(5).getName(),in.getAction(5).getName());
		assertEquals(temp.getAction(3).getName(),in.getAction(3).getName());
		assertEquals(temp.getAction(1).getName(),in.getAction(1).getName());
		temp =in.copyTraceupTo(2);
		assertTrue(temp.getSize()==3);
		
		
		
		
	}

}
