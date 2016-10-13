package com.dao;

import java.sql.SQLException;
import java.util.List;

import com.model.TblPriority;
import com.model.TblPriorityPK;

public interface TblPriorityDao {
	
	public void addPriority(TblPriority priority) throws SQLException;
	public void deletePriority(TblPriorityPK id) throws SQLException;
	public void updatePriority(TblPriority priority, TblPriorityPK id) throws SQLException;
	public List<TblPriority> getAllPriorities(int startPageIndex, int recordsPerPage);
	public List<TblPriority> getAllPrioritiesDistinct();
	public List<TblPriority> getAllPriorities();
	public List<TblPriority> getAllActivePriorities();
	public TblPriority getPriorityById(String name);
	public int getPriorityCount();
}
