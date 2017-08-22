package org.jLOAF.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

public class DBWriter {

	public void writeToDB(String filename, HashMap<String, Float> mean, HashMap<String, Float> stdev, double filterTime, double testTime) throws ClassNotFoundException{
		Class.forName("org.sqlite.JDBC");

		Connection connection = null;
		try
		{
			// create a database connection
			connection = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\sachagunaratne\\Documents\\GitHub\\batch_files\\database.db");

			Statement statement = connection.createStatement();
			statement.setQueryTimeout(0); 
			
			statement.executeUpdate("pragma busy_timeout=600000");
			
			//convert filename into string for sending executeUpdate
			String[] temp = filename.split(",");
			
			temp[0]=temp[0].split("/")[1];
			
			String input[] = new String[temp.length-1];
			
			for(int i=0;i<temp.length-1;i++){
				input[i]=temp[i];
			}
			
			//creates mean table
			StringBuilder sb = new StringBuilder();
			sb.append("Filter_time String, Test_time String, ");
			for(String feature: mean.keySet()){
				sb.append(feature+" STRING,");
			}
			sb.deleteCharAt(sb.length()-1);
			
			StringBuilder sb2 = new StringBuilder();
			sb2.append("BEGIN IMMEDIATE; INSERT INTO mean values(");
			
			for (String s: input){
				sb2.append("'");
				sb2.append(s);
				sb2.append("'");
				sb2.append(",");
			}
			//inset filter and test time
			sb2.append("'");
			sb2.append(filterTime);
			sb2.append("','");
			sb2.append(testTime);
			sb2.append("',");
			
			for(String feature: mean.keySet()){
				sb2.append("'");
				sb2.append(mean.get(feature));
				sb2.append("'");
				sb2.append(",");
			}
			sb2.deleteCharAt(sb2.length()-1);
			sb2.append("); END;");
			
			statement.executeUpdate("BEGIN IMMEDIATE; CREATE TABLE IF NOT EXISTS mean (Agent STRING, Reasoner STRING, Filter1 STRING, Filter2 STRING, stateBasedSim STRING, complexSim STRING,"+sb.toString()+"); END;");
			statement.executeUpdate(sb2.toString());
			
			//creates std table
			StringBuilder sb3 = new StringBuilder();
			for(String feature: mean.keySet()){
				sb3.append(feature+" STRING,");
			}
			sb3.deleteCharAt(sb3.length()-1);
			
			StringBuilder sb4 = new StringBuilder();
			sb4.append("BEGIN IMMEDIATE; INSERT INTO standard_dev values(");
			for (String s: input){
				sb4.append("'");
				sb4.append(s);
				sb4.append("'");
				sb4.append(",");
			}
			for(String feature: stdev.keySet()){
				sb4.append("'");
				sb4.append(stdev.get(feature));
				sb4.append("'");
				sb4.append(",");
			}
			sb4.deleteCharAt(sb4.length()-1);
			sb4.append("); END;");
			
			statement.executeUpdate("BEGIN IMMEDIATE; CREATE TABLE IF NOT EXISTS standard_dev (Agent STRING, Reasoner STRING, Filter1 STRING, Filter2 STRING, stateBasedSim STRING, complexSim STRING,"+sb3.toString()+"); END;");
			statement.executeUpdate(sb4.toString());
			
			statement.close();
			connection.close();

		}catch(SQLException e){  System.err.println(e.getMessage()); 

		}
	}
}
