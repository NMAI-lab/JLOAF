package org.jLOAF.preprocessing.filter.casebasefilter;

import java.util.ArrayList;

import org.jLOAF.casebase.Case;
import org.jLOAF.casebase.CaseBase;
import org.jLOAF.preprocessing.filter.CaseBaseFilter;
import org.jLOAF.preprocessing.filter.Cluster;
/**
 * this class clusters the CaseBase based on the number of actions the domain used has.
 * if N Actions were only performend in the domain used, the final number of cases in the casebase would be n.
 * and the Case that represents those cases is the closest case to all the cases of each action(Centroid)
 * @author Ibrahim Ali Fawaz
 *
 */
public class ActionClustering extends CaseBaseFilter{
	
	private ArrayList<Cluster> clusters;
	
	/**
	 * Constructor
	 * @param fs the filter which this filter has
	 */
	public ActionClustering(CaseBaseFilter fs){
		super(fs);
		clusters = new ArrayList<Cluster>();
	}
	
	@Override
	public CaseBase filter(CaseBase initial) {
		clusters.clear();
		setClusters(initial);
		calculateCentroids();
		CaseBase cb = new CaseBase();
		for(Cluster c:clusters){
			cb.add(c.getCentroid());
		}
		return cb;
	}
		
	/**
	 * categorizes the cases based on their Actions, where the cases that have the same Actions are in the same Cluster
	 * @param cb the original casebase
	 */
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
	/**
	 * calculates the centroid of each Cluster
	 */
	private void calculateCentroids(){
		
	for(Cluster c:clusters){
		recalculateCentroid(c);
	}
	}
	/**
	 * Calculate the centroid of the cluster passed to it
	 * @param c a cluster where its centroid needs to be calculated
	 */
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
		//
		
	}


	

}
