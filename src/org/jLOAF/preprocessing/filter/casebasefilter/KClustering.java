package org.jLOAF.preprocessing.filter.casebasefilter;

import java.util.List;

import org.jLOAF.Reasoning;
import org.jLOAF.casebase.Case;
import org.jLOAF.casebase.CaseBase;
import org.jLOAF.preprocessing.filter.CaseBaseFilter;
import org.jLOAF.reasoning.SimpleKNN;

public class KClustering extends Clustering{

	public KClustering(CaseBaseFilter f) {
		super(f,5);
		
	}
	
	
	@Override
	public CaseBase filter(CaseBase cb){
		
		setCluster(cb);
		
		
		
		Reasoning knn = new SimpleKNN(k,cb);
		cb.getCases().clear();
		for(Case c:clusters.keySet()){
			List<Case> cases= clusters.get(c).getMembers();
			c.setAction(knn.mostLikelyAction(cases));
			cb.add(c);
		}
		
		
		return cb;
		
		
	}

}
