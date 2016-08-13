package com.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.daoImpl.TblCIDaoImpl;
import com.daoImpl.TblCMDBDaoImpl;
import com.daoImpl.TblDepartmentDaoImpl;
import com.daoImpl.TblDivisionDaoImpl;
import com.daoImpl.TblEevntDaoImpl;
import com.daoImpl.TblSupplierDaoImpl;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.model.TblCI;
import com.model.TblCMDB;
import com.model.TblCMDBPK;
import com.model.TblDepartment;
import com.model.TblDepartmentPK;
import com.model.TblDivision;
import com.model.TblEvent;
import com.model.TblIncident;
import com.model.TblResource_CI;
import com.model.TblResource_CIPK;
import com.model.TblService;
import com.model.TblSupplier;

public class DataController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private HashMap<String, Object> JSONROOT = new HashMap<String, Object>();

	private TblSupplierDaoImpl daoSupplier;
	private TblCIDaoImpl daoCI;
	private TblCMDBDaoImpl daoCMDB;
	private TblDepartmentDaoImpl daoDepartment;
	private TblDivisionDaoImpl daoDivision;
	private TblEevntDaoImpl daoEvent;

	public DataController() {
		daoSupplier = new TblSupplierDaoImpl();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setCharacterEncoding("UTF-8");
		request.setCharacterEncoding("UTF-8");
		response.setContentType("application/json");

		String action = request.getParameter("action");
		String table = request.getParameter("table");
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		switch (table) {
		case "supplier":
			tblSuppliers(action, request, response, gson);
			break;
		case "ci":
			tblCIs(action, request, response, gson);
			break;
		case "cmdb":
			tblCMDBs(action, request, response, gson);
			break;
		case "department":
			tblDepartments(action, request, response, gson);
			break;
		case "division":
			tblDivisions(action, request, response, gson);
			break;
		case "event":
			tblEvents(action, request, response, gson);
			break;
		}

	}// end doPost

	protected void tblSuppliers(String action, HttpServletRequest request, HttpServletResponse response, Gson gson)
			throws IOException {

		List<TblSupplier> supList = new ArrayList<TblSupplier>();
		if (action != null) {
			try {
				if (action.equals("list")) {
					// Fetch Data from User Table
					int startPageIndex = Integer.parseInt(request.getParameter("jtStartIndex"));
					int recordsPerPage = Integer.parseInt(request.getParameter("jtPageSize"));
					// Fetch Data from Supplier Table
					supList = daoSupplier.getAllSuppliers(startPageIndex, recordsPerPage);
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
						Double cost = Double.parseDouble(request.getParameter("solutionCost"));
						supplier.setSolutionCost(cost);
					}

					if (request.getParameter("isActive") != null) {
						byte isActive = Byte.parseByte(request.getParameter("isActive"));
						supplier.setIsActive(isActive);
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
						daoSupplier.updateSupplier(supplier);
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
				JSONROOT.put("Result", "ERROR");
				JSONROOT.put("Message", ex.getMessage());
				String error = gson.toJson(JSONROOT);
				response.getWriter().print(error);
			}
		}
	}
	
	protected void tblCIs(String action, HttpServletRequest request, HttpServletResponse response, Gson gson)
			throws IOException {

		List<TblCI> ciList = new ArrayList<TblCI>();
		if (action != null) {
			try {
				if (action.equals("list")) {
					// Fetch Data from User Table
					int startPageIndex = Integer.parseInt(request.getParameter("jtStartIndex"));
					int recordsPerPage = Integer.parseInt(request.getParameter("jtPageSize"));
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

					if (request.getParameter("isActive") != null) {
						Boolean isActive = Boolean.parseBoolean(request.getParameter("isActive"));
						ci.setIsActive(isActive);
					}
					// end set fields

					if (action.equals("create")) {
						// Create new record
						daoCI.addCI(ci);
					} else if (action.equals("update")) {
						// Update existing record
						daoCI.updateCI(ci);
					}

					// Return in the format required by jTable plugin
					JSONROOT.put("Result", "OK");
					JSONROOT.put("Record", ci);

					// Convert Java Object to Json
					String jsonArray = gson.toJson(JSONROOT);
					response.getWriter().print(jsonArray);
				} else if (action.equals("delete")) {
					// Delete record
					if (request.getParameter("CiId") != null) {
						Byte CiId = Byte.parseByte(request.getParameter("CiId"));
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
				JSONROOT.put("Result", "ERROR");
				JSONROOT.put("Message", ex.getMessage());
				String error = gson.toJson(JSONROOT);
				response.getWriter().print(error);
			}
		}
	}
	
	protected void tblCMDBs(String action, HttpServletRequest request, HttpServletResponse response, Gson gson)
			throws IOException {

		List<TblCMDB> cmdbList = new ArrayList<TblCMDB>();
		if (action != null) {
			try {
				TblCMDBPK pk = new TblCMDBPK();
				if (action.equals("list")) {
					// Fetch Data from User Table
					int startPageIndex = Integer.parseInt(request.getParameter("jtStartIndex"));
					int recordsPerPage = Integer.parseInt(request.getParameter("jtPageSize"));
					// Fetch Data from Supplier Table
					cmdbList = daoCMDB.getAllCMDBs(startPageIndex, recordsPerPage);
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
						Byte ciId = Byte.parseByte(request.getParameter("ciId"));
						pk.setCiId(ciId);
					}

					if (request.getParameter("serviceId") != null) {
						Byte serviceId = Byte.parseByte(request.getParameter("serviceId"));
						pk.setServiceId(serviceId);
						cmdb.setId(pk);
					}

					if (request.getParameter("isActive") != null) {
						Boolean isActive = Boolean.parseBoolean(request.getParameter("isActive"));
						cmdb.setIsActive(isActive);
					}
					// end set fields

					if (action.equals("create")) {
						// Create new record
						daoCMDB.addCMDB(cmdb);
					} else if (action.equals("update")) {
						// Update existing record
						daoCMDB.updateCMDB(cmdb);
					}

					// Return in the format required by jTable plugin
					JSONROOT.put("Result", "OK");
					JSONROOT.put("Record", cmdb);

					// Convert Java Object to Json
					String jsonArray = gson.toJson(JSONROOT);
					response.getWriter().print(jsonArray);
				} else if (action.equals("delete")) {
					// Delete record
					if (request.getParameter("ciId") != null && request.getParameter("serviceId") != null) {
						Byte ciId = Byte.parseByte(request.getParameter("ciId"));
						Byte serviceId = Byte.parseByte(request.getParameter("serviceId"));
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
				JSONROOT.put("Result", "ERROR");
				JSONROOT.put("Message", ex.getMessage());
				String error = gson.toJson(JSONROOT);
				response.getWriter().print(error);
			}
		}
	}
	
	protected void tblDepartments(String action, HttpServletRequest request, HttpServletResponse response, Gson gson)
			throws IOException {

		List<TblDepartment> depList = new ArrayList<TblDepartment>();
		if (action != null) {
			try {
				TblDepartmentPK pk = new TblDepartmentPK();
				if (action.equals("list")) {
					// Fetch Data from User Table
					int startPageIndex = Integer.parseInt(request.getParameter("jtStartIndex"));
					int recordsPerPage = Integer.parseInt(request.getParameter("jtPageSize"));
					// Fetch Data from Supplier Table
					depList = daoDepartment.getAllDepartments(startPageIndex, recordsPerPage);
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
						String departmentName = request.getParameter("departmentName");
						pk.setDepartmentName(departmentName);
					}

					if (request.getParameter("divisionName") != null) {
						String divisionName = request.getParameter("divisionName");
						pk.setDevisionName(divisionName);
						department.setId(pk);
					}

					if (request.getParameter("isActive") != null) {
						Boolean isActive = Boolean.parseBoolean(request.getParameter("isActive"));
						department.setIsActive(isActive);
					}
					// end set fields

					if (action.equals("create")) {
						// Create new record
						daoDepartment.addDepartment(department);
					} else if (action.equals("update")) {
						// Update existing record
						daoDepartment.updateDepartment(department);
					}

					// Return in the format required by jTable plugin
					JSONROOT.put("Result", "OK");
					JSONROOT.put("Record", department);

					// Convert Java Object to Json
					String jsonArray = gson.toJson(JSONROOT);
					response.getWriter().print(jsonArray);
				} else if (action.equals("delete")) {
					// Delete record
					if (request.getParameter("supplierName") != null && request.getParameter("divisionName") != null) {
						String departmentName = request.getParameter("departmentName");
						String divisionName = request.getParameter("divisionName");
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
				JSONROOT.put("Result", "ERROR");
				JSONROOT.put("Message", ex.getMessage());
				String error = gson.toJson(JSONROOT);
				response.getWriter().print(error);
			}
		}
	}
	
	protected void tblDivisions(String action, HttpServletRequest request, HttpServletResponse response, Gson gson)
			throws IOException {

		List<TblDivision> divisionList = new ArrayList<TblDivision>();
		if (action != null) {
			try {
				if (action.equals("list")) {
					// Fetch Data from User Table
					int startPageIndex = Integer.parseInt(request.getParameter("jtStartIndex"));
					int recordsPerPage = Integer.parseInt(request.getParameter("jtPageSize"));
					// Fetch Data from Supplier Table
					divisionList = daoDivision.getAllDivisions(startPageIndex, recordsPerPage);
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
						String divisionName = request.getParameter("divisionName");
						division.setDivisionName(divisionName);

					}

					if (request.getParameter("isActive") != null) {
						Boolean isActive = Boolean.parseBoolean(request.getParameter("isActive"));
						division.setIsActive(isActive);
					}

					// end set fields

					if (action.equals("create")) {
						// Create new record
						daoDivision.addDivision(division);
					} else if (action.equals("update")) {
						// Update existing record
						daoDivision.updateDivision(division);
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
						String divisionName = request.getParameter("divisionName");
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
				JSONROOT.put("Result", "ERROR");
				JSONROOT.put("Message", ex.getMessage());
				String error = gson.toJson(JSONROOT);
				response.getWriter().print(error);
			}
		}
	}
	
	protected void tblEvents(String action, HttpServletRequest request, HttpServletResponse response, Gson gson)
			throws IOException {

		List<TblEvent> eventList = new ArrayList<TblEvent>();
		if (action != null) {
			try {
				if (action.equals("list")) {
					// Fetch Data from User Table
					int startPageIndex = Integer.parseInt(request.getParameter("jtStartIndex"));
					int recordsPerPage = Integer.parseInt(request.getParameter("jtPageSize"));
					// Fetch Data from Supplier Table
					eventList = daoEvent.getAllEvents(startPageIndex, recordsPerPage);
					// Get Total Record Count for Pagination
					int userCount = daoEvent.getEventCount();
					// Return in the format required by jTable plugin
					JSONROOT.put("Result", "OK");
					JSONROOT.put("Records", eventList);
					JSONROOT.put("TotalRecordCount", userCount);

					// Convert Java Object to Json
					String jsonArray = gson.toJson(JSONROOT);
					response.getWriter().print(jsonArray);
				} else if (action.equals("create") || action.equals("update")) {
					TblEvent event = new TblEvent();

					// Set fields
					if (request.getParameter("eventId") != null) {
						Byte eventId = Byte.parseByte(request.getParameter("eventId"));
						event.setEventId(eventId);

					}

					if (request.getParameter("incidentId") != null) {
						Byte incidentId = Byte.parseByte(request.getParameter("incidentId"));
						TblIncident incident = new TblIncident();
						incident.setIncidentId(incidentId);
						event.setTblIncident(incident);
					}
					
					if (request.getParameter("serviceId") != null) {
						Byte serviceId = Byte.parseByte(request.getParameter("serviceId"));
						TblService service = new TblService();
						service.setServiceId(serviceId);
						event.setTblService(service);
					}

					if (request.getParameter("isActive") != null) {
						Boolean isActive = Boolean.parseBoolean(request.getParameter("isActive"));
						event.setIsActive(isActive);
					}
					// end set fields

					if (action.equals("create")) {
						// Create new record
						daoEvent.addEvent(event);
					} else if (action.equals("update")) {
						// Update existing record
						daoEvent.updateEvent(event);
					}

					// Return in the format required by jTable plugin
					JSONROOT.put("Result", "OK");
					JSONROOT.put("Record", event);

					// Convert Java Object to Json
					String jsonArray = gson.toJson(JSONROOT);
					response.getWriter().print(jsonArray);
				} else if (action.equals("delete")) {
					// Delete record
					if (request.getParameter("eventId") != null) {
						Byte eventId = Byte.parseByte(request.getParameter("eventId"));
						daoEvent.deleteEvent(eventId);

						// Return in the format required by jTable plugin
						JSONROOT.put("Result", "OK");

						// Convert Java Object to Json
						String jsonArray = gson.toJson(JSONROOT);
						response.getWriter().print(jsonArray);
					}
				} else if (action.equals("excel")) {
					// Export to excel
					eventList = daoEvent.getAllEvents();
					// Return in the format required by jTable plugin
					JSONROOT.clear();
					JSONROOT.put("Records", eventList);
					// Convert Java Object to Json
					String jsonArray = gson.toJson(JSONROOT);
					response.getWriter().print(jsonArray);
				}
			} catch (Exception ex) {
				JSONROOT.put("Result", "ERROR");
				JSONROOT.put("Message", ex.getMessage());
				String error = gson.toJson(JSONROOT);
				response.getWriter().print(error);
			}
		}
	}

}// end class