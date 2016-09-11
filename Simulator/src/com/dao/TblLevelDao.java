package com.dao;

import java.sql.SQLException;
import java.util.List;

import com.model.TblChange;
import com.model.TblLevel;

public interface TblLevelDao {
	
	public void addLevel(TblLevel Level);
	public void deleteLevel(String name);
	public void updateLevel(TblLevel Level);
	public List<TblLevel> getAllLevels(int startPageIndex, int recordsPerPage);
	public List<TblLevel> getAllLevels() throws SQLException;
	public TblLevel getLevelById(String Level);
	public int getLevelCount();
}
