package log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

import utils.Queries;
import utils.SimulationTime;
import utils.SolutionElement;

import com.dao.TblCIDao;
import com.dao.TblCMDBDao;
import com.daoImpl.TblCIDaoImpl;
import com.daoImpl.TblCMDBDaoImpl;
import com.daoImpl.TblIncidentDaoImpl;
import com.jdbc.DBUtility;
import com.model.TblCI;
import com.model.TblCMDB;
import com.model.TblIncident;

public class LogUtils {

	private static HashMap<Byte, HashSet<Byte>> dbAffectingCis;
	private static HashMap<Byte, HashSet<Byte>> dbAffectedServices;
	private static HashMap<Byte, Double> ciSolCosts;
	private static HashMap<Byte, Double> serviceCosts;
	// private static Collection<IncidentLog> incidentLogs;
	private static HashMap<SimulationTime, HashSet<String>> round_events;
	private static HashMap<Byte, String> servicePriority;
	private static HashMap<SimulationTime, HashSet<Byte>> time_cis;
	private static HashMap<Byte, HashSet<SimulationTime>> cis_time;
	private static HashMap<Byte, SolutionElement> cis_solutions;
	private static HashMap<Integer, Integer> incident_count_in_round;
	private static HashMap<Integer, Byte> question_ci;

	public static void runAll() {
		LogUtils.dbAffectingCis = getDBAffectingCIs();
		LogUtils.dbAffectedServices = getDBAffectedServices();
		LogUtils.ciSolCosts = getCISolCosts();
		LogUtils.serviceCosts = getServiceDownTimeCosts();
		// LogUtils.incidentLogs = getIncidentLogs();
		LogUtils.servicePriority = getServicePriorities();
		LogUtils.time_cis = getTimeCis();
		LogUtils.cis_time = getCisTime();
		LogUtils.cis_solutions = getCiSolutions();
		LogUtils.incident_count_in_round = getIncidentCountInRound();
		LogUtils.question_ci = getQuestionsCis();
	}

	static HashMap<Integer, Byte> getQuestionsCis() {
		if (question_ci != null) {
			return question_ci;
		}

		TblCIDao dao = new TblCIDaoImpl();
		Collection<TblCI> all_cis = dao.getAllActiveCIs();

		if (all_cis == null) {
			return null;
		}

		question_ci = new HashMap<>();
		for (TblCI ci : all_cis) {
			question_ci.put(ci.getSolutionId(), ci.getCiId());
		}
		return question_ci;
	}

	public static HashMap<Byte, SolutionElement> getCiSolutions() {
		if (cis_solutions != null) {
			return cis_solutions;
		}

		HashMap<Byte, SolutionElement> solutions = new HashMap<>();
		try {
			Statement stmt = DBUtility.getConnection().createStatement();
			ResultSet rs = stmt.executeQuery(Queries.solutionsForClient);
			while (rs.next()) {
				solutions.put(
						rs.getByte("ci_id"),
						new SolutionElement(rs.getByte("ci_id"), rs
								.getInt("solution_id"), rs
								.getInt("solution_marom"), rs
								.getInt("solution_rakia"), rs
								.getDouble("solution_cost"), rs
								.getString("currency")));
			}
			return solutions;
		} catch (SQLException e) {
		}
		return null;
	}

	/**
	 * @return A map of the CI's and their affected services
	 */
	static HashMap<Byte, HashSet<Byte>> getDBAffectingCIs() {
		if (dbAffectingCis != null) {
			return dbAffectingCis;
		}

		TblCMDBDao dao = new TblCMDBDaoImpl();
		Collection<TblCMDB> all_cmdbs = dao.getAllActiveCMDBs();

		if (all_cmdbs == null) {
			return null;
		}

		dbAffectingCis = new HashMap<>();
		for (TblCMDB cmdb : all_cmdbs) {
			byte ci = cmdb.getCiId();
			HashSet<Byte> services = dbAffectingCis.get(ci);
			if (services == null) {
				services = new HashSet<Byte>();
			}
			services.add((byte) cmdb.getServiceId());
			dbAffectingCis.put(ci, services);
		}
		return dbAffectingCis;
	}

