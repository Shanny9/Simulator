package com.daoImpl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import utils.QueryLogger;

import com.dao.TblSupplierDao;
import com.jdbc.DBUtility;
import com.model.TblSupplier;

public class TblSupplierDaoImpl implements TblSupplierDao {


	private PreparedStatement pStmt;

	public TblSupplierDaoImpl() {

	}

	@Override
	public void addSupplier(TblSupplier supplier) throws SQLException {
		String insertQuery = "INSERT INTO tblSupplier(supplier_name, solution_cost, " + "isActive, currency) VALUES (?,?,?,?)";

			pStmt = DBUtility.getConnection().prepareStatement(insertQuery);
			pStmt.setString(1, supplier.getSupplierName());
			pStmt.setDouble(2, supplier.getSolutionCost());
			pStmt.setBoolean(3, supplier.isActive());
			pStmt.setString(4, supplier.getCurrency());
			pStmt.executeUpdate();
			QueryLogger.log(pStmt.toString());
	}

	@Override
	public void deleteSupplier(String name) throws SQLException {
		String deleteQuery = "DELETE FROM tblSupplier WHERE supplier_name = ?";

			pStmt = DBUtility.getConnection().prepareStatement(deleteQuery);
			pStmt.setString(1, name);
			pStmt.executeUpdate();
			QueryLogger.log(pStmt.toString());
	}

	@Override
	public void updateSupplier(TblSupplier supplier, String name) throws SQLException {
		String updateQuery = "UPDATE tblSupplier SET \n " + "supplier_name = ?, solution_cost = ?, isActive = ?, currency = ? WHERE supplier_name = ?";

			pStmt = DBUtility.getConnection().prepareStatement(updateQuery);
			pStmt.setString(1, supplier.getSupplierName());
			pStmt.setDouble(2, supplier.getSolutionCost());
			pStmt.setBoolean(3, supplier.isActive());
			pStmt.setString(4, supplier.getCurrency());
			pStmt.setString(5, name);
			pStmt.executeUpdate();
			QueryLogger.log(pStmt.toString());
	}

	@Override
	public List<TblSupplier> getAllSuppliers(int startPageIndex, int recordsPerPage) {
		List<TblSupplier> suppliers = new ArrayList<TblSupplier>();

		String query = "SELECT * FROM tblSupplier ORDER BY supplier_name\n" + "limit " + startPageIndex + ","
				+ recordsPerPage;

		try {
			Statement stmt = DBUtility.getConnection().createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				TblSupplier supplier = new TblSupplier();

				supplier.setSupplierName(rs.getString("supplier_name"));
				supplier.setSolutionCost(rs.getDouble("solution_cost"));
				supplier.setActive(rs.getBoolean("isActive"));
				supplier.setCurrency(rs.getString("currency"));
				suppliers.add(supplier);
			}
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		return suppliers;
	}

	@Override
	public List<TblSupplier> getAllSuppliers() {
		List<TblSupplier> suppliers = new ArrayList<TblSupplier>();

		String query = "SELECT * FROM tblSupplier ORDER BY supplier_name";

		try {
			Statement stmt = DBUtility.getConnection().createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				TblSupplier supplier = new TblSupplier();
				supplier.setSupplierName(rs.getString("supplier_name"));
				supplier.setSolutionCost(rs.getDouble("solution_cost"));
				supplier.setActive(rs.getBoolean("isActive"));
				supplier.setCurrency(rs.getString("currency"));
				suppliers.add(supplier);
			}
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		return suppliers;
	}
	
	@Override
	public List<TblSupplier> getAllActiveSuppliers() {
		List<TblSupplier> suppliers = new ArrayList<TblSupplier>();

		String query = "SELECT * FROM tblSupplier WHERE isActive = 1;";

		try {
			Statement stmt = DBUtility.getConnection().createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				TblSupplier supplier = new TblSupplier();
				supplier.setSupplierName(rs.getString("supplier_name"));
				supplier.setSolutionCost(rs.getDouble("solution_cost"));
				supplier.setActive(rs.getBoolean("isActive"));
				supplier.setCurrency(rs.getString("currency"));
				suppliers.add(supplier);
			}
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		return suppliers;
	}

	@Override
	public TblSupplier getSupplierById(String name) {
		TblSupplier supplier = null;
		String query = "SELECT * FROM tblSupplier WHERE supplier_name = ?";

		try {
			pStmt = DBUtility.getConnection().prepareStatement(query);
			pStmt.setString(1, name);
			ResultSet rs = pStmt.executeQuery();
			rs.next();
			supplier = new TblSupplier();
			supplier.setSupplierName(rs.getString("supplier_name"));
			supplier.setSolutionCost(rs.getDouble("solution_cost"));
			supplier.setActive(rs.getBoolean("isActive"));
			supplier.setCurrency(rs.getString("currency"));

		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		return supplier;
	}

	@Override
	public int getSupplierCount() {
		int count = 0;
		try {
			Statement stmt = DBUtility.getConnection().createStatement();
			ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS COUNT FROM SIMULATOR.tblSupplier;");
			while (rs.next()) {
				count = rs.getInt("COUNT");
			}
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		return count;
	}
}// end
