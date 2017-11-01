package org.jLOAF.performance;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import org.jLOAF.Agent;
import org.jLOAF.inputs.*;
import org.jLOAF.Reasoning;
import org.jLOAF.casebase.Case;
import org.jLOAF.casebase.CaseBase;
import org.jLOAF.preprocessing.filter.CaseBaseFilter;
import org.jLOAF.sim.ComplexSimilarityMetricStrategy;
import org.jLOAF.sim.StateBasedSimilarity;
import org.jLOAF.util.CsvWriter;
import org.jLOAF.util.DBWriter;
/***
 * Abstract class that can be extended and used to test performance
 * 
 * Implement the abstract trainAgent method and make sure to change return type from Agent to the subclassed Agent type
 * A sample implementation is provided in the comments below it
 * @author sachagunaratne
 * ***/
public abstract class PerformanceEvaluator {

	CaseBase tb = null;
	CaseBase cb = new CaseBase();
	/***
	 * Converts a string[] of log filenames into a string[] of casebase names
	 * After converting each log into casebase using LogFile2CaseBase methods
	 * @param filenames An array containing the logfile names
	 * @return An array with the CaseBase names
	 * @throws IOException 
	 * ***/
	public abstract String[] createArrayOfCasebaseNames(String [] filenames) throws IOException;

	/*
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
	 * */

	/**
	 * Creates an Agent
	 * @return An Agent
	 */
	public abstract Agent  createAgent();

	/**
	 * Evaluates performance using leave one out method using multiple casebases
	 * and prints out performance data as well as saves to a CSV.
	 * 
	 * @param filenames An array of the logfile names
	 * @param filter A CaseBaseFilter 
	 * @param output_stats the location and name of the output statistics file
	 * @param r reasoner name
	 * @param st a stateBasedSimilarityMetricStrategy name  
	 * @param cp a complexSimilarityMetricStrategy name
	 * @throws IOException
	 */
	public void PerformanceEvaluatorMethod(String []filenames,CaseBaseFilter filter, String output_stats,String r,String st,String cp) throws IOException{
		ArrayList<CaseBase> listOfCaseBases=new ArrayList<CaseBase>();
		ArrayList<CaseBase> tempList = new ArrayList<CaseBase>();
		int ignore =0;
		
		if(r==null){
			throw new IllegalArgumentException("The reasoner cannot be null. Please set the reasoner");
		}

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
		
		double [] filterTime = new double[listOfCaseBases.size()];
		double [] testTime = new double[listOfCaseBases.size()];

		//loop over all casebases
		long totalTime = System.currentTimeMillis();
		for(int ii=0;ii<listOfCaseBases.size();ii++){
			//temp list
			tempList.clear();
			tempList.addAll(listOfCaseBases);

			//add ignore index casebase to testbase tb and remove from templist
			//add temp list to caseBase cb
			//remove this method here
			//cb.addListOfCaseBases(listOfCaseBases);
			for(int i=0;i<listOfCaseBases.size();i++){
				if(ignore==i) {tb = listOfCaseBases.get(i);tempList.remove(ignore);break;}
			}
			
			cb.getCases().clear();
			cb.addListOfCaseBases(tempList);
			
			//setting SimilarityMetricStrategies
			if(st!=null){
				StateBasedSimilarity sim = (StateBasedSimilarity) StateBasedSimilarity.getSim(st);
				for(Case c: tb.getCases()){
					c.getInput().setSimilarityMetric(sim);
				}

			}
			if(cp!=null){
				
				ComplexSimilarityMetricStrategy sim = (ComplexSimilarityMetricStrategy) ComplexSimilarityMetricStrategy.getSim(cp);
				for(Case c: tb.getCases()){
					((StateBasedInput)c.getInput()).getInput().setSimilarityMetric(sim);
				}
				
			}

			if(filter!=null){

				System.out.println("performing Filtering on the casesbases");
				long tempTime = System.currentTimeMillis();
				cb=filter.filter(cb);
				//remove tb filter
				tb = filter.filter(tb);

				tempTime = System.currentTimeMillis() - tempTime;

				filterTime[ii]= tempTime/1000.0;
				System.out.println("time Taken to Filter is " + tempTime/1000.0 +" seconds");
			}
			
			System.out.println("CaseBase size: " + cb.getSize());
			System.out.println("TestBase size: " + tb.getSize());

			//add function to split casebase into cb and tb
			//SplitTrainTest(cb);

			agent.train(Reasoning.getReasoner(r, cb));

			Statistics stats_module = new Statistics(agent);
			

			//start testing 
			System.out.println("Cycle: "+ ignore + " - Starting testing...");
			startTime = System.currentTimeMillis();

			for(Case test: tb.getCases()){
				stats_module.predictedCorrectActionName(test);
			}

			endTime = System.currentTimeMillis();
			
			testTime[ii] = (endTime - startTime)/(1000.0*60.0);
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
		DBWriter writer2 = new DBWriter();
		
		writer.writeCalculatedStats(output_stats, pmc.calcMean(), pmc.calcStDev(pmc.calcMean(), pmc.calcMatrix()), filterTime, testTime);
		
		
		writer.writeRawStats(AllStats, output_stats);
		try {
			writer2.writeToDB(output_stats, pmc.calcMean(), pmc.calcStDev(pmc.calcMean(), pmc.calcMatrix()),getMeanTime(filterTime),getMeanTime(testTime));
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		System.out.println("Done");
	}

	private double getMeanTime(double[] array) {
		double sum = 0;
		for(int i=0;i<array.length;i++){
			sum+=array[i];
		}
		return sum/array.length;
	}

	protected void SplitTrainTest(CaseBase casebase){ 
		Random r = new Random();

		for(int i=0;i<casebase.getSize()*0.2;){
			Case c =(Case)casebase.getCases().toArray()[r.nextInt(casebase.getSize())];
			if(!tb.getCases().contains(c)){
				tb.add(c);
				i++;
			}
		}

		for(Case c:casebase.getCases()){
			if(!tb.getCases().contains(c)){
				cb.add(c);
			}
		}	
	}
}
