package org.jLOAF.performance;

import java.util.ArrayList;

import org.jLOAF.Agent;
import org.jLOAF.casebase.Case;
import org.jLOAF.casebase.CaseBase;
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
	public abstract Agent trainAgent(String matchType,CaseBase cb);
	
	/***
	 * Evaluates performance using leave one out method using multiple casebases
	 * and prints out performance data as well as saves to a csv
	 * ***/
	public void PerformanceEvaluatorMethod(String matchType, String []cbname){
		ArrayList<CaseBase> listOfCaseBases=new ArrayList<CaseBase>();
		ArrayList<CaseBase> tempList = new ArrayList<CaseBase>();
		int ignore =0;
		CaseBase tb = null;
		CaseBase cb = new CaseBase();;
		
		//adds all casebases to masterlist
		for(String s: cbname){
			listOfCaseBases.add(CaseBase.load(s));
		}
		
		//creates main stats bundle list
		ArrayList<StatisticsBundle>AllStats = new ArrayList<StatisticsBundle>();
		
		
		//loop over all casebases
		for(int ii=0;ii<listOfCaseBases.size();ii++){
			//temp list
			tempList.addAll(listOfCaseBases);
			
			//add ignore index casebase to testbase tb and remove from templist
			//add temp list to caseBase cb
			for(int i=0;i<listOfCaseBases.size();i++){
				if(ignore==i) {tb = listOfCaseBases.get(i);tempList.remove(ignore);}
				else {cb.addListOfCaseBases(tempList);}
			}

			Agent agent = trainAgent(matchType,cb);
			Statistics stats_module = new Statistics(agent);
			
			//start testing 
			System.out.println("Cycle: "+ ignore + " - Starting testing...");
			for(Case test: tb.getCases()){
				stats_module.predictedCorrectActionName(test);
			}
			
			System.out.println("Testing complete");
			//adds current stats bundle to main list
			AllStats.add(stats_module.getStatisticsBundle());

			ignore++;
		}
		
		//calculate all stats
		PerformanceMeasureCalculator pmc = new PerformanceMeasureCalculator(AllStats);
		pmc.CalculateAllStats();
		
		//writes calculated stats into a csv file
		CsvWriter writer = new CsvWriter();
		writer.writeCalculatedStats("Sample.csv", pmc.getLabels(), pmc.calcMean(), pmc.calcStDev(pmc.calcMean(), pmc.calcMatrix()));
	}
}
