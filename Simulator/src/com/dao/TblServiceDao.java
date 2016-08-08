package com.dao;

import java.util.List;

import com.model.TblService;

public interface TblServiceDao {
	
	public void addService(TblService service);
	public void deleteService(Byte id);
	public void updateService(TblService service, byte id);
	public List<TblService> getAllServices(int startPageIndex, int recordsPerPage);
	public List<TblService> getAllServices();
	public TblService getServiceById(byte id);
	public int getServiceCount();
}
