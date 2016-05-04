package com.dao;

import java.util.List;

import com.model.TblService;

public interface TblServiceDao {
	
	public void addService(TblService service);
	public void deleteService(String name);
	public void updateService(TblService service);
	public List<TblService> getAllServices(int startPageIndex, int recordsPerPage);
	public List<TblService> getAllServices();
	public TblService getServiceById(int id);
	public int getServiceCount();
}
