package com.daoImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.dao.TblSolutionDao;
import com.jdbc.DBUtility;
import com.model.TblSolution;

public class TblSolutionDaoImpl implements TblSolutionDao {

	private Connection dbConnection;
	private PreparedStatement pStmt;
	
	public TblSolutionDaoImpl() {
		dbConnection = DBUtility.getConnection();
	}
	
	@Override
	public void addSolution(TblSolution sol) {

		String insertQuery = "INSERT INTO tblSolution (solution_id, solution_marom, " 
	+ "solution_rakia, isActive) VALUES (?,?,?,?)";
		try {
			pStmt = dbConnection.prepareStatement(insertQuery);
			pStmt.setInt(1, sol.getSolutionId());
			pStmt.setInt(2, sol.getSolutionMarom());
			pStmt.setInt(3, sol.getSolutionRakia());
			pStmt.setBoolean(4, sol.isActive());

			pStmt.executeUpdate();
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		
	}

	@Override
	public void deleteSolution(int id) {

		String deleteQuery = "DELETE FROM tblSolution WHERE solution_id = ? ";
		
		try {
			pStmt = dbConnection.prepareStatement(deleteQuery);
			pStmt.setInt(1, id);

			pStmt.executeUpdate();
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}

	}

	@Override
	public void updateSolution(TblSolution sol, int id) {

		String updateQuery = "UPDATE tblSolution SET \n " + "solution_id=?, solution_marom=?, \r\n" + 
				"solution_rakia=?, isActive=? WHERE solution_id = ?";
		try {
			pStmt = dbConnection.prepareStatement(updateQuery);
			pStmt.setInt(1, sol.getSolutionId());
			pStmt.setInt(2, sol.getSolutionMarom());
			pStmt.setInt(3, sol.getSolutionRakia());
			pStmt.setBoolean(4, sol.isActive());
			
			pStmt.setInt(5, id);

			pStmt.executeUpdate();

		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}

	}

	@Override
	public List<TblSolution> getAllSolutions(int startPageIndex,
			int recordsPerPage) {
		List<TblSolution> solutions = new ArrayList<TblSolution>();

		String query = "SELECT * FROM tblSolution "+ " limit " + startPageIndex + ","
				+ recordsPerPage;

		try {
			Statement stmt = dbConnection.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				TblSolution sol = new TblSolution();
				
				sol.setSolutionId(rs.getInt("solution_id"));
				sol.setSolutionMarom(rs.getInt("solution_marom"));
				sol.setSolutionRakia(rs.getInt("solution_rakia"));
				sol.setActive(rs.getBoolean("isActive"));


				solutions.add(sol);
			}
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		return solutions;
	}

	@Override
	public List<TblSolution> getAllSolutions() {
		List<TblSolution> solutions = new ArrayList<TblSolution>();

		String query = "SELECT * FROM tblSolution ";

		try {
			Statement stmt = dbConnection.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				TblSolution sol = new TblSolution();
				
				sol.setSolutionId(rs.getInt("solution_id"));
				sol.setSolutionMarom(rs.getInt("solution_marom"));
				sol.setSolutionRakia(rs.getInt("solution_rakia"));
				sol.setActive(rs.getBoolean("isActive"));


				solutions.add(sol);
			}
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		return solutions;
	}

	@Override
	public TblSolution getSolutionById(int id) {
		TblSolution sol = null;
		String query = "SELECT * FROM tblSolution WHERE solution_id = ?";

		try {
			pStmt = dbConnection.prepareStatement(query);
			pStmt.setInt(1, id);
			
			ResultSet rs = pStmt.executeQuery();
			rs.next();
			sol = new TblSolution();
			
			sol.setSolutionId(rs.getInt("solution_id"));
			sol.setSolutionMarom(rs.getInt("solution_marom"));
			sol.setSolutionRakia(rs.getInt("solution_rakia"));
			sol.setActive(rs.getBoolean("isActive"));


		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		return sol;
	}

	@Override
	public int getSolutionCount() {
		int count = 0;
		try {
			Statement stmt = dbConnection.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS COUNT FROM SIMULATOR.tblSolution;");
			while (rs.next()) {
				count = rs.getInt("COUNT");
			}
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		return count;
	
	}

}
