package utils;

public class Queries {

	/**
	 * @return A table containing the columns: <li>{@code service_id} (byte)</li>
	 *         <li>{@code down_cost} (int)</li>
	 */
	public static final String serviceDownCosts = "select S.service_id, PC.pCost as 'down_cost'\r\n" + 
			"from tblService S join tblPriority P on S.urgency = P.urgency and S.impact = P.impact\r\n" + 
			"join SIMULATOR.tblPriority_Cost PC on P.priorityName = PC.pName\r\n" + 
			"where S.isActive = 1 and P.isActive = 1 and PC.isActive = 1;";

	/**
	 * @return A table containing the columns: <li>{@code incident_id} (byte)</li>
	 *         <li>{@code solution_marom} (int)</li> <li>{@code solution_rakia}
	 *         (int)</li> <li>{@code solution_cost} (int)</li> <li>
	 *         {@code currency} (String)</li>
	 */
	public static final String solutionsForClient = "select I.ci_id, Sol.solution_marom, Sol.solution_rakia,\r\n" + 
			"Sup.solution_cost, Sup.currency\r\n" + 
			"from tblIncident I join tblCI CI on I.ci_id = CI.ci_id\r\n" + 
			"join tblSolution Sol on CI.solution_id = Sol.solution_id\r\n" + 
			"join tblSupplier Sup on CI.supplier_level2  = Sup.supplier_name\r\n" + 
			"where I.isActive = 1 and CI.isActive = 1 and Sol.isActive = 1 and Sup.isActive = 1;";

	/**
	 * @return A table containing the columns: <li>{@code service_id} (byte)</li>
	 *         <li>{@code priorityName} (String)</li>
	 */
	public static final String servicePriorities = "select service_id, priorityName from tblService S \r\n" + 
			"join tblPriority P on S.urgency = P.urgency and S.impact  = P.impact\r\n" + 
			"where S.isActive = 1 and P.isActive = 1;";

	/**
	 * @param service_ID
	 *            (byte)
	 * @return Table containing the columns:
	 * 	<li>{@code division_name} (String)</li> <li>{@code division_shortName} (String)</li>
	 * <li>{@code department_name} (String)</li> <li>{@code department_shortName} (String)</li>
	 * <li>{@code service_code} (String)</li> <li>{@code service_name} (String)</li>
	 */
	public static final String getDepartmentsByService = "select Di.division_name, Di.shortName 'division_shortName', De.department_name, \r\n" + 
			"De.shortName 'department_shortName', S.service_code, S.service_name\r\n" + 
			"from tblService_Department SD join tblDepartment De on SD.devision_name = De.division_name\r\n" + 
			"and SD.department_name = De.department_name\r\n" + 
			"join tblService S on SD.service_ID = S.service_id\r\n" + 
			"join tblDivision Di on SD.devision_name = Di.division_name \r\n" + 
			"where SD.service_ID = ? and SD.isActive = 1 and De.isActive = 1 and S.isActive = 1 and Di.isActive = 1\r\n" + 
			"group by division_name,department_name,service_name;";

	public static final String eventsInTime = "select I.time,S.event_id from tblIncident I join tblCMDB CMDB on I.ci_id = CMDB.ci_id\r\n" + 
			"join tblService S on CMDB.service_id = S.service_id\r\n" + 
			"where I.isActive = 1 and S.isActive = 1\r\n" + 
			"order by time;";
}
