package com.dao;

import java.sql.SQLException;
import java.util.List;

import com.model.TblCMDB;
import com.model.TblCMDBPK;

public interface TblCMDBDao {
	
	public void addCMDB(TblCMDB cmdb) throws SQLException;
	public void deleteCMDB(TblCMDBPK pk) throws SQLException;
	public void updateCMDB(TblCMDB cmdb, TblCMDBPK id) throws SQLException;
	public List<TblCMDB> getAllCMDBs(int startPageIndex, int recordsPerPage);
	public List<TblCMDB> getAllCMDBs();
	public List<TblCMDB> getAllActiveCMDBs();
	public int getCMDBCount();
}
