package org.jLOAF.reasoning;

import java.util.List;

import org.jLOAF.Reasoning;
import org.jLOAF.action.Action;
import org.jLOAF.casebase.Case;
import org.jLOAF.casebase.CaseBase;
import org.jLOAF.inputs.Input;
import org.jLOAF.retrieve.Retrieval;
import org.jLOAF.retrieve.kNN;

public class SimpleKNN implements Reasoning {

	private Retrieval ret;
	
	public SimpleKNN(int k, CaseBase cb){
		ret = new kNN(k, cb);
	}
	
	@Override
	public Action selectAction(Input i) {
		List<Case> nn = ret.retrieve(i);
		//FIXME I should do something in the case k > 1
		return nn.get(0).getAction();
	}

}
