package com.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dao.TblSupplierDao;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.model.TblSupplier;

public class Controller extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private HashMap<String, Object> JSONROOT = new HashMap<String, Object>();

	private TblSupplierDao dao;

	public Controller() {
		dao = new TblSupplierDao();
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		String action = request.getParameter("action");
		List<TblSupplier> supList = new ArrayList<TblSupplier>();
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		response.setContentType("application/json");

		if (action != null) {
			try {
				if (action.equals("list")) {
					// Fetch Data from User Table
			        int startPageIndex = Integer.parseInt(request.getParameter("jtStartIndex"));
			        int recordsPerPage = Integer.parseInt(request.getParameter("jtPageSize"));
					// Fetch Data from Supplier Table
					supList = dao.getAllSuppliers(startPageIndex, recordsPerPage);
			        // Get Total Record Count for Pagination
			        int userCount = dao.getSupplierCount();
					// Return in the format required by jTable plugin
					JSONROOT.put("Result", "OK");
					JSONROOT.put("Records", supList);
					JSONROOT.put("TotalRecordCount", userCount);

					// Convert Java Object to Json
					String jsonArray = gson.toJson(JSONROOT);
					//System.out.println("p: "+request.getParameter("supplierName"));//**
					response.getWriter().print(jsonArray);
				} else if (action.equals("create") || action.equals("update")) {
					TblSupplier supplier = new TblSupplier();
					//System.out.println("p: "+request.getParameter("supplierName"));
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

					if (action.equals("create")) {
						// Create new record
						dao.addSupplier(supplier);
					} else if (action.equals("update")) {
						// Update existing record
						dao.updateSupplier(supplier);
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
						dao.deleteSupplier(name);

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
					supList = dao.getAllSuppliers();
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
}