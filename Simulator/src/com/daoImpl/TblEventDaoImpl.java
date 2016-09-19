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


public class TblEventDaoImpl implements TblEventDao {
	private Connection dbConnection;
	private PreparedStatement pStmt;

	public TblEventDaoImpl() {
		dbConnection = DBUtility.getConnection();
	}

	@Override
	public void addEvent(TblEvent event) throws SQLException {
		String insertQuery = "INSERT INTO tblEvent(event_id, incident_id, service_id, isActive) VALUES (?,?,?,?)";

			pStmt = dbConnection.prepareStatement(insertQuery);
			pStmt.setInt(1, event.getEventId());
			pStmt.setByte(2, event.getIncidentId());
			pStmt.setByte(3, event.getServiceId());
			pStmt.setBoolean(4, event.getIsActive());
			pStmt.executeUpdate();

	}

	@Override
	public void deleteEvent(int event_id) throws SQLException {
		String deleteQuery = "DELETE FROM tblEvent WHERE event_id = ?";

			pStmt = dbConnection.prepareStatement(deleteQuery);
			pStmt.setInt(1, event_id);
			pStmt.executeUpdate();

	}

	@Override
	public void updateEvent(TblEvent event, int eventId) throws SQLException {
		String updateQuery = "UPDATE tblEvent SET event_id = ?, incident_id = ?, service_id = ?, isActive = ? WHERE event_id = ?";

			pStmt = dbConnection.prepareStatement(updateQuery);
			pStmt.setInt(1, event.getEventId());
			pStmt.setByte(2, event.getIncidentId());
			pStmt.setByte(3, event.getServiceId());
			pStmt.setBoolean(4, event.getIsActive());
			pStmt.setInt(5, eventId);
			pStmt.executeUpdate();

	}

	@Override
	public List<TblEvent> getAllEvents(int startPageIndex, int recordsPerPage) {
		List<TblEvent> events = new ArrayList<TblEvent>();

		String query = "SELECT * FROM tblEvent limit " + startPageIndex + "," + recordsPerPage;

		try {
			Statement stmt = dbConnection.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				TblEvent event = new TblEvent();

				event.setEventId(rs.getInt("event_id"));
				event.setIncidentId(rs.getByte("incident_id"));
				event.setServiceId(rs.getByte("service_id"));
				event.setIsActive(rs.getBoolean("isActive"));
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

		String query = "SELECT * FROM tblEvent";

		try {
			Statement stmt = dbConnection.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				TblEvent event = new TblEvent();

				event.setEventId(rs.getInt("event_id"));
				event.setIncidentId(rs.getByte("incident_id"));
				event.setServiceId(rs.getByte("service_id"));
				event.setIsActive(rs.getBoolean("isActive"));
				events.add(event);
			}
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		return events;
	}

	@Override
	public TblEvent getEventById(int event_id) {
		TblEvent event = new TblEvent();
		String query = "SELECT * FROM tblEvent WHERE event_id = ?";

		try {
			pStmt = dbConnection.prepareStatement(query);
			pStmt.setInt(1, event_id);
			ResultSet rs = pStmt.executeQuery();
			rs.next();
			event.setEventId(rs.getInt("event_id"));
			event.setIncidentId(rs.getByte("incident_id"));
			event.setServiceId(rs.getByte("service_id"));
			event.setIsActive(rs.getBoolean("isActive"));

		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		return event;
	}

	@Override
	public int getEventCount() {
		int count = 0;
		try {
			Statement stmt = dbConnection.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS COUNT FROM SIMULATOR.tblEvent;");
			while (rs.next()) {
				count = rs.getInt("COUNT");
			}
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		return count;
	}
}