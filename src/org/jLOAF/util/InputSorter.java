package org.jLOAF.util;



import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.jLOAF.inputs.AtomicInput;
import org.jLOAF.inputs.ComplexInput;
/*
 * this class is used to Sort a list of ComplexInputs  by a given feature
 * @author Ibrahim Ali Fawaz
 * @since May 2017
 */
public class InputSorter  {
	//the name of the input  holding the inputs to be sorted
	private String sortedByInput;
	//the feature by which the selected input will be sorted.
	private String feature;
	/*
	 * Constructor 
	 * @param inputName the name of the input which will be sorted
	 * @param feature the feature by which the selected input will be sorted.
	 */
	public InputSorter(String inputName,String feature){
		sortedByInput=inputName;
		this.feature=feature;
	}

	
	
		/*
		 * Sorts a list of Inputs by their value of the specified feature
		 * @param l a list of complex inputs to be sorted
		 * @param feature the feature by which the list is going to be sorted
		 */
		private void sortByFeature(List<ComplexInput> l,String feature){
			
			 
		//define a comparator for sorting a List of inputs by the feature feature
				Comparator<ComplexInput> comp = new Comparator<ComplexInput>(){

					@Override
					public int compare(ComplexInput o1, ComplexInput o2) {
						double v1=((AtomicInput)o1.get(feature)).getFeature().getValue();
						double v2=((AtomicInput)o1.get(feature)).getFeature().getValue();
						if (v1 == v2){
							 return 0;
						 }else if(v1 > v2){
							 return 1;
						 }else{
							 return -1;
						 }
						
					}
					
				};
		
		Collections.sort(l,comp);
	}
		
	/*
	 * returns a list of names of the sorted list on inputs
	 * @param i the complexInput holding the inputs to be sorted
	 * @return a list of names of the sorted list of inputs
	 */
	public ArrayList<String> getList(ComplexInput i) {
		ArrayList<String> list = new ArrayList<String>();
		if(!i.getName().equals(sortedByInput)){
			list.addAll(i.getChildNames());
			return list;
		}
		ArrayList<ComplexInput> list2 = new ArrayList<ComplexInput>();
		for(String name:i.getChildNames()){
			list2.add((ComplexInput)i.get(name));
		}
		sortByFeature(list2,feature);
		for(ComplexInput i1:list2){
			list.add(i1.getName());
		}
		return list;
		
		
	
	}

}
