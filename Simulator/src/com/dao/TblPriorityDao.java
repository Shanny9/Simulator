package com.dao;

import java.util.List;

import com.model.TblPriority;

public interface TblPriorityDao {
	
	public void addPriority(TblPriority priority);
	public void deletePriority(byte id);
	public void updatePriority(TblPriority priority);
	public List<TblPriority> getAllPriorities(int startPageIndex, int recordsPerPage);
	public List<TblPriority> getAllPriorities();
	public int getPriorityCount();
}
