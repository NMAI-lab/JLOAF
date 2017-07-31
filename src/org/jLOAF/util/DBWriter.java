package org.jLOAF.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

public class DBWriter {

	public void writeToDB(String filename, HashMap<String, Float> mean, HashMap<String, Float> stdev) throws ClassNotFoundException{
		Class.forName("org.sqlite.JDBC");

		Connection connection = null;
		try
		{
			// create a database connection
			connection = DriverManager.getConnection("jdbc:sqlite:robocup.db");

			Statement statement = connection.createStatement();
			statement.setQueryTimeout(30); 
			
			//convert filename into string for sending executeUpdate
			String[] input = filename.split(",");
			
			
			//creates mean table
			StringBuilder sb = new StringBuilder();
			for(String feature: mean.keySet()){
				sb.append(feature+" STRING,");
			}
			sb.deleteCharAt(sb.length()-1);
			
			StringBuilder sb2 = new StringBuilder();
			sb2.append("INSERT INTO mean values(");
			for (String s: input){
				sb2.append("'");
				sb2.append(s);
				sb2.append("'");
				sb2.append(",");
			}
			for(String feature: mean.keySet()){
				sb2.append("'");
				sb2.append(mean.get(feature));
				sb2.append("'");
				sb2.append(",");
			}
			sb2.deleteCharAt(sb2.length()-1);
			sb2.append(")");
			
			statement.executeUpdate("CREATE TABLE IF NOT EXISTS mean (Agent STRING, Reasoner STRING, Filter1 STRING, Filter2 STRING, stateBasedSim STRING, complexSim STRING,"+sb.toString()+")");
			statement.executeUpdate(sb2.toString());
			
			//creates std table
			StringBuilder sb3 = new StringBuilder();
			for(String feature: mean.keySet()){
				sb3.append(feature+" STRING,");
			}
			sb3.deleteCharAt(sb3.length()-1);
			
			StringBuilder sb4 = new StringBuilder();
			sb4.append("INSERT INTO standard_dev values(");
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
			sb4.append(")");
			
			statement.executeUpdate("CREATE TABLE IF NOT EXISTS standard_dev (Agent STRING, Reasoner STRING, Filter1 STRING, Filter2 STRING, stateBasedSim STRING, complexSim STRING,"+sb3.toString()+")");
			statement.executeUpdate(sb4.toString());

		}catch(SQLException e){  System.err.println(e.getMessage()); 

		}
	}
}
