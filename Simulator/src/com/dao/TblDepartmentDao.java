package com.dao;

import java.sql.SQLException;
import java.util.List;

import com.model.TblDepartment;
import com.model.TblDepartmentPK;

public interface TblDepartmentDao {
	
	public void addDepartment(TblDepartment department) throws SQLException;
	public void deleteDepartment(TblDepartmentPK pk) throws SQLException;
	public void updateDepartment(TblDepartment department, TblDepartmentPK pk) throws SQLException;
	public List<TblDepartment> getAllDepartments(int startPageIndex, int recordsPerPage);
	public List<TblDepartment> getAllDepartments();
	public List<TblDepartment> getAllActiveDepartments();
	public TblDepartment getDepartmentById(String divisionName, String departmentName);
	public int getDepartmentCount();
}
