package com.dao;

import java.util.List;

import com.model.TblCMDB;

public interface TblCMDBDao {
	
	public void addCMDB(TblCMDB cmdb);
	public void deleteCMDB(int ci_id, int service_id);
	public void updateCMDB(int ci_id, int service_id);
	public List<TblCMDB> getAllCMDBs(int startPageIndex, int recordsPerPage);
	public List<TblCMDB> getAllCMDBs();
	public int getCMDBCount();
}
