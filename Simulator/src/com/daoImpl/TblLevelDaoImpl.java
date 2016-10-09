package com.daoImpl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.dao.TblLevelDao;
import com.jdbc.DBUtility;
import com.model.TblLevel;

public class TblLevelDaoImpl implements TblLevelDao {
	
	private PreparedStatement pStmt;
	
	public TblLevelDaoImpl() {

	}

	@Override
	public void addLevel(TblLevel Level) throws SQLException {
		String insertQuery = "INSERT INTO `SIMULATOR`.`tblLevel`\r\n" + 
				"(`level`,\r\n" + 
				"`isActive`)\r\n" + 
				" VALUES (?,?);";

				pStmt = DBUtility.getConnection().prepareStatement(insertQuery);
				pStmt.setString(1, Level.getLevel());
				pStmt.setBoolean(2, Level.isActive());
				pStmt.executeUpdate();

	}

	@Override
	public void deleteLevel(String name) throws SQLException {
		String deleteQuery = "DELETE FROM tblLevel WHERE level = ?";

		pStmt = DBUtility.getConnection().prepareStatement(deleteQuery);
		pStmt.setString(1, name);
		pStmt.executeUpdate();

	}

	@Override
	public void updateLevel(TblLevel Level, String name) throws SQLException {
		String updateQuery = "UPDATE `SIMULATOR`.`tblLevel`\r\n" + 
				"SET\r\n" + 
				"`level` = ?,\r\n" + 
				"`isActive` = ?\r\n" + 
				"WHERE `level` = ?;";

			pStmt = DBUtility.getConnection().prepareStatement(updateQuery);
			pStmt.setString(1, Level.getLevel());
			pStmt.setBoolean(2, Level.isActive());
			pStmt.setString(3, name);

			pStmt.executeUpdate();

	}

	@Override
	public List<TblLevel> getAllLevels(int startPageIndex, int recordsPerPage) {
		List<TblLevel> levels = new ArrayList<TblLevel>();

		String query = "SELECT * FROM tblLevel "+ "limit " + startPageIndex + ","
				+ recordsPerPage;

		try {
			Statement stmt = DBUtility.getConnection().createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				TblLevel level = new TblLevel();
				level.setLevel(rs.getString("level"));
				level.setActive(rs.getBoolean("isActive"));
				levels.add(level);
			}
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		return levels;
	}

	@Override
	public List<TblLevel> getAllLevels() {
		List<TblLevel> levels = new ArrayList<TblLevel>();

		String query = "SELECT * FROM tblLevel";

		try {
			Statement stmt = DBUtility.getConnection().createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				TblLevel level = new TblLevel();
				level.setLevel(rs.getString("level"));
				level.setActive(rs.getBoolean("isActive"));
				levels.add(level);
			}
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		return levels;
	}
	
	@Override
	public List<TblLevel> getAllActiveLevels() {
		List<TblLevel> levels = new ArrayList<TblLevel>();

		String query = "SELECT * FROM tblLevel WHERE isActive = 1;";

		try {
			Statement stmt = DBUtility.getConnection().createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				TblLevel level = new TblLevel();
				level.setLevel(rs.getString("level"));
				level.setActive(rs.getBoolean("isActive"));
				levels.add(level);
			}
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		return levels;
	}

	@Override
	public TblLevel getLevelById(String Level) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getLevelCount() {
		int count = 0;
		try {
			Statement stmt = DBUtility.getConnection().createStatement();
			ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS COUNT FROM SIMULATOR.tblLevel;");
			while (rs.next()) {
				count = rs.getInt("COUNT");
			}
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		return count;
	}

}
