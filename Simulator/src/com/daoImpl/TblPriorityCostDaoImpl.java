package com.daoImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.dao.TblPriorityCostDao;
import com.jdbc.DBUtility;
import com.model.TblPriority;
import com.model.TblPriorityPK;
import com.model.TblPriority_Cost;
import com.model.TblSupplier;

public class TblPriorityCostDaoImpl implements TblPriorityCostDao {

	private Connection dbConnection;
	private PreparedStatement pStmt;

	public TblPriorityCostDaoImpl() {
		dbConnection = DBUtility.getConnection();
	}

	@Override
	public void addPriorityCost(TblPriority_Cost priority) {
		String insertQuery = "INSERT INTO `SIMULATOR`.`tblPriority_Cost`\r\n" + 
				"(`pName`,\r\n" + 
				"`pCost`,\r\n" + 
				"`isActive`,\r\n" + 
				"(?,?,?);";
		try {
			pStmt = dbConnection.prepareStatement(insertQuery);
			pStmt.setString(1, priority.getPName());
			pStmt.setDouble(2, priority.getPCost());
			pStmt.setBoolean(3, priority.getIsActive());
			pStmt.executeUpdate();
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
	}

	@Override
	public void deletePriorityCost(String pk) {
		String deleteQuery = "DELETE FROM tblPriority_Cost WHERE pName = ?";
		try {
			pStmt = dbConnection.prepareStatement(deleteQuery);
			pStmt.setString(1, pk);
			pStmt.executeUpdate();
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
	}

	@Override
	public void updatePriorityCost(TblPriority_Cost priority, String name) {
		String updateQuery = "UPDATE `SIMULATOR`.`tblPriority_Cost`\r\n" + 
				"SET\r\n" + 
				"`pName` = ?,\r\n" + 
				"`pCost` = ?,\r\n" + 
				"`isActive` = ?\r\n" + 
				"WHERE `pName` = ?;";
		try {
			pStmt = dbConnection.prepareStatement(updateQuery);
			pStmt.setString(1, priority.getPName());
			pStmt.setDouble(2, priority.getPCost());
			pStmt.setBoolean(3, priority.getIsActive());
			pStmt.setString(4, name);

			pStmt.executeUpdate();

		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
	}

	@Override
	public List<TblPriority_Cost> getAllPriorityCost(int startPageIndex, int recordsPerPage) {
		List<TblPriority_Cost> priorities = new ArrayList<TblPriority_Cost>();

		String query = "SELECT * FROM TblPriority_Cost " + "limit " + startPageIndex + ","
				+ recordsPerPage;

		try {
			Statement stmt = dbConnection.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				TblPriority_Cost pr = new TblPriority_Cost();
				pr.setPName(rs.getString("pName"));
				pr.setPCost(rs.getDouble("pCost"));
				pr.setIsActive(rs.getBoolean("isActive"));
				priorities.add(pr);
			}
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		return priorities;
	}

	@Override
	public List<TblPriority_Cost> getAllPriorityCost() {
		List<TblPriority_Cost> priorities = new ArrayList<TblPriority_Cost>();

		String query = "SELECT * FROM tblPriority";

		try {
			Statement stmt = dbConnection.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				TblPriority_Cost pr = new TblPriority_Cost();
				pr.setPName(rs.getString("pName"));
				pr.setPCost(rs.getDouble("pCost"));
				pr.setIsActive(rs.getBoolean("isActive"));
				priorities.add(pr);
			}
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		return priorities;
	}

	@Override
	public int getPriorityCostCount() {
		int count = 0;
		try {
			Statement stmt = dbConnection.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS COUNT FROM SIMULATOR.tblPriority_Cost;");
			while (rs.next()) {
				count = rs.getInt("COUNT");
			}
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		return count;
	}

	@Override
	public TblPriority_Cost getPriorityCostById(String name) {
		TblPriority_Cost p = null;
		String query = "SELECT * FROM tblPriority_Cost WHERE pName = ?";

		try {
			pStmt = dbConnection.prepareStatement(query);
			pStmt.setString(1, name);
			ResultSet rs = pStmt.executeQuery();
			rs.next();
			p = new TblPriority_Cost();
			p.setPName(rs.getString("pName"));
			p.setPCost(rs.getDouble("pCost"));
			p.setIsActive(rs.getBoolean("isActive"));

		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		return p;
	}	
}
