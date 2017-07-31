package org.jLOAF.preprocessing.filter;

import java.lang.reflect.InvocationTargetException;

import org.jLOAF.casebase.CaseBase;
/**
 * a caseBase Filter is the parent class of many casebase filter, they all use the decorator pattern , where a casebase filter takes a casebase filter
 * which also takes another casebase filter, and so on.
 * @author Owner
 *
 */
public abstract  class CaseBaseFilter {
	
		
		protected CaseBaseFilter filter;
		/**
		 * Constructor
		 * @param f a filter to filter the caseBase before this filter
		 */
		public CaseBaseFilter(CaseBaseFilter f){
			filter=f;
		}
		
	
	/** 
	 * Given a CaseBase, the method will filter the
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
	/**
	 * sets the filter of this Filter
	 * @param filter the filter to be set
	 */
	public void setFilter(CaseBaseFilter filter){
		this.filter=filter;
	}
	/**
	 * returns a Filter object that corresponds to the parameter 
	 * @param string the name that corresponds to this filter
	 * @return the filter object that has string as its name
	 */
	public static CaseBaseFilter getFilter(String string) {
		return Filters.valueOf(string).getFilter();
	}
	/**
	 * returns a new instance of the subclass calling this method
	 * @return a new instance of the subclass calling this method
	 */
	public CaseBaseFilter getCopy() {
		Class<? extends CaseBaseFilter> c=this.getClass();
		Object [] paramValuesSub ={null};
		try {
			return c.getConstructor(CaseBaseFilter.class).newInstance(paramValuesSub);
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
	}
}
