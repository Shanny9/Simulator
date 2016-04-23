package utils;

public class Queries {

	public static String incidentsForHomeTable = "select I.time_, CI1.CI_ID from "
			+ "SIMULATOR.tblIncident I join SIMULATOR.tblCI CI1 on I.root_CI_ID"
			+ " = CI1.CI_ID Order By I.time_";

	public static String serviceCosts = "select S.service_ID, S.fixed_income, S. fixed_cost"
			+ ",P.cost as 'down_cost' from SIMULATOR.tblService S join SIMULATOR.tblPriority P"
			+ " on S.priority = P.priority_number";
}
