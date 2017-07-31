package org.jLOAF.preprocessing.filter.casebasefilter;

import org.jLOAF.casebase.CaseBase;
import org.jLOAF.preprocessing.filter.CaseBaseFilter;

public class NoFilter extends CaseBaseFilter {

	public NoFilter(CaseBaseFilter f) {
		super(f);
		
	}

	@Override
	public CaseBase filter(CaseBase initial) {
		if(filter!=null){
			initial=filter.filter(initial);
		}
		return initial;
	}

}
