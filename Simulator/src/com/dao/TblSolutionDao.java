package com.dao;

import java.util.List;
import com.model.TblSolution;

public interface TblSolutionDao {
	
	public void addSolution(TblSolution sol);
	public void deleteSolution(int id);
	public void updateSolution(TblSolution sol, int id);
	public List<TblSolution> getAllSolutions(int startPageIndex, int recordsPerPage);
	public List<TblSolution> getAllSolutions();
	public TblSolution getSolutionById(int id);
	public int getSolutionCount();
}
