package com.daoImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.jdbc.DBUtility;

public class DaoImpl {

	ResultSet rs;
	private static Connection dbConnection;
	private static ArrayList<String> tableNames = new ArrayList<String>();

	public DaoImpl() {
		dbConnection = DBUtility.getConnection();
		ResultSet rs;
		try {
			rs = dbConnection.getMetaData().getTables(null, null, "%", null);
			while (rs.next()) {
				tableNames.add(rs.getString(3));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static ResultSet getColumnType(String tblName, String colName) {

		if (tableNames.contains(tblName)) {
			
			String query ="SELECT column_type FROM INFORMATION_SCHEMA.columns where table_schema=? and table_name=?"
					+ " and column_name =? ";
			try {
				PreparedStatement pstmt = dbConnection.prepareStatement(query);
				pstmt.setString(1, DBUtility.getSchemaName());
				pstmt.setString(2, tblName);
				pstmt.setString(3, colName);
				ResultSet rs = pstmt.executeQuery(query);
				return rs;
			}

			catch (SQLException e) {
				e.printStackTrace();
				return null;
			}
		}
		else
			return null;
	}	
}
