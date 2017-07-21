package org.jLOAF.preprocessing.filter.featureSelection;

import java.util.ArrayList;
import java.util.Random;

import org.jLOAF.Agent;
import org.jLOAF.agents.GenericAgent;
import org.jLOAF.casebase.Case;
import org.jLOAF.casebase.CaseBase;
import org.jLOAF.inputs.StateBasedInput;
import org.jLOAF.performance.Statistics;
import org.jLOAF.preprocessing.filter.CaseBaseFilter;
import org.jLOAF.weights.SimilarityWeights;
import org.jLOAF.weights.Weights;
import org.jLOAF.sim.complex.WeightedMean;
/**
 * this class extends the caseBaseFilter class and follows the decorator design pattern as well,
 * but it also the parent of all the classes that has to do with the weights of the features,
 * usually this filters are used when the inputs used in the cases have a weightedMean similarity.
 * @author Ibrahim Ali Fawaz
 *
 */
public abstract class FeatureSelectionAlgorithm extends CaseBaseFilter {
	
	protected Statistics st;
	protected CaseBase testCases;
	protected CaseBase trainCases;
	protected ArrayList<FeatureNode> open;
	protected Agent a;
	
	/**
	 * 
	 * @param fs the caseBaseFilter to be passed to this filter
	 */
	public FeatureSelectionAlgorithm(CaseBaseFilter fs){
		super(fs);
		a = new GenericAgent();
		st=new Statistics(a);
		open = new ArrayList<FeatureNode>();
		testCases = new CaseBase();
		trainCases= new CaseBase();
	}
	
	/**
	 * this method evaluate the given node, and then sets all its statistics information.
	 * @param allIn the FeatureNode to be evaluated
	 */
	protected void evaluate(FeatureNode allIn) {
		st.getStatisticsHashMap().clear();
		Case c =(Case) testCases.getCases().toArray()[0];
				Weights w1 =allIn.getWeights();
				Weights w2=((WeightedMean)((StateBasedInput)c.getInput()).getInput().getSimilarityMetricStrategy()).getSimilarityWeights();
				if(!w1.equals(w2)){
				w2.copyWeights(w1);
				}
		for(Case test:testCases.getCases()){
			st.predictedCorrectActionName(test);
		}
	allIn.setM_statsBest(st.getStatisticsBundle());
	allIn.setEvaluateNumber(st.getStatisticsBundle().getPrimaryStatistic());
	st= new Statistics(st.getAgent());
	}
	/**
	 * places a given node in its right place in the open list,
	 * usually the biggest node is placed at the front
	 * @param child the FeatureNode to be placed in the open list
	 */
	protected void placeInList(FeatureNode child) {
		boolean placed = false;
	
	for(int ii=0;ii<open.size();ii++){
		//if it has a higher evaluation that the current item, add it here
		if(child.bigger(open.get(ii))){
			open.add(ii,child);
			placed = true;
			break;
		}
	}
	
	//if it was not placed somewhere in the list, add to the end
	if(!placed){
		open.add(child);
	
	}
	
}


	@Override
	public CaseBase filter(CaseBase initial) {
			if(filter!=null){
				initial =filter.filter(initial);
			}
			
			SplitTrainTest(initial);
			Case c = (Case)initial.getCases().toArray()[0];
			Weights sim1 =((Weights)((WeightedMean) (((StateBasedInput)c.getInput()).getInput().getSimilarityMetricStrategy())).getSimilarityWeights());
			Weights sim2 =new SimilarityWeights();
			sim2.copyWeights(sim1);
			
			Weights sim3 =filterFeatures(sim2).getWeights();
			
			sim1.copyWeights(sim3);
			
			
		 return testCases;
	}

	/**
	 * returns a single feature node that has the last state of the weights of the feature,
	 * this method is overriden by all the subclasses of this class. and it is called in the filter method of this class
	 * @param allIn the initial state of the weights of the features
	 * @return single feature node that has the last state of the weights of the feature,
	 */
	protected abstract FeatureNode filterFeatures(Weights allIn);
		/**
		 * chooses randomly 20 percent of the casebase cases to pass them to the test casebases, and the whole casebase cases are trained by the agent.
		 * @param casebase the casebase to be trained and split
		 */
	protected void SplitTrainTest(CaseBase casebase){ 
		
		Random r = new Random();	
		testCases.getCases().clear();
		for(int i=0;i<casebase.getSize()*0.3;){
			Case c =(Case)casebase.getCases().toArray()[r.nextInt(casebase.getSize())];
				if(!testCases.getCases().contains(c)){
					testCases.add(c);
				i++;
				}
			
			
		}
		 for(Case c:casebase.getCases()){
			 
				 trainCases.add(c);
			 
		 }
		a.train(trainCases);
	}

}
