package com.daoImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.dao.TblServiceDepartmentDao;
import com.jdbc.DBUtility;
import com.model.TblService_Department;
import com.model.TblService_DepartmentPK;

public class TblServiceDepartmentDaoImpl implements TblServiceDepartmentDao {
	
	private Connection dbConnection;
	private PreparedStatement pStmt;
	
	public TblServiceDepartmentDaoImpl() {
		dbConnection = DBUtility.getConnection();
	}
	@Override
	public void addServiceDepartment(TblService_Department service) {
		String insertQuery = "INSERT INTO tblService_Department (service_id, devision_name, " 
	+ "department_name, isActive) VALUES (?,?,?,?)";
		try {
			pStmt = dbConnection.prepareStatement(insertQuery);
			pStmt.setByte(1, service.getService_ID());
			pStmt.setString(2, service.getDivisionName());
			pStmt.setString(3, service.getDepartmentName());
			pStmt.setBoolean(4, service.getIsActive());

			pStmt.executeUpdate();
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		
	}

	@Override
	public void deleteServiceDepartment(TblService_DepartmentPK pk) {
		String deleteQuery = "DELETE FROM tblService_Department WHERE service_id = ? AND"
				+ " devision_name=? AND department_name=?";
		try {
			pStmt = dbConnection.prepareStatement(deleteQuery);
			pStmt.setByte(1, pk.getService_ID());
			pStmt.setString(2, pk.getDivisionName());
			pStmt.setString(3, pk.getDepartmentName());
			pStmt.executeUpdate();
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		
	}

	@Override
	public void updateServiceDepartment(TblService_Department service, TblService_DepartmentPK pk) {
		String updateQuery = "UPDATE tblService_Department SET \n " + "service_id=?, devision_name=?,  \r\n" + 
				"department_name=?, isActive=? WHERE service_id = ? AND devision_name=? AND department_name=?";
		try {
			pStmt = dbConnection.prepareStatement(updateQuery);
			pStmt.setByte(1, service.getId().getService_ID());
			pStmt.setString(2, service.getId().getDivisionName());
			pStmt.setString(3, service.getId().getDepartmentName());
			pStmt.setBoolean(4, service.getIsActive());
			
			pStmt.setByte(5, pk.getService_ID());
			pStmt.setString(6, pk.getDivisionName());
			pStmt.setString(7, pk.getDepartmentName());
			pStmt.executeUpdate();

		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		
	}

	@Override
	public List<TblService_Department> getAllServiceDepartments(
			int startPageIndex, int recordsPerPage) {
		List<TblService_Department> services = new ArrayList<TblService_Department>();

		String query = "SELECT * FROM tblService_Department "+ " limit " + startPageIndex + ","
				+ recordsPerPage;

		try {
			Statement stmt = dbConnection.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				TblService_Department service = new TblService_Department();

				service.setService_ID(rs.getByte("service_id"));
				service.setDivisionName(rs.getString("devision_name"));
				service.setDepartmentName(rs.getString("department_name"));

				service.setIsActive(rs.getBoolean("isActive"));


				services.add(service);
			}
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		return services;
	}

	@Override
	public List<TblService_Department> getAllServiceDepartments() {
		List<TblService_Department> services = new ArrayList<TblService_Department>();

		String query = "SELECT * FROM tblService_Department";

		try {
			Statement stmt = dbConnection.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				TblService_Department service = new TblService_Department();

				service.setService_ID(rs.getByte("service_id"));
				service.setDivisionName(rs.getString("devision_name"));
				service.setDepartmentName(rs.getString("department_name"));

				service.setIsActive(rs.getBoolean("isActive"));


				services.add(service);
			}
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		return services;
	}

	@Override
	public TblService_Department getServiceDepartmentById(TblService_DepartmentPK pk) {
		TblService_Department service = null;
		String query = "SELECT * FROM tblService_Department WHERE service_id = ? AND devision_name=? AND department_name=?";

		try {
			pStmt = dbConnection.prepareStatement(query);
			pStmt.setByte(1, pk.getService_ID());
			pStmt.setString(2, pk.getDivisionName());
			pStmt.setString(3, pk.getDepartmentName());
			
			ResultSet rs = pStmt.executeQuery();
			rs.next();
			service = new TblService_Department();
			
			service.setService_ID(pk.getService_ID());
			service.setDivisionName(pk.getDivisionName());
			service.setDepartmentName(pk.getDepartmentName());
			service.setIsActive(rs.getBoolean("isActive"));


		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		return service;
	}

	@Override
	public int getServiceDepartmentCount() {
		int count = 0;
		try {
			Statement stmt = dbConnection.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS COUNT FROM SIMULATOR.tblService_Department;");
			while (rs.next()) {
				count = rs.getInt("COUNT");
			}
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		return count;
	}
	

}
