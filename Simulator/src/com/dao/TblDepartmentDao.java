package com.dao;

import java.util.List;

import com.model.TblDepartment;
import com.model.TblDepartmentPK;

public interface TblDepartmentDao {
	
	public void addDepartment(TblDepartment department);
	public void deleteDepartment(TblDepartmentPK pk);
	public void updateDepartment(TblDepartment department);
	public List<TblDepartment> getAllDepartments(int startPageIndex, int recordsPerPage);
	public List<TblDepartment> getAllDepartments();
	public TblDepartment getDepartmentById(String divisionName, String departmentName);
	public int getDepartmentCount();
}
