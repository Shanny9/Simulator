package com.dao;

import java.util.List;

import com.model.TblCourse;

public interface TblCourseDao {
	
	public void addCourse(TblCourse course);
	public void deleteCourse(String name);
	public void updateCourse(TblCourse course);
	public List<TblCourse> getAllCourses(int startPageIndex, int recordsPerPage);
	public List<TblCourse> getAllCourses();
	public TblCourse getCourseById(String courseName);
	public int getCourseCount();
}
