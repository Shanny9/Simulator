package com.daoImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.dao.TblIncidentDao;
import com.jdbc.DBUtility;
import com.model.TblIncident;


public class TblIncidentDaoImpl implements TblIncidentDao {

	private Connection dbConnection;
	private PreparedStatement pStmt;

	public TblIncidentDaoImpl() {
		dbConnection = DBUtility.getConnection();
	}

	@Override
	public void addIncident(TblIncident incident) throws SQLException {
		String insertQuery = "INSERT INTO `SIMULATOR`.`tblIncident`\r\n" + 
				"(`incident_id`,\r\n" + 
				"`incidentTime`,\r\n" + 
				"`ci_id`,\r\n" + 
				"`isActive`,\r\n" + 
				"`solution_id`)\r\n" + 
				"VALUES\r\n" + 
				"(?,?,?,?,?)";

			pStmt = dbConnection.prepareStatement(insertQuery);
			pStmt.setByte(1, incident.getIncidentId());
			pStmt.setInt(2, incident.getIncidentTime());
			pStmt.setByte(3, incident.getCiId());
			pStmt.setBoolean(4, incident.getIsActive());
			pStmt.setInt(5, incident.getSolutionId());
			pStmt.executeUpdate();

	}

	@Override
	public void deleteIncident(Byte id) throws SQLException {
		String deleteQuery = "DELETE FROM tblIncident WHERE incident_id = ?";

			pStmt = dbConnection.prepareStatement(deleteQuery);
			pStmt.setByte(1, id);
			pStmt.executeUpdate();

	}

	@Override
	public void updateIncident(TblIncident incident, byte id) throws SQLException {
		String updateQuery = "UPDATE `SIMULATOR`.`tblIncident`\r\n" + 
				"SET\r\n" + 
				"`incident_id` = ?,\r\n" + 
				"`incidentTime` = ?,\r\n" + 
				"`ci_id` = ?,\r\n" + 
				"`isActive` = ?,\r\n" + 
				"`solution_id` = ?\r\n" + 
				"WHERE `incident_id` = ?;";

			pStmt = dbConnection.prepareStatement(updateQuery);
			pStmt.setByte(1, incident.getIncidentId());
			pStmt.setInt(2, incident.getIncidentTime());
			pStmt.setByte(3, incident.getCiId());
			pStmt.setBoolean(4, incident.getIsActive());
			pStmt.setInt(5, incident.getSolutionId());
			pStmt.setByte(6, id);
			pStmt.executeUpdate();

	}

	@Override
	public List<TblIncident> getAllIncidents(int startPageIndex, int recordsPerPage) {
		List<TblIncident> incidents = new ArrayList<TblIncident>();
		String query = "SELECT * FROM SIMULATOR.tblIncident \n" + "limit " + startPageIndex + "," + recordsPerPage;

		try {
			Statement stmt = dbConnection.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				TblIncident incident = new TblIncident();

				incident.setIncidentId(rs.getByte("incident_id"));
				incident.setCiId(rs.getByte("ci_id"));
				incident.setIncidentTime(rs.getInt("incidentTime"));
				incident.setIsActive(rs.getBoolean("isActive"));
				incident.setSolutionId(rs.getInt("solution_id"));			
				incidents.add(incident);
			}
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		return incidents;
	}

	@Override
	public List<TblIncident> getAllIncidents() {
		List<TblIncident> incidents = new ArrayList<TblIncident>();

		String query = "SELECT * FROM tblIncident";

		try {
			Statement stmt = dbConnection.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				TblIncident incident = new TblIncident();

				incident.setIncidentId(rs.getByte("incident_id"));
				incident.setCiId(rs.getByte("ci_id"));
				incident.setIncidentTime(rs.getInt("incidentTime"));
				incident.setIsActive(rs.getBoolean("isActive"));
				incident.setSolutionId(rs.getInt("solution_id"));			
				incidents.add(incident);
			}
		} catch (SQLException e) {
			System.err.println("getAllIncidents: " + e.getMessage());
		}
		return incidents;
	}
	
	@Override
	public TblIncident getIncidentById(byte id) {
		TblIncident inci = null;
		String query = "SELECT * FROM tblIncident WHERE incident_id = ?";

		try {
			pStmt = dbConnection.prepareStatement(query);
			pStmt.setByte(1, id);
			
			ResultSet rs = pStmt.executeQuery();
			rs.next();
			inci = new TblIncident();
			
			inci.setIncidentId(rs.getByte("incident_id"));
			inci.setIncidentTime(rs.getInt("incidentTime"));
			inci.setCiId(rs.getByte("ci_id"));
			inci.setIsActive(rs.getBoolean("isActive"));
			inci.setSolutionId(rs.getInt("solution_id"));


		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		return inci;
	}

	@Override
	public int getIncidentCount() {
		int count = 0;
		try {
			Statement stmt = dbConnection.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS COUNT FROM SIMULATOR.tblIncident;");
			while (rs.next()) {
				count = rs.getInt("COUNT");
			}
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		return count;
	}
}
