package com.daoImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.dao.TblResolutionDao;
import com.jdbc.DBUtility;
import com.model.TblIncident;
import com.model.TblResolution;
import com.model.TblResolutionPK;

public class TblResolutionDaoImpl implements TblResolutionDao {

	private Connection dbConnection;
	private PreparedStatement pStmt;

	public TblResolutionDaoImpl() {
		dbConnection = DBUtility.getConnection();
	}
	
	@Override
	public void addResolution(TblResolution resolution) {
		String insertQuery = "INSERT INTO `SIMULATOR`.`tblResolution`\r\n" + "(`incident_ID`,\r\n" + "`course`,\r\n"
				+ "`resolution_timeA`,\r\n" + "`resolution_timeB`,\r\n" + "`isPurchasedA`,\r\n" + "`isPurchasedB`,\r\n"
				+ "`isResolvedA`,\r\n" + "`isResolvedB`,\r\n" + "VALUES (?,?,?,?,?,?,?);\r\n";
		try {
			pStmt = dbConnection.prepareStatement(insertQuery);
			pStmt.setByte(1, resolution.getId().getIncident_ID());
			pStmt.setString(2, resolution.getId().getCourse());
			pStmt.setTime(3, resolution.getResolution_timeA());
			pStmt.setTime(4, resolution.getResolution_timeB());
			pStmt.setByte(5, resolution.getIsPurchasedA());
			pStmt.setByte(6, resolution.getIsPurchasedB());
			pStmt.setByte(7, resolution.getIsResolvedA());
			pStmt.setByte(8, resolution.getIsResolvedB());
			pStmt.executeUpdate();
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
	}
	
	@Override
	public void deleteResolution(TblResolutionPK id) {
		String deleteQuery = "DELETE FROM tblResolution WHERE incident_ID = ? AND course=? ";
		try {
			pStmt = dbConnection.prepareStatement(deleteQuery);
			pStmt.setByte(1, id.getIncident_ID());
			pStmt.setString(2, id.getCourse());
			pStmt.executeUpdate();
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
	}
	
	@Override
	public void updateResolution(TblResolution resolution) {
		String updateQuery = "UPDATE `SIMULATOR`.`tblResolution`\r\n" + "SET\r\n" + "`incident_ID` = ?,\r\n"
				+ "`course` = ?,\r\n" + "`resolution_timeA` = ?,\r\n" + "`resolution_timeB` = ?,\r\n"
				+ "`isPurchasedA` = ?,\r\n" + "`isPurchasedB` = ?,\r\n" + "`isResolvedA` = ?,\r\n"
				+ "`isResolvedB` = ?\r\n" + "WHERE `incident_ID` = ? AND `course` = ?;";
		try {
			pStmt = dbConnection.prepareStatement(updateQuery);
			pStmt.setByte(1, resolution.getId().getIncident_ID());
			pStmt.setString(2, resolution.getId().getCourse());
			pStmt.setTime(3, resolution.getResolution_timeA());
			pStmt.setTime(4, resolution.getResolution_timeB());
			pStmt.setByte(5, resolution.getIsPurchasedA());
			pStmt.setByte(6, resolution.getIsPurchasedB());
			pStmt.setByte(7, resolution.getIsResolvedA());
			pStmt.setByte(8, resolution.getIsResolvedB());

			pStmt.setByte(9, resolution.getId().getIncident_ID());
			pStmt.setString(10, resolution.getId().getCourse());
			pStmt.executeUpdate();

		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
	}
	
	@Override
	public List<TblResolution> getAllResolutions() {
		List<TblResolution> resolutions = new ArrayList<TblResolution>();

		String query = "SELECT * FROM TblResolution ";

		try {
			Statement stmt = dbConnection.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				byte incidentId = 0;
				TblResolution res = new TblResolution();
				TblResolutionPK pk = new TblResolutionPK();
				incidentId = rs.getByte("incident_ID");
				pk.setIncident_ID(incidentId);
				pk.setCourse(rs.getString("course"));
				res.setId(pk);

				res.setIsPurchasedA(rs.getByte("isPurchasedA"));
				res.setIsPurchasedB(rs.getByte("isPurchasedB"));
				res.setIsResolvedA(rs.getByte("isResolvedA"));
				res.setIsResolvedB(rs.getByte("isResolvedB"));
				res.setResolution_timeA(rs.getTime("resolution_timeA"));
				res.setResolution_timeB(rs.getTime("resolution_timeB"));

				TblIncident in = new TblIncident();
				in.setIncident_ID(incidentId);
				res.setTblIncident(in);

				resolutions.add(res);
			}
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		return resolutions;
	}
	
	@Override
	public List<TblResolution> getAllResolutions(int startPageIndex, int recordsPerPage) {
		List<TblResolution> resolutions = new ArrayList<TblResolution>();

		String query = "SELECT * FROM TblResolution " + "limit " + startPageIndex + "," + recordsPerPage;

		try {
			Statement stmt = dbConnection.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				byte incidentId = 0;
				TblResolution res = new TblResolution();
				TblResolutionPK pk = new TblResolutionPK();
				incidentId = rs.getByte("incident_ID");
				pk.setIncident_ID(incidentId);
				pk.setCourse(rs.getString("course"));
				res.setId(pk);

				res.setIsPurchasedA(rs.getByte("isPurchasedA"));
				res.setIsPurchasedB(rs.getByte("isPurchasedB"));
				res.setIsResolvedA(rs.getByte("isResolvedA"));
				res.setIsResolvedB(rs.getByte("isResolvedB"));
				res.setResolution_timeA(rs.getTime("resolution_timeA"));
				res.setResolution_timeB(rs.getTime("resolution_timeB"));

				TblIncident in = new TblIncident();
				in.setIncident_ID(incidentId);
				res.setTblIncident(in);

				resolutions.add(res);
			}
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		return resolutions;
	}
	
	@Override
	public int getResoltionCount() {
		int count = 0;
		try {
			Statement stmt = dbConnection.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS COUNT FROM SIMULATOR.TblResolution;");
			while (rs.next()) {
				count = rs.getInt("COUNT");
			}
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		return count;
	}
}
