package com.dao;

import java.sql.SQLException;
import java.util.List;

import com.model.TblService;

public interface TblServiceDao {
	
	public void addService(TblService service) throws SQLException;
	public void deleteService(Byte id) throws SQLException;
	public void updateService(TblService service, byte id) throws SQLException;
	public List<TblService> getAllServices(int startPageIndex, int recordsPerPage);
	public List<TblService> getAllServices();
	public TblService getServiceById(byte id);
	public int getServiceCount();
}
