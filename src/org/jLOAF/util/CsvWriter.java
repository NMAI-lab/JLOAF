package org.jLOAF.util;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
/**
 * Creates a writer instance
 * @author sachagunaratne
 *
 */
public class CsvWriter {
	/**
	 * Takes means and std and writes them to a CSV file. 
	 * @param filename Output filename
	 * @param mean HashMap of Means
	 * @param stdev HashMap of Standard Deviations
	 */
	public void writeCalculatedStats(String filename, HashMap<String, Float> mean, HashMap<String, Float> stdev, double [] filterTime, double [] testTime){
		try {
			FileWriter fw = new FileWriter(filename);
			PrintWriter out = new PrintWriter(fw);
			StringBuilder sb = new StringBuilder();
			sb.append("Performance Measure");
			sb.append(",");
			sb.append("Mean");
			sb.append(",");
			sb.append("Standard Deviation");
			sb.append('\n');
				
			for(String feature:mean.keySet()){
				sb.append(feature);
				sb.append(",");
				sb.append(mean.get(feature));
				sb.append(",");
				sb.append(stdev.get(feature));
				sb.append('\n');
			}
			out.write(sb.toString());
			
			StringBuilder sb2 = new StringBuilder();
			sb2.append('\n');
			sb2.append(",");
			for(int i=0;i<filterTime.length;i++){
				sb2.append("Cycle"+i);
				if(i!=filterTime.length-1){
					sb2.append(",");
				}
			}
			sb2.append('\n');
			sb2.append("Filter Time (Seconds)");
			sb2.append(",");
			for(double val: filterTime){
				sb2.append(val);
				sb2.append(",");
			}
			sb2.append('\n');
			sb2.append("Test Time (Minutes)");
			sb2.append(",");
			for(double val: testTime){
				sb2.append(val);
				sb2.append(",");
			}
			sb2.append('\n');
			out.write(sb2.toString());
			
			out.close();
			fw.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * Writes the Raw statistics of each cv fold to a CSV File.
	 * @param AllStats
	 */
	public void writeRawStats(List<HashMap<String, Float>> AllStats, String filename){
		try {
			
			String[] temp = filename.split(",");
			temp[0]=temp[0].split("/")[1];
			
			FileWriter fw = new FileWriter(temp[0]+"-rawStats.csv");
			PrintWriter out = new PrintWriter(fw);
			StringBuilder sb = new StringBuilder();
			sb.append("Performance Measure");
			
			for(int i=0;i<AllStats.size();i++){
				sb.append(",");
				sb.append("Cycle"+i);
			}
			
			sb.append("\n");
			
			Set<String> keys = AllStats.get(0).keySet();
			for(String s:keys){
				sb.append(s);
				for(HashMap<String, Float> stats: AllStats){
					sb.append(",");
					sb.append(stats.get(s));
				}
				sb.append("\n");		
			}		
			
			out.write(sb.toString());
			
			out.close();
			fw.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
