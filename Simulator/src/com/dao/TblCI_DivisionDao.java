package com.dao;

import java.util.List;

import com.model.TblCI_Division;

public interface TblCI_DivisionDao {
	
	public void addCI_Division(TblCI_Division CI_Division);
	public void deleteCI_Division(String name);
	public void updateCI_Division(TblCI_Division CI_Division);
	public List<TblCI_Division> getAllCI_Divisions(int startPageIndex, int recordsPerPage);
	public List<TblCI_Division> getAllCI_Divisions();
	public TblCI_Division getCI_DivisionById(String CI_DivisionName);
	public int getCourseCount();
}
