package com.daoImpl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.dao.TblLevelDao;
import com.jdbc.DBUtility;
import com.model.TblLevel;

public class TblLevelDaoImpl implements TblLevelDao {
	
	private Connection dbConnection;
	
	public TblLevelDaoImpl() {
		dbConnection = DBUtility.getConnection();
	}

	@Override
	public void addLevel(TblLevel Level) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteLevel(String name) {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateLevel(TblLevel Level) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<TblLevel> getAllLevels(int startPageIndex, int recordsPerPage) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TblLevel> getAllLevels() {
		List<TblLevel> levels = new ArrayList<TblLevel>();

		String query = "SELECT * FROM tblLevel";

		try {
			Statement stmt = dbConnection.createStatement();
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
			Statement stmt = dbConnection.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				TblLevel level = new TblLevel();
				level.setLevel(rs.getString("level"));
				//TODO: LEVEL SET IS-ACTIVE!
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
		// TODO Auto-generated method stub
		return 0;
	}

}
