package com.dao;

import java.util.List;

import com.model.TblDivision;

public interface TblDivisionDao {
	
	public void addDivision(TblDivision Division);
	public void deleteDivision(String name);
	public void updateDivision(TblDivision Division);
	public List<TblDivision> getAllDivisions(int startPageIndex, int recordsPerPage);
	public List<TblDivision> getAllDivisions();
	public TblDivision getDivisionById(String DivisionName);
	public int getDivisionCount();
}
