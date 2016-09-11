package com.dao;

import java.sql.SQLException;
import java.util.List;

import com.model.TblSupplier;

public interface TblSupplierDao {
	
	public void addSupplier(TblSupplier supplier) throws SQLException;
	public void deleteSupplier(String name) throws SQLException;
	public void updateSupplier(TblSupplier supplier, String name) throws SQLException;
	public List<TblSupplier> getAllSuppliers(int startPageIndex, int recordsPerPage);
	public List<TblSupplier> getAllSuppliers();
	public TblSupplier getSupplierById(String name);
	public int getSupplierCount();
}
