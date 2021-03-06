package com.servlet;

import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import utils.DBValidator;

import com.daoImpl.TblCIDaoImpl;
import com.daoImpl.TblCMDBDaoImpl;
import com.daoImpl.TblCurrencyDaoImpl;
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
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.model.TblCI;
import com.model.TblCMDB;
import com.model.TblCMDBPK;
import com.model.TblCurrency;
import com.model.TblDepartment;
import com.model.TblDepartmentPK;
import com.model.TblDivision;
import com.model.TblIncident;
import com.model.TblIncidentPK;
import com.model.TblLevel;
import com.model.TblPriority;
import com.model.TblPriorityPK;
import com.model.TblPriority_Cost;
import com.model.TblService;
import com.model.TblService_Department;
import com.model.TblService_DepartmentPK;
import com.model.TblSolution;
import com.model.TblSupplier;

public class DataController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private HashMap<String, Object> JSONROOT = new HashMap<String, Object>();

	private TblSupplierDaoImpl daoSupplier;
	private TblSolutionDaoImpl daoSolution;
	private TblIncidentDaoImpl daoIncident;
	private TblPriorityCostDaoImpl daoPriorityCost;
	private TblServiceDaoImpl daoService;
	private TblServiceDepartmentDaoImpl daoServiceDepartment;
	private TblCIDaoImpl daoCI;
	private TblCMDBDaoImpl daoCMDB;
	private TblDepartmentDaoImpl daoDepartment;
	private TblDivisionDaoImpl daoDivision;
	private TblPriorityDaoImpl daoPriority;
	private TblLevelDaoImpl daoLevel;
	private TblCurrencyDaoImpl daoCurrency;

	/**
	 * Empty Constructor
	 */
	public DataController() {
		daoSupplier = new TblSupplierDaoImpl();
		daoSolution = new TblSolutionDaoImpl();
		daoIncident = new TblIncidentDaoImpl();
		daoPriorityCost = new TblPriorityCostDaoImpl();
		daoService = new TblServiceDaoImpl();
		daoServiceDepartment = new TblServiceDepartmentDaoImpl();
		daoCurrency = new TblCurrencyDaoImpl();

		// daoServiceDivision = new TblServiceDivisionDaoImpl();

		daoCI = new TblCIDaoImpl();
		daoCMDB = new TblCMDBDaoImpl();
		daoDepartment = new TblDepartmentDaoImpl();
		daoDivision = new TblDivisionDaoImpl();
		daoPriority = new TblPriorityDaoImpl();
		daoLevel = new TblLevelDaoImpl();
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	@SuppressWarnings("unchecked")
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		response.setCharacterEncoding("UTF-8");
		request.setCharacterEncoding("UTF-8");
		response.setContentType("application/json");

		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String options = request.getParameter("options");

		if (options != null) {
			JSONObject jo = new JSONObject();

			JSONArray optionArr = null;
			switch (options) {
			case "ci":
				optionArr = toOptionArray(daoCI.getAllCIs(), "ciId", "CI_name");
				break;
			case "solution":
				optionArr = toOptionArray(daoSolution.getAllSolutions(),
						"solutionId", "solutionId");
				
				JSONObject option = new JSONObject();
				option.put("DisplayText", "No Solution");
				option.put("Value", null);
				optionArr.add(option);
				break;
			case "supplier":
				optionArr = toOptionArray(daoSupplier.getAllSuppliers(),
						"supplierName", "supplierName");
				break;
			case "division":
				optionArr = toOptionArray(daoDivision.getAllDivisions(),
						"divisionName", "divisionName");
				break;
			case "department":
				if(request.getParameter("divisionName") == null)
					optionArr = toOptionArray(daoDepartment.getAllDepartments(),
							"departmentName", "departmentName");
				else
					optionArr = toOptionArray(daoDepartment.getDepartmentByDivision(request.getParameter("divisionName")),
							"departmentName", "departmentName");
				break;
				/* gets only distincts division names that have department, for service dep table */
			case "divisionForService":
				optionArr = toOptionArray(daoDepartment.getAllDistinctDepartments(),
						"divisionName", "divisionName");
				break;
			case "service":
				optionArr = toOptionArray(daoService.getAllServices(),
						"serviceId", "serviceName");
				break;
			case "incident":
				optionArr = toOptionArray(daoIncident.getAllIncidents(),
						"incidentId", "incidentId");
				break;
			case "priority":
				optionArr = toOptionArray(daoPriority.getAllPrioritiesDistinct(),
						"priorityName", "priorityName");
				break;
			case "level":
				optionArr = toOptionArray(daoLevel.getAllLevels(), "level",
						"level");
				break;
			case "currency":
				optionArr = toOptionArray(daoCurrency.getAllCurrencies(), "currency",
						"currency");
				break;
			}
			jo.put("Result", "OK");
			jo.put("Options", optionArr);
			response.getWriter().print(jo);
			return;
		}

		String action = request.getParameter("action");
		String table = request.getParameter("table");
		String validateTable = request.getParameter("vTable");

		if (action != null && action.equals("warnings")) {
			if(validateTable!=null){
				JSONObject json = new JSONObject();
				switch(validateTable){
				case "tblCi":
					json.put("warnings", DBValidator.validateTblCI());
					break;
				case "tblDepartment":
					json.put("warnings", DBValidator.validateTblDepartment());
					break;
				case "tblDivision":
					json.put("warnings", DBValidator.validateTblDivision());
					break;
				case "tblService":
					json.put("warnings", DBValidator.validateTblService());
					break;
				case "tblSupplier":
					json.put("warnings", DBValidator.validateTblSupplier());
					break;
				case "tblSolution":
					json.put("warnings", DBValidator.validateTblSolution());
					break;
				}
				response.getWriter().print(json);
			}
		
		}
		if (table != null) {
			switch (table) {
			case "supplier":
				tblSuppliers(action, request, response, gson);
				break;
			case "solution":
				tblSolution(action, request, response, gson);
				break;
			case "incident":
				tblIncident(action, request, response, gson);
				break;
			case "priorityCost":
				tblPriorityCost(action, request, response, gson);
				break;
			case "priority":
				tblPriority(action, request, response, gson);
				break;
			case "service":
				tblService(action, request, response, gson);
				break;
			case "serviceDepartment":
				tblServiceDepartment(action, request, response, gson);
				break;
			// case "serviceDivision":
			// tblServiceDivision(action, request, response, gson);
			// break;
			case "ci":
				tblCIs(action, request, response, gson);
				break;
			case "cmdb":
				tblCMDBs(action, request, response, gson);
				break;
			case "currency":
				tblCurrency(action, request, response, gson);
				break;
			case "department":
				tblDepartments(action, request, response, gson);
				break;
			case "division":
				tblDivisions(action, request, response, gson);
				break;
			case "level":
				tblLevel(action, request, response, gson);
				break;
/*			case "event":
				tblEvents(action, request, response, gson);
				break;*/
			}

		}
	}// end
		// doPost

	protected void tblSuppliers(String action, HttpServletRequest request,
			HttpServletResponse response, Gson gson) throws IOException {

		List<TblSupplier> supList = new ArrayList<TblSupplier>();
		if (action != null) {
			try {
				if (action.equals("list")) {
					// Fetch Data from User Table
					int startPageIndex = Integer.parseInt(request
							.getParameter("jtStartIndex"));
					int recordsPerPage = Integer.parseInt(request
							.getParameter("jtPageSize"));
					// Fetch Data from Supplier Table
					supList = daoSupplier.getAllSuppliers(startPageIndex,
							recordsPerPage);
					// Get Total Record Count for Pagination
					int userCount = daoSupplier.getSupplierCount();
					// Return in the format required by jTable plugin
					JSONROOT.put("Result", "OK");
					JSONROOT.put("Records", supList);
					JSONROOT.put("TotalRecordCount", userCount);

					// Convert Java Object to Json
					String jsonArray = gson.toJson(JSONROOT);
					response.getWriter().print(jsonArray);
				} else if (action.equals("create") || action.equals("update")) {
					TblSupplier supplier = new TblSupplier();

					// Set fields
					if (request.getParameter("supplierName") != null) {
						String supName = request.getParameter("supplierName");
						supplier.setSupplierName(supName);

					}

					if (request.getParameter("solutionCost") != null) {
						Double cost = Double.parseDouble(request
								.getParameter("solutionCost"));
						supplier.setSolutionCost(cost);
					}

					if (request.getParameter("isActive") != null) {
						boolean isActive = Boolean.parseBoolean(request
								.getParameter("isActive"));
						supplier.setActive(isActive);
					}

					if (request.getParameter("currency") != null) {
						String currency = request.getParameter("currency");
						supplier.setCurrency(currency);
					}
					// end set fields

					if (action.equals("create")) {
						// Create new record
						daoSupplier.addSupplier(supplier);
					} else if (action.equals("update")) {
						// Update existing record
						String name = request
								.getParameter("jtRecordKey_supplierName");
						daoSupplier.updateSupplier(supplier, name);
					}

					// Return in the format required by jTable plugin
					JSONROOT.put("Result", "OK");
					JSONROOT.put("Record", supplier);

					// Convert Java Object to Json
					String jsonArray = gson.toJson(JSONROOT);
					response.getWriter().print(jsonArray);
				} else if (action.equals("delete")) {
					// Delete record
					if (request.getParameter("supplierName") != null) {
						String name = request.getParameter("supplierName");
						daoSupplier.deleteSupplier(name);

						// Return in the format required by jTable plugin
						JSONROOT.put("Result", "OK");

						// Convert Java Object to Json
						String jsonArray = gson.toJson(JSONROOT);
						response.getWriter().print(jsonArray);
					}
				} else if (action.equals("excel")) {
					// Export to excel
					supList = daoSupplier.getAllSuppliers();
					// Return in the format required by jTable plugin
					JSONROOT.clear();
					JSONROOT.put("Records", supList);
					// Convert Java Object to Json
					String jsonArray = gson.toJson(JSONROOT);
					response.getWriter().print(jsonArray);
				}
			} catch (Exception ex) {
				System.out.println("DataController: Data-tblSupplier: "
						+ ex.getMessage());
				JSONROOT.put("Result", "ERROR");
				JSONROOT.put(
						"Message",
						(ex instanceof SQLException) ? getErrorMsg(
								((SQLException) ex).getErrorCode(),
								ex.getMessage(), action) : ex.getMessage());
				String error = gson.toJson(JSONROOT);
				response.getWriter().print(error);
			}

		}
	}

	protected void tblSolution(String action, HttpServletRequest request,
			HttpServletResponse response, Gson gson) throws IOException {

		List<TblSolution> solList = new ArrayList<TblSolution>();
		if (action != null) {
			try {
				if (action.equals("list")) {
					// Fetch Data from User Table
					int startPageIndex = Integer.parseInt(request
							.getParameter("jtStartIndex"));
					int recordsPerPage = Integer.parseInt(request
							.getParameter("jtPageSize"));
					// Fetch Data from Supplier Table
					solList = daoSolution.getAllSolutions(startPageIndex,
							recordsPerPage);
					// Get Total Record Count for Pagination
					int userCount = daoSolution.getSolutionCount();
					// Return in the format required by jTable plugin
					JSONROOT.put("Result", "OK");
					JSONROOT.put("Records", solList);
					JSONROOT.put("TotalRecordCount", userCount);

					// Convert Java Object to Json
					String jsonArray = gson.toJson(JSONROOT);
					response.getWriter().print(jsonArray);
				} else if (action.equals("create") || action.equals("update")) {
					TblSolution sol = new TblSolution();

					// Set fields
					if (request.getParameter("solutionId") != null) {
						int solId = Integer.parseInt(request
								.getParameter("solutionId"));
						sol.setSolutionId(solId);

					}

					if (request.getParameter("solutionMarom") != null) {
						int marom = Integer.parseInt(request
								.getParameter("solutionMarom"));
						sol.setSolutionMarom(marom);
					}

					if (request.getParameter("solutionRakia") != null) {
						int rakia = Integer.parseInt(request
								.getParameter("solutionRakia"));
						sol.setSolutionRakia(rakia);
					}

					if (request.getParameter("isActive") != null) {
						boolean active = Boolean.parseBoolean(request
								.getParameter("isActive"));
						sol.setActive(active);
					}
					// end set fields

					if (action.equals("create")) {
						// Create new record
						daoSolution.addSolution(sol);
					} else if (action.equals("update")) {
						// Update existing record
						int id = Integer.parseInt(request
								.getParameter("jtRecordKey_solutionId"));
						daoSolution.updateSolution(sol, id);
					}

					// Return in the format required by jTable plugin
					JSONROOT.put("Result", "OK");
					JSONROOT.put("Record", sol);

					// Convert Java Object to Json
					String jsonArray = gson.toJson(JSONROOT);
					response.getWriter().print(jsonArray);
				} else if (action.equals("delete")) {
					// Delete record
					if (request.getParameter("solutionId") != null) {
						int solId = Integer.parseInt(request
								.getParameter("solutionId"));
						daoSolution.deleteSolution(solId);

						// Return in the format required by jTable plugin
						JSONROOT.put("Result", "OK");

						// Convert Java Object to Json
						String jsonArray = gson.toJson(JSONROOT);
						response.getWriter().print(jsonArray);
					}
				} else if (action.equals("excel")) {
					// Export to excel
					solList = daoSolution.getAllSolutions();
					// Return in the format required by jTable plugin
					JSONROOT.clear();
					JSONROOT.put("Records", solList);
					// Convert Java Object to Json
					String jsonArray = gson.toJson(JSONROOT);
					response.getWriter().print(jsonArray);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				System.out.println("DataController: Data-tblSolution: "
						+ ex.getMessage());
				JSONROOT.put("Result", "ERROR");
				JSONROOT.put(
						"Message",
						(ex instanceof SQLException) ? getErrorMsg(
								((SQLException) ex).getErrorCode(),
								ex.getMessage(), action) : ex.getMessage());
				String error = gson.toJson(JSONROOT);
				response.getWriter().print(error);
			}
		}
	}
	
	protected void tblLevel(String action, HttpServletRequest request,
			HttpServletResponse response, Gson gson) throws IOException {

		List<TblLevel> levelList = new ArrayList<TblLevel>();
		if (action != null) {
			try {
				if (action.equals("list")) {
					// Fetch Data from User Table
					int startPageIndex = Integer.parseInt(request
							.getParameter("jtStartIndex"));
					int recordsPerPage = Integer.parseInt(request
							.getParameter("jtPageSize"));
					// Fetch Data from Supplier Table
					levelList = daoLevel.getAllLevels(startPageIndex,
							recordsPerPage);
					// Get Total Record Count for Pagination
					int userCount = daoLevel.getLevelCount();
					// Return in the format required by jTable plugin
					JSONROOT.put("Result", "OK");
					JSONROOT.put("Records", levelList);
					JSONROOT.put("TotalRecordCount", userCount);

					// Convert Java Object to Json
					String jsonArray = gson.toJson(JSONROOT);
					response.getWriter().print(jsonArray);
				} else if (action.equals("create") || action.equals("update")) {
					TblLevel level = new TblLevel();

					// Set fields
					if (request.getParameter("level") != null) {
						String levelName = request.getParameter("level");
						level.setLevel(levelName);

					}

					if (request.getParameter("isActive") != null) {
						boolean isActive = Boolean.parseBoolean(request
								.getParameter("isActive"));
						level.setActive(isActive);
					}

					// end set fields

					if (action.equals("create")) {
						// Create new record
						daoLevel.addLevel(level);
					} else if (action.equals("update")) {
						// Update existing record
						String name = request
								.getParameter("jtRecordKey_level");
						daoLevel.updateLevel(level, name);
					}

					// Return in the format required by jTable plugin
					JSONROOT.put("Result", "OK");
					JSONROOT.put("Record", level);

					// Convert Java Object to Json
					String jsonArray = gson.toJson(JSONROOT);
					response.getWriter().print(jsonArray);
				} else if (action.equals("delete")) {
					// Delete record
					if (request.getParameter("level") != null) {
						String name = request.getParameter("level");
						daoLevel.deleteLevel(name);

						// Return in the format required by jTable plugin
						JSONROOT.put("Result", "OK");

						// Convert Java Object to Json
						String jsonArray = gson.toJson(JSONROOT);
						response.getWriter().print(jsonArray);
					}
				} else if (action.equals("excel")) {
					// Export to excel
					levelList = daoLevel.getAllLevels();
					// Return in the format required by jTable plugin
					JSONROOT.clear();
					JSONROOT.put("Records", levelList);
					// Convert Java Object to Json
					String jsonArray = gson.toJson(JSONROOT);
					response.getWriter().print(jsonArray);
				}
			} catch (Exception ex) {
				System.out.println("DataController: Data-tblLevel: "
						+ ex.getMessage());
				JSONROOT.put("Result", "ERROR");
				JSONROOT.put(
						"Message",
						(ex instanceof SQLException) ? getErrorMsg(
								((SQLException) ex).getErrorCode(),
								ex.getMessage(), action) : ex.getMessage());
				String error = gson.toJson(JSONROOT);
				response.getWriter().print(error);
			}

		}
	}
	
	

	protected void tblCurrency(String action, HttpServletRequest request,
			HttpServletResponse response, Gson gson) throws IOException {

		List<TblCurrency> curList = new ArrayList<TblCurrency>();
		if (action != null) {
			try {
				if (action.equals("list")) {
					// Fetch Data from User Table
					int startPageIndex = Integer.parseInt(request
							.getParameter("jtStartIndex"));
					int recordsPerPage = Integer.parseInt(request
							.getParameter("jtPageSize"));
					// Fetch Data from Supplier Table
					curList = daoCurrency.getAllCurrencies(startPageIndex,
							recordsPerPage);
					// Get Total Record Count for Pagination
					int userCount = daoCurrency.getCurrencyCount();
					// Return in the format required by jTable plugin
					JSONROOT.put("Result", "OK");
					JSONROOT.put("Records", curList);
					JSONROOT.put("TotalRecordCount", userCount);

					// Convert Java Object to Json
					String jsonArray = gson.toJson(JSONROOT);
					response.getWriter().print(jsonArray);
				} else if (action.equals("create") || action.equals("update")) {
					TblCurrency cur = new TblCurrency();

					// Set fields
					if (request.getParameter("currency") != null) {
						String currency = request.getParameter("currency");
						cur.setCurrency(currency);

					}
					
					if (request.getParameter("value") != null) {
						Double value = Double.parseDouble(request.getParameter("value"));
						cur.setValue(value);

					}

					if (request.getParameter("isActive") != null) {
						boolean isActive = Boolean.parseBoolean(request
								.getParameter("isActive"));
						cur.setActive(isActive);
					}

					// end set fields

					if (action.equals("create")) {
						// Create new record
						daoCurrency.addCurrency(cur);
					} else if (action.equals("update")) {
						// Update existing record
						String name = request
								.getParameter("jtRecordKey_currency");
						daoCurrency.updateCurrency(cur, name);
					}

					// Return in the format required by jTable plugin
					JSONROOT.put("Result", "OK");
					JSONROOT.put("Record", cur);

					// Convert Java Object to Json
					String jsonArray = gson.toJson(JSONROOT);
					response.getWriter().print(jsonArray);
				} else if (action.equals("delete")) {
					// Delete record
					if (request.getParameter("currency") != null) {
						String name = request.getParameter("currency");
						daoCurrency.deleteCurrency(name);

						// Return in the format required by jTable plugin
						JSONROOT.put("Result", "OK");

						// Convert Java Object to Json
						String jsonArray = gson.toJson(JSONROOT);
						response.getWriter().print(jsonArray);
					}
				} else if (action.equals("excel")) {
					// Export to excel
					curList = daoCurrency.getAllCurrencies();
					// Return in the format required by jTable plugin
					JSONROOT.clear();
					JSONROOT.put("Records", curList);
					// Convert Java Object to Json
					String jsonArray = gson.toJson(JSONROOT);
					response.getWriter().print(jsonArray);
				}
			} catch (Exception ex) {
				System.out.println("DataController: Data-tblCurrency: "
						+ ex.getMessage());
				JSONROOT.put("Result", "ERROR");
				JSONROOT.put(
						"Message",
						(ex instanceof SQLException) ? getErrorMsg(
								((SQLException) ex).getErrorCode(),
								ex.getMessage(), action) : ex.getMessage());
				String error = gson.toJson(JSONROOT);
				response.getWriter().print(error);
			}

		}
	}

	protected void tblIncident(String action, HttpServletRequest request,
			HttpServletResponse response, Gson gson) throws IOException {

		List<TblIncident> inciList = new ArrayList<TblIncident>();
		if (action != null) {
			try {
				if (action.equals("list")) {
					// Fetch Data from User Table
					int startPageIndex = Integer.parseInt(request
							.getParameter("jtStartIndex"));
					int recordsPerPage = Integer.parseInt(request
							.getParameter("jtPageSize"));
					// Fetch Data from Supplier Table
					inciList = daoIncident.getAllIncidents(startPageIndex,
							recordsPerPage);
					// Get Total Record Count for Pagination
					int userCount = daoIncident.getIncidentCount();
					// Return in the format required by jTable plugin
					JSONROOT.put("Result", "OK");
					JSONROOT.put("Records", inciList);
					JSONROOT.put("TotalRecordCount", userCount);

					// Convert Java Object to Json
					String jsonArray = gson.toJson(JSONROOT);
					response.getWriter().print(jsonArray);
				} else if (action.equals("create") || action.equals("update")) {
					TblIncident incident = new TblIncident();

					// Set fields
					if (request.getParameter("ci_id") != null) {
						byte ci_id = Byte.valueOf(request
								.getParameter("ci_id"));
						incident.setCiId(ci_id);
					}
					
					if (request.getParameter("time") != null) {
						int time = Integer.parseInt(request
								.getParameter("time"));
						incident.setIncidentTime(time);
					}

					if (request.getParameter("isActive") != null) {
						boolean active = Boolean.parseBoolean(request
								.getParameter("isActive"));
						incident.setActive(active);
					}
					
					// end set fields
					if (action.equals("create")) {
						// Create new record
						daoIncident.addIncident(incident);
					} else if (action.equals("update")) {
						// Update existing record
						byte ci_id = Byte.parseByte(request
								.getParameter("jtRecordKey_ci_id"));
						int time = Integer.valueOf(request
								.getParameter("jtRecordKey_time"));
						TblIncidentPK pk = new TblIncidentPK();
						pk.setCiId(ci_id);
						pk.setTime(time);
						daoIncident.updateIncident(incident, pk);
					}

					// Return in the format required by jTable plugin
					JSONROOT.put("Result", "OK");
					JSONROOT.put("Record", incident);

					// Convert Java Object to Json
					String jsonArray = gson.toJson(JSONROOT);
					response.getWriter().print(jsonArray);
				} else if (action.equals("delete")) {
					// Delete record
					if (request.getParameter("ci_id") != null && request.getParameter("time") != null) {
						byte ci_id = Byte.parseByte(request
								.getParameter("ci_id"));
						int time = Integer.valueOf(request.getParameter("time"));
						TblIncidentPK pk = new TblIncidentPK();
						pk.setCiId(ci_id);
						pk.setTime(time);
						daoIncident.deleteIncident(pk);

						// Return in the format required by jTable plugin
						JSONROOT.put("Result", "OK");

						// Convert Java Object to Json
						String jsonArray = gson.toJson(JSONROOT);
						response.getWriter().print(jsonArray);
					}
				} else if (action.equals("excel")) {
					// Export to excel
					inciList = daoIncident.getAllIncidents();
					// Return in the format required by jTable plugin
					JSONROOT.clear();
					JSONROOT.put("Records", inciList);
					// Convert Java Object to Json
					String jsonArray = gson.toJson(JSONROOT);
					response.getWriter().print(jsonArray);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				System.out.println("DataController: Data-tblIncident: "
						+ ex.getMessage());
				JSONROOT.put("Result", "ERROR");
				JSONROOT.put(
						"Message",
						(ex instanceof SQLException) ? getErrorMsg(
								((SQLException) ex).getErrorCode(),
								ex.getMessage(), action) : ex.getMessage());
				String error = gson.toJson(JSONROOT);
				response.getWriter().print(error);
			}
		}
	}

	protected void tblPriorityCost(String action, HttpServletRequest request,
			HttpServletResponse response, Gson gson) throws IOException {

		List<TblPriority_Cost> pcList = new ArrayList<TblPriority_Cost>();
		if (action != null) {
			try {
				if (action.equals("list")) {
					// Fetch Data from User Table
					int startPageIndex = Integer.parseInt(request
							.getParameter("jtStartIndex"));
					int recordsPerPage = Integer.parseInt(request
							.getParameter("jtPageSize"));
					// Fetch Data from Supplier Table
					pcList = daoPriorityCost.getAllPriorityCost(startPageIndex,
							recordsPerPage);
					// Get Total Record Count for Pagination
					int userCount = daoPriorityCost.getPriorityCostCount();
					// Return in the format required by jTable plugin
					JSONROOT.put("Result", "OK");
					JSONROOT.put("Records", pcList);
					JSONROOT.put("TotalRecordCount", userCount);

					// Convert Java Object to Json
					String jsonArray = gson.toJson(JSONROOT);
					response.getWriter().print(jsonArray);
				} else if (action.equals("create") || action.equals("update")) {
					TblPriority_Cost pc = new TblPriority_Cost();

					// Set fields
					if (request.getParameter("pName") != null) {
						String pName = request.getParameter("pName");
						pc.setPName(pName);

					}

					if (request.getParameter("pCost") != null) {
						double pCost = Double.parseDouble(request
								.getParameter("pCost"));
						pc.setPCost(pCost);
					}

					if (request.getParameter("isActive") != null) {
						boolean active = Boolean.parseBoolean(request
								.getParameter("isActive"));
						pc.setActive(active);
					}

					// end set fields

					if (action.equals("create")) {
						// Create new record
						daoPriorityCost.addPriorityCost(pc);
					} else if (action.equals("update")) {
						// Update existing record
						String pName = request
								.getParameter("jtRecordKey_pName");
						daoPriorityCost.updatePriorityCost(pc, pName);
					}

					// Return in the format required by jTable plugin
					JSONROOT.put("Result", "OK");
					JSONROOT.put("Record", pc);

					// Convert Java Object to Json
					String jsonArray = gson.toJson(JSONROOT);
					response.getWriter().print(jsonArray);
				} else if (action.equals("delete")) {
					// Delete record
					if (request.getParameter("pName") != null) {
						String pName = request.getParameter("pName");
						daoPriorityCost.deletePriorityCost(pName);

						// Return in the format required by jTable plugin
						JSONROOT.put("Result", "OK");

						// Convert Java Object to Json
						String jsonArray = gson.toJson(JSONROOT);
						response.getWriter().print(jsonArray);
					}
				} else if (action.equals("excel")) {
					// Export to excel
					pcList = daoPriorityCost.getAllPriorityCost();
					// Return in the format required by jTable plugin
					JSONROOT.clear();
					JSONROOT.put("Records", pcList);
					// Convert Java Object to Json
					String jsonArray = gson.toJson(JSONROOT);
					response.getWriter().print(jsonArray);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				System.out.println("DataController: Data-tblPriorityCost: "
						+ ex.getMessage());
				JSONROOT.put("Result", "ERROR");
				JSONROOT.put(
						"Message",
						(ex instanceof SQLException) ? getErrorMsg(
								((SQLException) ex).getErrorCode(),
								ex.getMessage(), action) : ex.getMessage());
				String error = gson.toJson(JSONROOT);
				response.getWriter().print(error);
			}
		}
	}
	
	protected void tblPriority(String action, HttpServletRequest request,
			HttpServletResponse response, Gson gson) throws IOException {

		List<TblPriority> pList = new ArrayList<TblPriority>();
		if (action != null) {
			try {
				if (action.equals("list")) {
					// Fetch Data from User Table
					int startPageIndex = Integer.parseInt(request
							.getParameter("jtStartIndex"));
					int recordsPerPage = Integer.parseInt(request
							.getParameter("jtPageSize"));
					// Fetch Data from Supplier Table
					pList = daoPriority.getAllPriorities(startPageIndex,
							recordsPerPage);
					// Get Total Record Count for Pagination
					int userCount = daoPriority.getPriorityCount();
					// Return in the format required by jTable plugin
					JSONROOT.put("Result", "OK");
					JSONROOT.put("Records", pList);
					JSONROOT.put("TotalRecordCount", userCount);

					// Convert Java Object to Json
					String jsonArray = gson.toJson(JSONROOT);
					response.getWriter().print(jsonArray);
				} else if (action.equals("create") || action.equals("update")) {
					TblPriority pc = new TblPriority();

					// Set fields
					if (request.getParameter("urgency") != null) {
						String ur = request.getParameter("urgency");
						pc.setUrgency(ur);

					}

					if (request.getParameter("impact") != null) {
						String im = request.getParameter("impact");
						pc.setImpact(im);

					}
					
					if (request.getParameter("priorityName") != null) {
						String name = request.getParameter("priorityName");
						pc.setPriorityName(name);

					}

					if (request.getParameter("isActive") != null) {
						boolean active = Boolean.parseBoolean(request
								.getParameter("isActive"));
						pc.setActive(active);
					}

					// end set fields

					if (action.equals("create")) {
						// Create new record
						daoPriority.addPriority(pc);
					} else if (action.equals("update")) {
						// Update existing record
						String urgency = request
								.getParameter("jtRecordKey_urgency");
						String impact = request
								.getParameter("jtRecordKey_impact");
						TblPriorityPK id = new TblPriorityPK();
						id.setUrgency(urgency);
						id.setImpact(impact);
						daoPriority.updatePriority(pc,id);
					}

					// Return in the format required by jTable plugin
					JSONROOT.put("Result", "OK");
					JSONROOT.put("Record", pc);

					// Convert Java Object to Json
					String jsonArray = gson.toJson(JSONROOT);
					response.getWriter().print(jsonArray);
				} else if (action.equals("delete")) {
					// Delete record
					TblPriorityPK id  = new TblPriorityPK();
					if (request.getParameter("urgency") != null
							&& request.getParameter("impact") != null) {
						String urgency = request
								.getParameter("urgency");
						String impact = request
								.getParameter("impact");
						id.setUrgency(urgency);
						id.setImpact(impact);
						daoPriority.deletePriority(id);
						// Return in the format required by jTable plugin
						JSONROOT.put("Result", "OK");

						// Convert Java Object to Json
						String jsonArray = gson.toJson(JSONROOT);
						response.getWriter().print(jsonArray);
					}
				} else if (action.equals("excel")) {
					// Export to excel
					pList = daoPriority.getAllPriorities();
					// Return in the format required by jTable plugin
					JSONROOT.clear();
					JSONROOT.put("Records", pList);
					// Convert Java Object to Json
					String jsonArray = gson.toJson(JSONROOT);
					response.getWriter().print(jsonArray);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				System.out.println("DataController: Data-tblPriority: "
						+ ex.getMessage());
				JSONROOT.put("Result", "ERROR");
				JSONROOT.put(
						"Message",
						(ex instanceof SQLException) ? getErrorMsg(
								((SQLException) ex).getErrorCode(),
								ex.getMessage(), action) : ex.getMessage());
				String error = gson.toJson(JSONROOT);
				response.getWriter().print(error);
			}
		}
	}

	protected void tblService(String action, HttpServletRequest request,
			HttpServletResponse response, Gson gson) throws IOException {

		List<TblService> serviceList = new ArrayList<TblService>();
		if (action != null) {
			try {
				if (action.equals("list")) {
					// Fetch Data from User Table
					int startPageIndex = Integer.parseInt(request
							.getParameter("jtStartIndex"));
					int recordsPerPage = Integer.parseInt(request
							.getParameter("jtPageSize"));
					// Fetch Data from Supplier Table
					serviceList = daoService.getAllServices(startPageIndex,
							recordsPerPage);
					// Get Total Record Count for Pagination
					int userCount = daoService.getServiceCount();
					// Return in the format required by jTable plugin
					JSONROOT.put("Result", "OK");
					JSONROOT.put("Records", serviceList);
					JSONROOT.put("TotalRecordCount", userCount);

					// Convert Java Object to Json
					String jsonArray = gson.toJson(JSONROOT);
					response.getWriter().print(jsonArray);
				} else if (action.equals("create") || action.equals("update")) {
					TblService ser = new TblService();

					// Set fields
					if (request.getParameter("serviceId") != null) {
						byte sId = Byte.parseByte(request
								.getParameter("serviceId"));
						ser.setServiceId(sId);
					}

					if (request.getParameter("fixedCost") != null) {
						double fixedC = Double.parseDouble(request
								.getParameter("fixedCost"));
						ser.setFixedCost(fixedC);
					}

					if (request.getParameter("fixedIncome") != null) {
						double fixedI = Double.parseDouble(request
								.getParameter("fixedIncome"));
						ser.setFixedIncome(fixedI);
					}

					if (request.getParameter("isActive") != null) {
						boolean active = Boolean.parseBoolean(request
								.getParameter("isActive"));
						ser.setActive(active);
					}

					if (request.getParameter("isTechnical") != null) {
						Boolean tech = Boolean.parseBoolean(request
								.getParameter("isTechnical"));
						ser.setIsTechnical(tech);
					}

					if (request.getParameter("serviceCode") != null) {
						String code = request.getParameter("serviceCode");
						ser.setServiceCode(code);
					}

					if (request.getParameter("serviceName") != null) {
						String name = request.getParameter("serviceName");
						ser.setServiceName(name);
					}

					if (request.getParameter("urgency") != null) {
						String urgency = request.getParameter("urgency");
						ser.setUrgency(urgency);
					}

					if (request.getParameter("impact") != null) {
						String im = request.getParameter("impact");
						ser.setImpact(im);
					}
					
					if (request.getParameter("event_id") != null){
						byte event_id = Byte.parseByte(request.getParameter("event_id"));
						ser.setEventId(event_id);
					}

					// end set fields

					if (action.equals("create")) {
						// Create new record
						daoService.addService(ser);
					} else if (action.equals("update")) {
						// Update existing record
						byte id = Byte.parseByte(request
								.getParameter("jtRecordKey_serviceId"));
						daoService.updateService(ser, id);
					}

					// Return in the format required by jTable plugin
					JSONROOT.put("Result", "OK");
					JSONROOT.put("Record", ser);

					// Convert Java Object to Json
					String jsonArray = gson.toJson(JSONROOT);
					response.getWriter().print(jsonArray);
				} else if (action.equals("delete")) {
					// Delete record
					if (request.getParameter("serviceId") != null) {
						byte sId = Byte.parseByte(request
								.getParameter("serviceId"));
						daoService.deleteService(sId);

						// Return in the format required by jTable plugin
						JSONROOT.put("Result", "OK");

						// Convert Java Object to Json
						String jsonArray = gson.toJson(JSONROOT);
						response.getWriter().print(jsonArray);
					}
				} else if (action.equals("excel")) {
					// Export to excel
					serviceList = daoService.getAllServices();
					// Return in the format required by jTable plugin
					JSONROOT.clear();
					JSONROOT.put("Records", serviceList);
					// Convert Java Object to Json
					String jsonArray = gson.toJson(JSONROOT);
					response.getWriter().print(jsonArray);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				System.out.println("DataController: Data-tblService: "
						+ ex.getMessage());
				JSONROOT.put("Result", "ERROR");
				JSONROOT.put(
						"Message",
						(ex instanceof SQLException) ? getErrorMsg(
								((SQLException) ex).getErrorCode(),
								ex.getMessage(), action) : ex.getMessage());
				String error = gson.toJson(JSONROOT);
				response.getWriter().print(error);
			}
		}
	}

	protected void tblServiceDepartment(String action,
			HttpServletRequest request, HttpServletResponse response, Gson gson)
			throws IOException {

		List<TblService_Department> serDepList = new ArrayList<TblService_Department>();
		if (action != null) {
			try {
				if (action.equals("list")) {
					// Fetch Data from User Table
					int startPageIndex = Integer.parseInt(request
							.getParameter("jtStartIndex"));
					int recordsPerPage = Integer.parseInt(request
							.getParameter("jtPageSize"));
					// Fetch Data from Supplier Table
					serDepList = daoServiceDepartment.getAllServiceDepartments(
							startPageIndex, recordsPerPage);
					// Get Total Record Count for Pagination
					int userCount = daoServiceDepartment
							.getServiceDepartmentCount();
					// Return in the format required by jTable plugin
					JSONROOT.put("Result", "OK");
					JSONROOT.put("Records", serDepList);
					JSONROOT.put("TotalRecordCount", userCount);

					// Convert Java Object to Json
					String jsonArray = gson.toJson(JSONROOT);
					response.getWriter().print(jsonArray);
				} else if (action.equals("create") || action.equals("update")) {
					TblService_Department ser = new TblService_Department();

					// Set fields
					if (request.getParameter("service_ID") != null) {
						byte serId = Byte.parseByte(request
								.getParameter("service_ID"));
						ser.setService_ID(serId);

					}

					if (request.getParameter("departmentName") != null) {
						String departmentName = request
								.getParameter("departmentName");
						ser.setDepartmentName(departmentName);
					}

					if (request.getParameter("divisionName") != null) {
						String divisionName = request
								.getParameter("divisionName");
						ser.setDivisionName(divisionName);
					}

					if (request.getParameter("isActive") != null) {
						boolean active = Boolean.parseBoolean(request
								.getParameter("isActive"));
						ser.setActive(active);
					}

					// end set fields

					if (action.equals("create")) {
						// Create new record
						daoServiceDepartment.addServiceDepartment(ser);
					} else if (action.equals("update")) {
						// Update existing record
						byte id = Byte.parseByte(request
								.getParameter("jtRecordKey_service_ID"));
						String depName = request
								.getParameter("jtRecordKey_departmentName");
						String divName = request
								.getParameter("jtRecordKey_divisionName");
						TblService_DepartmentPK pk = new TblService_DepartmentPK();
						pk.setService_ID(id);
						pk.setDepartmentName(depName);
						pk.setDivisionName(divName);
						daoServiceDepartment.updateServiceDepartment(ser, pk);
					}

					// Return in the format required by jTable plugin
					JSONROOT.put("Result", "OK");
					JSONROOT.put("Record", ser);

					// Convert Java Object to Json
					String jsonArray = gson.toJson(JSONROOT);
					response.getWriter().print(jsonArray);
				} else if (action.equals("delete")) {
					// Delete record
					if (request.getParameter("service_ID") != null) {
						byte serId = Byte.parseByte(request
								.getParameter("service_ID"));
						if (request.getParameter("departmentName") != null) {
							String departmentName = request
									.getParameter("departmentName");
							if (request.getParameter("divisionName") != null) {
								String divisionName = request
										.getParameter("divisionName");
								TblService_DepartmentPK pk = new TblService_DepartmentPK();
								pk.setService_ID(serId);
								pk.setDepartmentName(departmentName);
								pk.setDivisionName(divisionName);

								daoServiceDepartment
										.deleteServiceDepartment(pk);

								// Return in the format required by jTable
								// plugin
								JSONROOT.put("Result", "OK");

								// Convert Java Object to Json
								String jsonArray = gson.toJson(JSONROOT);
								response.getWriter().print(jsonArray);
							}
						}
					}

				} else if (action.equals("excel")) {
					// Export to excel
					serDepList = daoServiceDepartment
							.getAllServiceDepartments();
					// Return in the format required by jTable plugin
					JSONROOT.clear();
					JSONROOT.put("Records", serDepList);
					// Convert Java Object to Json
					String jsonArray = gson.toJson(JSONROOT);
					response.getWriter().print(jsonArray);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				System.out.println("DataController: Data-tblServiceDep: "
						+ ex.getMessage());
				JSONROOT.put("Result", "ERROR");
				JSONROOT.put(
						"Message",
						(ex instanceof SQLException) ? getErrorMsg(
								((SQLException) ex).getErrorCode(),
								ex.getMessage(), action) : ex.getMessage());
				String error = gson.toJson(JSONROOT);
				response.getWriter().print(error);
			}
		}
	}

	// protected void tblServiceDivision(String action,
	// HttpServletRequest request, HttpServletResponse response, Gson gson)
	// throws IOException {
	//
	// List<TblService_Division> serDivList = new
	// ArrayList<TblService_Division>();
	// if (action != null) {
	// try {
	// if (action.equals("list")) {
	// // Fetch Data from User Table
	// int startPageIndex = Integer.parseInt(request
	// .getParameter("jtStartIndex"));
	// int recordsPerPage = Integer.parseInt(request
	// .getParameter("jtPageSize"));
	// // Fetch Data from Supplier Table
	// serDivList = daoServiceDivision.getAllServiceDivisions(
	// startPageIndex, recordsPerPage);
	// // Get Total Record Count for Pagination
	// int userCount = daoServiceDivision
	// .getServiceDivisionCount();
	// // Return in the format required by jTable plugin
	// JSONROOT.put("Result", "OK");
	// JSONROOT.put("Records", serDivList);
	// JSONROOT.put("TotalRecordCount", userCount);
	//
	// // Convert Java Object to Json
	// String jsonArray = gson.toJson(JSONROOT);
	// response.getWriter().print(jsonArray);
	// } else if (action.equals("create") || action.equals("update")) {
	// TblService_Division ser = new TblService_Division();
	//
	// // Set fields
	// if (request.getParameter("service_ID") != null) {
	// byte serId = Byte.parseByte(request
	// .getParameter("service_ID"));
	// ser.setService_ID(serId);
	//
	// }
	//
	// if (request.getParameter("divisionName") != null) {
	// String divisionName = request
	// .getParameter("divisionName");
	// ser.setDivisionName(divisionName);
	// }
	//
	// if (request.getParameter("isActive") != null) {
	// boolean active = Boolean.parseBoolean(request
	// .getParameter("isActive"));
	// ser.setIsActive(active);
	// }
	//
	// // end set fields
	//
	// if (action.equals("create")) {
	// // Create new record
	// daoServiceDivision.addServiceDivision(ser);
	// } else if (action.equals("update")) {
	// // Update existing record
	// byte id = Byte.parseByte(request
	// .getParameter("jtRecordKey_service_ID"));
	// String divName = request
	// .getParameter("jtRecordKey_divisionName");
	// TblService_DivisionPK pk = new TblService_DivisionPK();
	// pk.setService_ID(id);
	// pk.setDivisionName(divName);
	// daoServiceDivision.updateServiceDivision(ser, pk);
	// }
	//
	// // Return in the format required by jTable plugin
	// JSONROOT.put("Result", "OK");
	// JSONROOT.put("Record", ser);
	//
	// // Convert Java Object to Json
	// String jsonArray = gson.toJson(JSONROOT);
	// response.getWriter().print(jsonArray);
	// } else if (action.equals("delete")) {
	// // Delete record
	// if (request.getParameter("service_ID") != null) {
	// byte serId = Byte.parseByte(request
	// .getParameter("service_ID"));
	// if (request.getParameter("divisionName") != null) {
	// String divisionName = request
	// .getParameter("divisionName");
	// TblService_DivisionPK pk = new TblService_DivisionPK();
	// pk.setService_ID(serId);
	// pk.setDivisionName(divisionName);
	//
	// daoServiceDivision.deleteServiceDivision(pk);
	//
	// // Return in the format required by jTable plugin
	// JSONROOT.put("Result", "OK");
	//
	// // Convert Java Object to Json
	// String jsonArray = gson.toJson(JSONROOT);
	// response.getWriter().print(jsonArray);
	// }
	// }
	//
	// } else if (action.equals("excel")) {
	// // Export to excel
	// serDivList = daoServiceDivision.getAllServiceDivisions();
	// // Return in the format required by jTable plugin
	// JSONROOT.clear();
	// JSONROOT.put("Records", serDivList);
	// // Convert Java Object to Json
	// String jsonArray = gson.toJson(JSONROOT);
	// response.getWriter().print(jsonArray);
	// }
	// } catch (Exception ex) {
	// ex.printStackTrace();
	// System.out.println("DataController: Data-tblServiceDiv: "
	// + ex.getMessage());
	// JSONROOT.put("Result", "ERROR");
	// JSONROOT.put(
	// "Message",
	// (ex instanceof SQLException) ? getErrorMsg(
	// ((SQLException) ex).getErrorCode(),
	// ex.getMessage(), action) : ex.getMessage());
	// String error = gson.toJson(JSONROOT);
	// response.getWriter().print(error);
	// }
	// }
	// }

	protected void tblCIs(String action, HttpServletRequest request,
			HttpServletResponse response, Gson gson) throws IOException {

		List<TblCI> ciList = new ArrayList<TblCI>();
		if (action != null) {
			try {
				if (action.equals("list")) {
					// Fetch Data from User Table
					int startPageIndex = Integer.parseInt(request
							.getParameter("jtStartIndex"));
					int recordsPerPage = Integer.parseInt(request
							.getParameter("jtPageSize"));
					// Fetch Data from Supplier Table
					ciList = daoCI.getAllCIs(startPageIndex, recordsPerPage);
					// Get Total Record Count for Pagination
					int ciCount = daoCI.getCICount();
					// Return in the format required by jTable plugin
					JSONROOT.put("Result", "OK");
					JSONROOT.put("Records", ciList);
					JSONROOT.put("TotalRecordCount", ciCount);

					// Convert Java Object to Json
					String jsonArray = gson.toJson(JSONROOT);
					response.getWriter().print(jsonArray);
				} else if (action.equals("create") || action.equals("update")) {
					TblCI ci = new TblCI();
					Byte ciId = null;

					// Set fields
					if (request.getParameter("CI_name") != null) {
						String CI_name = request.getParameter("CI_name");
						ci.setCI_name(CI_name);
					}

					if (request.getParameter("ciId") != null) {
						ciId = Byte.parseByte(request.getParameter("ciId"));
						ci.setCiId(ciId);
					}
					
					if (request.getParameter("solutionId") != null){
						int solutionId = Integer.parseInt(request.getParameter("solutionId"));
						ci.setSolutionId(solutionId);
					}

					if (request.getParameter("isActive") != null) {
						Boolean isActive = Boolean.parseBoolean(request
								.getParameter("isActive"));
						ci.setActive(isActive);
					}

					if (request.getParameter("supplierName1") != null) {
						String supName1 = request.getParameter("supplierName1");
						ci.setSupplierName1(supName1);
					}

					if (request.getParameter("supplierName2") != null) {
						String supName2 = request.getParameter("supplierName2");
						ci.setSupplierName2(supName2);
					}
					// end set fields

					if (action.equals("create")) {
						// Create new record
						daoCI.addCI(ci);
					} else if (action.equals("update")) {
						// Update existing record
						byte id = Byte.parseByte(request
								.getParameter("jtRecordKey_ciId"));
						daoCI.updateCI(ci, id);
					}

					// Return in the format required by jTable plugin
					JSONROOT.put("Result", "OK");
					JSONROOT.put("Record", ci);

					// Convert Java Object to Json
					String jsonArray = gson.toJson(JSONROOT);
					response.getWriter().print(jsonArray);
				} else if (action.equals("delete")) {
					// Delete record
					if (request.getParameter("ciId") != null) {
						Byte CiId = Byte
								.parseByte(request.getParameter("ciId"));
						daoCI.deleteCI(CiId);

						// Return in the format required by jTable plugin
						JSONROOT.put("Result", "OK");

						// Convert Java Object to Json
						String jsonArray = gson.toJson(JSONROOT);
						response.getWriter().print(jsonArray);
					}
				} else if (action.equals("excel")) {
					// Export to excel
					ciList = daoCI.getAllCIs();
					// Return in the format required by jTable plugin
					JSONROOT.clear();
					JSONROOT.put("Records", ciList);
					// Convert Java Object to Json
					String jsonArray = gson.toJson(JSONROOT);
					response.getWriter().print(jsonArray);
				}
			} catch (Exception ex) {
				// ex.printStackTrace();
				System.out.println("DataController: Data-tblCi: "
						+ ex.getMessage());
				JSONROOT.put("Result", "ERROR");
				JSONROOT.put(
						"Message",
						(ex instanceof SQLException) ? getErrorMsg(
								((SQLException) ex).getErrorCode(),
								ex.getMessage(), action) : ex.getMessage());
				String error = gson.toJson(JSONROOT);
				response.getWriter().print(error);
			}
		}
	}

	protected void tblCMDBs(String action, HttpServletRequest request,
			HttpServletResponse response, Gson gson) throws IOException {

		List<TblCMDB> cmdbList = new ArrayList<TblCMDB>();
		if (action != null) {
			try {
				if (action.equals("list")) {
					// Fetch Data from User Table
					int startPageIndex = Integer.parseInt(request
							.getParameter("jtStartIndex"));
					int recordsPerPage = Integer.parseInt(request
							.getParameter("jtPageSize"));
					// Fetch Data from Supplier Table
					cmdbList = daoCMDB.getAllCMDBs(startPageIndex,
							recordsPerPage);
					// Get Total Record Count for Pagination
					int userCount = daoCMDB.getCMDBCount();
					// Return in the format required by jTable plugin
					JSONROOT.put("Result", "OK");
					JSONROOT.put("Records", cmdbList);
					JSONROOT.put("TotalRecordCount", userCount);

					// Convert Java Object to Json
					String jsonArray = gson.toJson(JSONROOT);
					response.getWriter().print(jsonArray);
				} else if (action.equals("create") || action.equals("update")) {
					TblCMDB cmdb = new TblCMDB();

					// Set fields
					if (request.getParameter("ciId") != null) {
						Byte ciId = Byte
								.parseByte(request.getParameter("ciId"));
						cmdb.setCiId(ciId);
					}

					if (request.getParameter("serviceId") != null) {
						Byte serviceId = Byte.parseByte(request
								.getParameter("serviceId"));
						cmdb.setServiceId(serviceId);

					}

					if (request.getParameter("isActive") != null) {
						Boolean isActive = Boolean.parseBoolean(request
								.getParameter("isActive"));
						cmdb.setActive(isActive);
					}
					// end set fields

					if (action.equals("create")) {
						// Create new record
						daoCMDB.addCMDB(cmdb);
					} else if (action.equals("update")) {
						// Update existing record
						byte cId = Byte.parseByte(request
								.getParameter("jtRecordKey_ciId"));
						byte sId = Byte.parseByte(request
								.getParameter("jtRecordKey_serviceId"));
						TblCMDBPK pk = new TblCMDBPK();
						pk.setCiId(cId);
						pk.setServiceId(sId);
						daoCMDB.updateCMDB(cmdb, pk);
					}

					// Return in the format required by jTable plugin
					JSONROOT.put("Result", "OK");
					JSONROOT.put("Record", cmdb);

					// Convert Java Object to Json
					String jsonArray = gson.toJson(JSONROOT);
					response.getWriter().print(jsonArray);
				} else if (action.equals("delete")) {
					// Delete record
					if (request.getParameter("ciId") != null
							&& request.getParameter("serviceId") != null) {
						Byte ciId = Byte
								.parseByte(request.getParameter("ciId"));
						Byte serviceId = Byte.parseByte(request
								.getParameter("serviceId"));
						TblCMDBPK pk = new TblCMDBPK();
						pk.setCiId(ciId);
						pk.setServiceId(serviceId);
						daoCMDB.deleteCMDB(pk);

						// Return in the format required by jTable plugin
						JSONROOT.put("Result", "OK");

						// Convert Java Object to Json
						String jsonArray = gson.toJson(JSONROOT);
						response.getWriter().print(jsonArray);
					}
				} else if (action.equals("excel")) {
					// Export to excel
					cmdbList = daoCMDB.getAllCMDBs();
					// Return in the format required by jTable plugin
					JSONROOT.clear();
					JSONROOT.put("Records", cmdbList);
					// Convert Java Object to Json
					String jsonArray = gson.toJson(JSONROOT);
					response.getWriter().print(jsonArray);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				System.out.println("DataController: Data-tblCMDB: "
						+ ex.getMessage());
				JSONROOT.put("Result", "ERROR");
				JSONROOT.put(
						"Message",
						(ex instanceof SQLException) ? getErrorMsg(
								((SQLException) ex).getErrorCode(),
								ex.getMessage(), action) : ex.getMessage());
				String error = gson.toJson(JSONROOT);
				response.getWriter().print(error);
			}
		}
	}

	protected void tblDepartments(String action, HttpServletRequest request,
			HttpServletResponse response, Gson gson) throws IOException {

		List<TblDepartment> depList = new ArrayList<TblDepartment>();
		if (action != null) {
			try {
				TblDepartmentPK pk = new TblDepartmentPK();
				if (action.equals("list")) {
					// Fetch Data from User Table
					int startPageIndex = Integer.parseInt(request
							.getParameter("jtStartIndex"));
					int recordsPerPage = Integer.parseInt(request
							.getParameter("jtPageSize"));
					// Fetch Data from Supplier Table
					depList = daoDepartment.getAllDepartments(startPageIndex,
							recordsPerPage);
					// Get Total Record Count for Pagination
					int userCount = daoDepartment.getDepartmentCount();
					// Return in the format required by jTable plugin
					JSONROOT.put("Result", "OK");
					JSONROOT.put("Records", depList);
					JSONROOT.put("TotalRecordCount", userCount);

					// Convert Java Object to Json
					String jsonArray = gson.toJson(JSONROOT);
					response.getWriter().print(jsonArray);
				} else if (action.equals("create") || action.equals("update")) {
					TblDepartment department = new TblDepartment();

					// Set fields
					if (request.getParameter("departmentName") != null) {
						String departmentName = request
								.getParameter("departmentName");
						department.setDepartmentName(departmentName);
					}

					if (request.getParameter("divisionName") != null) {
						String divisionName = request
								.getParameter("divisionName");
						department.setDivisionName(divisionName);
					}

					if (request.getParameter("shortName") != null) {
						String shortName = request.getParameter("shortName");
						department.setShortName(shortName);

					}

					if (request.getParameter("isActive") != null) {
						Boolean isActive = Boolean.parseBoolean(request
								.getParameter("isActive"));
						department.setActive(isActive);
					}
					// end set fields

					if (action.equals("create")) {
						// Create new record
						daoDepartment.addDepartment(department);
					} else if (action.equals("update")) {
						// Update existing record
						String dep = request
								.getParameter("jtRecordKey_departmentName");
						String div = request
								.getParameter("jtRecordKey_divisionName");

						pk.setDepartmentName(dep);
						pk.setDevisionName(div);
						daoDepartment.updateDepartment(department, pk);
					}

					// Return in the format required by jTable plugin
					JSONROOT.put("Result", "OK");
					JSONROOT.put("Record", department);

					// Convert Java Object to Json
					String jsonArray = gson.toJson(JSONROOT);
					response.getWriter().print(jsonArray);
				} else if (action.equals("delete")) {
					// Delete record
					if (request.getParameter("departmentName") != null
							&& request.getParameter("divisionName") != null) {
						String departmentName = request
								.getParameter("departmentName");
						String divisionName = request
								.getParameter("divisionName");
						pk.setDepartmentName(departmentName);
						pk.setDevisionName(divisionName);
						daoDepartment.deleteDepartment(pk);

						// Return in the format required by jTable plugin
						JSONROOT.put("Result", "OK");

						// Convert Java Object to Json
						String jsonArray = gson.toJson(JSONROOT);
						response.getWriter().print(jsonArray);
					}
				} else if (action.equals("excel")) {
					// Export to excel
					depList = daoDepartment.getAllDepartments();
					// Return in the format required by jTable plugin
					JSONROOT.clear();
					JSONROOT.put("Records", depList);
					// Convert Java Object to Json
					String jsonArray = gson.toJson(JSONROOT);
					response.getWriter().print(jsonArray);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				System.out.println("DataController: Data-tblDepartment: "
						+ ex.getMessage());
				JSONROOT.put("Result", "ERROR");
				JSONROOT.put(
						"Message",
						(ex instanceof SQLException) ? getErrorMsg(
								((SQLException) ex).getErrorCode(),
								ex.getMessage(), action) : ex.getMessage());
				String error = gson.toJson(JSONROOT);
				response.getWriter().print(error);
			}
		}
	}

	protected void tblDivisions(String action, HttpServletRequest request,
			HttpServletResponse response, Gson gson) throws IOException {

		List<TblDivision> divisionList = new ArrayList<TblDivision>();
		if (action != null) {
			try {
				if (action.equals("list")) {
					// Fetch Data from User Table
					int startPageIndex = Integer.parseInt(request
							.getParameter("jtStartIndex"));
					int recordsPerPage = Integer.parseInt(request
							.getParameter("jtPageSize"));
					// Fetch Data from Supplier Table
					divisionList = daoDivision.getAllDivisions(startPageIndex,
							recordsPerPage);
					// Get Total Record Count for Pagination
					int userCount = daoDivision.getDivisionCount();
					// Return in the format required by jTable plugin
					JSONROOT.put("Result", "OK");
					JSONROOT.put("Records", divisionList);
					JSONROOT.put("TotalRecordCount", userCount);

					// Convert Java Object to Json
					String jsonArray = gson.toJson(JSONROOT);
					response.getWriter().print(jsonArray);
				} else if (action.equals("create") || action.equals("update")) {
					TblDivision division = new TblDivision();

					// Set fields
					if (request.getParameter("divisionName") != null) {
						String divisionName = request
								.getParameter("divisionName");
						division.setDivisionName(divisionName);

					}

					if (request.getParameter("shortName") != null) {
						String shortName = request.getParameter("shortName");
						division.setShortName(shortName);
					}

					if (request.getParameter("isActive") != null) {
						Boolean isActive = Boolean.parseBoolean(request
								.getParameter("isActive"));
						division.setActive(isActive);
					}

					// end set fields

					if (action.equals("create")) {
						// Create new record
						daoDivision.addDivision(division);
					} else if (action.equals("update")) {
						// Update existing record
						String div = request
								.getParameter("jtRecordKey_divisionName");
						daoDivision.updateDivision(division, div);
					}

					// Return in the format required by jTable plugin
					JSONROOT.put("Result", "OK");
					JSONROOT.put("Record", division);

					// Convert Java Object to Json
					String jsonArray = gson.toJson(JSONROOT);
					response.getWriter().print(jsonArray);
				} else if (action.equals("delete")) {
					// Delete record
					if (request.getParameter("divisionName") != null) {
						String divisionName = request
								.getParameter("divisionName");
						daoDivision.deleteDivision(divisionName);

						// Return in the format required by jTable plugin
						JSONROOT.put("Result", "OK");

						// Convert Java Object to Json
						String jsonArray = gson.toJson(JSONROOT);
						response.getWriter().print(jsonArray);
					}
				} else if (action.equals("excel")) {
					// Export to excel
					divisionList = daoDivision.getAllDivisions();
					// Return in the format required by jTable plugin
					JSONROOT.clear();
					JSONROOT.put("Records", divisionList);
					// Convert Java Object to Json
					String jsonArray = gson.toJson(JSONROOT);
					response.getWriter().print(jsonArray);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				System.out.println("DataController: Data-tblDivision: "
						+ ex.getMessage());
				JSONROOT.put("Result", "ERROR");
				JSONROOT.put(
						"Message",
						(ex instanceof SQLException) ? getErrorMsg(
								((SQLException) ex).getErrorCode(),
								ex.getMessage(), action) : ex.getMessage());
				String error = gson.toJson(JSONROOT);
				response.getWriter().print(error);
			}
		}
	}

	// protected void tblEvents(String action, HttpServletRequest request,
	// HttpServletResponse response, Gson gson) throws IOException {
	//
	// List<TblEvent> eventList = new ArrayList<TblEvent>();
	// if (action != null) {
	// try {
	// if (action.equals("list")) {
	// // Fetch Data from User Table
	// int startPageIndex = Integer.parseInt(request
	// .getParameter("jtStartIndex"));
	// int recordsPerPage = Integer.parseInt(request
	// .getParameter("jtPageSize"));
	// // Fetch Data from Supplier Table
	// eventList = daoEvent.getAllEvents(startPageIndex,
	// recordsPerPage);
	// // Get Total Record Count for Pagination
	// int userCount = daoEvent.getEventCount();
	// // Return in the format required by jTable plugin
	// JSONROOT.put("Result", "OK");
	// JSONROOT.put("Records", eventList);
	// JSONROOT.put("TotalRecordCount", userCount);
	//
	// // Convert Java Object to Json
	// String jsonArray = gson.toJson(JSONROOT);
	// response.getWriter().print(jsonArray);
	// } else if (action.equals("create") || action.equals("update")) {
	// TblEvent event = new TblEvent();
	//
	// // Set fields
	// if (request.getParameter("eventId") != null) {
	// int eventId = Integer.parseInt(request
	// .getParameter("eventId"));
	// event.setEventId(eventId);
	//
	// }
	//
	// if (request.getParameter("incidentId") != null) {
	// Byte incidentId = Byte.parseByte(request
	// .getParameter("incidentId"));
	// event.setIncidentId(incidentId);
	// }
	//
	// if (request.getParameter("serviceId") != null) {
	// Byte serviceId = Byte.parseByte(request
	// .getParameter("serviceId"));
	// event.setServiceId(serviceId);
	// }
	//
	// if (request.getParameter("round") != null) {
	// Byte round = Byte.parseByte(request.getParameter("round"));
	// event.setRound(round);
	// }
	//
	// if (request.getParameter("session") != null) {
	// Byte session = Byte.parseByte(request.getParameter("session"));
	// event.setSession(session);
	// }
	//
	// if (request.getParameter("isActive") != null) {
	// Boolean isActive = Boolean.parseBoolean(request
	// .getParameter("isActive"));
	// event.setIsActive(isActive);
	// }
	// // end set fields
	//
	// if (action.equals("create")) {
	// // Create new record
	// daoEvent.addEvent(event);
	// } else if (action.equals("update")) {
	// // Update existing record
	// int id = Integer.parseInt(request
	// .getParameter("jtRecordKey_eventId"));
	// daoEvent.updateEvent(event, id);
	// }
	//
	// // Return in the format required by jTable plugin
	// JSONROOT.put("Result", "OK");
	// JSONROOT.put("Record", event);
	//
	// // Convert Java Object to Json
	// String jsonArray = gson.toJson(JSONROOT);
	// response.getWriter().print(jsonArray);
	// } else if (action.equals("delete")) {
	// // Delete record
	// if (request.getParameter("eventId") != null) {
	// int eventId = Integer.parseInt(request
	// .getParameter("eventId"));
	// daoEvent.deleteEvent(eventId);
	//
	// // Return in the format required by jTable plugin
	// JSONROOT.put("Result", "OK");
	//
	// // Convert Java Object to Json
	// String jsonArray = gson.toJson(JSONROOT);
	// response.getWriter().print(jsonArray);
	// }
	// } else if (action.equals("excel")) {
	// // Export to excel
	// eventList = daoEvent.getAllEvents();
	// // Return in the format required by jTable plugin
	// JSONROOT.clear();
	// JSONROOT.put("Records", eventList);
	// // Convert Java Object to Json
	// String jsonArray = gson.toJson(JSONROOT);
	// response.getWriter().print(jsonArray);
	// }
	// } catch (Exception ex) {
	// ex.printStackTrace();
	// System.out.println("DataController: Data-tblEvent: "
	// + ex.getMessage());
	// JSONROOT.put("Result", "ERROR");
	// JSONROOT.put(
	// "Message",
	// (ex instanceof SQLException) ? getErrorMsg(
	// ((SQLException) ex).getErrorCode(),
	// ex.getMessage(), action) : ex.getMessage());
	// String error = gson.toJson(JSONROOT);
	// response.getWriter().print(error);
	// }
	// }
	// }

	@SuppressWarnings("unchecked")
	private <T> JSONArray toOptionArray(List<T> table, String key,
			String display) {

		JSONArray optionArray = new JSONArray();
		Class<? extends Object> objClass = table.get(0).getClass();
		Field keyField;
		Field displayField;
		String keyResult;
		String displayResult;

		try {
			keyField = objClass.getDeclaredField(key);
			displayField = objClass.getDeclaredField(display);

			keyField.setAccessible(true);
			displayField.setAccessible(true);

			for (T record : table) {
				keyResult = keyField.get(record).toString();
				displayResult = displayField.get(record).toString();

				JSONObject option = new JSONObject();
				option.put("DisplayText", displayResult);
				option.put("Value", keyResult);
				optionArray.add(option);
			}

		} catch (NoSuchFieldException | SecurityException
				| IllegalArgumentException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return optionArray;
	}

	private String getErrorMsg(int errCode, String errMsg, String action) {
		switch (errCode) {
		case 1062:
			Pattern pattern = Pattern.compile("'(.*?)'");
			Matcher matcher = pattern.matcher(errMsg);
			String key = "";
			if (matcher.find())
				key = matcher.group(1);
			return "Key '" + key + "' already exists. (Duplicate Error)";
		case 1451:
			Pattern pat = Pattern.compile("`(.*?)`");
			Matcher match = pat.matcher(errMsg);
			String tbl = "";
			if (match.find()) {
				match.find();
				tbl = match.group(1);
			}
			return "Cannot " + action
					+ " row because it is connected to table '" + tbl
					+ "'. (Foreign-Key Constraint)";
		}
		return "Error not specified.";
	}

}// end class
