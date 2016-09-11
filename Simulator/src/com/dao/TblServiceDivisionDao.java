package com.dao;

import java.sql.SQLException;
import java.util.List;

import com.model.TblService_Division;
import com.model.TblService_DivisionPK;

public interface TblServiceDivisionDao {
	
	public void addServiceDivision(TblService_Division service) throws SQLException;
	public void deleteServiceDivision(TblService_DivisionPK pk) throws SQLException;
	public void updateServiceDivision(TblService_Division service, TblService_DivisionPK pk) throws SQLException;
	public List<TblService_Division> getAllServiceDivisions(int startPageIndex, int recordsPerPage);
	public List<TblService_Division> getAllServiceDivisions();
	public TblService_Division getServiceDivisionById(TblService_DivisionPK pk);
	public int getServiceDivisionCount();
}
