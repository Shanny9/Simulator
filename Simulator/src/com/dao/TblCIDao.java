package com.dao;

import java.sql.SQLException;
import java.util.List;

import com.model.TblCI;

public interface TblCIDao {
	
	public void addCI(TblCI ci) throws SQLException;
	public void deleteCI(byte ci_id) throws SQLException;
	public void updateCI(TblCI ci, byte ciId) throws SQLException;
	public List<TblCI> getAllCIs(int startPageIndex, int recordsPerPage);
	public List<TblCI> getAllCIs();
	public TblCI getCIById(byte ci_id);
	public int getCICount();
}
