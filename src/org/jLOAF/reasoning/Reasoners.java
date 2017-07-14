package org.jLOAF.reasoning;

import java.util.List;

import org.jLOAF.Reasoning;
import org.jLOAF.casebase.CaseBase;

public enum Reasoners {
		 cb(new CaseBase()),weightedKNN(new WeightedKNN(5,cb.returnCaseBase())),KNN(new SimpleKNN(5,cb.returnCaseBase())),
		 TB(new TBReasoning(cb.returnCaseBase()));
	
		CaseBase cbr;
		Reasoning r;
		Reasoners(Reasoning r){
			this.r =r;
		}
		Reasoners(CaseBase cb){
			cbr=cb;
		}
		public Reasoning getR(){
			return  r;
		}
		public CaseBase returnCaseBase(){
			return cbr;
		}
		public void setCaseBase(CaseBase cb1){
			cbr.addListOfCaseBases((List)cb1.getCases());
		}
}
