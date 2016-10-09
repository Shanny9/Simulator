package com.daoImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import utils.QueryLogger;

import com.dao.TblDepartmentDao;
import com.jdbc.DBUtility;
import com.model.TblDepartment;
import com.model.TblDepartmentPK;

public class TblDepartmentDaoImpl implements TblDepartmentDao {

	private Connection dbConnection;
	private PreparedStatement pStmt;

	public TblDepartmentDaoImpl() {
		dbConnection = DBUtility.getConnection();
	}

	@Override
	public void addDepartment(TblDepartment department) throws SQLException {

		String insertQuery = "INSERT INTO tblDepartment(department_name, division_name, isActive, shortName) VALUES (?,?,?,?)";

			pStmt = dbConnection.prepareStatement(insertQuery);
			pStmt.setString(1, department.getDepartmentName());
			pStmt.setString(2, department.getDivisionName());
			pStmt.setBoolean(3, department.isActive());
			pStmt.setString(4, department.getShortName());
			pStmt.executeUpdate();
			QueryLogger.log(pStmt.toString());
	}

	@Override
	public void deleteDepartment(TblDepartmentPK pk) throws SQLException {
		String deleteQuery = "DELETE FROM tblDepartment WHERE division_name = ? and department_name = ?";

		pStmt = dbConnection.prepareStatement(deleteQuery);
		pStmt.setString(1, pk.getDevisionName());
		pStmt.setString(2, pk.getDepartmentName());
		pStmt.executeUpdate();
		QueryLogger.log(pStmt.toString());
	}

	@Override
	public void updateDepartment(TblDepartment department, TblDepartmentPK pk) throws SQLException {
		String updateQuery = "UPDATE tblDepartment SET department_name = ?, division_name=?, isActive = ?, shortName = ? WHERE division_name = ? and department_name = ?";

			pStmt = dbConnection.prepareStatement(updateQuery);
			pStmt.setString(1, department.getDepartmentName());
			pStmt.setString(2, department.getDivisionName());
			pStmt.setBoolean(3, department.isActive());
			pStmt.setString(4, department.getShortName());
			pStmt.setString(5, pk.getDevisionName());
			pStmt.setString(6, pk.getDepartmentName());
			pStmt.executeUpdate();
			QueryLogger.log(pStmt.toString());
	}

	@Override
	public List<TblDepartment> getAllDepartments(int startPageIndex,
			int recordsPerPage) {
		List<TblDepartment> departments = new ArrayList<TblDepartment>();

		String query = "SELECT * FROM tblDepartment limit " + startPageIndex
				+ "," + recordsPerPage;

		try {
			Statement stmt = dbConnection.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				TblDepartment department = new TblDepartment();
				department.setDivisionName(rs.getString("division_name"));
				department.setDepartmentName(rs.getString("department_name"));
				department.setActive(rs.getBoolean("isActive"));
				department.setShortName(rs.getString("shortName"));
				departments.add(department);
			}
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		return departments;
	}

	@Override
	public List<TblDepartment> getAllDepartments() {
		List<TblDepartment> departments = new ArrayList<TblDepartment>();

		String query = "SELECT * FROM tblDepartment";

		try {
			Statement stmt = dbConnection.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				TblDepartment department = new TblDepartment();
				department.setDivisionName(rs.getString("division_name"));
				department.setDepartmentName(rs.getString("department_name"));
				department.setActive(rs.getBoolean("isActive"));
				department.setShortName(rs.getString("shortName"));
				departments.add(department);
			}
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		return departments;
	}
	
	@Override
	public List<TblDepartment> getAllActiveDepartments() {
		List<TblDepartment> departments = new ArrayList<TblDepartment>();

		String query = "SELECT * FROM tblDepartment WHERE isActive = 1;";

		try {
			Statement stmt = dbConnection.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				TblDepartment department = new TblDepartment();
				department.setDivisionName(rs.getString("division_name"));
				department.setDepartmentName(rs.getString("department_name"));
				department.setActive(rs.getBoolean("isActive"));
				department.setShortName(rs.getString("shortName"));
				departments.add(department);
			}
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		return departments;
	}

	@Override
	public TblDepartment getDepartmentById(String divisionName,
			String departmentName) {
		TblDepartment department = null;
		String query = "SELECT * FROM tblDepartment WHERE division_name = ? and department_name = ?";

		try {
			pStmt = dbConnection.prepareStatement(query);
			pStmt.setString(1, divisionName);
			pStmt.setString(1, departmentName);
			ResultSet rs = pStmt.executeQuery();
			rs.next();
			department = new TblDepartment();
			department.setDivisionName(rs.getString("division_name"));
			department.setDepartmentName(rs.getString("department_name"));
			department.setActive(rs.getBoolean("isActive"));
			department.setShortName(rs.getString("shortName"));

		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		return department;
	}

	@Override
	public int getDepartmentCount() {
		int count = 0;
		try {
			Statement stmt = dbConnection.createStatement();
			ResultSet rs = stmt
					.executeQuery("SELECT COUNT(*) AS COUNT FROM SIMULATOR.tblDepartment;");
			while (rs.next()) {
				count = rs.getInt("COUNT");
			}
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		return count;
	}

}
