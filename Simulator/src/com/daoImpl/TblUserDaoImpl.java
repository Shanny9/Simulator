package com.daoImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.dao.TblUserDao;
import com.jdbc.DBUtility;
import com.model.TblUser;

public class TblUserDaoImpl implements TblUserDao {

	private Connection dbConnection;
	private PreparedStatement pStmt;
	private static TblUser gp;
	private static int sessionTime;
	private static int roundTime;

	public TblUserDaoImpl() {
		dbConnection = DBUtility.getConnection();
	}

	@Override
	public void addUser(TblUser user) {
		String insertQuery = "INSERT INTO `SIMULATOR`.`tblUser`\r\n" + "(`username`,\r\n"
				+ "`password`,\r\n" + "`type`\n" + "VALUES\r\n" + "(?,?,?);\r\n";
		try {
			pStmt = dbConnection.prepareStatement(insertQuery);
			pStmt.setString(1, user.getUsername());
			pStmt.setString(2, user.getPassword());
			pStmt.setString(3, user.getType());

			pStmt.executeUpdate();
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
	}

	@Override
	public void deleteUser(String pk) {
		String deleteQuery = "DELETE FROM tblUser WHERE username = ?";
		try {
			pStmt = dbConnection.prepareStatement(deleteQuery);
			pStmt.setString(1, pk);
			pStmt.executeUpdate();
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
	}

	@Override
	public void updateUser(TblUser user) {
		String updateQuery = "UPDATE tblUser SET username=?, password=?, type=? WHERE username=?";
		try {
			pStmt = dbConnection.prepareStatement(updateQuery);
			pStmt.setString(1, user.getUsername());
			pStmt.setString(2, user.getPassword());
			pStmt.setString(3, user.getType());
			pStmt.executeUpdate();

		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
	}

	@Override
	public List<TblUser> getAllUsers() {
		List<TblUser> users = new ArrayList<TblUser>();

		String query = "SELECT * FROM tblUser";

		try {
			Statement stmt = dbConnection.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				TblUser user = new TblUser();

				user.setUsername(rs.getString("username"));
				user.setPassword(rs.getString("password"));
				user.setType(rs.getString("type"));
				users.add(user);
			}
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		return users;
	}
	
/*	@Override
	public TblUser getUser() {

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
	}*/

/*	@Override
	public int getSessionTime() {
		if (sessionTime == 0) {
			if (gp == null)
				getUser();
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
	}*/
}