	/**
	 * @return A map of the services and their affecting CIs
	 */
	public static HashMap<Byte, HashSet<Byte>> getDBAffectedServices() {

		if (dbAffectedServices != null) {
			return dbAffectedServices;
		}

		TblCMDBDao dao = new TblCMDBDaoImpl();
		Collection<TblCMDB> all_cmdbs = dao.getAllActiveCMDBs();

		if (all_cmdbs == null) {
			return null;
		}

		dbAffectedServices = new HashMap<>();
		for (TblCMDB cmdb : all_cmdbs) {
			byte service = cmdb.getServiceId();
			HashSet<Byte> cis = dbAffectedServices.get(service);
			if (cis == null) {
				cis = new HashSet<Byte>();
			}
			cis.add(cmdb.getCiId());
			dbAffectedServices.put(service, cis);
		}
		return dbAffectedServices;
	}

	/**
	 * @return The CIs and their costs
	 */
	static HashMap<Byte, Double> getCISolCosts() {
		if (ciSolCosts != null) {
			return ciSolCosts;
		}

		ciSolCosts = new HashMap<>();
		try {
			Statement stmt = DBUtility.getConnection().createStatement();
			ResultSet rs = stmt.executeQuery(Queries.ci_sol_costs);
			while (rs.next()) {
				ciSolCosts.put(rs.getByte("ci_id"),
						rs.getDouble("solution_cost"));
			}
			return ciSolCosts;
		} catch (SQLException e) {
		}
		return null;
	}

	/**
	 * @return The services' down-time costs
	 */
	static HashMap<Byte, Double> getServiceDownTimeCosts() {
		if (serviceCosts != null) {
			return serviceCosts;
		}

		serviceCosts = new HashMap<>();
		try {
			Statement stmt = DBUtility.getConnection().createStatement();
			ResultSet rs = stmt.executeQuery(Queries.serviceDownCosts);
			while (rs.next()) {
				serviceCosts.put(rs.getByte("service_id"),
						rs.getDouble("down_cost"));
			}
			return serviceCosts;
		} catch (SQLException e) {
		}
		return null;
	}

	// /**
	// * @param round
	// * the current round
	// * @return empty incident logs for the current round
	// */
	// static HashMap<Byte, IncidentLog> getIncidentLogsOfRound(int round) {
	//
	// if (incidentLogs == null) {
	// incidentLogs = getIncidentLogs();
	// }
	//
	// HashMap<Byte, IncidentLog> incidentLogsOfRound = new HashMap<>();
	// for (IncidentLog inc_log : incidentLogs) {
	// if (round == 0 || round == inc_log.getStartTime().getRound()) {
	// IncidentLog log_to_put = incidentLogsOfRound.get(inc_log.getCiId());
	// if (log_to_put == null){
	// log_to_put = inc_log;
	// } else{
	// // gets the first log of the incident
	// SimulationTime inc_log_time = inc_log.getStartTime();
	// SimulationTime log_to_put_time = log_to_put.getStartTime();
	// log_to_put = (inc_log_time.before(log_to_put_time))? inc_log :
	// log_to_put;
	// }
	// incidentLogsOfRound.put(inc_log.getCiId(), log_to_put);
	// }
	// }
	// return incidentLogsOfRound;
	// }

	// /**
	// * Note that 'incidentLog' does not have a unique id (a CI can have
	// multiple
	// * logs.).
	// *
	// * @return empty incident logs for the entire course.
	// */
	// static Collection<IncidentLog> getIncidentLogs() {
	//
	// if (incidentLogs != null) {
	// return incidentLogs;
	// }
	//
	// incidentLogs = new ArrayList<>();
	// Collection<TblIncident> incidents = new TblIncidentDaoImpl()
	// .getAllActiveIncidents();
	// for (TblIncident inc : incidents) {
	// byte ci_id = inc.getCiId();
	// int time = inc.getIncidentTime();
	// TblIncidentPK inc_pk = new TblIncidentPK();
	// inc_pk.setCiId(ci_id);
	// inc_pk.setTime(time);
	// incidentLogs.add(new IncidentLog(inc_pk));
	// }
	// return incidentLogs;
	// }

	/**
	 * @return Incidnets (key=time, value=set of cis)
	 */
	static HashMap<SimulationTime, HashSet<Byte>> getTimeCis() {

		if (time_cis != null) {
			return time_cis;
		}

		time_cis = new HashMap<>();
		Collection<TblIncident> all_incidents = new TblIncidentDaoImpl()
				.getAllActiveIncidents();
		for (TblIncident inc : all_incidents) {
			HashSet<Byte> cis = time_cis.get(inc.getSimulationTime());
			if (cis == null) {
				cis = new HashSet<>();
			}
			cis.add(inc.getCiId());
			time_cis.put(inc.getSimulationTime(), cis);

		}
		return time_cis;
	}

