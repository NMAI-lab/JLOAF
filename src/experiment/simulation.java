package experiment;


import java.util.Set;

import org.jLOAF.Agent;
import org.jLOAF.action.Action;
import org.jLOAF.action.AtomicAction;
import org.jLOAF.agents.GenericAgent;
import org.jLOAF.casebase.Case;
import org.jLOAF.casebase.CaseBase;
import org.jLOAF.inputs.AtomicInput;
import org.jLOAF.inputs.ComplexInput;
import org.jLOAF.inputs.Feature;
import org.jLOAF.inputs.Input;
import org.jLOAF.inputs.StateBasedInput;
import org.jLOAF.reasoning.SimpleKNN;
import org.jLOAF.reasoning.TBReasoning;
import org.jLOAF.reasoning.WeightedKNN;
import org.jLOAF.sim.AtomicSimilarityMetricStrategy;
import org.jLOAF.sim.SimilarityMetricStrategy;
import org.jLOAF.sim.StateBasedSimilarity;
import org.jLOAF.sim.StateBased.KOrderedSimilarity;
import org.jLOAF.sim.atomic.Equality;
import org.jLOAF.sim.atomic.EuclideanDistance;
import org.jLOAF.sim.atomic.PercentDifference;

public class simulation {
	/**
	 * This main is used to test the reasoners simpleKNN and weightedKNN
	 * @param args
	 */
	public static void main(String [] args){
		//create a casebase and testbase
		Feature f1 = new Feature(1.0);
		Feature f2 = new Feature(2.0);
		Feature f3 = new Feature(1.0);
		Feature f4 = new Feature(1.0);
		Feature f5 = new Feature(2.0);
		Feature f6 = new Feature(1.0);
		Feature f7 = new Feature(1.0);
		Feature f8 = new Feature(2.0);
		Feature f9 = new Feature(1.0);
		AtomicSimilarityMetricStrategy sim = new Equality();
		
		Input i1 = new AtomicInput("sense", f1,sim);
		Input i2 = new AtomicInput("sense", f2,sim);
		Input i3 = new AtomicInput("sense", f3,sim);
		Input i4 = new AtomicInput("sense", f4,sim);
		Input i5 = new AtomicInput("sense", f5,sim);
		Input i6 = new AtomicInput("sense", f6,sim);
		Input i7 = new AtomicInput("sense", f7,sim);
		Input i8 = new AtomicInput("sense", f8,sim);
		Input i9 = new AtomicInput("sense", f9,sim);
			
		Action a1 = new AtomicAction("left");
		Action a2 = new AtomicAction("forward");
		Action a3 = new AtomicAction("right");
		Action a4 = new AtomicAction("right");
		Action a5 = new AtomicAction("forward");
		Action a6 = new AtomicAction("left");
		Action a7 = new AtomicAction("right");
		Action a8 = new AtomicAction("forward");
		
		StateBasedSimilarity sstsim = new KOrderedSimilarity(1);
		
		StateBasedInput s1 = new StateBasedInput("Case1",sstsim);
		s1.setInput(i1);
		
		Case c1 = new Case(i1, a1);
		Case c2 = new Case(i2, a2);
		Case c3 = new Case(i3, a3);
		Case c4 = new Case(i4, a4);
		Case c5 = new Case(i5, a5);
		
		CaseBase cb = new CaseBase();
		cb.add(c1);
		cb.add(c2);
		cb.add(c3);
		cb.add(c4);
		cb.add(c5);
		
		//CaseBase cb2 = CaseBase.load("data/Left_1.cb");
		//CaseBase tb = CaseBase.load("Right_1.cb");
		
		//set similarity strategy
		
		//testcase
		//Input i6 = new AtomicInput("test", new Feature(1.5),sim);
		//Action a6 = new AtomicAction("down");
		Case c6 = new Case(i6,a6);
		
		//create generic agent
		int k = 3;
		Agent a = new GenericAgent();
		a.setR(new TBReasoning(cb));
		
		Action predicted = a.getR().selectAction(c6.getInput());
		System.out.println("Action Predicted: " + predicted.getName() +", and the correct action is " + a6.getName() + ".");
	}
}
