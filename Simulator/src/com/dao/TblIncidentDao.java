package com.dao;

import java.sql.SQLException;
import java.util.List;

import com.model.TblIncident;

public interface TblIncidentDao {
	
	public void addIncident(TblIncident incident) throws SQLException;
	public void deleteIncident(Byte id) throws SQLException;
	public void updateIncident(TblIncident incident, byte id) throws SQLException;
	public List<TblIncident> getAllIncidents(int startPageIndex, int recordsPerPage);
	public List<TblIncident> getAllIncidents();
	public TblIncident getIncidentById(byte id);
	public int getIncidentCount();
}
