package org.jLOAF.preprocessing.filter;

import org.jLOAF.casebase.CaseBase;

public interface CaseBaseFilter {
	/** Given a CaseBase, the method will filter the
	 * CaseBase based on specific criteria. It will
	 * then return the filtered CaseBase.
	 * 
	 * @param initialCB The CaseBase to filter
	 * @return The filtered CaseBase
	 *
	 * @author Michael W. Floyd
	 * @since 0.3
	 */
	public CaseBase filter(CaseBase initial);
}
