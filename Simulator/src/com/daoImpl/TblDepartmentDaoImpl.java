package com.daoImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

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
	public void addDepartment(TblDepartment department) {
		String insertQuery = "INSERT INTO tblDepartment(department_name, devision_name, isActive) VALUES (?,?,?)";
		try {
			pStmt = dbConnection.prepareStatement(insertQuery);
			pStmt.setString(1, department.getDepartmentName());
			pStmt.setString(2, department.getDevisionName());
			pStmt.setBoolean(3, department.getIsActive());
			pStmt.executeUpdate();
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
	}

	@Override
	public void deleteDepartment(TblDepartmentPK pk) {
		String deleteQuery = "DELETE FROM tblDepartment WHERE devision_name = ? and department_name = ?";
		try {
			pStmt = dbConnection.prepareStatement(deleteQuery);
			pStmt.setString(1, pk.getDevisionName());
			pStmt.setString(2, pk.getDepartmentName());
			pStmt.executeUpdate();
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
	}

	@Override
	public void updateDepartment(TblDepartment department, TblDepartmentPK pk) {
		String updateQuery = "UPDATE tblDepartment SET department_name = ?, devision_name=?, isActive = ? WHERE devision_name = ? and department_name = ?";
		try {
			pStmt = dbConnection.prepareStatement(updateQuery);
			pStmt.setString(2, department.getDevisionName());
			pStmt.setBoolean(3, department.getIsActive());
			pStmt.setString(1, department.getDepartmentName());
			
			pStmt.setString(4, pk.getDevisionName());
			pStmt.setString(5, pk.getDepartmentName());
			pStmt.executeUpdate();
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
	}

	@Override
	public List<TblDepartment> getAllDepartments(int startPageIndex, int recordsPerPage) {
		List<TblDepartment> departments = new ArrayList<TblDepartment>();

		String query = "SELECT * FROM tblDepartment limit " + startPageIndex + "," + recordsPerPage;

		try {
			Statement stmt = dbConnection.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				TblDepartment department = new TblDepartment();
				department.setDevisionName(rs.getString("devision_name"));
				department.setDepartmentName(rs.getString("department_name"));
				department.setIsActive(rs.getBoolean("isActive"));
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
				department.setDevisionName(rs.getString("devision_name"));
				department.setDepartmentName(rs.getString("department_name"));
				department.setIsActive(rs.getBoolean("isActive"));
				departments.add(department);
			}
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		return departments;
	}

	@Override
	public TblDepartment getDepartmentById(String divisionName, String departmentName) {
		TblDepartment department = null;
		String query = "SELECT * FROM tblDepartment WHERE devision_name = ? and department_name = ?";

		try {
			pStmt = dbConnection.prepareStatement(query);
			pStmt.setString(1, divisionName);
			pStmt.setString(1, departmentName);
			ResultSet rs = pStmt.executeQuery();
			rs.next();
			department = new TblDepartment();
			department.setDevisionName(rs.getString("devision_name"));
			department.setDepartmentName(rs.getString("department_name"));
			department.setIsActive(rs.getBoolean("isActive"));

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
			ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS COUNT FROM SIMULATOR.tblDepartment;");
			while (rs.next()) {
				count = rs.getInt("COUNT");
			}
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		return count;
	}

}
