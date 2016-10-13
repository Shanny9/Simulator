package com.daoImpl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.dao.TblCurrencyDao;
import com.jdbc.DBUtility;
import com.model.TblCurrency;

public class TblCurrencyDaoImpl implements TblCurrencyDao {
	
	private PreparedStatement pStmt;
	
	public TblCurrencyDaoImpl() {

	}


	@Override
	public void addCurrency(TblCurrency cur) throws SQLException {
		String insertQuery = "INSERT INTO `SIMULATOR`.`tblCurrency`\r\n" + 
				"(`currency`,\r\n" + 
				"`value`,\r\n" +
				"`isActive`)\r\n" + 
				" VALUES (?,?,?);";

				pStmt = DBUtility.getConnection().prepareStatement(insertQuery);
				pStmt.setString(1, cur.getCurrency());
				pStmt.setDouble(2, cur.getValue());
				pStmt.setBoolean(3, cur.isActive());
				pStmt.executeUpdate();
		
	}

	@Override
	public void deleteCurrency(String name) throws SQLException {
		String deleteQuery = "DELETE FROM tblCurrency WHERE currency = ?";

		pStmt = DBUtility.getConnection().prepareStatement(deleteQuery);
		pStmt.setString(1, name);
		pStmt.executeUpdate();
		
	}

	@Override
	public List<TblCurrency> getAllCurrencies(int startPageIndex,
			int recordsPerPage) {
		List<TblCurrency> currs = new ArrayList<TblCurrency>();

		String query = "SELECT * FROM tblCurrency "+ "limit " + startPageIndex + ","
				+ recordsPerPage;

		try {
			Statement stmt = DBUtility.getConnection().createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				TblCurrency cur = new TblCurrency();
				cur.setCurrency(rs.getString("currency"));
				cur.setValue(rs.getDouble("value"));
				cur.setActive(rs.getBoolean("isActive"));
				currs.add(cur);
			}
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		return currs;
	}

	@Override
	public List<TblCurrency> getAllCurrencies() {
		List<TblCurrency> currs = new ArrayList<TblCurrency>();

		String query = "SELECT * FROM tblCurrency;";

		try {
			Statement stmt = DBUtility.getConnection().createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				TblCurrency cur = new TblCurrency();
				cur.setCurrency(rs.getString("currency"));
				cur.setValue(rs.getDouble("value"));
				cur.setActive(rs.getBoolean("isActive"));
				currs.add(cur);
			}
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		return currs;
	}

	@Override
	public List<TblCurrency> getAllActiveCurrencies() {
		List<TblCurrency> currs = new ArrayList<TblCurrency>();

		String query = "SELECT * FROM tblCurrency WHERE isActive = 1;";

		try {
			Statement stmt = DBUtility.getConnection().createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				TblCurrency cur = new TblCurrency();
				cur.setCurrency(rs.getString("currency"));
				cur.setValue(rs.getDouble("value"));
				cur.setActive(rs.getBoolean("isActive"));
				currs.add(cur);
			}
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		return currs;
	}

	@Override
	public TblCurrency getCurrencyById(String name) {
		TblCurrency cur = null;
		String query = "SELECT * FROM tblCurrency WHERE currency = ?";

		try {
			pStmt = DBUtility.getConnection().prepareStatement(query);
			pStmt.setString(1, name);

			ResultSet rs = pStmt.executeQuery();
			if (rs.next()) {
				cur = new TblCurrency();
				cur.setCurrency(rs.getString("currency"));
				cur.setValue(rs.getDouble("value"));
				cur.setActive(rs.getBoolean("isActive"));
			}
			
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		return cur;
	}

	@Override
	public int getCurrencyCount() {
		int count = 0;
		try {
			Statement stmt = DBUtility.getConnection().createStatement();
			ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS COUNT FROM SIMULATOR.tblCurrency;");
			while (rs.next()) {
				count = rs.getInt("COUNT");
			}
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		return count;
	}

	@Override
	public void updateCurrency(TblCurrency cur, String name)
			throws SQLException {
		String updateQuery = "UPDATE `SIMULATOR`.`tblCurrency`\r\n" + 
				"SET\r\n" + 
				"`currency` = ?,\r\n" + 
				"`value` = ?,\r\n" + 
				"`isActive` = ?\r\n" + 
				"WHERE `currency` = ?;";

			pStmt = DBUtility.getConnection().prepareStatement(updateQuery);
			pStmt.setString(1, cur.getCurrency());
			pStmt.setDouble(2, cur.getValue());
			pStmt.setBoolean(3, cur.isActive());
			pStmt.setString(4, name);

			pStmt.executeUpdate();
		
	}

}
