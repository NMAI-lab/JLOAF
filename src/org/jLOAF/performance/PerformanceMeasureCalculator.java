package org.jLOAF.performance;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.apache.commons.math3.stat.descriptive.SummaryStatistics;
import org.jLOAF.util.CsvWriter;
/**
 * This class calculates the Mean and Standard Deviation of multiple test set results.
 * @author sachagunaratne
 *
 */
public class PerformanceMeasureCalculator {
	List<HashMap<String, Float>> AllStats;
	int numMaps;
	/**
	 * @param AllStats A list of HashMaps containing all the stats for each test
	 */
	public PerformanceMeasureCalculator(List<HashMap<String, Float>> AllStats){
		this.AllStats = AllStats;
		numMaps = AllStats.size();
	}
	
	/**
	 * Calculates all the stats by calling all the helper functions.
	 * Prints out the stats to the console. 
	 */
	public void CalculateAllStats(){
		HashMap<String, Float> mean = calcMean();
		HashMap<String, Float> sumdeviationquared = calcStDev();
		
		System.out.format("%45s \n", "_________________________________________________");
		System.out.format("|%25s|%10s|%10s| \n", "Performance Measure","Mean","St.Dev" );
		System.out.format("|%45s| \n", "-----------------------------------------------");
		for(String feature: mean.keySet()){
			//String s = labels[i]+ ": "+ mean[i] + " \u00B1 " +sumdeviationquared[i];
			System.out.format("|%25s|%10.4f|%10.4f| \n", feature,mean.get(feature),sumdeviationquared.get(feature));
		}
		System.out.format("|%45s| \n", "_______________________________________________");
	}
	
	/**
	 * Calculates the Mean of each statistic for each test.
	 * @return A HashMap containing the mean across each statistic for all the tests
	 */
	public HashMap<String, Float> calcMean(){
		HashMap<String, Float> mean = new HashMap<String, Float>();
		for(HashMap<String, Float> stats: AllStats){
			Set<String> keys = stats.keySet();
			for(String s:keys){
				if(!mean.containsKey(s)){
					mean.put(s,0.0f);
				}
				float val = mean.get(s).floatValue();
				mean.put(s,val+stats.get(s).floatValue());
			}
		}
		
		
		Set<String> measures = mean.keySet();
		
		for(String s: measures){
			float val = mean.get(s).floatValue();
			mean.put(s, val/numMaps);
		}
		
		return mean;
	}
	
	
	/**
	 * Calculates the Standard deviation for each statstic and returns it in a HashMap
	 * @param mean HashMap containing Means of statistics
	 * @param matrix HashMap of statistics
	 * @return HashMap with Standard Deviations for each Statistic
	 */
	public HashMap<String, Float> calcStDev(){
		
		HashMap<String, Float> tempfeatures2 = new HashMap<String, Float>();
		
		HashMap<String, SummaryStatistics> mean = new HashMap<String, SummaryStatistics>();
		for(HashMap<String, Float> stats: AllStats){
			Set<String> keys = stats.keySet();
			for(String s:keys){
				if(!mean.containsKey(s)){
					mean.put(s,new SummaryStatistics());
				}
				SummaryStatistics ss = mean.get(s);
				ss.addValue(stats.get(s).doubleValue());
				mean.put(s,ss);
			}
		}
		
		Set<String> measures = mean.keySet();
		
		for(String s: measures){
			SummaryStatistics ss = mean.get(s);
			tempfeatures2.put(s, (float) ss.getStandardDeviation());
		}
		
		
		return tempfeatures2;
	}
	
	/**
	 * Sample test function that tests the above functions. 
	 * @param args
	 */
	public static void main(String args[]){
		//little test
		
		HashMap<String, Float> stats1 = new HashMap<String, Float>();
		stats1.put("height", 0.695f);
		stats1.put("width", 0.781f);
		
		HashMap<String, Float> stats2 = new HashMap<String, Float>();
		stats2.put("height", 0.875f);
		stats2.put("width", 0.762f);
		
		ArrayList<HashMap<String, Float>> li = new ArrayList<HashMap<String, Float>>();
		li.add(stats1);
		li.add(stats2);
		
		PerformanceMeasureCalculator pmc = new PerformanceMeasureCalculator(li);
		pmc.CalculateAllStats();
		
		CsvWriter writer = new CsvWriter();
		writer.writeCalculatedStats("Sample.csv", pmc.calcMean(), pmc.calcStDev(), null, null);
	//	writer.writeRawStats("rawStats.csv", pmc.labels, pmc.calcMatrix());
		
	}
}
