package com.daoImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.dao.TblEventDao;
import com.jdbc.DBUtility;
import com.model.TblEvent;
import com.model.TblIncident;
import com.model.TblService;

public class TblEevntDaoImpl implements TblEventDao {
	private Connection dbConnection;
	private PreparedStatement pStmt;

	public TblEevntDaoImpl() {
		dbConnection = DBUtility.getConnection();
	}

	@Override
	public void addEvent(TblEvent event) {
//		String insertQuery = "INSERT INTO `SIMULATOR`.`tblCourse`\r\n" + "(`course_name`,\r\n" + "`date`,\r\n"
//				+ "`isActive`,\r\n" + "`lastRoundDone`)\r\n" + "VALUES (?,?,?,?);\r\n";
//		try {
//			pStmt = dbConnection.prepareStatement(insertQuery);
//			pStmt.setString(1, course.getCourseName());
//			pStmt.setDate(2, new java.sql.Date(course.getDate().getTime()));
//			pStmt.setByte(3, course.getIsActive());
//			pStmt.setByte(4, course.getLastRoundDone());
//			pStmt.executeUpdate();
//		} catch (SQLException e) {
//			System.err.println(e.getMessage());
//		}
	}

	@Override
	public void deleteEvent(String name) {
//		String deleteQuery = "DELETE FROM tblCourse WHERE course_name = ?";
//		try {
//			pStmt = dbConnection.prepareStatement(deleteQuery);
//			pStmt.setString(1, name);
//			pStmt.executeUpdate();
//		} catch (SQLException e) {
//			System.err.println(e.getMessage());
//		}
	}

	@Override
	public void updateEvent(TblEvent event) {
//		String updateQuery = "UPDATE `SIMULATOR`.`tblCourse`\r\n" + "SET\r\n" + "`course_name` = ?,\r\n"
//				+ "`date` = ?,\r\n" + "`isActive` = ?,\r\n" + "`lastRoundDone` = ?\r\n" + "WHERE `course_name` = ?;";
//		try {
//			pStmt = dbConnection.prepareStatement(updateQuery);
//			pStmt.setString(1, course.getCourseName());
//			pStmt.setDate(2, new java.sql.Date(course.getDate().getTime()));
//			pStmt.setByte(3, course.getIsActive());
//			pStmt.setByte(4, course.getLastRoundDone());
//
//			pStmt.setString(5, course.getCourseName());
//			pStmt.executeUpdate();
//
//		} catch (SQLException e) {
//			System.err.println(e.getMessage());
//		}
	}

	@Override
	public List<TblEvent> getAllEvents(int startPageIndex, int recordsPerPage) {
		List<TblEvent> events = new ArrayList<TblEvent>();

		String query = "SELECT * FROM tblEvent ORDER BY event_id\n" + "limit " + startPageIndex + ","
				+ recordsPerPage;

		try {
			Statement stmt = dbConnection.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				TblEvent event = new TblEvent();
				event.setEventId(rs.getInt("event_id"));
				TblIncident inc = new TblIncident();
				inc.setIncidentId(rs.getByte("incident_id"));
				event.setTblIncident(inc);
				TblService ser = new TblService();
				ser.setServiceId(rs.getByte("service_id"));
				event.setTblService(ser);
				events.add(event);
			}
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		return events;
	}

	@Override
	public List<TblEvent> getAllEvents() {
		List<TblEvent> events = new ArrayList<TblEvent>();

		String query = "SELECT * FROM tblEvent ORDER BY event_id\n";
		try {
			Statement stmt = dbConnection.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				TblEvent event = new TblEvent();
				event.setEventId(rs.getInt("event_id"));
				TblIncident inc = new TblIncident();
				inc.setIncidentId(rs.getByte("incident_id"));
				event.setTblIncident(inc);
				TblService ser = new TblService();
				ser.setServiceId(rs.getByte("service_id"));
				event.setTblService(ser);
				events.add(event);
			}
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		return events;
	}

	@Override
	public TblEvent getEventById(String eventName) {
//		TblCourse course = null;
//		String query = "SELECT * FROM tblCourse Where course_name = ?";
//		try {
//			pStmt = dbConnection.prepareStatement(query);
//			pStmt.setString(1, eventName);
//			ResultSet rs = pStmt.executeQuery();
//			if (rs.next()) {
//				course = new TblCourse();
//				course.setCourseName(rs.getString("course_name"));
//
//			}
//		} catch (SQLException e) {
//			System.err.println(e.getMessage());
//		}
		return null;
	}

	@Override
	public int getEventCount() {
		int count = 0;
//		try {
//			Statement stmt = dbConnection.createStatement();
//			ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS COUNT FROM SIMULATOR.tblCourse;");
//			while (rs.next()) {
//				count = rs.getInt("COUNT");
//			}
//		} catch (SQLException e) {
//			System.err.println(e.getMessage());
//		}
		return count;
	}
}
