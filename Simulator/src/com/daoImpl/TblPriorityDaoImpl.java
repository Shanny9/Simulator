package com.daoImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import utils.QueryLogger;

import com.dao.TblPriorityDao;
import com.jdbc.DBUtility;
import com.model.TblPriority;
import com.model.TblPriorityPK;

public class TblPriorityDaoImpl implements TblPriorityDao {

	private Connection dbConnection;
	private PreparedStatement pStmt;

	public TblPriorityDaoImpl() {
		dbConnection = DBUtility.getConnection();
	}

	@Override
	public void addPriority(TblPriority priority) {
		String insertQuery = "INSERT INTO `SIMULATOR`.`tblPriority`\r\n" + 
				"(`urgency`,\r\n" + 
				"`impact`,\r\n" + 
				"`priorityName`)\r\n" + 
				"VALUES\r\n" + 
				"(?,?,?);";
		try {
			pStmt = dbConnection.prepareStatement(insertQuery);
			pStmt.setString(1, priority.getId().getUrgency());
			pStmt.setString(2, priority.getId().getImpact());
			pStmt.setString(3, priority.getPriorityName());
			pStmt.executeUpdate();
			QueryLogger.log(pStmt.toString());
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
	}

	@Override
	public void deletePriority(TblPriorityPK pk) {
		String deleteQuery = "DELETE FROM tblPriority WHERE urgency = ? and impact = ?";
		try {
			pStmt = dbConnection.prepareStatement(deleteQuery);
			pStmt.setString(1, pk.getUrgency());
			pStmt.setString(2, pk.getImpact());
			pStmt.executeUpdate();
			QueryLogger.log(pStmt.toString());
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
	}

	@Override
	public void updatePriority(TblPriority priority) {
		String updateQuery = "UPDATE `SIMULATOR`.`tblPriority`\r\n" + 
				"SET\r\n" + 
				"`urgency` = ?,\r\n" + 
				"`impact` = ?,\r\n" + 
				"`priorityName` = ?\r\n" + 
				"WHERE `urgency` = ? AND `impact` = ?;";
		try {
			pStmt = dbConnection.prepareStatement(updateQuery);
			pStmt.setString(1, priority.getId().getUrgency());
			pStmt.setString(2, priority.getId().getImpact());
			pStmt.setString(3, priority.getPriorityName());
			pStmt.setString(4, priority.getId().getUrgency());
			pStmt.setString(5, priority.getId().getImpact());
			pStmt.executeUpdate();
			QueryLogger.log(pStmt.toString());
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
	}

	@Override
	public List<TblPriority> getAllPriorities(int startPageIndex, int recordsPerPage) {
		List<TblPriority> priorities = new ArrayList<TblPriority>();

		String query = "SELECT * FROM tblPriority " + "limit " + startPageIndex + ","
				+ recordsPerPage;

		try {
			Statement stmt = dbConnection.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				TblPriority pr = new TblPriority();
				pr.setImpact(rs.getString("impact"));
				pr.setUrgency(rs.getString("urgency"));
				pr.setPriorityName(rs.getString("priorityName"));
				priorities.add(pr);
			}
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		return priorities;
	}

	@Override
	/**
	 * gets all priorities by DISTINCT NAMES
	 */
	public List<TblPriority> getAllPriorities() {
		List<TblPriority> priorities = new ArrayList<TblPriority>();

		String query = "SELECT distinct priorityName FROM tblPriority";

		try {
			Statement stmt = dbConnection.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				TblPriority pr = new TblPriority();
//				pr.setImpact(rs.getString("impact"));
//				pr.setUrgency(rs.getString("urgency"));
				pr.setPriorityName(rs.getString("priorityName"));
				priorities.add(pr);
			}
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		return priorities;
	}
	
	@Override
	public List<TblPriority> getAllActivePriorities() {
		List<TblPriority> priorities = new ArrayList<TblPriority>();

		String query = "SELECT priorityName FROM tblPriority WHERE isActive = 1;";

		try {
			Statement stmt = dbConnection.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				TblPriority pr = new TblPriority();
				pr.setImpact(rs.getString("impact"));
				pr.setUrgency(rs.getString("urgency"));
				pr.setPriorityName(rs.getString("priorityName"));
				pr.setActive(rs.getBoolean("isActive"));
				priorities.add(pr);
			}
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		return priorities;
	}
	
	@Override
	public TblPriority getPriorityById(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getPriorityCount() {
		int count = 0;
		try {
			Statement stmt = dbConnection.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS COUNT FROM SIMULATOR.tblPriority;");
			while (rs.next()) {
				count = rs.getInt("COUNT");
			}
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		return count;
	}	
}
