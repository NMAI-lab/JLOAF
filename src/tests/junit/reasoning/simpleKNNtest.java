package tests.junit.reasoning;

import static org.junit.Assert.assertEquals;

import org.jLOAF.Reasoning;
import org.jLOAF.action.Action;
import org.jLOAF.action.AtomicAction;
import org.jLOAF.casebase.Case;
import org.jLOAF.casebase.CaseBase;
import org.jLOAF.inputs.AtomicInput;
import org.jLOAF.inputs.Feature;
import org.jLOAF.inputs.Input;
import org.jLOAF.reasoning.SimpleKNN;
import org.jLOAF.sim.AtomicSimilarityMetricStrategy;
import org.jLOAF.sim.SimilarityMetricStrategy;
import org.jLOAF.sim.StateBasedSimilarity;
import org.jLOAF.sim.StateBased.KOrderedSimilarity;
import org.jLOAF.sim.atomic.Equality;
import org.jLOAF.sim.atomic.PercentDifference;
import org.junit.Before;
import org.junit.Test;

public class simpleKNNtest {
	
	CaseBase cb;
	AtomicSimilarityMetricStrategy sim = new Equality();
	StateBasedSimilarity ssim = new KOrderedSimilarity(1);
	@Before
	public void setup(){
		
		
		Feature f1 = new Feature(1.0);
		Feature f2 = new Feature(2.0);
		Feature f3 = new Feature(3.0);
		Feature f4 = new Feature(3.0);
		Feature f5 = new Feature(8.0);
		
		Input i1 = new AtomicInput("1", f1, sim);
		
		Input i2 = new AtomicInput("2", f2, sim);
	
		Input i3 = new AtomicInput("3", f3, sim);
		
		Input i4 = new AtomicInput("4", f4, sim);
		
		Input i5 = new AtomicInput("5", f5,sim);
	
			
		Action a1 = new AtomicAction("down");
		Action a2 = new AtomicAction("down");
		Action a3 = new AtomicAction("up");
		Action a4 = new AtomicAction("up");
		Action a5 = new AtomicAction("up");
	
		
		Case c1 = new Case(i1, a1);
		Case c2 = new Case(i2, a2);
		Case c3 = new Case(i3, a3);
		Case c4 = new Case(i4, a4);
		Case c5 = new Case(i5, a5);
		
		cb = new CaseBase();
		
		cb.createThenAdd(i1,a1,ssim);
		cb.createThenAdd(i2,a2,ssim);
		cb.createThenAdd(i3,a3,ssim);
		cb.createThenAdd(i4,a4,ssim);
		cb.createThenAdd(i5,a5,ssim);
	}
	
	 @Test
	    public void testSimpleKNNk_5() {
		 	int k = 5;
	        Reasoning r = new SimpleKNN(k,cb);
	       
	        Input i6 = new AtomicInput("test", new Feature(1.5), sim);
			Action a6 = new AtomicAction("down");
			CaseBase cb2 = new CaseBase();
			cb2.createThenAdd(i6, a6, ssim);
	        Case c = (Case) cb2.getCases().toArray()[0];
	        
	        Action predicted = r.selectAction(c.getInput());
	        assertEquals(predicted.getName(), "up");
	    }
	 
	 @Test
	    public void testSimpleKNNk_3() {
		 	int k = 3;
	        Reasoning r = new SimpleKNN(k,cb);
	        
	        Input i6 = new AtomicInput("test", new Feature(1.5),sim);
			Action a6 = new AtomicAction("down");
			CaseBase cb2 = new CaseBase();
			cb2.createThenAdd(i6, a6, ssim);
	        Case c = (Case) cb2.getCases().toArray()[0];
	        
	        Action predicted = r.selectAction(c.getInput());
	        assertEquals(predicted.getName(), a6.getName());
	    }
}
