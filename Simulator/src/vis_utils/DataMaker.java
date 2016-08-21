package vis_utils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;

import log.LogUtils;
import log.Settings;
import log.SimulationLog;
import log.TeamLog;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class DataMaker {
	
	@SuppressWarnings("unchecked")
	/**
	 * 
	 * @param courseName The name of the course
	 * @param round the round number
	 * @return data for pie chart
	 */
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
		percentages_marom.add(df.format(marom_upTimePercentage));
		percentages_marom.add(df.format(1-marom_upTimePercentage));
			
		double rakia_upTimePercentage = rakia.getUpTimePercentage();		
		percentages_rakia.add(df.format(rakia_upTimePercentage));
		percentages_rakia.add(df.format(1-rakia_upTimePercentage));
		
		jsonMarom.put("labels", labels);
		jsonMarom.put("percentages", percentages_marom);
		
		jsonRakia.put("labels", labels);
		jsonRakia.put("percentages", percentages_rakia);
		
		output.put("marom", jsonMarom);
		output.put("rakia", jsonRakia);
		return output;
	}
	
	@SuppressWarnings("unchecked")
	public static JSONObject getMTBFforLineChart(String courseName, Integer service){
		
		JSONObject obj = new JSONObject();
		JSONArray maromData = new JSONArray();
		JSONArray rakiaData = new JSONArray();
		
		Settings settings = LogUtils.openSettings(courseName);
		int lastRoundDone = settings.getLastRoundDone();
		
		for (int r=1 ; r<=lastRoundDone ; r++){
			SimulationLog simLog = log.LogUtils.openLog(courseName, r);
			TeamLog marom = simLog.getTeam(log.SimulationLog.MAROM);
			TeamLog rakia = simLog.getTeam(log.SimulationLog.RAKIA);
			
			JSONArray arrMarom = new JSONArray();
			JSONArray arrRakia = new JSONArray();
			
			double mtbfMarom;
			double mtbfRakia;
			if (service == null || service == 0){
				// all services
				mtbfMarom = marom.getMTBF();
				mtbfRakia = rakia.getMTBF();
			} else{
				// selected service
				mtbfMarom = marom.getService_log(service).getMTBF();
				mtbfRakia = rakia.getService_log(service).getMTBF();
			}
			
			arrMarom.add(r);
			arrMarom.add(mtbfMarom);
			arrRakia.add(r);
			arrRakia.add(mtbfRakia);
			
			maromData.add(arrMarom);
			rakiaData.add(arrRakia);
			

		}
		obj.put("maromData", maromData);
		obj.put("rakiaData", rakiaData);
		return obj;
		
	}
}
