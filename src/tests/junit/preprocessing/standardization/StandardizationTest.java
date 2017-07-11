package tests.junit.preprocessing.standardization;

import static org.junit.Assert.*;

import java.util.HashMap;

import org.jLOAF.action.AtomicAction;
import org.jLOAF.casebase.Case;
import org.jLOAF.casebase.CaseBase;
import org.jLOAF.inputs.AtomicInput;
import org.jLOAF.inputs.ComplexInput;
import org.jLOAF.inputs.Feature;
import org.jLOAF.inputs.Input;
import org.jLOAF.inputs.StateBasedInput;
import org.jLOAF.preprocessing.filter.CaseBaseFilter;
import org.jLOAF.preprocessing.standardization.Standardization;
import org.jLOAF.sim.SimilarityMetricStrategy;
import org.jLOAF.sim.StateBasedSimilarity;
import org.jLOAF.sim.StateBased.KOrderedSimilarity;
import org.jLOAF.sim.atomic.EuclideanDistance;
import org.jLOAF.sim.complex.Mean;
import org.jLOAF.sim.complex.WeightedMean;
import org.jLOAF.weights.SimilarityWeights;
import org.junit.Test;

public class StandardizationTest {
	
	SimilarityMetricStrategy a_sim = new EuclideanDistance();
	SimilarityMetricStrategy ballGoal_strat = new Mean();
	StateBasedSimilarity stateBasedSim = new KOrderedSimilarity(1);
	SimilarityWeights sim_weights = new SimilarityWeights(); 
	SimilarityMetricStrategy RoboCup_strat = new WeightedMean(sim_weights);
	
	/***
	 * This method creates a casebase based on the following structure:
	 * ComplexInput - RobocupInput
	 * 					  /   \
	 * ComplexInput - Ball     Goal
	 * 				   /\       / \
	 * AtomicInput- dir dist  dir dist
	 * 
	 * Two inputs of this structure are created and are added to a casebase. The casebase is then standardized. The Inputs are unpacked into a 
	 * a HashMap<String, List<Double>> where each string name is the Unique AtomicInput name and the List of double values  contain the feature values. 
	 * The list of doubles is then standardized by calculating mean and standard deviation and applying:
	 * new_val = (old_val-mean)/std
	 * 
	 * The new values are then placed back into the casebase. 
	 *
	 * ***/
	@Test
	public void TestStandardization(){
		AtomicInput a1 = new AtomicInput("goal_dist", new Feature(10.0),a_sim);
		AtomicInput a2 = new AtomicInput("goal_dir", new Feature(-5.0),a_sim);
		AtomicInput a3 = new AtomicInput("ball_dist", new Feature(76.0),a_sim);
		AtomicInput a4 = new AtomicInput("ball_dir", new Feature(-25.0),a_sim);
		
		ComplexInput c1 = new ComplexInput("Ball", ballGoal_strat);
		ComplexInput c2 = new ComplexInput("Goal", ballGoal_strat);
		
		c1.add(a3);
		c1.add(a4);
		
		c2.add(a1);
		c2.add(a2);
		
		ComplexInput top1 = new ComplexInput("RobocupInput", ballGoal_strat);
		
		top1.add(c1);
		top1.add(c2);
		
		AtomicInput a5 = new AtomicInput("goal_dist", new Feature(20.0),a_sim);
		AtomicInput a6 = new AtomicInput("goal_dir", new Feature(-10.0),a_sim);
		AtomicInput a7 = new AtomicInput("ball_dist", new Feature(80.0),a_sim);
		AtomicInput a8 = new AtomicInput("ball_dir", new Feature(-20.0),a_sim);
		
		ComplexInput c3 = new ComplexInput("Ball", ballGoal_strat);
		ComplexInput c4 = new ComplexInput("Goal", ballGoal_strat);
		
		c3.add(a7);
		c3.add(a8);
		
		c4.add(a5);
		c4.add(a6);
		
		ComplexInput top2 = new ComplexInput("RobocupInput", ballGoal_strat);
		
		top2.add(c3);
		top2.add(c4);
		
		CaseBase cb = new CaseBase();
		cb.createThenAdd(top1, new AtomicAction("Kick"), stateBasedSim);
		cb.createThenAdd(top2, new AtomicAction("Turn"), stateBasedSim);
		
		CaseBaseFilter standardize = new Standardization(null);
		cb = standardize.filter(cb);
		
		HashMap<String, Double> input = new HashMap<String, Double>();
		Case c = (Case)cb.getCases().toArray()[0];
		Input i = ((StateBasedInput)c.getInput()).getInput();
		input = CaseBase.convert(i);
		
		assertEquals(input.get("goal_dist"),-0.7071,0.0001);
	}
	
	@Test
	/***
	 * This test checks the functionality if each Case only contains one AtomicInput 
	 * The AtomicInputs feature is changed using th setFeature method. 
	 * ***/
	public void AtomicInputTest(){
		AtomicInput a1 = new AtomicInput("goal_dist", new Feature(20.0),a_sim);
		AtomicInput a2 = new AtomicInput("goal_dist", new Feature(10.0),a_sim);
		CaseBase cb = new CaseBase();
		
		cb.createThenAdd(a1,new AtomicAction("Kick"), stateBasedSim);
		cb.createThenAdd(a2,new AtomicAction("Kick"), stateBasedSim);
		
		CaseBaseFilter standardize = new Standardization(null);
		cb = standardize.filter(cb);
		
		HashMap<String, Double> input = new HashMap<String, Double>();
		Case c = (Case)cb.getCases().toArray()[0];
		Input i = ((StateBasedInput)c.getInput()).getInput();
		input = CaseBase.convert(i);
		
		assertEquals(input.get("goal_dist"),0.7071,0.0001);
	}
}
