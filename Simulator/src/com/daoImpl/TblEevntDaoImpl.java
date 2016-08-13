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
		String insertQuery = "INSERT INTO tblEvent(event_id, incident_id, service_id, isActive) VALUES (?,?,?,?)";
		try {
			pStmt = dbConnection.prepareStatement(insertQuery);
			pStmt.setInt(1, event.getEventId());
			pStmt.setByte(2, event.getTblIncident().getIncidentId());
			pStmt.setByte(3, event.getTblService().getServiceId());
			pStmt.setBoolean(4, event.getIsActive());
			pStmt.executeUpdate();
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
	}

	@Override
	public void deleteEvent(int event_id) {
		String deleteQuery = "DELETE FROM tblEvent WHERE event_id = ?";
		try {
			pStmt = dbConnection.prepareStatement(deleteQuery);
			pStmt.setInt(1, event_id);
			pStmt.executeUpdate();
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
	}

	@Override
	public void updateEvent(TblEvent event) {
		String updateQuery = "UPDATE tblEvent SET event_id = ?, incident_id = ?, service_id = ?, isActive = ? WHERE event_id = ?";
		try {
			pStmt = dbConnection.prepareStatement(updateQuery);
			pStmt.setInt(1, event.getEventId());
			pStmt.setByte(2, event.getTblIncident().getIncidentId());
			pStmt.setByte(3, event.getTblService().getServiceId());
			pStmt.setBoolean(4, event.getIsActive());
			pStmt.setInt(5, event.getEventId());
			pStmt.executeUpdate();
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
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
				TblIncident incident = new TblIncident();
				incident.setIncidentId(rs.getByte("incident_id"));
				TblService service = new TblService();
				service.setServiceId(rs.getByte("service_id"));
				event.setTblService(service);
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
				TblIncident incident = new TblIncident();
				incident.setIncidentId(rs.getByte("incident_id"));
				TblService service = new TblService();
				service.setServiceId(rs.getByte("service_id"));
				event.setTblService(service);
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
			TblIncident incident = new TblIncident();
			incident.setIncidentId(rs.getByte("incident_id"));
			TblService service = new TblService();
			service.setServiceId(rs.getByte("service_id"));
			event.setTblService(service);
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
