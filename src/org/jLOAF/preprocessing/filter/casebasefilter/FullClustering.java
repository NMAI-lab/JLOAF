package org.jLOAF.preprocessing.filter.casebasefilter;

import java.util.List;

import org.jLOAF.casebase.Case;
import org.jLOAF.casebase.CaseBase;
import org.jLOAF.preprocessing.filter.CaseBaseFilter;

public class FullClustering extends Clustering{

	public FullClustering(CaseBaseFilter f) {
		super(f, Integer.MAX_VALUE);
		
	}

	@Override
	public CaseBase filter(CaseBase casebase) {
		setCluster(casebase);
		
		casebase.getCases().clear();
		for(Case c:clusters.keySet()){
		
		
			casebase.add(c);
		}
		
		
		return casebase;
		
	}

}
