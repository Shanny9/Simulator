package utils;

public class Queries {

	public static final String eventsForHomeTable = "select I.incidentTime, E.event_id\r\n"
			+ "from SIMULATOR.tblIncident I\r\n" + "join SIMULATOR.tblEvent E\r\n" + "on I.incident_id = E.incident_ID";

	public static final String serviceDownCosts = "select S.service_id, PC.pCost as 'down_cost'\r\n"
			+ "from SIMULATOR.tblService S \r\n" + "join SIMULATOR.tblPriority P\r\n" + "on S.urgency = P.urgency\r\n"
			+ "and S.impact = P.impact\r\n" + "join SIMULATOR.tblPriority_Cost PC\r\n" + "on P.priorityName = PC.pName";

	public static final String solutionsForClient = "select I.incident_id, \r\n" + "Sol.solution_marom,\r\n"
			+ "Sol.solution_rakia,\r\n" + "Sup.solution_cost,\r\n" + "Sup.currency\r\n"
			+ "from SIMULATOR.tblIncident I \r\n"
			+ "join SIMULATOR.tblSolution Sol on I.solution_id = Sol.solution_id\r\n"
			+ "join SIMULATOR.tblCI CI on I.ci_id = CI.CI_ID\r\n"
			+ "join SIMULATOR.tblSupplier Sup on CI.supplier_level2  = Sup.supplier_name";

	public static String servicePriorities = "select service_id, priorityName from SIMULATOR.tblService S join tblPriority P"
			+ " on S.urgency = P.urgency and S.impact  = P.impact";
}
