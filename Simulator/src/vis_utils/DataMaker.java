package vis_utils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import log.IncidentLog;
import log.LogUtils;
import log.Settings;
import log.SimulationLog;
import log.TeamLog;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.daoImpl.TblServiceDaoImpl;
import com.model.TblService;

public class DataMaker {

	@SuppressWarnings("unchecked")
	/**
	 * 
	 * @param courseName The name of the course
	 * @param round the round number
	 * @return data for pie chart
	 */
	public static JSONObject getTeamMT(String courseName, int round) {

		JSONObject jsonMarom = new JSONObject();
		JSONObject jsonRakia = new JSONObject();
		JSONObject output = new JSONObject();

		ArrayList<String> labels = new ArrayList<>(Arrays.asList(
				"Availability", "Breakdown"));
		ArrayList<String> percentages_marom = new ArrayList<>();
		ArrayList<String> percentages_rakia = new ArrayList<>();

		SimulationLog simLog = log.LogUtils.openLog(courseName, round);
		TeamLog marom = simLog.getTeam(log.SimulationLog.MAROM);
		TeamLog rakia = simLog.getTeam(log.SimulationLog.RAKIA);

		DecimalFormat df = new DecimalFormat();
		df.setMaximumFractionDigits(2);

		double marom_upTimePercentage = marom.getUpTimePercentage();
		percentages_marom.add(df.format(marom_upTimePercentage));
		percentages_marom.add(df.format(1 - marom_upTimePercentage));

		double rakia_upTimePercentage = rakia.getUpTimePercentage();
		percentages_rakia.add(df.format(rakia_upTimePercentage));
		percentages_rakia.add(df.format(1 - rakia_upTimePercentage));

		jsonMarom.put("labels", labels);
		jsonMarom.put("percentages", percentages_marom);

		jsonRakia.put("labels", labels);
		jsonRakia.put("percentages", percentages_rakia);

		output.put("marom", jsonMarom);
		output.put("rakia", jsonRakia);
		return output;
	}

	@SuppressWarnings("unchecked")
	public static JSONObject getMTBFPerRound(String courseName, Integer service) {

		JSONObject obj = new JSONObject();
		JSONArray labels = new JSONArray();
		JSONArray arrMarom = null;
		JSONArray arrRakia = null;
		arrMarom = new JSONArray();
		arrRakia = new JSONArray();

		Settings settings = LogUtils.openSettings(courseName);
		int lastRoundDone = settings.getLastRoundDone();

		for (int r = 1; r <= lastRoundDone; r++) {

			SimulationLog simLog = log.LogUtils.openLog(courseName, r);
			TeamLog marom = simLog.getTeam(log.SimulationLog.MAROM);
			TeamLog rakia = simLog.getTeam(log.SimulationLog.RAKIA);

			double mtbfMarom;
			double mtbfRakia;
			if (service == null || service == 0) {
				// all services
				mtbfMarom = simLog.getMTBF(log.SimulationLog.MAROM);
				mtbfRakia = simLog.getMTBF(log.SimulationLog.RAKIA);
			} else {
				// selected service
				mtbfMarom = marom.getService_log(service).getMTBF();
				mtbfRakia = rakia.getService_log(service).getMTBF();
			}

			arrMarom.add(mtbfMarom);
			arrRakia.add(mtbfRakia);
			labels.add("Round " + r);
		}
		obj.put("maromData", arrMarom);
		obj.put("rakiaData", arrRakia);
		obj.put("labels", labels);
		return obj;
	}

	@SuppressWarnings("unchecked")
	public static JSONObject getMTBFPerTeam(String courseName, Integer service) {

		JSONObject obj = new JSONObject();
		JSONArray labels = new JSONArray();
		labels.add("Marom");
		labels.add("Rakia");

		Settings settings = LogUtils.openSettings(courseName);
		int lastRoundDone = settings.getLastRoundDone();

		for (int r = 1; r <= lastRoundDone; r++) {
			JSONArray roundArr = new JSONArray();

			SimulationLog simLog = log.LogUtils.openLog(courseName, r);
			TeamLog marom = simLog.getTeam(log.SimulationLog.MAROM);
			TeamLog rakia = simLog.getTeam(log.SimulationLog.RAKIA);

			double mtbfMarom;
			double mtbfRakia;
			if (service == null || service == 0) {
				// all services
				mtbfMarom = simLog.getMTBF(log.SimulationLog.MAROM);
				mtbfRakia = simLog.getMTBF(log.SimulationLog.RAKIA);
			} else {
				// selected service
				mtbfMarom = marom.getService_log(service).getMTBF();
				mtbfRakia = rakia.getService_log(service).getMTBF();
			}

			roundArr.add(mtbfMarom);
			roundArr.add(mtbfRakia);
			obj.put("round#" + r, roundArr);
			obj.put("labels", labels);
		}
		return obj;
	}

