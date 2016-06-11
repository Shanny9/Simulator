package com.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.daoImpl.TblSupplierDaoImpl;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.model.TblSupplier;

public class DataController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private HashMap<String, Object> JSONROOT = new HashMap<String, Object>();

	private TblSupplierDaoImpl daoSupplier;

	public DataController() {
		daoSupplier = new TblSupplierDaoImpl();
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
				JSONROOT.put("Result", "ERROR");
				JSONROOT.put("Message", ex.getMessage());
				String error = gson.toJson(JSONROOT);
				response.getWriter().print(error);
			}
		}	
  }
  
  
}//end class