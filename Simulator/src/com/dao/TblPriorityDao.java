package com.dao;

import java.util.List;

import com.model.TblPriority;
import com.model.TblPriorityPK;

public interface TblPriorityDao {
	
	public void addPriority(TblPriority priority);
	public void deletePriority(TblPriorityPK id);
	public void updatePriority(TblPriority priority);
	public List<TblPriority> getAllPriorities(int startPageIndex, int recordsPerPage);
	public List<TblPriority> getAllPriorities();
	public List<TblPriority> getAllActivePriorities();
	public TblPriority getPriorityById(String name);
	public int getPriorityCount();
}
