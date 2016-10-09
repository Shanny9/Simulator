package com.daoImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import utils.QueryLogger;

import com.dao.TblCMDBDao;
import com.jdbc.DBUtility;
import com.model.TblCMDB;
import com.model.TblCMDBPK;

public class TblCMDBDaoImpl implements TblCMDBDao {

	private Connection dbConnection;
	private PreparedStatement pStmt;

	public TblCMDBDaoImpl() {
		dbConnection = DBUtility.getConnection();
	}

	@Override
	public void addCMDB(TblCMDB cmdb) throws SQLException {
		String insertQuery = "INSERT INTO tblCMDB(ci_id, service_id, isActive) VALUES (?,?,?)";

			pStmt = dbConnection.prepareStatement(insertQuery);
			pStmt.setByte(1, cmdb.getCiId());
			pStmt.setByte(2, cmdb.getServiceId());
			pStmt.setBoolean(3, cmdb.isActive());
			pStmt.executeUpdate();
			QueryLogger.log(pStmt.toString());
	}

	@Override
	public void deleteCMDB(TblCMDBPK pk) throws SQLException {
		String deleteQuery = "DELETE FROM tblCMDB WHERE ci_id = ? and service_id = ?";

			pStmt = dbConnection.prepareStatement(deleteQuery);
			pStmt.setByte(1, pk.getCiId());
			pStmt.setByte(2, pk.getServiceId());
			pStmt.executeUpdate();
			QueryLogger.log(pStmt.toString());
	}

	@Override
	public void updateCMDB(TblCMDB cmdb, TblCMDBPK id) throws SQLException {
		String updateQuery = "UPDATE tblCMDB SET ci_id = ? , service_id = ? , isActive = ? WHERE ci_id = ? and service_id = ?";

			pStmt = dbConnection.prepareStatement(updateQuery);
			pStmt.setByte(1, cmdb.getCiId());
			pStmt.setByte(2, cmdb.getServiceId());
			pStmt.setBoolean(3, cmdb.isActive());
			pStmt.setByte(4, id.getCiId());
			pStmt.setByte(5, id.getServiceId());
			pStmt.executeUpdate();
			QueryLogger.log(pStmt.toString());
	}

	@Override
	public List<TblCMDB> getAllCMDBs(int startPageIndex, int recordsPerPage) {
		List<TblCMDB> ci_services = new ArrayList<>();

		String query = "SELECT * FROM tblCMDB limit " + startPageIndex + "," + recordsPerPage;

		try {
			Statement stmt = dbConnection.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				TblCMDB cmdb = new TblCMDB();
				cmdb.setCiId(rs.getByte("ci_id"));
				cmdb.setServiceId(rs.getByte("service_id"));
				cmdb.setActive(rs.getBoolean("isActive"));
				ci_services.add(cmdb);
			}
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		return ci_services;
	}

	@Override
	public List<TblCMDB> getAllCMDBs() {
		List<TblCMDB> ci_services = new ArrayList<>();

		String query = "SELECT * FROM tblCMDB";

		try {
			Statement stmt = dbConnection.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				TblCMDB cmdb = new TblCMDB();
				cmdb.setCiId(rs.getByte("ci_id"));
				cmdb.setServiceId(rs.getByte("service_id"));
				cmdb.setActive(rs.getBoolean("isActive"));
				ci_services.add(cmdb);
			}
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		return ci_services;
	}
	
	@Override
	public List<TblCMDB> getAllActiveCMDBs() {
		List<TblCMDB> ci_services = new ArrayList<>();

		String query = "SELECT * FROM tblCMDB WHERE isActive = 1;";

		try {
			Statement stmt = dbConnection.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				TblCMDB cmdb = new TblCMDB();
				cmdb.setCiId(rs.getByte("ci_id"));
				cmdb.setServiceId(rs.getByte("service_id"));
				cmdb.setActive(rs.getBoolean("isActive"));
				ci_services.add(cmdb);
			}
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		return ci_services;
	}

	@Override
	public int getCMDBCount() {
		int count = 0;
		try {
			Statement stmt = dbConnection.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS COUNT FROM SIMULATOR.tblCMDB;");
			while (rs.next()) {
				count = rs.getInt("COUNT");
			}
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		return count;
	}

}
