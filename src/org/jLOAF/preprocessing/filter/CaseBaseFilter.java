package org.jLOAF.preprocessing.filter;

import org.jLOAF.casebase.CaseBase;

public abstract class CaseBaseFilter {
	
		
		protected CaseBaseFilter filter;
		public CaseBaseFilter(CaseBaseFilter f){
			filter=f;
		}
	
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
	
	
	public abstract CaseBase filter(CaseBase initial);
	public void setFilter(CaseBaseFilter filter){
		this.filter=filter;
	}

	public static CaseBaseFilter getFilter(String string) {
		return Filters.valueOf(string).getFilter();
	}
}
