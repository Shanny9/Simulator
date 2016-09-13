package com.dao;

import java.sql.SQLException;
import java.util.List;

import com.model.TblPriority_Cost;

public interface TblPriorityCostDao {
	
	public void addPriorityCost(TblPriority_Cost priority) throws SQLException;
	public void deletePriorityCost(String id) throws SQLException;
	public void updatePriorityCost(TblPriority_Cost priority, String name) throws SQLException;
	public List<TblPriority_Cost> getAllPriorityCost(int startPageIndex, int recordsPerPage);
	public List<TblPriority_Cost> getAllPriorityCost();
	public TblPriority_Cost getPriorityCostById(String name);
	public int getPriorityCostCount();
}
