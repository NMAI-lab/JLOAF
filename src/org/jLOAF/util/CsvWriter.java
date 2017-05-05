package org.jLOAF.util;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class CsvWriter {

	public void writeCalculatedStats(String filename, String [] labels, float[] mean, float[] stdev){
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
				
			for(int i=0;i<labels.length;i++){
				sb.append(labels[i]);
				sb.append(",");
				sb.append(mean[i]);
				sb.append(",");
				sb.append(stdev[i]);
				sb.append('\n');
			}
			out.write(sb.toString());
			
			out.close();
			fw.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void writeRawStats(String filename, String [] labels, float[][] stats){
		try {
			FileWriter fw = new FileWriter(filename);
			PrintWriter out = new PrintWriter(fw);
			out.print("Performance Measure");
			for(int ii=0;ii<stats[0].length;ii++){
				out.print(",");
				out.print(ii);
			}
			out.println();
			
			for(int i=0;i<labels.length;i++){
				out.print(labels[i]);
				out.print(",");
				for(int j =0;j<stats[0].length;j++){	
					out.print(stats[i][j]);
					out.print(",");
				}
				out.println();
			}
			
			out.close();
			fw.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
