package utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashSet;

import log.Settings;

import com.daoImpl.TblCIDaoImpl;
import com.daoImpl.TblCMDBDaoImpl;
import com.daoImpl.TblDepartmentDaoImpl;
import com.daoImpl.TblDivisionDaoImpl;
import com.daoImpl.TblIncidentDaoImpl;
import com.daoImpl.TblLevelDaoImpl;
import com.daoImpl.TblPriorityCostDaoImpl;
import com.daoImpl.TblPriorityDaoImpl;
import com.daoImpl.TblServiceDaoImpl;
import com.daoImpl.TblServiceDepartmentDaoImpl;
import com.daoImpl.TblSolutionDaoImpl;
import com.daoImpl.TblSupplierDaoImpl;
import com.model.TblCI;
import com.model.TblCMDB;
import com.model.TblDepartment;
import com.model.TblDivision;
import com.model.TblIncident;
import com.model.TblLevel;
import com.model.TblPriority;
import com.model.TblPriority_Cost;
import com.model.TblService;
import com.model.TblService_Department;
import com.model.TblSolution;
import com.model.TblSupplier;

public class DBValidator {

	static final int MIN_INCIDENTS = 1;

	static final int MAX_INCIDENTS = 12;

	static int warnings = 0;

	public static void main(String[] args) {

		Object instance;
		try {
			Class<?> c = DBValidator.class;
			instance = c.newInstance();
			Field warnings = c.getDeclaredField("warnings");
			warnings.set(instance, 0);
			Method[] methods = c.getDeclaredMethods();

			if (methods == null) {
				return;
			}

			for (Method method : methods) {
				String method_name = method.getName();
				if (!method_name.startsWith("validate")) {
					continue;
				}
				Type[] pType = method.getGenericParameterTypes();
				if (pType.length != 0) {
					continue;
				}
				method.invoke(instance);
			}
			System.out.println("Total DB warnings: "
					+ warnings.getInt(instance) + ".");
		} catch (InstantiationException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException
				| NoSuchFieldException | SecurityException e) {
			e.printStackTrace();
		}
	}

	static void validateTblCI() {
		Collection<TblCI> all_cis = new TblCIDaoImpl().getAllActiveCIs();

		Collection<TblCMDB> all_cmdbs = new TblCMDBDaoImpl()
				.getAllActiveCMDBs();
		HashSet<Byte> all_cmdb_ci_ids = new HashSet<>();
		for (TblCMDB cmdb : all_cmdbs) {
			all_cmdb_ci_ids.add(cmdb.getCiId());
		}

		Collection<TblIncident> all_incidents = new TblIncidentDaoImpl()
				.getAllActiveIncidents();
		HashSet<Byte> all_incidents_ci_ids = new HashSet<>();

		if (all_incidents != null) {
			for (TblIncident inc : all_incidents) {
				all_incidents_ci_ids.add(inc.getCiId());
			}
		}

		Collection<TblSupplier> all_suppliers = new TblSupplierDaoImpl()
				.getAllSuppliers();
		HashSet<String> inactive_suppliers = new HashSet<>();
		for (TblSupplier sup : all_suppliers) {
			if (!sup.isActive()) {
				inactive_suppliers.add(sup.getSupplierName());
			}
		}

		if (all_cis != null) {
			for (TblCI ci : all_cis) {
				byte ci_id = ci.getCiId();

				if (!all_cmdb_ci_ids.contains(ci_id)) {
					System.err.println("CI '" + ci_id
							+ "' is not used in table 'tblCMDB'.");
					warnings++;
				}

				if (!all_incidents_ci_ids.contains(ci_id)) {
					System.err.println("CI '" + ci_id
							+ "' is not used in table 'tblIncident'.");
					warnings++;
				}

				if (inactive_suppliers.contains(ci.getSupplierName1())) {
					System.err.println("CI '" + ci_id
							+ "' has an inactive supplier2'.");
					warnings++;
				}

				if (inactive_suppliers.contains(ci.getSupplierName2())) {
					System.err.println("CI '" + ci_id
							+ "' has an inactive supplier3'.");
					warnings++;
				}
			}
		}
	}

