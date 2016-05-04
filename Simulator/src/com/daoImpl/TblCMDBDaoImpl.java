package com.daoImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.dao.TblCMDBDao;
import com.jdbc.DBUtility;
import com.model.TblCMDB;
import com.model.TblCMDBPK;

public class TblCMDBDaoImpl implements TblCMDBDao{
	
	private Connection dbConnection;
	private PreparedStatement pStmt;

	public TblCMDBDaoImpl() {
		dbConnection = DBUtility.getConnection();
	}
	
	@Override
	public void addCMDB(TblCMDB cmdb) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteCMDB(int ci_id, int service_id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateCMDB(int ci_id, int service_id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<TblCMDB> getAllCMDBs(int startPageIndex, int recordsPerPage) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TblCMDB> getAllCMDBs() {
		List<TblCMDB> ci_services = new ArrayList<>();

		String query = "SELECT * FROM tblCMDB";

		try {
			Statement stmt = dbConnection.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				TblCMDB cmdb = new TblCMDB();
				TblCMDBPK pk = new TblCMDBPK();
				pk.setCiId(rs.getByte("ci_id"));
				pk.setServiceId(rs.getByte("service_id"));
				cmdb.setId(pk);
				ci_services.add(cmdb);
			}
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		return ci_services;
	}

	@Override
	public int getCMDBCount() {
		// TODO Auto-generated method stub
		return 0;
	}

}
