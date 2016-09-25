package com.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBUtility {
	private static Connection connection = null;
	private PreparedStatement pStmt;
	private static final String driver = "jdbc:mysql";
	private static final String server = "132.75.252.108";
	private static final String port = "3306";
	private static final String schema = "SIMULATOR";
	private static final String username = "ts2016";
	private static final String password = "n227u31";

	public synchronized static Connection getConnection() {
		if (connection != null) {
			try {
				String query = "SELECT * FROM tblGeneral_parameters";
				Statement stmt = connection.createStatement();
				ResultSet rs = stmt.executeQuery(query);
			} catch (com.mysql.jdbc.exceptions.jdbc4.CommunicationsException ex) {
				try {
					connection.close();
					return createNewConnection();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return connection;
		} else {

			return createNewConnection();
		}
	}

	private static Connection createNewConnection() {
		String urlPattern = "[driver]://[server]:[port]/[schema]";
		String dbUrl = urlPattern.replace("[driver]", driver)
				.replace("[server]", server).replace("[port]", port)
				.replace("[schema]", schema);

		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection(dbUrl, username, password);
		} catch (Exception e) {
			// com.mysql.jdbc.exceptions.jdbc4.CommunicationsException :
			// communications link failure
			e.printStackTrace();
		}
		return connection;
	}

	public static String getSchemaName() {
		return schema;
	}
}
