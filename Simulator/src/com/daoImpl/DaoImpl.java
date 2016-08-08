package com.daoImpl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.jdbc.DBUtility;
import com.model.TblSupplier;

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

	public static ResultSet getMetaData(String tblName) {

		if (tableNames.contains(tblName)) {
			String query ="SELECT column_name, column_type FROM INFORMATION_SCHEMA.columns where table_schema='SIMULATOR' and table_name="
					+ "'"+tblName+ "'";
			try {
				Statement stmt = dbConnection.createStatement();
				ResultSet rs = stmt.executeQuery(query);
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

	private List<Object> getAllRecords (String tblName){
		
		List<Object> records = new ArrayList<Object>();

		String query = "SELECT * FROM "+ tblName;

		try {
			Statement stmt = dbConnection.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				TblSupplier supplier = new TblSupplier();
				supplier.setSupplierName(rs.getString("supplier_name"));
				supplier.setSolutionCost(rs.getDouble("solution_cost"));
				supplier.setIsActive(rs.getByte("isActive"));
				supplier.setCurrency(rs.getString("currency"));
				records.add(supplier);
			}
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		return records;
	}
	
	
}
