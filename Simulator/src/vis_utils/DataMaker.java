package vis_utils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;

import log.SimulationLog;
import log.TeamLog;

import org.json.simple.JSONObject;

public class DataMaker {
	
	@SuppressWarnings("unchecked")
	public static JSONObject getTeamMT(String courseName, int round){
		
		JSONObject jsonMarom = new JSONObject();
		JSONObject jsonRakia = new JSONObject();
		JSONObject output = new JSONObject();
		
		ArrayList<String> labels = new ArrayList<>(Arrays.asList("Availability","Breakdown"));
		ArrayList<String> percentages_marom = new ArrayList<>();
		ArrayList<String> percentages_rakia = new ArrayList<>();
		
		SimulationLog simLog = log.LogUtils.openLog(courseName, round);
		TeamLog marom = simLog.getTeam(log.SimulationLog.MAROM);
		TeamLog rakia = simLog.getTeam(log.SimulationLog.RAKIA);
		
		DecimalFormat df = new DecimalFormat();
		df.setMaximumFractionDigits(2);
		
		double marom_upTimePercentage = marom.getUpTimePercentage();
//		double marom_mtbf = marom.getMTBF();
//		double marom_mtrs = marom.getMTRS();
		
		percentages_marom.add(df.format(marom_upTimePercentage));
		percentages_marom.add(df.format(1-marom_upTimePercentage));
	
//		jsonMarom.put("MTBF", marom_mtbf);
//		jsonMarom.put("MTRS", marom_mtrs);
		
		double rakia_upTimePercentage = rakia.getUpTimePercentage();
//		double rakia_mtbf = rakia.getMTBF();
//		double rakia_mtrs = rakia.getMTRS();
		
		percentages_rakia.add(df.format(rakia_upTimePercentage));
		percentages_rakia.add(df.format(1-rakia_upTimePercentage));

//		jsonRakia.put("MTBF", rakia_mtbf);
//		jsonRakia.put("MTRS", rakia_mtrs);
		
		jsonMarom.put("labels", labels);
		jsonMarom.put("percentages", percentages_marom);
		
		jsonRakia.put("labels", labels);
		jsonRakia.put("percentages", percentages_rakia);
		
		output.put("marom", jsonMarom);
		output.put("rakia", jsonRakia);
		return output;
	}
}
