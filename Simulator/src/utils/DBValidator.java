package utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashSet;

import com.daoImpl.TblCIDaoImpl;
import com.daoImpl.TblCMDBDaoImpl;
import com.daoImpl.TblDepartmentDaoImpl;
import com.daoImpl.TblDivisionDaoImpl;
import com.daoImpl.TblEventDaoImpl;
import com.daoImpl.TblIncidentDaoImpl;
import com.daoImpl.TblServiceDaoImpl;
import com.daoImpl.TblServiceDepartmentDaoImpl;
import com.daoImpl.TblSolutionDaoImpl;
import com.daoImpl.TblSupplierDaoImpl;
import com.model.TblCI;
import com.model.TblCMDB;
import com.model.TblDepartment;
import com.model.TblDivision;
import com.model.TblEvent;
import com.model.TblIncident;
import com.model.TblService;
import com.model.TblService_Department;
import com.model.TblSolution;
import com.model.TblSupplier;

public class DBValidator {

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
					warnings++;
				}

				if (!all_incidents_ci_ids.contains(ci_id)) {
					System.err.println("CI '" + ci_id
							+ "' is not used in table 'tblIncident'.");
					warnings++;
				}
			}
		}
	}

	static void validateTblDepartment() {
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
					warnings++;
				}
			}
		}
	}

	static void validateTblDivision() {
		Collection<TblDivision> all_divisions = new TblDivisionDaoImpl()
				.getAllDivisions();

//		Collection<TblService_Division> all_service_divisions = new TblServiceDivisionDaoImpl()
//				.getAllServiceDivisions();
//		HashSet<String> all_service_division_names = new HashSet<>();
//		if (all_service_divisions != null) {
//			for (TblService_Division ser_div : all_service_divisions) {
//				all_service_division_names.add(ser_div.getDivisionName());
//			}
//		}

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

//				if (!all_service_division_names.contains(div_name)) {
//					System.err.println("Division '" + div_name
//							+ "' is not used in table 'tblService_Division'.");
//					warnings++;
//				}

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
				.getAllIncidents();

		Collection<TblEvent> all_events = new TblEventDaoImpl().getAllEvents();
		HashSet<Byte> all_event_incident_ids = new HashSet<>();
		if (all_events != null) {
			for (TblEvent event : all_events) {
				all_event_incident_ids.add(event.getIncidentId());
			}
		}

		if (all_incidents != null) {
			for (TblIncident div : all_incidents) {
				byte inc_id = div.getIncidentId();

				if (!all_event_incident_ids.contains(inc_id)) {
					System.err.println("Incident '" + inc_id
							+ "' is not used in table 'tblEvent'.");
					warnings++;
				}
			}
		}
	}

	static void validateTblService() {
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

//		Collection<TblService_Division> all_service_divisions = new TblServiceDivisionDaoImpl()
//				.getAllServiceDivisions();
//		if (all_service_divisions != null) {
//			for (TblService_Division ser_div : all_service_divisions) {
//				all_service_bizUnit_ids.add(ser_div.getService_ID());
//			}
//		}

		Collection<TblEvent> all_events = new TblEventDaoImpl().getAllEvents();
		HashSet<Byte> all_event_service_ids = new HashSet<>();
		if (all_events != null) {
			for (TblEvent event : all_events) {
				all_event_service_ids.add(event.getServiceId());
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

				if (!all_event_service_ids.contains(ser_id)) {
					System.err.println("Service '" + ser_id
							+ "' is not used in table 'tblEvent'.");
					warnings++;
				}
			}
		}
	}

	static void validateTblSolution() {
		Collection<TblSolution> all_solutions = new TblSolutionDaoImpl()
				.getAllSolutions();

		Collection<TblCMDB> all_cmdbs = new TblCMDBDaoImpl().getAllCMDBs();
		HashSet<Byte> all_cmdb_ci_ids = new HashSet<>();
		if (all_cmdbs != null) {
			for (TblCMDB cmdb : all_cmdbs) {
				all_cmdb_ci_ids.add(cmdb.getCiId());
			}
		}

		Collection<TblIncident> all_incidents = new TblIncidentDaoImpl()
				.getAllIncidents();
		HashSet<Integer> all_incidents_solutions = new HashSet<>();
		if (all_incidents != null) {
			for (TblIncident inc : all_incidents) {
				all_incidents_solutions.add(inc.getSolutionId());
			}
		}

		if (all_solutions != null) {
			for (TblSolution sol : all_solutions) {
				int sol_id = sol.getSolutionId();

				if (!all_incidents_solutions.contains(sol_id)) {
					System.err.println("Solution '" + sol_id
							+ "' is not used in table 'tblIncident'.");
					warnings++;
				}
			}
		}
	}

	static void validateTblSupplier() {
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
					warnings++;
				}
			}
		}
	}
}
