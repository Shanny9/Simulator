package com.dao;

import java.util.List;

import com.model.TblDepartment;

public interface TblCI_DepartmentDao {
	
	public void addDepartment(TblDepartment Department);
	public void deleteDepartment(String name);
	public void updateDepartment(TblDepartment Department);
	public List<TblDepartment> getAllDepartments(int startPageIndex, int recordsPerPage);
	public List<TblDepartment> getAllDepartments();
	public TblDepartment getDepartmentById(String DepartmentName);
	public int getCourseCount();
}