	static void validateTblCMDB() {
		Collection<TblCMDB> all_cmdbs = new TblCMDBDaoImpl()
				.getAllActiveCMDBs();

		Collection<TblCI> all_cis = new TblCIDaoImpl().getAllCIs();
		HashSet<Byte> inactive_cis = new HashSet<>();
		for (TblCI ci : all_cis) {
			if (!ci.isActive()) {
				inactive_cis.add(ci.getCiId());
			}
		}

		Collection<TblService> all_services = new TblServiceDaoImpl()
				.getAllServices();
		HashSet<Byte> inactive_services = new HashSet<>();
		for (TblService ser : all_services) {
			if (!ser.isActive()) {
				inactive_services.add(ser.getServiceId());
			}
		}

		for (TblCMDB cmdb : all_cmdbs) {
			if (inactive_cis.contains(cmdb.getCiId())) {
				System.err.println("CMDB '(" + cmdb.getCiId() + ", "
						+ cmdb.getServiceId() + ")' has an inactive CI'.");
				warnings++;
			}
			if (inactive_services.contains(cmdb.getCiId())) {
				System.err.println("CMDB '(" + cmdb.getCiId() + ", "
						+ cmdb.getServiceId() + ")' has an inactive service'.");
				warnings++;
			}
		}
	}

	static void validateTblDepartment() {
		Collection<TblDepartment> all_departments = new TblDepartmentDaoImpl()
				.getAllActiveDepartments();

		Collection<TblService_Department> all_service_departments = new TblServiceDepartmentDaoImpl()
				.getAllActiveServiceDepartments();
		HashSet<String> all_service_department_names = new HashSet<>();
		if (all_service_departments != null) {
			for (TblService_Department ser_dep : all_service_departments) {
				all_service_department_names.add(ser_dep.getDepartmentName());
			}
		}

		Collection<TblDivision> all_divisions = new TblDivisionDaoImpl()
				.getAllActiveDivisions();
		HashSet<String> inactive_divisions = new HashSet<>();
		for (TblDivision div : all_divisions) {
			if (!div.isActive()) {
				inactive_divisions.add(div.getDivisionName());
			}
		}

		if (all_departments != null) {
			for (TblDepartment dep : all_departments) {
				String dep_name = dep.getDepartmentName();

				if (!all_service_departments.contains(dep_name)) {
					System.err
							.println("Department '"
									+ dep_name
									+ "' is not used in table 'tblService_Department'.");
					warnings++;
				}

				if (inactive_divisions.contains(dep.getDivisionName())) {
					System.err.println("Department '" + dep_name
							+ "' has an inactive division'.");
					warnings++;
				}
			}
		}
	}

	static void validateTblDivision() {
		Collection<TblDivision> all_divisions = new TblDivisionDaoImpl()
				.getAllActiveDivisions();

		Collection<TblDepartment> all_departments = new TblDepartmentDaoImpl()
				.getAllActiveDepartments();
		HashSet<String> all_departments_divisions = new HashSet<>();
		if (all_departments != null) {
			for (TblDepartment dep : all_departments) {
				all_departments_divisions.add(dep.getDivisionName());
			}
		}

		if (all_divisions != null) {
			for (TblDivision div : all_divisions) {
				String div_name = div.getDivisionName();

				if (!all_departments_divisions.contains(div_name)) {
					System.err.println("Division '" + div_name
							+ "' is not used in table 'tblDepartment'.");
					warnings++;
				}
			}
		}
	}

	static void validateTblIncident() {
		Collection<TblIncident> all_incidents = new TblIncidentDaoImpl()
				.getAllActiveIncidents();

		Collection<TblCI> all_cis = new TblCIDaoImpl().getAllCIs();
		HashSet<Byte> inactive_cis = new HashSet<>();
		for (TblCI ci : all_cis) {
			if (!ci.isActive()) {
				inactive_cis.add(ci.getCiId());
			}
		}

		for (TblIncident inc : all_incidents) {
			if (inactive_cis.contains(inc.getCiId())) {
				System.err.println("Incident '(" + inc.getIncidentTime() + ", "
						+ inc.getCiId() + ")' has an inactive CI'.");
				warnings++;
			}
		}
	}

