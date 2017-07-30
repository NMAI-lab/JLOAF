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
			
			//creates mean table
			StringBuilder sb = new StringBuilder();
			for(String feature: mean.keySet()){
				sb.append(feature+" STRING,");
			}
			sb.deleteCharAt(sb.length()-1);
			
			StringBuilder sb2 = new StringBuilder();
			for(String feature: mean.keySet()){
				sb2.append("'");
				sb2.append(mean.get(feature));
				sb2.append("'");
				sb2.append(",");
			}
			sb2.deleteCharAt(sb2.length()-1);
			
			statement.executeUpdate("CREATE TABLE IF NOT EXISTS mean (Agent STRING, Reasoner STRING, Filter1 STRING, Filter2 STRING, stateBasedSim STRING, complexSim STRING,"+sb.toString()+")");
			statement.executeUpdate("INSERT INTO mean values('wf', 'KNN','clustering','none','none','none',"+sb2.toString()+")");
			
			//creates std table
			StringBuilder sb3 = new StringBuilder();
			for(String feature: mean.keySet()){
				sb3.append(feature+" STRING,");
			}
			sb3.deleteCharAt(sb3.length()-1);
			
			StringBuilder sb4 = new StringBuilder();
			for(String feature: stdev.keySet()){
				sb4.append("'");
				sb4.append(stdev.get(feature));
				sb4.append("'");
				sb4.append(",");
			}
			sb4.deleteCharAt(sb4.length()-1);
			
			statement.executeUpdate("CREATE TABLE IF NOT EXISTS standard_dev (Agent STRING, Reasoner STRING, Filter1 STRING, Filter2 STRING, stateBasedSim STRING, complexSim STRING,"+sb3.toString()+")");
			statement.executeUpdate("INSERT INTO standard_dev values('wf', 'KNN','clustering','none','none','none',"+sb4.toString()+")");

		}catch(SQLException e){  System.err.println(e.getMessage()); 

		}
	}
}
