package com.dao;

import java.util.List;

import com.model.TblChange;

public interface TblChangeDao {
	
	public void addChange(TblChange Change);
	public void deleteChange(String name);
	public void updateChange(TblChange Change);
	public List<TblChange> getAllChanges(int startPageIndex, int recordsPerPage);
	public List<TblChange> getAllChanges();
	public TblChange getChangeById(String ChangeName);
	public int getCourseCount();
}
