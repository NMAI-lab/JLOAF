package org.jLOAF.preprocessing.filter.casebasefilter;

import java.util.ArrayList;

import org.jLOAF.casebase.Case;
import org.jLOAF.casebase.CaseBase;
import org.jLOAF.preprocessing.filter.CaseBaseFilter;
import org.jLOAF.preprocessing.filter.Cluster;

public class ActionClustering extends CaseBaseFilter{
	
	private ArrayList<Cluster> clusters;
	
	
	public ActionClustering(CaseBaseFilter fs){
		super(fs);
		clusters = new ArrayList<Cluster>();
	}
	
	@Override
	public CaseBase filter(CaseBase initial) {
		setClusters(initial);
		calculateCentroids();
		CaseBase cb = new CaseBase();
		for(Cluster c:clusters){
			cb.add(c.getCentroid());
		}
		return cb;
	}
		
	
	public void setClusters(CaseBase cb){
		
		for(Case c:cb.getCases()){
			
				boolean hasBeenPut=false;
				for(Cluster cluster:clusters){
				if(c.getAction().getName().equals(cluster.getCentroid().getAction().getName())){
					cluster.addMember(c);
					hasBeenPut =true;
				}
				
				}
				if(!hasBeenPut){
					Cluster cl = new Cluster();
					cl.setCentroid(c);
					cl.addMember(c);
					clusters.add(cl);
				}
				
				
				
				
				
			}
			
			
		
		
	}
	
	private void calculateCentroids(){
		
	for(Cluster c:clusters){
		recalculateCentroid(c);
	}
	}
	
	private void recalculateCentroid(Cluster c){
		Case winner =c.getCentroid();	
		int winning=0;
		int maxWinning=0;
		for(Case c1:c.getMembers()){
			
			for(Case c2:c.getMembers()){
				if(!c2.equals(c1) && c1.getInput().similarity(c2.getInput())>c.getCentroid().getInput().similarity(c2.getInput())){
					winning++;
				
				}
			}
			if(winning>c.getSize()/2 && winning>maxWinning){
				maxWinning = winning;
				winner =c1;
			}
			
		}
		c.setCentroid(winner);
		
		
	}

































	

}
