package com.dao;

import java.sql.SQLException;
import java.util.List;

import com.model.TblIncident;
import com.model.TblIncidentPK;

public interface TblIncidentDao {
	
	public void addIncident(TblIncident incident) throws SQLException;
	public void deleteIncident(TblIncidentPK pk) throws SQLException;
	public void updateIncident(TblIncident incident, TblIncidentPK pk) throws SQLException;
	public List<TblIncident> getAllIncidents(int startPageIndex, int recordsPerPage);
	public List<TblIncident> getAllIncidents();
	public List<TblIncident> getAllActiveIncidents();
	public TblIncident getIncidentById(TblIncidentPK pk);
	public int getIncidentCount();
}
