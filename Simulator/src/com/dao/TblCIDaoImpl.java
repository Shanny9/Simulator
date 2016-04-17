package com.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.gson.JsonObject;
import com.jdbc.DBUtility;
import com.model.TblCI;

public class TblCIDaoImpl implements TblCIDao{
	
	private Connection dbConnection;
	private PreparedStatement pStmt;

	public TblCIDaoImpl() {
		dbConnection = DBUtility.getConnection();
	}
	
	@Override
	public void addCI(TblCI ci) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteCI(String name) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateCI(TblCI ci) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<TblCI> getAllCIs(int startPageIndex, int recordsPerPage) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TblCI> getAllCIs() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TblCI getCIById(String ciName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getCourseCount() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	public List<JsonObject> getSolutions(String team){
		List<JsonObject> solutions = new ArrayList<JsonObject>();
		
		String teamInDb;
		if (team.equals("Marom")){
			teamInDb = "A";
		} else{
			teamInDb = "B";
		}
		
		String query = "SELECT CI_ID, solution_" + teamInDb + " FROM tblCI";

		try {
			Statement stmt = dbConnection.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				JsonObject row = new JsonObject();
				row.addProperty("ciID", rs.getInt("CI_ID"));
				row.addProperty("solution", rs.getInt("solution_"+teamInDb));
				
				solutions.add(row);
			}
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		return solutions;
	}

}
