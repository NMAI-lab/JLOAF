package org.jLOAF.util;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;

public class writer {
	HashMap <String, List<String>> container = new HashMap<String, List<String>>();
	String [] agents = {"MultipleSequenceAgent", "WallFollowerAgent", "ZigZagAgent","PreviousThreeStatesDependentAgent","SmartSquareAgent"};
	String agentName = agents[4];
	String [] traceNames = {"trace-m0-","trace-m1-","trace-m2-","trace-m3-","trace-m4-"};
	String fileext = ".txt ";
	String output_file = "";
	String [] stsim = {"korderd", "ordered", "unordered","weighted"};
	Boolean stateBased = true;
	String [] reasoners = {"TB","weightedKNN"};
	String [] cbf = {"fullclustering", "sampling", ""};
	//String [] cbf2 = {"", "hillclimbing", "geneticAlgorithm", "sequentialBackwardsAlgorithm", "weightsSeperator"};
	String [] cbf2 = {""};
	String loc = "batch_files/"+agentName+"/";
	
	public void writeReactive() {
		int count = 0;
		try {
			Files.createDirectories(Paths.get(loc.substring(0, loc.length()-1)));
			FileWriter master = new FileWriter(loc+agentName+"master.bat");
			FileWriter fw = new FileWriter(loc+agentName+"0.bat");
			master.write("start /wait cmd /k CALL "+ agentName+"0.bat");
			master.write("\r\n");
			for(int i=0;i<reasoners.length;i++) {
				for(int k = 0;k<cbf.length;k++) {
					for(int l =0;l<cbf2.length;l++) {
						if(count%4==0 && count!=0) {
							fw.flush();
							fw = new FileWriter(loc+agentName+String.valueOf(count/4)+".bat");
							master.write("start /wait cmd /k CALL "+ agentName+String.valueOf(count/4)+".bat");
							master.write("\r\n");
						}
					String s1 = "start java -jar vacuum.jar files ";
					fw.write(s1);
					for (int j=0;j<traceNames.length;j++) {
						String s2 =traceNames[j]+agentName+fileext;
						fw.write(s2);
					}
					fw.write("files ");
					fw.write("output " + reasoners[i]+"_"+agentName+"_"+cbf[k]+"_"+cbf2[l]+".csv" +" output ");
					
					//accounts for empty filter
					if(cbf[k].equals("") & !cbf2[l].equals("")) {
						fw.write("filter " + cbf2[l]+ " filter ");
					}else if(cbf2[l].equals("") & !cbf[k].equals("")) {
						fw.write("filter " + cbf[k]+ " filter ");
					}else if(cbf[k].equals("") & cbf2[l].equals("")){
						fw.write("filter filter ");
					}else {
						fw.write("filter " + cbf[k] +" "+ cbf2[l]+ " filter ");
					}
					
					//reasoner
					fw.write("reas "+ reasoners[i] + " reas");
					
					fw.write("\r\n");
					count++;
					
				}
				}
			}
			fw.close();
			master.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void main(String []a) {
		writer w = new writer();
		for(int i=0;i<5;i++){
		
		if (i!=1) {
			w.stateBased = true;
			w.writeReactive();
		}else {
			w.stateBased = false;
			w.writeState();	
		}
		}
	}

	private void writeState() {
		int count = 0;
		try {
			Files.createDirectories(Paths.get(loc.substring(0, loc.length()-1)));
			FileWriter master = new FileWriter(loc+agentName+"master.bat");
			FileWriter fw = new FileWriter(loc+agentName+"0.bat");
			master.write("start /wait cmd /k CALL "+ agentName+"0.bat");
			master.write("\r\n");
			for(int i=0;i<reasoners.length;i++) {
				for(int k = 0;k<cbf.length;k++) {
					for(int l =0;l<cbf2.length;l++) {
						for(int m=0;m<stsim.length;m++) {
							if(count%4==0) {
								fw.flush();
								fw = new FileWriter(loc+agentName+String.valueOf(count/4)+".bat");
								master.write("start /wait cmd /k CALL "+ agentName+String.valueOf(count/4)+".bat");
								master.write("\r\n");
							}
							String s1 = "start java -jar vacuum.jar files ";
							fw.write(s1);
							for (int j=0;j<traceNames.length;j++) {
								String s2 =traceNames[j]+agentName+fileext;
								fw.write(s2);
							}
							fw.write("files ");
							fw.write("output " + reasoners[i]+"_"+agentName+"_"+cbf[k]+"_"+cbf2[l]+".csv" +" output ");

							//accounts for empty filter
							if(cbf[k].equals("") & !cbf2[l].equals("")) {
								fw.write("filter " + cbf2[l]+ " filter ");
							}else if(cbf2[l].equals("") & !cbf[k].equals("")) {
								fw.write("filter " + cbf[k]+ " filter ");
							}else if(cbf[k].equals("") & cbf2[l].equals("")){
								fw.write("filter filter ");
							}else {
								fw.write("filter " + cbf[k] +" "+ cbf2[l]+ " filter ");
							}

							//reasoner
							fw.write("reas "+ reasoners[i] + " reas ");
							//stsim
							fw.write("stsim "+stsim[m]+" stsim");
							fw.write("\r\n");
							count++;
						}
					}
				}
			}
			fw.close();
			master.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
}
