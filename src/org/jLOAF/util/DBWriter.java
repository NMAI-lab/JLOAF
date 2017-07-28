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

			statement.executeUpdate("CREATE TABLE choco (Performance STRING, Mean STRING, Standard Deviation STRING)");

			for(String feature: mean.keySet()){
				statement.executeUpdate("INSERT INTO choco values(' "+feature+"', '"+mean.get(feature)+"',' "+stdev.get(feature)+"')");
			}


		}catch(SQLException e){  System.err.println(e.getMessage()); 

		}
	}
}
