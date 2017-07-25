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
public abstract class Clustering extends CaseBaseFilter {
	
	protected HashMap<Case,Cluster> clusters;
	private double td =0.95;
	protected int k;
	
	/**
	 * Constructor
	 * @see org.JLOAF.preprocessing.filter.CaseBaseFilter
	 */
	public Clustering(CaseBaseFilter f, int k){
		super(f);
		this.k=k;
		clusters = new HashMap<Case,Cluster>();
	}
	

	
	public abstract CaseBase filter(CaseBase casebase) ;
	
	public void setCluster(CaseBase casebase){
		if(filter!=null){
			casebase=filter.filter(casebase);
			}
			
			clusters.clear();
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
						
						if(sim>=td && clusters.get(c1).getSize()<k){
							
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
			
			
	}
}
