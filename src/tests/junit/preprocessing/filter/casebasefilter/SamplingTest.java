package tests.junit.preprocessing.filter.casebasefilter;

import static org.junit.Assert.*;

import org.jLOAF.action.AtomicAction;
import org.jLOAF.casebase.Case;
import org.jLOAF.casebase.CaseBase;
import org.jLOAF.inputs.AtomicInput;
import org.jLOAF.inputs.Feature;
import org.jLOAF.preprocessing.filter.CaseBaseFilter;
import org.jLOAF.preprocessing.filter.casebasefilter.Sampling;
import org.jLOAF.sim.SimilarityMetricStrategy;
import org.jLOAF.sim.atomic.PercentDifference;
import org.junit.Test;

public class SamplingTest {
	/***
	 * This test shows an example of duplicate rows of data in the casebase
	 * The sampling method will remove the duplicates
	 * It will also only add in a case if the agent cannot guess the correct action for that case given its current casebase
	 * This way it oversamples from the under represented cases and undersamples from the overrepresented cases
	 * 
	 * The data set is made up in the following manner:
	 * name|value|action
	 * a1  |1.0  |Up
	 * a2  |1.0  |Up
	 * a3  |0.0  |Down
	 * 
	 * When preprocessing:
	 * ------------------------------- 
	 * Cycle 0
	 * CaseBase -> <a1>
	 * 
	 * -------------------------------
	 * Cycle 1
	 * Given the input a2 predict the action: Up
	 * Prediction is correct so don't add a2 to the casebase
	 * CaseBase -> <a1>
	 * 
	 * -------------------------------
	 * Cycle 2
	 * Given the input a3 predict the action: Up
	 * Prediction is incorrect so add a3 to the casebase
	 * CaseBase -> <a1,a3>
	 * 
	 * ------------------------------
	 * 
	 * This results in a smaller casebase which overcomes skewed datasets.
	 * 
	 * @author sachagunaratne
	 * @since may 18 2017
	 * 
	 * ***/
	@Test
	public void TestSampling(){
		SimilarityMetricStrategy sim = new PercentDifference();
		
		AtomicInput a1 = new AtomicInput("a1",new Feature(1.0),sim);
		AtomicInput a2 = new AtomicInput("a2",new Feature(1.0),sim);
		AtomicInput a3 = new AtomicInput("a3",new Feature(0.0),sim);
		
		AtomicAction ac1 = new AtomicAction("Up");
		AtomicAction ac2 = new AtomicAction("Up");
		AtomicAction ac3 = new AtomicAction("Down");
		
		Case c1 = new Case(a1,ac1);
		Case c2 = new Case(a2,ac2);
		Case c3 = new Case(a3,ac3);
		
		CaseBase cb = new CaseBase();
		
		cb.add(c1);cb.add(c2);cb.add(c3);
		
		CaseBaseFilter filter = new Sampling();
		
		CaseBase preprocessed = filter.filter(cb);
		
		assertEquals(preprocessed.getSize(),2);
	}
	
	/***
	 * There is a fault in this method. Since we increase the k from 1-7 as new cases are added to the casebase, there are situations where there is a tie
	 * (when k is even). 
	 * 
	 * name|value|action
	 * a1  |1.0  |Up
	 * a4  |0.0  |Down
	 * a2  |1.0  |Up
	 * a3  |0.0  |Down
	 *  ------------------------------- 
	 * Cycle 0
	 * CaseBase -> <a1>
	 * 
	 * -------------------------------
	 * Cycle 1
	 * Given the input a4 predict the action: Up
	 * Prediction is incorrect so add a4 to the casebase
	 * CaseBase -> <a1,a4>
	 * 
	 * -------------------------------
	 * Cycle 2
	 * Given the input a2 predict the action: Up
	 * Prediction is correct so don't add a2 to the casebase
	 * CaseBase -> <a1,a4>
	 * 
	 * ------------------------------
	 * Cycle 3
	 * Given the input a3 predict the action: Up
	 * (The prediction here fails because there are only 2 cases in the casebase and they each are a different action. K=2 so there is a tie for which action
	 * to choose. The tie is solved by picking the first case which happens to be the case with Up)
	 * Prediction is incorrect so add a3 to the casebase
	 * CaseBase -> <a1,a4,a3>
	 * 
	 * ------------------------------
	 * 
	 * Since there are two duplicate cases we would expect only 2 cases to remain in the casebase after preprocessing. But due to the error in Cycle 3
	 * a duplicate case is added. To get around this, the casebase could be made sure to always have odd number of cases. So if there is even, then simply add
	 * the next case to it. But then if it is a duplicate case, we end up in the same situation. 
	 * 
	 *  This also does occur because the cases are binary. If they are real values the weighted KNN will get rid of the ties. 
	 * 
	 * ***/
	@Test
	public void TestSamplingFailing(){
		SimilarityMetricStrategy sim = new PercentDifference();
		
		AtomicInput a1 = new AtomicInput("a1",new Feature(1.0),sim);
		AtomicInput a2 = new AtomicInput("a2",new Feature(1.0),sim);
		AtomicInput a3 = new AtomicInput("a3",new Feature(0.0),sim);
		AtomicInput a4 = new AtomicInput("a4",new Feature(0.0),sim);
		
		AtomicAction ac1 = new AtomicAction("Up");
		AtomicAction ac2 = new AtomicAction("Up");
		AtomicAction ac3 = new AtomicAction("Down");
		AtomicAction ac4 = new AtomicAction("Down");
		
		Case c1 = new Case(a1,ac1);
		Case c4 = new Case(a4,ac4);
		Case c2 = new Case(a2,ac2);
		Case c3 = new Case(a3,ac3);
		
		CaseBase cb = new CaseBase();
		
		cb.add(c1);cb.add(c4);cb.add(c2);cb.add(c3);
		
		CaseBaseFilter filter = new Sampling();
		
		CaseBase preprocessed = filter.filter(cb);
		assertEquals(preprocessed.getSize(),3);
	}
	/***
	 * This tests with real values where the Up actions are centered around a mean of 1.3+0.8/2=1.1 and the Down actions are centred around 0. 
	 * Since the down actions are close to there mean than the up actions when we calculate the similarity between down actions they are going to be closer
	 * and have a higher weighting. 
	 * This allows for the tie to be resolved the duplicates to be removed.  
	 * ***/
	@Test
	public void TestSamplingWithRealInputs(){
		SimilarityMetricStrategy sim = new PercentDifference();
		
		AtomicInput a1 = new AtomicInput("a1",new Feature(1.3),sim);
		AtomicInput a2 = new AtomicInput("a2",new Feature(0.8),sim);
		AtomicInput a3 = new AtomicInput("a3",new Feature(-0.1),sim);
		AtomicInput a4 = new AtomicInput("a4",new Feature(0.1),sim);
		
		AtomicAction ac1 = new AtomicAction("Up");
		AtomicAction ac2 = new AtomicAction("Up");
		AtomicAction ac3 = new AtomicAction("Down");
		AtomicAction ac4 = new AtomicAction("Down");
		
		Case c1 = new Case(a1,ac1);
		Case c4 = new Case(a4,ac4);
		Case c2 = new Case(a2,ac2);
		Case c3 = new Case(a3,ac3);
		
		CaseBase cb = new CaseBase();
		
		cb.add(c1);cb.add(c4);cb.add(c2);cb.add(c3);
		
		CaseBaseFilter filter = new Sampling();
		
		CaseBase preprocessed = filter.filter(cb);
		assertEquals(preprocessed.getSize(),2);
	}
}
