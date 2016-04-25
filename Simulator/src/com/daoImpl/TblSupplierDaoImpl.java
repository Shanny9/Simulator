package com.daoImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.dao.TblSupplierDao;
import com.jdbc.DBUtility;
import com.model.TblSupplier;

public class TblSupplierDaoImpl implements TblSupplierDao {

	private Connection dbConnection;
	private PreparedStatement pStmt;

	public TblSupplierDaoImpl() {
		dbConnection = DBUtility.getConnection();
	}

	@Override
	public void addSupplier(TblSupplier supplier) {
		String insertQuery = "INSERT INTO tblSupplier(supplier_name, solution_cost, " + "isActive) VALUES (?,?,?)";
		try {
			pStmt = dbConnection.prepareStatement(insertQuery);
			pStmt.setString(1, supplier.getSupplierName());
			pStmt.setDouble(2, supplier.getSolutionCost());
			pStmt.setByte(3, supplier.getIsActive());
			pStmt.executeUpdate();
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
	}

	@Override
	public void deleteSupplier(String name) {
		String deleteQuery = "DELETE FROM tblSupplier WHERE supplier_name = ?";
		try {
			pStmt = dbConnection.prepareStatement(deleteQuery);
			pStmt.setString(1, name);
			pStmt.executeUpdate();
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
	}

	@Override
	public void updateSupplier(TblSupplier supplier) {
		String updateQuery = "UPDATE tblSupplier SET \n " + "solution_cost = ?, isActive = ? WHERE supplier_name = ?";
		try {
			pStmt = dbConnection.prepareStatement(updateQuery);
			pStmt.setDouble(1, supplier.getSolutionCost());
			pStmt.setByte(2, supplier.getIsActive());
			pStmt.setString(3, supplier.getSupplierName());
			pStmt.executeUpdate();

		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
	}

	@Override
	public List<TblSupplier> getAllSuppliers(int startPageIndex, int recordsPerPage) {
		List<TblSupplier> suppliers = new ArrayList<TblSupplier>();

		String query = "SELECT * FROM tblSupplier ORDER BY supplier_name\n" + "limit " + startPageIndex + ","
				+ recordsPerPage;

		try {
			Statement stmt = dbConnection.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				TblSupplier supplier = new TblSupplier();

				supplier.setSupplierName(rs.getString("supplier_name"));
				supplier.setSolutionCost(rs.getDouble("solution_cost"));
				supplier.setIsActive(rs.getByte("isActive"));
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
			Statement stmt = dbConnection.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				TblSupplier supplier = new TblSupplier();

				supplier.setSupplierName(rs.getString("supplier_name"));
				supplier.setSolutionCost(rs.getDouble("solution_cost"));
				supplier.setIsActive(rs.getByte("isActive"));
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
			pStmt = dbConnection.prepareStatement(query);
			pStmt.setString(1, name);
			ResultSet rs = pStmt.executeQuery();
			rs.next();
			supplier = new TblSupplier();
			supplier.setSupplierName(rs.getString("supplier_name"));
			supplier.setSolutionCost(rs.getDouble("solution_cost"));
			supplier.setIsActive(rs.getByte("isActive"));
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
			Statement stmt = dbConnection.createStatement();
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