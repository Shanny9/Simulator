package com.dao;

import java.util.List;

import com.model.TblIncident;

public interface TblIncidentDao {
	
	public void addIncident(TblIncident incident);
	public void deleteIncident(Byte id);
	public void updateSupplier(TblIncident incident);
	public List<TblIncident> getAllIncidents(int startPageIndex, int recordsPerPage);
	public List<TblIncident> getAllIncidents();
	public TblIncident getIncidentById(int id);
	public int getIncidentCount();
}
