package org.jLOAF.performance;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.jLOAF.util.CsvWriter;

public class PerformanceMeasureCalculator {
	List<HashMap<String, Float>> AllStats;
	int numMaps;
	public PerformanceMeasureCalculator(List<HashMap<String, Float>> AllStats){
		this.AllStats = AllStats;
		numMaps = AllStats.size();
	}
	
	public void CalculateAllStats(){
		HashMap<String, Float> mean = calcMean();
		HashMap<String, HashMap<String, Float>> matrix = calcMatrix();
		HashMap<String, Float> sumdeviationquared = calcStDev(mean,matrix);
		
		System.out.format("%45s \n", "_________________________________________________");
		System.out.format("|%25s|%10s|%10s| \n", "Performance Measure","Mean","St.Dev" );
		System.out.format("|%45s| \n", "-----------------------------------------------");
		for(String feature: mean.keySet()){
			//String s = labels[i]+ ": "+ mean[i] + " \u00B1 " +sumdeviationquared[i];
			System.out.format("|%25s|%10.4f|%10.4f| \n", feature,mean.get(feature),sumdeviationquared.get(feature));
		}
		System.out.format("|%45s| \n", "_______________________________________________");
	}
	
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
	
	public HashMap<String, HashMap<String, Float>> calcMatrix(){
		HashMap<String, HashMap<String, Float>> matrix = new HashMap<String, HashMap<String, Float>>();
		int count = 0;
		for(HashMap<String, Float> stats: AllStats){
				matrix.put(String.valueOf(count),stats);
				count++;
		}
		return matrix;
	}
	
	public HashMap<String, Float> calcStDev(HashMap<String, Float> mean, HashMap<String, HashMap<String, Float>> matrix){
		HashMap<String, Float> tempfeatures = new HashMap<String, Float>();
		HashMap<String, HashMap<String, Float>> indexnum = new HashMap<String, HashMap<String, Float>>();
		
		//calculate deviationsquared
		for(String index: matrix.keySet()){
			for(String feature: matrix.get(index).keySet()){
				float val = (matrix.get(index).get(feature).floatValue()-mean.get(feature).floatValue())*(matrix.get(index).get(feature).floatValue()-mean.get(feature).floatValue());
				tempfeatures.put(feature, val);
			}
			indexnum.put(index, tempfeatures);
		}
		
		HashMap<String, Float> tempfeatures2 = new HashMap<String, Float>();
		
		//calculate sumdeviationsquared
		for(String index:indexnum.keySet()){
			for(String feature: indexnum.get(index).keySet()){		
				if(!tempfeatures.containsKey(feature)){tempfeatures.put(feature,0.0f);}
				float val = tempfeatures.get(feature).floatValue()+indexnum.get(index).get(feature).floatValue();
				tempfeatures2.put(feature, val);
			}
		}
		
		//calculate division
		for(String feature:tempfeatures2.keySet()){
			float val = tempfeatures2.get(feature).floatValue()/(float) (numMaps);
			tempfeatures2.put(feature, val);
		}
		
		//calculate sqrt
		for(String feature:tempfeatures2.keySet()){
			float val = (float) Math.pow(tempfeatures.get(feature).doubleValue(), 0.5);
			tempfeatures2.put(feature, val);
		}
		
		return tempfeatures2;
	}
	
	public static void main(String a[]){
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
		writer.writeCalculatedStats("Sample.csv", pmc.calcMean(), pmc.calcStDev(pmc.calcMean(), pmc.calcMatrix()));
	//	writer.writeRawStats("rawStats.csv", pmc.labels, pmc.calcMatrix());
		
	}
}
