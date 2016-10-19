package vis_utils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import log.FilesUtils;
import log.IncidentLog;
import log.LogUtils;
import log.Settings;
import log.SimulationLog;
import log.TeamLog;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import utils.DepartmentService;
import utils.Queries;

import com.daoImpl.TblServiceDaoImpl;
import com.jdbc.DBUtility;
import com.model.TblService;

public class DataMaker {
	private static Settings settings;

	private static Settings getSettings(String courseName) {
		if (DataMaker.settings == null) {
			DataMaker.settings = FilesUtils.openSettings(courseName);
		}
		return DataMaker.settings;
	}
	
	public static JSONObject getGeneralData(String courseName){
		JSONObject obj = new JSONObject();
		
		int round = Collections.max(FilesUtils.openSettings(courseName).getRoundsDone());
		obj.put("incidents",LogUtils.getIncidentsInRound().get(round));
		obj.put("services", new TblServiceDaoImpl().getActiveServiceCount());
		obj.put("rounds", settings.getRounds());
		obj.put("round_time", settings.getRoundTime());
		obj.put("rounds_done", settings.getRoundsDone().size());
		return obj;
	}
	
	@SuppressWarnings("unchecked")
	public static JSONArray getBizUnits(boolean isAbbreviated,
			boolean isHierachical, byte service_id) {

		List<DepartmentService> all_departments = getServicesAndTheirRelatedOrgUnits(
				isAbbreviated, service_id, true);

		// converts the HashMap to a JSONArray
		JSONArray divisions = new JSONArray();
		if (all_departments != null) {
			if (isHierachical) {
				String previous_division_name = "";
				String previous_department_name = "";
				JSONObject previous_division = null;
				JSONObject previous_department = null;
				JSONObject division = null;
				JSONObject department = null;
				boolean newDivision = false;
				boolean newDepartment = false;

				for (DepartmentService dep_ser : all_departments) {
					String division_name = dep_ser.getDepartmentName().split(
							"-")[0];
					newDivision = !division_name.equals(previous_division_name);
					if (newDivision) {
						division = new JSONObject();
						division.put("division", division_name);
						division.put("departments", new JSONArray());

					}
					String department_name = dep_ser.getDepartmentName().split(
							"-")[1];
					newDepartment = (newDivision) ? true : !department_name
							.equals(previous_department_name);
					if (newDepartment) {
						if (!newDivision && previous_department != null) {
							((JSONArray) division.get("departments"))
									.add(previous_department);
						}
						department = new JSONObject();
						department.put("department", department_name);
						department.put("services", new JSONArray());

					}

					if (newDivision && previous_division != null) {
						if (previous_department != null) {
							((JSONArray) previous_division.get("departments"))
									.add(previous_department);
						}
						divisions.add(previous_division);
					}

					String service_name = dep_ser.getServiceName();
					((JSONArray) department.get("services")).add(service_name);

					previous_division_name = division_name;
					previous_division = division;
					previous_department_name = department_name;
					previous_department = department;
				}
				((JSONArray) previous_division.get("departments"))
						.add(previous_department);
				divisions.add(previous_division);

			} else {
				// flat
				for (DepartmentService dep_ser : all_departments) {
					divisions.add(dep_ser.toString());
				}
			}
		}
		return divisions;
	}

	/**
	 * Generates a file named <b>ITBudgetBreakdown.csv</b> in the format
	 * <b>bizUnitName,gain</b>.
	 * 
	 * @param courseName
	 *            The name of The course
	 * @param round
	 *            The Round
	 * @param team
	 *            The team name
	 * @return
	 */
	
