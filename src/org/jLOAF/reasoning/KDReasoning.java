package org.jLOAF.reasoning;

import java.util.List;

import org.jLOAF.Reasoning;
import org.jLOAF.action.Action;
import org.jLOAF.casebase.Case;
import org.jLOAF.casebase.CaseBase;
import org.jLOAF.inputs.Input;
import org.jLOAF.preprocessing.filter.casebasefilter.KDTree;
import org.jLOAF.retrieve.KDRetrieval;
import org.jLOAF.retrieve.Retrieval;

public class KDReasoning extends Reasoning{
	private CaseBase cb ;
	private Case testCase;
	private  KDTree tree;
	private boolean first =true;
	public KDReasoning(CaseBase cb,KDTree tree) {
		super(new KDRetrieval(tree));
		this.cb=cb;
		this.tree=tree;
		testCase=null;
		if(cb.getCases().size()>0){
			testCase =(Case)cb.getCases().toArray()[0];
		}
		
		tree.createTree(cb);
		
	}
	
	public Action selectAction(Input i) {
		if(hasChanged(cb)){
			tree.createTree(cb);
		}
		List<Case> nn = ret.retrieve(i);
		
		return nn.get(0).getAction();
	}

	private boolean hasChanged(CaseBase cb2) {
		Case c=(Case)cb2.getCases().toArray()[0];
		if(!c.equals(testCase)){
			testCase=c;
			return true;
		}
		return false;
	}

	@Override
	public Action mostLikelyAction(List<Case> nn) {
		// TODO Auto-generated method stub
		return null;
	}

}
