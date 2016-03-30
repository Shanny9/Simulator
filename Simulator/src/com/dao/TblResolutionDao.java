package com.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.jdbc.DBUtility;
import com.model.TblResolution;
import com.model.TblResolutionPK;
import com.model.TblSupplier;

public class TblResolutionDao {
	
	private Connection dbConnection;
	private PreparedStatement pStmt;

	public TblResolutionDao() {
		dbConnection = DBUtility.getConnection();
	}

	public void addResolution(TblResolution resolution) {
		String insertQuery = "INSERT INTO `SIMULATOR`.`tblResolution`\r\n" + 
				"(`incident_ID`,\r\n" + 
				"`course`,\r\n" + 
				"`resolution_timeA`,\r\n" + 
				"`resolution_timeB`,\r\n" + 
				"`isPurchasedA`,\r\n" + 
				"`isPurchasedB`,\r\n" + 
				"`isResolvedA`,\r\n" + 
				"`isResolvedB`,\r\n" + 
				"VALUES (?,?,?,?,?,?,?);\r\n";
		try {
			pStmt = dbConnection.prepareStatement(insertQuery);
			pStmt.setByte(1,resolution.getId().getIncident_ID());
			pStmt.setString(2, resolution.getId().getCourse());
			pStmt.setTime(3, resolution.getResolution_timeA());
			pStmt.setTime(4, resolution.getResolution_timeB());
			pStmt.setByte(5,resolution.getIsPurchasedA());
			pStmt.setByte(6,resolution.getIsPurchasedB());
			pStmt.setByte(7,resolution.getIsResolvedA());
			pStmt.setByte(8,resolution.getIsResolvedB());
			pStmt.executeUpdate();
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
	}

	public void deleteResolution(TblResolutionPK id) {
		String deleteQuery = "DELETE FROM tblResolution WHERE incident_ID = ? AND course=? ";
		try {
			pStmt = dbConnection.prepareStatement(deleteQuery);
			pStmt.setByte(1, id.getIncident_ID());
			pStmt.setString(2, id.getCourse());
			pStmt.executeUpdate();
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
	}

	public void updateResolution(TblResolution resolution)  {
		String updateQuery = "UPDATE `SIMULATOR`.`tblResolution`\r\n" + 
				"SET\r\n" + 
				"`incident_ID` = ?,\r\n" + 
				"`course` = ?,\r\n" + 
				"`resolution_timeA` = ?,\r\n" + 
				"`resolution_timeB` = ?,\r\n" + 
				"`isPurchasedA` = ?,\r\n" + 
				"`isPurchasedB` = ?,\r\n" + 
				"`isResolvedA` = ?,\r\n" + 
				"`isResolvedB` = ?\r\n" + 
				"WHERE `incident_ID` = ? AND `course` = ?;";
		try {
			pStmt = dbConnection.prepareStatement(updateQuery);		
			pStmt.setByte(1,resolution.getId().getIncident_ID());
			pStmt.setString(2, resolution.getId().getCourse());
			pStmt.setTime(3, resolution.getResolution_timeA());
			pStmt.setTime(4, resolution.getResolution_timeB());
			pStmt.setByte(5,resolution.getIsPurchasedA());
			pStmt.setByte(6,resolution.getIsPurchasedB());
			pStmt.setByte(7,resolution.getIsResolvedA());
			pStmt.setByte(8,resolution.getIsResolvedB());
			
			pStmt.setByte(9,resolution.getId().getIncident_ID());
			pStmt.setString(10, resolution.getId().getCourse());
			pStmt.executeUpdate();

		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
	}
//TODO
	public List<TblSupplier> getAllSuppliers(int startPageIndex, int recordsPerPage) {
		List<TblSupplier> suppliers = new ArrayList<TblSupplier>();
		
		String query = "SELECT * FROM tblSupplier ORDER BY supplier_name\n"
		+"limit "+startPageIndex + "," +recordsPerPage;

		try {
			Statement stmt = dbConnection.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				TblSupplier supplier = new TblSupplier();

				supplier.setSupplierName(rs.getString("supplier_name"));
				supplier.setSolutionCost(rs.getDouble("solution_cost"));
				supplier.setIsActive(rs.getByte("isActive"));
				suppliers.add(supplier);
			}
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		return suppliers;
	  }

	public List<TblSupplier> getAllSuppliers()
	{
		List<TblSupplier> suppliers = new ArrayList<TblSupplier>();
		
		String query = "SELECT * FROM tblSupplier ORDER BY supplier_name";

		try {
			Statement stmt = dbConnection.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				TblSupplier supplier = new TblSupplier();

				supplier.setSupplierName(rs.getString("supplier_name"));
				supplier.setSolutionCost(rs.getDouble("solution_cost"));
				supplier.setIsActive(rs.getByte("isActive"));
				suppliers.add(supplier);
			}
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		return suppliers;
	}

	public int getSupplierCount()
	{
	        int count=0;
	        try 
	        {
	           Statement stmt = dbConnection.createStatement();
	           ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS COUNT FROM SIMULATOR.tblSupplier;");
	           while (rs.next()) 
	           {
	                count=rs.getInt("COUNT");
	           }
	        } 
	        catch (SQLException e) 
	        {
	                System.err.println(e.getMessage());
	        }
	        return count;
	}

}
