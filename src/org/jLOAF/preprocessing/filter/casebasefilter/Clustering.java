package org.jLOAF.preprocessing.filter.casebasefilter;


import java.util.HashMap;

import org.jLOAF.casebase.Case;
import org.jLOAF.casebase.CaseBase;
import org.jLOAF.preprocessing.filter.CaseBaseFilter;
import org.jLOAF.preprocessing.filter.Cluster;

/**
 * this class is used to cluster casebases, it puts similar cases in one cluster,the picks one of them to add it to the new casebase
 * removing almost duplicated cases
 * @author Ibrahim Ali Fawaz
 * @since May 2017
 */
public class Clustering extends CaseBaseFilter {
	
	private HashMap<Case,Cluster> clusters;
	private double td =0.95;
	
	/**
	 * Constructor
	 * @see org.JLOAF.preprocessing.filter.CaseBaseFilter
	 */
	public Clustering(CaseBaseFilter f){
		super(f);
		clusters = new HashMap<Case,Cluster>();
	}
	

	@Override
	public CaseBase filter(CaseBase casebase) {
		if(filter!=null){
		casebase=filter.filter(casebase);
		}
		boolean firstTime=true;
		for(Case c :casebase.getCases()){
			if(clusters.size()==0){
				Cluster cluster= new Cluster();
				cluster.addMember(c);
				clusters.put(c,cluster);
				
				
			}else {
				
				
				boolean hasBeenPut = false;
				
				for(Case c1:clusters.keySet()){
					if(firstTime){
						
						firstTime=false;
					}
					double sim =c.getInput().similarity(c1.getInput());
					
					if(sim>=td){
						
						clusters.get(c1).addMember(c);
						hasBeenPut=true;
						break;
						
					}
					
				}
				if(!hasBeenPut){
					Cluster cluster = new Cluster();
					cluster.addMember(c);
					clusters.put(c,cluster);
					
				}
					
			}
		
		
		
		
		
		}
		
		casebase.getCases().clear();
		for(Case c:clusters.keySet()){
			casebase.add(c);
		}
		
		clusters.clear();
		return casebase;
		
	}
	

}