	static void validateTblPriority() {
		Collection<TblPriority> all_priorities = new TblPriorityDaoImpl()
				.getAllActivePriorities();

		Collection<TblLevel> all_levels = new TblLevelDaoImpl().getAllLevels();
		HashSet<String> inactive_levels = new HashSet<>();
		for (TblLevel lvl : all_levels) {
			if (!lvl.isActive()) {
				inactive_levels.add(lvl.getLevel());
			}
		}

		for (TblPriority pr : all_priorities) {
			if (inactive_levels.contains(pr.getUrgency())) {
				System.err.println("Service '(" + pr.getUrgency() + ", "
						+ pr.getImpact() + ")' has an inactive urgency.");
				warnings++;
			}

			if (inactive_levels.contains(pr.getImpact())) {
				System.err.println("Service '(" + pr.getUrgency() + ", "
						+ pr.getImpact() + ")' has an inactive impact.");
				warnings++;
			}
		}
	}

	static void validateTblPriorityCost() {
		Collection<TblPriority_Cost> all_priority_costs = new TblPriorityCostDaoImpl()
				.getAllActivePriorityCosts();
		
		Collection<TblPriority> all_priorities = new TblPriorityDaoImpl().getAllPriorities();
		HashSet<String> inactive_priorities = new HashSet<>();
		for (TblPriority pr : all_priorities){
			if (!pr.isActive()){
				inactive_priorities.add(pr.getPriorityName());
			}
		}
		
		for (TblPriority_Cost pc : all_priority_costs){
			if (inactive_priorities.contains(pc.getPName())){
				System.err.println("PriorityCost '(" + pc.getPName() + ", "
						+ pc.getPCost() + ")' has an inactive priority name.");
				warnings++;
			}
		}
	}

	static void validateTblService() {
		Collection<TblService> all_services = new TblServiceDaoImpl()
				.getAllActiveServices();

		Collection<TblService_Department> all_service_departments = new TblServiceDepartmentDaoImpl()
				.getAllActiveServiceDepartments();
		HashSet<Byte> all_service_bizUnit_ids = new HashSet<>();
		if (all_service_departments != null) {
			for (TblService_Department ser_dep : all_service_departments) {
				all_service_bizUnit_ids.add(ser_dep.getService_ID());
			}
		}

		Collection<TblLevel> all_levels = new TblLevelDaoImpl().getAllLevels();
		HashSet<String> inactive_levels = new HashSet<>();
		for (TblLevel lvl : all_levels) {
			if (!lvl.isActive()) {
				inactive_levels.add(lvl.getLevel());
			}
		}

		if (all_services != null) {
			for (TblService ser : all_services) {
				byte ser_id = ser.getServiceId();

				if (!all_service_bizUnit_ids.contains(ser_id)) {
					System.err
							.println("Service '"
									+ ser_id
									+ "' is not used in tables"
									+ " 'tblService_Division' and 'tblService_Department'.");
					warnings++;
				}

				if (inactive_levels.contains(ser.getUrgency())) {
					System.err.println("Service '" + ser_id
							+ "' has an inactive urgency.");
					warnings++;
				}

				if (inactive_levels.contains(ser.getImpact())) {
					System.err.println("Service '" + ser_id
							+ "' has an inactive impact.");
					warnings++;
				}
			}
		}
	}

	static void validateTblSolution() {
		Collection<TblSolution> all_solutions = new TblSolutionDaoImpl()
				.getAllActiveSolutions();

		Collection<TblCMDB> all_cmdbs = new TblCMDBDaoImpl()
				.getAllActiveCMDBs();
		HashSet<Byte> all_cmdb_ci_ids = new HashSet<>();
		if (all_cmdbs != null) {
			for (TblCMDB cmdb : all_cmdbs) {
				all_cmdb_ci_ids.add(cmdb.getCiId());
			}
		}

		Collection<TblCI> all_cis = new TblCIDaoImpl().getAllActiveCIs();
		HashSet<Integer> all_cis_solutions = new HashSet<>();
		if (all_cis != null) {
			for (TblCI inc : all_cis) {
				all_cis_solutions.add(inc.getSolutionId());
			}
		}

		if (all_solutions != null) {
			for (TblSolution sol : all_solutions) {
				int sol_id = sol.getSolutionId();

				if (!all_cis_solutions.contains(sol_id)) {
					System.err.println("Solution '" + sol_id
							+ "' is not used in table 'tblIncident'.");
					warnings++;
				}
			}
		}
	}

