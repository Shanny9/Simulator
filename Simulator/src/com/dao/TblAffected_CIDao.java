package com.dao;

import java.util.List;

import com.model.TblAffected_CI;

public interface TblAffected_CIDao {
	
	public void addAffected_CI(TblAffected_CI Affected_CI);
	public void deleteAffected_CI(String name);
	public void updateAffected_CI(TblAffected_CI Affected_CI);
	public List<TblAffected_CI> getAllAffected_CIs(int startPageIndex, int recordsPerPage);
	public List<TblAffected_CI> getAllAffected_CIs();
	public TblAffected_CI getAffected_CIById(String Affected_CIName);
	public int getCourseCount();
}
