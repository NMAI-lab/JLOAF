package org.jLOAF.performance;

import java.io.IOException;
import java.util.ArrayList;

import org.jLOAF.Agent;
import org.jLOAF.Reasoning;
import org.jLOAF.casebase.CaseBase;
import org.jLOAF.performance.PerformanceEvaluator;
import org.jLOAF.preprocessing.filter.CaseBaseFilter;
/**
 * this class is a testing configuration class, it is used to test the performance of different agents with different parameters, such as different
 * similarities and different reasoners.
 * @author Ibrahim Ali Fawaz
 *
 */
public class TestingConfig {
	
	/**
	 * returns an array of strings with the names of log files
	 * @param args a list of Strings which has the names of the files, between the word "files" (files ali.txt ali2.txt files)
	 * @return an array of strings with the names of log files
	 */
	public String[] getFileNames(String[] args){
		//create an array that has all the strings between the two words files.
		System.out.println("got the files");
		return returnStrings("files",args);
	}
	//
	/**
	 * returns a string with the name of the output file
	 * @param args a list of Strings which has the name of output file and diffrent arguments.
	 * @return a string with the name of the output file.
	 */
	public String getOutputFileName(String[] args){
		//get the string that is between the two words output
		
		return returnStrings("output",args)[0];
	}
	/**
	 * returns a chain of caseBaseFilters that correspond the Strings passed to this function.
	 * using the private Method returnFilters. 
	 * @param args a list of Strings which has the names of the filters.
	 * @return a chain of caseBaseFilters that correspond the Strings passed to this function. 
	 */
	public CaseBaseFilter createCaseBaseFilter(String[] args){
		//create an array that has all the strings between the two words filters
		String[] filters = returnStrings("filter",args);
		if(filters==null){
			return null;
		}
		
		
		return returnFilters(0,filters);
	}
	/**
	 * takes a list of casebaseFilter names and return their Corresponding chain of caseBaseFilters
	 * @param i used for the base condition of this recursive funtion, when i becomes equal to the size of the filters, the functions stops and returns the chain of the caseBaseFilters
	 * @param filters the names of the filters to be returned
	 * @return a  chain of caseBaseFilters that correspond to a list of caseBaseFilter names.
	 */
	private CaseBaseFilter returnFilters(int i, String[] filters) {
		CaseBaseFilter f =null;
		if(i==filters.length-1){
			return CaseBaseFilter.getFilter(filters[i]);
		}
			f=CaseBaseFilter.getFilter(filters[i]);
			f.setFilter(returnFilters(i+1,filters));
			return f;
	}
	/**
	 * 
	 * @param args an array of string arguments that has the name of the reasoner between the word "reas" (reas knn reas)
	 * @return the name of the reasoner inside the array of args.
	 */
	public String getReasoner(String[] args){
		return returnStrings("reas",args)[0];
		
	}
	/**
	 * returns an array of strings that has all the strings between the same word (a) in the array args.  
	 * @param a the word that will surround the strings to be returned
	 * @param args the array of Strings that we want to get the strings from
	 * @return an array of strings that has all the strings between the same word (a) in the array args.  
	 */
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
		if(arguments.size()==0){
			return null;
		}
		String[] f=new String[arguments.size()];
		f=arguments.toArray(f);
		return f;
	}
	/**
	 * return the name of stateBasedSimilarity that is surrounded by the word stsim in the array of Strings args.
	 * this is then used to change the similarity of the stateBasedInput while testing performance.
	 * @param args the array of Strings that might have the name of the stateBasedInput Similarity
	 * @return the name of stateBasedSimilarity that is surrounded by the word stsim in the array of Strings args
	 */
	public String getStSim(String[] args) {
	String a[] =returnStrings("stsim",args);
		if(a.length!=0){
			return a[0];
		}
		return null;
	}
	/**
	 * return the name of ComplexInput Similarity that is surrounded by the word cpsim in the array of Strings args.
	 * this is then used to change the similarity of the ComplexInput while testing performance.
	 * @param args the array of Strings that might have the name of the ComplexInput Similarity
	 * @return the name of ComplexInput Similarity that is surrounded by the word cpsim in the array of Strings args.
	 */
	public String getCpSim(String[] args) {
		String a[] =returnStrings("cpsim",args);
		if(a.length!=0){
			return a[0];
		}
		return null;
	}
	
}

	

