package com.daoImpl;

import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.dao.TblPriorityDao;
import com.jdbc.DBUtility;
import com.model.TblPriority;

public class TblPriorityDaoImpl implements TblPriorityDao {

	private Connection dbConnection;
	private PreparedStatement pStmt;

	public TblPriorityDaoImpl() {
		dbConnection = DBUtility.getConnection();
	}

	@Override
	public void addPriority(TblPriority priority) {
		String insertQuery = "INSERT INTO `SIMULATOR`.`tblPriority`\r\n" + "(`priority_number`,\r\n"
				+ "`priority_name`,\r\n" + "`cost`,\r\n" + "`isActive`)\r\n" + "VALUES\r\n" + "(?,?,?,?);\r\n";
		try {
			pStmt = dbConnection.prepareStatement(insertQuery);
			pStmt.setByte(1, priority.getPriorityNumber());
			pStmt.setString(2, priority.getPriorityName());
			pStmt.setDouble(3, priority.getCost());
			pStmt.setByte(4, priority.getIsActive());
			pStmt.executeUpdate();
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
	}

	@Override
	public void deletePriority(byte id) {
		String deleteQuery = "DELETE FROM tblPriority WHERE priority_number = ?";
		try {
			pStmt = dbConnection.prepareStatement(deleteQuery);
			pStmt.setByte(1, id);
			pStmt.executeUpdate();
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
	}

	@Override
	public void updatePriority(TblPriority priority) {
		String updateQuery = "UPDATE `SIMULATOR`.`tblPriority`\r\n" + "SET\r\n" + "`priority_number` = ?,\r\n"
				+ "`priority_name` = ?,\r\n" + "`cost` = ?,\r\n" + "`isActive` = ?\r\n" + "WHERE `priority_number` =?;";
		try {
			pStmt = dbConnection.prepareStatement(updateQuery);
			pStmt.setByte(1, priority.getPriorityNumber());
			pStmt.setString(2, priority.getPriorityName());
			pStmt.setDouble(3, priority.getCost());
			pStmt.setByte(4, priority.getIsActive());

			pStmt.setByte(5, priority.getPriorityNumber());
			pStmt.executeUpdate();

		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
	}

	@Override
	public List<TblPriority> getAllPriorities(int startPageIndex, int recordsPerPage) {
		List<TblPriority> priorities = new ArrayList<TblPriority>();

		String query = "SELECT * FROM tblPriority ORDER BY priority_name\n" + "limit " + startPageIndex + ","
				+ recordsPerPage;

		try {
			Statement stmt = dbConnection.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				TblPriority pr = new TblPriority();

				pr.setPriorityNumber(rs.getByte("priority_number"));
				pr.setPriorityName(rs.getString("priority_name"));
				pr.setCost(rs.getDouble("cost"));
				pr.setIsActive(rs.getByte("isActive"));
				priorities.add(pr);
			}
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		return priorities;
	}

	@Override
	public List<TblPriority> getAllPriorities() {
		List<TblPriority> priorities = new ArrayList<TblPriority>();

		String query = "SELECT * FROM tblPriority ORDER BY priority_name\n";

		try {
			Statement stmt = dbConnection.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				TblPriority pr = new TblPriority();

				pr.setPriorityNumber(rs.getByte("priority_number"));
				pr.setPriorityName(rs.getString("priority_name"));
				pr.setCost(rs.getDouble("cost"));
				pr.setIsActive(rs.getByte("isActive"));
				priorities.add(pr);
			}
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		return priorities;
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