	@SuppressWarnings("unchecked")
	public static JSONArray generateITBudgetBreakdown(String courseName,
			int round, String team, byte service_id, boolean isAbbreviated) {
		
		
		// Initializes an array of rounds
		Set<Integer> rounds = getRounds(round, getSettings(courseName)
				.getRoundsDone());
		List<DepartmentService> departmentServiceArr = null;
		SimulationLog simLog;
		TeamLog marom = null;
		TeamLog rakia = null;

		// Iterates the rounds
		if (rounds != null) {
			for (Integer r : rounds) {
				simLog = FilesUtils.openLog(courseName, r);
				switch (team) {
				case "marom":
					marom = simLog.getTeam(log.SimulationLog.MAROM);
					break;
				case "rakia":
					rakia = simLog.getTeam(log.SimulationLog.RAKIA);
					break;
				default:
					// both teams;
					marom = simLog.getTeam(log.SimulationLog.MAROM);
					rakia = simLog.getTeam(log.SimulationLog.RAKIA);
					break;
				}

				// Adds the expense of the team in the rounds to the variable
				// 'BizUnitService'
				departmentServiceArr = getServicesAndTheirRelatedOrgUnits(
						isAbbreviated, service_id, false);
				if (departmentServiceArr != null) {
					for (DepartmentService bus : departmentServiceArr) {
						double maromExpense;
						double rakiaExpense;

						switch (team) {
						case "marom":
							maromExpense = marom.getService_log(
									bus.getServiceId()).getExpense();
							bus.addServiceExpense(maromExpense);
							break;
						case "rakia":
							rakiaExpense = rakia.getService_log(
									bus.getServiceId()).getExpense();
							bus.addServiceExpense(rakiaExpense);
							break;
						default:
							maromExpense = marom.getService_log(
									bus.getServiceId()).getExpense();
							rakiaExpense = rakia.getService_log(
									bus.getServiceId()).getExpense();
							bus.addServiceExpense((maromExpense + rakiaExpense) / 2);
							break;
						}
					}
				}
			}

			JSONArray data = new JSONArray();
			// Writes a new BizUnitService object list to array
			if (departmentServiceArr != null) {
				for (DepartmentService bus : departmentServiceArr) {
					JSONArray item = new JSONArray();
					item.add(bus.getDepartmentName()
							+ ((service_id == 0) ? "-" + bus.getServiceName()
									: ""));
					item.add(fmt(bus.getExpense()));
					data.add(item);
				}
			}

			/*
			 * final String FILE_HEADER = "bizUnitName,gain"; final String
			 * FILE_NAME = "ITBudgetBreakdown.csv";
			 * 
			 * FileWriter fileWriter = null;
			 * 
			 * try { fileWriter = new FileWriter("C:" + File.separator +
			 * "SIMULATOR" + File.separator + FILE_NAME); // Writes the CSV file
			 * header fileWriter.append(FILE_HEADER.toString()); // Adds a new
			 * line separator after the header
			 * fileWriter.append(NEW_LINE_SEPARATOR);
			 * 
			 * // Writes a new BizUnitService object list to the CSV file if
			 * (departmentServiceArr != null) { for (DepartmentService bus :
			 * departmentServiceArr) { fileWriter.append(bus.getDepartmentName()
			 * + ((service_id == 0) ? "-" + bus.getServiceName() : ""));
			 * fileWriter.append(COMMA_DELIMITER);
			 * fileWriter.append(fmt(bus.getExpense()));
			 * fileWriter.append(NEW_LINE_SEPARATOR); } }
			 * System.out.println("DataMaker: CSV file was created."); } catch
			 * (Exception e) {
			 * System.err.println("DataMaker: Error in CsvFileWriter.");
			 * e.printStackTrace(); } finally { try { fileWriter.flush();
			 * fileWriter.close(); } catch (IOException e) { System.err
			 * .println("DataMaker: Error while flushing/closing fileWriter");
			 * e.printStackTrace(); } }
			 */
			return data;
		} else
			return null;
	}

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

