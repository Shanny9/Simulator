package com.dao;

import java.util.List;

import com.model.TblResource_Change;

public interface TblResource_ChangeDao {
	
	public void addResource_Change(TblResource_Change Resource_Change);
	public void delete(String name);
	public void updateResource_Change(TblResource_Change Resource_Change);
	public List<TblResource_Change> getAllResource_Changes(int startPageIndex, int recordsPerPage);
	public List<TblResource_Change> getAllResource_Changes();
	public TblResource_Change getResource_ChangeById(String Resource_ChangeName);
	public int getCourseCount();
}
