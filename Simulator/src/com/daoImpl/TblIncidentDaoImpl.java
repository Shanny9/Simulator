package com.daoImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import utils.SimulationTime;

import com.dao.TblIncidentDao;
import com.jdbc.DBUtility;
import com.model.TblIncident;
import com.model.TblIncidentPK;

public class TblIncidentDaoImpl implements TblIncidentDao {

	private Connection dbConnection;
	private PreparedStatement pStmt;

	public TblIncidentDaoImpl() {
		dbConnection = DBUtility.getConnection();
	}

	@Override
	public void addIncident(TblIncident incident) throws SQLException {
		String insertQuery = "INSERT INTO tblIncident "
				+ "(time, ci_id, isActive) VALUES (?,?,?);";

		pStmt = dbConnection.prepareStatement(insertQuery);
		pStmt.setInt(1, incident.getIncidentTime());
		pStmt.setByte(2, incident.getCiId());
		pStmt.setBoolean(3, incident.getIsActive());
		pStmt.executeUpdate();

	}

	@Override
	public void deleteIncident(TblIncidentPK pk)
			throws SQLException {
		String deleteQuery = "DELETE FROM tblIncident WHERE time = ? and ci_id = ?;";

		pStmt = dbConnection.prepareStatement(deleteQuery);
		pStmt.setInt(1, pk.getTime());
		pStmt.setByte(2, pk.getCiId());
		pStmt.executeUpdate();

	}

	@Override
	public void updateIncident(TblIncident incident, TblIncidentPK pk) throws SQLException {
		String updateQuery = "UPDATE tblIncident SET time = ?, "
				+ "ci_id = ?, isActive = ? WHERE time = ? and ci_id = ?;";

		pStmt = dbConnection.prepareStatement(updateQuery);
		pStmt.setInt(1, incident.getIncidentTime());
		pStmt.setByte(2, incident.getCiId());
		pStmt.setBoolean(3, incident.getIsActive());
		pStmt.setInt(4, pk.getTime());
		pStmt.setByte(5, pk.getCiId());
		pStmt.executeUpdate();
	}

	@Override
	public List<TblIncident> getAllIncidents(int startPageIndex,
			int recordsPerPage) {
		List<TblIncident> incidents = new ArrayList<TblIncident>();
		String query = "SELECT * FROM tblIncident limit " + startPageIndex
				+ "," + recordsPerPage;

		try {
			Statement stmt = dbConnection.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				TblIncident incident = new TblIncident();
				incident.setIncidentTime(rs.getInt("time"));
				incident.setCiId(rs.getByte("ci_id"));
				incident.setIsActive(rs.getBoolean("isActive"));
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

		String query = "SELECT * FROM tblIncident;";

		try {
			Statement stmt = dbConnection.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				TblIncident incident = new TblIncident();
				incident.setIncidentTime(rs.getInt("time"));
				incident.setCiId(rs.getByte("ci_id"));
				incident.setIsActive(rs.getBoolean("isActive"));
				incidents.add(incident);
			}
		} catch (SQLException e) {
			System.err.println("getAllIncidents: " + e.getMessage());
		}
		return incidents;
	}

	@Override
	public TblIncident getIncidentById(TblIncidentPK pk) {
		TblIncident inci = null;
		String query = "SELECT * FROM tblIncident WHERE time = ? and ci_id = ?";

		try {
			pStmt = dbConnection.prepareStatement(query);
			pStmt.setInt(1, pk.getTime());
			pStmt.setByte(1, pk.getCiId());

			ResultSet rs = pStmt.executeQuery();
			if (rs.next()) {
				inci = new TblIncident();
				inci.setIncidentTime(rs.getInt("time"));
				inci.setCiId(rs.getByte("ci_id"));
				inci.setIsActive(rs.getBoolean("isActive"));
			}
			
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
			ResultSet rs = stmt
					.executeQuery("SELECT COUNT(*) AS COUNT FROM tblIncident;");
			while (rs.next()) {
				count = rs.getInt("COUNT");
			}
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		return count;
	}
}
