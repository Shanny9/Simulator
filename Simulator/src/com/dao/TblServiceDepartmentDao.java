package com.dao;

import java.sql.SQLException;
import java.util.List;

import com.model.TblService_Department;
import com.model.TblService_DepartmentPK;

public interface TblServiceDepartmentDao {
	
	public void addServiceDepartment(TblService_Department service) throws SQLException;
	public void deleteServiceDepartment(TblService_DepartmentPK pk) throws SQLException;
	public void updateServiceDepartment(TblService_Department service, TblService_DepartmentPK pk) throws SQLException;
	public List<TblService_Department> getAllServiceDepartments(int startPageIndex, int recordsPerPage);
	public List<TblService_Department> getAllServiceDepartments();
	public TblService_Department getServiceDepartmentById(TblService_DepartmentPK pk);
	public int getServiceDepartmentCount();
}
