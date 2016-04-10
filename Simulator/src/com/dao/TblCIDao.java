package com.dao;

import java.util.List;

import com.model.TblCI;

public interface TblCIDao {
	
	public void addCI(TblCI ci);
	public void deleteCI(String name);
	public void updateCI(TblCI ci);
	public List<TblCI> getAllCIs(int startPageIndex, int recordsPerPage);
	public List<TblCI> getAllCIs();
	public TblCI getCIById(String ciName);
	public int getCourseCount();
}
