package org.jLOAF.preprocessing.filter;


import org.jLOAF.preprocessing.filter.casebasefilter.ActionClustering;
import org.jLOAF.preprocessing.filter.casebasefilter.FullClustering;
import org.jLOAF.preprocessing.filter.casebasefilter.KClustering;
import org.jLOAF.preprocessing.filter.casebasefilter.NoFilter;
import org.jLOAF.preprocessing.filter.casebasefilter.Sampling;
import org.jLOAF.preprocessing.filter.casebasefilter.UnderSampling;
import org.jLOAF.preprocessing.filter.featureSelection.GeneticAlgorithmWeightSelector;
import org.jLOAF.preprocessing.filter.featureSelection.HillClimbingFeatureSelection;
import org.jLOAF.preprocessing.filter.featureSelection.SequentialBackwardGeneration;
import org.jLOAF.preprocessing.filter.featureSelection.WeightsSeperatorFilter;
/**
 * this enum is only used for testing purposes, it is used to return an instance of a casebase filter, which corresponds to the string representation
 * of its name.
 * 
 * @author Ibrahim Ali Fawaz
 *
 */
public enum Filters {

	kclustering(new KClustering(null)),fullclustering(new FullClustering(null)),sampling(new Sampling(null)),geneticAlgorithm(new GeneticAlgorithmWeightSelector(null))
	,hillclimbing(new HillClimbingFeatureSelection(null)),sequentialBackwardsAlgorithm(new SequentialBackwardGeneration(null,5,0.9))
	,weightsSeperator(new WeightsSeperatorFilter(null)),underSampling(new UnderSampling(null)),actionClustering(new ActionClustering(null)),none(new NoFilter(null));
	
		CaseBaseFilter fs ;
		
	Filters(CaseBaseFilter fs){
		this.fs=fs;
	}
	
	/**
	 * 
	 * @return the caseBase filter of this enumerated element
	 */
	public CaseBaseFilter getFilter(){
		return fs;
	}
	
}