	@SuppressWarnings("unchecked")
	public static JSONObject getMTBFPerService(String courseName, Integer round) {

		JSONObject obj = new JSONObject();
		JSONArray labels = new JSONArray();
		List<TblService> services = new TblServiceDaoImpl().getAllServices();

		Settings settings = LogUtils.openSettings(courseName);
		int lastRoundDone = settings.getLastRoundDone();
		if (round > lastRoundDone) {
			return null;
		}
		ArrayList<ArrayList<Double>> mtbfAllRoundsMarom = new ArrayList<>();
		ArrayList<ArrayList<Double>> mtbfAllRoundsRakia = new ArrayList<>();

		JSONArray mtbfAvgMarom = new JSONArray();
		JSONArray mtbfAvgRakia = new JSONArray();

		boolean firstIteration = true;
		for (int r = 1; r <= lastRoundDone; r++) {

			// quits calculation of unselected rounds
			if (round != 0 && round != r) {
				continue;
			}

			ArrayList<Double> roundMTBFMarom = new ArrayList<>();
			ArrayList<Double> roundMTBFRakia = new ArrayList<>();
			SimulationLog simLog = log.LogUtils.openLog(courseName, r);

			for (TblService service : services) {

				TeamLog marom = simLog.getTeam(log.SimulationLog.MAROM);
				TeamLog rakia = simLog.getTeam(log.SimulationLog.RAKIA);

				double mtbfMarom = marom.getService_log(service.getServiceId())
						.getMTBF();
				double mtbfRakia = rakia.getService_log(service.getServiceId())
						.getMTBF();

				roundMTBFMarom.add(mtbfMarom);
				roundMTBFRakia.add(mtbfRakia);

				if (firstIteration) {
					labels.add(service.getServiceName());
				}
			}
			mtbfAllRoundsMarom.add(roundMTBFMarom);
			mtbfAllRoundsRakia.add(roundMTBFRakia);
			firstIteration = false;
		}
		mtbfAvgMarom = calcAVG(mtbfAllRoundsMarom);
		mtbfAvgRakia = calcAVG(mtbfAllRoundsRakia);

		obj.put("maromData", mtbfAvgMarom);
		obj.put("rakiaData", mtbfAvgRakia);
		obj.put("labels", labels);
		return obj;
	}

	@SuppressWarnings("unchecked")
	private static JSONArray calcAVG(ArrayList<ArrayList<Double>> arr_all_rounds) {
		/**
		 * total number of services
		 */
		int num_of_services = arr_all_rounds.get(0).size();
		/**
		 * array of MTBF sums of all the rounds
		 */
		ArrayList<Double> sum_mtbf_all_rounds = new ArrayList<>(num_of_services);
		/**
		 * array of average MTBFs for all rounds
		 */
		JSONArray avg_mtbf_rounds = new JSONArray();

		// initializes @sum_mtbf_all_rounds with zero per service
		while (sum_mtbf_all_rounds.size() < num_of_services)
			sum_mtbf_all_rounds.add(0.d);

		int rounds = arr_all_rounds.size();

		// calculates the MTBF sums
		for (int r = 1; r <= rounds; r++) {
			ArrayList<Double> mtbf_round = arr_all_rounds.get(r - 1);
			for (int service = 0; service < mtbf_round.size(); service++) {
				double old_sum = sum_mtbf_all_rounds.get(service);
				sum_mtbf_all_rounds.set(service,
						old_sum + mtbf_round.get(service));
			}
		}

		// calculates the MTBF averages
		for (Double sum_mtbf : sum_mtbf_all_rounds) {
			avg_mtbf_rounds.add(sum_mtbf / rounds);
		}
		return avg_mtbf_rounds;
	}

	public static void getMTRSStatistics() {

		ArrayList<Double> data = new ArrayList<Double>();

		String[] courses = LogUtils.getCourses();
		for (String course : courses) {
			int rounds = LogUtils.openSettings(course).getLastRoundDone();

			for (int r = 1; r <= rounds; r++) {
				SimulationLog simLog = LogUtils.openLog(course, r);
				data.add(simLog.getMTRS(SimulationLog.MAROM));
				data.add(simLog.getMTRS(SimulationLog.RAKIA));
			}
		}

		Statistics stat = new Statistics(data);
		System.out.println(stat);
	}

