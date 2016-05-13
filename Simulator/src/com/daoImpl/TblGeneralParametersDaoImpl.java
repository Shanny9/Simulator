package com.daoImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.dao.TblGeneralParametersDao;
import com.jdbc.DBUtility;
import com.model.TblGeneral_parameter;

public class TblGeneralParametersDaoImpl implements TblGeneralParametersDao {

	private Connection dbConnection;
	private PreparedStatement pStmt;
	private static TblGeneral_parameter gp;
	private static int sessionTime;
	private static int roundTime;

	public TblGeneralParametersDaoImpl() {
		dbConnection = DBUtility.getConnection();
	}

	@Override
	public void addGeneralParameters(TblGeneral_parameter generalParameter) {
		String insertQuery = "INSERT INTO `SIMULATOR`.`tblGeneral_parameters`\r\n" + "(`pk`,\r\n"
				+ "`num_of_rounds`,\r\n" + "`run_time`,\r\n" + "`pause_time`,\r\n" + "`sessions_per_round`,\r\n"
				+ "`initial_capital`)\r\n" + "VALUES\r\n" + "(?,?,?,?,?,?);\r\n";
		try {
			pStmt = dbConnection.prepareStatement(insertQuery);
			pStmt.setInt(1, generalParameter.getPk());
			pStmt.setByte(2, generalParameter.getNumOfRounds());
			pStmt.setInt(3, generalParameter.getRunTime());
			pStmt.setInt(4, generalParameter.getPauseTime());
			pStmt.setByte(5, generalParameter.getSessionsPerRound());
			pStmt.setDouble(6, generalParameter.getInitialCapital());
			pStmt.executeUpdate();
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
	}

	@Override
	public void deleteGeneralParameters(int pk) {
		String deleteQuery = "DELETE FROM tblGeneral_parameters WHERE pk = ?";
		try {
			pStmt = dbConnection.prepareStatement(deleteQuery);
			pStmt.setInt(1, pk);
			pStmt.executeUpdate();
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
	}

	@Override
	public void updateGeneralParameters(TblGeneral_parameter generalParameter) {
		String updateQuery = "UPDATE `SIMULATOR`.`tblGeneral_parameters`\r\n" + "SET\r\n" + "`pk` =?\r\n"
				+ "`num_of_rounds` =?\r\n" + "`run_time` =?\r\n" + "`pause_time` =?\r\n" + "`sessions_per_round` =?\r\n"
				+ "`initial_capital` =?\r\n" + "WHERE `pk` =?\r\n" + "`\r\n" + "WHERE pk =?;\r\n";
		try {
			pStmt = dbConnection.prepareStatement(updateQuery);
			pStmt.setInt(1, generalParameter.getPk());
			pStmt.setByte(2, generalParameter.getNumOfRounds());
			pStmt.setInt(3, generalParameter.getRunTime());
			pStmt.setInt(4, generalParameter.getPauseTime());
			pStmt.setByte(5, generalParameter.getSessionsPerRound());
			pStmt.setDouble(6, generalParameter.getInitialCapital());
			pStmt.setInt(7, generalParameter.getPk());
			pStmt.executeUpdate();

		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
	}

	@Override
	public TblGeneral_parameter getGeneralParameters() {
		gp = new TblGeneral_parameter();

		String query = "SELECT * FROM tblGeneral_parameters";

		try {
			Statement stmt = dbConnection.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			if (rs.next()) {
				gp.setInitialCapital(rs.getDouble("initial_capital"));
				gp.setNumOfRounds(rs.getByte("num_of_rounds"));
				gp.setPauseTime(rs.getInt("pause_time"));
				gp.setPk(rs.getInt("pk"));
				gp.setRunTime(rs.getInt("run_time"));
				gp.setSessionsPerRound(rs.getByte("sessions_per_round"));
				
				gp.setHomePass(rs.getString("home_pass"));
				gp.setHomeUser(rs.getString("home_user"));

			}
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		return gp;
	}

	@Override
	public int getSessionTime() {
		if (sessionTime == 0) {
			if (gp == null)
				getGeneralParameters();
			sessionTime = gp.getPauseTime() + gp.getRunTime();
		}
		return sessionTime;
	}

	@Override
	public int getRoundTime() {
		if (roundTime == 0) {
			if (sessionTime == 0) {
				getSessionTime();
			}
			roundTime = sessionTime * gp.getSessionsPerRound();
		}
		return roundTime;
	}

	@Override
	public int getTotalTime() {
		return getRoundTime() * gp.getNumOfRounds();
	}

	@Override
	public int getRoundTotalRunTime() {
		return getRoundTime() * gp.getSessionsPerRound();
	}
}
