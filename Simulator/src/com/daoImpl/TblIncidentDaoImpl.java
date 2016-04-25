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
import com.model.TblCI;
import com.model.TblIncident;
import com.model.TblSolution;


public class TblIncidentDaoImpl implements TblIncidentDao {

	private Connection dbConnection;
	private PreparedStatement pStmt;

	public TblIncidentDaoImpl() {
		dbConnection = DBUtility.getConnection();
	}

	@Override
	public void addIncident(TblIncident incident) {
		String insertQuery = "INSERT INTO `SIMULATOR`.`tblIncident`\r\n" + 
				"(`incident_id`,\r\n" + 
				"`incidentTime`,\r\n" + 
				"`ci_id`,\r\n" + 
				"`isActive`,\r\n" + 
				"`solution_id`)\r\n" + 
				"VALUES\r\n" + 
				"(?,?,?,?,?)";
		try {
			pStmt = dbConnection.prepareStatement(insertQuery);
			pStmt.setByte(1, incident.getIncidentId());
			pStmt.setInt(2, incident.getIncidentTime());
			pStmt.setByte(4, incident.getCiId());
			pStmt.setByte(5, incident.getIsActive());
			pStmt.setInt(5, incident.getTblSolution().getSolutionId());
			pStmt.executeUpdate();
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
	}

	@Override
	public void deleteIncident(Byte id) {
		String deleteQuery = "DELETE FROM tblIncident WHERE incident_id = ?";
		try {
			pStmt = dbConnection.prepareStatement(deleteQuery);
			pStmt.setByte(1, id);
			pStmt.executeUpdate();
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
	}

	@Override
	public void updateSupplier(TblIncident incident) {
		String updateQuery = "UPDATE `SIMULATOR`.`tblIncident`\r\n" + 
				"SET\r\n" + 
				"`incident_id` = ?,\r\n" + 
				"`incidentTime` = ?,\r\n" + 
				"`ci_id` = ?,\r\n" + 
				"`isActive` = ?,\r\n" + 
				"`solution_id` = ?\r\n" + 
				"WHERE `incident_id` = ?;";
		try {
			pStmt = dbConnection.prepareStatement(updateQuery);
			pStmt.setByte(1, incident.getIncidentId());
			pStmt.setInt(2, incident.getIncidentTime());
			pStmt.setByte(4, incident.getCiId());
			pStmt.setByte(5, incident.getIsActive());
			pStmt.setInt(5, incident.getTblSolution().getSolutionId());
			pStmt.setByte(6, incident.getIncidentId());
			pStmt.executeUpdate();

		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
	}

	@Override
	public List<TblIncident> getAllIncidents(int startPageIndex, int recordsPerPage) {
		List<TblIncident> incidents = new ArrayList<TblIncident>();

		String query = "SELECT * FROM tblIncident \n" + "limit " + startPageIndex + "," + recordsPerPage;

		try {
			Statement stmt = dbConnection.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				TblIncident incident = new TblIncident();

				incident.setIncidentId(rs.getByte("incident_id"));
				incident.setCiId(rs.getByte("ci_id"));
				incident.setIncidentTime(rs.getInt("incidentTime"));
				incident.setIsActive(rs.getByte("isActive"));
				TblSolution sol = new TblSolution();
				sol.setSolutionId(rs.getInt(rs.getInt("solution_id")));
				incident.setTblSolution(sol);				
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
				incident.setIsActive(rs.getByte("isActive"));
				TblSolution sol = new TblSolution();
				sol.setSolutionId(rs.getInt("solution_id"));
				incident.setTblSolution(sol);				
				incidents.add(incident);
			}
		} catch (SQLException e) {
			System.err.println("getAllIncidents: " + e.getMessage());
		}
		return incidents;
	}
	
	@Override
	public TblIncident getIncidentById(int id) {
		// TODO Auto-generated method stub
		return null;
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
