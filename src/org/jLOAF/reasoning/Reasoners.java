package org.jLOAF.reasoning;

import java.util.List;

import org.jLOAF.Reasoning;
import org.jLOAF.casebase.CaseBase;
/**
 * Enum class containing all the reasoners.
 * @author sachagunaratne
 *
 */
public enum Reasoners {
		 cb(new CaseBase()),weightedKNN(new WeightedKNN(5,cb.returnCaseBase())),KNN(new SimpleKNN(5,cb.returnCaseBase())),
		 TB(new TBReasoning(cb.returnCaseBase())),bayesian(new BayesianReasoner(cb.returnCaseBase(),"bayesian_reactive.txt"));
	
		CaseBase cbr;
		Reasoning r;
		/**
		 * Sets r to a reasoner
		 * @param r
		 */
		private Reasoners(Reasoning r){
			this.r =r;
		}
		/**
		 * Sets cbr to a CaseBase
		 * @param cb
		 */
		private Reasoners(CaseBase cb){
			cbr=cb;
		}
		/**
		 * Returns a reasoner
		 * @return r
		 */
		public Reasoning getR(){
			return  r;
		}
		/**
		 * Returns a CaseBase
		 * @return CaseBase
		 */
		public CaseBase returnCaseBase(){
			return cbr;
		}
		/**
		 * Adds a CaseBase to CaseBase cbr
		 * @param cb1
		 */
		public void setCaseBase(CaseBase cb1){
			cbr.addCaseBase(cb1);
		}
}
