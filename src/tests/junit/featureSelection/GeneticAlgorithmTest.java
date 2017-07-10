package tests.junit.featureSelection;

import static org.junit.Assert.assertTrue;

import org.jLOAF.preprocessing.filter.CaseBaseFilter;
import org.jLOAF.preprocessing.filter.featureSelection.GeneticAlgorithmWeightSelector;
import org.jLOAF.preprocessing.filter.featureSelection.SequentialBackwardGeneration;
import org.junit.Before;
import org.junit.Test;
/*
 * the geneticAlgorithmWeightSelector Class, does several performances on multiple sets of features of different Weights and chooses the set that performs best.
 * all you need to do is to pass it a casesBase and it does the rest
 * keep in mind that this algorithm would only work if the inputs are complex and have weightedMean as a similarity
 * @author Ibrahim Ali Fawaz
 */
public class GeneticAlgorithmTest extends FeatureSelectionTest{
	
	
	@Before
	public void doInitialize(){
		initialize();
	}
	
	
	@Test
	public void testWeights(){
		CaseBaseFilter sq = new GeneticAlgorithmWeightSelector(null);
		sq.filter(cb);
		System.out.println(sim2.getWeight("a"));
		System.out.println(sim2.getWeight("a1"));
		assertTrue(sim2.getWeight("a")>sim2.getWeight("a1"));//the weight of feature a should be greater then the weight of feature a1
	}

}
