package com.dao;

import java.util.List;

import com.model.TblCMDB;
import com.model.TblCMDBPK;

public interface TblCMDBDao {
	
	public void addCMDB(TblCMDB cmdb);
	public void deleteCMDB(TblCMDBPK pk);
	public void updateCMDB(TblCMDB cmdb);
	public List<TblCMDB> getAllCMDBs(int startPageIndex, int recordsPerPage);
	public List<TblCMDB> getAllCMDBs();
	public int getCMDBCount();
}
