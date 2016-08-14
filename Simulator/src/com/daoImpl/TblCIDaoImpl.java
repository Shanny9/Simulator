package com.daoImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.dao.TblCIDao;
import com.jdbc.DBUtility;
import com.model.TblCI;

public class TblCIDaoImpl implements TblCIDao {

	private Connection dbConnection;
	private PreparedStatement pStmt;

	public TblCIDaoImpl() {
		dbConnection = DBUtility.getConnection();
	}

	@Override
	public void addCI(TblCI ci) {
		String insertQuery = "INSERT INTO tblCI(CI_ID, CI_name, "
				+ "supplier_level2, supplier_level3, isActive) VALUES (?,?,?,?,?)";
		try {
			pStmt = dbConnection.prepareStatement(insertQuery);
			pStmt.setByte(1, ci.getCiId());
			pStmt.setString(2, ci.getCI_name());
			pStmt.setString(3, ci.getSupplierName1());
			pStmt.setString(4, ci.getSupplierName2());
			pStmt.setBoolean(5, ci.getIsActive());
			pStmt.executeUpdate();
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
	}

	@Override
	public void deleteCI(byte ci_id) {
		String deleteQuery = "DELETE FROM tblCI WHERE CI_ID = ?";
		try {
			pStmt = dbConnection.prepareStatement(deleteQuery);
			pStmt.setByte(1, ci_id);
			pStmt.executeUpdate();
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}

	}

	@Override
	public void updateCI(TblCI ci, byte ciId) {
		String updateQuery = "UPDATE tblCI SET CI_ID = ?, CI_name = ?, supplier_level2 = ?, supplier_level3 = ?, isActive = ? WHERE CI_ID = ?";
		try {
			pStmt = dbConnection.prepareStatement(updateQuery);
			pStmt.setByte(1, ci.getCiId());
			pStmt.setString(2, ci.getCI_name());
			pStmt.setString(3, ci.getSupplierName1());
			pStmt.setString(4, ci.getSupplierName2());
			pStmt.setBoolean(5, ci.getIsActive());
			pStmt.setByte(6, ciId);
			pStmt.executeUpdate();

		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
	}

	@Override
	public List<TblCI> getAllCIs(int startPageIndex, int recordsPerPage) {

		List<TblCI> cis = new ArrayList<TblCI>();

		String query = "SELECT * FROM tblCI limit " + startPageIndex + "," + recordsPerPage;

		try {
			Statement stmt = dbConnection.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				TblCI ci = new TblCI();

				ci.setCiId(rs.getByte("CI_ID"));
				ci.setCI_name(rs.getString("CI_name"));
				ci.setSupplierName1(rs.getString("supplier_level2"));
				ci.setSupplierName2(rs.getString("supplier_level3"));
				ci.setIsActive(rs.getBoolean("isActive"));
				cis.add(ci);
			}
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		return cis;
	}

	@Override
	public List<TblCI> getAllCIs() {
		List<TblCI> cis = new ArrayList<TblCI>();

		String query = "SELECT * FROM SIMULATOR.tblCI";

		try {
			Statement stmt = dbConnection.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				TblCI ci = new TblCI();

				ci.setCiId(rs.getByte("CI_ID"));
				ci.setCI_name(rs.getString("CI_name"));
				ci.setSupplierName1(rs.getString("supplier_level2"));
				ci.setSupplierName2(rs.getString("supplier_level3"));
				ci.setIsActive(rs.getBoolean("isActive"));
				cis.add(ci);
			}
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		return cis;
	}

	@Override
	public TblCI getCIById(byte ci_id) {

		TblCI ci = null;
		String query = "SELECT * FROM tblCI WHERE CI_ID = ?";

		try {
			pStmt = dbConnection.prepareStatement(query);
			pStmt.setByte(1, ci_id);
			ResultSet rs = pStmt.executeQuery();
			rs.next();
			ci = new TblCI();
			ci.setCI_name(rs.getString("CI_name"));

			ci.setSupplierName1(rs.getString("supplier_level2"));
			ci.setSupplierName2(rs.getString("supplier_level3"));
			ci.setIsActive(rs.getBoolean("isActive"));

		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		return ci;
	}

	@Override
	public int getCICount() {
		int count = 0;
		try {
			Statement stmt = dbConnection.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS COUNT FROM SIMULATOR.tblCI;");
			while (rs.next()) {
				count = rs.getInt("COUNT");
			}
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		return count;
	}
}
