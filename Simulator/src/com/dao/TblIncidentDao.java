package com.dao;

import java.util.List;

import com.model.TblIncident;

public interface TblIncidentDao {
	
	public void addIncident(TblIncident incident);
	public void deleteIncident(Byte id);
	public void updateIncident(TblIncident incident, byte id);
	public List<TblIncident> getAllIncidents(int startPageIndex, int recordsPerPage);
	public List<TblIncident> getAllIncidents();
	public TblIncident getIncidentById(byte id);
	public int getIncidentCount();
}
