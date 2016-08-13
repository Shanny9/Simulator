package com.dao;

import java.util.List;

import com.model.TblCI;

public interface TblCIDao {
	
	public void addCI(TblCI ci);
	public void deleteCI(byte ci_id);
	public void updateCI(TblCI ci, byte ciId);
	public List<TblCI> getAllCIs(int startPageIndex, int recordsPerPage);
	public List<TblCI> getAllCIs();
	public TblCI getCIById(byte ci_id);
	public int getCICount();
}
