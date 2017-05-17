package tests.junit.reasoning;

import static org.junit.Assert.assertEquals;

import org.jLOAF.Reasoning;
import org.jLOAF.action.Action;
import org.jLOAF.casebase.Case;
import org.jLOAF.casebase.CaseBase;
import org.jLOAF.inputs.AtomicInput;
import org.jLOAF.inputs.Feature;
import org.jLOAF.inputs.Input;
import org.jLOAF.reasoning.SimpleKNN;
import org.jLOAF.sim.SimilarityMetricStrategy;
import org.jLOAF.sim.atomic.Equality;
import org.jLOAF.sim.atomic.PercentDifference;
import org.junit.Before;
import org.junit.Test;

public class simpleKNNtest {
	
	CaseBase cb;
	SimilarityMetricStrategy sim = new Equality();
	
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
	
			
		Action a1 = new Action("down");
		Action a2 = new Action("down");
		Action a3 = new Action("up");
		Action a4 = new Action("up");
		Action a5 = new Action("up");
	
		
		Case c1 = new Case(i1, a1);
		Case c2 = new Case(i2, a2);
		Case c3 = new Case(i3, a3);
		Case c4 = new Case(i4, a4);
		Case c5 = new Case(i5, a5);
		
		cb = new CaseBase();
		
		cb.add(c1);
		cb.add(c2);
		cb.add(c3);
		cb.add(c4);
		cb.add(c5);
	}
	
	 @Test
	    public void testSimpleKNNk_5() {
		 	int k = 5;
	        Reasoning r = new SimpleKNN(k,cb);
	       
	        Input i6 = new AtomicInput("test", new Feature(1.5), sim);
			Action a6 = new Action("down");
			Case c6 = new Case(i6,a6);
	        
	        Action predicted = r.selectAction(c6.getInput());
	        assertEquals(predicted.getName(), "up");
	    }
	 
	 @Test
	    public void testSimpleKNNk_3() {
		 	int k = 3;
	        Reasoning r = new SimpleKNN(k,cb);
	        
	        Input i6 = new AtomicInput("test", new Feature(1.5),sim);
			Action a6 = new Action("down");
			Case c6 = new Case(i6,a6);
	        
	        Action predicted = r.selectAction(c6.getInput());
	        assertEquals(predicted.getName(), a6.getName());
	    }
}
