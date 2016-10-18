package com.daoImpl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import utils.QueryLogger;

import com.dao.TblServiceDao;
import com.jdbc.DBUtility;
import com.model.TblService;

public class TblServiceDaoImpl implements TblServiceDao {


	private PreparedStatement pStmt;

	public TblServiceDaoImpl() {

	}

	@Override
	public void addService(TblService service) throws SQLException {
		String insertQuery = "INSERT INTO tblService(service_id, service_code, "
				+ "service_name, isTechnical, supplier_level2, supplier_level3, fixed_cost, "
				+ "fixed_income, isActive, urgency, impact, event_id) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";

		pStmt = DBUtility.getConnection().prepareStatement(insertQuery);
		pStmt.setByte(1, service.getServiceId());
		pStmt.setString(2, service.getServiceCode());
		pStmt.setString(3, service.getServiceName());
		pStmt.setBoolean(4, service.getIsTechnical());
		pStmt.setString(5, service.getSupplierLevel2());
		pStmt.setString(6, service.getSupplierLevel3());
		pStmt.setDouble(7, service.getFixedCost());
		pStmt.setDouble(8, service.getFixedIncome());
		pStmt.setBoolean(9, service.isActive());
		pStmt.setString(10, service.getUrgency());
		pStmt.setString(11, service.getImpact());
		pStmt.setInt(12, service.getEventId());
		pStmt.executeUpdate();
		QueryLogger.log(pStmt.toString());
	}

	@Override
	public void deleteService(Byte id) throws SQLException {
		String deleteQuery = "DELETE FROM tblService WHERE service_id = ?";
		pStmt = DBUtility.getConnection().prepareStatement(deleteQuery);
		pStmt.setByte(1, id);
		pStmt.executeUpdate();
		QueryLogger.log(pStmt.toString());
	}

	@Override
	public void updateService(TblService service, byte id) throws SQLException {
		String updateQuery = "UPDATE tblService SET service_id = ?, service_code = ?, "
				+ "service_name = ?, isTechnical = ?, supplier_level2 = ?, supplier_level3=?, "
				+ "fixed_cost = ? ,fixed_income = ?, isActive = ?, urgency = ?, impact = ?, "
				+ "event_id = ? WHERE service_id = ?;";

		pStmt = DBUtility.getConnection().prepareStatement(updateQuery);
		pStmt.setByte(1, service.getServiceId());
		pStmt.setString(2, service.getServiceCode());
		pStmt.setString(3, service.getServiceName());
		pStmt.setBoolean(4, service.getIsTechnical());
		pStmt.setString(5, service.getSupplierLevel2());
		pStmt.setString(6, service.getSupplierLevel3());
		pStmt.setDouble(7, service.getFixedCost());
		pStmt.setDouble(8, service.getFixedIncome());
		pStmt.setBoolean(9, service.isActive());
		pStmt.setString(10, service.getUrgency());
		pStmt.setString(11, service.getImpact());
		pStmt.setInt(12, service.getEventId());
		pStmt.setByte(13, id);
		pStmt.executeUpdate();
		QueryLogger.log(pStmt.toString());
	}

	@Override
	public List<TblService> getAllServices(int startPageIndex,
			int recordsPerPage) {
		List<TblService> services = new ArrayList<TblService>();

		String query = "SELECT * FROM tblService " + "limit " + startPageIndex
				+ "," + recordsPerPage;

		try {
			Statement stmt = DBUtility.getConnection().createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				TblService service = new TblService();

				service.setFixedCost(rs.getDouble("fixed_cost"));
				service.setFixedIncome(rs.getDouble("fixed_income"));
				service.setImpact(rs.getString("impact"));
				service.setActive(rs.getBoolean("isActive"));
				service.setIsTechnical(rs.getBoolean("isTechnical"));
				service.setServiceId(rs.getByte("service_id"));
				service.setServiceCode(rs.getString("service_code"));
				service.setServiceName(rs.getString("service_name"));
				service.setSupplierLevel2(rs.getString("supplier_level2"));
				service.setSupplierLevel3(rs.getString("supplier_level3"));
				service.setUrgency(rs.getString("urgency"));
				service.setEventId(rs.getInt("event_id"));
				services.add(service);
			}
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		return services;
	}

