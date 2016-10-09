package com.dao;

import java.sql.SQLException;
import java.util.List;

import com.model.TblLevel;

public interface TblLevelDao {
	
	public void addLevel(TblLevel Level) throws SQLException;
	public void deleteLevel(String name) throws SQLException;
	public List<TblLevel> getAllLevels(int startPageIndex, int recordsPerPage);
	public List<TblLevel> getAllLevels();
	public List<TblLevel> getAllActiveLevels();
	public TblLevel getLevelById(String Level);
	public int getLevelCount();
	void updateLevel(TblLevel Level, String name) throws SQLException;
}
