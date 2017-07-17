package org.jLOAF.performance;

import java.io.IOException;
import java.util.ArrayList;

import org.jLOAF.Agent;
import org.jLOAF.Reasoning;
import org.jLOAF.casebase.CaseBase;
import org.jLOAF.performance.PerformanceEvaluator;
import org.jLOAF.preprocessing.filter.CaseBaseFilter;

public class TestingConfig {
	
	
	public String[] getFileNames(String[] args){
		//create an array that has all the strings between the two words files.
		System.out.println("got the files");
		return returnStrings("files",args);
	}
	public String getOutputFileName(String[] args){
		//get the string that is between the two words output
		
		return returnStrings("output",args)[0];
	}
	
	public CaseBaseFilter createCaseBaseFilter(String[] args){
		//create an array that has all the strings between the two words filters
		String[] filters = returnStrings("filter",args);
		
		
		
		return returnFilters(0,filters);
	}
	private CaseBaseFilter returnFilters(int i, String[] filters) {
		CaseBaseFilter f =null;
		if(i==filters.length-1){
			return CaseBaseFilter.getFilter(filters[i]);
		}
			f=CaseBaseFilter.getFilter(filters[i]);
			f.setFilter(returnFilters(i+1,filters));
			return f;
	}
	public void changeSimilarity(String[] args){
		
		
		
	}
	public String getReasoner(String[] args){
		return returnStrings("reas",args)[0];
		
	}
	public String[] returnStrings(String a, String[] args){
		ArrayList<String> arguments= new ArrayList<String>();
		for(int i=0;i<args.length;i++){
			if(args[i].equals(a)){
				for(int j=i+1;j<args.length;j++){
						if(!args[j].equals(a)){
							arguments.add(args[j]);
						}else{
							break;
							
						}
					
				}
				break;
			}
		}
		String[] f=new String[arguments.size()];
		f=arguments.toArray(f);
		return f;
	}
	public String getStSim(String[] args) {
	
		return returnStrings("stsim",args)[0];
		
				}
	public String getCpSim(String[] args) {
		return returnStrings("cpsim",args)[0];
	}
	
}

	

