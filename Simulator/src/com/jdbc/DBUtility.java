package com.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBUtility {
	private static Connection connection = null;
	private static final String driver = "jdbc:mysql";
	private static final String server = "132.75.252.108";
	private static final String port = "3306";
	private static final String schema = "SIMULATOR";
	private static final String username = "ts2016";
	private static final String password = "n227u31";

	public synchronized static Connection getConnection() {
		if (connection != null) {
			return connection;
		} else {

			String urlPattern = "[driver]://[server]:[port]/[schema]";
			String dbUrl = urlPattern
					.replace("[driver]", driver)
					.replace("[server]", server)
					.replace("[port]", port)
					.replace("[schema]", schema);

			try {
				Class.forName("com.mysql.jdbc.Driver");
				connection = DriverManager.getConnection(dbUrl, username, password);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return connection;
		}
	}
	
	public static String getSchemaName(){
		return schema;
	}
}
