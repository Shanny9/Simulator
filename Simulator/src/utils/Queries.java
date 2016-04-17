package utils;

public class Queries {
	
	public static String incidentsForHomeTable = "select I.time_, CI1.CI_ID from SIMULATOR.tblIncident I\r\n" + 
			"join SIMULATOR.tblCI CI1 on I.root_CI_ID = CI1.CI_ID Order By I.time_";

}
