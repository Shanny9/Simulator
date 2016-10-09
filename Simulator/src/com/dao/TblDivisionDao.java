package com.dao;

import java.sql.SQLException;
import java.util.List;

import com.model.TblDivision;

public interface TblDivisionDao {
	
	public void addDivision(TblDivision Division) throws SQLException;
	public void deleteDivision(String name) throws SQLException;
	public void updateDivision(TblDivision Division, String divisionName) throws SQLException;
	public List<TblDivision> getAllDivisions(int startPageIndex, int recordsPerPage);
	public List<TblDivision> getAllDivisions();
	public List<TblDivision> getAllActiveDivisions();
	public TblDivision getDivisionById(String DivisionName);
	public int getDivisionCount();
}
