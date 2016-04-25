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
	
	public static String solutionsForClient = "select I.incident_id, \r\n" + 
			"Sol.solution_marom,\r\n" + 
			"Sol.solution_rakia,\r\n" + 
			"Sup.solution_cost,\r\n" + 
			"Sup.currency\r\n" + 
			"from SIMULATOR.tblIncident I \r\n" + 
			"join SIMULATOR.tblSolution Sol on I.solution_id = Sol.solution_id\r\n" + 
			"join SIMULATOR.tblCI CI on I.ci_id = CI.CI_ID\r\n" + 
			"join SIMULATOR.tblSupplier Sup on CI.supplier_level2  = Sup.supplier_name";
}
