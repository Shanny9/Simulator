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

import utils.Queries;
import utils.SimulationTime;

import com.dao.TblCIDao;
import com.dao.TblCMDBDao;
import com.dao.TblEventDao;
import com.dao.TblIncidentDao;
import com.dao.TblServiceDao;
import com.daoImpl.TblCIDaoImpl;
import com.daoImpl.TblCMDBDaoImpl;
import com.daoImpl.TblEventDaoImpl;
import com.daoImpl.TblIncidentDaoImpl;
import com.daoImpl.TblServiceDaoImpl;
import com.daoImpl.TblSupplierDaoImpl;
import com.jdbc.DBUtility;
import com.model.TblCI;
import com.model.TblCMDB;
import com.model.TblEvent;
import com.model.TblIncident;
import com.model.TblService;
import com.model.TblSupplier;

public class LogUtils {

	/**
	 * @return A map of the CI's and their affected services
	 */
	static HashMap<Byte, HashSet<Byte>> getDBAffectingCIs() {
		HashMap<Byte, HashSet<Byte>> dbAffectingCis = new HashMap<>();
		TblCMDBDao dao = new TblCMDBDaoImpl();

		for (TblCMDB cmdb : dao.getAllCMDBs()) {
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
	 * @return A map of CIs and their incidents
	 */
	static HashMap<Byte, HashSet<Byte>> getCIsAndTheirIncidents() {
		HashMap<Byte, HashSet<Byte>> ci_incidents = new HashMap<>();
		TblIncidentDao dao = new TblIncidentDaoImpl();

		for (TblIncident incident : dao.getAllIncidents()) {
			byte ci = incident.getCiId();
			HashSet<Byte> incidents = ci_incidents.get(ci);
			if (incidents == null) {
				incidents = new HashSet<Byte>();
			}
			incidents.add(incident.getIncidentId());
			ci_incidents.put(ci, incidents);
		}
		return ci_incidents;
	}

	/**
	 * @return A map of the services and their affecting CIs
	 */
	static HashMap<Byte, HashSet<Byte>> getDBAffectedServices() {
		HashMap<Byte, HashSet<Byte>> dbAffectedServices = new HashMap<>();
		TblCMDBDao dao = new TblCMDBDaoImpl();

		for (TblCMDB cmdb : dao.getAllCMDBs()) {
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
		HashMap<Byte, Double> ciSolCosts = new HashMap<>();
		TblCIDao dao = new TblCIDaoImpl();
		for (TblCI ci : dao.getAllCIs()) {
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
		HashMap<Byte, Double> serviceCosts = new HashMap<>();
		try {
			Statement stmt = DBUtility.getConnection().createStatement();
			ResultSet rs = stmt.executeQuery(Queries.serviceDownCosts);
			while (rs.next()) {
				serviceCosts.put(rs.getByte("service_id"),
						rs.getDouble("down_cost"));
			}
			return serviceCosts;
		} catch (SQLException e) {
			System.out.println("LogUtils: getServiceDownTimeCosts(): "
					+ e.getMessage());
		}
		return null;
	}

	/**
	 * @return Initialized incident logs
	 */
	static HashMap<Byte, IncidentLog> getIncidentLogs() {
		HashMap<Byte, IncidentLog> incidents = new HashMap<>();
		TblIncidentDao dao = new TblIncidentDaoImpl();
		for (TblIncident inc : dao.getAllIncidents()) {
			byte inc_id = (byte) inc.getIncidentId();
			byte ci_id = (byte) inc.getCiId();
			SimulationTime start_time = inc.getIncidentTime();
			incidents.put(inc_id, new IncidentLog(inc_id, ci_id, start_time));
		}
		return incidents;
	}

	/**
	 * @return The incidents and their timings (key= time, value= incident)
	 */
	static HashMap<SimulationTime, Byte> getIncidentTimes() {
		HashMap<SimulationTime, Byte> incidents = new HashMap<>();
		TblIncidentDao dao = new TblIncidentDaoImpl();
		Collection<TblIncident> all_incidents = dao.getAllIncidents();
		for (TblIncident inc : all_incidents) {
			incidents.put(inc.getIncidentTime(),
					(byte) inc.getIncidentId());
		}
		return incidents;
	}

	/**
	 * @return The incidents and their events (key= incident, value= set of
	 *         events)
	 */
	public static HashMap<Byte, HashSet<String>> getIncidentEvents() {
		HashMap<Byte, HashSet<String>> incident_events = new HashMap<>();
		TblEventDao dao = new TblEventDaoImpl();

		for (TblEvent event : dao.getAllEvents()) {
			byte incident = event.getIncidentId();
			HashSet<String> events = incident_events.get(incident);
			if (events == null) {
				events = new HashSet<String>();
			}
			events.add(String.valueOf(event.getEventId()));
			incident_events.put(incident, events);
		}
		return incident_events;
	}

	/**
	 * @return The services and their affecting incidents (key= services, value=
	 *         affecting incidents)
	 */
	public static HashMap<Byte, HashSet<Byte>> getServiceIncidents() {
		HashMap<Byte, HashSet<Byte>> service_incidents = new HashMap<>();
		TblServiceDao dao = new TblServiceDaoImpl();
		HashMap<Byte, HashSet<Byte>> affected_services = LogUtils
				.getDBAffectedServices();
		HashMap<Byte, HashSet<Byte>> ci_incidents = LogUtils
				.getCIsAndTheirIncidents();

		for (TblService service : dao.getAllServices()) {
			HashSet<Byte> incidents = service_incidents.get(service
					.getServiceId());
			if (incidents == null) {
				incidents = new HashSet<Byte>();
			}

			HashSet<Byte> affecting_cis = affected_services.get(service
					.getServiceId());
			if (affecting_cis == null) {
				continue;
			}

			HashSet<Byte> affecting_incidents = new HashSet<>();
			for (Byte ci : affecting_cis) {
				affecting_incidents.addAll(ci_incidents.get(ci));
			}

			incidents.addAll(affecting_incidents);
			service_incidents.put(service.getServiceId(), incidents);
		}
		return service_incidents;
	}

	/**
	 * @return A map of services and their priorities (key= service, value=
	 *         priority name)
	 */
	static HashMap<Byte, String> getServicePriorities() {
		HashMap<Byte, String> servicePriority = new HashMap<>();
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
