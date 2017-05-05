package org.jLOAF.performance;

import java.util.ArrayList;
import java.util.List;

public class PerformanceMeasureCalculator {
	
	/***
	 * Takes a list of statistics bundles and prints a list of mean and standard deviation values for each label
	 * @author Sacha Gunaratne 
	 * @since 2017 May
	 * ***/
	public void CalculateAllStats(List<StatisticsBundle> AllStats){
		//get data
		int len = AllStats.get(0).getAllStatistics().length;
		String [] labels = AllStats.get(0).getLabels();
		int num_bndls = AllStats.size();
		
		//variable defintion
		float [][] stats = new float[num_bndls][len];
		float [] mean = new float[len];
		float [] sumdeviationquared = new float[len];
		
		//calculate totals
		for(int j=0;j<num_bndls;j++){
			for(int i=0;i<len;i++){
				stats[j][i] = AllStats.get(j).getAllStatistics()[i];
				mean[i]+= AllStats.get(j).getAllStatistics()[i];
			}
		}
		//calculate mean
		for(int i =0;i<len;i++){
			mean[i] = mean[i]/(float) num_bndls;
		}
		//calculate deviationsquared
		for(int j=0;j<num_bndls;j++){
			for(int i=0;i<len;i++){
				stats[j][i] = (stats[j][i]-mean[i])*(stats[j][i]-mean[i]);
			}
		}
		//calculate sumdeviationsquared
		for(int j=0;j<num_bndls;j++){
			for(int i=0;i<len;i++){
				sumdeviationquared[i] += stats[j][i];
			}
		}
		//calculate division
		for(int i =0;i<len;i++){
			sumdeviationquared[i] = sumdeviationquared[i]/(float) num_bndls;
		}
		
		//calculate sqrt
		for(int i =0;i<len;i++){
			sumdeviationquared[i] = (float) Math.pow((double)sumdeviationquared[i],0.5);
		}
		System.out.format("%45s \n", "_________________________________________________");
		System.out.format("|%25s|%10s|%10s| \n", "P. Measure","Mean","St.Dev" );
		System.out.format("|%45s| \n", "-----------------------------------------------");
		for(int i=0;i<len;i++){
			//String s = labels[i]+ ": "+ mean[i] + " \u00B1 " +sumdeviationquared[i];
			System.out.format("|%25s|%10.4f|%10.4f| \n", labels[i],mean[i],sumdeviationquared[i]);
		}
		System.out.format("|%45s| \n", "_______________________________________________");
	
	}
	
	public static void main(String a[]){
		//little test
		float [] stats1 = {(float) 0.695,(float) 0.781};
		String [] labels1 = {"height","width"};
		StatisticsBundle bdnl1 = new StatisticsBundle(stats1,labels1);
		
		float [] stats2 = {(float) 0.875,(float) 0.762};
		String [] labels2 = {"height","width"};
		StatisticsBundle bdnl2 = new StatisticsBundle(stats2,labels2);
		
		ArrayList<StatisticsBundle> li = new ArrayList<StatisticsBundle>();
		li.add(bdnl1);
		li.add(bdnl2);
		
		PerformanceMeasureCalculator pmc = new PerformanceMeasureCalculator();
		pmc.CalculateAllStats(li);
		
	}
	
}
