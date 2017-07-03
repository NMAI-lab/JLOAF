package org.jLOAF.performance;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.jLOAF.Agent;
import org.jLOAF.casebase.Case;
import org.jLOAF.casebase.CaseBase;
import org.jLOAF.preprocessing.filter.CaseBaseFilter;
import org.jLOAF.util.CsvWriter;
/***
 * Abstract class that can be extended and used to test performance
 * Steps:
 * implement the abstract trainAgent method and make sure to change return typ from Agent to the subclassed Agent type
 * A sample implementation is provided in the comments below it
 * @author sachagunaratne
 * ***/
public abstract class PerformanceEvaluator {
	
	
	
	
	/***
	 * Converts a string[] of log filenames into a string[] of casebase names
	 * After converting each log into casebase using LogFile2CaseBase methods
	 * @throws IOException 
	 * ***/
	public abstract String[] createArrayOfCasebaseNames(String [] filenames) throws IOException;
	
	/***
	 * 
	 * Sample implementation:
	 * 
	 * in this setup the RobocupAgent is a subclass of Agent
	 *  
	 * public RoboCupAgent(String matchType, CaseBase cb){
	 *    RoboCupAgent agent = new RoboCupAgent(cb)
	 *    agent.setSim(matchType);
	 *    return agent;
	 * }
	 * 
	 * ***/
	public abstract Agent  createAgent();
	
	/***
	 * Evaluates performance using leave one out method using multiple casebases
	 * and prints out performance data as well as saves to a csv
	 * @throws IOException 
	 * ***/
	public void PerformanceEvaluatorMethod(String []filenames,CaseBaseFilter filter) throws IOException{
		ArrayList<CaseBase> listOfCaseBases=new ArrayList<CaseBase>();
		ArrayList<CaseBase> tempList = new ArrayList<CaseBase>();
		int ignore =0;
		CaseBase tb = null;
		CaseBase cb = new CaseBase();;
		
		long startTime = System.currentTimeMillis();
		
		String[]cbname = createArrayOfCasebaseNames(filenames);
		
		long endTime = System.currentTimeMillis();
		
		System.out.println("Time Taken to load casebases: " + (endTime - startTime)/1000.0 + " seconds");
		
		//adds all casebases to masterlist
		for(String s: cbname){
			listOfCaseBases.add(CaseBase.load(s));
		}
		
		//end of it
		//creates main stats bundle list
		ArrayList<HashMap<String, Float>>AllStats = new ArrayList<HashMap<String, Float>>();
		Agent agent = createAgent();
		
		
		
		
		//loop over all casebases
		long totalTime = System.currentTimeMillis();
		for(int ii=0;ii<listOfCaseBases.size();ii++){
			//temp list
			tempList.addAll(listOfCaseBases);
			
			//add ignore index casebase to testbase tb and remove from templist
			//add temp list to caseBase cb
			for(int i=0;i<listOfCaseBases.size();i++){
				if(ignore==i) {tb = listOfCaseBases.get(i);tempList.remove(ignore);}
				else {cb.addListOfCaseBases(tempList);}
			}
			
			if(filter!=null){
				
				System.out.println("performing Filtering on the casesbases");
				long tempTime = System.currentTimeMillis();
				
				cb=filter.filter(cb);
				tb = filter.filter(tb);
				
				tempTime = System.currentTimeMillis() - tempTime;
				
				System.out.println("time Taken to Filter is " + tempTime/1000.0 +" seconds");
			}
			

			agent.train(cb);
			Statistics stats_module = new Statistics(agent);
			
			//start testing 
			System.out.println("Cycle: "+ ignore + " - Starting testing...");
			startTime = System.currentTimeMillis();
			
			for(Case test: tb.getCases()){
				stats_module.predictedCorrectActionName(test);
			}
			
			endTime = System.currentTimeMillis();
		
			System.out.println("Testing complete in: "+ (endTime - startTime)/(1000.0*60.0) + " min");
			//adds current stats bundle to main list
			AllStats.add(stats_module.getStatisticsHashMap());

			ignore++;
		}
		
		long finalTime = System.currentTimeMillis();
		System.out.println("Total testing time: "+ (finalTime - totalTime)/(1000.0*60.0) + " min");
		
		//calculate all stats
		System.out.println("Calculating stats...");
		PerformanceMeasureCalculator pmc = new PerformanceMeasureCalculator(AllStats);
		pmc.CalculateAllStats();
		System.out.println("Done");
		
		System.out.println("Writing stats to file...");
		//writes calculated stats into a csv file
		CsvWriter writer = new CsvWriter();
		writer.writeCalculatedStats("Sample.csv", pmc.calcMean(), pmc.calcStDev(pmc.calcMean(), pmc.calcMatrix()));
		System.out.println("Done");
	}
}
