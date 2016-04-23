package com.dao;

import java.util.List;

import com.model.TblSupplier;

public interface TblSupplierDao {
	
	public void addSupplier(TblSupplier supplier);
	public void deleteSupplier(String name);
	public void updateSupplier(TblSupplier supplier);
	public List<TblSupplier> getAllSuppliers(int startPageIndex, int recordsPerPage);
	public List<TblSupplier> getAllSuppliers();
	public TblSupplier getSupplierById(String name);
	public int getSupplierCount();
}
