package com.dao;

import java.util.List;

import com.model.TblResource;

public interface TblResourceDao {
	
	public void addResource(TblResource Resource);
	public void delete(String name);
	public void updateResource(TblResource Resource);
	public List<TblResource> getAllResources(int startPageIndex, int recordsPerPage);
	public List<TblResource> getAllResources();
	public TblResource getResourceById(String ResourceName);
	public int getCourseCount();
}
