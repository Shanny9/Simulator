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
	public List<JsonObject> getEvents(){
		
		List<JsonObject> events = new ArrayList<JsonObject>();
		String query = Queries.eventsForHomeTable;
		try {
			Statement stmt = dbConnection.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				JsonObject row = new JsonObject();
				row.addProperty("time", rs.getInt("incidentTime"));
				row.addProperty("event_id", rs.getInt("event_id"));
				events.add(row);	
			}
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
	//	System.out.println("HomeData:getIncidents(): "+incidents);
		return events;
	}
	
}
