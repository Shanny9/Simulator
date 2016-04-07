package com.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.jdbc.DBUtility;
import com.model.TblCI;
import com.model.TblIncident;


public class TblIncidentDaoImpl implements TblIncidentDao {

	private Connection dbConnection;
	private PreparedStatement pStmt;

	public TblIncidentDaoImpl() {
		dbConnection = DBUtility.getConnection();
	}

	@Override
	public void addIncident(TblIncident incident) {
		String insertQuery = "INSERT INTO `SIMULATOR`.`tblIncident` (`incident_ID`,`time_`,`priority_`,`root_service_ID`,`isActive`) "
				+ " VALUES (?,?,?,?,?);";
		try {
			pStmt = dbConnection.prepareStatement(insertQuery);
			pStmt.setByte(1, incident.getIncident_ID());
			pStmt.setTime(2, incident.getTime());
			pStmt.setByte(3, incident.getPriority());
			pStmt.setByte(4, incident.getTblCi().getService_ID());
			pStmt.setByte(5, incident.getIsActive());
			pStmt.executeUpdate();
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
	}

	@Override
	public void deleteIncident(Byte id) {
		String deleteQuery = "DELETE FROM tblIncident WHERE incident_ID = ?";
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
		String updateQuery = "UPDATE `SIMULATOR`.`tblIncident` SET `incident_ID` =?, `time_` =?, `priority_` = ?,\n"
				+ " `root_service_ID` =?, `isActive` =? WHERE `incident_ID` =?;";
		try {
			pStmt = dbConnection.prepareStatement(updateQuery);
			pStmt.setByte(1, incident.getIncident_ID());
			pStmt.setTime(2, incident.getTime());
			pStmt.setByte(3, incident.getPriority());
			pStmt.setByte(4, incident.getTblCi().getService_ID());
			pStmt.setByte(5, incident.getIsActive());
			pStmt.setByte(6, incident.getIncident_ID());
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

				incident.setIncident_ID(rs.getByte("incident_ID"));
				incident.setTime(rs.getTime("time_"));
				incident.setPriority(rs.getByte("priority_"));

				TblCI tblci = new TblCI();
				tblci.setService_ID(rs.getByte("root_service_ID"));
				incident.setTblCi(tblci);

				incident.setIsActive(rs.getByte("isActive"));
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

				incident.setIncident_ID(rs.getByte("incident_ID"));
				incident.setTime(rs.getTime("time_"));
				incident.setPriority(rs.getByte("priority_"));

				TblCI tblci = new TblCI();
				tblci.setService_ID(rs.getByte("root_service_ID"));
				incident.setTblCi(tblci);

				incident.setIsActive(rs.getByte("isActive"));
				incidents.add(incident);
			}
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		return incidents;
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
