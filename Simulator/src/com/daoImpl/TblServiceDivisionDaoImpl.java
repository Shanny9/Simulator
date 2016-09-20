//package com.daoImpl;
//
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.sql.Statement;
//import java.util.ArrayList;
//import java.util.List;
//
//import com.dao.TblServiceDivisionDao;
//import com.jdbc.DBUtility;
//import com.model.TblService_Division;
//import com.model.TblService_DivisionPK;
//
//public class TblServiceDivisionDaoImpl implements TblServiceDivisionDao {
//
//	private Connection dbConnection;
//	private PreparedStatement pStmt;
//
//	public TblServiceDivisionDaoImpl() {
//		dbConnection = DBUtility.getConnection();
//	}
//	
//	@Override
//	public void addServiceDivision(TblService_Division service) throws SQLException {
//
//		String insertQuery = "INSERT INTO tblService_Division (service_id, division_name, " 
//	+ " isActive) VALUES (?,?,?)";
//
//			pStmt = dbConnection.prepareStatement(insertQuery);
//			pStmt.setByte(1, service.getService_ID());
//			pStmt.setString(2, service.getDivisionName());
//			pStmt.setBoolean(3, service.getIsActive());
//			pStmt.executeUpdate();
//
//	}
//
//	@Override
//	public void deleteServiceDivision(TblService_DivisionPK pk) throws SQLException {
//
//		String deleteQuery = "DELETE FROM tblService_Division WHERE service_id = ? AND"
//				+ " division_name=?";
//
//			pStmt = dbConnection.prepareStatement(deleteQuery);
//			pStmt.setByte(1, pk.getService_ID());
//			pStmt.setString(2, pk.getDivisionName());
//			pStmt.executeUpdate();
//		
//		
//	}
//
//	@Override
//	public List<TblService_Division> getAllServiceDivisions(int startPageIndex,
//			int recordsPerPage) {
//		List<TblService_Division> services = new ArrayList<TblService_Division>();
//
//		String query = "SELECT * FROM tblService_Division " + " limit " + startPageIndex + ","
//				+ recordsPerPage;
//
//		try {
//			Statement stmt = dbConnection.createStatement();
//			ResultSet rs = stmt.executeQuery(query);
//			while (rs.next()) {
//				TblService_Division service = new TblService_Division();
//
//				service.setService_ID(rs.getByte("service_id"));
//				service.setDivisionName(rs.getString("division_name"));
//				
//				service.setIsActive(rs.getBoolean("isActive"));
//
//				services.add(service);
//			}
//		} catch (SQLException e) {
//			System.err.println(e.getMessage());
//		}
//		return services;
//	}
//
//	@Override
//	public List<TblService_Division> getAllServiceDivisions() {
//		List<TblService_Division> services = new ArrayList<TblService_Division>();
//
//		String query = "SELECT * FROM tblService_Division";
//
//		try {
//			Statement stmt = dbConnection.createStatement();
//			ResultSet rs = stmt.executeQuery(query);
//			while (rs.next()) {
//				TblService_Division service = new TblService_Division();
//
//				service.setService_ID(rs.getByte("service_id"));
//				service.setDivisionName(rs.getString("division_name"));
//				
//				service.setIsActive(rs.getBoolean("isActive"));
//
//				services.add(service);
//			}
//		} catch (SQLException e) {
//			System.err.println(e.getMessage());
//		}
//		return services;
//	}
//
//	@Override
//	public TblService_Division getServiceDivisionById(TblService_DivisionPK pk) {
//		TblService_Division service = null;
//		String query = "SELECT * FROM tblService_Division WHERE service_id = ? AND division_name=? ";
//
//		try {
//			pStmt = dbConnection.prepareStatement(query);
//			pStmt.setByte(1, pk.getService_ID());
//			pStmt.setString(2, pk.getDivisionName());
//			
//			ResultSet rs = pStmt.executeQuery();
//			rs.next();
//			service = new TblService_Division();
//			
//			service.setService_ID(pk.getService_ID());
//			service.setDivisionName(pk.getDivisionName());
//			service.setIsActive(rs.getBoolean("isActive"));
//
//
//		} catch (SQLException e) {
//			System.err.println(e.getMessage());
//		}
//		return service;
//	}
//
//	@Override
//	public int getServiceDivisionCount() {
//		int count = 0;
//		try {
//			Statement stmt = dbConnection.createStatement();
//			ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS COUNT FROM SIMULATOR.tblService_Division;");
//			while (rs.next()) {
//				count = rs.getInt("COUNT");
//			}
//		} catch (SQLException e) {
//			System.err.println(e.getMessage());
//		}
//		return count;
//	}
//
//	@Override
//	public void updateServiceDivision(TblService_Division service,
//			TblService_DivisionPK pk) throws SQLException {
//		String updateQuery = "UPDATE tblService_Division SET \n " + "service_id=?, division_name=?,  \r\n" + 
//				" isActive=? WHERE service_id = ? AND division_name=? ";
//
//			pStmt = dbConnection.prepareStatement(updateQuery);
//			pStmt.setByte(1, service.getService_ID());
//			pStmt.setString(2, service.getDivisionName());
//			pStmt.setBoolean(3, service.getIsActive());
//			
//			pStmt.setByte(4, pk.getService_ID());
//			pStmt.setString(5, pk.getDivisionName());
//
//			pStmt.executeUpdate();
//
//		
//	}
//
//}
