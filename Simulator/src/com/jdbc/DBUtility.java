package com.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBUtility {
	private static Connection connection = null;

	public synchronized static Connection getConnection() {
		if (connection != null){
			return connection;
		}
		else {
//			System.out.println("DBUtility: Thread number "+ Thread.currentThread().getId());
			// Store the database URL in a string
			/*String serverName = "132.75.252.108";
			String portNumber = "3306";*/

			String dbUrl = "jdbc:mysql://132.75.252.108:3306/SIMULATOR";
//			String dbUrl = "jdbc:mysql://localhost:3306/SIMULATOR";

			try {
			Class.forName("com.mysql.jdbc.Driver");
			// set the url, username and password for the database
			connection = DriverManager.getConnection(dbUrl, "ts2016", "n227u31");
//			System.out.println("DBUtility: connected successfully.");
			} catch (Exception e) {
				e.printStackTrace();
			}
			return connection;
		}
	}
}
