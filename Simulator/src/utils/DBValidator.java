package utils;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashSet;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import log.Settings;

import com.daoImpl.TblCIDaoImpl;
import com.daoImpl.TblCMDBDaoImpl;
import com.daoImpl.TblDepartmentDaoImpl;
import com.daoImpl.TblDivisionDaoImpl;
import com.daoImpl.TblIncidentDaoImpl;
import com.daoImpl.TblServiceDaoImpl;
import com.daoImpl.TblServiceDepartmentDaoImpl;
import com.daoImpl.TblSolutionDaoImpl;
import com.daoImpl.TblSupplierDaoImpl;
import com.model.TblCI;
import com.model.TblCMDB;
import com.model.TblDepartment;
import com.model.TblDivision;
import com.model.TblIncident;
import com.model.TblService;
import com.model.TblService_Department;
import com.model.TblSolution;
import com.model.TblSupplier;

public class DBValidator {

	private static final Logger LOGGER = Logger.getLogger(Thread
			.currentThread().getStackTrace()[0].getClassName());

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
			logQuery();
		} catch (InstantiationException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException
				| NoSuchFieldException | SecurityException e) {
			e.printStackTrace();
		}
	}

	public static void logQuery() {
		Formatter simpleFormatter = null;
		Handler fileHandler = null;
		try {
			// Creating FileHandler
			fileHandler = new FileHandler("./mylog.log");

			// Creating SimpleFormatter
			simpleFormatter = new SimpleFormatter();

			// Assigning handler to logger
			LOGGER.addHandler(fileHandler);

			// Logging message of Level info (this should be publish in the
			// default format i.e. XMLFormat)
			LOGGER.info("Finnest message: Logger with DEFAULT FORMATTER");
			LOGGER.info("AAA");
			LOGGER.info("BBB");

			// Setting formatter to the handler
			fileHandler.setFormatter(simpleFormatter);

			// Setting Level to ALL
			fileHandler.setLevel(Level.ALL);
			LOGGER.setLevel(Level.ALL);

			// Logging message of Level finest (this should be publish in the
			// simple format)

		} catch (IOException exception) {
			LOGGER.log(Level.SEVERE, "Error occur in FileHandler.", exception);
		}
	}

	public static String validateTblCI() {
		String str ="";
		int ciWarnings = 0;
		Collection<TblCI> all_cis = new TblCIDaoImpl().getAllCIs();

		Collection<TblCMDB> all_cmdbs = new TblCMDBDaoImpl().getAllCMDBs();
		HashSet<Byte> all_cmdb_ci_ids = new HashSet<>();
		for (TblCMDB cmdb : all_cmdbs) {
			all_cmdb_ci_ids.add(cmdb.getCiId());
		}

		Collection<TblIncident> all_incidents = new TblIncidentDaoImpl()
				.getAllIncidents();
		HashSet<Byte> all_incidents_ci_ids = new HashSet<>();

		if (all_incidents != null) {
			for (TblIncident inc : all_incidents) {
				all_incidents_ci_ids.add(inc.getCiId());
			}
		}

		if (all_cis != null) {
			for (TblCI ci : all_cis) {
				byte ci_id = ci.getCiId();

				if (!all_cmdb_ci_ids.contains(ci_id)) {
					System.err.println("CI '" + ci_id
							+ "' is not used in table 'tblCMDB'.");
					str += "CI '" + ci_id
							+ "' is not used in table 'tblCMDB'.<br>\n";
					warnings++;
					ciWarnings++;
				}

				if (!all_incidents_ci_ids.contains(ci_id)) {
					System.err.println("CI '" + ci_id
							+ "' is not used in table 'tblIncident'.");
					str += "CI '" + ci_id
							+ "' is not used in table 'tblIncident'.<br>\n";
					warnings++;
					ciWarnings++;
				}
			}
		}
		str += "Total Warnings: "+ciWarnings;
		return str;
	}

	public static String validateTblDepartment() {
		String str ="";
		int depWarnings = 0;
		Collection<TblDepartment> all_departments = new TblDepartmentDaoImpl()
				.getAllDepartments();

		Collection<TblService_Department> all_service_departments = new TblServiceDepartmentDaoImpl()
				.getAllServiceDepartments();
		HashSet<String> all_service_department_names = new HashSet<>();
		if (all_service_departments != null) {
			for (TblService_Department ser_dep : all_service_departments) {
				all_service_department_names.add(ser_dep.getDepartmentName());
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
					str += "Department '"
							+ dep_name
							+ "' is not used in table 'tblService_Department'.<br>\n";
					warnings++;
					depWarnings++;
				}
			}
		}
		str += "Total Warnings: "+depWarnings;
		return str;
	}

	public static String validateTblDivision() {
		String str ="";
		int divWarnings = 0;
		Collection<TblDivision> all_divisions = new TblDivisionDaoImpl()
				.getAllDivisions();

		Collection<TblDepartment> all_departments = new TblDepartmentDaoImpl()
				.getAllDepartments();
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
					str += "Division '" + div_name
							+ "' is not used in table 'tblDepartment'.<br>\n";
					divWarnings++;
					warnings++;
				}
			}
		}
		str += "Total Warnings: "+divWarnings;
		return str;
	}

	public static String validateTblService() {
		String str ="";
		int serWarnings = 0;
		Collection<TblService> all_services = new TblServiceDaoImpl()
				.getAllServices();

		Collection<TblService_Department> all_service_departments = new TblServiceDepartmentDaoImpl()
				.getAllServiceDepartments();
		HashSet<Byte> all_service_bizUnit_ids = new HashSet<>();
		if (all_service_departments != null) {
			for (TblService_Department ser_dep : all_service_departments) {
				all_service_bizUnit_ids.add(ser_dep.getService_ID());
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
					str += "Service '"
							+ ser_id
							+ "' is not used in tables"
							+ " 'tblService_Division' and 'tblService_Department'.<br>\n";
					warnings++;
					serWarnings++;
				}
			}
		}
		str += "Total Warnings: "+serWarnings;
		return str;
	}

	public static String validateTblSolution() {
		String str ="";
		int solWarnings = 0;
		Collection<TblSolution> all_solutions = new TblSolutionDaoImpl()
				.getAllSolutions();

		Collection<TblCMDB> all_cmdbs = new TblCMDBDaoImpl().getAllCMDBs();
		HashSet<Byte> all_cmdb_ci_ids = new HashSet<>();
		if (all_cmdbs != null) {
			for (TblCMDB cmdb : all_cmdbs) {
				all_cmdb_ci_ids.add(cmdb.getCiId());
			}
		}

		Collection<TblCI> all_cis = new TblCIDaoImpl().getAllCIs();
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
					str += "Solution '" + sol_id
							+ "' is not used in table 'tblIncident'.<br>\n";
					solWarnings++;
					warnings++;
				}
			}
		}
		str += "Total Warnings: "+solWarnings;
		return str;
	}

	public static String validateTblSupplier() {
		String str ="";
		int supWarnings = 0;
		Collection<TblSupplier> all_suppliers = new TblSupplierDaoImpl()
				.getAllSuppliers();

		Collection<TblCI> all_cmdbs = new TblCIDaoImpl().getAllCIs();
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
					str += "Supplier '" + supp_name
							+ "' is not used in table 'tblCI'.<br>\n";
					supWarnings++;
					warnings++;
				}
			}
		}
		str += "Total Warnings: "+supWarnings;
		return str;
	}

	public static String checkSettings(Settings sett) {
		Collection<TblIncident> all_incidents = new TblIncidentDaoImpl()
				.getAllIncidents();

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
