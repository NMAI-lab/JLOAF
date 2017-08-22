package tests.junit.preprocessing.filter.casebasefilter;

import java.io.IOException;

import org.jLOAF.Agent;
import org.jLOAF.agents.GenericAgent;
import org.jLOAF.casebase.Case;
import org.jLOAF.casebase.CaseBase;
import org.jLOAF.performance.Statistics;
import org.jLOAF.preprocessing.filter.featureSelection.SequentialBackwardGeneration;
import org.jLOAF.reasoning.WeightedKNN;
import org.junit.Before;
import org.junit.Test;


/*
 * in this class we are going to test the sequentialBackwardGeneration on krislet, it is supposed to remove to flag feature
 * from the set of features that we have
 */
public class SequentialBackwardGenerationTest {
	
	
	@Before
	public void setUp(){
		//here we have to assume that during the parser the similarity chosen was the weightedMean one.
		CaseBase cb= CaseBase.load("cb1.cb");
		Agent rc= new GenericAgent();
		rc.train(new WeightedKNN(1,cb));
		Statistics st = new Statistics(rc);
			CaseBase testCases = new CaseBase();
			int i=0;
			for(Case c:cb.getCases()){
				testCases.add(c);
				if(i>cb.getCases().size()/10){
					break;
				}
				i++;
			}
			
		SequentialBackwardGeneration sbg = new SequentialBackwardGeneration(null,4,0.9);
		System.out.println("Sd");
		
	}
	@Test
	public void testSomething(){
	System.out.println("sd");	
	}

}
