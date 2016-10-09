package com.daoImpl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import utils.QueryLogger;

import com.dao.TblIncidentDao;
import com.jdbc.DBUtility;
import com.model.TblIncident;
import com.model.TblIncidentPK;

public class TblIncidentDaoImpl implements TblIncidentDao {


	private PreparedStatement pStmt;

	public TblIncidentDaoImpl() {

	}

	@Override
	public void addIncident(TblIncident incident) throws SQLException {
		String insertQuery = "INSERT INTO tblIncident "
				+ "(time, ci_id, isActive) VALUES (?,?,?);";

		pStmt = DBUtility.getConnection().prepareStatement(insertQuery);
		pStmt.setInt(1, incident.getIncidentTime());
		pStmt.setByte(2, incident.getCiId());
		pStmt.setBoolean(3, incident.isActive());
		pStmt.executeUpdate();
		QueryLogger.log(pStmt.toString());
	}

	@Override
	public void deleteIncident(TblIncidentPK pk)
			throws SQLException {
		String deleteQuery = "DELETE FROM tblIncident WHERE time = ? and ci_id = ?;";

		pStmt = DBUtility.getConnection().prepareStatement(deleteQuery);
		pStmt.setInt(1, pk.getTime());
		pStmt.setByte(2, pk.getCiId());
		pStmt.executeUpdate();
		QueryLogger.log(pStmt.toString());
	}

	@Override
	public void updateIncident(TblIncident incident, TblIncidentPK pk) throws SQLException {
		String updateQuery = "UPDATE tblIncident SET time = ?, "
				+ "ci_id = ?, isActive = ? WHERE time = ? and ci_id = ?;";

		pStmt = DBUtility.getConnection().prepareStatement(updateQuery);
		pStmt.setInt(1, incident.getIncidentTime());
		pStmt.setByte(2, incident.getCiId());
		pStmt.setBoolean(3, incident.isActive());
		pStmt.setInt(4, pk.getTime());
		pStmt.setByte(5, pk.getCiId());
		pStmt.executeUpdate();
		QueryLogger.log(pStmt.toString());
	}

	@Override
	public List<TblIncident> getAllIncidents(int startPageIndex,
			int recordsPerPage) {
		List<TblIncident> incidents = new ArrayList<TblIncident>();
		String query = "SELECT * FROM tblIncident limit " + startPageIndex
				+ "," + recordsPerPage;

		try {
			Statement stmt = DBUtility.getConnection().createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				TblIncident incident = new TblIncident();
				incident.setIncidentTime(rs.getInt("time"));
				incident.setCiId(rs.getByte("ci_id"));
				incident.setActive(rs.getBoolean("isActive"));
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
			Statement stmt = DBUtility.getConnection().createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				TblIncident incident = new TblIncident();
				incident.setIncidentTime(rs.getInt("time"));
				incident.setCiId(rs.getByte("ci_id"));
				incident.setActive(rs.getBoolean("isActive"));
				incidents.add(incident);
			}
		} catch (SQLException e) {
			System.err.println("getAllIncidents: " + e.getMessage());
		}
		return incidents;
	}
	
	@Override
	public List<TblIncident> getAllActiveIncidents() {
		List<TblIncident> incidents = new ArrayList<TblIncident>();

		String query = "SELECT * FROM tblIncident WHERE isActive = 1;";

		try {
			Statement stmt = DBUtility.getConnection().createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				TblIncident incident = new TblIncident();
				incident.setIncidentTime(rs.getInt("time"));
				incident.setCiId(rs.getByte("ci_id"));
				incident.setActive(rs.getBoolean("isActive"));
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
			pStmt = DBUtility.getConnection().prepareStatement(query);
			pStmt.setInt(1, pk.getTime());
			pStmt.setByte(1, pk.getCiId());

			ResultSet rs = pStmt.executeQuery();
			if (rs.next()) {
				inci = new TblIncident();
				inci.setIncidentTime(rs.getInt("time"));
				inci.setCiId(rs.getByte("ci_id"));
				inci.setActive(rs.getBoolean("isActive"));
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
			Statement stmt = DBUtility.getConnection().createStatement();
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
