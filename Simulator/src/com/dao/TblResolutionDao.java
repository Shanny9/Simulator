package com.dao;

import java.util.List;

import com.model.TblResolution;
import com.model.TblResolutionPK;

public interface TblResolutionDao {
	
	public void addResolution(TblResolution resolution);
	public void deleteResolution(TblResolutionPK id);
	public void updateResolution(TblResolution resolution);
	public List<TblResolution> getAllResolutions();
	public List<TblResolution> getAllResolutions(int startPageIndex, int recordsPerPage);
	public int getResoltionCount();
}
