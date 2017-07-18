package org.jLOAF.preprocessing.filter;

import java.util.ArrayList;
import java.util.List;

import org.jLOAF.casebase.Case;
/**
 * This class is used to store a grouping of Cases, called a Cluster.
 * 
 * @author Ibrahim Ali Fawaz
 * @since May 2017
 */
public class Cluster {

	//used to store the members of the cluster
		private List<Case> m_members;
		
		/**
		 * * Creates an empty Cluster that contains no Cases
		 */
		public Cluster(){
			this.m_members = new ArrayList<Case>();
		}
		
		/**
		 * Retrieves all of the Cases that are in the Cluster.
		 * 
		 * @return the Cases that are part of the Cluster
		 */
		public List<Case> getMembers() {
			return this.m_members;
		}

		/**
		 * Adds a Case to the Cluster
		 * 
		 * @param c The Case to add to the Cluster
		 */
		public void addMember(Case c) {
			if(c == null){
				throw new IllegalArgumentException("Null Case given to Cluster.addMember(Case c)");
			}
			this.m_members.add(c);
		}
		
		public Case getPrototype(){
			return m_members.get(0);
		}

		
}
