package com.dao;

import java.util.List;

import com.model.TblResource_CI;

public interface TblResource_CIDao {
	
	public void addResource_CI(TblResource_CI Resource_CI);
	public void delete(String name);
	public void updateResource_CI(TblResource_CI Resource_CI);
	public List<TblResource_CI> getAllResource_CIs(int startPageIndex, int recordsPerPage);
	public List<TblResource_CI> getAllResource_CIs();
	public TblResource_CI getResource_CIById(String Resource_CIName);
	public int getCourseCount();
}
