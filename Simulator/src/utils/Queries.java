package utils;

public class Queries {

	public static String eventsForHomeTable = "select I.incidentTime, E.event_id\r\n" + 
			"from SIMULATOR.tblIncident I\r\n" + 
			"join SIMULATOR.tblEvent E\r\n" + 
			"on I.incident_id = E.incident_ID";
	
	public static String serviceDownCosts = "select S.service_id, PC.cost as 'down_cost'\r\n" + 
			"from SIMULATOR.tblService S \r\n" + 
			"join SIMULATOR.tblPriority P\r\n" + 
			"on S.urgency = P.urgency\r\n" + 
			"and S.impact = P.impact\r\n" + 
			"join SIMULATOR.tblPriorityCost PC\r\n" + 
			"on P.priorityName = PC.priorityName";
}