		SimulationLog simLog = FilesUtils.openLog(courseName, round);
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
	public static JSONObject getMTBFPerRound(String courseName, Byte service) {

		JSONObject obj = new JSONObject();
		JSONArray labels = new JSONArray();
		JSONArray arrMarom = null;
		JSONArray arrRakia = null;
		arrMarom = new JSONArray();
		arrRakia = new JSONArray();

		Set<Integer> rounds = getRounds(0, getSettings(courseName)
				.getRoundsDone());

		if (rounds != null) {
			for (int r : rounds) {
				SimulationLog simLog = FilesUtils.openLog(courseName, r);
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
		}
		obj.put("maromData", arrMarom);
		obj.put("rakiaData", arrRakia);
		obj.put("labels", labels);
		return obj;
	}

	@SuppressWarnings("unchecked")
	public static JSONObject getMTBFPerTeam(String courseName, Byte service) {

		JSONObject obj = new JSONObject();
		JSONArray labels = new JSONArray();
		labels.add("Marom");
		labels.add("Rakia");

		Set<Integer> rounds = getRounds(0, getSettings(courseName)
				.getRoundsDone());

		if (rounds != null) {
			for (int r : rounds) {
				JSONArray roundArr = new JSONArray();

				SimulationLog simLog = FilesUtils.openLog(courseName, r);
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
		}
		return obj;
	}

	@SuppressWarnings("unchecked")
	public static JSONObject getMTBFPerService(String courseName, Integer round) {

		JSONObject obj = new JSONObject();
		JSONArray labels = new JSONArray();
		List<TblService> services = new TblServiceDaoImpl().getAllActiveServices();

		Set<Integer> rounds = getRounds(0, getSettings(courseName)
				.getRoundsDone());

		ArrayList<ArrayList<Double>> mtbfAllRoundsMarom = new ArrayList<>();
		ArrayList<ArrayList<Double>> mtbfAllRoundsRakia = new ArrayList<>();

		JSONArray mtbfAvgMarom = new JSONArray();
		JSONArray mtbfAvgRakia = new JSONArray();

		boolean firstIteration = true;
		if (rounds != null && services != null) {
			for (int r : rounds) {

				// quits calculation of unselected rounds
				if (round != 0 && round != r) {
					continue;
				}

				ArrayList<Double> roundMTBFMarom = new ArrayList<>();
				ArrayList<Double> roundMTBFRakia = new ArrayList<>();
				SimulationLog simLog = FilesUtils.openLog(courseName, r);

				for (TblService service : services) {

					TeamLog marom = simLog.getTeam(log.SimulationLog.MAROM);
					TeamLog rakia = simLog.getTeam(log.SimulationLog.RAKIA);

					double mtbfMarom = marom.getService_log(
							service.getServiceId()).getMTBF();
					double mtbfRakia = rakia.getService_log(
							service.getServiceId()).getMTBF();

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
		}
		mtbfAvgMarom = calcAVG(mtbfAllRoundsMarom);
		mtbfAvgRakia = calcAVG(mtbfAllRoundsRakia);

		obj.put("maromData", mtbfAvgMarom);
		obj.put("rakiaData", mtbfAvgRakia);
		obj.put("labels", labels);
		return obj;
	}

	// ****************** MTRS ******************

	@SuppressWarnings("unchecked")
	public static JSONObject getMTRSPerRound(String courseName, Byte service) {

		JSONObject obj = new JSONObject();
		JSONArray labels = new JSONArray();
		JSONArray arrMarom = null;
		JSONArray arrRakia = null;
		arrMarom = new JSONArray();
		arrRakia = new JSONArray();

		Set<Integer> rounds = getRounds(0, getSettings(courseName)
				.getRoundsDone());

		if (rounds != null) {
			for (int r : rounds) {

				SimulationLog simLog = FilesUtils.openLog(courseName, r);
				TeamLog marom = simLog.getTeam(log.SimulationLog.MAROM);
				TeamLog rakia = simLog.getTeam(log.SimulationLog.RAKIA);

				double mtrsMarom;
				double mtrsRakia;
				if (service == null || service == 0) {
					// all services
					mtrsMarom = simLog.getMTRS(log.SimulationLog.MAROM);
					mtrsRakia = simLog.getMTRS(log.SimulationLog.RAKIA);
				} else {
					// selected service
					mtrsMarom = marom.getService_log(service).getMTRS();
					mtrsRakia = rakia.getService_log(service).getMTRS();
				}

				arrMarom.add(mtrsMarom);
				arrRakia.add(mtrsRakia);
				labels.add("Round " + r);
			}
		}
		obj.put("maromData", arrMarom);
		obj.put("rakiaData", arrRakia);
		obj.put("labels", labels);
		return obj;
	}

	@SuppressWarnings("unchecked")
	public static JSONObject getMTRSPerTeam(String courseName, Byte service) {

		JSONObject obj = new JSONObject();
		JSONArray labels = new JSONArray();
		labels.add("Marom");
		labels.add("Rakia");

		Set<Integer> rounds = getRounds(0, getSettings(courseName)
				.getRoundsDone());

		if (rounds != null) {
			for (int r : rounds) {
				JSONArray roundArr = new JSONArray();

				SimulationLog simLog = FilesUtils.openLog(courseName, r);
				TeamLog marom = simLog.getTeam(log.SimulationLog.MAROM);
				TeamLog rakia = simLog.getTeam(log.SimulationLog.RAKIA);

				double mtrsMarom;
				double mtrsRakia;
				if (service == null || service == 0) {
					// all services
					mtrsMarom = simLog.getMTRS(log.SimulationLog.MAROM);
					mtrsRakia = simLog.getMTRS(log.SimulationLog.RAKIA);
				} else {
					// selected service
					mtrsMarom = marom.getService_log(service).getMTRS();
					mtrsRakia = rakia.getService_log(service).getMTRS();
				}

				roundArr.add(mtrsMarom);
				roundArr.add(mtrsRakia);
				obj.put("round#" + r, roundArr);
				obj.put("labels", labels);
			}
		}
		return obj;
	}

	@SuppressWarnings("unchecked")
	public static JSONObject getMTRSPerService(String courseName, Integer round) {

		JSONObject obj = new JSONObject();
		JSONArray labels = new JSONArray();
		List<TblService> services = new TblServiceDaoImpl().getAllActiveServices();

		// set of all requested rounds
		Set<Integer> rounds = getRounds(round, getSettings(courseName)
				.getRoundsDone());

		ArrayList<ArrayList<Double>> mtrsAllRoundsMarom = new ArrayList<>();
		ArrayList<ArrayList<Double>> mtrsAllRoundsRakia = new ArrayList<>();

		JSONArray mtrsAvgMarom = new JSONArray();
		JSONArray mtrsAvgRakia = new JSONArray();

		boolean firstIteration = true;
		if (rounds != null) {
			for (Integer r : rounds) {

				// quits calculation of unselected rounds
				if (round != 0 && round != r) {
					continue;
				}

				ArrayList<Double> roundMTRSMarom = new ArrayList<>();
				ArrayList<Double> roundMTRSRakia = new ArrayList<>();
				SimulationLog simLog = FilesUtils.openLog(courseName, r);

				for (TblService service : services) {

					TeamLog marom = simLog.getTeam(log.SimulationLog.MAROM);
					TeamLog rakia = simLog.getTeam(log.SimulationLog.RAKIA);

					double mtrsMarom = marom.getService_log(
							service.getServiceId()).getMTRS();
					double mtrsRakia = rakia.getService_log(
							service.getServiceId()).getMTRS();

					roundMTRSMarom.add(mtrsMarom);
					roundMTRSRakia.add(mtrsRakia);

					if (firstIteration) {
						labels.add(service.getServiceName());
					}
				}
				mtrsAllRoundsMarom.add(roundMTRSMarom);
				mtrsAllRoundsRakia.add(roundMTRSRakia);
				firstIteration = false;
			}
		}
		mtrsAvgMarom = calcAVG(mtrsAllRoundsMarom);
		mtrsAvgRakia = calcAVG(mtrsAllRoundsRakia);

		obj.put("maromData", mtrsAvgMarom);
		obj.put("rakiaData", mtrsAvgRakia);
		obj.put("labels", labels);
		return obj;
	}

	// ************** END MTRS ******************

	@SuppressWarnings("unchecked")
	private static JSONArray calcAVG(ArrayList<ArrayList<Double>> arr_all_rounds) {

		// total number of services
		int num_of_services = arr_all_rounds.get(0).size();

		// array of MTBF sums of all the rounds
		ArrayList<Double> sum_mtbf_all_rounds = new ArrayList<>(num_of_services);

		// array of average MTBFs for all rounds
		JSONArray avg_mtbf_rounds = new JSONArray();

		// initializes sum_mtbf_all_rounds with zero per service
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
		for (double sum_mtbf : sum_mtbf_all_rounds) {
			avg_mtbf_rounds.add(sum_mtbf / rounds);
		}
		return avg_mtbf_rounds;
	}

	public static void getMTRSStatistics() {

		ArrayList<Double> data = new ArrayList<Double>();

		String[] courses = FilesUtils.getCourses();

		if (courses != null) {
			for (String course : courses) {
				Set<Integer> rounds = getRounds(0,
						FilesUtils.openSettings(course).getRoundsDone());

				if (rounds != null) {
					for (int r : rounds) {
						SimulationLog simLog = FilesUtils.openLog(course, r);
						data.add(simLog.getMTRS(SimulationLog.MAROM));
						data.add(simLog.getMTRS(SimulationLog.RAKIA));
					}
				}
			}
		}

		Statistics stat = new Statistics(data);
		System.out.println(stat);
	}

	@SuppressWarnings("unchecked")
	public static JSONObject getMTRSPieData(String courseName, String team,
			int round, byte service_id, ArrayList<String> ranges) {

		// set of all requested rounds
		Set<Integer> rounds = getRounds(round, getSettings(courseName)
				.getRoundsDone());

		// Array of all ranges and their counts
		RangeCountArray rca = new RangeCountArray();

		// HashMap of all services and their incidents
		HashMap<Byte, HashSet<Byte>> service_cis = LogUtils
				.getDBAffectedServices();
		// Adds all the selected ranges to the RangeCountArray (variable
		// 'rca')
		if (ranges != null) {
			for (String range : ranges) {
				rca.addRange(range);
			}
		}

		// Checks the validity of the array
		if (!rca.isValid()) {
			return null;
		}

		if (rounds != null) {
			for (int r : rounds) {

				// The course's simulation log
				SimulationLog simLog = FilesUtils.openLog(courseName, r);

				// The constant of the team name (null means both teams)
				Boolean teamConst = SimulationLog.getTeamConst(team);

				// HashMap of all relevant incident logs
				HashSet<IncidentLog> relevant_inc_logs = new HashSet<>();

				// HashMap of all the incident logs (some may be irrelevant)
				HashSet<IncidentLog> team_inc_logs = new HashSet<>();

				if (teamConst != null) {
					// specific team - adds the team's incident logs to
					// variable team_inc_log
					team_inc_logs = (HashSet<IncidentLog>) simLog
							.getTeam(teamConst).getClosedIncidentLogs()
							.values();
				} else {
					// both teams - adds both teams' incident logs to variable
					// team_inc_log
					team_inc_logs.addAll(simLog.getTeam(SimulationLog.MAROM)
							.getClosedIncidentLogs().values());
					team_inc_logs.addAll(simLog.getTeam(SimulationLog.RAKIA)
							.getClosedIncidentLogs().values());
				}

				if (team_inc_logs != null) {
					if (service_id != 0) {
						// Specific service - takes only relevant logs and adds
						// them to variable relevant_inc_logs
						for (IncidentLog inc_log : team_inc_logs) {
							if (service_cis.get(service_id).contains(
									inc_log.getCiId())) {
								relevant_inc_logs.add(inc_log);
							}
						}
					} else {
						// All services - all the team's incident logs are
						// relevant
						relevant_inc_logs.addAll(team_inc_logs);
					}
				}

				// Adds all the relevant_inc_logs TRS values to the variable
				// 'rca'
				if (relevant_inc_logs != null) {
					for (IncidentLog inc_log : relevant_inc_logs) {
						rca.addValue(inc_log.getDuration());
					}
				}
			}
		}

		// Builds the JSON output based on the variable 'rca' calculations
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
			if (rangeCountAL == null) {
				return;
			}
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
			if (rangeCountAL == null || rangeCountAL.isEmpty()) {
				return false;
			}

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
			if (rangeCountAL != null) {
				return;
			}

			for (int i = 0; i < rangeCountAL.size(); i++) {
				if (rangeCountAL.get(i).getRange().inRange(val)) {
					rangeCountAL.get(i).addCount(1);
				}
			}

		}

		ArrayList<String> getRanges() {
			if (rangeCountAL == null) {
				return null;
			}

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

	private static HashSet<Integer> getRounds(int round,
			HashSet<Integer> roundsDone) {

		if (round == 0) {
			return roundsDone;
		}

		try {
			if (roundsDone.contains(round)) {
				return new HashSet<>(Arrays.asList(new Integer[] { round }));
			} else {
				throw new Exception("DataMaker: Requested round (" + round
						+ ") was not simulated.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * @return A {@code List} of {@code BizUnitService} objects.
	 */
	public static List<DepartmentService> getServicesAndTheirRelatedOrgUnits(
			boolean isAbbreviated, byte service_id, boolean isSorted) {

		ArrayList<DepartmentService> result = new ArrayList<>();
		try {
			DepartmentService departmentService;
			List<TblService> all_services = (service_id == 0) ? new TblServiceDaoImpl()
					.getAllActiveServices() : new ArrayList<>(
					Arrays.asList(new TblService[] { new TblServiceDaoImpl()
							.getServiceById(service_id) }));

			if (all_services == null) {
				return null;
			}
			List<DepartmentService> temp;
			PreparedStatement pstmt1;
			ResultSet rs;
			for (TblService ser : all_services) {

				pstmt1 = DBUtility.getConnection().prepareStatement(
						Queries.getDepartmentsByService);

				pstmt1.setByte(1, ser.getServiceId());
				rs = pstmt1.executeQuery();
				temp = new ArrayList<>();
				while (rs.next()) {
					String division_name = (isAbbreviated) ? rs
							.getString("division_shortName") : rs
							.getString("division_name");
					String department_name = (isAbbreviated) ? rs
							.getString("department_shortName") : rs
							.getString("division_name");
					String service_name = (isAbbreviated) ? rs
							.getString("service_code") : rs
							.getString("service_name");

					departmentService = new DepartmentService();
					departmentService.setDepartmentName(division_name + "-"
							+ department_name);
					departmentService.setserviceId(ser.getServiceId());
					departmentService.setserviceName(service_name);
					temp.add(departmentService);
				}

				double departmentPercentage = (temp.size() == 0) ? 1
						: 1.d / temp.size();
				for (DepartmentService ds : temp) {
					ds.setPercentage(departmentPercentage);
					result.add(ds);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if (isSorted) {
			Collections.sort(result, new Comparator<DepartmentService>() {
				@Override
				public int compare(DepartmentService arg0,
						DepartmentService arg1) {
					return arg0.toString().compareTo(arg1.toString());
				}
			});
		}
		return result;
	}

	private static String fmt(double d) {
		if (d == (long) d)
			return String.format("%d", (long) d);
		else
			return String.format("%s", d);
	}

	static class Statistics {
		ArrayList<Double> data;
		int size;

		public Statistics(ArrayList<Double> data) {
			this.data = data;
			size = data.size();
		}

		double getMean() {
			double sum = 0.0;
			for (double a : data)
				sum += a;
			return sum / size;
		}

		double getVariance() {
			double mean = getMean();
			double temp = 0;
			for (double a : data)
				temp += (a - mean) * (a - mean);
			return temp / size;
		}

		double getStdDev() {
			return Math.sqrt(getVariance());
		}

		public double median() {
			Collections.sort(data);

			if (data.size() % 2 == 0) {
				return (data.get((data.size() / 2) - 1) + data
						.get(data.size() / 2)) / 2.0;
			} else {
				return data.get(data.size() / 2);
			}
		}

		@Override
		public String toString() {
			return "Statistics [size=" + size + ", Mean=" + getMean()
					+ ", Variance=" + getVariance() + ", StdDev=" + getStdDev()
					+ ", Median=" + median() + "]";
		}
	}
}
