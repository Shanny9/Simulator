package com.daoImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

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
	public void addCMDB(TblCMDB cmdb) {
		String insertQuery = "INSERT INTO tblCMDB(ci_id, service_id, isActive) VALUES (?,?,?)";
		try {
			pStmt = dbConnection.prepareStatement(insertQuery);
			pStmt.setByte(1, cmdb.getId().getCiId());
			pStmt.setByte(2, cmdb.getId().getServiceId());
			pStmt.setBoolean(3, cmdb.getIsActive());
			pStmt.executeUpdate();
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}

	}

	@Override
	public void deleteCMDB(TblCMDBPK pk) {
		String deleteQuery = "DELETE FROM tblCMDB WHERE ci_id = ? and service_id = ?";
		try {
			pStmt = dbConnection.prepareStatement(deleteQuery);
			pStmt.setByte(1, pk.getCiId());
			pStmt.setByte(2, pk.getServiceId());
			pStmt.executeUpdate();
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}

	}

	@Override
	public void updateCMDB(TblCMDB cmdb) {
		String updateQuery = "UPDATE tblCMDB SET isActive = ? WHERE ci_id = ? and service_id = ?";
		try {
			pStmt = dbConnection.prepareStatement(updateQuery);
			pStmt.setBoolean(1, cmdb.getIsActive());
			pStmt.setByte(2, cmdb.getId().getCiId());
			pStmt.setByte(3, cmdb.getId().getServiceId());
			pStmt.executeUpdate();

		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}

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
				TblCMDBPK pk = new TblCMDBPK();
				pk.setCiId(rs.getByte("ci_id"));
				pk.setServiceId(rs.getByte("service_id"));
				cmdb.setId(pk);
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
				TblCMDBPK pk = new TblCMDBPK();
				pk.setCiId(rs.getByte("ci_id"));
				pk.setServiceId(rs.getByte("service_id"));
				cmdb.setId(pk);
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
