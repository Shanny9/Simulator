package utils;

public class Queries {

	/**
	 * @return A table containing the columns: <li>{@code incidentTime} (int)</li>
	 *         <li>{@code event_id} (int)</li>
	 */
	public static final String eventsForHomeTable = "select I.incidentTime, E.event_id\r\n"
			+ "from SIMULATOR.tblIncident I\r\n"
			+ "join SIMULATOR.tblEvent E\r\n"
			+ "on I.incident_id = E.incident_ID";

	/**
	 * @return A table containing the columns: <li>{@code service_id} (byte)</li>
	 *         <li>{@code down_cost} (int)</li>
	 */
	public static final String serviceDownCosts = "select S.service_id, PC.pCost as 'down_cost'\r\n"
			+ "from SIMULATOR.tblService S \r\n"
			+ "join SIMULATOR.tblPriority P\r\n"
			+ "on S.urgency = P.urgency\r\n"
			+ "and S.impact = P.impact\r\n"
			+ "join SIMULATOR.tblPriority_Cost PC\r\n"
			+ "on P.priorityName = PC.pName";

	/**
	 * @return A table containing the columns: <li>{@code incident_id} (byte)</li>
	 *         <li>{@code solution_marom} (int)</li> <li>{@code solution_rakia} (int)</li>
	 *         <li>{@code solution_cost} (int)</li> <li>{@code currency} (String)</li>
	 */
	public static final String solutionsForClient = "select I.incident_id, \r\n"
			+ "Sol.solution_marom,\r\n"
			+ "Sol.solution_rakia,\r\n"
			+ "Sup.solution_cost,\r\n"
			+ "Sup.currency\r\n"
			+ "from SIMULATOR.tblIncident I \r\n"
			+ "join SIMULATOR.tblSolution Sol on I.solution_id = Sol.solution_id\r\n"
			+ "join SIMULATOR.tblCI CI on I.ci_id = CI.CI_ID\r\n"
			+ "join SIMULATOR.tblSupplier Sup on CI.supplier_level2  = Sup.supplier_name";
	
	/**
	 * @return A table containing the columns: <li>{@code service_id} (byte)</li>
	 *         <li>{@code priorityName} (String)</li>
	 */
	public static final String servicePriorities = "select service_id, priorityName from SIMULATOR.tblService S join tblPriority P"
			+ " on S.urgency = P.urgency and S.impact  = P.impact";

	/**
	 * @param service_ID
	 *            (byte)
	 * @return Table containing the columns: <li>{@code division_name} (String)</li>
	 */
	public static final String getAllDivisionsUsingSameService = "select SD.division_name from tblService_Division SD \r\n"
			+ "where SD.service_ID = ?";

	/**
	 * @param service_ID
	 *            (byte)
	 * @return Table containing the columns: <li>{@code count} (byte)</li>
	 */
	public static final String countDivisionsUsingSameService = "select count(*) 'count' from tblService_Division SD \r\n"
			+ "where SD.service_ID = ?";

	/**
	 * @param division_name
	 *            (String)
	 * @param service_ID
	 *            (byte)
	 * @return Table containing the columns: <li>{@code department_name} (String)</li>
	 */
	public static final String getDepartmentsOfDivisionUsingSameService = "select D.department_name from tblDepartment D \r\n"
			+ "join tblService_Division SD on D.division_name = SD.divisionion_name\r\n"
			+ "where D.division_name = ? and SD.service_ID = ?";

	/**
	 * @param division_name
	 *            (String)
	 * @param service_ID
	 *            (byte)
	 * @return Table containing the columns: <li>{@code count} (byte)</li>
	 */
	public static final String countDepartmentsOfDivisionUsingSameService = "select count(*) from tblDepartment D \r\n"
			+ "join tblService_Division SD on D.division_name = SD.division_name\r\n"
			+ "where D.division_name = ? and SD.service_ID = ?";
}
