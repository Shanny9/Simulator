package log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import utils.Queries;
import utils.SimulationTime;

import com.dao.TblCIDao;
import com.dao.TblCMDBDao;
import com.daoImpl.TblCIDaoImpl;
import com.daoImpl.TblCMDBDaoImpl;
import com.daoImpl.TblIncidentDaoImpl;
import com.daoImpl.TblSupplierDaoImpl;
import com.jdbc.DBUtility;
import com.model.TblCI;
import com.model.TblCMDB;
import com.model.TblIncident;
import com.model.TblSupplier;

public class LogUtils {

	private static HashMap<Byte, HashSet<Byte>> dbAffectingCis;
	private static HashMap<Byte, HashSet<Byte>> dbAffectedServices;
	private static HashMap<Byte, Double> ciSolCosts;
	private static HashMap<Byte, Double> serviceCosts;
	private static HashMap<Byte, IncidentLog> incidentLogs;
	private static HashMap<SimulationTime, HashSet<String>> time_events;
	private static HashMap<Byte, String> servicePriority;
	private static HashMap<SimulationTime, HashSet<Byte>> time_cis;
	private static HashMap<Byte, HashSet<SimulationTime>> cis_time;

	public static void runAll() {
		LogUtils.dbAffectingCis = getDBAffectingCIs();
		LogUtils.dbAffectedServices = getDBAffectedServices();
		LogUtils.ciSolCosts = getCISolCosts();
		LogUtils.serviceCosts = getServiceDownTimeCosts();
		LogUtils.incidentLogs = getIncidentLogs();
		LogUtils.time_events = getTimetEvents();
		LogUtils.servicePriority = getServicePriorities();
		LogUtils.time_cis = getTimeCis();
	}

	/**
	 * @return A map of the CI's and their affected services
	 */
	static HashMap<Byte, HashSet<Byte>> getDBAffectingCIs() {
		if (dbAffectingCis != null) {
			return dbAffectingCis;
		}

		TblCMDBDao dao = new TblCMDBDaoImpl();
		Collection<TblCMDB> all_cmdbs = dao.getAllCMDBs();

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

	// /**
	// * @return A map of CIs and their incidents
	// */
	// static HashMap<Byte, HashSet<Byte>> getCIsAndTheirIncidents() {
	// if (ci_incidents != null) {
	// return ci_incidents;
	// }
	//
	// TblIncidentDao dao = new TblIncidentDaoImpl();
	// Collection<TblIncident> all_incidents = dao.getAllIncidents();
	//
	// if (all_incidents == null) {
	// return null;
	// }
	//
	// for (TblIncident incident : all_incidents) {
	// byte ci = incident.getCiId();
	// HashSet<Byte> incidents = ci_incidents.get(ci);
	// if (incidents == null) {
	// incidents = new HashSet<Byte>();
	// }
	// incidents.add(incident.getIncidentId());
	// ci_incidents.put(ci, incidents);
	// }
	// return ci_incidents;
	// }

	/**
	 * @return A map of the services and their affecting CIs
	 */
	public static HashMap<Byte, HashSet<Byte>> getDBAffectedServices() {

		if (dbAffectedServices != null) {
			return dbAffectedServices;
		}

		TblCMDBDao dao = new TblCMDBDaoImpl();
		Collection<TblCMDB> all_cmdbs = dao.getAllCMDBs();

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

		HashMap<Byte, Double> ciSolCosts = new HashMap<>();
		TblCIDao dao = new TblCIDaoImpl();
		Collection<TblCI> all_cis = dao.getAllCIs();

		if (all_cis == null) {
			return null;
		}

		for (TblCI ci : all_cis) {
			byte ci_id = ci.getCiId();
			String supName = ci.getSupplierName1();
			TblSupplier sup = new TblSupplierDaoImpl().getSupplierById(supName);
			int mul = 1;
			switch (sup.getCurrency()) {
			// TODO: hard-coded currencies and values. Can be stored somewhere
			case "NIS":
				mul = 1;
				break;
			case "USD":
				mul = 4;
				break;
			case "EUR":
				mul = 5;
				break;
			}
			double solCost = sup.getSolutionCost() * mul;

			ciSolCosts.put(ci_id, solCost);
		}
		return ciSolCosts;
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

	static HashMap<Byte, IncidentLog> getIncidentLogsOfRound(int round) {
		
		if (incidentLogs == null) {
			incidentLogs = getIncidentLogs();
		}

		HashMap<Byte, IncidentLog> incidentLogsOfRound = new HashMap<>();
		for (Map.Entry<Byte, IncidentLog> entry : incidentLogs.entrySet()) {
			if (round == 0
					|| round == entry.getValue().getStartTime().getRound()) {
				incidentLogsOfRound.put(entry.getKey(), entry.getValue());
			}
		}
		return incidentLogsOfRound;
	}

	/**
	 * @return Initialized incident logs
	 */
	static HashMap<Byte, IncidentLog> getIncidentLogs() {

		if (incidentLogs != null) {
			return incidentLogs;
		}

		incidentLogs = new HashMap<>();
		Collection<TblIncident> incidents = new TblIncidentDaoImpl()
				.getAllIncidents();
		for (TblIncident inc : incidents) {
			byte ci_id = inc.getCiId();
			SimulationTime inc_time = inc.getSimulationTime();
			incidentLogs.put(ci_id, new IncidentLog(inc_time, ci_id));
		}
		return incidentLogs;
	}

	/**
	 * @return Incidnets (key=time, value=set of cis)
	 */
	static HashMap<SimulationTime, HashSet<Byte>> getTimeCis() {

		if (time_cis != null) {
			return time_cis;
		}

		time_cis = new HashMap<>();
		Collection<TblIncident> all_incidents = new TblIncidentDaoImpl()
				.getAllIncidents();
		for (TblIncident inc : all_incidents) {
			HashSet<Byte> cis = time_cis.get(inc.getIncidentTime());
			if (cis == null) {
				cis = new HashSet<>();
			}
			cis.add(inc.getCiId());
			time_cis.put(inc.getSimulationTime(), cis);

		}
		return time_cis;
	}

	/**
	 * @return The incidents and their timings (key= time, value= incident)
	 */
	// static HashMap<SimulationTime, Byte> getIncidentTimes() {
	// if (incidentTimes != null) {
	// return incidentTimes;
	// }
	// getIncidentLogs();
	// return incidentTimes;
	//
	// }

	/**
	 * @return The incidents (key= ci, value= time)
	 */
	static HashMap<Byte, HashSet<SimulationTime>> getCisTime() {

		if (cis_time != null) {
			return cis_time;
		}

		cis_time = new HashMap<>();
		Collection<TblIncident> all_incidents = new TblIncidentDaoImpl()
				.getAllIncidents();
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
	public static HashMap<SimulationTime, HashSet<String>> getTimetEvents() {

		if (time_events != null) {
			return time_events;
		}
		
		time_events = new HashMap<>();
		try {
			Statement stmt = DBUtility.getConnection().createStatement();
			ResultSet rs = stmt.executeQuery(Queries.eventsInTime);
			while (rs.next()) {
				SimulationTime time = new SimulationTime(rs.getInt("time"));
				HashSet<String> events = time_events.get(time);
				if (events == null) {
					events = new HashSet<String>();
				}
				events.add(String.valueOf(rs.getInt("event_id")));
				time_events.put(time, events);
			}
		} catch (SQLException e) {
		}
		return time_events;
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
