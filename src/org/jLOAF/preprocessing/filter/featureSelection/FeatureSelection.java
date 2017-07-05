package org.jLOAF.preprocessing.filter.featureSelection;

import java.util.ArrayList;
import java.util.Random;

import org.jLOAF.Agent;
import org.jLOAF.agents.GenericAgent;
import org.jLOAF.casebase.Case;
import org.jLOAF.casebase.CaseBase;
import org.jLOAF.performance.Statistics;
import org.jLOAF.preprocessing.filter.CaseBaseFilter;
import org.jLOAF.weights.Weights;
import org.jLOAF.sim.complex.WeightedMean;

public abstract class FeatureSelection extends CaseBaseFilter {
	
	protected Statistics st;
	protected CaseBase testCases;
	protected CaseBase trainCases;
	protected ArrayList<FeatureNode> open;


	public FeatureSelection(FeatureSelection fs){
		super(fs);
		Agent a = new GenericAgent();
		st=new Statistics(a);
		open = new ArrayList<FeatureNode>();
	}
	
	
	protected void evaluate(FeatureNode allIn) {
		st.getStatisticsHashMap().clear();
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
			SplitTrainTest(initial);
			Case c = (Case)initial.getCases().toArray()[0];
			((Weights)((WeightedMean) c.getInput().getSimilarityMetricStrategy()).getSimilarityWeights()).copyWeights((filterFeatures((Weights)((WeightedMean) c.getInput().getSimilarityMetricStrategy()).getSimilarityWeights())).getWeights());
			
		 return testCases;
	}


	protected abstract FeatureNode filterFeatures(Weights allIn);
		
	protected void SplitTrainTest(CaseBase casebase){ 
		
		Random r = new Random();	
		for(int i=0;i<casebase.getSize()*0.2;){
			Case c =(Case)casebase.getCases().toArray()[r.nextInt(casebase.getSize())];
				if(!testCases.getCases().contains(c)){
					testCases.add(c);
				i++;
				}
			
			
		}
		 for(Case c:casebase.getCases()){
			 if(!testCases.getCases().contains(c)){
				 trainCases.add(c);
			 }
		 }
		
	}

}
