package utils;

public class Queries {
	
	public static String incidentsForHomeTable = "select * from(\r\n" + 
			"select I.time_, CI1.event_ID from SIMULATOR.tblIncident I\r\n" + 
			"join SIMULATOR.tblCI CI1 on I.root_service_ID = CI1.service_ID\r\n" + 
			"\r\n" + 
			"UNION\r\n" + 
			"\r\n" + 
			"select I.time_, CI2.event_ID from SIMULATOR.tblIncident I\r\n" + 
			"join SIMULATOR.tblAffected_CI ACI on I.incident_ID = ACI.incident_ID\r\n" + 
			"join SIMULATOR.tblCI CI2 on ACI.service_ID = CI2.service_ID) as T\r\n" + 
			"order by T.time_\r\n";

}
