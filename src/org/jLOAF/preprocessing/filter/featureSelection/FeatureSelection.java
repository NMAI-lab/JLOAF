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

public abstract class FeatureSelection extends CaseBaseFilter {
	
	protected Statistics st;
	protected CaseBase testCases;
	protected CaseBase trainCases;
	protected ArrayList<FeatureNode> open;
	protected Agent a;
	private CaseBase cb;

	public FeatureSelection(CaseBaseFilter fs){
		super(fs);
		a = new GenericAgent();
		st=new Statistics(a);
		open = new ArrayList<FeatureNode>();
		testCases = new CaseBase();
		trainCases= new CaseBase();
	}
	
	
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
			cb=initial;
			SplitTrainTest(initial);
			Case c = (Case)initial.getCases().toArray()[0];
			Weights sim1 =((Weights)((WeightedMean) (((StateBasedInput)c.getInput()).getInput().getSimilarityMetricStrategy())).getSimilarityWeights());
			Weights sim2 =new SimilarityWeights();
			sim2.copyWeights(sim1);
			
			Weights sim3 =filterFeatures(sim2).getWeights();
			
			sim1.copyWeights(sim3);
			
			
		 return testCases;
	}


	protected abstract FeatureNode filterFeatures(Weights allIn);
		
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
