package org.jLOAF.preprocessing.filter;

import org.jLOAF.preprocessing.filter.casebasefilter.Clustering;
import org.jLOAF.preprocessing.filter.casebasefilter.Sampling;

public enum Filters {
	CLUSTERING(new Clustering()),SAMPLING(new Sampling());
	private CaseBaseFilter cbf;
	Filters(CaseBaseFilter cbf){
		this.cbf=cbf;
		
	}
	public CaseBaseFilter getFilter(){
		return cbf;
	}

}
