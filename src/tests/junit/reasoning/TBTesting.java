package tests.junit.reasoning;

import static org.junit.Assert.*;

import org.jLOAF.action.Action;
import org.jLOAF.casebase.Case;
import org.jLOAF.casebase.CaseBase;
import org.jLOAF.inputs.AtomicInput;
import org.jLOAF.inputs.Feature;
import org.jLOAF.inputs.Input;
import org.jLOAF.inputs.StateBasedInput;
import org.jLOAF.reasoning.TBReasoning;
import org.jLOAF.sim.SimilarityMetricStrategy;
import org.jLOAF.sim.atomic.Equality;
import org.junit.Before;
import org.junit.Test;

public class TBTesting {

	CaseBase cb;
	SimilarityMetricStrategy sim = new Equality();
	StateBasedInput si1;
	StateBasedInput si2;
	StateBasedInput si3;
	StateBasedInput si4;
	StateBasedInput si5;
	TBReasoning tb;
	Feature f1;
	Feature f2;
	Feature f3;
	Feature f4;
	Feature f5;
	Action a1;
	Action a2;Action a3;Action a4;Action a5;
	
	
	@Before
	public void setup(){
		
		
			f1 = new Feature(1.0);
			f2 = new Feature(1.0);
			f3 = new Feature(1.0);
			f4 = new Feature(3.0);
			f5 = new Feature(5.0);
		
		Input i1 = new AtomicInput("1", f1, sim);
		
		Input i2 = new AtomicInput("1", f2, sim);
	
		Input i3 = new AtomicInput("1", f3, sim);
		
		Input i4 = new AtomicInput("4", f4, sim);
		
		Input i5 = new AtomicInput("4", f5,sim);
	
			
		 a1 = new Action("down");
		 a2 = new Action("right");
		 a3 = new Action("up");
		 a4 = new Action("still");
		 a5 = new Action("left");
		
		//here is how we create stateBased inputs.
		 si1 = new StateBasedInput("si1",null);
		 si2 = new StateBasedInput("si2",null);
		 si3 = new StateBasedInput("si3",null);
		 si4 = new StateBasedInput("si4",null);
		 si5 = new StateBasedInput("si5",null);
		
		Case c1 = new Case(si1, a1);
		Case c2 = new Case(si2, a3);
		Case c3 = new Case(si3, a2);
		Case c4 = new Case(si4, a4);
		Case c5 = new Case(si5, a5);
		si1.setInput(i1);
		si2.setInput(i2);
		si2.setCase(c1);
		si3.setInput(i3);
		si3.setCase(c2);
		si4.setInput(i4);
		si4.setCase(c3);
		si5.setInput(i5);
		si5.setCase(c4);
		
		cb = new CaseBase();
		
		cb.add(c1);
		cb.add(c2);
		cb.add(c3);
		cb.add(c4);
		cb.add(c5);
		tb= new TBReasoning(cb);
	}
	@Test
	public void testAsReactive(){
		StateBasedInput ts1 =new StateBasedInput("ts1",null);
		AtomicInput i1 = new AtomicInput("1",f1,sim);
		ts1.setInput(i1);
		//we have an exact similar input in the case base
		//since this stateBasedInput has only one input so the action we are supposed to get
		//is the one of the case with the exact same input.
		assertEquals("down",tb.selectAction(ts1).getName());
	}
	@Test
	public void testStateBased(){
		StateBasedInput ts1 =new StateBasedInput("ts1",null);
		AtomicInput i1 = new AtomicInput("1",f1,sim);
		ts1.setInput(i1);
		Case c= new Case(ts1,a1);
		StateBasedInput ts2 = new StateBasedInput("ts2",null);
		ts2.setInput(i1);
		ts2.setCase(c);
		
		
		//we have many exact similar inputs in the case base
		//since this stateBasedInput has more than one input, it will compare the
		//back track in the traces of the similar inputs to the tested input, and it will check where the similarity is
		//in our case, the trace that has the last action as a1(down) is the one to be chosen.
		//so what will happen is:
		//at the first round of the algorithm it will have three inputs which are similar to our input.
		//it will back track with those, 
		//and then it will check the actions, only one case will be found for the same action as the tested input
		assertEquals("up",tb.selectAction(ts2).getName());
	}
	
}
