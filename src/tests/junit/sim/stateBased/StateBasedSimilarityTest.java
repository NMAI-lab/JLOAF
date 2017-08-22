package tests.junit.sim.stateBased;

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
/*
 * this class is not actually a test, it is only used because of the initialization that it does, 
 * it is extended by the four test classes of the stateBasedInput similarities
 * @author Ibrahim Ali Fawaz
 */
public abstract class StateBasedSimilarityTest {

	protected CaseBase cb;
	protected StateBasedSimilarity sim1 ;
	protected Feature f1 ;
	protected Feature f3 ;Feature f2; 
	protected AtomicSimilarityMetricStrategy sim;
	
	
	public void initialize(StateBasedSimilarity simst){
		cb = new CaseBase();
		 sim = new Equality();
		sim1 = simst;
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
			//we will go through the casebase step by step to explain what happens
			cb.createThenAdd(i1, a1, sim1);// in this step, we only have one case in the casebase,and that case has an action a1, and a stateBasedInput that only has i1 in the trace
			cb.createThenAdd(i2, a2, sim1);// the input of this case will include the last case added, so it will have a trace of:i1,a1,i2 and the action of the case is a2
			cb.createThenAdd(i3, a3, sim1);//the case's input's trace is :i1-a1-i2-a2-i3 and the action is a4 
			cb.createThenAdd(i4, a2, sim1);//the case's input's trace is :i1-a1-i2-a2-i3-a4-i4 and the action is a2 
			cb.createThenAdd(i5, a3, sim1);//the case's input's trace is :i1-a1-i2-a2-i3-i4-a2-i5 and the action is a3 
			cb.createThenAdd(i6, a1, sim1);//the case's input's trace is :i1-a1-i2-a2-i3-i4-a2-i5-a3-i6 and the action is a1 
			cb.createThenAdd(i7, a1, sim1);//the case's input's trace is :i1-a1-i2-a2-i3-i4-a2-i5-a3-i6-a1-i7 and the action is a1 
			cb.createThenAdd(i8, a2, sim1);//the case's input's trace is :i1-a1-i2-a2-i3-i4-a2-i5-a3-i6-a1-i7-a1-i8 and the action is a2 
			cb.createThenAdd(i9, a4, sim1);//the case's input's trace is :i1-a1-i2-a2-i3-i4-a2-i5-a3-i6-a1-i7-a1-i8-a2-i9 and the action is a4 
		
	}
	

	public Case getMostSimilar(StateBasedInput si){
		double max=0;
		Case c0 =null;
		for(Case c1:cb.getCases()){
			double sim =si.similarity(c1.getInput());
			if(max<sim){
				max=sim;
				c0=c1;
			}
		}
		
		return c0;
		
	}
	
	
	


}