	@SuppressWarnings("unchecked")
	public static JSONObject getMTRSPieData(String course, String team,
			int round, int service_id, ArrayList<String> ranges) {
		/**
		 * array of all requested rounds
		 */
		ArrayList<Integer> rounds = new ArrayList<>();
		/**
		 * the course's settings
		 */
		Settings settings = LogUtils.openSettings(course);
		/**
		 * the last round of the course
		 */
		int lastRoundDone = settings.getLastRoundDone();

		if (round == 0) {
			// all rounds
			for (int r = 1; r <= lastRoundDone; r++) {
				rounds.add(r);
			}
		} else {
			// one round
			if (lastRoundDone < round) {
				// the requested round was not simulated
				return null;
			} else {
				rounds.add(round);
			}
		}

		/**
		 * array of all ranges and their counts
		 */
		RangeCountArray rca = new RangeCountArray();
		/**
		 * HashMap of all services and their incidents
		 */
		HashMap<Byte, HashSet<Byte>> service_incidents = LogUtils
				.getServiceIncidents();

		for (int r : rounds) {
			/**
			 * the course's simulation log
			 */
			SimulationLog simLog = LogUtils.openLog(course, r);
			/**
			 * the constant of the team name (NULL means both teams)
			 */
			Boolean teamConst = SimulationLog.getTeamConst(team);
			/**
			 * HashMap of all relevant incident logs
			 */
			HashSet<IncidentLog> relevant_inc_logs = new HashSet<>();
			/**
			 * HashMap of all the incident logs (some may be irrelevant)
			 */
			HashSet<IncidentLog> team_inc_logs = new HashSet<>();

			if (teamConst != null) {
				// specific team - adds the team's incident logs to
				// @team_inc_log
				team_inc_logs = (HashSet<IncidentLog>) simLog
						.getTeam(teamConst).getClosedIncident_logs().values();
			} else {
				// both teams - adds both teams' incident logs to @team_inc_log
				team_inc_logs.addAll(simLog.getTeam(SimulationLog.MAROM)
						.getClosedIncident_logs().values());
				team_inc_logs.addAll(simLog.getTeam(SimulationLog.RAKIA)
						.getClosedIncident_logs().values());
			}
			
			if (service_id != 0) {
				// specific service - takes only relevant logs and adds them
				// to @relevant_inc_logs
				for (IncidentLog inc_log : team_inc_logs) {
					if (service_incidents.get(service_id).contains(
							inc_log.getIncident_id())) {
						relevant_inc_logs.add(inc_log);
					}
				}
			} else {
				// all services - all the team's incident logs are relevant
				relevant_inc_logs.addAll(team_inc_logs);
			}
			
			// adds all the selected ranges to the RangeCountArray (@rca)
			for (String range : ranges) {
				rca.addRange(range);
			}
			
			//checks the validity of the array
			if (!rca.isValid()) {
				return null;
			}
			
			// adds all the @relevant_inc_logs TRS values to @rca
			for (IncidentLog inc_log : relevant_inc_logs) {
				rca.addValue(inc_log.getDuration());
			}
		}
		
		//build the json output based on the @rca's calculations
		JSONObject obj = new JSONObject();
		obj.put("labels", rca.getRanges());
		obj.put("data", rca.getValues());
		return obj;
	}

	static class RangeCountArray {
		ArrayList<RangeCount> rangeCountAL;

		RangeCountArray() {
			rangeCountAL = new ArrayList<>();
		}

		void addRange(int min, int max) {
			rangeCountAL.add(new RangeCount(new Range(min, max), 0));
		}

		void addRange(String range) {
			String[] args = range.split("-");
			int arg1 = Integer.valueOf(args[0]);
			int arg2 = Integer.valueOf(args[1]);
			int min = Math.min(arg1, arg2);
			int max = Math.max(arg1, arg2);
			addRange(min, max);
		}

		boolean isValid() {
			if (rangeCountAL.get(0).getRange().getLower() != 0) {
				return false;
			}

			for (RangeCount rc : rangeCountAL) {
				if (!rc.getRange().isValid()) {
					return false;
				}
			}

			for (int i = 1; i < rangeCountAL.size(); i++) {
				if (rangeCountAL.get(i).getRange().getLower() != rangeCountAL
						.get(i - 1).getRange().getUpper()) {
					return false;
				}
			}
			return true;
		}

		void addValue(int val) {
			for (int i = 0; i < rangeCountAL.size(); i++) {
				if (rangeCountAL.get(i).getRange().inRange(val)) {
					rangeCountAL.get(i).addCount(val);
				}
			}
		}

		ArrayList<String> getRanges() {
			ArrayList<String> ranges = new ArrayList<>();
			for (RangeCount rc : rangeCountAL) {
				ranges.add(rc.getRange().toString());
			}
			return ranges;
		}

		ArrayList<Integer> getValues() {
			ArrayList<Integer> values = new ArrayList<>();
			for (RangeCount rc : rangeCountAL) {
				values.add(rc.getCount());
			}
			return values;
		}
	}

	static class RangeCount {

		Range range;
		int count;

		RangeCount(Range range, int count) {
			super();
			this.range = range;
			this.count = count;
		}

		Range getRange() {
			return range;
		}

		void setRange(Range range) {
			this.range = range;
		}

		int getCount() {
			return count;
		}

		void addCount(int count) {
			this.count += count;
		}
	}

	static class Range {
		private int upper;
		private int lower;

		Range(int lower, int upper) {
			super();
			this.upper = upper;
			this.lower = lower;
		}

		int getUpper() {
			return upper;
		}

		void setUpper(int upper) {
			this.upper = upper;
		}

		int getLower() {
			return lower;
		}

		void setLower(int lower) {
			this.lower = lower;
		}

		boolean inRange(int num) {
			return num >= lower && num < upper;
		}

		boolean isValid() {
			return upper > lower;
		}

		public String toString() {
			return lower + "-" + upper;
		}
	}
}
