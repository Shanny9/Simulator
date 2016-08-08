package com.dao;

import java.util.List;

import com.model.TblPriority;
import com.model.TblPriority_Cost;

public interface TblPriorityCostDao {
	
	public void addPriorityCost(TblPriority_Cost priority);
	public void deletePriorityCost(String id);
	public void updatePriorityCost(TblPriority_Cost priority, String name);
	public List<TblPriority_Cost> getAllPriorityCost(int startPageIndex, int recordsPerPage);
	public List<TblPriority_Cost> getAllPriorityCost();
	public TblPriority_Cost getPriorityCostById(String name);
	public int getPriorityCostCount();
}
