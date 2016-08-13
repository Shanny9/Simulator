package com.daoImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.dao.TblDivisionDao;
import com.jdbc.DBUtility;
import com.model.TblDivision;

public class TblDivisionDaoImpl implements TblDivisionDao {

	private Connection dbConnection;
	private PreparedStatement pStmt;

	public TblDivisionDaoImpl() {
		dbConnection = DBUtility.getConnection();
	}

	@Override
	public void addDivision(TblDivision division) {
		String insertQuery = "INSERT INTO tblDivision(division_name, isActive) VALUES (?,?)";
		try {
			pStmt = dbConnection.prepareStatement(insertQuery);
			pStmt.setString(1, division.getDivisionName());
			pStmt.setBoolean(2, division.getIsActive());
			pStmt.executeUpdate();
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
	}

	@Override
	public void deleteDivision(String name) {
		String deleteQuery = "DELETE FROM tblDivision WHERE division_name = ?";
		try {
			pStmt = dbConnection.prepareStatement(deleteQuery);
			pStmt.setString(1, name);
			pStmt.executeUpdate();
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}

	}

	@Override
	public void updateDivision(TblDivision division, String divisionName) {
		String updateQuery = "UPDATE tblDivision SET division_name = ?, isActive = ? WHERE division_name = ?";
		try {
			pStmt = dbConnection.prepareStatement(updateQuery);
			pStmt.setString(1, division.getDivisionName());
			pStmt.setBoolean(2, division.getIsActive());
			pStmt.setString(3, divisionName);
			pStmt.executeUpdate();

		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
	}

	@Override
	public List<TblDivision> getAllDivisions(int startPageIndex, int recordsPerPage) {
		List<TblDivision> divisions = new ArrayList<TblDivision>();

		String query = "SELECT * FROM tblDivision limit " + startPageIndex + "," + recordsPerPage;

		try {
			Statement stmt = dbConnection.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				TblDivision division = new TblDivision();
				division.setDivisionName(rs.getString("division_name"));
				division.setIsActive(rs.getBoolean("isActive"));
				divisions.add(division);
			}
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		return divisions;
	}

	@Override
	public List<TblDivision> getAllDivisions() {
		List<TblDivision> divisions = new ArrayList<TblDivision>();

		String query = "SELECT * FROM tblDivision";

		try {
			Statement stmt = dbConnection.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				TblDivision division = new TblDivision();
				division.setDivisionName(rs.getString("division_name"));
				division.setIsActive(rs.getBoolean("isActive"));
				divisions.add(division);
			}
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		return divisions;
	}
	
	@Override
	public TblDivision getDivisionById(String divisionName) {
		TblDivision division = null;
		String query = "SELECT * FROM tblDivision WHERE division_name = ?";

		try {
			pStmt = dbConnection.prepareStatement(query);
			pStmt.setString(1, divisionName);
			ResultSet rs = pStmt.executeQuery();
			rs.next();
			division = new TblDivision();
			division.setDivisionName(rs.getString("division_name"));
			division.setIsActive(rs.getBoolean("isActive"));

		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		return division;
	}

	@Override
	public int getDivisionCount() {
		int count = 0;
		try {
			Statement stmt = dbConnection.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS COUNT FROM SIMULATOR.tblDivision;");
			while (rs.next()) {
				count = rs.getInt("COUNT");
			}
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		return count;
	}

}
