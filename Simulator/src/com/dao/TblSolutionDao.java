package com.dao;

import java.sql.SQLException;
import java.util.List;

import com.model.TblSolution;

public interface TblSolutionDao {
	
	public void addSolution(TblSolution sol) throws SQLException;
	public void deleteSolution(int id) throws SQLException;
	public void updateSolution(TblSolution sol, int id) throws SQLException;
	public List<TblSolution> getAllSolutions(int startPageIndex, int recordsPerPage);
	public List<TblSolution> getAllSolutions();
	public TblSolution getSolutionById(int id);
	public int getSolutionCount();
}