	@Override
	public List<TblService> getAllServices() {
		List<TblService> services = new ArrayList<TblService>();

		String query = "SELECT * FROM tblService";

		try {
			Statement stmt = DBUtility.getConnection().createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				TblService service = new TblService();
				service.setFixedCost(rs.getDouble("fixed_cost"));
				service.setFixedIncome(rs.getDouble("fixed_income"));
				service.setImpact(rs.getString("impact"));
				service.setActive(rs.getBoolean("isActive"));
				service.setIsTechnical(rs.getBoolean("isTechnical"));
				service.setServiceId(rs.getByte("service_id"));
				service.setServiceCode(rs.getString("service_code"));
				service.setServiceName(rs.getString("service_name"));
				service.setSupplierLevel2(rs.getString("supplier_level2"));
				service.setSupplierLevel3(rs.getString("supplier_level3"));
				service.setUrgency(rs.getString("urgency"));
				service.setEventId(rs.getInt("event_id"));
				services.add(service);
			}
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		return services;
	}
	
	@Override
	public List<TblService> getAllActiveServices() {
		List<TblService> services = new ArrayList<TblService>();

		String query = "SELECT * FROM tblService WHERE isActive = 1;";

		try {
			Statement stmt = DBUtility.getConnection().createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				TblService service = new TblService();
				service.setFixedCost(rs.getDouble("fixed_cost"));
				service.setFixedIncome(rs.getDouble("fixed_income"));
				service.setImpact(rs.getString("impact"));
				service.setActive(rs.getBoolean("isActive"));
				service.setIsTechnical(rs.getBoolean("isTechnical"));
				service.setServiceId(rs.getByte("service_id"));
				service.setServiceCode(rs.getString("service_code"));
				service.setServiceName(rs.getString("service_name"));
				service.setSupplierLevel2(rs.getString("supplier_level2"));
				service.setSupplierLevel3(rs.getString("supplier_level3"));
				service.setUrgency(rs.getString("urgency"));
				service.setEventId(rs.getInt("event_id"));
				services.add(service);
			}
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		return services;
	}

	@Override
	public TblService getServiceById(byte id) {
		TblService service = null;
		String query = "SELECT * FROM tblService WHERE service_id = ?";

		try {
			pStmt = DBUtility.getConnection().prepareStatement(query);
			pStmt.setByte(1, id);
			ResultSet rs = pStmt.executeQuery();

			if (rs.next()) {
				service = new TblService();
				service.setFixedCost(rs.getDouble("fixed_cost"));
				service.setFixedIncome(rs.getDouble("fixed_income"));
				service.setImpact(rs.getString("impact"));
				service.setActive(rs.getBoolean("isActive"));
				service.setIsTechnical(rs.getBoolean("isTechnical"));
				service.setServiceId(rs.getByte("service_id"));
				service.setServiceCode(rs.getString("service_code"));
				service.setServiceName(rs.getString("service_name"));
				service.setSupplierLevel2(rs.getString("supplier_level2"));
				service.setSupplierLevel3(rs.getString("supplier_level3"));
				service.setUrgency(rs.getString("urgency"));
				service.setEventId(rs.getInt("event_id"));
			}
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		return service;
	}

	@Override
	public int getServiceCount() {
		int count = 0;
		try {
			Statement stmt = DBUtility.getConnection().createStatement();
			ResultSet rs = stmt
					.executeQuery("SELECT COUNT(*) AS COUNT FROM tblService;");
			while (rs.next()) {
				count = rs.getInt("COUNT");
			}
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		return count;
	}
	
	public int getActiveServiceCount() {
		int count = 0;
		try {
			Statement stmt = DBUtility.getConnection().createStatement();
			ResultSet rs = stmt
					.executeQuery("SELECT COUNT(*) AS COUNT FROM tblService where isActive = 1;");
			while (rs.next()) {
				count = rs.getInt("COUNT");
			}
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		return count;
	}
}
