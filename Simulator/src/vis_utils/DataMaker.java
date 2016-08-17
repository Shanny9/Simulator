package vis_utils;

import java.text.DecimalFormat;

import org.json.simple.JSONObject;

import log.SimulationLog;
import log.TeamLog;

public class DataMaker {
	
	@SuppressWarnings("unchecked")
	public static JSONObject getTeamMT(String courseName, int round){
		JSONObject jsonMarom = new JSONObject();
		JSONObject jsonRakia = new JSONObject();
		JSONObject output = new JSONObject();
		
		SimulationLog simLog = log.LogUtils.openLog(courseName, round);
		TeamLog marom = simLog.getTeam(log.SimulationLog.MAROM);
		TeamLog rakia = simLog.getTeam(log.SimulationLog.RAKIA);
		
		DecimalFormat df = new DecimalFormat();
		df.setMaximumFractionDigits(2);
		
		double marom_upTimePercentage = marom.getUpTimePercentage();
		double marom_mtbf = marom.getMTBF();
		double marom_mtrs = marom.getMTRS();
		
		jsonMarom.put("Availability", df.format(marom_upTimePercentage));
		jsonMarom.put("Breakdown", df.format(1-marom_upTimePercentage));
		jsonMarom.put("MTBF", marom_mtbf);
		jsonMarom.put("MTRS", marom_mtrs);
		
		double rakia_upTimePercentage = rakia.getUpTimePercentage();
		double rakia_mtbf = rakia.getMTBF();
		double rakia_mtrs = rakia.getMTRS();
		
		jsonRakia.put("Availability", df.format(rakia_upTimePercentage));
		jsonRakia.put("Breakdown", df.format(1-rakia_upTimePercentage));
		jsonRakia.put("MTBF", rakia_mtbf);
		jsonRakia.put("MTRS", rakia_mtrs);
		
		output.put("marom", jsonMarom);
		output.put("rakia", jsonRakia);
		return output;
	}
}
