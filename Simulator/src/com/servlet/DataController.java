package com.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.daoImpl.TblIncidentDaoImpl;
import com.daoImpl.TblPriorityCostDaoImpl;
import com.daoImpl.TblServiceDaoImpl;
import com.daoImpl.TblServiceDepartmentDaoImpl;
import com.daoImpl.TblServiceDivisionDaoImpl;
import com.daoImpl.TblSolutionDaoImpl;
import com.daoImpl.TblSupplierDaoImpl;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.model.TblIncident;
import com.model.TblPriority_Cost;
import com.model.TblService;
import com.model.TblService_Department;
import com.model.TblService_DepartmentPK;
import com.model.TblService_Division;
import com.model.TblService_DivisionPK;
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
	private TblServiceDivisionDaoImpl daoServiceDivision;

	public DataController() {
		daoSupplier = new TblSupplierDaoImpl();
		daoSolution = new TblSolutionDaoImpl();
		daoIncident = new TblIncidentDaoImpl();
		daoPriorityCost = new TblPriorityCostDaoImpl();
		daoService = new TblServiceDaoImpl();
		daoServiceDepartment = new TblServiceDepartmentDaoImpl();
		daoServiceDivision = new TblServiceDivisionDaoImpl();
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

	    response.setCharacterEncoding("UTF-8");
		request.setCharacterEncoding("UTF-8");
		response.setContentType("application/json");
		
		String action = request.getParameter("action");
		String table = request.getParameter("table");
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		switch(table){
			case "supplier":
				tblSuppliers(action, request, response, gson);
				break;
			case "solution":
				tblSolution(action, request, response, gson);
				break;
			case "incident":
				tblIncident(action, request, response, gson);
				break;
			case "priority":
				tblPriorityCost(action, request, response, gson);
				break;
			case "service":
				tblService(action, request, response, gson);
				break;
			case "serviceDepartment":
				tblServiceDepartment(action, request, response, gson);
				break;
			case "serviceDivision":
				tblServiceDivision(action, request, response, gson);
				break;
				
		}
		
	}//end doPost
	
  protected void tblSuppliers(String action, HttpServletRequest request,
		  HttpServletResponse response, Gson gson) throws IOException{
	  
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
					
					//Set fields
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
					//end set fields
					
					if (action.equals("create")) {
						// Create new record
						daoSupplier.addSupplier(supplier);
					} else if (action.equals("update")) {
						// Update existing record
						System.out.println(request.getParameterMap().get("jtRecordKey").toString());
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
						String name =request.getParameter("supplierName");
						daoSupplier.deleteSupplier(name);

						// Return in the format required by jTable plugin
						JSONROOT.put("Result", "OK");

						// Convert Java Object to Json
						String jsonArray = gson.toJson(JSONROOT);
						response.getWriter().print(jsonArray);
					}
				}
				else if (action.equals("excel"))
				{
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
				System.out.println("Data-tblSupplier: " + ex.getMessage());
				JSONROOT.put("Result", "ERROR");
				JSONROOT.put("Message", ex.getMessage());
				String error = gson.toJson(JSONROOT);
				response.getWriter().print(error);
			}
		}	
  }
  
  protected void tblSolution(String action, HttpServletRequest request,
		  HttpServletResponse response, Gson gson) throws IOException{
	  
	  List<TblSolution> solList = new ArrayList<TblSolution>();
		if (action != null) {
			try {
				if (action.equals("list")) {
					// Fetch Data from User Table
			        int startPageIndex = Integer.parseInt(request.getParameter("jtStartIndex"));
			        int recordsPerPage = Integer.parseInt(request.getParameter("jtPageSize"));
					// Fetch Data from Supplier Table
					solList = daoSolution.getAllSolutions(startPageIndex, recordsPerPage);
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
					
					//Set fields
					if (request.getParameter("solutionId") != null) {
						int solId = Integer.parseInt(request.getParameter("solutionId"));
						sol.setSolutionId(solId);
						
					}

					if (request.getParameter("solutionMarom") != null) {
						int marom = Integer.parseInt(request.getParameter("solutionMarom"));
						sol.setSolutionMarom(marom);
					}

					if (request.getParameter("solutionRakia") != null) {
						int rakia = Integer.parseInt(request.getParameter("solutionRakia"));
						sol.setSolutionRakia(rakia);
					}
					
					if (request.getParameter("isActive") != null) {
						boolean active = Boolean.parseBoolean(request.getParameter("isActive"));
						sol.setActive(active);
					}
					//end set fields
					
					if (action.equals("create")) {
						// Create new record
						daoSolution.addSolution(sol);
					} else if (action.equals("update")) {
						// Update existing record
						int id = Integer.parseInt(request.getParameter("jtRecordKey_solutionId"));
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
						int solId = Integer.parseInt(request.getParameter("solutionId"));
						daoSolution.deleteSolution(solId);

						// Return in the format required by jTable plugin
						JSONROOT.put("Result", "OK");

						// Convert Java Object to Json
						String jsonArray = gson.toJson(JSONROOT);
						response.getWriter().print(jsonArray);
					}
				}
				else if (action.equals("excel"))
				{
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
				System.out.println("Data-tblSolution: " + ex.getMessage());
				JSONROOT.put("Result", "ERROR");
				JSONROOT.put("Message", ex.getMessage());
				String error = gson.toJson(JSONROOT);
				response.getWriter().print(error);
			}
		}	
  }
  
  protected void tblIncident(String action, HttpServletRequest request,
		  HttpServletResponse response, Gson gson) throws IOException{
	  
	  List<TblIncident> inciList = new ArrayList<TblIncident>();
		if (action != null) {
			try {
				if (action.equals("list")) {
					// Fetch Data from User Table
			        int startPageIndex = Integer.parseInt(request.getParameter("jtStartIndex"));
			        int recordsPerPage = Integer.parseInt(request.getParameter("jtPageSize"));
					// Fetch Data from Supplier Table
					inciList = daoIncident.getAllIncidents(startPageIndex, recordsPerPage);
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
					TblIncident inci = new TblIncident();

					//Set fields
					if (request.getParameter("incidentId") != null) {
						byte inciId = Byte.parseByte(request.getParameter("incidentId"));
						inci.setIncidentId(inciId);
						
					}

					if (request.getParameter("ciId") != null) {
						byte ciId = Byte.parseByte(request.getParameter("ciId"));
						inci.setCiId(ciId);
					}

					if (request.getParameter("incidentTime") != null) {
						int inciTime = Integer.parseInt(request.getParameter("incidentTime"));
						inci.setIncidentTime(inciTime);
					}
					
					if (request.getParameter("isActive") != null) {
						boolean active = Boolean.parseBoolean(request.getParameter("isActive"));
						inci.setIsActive(active);
					}
					
					if (request.getParameter("solutionId") != null) {
						int solId = Integer.parseInt(request.getParameter("solutionId"));
						inci.setSolutionId(solId);
					}
					//end set fields
					
					if (action.equals("create")) {
						// Create new record
						daoIncident.addIncident(inci);
					} else if (action.equals("update")) {
						// Update existing record
						byte id = Byte.parseByte(request.getParameter("jtRecordKey_incidentId"));
						daoIncident.updateIncident(inci, id);
					}

					// Return in the format required by jTable plugin
					JSONROOT.put("Result", "OK");
					JSONROOT.put("Record", inci);

					// Convert Java Object to Json
					String jsonArray = gson.toJson(JSONROOT);
					response.getWriter().print(jsonArray);
				} else if (action.equals("delete")) {
					// Delete record
					if (request.getParameter("incidentId") != null) {
						byte inciId = Byte.parseByte(request.getParameter("incidentId"));
						daoIncident.deleteIncident(inciId);

						// Return in the format required by jTable plugin
						JSONROOT.put("Result", "OK");

						// Convert Java Object to Json
						String jsonArray = gson.toJson(JSONROOT);
						response.getWriter().print(jsonArray);
					}
				}
				else if (action.equals("excel"))
				{
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
				System.out.println("Data-tblIncident: " + ex.getMessage());
				JSONROOT.put("Result", "ERROR");
				JSONROOT.put("Message", ex.getMessage());
				String error = gson.toJson(JSONROOT);
				response.getWriter().print(error);
			}
		}	
  }
  
  protected void tblPriorityCost(String action, HttpServletRequest request,
		  HttpServletResponse response, Gson gson) throws IOException{
	  
	  List<TblPriority_Cost> pcList = new ArrayList<TblPriority_Cost>();
		if (action != null) {
			try {
				if (action.equals("list")) {
					// Fetch Data from User Table
			        int startPageIndex = Integer.parseInt(request.getParameter("jtStartIndex"));
			        int recordsPerPage = Integer.parseInt(request.getParameter("jtPageSize"));
					// Fetch Data from Supplier Table
					pcList = daoPriorityCost.getAllPriorityCost(startPageIndex, recordsPerPage);
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
					
					//Set fields
					if (request.getParameter("pName") != null) {
						String pName = request.getParameter("pName");
						pc.setPName(pName);
						
					}

					if (request.getParameter("pCost") != null) {
						double pCost = Double.parseDouble(request.getParameter("pCost"));
						pc.setPCost(pCost);
					}

					if (request.getParameter("isActive") != null) {
						boolean active = Boolean.parseBoolean(request.getParameter("isActive"));
						pc.setIsActive(active);
					}
					
					//end set fields
					
					if (action.equals("create")) {
						// Create new record
						daoPriorityCost.addPriorityCost(pc);
					} else if (action.equals("update")) {
						// Update existing record
						String pName = request.getParameter("jtRecordKey_pName");
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
				}
				else if (action.equals("excel"))
				{
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
				System.out.println("Data-tblPriorityCost: " + ex.getMessage());
				JSONROOT.put("Result", "ERROR");
				JSONROOT.put("Message", ex.getMessage());
				String error = gson.toJson(JSONROOT);
				response.getWriter().print(error);
			}
		}	
  }
  
  protected void tblService(String action, HttpServletRequest request,
		  HttpServletResponse response, Gson gson) throws IOException{
	  
	  List<TblService> serviceList = new ArrayList<TblService>();
		if (action != null) {
			try {
				if (action.equals("list")) {
					// Fetch Data from User Table
			        int startPageIndex = Integer.parseInt(request.getParameter("jtStartIndex"));
			        int recordsPerPage = Integer.parseInt(request.getParameter("jtPageSize"));
					// Fetch Data from Supplier Table
					serviceList = daoService.getAllServices(startPageIndex, recordsPerPage);
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
					
					//Set fields
					if (request.getParameter("serviceId") != null) {
						byte sId = Byte.parseByte(request.getParameter("serviceId"));
						ser.setServiceId(sId);
					}

					if (request.getParameter("fixedCost") != null) {
						double fixedC = Double.parseDouble(request.getParameter("fixedCost"));
						ser.setFixedCost(fixedC);
					}
					
					if (request.getParameter("fixedIncome") != null) {
						double fixedI = Double.parseDouble(request.getParameter("fixedIncome"));
						ser.setFixedIncome(fixedI);
					}

					if (request.getParameter("isActive") != null) {
						boolean active = Boolean.parseBoolean(request.getParameter("isActive"));
						ser.setIsActive(active);
					}
					
					if (request.getParameter("isTechnical") != null) {
						byte tech = Byte.parseByte(request.getParameter("isTechnical"));
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
					
					if (request.getParameter("supplierLevel2") != null) {
						String level2 = request.getParameter("supplierLevel2");
						ser.setSupplierLevel2(level2);
					}
					
					if (request.getParameter("supplierLevel3") != null) {
						String level3 = request.getParameter("supplierLevel3");
						ser.setSupplierLevel3(level3);
					}
					
					if (request.getParameter("urgency") != null) {
						String urgency = request.getParameter("urgency");
						ser.setUrgency(urgency);
					}
					
					if (request.getParameter("impact") != null) {
						String im = request.getParameter("impact");
						ser.setImpact(im);
					}
					
					//end set fields
					
					if (action.equals("create")) {
						// Create new record
						daoService.addService(ser);
					} else if (action.equals("update")) {
						// Update existing record
						byte id = Byte.parseByte(request.getParameter("jtRecordKey_serviceId"));
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
						byte sId = Byte.parseByte(request.getParameter("serviceId"));
						daoService.deleteService(sId);

						// Return in the format required by jTable plugin
						JSONROOT.put("Result", "OK");

						// Convert Java Object to Json
						String jsonArray = gson.toJson(JSONROOT);
						response.getWriter().print(jsonArray);
					}
				}
				else if (action.equals("excel"))
				{
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
				System.out.println("Data-tblService: " + ex.getMessage());
				JSONROOT.put("Result", "ERROR");
				JSONROOT.put("Message", ex.getMessage());
				String error = gson.toJson(JSONROOT);
				response.getWriter().print(error);
			}
		}	
  }
  
  protected void tblServiceDepartment(String action, HttpServletRequest request,
		  HttpServletResponse response, Gson gson) throws IOException{
	  
	  List<TblService_Department> serDepList = new ArrayList<TblService_Department>();
		if (action != null) {
			try {
				if (action.equals("list")) {
					// Fetch Data from User Table
			        int startPageIndex = Integer.parseInt(request.getParameter("jtStartIndex"));
			        int recordsPerPage = Integer.parseInt(request.getParameter("jtPageSize"));
					// Fetch Data from Supplier Table
			        serDepList = daoServiceDepartment.getAllServiceDepartments(startPageIndex, recordsPerPage);
			        // Get Total Record Count for Pagination
			        int userCount = daoServiceDepartment.getServiceDepartmentCount();
					// Return in the format required by jTable plugin
					JSONROOT.put("Result", "OK");
					JSONROOT.put("Records", serDepList);
					JSONROOT.put("TotalRecordCount", userCount);

					// Convert Java Object to Json
					String jsonArray = gson.toJson(JSONROOT);
					response.getWriter().print(jsonArray);
				} else if (action.equals("create") || action.equals("update")) {
					TblService_Department ser = new TblService_Department();
					
					//Set fields
					if (request.getParameter("service_ID") != null) {
						byte serId = Byte.parseByte(request.getParameter("service_ID"));
						ser.setService_ID(serId);
						
					}

					if (request.getParameter("departmentName") != null) {
						String departmentName = request.getParameter("departmentName");
						ser.setDepartmentName(departmentName);
					}
					
					if (request.getParameter("divisionName") != null) {
						String divisionName = request.getParameter("divisionName");
						ser.setDivisionName(divisionName);
					}
					
					if (request.getParameter("isActive") != null) {
						boolean active = Boolean.parseBoolean(request.getParameter("isActive"));
						ser.setIsActive(active);
					}

					//end set fields
					
					if (action.equals("create")) {
						// Create new record
						daoServiceDepartment.addServiceDepartment(ser);
					} else if (action.equals("update")) {
						// Update existing record
						byte id = Byte.parseByte(request.getParameter("jtRecordKey_service_ID"));
						String depName = request.getParameter("jtRecordKey_departmentName");
						String divName = request.getParameter("jtRecordKey_divisionName");
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
						byte serId = Byte.parseByte(request.getParameter("service_ID"));
						if (request.getParameter("departmentName") != null) {
							String departmentName = request.getParameter("departmentName");
							if (request.getParameter("divisionName") != null) {
								String divisionName = request.getParameter("divisionName");
								TblService_DepartmentPK pk = new TblService_DepartmentPK();
								pk.setService_ID(serId);
								pk.setDepartmentName(departmentName);
								pk.setDivisionName(divisionName);
								
								daoServiceDepartment.deleteServiceDepartment(pk);

								// Return in the format required by jTable plugin
								JSONROOT.put("Result", "OK");

								// Convert Java Object to Json
								String jsonArray = gson.toJson(JSONROOT);
								response.getWriter().print(jsonArray);
								}
							}
						}
					
				}
				else if (action.equals("excel"))
				{
					// Export to excel
					serDepList = daoServiceDepartment.getAllServiceDepartments();
					// Return in the format required by jTable plugin
					JSONROOT.clear();
					JSONROOT.put("Records", serDepList);
					// Convert Java Object to Json
					String jsonArray = gson.toJson(JSONROOT);
					response.getWriter().print(jsonArray);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				System.out.println("Data-tblServiceDep: " + ex.getMessage());
				JSONROOT.put("Result", "ERROR");
				JSONROOT.put("Message", ex.getMessage());
				String error = gson.toJson(JSONROOT);
				response.getWriter().print(error);
			}
		}	
  }
  
  protected void tblServiceDivision(String action, HttpServletRequest request,
		  HttpServletResponse response, Gson gson) throws IOException{
	  
	  List<TblService_Division> serDivList = new ArrayList<TblService_Division>();
		if (action != null) {
			try {
				if (action.equals("list")) {
					// Fetch Data from User Table
			        int startPageIndex = Integer.parseInt(request.getParameter("jtStartIndex"));
			        int recordsPerPage = Integer.parseInt(request.getParameter("jtPageSize"));
					// Fetch Data from Supplier Table
			        serDivList = daoServiceDivision.getAllServiceDivisions(startPageIndex, recordsPerPage);
			        // Get Total Record Count for Pagination
			        int userCount = daoServiceDivision.getServiceDivisionCount();
					// Return in the format required by jTable plugin
					JSONROOT.put("Result", "OK");
					JSONROOT.put("Records", serDivList);
					JSONROOT.put("TotalRecordCount", userCount);

					// Convert Java Object to Json
					String jsonArray = gson.toJson(JSONROOT);
					response.getWriter().print(jsonArray);
				} else if (action.equals("create") || action.equals("update")) {
					TblService_Division ser = new TblService_Division();
					
					//Set fields
					if (request.getParameter("service_ID") != null) {
						byte serId = Byte.parseByte(request.getParameter("service_ID"));
						ser.setService_ID(serId);
						
					}
					
					if (request.getParameter("divisionName") != null) {
						String divisionName = request.getParameter("divisionName");
						ser.setDivisionName(divisionName);
					}
					
					if (request.getParameter("isActive") != null) {
						boolean active = Boolean.parseBoolean(request.getParameter("isActive"));
						ser.setIsActive(active);
					}

					//end set fields
					
					if (action.equals("create")) {
						// Create new record
						daoServiceDivision.addServiceDivision(ser);
					} else if (action.equals("update")) {
						// Update existing record
						byte id = Byte.parseByte(request.getParameter("jtRecordKey_service_ID"));
						String divName = request.getParameter("jtRecordKey_divisionName");
						TblService_DivisionPK pk = new TblService_DivisionPK();
						pk.setService_ID(id);
						pk.setDivisionName(divName);
						daoServiceDivision.updateServiceDivision(ser, pk);
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
						byte serId = Byte.parseByte(request.getParameter("service_ID"));
							if (request.getParameter("divisionName") != null) {
								String divisionName = request.getParameter("divisionName");
								TblService_DivisionPK pk = new TblService_DivisionPK();
								pk.setService_ID(serId);
								pk.setDivisionName(divisionName);
								
								daoServiceDivision.deleteServiceDivision(pk);

								// Return in the format required by jTable plugin
								JSONROOT.put("Result", "OK");

								// Convert Java Object to Json
								String jsonArray = gson.toJson(JSONROOT);
								response.getWriter().print(jsonArray);
								}
						}
					
				}
				else if (action.equals("excel"))
				{
					// Export to excel
					serDivList = daoServiceDivision.getAllServiceDivisions();
					// Return in the format required by jTable plugin
					JSONROOT.clear();
					JSONROOT.put("Records", serDivList);
					// Convert Java Object to Json
					String jsonArray = gson.toJson(JSONROOT);
					response.getWriter().print(jsonArray);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				System.out.println("Data-tblServiceDiv: " + ex.getMessage());
				JSONROOT.put("Result", "ERROR");
				JSONROOT.put("Message", ex.getMessage());
				String error = gson.toJson(JSONROOT);
				response.getWriter().print(error);
			}
		}	
  }
}//end class