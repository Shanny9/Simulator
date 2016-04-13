package utils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonObject;
import com.jdbc.DBUtility;

public class HomeData {
	
	private Connection dbConnection;
	
	public HomeData() {
		dbConnection = DBUtility.getConnection();
	}
	
	//TODO: complete
	public List<JsonObject> getIncidents(){
		
		List<JsonObject> incidents = new ArrayList<JsonObject>();
		String query = Queries.incidentsForHomeTable;
		try {
			Statement stmt = dbConnection.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				JsonObject row = new JsonObject();
				Time time = new Time(rs.getTime("time_").getTime());
				row.addProperty("event", rs.getInt("event_ID"));
				row.addProperty("time", time.toString());
				
				incidents.add(row);
				
			}
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
	//	System.out.println("HomeData:getIncidents(): "+incidents);
		return incidents;
	}
	
public List<JsonObject> getSolutions(){
		
		List<JsonObject> solutions = new ArrayList<JsonObject>();
		
		String query = Queries.incidentsForHomeTable;
		try {
			Statement stmt = dbConnection.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				JsonObject row = new JsonObject();
				Time time = new Time(rs.getTime("time_").getTime());
				row.addProperty("event", rs.getInt("event_ID"));
				row.addProperty("time", time.toString());
				
				solutions.add(row);
				
			}
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
	//	System.out.println("HomeData:getIncidents(): "+incidents);
		return solutions;
	}
}