	static void validateTblSupplier() {
		Collection<TblSupplier> all_suppliers = new TblSupplierDaoImpl()
				.getAllActiveSuppliers();

		Collection<TblCI> all_cmdbs = new TblCIDaoImpl().getAllActiveCIs();
		HashSet<String> all_ci_suppliers = new HashSet<>();
		if (all_cmdbs != null) {
			for (TblCI ci : all_cmdbs) {
				all_ci_suppliers.add(ci.getSupplierName1());
				all_ci_suppliers.add(ci.getSupplierName2());
			}
		}

		if (all_suppliers != null) {
			for (TblSupplier supp : all_suppliers) {
				String supp_name = supp.getSupplierName();

				if (!all_ci_suppliers.contains(supp_name)) {
					System.err.println("Supplier '" + supp_name
							+ "' is not used in table 'tblCI'.");
					warnings++;
				}
			}
		}
	}

	public static String checkSettings(Settings sett) {
		Collection<TblIncident> all_incidents = new TblIncidentDaoImpl()
				.getAllActiveIncidents();

		String minErr = "Round [R] session [S]:\r\n"
				+ "Insufficient incidents ([COUNT] < [MIN]).\r\n"
				+ "Consider the follwoing options:\r\n"
				+ "1. Decrease the number of sessions per round.\r\n"
				+ "2. Increase the run time duration.\r\n"
				+ "3. Add [A] incidents between [T1] to [T2].\n\n";

		String maxErr = "Round [R] session [S]:\r\n"
				+ "Too many incidents ([COUNT] > [MAX]).\r\n"
				+ "Consider the follwoing options:\r\n"
				+ "1. Increase the number of sessions per round.\r\n"
				+ "2. Decreade the run time duration.\r\n"
				+ "3. Reduce [A] incidents between [T1] to [T2].\n\n";

		int rounds = sett.getRounds();
		int sessionsPerRound = sett.getSessionsPerRound();

		int[][] inc_counts = new int[rounds][sessionsPerRound];
		SimulationTime inc_time;
		for (TblIncident inc : all_incidents) {
			inc_time = inc.getSimulationTime();
			int round = Math.min(inc_time.getRound(), sett.getRounds());
			int session = Math.min(inc_time.getSessionInRound(),
					sett.getSessionsPerRound());
			inc_counts[round - 1][session - 1]++;
		}
		SimulationTime startTime;
		SimulationTime endTime;
		int a;
		String err = "";
		for (int r = 1; r < rounds; r++) {
			for (int s = 1; s < sessionsPerRound; s++) {

				int num_of_incidents = inc_counts[r][s];
				if (num_of_incidents < MIN_INCIDENTS) {
					startTime = new SimulationTime(r * sett.getRoundRunTime()
							+ s * sett.getRunTime());
					endTime = new SimulationTime(startTime.getRunTime()
							+ sett.getRunTime());
					a = num_of_incidents - MIN_INCIDENTS;
					err += minErr
							.replace("[R]", String.valueOf(r))
							.replace("[S]", String.valueOf(s))
							.replace("[COUNT]",
									String.valueOf(num_of_incidents))
							.replace("[MIN]", String.valueOf(MIN_INCIDENTS))
							.replace("[A]", String.valueOf(a))
							.replace("[T1]", startTime.toString())
							.replace("[T2]", endTime.toString());
					continue;
				}

				if (num_of_incidents > MAX_INCIDENTS) {
					startTime = new SimulationTime(r * sett.getRoundRunTime()
							+ s * sett.getRunTime());
					endTime = new SimulationTime(startTime.getRunTime()
							+ sett.getRunTime());
					a = MAX_INCIDENTS - num_of_incidents;
					err += maxErr
							.replace("[R]", String.valueOf(r))
							.replace("[S]", String.valueOf(s))
							.replace("[COUNT]",
									String.valueOf(num_of_incidents))
							.replace("[MAX]", String.valueOf(MAX_INCIDENTS))
							.replace("[A]", String.valueOf(a))
							.replace("[T1]", startTime.toString())
							.replace("[T2]", endTime.toString());
				}
			}
		}
		return err;
	}
}