	/**
	 * @return The incidents (key= ci, value= time)
	 */
	static HashMap<Byte, HashSet<SimulationTime>> getCisTime() {

		if (cis_time != null) {
			return cis_time;
		}

		cis_time = new HashMap<>();
		Collection<TblIncident> all_incidents = new TblIncidentDaoImpl()
				.getAllActiveIncidents();
		for (TblIncident inc : all_incidents) {
			HashSet<SimulationTime> times = cis_time.get(inc.getCiId());
			if (times == null) {
				times = new HashSet<>();
			}
			times.add(inc.getSimulationTime());
			cis_time.put(inc.getCiId(), times);

		}
		return cis_time;
	}

	/**
	 * @return The incidents and their events (key= time, value= set of events)
	 */
	public static HashMap<SimulationTime, HashSet<String>> getRoundEvents(
			int round) {
/*		System.out.println("Getting round events for round "+round);
		if (round_events != null) {
			System.out.println("round events is not null: "+round_events);
			return round_events;
		}*/
		round_events = new HashMap<>();
		try {
			PreparedStatement pstmt = DBUtility.getConnection()
					.prepareStatement(Queries.eventsInTime);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				SimulationTime time = new SimulationTime(rs.getInt("time"));
				if (time.getRound() != round) {
					continue;
				}
				HashSet<String> events = round_events.get(time);
				if (events == null) {
					events = new HashSet<String>();
				}
				events.add(String.valueOf(rs.getInt("event_id")));
				round_events.put(time, events);
			}
		} catch (SQLException e) {
		}
		System.out.println("-End of the function, events: "+round_events);
		return round_events;
	}

	/**
	 * @return A map of services and their priorities (key= service, value=
	 *         priority name)
	 */
	static HashMap<Byte, String> getServicePriorities() {

		if (servicePriority != null) {
			return servicePriority;
		}

		servicePriority = new HashMap<>();
		try {
			Statement stmt = DBUtility.getConnection().createStatement();
			ResultSet rs = stmt.executeQuery(Queries.servicePriorities);
			while (rs.next()) {
				servicePriority.put(rs.getByte("service_id"),
						rs.getString("priorityName"));
			}
			return servicePriority;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static HashMap<Integer, Integer> getIncidentCountInRound() {
		if (incident_count_in_round != null) {
			return incident_count_in_round;
		}

		incident_count_in_round = new HashMap<>();
		Collection<TblIncident> incidents = new TblIncidentDaoImpl()
				.getAllActiveIncidents();
		for (TblIncident inc : incidents) {
			int round = inc.getSimulationTime().getRound();
			Integer count = incident_count_in_round.get(round);
			if (count == null) {
				count = 0;
			}
			incident_count_in_round.put(round, ++count);
		}
		return incident_count_in_round;
	}

	public static int getCICountInRound(int round) {
		return getCisInRound(round).size();
	}

	public static HashSet<Byte> getCisInRound(int round) {

		HashSet<Byte> cis_in_round = new HashSet<>();
		Collection<TblIncident> incidents = new TblIncidentDaoImpl()
				.getAllActiveIncidents();
		for (TblIncident inc : incidents) {
			if (inc.getSimulationTime().getRound() == round) {
				cis_in_round.add(inc.getCiId());
			}
		}
		return cis_in_round;
	}
	
	public static int getServiceCountInRound(int round) {
		return getServicesInRound(round).size();
	}

	public static HashSet<Byte> getServicesInRound(int round) {

		HashSet<Byte> services_in_round = new HashSet<>();
		Collection<Byte> cis = getCisInRound(round);
		for (byte ci : cis) {
			services_in_round.addAll(getDBAffectingCIs().get(ci));
		}
		return services_in_round;
	}

	/**
	 * Returns a copy of the object, or null if the object cannot be serialized.
	 */
	public static Object copy(Object orig) {
		Object obj = null;
		try {
			// Write the object out to a byte array
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ObjectOutputStream out = new ObjectOutputStream(bos);
			out.writeObject(orig);
			out.flush();
			out.close();

			// Make an input stream from the byte array and read
			// a copy of the object back in.
			ObjectInputStream in = new ObjectInputStream(
					new ByteArrayInputStream(bos.toByteArray()));
			obj = in.readObject();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException cnfe) {
			cnfe.printStackTrace();
		}
		return obj;
	}
}
