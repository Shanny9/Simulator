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
	public static final String solutionsForClient = "select I.ci_id, Sol.solution_id, Sol.solution_marom, Sol.solution_rakia,\r\n" + 
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
	
	
	public static final String service_prioritization = "select S.service_id, S.service_code, S.service_name, S.urgency,S.impact, S.isTechnical, P.priorityName\r\n" + 
			"	from tblService S join tblPriority P on S.urgency = P.urgency and S.impact = P.impact\r\n" + 
			"	where S.isActive = 1 and P.isActive = 1;";
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
	
	public static final String ci_events = "select CI.ci_id, S.event_id\r\n" + 
			"from tblCI CI join tblCMDB CMDB on CI.ci_id = CMDB.ci_id\r\n" + 
			"join tblService S on CMDB.service_id = S.service_id\r\n" + 
			"where CI.isActive = 1 and CMDB.isActive = 1 and S.isActive = 1\r\n" + 
			"order by ci_id";
	
	public static final String ci_sol_costs = "select CI.ci_id, Sup.solution_cost * C.value as 'solution_cost'\r\n" + 
			"from tblCI CI join tblSupplier Sup on CI.supplier_level2 = Sup.supplier_name\r\n" + 
			"	join tblCurrency C on Sup.currency = C.currency\r\n" + 
			"where CI.isActive = 1 and Sup.isActive = 1 and C.isActive = 1";
	
	public static final String incidents_flow = "select I.time,\r\n" + 
			"	S.event_id, \r\n" + 
			"	I.ci_id, \r\n" + 
			"	CI.ci_name, \r\n" + 
			"	S.service_id, \r\n" + 
			"	S.service_code,\r\n" + 
			"    Sol.solution_id,\r\n" + 
			"	Sol.solution_marom,\r\n" + 
			"	Sol.solution_rakia,\r\n" + 
			"	P.priorityName,\r\n" + 
			"	PC.pCost,\r\n" + 
			"	S.fixed_income,\r\n" + 
			"	Sup.solution_cost * C.value as 'solution_cost',\r\n" + 
			"    case when I.ci_id in (SELECT CMDB.ci_id \r\n" + 
			"		from SIMULATOR.tblCMDB CMDB\r\n" + 
			"		where CMDB.isActive = 1\r\n" + 
			"		group by CMDB.ci_id\r\n" + 
			"		having count(CMDB.service_id) > 1) then 1 else 0 end as 'isSystematic'\r\n" + 
			"		from tblIncident I join tblCI CI on I.ci_id = CI.ci_id\r\n" + 
			"	join tblSolution Sol on CI.solution_id = Sol.solution_id\r\n" + 
			"	join tblSupplier Sup on CI.supplier_level2  = Sup.supplier_name\r\n" + 
			"	join tblCMDB CMDB on CI.ci_id = CMDB.ci_id\r\n" + 
			"	join tblService S on CMDB.service_id = S.service_id\r\n" + 
			"	join tblPriority P on S.urgency = P.urgency and S.impact = P.impact\r\n" + 
			"	join tblPriority_Cost PC on P.priorityName = PC.pName\r\n" + 
			"	join tblCurrency C on Sup.currency = C.currency\r\n" + 
			"where I.isActive = 1 \r\n" + 
			"	and CI.isActive = 1 \r\n" + 
			"	and Sol.isActive = 1 \r\n" + 
			"	and Sup.isActive = 1 \r\n" + 
			"	and CMDB.isActive = 1 \r\n" + 
			"	and S.isActive = 1\r\n" + 
			"	and P.isActive = 1\r\n" + 
			"	and PC.isActive = 1\r\n" + 
			"	and C.isActive = 1\r\n" + 
			"order by time";
}
