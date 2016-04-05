package com.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.jdbc.DBUtility;
import com.model.TblCourse;

public class TblCourseDao {
	private Connection dbConnection;
	private PreparedStatement pStmt;

	public TblCourseDao() {
		dbConnection = DBUtility.getConnection();
	}

	public void addCourse(TblCourse course) {
		String insertQuery = "INSERT INTO `SIMULATOR`.`tblCourse`\r\n" + 
				"(`course_name`,\r\n" + 
				"`date`,\r\n" + 
				"`isActive`,\r\n" + 
				"`lastRoundDone`)\r\n" + 
				"VALUES (?,?,?,?);\r\n";
		try {
			pStmt = dbConnection.prepareStatement(insertQuery);
			pStmt.setString(1, course.getCourseName());
			pStmt.setDate(2, new java.sql.Date(course.getDate().getTime()));
			pStmt.setByte(3, course.getIsActive());
			pStmt.setByte(4, course.getLastRoundDone());
			pStmt.executeUpdate();
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
	}

	public void deleteCourse(String name) {
		String deleteQuery = "DELETE FROM tblCourse WHERE course_name = ?";
		try {
			pStmt = dbConnection.prepareStatement(deleteQuery);
			pStmt.setString(1, name);
			pStmt.executeUpdate();
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
	}

	public void updateCourse(TblCourse course)  {
		String updateQuery = "UPDATE `SIMULATOR`.`tblCourse`\r\n" + 
				"SET\r\n" + 
				"`course_name` = ?,\r\n" + 
				"`date` = ?,\r\n" + 
				"`isActive` = ?,\r\n" + 
				"`lastRoundDone` = ?\r\n" + 
				"WHERE `course_name` = ?;";
		try {
			pStmt = dbConnection.prepareStatement(updateQuery);		
			pStmt.setString(1, course.getCourseName());
			pStmt.setDate(2, new java.sql.Date(course.getDate().getTime()));
			pStmt.setByte(3, course.getIsActive());
			pStmt.setByte(4, course.getLastRoundDone());
			
			pStmt.setString(5, course.getCourseName());
			pStmt.executeUpdate();

		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
	}

	public List<TblCourse> getAllCourses(int startPageIndex, int recordsPerPage) {
		List<TblCourse> courses = new ArrayList<TblCourse>();
		
		String query = "SELECT * FROM tblCourse ORDER BY course_name\n"
		+"limit "+startPageIndex + "," +recordsPerPage;

		try {
			Statement stmt = dbConnection.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				TblCourse course = new TblCourse();
				course.setCourseName(rs.getString("course_name"));
				course.setDate(rs.getDate("date"));
				course.setIsActive(rs.getByte("isActive"));
				course.setLastRoundDone(rs.getByte("lastRoundDone"));
				courses.add(course);
			}
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		return courses;
	  }
	
	public List<TblCourse> getAllCourses() {
		List<TblCourse> courses = new ArrayList<TblCourse>();
		
		String query = "SELECT * FROM tblCourse ORDER BY course_name\n";
		try {
			Statement stmt = dbConnection.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				TblCourse course = new TblCourse();
				course.setCourseName(rs.getString("course_name"));
				course.setDate(rs.getDate("date"));
				course.setIsActive(rs.getByte("isActive"));
				course.setLastRoundDone(rs.getByte("lastRoundDone"));
				courses.add(course);
			}
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		return courses;
	  }
	
	public TblCourse getCourseById(String courseName) {
		TblCourse course = null;
		
		String query = "SELECT * FROM tblCourse Where course_name = ?";
		try {
			pStmt = dbConnection.prepareStatement(query);
			pStmt.setString(1, courseName);
			ResultSet rs = pStmt.executeQuery();
			if (rs.next()) {
				course = new TblCourse();
				course.setCourseName(rs.getString("course_name"));

			}
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		return course;
	  }


	public int getCourseCount()
	{
	        int count=0;
	        try 
	        {
	           Statement stmt = dbConnection.createStatement();
	           ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS COUNT FROM SIMULATOR.tblCourse;");
	           while (rs.next()) 
	           {
	                count=rs.getInt("COUNT");
	           }
	        } 
	        catch (SQLException e) 
	        {
	                System.err.println(e.getMessage());
	        }
	        return count;
	}
}
